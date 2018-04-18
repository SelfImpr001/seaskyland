/**
 * <p>
 * <b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b>
 * </p>
 * 
 **/

package com.cntest.foura.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cntest.common.page.Page;
import com.cntest.common.repository.Repository;
import com.cntest.common.service.AbstractEntityService;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Role;
import com.cntest.foura.domain.URIType;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserRole;
import com.cntest.foura.repository.RoleRepository;
import com.cntest.foura.repository.URLResourceRepository;
import com.cntest.foura.repository.UserRepository;
import com.cntest.foura.service.URLResourceService;
import com.cntest.fxpt.domain.ApplySet;
import com.cntest.fxpt.service.IApplySetService;
import com.cntest.fxpt.util.SystemConfig;
import com.cntest.security.UserDetails;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年6月11日
 * @version 1.0
 **/
@Transactional
@Service
public class URLResourceServiceImpl extends AbstractEntityService<URLResource, Long>
    implements URLResourceService {
  private static Logger logger = LoggerFactory.getLogger(URLResourceServiceImpl.class);
  @Autowired
  private URLResourceRepository resRepository;
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private IApplySetService applySetService;

  public URLResourceServiceImpl() {}

  @Autowired
  public URLResourceServiceImpl(URLResourceRepository resRepository) {
    this.resRepository = resRepository;
    this.setRepository(this.resRepository);
  }

  protected void setRepository(Repository<URLResource, Long> repository) {
    repository = resRepository;
  }

  @Override
  public URLResource load(Long pk) throws BusinessException {
    URLResource t = resRepository.load(pk);
    if (t == null || t.getPk() == null)
      t = resRepository.get(pk);
    return t;
  }

  @Override
  @Transactional
  public void update(URLResource t) throws BusinessException {
    logger.debug("Update {} ....", t);
    resRepository.update(t);
    logger.debug("Update {} success!", t);
    this.setRepository(this.resRepository);
  }

  @Override
  @Transactional
  public void remove(URLResource t) throws BusinessException {
    logger.debug("remove {} ....", t);
    if (t != null && t.getPk() != null) {
      t = load(t.getPk());
      resRepository.delete(t);
    }
    logger.debug("remove {} success!", t);
  }



  @Override
  @Transactional
  public List<URLResource> getAppMenuResources(User user, String uuid) {
    List<URLResource> menus = new ArrayList<URLResource>();
    if (user == null || !StringUtils.isNotEmpty(user.getName()) || !StringUtils.isNotEmpty(uuid)) {
      logger.debug("user is null or name is null or uuid is null!");
      return menus;
    }
    user = userRepository.selectUserBy(user.getName());
    menus = resRepository.findAppFor(user, uuid);

    logger.debug("Menu size : {}", menus.size());
    return menus;
  }

  @Override
  @Transactional
  public List<URLResource> getResourcesAllFor(User user) {
    List<URLResource> res = resRepository.findAppsFor(user);
    return urlResourceAll(res);
  }


  @Override
  public URLResource getUserDefaultApp(User user) {
    // TODO Auto-generated method stub
    logger.debug("Get User {} default ", user.getName());
    // 获取系统名称
    List<ApplySet> applySeList = applySetService.findApplyByStatus("1");
    String systemName = "";
    if (applySeList.size() > 0) {
      systemName = applySeList.get(0).getSystemName();
    } else {
      // 黄洪成2015-03-25 新增关于（系统版本）
      systemName = SystemConfig.newInstance().getValue("cz.systemName");
    }
    String appKey = com.cntest.common.config.SystemRuntimeConfiguration.getAppKey();
    URLResource thisApp = new URLResource.Builder().name(systemName).create();
    thisApp.setUuid(appKey);
    return thisApp;
    // if(user.getName().equals("admin"))
    // return new URLResource.Builder().name("海云天测评统一权限管理平台").create();
    // else
    // return new URLResource.Builder().name("海云天测评数据分析平台").create();
  }

  @Override
  public List<URLResource> getMenuChildren(User user, String uuid) {
    return resRepository.findMenusFor(user, uuid);
  }

  @Override
  public List<URLResource> getMenuChildren(String uuid, URIType type, String name) {
    if (type == null) {
      return resRepository.findSubResouresFor(uuid, name);
    } else {
      return resRepository.findSubResouresForUuid(uuid, type, name);
    }
  }

  @Override
  public List<URLResource> getResourceMethods(User user, String uuid) {
    return resRepository.findMenusFor(user, uuid);
  }

  @Override
  public List<URLResource> getModulesFor(User user, String uuid) {
    return resRepository.findModulesFor(user, uuid);
  }

  @Override
  public List<URLResource> getAppsFor(User user) {
    return resRepository.findAppsFor(user);
  }

  @Override
  @Transactional
  public void create(URLResource resource) throws BusinessException {
    logger.debug("Create {} ....", resource);
    resRepository.save(resource);
    logger.debug("Create {} success!", resource);
  }

  @Override
  public List<URLResource> list(Page<URLResource> page) throws BusinessException {
    return resRepository.list(page);
  }

  @Override
  public URLResource getResourceByUuid(String uuid) {

    return resRepository.getResourceByUuid(uuid);
  }

  @Override
  public List<URLResource> getDataChildren(User user, String uuid) {
    return resRepository.findDatasFor(user, uuid);
  }

  @Override
  public boolean relation(URLResource res) {
    return resRepository.associateRoleFor(res) || resRepository.associateUserFor(res);
  }

  @Override
  @Transactional
  public void createApp(URLResource resource) {
    logger.debug("Create {} ....", resource);
    Subject subject = SecurityUtils.getSubject();
    UserDetails userDetail = null;
    String username = "";
    if (subject.getPrincipal() instanceof UserDetails) {
      userDetail = (UserDetails) subject.getPrincipal();
      username = userDetail.getUserName();
    } else if (subject.getPrincipal() instanceof String) {
      username = (String) subject.getPrincipal();
    }
    User user = userRepository.selectUserBy(username);
    for (UserRole role : user.getRoles()) {
      Role r = role.getRole();
      r.addResource(resource);
      roleRepository.update(r);
    }

    logger.debug("Create {} ....", resource);
  }

  @Override
  public List<URLResource> findResourceAllFor(Long roleId) {
    return resRepository.findResourceFor(roleId);
  }

  @Override
  public List<URLResource> findResourceAllFor() {
    return resRepository.findResourceFor();
  }

  /**
   * 获取所有资源
   * 
   * @param res
   * @return
   */
  private List<URLResource> urlResourceAll(Collection<URLResource> res) {
    List<URLResource> resources = new ArrayList<URLResource>();
    for (Iterator<URLResource> i = res.iterator(); i.hasNext();) {
      URLResource urlResource = i.next();

      if (urlResource.getChildren().size() != 0)
        resources.addAll(urlResourceAll(urlResource.getChildren()));

      resources.add(urlResource);
    }
    return resources;
  }

  @Override
  public void filterNonRole(List<URLResource> menus) {

    Subject subject = SecurityUtils.getSubject();
    UserDetails userDetail = null;
    String username = "";
    if (subject.getPrincipal() instanceof UserDetails) {
      userDetail = (UserDetails) subject.getPrincipal();
      username = userDetail.getUserName();
    } else if (subject.getPrincipal() instanceof String) {
      username = (String) subject.getPrincipal();
    }
    User user = userRepository.selectUserBy(username);
    List<Long> rolePks = new ArrayList<Long>();
    for (UserRole ur : user.getRoles()) {
      rolePks.add(ur.getRole().getPk());
    }
    for (Iterator<URLResource> i = menus.iterator(); i.hasNext();) {
      URLResource urlResource = i.next();
      if (!filterNonRole(urlResource, rolePks))
        i.remove();
    }
  }

  private Boolean filterNonRole(URLResource urlResource, List<Long> rolePks) {
    if (urlResource.getChildren().size() > 0) {
      for (Iterator<URLResource> i = urlResource.getChildren().iterator(); i.hasNext();) {
        URLResource res = i.next();
        if (!filterNonRole(res, rolePks))
          i.remove();
      }
    }
    return resRepository.associateRoleFor(urlResource, rolePks);
  }

  @Override
  public List<URLResource> findSubURIFor(String uuid, URIType type) {
    return resRepository.findSubResouresForType(uuid, type);
  }

  @SuppressWarnings("resource")
  @Override
  public int saveOrUpdateIcon(File fileIcon, URLResource resource) {
    try {
      String path = fileIcon.getPath();
      logger.info("filepath {}", path);

      BufferedImage image = ImageIO.read(fileIcon);
      int width = image.getWidth();
      int height = image.getHeight();
      int postion = path.lastIndexOf(".");
      String type = path.substring(postion + 1);
      String name = path.substring(path.lastIndexOf(File.separatorChar) + 1);
      byte[] binary = new byte[(int) fileIcon.length()];
      new FileInputStream(fileIcon).read(binary);
      if (resRepository.findFileFor(resource.getPk()).size() == 0) {
        return resRepository.saveFileFor(resource.getPk(), width, height, type, name, binary);
      } else {
        return resRepository.updateFile(resource.getPk(), width, height, type, name, binary);
      }
    } catch (Exception e) {
      logger.debug(e.getMessage());
      return 0;
    }
  }

  @Override
  public void writeURIIcon(String file) {
    @SuppressWarnings("rawtypes")
    List imageList = resRepository.findFileFor();
    FileOutputStream out = null;
    if (file.lastIndexOf(File.separatorChar) + 1 != file.length())
      file = file + File.separatorChar;
    try {
      for (Object row : imageList) {
        logger.info("file info {}", row);
        if (row instanceof Object[]) {
          Object[] cells = (Object[]) row;
          out = new FileOutputStream(file + cells[2]);
          if (cells[7] instanceof byte[])
            out.write((byte[]) cells[7]);
          out.flush();
          out.close();
        }
      }
    } catch (FileNotFoundException e) {
      logger.debug(e.getMessage());
    } catch (IOException e) {
      logger.debug(e.getMessage());
    }
  }

  @Override
  public void removeUriIconFor(URLResource resource) {
    logger.debug("delete iconFile ", resource);
    resRepository.deleteFileBy(resource.getPk());
    logger.debug("delete success ", resource);

  }

  @Override
  public boolean appExists(String uuid) {
    return resRepository.getResourceByUuid(uuid) != null;
  }

  @Override
  public URLResource findResourseByPk(String pk) {
    return resRepository.findResourseByPk(pk);
  }

  @Override
  public void removeRoleOrUser(long pk) {
    resRepository.removeRoleOrUser(pk);
  }

  @Override
  public List<URLResource> getModulesFor(User user, URLResource urlResource) {
    return resRepository.findModulesFor(user, urlResource);
  }

  @Override
  public void updateURLResourceByIds(String id, String parentId, String type, String broId) {
    URLResource urlResource = resRepository.findResourseByPk(id);
    URLResource parentResource = resRepository.findResourseByPk(parentId);
    URLResource broResource = resRepository.findResourseByPk(broId);
    if (parentResource == null) {
      urlResource.setType(URIType.app);
    } else {
      // 此处类型判断很重要
      if (urlResource.getParent() == null) {
        if (type.endsWith("modlue")) {
          urlResource.setType(URIType.module);
        } else if (type.endsWith("menu")) {
          urlResource.setType(URIType.menu);
        } else {
          urlResource.setType(URIType.app);
        }
      }
      if (broResource != null) {
        updateResByPk(parentResource, broResource.getReorder());
        urlResource.setReorder(broResource.getReorder() + 1);
      } else {
        urlResource.setReorder(1);
        updateResByPk(parentResource, 0);
      }
    }
    urlResource.setParent(parentResource);
    resRepository.update(urlResource);
  }

  private void updateResByPk(URLResource res, int i) {
    List<URLResource> childs = resRepository.findSubResouresForType(res.getUuid(), res.getType());
    for (URLResource organization : childs) {
      if (organization.getReorder() > i) {
        organization.setReorder(organization.getReorder() + 1);
        resRepository.update(organization);
      }
    }
  }

  @Override
  public void deleteIconByPk(String pk) {
    URLResource res = resRepository.findResourseByPk(pk);
    if (res != null) {
      res.setIcon(null);
      resRepository.update(res);

    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<URLResource> list() {
    return resRepository.list();
  }

  @Override
  public List<URLResource> findPesMenus(User user) {

    return resRepository.findPesMenus(user);
  }

  @Override
  @Transactional(readOnly = true)
  public String findNametoURL(String urlName) {
    return resRepository.findNametoURL(urlName);
  }

  @Override
  public boolean uRLResourceList(String url) {
    List list = resRepository.uRLResourceList(url);
    return list.size() > 0 ? true : false;
  }


}


