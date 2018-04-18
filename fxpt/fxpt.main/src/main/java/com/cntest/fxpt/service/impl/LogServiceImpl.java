/**
 * <p>
 * <b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b>
 * </p>
 * 
 **/

package com.cntest.fxpt.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cntest.common.query.Query;
import com.cntest.common.service.AbstractEntityService;
import com.cntest.common.service.EntityService;
import com.cntest.foura.domain.User;
import com.cntest.foura.repository.URLResourceRepository;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.Log;
import com.cntest.fxpt.repository.IExamDao;
import com.cntest.fxpt.repository.LogDao;
import com.cntest.fxpt.service.LogService;
import com.cntest.fxpt.util.LogUtil;


/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2016年9月7日
 * @version 1.0
 **/
@Service
public class LogServiceImpl extends AbstractEntityService<Log, Long>
    implements EntityService<Log, Long>, LogService {
  @Autowired(required = false)
  private LogDao surveylogdao;

  @Override
  public void save(Log log) {
    surveylogdao.save(log);
  }

  @Autowired(required = false)
  private IExamDao examDao;

  @Autowired(required = false)
  private URLResourceRepository urlResourDao;

  @Override
  public List<Log> getlogList(Map<String, String> param) {
    return surveylogdao.getlogList(param);
  }

  @Override
  public void surveyLogQuery(Query<Log> query, Map<String, String> params) {
    surveylogdao.surveyLogQuery(query, params);
  }

  @Override
  public Log get(Long pk) {
    return surveylogdao.get(pk);
  }

  @Override
  public void addLogFromModule(Long examId, String urlResourName, User user) {
    Log log = new Log();
    Exam exam = examDao.findById(examId);
    // URLResource urlResource = urlResourDao.getResourceByUuid(uuid);
    // 不为空的时候记入查看日志
    if (urlResourName != null) {
      LogUtil.log("", urlResourName, exam.getName(), "成功",
          "查看" + exam.getName() + "--" + urlResourName + "数据", "");
    }

  }
}

