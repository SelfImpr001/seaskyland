package com.cntest.fxpt.controller.report;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.bi.domain.BiTokens;
import com.cntest.fxpt.service.bi.ReportBiMenuService;
import com.cntest.security.UserDetails;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;


@Controller
@RequestMapping("/report/biMenu")
public class ReportBiMenuController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(ReportBiMenuController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ReportBiMenuService reportBiMenuService;
	
	@RequestMapping(value = "/addTokens/{url}", method = RequestMethod.POST)
	public ModelAndView addTokens(@PathVariable String url, HttpServletRequest request) throws Exception {
		/*String myUrl = new String(Base64.decodeBase64(url.getBytes()));
        String newMyUrl = URLEncoder.encode(URLDecoder.decode(myUrl, "UTF-8"), "UTF-8")
        		.replaceAll("\\%2[fF]", "/").replaceAll("\\+", "%20").replaceAll("\\%3[fF]", "?");*/
		
		UserDetails userDetails = userService.getCurrentLoginedUser();
		User user = User.from(userDetails);
		String sessionId = request.getSession().getId();
		url = url.replaceAll("-", "/");
		BiTokens tokens = new BiTokens();
		tokens.setUsername(user.getName());
		tokens.setToken(sessionId);
		tokens.setUrl(url);
		tokens.setCreated(new Date());
		reportBiMenuService.addTokens(tokens);
		return ModelAndViewBuilder.newInstanceFor("")
				.append("tokens", tokens)
				.append(ResponseStatus.NAME,new ResponseStatus.Builder(Boolean.TRUE)
								.code("fxpt.report.biMenu.addTokens").msg("")
								.build()).build();
	}
	
	

}
