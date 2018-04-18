package com.cntest.fxpt.service.bi;

import java.util.List;

import com.cntest.fxpt.bi.domain.BiInfo;
import com.cntest.fxpt.bi.domain.BiUser;
import com.cntest.common.page.Page;

public interface BiService {
	public BiUser getBiUser(Integer id);
	public List<BiUser> getBiUsersByBiInfoId(Integer biInfoId);
	public void addBiUser(BiUser biUser);
	public void updateBiUser(BiUser biUser);
	public void deleteBiUser(BiUser biUser);
	public List<BiUser> getAllBiUsers();
	
	public BiInfo getBiInfo(Integer id);
	public List<BiInfo> getBiInfoList();
	public void addBiInfo(BiInfo biInfo);
	public void updateBiInfo(BiInfo biInfo);
	public void deleteBiInfo(BiInfo biInfo);
	public List<BiUser> findPageByBiUser(Page page);
	public List<BiInfo> findPageByBiInfo(Page page);
	/**
	 * 隐藏Bi报告工具栏参数
	* <Pre>
	* </Pre>
	* 
	* @param url
	* @param listParams --参考 BiParameters.Java 类中参数
	* @return
	* @return String
	* @author:黄洪成 2014年5月20日 下午2:27:58
	 */
	public String setHideParameForUrl(String url,List<String> listParams);
}
