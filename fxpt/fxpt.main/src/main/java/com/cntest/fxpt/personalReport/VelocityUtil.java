package com.cntest.fxpt.personalReport;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


public class VelocityUtil {
	
	public static String getContent(String fileName,Map map) throws Exception{
		Properties config=new Properties();
		
		config.setProperty(Velocity.OUTPUT_ENCODING, "utf-8");
		config.setProperty(Velocity.INPUT_ENCODING, "utf-8");
		String path=getFileUrl()+"/resources/";
		config.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
		VelocityEngine ve=new VelocityEngine();
		ve.init(config);
		
		Template t=ve.getTemplate(fileName);
		
		VelocityContext context=new VelocityContext();
		
		Iterator it=map.keySet().iterator();
		while(it.hasNext()){
			String key=(String)it.next();
			context.put(key, map.get(key));
		}
		
		StringWriter sw=new StringWriter();
		t.merge(context, sw);
		//System.out.println(sw.toString());
		return sw.toString();
	}
	
	public static String getFileUrl() {
//        String url =  "/E:/hhc/cntest.fxpt/src/main/webapp";
		String url = "";
        WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
        if(webApplicationContext!=null){
        	 ServletContext servletContext = webApplicationContext.getServletContext();
             url = servletContext.getRealPath("/");
        }else{
        	url = VelocityUtil.class.getResource("").getPath().replace("/target/classes/com/cntest/fxpt/personalReport/", "/src/main/webapp");
        }
//        String temp = url.substring(0,1);
//        if(temp.equals("/")){
//        	url = url.substring(1);
//        }
//        if(url.endsWith("/")){
//        	url = url.substring(0,url.length()-1);
//        }
        return url;
    }
	
	public static void copyOtherFile(String[] files,String dir){
		String path=getFileUrl()+"/resource/";
		for(int i=0;i<files.length;i++){
			File f1=new File(path,files[i]);
			File f2=new File(dir,files[i]);
			if(!f1.getParentFile().exists()){
				f1.getParentFile().mkdirs();
			}
			if(!f2.getParentFile().exists()){
				f2.getParentFile().mkdirs();
			}
			if(!f2.exists()){
				copyFile(f1,f2);
			}
		}
	}
	
	public static void copyDir(String dir,String dir2){
		File f1=new File(dir);
		File[] files=f1.listFiles();
		for(int i=0;i<files.length;i++){
			File f=files[i];
			File newFile=new File(dir2,f.getName());
			if(!f.getName().endsWith("svn")){
				copyFile(f, newFile);
			}
		}
	}
	
	public static void copyFile(File oldFile,File newFile){
		try{
			if(!newFile.getParentFile().exists()){
				newFile.getParentFile().mkdirs();
			}
			FileInputStream fis=new FileInputStream(oldFile);
			FileOutputStream fos=new FileOutputStream(newFile);
			byte[] bytes=new byte[1024];
			int len=0;
			while((len=fis.read(bytes))!=-1){
				fos.write(bytes, 0, len);
			}
			fos.close();
			fis.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
