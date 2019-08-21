/*
 * Copyright 2017 ~ 2025 the original author or authors.
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
package com.wl4g.devops.iam.common.attacks.csrf;

import java.io.IOException;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.DefaultCorsProcessor;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS(CSRF attack) resolve filter
 * 
 * @author Wangl.sir <983708408@qq.com>
 * @version v1.0 2019年4月25日
 * @since
 */
public class CorsResolveSecurityFilter extends CorsFilter {

	public CorsResolveSecurityFilter(CorsConfigurationSource configSource) {
		super(configSource);
	}

	/**
	 * Advanced matches CORS processor.
	 * 
	 * @author Wangl.sir
	 * @version v1.0 2019年8月21日
	 * @since
	 */
	public static class AdvancedMatchesCorsProcessor extends DefaultCorsProcessor {

		@Override
		protected boolean handleInternal(ServerHttpRequest request, ServerHttpResponse response, CorsConfiguration config,
				boolean preFlightRequest) throws IOException {
			/*
			 * TODO Custom advanced logic CORS processing ...
			 */
			return super.handleInternal(request, response, config, preFlightRequest);
		}

	}

}