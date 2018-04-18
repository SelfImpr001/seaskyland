
package com.cntest.fxpt.repository.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.domain.ShowMessage;
import com.cntest.fxpt.repository.IShowMessageDao;

@Repository("IShowMessageDao")
public class ShowMessageDaoImpl extends AbstractHibernateDao<ShowMessage, Long> implements
IShowMessageDao {
	private static final Logger log = LoggerFactory
			.getLogger(ShowMessageDaoImpl.class);

	@Override
	protected Class<ShowMessage> getEntityClass() {
		return ShowMessage.class;
	}

	@Override
	public void deleteMessageByExamid(Long examid,int showType,Long testPaperId) {
		String sql ="delete from kn_show_message where examid=? and showtype=?";
		if(testPaperId!=null && testPaperId>0){
			sql+=" and testPaperId="+testPaperId;
		}
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, examid);
		sqlQuery.setParameter(1, showType);
		sqlQuery.executeUpdate();
	}

	@Override
	public void addMessageByExamid(Long examid,Long testPaperId, int showType, List<DataField> dataField) {
		//删除原有配置信息
		deleteMessageByExamid(examid,showType,testPaperId);
		String sql="INSERT INTO kn_show_message(testpaperid,examid,showtype,fieldname,isshow) "
				+ "VALUES(?,?,?,?,?)";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setParameter(0, testPaperId);
		sqlQuery.setParameter(1, examid);
		sqlQuery.setParameter(2, showType);
		for(int i=0;i<dataField.size();i++){
			sqlQuery.setParameter(3, dataField.get(i).getFieldName());
			sqlQuery.setParameter(4, dataField.get(i).isValid()==true?1:0);
			sqlQuery.executeUpdate();
		}
		
	}

	@Override
	public List<ShowMessage> findShowMessageByExamid(Long examid,Long testPaperId, int showType) {
		String hql = "from ShowMessage where examid=? and showType=? ";
		if(testPaperId!=null){
			hql+=" and testPaperId="+testPaperId;
		}
		return findByHql(hql, examid,showType);
	}

	
}
