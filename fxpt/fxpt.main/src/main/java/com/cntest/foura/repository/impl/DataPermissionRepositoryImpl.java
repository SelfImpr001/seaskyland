/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.DataPermission;
import com.cntest.foura.repository.DataPermissionRepository;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年12月4日
 * @version 1.0
 **/
@Repository
public class DataPermissionRepositoryImpl extends AbstractHibernateDao<DataPermission, Long> implements DataPermissionRepository {
	private static Logger logger = LoggerFactory.getLogger(DataPermissionRepositoryImpl.class);

	@Override
	protected Class<DataPermission> getEntityClass() {
		return DataPermission.class;
	}

	@Override
	public List<DataPermission> selectByParentId(Long parentId) {
		logger.debug("selectByParentId for parentId is {}", parentId == null || parentId == 0 ? "null" : parentId);
		if (parentId != null && parentId > 0)
			return findByHql("From DataPermission where parent.pk=? and status=1 ", parentId);
		return findByHql("From DataPermission where parent is null and status=1 ");
	}

	@Override
	public void upateAuthorized(Long targetPk, String target, DataAuthorized[] dataAuthorizeds) {
		logger.debug("update Authorized for {} pk is {}", target, targetPk);

		List<DataAuthorized> oldDataAuthorizeds = this.selectDataAuthorizeds(target, targetPk);
		if (oldDataAuthorizeds != null) {
			for (DataAuthorized oda : oldDataAuthorizeds) {
				this.getSession().delete(oda);
			}
		}
		if (dataAuthorizeds != null) {
			for (DataAuthorized da : dataAuthorizeds) {
				this.getSession().save(da);
			}
		}
	}

	@Override
	public List<DataAuthorized> selectDataAuthorizeds(String target, Long targetPk) {
//		logger.debug("select  Authorizeds for {} pk is {}",target,targetPk);
//		String hql = "From DataAuthorized where target=? and targetPk=? and permission.status=1";
//		Query query = getSession().createQuery(hql);
//		query.setString(0, target);
//		query.setLong(1, targetPk);
//		return query.list();
		logger.debug("select  Authorizeds for {} pk is {}", target, targetPk);
		String hql = "From DataAuthorized where target=? and targetPk=? ";
		Query query = getSession().createQuery(hql);
		query.setString(0, target);
		query.setLong(1, targetPk);
		List<DataAuthorized> das = query.list();
//		ArrayList<DataAuthorized> newDas = new ArrayList<DataAuthorized>();
//		if (das != null) {
//			for (DataAuthorized da : das) {
//				DataPermission p = this.load(da.getPermission().getPk());
//				StringBuilder sql = new StringBuilder("Select " ).append(da.getPk())
//						.append(" as authorized_id,").append(da.getTargetPk()).append(" as target_id,'")
//						.append(da.getTarget()).append("' as target,").append(da.getPermission().getPk()).append("  as data_permission_id,")
//				        .append(p.getParamKeyField())
//						.append(" as data_from_id,").append(p.getParamNamefield()).append(" as permission_name,")
//						.append(p.getParamValueField()).append(" as  permission_value from ").append(p.getTable())
//						.append(" where ").append(p.getParamKeyField())
//						.append("=?");
//				SQLQuery sqlquery = getSession().createSQLQuery(sql.toString());
//				sqlquery.addEntity(DataAuthorized.class);
//				sqlquery.setParameter(0, da.getFromPk());
//				List mydas = sqlquery.list();
//				if(mydas != null && mydas.size() >0) {
//					DataAuthorized myda = (DataAuthorized) mydas.get(0);
//					newDas.add(myda);
//				}
//				
//			}
//		}
//		return newDas;
		return das;
	}

	@Override
	public List<DataAuthorized> selectDataAuthorizeds(DataPermission permission) {
		String hql = "From DataAuthorized where permission.pk=? ";
		Query query = getSession().createQuery(hql);
		query.setLong(0, permission.getPk());
		return query.list();
	}

	@Override
	public void deleteDataAuthorizeds(DataPermission permission) {
		List<DataAuthorized> das = this.selectDataAuthorizeds(permission);
		if (das != null) {
			for (DataAuthorized da : das) {
				getCurrentSession().delete(da);
			}
		}
	}

	@Override
	public List<DataAuthorized> selectAllDataAuthorizeds(String tableName,String orgIds) {
		String hql = "From DataAuthorized where data_from_table = '"+tableName
				+"'  And permission_value IN("+orgIds+")" ;
		Query query = getSession().createQuery(hql);
		return query.list();
	}

}
