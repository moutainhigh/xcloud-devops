/*
 * Copyright 2015 the original author or authors.
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
package com.wl4g.devops.shell.runner;

import static org.apache.commons.lang3.StringUtils.*;

import com.wl4g.devops.shell.config.Configuration;

import org.jline.reader.UserInterruptException;

/**
 * Interactive shell component runner
 * 
 * @author Wangl.sir <983708408@qq.com>
 * @version v1.0 2019年4月14日
 * @since
 */
public class InteractiveRunner extends AbstractRunner {

	public InteractiveRunner(Configuration config) {
		super(config);
	}

	@Override
	public void run(String[] args) {
		// Listening console input.
		while (true) {
			String line = null;
			try {
				// Read line
				line = lineReader.readLine(getAttributed().toAnsi(lineReader.getTerminal()));

				// Submission processing
				if (isNotBlank(line)) {
					submit(line);
				}
			} catch (UserInterruptException e) {
				shutdown(line);
			} catch (Throwable e) {
				printErr(EMPTY, e);
			} finally {
				try {
					Thread.sleep(200L);
				} catch (InterruptedException e) {
					printErr(EMPTY, e);
				}
			}
		}
	}

}
