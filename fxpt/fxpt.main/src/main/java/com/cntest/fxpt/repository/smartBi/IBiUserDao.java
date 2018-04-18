/**
 * 
 */
package com.cntest.fxpt.repository.smartBi;

import java.util.List;

import org.hibernate.SQLQuery;

import com.cntest.fxpt.bi.domain.BiUser;

/**
 * @author Administrator 获取BI相关信息的到处理接口
 */
public interface IBiUserDao {
	
	public BiUser getBiUser(Integer id);

	public List<BiUser> getBiUsersByBiInfoId(Integer biInfoId);
	
	public List<BiUser> getAllBiUsers();
	
	public void addBiUser(BiUser biUser);
	
	public void updateBiUser(BiUser biUser);
	
	public void deleteBiUser(BiUser biUser);
	
	public SQLQuery getSQLQuery(String sql);
	
}
