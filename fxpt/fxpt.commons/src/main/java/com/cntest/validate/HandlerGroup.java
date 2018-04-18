/**
 * <p><b>© 1997-2016 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/
package com.cntest.validate;

import java.util.List;

/** 
 * <pre>
 * 可以并行处理的验证处理器
 * 
 * </pre>
 *  
 * @author 李贵庆 @2016年1月24日
 * @since 1.0
 * @see ArrayHandlerGrop
 **/
public interface HandlerGroup {
	
	public List<ValidateHandler> handlers();
	
	public HandlerGroup add(ValidateHandler handler);
	
	public void remove(ValidateHandler handler);
}
