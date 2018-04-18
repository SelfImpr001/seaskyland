<div class="page-content">
<#import "/commons/breadcrumbs.ftl" as breadcrumbs>
<#import "/commons/grid.ftl" as biUserGrid>
<#import "/commons/contentTop.ftl" as biUserContentTop>
<@breadcrumbs.breadcrumbs currentPath="${title!'BI用户设置'}">
<!-- 表单内容-->
  <#assign buttons=[{"text":"新增","id":"bi_create","class":"btn-primary","closeBtn":false} ] >
  <@biUserContentTop.contentTop title="${title!'BI用户设置'}" buttons=buttons/>
  <#assign headers=["SmartBI用户","用户","操作"]>
  <#assign rows=[]>
  <#assign buttons=[					
  					{"type":"text","title":"编辑","trigger":"update","class":"icon-edit text-success"},
  					{"type":"text","title":"删除","trigger":"remove","class":"icon-trash text-primary"}]>
  <#assign optBtn=[]>
     <#if listBiUser??>
	        	<#list listBiUser as biUser>
				  <#assign cells=[biUser.biInfo.name,biUser.userName]>
				  <#assign rows=rows+[cells]>
				  <#assign optBtn=optBtn+[{"pk":biUser.id,"buttons":buttons}]>
	        	</#list>
	        </#if>
  <@biUserGrid.grid  headers=headers rows=rows buttons=optBtn pager=[] showPager=false hasFirstCheckbox=false showSeq=true idPreFix="biUser"/>
</@breadcrumbs.breadcrumbs>

</div>

