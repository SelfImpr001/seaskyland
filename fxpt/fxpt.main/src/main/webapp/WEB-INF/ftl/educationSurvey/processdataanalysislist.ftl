<#import "../commons/pageHead.ftl" as pageHead> 
<#import "../commons/querybar.ftl" as querybar>
<#import "../commons/table.ftl" as table>

<div class="page-content">
	<@pageHead.pageHead title="关联分析"/>
	<@querybar.querybar showAngle=false>
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


		<label class="col-md-1 col-sm-1 control-label">名称</label>
		<div class="col-md-3 col-sm-3">
			<input type="text" class="form-control myData" id="reportyear"
				name="reportyear" placeholder="">
		</div>

		<label class="col-md-1 col-sm-1 control-label">简称</label>
		<div class="col-md-3 col-sm-3">
			<input type="text" class="form-control myData" id="reportyear"
				name="reportyear" placeholder="">
		</div>
	</div>
	
	<div class="form-group">
			
			<label class="col-md-1 col-sm-1 control-label">信息状态</label>
			<div class="col-md-3 col-sm-3">
				<select class="form-control selectpicker myData" id="reporttype"
					name="reporttype">
					<option value="">全部</option>
					<option value="0">已发布</option>
					<option value="1">已结束</option>
					<option value="2">未发布</option>
					<option value="2">停止</option>
				</select>
			</div>
		</div>
	</@querybar.querybar>
	
	
	<@table.table tableId="examList" head=["日期","名称","信息状态","问卷数据采集","导入学生信息","导入细目表","导入成绩","操作"]>
		<#include "processdataanalysisBody.ftl">
	</@table.table>
</div>

