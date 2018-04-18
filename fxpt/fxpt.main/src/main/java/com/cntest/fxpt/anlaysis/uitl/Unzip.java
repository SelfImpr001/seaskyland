package com.cntest.fxpt.anlaysis.uitl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;  
  
/** 
 * 解压和压缩文件工具类
 * @author chenyou
 */  
public class Unzip {  

	 /** 
     * 解压缩 
     * @param sZipPathFile 要解压的文件 
     * @param sDestPath 解压到某文件夹 
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static ArrayList Ectract(String sZipPathFile, String sDestPath) {  
        ArrayList<String> allFileName = new ArrayList<String>();  
        try {  
            // 先指定压缩档的位置和档名，建立FileInputStream对象  
            FileInputStream fins = new FileInputStream(sZipPathFile);  
            // 将fins传入ZipInputStream中  
            ZipInputStream zins = new ZipInputStream(fins);  
            ZipEntry ze = null;  
            byte[] ch = new byte[256];  
            while ((ze = zins.getNextEntry()) != null) {  
                File zfile = new File(sDestPath + ze.getName());  
                //File zfile = new File(sDestPath +File.separator+ ze.getName());  
                File fpath = new File(zfile.getParentFile().getPath());  
                if (ze.isDirectory()) {  
                    if (!zfile.exists())  
                        zfile.mkdirs();  
                    zins.closeEntry();  
                } else {  
                    if (!fpath.exists())  
                        fpath.mkdirs();  
                    FileOutputStream fouts = new FileOutputStream(zfile);  
                    int i;  
                    allFileName.add(zfile.getAbsolutePath());  
                    while ((i = zins.read(ch)) != -1)  
                        fouts.write(ch, 0, i);  
                    zins.closeEntry();  
                    fouts.close();  
                }  
            }  
            fins.close();  
            zins.close();  
        } catch (Exception e) {  
            System.err.println("Extract error:" + e.getMessage());  
        }  
        return allFileName;  
    }  
    /**
     * 压缩文件
     * 
     * @param filePath
     *            待压缩的文件路径
     * @return 压缩后的文件
     * @throws IOException 
     */
    public static File zip(String filePath) throws IOException {
        File target = null;
        File source = new File(filePath);
        if (source.exists()) {
            // 压缩文件名=源文件名.zip
            String zipName = source.getName()+ ".war.update";
            target = new File(source.getParent(), zipName);
            if (target.exists()) {
	             target.delete();
	            // target.renameTo(new File(source.getName()+ ".war."+new Date())); // 备份旧war包
            }
            FileOutputStream fos = null;
            ZipOutputStream zos = null;
            try {
                fos = new FileOutputStream(target);
                zos = new ZipOutputStream(new BufferedOutputStream(fos));
                File[] sour =source.listFiles();
                // 添加对应的文件Entry
                addEntry("/", source, zos,true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
            	if(null!=zos) zos.close();
            	if(null!=fos) fos.close();
            }
        }
        return target;
    }
    
    /**
     * 扫描添加文件Entry
     * 
     * @param base
     *            基路径
     * 
     * @param source
     *            源文件
     * @param zos
     *            Zip文件输出流
     * @throws IOException
     */
    @SuppressWarnings("unused")
	private static void addEntry(String base, File source, ZipOutputStream zos,boolean flg)
            throws IOException {
        // 按目录分级，形如：/aaa/bbb.txt
    	String entry = base + source.getName();
    	if(flg){
    		entry="";
    	}
        if (source.isDirectory()) {
        	if(!flg){
        		entry+="/";
        	}
            for (File file : source.listFiles()) {
                // 递归列出目录下的所有文件，添加文件Entry
                addEntry(entry, file, zos,false);
            }
        } else {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                byte[] buffer = new byte[1024 * 10];
                fis = new FileInputStream(source);
                bis = new BufferedInputStream(fis, buffer.length);
                int read = 0;
                zos.putNextEntry(new ZipEntry(entry));
                while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                    zos.write(buffer, 0, read);
                }
                zos.closeEntry();
            } finally {
            	bis.close();
            	fis.close();
            }
        }
    }
}  