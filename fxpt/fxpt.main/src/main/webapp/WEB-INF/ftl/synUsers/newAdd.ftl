<div class="modal-body">
	<form id="synusersform" name="synusersform" method="post" action="">
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1" >用户对应角色</label>
			<div class="col-sm-9">
				<select class="form-control selectpicker myData" id="roleid" name="roleid">
						<option>请选择</option>
						<#list listRole as role>
								<option value="${role.pk}" id="role" trigger="${role.pk}">${role.name}</option>
						</#list>
				</select>
			</div>
		</div>
		
	   <div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1" >用户命名规则</label>
			<div class="col-sm-9">
				<input type="checkbox"  class="ace gardeIds myData" name="namingRules"
				value="provinceCode" required minlength="1"
				data-name="provinceCode"> <span class="lbl"></span>省 
													
				<input type="checkbox" class="ace gardeIds myData" name="namingRules"
				value="cityCode" required minlength="1"
				data-name="cityCode"> <span class="lbl"></span>市 
													
				<input type="checkbox"  class="ace gardeIds myData" name="namingRules"
				value="countyCode" required minlength="1"
				data-name="countyCode"> <span class="lbl"></span>区 
													
				<input type="checkbox" class="ace gardeIds myData" name="namingRules"
				value="schoolCode" required minlength="1"
				data-name="schoolCode"> <span class="lbl"></span>学校 
													
				<input type="checkbox" class="ace gardeIds myData" name="namingRules"
				value="gradeCode" required minlength="1"
				data-name="gradeCode"> <span class="lbl"></span>年级
													
				<input type="checkbox" class="ace gardeIds myData" name="namingRules"
				value="classCode" required minlength="1"
				data-name="classCode"> <span class="lbl"></span>班级
													
				<input type="checkbox" class="ace gardeIds myData" name="namingRules"
				value="subjectCode" required minlength="1"
				data-name="subjectCode"> <span class="lbl"></span>学科
			</div>
	   </div>
	   
	   	<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1" >密码规则</label>
			<div class="col-sm-9">
				<input type="radio" class="ace myData"
									name="passwordrules" id="passwordrules" value="any" checked> <span class="lbl">
									</span>随机产生
									<input type="radio" name="passwordrules" id="passwordrules" class="ace myData"
									value="one"> <span class="lbl"> </span>固定密码
									<input type="text" class="ace myData" id="defaultpassword"
									name="defaultpassword" placeholder="" value="">
			</div>
		</div>
		
			<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1" >数据发布时自动同步</label>
			<div class="col-sm-9">
				<input type="radio" class="ace myData"
									name="isSyn" id="isSyn" value="1" checked> <span class="lbl">
									</span>启用
									<input type="radio" name="isSyn" id="isSyn" class="ace myData"
									value="0"> <span class="lbl"> </span>停用
			</div>
		</div>
	</form>
</div>