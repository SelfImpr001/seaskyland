/**
 * <p>
 * <b>© 1997-2016 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b>
 * </p>
 * 
 **/
package com.cntest.fxpt.bi;

import com.cntest.fxpt.bi.domain.BiUser;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 张鑫 @2017年1月18日
 * @version 1.0
 **/
public interface BIOperater {
  public String createUser(String userName);

  public String removeUser(String userName);

  public String biServerUrl(String userName);

  public BiUser getBiUser(String userName);
}
