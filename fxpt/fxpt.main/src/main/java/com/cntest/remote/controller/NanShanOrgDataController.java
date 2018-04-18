/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.remote.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.service.OrganizationService;
import com.cntest.fxpt.etl.util.ReflectionsUtil;
import com.cntest.fxpt.etl.util.StringUtils;
import com.cntest.remote.RemoteHelper;
import com.cntest.remote.domain.NanShanOrgData;
import com.cntest.remote.service.INanShanOrgDataService;
import com.cntest.web.view.ModelAndViewBuilder;

/** 
 * @author cheny 2016年12月12日
 * @version 1.0
 **/
@Controller
@RequestMapping("/ns")
public class NanShanOrgDataController{
	
	private static Logger logger = LoggerFactory.getLogger(NanShanOrgDataController.class);
	
	@Autowired(required = false)
	@Qualifier("INanShanOrgDataService")
	private INanShanOrgDataService nanShanOrgDataService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@RequestMapping(value = "/org",method = RequestMethod.GET)
	public ModelAndView org(ServletRequest request, ServletResponse response,
				@RequestParam(value="result", required =false) String result) throws Exception{
		
		return ModelAndViewBuilder.newInstanceFor("/nanshanOrgTest").append("result", result).build();
	}
	
	/**
	 * 同步新增数据
	 * 新增： 根据orgCode没有对应的记录:4a_org
	 */
	@RequestMapping(value = "/org", method = RequestMethod.POST)
	public void user(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		logger.debug("URL: /nsOrg/org  method POST org ");
		
		String info = "";
		Map<String, String> paramMap = RemoteHelper.getParameterMap(request.getParameterMap());
		if(!paramMap.containsKey("action") || StringUtils.isBlank(paramMap.get("action"))){
			info = "信息同步失败，失败原因：action不能为空";
		}else{
			String action = paramMap.get("action");
			if(!("create".equals(action) || "update".equals(action) || "delete".equals(action))){
				info = "信息同步失败，失败原因：action值不符";
			}else if(!paramMap.containsKey("orgCode") || StringUtils.isBlank(paramMap.get("orgCode"))){
				info = "信息同步失败，失败原因：orgCode不能为空";
			}else{	
				String orgCode = paramMap.get("orgCode");
				switch (action) {
				case "create":
					//新增
					info= actionCreate(null,paramMap);
					break;
				case "delete":
					//删除
					info= actionDelete(orgCode);
					break;
				case "update":
					//修改
					info= actionUpdate(null,paramMap);
					break;
				default:
					info = "信息同步失败，失败原因：其他错误";
				} 
			}
		}
		RemoteHelper.getResult(response,("新增成功".equals(info) || "修改成功".equals(info) || "删除成功".equals(info)), info);
	}
	private String actionUpdate(NanShanOrgData shanData, Map<String, String> paramMap){
		if(shanData == null){
			shanData = new NanShanOrgData();
			Set<String> fieldNames = ReflectionsUtil.getFieldNames(NanShanOrgData.class);
			for (String fieldName : fieldNames) {
				if(paramMap.containsKey(fieldName)){
					ReflectionsUtil.invokeSetter(shanData, fieldName, paramMap.get(fieldName));
				}
			}
		}
		try {
			//parents可能为空或者时“,”，所以要兼容单独逗号也算空
			if((shanData.getParent()!=null && (shanData.getParents()==null) || !",".equals(shanData.getParents())) || (shanData.getParent()==null && shanData.getParents()!=null) || !",".equals(shanData.getParents())){
				return "信息同步失败，失败原因：上级组织代码和所有上级组织代码不匹配（存在空值）";
			}
			if(shanData.getDisplayName()==null){
				return "信息同步失败，失败原因：组织全称不能为空";
			}
			//存在此组织信息才去修改,否则新增
			if(organizationService.findOrgByCode(shanData.getOrgCode())){
				Organization org = organizationService.getOrgByCode(shanData.getOrgCode());
				Long pid =org.getPk();
				//验证上级所有组织是否存在
				if(shanData.getParent()!=null && shanData.getParents()!=null){
					Organization orgF =null;
					String[] str=shanData.getParents().split(",");
					for(int i=0;i<str.length;i++){
						orgF=organizationService.getOrgByCode(shanData.getParent());
						if(orgF==null){
							return "信息同步失败，失败原因：上级组织代码"+str[i]+"不存在";
						}
						if((shanData.getOrgCode().equals(shanData.getParent()))){
							return "信息同步失败，失败原因：不能将自己的作为上级";
						}
						String parents =shanData.getParents();
						//逗号兼容性
						if(parents.substring(parents.length()-1, parents.length())==","){
							parents=parents.substring(0, parents.length()-1);
						}
						//匹配层级关系
						if(orgF.getType()!=parents.split(",").length){
							return "信息同步失败，失败原因：上级组织代码和所有上级组织代码层级关系不匹配";
						}
						
						int typeMes =orgF.getType()-org.getType();
						if(typeMes==1){
							if(pid==(orgF.getParent()!=null?orgF.getParent().getPk():0)){
								return "信息同步失败，失败原因：不能将自己的子级设置为自己的父级";
							}
						}else if(typeMes==2){
							if(pid==(orgF.getParent().getParent()!=null?orgF.getParent().getParent().getPk():0)){
								return "信息同步失败，失败原因：不能将自己的子级设置为自己的父级";
							}
						}else if(typeMes==3){
							if(pid==(orgF.getParent().getParent().getParent()!=null?orgF.getParent().getParent().getParent().getPk():0)){
								return "信息同步失败，失败原因：不能将自己的子级设置为自己的父级";
							}
						}
					}
				}
				nanShanOrgDataService.update(shanData);
			}else{
				return actionCreate(shanData, paramMap);
			}
		} catch (Exception e) {
			return "信息同步失败，失败原因："+e.getMessage();
		}
		return "修改成功";
	}
	
	private String actionCreate(NanShanOrgData shanData, Map<String, String> paramMap){
		if(shanData == null){
			shanData = new NanShanOrgData();
			Set<String> fieldNames = ReflectionsUtil.getFieldNames(NanShanOrgData.class);
			for (String fieldName : fieldNames) {
				if(paramMap.containsKey(fieldName)){
					ReflectionsUtil.invokeSetter(shanData, fieldName, paramMap.get(fieldName));
				}
			}
		}
		try {
			if((shanData.getParent()!=null && (shanData.getParents()==null || ",".equals(shanData.getParents()))) 
				|| (shanData.getParent()==null  && (shanData.getParents()!=null && !",".equals(shanData.getParents())))){
				return "信息同步失败，失败原因：上级组织代码和所有上级组织代码不匹配（存在空值）";
			}
			//验证上级所有组织是否存在
			if(shanData.getParent()!=null && shanData.getParents()!=null){
				Organization orgF =null;
				String[] str=shanData.getParents().split(",");
				for(int i=0;i<str.length;i++){
					orgF=organizationService.getOrgByCode(shanData.getParent());
					if(orgF==null){
						return "信息同步失败，失败原因：上级组织代码"+str[i]+"不存在";
					}
				}
				if((shanData.getOrgCode().equals(shanData.getParent()))){
					return "信息同步失败，失败原因：不能将自己的作为上级";
				}
			}
			
			if(shanData.getParent()!=null && shanData.getParents()!=null){
				Organization orgParent =organizationService.getOrgByCode(shanData.getParent());
				//匹配层级关系
				String parents =shanData.getParents();
				//逗号兼容性
				if(parents.substring(parents.length()-1, parents.length())==","){
					parents=parents.substring(0, parents.length()-1);
				}
				if(orgParent.getType()!=parents.split(",").length){
					return "信息同步失败，失败原因：上级组织代码和所有上级组织代码层级关系不匹配";
				}
			}
			if(!organizationService.findOrgByCode(shanData.getOrgCode())){
				nanShanOrgDataService.add(shanData);
			}else{
				return "信息同步失败，失败原因：组织代码必须唯一";
			}
		} catch (BusinessException e) {
			return "信息同步失败，失败原因："+e.getMessage();
		}
		return "新增成功";
	}
	
	private String actionDelete(String  orgCode){
		try {
			Organization orgF=organizationService.getOrgByCode(orgCode);
			if(orgF.getCode()==null || orgF.getCode()==""){
				return "信息同步失败，失败原因:此组织不存在！";
			}
		List<Organization>	orgList=organizationService.getNextOrgList(orgF.getPk());
		if(orgList.size()>0){
			return "信息同步失败，失败原因:此组织存在下级组织！";
		}
			nanShanOrgDataService.updateState(orgCode);
		} catch (BusinessException e) {
			return "信息同步失败，失败原因："+e.getMessage();
		}
		return "删除成功";
	}
	
}

