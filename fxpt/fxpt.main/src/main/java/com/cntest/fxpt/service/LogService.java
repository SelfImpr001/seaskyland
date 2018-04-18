/**
 * <p>
 * <b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b>
 * </p>
 * 
 **/

package com.cntest.fxpt.service;

import java.util.List;
import java.util.Map;

import com.cntest.common.query.Query;
import com.cntest.common.service.EntityService;
import com.cntest.foura.domain.User;
import com.cntest.fxpt.domain.Log;


/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 陈勇2016年9月7日
 * @version 1.0
 **/
public interface LogService extends EntityService<Log, Long> {
  public void save(Log log);

  public Log get(Long pk);

  void surveyLogQuery(Query<Log> query, Map<String, String> params);

  public List<Log> getlogList(Map<String, String> param);

  /**
   * 保存报表查看日志
   * 
   * @param examId 考试Id
   * @param uuid 报表的uuid
   */
  public void addLogFromModule(Long examId, String uuid, User user);
}

