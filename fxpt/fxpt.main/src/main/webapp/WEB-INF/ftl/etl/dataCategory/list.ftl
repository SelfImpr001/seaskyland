<#import "/commons/breadcrumbs.ftl" as breadcrumbs>
<#import "/commons/grid.ftl" as dataCategoryGuid>
<#import "/commons/contentTop.ftl" as dataCategoryContentTop>
<@breadcrumbs.breadcrumbs currentPath="导入数据项管理">
<!-- 表单内容-->
  <#assign buttons=[{"text":"新增","id":"cate_create","class":"btn-primary","closeBtn":false} ] >
  <@dataCategoryContentTop.contentTop title="导入数据项管理" buttons=buttons/>
  <#assign headers=["名称","对应的表","操作"]>
  <#assign rows=[]>
  <#assign buttons=[					
  					{"type":"text","title":"编辑","trigger":"update","class":"icon-edit text-success"},
  					{"type":"text","title":"删除","trigger":"remove","class":"icon-trash text-primary"}]>
  <#assign optBtn=[]>
     <#if dataCategorys??>
	        	<#list dataCategorys as dataCategory>
				  <#assign cells=[dataCategory.name,dataCategory.tableName!"" ]>
				  <#assign rows=rows+[cells]>
				  <#assign optBtn=optBtn+[{"pk":dataCategory.id,"buttons":buttons}]>
	        	</#list>
	        </#if>
  <@dataCategoryGuid.grid  headers=headers rows=rows buttons=optBtn pager=[] showPager=false hasFirstCheckbox=false showSeq=true idPreFix="dataCategory"/>
</@breadcrumbs.breadcrumbs>

