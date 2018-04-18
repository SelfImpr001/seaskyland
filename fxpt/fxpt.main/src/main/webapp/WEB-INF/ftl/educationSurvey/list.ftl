<!-- 索引栏-->
<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<@breadcrumbs.breadcrumbs currentPath="${title!'教育监测'}">  
<!-- 表单内容-->

<!-- 表单头-->
<#import "../commons/contentTop.ftl" as orgContentTop>
<#assign buttons=[{"text":"新增","id":"addBtn"}] >
<@orgContentTop.contentTop title="${title!'教育监测'}"  buttons=buttons />
<!-- 表单体-->
<#import "../commons/grid.ftl" as orgGrid>
<#assign headers=["指标代码","指标名称","考察点","状态","操作"]>
<#assign rows=[]>
<#assign buttons=[
				  {"type":"text","title":"修改","trigger":"update","class":"icon-edit text-success"},
				  {"type":"text","title":"删除","trigger":"remove","class":"icon-trash text-primary"}]>
<#assign optBtn=[]>
<#if orgList??>
    <#list orgList![] as org>
    
     <#assign typeText="未启用">
      <#if org.available>
      　　　　<#assign typeText="已启用">
      </#if>
      <#assign cells=[org.code!"",org.name!"",(org.searchPoint)!"",typeText!""]>
      <#assign rows=rows+[cells]>
      <#assign optBtn=optBtn+[{"pk":org.pk,"buttons":buttons}]>
    </#list>
</#if>
<@orgGrid.grid  headers=headers rows=rows buttons=optBtn pager=[] showPager=false hasFirstCheckbox=false showSeq=false showTree=true idPreFix="orgTree" hasNested=true>
      <div class="query-form clearfix down" style="overflow: visible;">
        <div class="form-side col-xs-10 col-md-10 col-sm-10">
          <form class="form-horizontal " role="form">
			<div class="form-group  ">
			  <label class="col-md-1 col-sm-1 control-label">指标名称</label>
			  <div class="col-md-11 col-sm-11">
			    <input type="text" class="form-control" name="orgName" placeholder="请输入名称">
			  </div>	  
			</div>            
          </form>
        </div>
        <div class="form-side col-xs-2 col-md-2 col-sm-2">          
          <a class="btn btn-success " herf="javascript:void(0);" title="查询">查询</a>
          <a class="btn btn-success " herf="javascript:void(0);" title="清空">清空</a>          
        </div>
      </div>
</@orgGrid.grid>
</@breadcrumbs.breadcrumbs>