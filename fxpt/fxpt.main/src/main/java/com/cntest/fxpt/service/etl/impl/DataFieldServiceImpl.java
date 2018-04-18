/*
 * @(#)com.cntest.fxpt.service.etl.impl.DataFieldServiceImpl.java 1.0 2014年5月15日:下午3:43:49
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.service.etl.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cntest.fxpt.bean.DataField;
import com.cntest.fxpt.domain.Item;
import com.cntest.fxpt.repository.IITemDao;
import com.cntest.fxpt.repository.etl.IDataFieldDao;
import com.cntest.fxpt.service.etl.IDataFieldService;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年5月15日 下午3:43:49
 * @version 1.0
 */
@Service("etl.IDataFieldService")
public class DataFieldServiceImpl implements IDataFieldService {
  private static final Logger log = LoggerFactory.getLogger(DataFieldServiceImpl.class);
  @Autowired(required = false)
  @Qualifier("etl.IDataFieldDao")
  private IDataFieldDao dataFieldDao;

  @Autowired(required = false)
  @Qualifier("IITemDao")
  private IITemDao itemDao;

  /**
   * 
   */
  public DataFieldServiceImpl() {
    // TODO Auto-generated constructor stub
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.service.etl.IDataFieldService#add(com.cntest.fxpt.
   * systemsetting.etl.domain.DataField)
   */
  @Override
  public void add(DataField dataField) {
    dataFieldDao.add(dataField);

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.service.etl.IDataFieldService#delete(com.cntest.fxpt.
   * systemsetting.etl.domain.DataField)
   */
  @Override
  public void delete(DataField dataField) {
    dataFieldDao.delete(dataField);

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.service.etl.IDataFieldService#update(com.cntest.fxpt.
   * systemsetting.etl.domain.DataField)
   */
  @Override
  public void update(DataField dataField) {
    dataFieldDao.update(dataField);

  }

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.service.etl.IDataFieldService#list()
   */
  @Override
  public List<DataField> list() {
    return dataFieldDao.list();
  }

  @Override
  public List<DataField> list(Long dataCategoryId) {
    return dataFieldDao.findByDataCategoryId(dataCategoryId);
  }

  @Override
  public List<DataField> list(int schemeType, Long testPaperId) {
    log.debug("进入获取导入字段的服务");

    List<DataField> dataFields = new ArrayList<DataField>();
    if (schemeType == 2) {
      dataFields = dataFieldDao.list(schemeType, true);
    } else {
      dataFields = dataFieldDao.list(schemeType, true);
    }
    if (schemeType == 3) {
      log.debug("导入成绩，构建导入小题成绩的导入字段");
      List<Item> items = itemDao.list(testPaperId);
      for (Item item : items) {
        String[] cjFields = item.getCjField().split("\\|");

        DataField df = new DataField();
        df.setFieldName("score" + item.getSortNum());
        df.setAsName("第" + item.getItemNo() + "题分数");
        df.setValid(true);
        df.setNeed(true);
        if (item.getOptionType() == 0) {
          df.setSelItem(false);
          df.setSelOption(false);
          df.setDefaultName(cjFields[0]);
        } else {
          df.setSelItem(true);
          df.setSelOption(false);
          // if (cjFields.length == 2) {
          df.setDefaultName(cjFields[0]);
          // }
        }

        dataFields.add(df);

        if (item.getOptionType() != 0) {
          df = new DataField();
          df.setFieldName("sel" + item.getSortNum());
          df.setAsName("第" + item.getItemNo() + "题选项");
          df.setValid(true);
          df.setNeed(true);
          df.setSelItem(true);
          df.setSelOption(true);
          // if (cjFields.length == 2) {
          df.setDefaultName(cjFields[0]);
          // }
          dataFields.add(df);
        }

      }

      log.debug("执行完毕导入成绩，构建导入小题成绩的导入字段");
    }

    log.debug("执行完毕获取导入字段的服务");
    return dataFields;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.service.etl.IDataFieldService#findById(int)
   */
  @Override
  public DataField findById(Long dataFieldId) {
    return dataFieldDao.findDataCategoryById(dataFieldId);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.cntest.fxpt.service.etl.IDataFieldService#hasFiled(com.cntest.fxpt .bean.DataField)
   */
  @Override
  public boolean hasFiled(DataField dataField) {
    return dataFieldDao.hasFiled(dataField);
  }

  @Override
  public HashMap getDateByType(String type) {
    return dataFieldDao.getDateByType(type);
  }

  @Override
  public String getTestPaperDescription(int schemeType, String fieldName) {
    return dataFieldDao.getTestPaperDescription(schemeType, fieldName);
  }

  @Override
  public String initDataHead(List<String> listHead, int schemeType) {
    return dataFieldDao.initDataHead(listHead, schemeType);
  }

  @Override
  public List<DataField> list(int schemeType, boolean isValid) {
    return dataFieldDao.list(schemeType, isValid);
  }

}
