
<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<#import "../commons/grid.ftl" as reportGrid>
<#import "../commons/contentTop.ftl" as userContentTop>
<div class="page-content">
<@breadcrumbs.breadcrumbs currentPath="birt组织管理">
<!-- 表单内容-->
  <#assign buttons=[{"text":"新增","id":"addBtn"}] >
  <@userContentTop.contentTop title="birt组织管理" buttons=buttons/>
  
  <#assign headers=["组织名称","操作"]>
  <#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize}>
  <#assign rows=[]>
  
  <#assign buttons1=[ 				
  					{"type":"text","title":"修改脚本","trigger":"update","class":"btn btn-xs btn-info mr-2 "}, 				
  					{"type":"text","title":"删除","trigger":"remove","class":"btn btn-xs btn-info mr-2 "}
  					
  					]>
  					
  					
  <#assign optBtn=[]>
    <#assign parentName="-">
  <#if query.results??>
    <#list query.results as report>
    <#if report.parent??>
    <#assign parentName=report.parent.name>
    </#if>
      <#assign cells=[report.pk!"",report.name!""]>
      <#assign rows=rows+[cells]>
       <#assign optBtn=optBtn+[{"pk":report.pk,"buttons":buttons1}]>
    </#list>
  </#if>
  <@reportGrid.grid  headers=headers rows=rows hasNested=true
    buttons=optBtn pager=pager showTree=false showPager=true hasFirstCheckbox=true showSeq=false idPreFix="birtorg">
   
  </@reportGrid.grid>
</@breadcrumbs.breadcrumbs>
</div>