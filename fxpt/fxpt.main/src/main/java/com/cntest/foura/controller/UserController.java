/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
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
import com.cntest.common.page.Page;
import com.cntest.common.query.Query;
import com.cntest.foura.application.permission.PermissionZtreeBuilder;
import com.cntest.foura.application.permission.UserRealm;
import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.Role;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserBelong;
import com.cntest.foura.domain.UserResource;
import com.cntest.foura.domain.Ztree;
import com.cntest.foura.service.DataPermissionService;
import com.cntest.foura.service.OrganizationService;
import com.cntest.foura.service.RoleService;
import com.cntest.foura.service.URLResourceService;
import com.cntest.foura.service.UserBelongService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Subject;
import com.cntest.fxpt.service.IExamService;
import com.cntest.fxpt.service.ISubjectService;
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
 * @author 李贵庆2014年6月23日
 * @version 1.0
 **/
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private IExamService examService;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private UserBelongService belongService;

	@Autowired
	private URLResourceService resourceService;

	@Autowired
	private DataPermissionService dataPermissionService;

	@Autowired
	private ISubjectService subjectService;

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/list/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView list(@PathVariable int currentPage, @PathVariable int pageSize, HttpServletRequest request)
			throws Exception {
		logger.debug("URL: /user/list/{}/{}", currentPage, pageSize);
		UserDetails userDetails = userService.getCurrentLoginedUser();
		User currentUser = User.from(userDetails);
		Query<User> query = newQuery(currentPage, pageSize, request);
		userService.query(query);
		for (User user : query.getResults()) {
			user.setResources(null);
		}

		String title = TitleUtil.getTitle(request.getServletPath());
		return ModelAndViewBuilder.newInstanceFor("/user/list").append("query", query)
				.append("currentUser", currentUser).append("title", title).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list").msg("用户列表").build())
				.build();
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
		// logger.debug("URL: /user method POST create {}",user);
		User user = new User.Builder(null).status(1).create();
		List<Subject> subjects = subjectService.list();
		user.setPk(-1l);
		List<UserBelong> ubelongs = new ArrayList<UserBelong>();
		List<DataAuthorized> dataAuts = null;
		String dataAutStr = "";
		if (pk > 0) {
			user = userService.load(pk);
			ubelongs = belongService.findBelongFor(user);
			dataAuts = dataPermissionService.findDataAuthorizeds("user", user.getPk());
			dataAutStr = dataAuts.size() > 0 ? dataAuts.toString().substring(1, dataAuts.toString().length() - 1) : "";
			for (int i = 0; i < ubelongs.size(); i++) {
				Long num = ubelongs.get(i).getOrg().getPk();
				if (!organizationService.findOrgByOrgId(num)) {
					belongService.deleteBelongUser(ubelongs.get(i).getPk());
					;
				}
			}
			ubelongs = belongService.findBelongFor(user);
		}
		return ModelAndViewBuilder.newInstanceFor("/user/edit").append("dataAutStr", dataAutStr)
				.append("subjects", subjects).append("size", subjects.size()).append("user", user)
				.append("ubelongs", ubelongs).append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.view").msg("用户信息").build())
				.build();
	}

	/**
	 * 验证用户名
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "validata", method = RequestMethod.POST)
	public ModelAndView validata(@RequestBody User user) throws Exception {
		Boolean is = false;
		User other;
		if (user.getPk() != null) {
			is = true;
		} else {
			is = (other = userService.findUserBy(user.getName())) == null;
		}
		return ModelAndViewBuilder.newInstanceFor("").append("is", is).build();
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
	public ModelAndView create(@RequestBody User user) throws Exception {
		logger.debug("URL: /user  method POST create {}", user);
		String status = "失败", info = "新增用户<b style='color:red;'>", erre = "";
		User other = userService.findUserBy(user.getName());
		if (other != null) {
			return ModelAndViewBuilder.newInstanceFor("").append("mag", "用户名已存在").build();
		}

		try {
			user.setIsup("1");
			userService.create(user);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		} finally {
			info += user.getName() + "</b>" + status;
			LogUtil.log("用户管理>用户列表", "新增", user.getName(), status, info, erre);
		}

		return ModelAndViewBuilder.newInstanceFor("/user/edit").append("pk", user.getPk()).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.create").msg("用户列表").build()).build();
	}

	/**
	 * 同步用户
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/synchronous", method = RequestMethod.POST)
	public ModelAndView synchronous() throws Exception {
		logger.debug("URL: /user/synchronous  method POST ");

		String status = "失败", info = "同步用户", erre = "";
		int count = 0;
		try {
			count = userService.synchronous();
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		} finally {
			LogUtil.log("用户管理>用户列表", "同步用户到Pentaho",
					"<div style='width:100%; max-height:60px; overflow-y:auto; overflow-x:auto;'>" + count + "</div>条",
					status, info, erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").build();
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
	public ModelAndView update(@RequestBody User user) throws Exception {
		logger.debug("URL: /user  method PUT  update {}", user);
		String status = "失败", info = " ", erre = "";
		User u = userService.load(user.getPk());
		try {
			info += "</br>修改项</br>";
			info += updatainfo(user, u);
			userService.evictSession(u);
			userService.update(user);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		} finally {
			LogUtil.log("用户管理>用户列表", "修改", user.getName(), status,
					"用户<b style='color:red;'>" + user.getName() + "</b>修改" + status + "</br>" + info, erre);
		}
		return ModelAndViewBuilder.newInstanceFor("/user/edit").append("pk", user.getPk()).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.update").msg("用户列表").build()).build();
	}

	public String updatainfo(User resource, User temp) {
		// resource:新来的数据，temp:之前的数据

		String info = "";
		if (resource.getUserInfo().getNickName() != null
				&& (!resource.getUserInfo().getNickName().equals(temp.getUserInfo().getNickName()))) {
			info += ("用户名字：<b style='color:red;'>"
					+ (temp.getUserInfo().getNickName() == null ? "" : temp.getUserInfo().getNickName())
					+ "</b> 改  <b style='color:red;'>" + resource.getUserInfo().getNickName() + "</b><br/>");
		}

		if (resource.getUserInfo().getTelphone() != null
				&& (!resource.getUserInfo().getTelphone().equals(temp.getUserInfo().getTelphone()))) {
			info += ("固定电话:<b style='color:red;'>"
					+ (temp.getUserInfo().getTelphone() == null ? "" : temp.getUserInfo().getTelphone())
					+ "</b> 改  <b style='color:red;'>" + resource.getUserInfo().getTelphone() + "</b><br/>");
		}

		if (resource.getUserInfo().getCellphone() != null
				&& (!resource.getUserInfo().getCellphone().equals(temp.getUserInfo().getCellphone()))) {
			info += ("手机:<b style='color:red;'>"
					+ (temp.getUserInfo().getCellphone() == null ? "" : temp.getUserInfo().getCellphone())
					+ "</b> 改  <b style='color:red;'>" + resource.getUserInfo().getCellphone() + "</b><br/>");
		}

		if (resource.getPassword() != null && (!resource.getPassword().equals(temp.getPassword()))) {
			info += ("密码已被修改<br/>");
		}

		if (resource.getStatus() != temp.getStatus()) {
			Map<String, String> span = new HashMap<String, String>();
			span.put(null, "锁定 ");
			span.put("1", "启用");
			span.put("2", "禁用");
			span.put("3", "锁定 ");
			info += ("状态:<b style='color:red;'>" + span.get(temp.getStatus() + "") + "</b> 改  <b style='color:red;'>"
					+ span.get(resource.getStatus() + "") + "</b><br/>");
		}
		if (resource.getUserInfo().getSex() != null
				&& (!resource.getUserInfo().getSex().equals(temp.getUserInfo().getSex()))) {
			Map<String, String> span = new HashMap<String, String>();
			span.put("", "未知 ");
			span.put("FEMALE", "男");
			span.put("MALE", "女");
			span.put("UNKNOW", "未知 ");
			info += ("性别:<b style='color:red;'>"
					+ span.get((temp.getUserInfo().getSex() == null ? "" : temp.getUserInfo().getSex()) + "")
					+ "</b> 改  <b style='color:red;'>" + span.get(resource.getUserInfo().getSex() + "") + "</b><br/>");
		}

		if (resource.getUserInfo().getEmail() != null
				&& (!resource.getUserInfo().getEmail().equals(temp.getUserInfo().getEmail()))) {
			info += ("邮箱:<b style='color:red;'>"
					+ (temp.getUserInfo().getEmail() == null ? "" : temp.getUserInfo().getEmail())
					+ "</b> 改  <b style='color:red;'>" + resource.getUserInfo().getEmail() + "</b><br/>");
		}

		if (info.length() == 0) {
			info = "没有修改项";
		}
		return info;
	}

	/**
	 * 
	 * 新增角色时，验证code的唯一性
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/validate/{pk}", method = RequestMethod.POST)
	public ModelAndView validate(@PathVariable String pk) throws Exception {
		String flg = userService.initUserHasMessage(pk);
		return ModelAndViewBuilder.newInstanceFor("").append("flg", flg).build();
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
	public ModelAndView delete(@RequestBody User user) throws Exception {
		logger.debug("URL: /user  method Delete  ");
		user = userService.load(user.getPk());
		// boolean hasUrlResource = user.getResources().size() > 0
		// || user.getRoles().size() > 0 ? true : false;
		// if (!hasUrlResource) {
		// 删除用户和关联信息（用户，角色关联，权限关联，考试关联）
		String status = "失败", info = "删除<b style='color:red;'>", erre = "";
		try {
			userService.gotoMoveUserAndMessage(user);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		} finally {
			info += user.getName() + "</b>" + status;
			LogUtil.log("用户管理>用户列表", "删除", user.getName(), status, info, erre);
		}

		// } else {
		// throw new UserHasRefrencetException();
		// }
		return ModelAndViewBuilder.newInstanceFor("/user/list")
				// .append("hasUrlResource", hasUrlResource)
				.append(ResponseStatus.NAME, new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.delete")
						.msg("用户" + user.getName() + "删除成功").build())
				.build();
	}

	@RequestMapping(value = "/moreDelete/{pks}", method = RequestMethod.POST)
	public ModelAndView moreDelete(@PathVariable String pks) throws Exception {
		logger.debug("URL: /user/moreDelete  method GET  pk {}", pks);

		String status = "失败", info = "删除<b style='color:red;'>", erre = "";
		List<String> listName = new ArrayList<String>();
		String[] ids = pks.split(",");
		try {
			for (int i = 0; i < ids.length; i++) {
				User user = userService.load(Long.valueOf(ids[i]));
				listName.add(user.getName());
			}
			userService.delteMoreUser(pks);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		} finally {
			info += StringUtils.join(listName, "</br>") + "</b></br>" + status;
			LogUtil.log("用户管理>用户列表", "批量删除", listName.size() + "位用户被操作", status, "</br>" + info, erre);
		}
		return ModelAndViewBuilder.newInstanceFor("").build();
	}

	/**
	 * 修改用户密码界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getpwd/{pk}", method = RequestMethod.GET)
	public ModelAndView getPassword(@PathVariable Long pk) throws Exception {
		logger.debug("URL: /user/getpwd  method GET  pk {}", pk);
		User user = userService.load(pk);
		return ModelAndViewBuilder.newInstanceFor("/user/updatePassword").append("user", user)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.getPassword").msg("用户密码").build())
				.build();
	}

	/**
	 * 修改用户密码界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getpwds/{pk}", method = RequestMethod.GET)
	public ModelAndView getPasswords(@PathVariable Long pk) throws Exception {
		logger.debug("URL: /user/getpwd  method GET  pk {}", pk);
		User user = userService.load(pk);
		return ModelAndViewBuilder.newInstanceFor("/user/updatePasswords").append("user", user)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.getPassword").msg("用户密码").build())
				.build();
	}

	/**
	 * 修改用户密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updatepwd", method = RequestMethod.PUT)
	public ModelAndView updatePassword(@RequestBody User user) throws Exception {
		logger.debug("URL: /user  method PUT  update {}", user);

		String status = "失败", info = "用户<b style='color:red;'>", erre = "";
		try {

			String newPassword = user.getPassword();
			user = userService.load(user.getPk());
			userService.updatePassword(user, newPassword);
			UserRealm userRealm = SpringContext.getBean("userRealm");
			if (userRealm.getAuthenticationCache() != null) {
				userRealm.getAuthenticationCache().remove(user.getName());
			}

			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		} finally {
			info += user.getName() + "</b>密码修改" + status;
			LogUtil.log("用户管理>用户列表", "修改密码", user.getName(), status, info, erre);
		}

		return ModelAndViewBuilder.newInstanceFor("/user/edit")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.create").msg("修改用户密码成功").build())
				.build();
	}

	/**
	 * 修改用户密码界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/view/role/{pk}", method = RequestMethod.GET)
	public ModelAndView viewRole(@PathVariable Long pk, HttpServletRequest request) throws Exception {
		logger.debug("URL: /user/view  method GET  pk {}", pk);

		User user = userService.load(pk);
		return ModelAndViewBuilder.newInstanceFor("/user/roles").append("user", user).append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.getrols").msg("用户角色").build()).build();
	}

	/**
	 * 修改用户密码界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/view/role/tree/{userPk}", method = RequestMethod.GET)
	public ModelAndView viewRoleTree(HttpServletRequest request, HttpServletResponse response,
			@PathVariable Long userPk) throws Exception {
		logger.debug("URL: /user/view/role/tree  method GET  ");
		List<Role> roles = roleService.list();
		User user = null;
		if (userPk > 0)
			user = userService.load(userPk);
		else
			user = new User();

		JqueryZtree root = new JqueryZtree().isParent(true).name("系统角色").open(true);

		List ztreeList = new ArrayList();
		for (Role role : roles) {
			JqueryZtree ztree = new JqueryZtree().id(role.getPk()).name(role.getName()).open(false);
			if (user.hasRole(role)) {
				ztree.checked(true);
			}
			root.child(ztree);
		}

		ArrayList treeNodes = new ArrayList();
		treeNodes.add(root.getZtreeNode(false));
		return ModelAndViewBuilder.newInstanceFor("").append("ztree", treeNodes)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.role.tree").msg("获取用户角色").build())
				.build();
	}

	/**
	 * 修改用户角色
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateroles/{pk}", method = RequestMethod.PUT)
	public ModelAndView updateRoles(@PathVariable Long pk, @RequestBody Role[] roles) throws Exception {
		logger.debug("URL: /user/updateroles  method PUT  user.pk {}", pk);
		String status = "失败", info = "修改角色<b style='color:red;'>", erre = "";
		User user = userService.load(pk);
		try {
			userService.updateRoles(user, roles);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		} finally {
			info += user.getName() + "</b>密码" + status;
			LogUtil.log("用户管理>角色", "修改角色", user.getName(), status, info, erre);
		}
		return ModelAndViewBuilder.newInstanceFor("/user/list")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.update.roles").msg("修改用户角色成功").build())
				.build();
	}

	@RequestMapping(value = "/dataAuthorized/{pk}", method = RequestMethod.POST)
	public ModelAndView dataAuthorized(@PathVariable Long pk, @RequestBody DataAuthorized[] dataAuthorizeds,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = userService.load(pk);
		userService.updateRoleDataAuthorized(user, dataAuthorizeds);
		return ModelAndViewBuilder.newInstanceFor("").append(ResponseStatus.NAME,
				new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.list").msg("用户列表").build()).build();
	}

	/**
	 * 修改用户权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateGrant/{pk}", method = RequestMethod.PUT)
	public ModelAndView updateGrant(@PathVariable Long pk, @RequestBody URLResource[] urlResources) throws Exception {
		logger.debug("URL: /user/updateroles  method PUT  user.pk {}", pk);
		String status = "失败", info = "用户<b style='color:red;'>", erre = "";
		User user = userService.load(pk);
		try {
			userService.updateUrls(user, urlResources);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		} finally {
			info += user.getName() + "</b>修改权限" + status;
			LogUtil.log("用户管理", "删除权限", user.getName(), status, info, erre);
		}

		return ModelAndViewBuilder.newInstanceFor("/user/list")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.update.grant").msg("修改用户权限成功").build())
				.build();
	}

	/**
	 * 修改用户密码界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/view/batchgrant", method = RequestMethod.GET)
	public ModelAndView viewBeathGrant(ServletRequest request) throws Exception {
		List<Role> roles = roleService.list();
		Query<User> query = newQuery(1, 5, request);
		userService.query(query);

		for (User user : query.getResults()) {
			user.setResources(null);
		}

		return ModelAndViewBuilder.newInstanceFor("/user/batchGrant").append("roles", roles).append("users", query)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.grant").msg("用户权限").build())
				.build();
	}

	/**
	 * 获取用户权限(包括角色权限)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/view/grant/tree/{userPk}", method = RequestMethod.GET)
	public ModelAndView viewGrantTree(HttpServletRequest request, HttpServletResponse response,
			@PathVariable Long userPk) throws Exception {
		logger.debug("URL: /view/grant/tree  method GET  ");

		User user = null;
		if (userPk > 0)
			user = userService.load(userPk);
		else
			user = new User();
		URLResource app = resourceService.getUserDefaultApp(user);
		PermissionZtreeBuilder ztreeBuilder = SpringContext.getBean(PermissionZtreeBuilder.class);

		ArrayList<URLResource> userMenus = new ArrayList<URLResource>();
		Set<UserResource> urs = user.getResources();
		if (urs != null) {
			for (UserResource ur : urs) {

				userMenus.add(ur.getResource());
			}
		}

		JqueryZtree root = ztreeBuilder.build(app, userMenus, PermissionZtreeBuilder.PermissionType.user, userPk);
		// JqueryZtree root =
		// ztreeBuilder.build(app,PermissionZtreeBuilder.PermissionType.user,
		// userPk);
		List treeNodes = root.getZtreeNodes();
		// treeNodes.add(root.getZtreeNode(false));
		return ModelAndViewBuilder.newInstanceFor("").append("ztree", treeNodes)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.grant.tree").msg("获取用户权限").build())
				.build();
	}

	/**
	 * 根据角色获取权限
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/roles/url", method = RequestMethod.GET)
	public ModelAndView roleUrls(@RequestParam long[] rpk) throws Exception {
		logger.debug("URL /user/roles/url");
		List ztreeList = new ArrayList();
		for (long pk : rpk) {
			List<URLResource> resList = resourceService.findResourceAllFor(pk);
			for (URLResource urlResource : resList) {
				Ztree ztree = new Ztree(String.valueOf(urlResource.getPk()), urlResource.getName(), false,
						urlResource.getParent() == null ? "" : String.valueOf(urlResource.getParent().getPk()));
				HashMap treeMap = ztree.getZtree();
				treeMap.put("open", true);
				if (!ztreeList.contains(treeMap)) {
					ztreeList.add(treeMap);
				}
			}
		}
		return ModelAndViewBuilder.newInstanceFor("").append("ztree", ztreeList)
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.grant.tree").msg("获取用户权限").build())
				.build();
	}

	/**
	 * 授权批量确认
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateBatchGrant", method = RequestMethod.PUT)
	public ModelAndView updateBatchGrant(HttpServletRequest request, HttpServletResponse response,
			@RequestBody URLResource[] urlResources) throws Exception {
		String status = "失败", name = "", info = "用户<b style='color:red;'>", erre = "";
		try {
			String roleIds = request.getParameter("roleIds");
			String[] roleList = new String[] {};
			if (!roleIds.equals("")) {
				roleList = roleIds.split(",");
			}
			String userIds = request.getParameter("userIds");
			String[] userList = userIds.split(",");
			for (String userId : userList) {
				User user = userService.load(Long.valueOf(userId));
				name += user.getName() + ",";
				Role[] roles = new Role[] {};
				if (roleList.length != 0) {
					roles = new Role[roleList.length];
					for (int i = 0; i < roleList.length; i++) {
						String roleId = roleList[i];
						roles[i] = roleService.load(Long.valueOf(roleId));
					}
				}
				userService.updateRoles(user, roles);
				userService.updateUrls(user, urlResources);
			}
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		} finally {
			info += name + "</b>授权" + status;
			LogUtil.log("考试管理>查看", "删除", name, status, info, erre);
		}
		return ModelAndViewBuilder.newInstanceFor("")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.updateBatchGrant").msg("用户列表").build())
				.build();
	}

	/**
	 * 用户考试权限列表
	 * 
	 * @param pk
	 * @param request
	 * @param response
	 * @return
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/examList/{pk}/{currentPage}/{pageSize}", method = RequestMethod.GET)
	public ModelAndView examList(@PathVariable Long pk, @PathVariable int currentPage, @PathVariable int pageSize,
			ServletRequest request) throws Exception {
		logger.debug("URL: /user/examList/{}/{}/{}", pk, currentPage, pageSize);
		User user = userService.load(pk);
		// 获取用户考试权限 （考试ID串）
		String examids = userService.getUserExamByRoleId(pk).replaceAll(" ", "");

		Page<Exam> page = newPage(currentPage, 1000, request);
		List<Exam> exams = examService.list(page);

		// 已授权的默认选择
		for (int i = 0; i < page.getList().size(); i++) {
			if (page.getList().get(i) != null
					&& examids.indexOf(("," + page.getList().get(i).getId().toString() + ",")) > -1) {
				page.getList().get(i).setStudentBaseStatus(1);
			} else {
				page.getList().get(i).setStudentBaseStatus(0);
			}
		}
		return ModelAndViewBuilder.newInstanceFor("user/examList").append("user", user).append("page", page).build();
	}

	/**
	 * 考试授权
	 * 
	 * @param examids
	 * @param pk
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/givePower/{examids}/{pk}", method = RequestMethod.POST)
	public ModelAndView givePower(@PathVariable String examids, @PathVariable Long pk, ServletRequest request)
			throws Exception {
		logger.debug("URL: /user/givePower/" + pk);
		User user = userService.load(pk);
		String status = "失败", info = "给予用户<b style='color:red;'>", erre = "";
		try {
			userService.givePower(examids, pk);
			status = "成功";
		} catch (Exception e) {
			erre = LogUtil.e(e);
			throw e;
		} finally {

			info += user.getName() + "</b>指定考试" + status + "</br>考试列表<br>";
			String[] str = examids.split(",");
			for (int i = 0; i < str.length; i++) {
				Exam exam = examService.findById(Long.valueOf(str[i]));
				if (exam != null) {
					info += "<b style='color:red;'>" + exam.getName() + ",</br>";
				}
			}
			LogUtil.log("用户管理>用户列表", "指定考试", user.getName(), status, info, erre);
		}
		return ModelAndViewBuilder.newInstanceFor("user/examList").append("page", "")
				.append(ResponseStatus.NAME,
						new ResponseStatus.Builder(Boolean.TRUE).code("4a.user.update.grant").msg("修改用户权限成功").build())
				.build();
	}

	/**
	 * 查看用户详情
	 * 
	 * @param pk
	 * @param currentPage
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/viewUI/{pk}/{currentPage}/{pageSize}")
	public ModelAndView viewUI(@PathVariable Long pk, @PathVariable int currentPage, @PathVariable int pageSize,
			ServletRequest request) throws Exception {
		String rolenames = "";
		Query<Exam> query = newQuery(currentPage, pageSize, request);
		examService.examlist(query, pk);
		List<Role> roleList = new ArrayList<Role>();
		List<UserBelong> ubelongs = new ArrayList<UserBelong>();
		if (query.getParameters().isEmpty()) {
			User user = userService.load(pk);
			ubelongs = belongService.findBelongFor(user);
			roleList = roleService.findRolesById(pk);
			if (roleList != null && roleList.size() > 0) {
				for (int i = 0; i < roleList.size(); i++) {
					rolenames += roleList.get(i).getName() + ",";
				}
				rolenames = rolenames.substring(0, rolenames.length() - 1);
			}
			return ModelAndViewBuilder.newInstanceFor("/user/view").append("user", user).append("ubelongs", ubelongs)
					.append("rolenames", rolenames).append("query", query).build();
		} else {
			return ModelAndViewBuilder.newInstanceFor("/user/examDetail").append("query", query).build();
		}
	}

}
