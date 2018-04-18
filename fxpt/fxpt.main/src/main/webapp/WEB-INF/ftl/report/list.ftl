<!-- 索引栏-->
<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<@breadcrumbs.breadcrumbs currentPath="组织管理">  
<!-- 表单内容-->

<!-- 表单头-->
<#import "../commons/contentTop.ftl" as orgContentTop>
<#assign buttons=[{"text":"新增","id":"addBtn"}] >
<@orgContentTop.contentTop title="组织管理" buttons=buttons/>
<!-- 表单体-->
<#import "../commons/grid.ftl" as orgGrid>
<#assign headers=["组织名称","组织代号","上级组织","是否有效","操作"]>
<#assign rows=[]>
<#assign buttons=[
				  {"type":"icon-edit","title":"编辑","trigger":"update","class":"icon-edit text-success"},
				  {"type":"icon-trash","title":"删除","trigger":"remove","class":"icon-trash text-primary"}]>
<#assign optBtn=[]>
<#if orgList??>
    <#list orgList![] as org>
      <#assign cells=[org.name!"",org.code!"",(org.parent.name)!,"有效"]>
      <#assign rows=rows+[cells]>
      <#assign optBtn=optBtn+[{"pk":org.pk,"buttons":buttons}]>
    </#list>
</#if>
<@orgGrid.grid headers=headers rows=rows buttons=optBtn pager=[] showPager=false hasFirstCheckbox=false showSeq=true showTree=true/>

</@breadcrumbs.breadcrumbs>