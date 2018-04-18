<#-- 索引栏-->
<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<@breadcrumbs.breadcrumbs currentPath="${title!'监测管理'}">  
    <#-- 表单内容-->
	<#-- 表单头-->
	<#import "../commons/contentTop.ftl" as roleContentTop>
	<#assign buttons=[{"text":"新增","trigger":"newAdd"}] >
	<@roleContentTop.contentTop title="${title!'监测管理'}" buttons=buttons/>
	<#-- 表单体-->
	<#import "../commons/grid.ftl" as roleGrid>
	<#assign headers=["监测日期","名称","分析类型","组织机构","创建人","信息状态","操作"]>
	
	<#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize,"totalRows":query.totalRows}>
	<#assign rows=[]>
	<#assign buttons=[{"type":"text","title":"修改","trigger":"update","class":"icon-edit text-success"},
				  	{"type":"text","title":"删除","trigger":"remove","class":"icon-trash text-primary"}]>
	<#assign optBtn=[]>
	
	<#if query.results??>
	    <#list query.results as role>
	      <#assign cells=[(role.monitorDate!"")?string('YYYY-MM-dd'),(role.questionName!''),role.analysisType!"",role.institutions!"",role.createUser!'','未发布']>
	      <#assign rows=rows+[cells]>
	      <#assign optBtn=optBtn+[{"pk":role.pk,"buttons":buttons}]>
	    </#list>
	</#if>
	<@roleGrid.grid  headers=headers rows=rows buttons=optBtn pager=pager showPager=true hasFirstCheckbox=false showSeq=fa idPreFix="role"/>
</@breadcrumbs.breadcrumbs>
 