package com.cntest.addressSet.controller;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileUpload;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.fxpt.util.DBConnectTest;
import com.cntest.fxpt.util.JDBCConfig;
import com.cntest.fxpt.util.PropertiesConfig;
import com.cntest.fxpt.util.ShiroClientConfig;
import com.cntest.fxpt.util.SystemConfig;
import com.cntest.remote.RemoteHelper;
import com.cntest.web.view.ModelAndViewBuilder;

/**
 * sso、pentaho配置
 * @author chenyou
 * @date 2017-05-08
 * @version 1.0 
 */
@Controller
@RequestMapping("/systemSet")
public class CasManager {
	private static Logger logger = LoggerFactory.getLogger(CasManager.class);
	static String[] ssoSub=new String[]{"area.org.code","jdbc_username","jdbc_password","jdbc_ip","jdbc_port","jdbc_database","shiro.sso.server.url","shiro.sso.service.url","shiro.sso.login.url","shiro.sso.loginOut.url"};
	static String[] pentahoSub=new String[]{"contextAddress","name","password","post","tableAddress","pentahoAddress"};
	@RequestMapping(value = "/sso",method = RequestMethod.GET)
	public ModelAndView user(HttpServletRequest request, HttpServletResponse response,
				@RequestParam(value="result", required =false) String result) throws Exception{
		HttpSession session =request.getSession(); 
		String rname=SystemConfig.newInstance().getValue("system.name");
		String rpassword=SystemConfig.newInstance().getValue("system.password");
		if(session.getAttribute("userNameForSet")!=null && session.getAttribute("passwordForSet")!=null && session.getAttribute("userNameForSet").equals(rname) && session.getAttribute("passwordForSet").equals(rpassword)){
			Properties prop = new Properties();
			InputStream fis =ShiroClientConfig.class.getResourceAsStream("/properties/shiro-client-default.properties");
	        prop.load(fis);  
	        //一定要在修改值之前关闭fis  
	        fis.close();  
			String serverUrl=(String) prop.get("shiro.sso.server.url");
			//ShiroClientConfig.newInstance().getValue("shiro.sso.server.url");
			String serviceUrl=(String) prop.get("shiro.sso.service.url");
			String loginUrl=(String) prop.get("shiro.sso.login.url");
			String loginOutUrl=(String) prop.get("shiro.sso.loginOut.url");
			InputStream fis1 =SystemConfig.class.getResourceAsStream("/properties/system.properties");
			prop.load(fis1);
			fis1.close();  
			String areaCode=(String) prop.get("area.org.code");
			Properties prop2 = new Properties();
			InputStream fis2 =JDBCConfig.class.getResourceAsStream("/properties/jdbc.properties");
			prop2.load(fis2);
			fis2.close();  
			String fxpthost=(String) prop2.get("JDBC_IP");
			String post=(String) prop2.get("JDBC_PORT");
			String name=(String) prop2.get("JDBC_USERNAME");
			String password=(String) prop2.get("JDBC_PASSWORD");
			String tablname=(String) prop2.get("JDBC_DATABASE");
			
			return ModelAndViewBuilder.newInstanceFor("/ssoSet").
					append("serverUrl", serverUrl).
					append("serviceUrl", serviceUrl). 
					append("loginUrl", loginUrl).
					append("loginOutUrl", loginOutUrl).
					append("areaCode", areaCode).
					append("fxpthost", fxpthost).
					append("post", post).
					append("name", name).
					append("password", password).
					append("tablname", tablname).
					append("message", "").
					append("result", result).build();
		}else{
			return ModelAndViewBuilder.newInstanceFor("/loginSet").build();
		}
		
	}
	/**
	 * 
	 * @param status
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(value = "/pentahoXML", method = RequestMethod.POST)
	public ModelAndView pentahoGiveXML(HttpServletRequest request, ServletResponse response) throws Exception {
		logger.debug("URL: /pentahoXML  method GET cas ");
		String filePath=request.getParameter("selectvalue");
		String address="";
		String post="";
		String dataBase="";
		String userName="";
		String password="";
		boolean info=false;
    	try {
			SAXReader saxReader =new SAXReader();
			File oldFile =new File(filePath);
			if(oldFile.exists()){
				Document doc =saxReader.read(oldFile);
				List list =doc.selectNodes("//Resource");
				if(list!=null){
					Iterator it =list.iterator();
					while(it.hasNext() && !info){
						Element element =(Element) it.next();
						//address获取类似这种，需要信息提取---jdbc:mysql://192.168.2.126:3306/cntest_fxpt_ns
						String	address1=element.attribute("url")!=null?element.attribute("url").getStringValue():"";
						userName=element.attribute("username")!=null?element.attribute("username").getStringValue():"";
						password=element.attribute("password")!=null?element.attribute("password").getStringValue():"";
						if(address1!=""){
							String[] url=address1.split("/");
							if(url.length>3){
								dataBase=url[3];
								String[] l=url[2].split(":");
								if(l.length>1){
									address=l[0];
									post=l[1];
								}
							}
						}
						info=true;
					}
				}
			}
		} catch (Exception  ex) {
			ex.printStackTrace();
		}
    	if(info){
    		return ModelAndViewBuilder.newInstanceFor("/pentahoSet").
        			append("address", address).
    				append("userName", userName). 
    				append("password", password).
    				append("dataBase", dataBase).
    				append("post", post).
    				build();
    	}else{
    		return ModelAndViewBuilder.newInstanceFor("").
    				build();
    	}
    	
	}
	/**
	 * 进来登录页面
	 * @param request
	 * @param response
	 * @param result
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public ModelAndView login(ServletRequest request, ServletResponse response,
				@RequestParam(value="result", required =false) String result) throws Exception{
		return ModelAndViewBuilder.newInstanceFor("/loginSet").
				append("result", result).build();
	}
	/**
	 * 登录去设置pentaho
	 * @param status
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loginGo", method = RequestMethod.GET)
	public ModelAndView loginGo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("URL: /casSet/cas  method POST cas ");
		String info = "用户名或密码错误！";
		boolean flg=false;
		HttpSession session =request.getSession(); 
		//页面获取的用户名密码
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		//配置文件的用户名密码
		String rname=SystemConfig.newInstance().getValue("system.name");
		String rpassword=SystemConfig.newInstance().getValue("system.password");	
		
		if(rname.equals(name) && rpassword.equals(password)){
			session.setAttribute("userNameForSet", name);
			session.setAttribute("passwordForSet", password);
			return ModelAndViewBuilder.newInstanceFor("/ssoSet")
					.build();
		}else{
			return ModelAndViewBuilder.newInstanceFor("")
					.build();
		}
	}
	
	@RequestMapping(value = "/ssoSub", method = RequestMethod.GET)
	public ModelAndView cas(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("URL: /casSet/cas  method POST cas ");
		Map<String, String> paramMap = new HashMap<String, String>();
		for(String str:ssoSub){
			paramMap.put(str, request.getParameter(str));
		}
		Boolean flg=PropertiesConfig.writeData(paramMap);
		if(flg){
			return ModelAndViewBuilder.newInstanceFor("/ssoSet").build();
		}else{
			return ModelAndViewBuilder.newInstanceFor("").build();
		}
		
	}
	@RequestMapping(value = "/testDBconnect/{status}", method = RequestMethod.GET)
	public ModelAndView validate(@PathVariable String  status,HttpServletRequest request, ServletResponse response) throws Exception {
		logger.debug("URL: /casSet/cas  method GET cas ");
		String info = "";
		boolean flg=false;
		String name=request.getParameter("name");
		String password=request.getParameter("password");
		String host=request.getParameter("host");
		String tablename=request.getParameter("tablename");
		
		Map<String, String> paramMap = RemoteHelper.getParameterMap(request.getParameterMap());
		String action =" ";
	    flg=DBConnectTest.getConnection(host,tablename,name,password);
	    if(flg){
	    	action="/pentahoSet";
		}	
	    return ModelAndViewBuilder.newInstanceFor(action)
				.build();
	}
	@RequestMapping(value = "/pentaho",method = RequestMethod.GET)
	public ModelAndView pentaho(HttpServletRequest request, HttpServletResponse response,
				@RequestParam(value="result", required =false) String result) throws Exception{
		HttpSession session =request.getSession(); 
		String rname=SystemConfig.newInstance().getValue("system.name");
		String rpassword=SystemConfig.newInstance().getValue("system.password");
		if(session.getAttribute("userNameForSet")!=null && session.getAttribute("passwordForSet")!=null && session.getAttribute("userNameForSet").equals(rname) && session.getAttribute("passwordForSet").equals(rpassword)){
			return ModelAndViewBuilder.newInstanceFor("/pentahoSet").
					append("result", result).
					append("address", "").
					append("userName", ""). 
					append("password", "").
					append("dataBase", "").
					append("post", "").
					build();
		}else{
			return ModelAndViewBuilder.newInstanceFor("/loginSet").
					append("result", result).build();
		}
	}
	
	@RequestMapping(value = "/fileview",method = RequestMethod.POST)
	public ModelAndView fileview(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String cfile = request.getParameter("pfile");
		String r = request.getParameter("return");
		File paremt = new File("");
		File[] file = null;
		boolean isRoot = false;
		boolean isDire = true;
		if(cfile!=null &&cfile.length()>0){
			if(r!=null && "true".equals(r)){
				paremt = new File(cfile).getParentFile();
				if(paremt!=null){
					file = paremt.listFiles();//获取下一级全部列表
				}else{
					file = File.listRoots();
					if(file.length==1){
						file = file[0].listFiles();
						isRoot = false;
					}else{
						isRoot = true;
					}
				}
			}else{
				paremt = new File(cfile);
				isDire = paremt.isDirectory();
				file = paremt.listFiles();//获取下一级全部列表
			}
		}else{
			file = File.listRoots();
			if(file.length==1){
				file = file[0].listFiles();
				isRoot = false;
			}else{
				isRoot = true;
			}
		}
		
		return ModelAndViewBuilder.newInstanceFor("/fileShow")
				.append("fileArray",file)
				.append("isDire",isDire)
				.append("isRoot",isRoot)
				.append("paremt",paremt).build();
	 
	}
	public static void main(String[] args) {
		File[] file = File.listRoots();
		if(file.length==4){
			file = file[0].listFiles();
		}
		for (File f : file) {
			System.out.println(f.getPath());
		}
	}
	
	@RequestMapping(value = "/pentahoSub", method = RequestMethod.GET)
	public ModelAndView pentaho(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("URL: /casSet/cas  method POST cas ");
		String action="";
		Map<String, String> paramMap = RemoteHelper.getParameterMap(request.getParameterMap());
		for(String p:pentahoSub){
			paramMap.put(p, request.getParameter(p));
		}
		boolean flg=PropertiesConfig.updateXML(paramMap);
		if(flg){
		    action="/pentahoSet";
		}	
		return ModelAndViewBuilder.newInstanceFor(action)
					.build();
	}
}
