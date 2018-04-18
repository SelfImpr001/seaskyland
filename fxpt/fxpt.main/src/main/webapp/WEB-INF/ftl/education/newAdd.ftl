
<div class="modal-body">
	<form id="form1" name="form1" method="post" action="">
		<#if (educationType>1)>
		<div class="form-group">
			<label for="m-password1" class="col-sm-3 control-label">所属省市:</label>
			<div class="col-sm-9">
				<select class="selectpicker form-control" id="provinceEducation">
					<#list provinceEducations as education>
					<option value="${education.code}">${education.name}</option>
					</#list>
				</select>
			</div>
		</div>


		</#if> <#if (educationType>2)>
		<div class="form-group">
			<label for="m-password1" class="col-sm-3 control-label">所属地市:</label>
			<div class="col-sm-9">
				<select class="selectpicker form-control" id="cityEducation">
					<#list cityEducations as education>
					<option value="${education.code}">${education.name}</option>
					</#list>
				</select>
			</div>
		</div>
		</#if>


		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">代码:</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" name="code" id="code"
					placeholder="教育局代码">
				<p class="error-tip" id="codeTip"></p>
			</div>
		</div>


		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">名称:</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" name="name"
					placeholder="教育局名称">
				<p class="error-tip"></p>
			</div>
		</div>


		<input type="hidden" class="myData" name="type" id="type"
			value="${educationType}">
	</form>
</div>



