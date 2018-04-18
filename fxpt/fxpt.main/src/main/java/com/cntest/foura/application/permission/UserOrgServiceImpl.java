/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.application.permission;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserBelong;
import com.cntest.foura.service.UserBelongService;
import com.cntest.security.DefaultUserOrg;
import com.cntest.security.UserDetails;
import com.cntest.security.UserOrg;
import com.cntest.security.remote.UserOrgService;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年11月7日
 * @version 1.0
 **/
public class UserOrgServiceImpl implements UserOrgService {

	@Autowired
	private UserBelongService belongService;

	@Override
	public List<UserOrg> getUserOrgs(UserDetails user) {
		List<UserBelong> orgs = belongService.findBelongFor(User.from(user));
		ArrayList<UserOrg> myOrgs = new ArrayList<UserOrg>();
		if (orgs != null) {
			for (UserBelong org : orgs) {
				myOrgs.add(new DefaultUserOrg(org.getOrg().getCode(), org
						.getOrg().getName(), org.getOrg().getName(), org
						.getOrg().getType()));
			}
		}
		return myOrgs;
	}

}
