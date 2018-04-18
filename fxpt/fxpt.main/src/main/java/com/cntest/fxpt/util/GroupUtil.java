package com.cntest.fxpt.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GroupUtil {
	
	public interface Groupby<T>{
		T groupby(Object obj);
	}
	
	public final <T extends Comparable<T>,D> Map<T, List<D>> group(Collection<D> colls,Groupby<T> gb){
		if(colls==null || colls.isEmpty()){
			return null;
		}
		if(gb == null){
			return null;
		}
		
		Iterator<D> iter = colls.iterator();
		Map<T, List<D>> map = new HashMap<T, List<D>>();
		
		while (iter.hasNext()) {
			D d = iter.next();
			T t = gb.groupby(d);
			if(map.containsKey(t)){
				map.get(t).add(d);
			}else{
				List<D> list = new ArrayList<D>();
				list.add(d);
				map.put(t, list);
			}
		}
		return map;
	}
	
//	public static void main(String[] args) {
//		List<Item> list = new ArrayList<Item>();
//			list.add(new Item("36","1/1"));
//			list.add(new Item("36","1/1"));
//			list.add(new Item("36","1/1"));
//			list.add(new Item("37","1/2"));
//			list.add(new Item("37","1/2"));
//			list.add(new Item("37","1/2"));
//			list.add(new Item("38","1/3"));
//			list.add(new Item("38","1/3"));
//			list.add(new Item("38","1/3"));
//		Map<String, List<Item>> itemmap = group(list, new Groupby<String>() {
//			@Override
//			public String groupby(Object obj) {
//				Item i = (Item)obj;
//				return i.getChoiceGroup();
//			}
//
//		});
//
//		System.out.println(itemmap);
//	}

}
