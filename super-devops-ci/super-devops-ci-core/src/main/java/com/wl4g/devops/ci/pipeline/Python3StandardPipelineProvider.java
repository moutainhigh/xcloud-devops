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
package com.wl4g.devops.ci.pipeline;

import java.io.File;

import com.wl4g.devops.ci.core.context.PipelineContext;
import com.wl4g.devops.ci.pipeline.deploy.Python3StandardPipeDeployer;
import com.wl4g.devops.common.bean.share.AppInstance;

/**
 * Python3 standard deployments provider.
 *
 * @author Wangl.sir <983708408@qq.com>
 * @version v1.0 2019年5月22日
 * @since
 */
public class Python3StandardPipelineProvider extends BasedDiskBackupPipelineProvider {

	public Python3StandardPipelineProvider(PipelineContext info) {
		super(info);
	}

	@Override
	public void execute() throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public void rollback() throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	protected Runnable newDeployer(AppInstance instance) {
		Object[] args = { this, instance, getContext().getTaskHistoryDetails() };
		return beanFactory.getBean(Python3StandardPipeDeployer.class, args);
	}

	@Override
	protected void doBuildWithDefaultCommands(String projectDir, File logPath, Integer taskId) throws Exception {
		// Dynamic interpretation language, no execution to build command.
	}

}