
<div class="modal-body">
	<form id="gradeform" name="gradeform" method="post" action="">
		<div class="form-group">
			<label class="col-sm-3 control-label">年级名称</label>
			<div class="col-sm-9">
				<input value="${grade.name}" type="text" class="form-control myData"
					name="gradeName" id="gradeName" placeholder="年级名称" maxlength="50">
				<p class="error-tip"></p>
			</div>
		</div>
		<input type="hidden" class="myData" name="id" id="gradeId"
			value="${grade.id}">
	</form>
</div>

