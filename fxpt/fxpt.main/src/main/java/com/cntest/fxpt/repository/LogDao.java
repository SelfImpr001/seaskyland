/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.repository;

import java.util.List;
import java.util.Map;

import com.cntest.common.query.Query;
import com.cntest.common.repository.Repository;
import com.cntest.fxpt.domain.Log;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 陈勇 2016年9月7日
 * @version 1.0
 **/
public interface LogDao extends Repository<Log, Long> {

	public void save(Log log);

	public Log get(Long pk);

	/**日志下载不分页
	 * @param params
	 * @return
	 */
	List<Log> downLogQueryByParams(Map<String, String> params);

	void surveyLogQuery(Query<Log> query, Map<String, String> params);

	public List<Log> getlogList(Map<String, String> param);

}
