package com.cntest.fxpt.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@SuppressWarnings("rawtypes")
public class LikeHashMap<K, V> extends HashMap<K, V>{
	
	private static final long serialVersionUID = 1L;

	public Set keySet(){
		Set set = super.keySet();
		TreeSet tset = null;
		if(set!=null){
			tset = new TreeSet(set);
		}
		return set;
	}
	
	public List<V> get(String key,boolean like){
		List<V> valueList = null;
		List<String> keyList = new ArrayList<String>();
		if(like){
			valueList = new ArrayList<V>();
			K[] a = null;
			Set<K> set = this.keySet();
			a = (K[])set.toArray();
			Arrays.sort(a);
			
			for (int i = 0; i < a.length; i++) {
				if(a[i].toString().indexOf(key)==-1){
					continue;
				}else{
					valueList.add(this.get(a[i]));
				}
			}
		}
		return valueList;
	}
	
	public static void main(String[] args) {
		LikeHashMap<String, String> mh = new LikeHashMap<String,String>();
		for (int i = 0; i < 1000000; i++) {
			mh.put("NUM_" + i, "AAAA" + i);
		}
		long time = System.currentTimeMillis();
		System.out.println(mh.get("NU",true).size());
		System.out.println(System.currentTimeMillis() - time);
	}
	
}
