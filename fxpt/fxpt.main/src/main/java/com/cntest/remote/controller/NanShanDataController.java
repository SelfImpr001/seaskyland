/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.remote.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.User;
import com.cntest.fxpt.etl.util.ReflectionsUtil;
import com.cntest.fxpt.etl.util.StringUtils;
import com.cntest.remote.RemoteHelper;
import com.cntest.remote.domain.NanShanData;
import com.cntest.remote.service.INanShanDataService;
import com.cntest.web.view.ModelAndViewBuilder;

/** 
 * @author 吕萌 2016年12月5日
 * @version 1.0
 **/
@Controller
@RequestMapping("/ns")
public class NanShanDataController{
	
	private static Logger logger = LoggerFactory.getLogger(NanShanDataController.class);
	
	@Value("${nanshan.demain}")
	private String nanshanDemain;
	
	@Autowired(required = false)
	@Qualifier("INanShanDataService")
	private INanShanDataService nanShanDataService;
	
	@RequestMapping(value = "/user",method = RequestMethod.GET)
	public ModelAndView user(ServletRequest request, ServletResponse response,
				@RequestParam(value="result", required =false) String result) throws Exception{
		
		return ModelAndViewBuilder.newInstanceFor("/nanshan").append("result", result).build();
	}

	
	/**
	 * 登录  get 方式（目前在用的）
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public void login(HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.debug("URL: /ns/get method get");
		String uid =request.getHeader("iv-user");
		//本地测试代码
//		if(uid==null)
//			uid=request.getParameter("uuid");
		logger.debug("login get uid:"+uid);
		if(StringUtils.isBlank(uid))
			uid="";
		if("unauthenticated".equalsIgnoreCase(uid))
			uid="";
		if(!"".equals(uid) && uid.indexOf(",") > -1){
			uid = uid.split(",")[0].split("=")[1];
		}
		
		String info = "";
		if("".equals(uid))
			info = "登录失败，失败原因：当前账号不存在";
		else{
			NanShanData nanShanData = nanShanDataService.findByUid(uid);
			if(nanShanData == null)
				info = "登录失败，失败原因：当前账号不存在";
			else if(nanShanData.getStatus() == 0)
				info = "登录失败，失败原因：当前账号已被删除";
			else{
					logger.debug("nanshan userType" + nanShanData.getLoginId());
					User user = nanShanData.getUser();
					Subject subject = SecurityUtils.getSubject();
					UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), "123456", true);
					try {
						subject.login(token);
						response.sendRedirect(nanshanDemain);
						//response.sendRedirect(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/");
						return;
					} catch (Exception e) {
						logger.error("登录信息错误"+e);
						token.clear();
						info = "登录失败，失败原因："+e.getMessage();
					}
				}
			}
		
		
		RemoteHelper.getResult(response, null, info);
	}
	/**
	 * 同步新增数据
	 * 新增： 根据uid没有对应的记录:nanshan_data 4a_user 4a_user_detail (RoleId给他对应的角色 --并赋予它对应角色拥有的权限)
	 * 	         根据uid有对应的记录:更改 nanshan_data status=1
	 * 删除：更改 nanshan_data status=0
	 */
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public void user(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		logger.debug("URL: /ns/user  method POST user ");
		
		String info = "";
		Map<String, String> paramMap = RemoteHelper.getParameterMap(request.getParameterMap());
		if(!paramMap.containsKey("action") || StringUtils.isBlank(paramMap.get("action"))){
			info = "信息同步失败，失败原因：action不能为空";
		}else{
			String action = paramMap.get("action");
			if(!("create".equals(action) || "update".equals(action) || "delete".equals(action) ||  "login".equals(action))){
				info = "信息同步失败，失败原因：action值不符";
			}else if(!paramMap.containsKey("uid") || StringUtils.isBlank(paramMap.get("uid"))){
				info = "信息同步失败，失败原因：UID不能为空";
			}else{	
				String uIDStr = paramMap.get("uid");
				NanShanData shanData = nanShanDataService.findByUid(uIDStr);
				switch (action) {
				case "create":
					info = actionCreate(shanData, paramMap);
					break;
				case "update":
					info = actionUpdate(shanData, paramMap);
					break;
				case "delete":
					info = actionDelete(shanData);
					break;
				default:
					info = "信息同步失败，失败原因：其他错误";
				} 
			}
		}
		
		RemoteHelper.getResult(response,("新增成功".equals(info) || "修改成功".equals(info) || "新增成功".equals(info)), info);
	}
	private String actionUpdate(NanShanData shanData, Map<String, String> paramMap){
		if(shanData == null)
			return "信息同步失败，失败原因：当前账号不存在";
		else if(shanData.getStatus() == 0)
			return "信息同步失败，失败原因：当前账号已被删除";
		
		Set<String> fieldNames = ReflectionsUtil.getFieldNames(NanShanData.class);
		for (String fieldName : fieldNames) {
			if(paramMap.containsKey(fieldName)){
				ReflectionsUtil.invokeSetter(shanData, fieldName, paramMap.get(fieldName));
			}
		}
		
		try {
			nanShanDataService.update(shanData);
		} catch (BusinessException e) {
			return "信息同步失败，失败原因："+e.getMessage();
		}
		return "修改成功";
	}
	
	private String actionCreate(NanShanData shanData, Map<String, String> paramMap){
		if(shanData != null && shanData.getStatus() == 1)
			return "信息同步失败，失败原因：已存在有效的账号";
		
		if(shanData == null){
			shanData = new NanShanData();
			Set<String> fieldNames = ReflectionsUtil.getFieldNames(NanShanData.class);
			for (String fieldName : fieldNames) {
				if(paramMap.containsKey(fieldName)){
					ReflectionsUtil.invokeSetter(shanData, fieldName, paramMap.get(fieldName));
				}
			}
		}
			
		try {
			nanShanDataService.add(shanData);
		} catch (BusinessException e) {
			return "信息同步失败，失败原因："+e.getMessage();
		}
		return "新增成功";
	}
	
	private String actionDelete(NanShanData shanData){
		if(shanData == null)
			return "信息同步失败，失败原因：当前账号不存在";
		else if(shanData.getStatus() == 0)
			return "信息同步失败，失败原因：当前账号已被删除";
		
		try {
			nanShanDataService.updateState(shanData);
		} catch (BusinessException e) {
			return "信息同步失败，失败原因："+e.getMessage();
		}
		return "删除成功";
	}
	
}

