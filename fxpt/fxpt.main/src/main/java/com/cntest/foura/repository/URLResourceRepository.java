/**
 * <p>
 * <b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b>
 * </p>
 * 
 **/

package com.cntest.foura.repository;

import java.util.List;

import com.cntest.common.repository.Repository;
import com.cntest.foura.domain.URIType;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;


/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 肖 肖 2014年6月17日
 * @version 1.0
 **/
public interface URLResourceRepository extends Repository<URLResource, Long> {
  public URLResource getURLResourceByUUID(String uuid);

  /**
   * 查询用户的app资源
   * 
   * @param user
   * @return
   */
  List<URLResource> findAppsFor(User user);

  /**
   * 获得某级子菜单资源
   * 
   * @param user
   * @param uuid
   * @return
   */
  List<URLResource> findMenusFor(User user, String uuid);

  /**
   * 查询模块资源
   * 
   * @param user
   * @param uuid
   * @return
   */
  public List<URLResource> findModulesFor(User user, String uuid);

  /**
   * 查询模块资源
   * 
   * @param user
   * @param urlRescource
   * @return
   */
  public List<URLResource> findModulesFor(User user, URLResource urlRescource);

  /**
   * 查询应用资源
   * 
   * @param user
   * @param uuid
   * @return
   */
  public List<URLResource> findAppFor(User user, String uuid);

  /**
   * 查询资源的直接下级资源
   * 
   * @param uuid
   * @return
   */
  public List<URLResource> findSubResouresFor(String uuid, String name);

  /**
   * 依据uuid找下级指定类型的资源
   * 
   * @param uuid
   * @return
   */
  public List<URLResource> findSubResouresForUuid(String uuid, URIType type, String name);

  /**
   * 依据uuid和类型 找 下级资源
   * 
   * @param uuid
   * @return
   */
  public List<URLResource> findSubResouresForType(String uuid, URIType type);

  /**
   * 只依据uuid获取资源
   * 
   * @param uuid
   * @return
   */
  public URLResource getResourceByUuid(String uuid);

  /**
   * 查询数据资源
   * 
   * @param user
   * @param uuid
   * @return
   */
  List<URLResource> findDatasFor(User user, String uuid);

  /**
   * 根据资源查找与角色的关联
   * 
   * @param res
   * @return
   */
  public Boolean associateRoleFor(URLResource res);

  /**
   * 查找角色所有资源
   * 
   * @param roleId
   * @return
   */
  List<URLResource> findResourceFor(Long roleId);

  /**
   * 根据资源,角色 查找他们的关联
   * 
   * @param urlResource
   * @param rolePks
   * @return
   */
  public Boolean associateRoleFor(URLResource urlResource, List<Long> rolePks);

  /**
   * 查找所有资源
   * 
   * @param roleId
   * @return
   */
  public List<URLResource> findResourceFor();

  /**
   * 保存文件
   * 
   * @param URIPk TODO
   * @param width TODO
   * @param height TODO
   * @param type TODO
   * @param name TODO
   * @param binary TODO
   * @param roleId
   * @return
   */
  public int saveFileFor(Long uriPk, Integer width, Integer height, String type, String name,
      byte[] binary);

  /**
   * 获取所有文件
   * 
   * @param roleId
   * @return
   */
  public List findFileFor();

  /**
   * 修改文件
   * 
   * @param URIPk
   * @param width
   * @param height
   * @param type
   * @param name
   * @param binary
   * @return
   * @throws Exception
   */
  int updateFile(Long uriPk, Integer width, Integer height, String type, String name,
      byte[] binary);

  /**
   * 删除文件
   * 
   * @param URIPk
   * 
   * @return
   * @throws Exception
   */
  int deleteFileBy(Long URIPk);

  /**
   * 根据资源pk查找文件
   * 
   * @param pk
   * @return
   */
  List findFileFor(Long uriPk);

  /**
   * 根据资源查找与用户的关联
   * 
   * @param res
   * @return
   */
  boolean associateUserFor(URLResource res);

  public URLResource findResourseByPk(String pk);

  /**
   * 删除与该菜单关联的用户和角色关联关系
   * 
   * @param pk
   */
  public void removeRoleOrUser(long pk);

  /**
   * 删除角色权限
   * 
   * @param pk
   */
  public void deleteRoleUrlByPk(Long pk);

  /**
   * 删除角色用户关联
   * 
   * @param pk
   */
  public void deleteUserRoleByPk(Long pk);

  /**
   * 查找用户资源
   * 
   * @param user
   * @return
   */
  public List<URLResource> findPesMenus(User user);

  /**
   * 根据URL名称获取
   * 
   */
  public String findNametoURL(String urlName);

  /**
   * 根据url模糊查询以此url开头的菜单信息
   * 
   * @param url
   * @return
   */
  public List<URLResource> uRLResourceList(String url);

}

