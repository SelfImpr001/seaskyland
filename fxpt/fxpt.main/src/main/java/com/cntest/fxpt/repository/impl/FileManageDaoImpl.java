
package com.cntest.fxpt.repository.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.cntest.common.repository.AbstractHibernateDao;
import com.cntest.fxpt.bean.WebRetrieveResult;
import com.cntest.fxpt.domain.FileManage;
import com.cntest.fxpt.repository.IFileManageDao;

@Repository("IFileManageDao")
public class FileManageDaoImpl extends AbstractHibernateDao<FileManage, Long> implements
		IFileManageDao {
	private static final Logger log = LoggerFactory
			.getLogger(FileManageDaoImpl.class);

	@Override
	public List<FileManage> fileList(Long examId, String type) {
		String hql = "from FileManage where examId=? and file_type in ("+type+")";
		return findByHql(hql, examId);
		
	}

	@Override
	protected Class<FileManage> getEntityClass() {
		return FileManage.class;
	}

	@Override
	public void saveFileMsg(WebRetrieveResult wrr,Long type,Long testPaperId,String username) {
		String sql ="insert into kn_file_manage(examId,file_type,file_name,import_time,file_path,testPaperId,importer) "
				+ "values(?,?,?,?,?,?,?)";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setLong(0, wrr.getExamId());
		sqlQuery.setLong(1, type);
		sqlQuery.setString(2, wrr.getFileRealName());
		sqlQuery.setDate(3, new Date());
		sqlQuery.setString(4, wrr.getFileParentDir()+"/fileManage/"+wrr.getFileName());
		sqlQuery.setLong(5,testPaperId);
		sqlQuery.setString(6, username);
		sqlQuery.executeUpdate();
	}

	@Override
	public FileManage findFileByFileId(Long fileId) {
		String hql = "from FileManage where fileId=?";
		return findEntityByHql(hql,fileId);
	}

	@Override
	public void deleteFileByExamid(Long examId,String type) {
		String sql = "delete from kn_file_manage where examid=? and file_type in ("+type+")";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setLong(0, examId);
		sqlQuery.executeUpdate();
	}

	@Override
	public void deleteFiels(List<FileManage> fileList) {
		if(fileList!=null) {
			for(int i =0;i<fileList.size();i++) {
				String path=fileList.get(i).getFilePath();
				File file = new File(path);
				if(file.exists()) {
					file.delete();
				}
			}
		}
	}

	@Override
	public void deleteFileByTestPaperId(Long testPaperId) {
		String sql = "delete from kn_file_manage where testPaperId=?";
		SQLQuery sqlQuery = createSQLQuery(sql);
		sqlQuery.setLong(0, testPaperId);
		sqlQuery.executeUpdate();
	}

	@Override
	public List<FileManage> findFileByTestPaperId(Long testPaperId) {
		String hql = "from FileManage where testPaperId=?";
		return findByHql(hql,testPaperId);
	}
	@Override
	public void deleteLSBorg() {
		String sql ="delete from 4a_org_tmp where 1=1";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public void marchingOrg() {
		//获取最大的组织id
		Long maxOrgId=getMaxOrgId();
		//临时表id增加maxOrgId  防止和4a表冲突
		if(maxOrgId>0){
			udateOrgId(maxOrgId);
		}
		//查询所有
		String sql = "SELECT DISTINCT(p_id) FROM 4a_org_tmp WHERE p_id IS NOT NULL ";
		SQLQuery query = getSession().createSQLQuery(sql);
		List list =query.list();
		for(int i =0;i<list.size();i++){
			//根据现在的临时pid找到真正的上级id
			String pid =list.get(i).toString();
			String id=getIdByPid(pid);
			sql="update 4a_org_tmp set p_id="+id
					+ " where p_id="+pid;
			query= getSession().createSQLQuery(sql);
			query.executeUpdate();
		}
	}
	private String getIdByPid(String code){
		String id ="";
		String sql ="select org_id from 4a_org_tmp where org_code="+code;
		SQLQuery query = getSession().createSQLQuery(sql);
		List list =query.list();
		if(list.size()>0 && list.get(0)!=null){
			id=list.get(0).toString();
		}else{
			sql ="select org_id from 4a_org where org_code="+code;
			query = getSession().createSQLQuery(sql);
			list =query.list();
			id=list.get(0).toString();
		}
		return id;
		
	}

	private void udateOrgId(Long maxOrgId){
		String sql="update 4a_org_tmp  SET "
				+ "org_id=(org_id+"+maxOrgId
				+ ") WHERE org_id>0";
		SQLQuery sQLquery = getSession().createSQLQuery(sql);
		sQLquery.executeUpdate();
	}
	private Long getMaxOrgId(){
		Long maxOrgId=0L;
		String sql="SELECT MAX(org_id) FROM 4a_org";
		SQLQuery sQLquery = getSession().createSQLQuery(sql);
		List list=sQLquery.list();
		if(list.size()>0 && list.get(0)!=null){
			maxOrgId=Long.parseLong(list.get(0).toString());
		}
		return maxOrgId;
	}

	@Override
	public void copyOrgTo4a() {
		//更新available字段默认为1
		String sql ="update 4a_org_tmp set available=1 where available is NULL ";
		SQLQuery sQLquery = getSession().createSQLQuery(sql);
		sQLquery.executeUpdate();
		//查询重复
		sql ="select org_code from 4a_org_tmp where 1=1";
		SQLQuery query = getSession().createSQLQuery(sql);
		List list=query.list();
		if(list.size()>0) {
			//删除重复信息
			cleanOrgData(list);
			//将零时表的数据copy到4a表
			copyOrgTo4aOrg();
		}
		
	}
	
	private void cleanOrgData(List list) {
		String str = list.toString();
		str=str.substring(1,str.length()-1);
		//删除导入的重复数据
		String sql ="DELETE FROM 4a_org WHERE org_code IN("+str+")";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}
	
	private void copyOrgTo4aOrg() {
		String sql ="INSERT INTO 4a_org SELECT * FROM 4a_org_tmp";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public List findAllListFromTmp() {
		String sql ="select org_id,p_id,org_type from 4a_org_tmp";
		SQLQuery query = getSession().createSQLQuery(sql);
		List orgList = query.list();
		return orgList;
	}
	
	
	@Override
	public List findOrgListByCodeFrom4a(String orgId) {
		Long code =Long.parseLong(orgId);
		String sql ="select org_id,p_id,org_type from 4a_org where org_code="+code;
		SQLQuery query = getSession().createSQLQuery(sql);
		List orgList = query.list();
		return orgList;
	}
	
	
	@Override
	public List findOrgListByCodeFromTmp(String orgId) {
		Long org_id = Long.parseLong(orgId);
		String sql ="select org_id,p_id,org_type from 4a_org_tmp where org_code="+org_id;
		SQLQuery query = getSession().createSQLQuery(sql);
		List orgList = query.list();
		return orgList;
	}

	@Override
	public void copyFile(WebRetrieveResult wrr) {
		try {
			String fileParentPath =wrr.getFileParentDir()+"/"+"fileManage";
			String fileOld=wrr.getFileDir()+wrr.getFileName();
			//文件不存在则新建
			File file= new File(fileParentPath);
			if(!file.exists()) {
				file.mkdir();
			}
			//需要拷贝的文件
			FileInputStream fis= new FileInputStream(fileOld);
			//拷贝文件到此处
			FileOutputStream fos= new FileOutputStream(fileParentPath+"/"+wrr.getFileName());
			int len=0;
			byte[] buf=new byte[1024];
			while((len=fis.read(buf))!=-1) {
				fos.write(buf,0,len);
			}
			fis.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
