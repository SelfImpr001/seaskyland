/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.web.view;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.cntest.license.IllegalLicenseException;
import com.cntest.license.LicenseInfo;
import com.cntest.license.LicenseManager;

/**
 * <pre>
 *  
 * </pre>
 * 
 * @author 李贵庆 2013-6-10
 * @version 1.0 
 **/
public class ModelAndViewBuilder {

	private Map<String,Object> model;
	
	private String viewName;
	
	private HttpServletRequest request;

	
	private ModelAndViewBuilder(String viewName) {
		this.viewName = viewName;
		this.model = new HashMap<String,Object>();
	}
	
	public static ModelAndViewBuilder newInstanceFor() {
		return ModelAndViewBuilder.newInstanceFor("",Boolean.TRUE);
	}
	
	public static ModelAndViewBuilder newInstanceFor(String viewName) {
		return ModelAndViewBuilder.newInstanceFor(viewName,Boolean.TRUE);
	}
	
	public static ModelAndViewBuilder newInstanceFor(String viewName,Boolean successed) {
		LicenseManager.instance().doLicense();
		LicenseInfo info = LicenseManager.instance().getLicenseResult();
		if(!info.isAccessabled())
			throw new IllegalLicenseException(info.getMessage());
		return new ModelAndViewBuilder(viewName).append(ResponseStatus.NAME,new ResponseStatus.Builder(successed).build());
		//return ModelAndViewBuilder.newInstanceFor(viewName).append(ResponseStatus.NAME,new ResponseStatus.Builder(Boolean.TRUE).build());
	}
	
	
	public ModelAndViewBuilder request(HttpServletRequest request) {
		this.request = request;
		return this;
	}
	
	public ModelAndViewBuilder append(String modelName,Object model) {
		this.model.put(modelName, model);
		return this;
	}
	
	public ModelAndView build() {
		if(this.model == null)
			this.model = new HashMap<String,Object>();
		if(this.viewName == null || this.viewName.length() == 0)
			this.viewName = "/nothing";
		return new ModelAndView(viewName,model);
	}
	
}
