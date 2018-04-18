/*
 * @(#)com.cntest.fxpt.repository.impl.CjDaoImpl.java 1.0 2014年10月17日:下午3:40:47
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.anlaysis.bean.Param;
import com.cntest.fxpt.anlaysis.bean.TempItemScore;
import com.cntest.fxpt.anlaysis.bean.TempTotalScore;
import com.cntest.fxpt.bean.CjWebRetrieveResult;
import com.cntest.fxpt.domain.ExamCjDataSum;
import com.cntest.fxpt.domain.TestPaper;
import com.cntest.fxpt.repository.ICjDao;
import com.cntest.fxpt.util.SystemConfig;
import com.cntest.util.DateUtil;
import com.cntest.util.SpringContext;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年10月17日 下午3:40:47
 * @version 1.0
 */
@Repository("ICjDao")
public class CjDaoImpl extends AbstractHibernateDao<TestPaper, Long> implements ICjDao {

  /**
   * 导入失败后，删除零时表成绩
   * 
   * @param testPaperId
   * @param name
   * @return
   */
  @Override
  public int deleteCjKn(Long testPaperId) {

    String sql = "DELETE FROM kn_examitemcj_tmp where 1=1 ";
    SQLQuery sqlQuery = createSQLQuery(sql);
    sqlQuery.executeUpdate();

    sql = "DELETE FROM kn_examtestpapercj_tmp where 1=1";
    sqlQuery = createSQLQuery(sql);
    int count = sqlQuery.executeUpdate();

    return count;
  }

  @Override
  public int deleteCj(Long testPaperId) {

    String sql = "DELETE FROM dw_itemcj_fact WHERE testPaperId=?";
    SQLQuery sqlQuery = createSQLQuery(sql);
    sqlQuery.setParameter(0, testPaperId);
    sqlQuery.executeUpdate();

    sql = "DELETE FROM dw_testpapercj_fact WHERE testPaperId=?";
    sqlQuery = createSQLQuery(sql);
    sqlQuery.setParameter(0, testPaperId);
    int count = sqlQuery.executeUpdate();

    return count;
  }

  @Override
  public void copyCjandItemCj(CjWebRetrieveResult wrr) {
    boolean flag = false;
    Long examId = wrr.getExamId();
    Long testPaperId = wrr.getTestPaperId();
    int importType = wrr.getImportType();
    // 追加导入时，删除重复数据（重复最小单位：学校）
    try {
      if (importType == 1) {
        // copy学生之前删除数据库本来的学生信息（按学校）
        String hql =
            "SELECT DISTINCT(studentId) FROM kn_examtestpapercj_tmp where examid=? and testPaperId=?";
        SQLQuery query = getSession().createSQLQuery(hql);
        query.setLong(0, examId);
        query.setLong(1, testPaperId);
        List list = query.list();
        if (list.size() > 0) {
          // 删除数据
          flag = appendCleanBySchoolIds(testPaperId, list);
        }
      }
      if (flag || importType != 1) {
        // 复制全卷成绩零时表数据，并删除零时表数据
        insertCjTofact(examId, testPaperId);
        // 复制小题成绩零时表数据，并删除零时表数据
        insertItemCjTofact(examId, testPaperId);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  public void dataValidate(Long examId, Long testPaperId) {
    HashMap<String, String> hmap = new HashMap<String, String>();
    // 出现次数的集合
    String one = "";
    String two = "";
    String three = "";
    String paper = "";
    String[] others = null;
    int mes = 0;
    // 小题分小数点有后两位处理：0.25处理成0.5，0.75处理成1
    // 查询是否存在AB卷
    String sql = "SELECT DISTINCT(paper) FROM  dw_dim_item WHERE examid = " + examId
        + " AND testPaperId =" + testPaperId;
    SQLQuery query = getSession().createSQLQuery(sql);
    List listAB = query.list();
    for (int a = 0; a < listAB.size(); a++) {
      paper = listAB.get(a) == null ? null : "='" + listAB.get(a).toString() + "'";
      // 同一个学生出现x.25或者x.75的成绩的Id和出现次数
      sql = "SELECT studentid,COUNT(studentId) FROM kn_examitemcj_tmp WHERE examid = " + examId
          + "  AND testPaperId = " + testPaperId + "  AND optionType = 0  AND paper "
          + (paper == null ? " IS NULL " : paper)
          + " AND (score LIKE '%.25' or score LIKE '%.75') GROUP BY studentId";
      query = getSession().createSQLQuery(sql);
      List list = query.list();
      for (int i = 0; i < list.size(); i++) {
        Object[] ant = (Object[]) list.get(i);
        if ("1".equals(ant[1].toString())) {
          one += ant[0].toString() + ",";
        } else if ("2".equals(ant[1].toString())) {
          two += ant[0].toString() + ",";
        } else if ("3".equals(ant[1].toString())) {
          three += ant[0].toString() + ",";
        } else {
          others[mes] = ant[0].toString();
          mes++;
          hmap.put(ant[0].toString(), ant[1].toString());
        }
      }
      updateScore(one, 1, examId, testPaperId, paper);
      updateScore(two, 2, examId, testPaperId, paper);
      updateScore(three, 3, examId, testPaperId, paper);
      // 重复执行同一个人出现.25和.75次数大于3次的（此情况较少，所以单独处理）
      if (others != null)
        for (String m : others) {
          if (!"".equals(m)) {
            updateScore(m + ",", Integer.parseInt(hmap.get(m)), examId, testPaperId, paper);
          }
        }
    }
    // 初三和高三的总分四舍五入，其他的保留一位小数
    updateTotalScore(examId, testPaperId);
  }

  // 初三和高三的总分四舍五入，其他的保留一位小数
  private void updateTotalScore(Long examId, Long testPaperId) {
    // 查询此次考试是否是初三或者高三
    String schoolType = "";
    String sql =
        "SELECT grade.NAME FROM kn_exam exam INNER JOIN kn_grade grade ON exam.gradeId=grade.id WHERE exam.id="
            + examId;
    SQLQuery query = getSession().createSQLQuery(sql);
    Object obj = query.uniqueResult();
    schoolType = obj == null ? "" : obj.toString();
    // 初三高三总分四舍五入
    if ("初三".equals(schoolType) || "高三".equals(schoolType)) {
      // 更新细目表的总分
      sql =
          "UPDATE kn_examitemcj_tmp SET totalScore=ROUND(CONVERT(totalscore,DECIMAL))  WHERE examid = "
              + examId + " AND testPaperId=" + testPaperId;
      query = getSession().createSQLQuery(sql);
      query.executeUpdate();
      // 更新试卷成绩的总分
      sql =
          "UPDATE kn_examtestpapercj_tmp SET totalScore=ROUND(CONVERT(totalscore,DECIMAL))  WHERE examid = "
              + examId + " AND testPaperId=" + testPaperId;
      query = getSession().createSQLQuery(sql);
      query.executeUpdate();
    }

  }

  /**
   * 更新小题分数，客观题总分，以及试卷总分(根据考生Id和对应出现需要处理的分数的个数)
   * 
   * @param studentIds 考生id串
   * @param num 出现 0.25或者 0.75的次数
   * @param examId 考试id
   * @param testPaperId 试卷Id
   * @param paper AB卷 为空时表示不分AB卷
   */
  private void updateScore(String studentIds, int num, Long examId, Long testPaperId,
      String paper) {
    if (studentIds != "") {
      // 去掉最后一个逗号
      studentIds = studentIds.substring(0, studentIds.length() - 1);
      // 更新需要处理的小题成绩（0.25和0.75的成绩都加0.25）
      String sql = "UPDATE kn_examitemcj_tmp SET score=score+0.25 " + " WHERE examid = " + examId
          + "  AND testPaperId = " + testPaperId + "  AND  optionType=0 AND paper "
          + (paper == null ? " IS NULL " : paper) + " AND  studentId in(" + studentIds + ")"
          + " AND (score LIKE '%.25' OR score LIKE '%.75')";
      SQLQuery query = getSession().createSQLQuery(sql);
      query.executeUpdate();
      // 更新小题表的总成绩字段
      sql = "UPDATE kn_examitemcj_tmp SET " + " totalScore=totalScore+" + num * 0.25
          + " WHERE examid = " + examId + " AND testPaperId = " + testPaperId
          + " AND  studentId in(" + studentIds + ")";
      query = getSession().createSQLQuery(sql);
      query.executeUpdate();
      // 主观题成绩和总分成绩都加 num*0.25
      sql = "UPDATE kn_examtestpapercj_tmp SET zgscore=zgscore+" + num * 0.25
          + ",totalScore=totalScore+" + num * 0.25 + "  WHERE examid = " + examId
          + "  AND testPaperId = " + testPaperId + "  AND paper "
          + (paper == null ? " IS NULL " : paper) + " AND  studentId in(" + studentIds + ")";
      query = getSession().createSQLQuery(sql);
      query.executeUpdate();
      if (paper != null) {
        sql = "UPDATE kn_examtestpapercj_tmp SET zgScore=zgScore+" + num * 0.25
            + ",totalScore=totalScore+" + num * 0.25 + "  WHERE examid = " + examId
            + "  AND testPaperId = " + testPaperId + "  AND (paper='' or paper='SX')"
            + " AND  studentId in(" + studentIds + ")";
        query = getSession().createSQLQuery(sql);
        query.executeUpdate();
      }
    }
  }

  private void insertItemCjTofact(Long examId, Long testPaperId) {



    String sql = "select count(*) from kn_examitemcj_tmp where examId=" + examId
        + " and testPaperId=" + testPaperId;
    SQLQuery sqlQuery = getSession().createSQLQuery(sql);
    Object obj = sqlQuery.uniqueResult();
    int sumNum = Integer.parseInt(obj.toString());
    // 删除重复学生的批处理。
    int sum = 100000;// 定义处理条数常量
    int num = 0; // 批量处理的次数(一次最多处理sum条数据)
    num = sumNum / sum;
    if (sumNum % sum > 0) {
      num = num + 1;
    }
    // 获取导入字段信息
    String[] fieldss = loadBeCopyCJFields("beCopyItemCjFileds");
    StringBuffer insertFields = new StringBuffer();
    StringBuffer valueFields = new StringBuffer();

    for (String f : fieldss) {
      insertFields.append(",").append(f);
      valueFields.append(",").append("xs.").append(f);
    }
    for (int i = 0; i < num; i++) {
      // 拼装SQL语句
      sql = "INSERT INTO dw_itemcj_fact(examid" + insertFields.toString() + ") "
          + "SELECT  xs.examid" + valueFields.toString() + " FROM kn_examitemcj_tmp xs "
          + "LEFT JOIN (SELECT id,examId  FROM  dw_examstudent_fact WHERE examid=" + examId
          + ") ef ON ef.examId =xs.examId AND xs.studentId=ef.id " + " WHERE xs.examId=" + examId
          + " AND xs.testPaperId=" + testPaperId + " order by xs.studentId LIMIT " + sum;;
      sqlQuery = getSession().createSQLQuery(sql);
      sqlQuery.executeUpdate();
      // 删除零时表信息
      sql =
          "DELETE FROM kn_examitemcj_tmp where examId=? and testPaperId=?  order by studentId  LIMIT ?";
      sqlQuery = getSession().createSQLQuery(sql);
      sqlQuery.setLong(0, examId);
      sqlQuery.setLong(1, testPaperId);
      sqlQuery.setLong(2, sum);
      sqlQuery.executeUpdate();
    }
  }

  private void insertCjTofact(Long examId, Long testPaperId) {


    String sql = "select count(*) from kn_examtestpapercj_tmp where examId=" + examId
        + " and testPaperId=" + testPaperId;
    SQLQuery sqlQuery = getSession().createSQLQuery(sql);
    Object obj = sqlQuery.uniqueResult();
    int sumNum = Integer.parseInt(obj.toString());
    // 删除重复学生的批处理。
    int sum = 10000;// 定义处理条数常量
    int num = 0; // 批量处理的次数(一次最多处理sum条数据)
    num = sumNum / sum;
    if (sumNum % sum > 0) {
      num = num + 1;
    }
    // 获取导入字段信息
    String[] fieldss = loadBeCopyCJFields("beCopyCjFileds");

    StringBuffer insertFields = new StringBuffer();
    StringBuffer valueFields = new StringBuffer();

    for (String f : fieldss) {
      insertFields.append(",").append(f);
      valueFields.append(",").append("xs.").append(f);
    }
    for (int i = 0; i < num; i++) {
      // 拼装SQL语句
      sql = "INSERT INTO dw_testpapercj_fact(examid" + insertFields.toString() + ") "
          + " SELECT xs.examid" + valueFields.toString() + " FROM kn_examtestpapercj_tmp xs "
          + " LEFT JOIN (SELECT id,examId FROM  dw_examstudent_fact WHERE examid=" + examId
          + ") ef ON ef.examId =xs.examId AND xs.studentId=ef.id " + " WHERE xs.examId=" + examId
          + " AND xs.testPaperId=" + testPaperId + " order by xs.studentId LIMIT " + sum;;

      sqlQuery = getSession().createSQLQuery(sql);
      sqlQuery.executeUpdate();
      // 删除零时表信息
      sql =
          "DELETE FROM kn_examtestpapercj_tmp where examId=? and testPaperId=?  order by studentId  LIMIT ?";
      sqlQuery = getSession().createSQLQuery(sql);
      sqlQuery.setLong(0, examId);
      sqlQuery.setLong(1, testPaperId);
      sqlQuery.setLong(2, sum);
      sqlQuery.executeUpdate();
    }

  }

  private String[] loadBeCopyCJFields(String name) {
    String filed = SystemConfig.newInstance().getValue(name);
    return filed.split(",");
  }

  /**
   * 追加导入成绩前的清除原有数据
   */
  @Override
  public boolean appendCleanBySchoolIds(Long testPaperId, List schoolIds) {
    boolean flag = false;
    try {
      StringBuffer strBuffer = new StringBuffer();
      // 将hashSet里保存的学习code拼装成字符串“，”隔开
      for (int i = 0; i < schoolIds.size(); i++) {
        strBuffer.append("'" + schoolIds.get(i) + "'" + ",");
      }
      // 去除最后一个“，”
      String str = strBuffer.substring(0, strBuffer.length() - 1);
      if (!"".equals(str) || str != null) {

        String sql = "DELETE FROM dw_itemcj_fact WHERE testPaperId=" + testPaperId
            + " and studentId in(" + str + ")";
        SQLQuery sqlQuery = createSQLQuery(sql);
        sqlQuery.executeUpdate();

        sql = "DELETE FROM dw_testpapercj_fact WHERE testPaperId=" + testPaperId
            + " and studentId in(" + str + ")";
        sqlQuery = createSQLQuery(sql);
        sqlQuery.executeUpdate();

        flag = true;
      } else {
        flag = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return flag;
  }

  @Override
  public int deleteExamAllCj(Long examId) {
    String sql = "DELETE FROM dw_itemcj_fact WHERE examId=?";
    SQLQuery sqlQuery = createSQLQuery(sql);
    sqlQuery.setParameter(0, examId);
    sqlQuery.executeUpdate();

    sql = "DELETE FROM dw_testpapercj_fact WHERE examId=?";
    sqlQuery = createSQLQuery(sql);
    sqlQuery.setParameter(0, examId);
    int count = sqlQuery.executeUpdate();

    return count;
  }

  @Override
  protected Class<TestPaper> getEntityClass() {
    return TestPaper.class;
  }

  @Override
  public List<Integer> getExamStudentWlFor(Long testPaperId) {
    String sql =
        "SELECT wl FROM dw_testpapercj_fact cj WHERE cj.testPaperId=? GROUP BY wl ORDER BY NULL";
    SQLQuery sqlQuery = createSQLQuery(sql);
    sqlQuery.setLong(0, testPaperId);
    List<Object> tmpList = sqlQuery.list();
    ArrayList<Integer> resultList = new ArrayList<Integer>();
    for (Object tmp : tmpList) {
      resultList.add(Integer.parseInt(tmp.toString()));
    }
    return resultList;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.repository.ICjDao#list(java.lang.Long, java.lang.Long, int, int)
   */
  @Override
  public List<Map<String, Object>> list(Long examId, int wl, Long testPaperId, Long schoolId,
      int firstNum, int maxNum) {
    String sql = "{Call P_cjList(?,?,?,?,?,?)}";
    SQLQuery sqlQuery = createSQLQuery(sql);
    sqlQuery.addScalar("id");
    sqlQuery.addScalar("NAME");
    sqlQuery.addScalar("zkzh");
    sqlQuery.addScalar("isQk");
    sqlQuery.addScalar("totalScore");
    sqlQuery.addScalar("zgScore");
    sqlQuery.addScalar("kgScore");
    sqlQuery.addScalar("itemId");
    sqlQuery.addScalar("score");
    sqlQuery.addScalar("selOpt");

    sqlQuery.setLong(0, examId);
    sqlQuery.setLong(1, wl);
    sqlQuery.setLong(2, schoolId);
    sqlQuery.setLong(3, testPaperId);
    sqlQuery.setInteger(4, firstNum);
    sqlQuery.setInteger(5, maxNum);

    sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    return sqlQuery.list();
  }

  @Override
  public Map<Long, Integer> statTestPaperSKRS(Long examId) {
    String sql =
        "SELECT testPaperId,COUNT(DISTINCT analysisTestpaperId) sjNum,SUM(IF(isQk = 0,1,0)) skrs "
            + "FROM dw_testpapercj_fact " + "WHERE examId=? " + "GROUP BY testPaperId "
            + "ORDER BY NULL";
    SQLQuery sqlQuery = createSQLQuery(sql);
    sqlQuery.setParameter(0, examId);
    sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    List<Map<String, Object>> testPapersSKRS = sqlQuery.list();

    HashMap<Long, Integer> result = new HashMap<Long, Integer>();
    for (Map<String, Object> tmp : testPapersSKRS) {
      Long testPaperId = Long.parseLong(tmp.get("testPaperId").toString());
      Integer skrs = Integer.parseInt(tmp.get("skrs").toString());
      Integer sjNum = Integer.parseInt(tmp.get("sjNum").toString());
      result.put(testPaperId, skrs / sjNum);
    }

    return result;
  }

  @Override
  public List<TempTotalScore> loadCj(Long examId, Param... params) {
    String sql =
        "SELECT studentId,analysisTestpaperId,isQk,totalScore,zgScore,kgScore FROM dw_testpapercj_fact WHERE examId=?";
    for (Param param : params) {
      sql += " AND " + param.getKey() + "=?";
    }

    SQLQuery query = getSession().createSQLQuery(sql);
    query.setParameter(0, examId);
    int idx = 1;
    for (Param param : params) {
      query.setParameter(idx++, param.getValue());
    }
    query.addScalar("studentId", LongType.INSTANCE)
        .addScalar("analysisTestpaperId", LongType.INSTANCE).addScalar("isQk", BooleanType.INSTANCE)
        .addScalar("totalScore", DoubleType.INSTANCE).addScalar("zgScore", DoubleType.INSTANCE)
        .addScalar("kgScore", DoubleType.INSTANCE);
    query.setResultTransformer(Transformers.aliasToBean(TempTotalScore.class));
    return query.list();
  }

  @Override
  public List<TempItemScore> loadItemCj(Long examId, Param... params) {
    String sql =
        "SELECT studentId,analysisTestpaperId,itemId,score,selOption FROM dw_itemcj_fact WHERE examId=?";
    for (Param param : params) {
      sql += " AND " + param.getKey() + "=?";
    }
    SQLQuery query = getSession().createSQLQuery(sql);
    query.setParameter(0, examId);
    int idx = 1;
    for (Param param : params) {
      query.setParameter(idx++, param.getValue());
    }
    query.addScalar("studentId", LongType.INSTANCE)
        .addScalar("analysisTestpaperId", LongType.INSTANCE).addScalar("itemId", LongType.INSTANCE)
        .addScalar("score", DoubleType.INSTANCE).addScalar("selOption", StringType.INSTANCE);
    query.setResultTransformer(Transformers.aliasToBean(TempItemScore.class));
    return query.list();
  }

  @Override
  public List<ExamCjDataSum> dataSumList(String examData, String examName) {
    String sqlAll = "";
    if (examName != null && examName != "") {
      sqlAll = " where exam.examName like '%" + examName + "%'";
    }
    String sql = "SELECT cjf.examId examId,exam.examDate examdate,exam.examName ,"
        + "exam.num allNum,cjf.subjectName subjectName,cjf.num subjectNum FROM "
        + "(SELECT COUNT(cj.testpaperId) num,tp.name subjectName,cj.examId examId FROM dw_testpapercj_fact cj "
        + "LEFT JOIN kn_testpaper tp " + "ON tp.examId=cj.examId AND tp.id=cj.testpaperId "
        + "GROUP BY cj.testpaperId) cjf " + "LEFT JOIN ("
        + "SELECT ex.NAME examName,ex.examDate examDate,stu.num num,ex.id examId FROM kn_exam ex "
        + "LEFT JOIN (SELECT COUNT(st.examId) num,st.examId examId FROM dw_examstudent_fact st GROUP BY st.examId)  stu "
        + "ON ex.id=stu.examId " + ") exam ON exam.examId = cjf.examId";
    sql += sqlAll + "  ORDER BY   exam.examDate DESC ";
    SQLQuery sqlQuery = createSQLQuery(sql);
    List list = sqlQuery.list();
    List<ExamCjDataSum> dataList = listToDataList(list);
    return dataList;
  }

  /**
   * 类型转换
   * 
   * @param list
   * @return
   */
  private List<ExamCjDataSum> listToDataList(List list) {
    List<ExamCjDataSum> dataList = new ArrayList<ExamCjDataSum>();
    if (list.size() > 0) {

      Object[] obj = null;
      for (int i = 0; i < list.size(); i++) {
        obj = (Object[]) list.get(i);
        ExamCjDataSum dataSum = new ExamCjDataSum();
        dataSum.setExamId(Long.parseLong(obj[0] == "" ? "" : obj[0].toString()));
        dataSum.setExamDate(DateUtil.convertStringToDate(obj[1].toString()));
        dataSum.setExamName(obj[2] == "" ? "" : obj[2].toString());
        dataSum.setAllNum(Long.parseLong(obj[3] == null ? "0" : obj[3].toString()));
        dataSum.setSubjectName(obj[4] == "" ? "" : obj[4].toString());
        dataSum.setSubjectNum(Long.parseLong(obj[5] == "" ? "0" : obj[5].toString()));

        dataList.add(dataSum);
      }

    }
    return dataList;

  }

  @Override
  public List getStudentIds(Long examId, Long testPaperId) {
    String hql = "SELECT DISTINCT(studentId) FROM kn_examitemcj_tmp WHERE "
        + "examid=? AND testPaperId=? AND studentId IS NOT NULL";
    SQLQuery query = getSession().createSQLQuery(hql);
    query.setLong(0, examId);
    query.setLong(1, testPaperId);
    return query.list();
  }

  @Override
  public List<TempItemScore> finChoiseByStudentId(String studentid, String itemNos, Long examId,
      Long testPaperId) {
    String sql =
        "SELECT studentId,analysisTestpaperId,itemId,score,selOption FROM kn_examitemcj_tmp "
            + "WHERE examId=? and studentId=? " + "and testPaperId=? and itemid " + "in (" + itemNos
            + ")";
    SQLQuery query = getSession().createSQLQuery(sql);
    query.setParameter(0, examId);
    query.setParameter(1, studentid);
    query.setParameter(2, testPaperId);
    query.addScalar("studentId", LongType.INSTANCE)
        .addScalar("analysisTestpaperId", LongType.INSTANCE).addScalar("itemId", LongType.INSTANCE)
        .addScalar("score", DoubleType.INSTANCE).addScalar("selOption", StringType.INSTANCE);
    query.setResultTransformer(Transformers.aliasToBean(TempItemScore.class));
    return query.list();
  }

  @Override
  public void updateCjTempStatusNo(String studentid, String itemNos, Long examId, Long testPaperId,
      int score) {
    String sc = "";
    if (score == -888) {
      sc = ",score=-888";
    }
    String sql = "UPDATE  kn_examitemcj_tmp SET isvalid=0" + sc + "  WHERE "
        + "examId=? AND studentId=? " + "AND testPaperId=? AND itemid " + "in (" + itemNos + ")";
    SQLQuery query = getSession().createSQLQuery(sql);
    query.setParameter(0, examId);
    query.setParameter(1, studentid);
    query.setParameter(2, testPaperId);
    query.executeUpdate();
  }

  @Override
  public void updateCjTempStatusIn(String studentid, String itemNos, Long examId, Long testPaperId,
      int score) {
    String sc = "";
    if (score == 0) {
      sc = ",score=0";
    }
    String sql = "UPDATE  kn_examitemcj_tmp SET isvalid=1" + sc + "  WHERE "
        + "examId=? AND studentId=? " + "AND testPaperId=? AND itemid " + "in (" + itemNos + ")";
    SQLQuery query = getSession().createSQLQuery(sql);
    query.setParameter(0, examId);
    query.setParameter(1, studentid);
    query.setParameter(2, testPaperId);
    query.executeUpdate();
  }

  /**
   * 更新选做题异常数据
   * 
   * @param testpaperId
   * @param rules 多选题的方式 before:从前往后取数； 反之 从高往低取数；
   * @throws Exception
   */
  public void updateChoiceData(Long testpaperId, String rules) throws Exception {
    /*
     * #少，多，正常 #正常(同一小题，同一学生对应的score不为-888的 个数 = 此小题的选择个数)：将score有值对应的isvalid 设置为1
     * #少(同一小题，同一学生对应的score不为-888的个数< 此小题的选择个数)：从最上面起对应的score设置为0 && isvalid 设置为1
     * #多(同一小题，同一学生对应的score不为-888的个数>此小题的选择的个数)： 1. 取最大值 2. 取前面的值
     */
    List<String> tmpSqls = new ArrayList<>();
    Long studentid = null;
    String choicegroup = "";
    Long itemid = null;
    int num = 0;
    List<Object[]> list = getUnusual(testpaperId);
    for (Object[] object : list) {
      studentid = Long.parseLong(object[0] == "" ? "0" : object[0].toString());
      choicegroup = object[1] == "" ? "" : object[1].toString();
      num = Integer.parseInt(object[2] == "" ? "" : object[2].toString());
      if (num > 0) {
        List list2 = getLessRecord(testpaperId, studentid, choicegroup);
        for (int i = 0; i < list2.size() && i < num; i++) {
          itemid = Long.parseLong(list2.get(i) == null ? null : list2.get(i).toString());
          String sql = "UPDATE kn_examitemcj_tmp SET score =0 WHERE studentid =" + studentid
              + " AND itemid in " + getItemIds(itemid);
          tmpSqls.add(sql);
        }

      } else {
        List list2 = getMoreRecord(testpaperId, studentid, choicegroup, rules);
        for (int i = 0; i < list2.size() && i < (-num); i++) {
          itemid = Long.parseLong(list2.get(i) == null ? null : list2.get(i).toString());
          String sql = "UPDATE kn_examitemcj_tmp SET score =-888 WHERE studentid =" + studentid
              + " AND itemid in " + getItemIds(itemid);
          tmpSqls.add(sql);
        }
      }
    }
    if (tmpSqls.size() > 0) {
      updateScore(tmpSqls);
    }
    updateCJEffective(testpaperId);
    updateCjIsvalid(testpaperId);
  }

  private String getItemIds(Long itemid) {
    return "(SELECT item2.id FROM dw_dim_item item1 "
        + "\n LEFT JOIN dw_dim_item item2 ON item1.choicegroup =item2.choicegroup AND item1.choiceNumber=item2.choiceNumber AND item1.testpaperId = item2.testpaperId"
        + "\n WHERE item1.id =" + itemid + ")";
  }

  /**
   * 执行所有-888分数的isvalid的状态为0
   * 
   * @param testPaperId
   */
  private void updateCjIsvalid(Long testPaperId) {
    StringBuffer buffer = new StringBuffer(" ");
    buffer.append(" UPDATE kn_examitemcj_tmp cj SET cj.isvalid=0 ");
    buffer.append(" WHERE testpaperId =? AND cj.score=-888 ");
    SQLQuery query = getSession().createSQLQuery(buffer.toString());
    query.setLong(0, testPaperId);
    query.executeUpdate();
  }

  /**
   * 执行所有异常数据
   * 
   * @param tmpSqls
   * @throws Exception
   */
  private void updateScore(List<String> tmpSqls) throws Exception {
    DataSource ds = SpringContext.getBean("ds");
    Connection con = ds.getConnection();
    con.setAutoCommit(false);
    PreparedStatement ps = con.prepareStatement("");
    try {
      for (String sql : tmpSqls) {
        ps.addBatch(sql);
      }
      ps.executeBatch();
      con.commit();
      ps.clearBatch();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      ps.close();
      con.close();
    }
  }

  /**
   * 更新所有有效的选做题 isvalid = 1
   * 
   * @param testpaperId
   */
  private void updateCJEffective(Long testpaperId) throws Exception {
    StringBuffer buffer = new StringBuffer(" ");
    buffer.append(" UPDATE kn_examitemcj_tmp cj SET cj.isvalid=1 ");
    buffer.append(" WHERE testpaperId =? AND cj.score <> -888 ");
    buffer.append("	AND cj.itemid IN( ");
    buffer.append(
        "		SELECT id FROM dw_dim_item WHERE ischoice =1 AND choiceNumber IS NOT NULL AND testpaperId =? ");
    buffer.append("	)");

    SQLQuery query = getSession().createSQLQuery(buffer.toString());
    query.setLong(0, testpaperId);
    query.setLong(1, testpaperId);
    query.executeUpdate();
  }

  /**
   * 1. 方式1 （从前到后） 2.方式2（从分数高到低）
   * 
   * @param testpaperId
   * @param studentid
   * @param choicegroup
   * @param isSort
   * @return
   */
  private List getMoreRecord(Long testpaperId, Long studentid, String choicegroup, String rules) {
    StringBuffer buffer = new StringBuffer(" ");
    buffer.append(" SELECT cj.itemid ").append("\n");
    buffer.append("	FROM kn_examitemcj_tmp cj ").append("\n");
    buffer.append("	INNER JOIN " + getItemTmp2(testpaperId) + " item ON cj.itemid = item.id ")
        .append("\n");
    buffer
        .append(
            "	WHERE score<>-888 AND cj.testpaperId =? AND cj.studentid =? AND item.choicegroup = ? ")
        .append("\n");
    buffer.append(" GROUP BY choiceNumber ").append("\n");
    if (rules.equalsIgnoreCase("before")) // 取前面的分数
      buffer.append(" ORDER BY RIGHT(item.choiceNumber,1) DESC ");
    else // 取最高分
      buffer.append(" ORDER BY SUM(score) DESC ");

    SQLQuery query = getSession().createSQLQuery(buffer.toString());
    query.setLong(0, testpaperId);
    query.setLong(1, studentid);
    query.setString(2, choicegroup);
    return query.list();
  }

  private List getLessRecord(Long testpaperId, Long studentid, String choicegroup) {
    StringBuffer buffer = new StringBuffer(" ");
    buffer.append(" SELECT cj.itemid ").append("\n");
    buffer.append("	FROM kn_examitemcj_tmp cj ").append("\n");
    buffer.append("	INNER JOIN " + getItemTmp(testpaperId) + " item ON cj.itemid = item.id ")
        .append("\n");
    buffer
        .append(
            "	WHERE score=-888 AND cj.testpaperId =? AND cj.studentid =? AND item.choicegroup = ? ")
        .append("\n");
    buffer.append("	ORDER BY RIGHT(item.choiceNumber,1) ASC ");

    SQLQuery query = getSession().createSQLQuery(buffer.toString());
    query.setLong(0, testpaperId);
    query.setLong(1, studentid);
    query.setString(2, choicegroup);
    return query.list();
  }

  /* 获取临时表 ,用于存储选做题同一题目的第一个小题 */
  private String getItemTmp(long testpaperId) {
    return "(SELECT id, choicegroup, choiceNumber FROM dw_dim_item "
        + "\n WHERE ischoice=1 AND choiceNumber IS NOT NULL AND testpaperId = " + testpaperId
        + "\n GROUP BY choicegroup, choiceNumber )";
  }

  private String getItemTmp2(long testpaperId) {
    return "(SELECT id, choicegroup, choiceNumber FROM dw_dim_item "
        + "\n WHERE ischoice=1 AND choiceNumber IS NOT NULL AND testpaperId = " + testpaperId
        + "\n GROUP BY id, choicegroup, choiceNumber )";
  }

  /**
   * 找到选做题异常数据
   * 
   * @param testpaperId
   * @return 学生ID 选做题组号 异常数据个数(应该做的题目个数-已做的题目个数) 忽略选做题中存在 选做题的小数部分有数据，部分为-888的情况，实施人员已规避处理
   */
  private List<Object[]> getUnusual(Long testpaperId) {
    StringBuffer buffer = new StringBuffer(" ");
    buffer.append("select * from( (SELECT studentid, choicegroup, choicedata-scorenum num FROM (")
        .append("\n");
    buffer
        .append(
            " 	SELECT item.choicegroup, cj.studentid, LEFT(item.choiceNumber, 1) choicedata, COUNT(cj.score) scorenum")
        .append("\n");
    buffer.append(" 	FROM kn_examitemcj_tmp cj").append("\n");
    buffer.append(" 	INNER JOIN " + getItemTmp(testpaperId) + " item ON cj.itemid = item.id ")
        .append("\n");
    buffer.append(" 	WHERE cj.score <> -888 AND cj.testpaperId =" + testpaperId).append("\n");
    buffer.append(" 	GROUP BY item.choicegroup, cj.studentid, LEFT(item.choiceNumber, 1)	")
        .append("\n");
    buffer.append(" ) t").append("\n");
    buffer.append(" WHERE choicedata <> scorenum").append("\n");
    buffer.append(" ORDER BY t.studentId, t.choicegroup ASC)").append("\n");

    buffer.append(" UNION ALL").append("\n");
    // 全部为-888
    buffer.append(" (SELECT a.studentid, a.choicegroup, a.choicedata FROM (").append("\n");
    buffer
        .append(
            " 	SELECT item.choicegroup, cj.studentid, LEFT(item.choiceNumber, 1) choicedata, COUNT(cj.score) scorenum ")
        .append("\n");
    buffer.append(" 	FROM kn_examitemcj_tmp cj").append("\n");
    buffer.append(" 	INNER JOIN " + getItemTmp(testpaperId) + " item ON cj.itemid = item.id ")
        .append("\n");
    buffer.append(" 	WHERE cj.score = -888 AND cj.testpaperId =" + testpaperId).append("\n");
    buffer.append(" 	GROUP BY item.choicegroup, cj.studentid, LEFT(item.choiceNumber, 1)	")
        .append("\n");
    buffer.append(" ) a").append("\n");
    buffer.append(" LEFT JOIN (").append("\n");
    buffer.append(" 	SELECT item.choicegroup, cj.studentid, COUNT(cj.score) scorenum ")
        .append("\n");
    buffer.append(" 	FROM kn_examitemcj_tmp cj").append("\n");
    buffer.append(" 	INNER JOIN " + getItemTmp(testpaperId) + " item ON cj.itemid = item.id ")
        .append("\n");
    buffer.append(" 	WHERE cj.testpaperId =" + testpaperId).append("\n");
    buffer.append(" 	GROUP BY item.choicegroup, cj.studentid, LEFT(item.choiceNumber, 1)")
        .append("\n");
    buffer.append(" ) b ON a.choicegroup = b.choicegroup AND a.studentid = b.studentid  ")
        .append("\n");
    buffer.append(" WHERE a.scorenum = b.scorenum").append("\n");
    buffer.append(" ORDER BY a.studentId, a.choicegroup ASC	)) a").append("\n");

    SQLQuery query = getSession().createSQLQuery(buffer.toString());
    /*
     * query.setLong(0, testpaperId); query.setLong(1, testpaperId); query.setLong(2, testpaperId);
     */
    List<Object[]> objList = query.list();
    return objList;
  }

  @Override
  public List<String> cjValidate(CjWebRetrieveResult wrr) throws Exception {
    List<String> result = new ArrayList<String>();

    String sql = "SELECT DISTINCT(stu.zkzh) FROM kn_examtestpapercj_tmp kn "
        + "INNER JOIN dw_testpapercj_fact dw ON kn.examid = dw.examid AND dw.studentId=kn.studentId   AND kn.testPaperId=dw.testPaperId   "
        + "INNER JOIN dw_examstudent_fact stu ON stu.id=kn.studentid AND stu.examId= kn.examId "
        + "WHERE kn.examid=" + wrr.getExamId() + " and kn.testPaperId=" + wrr.getTestPaperId();
    // "SELECT kn.studentId FROM kn_examtestpapercj_tmp kn INNER JOIN dw_testpapercj_fact dw ON
    // kn.examid = dw.examid AND dw.studentId=kn.studentId WHERE kn.examid = "+wrr.getExamId();
    SQLQuery query = getSession().createSQLQuery(sql);
    return (List<String>) query.list();
  }

}
