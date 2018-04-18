package com.cntest.fxpt.personalReport;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class FileFactory {
	public static boolean createTxt(String fileName,String txt){
		try {
			File file=new File(fileName);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if(!file.exists()){
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), "UTF-8"));
			bw.write(txt);
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean createHtml(String fileName,String content){
		try {
			File file=new File(fileName);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if(!file.exists()){
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), "UTF-8"));
			bw.write(content);
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public static void createXML(String fileDir,String fileName,List<StringBuffer> listb) throws IOException{
		//--------------------------创建文件夹及文件----------------------------------------
		File file = new File(fileDir);
			 file.mkdir(); 
			 file = new File(fileDir+"/"+fileName+".xml");
			 file.createNewFile();
	    System.err.println(file.getName()+"已创建");
	    //----------------------------向文件中写内容------------------------------------------
	    FileWriter fileWriter = new FileWriter(fileDir+"/"+file.getName());
		String str="\r\n";
	    fileWriter.write("<?xml version='1.0' encoding='UTF-8' ?>");
	    fileWriter.write(str);
	    if(listb!=null && listb.size()>0){
	    	for (int i = 0; i < listb.size(); i++) {
	    		fileWriter.write(String.valueOf(listb.get(i))+str);
			}
	    }
	    fileWriter.flush();
	    fileWriter.close();
	}

}
