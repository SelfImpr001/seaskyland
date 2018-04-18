<div class="page-content">
<#assign name="系统">
<#if type=="menu">
	<#assign name="菜单">
<#elseif type=="data">
	<#assign name="数据权限">
<#elseif type=="module">
	<#assign name="报告">
</#if>
<!-- 索引栏-->
<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<@breadcrumbs.breadcrumbs currentPath="资源管理">  
<!-- 表单内容-->
<input type="hidden" name="type" value="${type!}" id="menuType">
<!-- 表单头-->
<#import "../commons/contentTop.ftl" as contentTop>
<#assign buttons=[{"text":"新增","id":"addBtn","name":"addMenu","trigger":"create"}] >
<@contentTop.contentTop title="${name}管理" buttons=buttons/>
<!-- 表单体-->
<#import "../commons/grid.ftl" as grid>
<#assign headers=["${name}名称","上级","状态","描述","操作"]>
<#assign rows=[]>
<#assign buttons=[
				  {"type":"text","title":"修改","name":"update","trigger":"update","class":"icon-edit text-success"},
				  {"type":"text","title":"删除","name":"delete","trigger":"remove","class":"icon-trash text-primary"}]>
<#assign optBtn=[]>
<#if resList??>
    <#list resList![] as data>
      <#assign cells=[data.name!"",(data.parent.name)!,data.available?string("有效","无效"),data.remarks!""]>
      <#assign rows=rows+[cells]>
      <#assign optBtn=optBtn+[{"pk":data.pk,"buttons":buttons}]>
    </#list>
</#if>
<@grid.grid headers=headers rows=rows buttons=optBtn pager=[] showPager=false hasFirstCheckbox=false showSeq=true showTree=true idPreFix="${type!}"/>
</@breadcrumbs.breadcrumbs>

</div>



