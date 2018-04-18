/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.report;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.cntest.common.query.Query;
import com.cntest.common.repository.DynamicHibernateTemplate;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.ExamType;
import com.cntest.fxpt.domain.Grade;
import com.cntest.remote.domain.NanShanData;
import com.cntest.remote.service.INanShanDataService;
import com.cntest.security.UserDetails;
import com.cntest.security.UserOrg;
import com.cntest.security.remote.UserDetailsService;
import com.cntest.util.DateUtil;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年12月19日
 * @version 1.0
 **/
public abstract class AbstractReportQuery implements ReportQuery {

	private static Logger logger = LoggerFactory.getLogger(AbstractReportQuery.class);

	protected static String ORGS_KEY = "orgs";

	protected static String PARAMS_KEY = "params";

	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@Autowired(required = false)
	@Qualifier("INanShanDataService")
	private INanShanDataService nanShanDataService;
	
	@Autowired
	protected DynamicHibernateTemplate dynamicHibernateTemplate;

	@Autowired(required = false)
	protected UserDetailsService userDetailsService;
	
	@Autowired(required = false)
	private UserService userService;

	@Override
	public List<Exam> queryFor(UserDetails user, Query<Exam> query) {
		logger.debug("Query Report For User {}", user.getUserName());
		List<UserOrg> orgs = this.userDetailsService.findUserOrgs(user);
		if (orgs == null || orgs.size() == 0)
			return null;
		Map root = createRoot(ORGS_KEY, orgs);
		addParamsToRoot(root, query.getParameters());
		return doQuery(root,query);
	}
	
	protected List<Exam> doQuery(Map root,Query<Exam> query) {
		org.hibernate.Query sqlQuery = sqlQuery= this.createReportQuery(root);;
		User user = User.from(userService.getCurrentLoginedUser());
		User users=userService.findUserBy(user.getName());
		//南山学生单点登录处理（查看nanshan_data表中的typeId是否是学生）
		NanShanData nanShanData =nanShanDataService.findByUid(user.getName());
		boolean isNanshanStudent =false;
		if(nanShanData!=null)
			if("student".equalsIgnoreCase(nanShanData.getRoleId())){
				sqlQuery= nanShanDataService.getQueryByUser(nanShanData.getLoginId(),users,(Map) root.get(PARAMS_KEY));
				isNanshanStudent=true;
			}
		if(!isNanshanStudent){
			setParameters(sqlQuery, (Map) root.get(PARAMS_KEY));
		}
		

		org.hibernate.Query countSqlQuery = this.createReportCountyQuery(root);
		setParameters(countSqlQuery, (Map) root.get(PARAMS_KEY));

		BigInteger countNum = (BigInteger) countSqlQuery.uniqueResult();
		query.setTotalRows(countNum.intValue());

		sqlQuery.setFirstResult(query.getStartRow());
		sqlQuery.setMaxResults(query.getPagesize());

		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = sqlQuery.list();
		
		ArrayList<Exam> exams = new ArrayList<Exam>();
		HashMap<Long, Grade> gradeMap = new HashMap<Long, Grade>();
		for (Map<String, Object> row : list) {
			exams.add(createExam(row, gradeMap));
		}
		query.setResults(exams);
		//报告权限过滤
		if(!user.getName().equals("student") && !isNanshanStudent){
			//查询考试授权
			String examids =userService.getUserExamByRoleId(users.getPk());
			//空格处理
			if(examids!="" && examids!=null) {
				examids=examids.replaceAll(" ", "");
			}
			for(int i=exams.size()-1;i>=0;i--) {
				Exam exam=exams.get(i);
				String id =","+exam.getId()+",";
				if(examids=="" || examids==null || examids.indexOf(id)==-1) {
					exams.remove(i);
				}
			}
		}
		return exams;
	}

	@Override
	public List<Exam> querySameTermExamReport(UserDetails user, Exam exam) {
		List<UserOrg> userOrgs = userDetailsService.findUserOrgs(user);
		org.hibernate.Query sqlQuery = dynamicHibernateTemplate.createSQLQuery(
				"com.cntest.fxpt.anlaysis.repository.impl.ReportExamDao.selectSameTermExams", userOrgs.size() > 0?userOrgs.get(0):null);
		sqlQuery.setParameter(0, exam);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> list = sqlQuery.list();
		ArrayList<Exam> exams = new ArrayList<Exam>();
		HashMap<Long, Grade> gradeMap = new HashMap<Long, Grade>();
		for (Map<String, Object> row : list) {
			exams.add(createExam(row, gradeMap));
		}
		return exams;
	}

	protected abstract org.hibernate.Query createReportQuery(Map root);
	
	protected abstract org.hibernate.Query createReportCountyQuery(Map root);

	protected Map<String, Object> createRoot(String name, Object entity) {
		HashMap<String, Object> root = new HashMap<String, Object>();
		root.put(name, entity);
		return root;
	}

	protected void addParamsToRoot(Map root, Map<String,String[]> parameters) {
		if (parameters != null) {
			LinkedHashMap<String, Object> parameter = new LinkedHashMap();
			Set keys = parameters.keySet();
			int i = 0;
			for (Object o : keys) {
				switch (o+"") {
				case "schoolYear" :
					parameter.put("schoolYear",parameters.get(o)[0]);
					break;
				case "schoolTerm" :
					parameter.put("schoolTerm",parameters.get(o)[0]);
					break;
				case "examDate" :
					parameter.put("examDate",parameters.get(o)[0]);
				    break;
				case "examTypeId" :
					parameter.put("examTypeId",parameters.get(o)[0]);
					break;
				case "gradeid" :
					parameter.put("gradeid",parameters.get(o)[0]);
					break;
				}
			}

			root.put(PARAMS_KEY, parameter);
		}
	}

	
	protected void setParameters(org.hibernate.Query query, Map<String,String> parameters) {
		if (parameters != null && parameters.size() > 0) {
			Set keys = parameters.keySet();
			int i = 0;
			for (Object o : keys) {
//				query.setParameter(i++, (parameters.get(o)[0]+""));
				switch (o+"") {
				case "schoolYear" :
					query.setString(i++, parameters.get(o));
					break;
				case "schoolTerm" :
					query.setInteger(i++, Integer.valueOf(parameters.get(o)));
					break;
				case "examDate" :
					query.setDate(i++, DateUtil.convertStringToDate(parameters.get(o)));
				    break;
				case "examTypeId" :
					query.setInteger(i++, Integer.valueOf(parameters.get(o)));
					break;
				case "gradeid" :
					query.setInteger(i++, Integer.valueOf(parameters.get(o)));
					break;
				}
			}
		}
	}

	protected Exam createExam(Map<String, Object> row, Map<Long, Grade> gradeMap) {
		Exam exam = new Exam();
		exam.setId(Long.parseLong(row.get("id").toString()));
		exam.setName((String) row.get("name"));
		exam.setSortName((String) row.get("sortName"));
		exam.setExamDate((Date) row.get("examDate"));
		exam.setWlForExamStudent((Boolean) row.get("isWlForExamStudent"));
		exam.setExamStudentJiebie((Integer) row.get("examStudentJiebie"));
		exam.setSchoolYear((Integer) row.get("schoolYear"));
		exam.setSchoolTerm((Integer) row.get("schoolTerm"));
		exam.setOwnerCode((String) row.get("ownerCode"));
		exam.setOwnerName((String) row.get("ownerName"));
		exam.setLevelCode((Integer) row.get("levelCode"));
		exam.setLevelName((String) row.get("levelName"));
		if (row.get("typeName") != null) {
			ExamType type = new ExamType();
			type.setName((String) row.get("typeName"));
			exam.setExamType(type);
		}

		if (row.get("gradeId") != null) {
			Long gradeId = Long.parseLong(row.get("gradeId").toString());
			Grade grade = gradeMap.get(gradeId);
			if (grade == null) {
				grade = new Grade();
				grade.setId(gradeId);
				grade.setName((String) row.get("gradeName"));
				gradeMap.put(gradeId, grade);
			}
			exam.setGrade(grade);
		}
		return exam;
	}

}
