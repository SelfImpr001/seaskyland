/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.common.service.AbstractEntityService;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.DataPermission;
import com.cntest.foura.repository.DataPermissionRepository;
import com.cntest.foura.service.DataPermissionService;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年12月4日
 * @version 1.0
 **/

@Service
public class DataPermissionServiceImpl extends AbstractEntityService<DataPermission, Long> implements DataPermissionService {
	private static Logger logger = LoggerFactory.getLogger(DataPermissionServiceImpl.class);

	@Autowired
	private DataPermissionRepository dataPermissionRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setRepository(DataPermissionRepository  repository) {
		super.setRepository(repository);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<DataPermission> getChildren(Long pk) {
		logger.debug("getChildren for pk={}", pk);
		return dataPermissionRepository.selectByParentId(pk);
	}
	
	@Override
	@Transactional
	public void remove(DataPermission permission) throws  BusinessException{
		logger.debug("remove permission={}", permission.toString());
		List<DataPermission> dps =  dataPermissionRepository.selectByParentId(permission.getPk());
		if(dps!=null) {
			for(DataPermission dp:dps) {
				dataPermissionRepository.deleteDataAuthorizeds(permission);
				this.remove(dp);
			}
		}
		super.remove(permission);
	}
	
	@Transactional
	public void upateAuthorized(Long targetId, String target, DataAuthorized[] dataAuthorizeds) {
		logger.debug("upateAuthorized for tagert={}", target);
		dataPermissionRepository.upateAuthorized(targetId, target,dataAuthorizeds);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DataAuthorized> findDataAuthorizeds(String target, Long targetPk) {
		logger.debug("findDataAuthorizeds for tagert={}", target);
		return dataPermissionRepository.selectDataAuthorizeds(target,targetPk);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DataPermission> getTargetPermissionAuthorized(String target, Long targetPk) {
		logger.debug("getTargetPermissionAuthorized for tagert={}", target);
		List<DataAuthorized> targetDataAuthorizeds = this.findDataAuthorizeds(target, targetPk);
		HashMap<DataPermission,DataPermission> dps = new HashMap<DataPermission,DataPermission>();
		if(targetDataAuthorizeds != null) {
			for(DataAuthorized dataauthorized:targetDataAuthorizeds) {
				DataPermission dp = dataauthorized.getPermission();
				DataPermission existdp = dps.get(dp);
				if(dp.equals(existdp)) {
					existdp.addAuthorized(dataauthorized);
				}else {
					dps.put(dp, dp);
					dp.addAuthorized(dataauthorized);
				}
			}
		}
		ArrayList<DataPermission> result = new ArrayList<DataPermission>();
		result.addAll(dps.keySet());		
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DataAuthorized> getDetail(final DataPermission permission) {
		//return dataPermissionRepository.selectDataAuthorizeds(permission);
		String sql = permission.getSource();
		Object [] args = null;
		if(permission.getParentRefKey() != null && permission.getParentRefKey().length() > 0)
			args = new String[] {permission.getParentRefKey()};
		return jdbcTemplate.query(sql.toString(), args, new RowMapper<DataAuthorized>() {

			@Override
			public DataAuthorized mapRow(ResultSet rs, int rowNum) throws SQLException {
				DataAuthorized da = new DataAuthorized();
				da.setPermissionName(rs.getString(permission.getParamNamefield()));
				da.setPermissionValue(rs.getString(permission.getParamValueField()));
				return da;
			}
			
		});
	}
}
