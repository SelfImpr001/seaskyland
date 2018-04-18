/**
 * <p>
 * <b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b>
 * </p>
 * 
 **/

package com.cntest.foura.repository.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.foura.domain.RoleResource;
import com.cntest.foura.domain.URIType;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.foura.domain.UserResource;
import com.cntest.foura.repository.URLResourceRepository;
import com.cntest.security.UserDetails;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 肖 肖 2014年6月17日
 * @version 1.0
 **/
@Repository
@SuppressWarnings("rawtypes")
public class URLResourceRepositoryHibernateImpl extends AbstractHibernateDao<URLResource, Long>
    implements URLResourceRepository {

  private static Logger logger = LoggerFactory.getLogger(URLResourceRepositoryHibernateImpl.class);

  @Override
  protected Class<URLResource> getEntityClass() {
    return URLResource.class;
  }

  @Override
  public URLResource getURLResourceByUUID(String uuid) {
    Criteria criteria = this.getSession().createCriteria(URLResource.class);
    criteria.add(Restrictions.eq("uuid", uuid));
    return criteria.list().size() > 0 ? (URLResource) criteria.list().get(0) : null;
  }

  @Override
  public List<URLResource> findAppsFor(User user) {
    logger.info("findAppsFor {}", user);

    // String hql = "select DISTINCT app from URLResource app join app.roles as rs join rs.role "
    // + "as r join r.users as us join us.user as u where app.available=? and app.type=? "
    // + " and app.parent.pk is null"; //app是没有父资源
    Criteria criteria = this.getSession().createCriteria(URLResource.class);
    // .createAlias("resource", "re")
    // .createAlias("user", "user");
    criteria
        // .add(Restrictions.eq("user.name", user.getName()))
        .add(Restrictions.isNull("parent")).add(Restrictions.eq("type", URIType.app))
        .add(Restrictions.eq("available", true)).addOrder(Order.asc("reorder"));
    // .setResultTransformer(Criteria.ROOT_ENTITY);
    // List li = criteria.list();
    // List<URLResource> resList = new ArrayList<URLResource>();
    // for (Iterator i =li.iterator(); i.hasNext(); ) {
    // UserResource ur = (UserResource)i.next();
    // URLResource res = ur.getResource();
    // resList.add(res);
    // }
    return criteria.list();
  }

  @Override
  public List<URLResource> list() {
    return findByHql("from URLResource");
  }

  @Override
  public List<URLResource> findAppFor(User user, String uuid) {
    logger.info("findAppFor {}", user);
    // String hql = "select DISTINCT m from URLResource m "
    // + " join m.roles as rs join rs.role as r join r.users as us join us.user as u where
    // m.available=? "
    // + " and u.name=? and m.type=? and m.uuid=? order by m.reorder";
    Criteria criteria = this.getSession().createCriteria(UserResource.class)
        .createAlias("resource", "re").createAlias("user", "user");
    criteria.add(Restrictions.eq("user.name", user.getName()))
        .add(Restrictions.eq("re.type", URIType.app)).add(Restrictions.eq("re.uuid", uuid))
        .add(Restrictions.eq("re.available", true)).addOrder(Order.asc("re.reorder"))
        .setResultTransformer(Criteria.ROOT_ENTITY);
    List li = criteria.list();
    List<URLResource> resList = new ArrayList<URLResource>();
    for (Iterator i = li.iterator(); i.hasNext();) {
      UserResource ur = (UserResource) i.next();
      URLResource res = ur.getResource();
      resList.add(res);
    }
    return resList;
  }

  @Override
  public List<URLResource> findMenusFor(User user, String uuid) {
    return findSubResouresFor(user, uuid, URIType.menu);
  }

  @Override
  public List<URLResource> findModulesFor(User user, String uuid) {
    logger.info("findModulesFor  {}", user);
    return findSubResouresFor(user, uuid, URIType.module);
  }

  @Override
  public List<URLResource> findDatasFor(User user, String uuid) {
    logger.info("findDatasFor  {}", user);
    return findSubResouresFor(user, uuid, URIType.data);
  }

  @Override
  public List<URLResource> findSubResouresFor(String uuid, String name) {
    logger.info("findMenusFor  {}", uuid);
    return findSubResouresForUuid(uuid, URIType.menu, name);
  }

  @Override
  public List<URLResource> findSubResouresForUuid(String uuid, URIType type, String name) {
    logger.info("findMenusFor  {},{}", uuid, type);
    String hql = "select DISTINCT m from  URLResource m join m.parent as p "
        + " where  m.type=? and p.uuid=? ";
    if (name != null) {
      if (name.equals("1")) {
        hql += " and m.available= " + Boolean.TRUE;
      }
    }
    hql += " order by m.reorder";
    return findByHql(hql, type.toString(), uuid);
  }

  @Override
  public List<URLResource> findSubResouresForType(String uuid, URIType type) {
    logger.info("findMenusFor  {},{}", uuid, type);
    String hql = "select DISTINCT m from  URLResource m join m.parent as p "
        + " where  p.type=? and p.uuid=? order by m.reorder";
    return findByHql(hql, type.toString(), uuid);
  }

  private List<URLResource> findSubResouresFor(User user, String uuid, URIType type) {
    return findSubResouresFor(0, 0, user, uuid, type);
  }

  private List<URLResource> findSubResouresFor(int start, int size, User user, String uuid,
      URIType type) {
    logger.info("findMenusFor  {}", user);
    // String hql = "select DISTINCT m from URLResource m join m.parent as p "
    // + " join m.roles as rs join rs.role as r join r.users as us join us.user as u where
    // m.available=? "
    // + " and u.name=? and m.type=? and p.uuid=? order by m.reorder";
    Criteria criteria =
        this.getSession().createCriteria(UserResource.class).createAlias("resource", "re")
            .createAlias("resource.parent", "rp").createAlias("user", "user");
    criteria.add(Restrictions.eq("user.name", user.getName())).add(Restrictions.eq("rp.uuid", uuid))
        .add(Restrictions.eq("re.type", type)).add(Restrictions.eq("re.available", true))
        .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    criteria.addOrder(Order.asc("order"));
    // if(size == 0) return findByHql(hql,Boolean.TRUE, user.getName(),type.toString(),uuid);
    // return findByHqlLimit(hql,start,size,Boolean.TRUE, user.getName(),type.toString(),uuid);
    if (size != 0) {
      criteria.setFirstResult(start);
      criteria.setMaxResults(size);
    }
    List li = criteria.list();
    List<URLResource> resList = new ArrayList<URLResource>();
    for (Iterator i = li.iterator(); i.hasNext();) {
      UserResource ur = (UserResource) i.next();
      URLResource res = ur.getResource();
      resList.add(res);
    }
    return resList;
  }

  @Override
  public URLResource getResourceByUuid(String uuid) {
    Criteria criteria = this.getSession().createCriteria(this.getEntityClass().getName());
    return (URLResource) criteria.add(Restrictions.eq("uuid", uuid)).uniqueResult();
  }

  @Override
  public Boolean associateRoleFor(URLResource res) {

    return associateRoleFor(res, null);
  }

  @Override
  public Boolean associateRoleFor(URLResource urlResource, List<Long> rolePks) {
    Criteria criteria = this.getSession().createCriteria(URLResource.class)
        .createAlias("roles", "roleRes").createAlias("roles.role", "role");
    if (rolePks != null) {
      rolePks.add(-1L);
      criteria.add(Restrictions.in("role.pk", rolePks));
    }
    criteria.add(Restrictions.eq("roleRes.resource.pk", urlResource.getPk()));

    return criteria.list().size() > 0;
  }

  @Override
  public boolean associateUserFor(URLResource res) {
    Subject subject = SecurityUtils.getSubject();
    UserDetails userDetail = null;
    String username = "";
    if (subject.getPrincipal() instanceof UserDetails) {
      userDetail = (UserDetails) subject.getPrincipal();
      username = userDetail.getUserName();

    } else if (subject.getPrincipal() instanceof String) {
      username = (String) subject.getPrincipal();
    }
    // User user = User.currentUser(userDetail);
    Criteria criteria = this.getSession().createCriteria(UserResource.class)
        .createAlias("resource", "res").createAlias("user", "user");
    criteria.add(Restrictions.eq("res.pk", res.getPk()))
        .add(Restrictions.eq("user.name", username));

    return criteria.list().size() > 0;
  }

  @Override
  public List<URLResource> findResourceFor(Long roleId) {
    Set<URLResource> ress = new HashSet<URLResource>();
    Criteria criteria = this.getSession().createCriteria(URLResource.class)
        .createAlias("roles", "roleRes").add(Restrictions.eq("roleRes.role.pk", roleId))
        .add(Restrictions.eq("available", true)).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
    Iterator i = criteria.list().iterator();
    /* 过滤掉联表之后产生的集合 */
    while (i.hasNext()) {
      Map map = (Map) i.next();
      RoleResource roleRes = (RoleResource) map.get("roleRes");
      ress.add(roleRes.getResource());
    }
    return new ArrayList<URLResource>(ress);

  }

  @SuppressWarnings("unchecked")
  @Override
  public List<URLResource> findResourceFor() {
    return this.getSession().createQuery("From URLResource where available=true").list();
  }

  @Override
  public int saveFileFor(Long uriPk, Integer width, Integer height, String type, String name,
      byte[] binary) {
    String sql =
        " INSERT INTO 4a_img(res_id,img_name,img_width,img_height,img_type,img_available,img_blob) "
            + " VALUES(?,?,?,?,?,1,?)";
    logger.info("save imageFile sql{}", sql);
    return executeBySQL(sql, uriPk, name, width, height, type, binary);
  }

  @Override
  public int updateFile(Long uriPk, Integer width, Integer height, String type, String name,
      byte[] binary) {
    String sql =
        " UPDATE 4a_img SET img_name = ?,img_width = ?,img_height = ?,img_type = ?,img_available = 1,img_blob = ?"
            + " WHERE res_id = ? ";
    logger.info("update imageFile sql{}", sql);
    return executeBySQL(sql, name, width, height, type, binary, uriPk);
  }

  @Override
  public List findFileFor() {
    return findFileFor(null);
  }

  @Override
  public List findFileFor(Long uriPk) {
    String sql = " select * from 4a_img";
    if (uriPk != null)
      sql += " where res_id = ?";
    logger.info("find imageFile sql{}", sql);
    SQLQuery sqlQuery = createSQLQuery(sql);
    if (uriPk != null)
      sqlQuery.setLong(0, uriPk);
    return sqlQuery.list();
  }

  @Override
  public int deleteFileBy(Long uriPk) {
    String sql = "DELETE FROM 4a_img  WHERE res_id = ?";
    logger.info("delete imageFile sql{}", sql);
    return executeBySQL(sql, uriPk);
  }

  @Override
  public URLResource findResourseByPk(String pk) {
    logger.info("findResourseByPk  {},{}", pk);
    String hql = "from URLResource where res_id=?";
    return findByHql(hql, pk).size() > 0 ? findByHql(hql, pk).get(0) : null;
  }

  @Override
  public void removeRoleOrUser(long pk) {
    String sql = "DELETE FROM 4a_role_resource  WHERE res_id = " + pk; // 与角色的关联关系
    Query query = getSession().createSQLQuery(sql);
    query.executeUpdate();

    sql = "DELETE FROM 4a_user_resource  WHERE res_id = " + pk; // 与用户的关联关系
    query = getSession().createSQLQuery(sql);
    query.executeUpdate();

  }

  @Override
  public List<URLResource> findModulesFor(User user, URLResource urlRescource) {
    return findSubResouresFor(user, urlRescource.getUuid(), urlRescource.getType());
  }

  @Override
  public void deleteRoleUrlByPk(Long pk) {
    String sql = "DELETE FROM 4a_role_resource  WHERE role_id = " + pk; // 删除角色权限
    Query query = getSession().createSQLQuery(sql);
    query.executeUpdate();

  }

  @Override
  public void deleteUserRoleByPk(Long pk) {
    String sql = "DELETE FROM 4a_user_role  WHERE role_id = " + pk; // 删除用户关联关系
    Query query = getSession().createSQLQuery(sql);
    query.executeUpdate();

  }

  @Override
  public List<URLResource> findPesMenus(User user) {
    logger.info("findAppFor {}", user);
    // String hql = "select DISTINCT m from URLResource m "
    // + " join m.roles as rs join rs.role as r join r.users as us join us.user as u where
    // m.available=? "
    // + " and u.name=? and m.type=? and m.uuid=? order by m.reorder";
    Criteria criteria = this.getSession().createCriteria(UserResource.class)
        .createAlias("resource", "re").createAlias("user", "user");
    criteria.add(Restrictions.eq("user.name", user.getName()))

        .add(Restrictions.eq("re.available", true)).addOrder(Order.asc("re.reorder"))
        .setResultTransformer(Criteria.ROOT_ENTITY);
    List li = criteria.list();
    List<URLResource> resList = new ArrayList<URLResource>();
    for (Iterator i = li.iterator(); i.hasNext();) {
      UserResource ur = (UserResource) i.next();
      URLResource res = ur.getResource();
      resList.add(res);
    }
    return resList;
  }

  @Override
  public String findNametoURL(String urlName) {
    logger.info("findNametoURL {}", urlName);
    SQLQuery query = createSQLQuery(
        "SELECT  res_name  FROM 4a_urlresource  where res_type ='menu' and res_url like ? LIMIT 1 ");
    query.setString(0, urlName + "%");
    Object o = query.uniqueResult();
    if (o != null && o instanceof String[]) {
      String[] array = (String[]) o;
      if (array.length > 0) {
        return array[0];
      }
    } else if (o != null && o instanceof String) {
      return o.toString();
    }
    return "";
  }

  @Override
  public List<URLResource> uRLResourceList(String url) {
    SQLQuery query = createSQLQuery(
        "SELECT  res_name  FROM 4a_urlresource  where res_type ='menu' and res_url like ?");
    query.setString(0, url + "%");
    return query.list();
  }
}
