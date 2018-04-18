/*
 * @(#)com.cntest.fxpt.repository.etl.impl.DataFieldDaoImpl.java	1.0 2014年5月13日:下午4:47:36
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.repository.etl.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.repository.etl.IDataFieldDao;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月13日 下午4:47:36
 * @version 1.0
 */
@Repository("etl.IDataFieldDao")
public class DataFieldDaoImpl extends AbstractHibernateDao<DataField, Long>
		implements IDataFieldDao {

	/**
	 * 
	 */
	public DataFieldDaoImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void add(DataField dataField) {
		this.save(dataField);
	}

	@Override
	public void delete(DataField dataField) {
		super.delete(dataField);
	}

	@Override
	public void update(DataField dataField) {
		super.update(dataField);
	}

	@Override
	public DataField findDataCategoryById(Long dataFieldId) {
		return this.get(dataFieldId);
	}

	@Override
	public List<DataField> findByDataCategoryId(Long dataCategoryId) {
		String htl = "from DataField as df where df.dataCategory.id=? order by df.sortNum asc";
		List<DataField> result = findByHql(htl, dataCategoryId);
		return result;
	}
	
	

	@Override
	public List<DataField> findByDataCategoryId(Long dataCategoryId,
			boolean isValid) {
		String htl = "from DataField as df where df.dataCategory.id=? and df.valid=?";
		List<DataField> result = findByHql(htl, dataCategoryId,isValid);
		return result;
	}
	
	

	@Override
	public List<DataField> list(int schemeType, boolean isValid) {
		String htl = "from DataField as df where df.dataCategory.schemeType=? and df.valid=? order by df.sortNum";
		List<DataField> result = findByHql(htl, schemeType,isValid);
		return result;
	}
	
	@Override
	public List<DataField> list(int schemeType, boolean isValid,boolean isNeed) {
		String htl = "from DataField as df where df.dataCategory.schemeType=? and df.valid=? and df.need=? order by df.sortNum";
		List<DataField> result = findByHql(htl, schemeType,isValid,isNeed);
		return result;
	}


	@Override
	protected Class<DataField> getEntityClass() {
		return DataField.class;
	}

	@Override
	public List<DataField> list() {
		String hql = "from DataField";
		return findByHql(hql);
	}

	@Override
	public boolean hasFiled(DataField dataField) {
		String sql = "SELECT COUNT(1) FROM kn_etl_datafield WHERE fieldName='"+dataField.getFieldName()+"' and dataCategoryId="+dataField.getDataCategory().getId();
		if(dataField!=null && dataField.getId()!=null && dataField.getId()>0){
			sql = "SELECT COUNT(1) FROM kn_etl_datafield WHERE fieldName='"+dataField.getFieldName()+"' AND id<>"+dataField.getId()+" and dataCategoryId="+dataField.getDataCategory().getId();
		}
		SQLQuery sqlQuery = createSQLQuery(sql);
		List list = sqlQuery.list();
		if(list!=null && list.size()>0 && Integer.parseInt(list.get(0).toString())>0){
			return false;
		}
		return true;
	}

	@Override
	public HashMap getDateByType(String type) {
		String hql = "from DataField where dataCategoryId IN("+type+")";
		HashMap hashMap = new HashMap<>();
		List<DataField> dataFieldlist =findByHql(hql);
		for(int i=0;i<dataFieldlist.size();i++){
			DataField dataField =dataFieldlist.get(i);
			hashMap.put(dataField.getFieldName(), dataField.isValid()==true?1:0);
		}
		return hashMap;
	}

	@Override
	public String getTestPaperDescription(int schemeType, String fieldName) {
		String description="";
		String htl = "from DataField as df where df.dataCategory.schemeType=? and df.fieldName=?";
		List<DataField> result = findByHql(htl, schemeType,fieldName);
		if(result.size()>0){
			description=result.get(0).getDescription();
		}
		return description;
	}

	@Override
	public String initDataHead(List<String> listHead, int schemeType) {
		String message="";
		if(listHead!=null){
			String strs =listHead.toString();
			//schemeType 2：表示明细表  3表示成绩
			String val="-1";
			if(schemeType==2){
				val="6";
			}else if(schemeType==3){
				val="7,10";
			}
			List<DataField> dataFields= dataFieldValadate(val,true,true);
			//字段处理
			for(DataField data:dataFields){
				String defaultName = data.getDefaultName();
				String[] defaults=defaultName.split("\\|");
				boolean flg=false;
				for(int i =0;i<defaults.length;i++){
					if(strs.indexOf(defaults[i])>-1){
						flg=true;
						break;
					}
				}
				if(!flg){
					message=message+data.getAsName()+"-找不到匹配字段;";
				}
			}
		}else{
			message="空的excel，请检查excel在上传！";
		}
		return message;
	}
	//查询有效且必须的字段
	private List<DataField> dataFieldValadate(String dataCategoryId,
			boolean isValid,boolean isNeed) {
		String htl = "from DataField as df where df.dataCategory.id in("+dataCategoryId+") and df.valid=? and df.need=?";
		List<DataField> result = findByHql(htl,isValid,isNeed);
		return result;
	}
}
