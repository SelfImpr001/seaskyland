
<div class="modal-body">
	<form id="form1" name="form1" method="post" action="">
		<#if (education.type>1)>
		<div class="form-group">
			<label for="m-password1" class="col-sm-3 control-label">所属省市:</label>
			<div class="col-sm-9">
				<#if (education.type==2)> <input type="hidden" id="provinceCode"
					value="${education.parent.code}"> <input type="text"
					class="form-control myData" name="code" id="m-password1"
					value="${education.parent.name}" placeholder="所属省市教育局" readonly>
				</#if> <#if (education.type==3)> <input type="hidden"
					id="provinceCode" value="${education.parent.parent.code}">
				<input type="text" class="form-control myData" name="code"
					id="m-password1" value="${education.parent.parent.name}"
					placeholder="所属省市教育局" readonly> </#if>
			</div>
		</div>
		</#if> <#if (education.type>2)>
		<div class="form-group">
			<label for="m-password1" class="col-sm-3 control-label">所属地市:</label>
			<div class="col-sm-9">
				<input type="hidden" id="cityCode" value="${education.parent.code}">
				<input type="text" class="form-control"
					value="${education.parent.name}" placeholder="所属地市教育局" readonly>
			</div>
		</div>
		</#if>

		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">代码:</label>
			<div class="col-sm-9">
				<input value="${education.code} " type="text"
					class="form-control myData" name="code" readonly id="m-password1"
					placeholder="教育局代码">
				<p class="error-tip"></p>
			</div>
		</div>


		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">名称:</label>
			<div class="col-sm-9">
				<input value="${education.name}" type="text"
					class="form-control myData" name="name" id="m-password1"
					placeholder="教育局名称">
				<p class="error-tip"></p>
			</div>
		</div>

		<input type="hidden" class="myData" name="type" id="type"
			value="${education.type}"> <input type="hidden"
			class="myData" name="id" id="id" value="${education.id}">
	</form>
</div>
