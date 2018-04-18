<div class="modal-body">
	<form id="examTypeform" name="examTypeform" method="post" action="">
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">考试类型名称</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" name="examTypeName"
					id="examTypeName" placeholder="考试类型名称" maxlength="50">
				<p class="error-tip"></p>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">是否有效</label>
			<div class="col-sm-9  radio-content">
				<div class="download-check">
					<input type="radio" class="ace myData" name="valid" id="valid"
						value="true" checked="true"> <span class="lbl"></span>有效
				</div>
				<div class="download-check">
					<input type="radio" class="ace myData" name="valid" id="valid"
						value="false"> <span class="lbl"></span>无效
				</div>
			</div>
		</div>
	</form>
</div>

