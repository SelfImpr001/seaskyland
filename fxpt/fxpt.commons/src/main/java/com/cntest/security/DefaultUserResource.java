/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月18日
 * @version 1.0
 **/
public class DefaultUserResource implements UserResource {
    private String uuid;
    
    private String url;
    
    private String type;
    
    private String name;
    
    private String alias;
    
    private String icon;
    
    private String eventCode;
    
    private Integer order;
    
    private boolean hasChildren;
    
    private boolean hiddenInBrowser;
    
    private String desc;
    
    private Collection<UserResource> children;
    
    public DefaultUserResource clone() {
    	DefaultUserResource  other = new DefaultUserResource();
    	other.uuid = this.uuid;
    	other.url = this.url;
    	other.type = this.type;
    	other.name = this.name;
    	other.alias = this.alias;
    	other.icon = this.icon;
    	other.eventCode = this.eventCode;
    	other.order = this.order;
    	other.hasChildren = this.hasChildren;
    	other.hiddenInBrowser = this.hiddenInBrowser;
    	other.desc = this.desc;
    	if(this.children != null) {
    		for(UserResource child:children) {
    		    other.addChild(child.clone());	
    		}
    	}
    	return other;
    }
	
    public void addChild(UserResource child) {
    	if(this.children == null)
    		this.children = new ArrayList<UserResource>();
    	this.children.add(child);
    	this.hasChildren = true;
    	Collections.sort((ArrayList<UserResource>)children,new Comparator<UserResource>() {
			@Override
			public int compare(UserResource o1, UserResource o2) {
				if(o1.getOrder() != null)
				    return o1.getOrder().compareTo(o2.getOrder());
				else
					return 0;
			}
    		
    	});
    }
	
	public static class Builder{
		private DefaultUserResource ur;
		
		public Builder() {
			this.ur = new DefaultUserResource();
		}
		
		public Builder url(String url) {
			this.ur.url = url;
			return this;
		}
		
		public Builder uuid(String uuid) {
			this.ur.uuid = uuid;
			return this;
		}
		
		public Builder type(String type) {
			this.ur.type = type;
			return this;
		}
		
		public Builder name(String name) {
			this.ur.name = name;
			return this;
		}
		
		public Builder alias(String alias) {
			this.ur.alias = alias;
			return this;
		}
		
		public Builder icon(String icon) {
			this.ur.icon = icon;
			return this;
		}
		
		public Builder eventCode(String eventCode) {
			this.ur.eventCode = eventCode;
			return this;
		}
		
		public Builder order(Integer order) {
			if(order == null)
				this.ur.order = 0;
			else
			    this.ur.order = order;
			return this;
		}
		
		public Builder hasChildren(boolean hasChildren) {
			this.ur.hasChildren = hasChildren;
			return this;
		}
		
		
		public Builder hiddenInBrowser(boolean hiddenInBrowser) {
			this.ur.hiddenInBrowser = hiddenInBrowser;
			return this;
		}
		
		public Builder desc(String desc) {
			this.ur.desc = desc;
			return this;
		}
		
		public DefaultUserResource create() {
			if(this.ur.alias == null)
				this.ur.alias = this.ur.name;
			return this.ur;
		}
	}
	
	@Override
	public String getUuid() {
		return this.uuid;
	}

	public void setUrl(String newUrl) {
		this.url = newUrl;
	}

	@Override
	public String getUrl() {
		return this.url;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getAlias() {
		return this.alias;
	}

	@Override
	public String getIcon() {
		return this.icon;
	}

	@Override
	public String getEventCode() {
		return this.eventCode;
	}

	@Override
	public Integer getOrder() {
		// TODO Auto-generated method stub
		return this.order;
	}

	@Override
	public boolean isHasChild() {
		return this.hasChildren;
	}

	@Override
	public boolean isHiddenInBrowse() {
		return false;
	}

	@Override
	public String getDesc() {
		return this.desc;
	}

	@Override
	public Collection<UserResource> getChildren() {
		return this.children;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

}

