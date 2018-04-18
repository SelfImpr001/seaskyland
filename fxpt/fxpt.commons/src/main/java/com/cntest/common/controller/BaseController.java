/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.common.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;

import com.cntest.common.config.SystemRuntimeConfiguration;
import com.cntest.common.page.Page;
import com.cntest.common.query.DefaultQuery;
import com.cntest.common.query.Query;
import com.cntest.security.UserDetails;
import com.cntest.security.UserResource;
import com.cntest.security.remote.IUserResourceService;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆 2013-8-6
 * @version 1.0
 **/
public abstract class BaseController {

	public BaseController() {

	}

	/**
	 * 获取资源文件上的属性
	 * 
	 * @param attrName
	 * @return
	 */
	protected String getProperty(String attrName) {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("properties/resources.properties");
		Properties pro = new Properties();
		try {
			pro.load(in);
			return pro.getProperty(attrName);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			pro.clone();
		}
		return null;
	}

	@Deprecated
	protected UserDetails getCurrentUerDetails() throws Exception {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated())
			return (UserDetails) subject.getPrincipal();
		throw new UnknownAccountException();// 没找到帐号
	}

	protected String getAppKey() throws Exception {
		return SystemRuntimeConfiguration.getAppKey();
	}

	@Deprecated
	protected UserResource getDefaultApp(IUserResourceService resourceService) throws Exception {
		String appKey = getAppKey();
		List<UserResource> apps = resourceService.getResourcesFor(getCurrentUerDetails(), appKey,
				IUserResourceService.Type.APP, IUserResourceService.Level.FIRST);
		if (apps == null || apps.size() == 0)
			return null;
		UserResource defaultApp = apps.get(0);
		if (appKey.equalsIgnoreCase(defaultApp.getUuid()))
			return defaultApp;
		for (UserResource app : apps) {
			if (appKey.equalsIgnoreCase(app.getUuid()))
				return app;
		}
		return defaultApp;
	}

	protected <T> Query<T> newQuery(int currentPage, int pageSize, ServletRequest request) {
		DefaultQuery<T> query = new DefaultQuery.Builder<T>().pagesize(pageSize).curpage(currentPage).create();

		if (request != null) {
			HashMap pmap = new HashMap(request.getParameterMap());
			if (request.getAttribute("gradeid") != null) {
				int gradeid = Integer.parseInt(request.getAttribute("gradeid").toString());
				if (gradeid != -1) {
					String[] gradeStr = { gradeid + "" };
					pmap.put("gradeid", gradeStr);
				}
			}
			for (Object key : pmap.keySet()) {
				if (!"ran".equals(key.toString())) {
					query.setParameters(pmap);
				}
			}
		}
		return query;
	}

	@Deprecated
	protected <T> Page<T> newPage(int currentPage, int pageSize, ServletRequest request) {
		Page<T> page = new Page<T>().setCurpage(currentPage).setPagesize(pageSize);
		if (request != null) {
			Map<String, String[]> tmpMap = request.getParameterMap();
			for (String key : tmpMap.keySet()) {
				String[] value = tmpMap.get(key);
				if (value == null) {
					value = new String[] { "" };
				}
				if (!"ran".equals(key)) {
					page.addParameter(key, value[0]);
				}
			}

		}
		return page;
	}

}
