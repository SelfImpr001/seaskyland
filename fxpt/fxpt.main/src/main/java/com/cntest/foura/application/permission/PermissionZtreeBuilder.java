/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.application.permission;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import com.cntest.foura.domain.DataAuthorized;
import com.cntest.foura.domain.DataPermission;
import com.cntest.foura.domain.URIType;
import com.cntest.foura.domain.URLResource;
import com.cntest.foura.service.DataPermissionService;
import com.cntest.foura.service.URLResourceService;
import com.cntest.web.view.JqueryZtree;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年12月10日
 * @version 1.0
 **/
@Component
@Scope("prototype")
public class PermissionZtreeBuilder {
	private static Logger logger = LoggerFactory.getLogger(PermissionZtreeBuilder.class);

	@Autowired
	private DataPermissionService dataPermissionService;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private URLResourceService resourceService;
	
	@Autowired
	private org.springframework.cache.ehcache.EhCacheManagerFactoryBean cacheManagerFactory;

	
	public static enum PermissionType{
		role,user
	}

	public PermissionZtreeBuilder() {

	}

	public JqueryZtree build(URLResource menuRoot,PermissionType type,Long ownerPk) {
		JqueryZtree root = new JqueryZtree().isParent(true).name(menuRoot.getName()).open(true);
		JqueryZtree functions = root.genChild(true).name("功能权限").open(true).expendAttr("dataType", "funcs");
		buildFunsNodes(functions,ownerPk,type,menuRoot,"menu");
		//JqueryZtree report = root.genChild(true).name("报告查看权限").open(true).expendAttr("dataType", "reports");
		JqueryZtree datas = root.genChild(true).name("数据权限").open(true).expendAttr("dataType", "data");
		getPermissionTree(datas,type,ownerPk);
		return root;
	}
	
	private void buildFunsNodes(final JqueryZtree functions,final Long ownerPk,PermissionType type,final URLResource menuRoot,String resType) {
		String sql = "SELECT a.res_pid,a.res_id,a.res_name,a.res_type,b.user_id AS checked FROM 4a_urlresource a LEFT JOIN 4a_user_resource b ON b.res_id = a.res_id AND b.user_id=? WHERE a.res_available = 1 AND a.res_type=? ORDER BY a.res_pid ,a.res_id;";
		if(type.equals(PermissionType.role)) {
			sql = "SELECT a.res_pid,a.res_id,a.res_name,a.res_type,b.role_id AS checked FROM 4a_urlresource a LEFT JOIN 4a_role_resource b ON b.res_id = a.res_id AND b.role_id=? WHERE a.res_available = 1 AND a.res_type=? ORDER BY a.res_pid ,a.res_id";
		}
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {

			private HashMap<Long,JqueryZtree> nodes = new HashMap<>();		

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				boolean checked = ownerPk.equals(rs.getLong("checked"))?true:false;
				Long ppk = rs.getLong("res_pid");
				Long pk = rs.getLong("res_id");
				JqueryZtree thisNode = this.nodes.get(ppk);
				if(thisNode == null) {
					thisNode = functions.genChild();
				    this.nodes.put(pk, thisNode);
				}else {
					thisNode = thisNode.genChild();
					this.nodes.put(pk, thisNode);
				}				
				thisNode.name(rs.getString("res_name")).expendAttr("pk", pk).open(false).checked(checked);				
			}
			
		},ownerPk,resType);
		
	}
	
	public JqueryZtree build(URLResource menuRoot,List<URLResource> oldMenus,PermissionType type,Long ownerPk ) {
		JqueryZtree root = new JqueryZtree().isParent(true).name(menuRoot.getName()).open(true);
		JqueryZtree functions = root.genChild(true).name("菜单权限").open(true).expendAttr("dataType", "funcs");
		JqueryZtree report = root.genChild(true).name("报表权限").open(true).expendAttr("dataType", "reports");
		//JqueryZtree datas = root.genChild(false).name("数据权限").open(true).expendAttr("dataType", "data");
		
		List<URLResource> menus = resourceService.getMenuChildren(menuRoot.getUuid(), URIType.menu,"1");//菜单
		List<URLResource> reports = resourceService.getMenuChildren(menuRoot.getUuid(), URIType.module,"1");//报告
		//menus.addAll(reports);
		Collections.sort(menus, new Comparator<URLResource>() {

			@Override
			public int compare(URLResource o1, URLResource o2) {
				return o1.getReorder().compareTo(o1.getReorder());
			}
			
		});
		for (URLResource menu : menus) {
			//是否存在有效下级.
			List<URLResource> menuChilds = resourceService.getMenuChildren(menu.getUuid(), menu.getType(),"1");
			if (menuChilds == null || menuChilds.size() == 0){
				//是否存在下级
				List<URLResource> allMenuChilds =resourceService.getMenuChildren(menu.getUuid(), menu.getType(),null);
				if(allMenuChilds.size()>0)
					continue;
			}
			JqueryZtree ztree = functions.genChild().name(menu.getName()).expendAttr("pk", menu.getPk()).open(false);
			buildChildrenMenus(menu, oldMenus, ztree);
			if (oldMenus.contains(menu)) {
				ztree.checked(true);
			}		
		}
		for (URLResource rs : reports) {
			//是否存在有效下级.
			List<URLResource> menuChilds = resourceService.getMenuChildren(rs.getUuid(), rs.getType(),"1");
			if (menuChilds == null || menuChilds.size() == 0){
				//是否存在下级
				List<URLResource> allMenuChilds =resourceService.getMenuChildren(rs.getUuid(), rs.getType(),null);
				if(allMenuChilds.size()>0)
					continue;
			}
			JqueryZtree ztree = report.genChild().name(rs.getName()).expendAttr("pk", rs.getPk()).open(false);
			buildChildrenMenus(rs, oldMenus, ztree);
			if (oldMenus.contains(rs)) {
				ztree.checked(true);
			}		
		}
		//getPermissionTree(datas,type,ownerPk);
		return root;
	}

	private void buildChildrenMenus(URLResource parentMenu, List<URLResource> existMenus, JqueryZtree parentNode) {
		List<URLResource> menus = resourceService.getMenuChildren(parentMenu.getUuid(), parentMenu.getType(),"1");
		if (menus == null || menus.size() == 0){
			//是否存在下级
			List<URLResource> allMenuChilds =resourceService.getMenuChildren(parentMenu.getUuid(), parentMenu.getType(),null);
			if(allMenuChilds.size()>0)
				return;
		}
		for (URLResource menu : menus) {
			List<URLResource> menuChilds = resourceService.getMenuChildren(menu.getUuid(), menu.getType(),"1");
			if (menuChilds == null || menuChilds.size() == 0){
				//是否存在下级
				List<URLResource> allMenuChilds =resourceService.getMenuChildren(menu.getUuid(), menu.getType(),null);
				if(allMenuChilds.size()>0)
					continue;
			}
			JqueryZtree ztree = parentNode.genChild().name(menu.getName()).expendAttr("pk", menu.getPk()).open(false);
			  buildChildrenMenus(menu, existMenus, ztree);
			if (existMenus.contains(menu)) {
				ztree.checked(true); 
			}
		}
	}

	private JqueryZtree getPermissionTree(JqueryZtree root,PermissionType type,Long ownerPk) {
		List<DataAuthorized> dataAuthorizeds = dataPermissionService.findDataAuthorizeds(type+"", ownerPk);
		List<DataPermission> dps = dataPermissionService.getChildren(null);
		for (DataPermission dp : dps) {
			JqueryZtree ztree = root.genChild(false).name(dp.getName()).expendAttr("paramName", dp.getParamName()).open(false);
			ztree.expendAttr("pk", dp.getPk()).expendAttr("pk", dp.getPk());
			buildJqueryZtree(ztree,dp, null, dataAuthorizeds);
		}
		return root;
	}

	private void buildJqueryZtree(final JqueryZtree parentNode,final DataPermission dp, Object[] args, final List<DataAuthorized> dataAuthorizeds) {
		String sql = dp.getSource();
		logger.debug("buildPermissionTree for {}", sql);

		jdbcTemplate.query(sql, new RowCallbackHandler() {
			private HashMap<String,JqueryZtree> nodes = new HashMap<>();
			
			private boolean checked(Long pk,String fromTable) {
				if (dataAuthorizeds != null && dataAuthorizeds.size() > 0) {
					Iterator<DataAuthorized> itd = dataAuthorizeds.iterator();
					while(itd.hasNext()) {
						DataAuthorized da = itd.next();
						if (dp.equals(da.getPermission())) {
							if (pk.equals(da.getFromPk()) && da.getFromTable().equalsIgnoreCase(fromTable)) { //
								itd.remove();
								return true;
							}
						}
					}
					
				}
				return false;
			}
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Long keyValue = rs.getLong(dp.getParamKeyField());
				String dataFromTable = rs.getString(dp.getTable());
				boolean checked = checked(keyValue ,dataFromTable);
				String thisPkRefValue = rs.getObject(dp.getParentRefKey()) + "";
				String thisPkValue = rs.getObject(dp.getParamKeyField()) + "";
				JqueryZtree thisNode = this.nodes.get(thisPkRefValue);
				if(thisNode == null) {
					thisNode = parentNode.genChild(false);
				    this.nodes.put(thisPkValue, thisNode);
				}else {
					thisNode = thisNode.genChild(false);
					this.nodes.put(thisPkValue, thisNode);
				}
				thisNode.name(rs.getObject(dp.getParamNamefield()) + "");
				thisNode.expendAttr("checkOnly", true);
				thisNode.expendAttr("permissionPk", dp.getPk());
				thisNode.expendAttr("targetValue", rs.getLong(dp.getParamValueField()));
				thisNode.expendAttr(dp.getParamNamefield(), rs.getObject(dp.getParamNamefield()) + "");
				thisNode.expendAttr(dp.getParamValueField(), rs.getObject(dp.getParamValueField()) + "");
				thisNode.expendAttr(dp.getParamKeyField(), rs.getObject(dp.getParamKeyField()) + "");
				thisNode.expendAttr("nameKey", dp.getParamNamefield());
				thisNode.expendAttr("valueKey", dp.getParamValueField());
				thisNode.expendAttr("idKey", dp.getParamKeyField());
				thisNode.expendAttr("paramName", dp.getParamName());
				thisNode.expendAttr("dataFromTable", dataFromTable);
				thisNode.checked(checked);
				
			}},args);
	}
}
