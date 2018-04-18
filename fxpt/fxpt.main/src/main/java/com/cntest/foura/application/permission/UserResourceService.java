/**
 * <p>
 * <b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b>
 * </p>
 * 
 **/

package com.cntest.foura.application.permission;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.URLResourceService;
import com.cntest.foura.service.UserService;
import com.cntest.fxpt.util.TitleUtil;
import com.cntest.security.UserDetails;
import com.cntest.security.UserResource;
import com.cntest.security.remote.IUserResourceService;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年6月18日
 * @version 1.0
 **/
public class UserResourceService implements IUserResourceService {
  private static Logger logger = LoggerFactory.getLogger(UserResourceService.class);

  @Autowired
  private URLResourceService urlResourceService;

  @Autowired
  private UserService userService;

  @Override
  public boolean getUserResourceByUrl(String url) {
    boolean b = false;
    String title = TitleUtil.getTitleUrlCheck(url);
    if (title != null && title != "" && !url.equalsIgnoreCase("/")
        && !"home".equalsIgnoreCase(title)) {
      // 处理数据库中每有做权限设置的url,不存在的吗，默认有权限
      boolean isExist = urlResourceService.uRLResourceList(title);
      if (isExist) {
        User user = userService.findUserBy(userService.getCurrentLoginedUser().getUserName());
        try {
          Set<URLResource> result = userService.findUrlsByUserId(user.getPk());
          for (URLResource u : result) {
            if (u.getUrl().indexOf(title) == 0) {
              b = true;
              break;
            }
          }
        } catch (BusinessException e) {
          e.printStackTrace();
        }
      } else {
        b = true;
      }
    } else {
      b = true;
    }
    return b;
  }

  @Override
  public UserResource getUserResourceByUUId(UserDetails user, String uuId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<UserResource> getChildrenUserResourceByUrl(UserDetails user, String url) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<UserResource> getResourcesFor(UserDetails userDetails, String uuid, Type type,
      Level level) {
    User user = userService.findUserBy(userDetails.getUserName());
    logger.info("GET {}'s Resource with {}", user.getName(), uuid);
    return UserPermissonFactory.getInstanceOf(type).inject(urlResourceService).with(level)
        .query(user, uuid, null);
  }

  @Override
  public List<UserResource> getChildrenUserResourceByUrlAnType(UserDetails user, String url,
      String type) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<UserResource> getChildrenUserResourceByUUIdAndType(UserDetails user, String uuId,
      String type) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UserResource getUserResourceByUrl(UserDetails user, String url) {
    // TODO Auto-generated method stub
    return null;
  }

}
