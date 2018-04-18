<div class="page-content">
<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<#import "../commons/grid.ftl" as userGrid>
<#import "../commons/contentTop.ftl" as userContentTop>

<@breadcrumbs.breadcrumbs currentPath="个人报告">
<!-- 表单内容-->
  <#assign buttons=[] >
  <@userContentTop.contentTop title="个人报告管理" buttons=buttons/>
  
  <#assign headers=["考试日期","考试名称","考试简称","地区","操作"]>
  <#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize}>
  <#assign rows=[]>
  
  <#assign buttons=[{"type":"text","title":"生成个人报告","trigger":"batchExec","class":"icon-book text-success"},
  					{"type":"text","title":"重置","trigger":"batchReset","class":"icon-refresh text-primary"},
  					{"type":"text","title":"详情","batchInfo":"batchExec","class":" icon-search text-danger"}]>  		
  <#assign optBtn = []>
  <#if query.list??>
    <#list query.list as exam>
	
	
	
      <#assign cells=[(exam.createDate!'')?string("yyyy-MM-dd"), exam.name!'',exam.sortName!'',exam.ownerName!'']>
	  
	  
      <#assign rows=rows+[cells]>
      
     	<#if exam.status==11>
			<#assign buttons=[{"type":"text","title":"<span class='float_left'>正在生成</span><div class='progress-warp float_left' examId='${exam.id}'></div>","trigger":"","class":"btn btn-warning mr-2 "}]>  	
		<#else>
			<#assign buttons=[{"type":"text","title":"生成个人报告","trigger":"batchExec","class":"icon-book text-success"},
  					{"type":"text","title":"重置","trigger":"batchReset","class":" icon-refresh text-primary"},
  					{"type":"text","title":"详情","trigger":"batchInfo","class":"icon-search text-danger"}]>   
		</#if>
      
      <#assign optBtn=optBtn+[{"pk":exam.id!'',"buttons":buttons}]>
    </#list>
  </#if>
  <@userGrid.grid  headers=headers rows=rows hasNested=true buttons=optBtn pager=pager showPager=true hasFirstCheckbox=false showSeq=true idPreFix="exam">
  </@userGrid.grid>
  
</@breadcrumbs.breadcrumbs>
</div>