/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.fxpt.report;

import org.springframework.beans.factory.annotation.Autowired;

import com.cntest.foura.domain.User;
import com.cntest.foura.service.UserService;
import com.cntest.security.RoleType;
import com.cntest.security.UserDetails;
import com.cntest.util.SpringContext;

/** 
 * <pre>
 * 
 * </pre>
 *  
 * @author 李贵庆2014年12月19日
 * @version 1.0
 **/
public class ReportQueryFactory {
	
	
	public static  ReportQuery getReportQuery(UserDetails user,String orgTypes) throws ReportQueryUndefinedException{		
		if(user.roleOf(RoleType.school) || (orgTypes!="" && "4".indexOf(orgTypes)>-1))
			return SpringContext.getBean(RoleType.school+"ReportQuery");
		
		if(user.roleOf(RoleType.city) || (orgTypes!="" && "2".indexOf(orgTypes)>-1))
			return SpringContext.getBean(RoleType.city+"ReportQuery");
		
		if(user.roleOf(RoleType.county) || (orgTypes!="" && "3".indexOf(orgTypes)>-1))
			return SpringContext.getBean(RoleType.county+"ReportQuery");
		
		if(user.roleOf(RoleType.province) || (orgTypes!="" && "1".indexOf(orgTypes)>-1))
			return SpringContext.getBean(RoleType.province+"ReportQuery");
		
		if(user.roleOf(RoleType.student))
			return SpringContext.getBean(RoleType.student+"ReportQuery");
		else{
			try {
				return SpringContext.getBean(RoleType.school+"ReportQuery");
			} catch (Exception e) {
				 throw new ReportQueryUndefinedException("cntest-0001", "用户" + user.getUserName()+ "未定义角色！");
			}
		}
	}
}

