<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<#import "../commons/grid.ftl" as reportGrid>
<#import "../commons/contentTop.ftl" as userContentTop>
<div class="page-content">
<@breadcrumbs.breadcrumbs currentPath="脚本管理">
<!-- 表单内容-->
  <#assign buttons=[{"text":"导入脚本","trigger":"create"},{"text":"合成脚本","trigger":"batchGrant"}] >
  <@userContentTop.contentTop title="用户管理" buttons=buttons/>
  
  <#assign headers=["脚本名称","脚本来源","存放地址","备注","生成word文件名","操作"]>
  <#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize}>
  <#assign rows=[]>
  
  <#assign buttons1=[{"type":"text","title":"生成word","trigger":"permission","class":"btn btn-xs btn-success mr-2 "}, 	
  					{"type":"text","title":"下载word","trigger":"download","class":"btn btn-xs btn-success mr-2 "}, 				
  					{"type":"text","title":"修改脚本","trigger":"update","class":"btn btn-xs btn-info mr-2 "}, 				
  					{"type":"text","title":"删除","trigger":"remove","class":"btn btn-xs btn-info mr-2 "}]>
  					
  					
  <#assign optBtn=[]>
  <#if query.results??>
    <#list query.results as report>
      <#assign cells=[report.pk!"",report.name!"",report.source!"",report.directory!"",report.remark!"",report.wordName!"-"]>
      <#assign rows=rows+[cells]>
       <#assign optBtn=optBtn+[{"pk":report.pk,"buttons":buttons1}]>
    </#list>
  </#if>
  <@reportGrid.grid  headers=headers rows=rows hasNested=true
    buttons=optBtn pager=pager showPager=true hasFirstCheckbox=true showSeq=false idPreFix="user">
      <div class="query-form clearfix down" style="overflow: visible;">
        <div class="form-side col-xs-10 col-md-10 col-sm-10">
          <form class="form-horizontal " role="form">
			<div class="form-group  ">
			  <div class="col-md-12 col-sm-12">
			    <input type="text" class="form-control" name="q1" placeholder="请输入查询条件">
			  </div>	  
			</div>            
          </form>
        </div>
        <div class="form-side col-xs-2 col-md-2 col-sm-2">          
          <a class="btn btn-success " herf="javascript:void(0);" title="查询">查询</a>
          <a class="btn btn-success " herf="javascript:void(0);" title="重置">清空</a>          
        </div>
      </div>  
  </@reportGrid.grid>
</@breadcrumbs.breadcrumbs>
</div>