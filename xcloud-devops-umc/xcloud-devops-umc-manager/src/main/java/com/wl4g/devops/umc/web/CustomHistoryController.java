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
package com.wl4g.devops.umc.web;

import com.wl4g.components.core.bean.umc.CustomHistory;
import com.wl4g.components.core.web.BaseController;
import com.wl4g.components.core.web.RespBase;
import com.wl4g.devops.page.PageModel;
import com.wl4g.devops.umc.service.CustomHistoryService;

import static com.wl4g.components.common.lang.Assert2.notNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vjay
 * @date 2019-08-05 11:44:00
 */
@RestController
@RequestMapping("/history")
public class CustomHistoryController extends BaseController {


	@Autowired
	private CustomHistoryService customHistoryService;

	@RequestMapping(value = "/list")
	public RespBase<?> list(String name, PageModel pm) {
		RespBase<Object> resp = RespBase.create();
		PageModel list = customHistoryService.list(pm, name);
		resp.setData(list);
		return resp;
	}

	@RequestMapping(value = "/save")
	public RespBase<?> save(@RequestBody CustomHistory customHistory) {
		log.info("into CustomDatasourceController.save prarms::" + "customHistory = {} ", customHistory);
		notNull(customHistory, "customHistory is null");
		RespBase<Object> resp = RespBase.create();
		customHistoryService.save(customHistory);
		return resp;
	}

	@RequestMapping(value = "/detail")
	public RespBase<?> detail(Integer id) {
		RespBase<Object> resp = RespBase.create();
		CustomHistory customHistory = customHistoryService.detal(id);
		resp.setData(customHistory);
		return resp;
	}

	@RequestMapping(value = "/del")
	public RespBase<?> del(Integer id) {
		log.info("into CustomDatasourceController.del prarms::" + "id = {} ", id);
		RespBase<Object> resp = RespBase.create();
		customHistoryService.del(id);
		return resp;
	}



}