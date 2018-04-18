<div class="page-content">
<!-- 索引栏-->
<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<@breadcrumbs.breadcrumbs currentPath="${title!'应用管理'}">  
<!-- 表单内容-->
<div class="page-content">
<input type="hidden" name="type" value="${type!}" id="menuType">
<!-- 表单头-->
<#import "../commons/contentTop.ftl" as contentTop>
<#assign buttons=[] >
<@contentTop.contentTop title="${title!'应用管理'}" buttons=buttons/>
<!-- 表单体-->
<#import "../commons/grid.ftl" as grid>
<#assign headers=["系统名称","描述","操作"]>
<#assign rows=[]>
<#assign buttons=[
				  {"type":"text","title":"修改","name":"update","trigger":"update","class":"icon-edit text-success"}]>
<#assign optBtn=[]>
<#if resList??>
    <#list resList![] as data>
      <#assign cells=[data.name!"",data.remarks!""]>
      <#assign rows=rows+[cells]>
      <#assign optBtn=optBtn+[{"pk":data.pk,"buttons":buttons}]>
    </#list>
</#if>
<@grid.grid headers=headers rows=rows buttons=optBtn pager=[] showPager=false hasFirstCheckbox=false showSeq=true idPreFix="app"/>
</@breadcrumbs.breadcrumbs>
</div>