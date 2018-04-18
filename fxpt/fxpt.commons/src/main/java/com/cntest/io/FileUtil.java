/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import com.cntest.exception.ExceptionHelper;
import com.cntest.util.CloseableHelper;


/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2013年12月27日
 * @version 1.0
 **/
public class FileUtil {
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	public static File inputStreamToFile(InputStream ins,String fileName) throws Exception{
		String root = System.getProperty("java.io.tmpdir");
		File file = new File(root + File.separator + System.currentTimeMillis() + "_" + fileName);
		OutputStream outputStream = null;
		outputStream = new FileOutputStream(file);
		FileCopyUtils.copy(ins, outputStream);
		return file;
	}
	
	public static void zip(String src,String dest) throws Exception {
		File f = new File(src);
		// File temp = new File(zipFileName);
		File zipFile = new File(dest);//new String(dest.getBytes(encoding),encoding)
		zipFile.createNewFile();
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

		// 设置压缩编码

		//out.setEncoding("GBK");// 设置为GBK后在windows下就不会乱码了，如果要放到Linux或者Unix下就不要设置了
		zip(out, f, "");// 递归压缩方法
		logger.debug("zip done");
		out.close();
	}

	private static void zip(ZipOutputStream out, File f, String base) throws Exception {
		logger.debug("Zipping   " + f.getName()); // 记录日志，开始压缩
		if (f.isDirectory()) { // 如果是文件夹，则获取下面的所有文件
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));// 此处要将文件写到文件夹中只用在文件名前加"/"再加文件夹名
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else { // 如果是文件，则压缩
			out.putNextEntry(new ZipEntry(base)); // 生成下一个压缩节点
			FileInputStream in = new FileInputStream(f); // 读取文件内容
			int len;
			byte[] buf = new byte[1024];
			while ((len = in.read(buf, 0, 1024)) != -1) {
				out.write(buf, 0, len); // 写入到压缩包
			}
			in.close();
		}
	}
	
	/**
	 * 删除文件，可以是单个文件或文件夹
	 * 
	 * @param fileName
	 *            待删除的文件名
	 * @return 文件删除成功返回true,否则返回false
	 */
	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			logger.warn("删除文件失败：{}文件不存在", fileName );
			return false;
		} else {
			if (file.isFile()) {
				return deleteFile(fileName);
			} else {
				return deleteDirectory(fileName);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param fileName
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true,否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
			logger.warn("删除文件失败：{}文件不存在", fileName );
			return true;
		} else {
			logger.warn("删除文件失败：{}文件不存在", fileName );
			return false;
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param dir
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true,否则返回false
	 */
	public static boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			logger.warn("删除目录失败{}目录不存在！", dir );
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			logger.warn("删除目录{}失败", dir );
			return false;
		}

		// 删除当前目录
		if (dirFile.delete()) {
			logger.debug("删除目录{}成功", dir );
			return true;
		} else {
			logger.warn("删除目录{}失败", dir );
			return false;
		}
	}
	
	public static void copyTo(File source,File target) {
		logger.debug("Copy File[{}] To [{}]",source.getAbsolutePath(),target.getAbsolutePath());
		//logger.debug(target.getAbsolutePath());
		File targetDir = target.getParentFile();
		if(!targetDir.exists()) {
			targetDir.mkdirs();
		}
		
		FileChannel in = null;  
	    FileChannel out = null;  
	    FileInputStream inStream = null;
	    FileOutputStream outStream = null;  
	    try {  
	    	target.createNewFile();
	        inStream = new FileInputStream(source);  
	        outStream = new FileOutputStream(target);  
	        in = inStream.getChannel();  
	        out = outStream.getChannel();  
	        in.transferTo(0, in.size(), out);
	        logger.debug("File Copy  Success!");
	    } catch (IOException e) {  
	    	//throw new RuntimeException("Cntest-rr","文件复制失败：" + source.getAbsolutePath()) ;
	    } finally {  
	    	CloseableHelper.close(inStream);
	    	CloseableHelper.close(in);
	    	CloseableHelper.close(outStream);
	    	CloseableHelper.close(out);
	    	 
	    }  
	}
	
	public static void main(String[] args)throws Exception{
		zip("","");
	}
}
