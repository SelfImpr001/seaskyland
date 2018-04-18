/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.security;

import java.io.Serializable;
import java.util.Collection;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年6月16日
 * @version 1.0
 **/
public interface UserResource extends Serializable {
	public String getUuid();

	public String getUrl();
	
	

	public String getType();

	public String getName();
	
	public void setName(String name);

	public String getAlias();

	public String getIcon();

	public String getEventCode();

	public void setEventCode(String eventCode);
	
	public Integer getOrder();

	public boolean isHasChild();

	public boolean isHiddenInBrowse();

	public String getDesc();

	public Collection<UserResource> getChildren();
	
	public void addChild(UserResource child);
	
	public UserResource clone();
}
