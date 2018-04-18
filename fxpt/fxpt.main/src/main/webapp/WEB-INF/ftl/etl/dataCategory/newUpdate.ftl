<div class="modal-body">
	<form id="dataCategoryform" name="dataCategoryform" method="post" action="">
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">名称</label>
			<div class="col-sm-9">
				<input value="${dataCategory.name}" type="text"
					class="form-control myData" name="dataCategoryName" id="dataCategoryName"
					placeholder="导入数据类型名称">
				<p class="error-tip"></p>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">对应的表</label>
			<div class="col-sm-9">
				<input value="${dataCategory.tableName}" type="text"
					class="form-control myData" name="dataCategoryTableName" id="dataCategoryTableName"
					placeholder="对应数据表">
				<p class="error-tip"></p>
			</div>
		</div>
		<input type="hidden" class="myData" name="dataCategoryId" id="dataCategoryId"
			value="${dataCategory.id}">
		<input type="hidden" class="myData" name="schemeType" id="schemeType"
			value="${dataCategory.schemeType}">
	</form>
</div>
