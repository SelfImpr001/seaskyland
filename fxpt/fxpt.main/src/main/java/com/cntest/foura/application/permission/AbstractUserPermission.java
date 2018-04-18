/**
 * <p><b>© 1997-2014 深圳市海云天教育测评有限公司 TEL: (86)755 - 86024188</b></p>
 * 
 **/

package com.cntest.foura.application.permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.cntest.foura.domain.URLResource;
import com.cntest.foura.domain.User;
import com.cntest.foura.service.URLResourceService;
import com.cntest.fxpt.domain.Exam;
import com.cntest.security.UserResource;
import com.cntest.security.remote.IUserResourceService.Level;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author 李贵庆2014年6月21日
 * @version 1.0
 **/
public abstract class AbstractUserPermission  implements UserPermisson{
	
	private URLResourceService urlResourceService;

	private Level myLevel;
	
	protected void setMyLevel(Level level) {
		this.myLevel = level;
	}

	protected URLResourceService getUrlResourceService() {
		return urlResourceService;
	}

	protected void setUrlResourceService(URLResourceService urlResourceService) {
		this.urlResourceService = urlResourceService;
	}

	protected Level getMyLevel() {
		return myLevel;
	}
	
    @Override
	public UserPermisson inject(URLResourceService urlResourceService) {
		setUrlResourceService(urlResourceService);
		return this;
	}

    @Override
	public UserPermisson with(Level level) {
		setMyLevel(level);
		return this;
	}
    
    protected List<UserResource> toUserResource(List<URLResource> reses,User user, String uuid,Exam exam){
    	if(this.getMyLevel().equals(Level.FIRST))
		    return toUserResource(reses,user,uuid,false,exam);
		return toUserResources(reses,user,uuid,true,exam);
    }
    //迭代树形菜单方法变更
    protected List<UserResource> toUserResources(List<URLResource> reses,User user, String uuid,boolean copyChildren,Exam exam) {
    	String gradeid = "";
		String examtypeid = "";
    	ArrayList<UserResource> myReses = new ArrayList<UserResource>();
		List<URLResource> urls = urlResourceService.list();
		List<URLResource> persUrl =this.urlResourceService.findPesMenus(user);
		for (URLResource r : reses) {
			UserResource ur = r.toUserResource(false);
			if(exam==null){
				myReses.add(ur);
			}else{
				gradeid = r.getGradeids();
				examtypeid = r.getExamtypeids();
				if(gradeid!=null&&examtypeid!=null){
					if(gradeid.contains(exam.getGrade().getId()+",")&& examtypeid.contains(exam.getExamType().getId()+",")){
						myReses.add(ur);
					}
				}
			
			}
			if(copyChildren){
				//递归找出菜单栏
				setUrl(urls, r,ur,persUrl,exam);
			}
		}
		Collections.sort(myReses, new Comparator<UserResource>() {

			@Override
			public int compare(UserResource o1, UserResource o2) {
				// TODO Auto-generated method stub
				return o1.getOrder().compareTo(o2.getOrder());
			}});
    	return myReses;
    	
    }
    protected void setUrl(List<URLResource> urls, URLResource turl, UserResource ur, List<URLResource> persUrls,Exam exam) {
		String gradeid = "";
		String examtypeid = "";
		if (urls != null)
			for (URLResource urlResource : urls) {
				if (urlResource.getParent() != null)
					if (turl.getPk() == urlResource.getParent().getPk()) {
						for (URLResource persUrl : persUrls) {
							if (urlResource.getPk() == persUrl.getPk()) {
								UserResource resChildren = urlResource.toUserResource(false);
								if (resChildren != null) {
									setUrl(urls, urlResource, resChildren, persUrls,exam);
									gradeid = persUrl.getGradeids();
									examtypeid = persUrl.getExamtypeids();
									if(gradeid!=null&&examtypeid!=null&&exam!=null){
										if(gradeid.contains(exam.getGrade().getId()+",")&& examtypeid.contains(exam.getExamType().getId()+",")){
											ur.addChild(resChildren);
										}
									}else{
										ur.addChild(resChildren);
									}
								}
							}
						}

					}
			}
	}
    protected List<UserResource> toUserResource(List<URLResource> reses,User user, String uuid,boolean copyChildren,Exam exam){
		String gradeid = "";
		String examtypeid = "";
    	
    	ArrayList<UserResource> myReses = new ArrayList<UserResource>();
    	
    	if(reses != null) {
    		for(URLResource r:reses) {
    			
    			UserResource ur = r.toUserResource(false);
    			if(exam==null){
    				myReses.add(ur);
    			}else{
    				URLResource  urlRe=	urlResourceService.getResourceByUuid(ur.getUuid());
					gradeid = urlRe.getGradeids();
					examtypeid = urlRe.getExamtypeids();
					if(gradeid!=null&&examtypeid!=null){
						if(gradeid.contains(exam.getGrade().getId()+",")
								&& examtypeid.contains(exam.getExamType().getId()+",")){
							myReses.add(ur);
						}
					}
    			}
    			
    			if(copyChildren) {
    				List<URLResource> resChildren = this.urlResourceService.getModulesFor(user, r);
    				if(resChildren != null && resChildren.size() >0) {
    					List<UserResource> children = toUserResource(resChildren,user,uuid,copyChildren,exam);
    					for(UserResource child:children) {
    						ur.addChild(child);
    					}    					
    				}
    			}
    		}
    	}
    	Collections.sort(myReses, new Comparator<UserResource>() {

			@Override
			public int compare(UserResource o1, UserResource o2) {
				// TODO Auto-generated method stub
				return o1.getOrder().compareTo(o2.getOrder());
			}});
    	
    	return myReses;
    }
    
	
}
