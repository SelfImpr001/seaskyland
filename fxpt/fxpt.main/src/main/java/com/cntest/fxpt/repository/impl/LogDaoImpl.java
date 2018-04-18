/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.repository.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cntest.common.query.Query;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.domain.Log;
import com.cntest.fxpt.repository.LogDao;
/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 陈勇2016年9月7日
 * @version 1.0
 **/
@Repository
public class LogDaoImpl extends AbstractHibernateDao<Log, Long> implements LogDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	protected Class<Log> getEntityClass() {
		// TODO Auto-generated method stub
		return Log.class;
	}

	@Override
	public void save(Log log) {
		this.getSession().save(log);
	}

	public Log get(Long pk) {
		return (Log) this.getSession().createQuery("from Log where id = ?").setLong(0, pk).uniqueResult();
	}

	@Override
	public List<Log> getlogList(Map<String, String> param) {
		StringBuffer sql = new StringBuffer("FROM  Log ");

		return (List<Log>) this.getSession().createQuery(sql.toString()).list();
	}

	public List<Log> downLogQueryByParams(Map<String, String> params) {
		Criteria criteria = createCriteria();
		criteria=this.setCriteriaByParams(criteria,params);
		List<Log> logList = criteria.list();
		return logList;
	}

	@Override
	public void surveyLogQuery(Query<Log> query, Map<String,String> params) {
		Criteria criteria = createCriteria();
		ProjectionList ps = Projections.projectionList();
		ps.add(Projections.rowCount());
		criteria=this.setCriteriaByParams(criteria,params);
		/*String optionName = params.get("optionName");
		String optionValue = params.get("optionValue");
		String status = params.get("status");
		
		if(optionValue!=null && optionValue!="" && !optionValue.equals("")){
			switch (optionValue) {
			case "dateTime":
				String start = optionName.split(",")[0], end = optionName.split(",")[1]; 
				
				if(start!=null && !start.equals(" ") && (end==null || end.equals(" "))) {
					criteria.add(Restrictions.ge("handleTime", start) );
				}
				if((start==null || start.equals(" ")) && (end!=null && !end.equals(" "))) {
					criteria.add(Restrictions.le("handleTime", parseDate(end)));
				}
				if(start!=null && !start.equals(" ") && (end!=null && !end.equals(" "))) {
					criteria.add(Restrictions.ge("handleTime", start));
					criteria.add(Restrictions.le("handleTime", parseDate(end)));
				}
				break;
			case "handlePro":
				if(optionName!=null && optionName.length()>0) {
					criteria.add(Restrictions.like("handlePro", "%"+optionName+"%"));
				}
				break;
			case "handleOption":
				if(optionName!=null && optionName.length()>0) {
					criteria.add(Restrictions.like("handleOption", "%"+optionName+"%"));
				}
				break;
			case "suferHandleOption":
				if(optionName!=null && optionName.length()>0) {
					criteria.add(Restrictions.like("suferHandleOption", "%"+optionName+"%"));
				}
				break;
			default:
				break;
			}
		}
		if(status!=null && status.length()>0 && "success".equals(status)) {
			criteria.add(Restrictions.like("status", "%成功%"));
		}
		if(status!=null && status.length()>0 && "error".equals(status)) {
			criteria.add(Restrictions.like("status", "%失败%"));
		}
		criteria.addOrder(Order.desc("id"));*/
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Object o = criteria.setProjection(ps).uniqueResult();
		
		Long rowCount = (Long)o;
		int totalRows = rowCount.intValue();
		criteria.setProjection(null);
		
		criteria.setFirstResult(query.getStartRow());
		criteria.setMaxResults(query.getPagesize());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		query.setTotalRows(totalRows);
		query.setResults(criteria.list());
	}
	public static String parseDate(String date) {
		Calendar c =Calendar.getInstance();//GregorianCalendar();
		Date d =null;
		try {
			d = new SimpleDateFormat("yy-MM-dd").parse(date);
			c.setTime(d);
			int day = c.get(Calendar.DATE);
			c.set(Calendar.DATE, day+1);
			return new SimpleDateFormat("YYYY-MM-dd").format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return "";
	}

	private  Criteria setCriteriaByParams(Criteria criteria,Map<String,String> params){
		String optionName = params.get("optionName");
		String optionValue = params.get("optionValue");
		String status = params.get("status");

		if(optionValue!=null && optionValue!="" && !optionValue.equals("")){
			switch (optionValue) {
				case "dateTime":
					String start = optionName.split(",")[0], end = optionName.split(",")[1];

					if(start!=null && !start.equals(" ") && (end==null || end.equals(" "))) {
						criteria.add(Restrictions.ge("handleTime", start) );
					}
					if((start==null || start.equals(" ")) && (end!=null && !end.equals(" "))) {
						criteria.add(Restrictions.le("handleTime", parseDate(end)));
					}
					if(start!=null && !start.equals(" ") && (end!=null && !end.equals(" "))) {
						criteria.add(Restrictions.ge("handleTime", start));
						criteria.add(Restrictions.le("handleTime", parseDate(end)));
					}
					break;
				case "handlePro":
					if(optionName!=null && optionName.length()>0) {
						criteria.add(Restrictions.like("handlePro", "%"+optionName+"%"));
					}
					break;
				case "handleOption":
					if(optionName!=null && optionName.length()>0) {
						criteria.add(Restrictions.like("handleOption", "%"+optionName+"%"));
					}
					break;
				case "suferHandleOption":
					if(optionName!=null && optionName.length()>0) {
						criteria.add(Restrictions.like("suferHandleOption", "%"+optionName+"%"));
					}
					break;
				default:
					break;
			}
		}
		if(status!=null && status.length()>0 && "success".equals(status)) {
			criteria.add(Restrictions.like("status", "%成功%"));
		}
		if(status!=null && status.length()>0 && "error".equals(status)) {
			criteria.add(Restrictions.like("status", "%失败%"));
		}
		criteria.addOrder(Order.desc("id"));
		return  criteria;
	}
}
