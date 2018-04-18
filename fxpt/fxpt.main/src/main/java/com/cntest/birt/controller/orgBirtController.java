/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.birt.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.birt.domain.OrgBirt;
import com.cntest.birt.domain.ReportScript;
import com.cntest.birt.service.orgBirtService;
import com.cntest.common.controller.BaseController;
import com.cntest.common.query.Query;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;
import com.cntest.fxpt.util.SystemConfig;
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
@RequestMapping("/orgBirt")
public class orgBirtController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(orgBirtController.class);
	@Autowired
	private orgBirtService orgBirtService;
	
	@RequestMapping(value = "/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView findAll(@PathVariable int currentPage, @PathVariable int pageSize, ServletRequest request) throws BusinessException {
		Query<OrgBirt> query = newQuery(currentPage, pageSize, request);
		List<OrgBirt> orgList = orgBirtService.getTopOrgList(null);
		orgBirtService.querys(query);
		return ModelAndViewBuilder.newInstanceFor("/birtorg/list").append("query", query).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("4a.userBelong.list").msg("脚本").build()).build();
	}
	
	@RequestMapping(value="/tree/list/{level}",method = RequestMethod.GET)
	public ModelAndView list(@PathVariable int level,HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.info("treeNodeList {} 异步请求树节点数据");
        logger.info("treeNodeList {} select * From");

        List<OrgBirt> orgList = orgBirtService.getTopOrgList(null);       
        JsonArray jsonArray  = buildTreeData(orgList,level,0);        
        logger.info("treeNodeList {}",jsonArray.toString());
		return ModelAndViewBuilder
				.newInstanceFor("")
				.append("orgJson",jsonArray.toString())
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.tree.list")
						.msg("success").build()).build();
		
	}
	/**
	 * 合并展示的菜单
	 * @param level
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/tree/lists/{level}",method = RequestMethod.GET)
	public ModelAndView lists(@PathVariable int level,HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.info("treeNodeList {} 异步请求树节点数据");
        logger.info("treeNodeList {} select * From");

        List<OrgBirt> orgList = orgBirtService.getTopOrgList(null);       
        JsonArray jsonArray  = buildTreeDatas(orgList,level,0);        
        logger.info("treeNodeList {}",jsonArray.toString());
		return ModelAndViewBuilder
				.newInstanceFor("")
				.append("orgJson",jsonArray.toString())
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.tree.list")
						.msg("success").build()).build();
		
	}
	
	@RequestMapping(value="/tree/children",method = RequestMethod.GET)
	public ModelAndView childNodelist(HttpServletRequest request,HttpServletResponse response) throws Exception{;
		logger.info("childNodelist {} 异步请求树节点数据");
		logger.info("childNodelist {} select * From");
		String pk = request.getParameter("pk");
		List<OrgBirt> orgList = orgBirtService.getOrgSubList(Long.valueOf(pk));
		JsonArray jsonArray  = buildTreeData(orgList,0,0);
		logger.info("childNodelist {}",jsonArray.toString());

		return ModelAndViewBuilder
				.newInstanceFor("")
				.append("orgJson",jsonArray.toString())
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.tree.children")
						.msg("success").build()).build();
	}
	@RequestMapping(value="/tree/children/rep",method = RequestMethod.GET)
	public ModelAndView childNodelists(HttpServletRequest request,HttpServletResponse response) throws Exception{;
		logger.info("childNodelist {} 异步请求树节点数据");
		logger.info("childNodelist {} select * From");
		String pk = request.getParameter("pk");
		List<ReportScript> orgList = orgBirtService.getOrgSubList(Long.valueOf(pk),"");
		JsonArray jsonArray  = buildTree(orgList,0,0);
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
		String pk 		= request.getParameter("pk");
		String isParent = request.getParameter("isParent");
		String qname = request.getParameter("qname");
		List<ReportScript> orgtmp = new ArrayList<ReportScript>();
		if(pk.equals("-1")) {
			orgtmp = orgBirtService.getOrgSubList(null,qname);
		}else {
			if(isParent.equals("true")) {
				orgtmp = orgBirtService.getOrgSubList(Long.valueOf(pk),qname);
			}else {
				orgtmp = orgBirtService.getOrgSubList(Long.valueOf(pk),qname);
			}
		}
		
		
		return ModelAndViewBuilder
				.newInstanceFor("birt/subList")
				.append("orgList", orgtmp)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.forChildMenu")
						.msg("success").build()).build();
	}
	@RequestMapping(value="/subList/org",method = RequestMethod.GET)
	public ModelAndView forChildMenuOrg(HttpServletRequest request) throws Exception{;
		logger.info("forChildMenu {} 异步请求子列表数据");
		logger.info("forChildMenu {} select * From");
		String pk 		= request.getParameter("pk");
		String isParent = request.getParameter("isParent");
		String qname = request.getParameter("qname");
		List<OrgBirt> orgtmp = new ArrayList<OrgBirt>();
		if(pk.equals("-1")) {
			orgtmp = orgBirtService.getOrgSubList(null);
		}else {
			if(isParent.equals("true")) {
				orgtmp = orgBirtService.getOrgSubList(Long.valueOf(pk));
			}else {
				orgtmp = orgBirtService.getOrgSubList(Long.valueOf(pk));
			}
		}
		
		
		return ModelAndViewBuilder
				.newInstanceFor("birt/subList")
				.append("orgList", orgtmp)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.forChildMenu")
						.msg("success").build()).build();
	}
	@RequestMapping(value="/subList/{currentPage}/{pageSize}",method = RequestMethod.GET)
	public ModelAndView forChildMenu(@PathVariable int currentPage,@PathVariable int pageSize,HttpServletRequest request) throws Exception{;
		logger.info("forChildMenu {} 异步请求子列表数据");
		logger.info("forChildMenu {} select * From");
		String pk 		= request.getParameter("pk");

		Query<ReportScript> query  =newQuery(currentPage, pageSize, request);
		if(pk.equals("-1")) {
			orgBirtService.getOrgSubList(null,query);
		}else {			
			orgBirtService.getOrgSubList(Long.valueOf(pk),query);			
		}
		
		return ModelAndViewBuilder
				.newInstanceFor("birt/subList")
				.append("orgList", query.getResults())
				.append("query",query)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.forChildMenu")
						.msg("学校列表").build()).build();
	}
	@RequestMapping(value="/subList/org/{currentPage}/{pageSize}",method = RequestMethod.GET)
	public ModelAndView forChildOrg(@PathVariable int currentPage,@PathVariable int pageSize,HttpServletRequest request) throws Exception{;
		logger.info("forChildMenu {} 异步请求子列表数据");
		logger.info("forChildMenu {} select * From");
		String pk 		= request.getParameter("pk");

		Query<OrgBirt> query  =newQuery(currentPage, pageSize, request);
		if(pk.equals("-1")) {
			orgBirtService.getOrgSubLists(null,query);	
		}else {			
			orgBirtService.getOrgSubLists(Long.valueOf(pk),query);			
		}
		
		return ModelAndViewBuilder
				.newInstanceFor("birtorg/subList")
				.append("orgList", query.getResults())
				.append("query",query)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.forChildMenu")
						.msg("学校列表").build()).build();
	}
	//pk  pk值是用来展开树形结构节点的判断（连接下级列表时用到）
	private JsonArray buildTreeData(List<OrgBirt> orgList, int level, int i) {
		JsonArray jsonArray = new JsonArray();
		for (OrgBirt org : orgList) {
        	JsonObject object = new JsonObject();
        	logger.debug("PK:{}",org.getPk());
        	ArrayList<OrgBirt> children = new ArrayList<OrgBirt>();
        	
        	children.addAll(orgBirtService.getOrgChildrenNoLeaf(org.getPk()));
        	
        	
        	object = new ZTree().id(org.getPk())
        							   .pId(org.getParent()!=null?org.getParent().getPk():-1L)
        							   .open(i<level).isParent(children.size() > 0).expandDIY("type", org.getName())
        							   .zTreeJsonObj(org);
        	object.remove("url");
        	if(i < level) {
        		jsonArray.addAll(buildTreeData(children,level,i + 1));
        	}
        	jsonArray.add(object);
		}
		return jsonArray;
	}
	//pk  pk值是用来展开树形结构节点的判断（连接下级列表时用到）
		private JsonArray buildTreeDatas(List<OrgBirt> orgList, int level, int i) {
			JsonArray jsonArray = new JsonArray();
			for (OrgBirt org : orgList) {
	        	JsonObject object = new JsonObject();
	        	logger.debug("PK:{}",org.getPk());
	        	ArrayList<ReportScript> children = new ArrayList<ReportScript>();
	        	
	        	children.addAll(orgBirtService.getOrgSubList(org.getPk(),""));
	        	
	        	
	        	object = new ZTree().id(org.getPk())
	        							   .pId(org.getParent()!=null?org.getParent().getPk():-1L)
	        							   .open(i<level).isParent(children.size() > 0).expandDIY("type", org.getName())
	        							   .zTreeJsonObj(org);
	        	object.remove("url");
	        	if(i < level) {
	        		jsonArray.addAll(buildTree(children,level,i + 1));
	        	}
	        	jsonArray.add(object);
			}
			return jsonArray;
		}
		private JsonArray buildTree(List<ReportScript> report, int level, int i) {
			JsonArray jsonArray = new JsonArray();
			for (ReportScript org : report) {
	        	JsonObject object = new JsonObject();
	        	logger.debug("PK:{}",org.getPk());
	        	ArrayList<ReportScript> children = new ArrayList<ReportScript>();
	        	
	        	children.addAll(orgBirtService.getOrgSubList(org.getPk(),""));
	        	
	        	
	        	object = new ZTree().id(org.getPk())
	        							   .pId(org.getOrgBirt()!=null?org.getOrgBirt().getPk():-1L)
	        							   .open(i<level).isParent(children.size() > 0).expandDIY("type", org.getName())
	        							   .zTreeJsonObj(org);
	        	object.remove("url");
	        
	        	jsonArray.add(object);
			}
			return jsonArray;
		}
	/**
	 *增加修改组织
	 * @param parentPk
	 * @param pk
	 * @param methodType
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/view/{pk}",method = RequestMethod.GET)
	public ModelAndView view(@PathVariable Long pk, HttpServletRequest request) throws Exception{
		logger.info("view {} pk = {}",Organization.class,pk);
		
		OrgBirt	orgBirt =  	orgBirtService.load(pk);
		return ModelAndViewBuilder
				.newInstanceFor("birt/edit")
				.append("org", orgBirt)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.view")
						.msg("").build()).build();
		
	}
	/**
	 * 增加组织
	 * 
	 * @param reportScript
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView add(@RequestBody OrgBirt orgBirt,HttpServletRequest request) throws BusinessException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		orgBirtService.create(orgBirt);
		Query<OrgBirt> query  =newQuery(1, 15, request);
		if(orgBirt.getParent()!=null){
			orgBirtService.getOrgSubLists(Long.valueOf(orgBirt.getParent().getPk()),query);	
		}
		else{
			orgBirtService.getOrgSubLists(null,query);
		}
		return ModelAndViewBuilder.newInstanceFor("birtorg/subList")
				.append("orgList", query.getResults())
				.append("query", query)
			
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("4a.permission.dataList")
						.msg("4a.permission.data.list").build())
				.build();
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody OrgBirt orgBirt,HttpServletRequest request) throws Exception{
		logger.info("update {}",Organization.class);
		
		orgBirtService.update(orgBirt);
		Query<OrgBirt> query  =newQuery(1, 15, request);
		if(orgBirt.getParent()!=null){
			orgBirtService.getOrgSubLists(Long.valueOf(orgBirt.getParent().getPk()),query);	
		}
		else{
			orgBirtService.getOrgSubLists(null,query);
		}
		
		return ModelAndViewBuilder
				.newInstanceFor("birtorg/subList")
				.append("orgList", query.getResults())
				.append("query", query)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.update")
						.msg("修改成功").build()).build();
		
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
				List<ReportScript> reports =	 orgBirtService.getOrgSubList(Long.valueOf(pk),"");
				if(reports.size()>0){
					flg=false;
					mes="该组织下还存在脚本";
				}
			}
		}else{
			flg=true;
		}
		return ModelAndViewBuilder.newInstanceFor("").append("flg", flg).append("mes", mes)
				.build();
	}
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody OrgBirt orgBirt,HttpServletRequest request) throws Exception{
		logger.info(" pk = {}  delete {}",orgBirt.getPk(),Organization.class);
		//org = organizationService.load(org.getPk());
		//organizationService.remove(org);
		//删除组织及其下级所有组织
		orgBirtService.removeAll(orgBirt);
		Query<OrgBirt> query  =newQuery(1, 15, request);
		if(orgBirt.getParent()!=null){
			orgBirtService.getOrgSubLists(null,query);	
		}
		else{
			orgBirtService.getOrgSubLists(null,query);
		}
		return ModelAndViewBuilder
				.newInstanceFor("/organization/subList")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.organization.delete")
						.msg("组织"+orgBirt.getName()+"删除成功").build()).build();
		
	}

	
	
	
}

