/**
 * <p><b>© 1997-2013 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.repository;

import java.util.List;

import com.cntest.common.query.Query;
import com.cntest.common.repository.Repository;
import com.cntest.fxpt.domain.EducationSurvey;
import com.cntest.fxpt.domain.FSDataNnalysis;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年6月5日
 * @version 1.0
 **/
public interface EducationSurveyRepository extends Repository<EducationSurvey, Long> {
	/**
	 * 获取所有没有上级的组织
	 * @return
	 */
	List<EducationSurvey> listByParentIsNull(String name);
	/**
	 * 获取组织的下级
	 * @param pk 上级id
	 * @return
	 */
	List<EducationSurvey> listByParentFor(Long pk,String name);
	/**
	 * 根据pk值，获取数据
	 */

	public EducationSurvey getEduByPk(Long pk);
	
	public void savaEdu(EducationSurvey edu);
	
	public int findOrgList();
	
	void listByParentFor(Long pk, Query<EducationSurvey> query);
	
	List<EducationSurvey> selectChildrenNotLeafFor(Long parentPk);
	
	//获取下载内容--路径
		FSDataNnalysis getFSDataNnalysisById(Long id);
}

