
<div class="modal-body">
	<form id="biInfoForm" name="biInfoForm" method="post" action="">
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">SmartBI用户</label>
			<div class="col-sm-9">
				<input value="${biInfo.name}" type="text"
					class="form-control myData" id="name" name="name"
					placeholder="SmartBI用户" maxlength="30">
				<p class="error-tip"></p>
			</div>
		</div>


		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">地址</label>
			<div class="col-sm-9">
				<input value="${biInfo.url}" type="text" class="form-control myData"
					id="url" name="url" placeholder="地址" maxlength="200">
				<p class="error-tip"></p>
			</div>
		</div>


		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">描述</label>
			<div class="col-sm-9">
				<input value="${biInfo.remark}" type="text"
					class="form-control myData" id="remark" name="remark"
					placeholder="描述" maxlength="200">
			</div>
		</div>

		<input type="hidden" class="myData" name="id" id="id"
			value="${biInfo.id}">
	</form>
</div>
