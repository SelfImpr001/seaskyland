/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.domain;

import java.util.HashMap;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年7月4日
 * @version 1.0
 **/
public class Ztree {
	
	private HashMap<String,Object> ztree = new HashMap<String,Object>();
	
	private String id;
	
	private String name;
	
	private boolean open;
	
	private String pId;
	
	private boolean checked;
	
	public Ztree(String id, String name, boolean open, String pId) {
		super();
		this.id = id;
		this.name = name;
		this.open = open;
		this.pId = pId;
	}

	public Ztree(String id, String name, boolean open, String pId, boolean checked) {
		super();
		this.id = id;
		this.name = name;
		this.open = open;
		this.pId = pId;
		this.checked = checked;
	}

	public HashMap<String, Object> getZtree() {
		ztree.put("id", id);
		ztree.put("name", name);
		ztree.put("open", open);
		ztree.put("pId", pId);
		ztree.put("checked", checked);
		return ztree;
	}

	public void setZtree(HashMap<String, Object> ztree) {
		ztree.put("id", id);
		ztree.put("name", name);
		ztree.put("open", open);
		ztree.put("pId", pId);
		ztree.put("checked", checked);
		this.ztree = ztree;
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

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	
}

