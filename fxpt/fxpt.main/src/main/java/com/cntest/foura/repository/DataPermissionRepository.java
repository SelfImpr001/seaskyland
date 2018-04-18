/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.repository;

import java.util.List;

import com.cntest.common.repository.Repository;
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
public interface DataPermissionRepository extends Repository<DataPermission, Long> {

	List<DataPermission> selectByParentId(Long parentId);

	void upateAuthorized(Long targetPk, String target, DataAuthorized[] dataAuthorizeds);

	List<DataAuthorized> selectDataAuthorizeds(String target, Long targetPk);

	List<DataAuthorized> selectDataAuthorizeds(DataPermission permission);

	void deleteDataAuthorizeds(DataPermission permission);
	/**
	 * 根据表名称查询数据组织权限
	 * @param tableName
	 * @return
	 */
	List<DataAuthorized> selectAllDataAuthorizeds(String tableName,String orgIds);
	
}

