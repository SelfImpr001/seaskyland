<div class="modal-body">
	<form id="form1" name="form1" method="post" action="">
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">所属省市教育局:</label>
			<div class="col-sm-9">
				<select class="form-control" id="provinceEducation"> <#if
					provinceEducations??> <#list provinceEducations as education>
					<option value="${education.code}">${education.name}</option>
					</#list> </#if>
				</select>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">所属地市教育局:</label>
			<div class="col-sm-9">
				<select class="form-control" id="cityEducation"> <#if
					cityEducation??> <#list cityEducation as education>
					<option value="${education.code}">${education.name}</option>
					</#list> </#if>
				</select>
			</div>
		</div>


		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">所属区县教育局:</label>
			<div class="col-sm-9">
				<select class="form-control" id="countyEducation"> <#if
					countyEducations??> <#list countyEducations as education>
					<option value="${education.code}">${education.name}</option>
					</#list> </#if>
				</select>
			</div>
		</div>


		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">学校代码:</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" name="code" id="code"
					placeholder="学校代码">
				<p class="error-tip" id="codeTip"></p>
			</div>
		</div>


		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">学校名称:</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" name="name"
					placeholder="学校名称">
				<p class="error-tip"></p>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">学校类型:</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" name="schoolType"
					placeholder="学校类型">
			</div>
		</div>

	</form>
</div>