<div class="modal-body">
	<form id="form1" name="form1" method="post" action="">
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">所属省市教育局:</label>
			<div class="col-sm-9">
				<select class="form-control" id="provinceEducation"> <#if
					provinceEducations??> <#list provinceEducations as education> <#if
					education.code == school.education.parent.parent.code>
					<option value="${education.code}" selected>${education.name}</option>
					<#else>
					<option value="${education.code}">${education.name}</option> </#if>
					</#list> </#if>
				</select>
			</div>
		</div>


		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">所属地市教育局:</label>
			<div class="col-sm-9">
				<select class="form-control" id="cityEducation"
					cityCode="${school.education.parent.code}">
				</select>
			</div>
		</div>


		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">所属区县教育局:</label>
			<div class="col-sm-9">
				<select class="form-control" id="countyEducation"
					countyCode="${school.education.code}">
				</select>
			</div>
		</div>


		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">学校代码:</label>
			<div class="col-sm-9">
				<input value="${school.code}" type="text"
					class="form-control myData" readonly name="code" placeholder="学校代码">
				<p class="error-tip"></p>
			</div>
		</div>


		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">学校名称:</label>
			<div class="col-sm-9">
				<input value="${school.name}" type="text"
					class="form-control myData" name="name" placeholder="学校名称">
				<p class="error-tip"></p>
			</div>
		</div>


		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">学校类型:</label>
			<div class="col-sm-9">
				<input value="${school.schoolType!" "}" type="text"
					class="form-control myData" name="schoolType" placeholder="学校类型">
			</div>
		</div>


		<input type="hidden" class="myData" name="id" value="${school.id}">
	</form>
</div>

