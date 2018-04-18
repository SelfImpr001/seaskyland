<#import "../commons/pageHead.ftl" as pageHead> 
<#import "../commons/querybar.ftl" as querybar>
<#import "../commons/table.ftl" as table>

<div class="page-content">
	<!--页面内容部分-->
	<!-- page-header -->
	<#assign
	btns=[{"text":"双向细目表模板下载","id":"item-downloadTemplate","class":"btn-primary","closeBtn":false}]>
	<@pageHead.pageHead title="${title!'双向细目表导入'}" buttons=btns/>
	<!-- /.page-header -->
	
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
		<label class="col-md-1 col-sm-1 control-label" style="text-align: left;">导入双向细目表</label>
		<div class="col-md-2 col-sm-2">
				<select class="form-control selectpicker myData" id="hasItem"
					name="hasItem">
					<option value="">全部</option>
					<option value="true">已导入</option>
					<option value="false">未导入</option>
				</select>
		</div>
	</div>
		</@querybar.querybar>
	
	<@table.table tableId="examList" head=["考试日期","考试名称","考试简称","信息状态","导入双向细目表状态","操作"]>
		<#include "./examListBody.ftl">
	</@table.table>
	
</div>