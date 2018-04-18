/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.application.permission;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.DataPermission;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.DataPermissionService;
import com.cntest.fxpt.domain.Exam;
import com.cntest.security.DefaultUserResource;
import com.cntest.security.UserResource;
import com.cntest.util.SpringContext;

/**
 * <pre>
 * 用户对数据的访问权限
 * </pre>
 * 
 * @author 李贵庆2014年6月26日
 * @version 1.0
 **/
public class DataUserPermission extends AbstractUserPermission {
	private static Logger logger = LoggerFactory.getLogger(DataUserPermission.class);

	private DataUserPermission() {
	}

	@Override
	public List<UserResource> query(User user, String uuid,Exam exam) {
		logger.info("Get User {} DataUserPermission with {}", user, uuid);

		DataPermissionService dataPermissionService = SpringContext.getBean(DataPermissionService.class);
		List<DataPermission> dps = dataPermissionService.getTargetPermissionAuthorized("user", user.getPk());
		ArrayList<UserResource> reses = new ArrayList<UserResource>();
		if (dps != null) {
			int i = 1;
			for (DataPermission dp : dps) {
				DefaultUserResource dataUrl = new DefaultUserResource.Builder().uuid(dp.getPk() + "").name(dp.getName()).url(dp.getParamName()).order(i++)
						.create();
				List<DataAuthorized> targetPermissions = dp.getTargetPermissions();
				if (targetPermissions != null) {
					int j = 1;
					for (DataAuthorized authorized : targetPermissions) {
						dataUrl.addChild(new DefaultUserResource.Builder().name(authorized.getPermissionName()).url(authorized.getPermissionValue())
								.uuid(authorized.getPk() + "").order(j++).create());
					}

				}
				reses.add(dataUrl);
			}
		}
		return reses;
		// return toUserResource(reses,user,uuid);
	}

	private final static class SingletonHolder {
		private final static DataUserPermission instance = new DataUserPermission();
	}

	public final static UserPermisson getInstance() {
		return SingletonHolder.instance;
	}
}
