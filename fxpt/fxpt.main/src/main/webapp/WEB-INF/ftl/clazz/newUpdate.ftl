
<div class="modal-body">
	<form id="clazzform" name="clazzform" method="post" action="">
		<div class="form-group">
			<label class="col-sm-3 control-label">班级代码:</label>
			<div class="col-sm-9">
				<input value="${clazz.code}" type="text" class="form-control myData"
					name="clazzCode" id="clazzCode" placeholder="班级代码" readonly="true" maxlength="50">
				<p class="error-tip"></p>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label">班级名称:</label>
			<div class="col-sm-9">
				<input value="${clazz.name}" type="text" class="form-control myData"
					name="clazzName" id="clazzName" placeholder="班级名称" maxlength="50">
				<p class="error-tip"></p>
			</div>
		</div>
		<input type="hidden" class="myData" name="id" id="id"
			value="${clazz.id}">
	</form>
</div>

