/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/** <pre>
 * 	  给jszTree组件使用的后台方法
 *    id      ：zTerr节点标识
 *   isParent : 节点是否是父节点  默认false
 *   name     : 节点显示文本  如果对象有属性name 可以省略
 *   open     : 是否展开  默认 false
 *   pId      : 节点 的父节点id
 *   children      ：节点 子节点集合
 *   其他自定义属性 请参见   zTree API
 * </pre>
 *  
 * @author 肖 肖  2014年6月26日
 * @version 1.0
 **/ 
public class ZTree {
	private static Logger logger = LoggerFactory.getLogger(ZTree.class);
	private Map<String,JsonElement> param ;
	private JsonObject jsonObj;
	private JsonElement element ;

	public ZTree() {
		param   = new HashMap<String, JsonElement>();
		jsonObj = new JsonObject();
	}
	
	/**
	 * 将对象生产json数据   
	 * @param t 对象  只会生产int string char boolean类型的属性
	 * @return
	 */
	public JsonObject zTreeJsonObj(Object t) {
		if(t == null)      return null;
		Class<? extends Object> c = t.getClass();
		Field[] fields  = c.getDeclaredFields();
		for (Field field : fields) {
			if(!field.getType().getSimpleName().equals("String")
				&& !field.getType().getSimpleName().equals("Integer")
		          && !field.getType().getSimpleName().equals("Character")
		          	&& !field.getType().getSimpleName().equals("Long")
		          	  && !field.getType().getSimpleName().equals("Boolean")) {
		          continue;  	
		    }
			
			try {
				String methodName = "get" + field.getName().substring(0,1).toUpperCase() +  field.getName().substring(1);
				Method method     = c.getDeclaredMethod(methodName,field.getType().getClasses());
				Object value      = method.invoke(t);
				if(field.getType().getSimpleName().equals("String")) 
						jsonObj.addProperty(field.getName(), (String)value);
				
				if(field.getType().getSimpleName().equals("Integer"))
						jsonObj.addProperty(field.getName(), (Integer)value);
				
				if(field.getType().getSimpleName().equals("Long"))
					jsonObj.addProperty(field.getName(), (Long)value);
				
				if(field.getType().getSimpleName().equals("Character")) 
					jsonObj.addProperty(field.getName(), (Character)value);
				
				if(field.getType().getSimpleName().equals("Boolean")) 
					 jsonObj.addProperty(field.getName(), (Boolean)value);
			} catch (IllegalArgumentException e) {
				logger.error(ExceptionHelper.trace2String(e));
			} catch (SecurityException e) {
				logger.error(ExceptionHelper.trace2String(e));
			} catch (NoSuchMethodException e) {
				logger.error(ExceptionHelper.trace2String(e));
			} catch (IllegalAccessException e) {
				logger.error(ExceptionHelper.trace2String(e));
			} catch (InvocationTargetException e) {
				logger.error(ExceptionHelper.trace2String(e));
			} 
		}
		return filterJson(jsonObj);
	}
	/**
	 * 过滤值为null属性
	 */
	public static JsonObject filterIsNull(JsonObject jsonObj) {
		for (Iterator<Entry<String, JsonElement>> i = jsonObj.entrySet().iterator(); i.hasNext();) {
			Entry<String, JsonElement> enter = i.next();
			if(enter.getValue().isJsonNull())  i.remove();
		}
		return jsonObj;
	}
	private JsonObject filterJson(JsonObject jsonObj) {
		for (Entry<String, JsonElement> enter : param.entrySet()) {
			 jsonObj.add(enter.getKey(),enter.getValue());
		}
		return jsonObj;
	}
	public ZTree id(Long id) {
		element = new JsonParser().parse(id.toString());
		param.put("id", element);
		return this;
	}
	public ZTree name(String name) {
		element = new JsonParser().parse(name.toString());
		param.put("name", element);
		return this;
	}
	public ZTree isParent(Boolean isParent) {
		element = new JsonParser().parse(isParent.toString());
		param.put("isParent", element);
		return this;
	}
	public ZTree open(Boolean open) {
		element = new JsonParser().parse(open.toString());
		param.put("open", element);
		return this;
	}
	public ZTree children(JsonArray children) {
		element = new JsonParser().parse(children.toString());
		param.put("children", children.getAsJsonArray());
		return this;
	}
	public ZTree url(String url) {
		element = new JsonParser().parse(url.toString());
		param.put("url", element);
		return this;
	}
	public ZTree pId(Long pId) {
		element = new JsonParser().parse(pId.toString());
		param.put("pId", element);
		return this;
	}

	/**
	 * 自定义扩展
	 * @param name 名称
	 * @param value 值
	 * @return
	 */
	public ZTree expandDIY(String name,Boolean value) {
		element = new JsonParser().parse(value.toString());
		param.put(name, element);
		return this;
	}
	public ZTree expandDIY(String name,String value) {
		element = new JsonParser().parse(value);
		param.put(name, element);
		return this;
	}
	public ZTree expandDIY(String name,Integer value) {
		element = new JsonParser().parse(value.toString());
		param.put(name, element);
		return this;
	}
	public ZTree expandDIY(String name,Character value) {
		element = new JsonParser().parse(value.toString());
		param.put(name, element);
		return this;
	}
	public static void main(String[] args) {
		
	}
}

