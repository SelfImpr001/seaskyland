package com.cntest.fxpt.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cntest.common.controller.BaseController;
import com.cntest.foura.service.URLResourceService;
import com.cntest.util.SpringContext;

public class TitleUtil extends BaseController {

  private static URLResourceService resourceService;

  static {
    resourceService = SpringContext.getBean(URLResourceService.class);
  }

  public static String getTitle(String url) {
    StringBuffer urls = new StringBuffer(url);
    // 判断是否有分页
    url = urls.deleteCharAt(0).toString();

    String[] splitPage = url.split("[/]");
    StringBuffer sbuffer = new StringBuffer();

    // String tit = resourceService.findNametoURL(url.toString());

    int i = 0;
    for (String o : splitPage) {
      if (isNumber(o)) {
        if (i == 0) {
          o = "1";
          i++;
        }
        sbuffer.append("/" + o);
      } else {
        sbuffer.append("/" + o);
      }
    }
    String title = resourceService.findNametoURL(sbuffer.deleteCharAt(0).toString());
    if (null != title && title.length() > 0) {
      return title;
    }
    return null;
  }

  /**
   * 防止越权访问（处理访问的url）
   * 
   * @param url
   * @return
   */
  public static String getTitleUrlCheck(String url) {
    StringBuffer urls = new StringBuffer(url);
    // 判断是否有分页
    url = urls.deleteCharAt(0).toString();

    String[] splitPage = url.split("[/]");
    StringBuffer sbuffer = new StringBuffer();

    // String tit = resourceService.findNametoURL(url.toString());

    int i = 0;
    for (String o : splitPage) {
      // 每页多少条也必须要控制（此处只控制的当前第几页）
      if (isNumber(o)) {
        if (i == 0) {
          o = "1";
          i++;
        } else if (i == 1) {
          o = "15";
          i++;
        }
        sbuffer.append("/" + o);
      } else {
        sbuffer.append("/" + o);
      }
    }
    String title = sbuffer.deleteCharAt(0).toString();
    if (null != title && title.length() > 0) {
      return title;
    }
    return null;
  }

  public static boolean isNumber(String str) {
    Pattern pattetn = Pattern.compile("[0-9]*");
    Matcher isNum = pattetn.matcher(str);
    if (!isNum.matches()) {
      return false;
    }
    return true;
  }

  public static void main(String[] args) {
    String[] splitPage = "res/menuList/1/15".split("[/]");
    for (String o : splitPage) {
      if (isNumber(o)) {

      } else {

      }
    }
  }
}
