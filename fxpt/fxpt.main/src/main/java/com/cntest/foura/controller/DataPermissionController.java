/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cntest.common.controller.BaseController;
import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.DataPermission;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.DataPermissionService;
import com.cntest.foura.service.URLResourceService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.ExamType;
import com.cntest.fxpt.domain.Grade;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.web.view.JqueryZtree;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;


/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年12月3日
 * @version 1.0
 **/

@Controller
@RequestMapping("/permission/data")
public class DataPermissionController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(DataPermissionController.class);

	@Autowired
	private URLResourceService myResourceService;

	@Autowired
	private DataPermissionService dataPermissionService;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam Long pk, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("URL: /permission/data/list");
		List<DataPermission> permissions = dataPermissionService.getChildren(pk);

		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder
				.newInstanceFor("permission/dataList")
				.append("results", permissions)
				.append("title", title)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.permission.dataList").msg("4a.permission.data.list").build()).build();
	}

	@RequestMapping(value = "/detail/{pk}/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView detail(@PathVariable Long pk, @PathVariable int currentPage, @PathVariable int pageSize) throws Exception {
		logger.debug("URL: /permission/data/detail/{}",pk);

		List<DataAuthorized> permissions = dataPermissionService.getDetail(dataPermissionService.load(pk));
		return ModelAndViewBuilder
				.newInstanceFor("permission/dataDetail")
				.append("result", permissions)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.permission.dataList").msg("4a.permission.data.detail").build()).build();
	}

	/**
	 * 增加用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/view/{pk}", method = RequestMethod.GET)
	public ModelAndView view(@PathVariable Long pk) throws Exception {
		logger.debug("URL: permission/data/view/{}", pk);
		
		DataPermission dp = new DataPermission.Builder("").create();
		if (pk == -1)
			dp.setPk(pk);
		else
			dp = dataPermissionService.load(pk);
		return ModelAndViewBuilder.newInstanceFor("permission/dataEdit").append("dp", dp)
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("4a.permission.data.view").msg("数据权限信息").build()).build();
	}

	/**
	 * 增加用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView create(@RequestBody DataPermission dp) throws Exception {
		logger.debug("URL: /permission/data  method POST create {}", dp);
		String status = "失败",info = " ",erre="";
		try {
			dataPermissionService.create(dp);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="数据权限名称：<b style='color:red;'>"+dp.getName()+"</b>添加"+status+"</br>";
			LogUtil.log("资源管理>数据权限管理", "添加",dp.getName(),status,info,erre);
		}	
		return ModelAndViewBuilder.newInstanceFor("/user/edit").append("permission", dp)
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("4a.permission.data.create").msg("数据权限新增").build())
				.build();
	}

	/**
	 * 修改用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView update(@RequestBody DataPermission dp) throws Exception {
		logger.debug("URL: /permission/data  method PUT  update {}", dp);
		String status = "失败",info = " ",erre="";
		DataPermission permission = dataPermissionService.load(dp.getPk());
		try {
			info+= "</br>修改项</br>";
			info+= updatainfo(dp,permission);
			userService.evictSession(permission);
			dataPermissionService.update(dp);
	        status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("资源管理>数据权限管理", "修改",permission.getName(),status, " <b style='color:red;'>"+permission.getName()+"</b>修改"+status+"</br>"+info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("/user/edit").append("permission", dp)
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.update").msg("数据权限修改").build()).build();
	}

	public String updatainfo(DataPermission resource,DataPermission temp) {
		//resource:新来的数据，temp :之前的数据
		String info="";
		if(resource.getName()!=null && (!resource.getName().equals(temp.getName()))) {
			info+="名称：<b style='color:red;'>"+temp.getName()+"</b> 改  <b style='color:red;'>"+resource.getName()+"</b><br/>";
		}
		if(resource.getStatus()!=temp.getStatus()) {
			HashMap<Integer, String> span = new HashMap<Integer,String>();
			span.put(0, "禁用");
			span.put(1, "启用");
			info+="状态：<b style='color:red;'>"+span.get(temp.getStatus())+"</b> 改  <b style='color:red;'>"+span.get(resource.getStatus())+"</b><br/>";
		}
		if(resource.getParamName()!=null && (!resource.getParamName().equals(temp.getParamName()))) {
			info+="外部参数名称：<b style='color:red;'>"+temp.getParamName()+"</b> 改  <b style='color:red;'>"+resource.getParamName()+"</b><br/>";
		}
		if(resource.getTable()!=null && (!resource.getTable().equals(temp.getTable()))) {
			info+="数据权限来源表名称：<b style='color:red;'>"+temp.getTable()+"</b> 改  <b style='color:red;'>"+resource.getTable()+"</b><br/>";
		}
		if(resource.getParamKeyField()!=null && (!resource.getParamKeyField().equals(temp.getParamKeyField()))) {
			info+="数据权限值主健字段：<b style='color:red;'>"+temp.getParamKeyField()+"</b> 改  <b style='color:red;'>"+resource.getParamKeyField()+"</b><br/>";
		}
		if(resource.getParamNamefield()!=null && (!resource.getParamNamefield().equals(temp.getParamNamefield()))) {
			info+="权限名称字段：<b style='color:red;'>"+temp.getParamNamefield()+"</b> 改  <b style='color:red;'>"+resource.getParamNamefield()+"</b><br/>";
		}
		if(resource.getParamValueField()!=null && (!resource.getParamValueField().equals(temp.getParamValueField()))) {
			info+="权限值字段	：<b style='color:red;'>"+temp.getParamValueField()+"</b> 改  <b style='color:red;'>"+resource.getParamValueField()+"</b><br/>";
		}
		if(resource.getParentRefKey()!=null && (!resource.getParentRefKey().equals(temp.getParentRefKey()))) {
			info+="上级关联字段：<b style='color:red;'>"+temp.getParentRefKey()+"</b> 改  <b style='color:red;'>"+resource.getParentRefKey()+"</b><br/>";
		}
		if(resource.getSource()!=null && (!resource.getSource().equals(temp.getSource()))) {
			info+="数据取取值表达式：<b style='color:red;'>"+temp.getSource()+"</b> 改  <b style='color:red;'>"+resource.getSource()+"</b><br/>";
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}
	@RequestMapping(value = "/status/{status}", method = RequestMethod.PUT)
	public ModelAndView updateStatus(@PathVariable int status, @RequestBody DataPermission dp) throws Exception {
		logger.debug("URL: /permission/data  method PUT  updateStatus {}", dp);
		String statusd = "失败",info = " ",erre="",op ="启用";
		DataPermission dpn = dataPermissionService.load(dp.getPk());
		try {
			if(status==0) {
				op="禁用";	
			}
			dpn.setStatus(status);
			dataPermissionService.update(dpn);
			statusd = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="数据权限名称：<b style='color:red;'>"+dpn.getName()+"</b>"+op+statusd+"</br>";
			LogUtil.log("资源管理>数据权限管理", op,dpn.getName(),statusd,info,erre);
		}

		return ModelAndViewBuilder.newInstanceFor("/user/edit").append("pk", dp.getPk())
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.update").msg("数据权限修改").build()).build();
	}

	/**
	 * 删除用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody DataPermission dp) throws Exception {
		logger.debug("URL: /permission/data  method Delete  ");
		DataPermission dpn = dataPermissionService.load(dp.getPk());
		String status = "失败",info = " ",erre="",name ="";
		try {
			name =dpn.getName();
			dataPermissionService.remove(dpn);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info="数据权限名称：<b style='color:red;'>"+name+"</b>删除"+status+"</br>";
			LogUtil.log("资源管理>数据权限管理", "删除",name,status,info,erre);
		}
		return ModelAndViewBuilder
				.newInstanceFor("/user/list")
				// .append("hasUrlResource", hasUrlResource)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.permission.data.delete").msg("数据权限" + dpn.getName() + "删除成功").build())
				.build();
	}

	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public ModelAndView tree(@RequestParam Long pk, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("URL: /permission/data/list");
		  User user = User.from(userService.getCurrentLoginedUser());

		ArrayList<Map> treeNodes = new ArrayList<>();

		URLResource app = myResourceService.getUserDefaultApp(user);

		JqueryZtree root = new JqueryZtree().id(-1L).pId(-1L).isParent(true).name(app.getName()).open(true);
		List<DataPermission> permissions = dataPermissionService.getChildren(pk < 1 ? null : pk);
		for (DataPermission dp : permissions) {
			root.child(buildJqueryZtree(dp).pId(-1l));
		}

		treeNodes.add(root.getZtreeNode());
		return ModelAndViewBuilder.newInstanceFor("permission/dataTree").append("treeNodes", treeNodes)
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("4a.permission.dataList").msg(user.getName()).build())
				.build();
	}

	private JqueryZtree buildJqueryZtree(DataPermission dataPermission) {
		JqueryZtree ztree = new JqueryZtree().id(dataPermission.getPk()).name(dataPermission.getName()).open(false).isParent(false);
		List<DataPermission> permissions = dataPermissionService.getChildren(dataPermission.getPk());
		if (permissions != null && permissions.size() > 0) {
			ztree.isParent(true);
			for (DataPermission dp : permissions) {
				JqueryZtree child = buildJqueryZtree(dp);
				ztree.child(child);
			}
		}
		return ztree;
	}
}
