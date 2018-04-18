<div class="modal-body">
	<form id="biUserForm" name="biUserForm" method="post" action="">
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">SmartBI用户</label>
			<div class="col-sm-9">
				<select class="form-control selectpicker myData" id="biInfo.id" name="biInfo.id">
					<#if listBiInfo??> <#list listBiInfo as biInfo> <#if
					biInfo.id==biUser.biInfo.id>
					<option value="${biInfo.id}" selected>${biInfo.name}</option>
					<#else>
					<option value="${biInfo.id}">${biInfo.name}</option> </#if>
					</#list> </#if>
				</select>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">用户名</label>
			<div class="col-sm-9">
				<input value="${biUser.userName}" type="text"
					class="form-control myData" id="userName" name="userName"
					placeholder="用户名">
				<p class="error-tip"></p>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">密码</label>
			<div class="col-sm-9">
				<input value="${biUser.userPassword}" type="text"
					class="form-control myData" id="userPassword" name="userPassword"
					placeholder="密码">
				<p class="error-tip"></p>
			</div>
		</div>

		<input type="hidden" class="myData" name="id" id="id"
			value="${biUser.id}">
	</form>
</div>