/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.service.impl;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.common.query.Query;
import com.cntest.common.service.AbstractEntityService;
import com.cntest.exception.BusinessException;
import com.cntest.foura.application.permission.PasswordService;
import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.domain.Role;
import com.cntest.foura.domain.RoleResource;
import com.cntest.foura.domain.URIType;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserInfo;
import com.cntest.foura.domain.UserIsExistException;
import com.cntest.foura.repository.RoleRepository;
import com.cntest.foura.repository.UserBelongRepository;
import com.cntest.foura.repository.UserRepository;
import com.cntest.foura.service.DataPermissionService;
import com.cntest.foura.service.URLResourceService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.bi.BIOperater;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.repository.IExamDao;
import com.cntest.fxpt.service.bi.BiService;
import com.cntest.security.UserDetails;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年6月7日
 * @version 1.0
 **/
@Transactional
@Service
public class UserServiceImpl extends AbstractEntityService<User, Long> implements UserService {
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordService passwordService;

	@Autowired
	private URLResourceService resourceService;

	@Autowired(required = false)
	@Qualifier("BiService")
	private BiService biService;

	@Autowired
	private DataPermissionService dataPermissionService;

	@Autowired
	private UserBelongRepository userBelongRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private IExamDao examDao;

	@Autowired(required = false)
	@Qualifier("bi.BIOperater")
	private BIOperater biOperater;

	public UserServiceImpl() {

	}

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.setRepository(userRepository);
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public void create(User user) throws BusinessException {
		String newPassword = passwordService.encryptPassword(user.getPassword(), user.getName());
		user.setPassword(newPassword);
		User other = userRepository.findSameUser(user.getName());
		if (other != null)
			throw new UserIsExistException();
		logger.debug("Create User:{}", user);
		if (user.getUserInfo() != null)
			logger.debug("Create UserInfo:{}", user.getUserInfo());
		// userRepository.save(user);
		super.create(user);
		// biOperater.createUser(user.getName());
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> list() {
		return userRepository.list();
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> list(Long role) {
		return userRepository.list(role);
	}

	@Override
	@Transactional(readOnly = true)
	public User load(Long pk) throws BusinessException {
		return super.load(pk);
	}

	@Override
	@Transactional(readOnly = true)
	public User findUserBy(String userName) {
		logger.debug("Find user for {}", userName);
		User user = userRepository.selectUserBy(userName);
		logger.debug("Find user :{}", user);
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public URLResource getDefaultUrlResource(User user) {
		logger.debug("Get user {} default UrlResource", user);
		return new URLResource.Builder().name("统一权限管理平台").type(URIType.app).url("home").create();
	}

	@Override
	@Transactional(readOnly = true)
	public void query(Query<User> query) {
		// ArrayList<User> result = new ArrayList<User>();
		// Long total =
		// userRepository.findUsers(result,query.getStartRow(),query.getPagesize(),query.getParameters());
		userRepository.findUsers(query);
		// query.setTotalRows(total.intValue());
		// query.setResults(result);
	}

	@Override
	@Transactional
	public void update(User user) throws BusinessException {
		logger.debug("Update user {},....", user);
		User old = this.userRepository.load(user.getPk());
		UserInfo oldInfo = old.getUserInfo();
		oldInfo.setCellphone(user.getUserInfo().getCellphone());
		oldInfo.setComment(user.getUserInfo().getComment());
		oldInfo.setEmail(user.getUserInfo().getEmail());
		oldInfo.setIdentities(user.getUserInfo().getIdentities());
		oldInfo.setNickName(user.getUserInfo().getNickName());
		oldInfo.setRealName(user.getUserInfo().getRealName());
		oldInfo.setSex(user.getUserInfo().getSex());
		oldInfo.setTelphone(user.getUserInfo().getTelphone());
		old.setStatus(user.getStatus());

		userRepository.update(old);
	}

	@Transactional
	@Override
	public void updatePassword(User user, String newPassword) throws BusinessException {
		String password = passwordService.encryptPassword(newPassword, user.getName());
		user.setPassword(password);
		userRepository.update(user);
		// super.update(user);
	}

	@Transactional
	@Override
	public void updateIsupPassword(User user, String newPassword, String isloginPss) throws BusinessException {
		String password = passwordService.encryptPassword(newPassword, user.getName());
		user.setPassword(password);
		user.setIsup("0");
		userRepository.update(user);
		// super.update(user);
	}

	@Override
	@Transactional
	public void updateRoles(User user, Role... roles) throws BusinessException {
		logger.debug("Update user {} roles", user);
		user = this.load(user.getPk());

		user.getRoles().removeAll(user.getRoles());
		for (Role r : roles) {
			user.addRole(roleRepository.get(r.getPk()));
		}

		logger.debug("Update user {} roles success", user);
	}

	@Override
	@Transactional
	public void updateUrls(User user, URLResource... urlResources) throws BusinessException {
		logger.debug("Update user {} urls", user);
		user = this.load(user.getPk());
		user.getResources().removeAll(user.getResources());

		for (URLResource urlResource : urlResources) {
			user.addResource(resourceService.load(urlResource.getPk()));
		}

		logger.debug("Update user {} urls success", user);
	}

	@Override
	@Transactional
	public Boolean updatePassword(User user, String oldPassword, String newPassword) throws BusinessException {

		String password = passwordService.encryptPassword(newPassword, user.getName());
		String _password = passwordService.encryptPassword(oldPassword, user.getName());
		logger.debug("Update user {} password to {}", user, password);
		if (user.getPassword().equals(_password)) {
			user.setPassword(password);
			userRepository.update(user);
			logger.debug("Update user {} password success", user);
			return Boolean.TRUE;
		}
		throw new BusinessException("cntest-4a-0005", "原密码错误");
	}

	@Override
	@Transactional
	public void deleteRolesByUserId(String userId) throws BusinessException {
		logger.debug("delete user withd pk {}", userId);
		userRepository.deleteRolesByUserId(userId);
		logger.debug("delete user withd pk {}", userId);
	}

	@Override
	@Transactional
	public Set<Role> findRolesByUserId(Long userId) throws BusinessException {
		return userRepository.findRolesByUserId(userId);
	}

	@Override
	@Transactional
	public Set<User> findUsersByRoleId(Long roleId) throws BusinessException {
		return userRepository.findUsersByRoleId(roleId);
	}

	@Override
	@Transactional
	public void deleteResByUserId(String userId) throws BusinessException {
		userRepository.deleteResByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public void queryUsersByRoleId(Query<User> query) {
		userRepository.findUsersByRoleId(query);
	}

	@Override
	@Transactional(readOnly = true)
	public void queryUsersByOrg(Query<User> query, Organization role) {

		userRepository.findUsersByOrg(query, role);
	}

	@Override
	@Transactional(readOnly = true)
	public Set<URLResource> findUrlsByUserId(Long userId) throws BusinessException {
		return userRepository.findUrlsByUserId(userId);
	}

	@Override
	@Transactional
	public void updateRoleDataAuthorized(User user, DataAuthorized[] dataAuthorizeds) {
		logger.debug("update user {} data permissions", user);
		if (dataAuthorizeds == null)
			dataAuthorizeds = new DataAuthorized[] {};

		dataPermissionService.upateAuthorized(user.getPk(), "user", dataAuthorizeds);
	}

	@Override
	public UserDetails getCurrentLoginedUser() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			Object o = subject.getPrincipal();
			if (o instanceof UserDetails) {
				return (UserDetails) o;
			}
			if (o instanceof String) {
				User user = this.userRepository.findSameUser(o + "");
				return user.toUserDetails();
			}
		}

		throw new UnknownAccountException();// 没找到帐号
	}

	@Override
	public void addUserExamByPKandExamids(String examids, Long pk) {
		userRepository.addUserExamByPKandExamids(examids, pk);
	}

	@Override
	public void deleteUserExamByPk(String pk) {
		userRepository.deleteUserExamByPk(pk);
	}

	@Override
	public void givePower(String examids, Long pk) {
		logger.debug("givePower  {} ", pk);
		userRepository.deleteUserExamByPk(pk.toString());
		if (examids != "0") {
			userRepository.addUserExamByPKandExamids(examids, pk);
		}

	}

	@Override
	public String getUserExamByRoleId(Long pk) {
		return userRepository.getUserExamByRoleId(pk);
	}

	@Override
	public void deleteUserExamByExamId(Long examid) {
		userRepository.deleteUserExamByExamId(examid);
	}

	@Override
	@Transactional
	public void createNanshan(User user, String roleCode) throws BusinessException {
		user.setStatus(1);
		create(user);

		if (StringUtils.isBlank(roleCode))
			return;

		List<Role> roles = roleRepository.findRolesByCode(roleCode);
		for (Role role : roles) {
			user.addRole(role);
			Set<RoleResource> resources = role.getResources();
			for (RoleResource roleResource : resources) {
				user.addResource(resourceService.load(roleResource.getResource().getPk()));
			}
		}
	}

	@Override
	public Query<Role> findUserCountByRoleId(Query<Role> result) {
		return userRepository.findUserCountByRoleId(result);
	}

	@Override
	public String initUserHasMessage(String pk) {
		String message = "";
		// 验证是否关联了权限4a_user_resourse
		if (userRepository.findUserResourceByUserId(pk).size() > 0) {
			message = "权限,";
		}
		// 验证是否关联了角色 4a_user_role
		if (userRepository.findUserRoleByUserId(pk).size() > 0) {
			message += "角色,";
		}
		return message;
	}

	@Override
	public void gotoMoveUserAndMessage(User user) throws BusinessException {
		// 删除角色关联信息4a_user_role
		userRepository.deleteRolesByUserId(user.getPk().toString());
		// 删除权限关联信息4a_user_resource
		userRepository.deleteResByUserId(user.getPk().toString());
		// 删除考试关联信息kn_user_exam
		userRepository.deleteUserExamByPk(user.getPk().toString());
		// 删除用户权限信息 4a_data_authorized ,4a_userbelong
		userRepository.deleteUserLink(user.getPk().toString());
		// 删除发布生成的用户信息 kn_userandpassword
		userRepository.deletePublishUserByUserName("'" + user.getName() + "'");

		// 删除用户基本信息4a_user,4a_user_detail,4a_userbelong (必须最后删除，先删除基本信息找不到关联关系)
		remove(user);
	}

	@Override
	public void delteMoreUser(String pks) throws BusinessException {
		if (pks != "") {
			pks = pks.substring(0, pks.length() - 1);
		}
		// 批量查询用户名
		String usernames = userRepository.selectMoreUserNameByPks(pks);
		// 删除角色关联信息4a_user_role
		userRepository.deleteRolesByUserId(pks);
		// 删除权限关联信息4a_user_resource
		userRepository.deleteResByUserId(pks);
		// 发布用户存储表删除对应用户信息
		if (usernames != "") {
			usernames = usernames.trim().replaceAll(", ", "','");
			userRepository.deletePublishUserByUserName("'" + usernames.substring(1, usernames.length() - 1) + "'");
		}
		// 删除考试关联信息kn_user_exam
		userRepository.deleteUserExamByPk(pks);
		// 删除用户基本信息4a_user,4a_user_detail,4a_userbelong (必须最后删除，先删除基本信息找不到关联关系)
		userRepository.deleteUserByPk(pks);
		// 删除用户权限关联 4a_data_authorized,4a_userbelong
		userRepository.deleteUserLink(pks);
	}

	@Override
	public void giveExamPowerToNewUser(String[] orgs, Long userok) throws BusinessException {
		List<Exam> examList = examDao.getExamAllList();
		String examids = "";
		// 通过考试群体来检索
		for (Exam exam : examList) {
			if (exam.getContainOrg() != null && !"".equals(exam.getContainOrg())) {
				for (String org : orgs) {
					// 前后加逗号 避免特殊id 例如：566,7566这种部分相同
					org = "," + org + ",";
					if (("," + exam.getContainOrg() + ",").indexOf(org) > -1) {
						examids += exam.getId() + ",";
						break;
					}
				}
			}
		}
		if (!"".equals(examids))
			givePower(examids.substring(0, examids.length() - 1), userok);
	}

	@Override
	public void givePowerToUserByExam(Exam exam) throws BusinessException {
		if (exam.getContainOrg() != null && !"".equals(exam.getContainOrg())) {
			// 查询有权限的用户
			List userList = userBelongRepository.getUserByOrg(exam.getContainOrg());
			// 添加考试权限到用户
			if (userList.size() > 0) {
				String[] userIds = userList.toString().substring(1, userList.toString().length() - 1).split(",");
				for (String userid : userIds) {
					if (!userRepository.powerIsExist(userid, exam.getId().toString()))
						// 考试权限存在怎不用重新添加
						userRepository.addUserExamByPKandExamids(exam.getId().toString(),
								Long.parseLong(userid.trim()));
				}
			}
		}
	}

	public void evictSession(Object o) {
		userRepository.evictSession(o);
	}

	@Override
	public int synchronous() {
		List<User> list = list();
		if (list != null && list.size() > 0) {
			for (User user : list) {
				Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
				Matcher m = p.matcher(user.getName());
				boolean isChinese = false;
				if (m.find())
					isChinese = true;
				if (user.getName() != null && !"".equals(user.getName()) && !isChinese) {
					biOperater.createUser(user.getName());
				}
			}
			return list.size();
		}
		return 0;
	}

	public static void main(String[] args) {
		String str = "admin";
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		boolean isChinese = false;
		if (m.find())
			isChinese = true;
		System.out.println(isChinese);
	}
}
