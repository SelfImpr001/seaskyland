<div class="modal-body">
	<form id="form1" name="form1" method="post" action="">
		<#--对应的科目-->
		<div class="select-group clearfix">
			<label class="col-sm-3 control-label" for="m-password1">对应的科目:</label>
			<div class="col-sm-9">
				<select class="selectpicker myData" id="subjectId" name="subject.id">
					<#list subjects as subject>
					<option value="${subject.id}">${subject.name}</option> </#list>
				</select>
			</div>
		</div>

		<#--/对应的科目--> <#--试卷名称-->
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">试卷名称:</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" name="name"
					id="m-password1" placeholder="试卷名称">
				<p class="error-tip"></p>
			</div>
		</div>
		<#--试卷名称--> <#--试卷满分-->
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">试卷满分:</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" id="m-password1"
					name="fullScore" placeholder="试卷满分" value="0">
				<p class="error-tip"></p>
			</div>
		</div>
		<#--/试卷满分--> <#--客观题满分-->
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">客观题满分:</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" id="m-password1"
					name="kgScore" placeholder="客观题满分" value="0">
				<p class="error-tip"></p>
			</div>
		</div>
		<#--/客观题满分:--> <#--主观题满分-->
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">主观题满分:</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" id="m-password1"
					name="zgScore" placeholder="主观题满分" value="0">
				<p class="error-tip"></p>
			</div>
		</div>
		<#--/主观题满分--> <#--试卷类型-->
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">试卷类型:</label>
			<div class="col-sm-9 radio-content">
				<div class="download-check">
					<input name="paperType" type="radio" value="0" class="ace myData"
						checked> <span class="lbl"> </span>未分卷
				</div>
				<div class="download-check">
					<input name="paperType" type="radio" value="1" class="ace myData">
					<span class="lbl"></span>理科试卷
				</div>
				<div class="download-check">
					<input name="paperType" type="radio" value="2" class="ace myData">
					<span class="lbl"></span>文科试卷
				</div>
			</div>
		</div>
		<#--/试卷类型--> <#--是否综合试卷-->
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">是否综合试卷:</label>
			<div class="col-sm-9  radio-content">
				<div class="download-check">
					<input name="composite" type="radio" value="false"
						class="ace myData" checked> <span class="lbl"> </span>否
				</div>
				<div class="download-check">
					<input name="composite" type="radio" value="true"
						class="ace myData"> <span class="lbl"> </span>是
				</div>
			</div>
		</div>
		<#--/是否综合试卷-->

	</form>
</div>
