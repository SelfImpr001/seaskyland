/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.common.query.Query;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.validate.OrgExamDataNotNullValidator;
import com.cntest.foura.service.OrganizationService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.SchoolSegment;
import com.cntest.fxpt.domain.SchoolType;
import com.cntest.fxpt.etl.domain.EtlProcessResult;
import com.cntest.fxpt.service.ISchoolSegmentService;
import com.cntest.fxpt.service.ISchoolTypeService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.SystemConfig;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.util.ExceptionHelper;
import com.cntest.util.ZTree;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 肖 肖 2014年7月2日
 * @version 1.0
 **/
@Controller
@RequestMapping("/org")
public class OrganizationController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(URLResourceController.class);
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ISchoolTypeService schoolTypeService;
	
	@Autowired
	private ISchoolSegmentService schoolSegmentService;
	
	@RequestMapping(value="/list",method = RequestMethod.GET)
	public ModelAndView list(HttpServletRequest request) throws Exception{
		logger.info("list {}",Organization.class);
		User user = User.from(userService.getCurrentLoginedUser());
		List<Organization> orgtmp = organizationService.getTopOrgList(null);
		List<Organization> orgList=organizationService.nextOrgCount(orgtmp); 

		orgList = 	organizationService.finCountUser(orgList, null);
		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder
				.newInstanceFor("organization/list")
				.append("orgList", orgList)
				.append("title",title)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.list")
								.msg(user.getName()).build()).build();
		
	}
	
	
	@RequestMapping(value="/nextOrglist/{pk}",method = RequestMethod.GET)
	public ModelAndView nextOrglist(@PathVariable Long pk) throws Exception{
		logger.info("nextOrglist {}",Organization.class);
		User user = User.from(userService.getCurrentLoginedUser());
		List<Organization> orgtmp = organizationService.getNextOrgList(pk);
		List<Organization> orgList=organizationService.nextOrgCount(orgtmp); 
		
		return ModelAndViewBuilder
				.newInstanceFor("organization/list")
				.append("orgList", orgList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.nextOrglist")
								.msg(user.getName()).build()).build();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(@RequestBody Organization org) throws Exception{
		logger.info("add {}",Organization.class);
		if(org.getParent()!=null) {
			Organization parent = organizationService.load(org.getParent().getPk());
			org.setParent(parent);
		}
		if(org.getSchooltype()==null || (org.getSchooltype()!=null?org.getSchooltype().getId()==null:false)){
			org.setSchooltype(null);
		}
		if(org.getSchoolSegment()==null || (org.getSchoolSegment()!=null?org.getSchoolSegment().getId()==null:false)){
			org.setSchoolSegment(null);
		}
		String status = "失败",info = "新增组织<b style='color:red;'>",erre="";
		try {
			organizationService.create(org);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+=org.getName()+"</b>"+status;
			LogUtil.log("组织管理>组织列表", "新增",org.getName(),status, info,erre);
		}
		
		List<Organization> orgList = new ArrayList<Organization>();
		
		if(org.getParent() != null)
			 orgList = organizationService.getOrgSubList(org.getParent().getPk());
		else 
			orgList = organizationService.getTopOrgList(null);
		
		return ModelAndViewBuilder
				.newInstanceFor("organization/subList")
				.append("orgList", orgList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.add")
						.msg("保存成功,PK：" + org.getPk()).build()).build();
		
	}
	
	/**
	 * 
	 * 新增组织时，验证code的唯一性
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/validate/{code}/{orgId}", method = RequestMethod.POST)
	public ModelAndView validate(@PathVariable String code,@PathVariable String orgId) throws Exception {
		boolean flg = organizationService.findOrgByCode(code,orgId);
		return ModelAndViewBuilder.newInstanceFor("").append("flg", flg)
				.build();
	}
	
	
	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody Organization org) throws BusinessException{
		logger.info("update {}",Organization.class);
		
		List<Organization> orgList = new ArrayList<Organization>();
		
		String status = "失败",info = " ",erre="";
		Organization orgs = organizationService.load(org.getPk());
		try {
			info+= "</br>修改项</br>";
			info+= updatainfo(org,orgs);
			userService.evictSession(orgs);
			if(org.getParent() != null){
				if( org.getParent().getPk() == null){
					org.setParent(null);
				}else{
				/*	Long pk1 = org.getParent().getPk();
					Organization temParent =  new Organization();//organizationService.getOrgByPk(pk1);
					temParent.setPk(pk1);
					
					
					
					userService.evictSession(temParent);*/
					org.setParent(orgs.getParent());
					if(org.getType()==4){
						if(org.getSchooltype().getId()==null){
							org.setSchooltype(null);
						}
						
						if(org.getSchoolSegment().getId()==null){
							org.setSchoolSegment(null);
						}
					}
				}
			}
			organizationService.update(org);
			if(org.getParent() != null)
				 orgList = organizationService.getOrgSubList(org.getParent().getPk());
			else 
				orgList = organizationService.getTopOrgList(null);
			
			orgList = 	organizationService.finCountUser(orgList, null);
			orgList = organizationService.nextOrgCount(orgList);
			
			status = "成功";
			
		} catch (BusinessException e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("组织管理>组织列表", "修改",orgs.getName(),status, "组织<b style='color:red;'>"+orgs.getName()+"</b>修改"+status+"</br>"+info,erre);
		}
		return ModelAndViewBuilder
				.newInstanceFor("organization/subList")
				.append("orgList", orgList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.update")
						.msg("修改成功").build()).build();
		
	}
	
	public String updatainfo(Organization resource,Organization temp) throws BusinessException {
		//resource:新来的数据，temp:之前的数据
		String info="";
		if(resource.getName()!=null && (!resource.getName ().equals(temp.getName()))) {
			info+=("组织名称：<b style='color:red;'>"+temp.getName()+"</b> 改  <b style='color:red;'>"+resource.getName()+"</b><br/>");
		}
		if(resource.getCode()!=null && (!resource.getCode ().equals(temp.getCode()))) {
			info+=("组织代码：<b style='color:red;'>"+temp.getCode()+"</b> 改  <b style='color:red;'>"+resource.getCode()+"</b><br/>");
		}
		if(resource.getParent()!=null && temp.getParent()!=null) {
			if(!resource.getParent().getPk().equals(temp.getParent().getPk())) {
				Organization o = organizationService.load(resource.getParent().getPk());
				info+=("上级组织：<b style='color:red;'>"+temp.getParent().getName()+"</b> 改  <b style='color:red;'>"+o.getName()+"</b><br/>");
			}
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info+"</br>";
	}
	/**
	 * 删除组织验证其数据关联
	 * @param pk 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteValidate/{pk}", method = RequestMethod.POST)
	public ModelAndView deleteValidate(@PathVariable String pk) throws Exception {
		String mes ="";
		boolean flg=false;
		//获取system配置文件的校验开关cz.orgValidate
		String openOr = SystemConfig.newInstance().getValue("cz.orgValidate");
		if(openOr.equals("open")){
			if(pk=="" || pk==null){
				mes="未选中要删除的对象！";
			}else{
				flg=true;
				mes= organizationService.initOrganization(pk);
			}
		}else{
			flg=true;
		}
		return ModelAndViewBuilder.newInstanceFor("").append("flg", flg).append("mes", mes)
				.build();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody Organization org) throws Exception{
		logger.info(" pk = {}  delete {}",org.getPk(),Organization.class);
		//org = organizationService.load(org.getPk());
		//organizationService.remove(org);
		//删除组织及其下级所有组织
		String status = "失败",info = "删除组织<b style='color:red;'>",erre="";
		Organization orgs = organizationService.load(org.getPk());
		try {
			organizationService.deleteOrgAndChild(org.getPk());
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+=orgs.getName()+"</b>"+status;
			LogUtil.log("组织管理>用户列表", "删除组织",orgs.getName(),status, info,erre);
		}
		
		
		
		return ModelAndViewBuilder
				.newInstanceFor("/organization/subList")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.delete")
						.msg("组织"+org.getName()+"删除成功").build()).build();
		
	}
	
	/**
	 * 树形菜单拖拽更新
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dragOrg/{name}/{parentName}", method = RequestMethod.POST)
	public ModelAndView dragOrg(@PathVariable String name,@PathVariable String parentName) throws Exception {
		//拖动更改组织关系
		organizationService.updateOrgByName(name,parentName);
		return ModelAndViewBuilder.newInstanceFor("")
				.build();
	}
	//组织导出
	@RequestMapping(value="/orgOut/{type}/{pk}",method = RequestMethod.POST)
	public ModelAndView orgOut(@PathVariable Long type,@PathVariable Long pk, HttpServletRequest request,HttpServletResponse response) throws Exception{
		//根据pk值查询对应数据
		logger.info(" pk = {}  orgout {}",pk,Organization.class);
		EtlProcessResult processResult = new EtlProcessResult();
		String flg="";
		//获取路径
		String status = "失败",info = "导出组织有<b style='color:red;'>",erre="";
		List<String> list = new ArrayList<String>();
		try {
			if(pk!=null && pk!=0) {
				//生成excel
				organizationService.createOrgExcel(pk,type,request);
				list = (List<String>)request.getAttribute("orglist");
				if(list!=null && list.size()>0) {
					for (String name : list) {
						info+=name+"</br>";
					}
				}else {
					list = new ArrayList<String>();
				}
				
				processResult.setHasError(false);
				processResult.setMessage("导出成功！");
				flg="1";
				status = "成功";
			}else if(pk==0) {
				processResult.setHasError(false);
				processResult.setMessage("导出成功！");
			}else{
				processResult.setHasError(true);
				processResult.setMessage("没有选中要导出的数据");
			}
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+="</b>"+status;
			LogUtil.log("组织管理>组织列表", "组织导出", "<div style='width:100%; max-height:60px; overflow-y:auto; overflow-x:auto;'>"+StringUtils.join(list.toArray()),status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").append("flg", flg)
				.build();
	}
	@RequestMapping(value="/view/{parentPk}/{pk}/{methodType}",method = RequestMethod.GET)
	public ModelAndView view(@PathVariable Long parentPk,@PathVariable Long pk,@PathVariable String methodType, HttpServletRequest request) throws Exception{

		logger.info("view {} pk = {}",Organization.class,pk);
		Organization org = new Organization();
		Organization parent = null;
		SchoolType schoolType = null;
		SchoolSegment schoolSegment = null;
		List<Organization> parentLevels = new ArrayList<Organization>();
		boolean schoolFlg=true;
		boolean schoolTypeFlg=true;
		boolean hasDataRef = true;
		if(methodType.equals("update")){
			hasDataRef = false;
		}
		if(pk != -1) {
			org    = organizationService.load(Long.valueOf(pk));
			//org = organizationService.getOrgByPk(pk);
			if(org.getSchoolSegment()==null){
				schoolFlg=false;
			}
			if(org.getSchooltype()==null){
				schoolTypeFlg=false;
			}
			parent = org.getParent();
			schoolType = org.getSchooltype();
			schoolSegment = org.getSchoolSegment();
			
			OrgExamDataNotNullValidator validator = new OrgExamDataNotNullValidator();
			try {
				if(org.getParent()!=null && org.getParent().getParent()!=null)
					parentLevels = organizationService.getOrgSubList(org.getParent().getParent().getPk());
				else
					parentLevels = organizationService.getTopOrgList(null);
			    validator.validate(org);
			   // hasDataRef = false;
			  
			}catch(Exception e) {
				logger.warn(ExceptionHelper.trace2String(e));
			}
		}else {
			if(parentPk!=-1)
				parent = organizationService.load(parentPk);
		}
		
		List<SchoolType> schoolTypeList = schoolTypeService.getAllSchoolType();
		List<SchoolSegment> schoolSegmentList = schoolSegmentService.getAllSchoolSegment();
		return ModelAndViewBuilder
				.newInstanceFor("organization/edit")
				.append("org",org)
				.append("parent",parent)
				.append("schoolType",schoolType)
				.append("schoolSegment", schoolSegment)
				.append("parentLevels", parentLevels)
				.append("hasDataRef", hasDataRef)
				.append("schoolFlg", schoolFlg)
				.append("schoolTypeFlg", schoolTypeFlg)
				.append("schoolTypeSelect", schoolTypeList)
				.append("schoolSegmentSelect", schoolSegmentList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.view")
						.msg("").build()).build();
		
	}
	@RequestMapping(value="/tree/list/{level}",method = RequestMethod.GET)
	public ModelAndView treeNodeList(@PathVariable int level,HttpServletRequest request,HttpServletResponse response) throws Exception{;
		logger.info("treeNodeList {} 异步请求树节点数据");
        logger.info("treeNodeList {} select * From");

        List<Organization> orgList = organizationService.getTopOrgList(null);       
        JsonArray jsonArray  = buildTreeData(orgList,level,0);        
        logger.info("treeNodeList {}",jsonArray.toString());
		return ModelAndViewBuilder
				.newInstanceFor("")
				.append("orgJson",jsonArray.toString())
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.tree.list")
						.msg("success").build()).build();
	}
	
	@RequestMapping(value="/orExist",method = RequestMethod.POST)
	public ModelAndView orExist(HttpServletRequest request,HttpServletResponse response) throws Exception{;
		int num = organizationService.findOrgList();
		return ModelAndViewBuilder
				.newInstanceFor("")
				.append("num",num)
				.build();
	}
	
	@RequestMapping(value="/tree/children",method = RequestMethod.GET)
	public ModelAndView childNodelist(HttpServletRequest request,HttpServletResponse response) throws Exception{;
		logger.info("childNodelist {} 异步请求树节点数据");
		logger.info("childNodelist {} select * From");
		String pk = request.getParameter("pk");
		
		List<Organization> orgList = organizationService.getOrgSubList(Long.valueOf(pk));
		JsonArray jsonArray  = buildTreeData(orgList,0,0);
		logger.info("childNodelist {}",jsonArray.toString());

		return ModelAndViewBuilder
				.newInstanceFor("")
				.append("orgJson",jsonArray.toString())
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.tree.children")
						.msg("success").build()).build();
	}
	
	@RequestMapping(value="/subList",method = RequestMethod.GET)
	public ModelAndView forChildMenu(HttpServletRequest request) throws Exception{;
		logger.info("forChildMenu {} 异步请求子列表数据");
		logger.info("forChildMenu {} select * From");
		 User user = User.from(userService.getCurrentLoginedUser());
		String pk 		= request.getParameter("pk");
		String isParent = request.getParameter("isParent");
		String qname = request.getParameter("qname");
		String is = request.getParameter("is");
		
		List<Organization> orgtmp = new ArrayList<Organization>();
		if(is!=null && "true".equals(is)) {
			if(qname!=null && qname.trim().length()>0) {
				orgtmp = organizationService.listByNameFor(qname);
			}else {
				orgtmp = organizationService.getTopOrgList(null);
			}
		}else {
			if(pk.equals("-1")) {
				if(qname!=null && qname.trim().length()>0) {
					orgtmp = organizationService.listByNameFor(qname);
					orgtmp = 	organizationService.finCountUser(orgtmp, null);
				}else {
					orgtmp = organizationService.getTopOrgList(null);
					orgtmp = organizationService.nextOrgCount(orgtmp); 
					orgtmp = organizationService.finCountUser(orgtmp, null);	
				}
			}else {
				if(isParent.equals("true")) {
					if(qname!=null && !"".equals(qname)){
						int type = organizationService.load(Long.valueOf(pk)).getType();
						orgtmp = organizationService.getOrgAllByName(Long.valueOf(pk),Long.parseLong(type+""),qname);
					}else{
						orgtmp = organizationService.getOrgSubList(Long.valueOf(pk),qname);
					}
				}else {
					orgtmp.add(organizationService.getOrgByPk(Long.valueOf(pk)));
				}
			}
		}
		List<Organization> orgList=organizationService.nextOrgCount(orgtmp);
		return ModelAndViewBuilder
				.newInstanceFor("organization/subList")
				.append("orgList", orgList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.forChildMenu")
						.msg(user.getName()).build()).build();
	}
	private List<Organization> getOrgByPkAndName(Long pk,String name,List<Organization> sumList) throws BusinessException{
		//List<Organization> organizations=organizationService.getOrgSubList(pk, name);
		List<Organization> organizationsT=organizationService.getOrgSubList(pk);
		for (Organization organization : organizationsT) {
			sumList.add(organization);
			List<Organization> organizations=organizationService.getOrgSubList(pk, name);
			if(organizationService.getNextOrgList(organization.getPk()).size()>0){
				getOrgByPkAndName(organization.getPk(),name,sumList);
			}
		}
		return null;
	}
	@RequestMapping(value="/subList/{currentPage}/{pageSize}",method = RequestMethod.GET)
	public ModelAndView forChildMenu(@PathVariable int currentPage,@PathVariable int pageSize,HttpServletRequest request) throws Exception{;
		logger.info("forChildMenu {} 异步请求子列表数据");
		logger.info("forChildMenu {} select * From");
		String pk 		= request.getParameter("pk");
		Query<Organization> query  =newQuery(currentPage, pageSize, request);
		if(pk.equals("-1")) {
			query.setResults(organizationService.getTopOrgList(null));
		}else {			
			organizationService.getOrgSubList(Long.valueOf(pk),query);			
		}
		
		return ModelAndViewBuilder
				.newInstanceFor("organization/subList")
				.append("orgList", query.getResults())
				.append("query",query)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.forChildMenu")
						.msg("学校列表").build()).build();
	}
	//pk  pk值是用来展开树形结构节点的判断（连接下级列表时用到）
	private JsonArray buildTreeData(List<Organization> orgList, int level, int i) {
		JsonArray jsonArray = new JsonArray();
		for (Organization org : orgList) {
        	JsonObject object = new JsonObject();
        	logger.debug("PK:{}",org.getPk());
        	ArrayList<Organization> children = new ArrayList<Organization>();
        	
        	children.addAll(organizationService.getOrgChildrenNoLeaf(org.getPk()));
        	
        	
        	object = new ZTree().id(org.getPk())
        							   .pId(org.getParent()!=null?org.getParent().getPk():-1L)
        							   .open(i<level).isParent(children.size() > 0).expandDIY("type", org.getType())
        							   .zTreeJsonObj(org);
        	object.remove("url");
        	if(i < level) {
        		jsonArray.addAll(buildTreeData(children,level,i + 1));
        	}
        	jsonArray.add(object);
		}
		return jsonArray;
	}
	/**
	 * 查看角色
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userview/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView view(@PathVariable int currentPage,@PathVariable int pageSize,ServletRequest request) throws Exception {
		Long id=Long.valueOf((request.getParameterMap().get("roleId")[0]).toString());
		Organization role = organizationService.load(id);
		List<Organization> orgs = organizationService.list();
		role.setChildren("");
		role = 	organizationService.toUserorg(orgs, role);
		//当前单位id
		if(StringUtils.isNotBlank(role.getChildren())){
			role.setChildren(role.getChildren()+","+role.getPk());
		}else{
			role.setChildren(""+role.getPk());
		}
		Query<User> query  =newQuery(currentPage, pageSize, request);
	    userService.queryUsersByOrg(query,role);
	   
		return ModelAndViewBuilder.newInstanceFor("/organization/view")
				.append("role", role)
				.append("query", query)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}
	
	
	
	
	
}

