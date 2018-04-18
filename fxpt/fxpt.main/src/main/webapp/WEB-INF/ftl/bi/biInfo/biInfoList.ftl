<div class="page-content">

<#import "/commons/breadcrumbs.ftl" as breadcrumbs>
<#import "/commons/grid.ftl" as biInfoGrid>
<#import "/commons/contentTop.ftl" as biInfoContentTop>
<@breadcrumbs.breadcrumbs currentPath="${title!'BI系统设置'}">
<!-- 表单内容-->
  <#assign buttons=[{"text":"新增","id":"bi_create","class":"btn-primary","closeBtn":false} ] >
  <@biInfoContentTop.contentTop title="${title!'BI系统设置'}" buttons=buttons/>
  <#assign headers=["SmartBI用户","SmartBI访问地址","描述","操作"]>
  <#assign rows=[]>
  <#assign buttons=[					
  					{"type":"text","title":"编辑","trigger":"update","class":"icon-edit text-success"},
  					{"type":"text","title":"清空连接池","trigger":"remove","class":"icon-trash text-primary"}]>
  <#assign optBtn=[]>
     <#if listBiInfo??>
	        	<#list listBiInfo as biInfo>
				  <#assign cells=[biInfo.name,biInfo.url,biInfo.remark]>
				  <#assign rows=rows+[cells]>
				  <#assign optBtn=optBtn+[{"pk":biInfo.id,"buttons":buttons}]>
	        	</#list>
	        </#if>
  <@biInfoGrid.grid  headers=headers rows=rows buttons=optBtn pager=[] showPager=false hasFirstCheckbox=false showSeq=true idPreFix="biInfo"/>
</@breadcrumbs.breadcrumbs>
</div>

