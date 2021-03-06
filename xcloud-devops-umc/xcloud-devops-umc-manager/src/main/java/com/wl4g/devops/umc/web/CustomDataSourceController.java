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

import com.wl4g.components.common.serialize.JacksonUtils;
import com.wl4g.components.core.bean.umc.datasouces.MysqlDataSource;
import com.wl4g.components.core.bean.umc.model.DataSourceProvide;
import com.wl4g.components.core.web.BaseController;
import com.wl4g.components.core.web.RespBase;
import com.wl4g.devops.page.PageModel;
import com.wl4g.devops.umc.service.CustomDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.wl4g.components.common.lang.Assert2.notNull;
import static com.wl4g.components.core.bean.umc.model.DataSourceProvide.MYSQL;

/**
 * @author vjay
 * @date 2019-08-05 11:44:00
 */
@RestController
@RequestMapping("/datasource")
public class CustomDataSourceController extends BaseController {


	@Autowired
	private CustomDataSourceService customDataSourceService;

	@RequestMapping(value = "/list")
	public RespBase<?> list(String name, PageModel pm) {
		RespBase<Object> resp = RespBase.create();
		PageModel list = customDataSourceService.list(pm, name);
		resp.setData(list);
		return resp;
	}

	@RequestMapping(value = "/save")
	public RespBase<?> save(String dataSource,String provider) {
		log.info("into CustomDatasourceController.save prarms::" + "customDataSource = {} ", dataSource);
		notNull(dataSource, "customDataSource is null");
		RespBase<Object> resp = RespBase.create();
		if(MYSQL.toString().equalsIgnoreCase(provider)){
			MysqlDataSource mysqlDataSource = JacksonUtils.parseJSON(dataSource, MysqlDataSource.class);
			customDataSourceService.save(mysqlDataSource);
		}
		return resp;
	}

	@RequestMapping(value = "/detail")
	public RespBase<?> detail(Integer id) {
		RespBase<Object> resp = RespBase.create();
		resp.setData(customDataSourceService.detal(id));
		return resp;
	}

	@RequestMapping(value = "/del")
	public RespBase<?> del(Integer id) {
		log.info("into CustomDatasourceController.del prarms::" + "id = {} ", id);
		RespBase<Object> resp = RespBase.create();
		customDataSourceService.del(id);
		return resp;
	}

	@RequestMapping(value = "/dataSourceProvides")
	public RespBase<?> dataSourceProvides() {
		RespBase<Object> resp = RespBase.create();
		resp.setData(DataSourceProvide.dataSourceProvides());
		return resp;
	}

	@RequestMapping(value = "/dataSources")
	public RespBase<?> dataSources() {
		RespBase<Object> resp = RespBase.create();
		resp.setData(customDataSourceService.dataSources());
		return resp;
	}


	@RequestMapping(value = "/testConnect")
	public RespBase<?> testConnect(String provider, String url,String username,String password,Integer id) throws Exception {
		RespBase<Object> resp = RespBase.create();
		customDataSourceService.testConnect(DataSourceProvide.parse(provider),url,username,password,id);
		return resp;
	}



}