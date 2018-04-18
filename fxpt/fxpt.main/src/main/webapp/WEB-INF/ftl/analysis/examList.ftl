<#import "../commons/pageHead.ftl" as pageHead> 
<#import "../commons/querybar.ftl" as querybar>
<#import "../commons/table.ftl" as table>

<div class="page-content">
	<@pageHead.pageHead title="${title!'数据分析'}"/>
	<@querybar.querybar showAngle=false>
	<div class="form-group">

		<label class="col-md-1 col-sm-1 control-label">考试日期</label>
		<div class="col-md-2 col-sm-2">
			<div class="input-group">
				<input class="form-control date-picker myData" id="examData"
					name="examData" type="text"> <span
					class="input-group-addon"><i
					class="icon-calendar bigger-110"></i> </span>
			</div>
		</div>


		<label class="col-md-1 col-sm-1 control-label">考试名称</label>
		<div class="col-md-2 col-sm-2">
			<input type="text" class="form-control myData" id="examName"
				name="examName" placeholder="">
		</div>

		<label class="col-md-1 col-sm-1 control-label">考试简称</label>
		<div class="col-md-2 col-sm-2">
			<input type="text" class="form-control myData" id="examSortName"
				name="examSortName" placeholder="">
		</div>
			
			<label class="col-md-1 col-sm-1 control-label">信息状态</label>
			<div class="col-md-2 col-sm-2">
				<select class="form-control selectpicker myData" id="examStatus"
					name="examStatus">
					<option value="">全部</option>
					<option value="0">未分析</option>
					<option value="1">分析成功</option>
					<option value="2">正在分析</option>
					<option value="3">分析失败</option>
					<option value="5">已发布</option>
					<option value="6">等待分析</option>
				</select>
			</div>
		</div>
	</@querybar.querybar>
	
	
	<@table.table tableId="examList" head=["考试日期","考试名称","考试简称","信息状态","导入学生信息","导入双向细目","导入成绩","操作"]>
		<#include "./examListBody.ftl">
	</@table.table>
	
</div>

