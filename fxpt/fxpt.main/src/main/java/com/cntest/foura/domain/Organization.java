/**
 * <p>
 * <b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b>
 * </p>
 * 
 **/

package com.cntest.foura.domain;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.cntest.fxpt.domain.SchoolSegment;
import com.cntest.fxpt.domain.SchoolType;

/**
 * <pre>
 *  组织，每个用户都对应一个或者多个组织
 * </pre>
 * 
 * @author 李贵庆2014年6月4日
 * @version 1.0
 **/

public class Organization {

  private String name;

  private String code;

  private int type; // 1--省;2--市;3--区（县）;4--学校;

  private Boolean available = Boolean.TRUE;

  private Organization parent;

  private SchoolType schooltype;

  private SchoolSegment schoolSegment;

  private int orgCount;
  private int userCount;
  private String children;
  private User user;

  public Organization() {}


  public Organization(String name, String code) {
    this.name = name;
    this.code = code;
    this.type = 1;
  }

  public Organization(String name, String code, int type) {
    this.name = name;
    this.code = code;
    this.type = type;
  }


  public void parentTo(Organization other) {
    if (this.equals(other))
      return;
    if (other.childOf(this))
      return;
    this.parent = other;
  }

  /**
   * 判断是为某一组织的下一级组织
   * 
   * @param other
   * @return
   */
  public boolean childOf(Organization other) {
    // @Todo 过程待实现
    return false;
  }

  public void available(Boolean available) {
    this.available = available;
  }


  public int hashCode() {
    return new HashCodeBuilder().append(this.name).append(this.code).toHashCode();
  }

  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof Organization))
      return false;
    Organization other = (Organization) o;
    return new EqualsBuilder().append(this.name, other.name).append(this.code, other.code)
        .isEquals();
  }

  public String toString() {
    return new ToStringBuilder(this).append("Organization name", this.name)
        .append("code", this.code).build();
  }

  // for orm
  private Long pk;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Boolean getAvailable() {
    return available;
  }

  public void setAvailable(Boolean available) {
    this.available = available;
  }

  public Organization getParent() {
    return parent;
  }

  public void setParent(Organization parent) {
    this.parent = parent;
  }

  public Long getPk() {
    return pk;
  }

  public void setPk(Long pk) {
    this.pk = pk;
  }


  public int getType() {
    return type;
  }


  public void setType(int type) {
    this.type = type;
  }


  public SchoolType getSchooltype() {
    return schooltype;
  }


  public void setSchooltype(SchoolType schooltype) {
    this.schooltype = schooltype;
  }


  public SchoolSegment getSchoolSegment() {
    return schoolSegment;
  }


  public void setSchoolSegment(SchoolSegment schoolSegment) {
    this.schoolSegment = schoolSegment;
  }


  public int getOrgCount() {
    return orgCount;
  }


  public void setOrgCount(int orgCount) {
    this.orgCount = orgCount;
  }



  public int getUserCount() {
    return userCount;
  }


  public void setUserCount(int userCount) {
    this.userCount = userCount;
  }


  public String getChildren() {
    return children;
  }


  public void setChildren(String children) {
    this.children = children;
  }


  public User getUser() {
    return user;
  }


  public void setUser(User user) {
    this.user = user;
  }



}

