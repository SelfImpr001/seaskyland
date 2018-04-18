/*
 * @(#)com.cntest.fxpt.repository.impl.SynUsersDaoImpl.java	1.0 2016年4月8日:下午3:46:05
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFileChooser;

import jxl.CellView;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.net.io.SocketOutputStream;
import org.apache.poi.hssf.util.HSSFColor.GREY_25_PERCENT;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.util.IOUtils;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.exception.BusinessException;
import com.cntest.foura.application.permission.PasswordService;
import com.cntest.foura.service.RoleService;
import com.cntest.foura.service.impl.UserServiceImpl;
import com.cntest.fxpt.domain.SynUsers;
import com.cntest.fxpt.repository.ISynUsersDao;
import com.cntest.fxpt.util.FileFactory;
import com.cntest.fxpt.util.UploadHelper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 黄洪成 2016年4月8日 下午3:46:05
 * @version 1.0
 */
@Repository("ISynUsersDao")
public class SynUsersDaoImpl extends AbstractHibernateDao<SynUsers, Integer> implements
		ISynUsersDao{
	private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private PasswordService passwordService;
	
	@Autowired
	private RoleService roleService;
	

	@Override
	protected Class<SynUsers> getEntityClass() {
		return SynUsers.class;
	}

	@Override
	public SynUsers findByid(Long pk) {
		String hql = "from SynUsers where id=?";
		Query query = getSession().createQuery(hql);
		query.setLong(0, pk);
		List list = query.list();
		if(list!=null && list.size()>0)
			return (SynUsers) list.get(0);
		return null;
	}
	
	@Override
	public int loadByCode(String code) {
		String sql = "SELECT gradeid FROM `kn_userandpassword`  WHERE code=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, code);
		Object obj = sqlQuery.uniqueResult();
		int result = -1;
		if (obj instanceof Integer) {
			result = (Integer) obj;
		} else if (obj instanceof BigInteger) {
			BigInteger tmp = (BigInteger) obj;
			result = tmp.intValue();
		}
		return result;
	}

	@Override
	public void createUsers(Long examid) {
				/**
				 * 1.生成账号
				 */
				try {
				    //创建临时表
					String sql = "CREATE TEMPORARY TABLE IF NOT EXISTS tmp_userAndPassword LIKE kn_userandpassword";
					Query query = getSession().createSQLQuery(sql);
					query.executeUpdate();
					//清空临时表
					sql = "DELETE FROM tmp_userandpassword where 1=1";
					this.jdbcTemplate.execute(sql);
					//插入用户表4a_user、用户明细表4a_user_detail
					initUsers(examid);
					//将临时表数据复制到正式表
					sql = "insert into kn_userandpassword SELECT t.* FROM tmp_userAndPassword t LEFT JOIN kn_userandpassword u ON t.code=u.code WHERE u.code IS NULL;";
					query = getSession().createSQLQuery(sql);
					int i = query.executeUpdate();
					logger.debug("临时表插入正式表人数->exmiad:"+examid+" num:"+i);
					//20161012  批量新增人员加入考试数据
					insertToKn_User_Exam(examid);
					//-------------------------------
					//删除临时表
					sql = "DROP TABLE IF EXISTS tmp_userAndPassword";
					query = getSession().createSQLQuery(sql);
					query.executeUpdate();
				} catch (BusinessException e) {
					e.printStackTrace();
					logger.error("生成账号出错");
				}
	}
	
	/**
	 * 
	* <Pre>
	* 按科目来插入用户  学科老师
	* </Pre>
	* 
	* @param examid
	* @param codes
	* @param syn
	* @return void
	* @author:黄洪成 2016年4月27日 下午2:36:04
	 */
	public void initUsersFosSubjectsTeacher(Long examid,List<String> codes,SynUsers syn){
		//查询出本次考试所有科目
		String allSubjectSQL = "SELECT s.id,s.name FROM dw_testpapercj_fact f INNER JOIN kn_subject s ON s.id=f.subjectid WHERE f.examid=? GROUP BY f.subjectid";
		Query query = getSession().createSQLQuery(allSubjectSQL);
		query.setParameter(0, examid);
		List<Object[]> subjectList = query.list();
		for (Object[] subject : subjectList) {
			//查询生成账号
			StringBuffer userSql = new StringBuffer();
			userSql.append(" INSERT INTO tmp_userAndPassword (syn_id,examid,provinceCode,provinceName,cityCode,cityName,countyCode,countyName,schoolid,schoolCode,schoolName,gradeId,classid,classCode,className,CODE,PASSWORD,subjectid,subjectname)");
			userSql.append("SELECT ?,class.examid,school.provinceCode,school.provinceName,school.cityCode,school.cityName,school.countyCode, school.countyName,school.id schoolid,school.code schoolCode,school.name schoolName,e.gradeId,class.id classid,class.code classCode,class.name className,CONCAT(");
			String code = "";
			for (int i = 0; i < codes.size(); i++) {
				if(codes.get(i).equals("provinceCode")){
					code+="school.provinceCode,";
				}else if(codes.get(i).equals("cityCode")){
					code+="school.cityCode,";
				}else if(codes.get(i).equals("countyCode")){
					code+="school.countyCode,";
				}else if(codes.get(i).equals("schoolCode")){
					code+="school.code,";
				}else if(codes.get(i).equals("gradeCode")){
					code+="LPAD(e.gradeId,2,0),";
				}else if(codes.get(i).equals("classCode")){
					code+="class.code,";
				}else if(codes.get(i).equals("subjectCode")){
					code+="'"+getPinYin(subject[1].toString())+"'";
				}
			}
			userSql.append(code);
			userSql.append(") AS CODE,");
			if (syn.getPasswordrules().equals("any")) {
				userSql.append("FLOOR(RAND()*500000+500000)"); 
			}else{
				userSql.append(syn.getDefaultpassword());
			}
			userSql.append(" AS PASSWORD, "+subject[0]+" as subjectid,'"+subject[1]+"' as subjectname FROM dw_dim_class class");
			userSql.append(" INNER JOIN kn_exam e");
			userSql.append(" ON e.id=class.examId");
			userSql.append(" INNER JOIN dw_dim_school school ON school.code=class.schoolCode");
			userSql.append(" WHERE class.examId=?;");
			query = getSession().createSQLQuery(userSql.toString());
			query.setParameter(0, syn.getId());
			query.setParameter(1, examid);
			int num = query.executeUpdate();
		}
	}
	
	
	/**
	 * 
	* <Pre>
	* 按科目来插入用户  校长
	* </Pre>
	* 
	* @param examid
	* @param codes
	* @param syn
	* @return void
	* @author:黄洪成 2016年4月27日 下午2:36:04
	 */
	public void initUsersForHeadTeacher(Long examid,List<String> codes,SynUsers syn){
		StringBuffer userSql = new StringBuffer();
		userSql.append(" INSERT INTO tmp_userAndPassword (syn_id,examid,provinceCode,provinceName,cityCode,cityName,countyCode,countyName,schoolid,schoolCode,schoolName,CODE,PASSWORD)");
		userSql.append("SELECT ?,class.examid,school.provinceCode,school.provinceName,school.cityCode,school.cityName,school.countyCode, school.countyName,school.id schoolid,school.code schoolCode,school.name schoolName,CONCAT(");
		String code = "";
		for (int i = 0; i < codes.size(); i++) {
			if (codes.get(i).equals("provinceCode")) {
				code += "school.provinceCode,";
			} else if (codes.get(i).equals("cityCode")) {
				code += "school.cityCode,";
			} else if (codes.get(i).equals("countyCode")) {
				code += "school.countyCode,";
			} else if (codes.get(i).equals("schoolCode")) {
				code += "school.code,";
			} else if (codes.get(i).equals("gradeCode")) {
				code += "LPAD(e.gradeId,2,0),";
			} else if (codes.get(i).equals("classCode")) {
				code += "class.code,";
			}
		}
		code = code.substring(0, code.length() - 1);
		userSql.append(code);
		userSql.append(") AS CODE,");
		if (syn.getPasswordrules().equals("any")) {
			userSql.append("FLOOR(RAND()*500000+500000)");
		} else {
			userSql.append(syn.getDefaultpassword());
		}
		userSql.append(" FROM dw_dim_class class INNER JOIN kn_exam e");
		userSql.append(" ON e.id=class.examId");
		userSql.append(" INNER JOIN dw_dim_school school ON school.code=class.schoolCode");
		userSql.append(" WHERE class.examId=? GROUP BY CODE;");
		Query query = getSession().createSQLQuery(userSql.toString());
		query.setParameter(0, syn.getId());
		query.setParameter(1, examid);
		int num = query.executeUpdate();
		
	}
	
	/**
	 * 
	* <Pre>
	* 按科目来插入用户  班主任
	* </Pre>
	* 
	* @param examid
	* @param codes
	* @param syn
	* @return void
	* @author:黄洪成 2016年4月27日 下午2:36:04
	 */
	public void initUsersFoTeacher(Long examid,List<String> codes,SynUsers syn){
		//班主任生成
		StringBuffer userSql = new StringBuffer();
		userSql.append(" INSERT INTO tmp_userAndPassword (syn_id,examid,provinceCode,provinceName,cityCode,cityName,countyCode,countyName,schoolid,schoolCode,schoolName,gradeId,classid,classCode,className,CODE,PASSWORD)");
		userSql.append("SELECT ?,class.examid,school.provinceCode,school.provinceName,school.cityCode,school.cityName,school.countyCode, school.countyName,school.id schoolid,school.code schoolCode,school.name schoolName,e.gradeId,class.id classid,class.code classCode,class.name className,CONCAT(");
		String code = ""; 
		for (int i = 0; i < codes.size(); i++) {
			if (codes.get(i).equals("provinceCode")) {
				code += "school.provinceCode,";
			} else if (codes.get(i).equals("cityCode")) {
				code += "school.cityCode,";
			} else if (codes.get(i).equals("countyCode")) {
				code += "school.countyCode,";
			} else if (codes.get(i).equals("schoolCode")) {
				code += "school.code,";
			} else if (codes.get(i).equals("gradeCode")) {
				code += "LPAD(e.gradeId,2,0),";
			} else if (codes.get(i).equals("classCode")) {
				code += "class.code,";
			}
		}
		code = code.substring(0, code.length() - 1);
		userSql.append(code);
		userSql.append(") AS CODE,");
		if (syn.getPasswordrules().equals("any")) {
			userSql.append("FLOOR(RAND()*500000+500000)");
		} else {
			userSql.append(syn.getDefaultpassword());
		}
		userSql.append(" FROM dw_dim_class class INNER JOIN kn_exam e");
		userSql.append(" ON e.id=class.examId");
		userSql.append(" INNER JOIN dw_dim_school school ON school.code=class.schoolCode");
		userSql.append(" WHERE class.examId=?;");
		Query query = getSession().createSQLQuery(userSql.toString());
		query.setParameter(0, syn.getId());
		query.setParameter(1, examid);
		int num = query.executeUpdate();
	}
	
	/**
	 * 
	* <Pre>
	* // 所有科目授权
	* </Pre>
	* 
	* @param examid
	* @param syn
	* @return void
	* @author:黄洪成 2016年4月28日 上午9:27:15
	 */
	public void grantAllSubjects(Long examid,SynUsers syn){
		//判断科目成绩是否存在
		String sql ="select * from dw_testpapercj_fact where examid="+examid;
		Query query = getSession().createSQLQuery(sql);
		if(query.list().size()>0){
			StringBuffer insertDataBySubject = new StringBuffer();
			insertDataBySubject.append("INSERT INTO 4a_data_authorized (target_id,target,data_permission_id,data_from_table,data_from_id,permission_name,permission_value)");
			insertDataBySubject.append(" SELECT u.user_id,'user',1,'kn_subject',d.subjectid,d.subjectname,d.subjectid FROM tmp_userAndPassword p");
			insertDataBySubject.append(" INNER JOIN 4a_user u");
			insertDataBySubject.append(" ON p.code=u.user_name");
			insertDataBySubject.append(" LEFT JOIN (");
			insertDataBySubject.append(" SELECT f.examId,s.id AS subjectid,s.name AS  subjectname");
			insertDataBySubject.append(" FROM dw_testpapercj_fact f ");
			insertDataBySubject.append(" INNER JOIN kn_subject s");
			insertDataBySubject.append(" ON s.id=f.subjectid ");
			insertDataBySubject.append(" GROUP BY f.examId,f.subjectid ) d");
			insertDataBySubject.append("  ON p.examid=d.examid");
			insertDataBySubject.append(" WHERE p.examid=? and p.syn_id=?");
			query = getSession().createSQLQuery(insertDataBySubject.toString());
			query.setParameter(0, examid);
			query.setParameter(1, syn.getId());
			int num = query.executeUpdate();
			logger.debug("科目授权->exmiad:"+examid+" num:"+num);
		}else{
			logger.debug("科目授权->exmiad:"+examid+" num:"+0+";因为没有成绩信息");
		}
	}
	
	
	/**
	 * 
	* <Pre>
	* 对单个班级授权
	* </Pre>
	* 
	* @param examid
	* @param syn
	* @return void
	* @author:黄洪成 2016年4月28日 上午9:32:12
	 */
	private void grantSingleClass(Long examid,SynUsers syn){
		//2.班级授权
		StringBuffer insertDataByClass = new StringBuffer();
		insertDataByClass.append("INSERT INTO 4a_data_authorized (target_id,target,data_permission_id,data_from_table,data_from_id,permission_name,permission_value)");
		insertDataByClass.append(" SELECT u.user_id,'user',9,'dw_dim_class',p.classid,p.classname,p.classid FROM tmp_userAndPassword p");
		insertDataByClass.append(" INNER JOIN 4a_user u");
		insertDataByClass.append(" ON p.code=u.user_name");
		insertDataByClass.append(" WHERE p.examid=? and p.syn_id=?");
		Query query = getSession().createSQLQuery(insertDataByClass.toString());
		query.setParameter(0, examid);
		query.setParameter(1, syn.getId());
		int num = query.executeUpdate();
		logger.debug("班级授权->exmiad:"+examid+" num:"+num);
	}
	
	/**
	 * 
	* <Pre>
	* 对所有班级授权
	* </Pre>
	* 
	* @param examid
	* @param syn
	* @return void
	* @author:黄洪成 2016年4月28日 上午9:32:12
	 */
	private void grantAllClass(Long examid,SynUsers syn){
		//2.班级授权
		StringBuffer insertDataByClass = new StringBuffer();
		insertDataByClass.append("INSERT INTO 4a_data_authorized (target_id,target,data_permission_id,data_from_table,data_from_id,permission_name,permission_value)");
		insertDataByClass.append(" SELECT u.user_id,'user',9,'dw_dim_class',d.id,d.name,d.id FROM tmp_userAndPassword p");
		insertDataByClass.append(" INNER JOIN 4a_user u");
		insertDataByClass.append(" ON p.code=u.user_name");
		insertDataByClass.append(" LEFT JOIN (SELECT d.examid,d.id,d.name,d.schoolCode FROM dw_dim_class d  ) d");
		insertDataByClass.append(" ON p.examid=d.examid AND p.schoolCode=d.schoolCode");
		insertDataByClass.append(" WHERE p.examid=? and p.syn_id=?");
		Query query = getSession().createSQLQuery(insertDataByClass.toString());
		query.setParameter(0, examid);
		query.setParameter(1, syn.getId());
		int num = query.executeUpdate();
		logger.debug("班级授权->exmiad:"+examid+" num:"+num);
	}
	
	/**
	 * 
	* <Pre>
	* 对单个科目授权
	* </Pre>
	* 
	* @param examid
	* @param syn
	* @return void
	* @author:黄洪成 2016年4月28日 上午9:34:13
	 */
	private void grantSingleSubject(Long examid,SynUsers syn){
		StringBuffer insertDataBySubject = new StringBuffer();
		insertDataBySubject.append("INSERT INTO 4a_data_authorized (target_id,target,data_permission_id,data_from_table,data_from_id,permission_name,permission_value)");
		insertDataBySubject.append(" SELECT u.user_id,'user',1,'kn_subject',p.subjectid,p.subjectname,p.subjectid FROM tmp_userAndPassword p");
		insertDataBySubject.append(" INNER JOIN 4a_user u");
		insertDataBySubject.append(" ON p.code=u.user_name");
		insertDataBySubject.append(" WHERE p.examid=? and p.syn_id=?");
		Query query = getSession().createSQLQuery(insertDataBySubject.toString());
		query.setParameter(0, examid);
		query.setParameter(1, syn.getId());
		int num = query.executeUpdate();
		logger.debug("科目授权->exmiad:"+examid+" num:"+num);
	}
	
	
	/**
	* <Pre>
	* 插入用户表4a_user、用户明细表4a_user_detail
	* </Pre>
	* @param examid
	* @return void
	* @author:黄洪成 2016年4月15日 下午2:07:50
	 * @throws BusinessException 
	 */
	public void initUsers(Long examid) throws BusinessException {
		List<SynUsers> synusers = this.list();
		for (SynUsers syn : synusers) {
			if(syn.getIsSyn()!=1){
				continue;
			}
			int syn_id = syn.getId();
			List<String> codes = syn.getNr();
			boolean isHeadMaster = false;
			String userName = "("+roleService.load((long) syn.getRoleid()).getName()+")";
			//班主任生成
			//包含班级、学校；但不包含学科
			if((!(codes.contains("subjectCode")))&& codes.contains("classCode") && codes.contains("schoolCode")){
				initUsersFoTeacher(examid,codes,syn);
				isHeadMaster = true;
			}
			// 学科老师生成
			//包含学科、班级、学校
			if (codes.contains("subjectCode") && codes.contains("classCode") && codes.contains("schoolCode")) {
				initUsersFosSubjectsTeacher(examid, codes, syn);
				
			}  
			//校长生成
			//包含学校；但不包含学科、班级
			if((!codes.contains("subjectCode")) && (!codes.contains("classCode")) && codes.contains("schoolCode")) {
				initUsersForHeadTeacher(examid, codes, syn);
				isHeadMaster = true;
			}
			// 加密并替换
			String sql = "SELECT t.examid,t.code,t.password FROM tmp_userAndPassword t LEFT JOIN kn_userandpassword k ON t.code=k.code WHERE k.code IS NULL AND t.examid=? AND t.syn_id=?";
			Query query = getSession().createSQLQuery(sql);
			query.setParameter(0, examid);
			query.setParameter(1, syn_id);
			List<Object[]> list = new ArrayList<Object[]>();
			list = query.list();
			List<Object[]> reslist = new ArrayList<Object[]>();
			if (list != null && list.size() > 0) {
				for (Object[] objects : list) {
					Object[] obj = new Object[2];
					obj[0] = passwordService.encryptPassword(
							objects[2].toString(), objects[1].toString());
					obj[1] = objects[1].toString();
					reslist.add(obj);
				}
			}
			
			String update = "update tmp_userAndPassword set encryptpassword=? where code=?";
			for (int i = 0; i < reslist.size(); i++) {
				executeBySQL(update, reslist.get(i));
			}
			
			
			
			// 插入用户表4a_user
			String insertUserSql = "INSERT INTO 4a_user (user_name,user_pwd,STATUS,is_sys) SELECT a.* FROM(SELECT t.CODE,t.encryptpassword,1,2 FROM tmp_userAndPassword t LEFT JOIN kn_userAndPassword u ON t.code=u.code WHERE u.code IS NULL) a LEFT JOIN 4a_user r ON r.user_name=a.code WHERE r.user_name IS NULL;";
			query = getSession().createSQLQuery(insertUserSql);
			int num = query.executeUpdate();
			logger.debug("插入用户表4a_user"+userName+"->exmiad:" + examid + " num:" + num);
			if(num>0){
				// 插入用户明细表4a_user_detail
				String insertUserDeatilSql = "";
				if(isHeadMaster)
					insertUserDeatilSql = "INSERT INTO 4a_user_detail (user_id,nick_name,real_name,sex,telphone,cellphone,email) SELECT a.* FROM(SELECT u.user_id,CONCAT(p.schoolname,IFNULL(p.classname,''),'"+userName+"') nick_name,CONCAT(p.schoolname,IFNULL(p.classname,''),'"+userName+"') real_name,'UNKNOW','' a1,'' a2,'' a3 FROM tmp_userAndPassword p INNER JOIN 4a_user u ON p.code=u.user_name WHERE p.examid=? and p.syn_id=? ) a LEFT JOIN 4a_user_detail d ON d.user_id=a.user_id WHERE d.user_id IS NULL";	
				else
					insertUserDeatilSql = "INSERT INTO 4a_user_detail (user_id,nick_name,real_name,sex,telphone,cellphone,email) SELECT a.* FROM(SELECT u.user_id,CONCAT(p.schoolname,CONCAT(p.gradeid,'年级'),IFNULL(p.classname,''),concat(p.subjectname,'老师')) nick_name,CONCAT(p.schoolname,CONCAT(p.gradeid,'年级'),IFNULL(p.classname,''),concat(p.subjectname,'老师')) real_name,'UNKNOW','' a1,'' a2,'' a3 FROM tmp_userAndPassword p INNER JOIN 4a_user u ON p.code=u.user_name WHERE p.examid=? and p.syn_id=? ) a LEFT JOIN 4a_user_detail d ON d.user_id=a.user_id WHERE d.user_id IS NULL";
				query = getSession().createSQLQuery(insertUserDeatilSql);
				query.setParameter(0, examid);
				query.setParameter(1, syn_id);
				num = query.executeUpdate();
				logger.debug("插入用户明细表4a_user_detail"+userName+"->exmiad:" + examid + " num:"+ num);
	
				// 插入用户角色表4a_user_role
				String insertUserRoleSql = "INSERT INTO 4a_user_role (user_id,role_id) SELECT u.user_id,? FROM 4a_user u INNER JOIN tmp_userAndPassword p ON u.user_name=p.code LEFT JOIN 4a_user_role r ON r.user_id=u.user_id WHERE p.examid=? and p.syn_id=? AND r.user_id IS NULL";
				query = getSession().createSQLQuery(insertUserRoleSql);
				query.setParameter(0, syn.getRoleid());
				query.setParameter(1, examid);
				query.setParameter(2, syn_id);
				num = query.executeUpdate();
				logger.debug("插入用户角色表4a_user_role"+userName+"->exmiad:" + examid + " num:"
						+ num);
				
				/**
				 *  根据角色来授功能权限
				 */
				initUsersResource(examid, syn);
				
				/**
				 * 根据角色来授权数据权限
				 */
				//班主任生成
				//包含班级、学校；但不包含学科
				if((!(codes.contains("subjectCode")))&& codes.contains("classCode") && codes.contains("schoolCode")){
					//1.对班主任所有科目授权
					grantAllSubjects(examid,syn);
					//2.对班主任单个学校授权
	//				grantSingleSchool(examid, syn);
					//3.对班主任单个班级授权
					grantSingleClass(examid, syn);
				}
				// 学科老师生成
				//包含学科、班级、学校
				if (codes.contains("subjectCode") && codes.contains("classCode") && codes.contains("schoolCode")) {
					//1.单个科目授权
					grantSingleSubject(examid, syn);
					//2.单个学校授权
	//				grantSingleSchool(examid, syn);
					//2.单个班级授权
					grantSingleClass(examid, syn);
				}  
				//校长生成
				//包含学校；但不包含学科、班级
				if((!codes.contains("subjectCode")) && (!codes.contains("classCode")) && codes.contains("schoolCode")) {
					// 对学校授权
					grantSingleSchool(examid, syn);
					// 对所有班级授权
					grantAllClass(examid, syn);
					// 对所有科目授权
					grantAllSubjects(examid, syn);
				}
			}
			
		}
	}
	
	/**
	 * 
	* <Pre>
	* 批量插入用户时，给批量用户新增考试信息
	* </Pre>
	* 
	* @param examid
	* @return void
	* @author:黄洪成 2016年10月12日 下午5:28:27
	 */
	public void insertToKn_User_Exam(Long examid){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into kn_user_exam(user_id,exam_id,ue_extendproperty)");
		sql.append("SELECT a.* FROM(SELECT a.user_id,u.examid,a.user_name FROM tmp_userAndPassword u LEFT JOIN 4a_user a");
		sql.append(" ON u.code=a.user_name ");
		sql.append(" ) a LEFT JOIN kn_user_exam e");
		sql.append(" ON a.user_id=e.user_id AND a.examid=e.exam_id WHERE e.exam_id IS NULL;");
		Query query = getSession().createSQLQuery(sql.toString());
		int num = query.executeUpdate();
		logger.debug("给批量用户新增考试信息->exmiad:"+examid+" num:"+num);
	}
	
	/**
	* <Pre>
	* 按角色菜单分配新生成的用户
	* 插入用户资源4a_user_resource 
	* </Pre>
	* @param examid
	* @return void
	* @author:黄洪成 2016年4月15日 下午2:12:15
	 */
	public void initUsersResource(Long examid,SynUsers syn){
			int roleid = syn.getRoleid();
			StringBuffer insertUserResource = new StringBuffer();
			insertUserResource.append("INSERT INTO 4a_user_resource (user_id,res_id,ur_order)");
			insertUserResource.append(" SELECT d.user_id,rr.res_id,rr.res_order FROM 4a_role_resource rr ");
			insertUserResource.append(" LEFT JOIN (");
			insertUserResource.append(" SELECT u.user_id FROM 4a_user_resource r");
			insertUserResource.append(" RIGHT JOIN 4a_user u");
			insertUserResource.append(" ON u.user_id=r.user_id INNER JOIN tmp_userAndPassword p ON p.code=u.user_name WHERE p.examid=? and p.syn_id=? AND r.user_id IS  NULL) d");
			insertUserResource.append(" ON 1=1 WHERE rr.role_id=? AND d.user_id IS NOT NULL;");
			Query query = getSession().createSQLQuery(insertUserResource.toString());
			query.setParameter(0, examid);
			query.setParameter(1, syn.getId());
			query.setParameter(2, roleid);
			int num = query.executeUpdate();
			logger.debug("插入用户资源->exmiad:"+examid+" roleid:"+roleid+" num:"+num);
			
			
			//用户与组合关系表4a_userbelong
			StringBuffer insertUserBelongSql = new StringBuffer();
			insertUserBelongSql.append("INSERT INTO 4a_userbelong (uBelong_joinDate,uBelong_available,user_id,org_id)");
			insertUserBelongSql.append("SELECT CURRENT_TIMESTAMP(),1,u.user_id,p.schoolid FROM 4a_user u INNER JOIN tmp_userAndPassword p ON u.user_name=p.code LEFT JOIN 4a_userbelong r ON r.user_id=u.user_id WHERE p.examid=? and p.syn_id=? AND r.user_id IS NULL");
			query = getSession().createSQLQuery(insertUserBelongSql.toString());
			query.setParameter(0, examid);
			query.setParameter(1, syn.getId());
			num = query.executeUpdate();
			logger.debug("插入用户与组合关系表4a_userbelong->exmiad:"+examid+" roleid:"+roleid+" num:"+num);
	}
	/**
	 * 
	* <Pre>
	* 单个学校授权 4a_data_authorized
	* </Pre>
	* 
	* @param examid
	* @return void
	* @author:黄洪成 2016年4月15日 下午2:55:20
	 */
	public void grantSingleSchool(Long examid,SynUsers syn){
		//2.学校授权
		StringBuffer insertDataBySchool = new StringBuffer();
		insertDataBySchool.append("INSERT INTO 4a_data_authorized (target_id,target,data_permission_id,data_from_table,data_from_id,permission_name,permission_value)");
		insertDataBySchool.append(" SELECT u.user_id,'user',9,'4a_org',p.schoolid,p.schoolName,p.schoolid FROM tmp_userAndPassword p");
		insertDataBySchool.append(" INNER JOIN 4a_user u");
		insertDataBySchool.append(" ON p.code=u.user_name");
		insertDataBySchool.append(" WHERE p.examid=? and p.syn_id=?");
		Query query = getSession().createSQLQuery(insertDataBySchool.toString());
		query.setParameter(0, examid);
		query.setParameter(1, syn.getId());
		int num = query.executeUpdate();
		logger.debug("学校授权->exmiad:"+examid+" num:"+num);
	}
	
	
	
	
	/**
	 * 
	* <Pre>
	* 汉字转换成拼音
	* </Pre>
	* 
	* @param str
	* @return
	* @return String
	* @author:黄洪成 2016年4月14日 下午2:39:11
	 */
	public static String getPinYin(String str){
		char[] t1 = null;
		t1 = str.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4="";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				if(Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]")){
					t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i],t3);
					t4+=t2[0]+"";
				}else{
					t4+=Character.toString(t1[i]);
				}
				
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return t4;
	}
	
	/**
	 * 年级定义
	 */
	public static String[] GRADES = new String[] { "一", "二", "三", "四", "五", "六", "七", "八", "九", "高一", "高二", "高三" };

	
	public static void main(String[] args) {
		System.out.println(getPinYin("黄洪成"));
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		System.out.println(dateFormat.format(date));
	}

	
	private Map<String, List<Object[]>> getAllUsersBySchools(){
		Map<String, List<Object[]>> res = new HashMap<String, List<Object[]>>();
		List<Object[]> list = new ArrayList<Object[]>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT u.code,u.password,r.role_name,u.schoolCode,u.schoolName,u.gradeid,u.className,u.subjectname FROM kn_userandpassword u");
		sql.append(" INNER JOIN kn_synusers s ON u.syn_id=s.id INNER JOIN 4a_role r ON r.role_id=s.roleid");
		Query query = getSession().createSQLQuery(sql.toString());
		list = query.list();
		if(list!=null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = list.get(i);
				String schoolName = obj[4].toString();
				if(res.containsKey(schoolName)){
					List<Object[]> resList  = res.get(schoolName);
					resList.add(obj);
					res.put(schoolName, resList);
				}else{
					List<Object[]> resList = new ArrayList<Object[]>();
					resList.add(obj);
					res.put(schoolName, resList);
				}
			}
		}
		return res;
	}
	
	
	
	@Override
	public void downLoadExcelByZip(HttpServletRequest request,
			HttpServletResponse response) {
		List<File> fileList = new ArrayList<File>();
		String path = UploadHelper.getDir();
		//获得数据
		Map<String, List<Object[]>> res = this.getAllUsersBySchools();
		for (String schoolName:res.keySet()) {
			List<Object[]> result = res.get(schoolName);
			String fileName = schoolName+".xls";
			String sheetName = schoolName;
			
			File file = new File(path + File.separator + fileName);
			try {
				this.template(file, sheetName,result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			fileList.add(file);
		}
		FileFactory.zipFiles(fileList, "同步用户"+FileFactory.getNowDate(),response,true);
	}

	public void template(File file, String sheetName,List<Object[]> result) throws Exception {
		WritableWorkbook wb = Workbook.createWorkbook(file);
		WritableSheet sheet = wb.createSheet(sheetName, 0);
		//字体
		WritableFont font = new WritableFont(WritableFont.ARIAL,10,WritableFont.BOLD);
		//样式
		WritableCellFormat cellFormat = new WritableCellFormat(font);
		//背景
		cellFormat.setBackground(Colour.GRAY_25);
		//居中
		cellFormat.setAlignment(Alignment.CENTRE);
		//设置宽度
		sheet.setColumnView(0, 20);
		Label label = new Label(0, 0, "用户名",cellFormat);
		sheet.addCell(label);
		label = new Label(1, 0, "密码",cellFormat);
		sheet.addCell(label);
		label = new Label(2, 0, "角色",cellFormat);
		sheet.addCell(label);
		label = new Label(3, 0, "学校代码",cellFormat);
		sheet.addCell(label);
		//设置宽度
		sheet.setColumnView(4, 25);
		label = new Label(4, 0, "学校名称",cellFormat);
		sheet.addCell(label);
		label = new Label(5, 0, "年级",cellFormat);
		sheet.addCell(label);
		label = new Label(6, 0, "班级",cellFormat);
		sheet.addCell(label);
		label = new Label(7, 0, "学科",cellFormat);
		sheet.addCell(label);
		
		for (int i = 0; i < result.size(); i++) {
			Object[] obj = result.get(i);
			label = new Label(0,i+1, obj[0].toString());
			sheet.addCell(label);
			label = new Label(1,i+1, obj[1].toString());
			sheet.addCell(label);
			label = new Label(2,i+1, obj[2].toString());
			sheet.addCell(label);
			label = new Label(3,i+1, obj[3].toString());
			sheet.addCell(label);
			label = new Label(4,i+1, obj[4].toString());
			sheet.addCell(label);
			label = new Label(5,i+1, obj[5]==null?"--":obj[5].toString());
			sheet.addCell(label);
			label = new Label(6,i+1, obj[6]==null?"--":obj[6].toString());
			sheet.addCell(label);
			label = new Label(7,i+1, obj[7]==null?"--":obj[7].toString());
			sheet.addCell(label);
		}
		wb.write();
		wb.close();

	}

	@Override
	public void deleteSynByRoleId(Long id) {
		String sql = "delete from kn_synusers where roleid="+id;
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.executeUpdate();
	}

}