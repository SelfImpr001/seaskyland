/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.service;


import java.util.List;

import com.cntest.common.page.Page;
import com.cntest.common.query.Query;
import com.cntest.common.service.EntityService;
import com.cntest.fxpt.domain.EducationMonitor;
import com.cntest.fxpt.domain.EducationSurvey;
import com.cntest.fxpt.domain.Exam;
import com.cntest.fxpt.domain.FSDataNnalysis;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 肖 肖 2014年6月18日
 * @version 1.0
 **/
public interface EducationSurveyService extends EntityService<EducationSurvey,Long>{

	/**
	 * 获取所有顶级组织
	 * @return
	 */
	List<EducationSurvey> getTopEduList(String orgName);

	/**
	 * 根据pk 获取该组织的下级组织
	 * @param valueOf
	 * @return
	 */
	List<EducationSurvey> getEduSubList(Long valueOf);

	/**
	 * 根据pk获取组织
	 * @param valueOf
	 * @return
	 */
	EducationSurvey getEduByPk(Long valueOf);
	
	public void savaEdu(EducationSurvey edu);
	
	public int findOrgList();
	
	List<EducationSurvey> getEduSubList(Long pk, String name);

	void getEduSubList(Long pk, Query<EducationSurvey> query);
	
	List<EducationSurvey> getEduChildrenNoLeaf(Long pk);
	
	public List<FSDataNnalysis> reportgeneratelist(Page<FSDataNnalysis> page);
	
	void list(Long pk,Query<FSDataNnalysis> query);
	
	EducationMonitor get(Long pk);
	
	void deletem(Long pk);
	
	void updatem(EducationMonitor m);
	
	void savem(EducationMonitor m );
	
	//考试数据上传--结果导出增加验证
	void  saveReportData(FSDataNnalysis fsdn);
	//获取下载内容--路径
	FSDataNnalysis getFSDataNnalysisById(Long id);
	
}

