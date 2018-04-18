/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年7月5日
 * @version 1.0
 **/

@Controller
@RequestMapping("/manager")
public class ManagerController extends BaseController {
	private static Logger logger = LoggerFactory
			.getLogger(ManagerController.class);

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView getPwdModal() throws Exception {

		logger.debug("URL: /user/setpwd method PUT");

		return ModelAndViewBuilder.newInstanceFor("/manager/home").build();
	}

}
