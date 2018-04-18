/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.repository.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.query.Query;
import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.exception.BusinessException;
import com.cntest.foura.domain.Organization;
import com.cntest.foura.domain.Role;
import com.cntest.foura.repository.OrganizationRepository;
import com.cntest.foura.service.impl.OrganizationServiceImpl;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月5日
 * @version 1.0
 **/
@Repository
@SuppressWarnings("unchecked")
public class OrganizationRepositoryHibernateImpl extends AbstractHibernateDao<Organization,Long> implements OrganizationRepository {
	private static Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);
	@Override
	protected Class<Organization> getEntityClass() {
		return Organization.class;
	}

	
	@Override
	public List<Organization> listByParentIsNull(String name) {
		logger.info("list<Organization>  parent isNull");
		  List<Organization> orgList=new ArrayList<Organization>();
		Criteria c = getSession().createCriteria(getEntityClass())
				.add(Restrictions.isNull("parent"));

		    if(name != null && name.length()>0) {
		    	c.add(Restrictions.or(Restrictions.like("name", "%"+name+"%"), (Restrictions.like("code", "%"+name+"%"))));
		   }
		
		    orgList=c.list();
		
		return orgList;
	}

	@Override
	public List<Organization> listByParentFor(Long pk,String name) {
		logger.info("list<Organization>  parent.pk = {}",pk);
		Criteria c = getSession().createCriteria(getEntityClass());
		if(pk != null) 
			c.add(Restrictions.eq("parent.pk", pk));
		else
			c.add(Restrictions.isNull("parent.pk"));
		if(name != null && name.length() >0 && !"-1".equals(name))
			c.add(Restrictions.or(Restrictions.like("name", "%"+name+"%"), (Restrictions.like("code", "%"+name+"%"))));
		return c.list();
	}
	@Override
	public List<Organization> listByNameFor(String name) {
		logger.info("list<Organization> name",name);
		Criteria c = getSession().createCriteria(getEntityClass());
		if(name != null && name.length() >0 && !"-1".equals(name))
			c.add(Restrictions.or(Restrictions.like("name", "%"+name+"%"), (Restrictions.like("code", "%"+name+"%"))));
		return c.list();
	}
	@Override
	public List<Organization> selectChildrenNotLeafFor(Long parentPk) {
		logger.info("selectChildrenNotLeafFor  parent.pk = {}",parentPk);
		Criteria c = getSession().createCriteria(getEntityClass());
		if(parentPk != null) 
			c.add(Restrictions.eq("parent.pk", parentPk));
		else
			c.add(Restrictions.isNull("parent.pk"));
		c.add(Restrictions.le("type", 3));
		return c.list();
	}
	
	@Override
	public void listByParentFor(Long pk,Query<Organization> query) {
		logger.info("list<Organization>  parent.pk = {},name={}",query);

		Map<String, String[]> parameters = query.getParameters();
		if(parameters.get("is")!=null &&parameters.get("is").length> 0 && "false".equals(parameters.get("is")[0])) {
			parameters.remove("qname");
		}
		Criteria criteria = createCriteria();
		
		if(pk != null) 
			criteria.add(Restrictions.eq("parent.pk", pk));
		if(parameters != null) {
		    String[] name = parameters.get("qname");
		    if(name != null && name[0].length()>0) {
		    	//criteria.add(Restrictions.like("name", "%"+name[0]+"%"));
		    	criteria.add(Restrictions.or(Restrictions.like("name", "%"+name[0]+"%"), (Restrictions.like("code", "%"+name[0]+"%"))));
		    }
		}
		ProjectionList ps = Projections.projectionList();

		ps.add(Projections.rowCount());
		
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Object o = criteria.setProjection(ps).uniqueResult();
		Long rowCount = (Long)o;
		query.setTotalRows(rowCount.intValue());
		criteria.setProjection(null);
		
		criteria.setFirstResult(query.getStartRow());
		criteria.setMaxResults(query.getPagesize());
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		query.setResults(criteria.list());

	}


	@Override
	public List<Organization> getOrgAllByPk(Long pk,Long type) {
		logger.info("getOrgAllByPk  pk = {}",pk);
		List orgList = new ArrayList();
		String hal ="FROM 4a_org WHERE org_id IN "
				+ "(SELECT org_id FROM 4a_org WHERE "
				+ "org_id="+pk
				+ " OR p_id="+pk
				+ ") OR p_id IN (SELECT org_id FROM 4a_org WHERE org_id="+pk
				+ " OR p_id="+pk+")";
		if(type==4 || type== 3) {
			String sql ="SELECT 4a.org_id,4a.org_name,4a.org_code,4a.org_type,4a.available,org.org_code AS pd_id,4a.schoolsegmentid,4a.schooltypeid FROM  4a_org 4a LEFT JOIN 4a_org org ON  org.org_id=4a.p_id "
					+ " WHERE 4a.org_id=? OR 4a.p_id=?";
			SQLQuery query = getSession().createSQLQuery(sql);
			query.setLong(0, pk);
			query.setLong(1, pk);
			orgList = query.list();
		}else if(type==2) {
			String hql ="select org_id "+hal;
			String sql ="SELECT 4a.org_id,4a.org_name,4a.org_code,4a.org_type,4a.available,org.org_code AS pd_id,4a.schoolsegmentid,4a.schooltypeid FROM  4a_org 4a LEFT JOIN 4a_org org ON  org.org_id=4a.p_id  "
					+ " where 4a.org_id in ("+hql+")"
					+ "or 4a.p_id in ("+hql+")";
			SQLQuery query = getSession().createSQLQuery(sql);
			orgList = query.list();
		}else if(type==1) {
			String hql ="select org_id "+hal;
			String sqlMessage ="select org_id from 4a_org where org_id in ("+hql+")"
					+ "or p_id in ("+hql+")";
			String sql ="SELECT 4a.org_id,4a.org_name,4a.org_code,4a.org_type,4a.available,org.org_code AS pd_id,4a.schoolsegmentid,4a.schooltypeid "
					+ "FROM  4a_org 4a "
					+ "LEFT JOIN 4a_org org "
					+ "ON  org.org_id=4a.p_id "
					+ "where 4a.org_id in ("+sqlMessage+")"
					+ "or 4a.p_id in ("+sqlMessage+")";
			SQLQuery query = getSession().createSQLQuery(sql);
			orgList = query.list();
		}
		return orgList;
	}
	@Override
	public List<Organization> getOrgAllByName(Long pk,Long type,String name) {
		logger.info("getOrgAllByPk  pk = {}",pk);
		List orgList = new ArrayList();
		String hal ="FROM 4a_org WHERE org_id IN "
				+ "(SELECT org_id FROM 4a_org WHERE "
				+ "org_id="+pk
				+ " OR p_id="+pk
				+ ") OR p_id IN (SELECT org_id FROM 4a_org WHERE org_id="+pk
				+ " OR p_id="+pk+")";
		if(type==4 || type== 3) {
			String sql =" SELECT mm.* FROM( SELECT 4a.* FROM  4a_org 4a LEFT JOIN 4a_org org ON  org.org_id=4a.p_id "
					+ " ) mm WHERE (mm.org_code LIKE ? OR mm.org_name LIKE ? ) AND mm.p_id= ? ";
			SQLQuery query = getSession().createSQLQuery(sql);
			query.setString(0, "%"+name+"%");
			query.setString(1, "%"+name+"%");
			query.setLong(2, pk);
			query.addEntity(Organization.class);
			orgList = query.list();
		}else if(type==2) {
			String hql ="select org_id "+hal;
			String sql ="SELECT mm.* FROM( SELECT 4a.* FROM  4a_org 4a LEFT JOIN 4a_org org ON  org.org_id=4a.p_id  "
					+ " where 4a.org_id in ("+hql+")"
					+ "or 4a.p_id in ("+hql+")) mm WHERE(mm.org_code LIKE ? OR mm.org_name LIKE ? )"  ;
			SQLQuery query = getSession().createSQLQuery(sql);
			query.setString(0, "%"+name+"%");
			query.setString(1, "%"+name+"%");
			query.addEntity(Organization.class);
			orgList = query.list();
		}else if(type==1) {
			String hql ="select org_id "+hal;
			String sqlMessage ="select org_id from 4a_org where org_id in ("+hql+")"
					+ "or p_id in ("+hql+")";
			String sql ="SELECT mm.* FROM( SELECT 4a.* "
					+ "FROM  4a_org 4a "
					+ "LEFT JOIN 4a_org org "
					+ "ON  org.org_id=4a.p_id "
					+ "where 4a.org_id in ("+sqlMessage+")"
					+ "or 4a.p_id in ("+sqlMessage+")) mm WHERE (mm.org_code LIKE ? OR mm.org_name LIKE ? )";
			SQLQuery query = getSession().createSQLQuery(sql);
			query.setString(0, "%"+name+"%");
			query.setString(1, "%"+name+"%");
			query.addEntity(Organization.class);
			orgList = query.list();
		}
		return orgList;
	}
	
	
	@Override
	public int findCountuser(String in) {
			String hql ="SELECT count(*) FROM 4a_user USER,4a_userbelong org WHERE user.user_id = org.user_id AND org.org_id IN("+in+") ";
			SQLQuery query = getSession().createSQLQuery(hql);
		return Integer.valueOf(query.uniqueResult().toString());
	}

	@Override
	public void createOrgExcel(Long pk, Long type,HttpServletRequest request) {
		String fileName="";
		//获取路径
		String place = request.getSession().getServletContext()
				.getRealPath("/") + "WEB-INF/upload/";
		
		File file1 = new File(place);
		if (!file1.exists()) {
			// 创建目录
			file1.mkdirs();
		}
		List orgList=getOrgAllByPk(pk,type);
		//将数据写入EXCEL中输出
		//创建一个excel文件
		HSSFWorkbook wb =new HSSFWorkbook();
		//添加一个sheet
		HSSFSheet sheet =wb.createSheet("组织架构");
		//在sheet添加表头第行
		HSSFRow row =sheet.createRow(0);
		//创建单元格，并设置表头
		HSSFCell cell = row.createCell((short)0);
		cell.setCellValue("组织名称");
		
		cell = row.createCell((short)1);
		cell.setCellValue("组织代码");
		
		cell = row.createCell((short)2);
		cell.setCellValue("所属机构");
		
		cell = row.createCell((short)3);
		cell.setCellValue("学校类型");
		
		cell = row.createCell((short)4);
		cell.setCellValue("学校学段");
		
		cell = row.createCell((short)5);
		cell.setCellValue("上级组织代码");
		List<String> orglists = new ArrayList<String>();
		//将数据加到EXCEL中
		try {
			String orgType="";
			String schooltypeid="";
			String schoolsegmentid="";
			request.setAttribute("orglist", orgList);
			for(int i=0;i<orgList.size();i++) {
				row = sheet.createRow((int)i+1);
				Object[] obj =(Object[])orgList.get(i);
				//显示装换
				if(obj[3]!="" && obj[3]!=null){
					orgType=obj[3].toString();
					if(orgType.equals("0")){
						orgType="0-教育局";
					}else if(orgType.equals("1")){
						orgType="1-省";
					}else if(orgType.equals("2")){
						orgType="2-市";
					}else if(orgType.equals("3")){
						orgType="3-区";
					}else if(orgType.equals("4")){
						orgType="4-学校";
					}
				}
				if(obj[7]!="" && obj[7]!=null){
					schooltypeid=obj[7].toString();
					if(schooltypeid.equals("1")){
						schooltypeid="1-公办学校";
					}else if(schooltypeid.equals("2")){
						schooltypeid="2-民办优质学校";
					}else if(schooltypeid.equals("3")){
						schooltypeid="3-民办农民工子女学校";
					}
				}
				if(obj[6]!="" && obj[6]!=null){
					schoolsegmentid=obj[6].toString();
					if(schoolsegmentid.equals("1")){
						schoolsegmentid="1-小学";
					}else if(schoolsegmentid.equals("2")){
						schoolsegmentid="2-初中";
					}else if(schoolsegmentid.equals("3")){
						schoolsegmentid="3-九年制";
					}else if(schoolsegmentid.equals("4")){
						schoolsegmentid="4-高中";
					}else if(schoolsegmentid.equals("5")){
						schoolsegmentid="5-中职";
					}
				}
				row.createCell((short)0).setCellValue(obj[1]==null?"":obj[1].toString());
				row.createCell((short)1).setCellValue(obj[2]==null?"":obj[2].toString());
				row.createCell((short)2).setCellValue(obj[3]==null?"":orgType);
				row.createCell((short)3).setCellValue(obj[6]==null?"":schooltypeid);
				row.createCell((short)4).setCellValue(obj[7]==null?"":schoolsegmentid);
				row.createCell((short)5).setCellValue(obj[5]==null?"":obj[5].toString());
				orglists.add(obj[1].toString());
			}
			
			request.setAttribute("orglist",orglists);
			place=place+"zzjg.xls";
			FileOutputStream file = new FileOutputStream(place);
			wb.write(file);
			file.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查看是否已经存在组织信息
	 */
	@Override
	public int findOrgList() {
		int num=0;
		String sql ="select * from 4a_org";
		SQLQuery query = getSession().createSQLQuery(sql);
		List orgList = query.list();
		if(orgList!=null) {
			num=orgList.size();
		}
		return num;
	}

	/**
	 * 删除数据库中所有的组织信息
	 */
	@Override
	public void deleteAllOrg() {
		String sql ="delete from 4a_org where 1=1";
		SQLQuery query = getSession().createSQLQuery(sql);
		int a =query.executeUpdate();
	}

/**
 * 新增组织数据
 */
	@Override
	public void saveOrg(Organization org) {
		String sql ="insert into 4a_org(org_id, org_name, org_code, org_type, available, p_id, schooltypeid, schoolsegmentid) ";
		String pk="NULL,";
		String schooltypeId="NULL,";
		String schoolsegmentId="NULL";
		if(org.getParent()!=null) {
			pk=org.getParent().getPk()+",";
		}
		if(org.getSchooltype()!=null) {
			schooltypeId = org.getSchooltype().getId()+",";
		}
		if(org.getSchoolSegment()!=null) {
			schoolsegmentId = org.getSchoolSegment().getId().toString();
		}
		String values ="values(?,?,?,?,?,"
				+ pk
				+ schooltypeId
				+ schoolsegmentId
				+")";
		
		SQLQuery query = getSession().createSQLQuery(sql+values);
		query.setLong(0, org.getPk());
		query.setString(1, org.getName());
		query.setString(2, org.getCode());
		query.setLong(3, org.getType());
		query.setBoolean(4, org.getAvailable());
		query.executeUpdate();
	}


@Override
public List<Organization> nextOrgCountOfParent(List<Organization> list) {
	int num=0;	
	for(int i=0;i<list.size();i++){
		String sql ="select count(1) from 4a_org where p_id="+list.get(i).getPk();
		SQLQuery query = getSession().createSQLQuery(sql);
		num=Integer.valueOf(query.uniqueResult().toString());
		list.get(i).setOrgCount(num);
	}
	return list;
}


@Override
public List<Organization> getNextOrgList(Long pk) {
	logger.info("list<Organization>  parent");
	List<Organization> orgList=new ArrayList<Organization>();
	Criteria c = getSession().createCriteria(getEntityClass());

	    if(pk != null)
	    	c.add(Restrictions.eq("parent.pk", pk));
	
	    orgList=c.list();
	
	return orgList;
}
@Override
public boolean findOrgByCode(String code, String orgId) {
	boolean flg=true;
	//不为空表示此操作为修改，否则为新增
	Organization organization = new Organization();
	String codeEdit ="";
	if(orgId!="0" || !"0".equals(orgId)){
		String hql ="from Organization where org_id=?";
		List<Organization> orgList=findByHql(hql, orgId);
		if(orgList.size()>0){
			organization=orgList.get(0);
		    codeEdit =organization.getCode();
		}
	}
	//角色编号为在修改时无变化
	if(codeEdit!="" && codeEdit.equalsIgnoreCase(code)){
		flg=true;
	}else{
		String hql ="from Organization where org_code=?";
		List<Organization> orgList=findByHql(hql, code);
		if(orgList.size()>0){
			flg=false;
		}
	}
	return flg;
}


@Override
public boolean findOrgByCode(String code) {
	boolean flg=false;
	String hql ="from Organization where org_code=?";
	List<Organization> orgList=findByHql(hql, code);
	if(orgList.size()>0){
		flg=true;
	}
	return flg;
}


@Override
public Organization getOrgByCode(String code) {
	String hql ="from Organization where org_code=?";
	List<Organization> orgList=findByHql(hql, code);
	return  orgList.size()>0?orgList.get(0):new Organization();
}


@Override
public Organization findOrgByName(String name) {
	String hql ="from Organization where org_name=?";
	Organization org=findEntityByHql(hql, name);
	return  org;
}


@Override
public boolean findOrgByOrgId(Long id) {
	boolean flg=false;
	String hql ="SELECT  * FROM 4a_org WHERE org_id=?";
	SQLQuery query = getSession().createSQLQuery(hql);
	query.setLong(0, id);
	query.uniqueResult();
	if(query.uniqueResult()!=null){
		flg=true;
	}
	return flg;
}

//根据组织的pk值查询本身和所有上级组织
@Override
public List<Organization> getAllTotByMySelf(String orgId) throws BusinessException {
	Organization org =getOrgByCode(orgId);
	List<Organization> orgList=new ArrayList<Organization>();
	orgList.add(org);
	return getAllTopOrg(org,orgList);
}

private List<Organization> getAllTopOrg(Organization org,List<Organization> orgList) throws BusinessException{
	if(org!=null && org.getType()>0){
		Organization orgT =org.getParent();
		orgList.add(orgT);
		if(orgT!=null && orgT.getType()>1){
			getAllTopOrg(orgT,orgList);
		}
	}
	return orgList;
}


}

