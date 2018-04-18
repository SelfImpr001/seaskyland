package com.cntest.fxpt.tree;

public interface OrganizationService {
	
	/**
	 * 取得Organization整棵树
	 * @return
	 */
	public OrganizationTreeNode getOrgsTree(String appKey, String username);

}
