package com.cntest.fxpt.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;


/**
 * 处理json的工具类，负责json数据转换成java对象和java对象转换成json
 * @author LM
 *
 */
public class JsonUtil {
	
	/**
     * 从一个JSON 对象字符格式中得到一个java对象
     *
     * @param jsonString
     * @param pojoCalss
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToObject(String jsonString, Class<T> pojoCalss) {
        JSONObject jsonObject = JSONObject.fromObject(jsonString);
        Object pojo = JSONObject.toBean(jsonObject, pojoCalss);
        return (T) pojo;
    }
 
    /**
     * json字符串转换成集合
     *
     * @param jsonString
     * @param pojoClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToList(String jsonString, Class<T> pojoClass) {
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        
        Object pojoValue;
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < jsonArray.size(); i++) {
            pojoValue = JSONObject.toBean(jsonArray.getJSONObject(i), pojoClass);
            list.add((T) pojoValue);
        }
        return list;
    }
 
    /**
     * json字符串转换成集合
     *
     * @param jsonString
     * @param pojoClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> jsonToList(String jsonString, Class<T> pojoClass, String dataFormat) {
        JsonConfig jsonConfig = configJson(dataFormat);
        JSONArray jsonArray = JSONArray.fromObject(jsonString, jsonConfig);
        JSONObject jsonObject;
        Object pojoValue;
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < jsonArray.size(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            pojoValue = JSONObject.toBean(jsonObject, pojoClass);
            list.add((T) pojoValue);
        }
        return list;
    }
 
    /**
     * 将java对象转换成json字符串
     *
     * @param javaObj
     * @return
     */
    public static String objectToJson(Object javaObj) {
    	JSONObject json = JSONObject.fromObject(javaObj);
        return json.toString();
    }
 
    /**
     * 将java对象转换成json字符串,并设定日期格式
     *
     * @param javaObj  要转换的java对象
     * @param dataFormat  制定的日期格式
     * @return
     */
    public static String objectToJson(Object javaObj, String dataFormat) {
        JsonConfig jsonConfig = configJson(dataFormat);
        JSONObject json = JSONObject.fromObject(javaObj, jsonConfig);
        return json.toString();
    }
 
    /**
     * list变成json
     *
     * @param list
     * @return
     */
    public static <T> String listToJson(List<T> list) {
    	JSONArray json = JSONArray.fromObject(list);
        return json.toString();
    }
 
    /**
     * list变成json
     *
     * @param list
     * @return
     */
    public static <T> String listToJson(List<T> list, String dataFormat) {
        JsonConfig jsonConfig = configJson(dataFormat);
        JSONArray json = JSONArray.fromObject(list, jsonConfig);
        return json.toString();
    }
 
    /**
     * JSON 时间解析器
     *
     * @param datePattern
     * @return
     */
    public static JsonConfig configJson(final String datePattern) {
 
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { datePattern }));
 
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setIgnoreDefaultExcludes(false);
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        jsonConfig.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
 
            @Override
            public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
                if (value instanceof Date) {
                    String str = new SimpleDateFormat(datePattern).format((Date) value);
                    return str;
                }
                return value == null ? null : value.toString();
            }
 
            @Override
            public Object processArrayValue(Object value, JsonConfig jsonConfig) {
                String[] obj = {};
                if (value instanceof Date[]) {
                    SimpleDateFormat sf = new SimpleDateFormat(datePattern);
                    Date[] dates = (Date[]) value;
                    obj = new String[dates.length];
                    for (int i = 0; i < dates.length; i++) {
                        obj[i] = sf.format(dates[i]);
                    }
                }
                return obj;
            }
        });
        return jsonConfig;
    }
	
    @SuppressWarnings("rawtypes")
	public static Map transfor(List keys, Object[] values){
    	return transfor(keys, Arrays.asList(values));
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map transfor(List keys, List values){
    	Map params = new LinkedHashMap<>();
    	
    	int size = keys.size();
    	for (int i = 0; i < size; i++) {
    		Object key = keys.get(i);
    		if(params.containsKey(key)){
    			continue;
    		}
    		
    		Object object = (i < values.size())? values.get(i) : null;
    		params.put(key, object);
		}
    	
    	return params;
    }
    
    public static void main(String[] args) {
		
    	//map
    	/**Map params = new LinkedHashMap<>();
    	params.put("A", 30);
    	params.put("B", 16);
    	
    	JSONArray array = JSONArray.fromObject(params);
    	String arrayStr = array.toString();
    	System.out.println(arrayStr);
    	
    	JSONArray fromObject = JSONArray.fromObject(arrayStr);
    	System.err.println(fromObject.toString());*/
    	/*result	[{"A":30,"B":16}]
    			[{"A":30,"B":16}]*/
    	
    	//list
    	/**List params = new ArrayList<>();
    	params.add("A");
    	params.add("B");
    	params.add("C");
    	params.add("D");
    	
    	JSONArray array = JSONArray.fromObject(params);
    	String arrayStr = array.toString();
    	System.out.println(arrayStr);
    	
    	JSONArray fromObject = JSONArray.fromObject(arrayStr);
    	System.err.println(fromObject.toString());*/
	    /*result	["A","B","C","D"]
	    		["A","B","C","D"]*/
    	
    	
    	/*List keys = new ArrayList<>();
    	keys.add("A");
    	keys.add("B");
    	keys.add("C");
    	keys.add("D");
    	
    	List values = new ArrayList<>();
    	values.add(20);
    	values.add(40);
    	values.add(36);
    	values.add(17);
    	
    	Map map = JsonUtil.transfor(keys, values);
    	System.out.println(map);
    	
    	Object[] objects = new Object[]{12, 15, 18, 50};
    	map = JsonUtil.transfor(keys, objects);
    	System.out.println(map);*/
    	
    	/*String arrayStr = "[\"A\",\"B\",\"C\",\"D\"]";
    	JSONArray array = JSONArray.fromObject(arrayStr);
    	Object[] objects = new Object[]{12, 15, 18, 50};
    	Map map = JsonUtil.transfor(array, objects);
    	System.err.println(map);*/
	}
    
}