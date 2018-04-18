<#import "../../commons/pageHead.ftl" as pageHead> 
<#import "../../commons/querybar.ftl" as querybar>
<#import "../../commons/table.ftl" as table>

<div class="page-content">
	<@pageHead.pageHead title="${title!'参数设置'}"/>
	<@querybar.querybar showAngle=false>
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
	</@querybar.querybar>
	
	
	<@table.table tableId="examList" head=["考试日期","考试名称","考试简称","信息状态","导入学生信息","导入双向细目","导入成绩","操作"]>
		<#include "./examListBody.ftl">
	</@table.table>
	
</div>

