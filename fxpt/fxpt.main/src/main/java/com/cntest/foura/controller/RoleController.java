/**
 	* <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
import com.cntest.foura.application.permission.PermissionZtreeBuilder;
import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.Role;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.Ztree;
import com.cntest.foura.service.DataPermissionService;
import com.cntest.foura.service.RoleService;
import com.cntest.foura.service.URLResourceService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.SynUsers;
import com.cntest.fxpt.repository.ISynUsersDao;
import com.cntest.fxpt.service.ISynUsersService;
import com.cntest.fxpt.util.LogUtil;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.security.UserDetails;
import com.cntest.util.SpringContext;
import com.cntest.web.view.JqueryZtree;
import com.cntest.web.view.ModelAndViewBuilder;
import com.cntest.web.view.ResponseStatus;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 肖 肖 2014年6月18日
 * @version 1.0
 **/
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;
	@Autowired
	private ISynUsersService synUsersService;
	@Autowired
	private UserService userService;
	
	@Autowired
	private URLResourceService resourceService;
	
	@Autowired
	private DataPermissionService dataPermissionService;
	

	/**
	 * 获取角色列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView getRole(@PathVariable int currentPage, @PathVariable int pageSize,HttpServletRequest request) throws Exception {
		 logger.debug("URL: /role/list/{}/{}",currentPage,pageSize);
		 Query<Role> querylist  =newQuery(currentPage, pageSize, request);
         roleService.query(querylist);
         Query<Role> query=userService.findUserCountByRoleId(querylist);
         String title = TitleUtil.getTitle(request.getServletPath());
		 return ModelAndViewBuilder
				.newInstanceFor("/role/list")
				.append("query", query)
				.append("title",title)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.role.list")
								.msg("角色列表").build()).build();
	}

	/**
	 * 获取所有角色
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() throws Exception {
		List<Role> roles = roleService.list();
		return ModelAndViewBuilder.newInstanceFor("/role/list").append("roles", roles)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}

	/**
	 * 新增角色
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/newAdd", method = RequestMethod.GET)
	public ModelAndView newAdd() throws Exception {
		String code = UUID.randomUUID().toString();
		Role role = new Role();
		role.setCode(code);
		return ModelAndViewBuilder.newInstanceFor("/role/newAdd").append("role", role)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}

	/**
	 * 提交新增
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView subAdd(@RequestBody Role role) throws Exception {
		String status = "失败",info = "新增角色<b style='color:red;'>",erre="";
		try {
			roleService.create(role);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+=role.getName()+"</b>"+status;
			LogUtil.log("角色管理>角色列表", "新增",role.getName(),status, info,erre);
		}
		
		return ModelAndViewBuilder.newInstanceFor("")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}

	/**
	 * 
	 * 新增角色时，验证code的唯一性
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/validate/{code}/{roleId}", method = RequestMethod.POST)
	public ModelAndView validate(@PathVariable String code,@PathVariable String roleId) throws Exception {
		boolean flg = roleService.findRoleByCode(code,roleId);
		return ModelAndViewBuilder.newInstanceFor("").append("flg", flg)
				.build();
	}
	
	/**
	 * 修改角色
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update/{roleId}", method = RequestMethod.GET)
	public ModelAndView update(@PathVariable Long roleId) throws Exception {
		Role role = roleService.findRoleById(roleId);
		return ModelAndViewBuilder.newInstanceFor("/role/edit").append("role", role)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}

	/**
	 * 提交修改
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView subUpdate(@RequestBody Role role) throws Exception {
		for (URLResource urlResource : resourceService.findResourceAllFor(role.getPk())) {
			role.addResource(resourceService.load(urlResource.getPk()));
		}
		String status = "失败",info = " ",erre="";
		Role roles =  roleService.load(role.getPk());
		try {
			info+= "</br>修改项</br>";
			info+= updatainfo(role,roles);
			userService.evictSession(roles);
			roleService.update(role);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("角色管理>角色列表", "修改",role.getName(),status, "角色<b style='color:red;'>"+role.getName()+"</b>修改"+status+"</br>"+info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}
	
	public String updatainfo(Role resource,Role temp) {
		//resource:新来的数据，temp:之前的数据
		String info="";
		if(resource.getCode()!=null && (!resource.getCode().equals(temp.getCode()))) {
			info+=("角色代码：<b style='color:red;'>"+(temp.getCode()==null?"":temp.getCode())+"</b> 改  <b style='color:red;'>"+resource.getCode()+"</b><br/>");
		}
		if(resource.getName()!=null && (!resource.getName().equals(temp.getName()))) {
			info+=("角色名字：<b style='color:red;'>"+(temp.getName()==null?"":temp.getName())+"</b> 改  <b style='color:red;'>"+resource.getCode()+"</b><br/>");
		}
		if(resource.getDesc()!=null && (!resource.getDesc().equals(temp.getDesc()))) {
			info+=("角色描述：<b style='color:red;'>"+(temp.getDesc()==null?"":temp.getDesc())+"</b> 改  <b style='color:red;'>"+resource.getDesc()+"</b><br/>");
		}
		if(resource.getAvailable()!= temp.getAvailable()) {
			Map<String ,String> span = new HashMap<String,String>();
			span.put("true","有效");
			span.put("false","有效");
			info+=("状态:<b style='color:red;'>"+(span.get(temp.getAvailable()+"")) +"</b> 改  <b style='color:red;'>"+(span.get(resource.getAvailable()+""))+"</b><br/>");
		}
		if(info.length()==0) {
			info="没有修改项";
		}
		return info;
	}

	/**
	 * 查看角色
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/view/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView view(@PathVariable int currentPage,@PathVariable int pageSize,ServletRequest request) throws Exception {
		Long id=Long.valueOf((request.getParameterMap().get("roleId")[0]).toString());
		Role role = roleService.findRoleById(id);
		role.setResources(null);
		role.setUsers(null);
		Query<User> query  =newQuery(currentPage, pageSize, request);
	    userService.queryUsersByRoleId(query);
	    for(User user: query.getResults()) {
        	user.setResources(null);
        }
		return ModelAndViewBuilder.newInstanceFor("/role/view")
				.append("role", role)
				.append("query", query)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}
	
	/**
	 * 查看用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public ModelAndView manage(ServletRequest request) throws Exception {
		Long id=Long.valueOf((request.getParameterMap().get("roleId")[0]).toString());
		Role role = roleService.findRoleById(id);
		role.setResources(null);
		role.setUsers(null);
		//获取已选择的用户
		Query<User> query = newQuery(1, 1000000, request);
	    userService.queryUsersByRoleId(query);
	    for(User user: query.getResults()) {
        	user.setResources(null);
        }
		//获取未选择的用户
		List<User> userList =userService.list(id);
		return ModelAndViewBuilder.newInstanceFor("/role/roleManage")
				.append("role", role)
				.append("query", query)
				.append("userList", userList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}
	
	/**
	 * 成员管理
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userManage/{pk}", method = RequestMethod.POST)
	public ModelAndView userManage(@PathVariable String pk,@RequestBody Map<String,String> map , HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		String status = "失败",info = " ",erre="";
		Role ro = roleService.findRoleById(Long.parseLong(pk));
		try {
			String roleId = pk;
			String addUserIds = map.get("addUserIds");
			String deleteUserIds=map.get("deleteUserIds");
			roleService.updateRoleAndResource(roleId,deleteUserIds,addUserIds);
			
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info = "角色<b style='color:red;'>"+ro.getName()+"</b>成员操作成功";
			LogUtil.log("角色管理", "成员管理", ro.getName(),status, info,erre);
		}
		return ModelAndViewBuilder.newInstanceFor("")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}

	/**
	 * 删除角色
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( method = RequestMethod.DELETE)
	public ModelAndView delete(@RequestBody Role role,ServletRequest request) throws Exception {
		String count =request.getParameter("count");
		role = roleService.findRoleById(role.getPk());
		//boolean hasUrlResource = role.getResources().size() > 0 ? true : false;
		String status = "失败",info = "角色<b style='color:red;'>",erre="";
		
		try {
			synUsersService.deleteSynByRoleId(role.getPk());
			roleService.deleteRoleAndNexus(role,count);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+=role.getName()+"</b>"+status;
			LogUtil.log("角色管理>角色列表", "删除",role.getName(),status, info,erre);
		}
		// if (!hasUrlResource) {
		// roleService.remove(role);
		// }else {
		// throw new BusinessException("cntext.4a.rolehasres","该角色存在权限项，无法删除");
		// }
		return ModelAndViewBuilder.newInstanceFor("/role/list")
				.append(ResponseStatus.NAME,new ResponseStatus.Builder(Boolean.TRUE).code("4a.role.delete")
				.msg("角色"+role.getName()+"删除成功！").build()).build();
	}

	/**
	 * 授权单个确认
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/subGrant", method = RequestMethod.POST)
	public ModelAndView subGrant(HttpServletRequest request, HttpServletResponse response, @RequestBody URLResource[] urlResources)
			throws Exception {
		String status = "失败",info = " ",erre="",name="";
		try {
			Role role = roleService.load(Long.valueOf(request.getParameter("roleId")));
			name = role.getName();
			roleService.updateRole(role,urlResources);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			LogUtil.log("角色管理>用户列表", "授权",name,status, "角色<b style='color:red;'>"+name+"</b>授权操作"+status+"</br>"+info,erre);
		}
		
		return ModelAndViewBuilder.newInstanceFor("")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}
	
	@RequestMapping(value = "/dataAuthorized/{roleId}", method = RequestMethod.POST)
	public ModelAndView subDataPermission(@PathVariable Long roleId, @RequestBody DataAuthorized[] dataAuthorizeds,HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//Role role = roleService.load(Long.valueOf(request.getParameter("roleId")));
		Role role = roleService.load(roleId);		
		roleService.updateRoleDataAuthorized(role,dataAuthorizeds);
		return ModelAndViewBuilder.newInstanceFor("")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}

	/**
	 * 授权批量确认
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/subBatchGrant", method = RequestMethod.POST)
	public ModelAndView subBatchGrant(HttpServletRequest request, HttpServletResponse response, @RequestBody URLResource[] urlResources)
			throws Exception {
		String status = "失败",name="",info = "授权批量确认<b style='color:red;'>",erre="";
		try {
			String roleIds = request.getParameter("idList");
			String[] idList = roleIds.split(",");
			for (String idStr : idList) {
				Role role = roleService.load(Long.valueOf(idStr));
				name+=role.getName()+",";
				roleService.updateRole(role,urlResources);
			}
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		}finally {
			info+=name+"</b>"+status;
			LogUtil.log("角色管理>授权批量", "授权批量",name,status, info,erre);
		}
		

		return ModelAndViewBuilder.newInstanceFor("")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}


	
	/**
	 * 根据角色获取权限项
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/permission/{roleId}", method = RequestMethod.GET)
	public ModelAndView permission(HttpServletRequest request, HttpServletResponse response, @PathVariable Long roleId) throws Exception {
		
		Subject subject = SecurityUtils.getSubject();
		UserDetails userDetail = null;
		if(subject.getPrincipal() instanceof UserDetails) {
			userDetail = (UserDetails) subject.getPrincipal();
			
		} else if(subject.getPrincipal() instanceof String) {
			String username  = (String) subject.getPrincipal();
			User user = userService.findUserBy(username);
			userDetail = user.toUserDetails();
		}
		
		User user = User.currentUser(userDetail);		
		
		URLResource app = resourceService.getUserDefaultApp(user);
		
		PermissionZtreeBuilder ztreeBuilder = SpringContext.getBean(PermissionZtreeBuilder.class);
		List<URLResource> roleMenus = resourceService.findResourceAllFor(roleId);
		JqueryZtree root = ztreeBuilder.build(app, roleMenus,PermissionZtreeBuilder.PermissionType.role, roleId);
		List treeNodes = root.getZtreeNodes();
		return ModelAndViewBuilder.newInstanceFor("").append("treeNodes", treeNodes)
				.append("roleId",roleId)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}

	/**
	 * 单个授权
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/grant/{roleId}", method = RequestMethod.GET)
	public ModelAndView grant(@PathVariable Long roleId,HttpServletRequest request) throws Exception {
		Role role = roleService.load(roleId);
		
		return ModelAndViewBuilder.newInstanceFor("/role/grant").append("role", role)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}

	/**
	 * 批量授权
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/batchGrant", method = RequestMethod.GET)
	public ModelAndView batchGrant() throws Exception {
		List<Role> roles = roleService.list();
		return ModelAndViewBuilder.newInstanceFor("/role/grant").append("roles", roles)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}

	/**
	 * 确认查找
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search(ServletRequest request) throws Exception {
		Map<String, String[]> parameters=request.getParameterMap();
		List<Role> roles=new ArrayList<Role>();
		if(parameters.get("q")!= null) {
			String q =URLDecoder.decode(parameters.get("q")[0]) ;
			
			roles = roleService.findRolesByName(q);
		}else {
			roles = roleService.list();
		}
        for(Role role: roles) {
        	role.setResources(null);
        	role.setUsers(null);
        }
		return ModelAndViewBuilder.newInstanceFor("/role/roleTbody").append("roles", roles)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}

	/**
	 * 获取用户可分配的权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/grantList", method = RequestMethod.GET)
	public ModelAndView treeNodeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List ztreeList = new ArrayList();
		List<URLResource> allResList = resourceService.findResourceAllFor();
		for (URLResource urlResource : allResList) {
			Ztree ztree=new Ztree(String.valueOf(urlResource.getPk()), 
					urlResource.getName(), false, 
					urlResource.getParent()==null?"":String.valueOf(urlResource.getParent().getPk()));
			HashMap treeMap=ztree.getZtree();
			ztreeList.add(treeMap);
		}
		return ModelAndViewBuilder.newInstanceFor("").append("ztree",ztreeList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list")
								.msg("用户列表").build()).build();
	}
	

}
