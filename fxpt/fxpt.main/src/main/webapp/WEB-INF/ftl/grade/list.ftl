<div class="page-content">
<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<#import "../commons/grid.ftl" as userGrid>
<#import "../commons/contentTop.ftl" as userContentTop>
<@breadcrumbs.breadcrumbs currentPath="${title!'年级设置'}">
<!-- 表单内容-->
 <#assign buttons=[{"text":"新增","id":"grade_create","class":"btn-primary","closeBtn":false}] >
  <@userContentTop.contentTop title="${title!'年级设置'}" buttons=buttons/>
  <#assign headers=["年级","操作"]>
  <#assign rows=[]>
  <#assign buttons=[					
  					{"type":"text","title":"编辑","trigger":"update","class":"icon-edit text-success"},
  					{"type":"text","title":"删除","trigger":"remove","class":"icon-trash text-primary"}]>
  <#assign optBtn=[]>
     <#if grades??>
	        	<#list grades as grade>
				  <#assign cells=[grade.name]>
				  <#assign rows=rows+[cells]>
				  <#assign optBtn=optBtn+[{"pk":grade.id,"buttons":buttons}]>
	        	</#list>
	        </#if>
  <@userGrid.grid headers=headers rows=rows buttons=optBtn pager=[] showPager=false hasFirstCheckbox=false showSeq=true idPreFix="grade" hasNested=true>
	<div class="query-form clearfix down" style="overflow: visible;">
        <div class="form-side col-xs-10 col-md-10 col-sm-10">
          <form class="form-horizontal " role="form">
			<div class="form-group  ">
			  <label class="col-md-1 col-sm-1 control-label">年级</label>
			  <div class="col-md-11 col-sm-11">
			    <input type="text" class="form-control" name="gradeName" placeholder="请输入年级名称">
			  </div>	  
			</div>            
          </form>
        </div>
        <div class="form-side col-xs-2 col-md-2 col-sm-2">          
           <a class="btn btn-success " herf="javascript:void(0);" title="">查询</a>
           <a class="btn btn-success " herf="javascript:void(0);" title="">清空</a>        
        </div>
      </div>
   </@userGrid.grid >  

</@breadcrumbs.breadcrumbs>
</div >
