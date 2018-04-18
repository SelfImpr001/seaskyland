/*
 * @(#)com.cntest.fxpt.repository.impl.ExamStudentDaoImpl.java	1.0 2014年5月20日:上午11:26:58
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.RootEntityResultTransformer;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.page.Page;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.anlaysis.bean.Param;
import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.ExamStudent;
import com.cntest.fxpt.repository.IExamStudentDao;
import com.cntest.fxpt.util.SystemConfig;
import com.cntest.util.ExceptionHelper;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月20日 上午11:26:58
 * @version 1.0
 */
@Repository("IExamStudentDao")
public class ExamStudentDaoImpl extends AbstractHibernateDao<ExamStudent, Long> implements IExamStudentDao {
	private static Logger logger = LoggerFactory.getLogger(ExamStudentDaoImpl.class);

	@Override
	protected Class<ExamStudent> getEntityClass() {
		return ExamStudent.class;
	}

	@Override
	public List<ExamStudent> list(Page<ExamStudent> page, Long examId) {

		Criteria criteria = this.getSession().createCriteria(this.getEntityClass().getName());

		criteria.add(Restrictions.eq("exam.id", examId));

		Long rowCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		page.setTotalRows(rowCount.intValue());

		int first = (page.getCurpage() - 1) * page.getPagesize();
		first = first < 0 ? 0 : first;
		criteria.setFirstResult(first);
		criteria.setMaxResults(page.getPagesize());

		List<ExamStudent> list = criteria.list();

		page.setList(list);
		return list;
	}

	@Override
	public List<ExamStudent> listStudentWith(Long examId, Param... params) {

		// Criteria criteria = this.getSession().createCriteria(
		// this.getEntityClass().getName());
		// criteria.add(Restrictions.eq("exam.id", examId));
		// if (params != null) {
		// for (Param param : params) {
		// criteria.add(Restrictions.eq(param.getKey(), param.getValue()));
		// }
		// }
		// return criteria.list();
		List<ExamStudent> eslist = null;
		try {

			String hql = "from ExamStudent xs " + "left join fetch xs.exam exam " + "left join fetch xs.clazz clazz "
					+ "left join fetch xs.school school " + "left join fetch school.education county "
					+ "left join fetch county.parent city  " + "left join fetch city.parent province  "
					+ "where exam.id=" + examId;
			Object[] objs = null;
			if (params != null) {
				objs = new Object[params.length];
				StringBuffer sb = new StringBuffer();
				int idx = 0;
				for (Param param : params) {
					sb.append(" and " + param.getKey() + "=?");
					objs[idx++] = param.getValue();
				}
				hql += sb.toString();
			}

			eslist = findByHql(hql, objs);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("listStudentWith出现异常:" + e.toString());
		}
		return eslist;
	}

	@Override
	public void deleteTmpExamStudent(Long examId) {
		String sql = "DELETE FROM kn_examstudent";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.executeUpdate();
	}

	@Override
	public long getStudentMaxIdInDW() {
		String sql = "SELECT IFNULL(MAX(id),0) as maxNum FROM dw_examstudent_fact";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		Object obj = sqlQuery.uniqueResult();
		return Long.parseLong(obj.toString());
	}

	@Override
	public int deleteStudentInDW(Long examId) {
		String sql = "DELETE FROM dw_examstudent_fact WHERE examId=?";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setParameter(0, examId);
		return sqlQuery.executeUpdate();
	}

	@Override
	public int importExamClass(WebRetrieveResult wrr) {
		String sql = "INSERT INTO dw_dim_class(examId,schoolCode,schoolName,CODE,NAME,wl,classType) "
				+ "SELECT xs.examId,xx.code,xx.name,xs.classCode,xs.className,xs.wl,xs.classType FROM kn_examstudent xs "
				+ "INNER JOIN dw_dim_school xx ON xs.schoolCode=xx.code " + "WHERE xs.examId=? ";
		//获取已经存在的班级
		String dim = "SELECT DISTINCT(CODE) FROM dw_dim_class  WHERE examid="+wrr.getExamId();
		SQLQuery query = getSession().createSQLQuery(dim);
		List list=query.list();
		boolean flag=false;
		String classId="";
		if(list.size()>0){
			classId=list.toString().substring(1,list.toString().length()-1);
			classId=classId.replaceAll(", ", ",");
			flag=true;
			sql=sql+" and xs.classCode NOT IN("+classId+") ";
		}
		sql=sql+" GROUP BY xs.schoolCode,xs.classCode";
		int count = 0;
		int updateNum = 0;
		while (count < 5) {
			try {
				Long examId = wrr.getExamId();
				// importType==0为覆盖导入。
				if (wrr.getImportType() != 1) {
					SQLQuery sqlQuery = getSession().createSQLQuery("DELETE FROM dw_dim_class WHERE examId=?");
					sqlQuery.setLong(0, examId);
					sqlQuery.executeUpdate();
				}else{
					SQLQuery sqlQuery = getSession().createSQLQuery(sql);
					sqlQuery.setLong(0, examId);
					updateNum = sqlQuery.executeUpdate();
				}
				
				count = 5;
			} catch (Exception e) {
				count++;
				logger.debug("第" + count + "次提交失败，" + ExceptionHelper.trace2String(e));
			}
		}

		return updateNum;
	}

	@Override
	public int copyStudent(WebRetrieveResult wrr) {
		Long examId = wrr.getExamId();
		boolean flag = false;
		try {
			// copy学生之前删除数据库本来的学生信息

			String hql = "SELECT DISTINCT(studentId) FROM kn_examstudent where examid=?";
			SQLQuery query = getSession().createSQLQuery(hql);
			query.setLong(0, examId);
			List<String> list = query.list();

			hql = "SELECT DISTINCT(classCode) FROM kn_examstudent where examid=?";
			query = getSession().createSQLQuery(hql);
			query.setLong(0, examId);
			List<String> listClassId = query.list();
			if (list.size() > 0 && wrr.getSchemeType() == 1) {
				// 删除数据
				flag = cleanStudent(examId, list, listClassId);
			}

			importExamClass(wrr);
		} catch (Exception e) {
 			e.printStackTrace();
		}
		if (!flag) {
			logger.debug("删除原始数据失败！");
		}

		String[] fieldss = loadBeCopyStudentFields();

		StringBuffer insertFields = new StringBuffer();
		StringBuffer valueFields = new StringBuffer();

		for (String f : fieldss) {
			insertFields.append(",").append(f);
			valueFields.append(",").append("xs.").append(f);
		}
		String sql = "INSERT INTO dw_examstudent_fact(examId,provinceId,cityId,countyId,schoolId,classId"
				+ insertFields.toString() + ") " + "SELECT xs.examId examId,ss.id provinceId,ds.id cityId,"
				+ "qx.id countyId,xx.Id schoolId,bj.id classId" + valueFields.toString() + "  FROM kn_examstudent xs "
				+ "LEFT JOIN dw_dim_class bj ON xs.examId=bj.examId AND xs.schoolCode=bj.schoolCode AND xs.classCode=bj.code "
				+ "INNER JOIN dw_dim_school xx ON xs.schoolCode=xx.code "
				+ "INNER JOIN dw_dim_county qx ON xx.countyCode=qx.CODE "
				+ "INNER JOIN dw_dim_city   ds ON xx.cityCode=ds.CODE "
				+ "INNER JOIN dw_dim_province ss ON xx.provinceCode=ss.CODE " + "WHERE xs.examId=?";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		return sqlQuery.executeUpdate();
	}

	private String[] loadBeCopyStudentFields() {
		String filed = SystemConfig.newInstance().getValue("beCopyStudentFileds");
		return filed.split(",");
	}

	@Override
	public void statSchoolImportPersonNum(Long examId, Page<Map<String, Object>> page) {

		page.setTotalRows(getExamSchoolNum(examId));

		String sql = "SELECT xx.cityName cityName,xx.countyName countyName,xx.name schoolName,personCount num ,xx.code schoolCode "
				+ "FROM kn_examschoolpersoncount xxStat " + "INNER JOIN dw_dim_school xx ON xxStat.schoolId=xx.id "
				+ "WHERE xxStat.examId=?";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.addScalar("cityName");
		sqlQuery.addScalar("countyName");
		sqlQuery.addScalar("schoolName");
		sqlQuery.addScalar("num");
		sqlQuery.addScalar("schoolCode");

		sqlQuery.setLong(0, examId);
		int first = (page.getCurpage() - 1) * page.getPagesize();
		first = first < 0 ? 0 : first;
		sqlQuery.setFirstResult(first);
		sqlQuery.setMaxResults(page.getPagesize());
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> result = sqlQuery.list();
		page.setList(result);
	}

	@Override
	public int getExamSchoolNum(Long examId) {
		String sql = "SELECT COUNT(1) FROM kn_examschoolpersoncount xs WHERE xs.examId=?";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		Object obj = sqlQuery.uniqueResult();
		return Integer.parseInt(obj.toString());
	}

	@Override
	public int getExamStudentNum(Long examId) {
		String sql = "SELECT COUNT(*) FROM dw_examstudent_fact WHERE examid=" + examId;
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		Object obj = sqlQuery.uniqueResult();
		return Integer.parseInt(obj.toString());
	}

	@Override
	public List<Map<String, Object>> list(Long examId) {
		String sql = "SELECT zkzh,id,provinceId,cityId,countyId,schoolId,classId,wl FROM dw_examstudent_fact WHERE examId=?";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.addScalar("zkzh");
		sqlQuery.addScalar("id");
		sqlQuery.addScalar("provinceId");
		sqlQuery.addScalar("cityId");
		sqlQuery.addScalar("countyId");
		sqlQuery.addScalar("schoolId");
		sqlQuery.addScalar("classId");
		sqlQuery.addScalar("wl");
		sqlQuery.setLong(0, examId);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.IExamStudentDao#getStudentCount(java.lang.
	 * Long)
	 */
	@Override
	public int getStudentCount(Long examId, Long schoolId, int wl) {
		String wlStr = "";
		if (wl != 0) {
			wlStr = " AND xs.wl=" + wl;
		}

		String sql = "SELECT COUNT(1) FROM dw_examstudent_fact xs WHERE xs.examId=? And xs.schoolId=?" + wlStr;
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.setLong(1, schoolId);
		Object obj = sqlQuery.uniqueResult();
		return Integer.parseInt(obj.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.IExamStudentDao#isHasWL(java.lang.Long)
	 */
	@Override
	public boolean isHasWL(Long examId) {
		String sql = "SELECT wl FROM dw_examstudent_fact WHERE examId=? GROUP BY wl ORDER BY NULL";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		List list = sqlQuery.list();
		return list != null && list.size() > 1 ? true : false;
	}

	@Override
	public void statSchoolPersonCount(Long examId) {
		String sql = "INSERT INTO kn_examschoolpersoncount(examId,schoolId,personCount,personCountL,personCountW) "
				+ "SELECT examId,schoolId,COUNT(1) num,SUM(IF(wl=1,1,0)) numL,SUM(IF(wl=2,1,0)) numW "
				+ "FROM dw_examstudent_fact " + "WHERE examId=? " + "GROUP BY schoolId";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.executeUpdate();
	}

	@Override
	public void deleteSchoolStatPersonCount(Long examId) {
		String sql = "DELETE FROM kn_examschoolpersoncount WHERE examId=?";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.executeUpdate();
	}

	@Override
	public void deleteExamClass(Long examId) {
		String sql = "DELETE FROM dw_dim_class WHERE examId=?";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.executeUpdate();
	}

	@Override
	public void copyStudentTo(Long targetExamId, Long srcExamId) {
		// String sql =
		// "INSERT INTO dw_examstudent_fact ( examId, provinceId, cityId,
		// countyId, schoolId, classId, studentId, zkzh, NAME, gender, domicile,
		// nation, learLanguage, wl, isPast, isTransient)"
		// + " SELECT "
		// + targetExamId
		// +
		// " AS examId,provinceId, cityId, countyId, schoolId, classId,
		// studentId, zkzh, NAME, gender, domicile, nation, learLanguage, wl,
		// isPast, isTransient"
		// + " FROM dw_examstudent_fact WHERE examId = ?";
		String sql = "{Call P_CopyExamStudent(?,?)}";
		logger.debug(" copy student excute:{}", sql);

		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setLong(0, targetExamId);
		sqlQuery.setLong(1, srcExamId);
		sqlQuery.executeUpdate();

		// this.statSchoolPersonCount(srcExamId);
	}

	@Override
	public Map<String, Integer> statStudentCount(Long examId) {
		String sql = "SELECT COUNT(1) num,SUM(IF(wl = 1,1,0)) numL,SUM(IF(wl = 2,1,0)) numW "
				+ "FROM dw_examstudent_fact " + "WHERE examId=?";
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		Map<String, Object> tmpReuslt = (Map<String, Object>) sqlQuery.uniqueResult();
		Map<String, Integer> result = new HashMap<String, Integer>();
		Object num = tmpReuslt.get("num");
		int numInt = num == null ? 0 : Integer.parseInt(num.toString());
		result.put("num", numInt);

		num = tmpReuslt.get("numL");
		numInt = num == null ? 0 : Integer.parseInt(num.toString());
		result.put("numL", numInt);

		num = tmpReuslt.get("numW");
		numInt = num == null ? 0 : Integer.parseInt(num.toString());
		result.put("numW", numInt);

		return result;
	}

	@Override
	public void findStudentListBySchoolCode(Long examId, String schoolCode, Page page) {
		Criteria criteria = this.getSession().createCriteria(this.getEntityClass().getName());
		criteria.add(Restrictions.eq("exam.id", examId));
		criteria.createAlias("school", "school");
		criteria.add(Restrictions.eq("school.code", schoolCode));
		Long rowCount = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		page.setTotalRows(rowCount.intValue());
		int first = (page.getCurpage() - 1) * page.getPagesize();
		first = first < 0 ? 0 : first;
		criteria.setFirstResult(first);
		criteria.setMaxResults(page.getPagesize());
		criteria.setResultTransformer(RootEntityResultTransformer.INSTANCE);
		List<ExamStudent> list = criteria.list();
		// boolean类型的值转换
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setPastValue(0);
			list.get(i).setTrantValue(0);
			if (list.get(i).isPast()) {
				list.get(i).setPastValue(1);
			}
			if (list.get(i).isTransient()) {
				list.get(i).setTrantValue(1);
			}

		}
		page.setList(list);
	}

	@Override
	public ExamStudent findByExample(ExamStudent example) {
		StringBuilder hql = new StringBuilder("From ExamStudent where 1=1");
		ArrayList<Object> params = new ArrayList<>();
		if (example.getName() != null) {
			hql.append(" and name=? ");
			params.add(example.getName());
		}

		if (example.getStudentId() != null) {
			hql.append(" and zkzh=?");
			params.add(example.getStudentId());
		}

		/*
		 * if (example.getSchool() != null) { hql.append(" and school.code=?");
		 * params.add(example.getSchool().getCode()); }
		 */
		hql.append(" order by id");
		// return findEntityByHql(hql.toString(), params.toArray(new Object[]
		// {}));
		List<ExamStudent> list = findByHql(hql.toString(), params.toArray(new Object[] {}));
		return (list.size() > 0) ? list.get(0) : null;

	}

	@Override
	public ExamStudent get(Long examId, String studentId) {
		String hql = "from ExamStudent xs " + "left join fetch xs.exam exam " + "left join fetch xs.clazz clazz "
				+ "left join fetch xs.school school " + "where exam.id=? and xs.studentId=?";

		return findEntityByHql(hql, examId, studentId);
	}

	public List<Map<String, String>> getStudentList(Long examId) {
		List<Map<String, String>> res = new ArrayList<Map<String, String>>();
		String sql = "SELECT  e.id,e.name,f.zkzh zkzh,c.schoolCode schoolCode,c.schoolName schoolName,g.id gradeCode,g.name gradeName,c.code classCode,c.name className,f.NAME studentName,e.sortName xq,e.examStudentJiebieName xd,p.NAME km,t.totalScore zf  FROM dw_examstudent_fact f"
				+ " INNER JOIN dw_dim_class c" + " ON f.classId=c.id " + " INNER JOIN kn_exam e" + " ON e.id=f.examId"
				+ " INNER JOIN dw_testpapercj_fact t" + " ON t.examId=e.id AND t.studentId=f.id"
				+ " INNER JOIN kn_testpaper p" + " ON p.examId=e.id AND p.id=t.testPaperId"
				+ " INNER JOIN kn_grade g ON e.gradeId = g.id" + " WHERE e.id=? ORDER BY c.schoolCode,c.id";
		Query query = getSession().createSQLQuery(sql);
		query.setLong(0, examId);
		List list = query.list();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				Object[] obj = (Object[]) list.get(i);
				map.put("examId", obj[0].toString());
				map.put("examName", obj[1].toString());
				map.put("zkzh", obj[2].toString());
				map.put("schoolCode", obj[3].toString());
				map.put("schoolName", obj[4].toString());
				map.put("gradeCode", obj[5].toString());
				map.put("gradeName", obj[6].toString());
				map.put("classCode", obj[7].toString());
				map.put("className", obj[8].toString());
				map.put("studentName", obj[9].toString());
				map.put("xq", obj[10].toString());
				map.put("xd", obj[11].toString());
				map.put("km", obj[12].toString());
				map.put("cj", obj[13].toString());
				res.add(map);
			}
		}
		return res;
	}

	// 追加导入相同区域数据时，删除原来该区所有数据
	@Override
	public boolean cleanStudent(Long examid, List<String> studentIds, List<String> schoolIds) throws Exception {
		boolean flag = false;
		try {

			StringBuffer strBuffer = new StringBuffer();
			Iterator<String> iter = studentIds.iterator();
			// 将hashSet里保存的学习code拼装成字符串“，”隔开
			while (iter.hasNext()) {
				strBuffer.append("'" + iter.next() + "'" + ",");
			}
			// 去除最后一个“，”
			String str = strBuffer.substring(0, strBuffer.length() - 1);

			StringBuffer strBuffers = new StringBuffer();
			Iterator<String> iters = schoolIds.iterator();
			// 将hashSet里保存的学习code拼装成字符串“，”隔开
			while (iters.hasNext()) {
				strBuffers.append("'" + iters.next() + "'" + ",");
			}
			// 去除最后一个“，”
			String strs = strBuffers.substring(0, strBuffers.length() - 1);
			if (!"".equals(str) || str != null) {
				// 删除对应区域的学生信息
				String sql = "DELETE FROM dw_examstudent_fact WHERE examId=" + examid + " AND studentId in(" + str
						+ ")";
				SQLQuery sqlQuery = getSession().createSQLQuery(sql);
				sqlQuery.executeUpdate();

				sql = "DELETE FROM kn_examschoolpersoncount WHERE examId=" + examid;
				sqlQuery = getSession().createSQLQuery(sql);
				sqlQuery.executeUpdate();

				// 删除对应的班级信息（非追加导入在学生导入成功是删除）
//				sql = "DELETE FROM dw_dim_class WHERE examId=" + examid + " AND code in(" + strs + ")";
//				sqlQuery = getSession().createSQLQuery(sql);
//				sqlQuery.executeUpdate();

				// 删除对应成绩信息
				String sqlTol = "DELETE FROM dw_itemcj_fact WHERE examId=" + examid
						+ "  AND studentId in(" + str + ")";
				SQLQuery sqlQueryTol = getSession().createSQLQuery(sqlTol);
				sqlQueryTol.executeUpdate();
				
				sqlTol = "DELETE FROM dw_testpapercj_fact WHERE examId=" + examid
						+ "  AND studentId in(" + str + ")";
				sqlQueryTol = getSession().createSQLQuery(sqlTol);
				sqlQueryTol.executeUpdate();
				
				flag = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public int getImportStudentNum(Long examid) {
		String sql = "SELECT COUNT(1) FROM kn_examstudent WHERE examid=" + examid;
		SQLQuery sqlQuery = getSession().createSQLQuery(sql);
		Object obj = sqlQuery.uniqueResult();
		return Integer.parseInt(obj.toString());
	}

	@Override
	public List<ExamStudent> examByStudentForList(Long examid) {
		return findByHql("from ExamStudent xs where xs.exam.id= ? GROUP BY xs.studentId ", examid);
	}

	@Override
	public List<String> studentValidate(WebRetrieveResult wrr) {
		List<String> result = new ArrayList<String>();
		String sql = "SELECT kn.zkzh FROM kn_examstudent kn INNER JOIN dw_examstudent_fact dw ON kn.examid = dw.examid AND dw.zkzh=kn.zkzh  WHERE kn.examid = "
				+ wrr.getExamId();
		SQLQuery query = getSession().createSQLQuery(sql);
		return (List<String>) query.list();
	}
}
