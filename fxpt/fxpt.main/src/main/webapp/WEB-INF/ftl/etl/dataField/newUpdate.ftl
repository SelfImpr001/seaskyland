<div class="modal-body"style="padding:0">
	<form id="dataFieldform" name="dataFieldform" method="post" action="">
	<div class="form-group">

		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">字段名称</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" id="fieldName"
					name="fieldName" placeholder="字段名称" value="${dataField.fieldName}">
				<p class="error-tip"></p>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">字段别名</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" id="asName"
					name="asName" placeholder="字段别名" value="${dataField.asName}">
				<p class="error-tip"></p>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">默认字段</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" id="defaultName"
					name="defaultName" placeholder="默认字段，多个字段用 | 分开" value="${dataField.defaultName}">
			</div>
		</div>
	<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">字段描述</label>
			<div class="col-sm-9">
			<textarea  class="form-control myData" id="description"
				   name="description"  placeholder="字段描述" 
				   rows="5" style="width: 370px;">${dataField.description!""}</textarea>
			</div>
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">是否有效</label>
			<div class="col-sm-9  radio-content">
				<div class="download-check">
					<#if dataField.valid=true>
						<input type="radio" class="ace myData" name="valid" id="valid"
							value="true" checked="true"> <span class="lbl"></span>有效
						<input type="radio" class="ace myData" name="valid" id="valid"
							value="false"> <span class="lbl"></span>无效
					<#else>
						<input type="radio" class="ace myData" name="valid" id="valid"
							value="true" > <span class="lbl"></span>有效
						<input type="radio" class="ace myData" name="valid" id="valid"
							value="false" checked="true"> <span class="lbl"></span>无效
					</#if>
				</div>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">是否必须导入</label>
			<div class="col-sm-9  radio-content">
				<div class="download-check">
					<#if dataField.need=true>
						<input type="radio" class="ace myData" name="need" id="need"
						value="true" checked="true"> <span class="lbl"></span>是
						<input type="radio" class="ace myData" name="need" id="need"
						value="false"> <span class="lbl"></span>否
					<#else>
						<input type="radio" class="ace myData" name="need" id="need"
						value="true" > <span class="lbl"></span>是
						<input type="radio" class="ace myData" name="need" id="need"
						value="false" checked="true"> <span class="lbl"></span>否
					</#if>	
				</div>
			</div>
		</div>

		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">序号</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" id="sortNum"
					name="sortNum" placeholder="用于字段显示顺序,必须为整数" value="${dataField.sortNum}" maxlength="2" onkeydown="this.value=this.value.replace(/[\D]/g,'');" onchange="this.value=this.value.replace(/[\D]/g,'');">
				<p class="error-tip"></p>
			</div>
		</div>
			<input type="hidden" class="myData" name="dataCategoryId" id="dataCategoryId" value="${dataField.dataCategory.id}">
	</form>
</div>




		