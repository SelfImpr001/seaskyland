<div class="modal-body">
	<form id="biUserForm" name="biUserForm" method="post" action="">
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">SmartBI用户</label>
			<div class="col-sm-9">
				<select class="form-control selectpicker myData" id="biInfoId" name="biInfoId">
					<#if listBiInfo??> <#list listBiInfo as biInfo>
					<option value="${biInfo.id}">${biInfo.name}</option> </#list>
					</#if>
				</select>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">用户名</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" id="userName"
					name="userName" placeholder="用户名">
				<p class="error-tip"></p>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">密码</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" id="userPassword"
					name="userPassword" placeholder="密码">
				<p class="error-tip"></p>
			</div>
		</div>
	</form>
</div>


