package com.cntest.fxpt.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import org.apache.velocity.texen.util.PropertiesUtil;
import org.dom4j.*;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.hibernate.internal.util.xml.XMLHelper;
import org.xml.sax.EntityResolver;

import com.cntest.fxpt.anlaysis.uitl.Unzip;



  
/** 
 * Java读写修改Property文件，修改xml文件 
 * @author chenyou
 * @date 2017-05-08
 * @version 1.0 
 */  
public class PropertiesConfig {  
	private static Properties pro =new Properties();
	protected static transient XMLHelper x= new XMLHelper();
	private static EntityResolver entityResolver= XMLHelper.DEFAULT_DTD_RESOLVER;
	static String[] strShiro=new String[]{"shiro.sso.server.url","shiro.sso.service.url","shiro.sso.login.url","shiro.sso.loginOut.url"};
	static String[] strSystem=new String[]{"area.org.code"};
	static String[] strJDBC=new String[]{"JDBC_USERNAME","JDBC_PASSWORD","JDBC_IP","JDBC_PORT","JDBC_DATABASE"};
	public static boolean updateXML(Map<String, String> paramMap) {
			 Boolean infos=updateContextXML(paramMap);
			return infos;  
	    } 
    /** 
    /** 
     * 修改pentaho配置文件context.xml文件
     */  
    @SuppressWarnings("deprecation")
	private static boolean updateContextXML(Map<String, String> paramMap) {
    	boolean info=false;
    	try {
			SAXReader saxReader =new SAXReader();
			String filePath=paramMap.get("contextAddress");
			File oldFile =new File(filePath);
			if(oldFile.exists()){
				Document doc =saxReader.read(oldFile);
				List list =doc.selectNodes("//Resource");
				Iterator it =list.iterator();
				while(it.hasNext()){
					Element element =(Element) it.next();
					element.setAttributeValue("username", paramMap.get("name"));
					element.setAttributeValue("password",  paramMap.get("password"));
					element.setAttributeValue("url", "jdbc:mysql://"+paramMap.get("pentahoAddress")+":"+paramMap.get("post")+"/"+paramMap.get("tableAddress"));
				}
				//删除原来xml文件
				oldFile.delete();
				XMLWriter output = new XMLWriter(new FileWriter(new File(filePath)));
				//重构新的xml
				output.write(doc);
				output.close();
				info=true;
			}
			
		} catch (Exception  ex) {
			ex.printStackTrace();
		}
		return info;  
    }  
    /** 
     * 修改或添加键值对 如果key存在，修改, 反之，添加。 
     * @param filePath 文件路径，即文件所在包的路径，例如：java/util/config.properties 
     * @param key 键 
     * @param value 键对应的值 
     */  
    public static boolean writeData(Map<String, String> paramMap) {     
    	boolean flg=false;
    	flg=writeData("/properties/shiro-client-default.properties",paramMap,strShiro);
    	if(flg)
    		flg=writeDataSys("/properties/system.properties",paramMap,strSystem);
    	if(flg)
    		flg=writeDataJDBC("/properties/jdbc.properties",paramMap,strJDBC);
    	if(flg)
    		flg =makeWar("/properties/jdbc.properties");
		return flg;
    } 
    /** 
     * 修改或添加键值对 如果key存在，修改, 反之，添加。 
     * @param filePath 文件路径，即文件所在包的路径，例如：java/util/config.properties 
     * @param key 键 
     * @param value 键对应的值 
     */  
    public static boolean makeWar(String filePath) {  
    	boolean flg=false;
    	//修改完成之后，更新war包
    		filePath=PropertiesUtil.class.getClassLoader().getResource(filePath).getFile();
    		String[] str=filePath.split("/WEB-INF/");
    		if(str.length>0){
    			//windows系统
    			//filePath=str[0]!=""?str[0].substring(1,str[0].length()):"";
    			//linux系统
    			filePath=str[0]!=""?str[0]:"";
    		}
    		try {
				Unzip.zip(filePath);
				flg=true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		return flg;
       
    } 
    /** 
     * 修改或添加键值对 如果key存在，修改, 反之，添加。 
     * @param filePath 文件路径，即文件所在包的路径，例如：java/util/config.properties 
     * @param key 键 
     * @param value 键对应的值 
     */  
    public static boolean writeData(String filePath, Map<String, String> paramMap,String[] str) {  
    	boolean flg=false;
        //获取绝对路径  
    	
    	Properties prop = new Properties();
    	try {
    		InputStream fis =ShiroClientConfig.class.getResourceAsStream(filePath);
			filePath=PropertiesUtil.class.getClassLoader().getResource(filePath).getFile();
            prop.load(fis);  
            //一定要在修改值之前关闭fis  
            fis.close();  
            OutputStream fos = new FileOutputStream(filePath);  
            for(String s:str){
            	prop.setProperty(s, paramMap.get(s)); 
            }
            //保存，并加入注释  
            prop.store(fos, "Update '" + str.toString() + "' value");  
            fos.close();  
            flg=true;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return flg;
       
    } 
    /** 
     * 修改或添加键值对 如果key存在，修改, 反之，添加。 
     * @param filePath 文件路径，即文件所在包的路径，例如：java/util/config.properties 
     * @param key 键 
     * @param value 键对应的值 
     */  
    public static boolean writeDataSys(String filePath, Map<String, String> paramMap,String[] str) {  
    	boolean flg=false;
        //获取绝对路径  
    	Properties prop = new Properties();
    	try {
    		InputStream fis =SystemConfig.class.getResourceAsStream(filePath);
			filePath=PropertiesUtil.class.getClassLoader().getResource(filePath).getFile();
            prop.load(fis);  
            //一定要在修改值之前关闭fis  
            fis.close();  
            OutputStream fos = new FileOutputStream(filePath);  
            for(String s:str){
            	prop.setProperty(s, paramMap.get(s)); 
            }
            //保存，并加入注释  
            prop.store(fos, "Update '" + str.toString() + "' value");  
            fos.close();  
            flg=true;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return flg;
       
    }  
    
    /** 
     * 修改或添加键值对 如果key存在，修改, 反之，添加。 
     * @param filePath 文件路径，即文件所在包的路径，例如：java/util/config.properties 
     * @param key 键 
     * @param value 键对应的值 
     */  
    public static boolean writeDataJDBC(String filePath, Map<String, String> paramMap,String[] str) {  
    	boolean flg=false;
        //获取绝对路径  
    	Properties prop1 = new Properties();
    	try {
    		InputStream fis1 =JDBCConfig.class.getResourceAsStream(filePath);
			filePath=PropertiesUtil.class.getClassLoader().getResource(filePath).getFile();
            prop1.load(fis1);  
            //一定要在修改值之前关闭fis  
            fis1.close();  
            OutputStream fos = new FileOutputStream(filePath);  
            for(String s:str){
            	prop1.setProperty(s, paramMap.get(s.toLowerCase())); 
            }
            
            //保存，并加入注释  
            prop1.store(fos, "Update '" + str.toString() + "' value");  
            fos.close();  
            flg=true;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return flg;
       
    }  
   
    
}  