/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.permission;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.domain.StudentBase;
import com.cntest.fxpt.service.IExamStudentService;
import com.cntest.security.DefaultUserDetails;
import com.cntest.security.RoleType;
import com.cntest.util.SpringContext;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年11月27日
 * @version 1.0
 **/
public class ExamineeRealm extends AuthorizingRealm {
	private static Logger logger = LoggerFactory.getLogger(ExamineeRealm.class);

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		return new SimpleAuthorizationInfo();
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		StudentBase student = (StudentBase) token.getPrincipal();
		IExamStudentService studentService = SpringContext.getBean(IExamStudentService.class);
		ExamStudent example = new ExamStudent(student.getName(), student.getXh(), student.getSchool());
		ExamStudent examinee = studentService.findByExample(example);
		if (examinee == null)
			throw new UnknownAccountException("个人信息有误，请检查输入是否正确！");// 没找到帐号
		DefaultUserDetails<ExamStudent> userDetail = new DefaultUserDetails.Builder("student").realName(student.getName())
				.nickName(student.getName()).create();
		student.setSchool(examinee.getSchool()); //加入
		userDetail.addOrgCode(student.getSchool().getCode());
		userDetail.addTypeCode(RoleType.student.toString());
		userDetail.setOrigin(examinee);
		return new SimpleAuthenticationInfo(userDetail, userDetail, getName());
	}
	
	@Override
	public CredentialsMatcher getCredentialsMatcher() {
		
		return new CredentialsMatcher() {
			@Override
			public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
				StudentBase student1 = (StudentBase) token.getPrincipal();
				List list = info.getPrincipals().asList();
				for(int i=0;i<list.size();i++) {
					DefaultUserDetails<ExamStudent> userDetail = (DefaultUserDetails<ExamStudent>) list.get(i);
					if(student1.sameAs(userDetail.getOrigin())) {
						return true;
					}
				}
				return false;
			}
		};
	}

	
	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}

}
