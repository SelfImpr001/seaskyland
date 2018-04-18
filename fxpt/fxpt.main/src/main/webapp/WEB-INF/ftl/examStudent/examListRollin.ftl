<#import "../commons/pageHead.ftl" as pageHead> 
<#import "../commons/querybar.ftl" as querybar>
<#import "../commons/table.ftl" as table>
<div class="page-content">
  <input type="hidden" name="examId" value="${RequestParameters["examId"]!""}" id="examId">
  <#assign title="转入考生信息到：" + RequestParameters["targetExamName"]!"" >
  <@pageHead.pageHead title=title buttons=[{"text":"<<返回学生信息导入列表","type":"button","icon":"icon-reply"}] type="button"/>
  <@querybar.querybar>
	<div class="form-group">
		<label class="col-md-1 col-sm-1 control-label">考试日期</label>
		<div class="col-md-3 col-sm-3">
			<div class="input-group">
				<input class="form-control date-picker myData" id="examData" name="examData" type="text"> 
				<span class="input-group-addon"><i class="icon-calendar bigger-110"></i> </span>
			</div>
		</div>

		<label class="col-md-1 col-sm-1 control-label">考试名称</label>
		<div class="col-md-3 col-sm-3">
			<input type="text" class="form-control myData" id="examName" name="examName" placeholder="">
		</div>

		<label class="col-md-1 col-sm-1 control-label">考试简称</label>
		<div class="col-md-3 col-sm-3">
			<input type="text" class="form-control myData" id="examSortName" name="examSortName" placeholder="">
		</div>
	</div>
  </@querybar.querybar>
  <@table.table tableId="examRonllinList" head=["考试日期","考试名称","考试简称","信息状态","导入学生状态","操作"]>
    <#include "./examRollinListBody.ftl">
  </@table.table>
</div>
