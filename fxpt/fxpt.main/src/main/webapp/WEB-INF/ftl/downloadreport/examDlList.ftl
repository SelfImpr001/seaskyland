<#import "../commons/pageHead.ftl" as pageHead> 
<#import "../commons/querybar.ftl" as querybar>
<#import "../commons/table.ftl" as table>

<div class="page-content">
	<@pageHead.pageHead title="${title!'报表下载'}"/>
	<@querybar.querybar showAngle=true>
	<div class="form-group">

		<label class="col-md-1 col-sm-1 control-label">考试日期</label>
		<div class="col-md-3 col-sm-3">
			<div class="input-group">
				<input class="form-control date-picker myData" id="examData"
					name="examData" type="text"> <span
					class="input-group-addon"><i
					class="icon-calendar bigger-110"></i> </span>
			</div>
		</div>


		<label class="col-md-1 col-sm-1 control-label">考试名称</label>
		<div class="col-md-3 col-sm-3">
			<input type="text" class="form-control myData" id="examName"
				name="examName" placeholder="">
		</div>

		<label class="col-md-1 col-sm-1 control-label">考试简称</label>
		<div class="col-md-3 col-sm-3">
			<input type="text" class="form-control myData" id="examSortName"
				name="examSortName" placeholder="">
		</div>
	</div>
	
<!-- 	<div class="form-group">
			
			<label class="col-md-1 col-sm-1 control-label">信息状态</label>
			<div class="col-md-3 col-sm-3">
				<select class="form-control selectpicker myData" id="examStatus"
					name="examStatus">
					<option value="">全部</option>
					<option value="0">未分析</option>
					<option value="1">分析成功</option>
					<option value="2">分析失败</option>
					<option value="3">发布</option>
				</select>
			</div>
		</div> -->
	</@querybar.querybar>
	
	
	<@table.table tableId="examDlList" head=["考试日期","考试名称","考试简称","操作"]>
		<#include "./examDlListBody.ftl">
	</@table.table>
	
</div>

