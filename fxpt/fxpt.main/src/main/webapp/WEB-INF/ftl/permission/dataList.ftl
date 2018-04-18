
<#assign name="数据权限">
<!-- 索引栏-->
<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<@breadcrumbs.breadcrumbs currentPath="资源管理">  
<#import "../commons/contentTop.ftl" as contentTop>
<#assign buttons=[{"text":"新增","id":"addBtn","name":"addMenu","trigger":"create"}] >
<@contentTop.contentTop title="${name}管理" buttons=buttons/>
<!-- 表单体-->
<#import "../commons/grid.ftl" as grid>
<#assign headers=["名称","外部参数名称","数据来源","权限名称字段","权限值字段","操作"]>
<#assign rows=[]>
<#--{"type":"text","title":"查看","name":"delete","trigger":"view","class":"btn btn-xs btn-danger remove"}-->
<#assign buttons1=[
				  {"type":"text","title":"修改","name":"update","trigger":"update","class":"icon-edit text-success"},
				  {"type":"text","title":"删除","name":"delete","trigger":"remove","class":"icon-trash text-primary"},
				  {"type":"text","title":"停用","name":"delete","trigger":"disabled","class":"icon-circle-blank text-danger"}]>
<#assign buttons2=[
				  {"type":"text","title":"修改","name":"update","trigger":"update","class":"icon-edit text-success"},
				  {"type":"text","title":"删除","name":"delete","trigger":"remove","class":"icon-trash text-primary"},
				  {"type":"text","title":"启用","name":"delete","trigger":"enabled","class":"icon-circle text-danger"}]>				  
<#assign optBtn=[]>
<#if results??>
    <#list results![] as data>
      <#assign cells=[data.name!"",(data.paramName)!"",(data.table)!"",(data.paramNamefield)!"",(data.paramValueField)!""]>
      <#assign rows=rows+[cells]>
      <#if data.status=1>
        <#assign optBtn=optBtn+[{"pk":data.pk,"buttons":buttons1}]>
      <#else>
        <#assign optBtn=optBtn+[{"pk":data.pk,"buttons":buttons2}]>
      </#if>
    </#list>
</#if>
<@grid.grid headers=headers rows=rows buttons=optBtn pager=[] showPager=false hasFirstCheckbox=false showSeq=true showTree=true idPreFix="dataPermission"/>
</@breadcrumbs.breadcrumbs>