/*
 * @(#)com.cntest.fxpt.anlaysis.bean.TargetResultContainer.java 1.0 2014年11月28日:下午3:01:33
 *
 * Copyright 2014 seasky land, Inc. All rights reserved.
 */
package com.cntest.fxpt.anlaysis.bean;

import com.cntest.fxpt.anlaysis.uitl.Container;

/**
 * <Pre>
 * </Pre>
 * 
 * @author 刘海林 2014年11月28日 下午3:01:33
 * @version 1.0
 */
public class TargetResultContainer extends Container<String, TargetResult> {
  private String tableName;
  private String choicenum;

  @Override
  public void put(String key, TargetResult value) {
    super.put(key, value);
  }

  @Override
  public TargetResult get(String key) {
    return super.get(key);
  }

  @Override
  public String toString() {
    StringBuffer result = new StringBuffer();
    for (TargetResult tr : toList()) {
      result.append(tr.getName() + ":" + tr.getValue() + "\t");
    }
    return result.toString();
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }


  public String getChoicenum() {
    return choicenum;
  }

  public void setChoicenum(String choicenum) {
    this.choicenum = choicenum;
  }

  public String toSQL() {

    StringBuffer sqlHeaader = new StringBuffer("INSERT INTO ").append(tableName).append("(");

    StringBuffer sqlValue = new StringBuffer().append(" VALUES(");
    for (TargetResult tr : toList()) {
      sqlHeaader.append(tr.getName()).append(",");
      if (tr.getValue() instanceof String) {
        sqlValue.append("'").append(tr.getValue().toString()).append("',");
      } else {
        sqlValue.append(tr.getValue().toString()).append(",");
      }

    }

    sqlHeaader.deleteCharAt(sqlHeaader.length() - 1).append(")");
    sqlValue.deleteCharAt(sqlValue.length() - 1).append(")");

    return sqlHeaader.toString() + sqlValue.toString();
  }
}
