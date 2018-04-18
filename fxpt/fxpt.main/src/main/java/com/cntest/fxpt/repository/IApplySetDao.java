package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.fxpt.domain.ApplySet;

public interface IApplySetDao {
	/**
     * 查询所有应用设置
     * @return
     */
	public List<ApplySet> findAllApply();
	
	public void update(ApplySet applySet);
	
	public void updateByApply(ApplySet applySet);
	
	public void delete(ApplySet applySet);
	
	/**
	 * 根据状态删除
	 * @param status
	 */
	public void deleteByStatus(String status);
	
	public void save(ApplySet applySet);
	
	public ApplySet get(Long id);
	
	/**
	 * 查询禁用或启用的应用设置
	 * @param status
	 * @return
	 */
	public List<ApplySet> findApplyByStatus(String status);
	
	/**
	 * 根据名称模糊查询
	 * @param systemName
	 * @return
	 */
	public List<ApplySet> findApplyByName(String systemName);
	/**
	 * 临时图标保存
	 * @param applySet
	 */
	public void addApply(ApplySet applySet);
	
}
