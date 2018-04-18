/*
 * @(#)com.cntest.fxpt.anlaysis.bean.ItemGroup.java	1.0 2014年12月3日:上午9:51:32
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.cntest.fxpt.domain.Item;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年12月3日 上午9:51:32
 * @version 1.0
 */
public class ItemGroup {
	private String name;
	private List<Item> items = new ArrayList<>();
	private boolean isChoiceType = false;

	public String getName() {
		return name;
	}

	public List<Item> getItems() {
		return items;
	}

	public ItemGroup setName(String name) {
		this.name = name;
		return this;
	}

	public ItemGroup setItems(List<Item> items) {
		this.items = items;
		return this;
	}

	public boolean isChoiceType() {
		return isChoiceType;
	}

	public void setChoiceType(boolean isChoiceType) {
		this.isChoiceType = isChoiceType;
	}

	public double getFullScore() {
		double result = 0;
		for (Item item : items) {
			if (!item.isChoice()) {
				result += item.getFullScore();
			}
		}
		return result;
	}

	public double getFullScoreChoice() {
		double result = 0;
		int choicecount = 0;
		int i = 1;// 只支持多选一
		for (Item item : items) {
			if (item.isChoice()) {
				String choicenum = item.getChoiceNumber();
				choicecount = Integer.parseInt(choicenum.split("\\|")[1]);
				if (choicecount == i) {
					result += item.getFullScore();
				} else {
					break;
				}
				i = choicecount;
			}
		}
		return result;
	}

	public int getItemNum() {
		return items.size();
	}

	public String getItemNoToString() {
		StringBuffer itemStr = new StringBuffer();
		for (Item item : items) {
			if (!item.isChoice()) {
				itemStr.append(item.getItemNo() + ",");
			}
		}

		if (!items.isEmpty() && (itemStr.length() - 1) != -1) {
			itemStr.deleteCharAt(itemStr.length() - 1);
		}
		return itemStr.toString();
	}

	public String getItemNoToStringchoice() {
		StringBuffer itemStr = new StringBuffer();
		for (Item item : items) {
			if (item.isChoice()) {
				itemStr.append(item.getItemNo() + ",");
			}
		}

		if (!items.isEmpty() && (itemStr.length() - 1) != -1) {
			itemStr.deleteCharAt(itemStr.length() - 1);
		}
		return itemStr.toString();
	}

	public static List<ItemGroup> createItemGroupsWithItemAttr(String itemAttr, List<Item> items, boolean haschoice) {
		LinkedHashMap<String, List<Item>> itemAttrMap = new LinkedHashMap<>();
		try {
			for (Item item : items) {
				String classify = BeanUtils.getProperty(item, itemAttr);
				List<Item> tempItems = itemAttrMap.get(classify);
				if (null == tempItems) {
					tempItems = new ArrayList<>();
					itemAttrMap.put(classify, tempItems);
				}
				tempItems.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ArrayList<ItemGroup> result = new ArrayList<>();
		for (String classify : itemAttrMap.keySet()) {
			List<Item> tempItems = itemAttrMap.get(classify);
			ItemGroup desc = new ItemGroup().setName(classify).setItems(tempItems);
			result.add(desc);
			// if("foshan".equals(SystemConfig.newInstance().getValue(
			// "area.org.code"))){
			/*
			 * if(haschoice){ ItemGroup descc = new
			 * ItemGroup().setName(classify).setItems( tempItems);
			 * descc.setChoiceType(true); result.add(descc); }
			 */
		}
		return result;
	}
}
