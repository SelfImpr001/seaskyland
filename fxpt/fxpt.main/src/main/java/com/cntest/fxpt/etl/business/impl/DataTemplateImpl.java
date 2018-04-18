/*
 * @(#)com.cntest.fxpt.etl.business.impl.StudentTemplateImpl.java	1.0 2014年10月21日:上午11:17:05
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.etl.business.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.etl.business.IImportTemplateService;
import com.cntest.fxpt.service.etl.IDataFieldService;

/**
 * <Pre>
 * </Pre>
 */
@Service("Data.TemplateService")
public class DataTemplateImpl extends AbstractTemplateImpl implements
		IImportTemplateService {

	SessionFactory sessionFactory;
	
	@Resource(name="sessionFactory")
	public void setSessionFactory0(SessionFactory sessionFactory){
	  this.sessionFactory = sessionFactory;
	 
	}
	@Autowired(required = false)
	@Qualifier("etl.IDataFieldService")
	private IDataFieldService dataFieldService;
	
	
	@Override
	public void template(OutputStream out) throws Exception {
	
	}
	
	protected SQLQuery createSQLQuery(String queryString) {
		Session session = getSession();
		return session.createSQLQuery(queryString);
	}
	
	protected Session getSession() {
		try {
			return this.sessionFactory.getCurrentSession();
		}catch(Exception e) {
			
		}
		return this.sessionFactory.openSession();
	}

	@Override
	public void template(OutputStream out, HttpServletRequest request, HttpServletResponse response) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(out);
		WritableSheet sheet = wb.createSheet("成绩信息", 0);
		int type = Integer.parseInt(request.getParameter("type"));
		String examid = request.getParameter("examid");
		String orgName = request.getParameter("orgname");
		String wl = request.getParameter("wl");
		String className = request.getParameter("clazzsname");
		//获取学生信息

		String sql = "SELECT stuName,StuNo,wlType,orgName,schoolname,yztype,totalScore FROM smalllanguage_info where examid=? and wlType like ? and (orgName=? OR schoolName=? or CityName=?)";
		if(className!=null && className!=""){
			sql = "SELECT stuName,StuNo,wlType,orgName,schoolname,yztype,totalScore FROM smalllanguage_info where examid=? and wlType like ? and (orgName=? OR schoolName=? or CityName=?) and className=?";
		}
		
		List<String> subjectnamesW = new ArrayList<String>();
		subjectnamesW.add("语文");
		subjectnamesW.add("文数");
		subjectnamesW.add("英语");
		subjectnamesW.add("文综");
		subjectnamesW.add("政治");
		subjectnamesW.add("历史");
		subjectnamesW.add("地理");
		subjectnamesW.add("小语种");
		
		List<String> subjectnamesL = new ArrayList<String>();
		subjectnamesL.add("语文");
		subjectnamesL.add("理数");
		subjectnamesL.add("英语");
		subjectnamesL.add("理综");
		subjectnamesL.add("物理");
		subjectnamesL.add("化学");
		subjectnamesL.add("生物");
		subjectnamesL.add("小语种");

		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setString(0, examid);
		sqlQuery.setString(1,  "%"+wl+"%");
		sqlQuery.setString(2, orgName);
		sqlQuery.setString(3, orgName);
		sqlQuery.setString(4, orgName);
		if(className!=null && className!=""){
			sqlQuery.setString(5, className);
		}
		List stulist = sqlQuery.list();
		
//		if(type>2){	
//			sql = "SELECT subjectName FROM smalllanguage_subjectscores WHERE examid=?  and subjectName<>'英语' GROUP BY subjectName ORDER BY subjectName DESC";
//		}else{
//			sql = "SELECT subjectName FROM smalllanguage_subjectscores WHERE examid=? GROUP BY subjectName ORDER BY subjectName DESC";
//		}
//		//获取科目信息
//		
//		sqlQuery = createSQLQuery(sql);
//		sqlQuery.setString(0, examid);
//		List subjectNames = sqlQuery.list();
		
		List subjectNames = subjectnamesL;
		if(wl.contains("文")){
			subjectNames = subjectnamesW;
		}
		
		//获取科目数据
		sql = "SELECT stuNo,subjectName,score,isQk FROM smalllanguage_subjectscores WHERE examid=? ORDER BY id";
		sqlQuery = createSQLQuery(sql);
		sqlQuery.setString(0, examid);
		List subjectScores = sqlQuery.list();
		Map<String,Object[]>subjectDataMap = new HashMap<String,Object[]>();
		for(Object res : subjectScores){
			Object[] result = (Object[]) res;
			String stuNo = result[0].toString();
			String subjectName = result[1].toString();
			String score = result[2].toString();
			boolean isQk = (boolean) result[3];
			Object[] obj = {score,isQk};
			subjectDataMap.put(stuNo+"_"+subjectName, obj);
			//数据中暂未有真实的小语种,暂用英语代替，后续删除
			if ("英语".equals(subjectName)) {
				subjectDataMap.put(stuNo+"_小语种", obj);
			}
		}
		
		List<String> names = new ArrayList<String>();
		names.add("考生姓名");
		names.add("考生号");
		names.add("考生类别");
		names.add("所属区域");
		names.add("学校");
		names.add("语种");
		names.add("总分");
		
		int idx = 0;
		for(String name : names){
			sheet.setColumnView(idx, 15);//设置列宽
			Label label = new Label(idx, 0, name, getTextFormat());
			sheet.mergeCells(idx,0,0,1);
			idx++;
			sheet.addCell(label);
		}
		
		List<String> newSubjectList=  new 	ArrayList<String>();
		for(Object res : subjectNames){
			String subjectName = res.toString();
			if(type>2){
				if(subjectName.equals("英语")){
					continue;
				}
			}
			if(wl.contains("文")){
				if(!subjectName.equals("理综") & !subjectName.equals("物理") & !subjectName.equals("化学") & !subjectName.equals("生物") & !subjectName.equals("理数")){
					newSubjectList.add(subjectName);
				}
			}
			if(wl.contains("理")){
				if(!subjectName.equals("文综") & !subjectName.equals("政治") & !subjectName.equals("历史") & !subjectName.equals("地理") & !subjectName.equals("文数")){
					newSubjectList.add(subjectName);
				}
			}
		}

		
		//组装表头
		for(Object res : newSubjectList){
			String subjectName =res.toString();
				sheet.setColumnView(idx, 15);//设置列宽
				if(type<3){	
					Label label = new Label(idx, 0, subjectName, getTextFormat());
					sheet.mergeCells(idx,0,idx+1,0);
					sheet.setColumnView(idx+1, 15);//设置列宽
					Label label1 = new Label(idx, 1, "分数", getTextFormat());
					Label label2 = new Label(idx+1, 1, "是否缺考", getTextFormat());
					sheet.addCell(label);
					sheet.addCell(label1);
					sheet.addCell(label2);
					idx+=2;
				}else{
					Label label = new Label(idx, 0, subjectName, getTextFormat());
					sheet.mergeCells(idx,0,0,1);
					sheet.setColumnView(idx+1, 15);//设置列宽
					sheet.addCell(label);
					idx++;
				}
		}
		
		int rowIndex = 2;
		//输出数据
		for(Object res : stulist){
			Object[] result = (Object[]) res;
			int colIndex = 0;
			String stuNo = result[1].toString();
			if(type>2){
				if(subjectDataMap.get(stuNo+"_"+"小语种")==null){
					continue;
				}
			}
			for(int i =0;i<result.length;i++){
				Label label = new Label(colIndex++,rowIndex, result[i].toString(), getTextFormat());
				sheet.addCell(label);
			}
		
			for(Object sub : newSubjectList){
				String subjectName =sub.toString();
				String key = stuNo+"_"+subjectName;
				Object[] subjectResult = subjectDataMap.get(key);
				if(subjectResult!=null){
					String score = subjectResult[0].toString();
					boolean isQk = (boolean) subjectResult[1];
					Label label = new Label(colIndex++,rowIndex, String.valueOf(score), getTextFormat());
					sheet.addCell(label);
					if(type<3){
						label = new Label(colIndex++,rowIndex, isQk==true?"Y":"N", getTextFormat());
						sheet.addCell(label);
					}
				}else{
					Label label = new Label(colIndex++,rowIndex, "--", getTextFormat());
					sheet.addCell(label);
					if(type<3){
						label = new Label(colIndex++,rowIndex, "--", getTextFormat());
						sheet.addCell(label);
					}
				}
			}
			rowIndex++;
		}
		wb.write();
		wb.close();
	}

}
