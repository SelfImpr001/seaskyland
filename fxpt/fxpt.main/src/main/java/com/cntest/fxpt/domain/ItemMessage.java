package com.cntest.fxpt.domain;

/**
 * 选做题信息
 * @author cheny
 * 2016-10-26
 */
public class ItemMessage {
	/*
	 * 选做题组编号
	 */
	private String groupId;
	/*
	 * 每组选做题的题目ID
	 */
	private String itemNos;
	/*
	 * 选做几题
	 */
	private String num;
	/*
	 * 第几题
	 */
	private String serials;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getItemNos() {
		return itemNos;
	}
	public void setItemNos(String itemNos) {
		this.itemNos = itemNos;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getSerials() {
		return serials;
	}
	public void setSerials(String serials) {
		this.serials = serials;
	}
	
	
	
}
