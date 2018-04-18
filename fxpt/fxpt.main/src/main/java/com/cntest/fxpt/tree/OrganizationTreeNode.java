package com.cntest.fxpt.tree;

import java.util.List;

/**
 * Organization Tree Node Object
 * @author Administrator
 *
 */
public class OrganizationTreeNode {

	private String id;
	private String name;
	private String sn;
	private String description;
	private List<OrganizationTreeNode> childs;//子节点
	private OrganizationTreeNode parent;//父节点
	private int level;//深度
	private boolean leaf;//是否叶子节点
	
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<OrganizationTreeNode> getChilds() {
		return childs;
	}
	public void setChilds(List<OrganizationTreeNode> childs) {
		this.childs = childs;
	}
	public OrganizationTreeNode getParent() {
		return parent;
	}
	public void setParent(OrganizationTreeNode parent) {
		this.parent = parent;
	}
	
	
}
