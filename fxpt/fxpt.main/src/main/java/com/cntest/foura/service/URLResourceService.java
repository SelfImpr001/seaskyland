/**
 * <p>
 * <b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b>
 * </p>
 * 
 **/

package com.cntest.foura.service;

import java.io.File;
import java.util.List;

import com.cntest.common.service.EntityService;
import com.cntest.foura.domain.URIType;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年6月11日
 * @version 1.0
 **/
public interface URLResourceService extends EntityService<URLResource, Long> {

  /**
   * 获取用户在某个应用的菜单 该方法会验证菜单是否关联角色
   * 
   * @param user
   * @param app
   * @return
   */
  List<URLResource> getAppMenuResources(User user, String uuid);

  /**
   * 查询所有菜单
   * 
   * @param user
   * @author liguiqing
   * @return
   */
  public List<URLResource> list();

  /**
   * 获取用户缺省的系统
   * 
   * @param user
   * @return
   */
  URLResource getUserDefaultApp(User user);

  /**
   * 获得有效子菜单
   * 
   * @param user
   * @param uuId
   * @return
   */
  List<URLResource> getMenuChildren(User user, String uuid);

  /**
   * 获得资源的操作方法
   * 
   * @param user
   * @param uuId
   * @return
   */
  List<URLResource> getResourceMethods(User user, String uuid);

  /**
   * 获得用户可访问的app
   * 
   * @param user
   * @param uuid
   * @return
   */
  List<URLResource> getAppsFor(User user);

  /**
   * 查询uuid下地模块
   * 
   * @param user
   * @param uuid
   * @return
   */
  List<URLResource> getModulesFor(User user, String uuid);

  /**
   * 查询uuid下地模块
   * 
   * @param user
   * @param uuid
   * @return
   */
  List<URLResource> getModulesFor(User user, URLResource urlResource);

  /**
   * 查询uuid下模块
   * 
   * @param uuid
   * @param type TODO
   * @return
   */
  List<URLResource> getMenuChildren(String uuid, URIType type, String name);


  /**
   * 根据uuid获取资源
   * 
   * @param uuid
   */
  URLResource getResourceByUuid(String uuid);

  /**
   * 获取用户数据某个uuid下的数据权限
   * 
   * @param user
   * @param uuid
   * @return
   */
  List<URLResource> getDataChildren(User user, String uuid);

  /**
   * 判断资源是否被使用
   * 
   * @param res
   * @return 使用true/没使用false
   */
  boolean relation(URLResource res);

  /**
   * 创建应用
   * 
   * @param resource
   */
  void createApp(URLResource resource);

  /**
   * 查找角色的所有资源
   * 
   * @param roleId
   * @return
   */
  List<URLResource> findResourceAllFor(Long roleId);

  /**
   * 根据用户获取所有资源
   * 
   * @param user
   * @return
   */
  List<URLResource> getResourcesAllFor(User user);

  /**
   * 过滤掉与角色没关联的资源
   * 
   * @param menus
   * @param string
   */
  void filterNonRole(List<URLResource> menus);

  /**
   * 获取所有资源
   * 
   * @return
   */
  List<URLResource> findResourceAllFor();

  /**
   * 根据uuid 类型组合条件查找子资源
   */
  List<URLResource> findSubURIFor(String uuid, URIType type);

  /**
   * 存储或更新资源图标
   */
  int saveOrUpdateIcon(File fileIcon, URLResource resource);

  /**
   * 将图片写到指定位置
   * 
   * @param file
   */
  void writeURIIcon(String file);

  /**
   * 删除资源图标
   */
  void removeUriIconFor(URLResource resource);

  /**
   * 删除与该菜单关联的用户和角色关联关系
   * 
   * @param resource
   */
  void removeRoleOrUser(long pk);

  boolean appExists(String uuid);

  /**
   * 根据pk值 获取资源信息
   * 
   * @param pk
   * @return
   */
  public URLResource findResourseByPk(String pk);

  /**
   * 资源拖拽更新
   * 
   * @param id
   * @param parentId
   * @param type
   */
  public void updateURLResourceByIds(String id, String parentId, String type, String broId);

  /**
   * 删除资源图标
   * 
   * @param pk
   */
  public void deleteIconByPk(String pk);

  List<URLResource> findPesMenus(User user);

  /**
   * 根据URL名称获取
   * 
   */
  public String findNametoURL(String urlName);

  /**
   * 根据url模糊查询以此url开头的菜单信息
   * 
   * @param url
   * @return true 表示存在
   */
  public boolean uRLResourceList(String url);
}

