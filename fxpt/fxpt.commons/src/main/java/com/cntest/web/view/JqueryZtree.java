/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.web.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年12月1日
 * @version 1.0
 **/
public class JqueryZtree {
	
	private Long id = 0L;
	
	private JqueryZtree parent;
	
	private boolean autoParentChecked = true;
	
	private ArrayList<Map> nodes;
	
	public JqueryZtree() {
		this.node = new HashMap();
		this.id(id);
	}
	
	public JqueryZtree(Map node) {
		this.node = node;
	}
	
	public JqueryZtree(boolean autoParentChecked) {
		this();
		this.autoParentChecked = autoParentChecked;
	}
	
	public JqueryZtree genChild() {
		return genChild(Boolean.TRUE);
	}
	
	public JqueryZtree genChild(boolean autoParentChecked) {
		JqueryZtree child = new JqueryZtree();
		child.autoParentChecked = autoParentChecked;
		appendChild(child);
		return child;
	}
	
	public void appendChild(JqueryZtree child) {
		child.parent = this;
		this.isParent(true);
		child.pId(new Long(this.get(ZtreeField.id)+""));
		child.keepId(this.id+1);
		child.id(this.nowId());
		this.addNode(child);
	}
	
	
	private Map node;
	
	public Map ztreeNode(){
		return node;
	}
	
	public Map getZtreeNode(){
		return node;
	}
	
	public List<Map> getZtreeNodes(){
		ArrayList<Map> newNodes = new ArrayList<Map>();
		newNodes.add(this.node);
		newNodes.addAll(this.nodes);
		return newNodes;

	}
	
	public Map getZtreeNode(Boolean resetId){
		return node;
	}
	
	public Long nowId() {
		if(this.parent != null)
			return this.parent.nowId();
		else
			return this.id;
	}
	
	public void disdroy() {
		
	}
	
	private void keepId(Long newId) {
		if(this.parent != null) {
			this.parent.keepId(newId);
		}else {
			if(this.id < newId)
				this.id = newId;
			else {
				this.id++;
			}
		}
	}
	
	@Override
	public String toString() {
		return node.toString();
	}
	
	public JqueryZtree id(Long id) {
		this.id = id;
		this.put(ZtreeField.id, id);
		return this;
	}
	
	public JqueryZtree name(String name) {
		this.put(ZtreeField.name, name);
		return this;
	}
	
	public JqueryZtree isParent(Boolean isParent) {
		this.put(ZtreeField.isParent, isParent);
		return this;
	}
	
	public JqueryZtree open(Boolean open) {
		this.put(ZtreeField.open, open);
		return this;
	}

	public JqueryZtree url(String url) {
		this.put(ZtreeField.url, url);
		return this;
	}
	public JqueryZtree pId(Long pId) {
		this.put(ZtreeField.pId, pId);
		return this;
	}
	
	public JqueryZtree checked(Boolean checked) {
		this.put(ZtreeField.checked, checked);
		if(this.autoParentChecked && this.isChecked() && this.parent != null)
			this.parent.checked(checked);
		return this;
	}
	
	private boolean isChecked() {
		Boolean b =(Boolean) this.get(ZtreeField.checked);
		return b==null?false:b;
	}
	
	private ArrayList<Map<String,Object>> getChildren() {
		if(this.get(ZtreeField.children) != null)
			return (ArrayList<Map<String, Object>>) this.get(ZtreeField.children);
		return null;
	}
	
	private void addNode(JqueryZtree treeNode){
		if(this.parent!=null){
			this.parent.addNode(treeNode);
		}else{
			if(this.nodes==null)
			this.nodes = new ArrayList<Map>();
			this.nodes.add(treeNode.node);
		}
	} 
	
	
	public JqueryZtree expendAttr(String name,Object value) {
		if(name.equalsIgnoreCase("id") || name.equalsIgnoreCase("pid"))
			name = "_" + name;
		this.node.put(name, value);
		return this;
	}
	
	public JqueryZtree child(JqueryZtree child) {
		if(this.get(ZtreeField.children) == null)
			this.put(ZtreeField.children, new ArrayList<Map<String,Object>>());
		ArrayList<Map<String,Object>> children = (ArrayList<Map<String, Object>>) this.get(ZtreeField.children);
		if(child != null)
			children.add(child.ztreeNode());
		
		//child.parent = this;
		return this;
	}

	public void put(ZtreeField key,Object value) {
		this.node.put(key+"", value);
	}
	
	public Object get(ZtreeField key) {
		return this.node.get(key+"");
	}
	
	public JqueryZtree children(Collection<JqueryZtree> children) {
		for(JqueryZtree child:children)
			this.child(child);
		return this;
	}
	
	public static enum ZtreeField{
		id,name,open,url,pId,checked,children,isParent,uuid
	}
}

