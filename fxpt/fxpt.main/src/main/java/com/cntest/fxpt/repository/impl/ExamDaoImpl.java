/*
 * @(#)com.cntest.fxpt.repository.impl.ExamDaoImpl.java	1.0 2014年5月17日:上午10:40:11
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.RootEntityResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.cntest.common.page.Page;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.exception.BusinessException;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.School;
import com.cntest.fxpt.repository.IExamDao;
import com.cntest.fxpt.repository.IExamStudentDao;
import com.cntest.util.DateUtil;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月17日 上午10:40:11
 * @version 1.0
 */
@Repository("IExamDao")
public class ExamDaoImpl extends AbstractHibernateDao<Exam, Long> implements
		IExamDao {
	
	private UserService userService;
	
	@Autowired(required = false)
	@Qualifier("IExamStudentDao")
	private IExamStudentDao examStudentDao;
	
	private static final Logger log = LoggerFactory
			.getLogger(ExamDaoImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cntest.fxpt.repository.IExamDao#add(com.cntest.fxpt.domain.Exam)
	 */
	@Override
	public void add(Exam exam) {
		this.save(exam);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.IExamDao#update(com.cntest.fxpt.domain.Exam)
	 */
	@Override
	public void update(Exam exam) {
		Exam tmp = get(exam.getId());
		tmp.setName(exam.getName());
		tmp.setSortName(exam.getSortName());
		tmp.setGrade(exam.getGrade());
		tmp.setExamDate(exam.getExamDate());
		tmp.setExamType(exam.getExamType());
		tmp.setSchoolYear(exam.getSchoolYear());
		tmp.setSchoolYearName(exam.getSchoolYearName());
		tmp.setSchoolTerm(exam.getSchoolTerm());
		tmp.setOwnerCode(exam.getOwnerCode());
		tmp.setOwnerName(exam.getOwnerName());
		tmp.setLevelCode(exam.getLevelCode());
		tmp.setLevelName(exam.getLevelName());
		tmp.setExamStudentJiebie(exam.getExamStudentJiebie());
		tmp.setExamStudentJiebieName(exam.getExamStudentJiebieName());
		super.update(tmp);
	}

	@Override
	public void updateStatus(Long examId, int status) {
		String hql = "update Exam set status=? where id=?";
		Query query = getSession().createQuery(hql);
		int idx = 0;
		query.setParameter(idx++, status);
		query.setParameter(idx++, examId);
		query.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cntest.fxpt.repository.IExamDao#delete(com.cntest.fxpt.domain.Exam)
	 */
	@Override
	public void delete(Exam exam) {
		super.delete(exam);

	}

	@Override
	protected Class<Exam> getEntityClass() {
		return Exam.class;
	}

	@Override
	public Exam findById(Long examId) {
		return this.get(examId);
	}

	@Override
	public List<Exam> list(Page<Exam> page) {

		Criteria criteria = this.getSession().createCriteria(
				this.getEntityClass().getName());
		
		//考试授权控制显示列表
		// User user = User.from(userService.getCurrentLoginedUser());
		// User users=userService.findUserBy(user.getName());
		// //查询考试授权
		// String examids =userService.getUserExamByRoleId(users.getPk());
		// if(examids!="" && examids!=null){
		// String[] str =examids.substring(1, examids.length()-1).split(",");
		// Long[] lon= new Long[str.length];
		// for(int i=0;i<str.length;i++) {
		// lon[i]=Long.parseLong(str[i].trim());
		// }
		// //判断是否有权限查看
		// criteria.add(Restrictions.in("id", lon));
		// }else {
		// //判断是否有权限查看
		// criteria.add(Restrictions.isNull("id"));
		// }
		
		if (!page.getParameter().isEmpty()) {
			if (page.hasParam("examData")) {
				criteria.add(Restrictions.sqlRestriction("examDate='"
						+ page.getString("examData") + "'"));
			}

			if (page.hasParam("examName")) {
				criteria.add(Restrictions.like("name",
						"%" + page.getString("examName") + "%"));
			}
			if (page.hasParam("examSortName")) {
				criteria.add(Restrictions.like("sortName",
						"%" + page.getString("examSortName") + "%"));
			}
			if (page.hasParam("schoolYear")) {
				criteria.add(Restrictions.like("schoolYearName",
						"%" + page.getString("schoolYear") + "%"));
			}
			if (page.hasParam("schoolTerm")) {
				criteria.add(Restrictions.eq("schoolTerm",
						page.getInteger("schoolTerm")));
			}
			if (page.hasParam("grade")) {
				criteria.add(Restrictions.eq("grade.id", page.getLong("grade")));
			}
			if (page.hasParam("examType")) {
				criteria.add(Restrictions.eq("examType.id",
						page.getLong("examType")));
			}
			if (page.hasParam("examStatus")) {
				//状态信可以支持多个状态一起，用   , 号分开，只支持正整数字，
				String[] statusArray = page.getString("examStatus").split(",");
				List<Integer> statuss = new ArrayList<>();
				for (int i = 0; i < statusArray.length; i++) {
					if(StringUtils.isNumeric(statusArray[i])&& statusArray[i]!=""){
						statuss.add(Integer.parseInt(statusArray[i]));
					}
				}
				criteria.add(Restrictions.in("status",statuss));
			}

			if (page.hasParam("hasExamStudent")) {
				criteria.add(Restrictions.eq("hasExamStudent",
						page.getBoolean("hasExamStudent")));
			}

			if (page.hasParam("hasItem")) {
				Boolean tmp = page.getBoolean("hasItem");
				if (tmp) {
					criteria.add(Restrictions.gt("impItemCount", 0));
				} else {
					criteria.add(Restrictions.eq("impItemCount", 0));
				}
			}

			if (page.hasParam("hasCj")) {
				Boolean tmp = page.getBoolean("hasCj");
				if (tmp) {
					criteria.add(Restrictions.gt("impCjCount", 0));
				} else {
					criteria.add(Restrictions.eq("impCjCount", 0));
				}
			}

			if (page.hasParam("isCjList")) {
				Boolean tmp = page.getBoolean("isCjList");
				if (tmp) {
					criteria.add(Restrictions.eq("hasExamStudent", true));
					criteria.add(Restrictions.gt("impItemCount", 0));
				}
			}
			if (page.hasParam("isParamList")) {
				Boolean tmp = page.getBoolean("isParamList");
				if (tmp) {
					criteria.add(Restrictions.eq("hasExamStudent", true));
					criteria.add(Restrictions.eqProperty("impItemCount",
							"impCjCount"));
					criteria.add(Restrictions.gt("impItemCount", 0));
				}
			}
			if (page.hasParam("studentBaseStatus")) {
				criteria.add(Restrictions.eq("studentBaseStatus",
						page.getInteger("studentBaseStatus")));
			}
		}

		Long rowCount = (Long) criteria.setProjection(
				Projections.countDistinct("id")).uniqueResult();
		criteria.setProjection(null);
		page.setTotalRows(rowCount.intValue());
		if(page.getPagesize()!=0){
			int first = (page.getCurpage() - 1) * page.getPagesize();
			first = first < 0 ? 0 : first;
			criteria.setFirstResult(first);
			criteria.setMaxResults(page.getPagesize());
		}
		criteria.addOrder(Order.desc("examDate"));
		criteria.setResultTransformer(RootEntityResultTransformer.INSTANCE);

		List<Exam> list = criteria.list();

		page.setList(list);
		return list;
	}

	@Override
	public int getTestPaperCount(Exam exam) {
		String sql = "SELECT COUNT(1) FROM tb_testpaper WHERE examId=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, exam.getId());
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
	public int getExamStudentCount(Exam exam) {
		String sql = "SELECT COUNT(1) FROM tb_examstudent WHERE examId=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, exam.getId());
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
	public int updateHasExamStudent(Long examId, boolean hasExamStudent,
			boolean hasWlInStudentInfo) {
		String hql = "update Exam set hasExamStudent=?,isWlForExamStudent=?  where id=?";
		Query query = getSession().createQuery(hql);
		query.setBoolean(0, hasExamStudent);
		query.setBoolean(1, hasWlInStudentInfo);
		query.setLong(2, examId);
		return query.executeUpdate();
	}

	@Override
	public Exam tryGetAnalysisExam() {
		String hql = "from Exam where status=6";
		return findEntityByHql(hql);
	}

	@Override
	public List<School> findExamSchools(Exam exam) {
		String sql = "SELECT DISTINCT xx.* FROM dw_examstudent_fact xs INNER JOIN dw_dim_school xx ON xs.schoolId=xx.id WHERE xs.examId=?";
		SQLQuery query = createSQLQuery(sql);
		query.setParameter(0, exam.getId());
		query.addEntity("xx", School.class);
	
		return query.list();
	}
	
	@Override
	public String[] findExamNations(Exam exam) {
		String sql = "SELECT DISTINCT xs.nation FROM dw_examstudent_fact xs INNER JOIN dw_dim_school xx ON xs.schoolId=xx.id WHERE xs.examId=?";
		SQLQuery query = createSQLQuery(sql);
		query.setParameter(0, exam.getId());
		//query.setResultTransformer(Transformers.TO_LIST);
		List list = query.list();
		String[] nations = {};
		if(list!= null && list.size() > 0) {
			nations = new String[list.size()];
			for(int i=0;i<list.size();i++) {
				nations[i] = list.get(i) +"";
			}
				
		}
		return nations;
	}

	@Override
	public boolean hasStudentsAndSubjcetsAndCj(Long examid) {
		String sql = "SELECT COUNT(1) FROM kn_exam e WHERE e.hasExamStudent=1 AND e.impItemCount=e.impCjCount AND impCjCount>0 AND e.id=?";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setLong(0, examid);
		List list = query.list();
		if(list!=null && list.size()>0){
			if(Integer.parseInt(list.get(0).toString())>0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}

	@Override
	public List<Exam> getExamAllList() {
		String hql="From Exam where 1=1";
		return findByHql(hql);
	}

	@Override
	public int getItemNumByExamid(Long examid) {
		int num=0;
		String sql = "SELECT COUNT(DISTINCT(testPaperid)) FROM dw_dim_item WHERE examid=?";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setLong(0, examid);
		num=query.uniqueResult()!=""?Integer.parseInt(query.uniqueResult().toString()):0;
		return num;
	}
	
	@Override
	public boolean getHasChoice(Long examid) {
		int num=0;
		boolean haschoice = false;
		String sql = "SELECT COUNT(DISTINCT(ischoice)) FROM dw_dim_item WHERE examid=?";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setLong(0, examid);
		num=query.uniqueResult()!=""?Integer.parseInt(query.uniqueResult().toString()):0;
		if(num > 1){
			haschoice = true;
		}
		return haschoice;
	}

	@Override
	public List<Exam> getExamByorgCodes(String codes) {
		String hql = "select * from kn_exam where ownerCode IN("+codes+")";
		SQLQuery query = getSession().createSQLQuery(hql);
		return query.list();
	}

	@Override
	public void examlist(com.cntest.common.query.Query<Exam> query, Long userId) {
		SQLQuery qry=null;
		String sql="select count(1) from kn_exam a left join kn_user_exam b on a.id=b.exam_id "
				+ " left join 4a_user c on b.user_id=c.user_id where c.user_id= "+userId;
		qry=getSession().createSQLQuery(sql);
		int totalRows=((List<BigInteger>)qry.list()).get(0).intValue();
		query.setTotalRows(totalRows);
		
	    sql="select a.* from kn_exam "
				+ " a inner join kn_user_exam b on a.id=b.exam_id "
				+ " inner join 4a_user c on b.user_id=c.user_id where c.user_id= "+userId;
		qry=getSession().createSQLQuery(sql);
		
		qry.setFirstResult(query.getStartRow());
		qry.setMaxResults(query.getPagesize());
		List<Object[]> objs=qry.list();
	    List<Exam> examList=this.examListForObejct(objs);
	  
	    
       query.setResults(examList);
	}

	
	//值转换
	public List<Exam> examListForObejct(List<Object[]> list){
				List<Exam> examList=new ArrayList<Exam>();
				for(Object[] obj:list){
					Exam exam=new Exam();
					exam.setId(Long.valueOf(obj[0].toString()));
					exam.setExamDate(DateUtil.convertStringToDate(obj[3].toString()));
					exam.setName(obj[1].toString());
					exam.setOwnerName(obj[15].toString());
					exam.setSortName(obj[2].toString());
					exam.setCreateUserName(obj[20].toString());
					exam.setStatus(Integer.valueOf(obj[18].toString()));
					examList.add(exam);
				}
				return examList;
			}

	@Override
	public void updateExamById(Long examId) throws BusinessException {
		Exam exam=findById(examId);
		List orgList=new ArrayList<>();
		String sql=" FROM dw_examstudent_fact WHERE examid="+examId;
		SQLQuery query = null;
		if(exam.getLevelCode()<=4){
			String hql="SELECT DISTINCT(schoolId) "+sql;
			query=getSession().createSQLQuery(hql);
			orgList=query.list();
		}
		if(exam.getLevelCode()<=3){
			String hql="SELECT DISTINCT(countyId) "+sql;
			query=getSession().createSQLQuery(hql);
			List list2=query.list();
			for(int i=0;i<list2.size();i++){
				orgList.add(list2.get(i));
			}
		}
		if(exam.getLevelCode()<=2){
			String hql="SELECT DISTINCT(cityId) "+sql;
			query=getSession().createSQLQuery(hql);
			List list2=query.list();
			for(int i=0;i<list2.size();i++){
				orgList.add(list2.get(i));
			}
		}
		if(exam.getLevelCode()<=1){
			String hql="SELECT DISTINCT(provinceid) "+sql;
			query=getSession().createSQLQuery(hql);
			List list2=query.list();
			for(int i=0;i<list2.size();i++){
				orgList.add(list2.get(i));
			}
		}
		if(orgList.size()>0){
			//更新考试信息的群体
			boolean hasWlInStudentInfo = examStudentDao.isHasWL(examId);
			String containOrg=orgList.toString();
			containOrg=containOrg.substring(1,containOrg.length()-1);
			exam.setHasExamStudent(true);
			exam.setWlForExamStudent(hasWlInStudentInfo);
			exam.setContainOrg(containOrg.replaceAll(" ", ""));
			update(exam);
		}
	}

	@Override
	public List<Exam> listBybach(Page<Exam> page, Integer... status) {
		Map<String,String> map = page.getParameter();
		String examName = map.get("examName");
		Criteria criteria = this.getSession().createCriteria(this.getEntityClass().getName());
		if(examName!=null && !"".equals(examName)){
			criteria.add(Restrictions.like("name","%"+examName+"%"));
		}
		if (status!=null && status.length>0) {
			criteria.add(Restrictions.in("status",status));
		}
		Long rowCount = (Long) criteria.setProjection(Projections.countDistinct("id")).uniqueResult();
		criteria.setProjection(null);
		page.setTotalRows(rowCount.intValue());
		int first = (page.getCurpage() - 1) * page.getPagesize();
		first = first < 0 ? 0 : first;
		criteria.setFirstResult(first);
		criteria.setMaxResults(page.getPagesize());
		criteria.addOrder(Order.desc("examDate"));
		criteria.setResultTransformer(RootEntityResultTransformer.INSTANCE);
		List<Exam> list = criteria.list();
		page.setList(list);
		return list;
	}

	@Override
	public List<Exam> getExamAllSchoolYears() {
		String hql = "SELECT DISTINCT(schoolYear) schoolYear,schoolYearName FROM kn_exam WHERE 1=1 order by schoolYear";
		SQLQuery query = getSession().createSQLQuery(hql);
		List<Object[]>  list=query.list();
		List<Exam> schoolYears=new ArrayList<Exam>();
		for(Object[] obj:list){
			Exam exam= new Exam();
			exam.setSchoolYear(Integer.parseInt(obj[0].toString()));
			exam.setSchoolYearName(obj[1].toString());
			schoolYears.add(exam);
		}
//		if(schoolYears.size()==0){
//			Exam exam= new Exam();
//			exam.setSchoolYear(0);
//			exam.setSchoolYearName("暂无学年数据");
//			schoolYears.add(exam);
//		}
		return schoolYears;
	}
}
