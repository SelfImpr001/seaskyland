package com.cntest.fxpt.bean;

import java.util.List;

public class Uplinescore<T> {
  // 此实体类与数据库表无关联
  private String tag;
  private List<T> paramslsitW;
  private List<T> paramslsitL;
  private List<T> paramslsitN;

  public List<T> getParamslsitN() {
    return paramslsitN;
  }

  public void setParamslsitN(List<T> paramslsitN) {
    this.paramslsitN = paramslsitN;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public List<T> getParamslsitW() {
    return paramslsitW;
  }

  public void setParamslsitW(List<T> paramslsitW) {
    this.paramslsitW = paramslsitW;
  }

  public List<T> getParamslsitL() {
    return paramslsitL;
  }

  public void setParamslsitL(List<T> paramslsitL) {
    this.paramslsitL = paramslsitL;
  }

}
