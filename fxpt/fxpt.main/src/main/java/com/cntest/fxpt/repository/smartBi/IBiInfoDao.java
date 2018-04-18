/**
 * 
 */
package com.cntest.fxpt.repository.smartBi;

import java.util.List;

import org.hibernate.SQLQuery;

import com.cntest.fxpt.bi.domain.BiInfo;


/**
 * @author Administrator 获取BI相关信息的到处理接口
 */
public interface IBiInfoDao {
	public BiInfo getBiInfo(Integer id);
	public void addBiInfo(BiInfo biInfo);
	public void updateBiInfo(BiInfo biInfo);
	public void deleteBiInfo(BiInfo biInfo);
	public List<BiInfo> getBiInfoList();
	public SQLQuery getSQLQuery(String sql);
}
