/*
 * Copyright 2017 ~ 2025 the original author or authors. <wanglsir@gmail.com, 983708408@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wl4g.devops.iam.captcha.jigsaw;

import com.wl4g.devops.common.utils.serialize.JacksonUtils;
import com.wl4g.devops.iam.captcha.config.CaptchaProperties;
import com.wl4g.devops.support.cache.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.Assert;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static com.wl4g.devops.common.constants.IAMDevOpsConstants.JIGSAW_REDIS_CACHE_KEY;
import static io.netty.util.internal.ThreadLocalRandom.current;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Jigsaw image manager.
 * 
 * @author Wangl.sir <wanglsir@gmail.com, 983708408@qq.com>
 * @version v1.0 2019-09-02
 * @since
 */
public class JigsawImageManager implements ApplicationRunner, Serializable {
	private static final long serialVersionUID = -4133013721883654349L;

	/**
	 * Default jigsaw source image path.
	 */
	final public static String DEFAULT_JIGSAW_SOURCE_CLASSPATH = "classpath:static/jigsaw/*.*";

	final protected Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * CAPTCHA configuration properties.
	 */
	final protected CaptchaProperties config;

	private static int poolSize;


	@Autowired
	private JedisService jedisService;

	/**
	 * Jigsaw image cache pool.
	 */
	//final protected Map<Integer, JigsawImgCode> cachePool = new ConcurrentHashMap<>();



	public JigsawImageManager(CaptchaProperties config) {
		Assert.notNull(config, "Captcha properties must not be null.");
		this.config = config;
	}

	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		initJigsawImagePool();
	}

	/**
	 * Clear cache.
	 */
	//TODO
	public void clearCache() {
		if (log.isInfoEnabled()) {
			//log.info("Clear jigsaw image pool for {} ...", cachePool.size());
		}
		//this.cachePool.clear();
	}

	/**
	 * Get random borrow jigsaw image code.
	 * 
	 * @return
	 */
	public JigsawImgCode borrow() {
		String s = jedisService.get(JIGSAW_REDIS_CACHE_KEY + current().nextInt(config.getJigsaw().getPoolSize()));
		Assert.hasText(s,"Unable to borrow jigsaw image resource.");
		return JacksonUtils.parseJSON(s,JigsawImgCode.class);
	}

	/**
	 * Initialize jigsaw image buffer cache.
	 * 
	 * @throws Exception
	 */
	private void initJigsawImagePool() throws IOException {
		if (log.isInfoEnabled()) {
			log.info("Initializing jigsaw image buffer pool...");
		}

		if (isBlank(config.getJigsaw().getSourceDir())) {
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			Resource[] resources = resolver.getResources(DEFAULT_JIGSAW_SOURCE_CLASSPATH);
			// Loading
			doLoadBufferImage(resources);
		} else {
			File srcDir = new File(config.getJigsaw().getSourceDir());
			Assert.state((srcDir.canRead() && srcDir.exists()),
					String.format(
							"Failed to initialize jigsaw images, please check the path: %s is correct and has read permission",
							srcDir.getAbsolutePath()));
			// Read files.
			File[] files = srcDir.listFiles(new HideFileFilter());
			Assert.state((files != null && files.length > 0),
					String.format("Failed to initialize jigsaw images, path: %s material is empty", srcDir.getAbsolutePath()));
			// Loading
			doLoadBufferImage(files);
		}

	}

	/**
	 * Filter , filter hide file which start with "."
	 */
	public class HideFileFilter implements FileFilter {
		public boolean accept(File pathname) {
			return !pathname.getName().startsWith(".");
		}
	}


	/**
	 * Do load buffer image.
	 * 
	 * @param sources
	 * @throws IOException
	 */
	private void doLoadBufferImage(Object[] sources) throws IOException {
		// Statistic use material.
		Set<Integer> indexs = new HashSet<>();

		// Initialize jigsaw images.
		ImageTailor tailor = new ImageTailor();
		for (int i = 0; i < config.getJigsaw().getPoolSize(); i++) {

			int index = i;
			if (index >= sources.length) { // Inadequate material, random reuse.
				index = current().nextInt(sources.length);
			}
			indexs.add(index); // Statistic

			// Generate image.
			Object source = sources[index];
			if (log.isDebugEnabled()) {
				log.debug("Generate jigsaw image from material: {}", source);
			}

			if (source instanceof File) {
				String path = ((File) sources[index]).getAbsolutePath();
				jedisService.set(JIGSAW_REDIS_CACHE_KEY+i, JacksonUtils.toJSONString(tailor.getJigsawImageFile(path)),0);
				//this.cachePool.put(i, tailor.getJigsawImageFile(path));
			} else if (source instanceof Resource) {
				Resource resource = (Resource) source;
				jedisService.set(JIGSAW_REDIS_CACHE_KEY+i, JacksonUtils.toJSONString(tailor.getJigsawImageInputStream(resource.getInputStream())),0);
				//this.cachePool.put(i, tailor.getJigsawImageInputStream(resource.getInputStream()));
			} else {
				throw new IllegalStateException(String.format("Unsupported jigsaw image source: %s", source));
			}
		}

		if (log.isInfoEnabled()) {
			log.info("Initialized jigsaw images buffer total: {}, expend material: {}", config.getJigsaw().getPoolSize(),
					indexs.size());
		}
	}

}