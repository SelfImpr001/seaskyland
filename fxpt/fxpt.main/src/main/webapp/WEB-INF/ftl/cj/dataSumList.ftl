<#import "../commons/pageHead.ftl" as pageHead> 
<#import "../commons/querybar.ftl" as querybar>
<#import "../commons/table.ftl" as table>

<div class="page-content">
	<!--页面内容部分-->
	<!-- page-header -->
	<@pageHead.pageHead title="导入数据汇总" />
	<@querybar.querybar showAngle=false>
	<div class="form-group" >



		<label class="col-md-1 col-sm-1 control-label">考试名称</label>
		<div class="col-md-3 col-sm-3">
			<input type="text" class="form-control myData" id="examName"
				name="examName" placeholder="">
		</div>

	</div>
		</@querybar.querybar>

	<@table.table tableId="examList" head=["考试时间","考试名称","报考人数","考试科目","实考人数"]>
			<#include "./dataListBody.ftl">
	</@table.table>
		
</div>

