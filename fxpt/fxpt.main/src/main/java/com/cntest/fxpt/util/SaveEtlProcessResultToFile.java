/*
 * @(#)com.cntest.fxpt.util.SaveEtlProcessResult.java	1.0 2014年10月29日:下午5:08:15
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.util;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月29日 下午5:08:15
 * @version 1.0
 */
public class SaveEtlProcessResultToFile {
	private static String getUploadRoot() {
		File f = null;
		try {
			URL url = UploadHelper.class.getClassLoader().getResource("/");
			File tmpFile = new File(url.toURI());
			String filePath = tmpFile.getParentFile().getAbsolutePath()
					+ "/etlLog";
			f = new File(filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!f.exists()) {
			f.mkdirs();
		}
		return f.getAbsolutePath() + "/";
	}

	public static String getDir() {
		String dir = SystemConfig.newInstance().getValue("etl.log.dir");
		if (dir == null || dir.equals("")) {
			dir = getUploadRoot();
		}
		return dir;
	}

	public static void saveToFile(List<String> lines, String fileName)
			throws Exception {
		String dir = getDir();
		File f = new File(dir + fileName);
		FileUtils.writeLines(f, "GBK", lines);
	}
	
	public static double convertSubjectScoreToSX(Double fullScoreA,Double fullScoreB,String subjectName){
		double fs = -1;
		double sum = fullScoreA+fullScoreB;
		if("语文".equals(subjectName)||"数学".equals(subjectName)||"英语".equals(subjectName)){
			fs = sum;
		}else if("物理".equals(subjectName)){
			fs = fullScoreA/2 + fullScoreB;
		}else if("化学".equals(subjectName)){
			fs = (sum)/2;
		}else{
			if(sum>=80){
				fs = 20;
			}else if(sum>=70){
				fs = 16;
			}else if(sum>=60){
				fs = 12;
			}else{
				fs = 8;
			}
		}
		return fs;
	}
}
