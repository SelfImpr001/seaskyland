<#-- 索引栏-->
<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<div class="page-content">
<@breadcrumbs.breadcrumbs currentPath="${title!'角色管理'}">  
    <#-- 表单内容-->
	<#-- 表单头-->
	<#import "../commons/contentTop.ftl" as roleContentTop>
	<#assign buttons=[{"text":"新增","trigger":"newAdd"},{"text":"批量授权","trigger":"batchGrant"}] >
	
	<@roleContentTop.contentTop title="${title!'角色管理'}" buttons=buttons/>
	<#-- 表单体-->
	<#import "../commons/grid.ftl" as roleGrid>
	<#assign headers=["角色编号","角色名称","角色描述","是否有效","角色对应用户数","操作"]>
	
	<#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize,"totalRows":query.totalRows}>
	<#assign rows=[]>
	<#assign buttons=[{"type":"text","title":"授权","trigger":"grant","class":"icon-check text-success"},
					  {"type":"text","title":"成员管理","trigger":"manage","class":"icon-group text-primary"},
					  {"type":"text","title":"修改","trigger":"update","class":"icon-edit text-danger"},
					  {"type":"text","title":"删除","trigger":"remove","class":"icon-trash text-warning"}]>
	<#assign buttons1=[{"type":"text","title":"授权","trigger":"grant","class":"icon-check text-success"},
					   {"type":"text","title":"成员管理","trigger":"manage","class":"icon-group text-primary"},
					   {"type":"text","title":"修改","trigger":"update","class":"icon-edit text-danger"}]>					  
	<#assign optBtn=[]>
	<#if query.results??>
	    <#list query.results as role>
	       <#if role.roleCount != 0>
	           <#assign cells=[role.code!"",role.name!"",role.desc!"",role.available?string("有效","无效"),"<a href='javascript:void(0);' trigger='userlist' pk='"+role.pk+"' count='"+role.roleCount+"'>"+role.roleCount+"</a>"]>
	       <#else>
	          <#assign cells=[role.code!"",role.name!"",role.desc!"",role.available?string("有效","无效")," "]>
	      </#if>
	      <#assign rows=rows+[cells]>
	      <#if role.type = 2>
	        <#assign optBtn=optBtn+[{"pk":role.pk,"buttons":buttons}]>
	      <#else>
	        <#assign optBtn=optBtn+[{"pk":role.pk,"buttons":buttons1}]>
	      </#if>
	    </#list>
	</#if>
	<@roleGrid.grid  headers=headers rows=rows buttons=optBtn pager=pager showPager=true hasFirstCheckbox=false showSeq=true idPreFix="role"/>
</@breadcrumbs.breadcrumbs>
</div>