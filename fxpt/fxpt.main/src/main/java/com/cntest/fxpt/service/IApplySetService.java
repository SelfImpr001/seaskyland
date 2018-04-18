package com.cntest.fxpt.service;

import java.util.List;

import com.cntest.fxpt.domain.ApplySet;

public interface IApplySetService {

	/**
     * 查询所有应用设置
     * @return
     */
	public List<ApplySet> findAllApply();
	
	public void update(ApplySet applySet);
	
	public void delete(ApplySet applySet);
	
	/**
	 * 根据状态删除
	 * @param status
	 */
	public void deleteByStatus(String status);
	
	public void save(ApplySet applySet);
	/**
	 * 根据名称模糊查询
	 * @param systemName
	 * @return
	 */
	public List<ApplySet> findApplyByName(String systemName);
	/**
	 * 根据id获取应用数据
	 * @param id
	 */
	public ApplySet get(Long id);
	
	/**
	 * 查询禁用或启用的应用设置
	 * @param status
	 * @return
	 */
	public List<ApplySet> findApplyByStatus(String status);
	/**
	 * 上传临时图片，方便预览
	 * @param applyList
	 * type  登录界面 还是操作界面
	 * baseImage  转码后的图标
	 */
	public void justAddImage(List<ApplySet> applyList,String type, ApplySet applySet);
}
