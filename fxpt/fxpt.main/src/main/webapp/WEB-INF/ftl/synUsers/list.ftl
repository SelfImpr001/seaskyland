<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<#import "../commons/grid.ftl" as userGrid>
<#import "../commons/contentTop.ftl" as userContentTop>
<@breadcrumbs.breadcrumbs currentPath="${title!'同步用户设置'}">
<!-- 表单内容-->
  <#assign buttons=[{"text":"增加同步用户设置","id":"addSynUsers","class":"btn-primary","closeBtn":false},{"text":"导出同步用户数据","id":"downSynUsers","class":"btn-primary","closeBtn":false}] >
  <@userContentTop.contentTop title="${title!'同步用户设置'}" buttons=buttons/>
  <#assign headers=["用户对应角色","用户命名规则","密码规则","数据发布时自动同步","操作"]>
  <#assign rows=[]>
  <#assign buttons=[					
  					{"type":"text","title":"编辑","trigger":"update","class":"icon-edit text-success"},
  					{"type":"text","title":"删除","trigger":"remove","class":"icon-trash text-primary"}]>
  <#assign optBtn=[]>
    	 <#if list??>
			      <#list list as syn>
				       <#assign rolename><#list listRole as role><#if (syn.roleid==role.pk)>${role.name}</#if></#list></#assign>
				        <#assign namingrules>
				       			<#list syn.nr as n> 
						      		<#if n=='provinceCode'>省
						      		<#elseif n=='cityCode'>市
						      		<#elseif n=='countyCode'>区
						      		<#elseif n=='schoolCode'>学校
						      		<#elseif n=='gradeCode'>年级
						      		<#elseif n=='classCode'>班级
						      		<#elseif n=='subjectCode'>学科
						      		</#if>
					      		</#list> 
					     </#assign>		
					      <#assign passwordrules>
					     	<#if (syn.passwordrules=='any')>
				       			   随机产生
				       		<#else>
				       			    固定密码 :${syn.defaultpassword!}
				       		</#if>
				       	  </#assign>
				       	  <#assign syns><#if (syn.isSyn==1)>启用<#else>停用</#if></#assign>
					  <#assign cells=[rolename,namingrules,passwordrules,syns]>
					  <#assign rows=rows+[cells]>
					  <#assign optBtn=optBtn+[{"pk":syn.id,"buttons":buttons}]>
	        	</#list>
	     </#if>
  <@userGrid.grid headers=headers rows=rows buttons=optBtn pager=[] showPager=false hasFirstCheckbox=false showSeq=false idPreFix="subject" hasNested=true>
   </@userGrid.grid >  
</@breadcrumbs.breadcrumbs>

