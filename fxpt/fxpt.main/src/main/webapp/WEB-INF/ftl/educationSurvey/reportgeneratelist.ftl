<#import "../commons/pageHead.ftl" as pageHead> 
<#import "../commons/querybar.ftl" as querybar>
<#import "../commons/table.ftl" as table>

<div class="page-content">
	<@pageHead.pageHead title="监测报告列表"/>
	<@querybar.querybar showAngle=true>
	<div class="form-group">

		<label class="col-md-1 col-sm-1 control-label">日期</label>
		<div class="col-md-3 col-sm-3">
			<div class="input-group">
				<input class="form-control date-picker myData" id="reportdata"
					name="reportdata" type="text" placeholder="2016-09-08"> <span
					class="input-group-addon"><i
					class="icon-calendar bigger-110"></i> </span>
			</div>
		</div>


		<label class="col-md-1 col-sm-1 control-label">学年</label>
		<div class="col-md-3 col-sm-3">
			<input type="text" class="form-control myData" id="reportyear"
				name="reportyear" placeholder="请输入学年,如:2016">
		</div>

		<label class="col-md-1 col-sm-1 control-label">学期</label>
			<div class="col-md-3 col-sm-3">
				<select class="form-control selectpicker myData" id="reportsemester"
					name="reportsemester">
					<option value="">全部</option>
					<option value="上学期">上学期</option>
					<option value="下学期">下学期</option>
				</select>
			</div>
	</div>
	
	<div class="form-group">
			
			<label class="col-md-1 col-sm-1 control-label">类型</label>
			<div class="col-md-3 col-sm-3">
				<select class="form-control selectpicker myData" id="reporttype"
					name="reporttype">
					<option value="">全部</option>
					<option value="学业检测">学业检测</option>
					<option value="综合素质监测">综合素质监测</option>
					<option value="学生成长环境监测">学生成长环境监测</option>
				</select>
			</div>
		</div>
	</@querybar.querybar>
	
	
	<@table.table tableId="reportList" head=["名称","学年","学期","类型","日期","操作"]>
		<#include "reportgenerateBody.ftl">
	</@table.table>
	
</div>

