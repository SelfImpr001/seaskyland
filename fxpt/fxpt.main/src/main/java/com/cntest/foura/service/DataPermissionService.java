/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.service;

import java.util.List;

import com.cntest.common.service.EntityService;
import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.DataPermission;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年12月4日
 * @version 1.0
 **/
public interface DataPermissionService extends EntityService<DataPermission, Long> {

	List<DataPermission> getChildren(Long pk);
	
	void upateAuthorized(Long targetId, String target, DataAuthorized[] dataAuthorizeds);

	List<DataAuthorized> findDataAuthorizeds(String target, Long targetPk);
	
	List<DataPermission> getTargetPermissionAuthorized(String target, Long targetPk);

	List<DataAuthorized> getDetail(DataPermission permission);
}

