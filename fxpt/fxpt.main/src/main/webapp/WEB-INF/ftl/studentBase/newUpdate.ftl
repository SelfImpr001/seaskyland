
<div class="modal-body">
	<form id="studentBaseform" name="studentBaseform" method="post" action="">
	
	<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">学号</label>
			<div class="col-sm-9">
				<input value="${studentBase.xh}" type="text"
					class="form-control myData" name="studentBasexh" id="studentBasexh"
					placeholder="学号" readonly="true">
				<p class="error-tip"></p>
			</div>
		</div>
	
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">姓名</label>
			<div class="col-sm-9">
				<input value="${studentBase.name}" type="text"
					class="form-control myData" name="studentBaseName" id="studentBaseName"
					placeholder="姓名" maxlength="20">
				<p class="error-tip"></p>
			</div>
		</div>
		
		
		<div class="form-group" style="height:42px;">
			<label class="col-sm-3 control-label" for="m-password1">性别</label>
				<div class="col-sm-9">
					<select class="form-control selectpicker myData" id="studentBaseSex" name="studentBaseSex"> 
						<#if studentBase.sex=1>
							<option value="1" selected>男</option>
							<option value="2">女</option>
						<#elseif studentBase.sex=2>
							<option value="2" selected>女</option>
							<option value="1">男</option>
					    <#else>
					        <option value="0" selected>无</option>
					        <option value="1">男</option>
							<option value="2">女</option>
					    </#if>
					</select>
				</div>
		</div>
	
		<div class="form-group">
			<label class="col-sm-3 control-label">所属学校</label>
			<div class="col-sm-9 ztree-box">
				<input type="hidden"
					class="myData" name="ownerCode" id="ownerCode"
					value="${studentBase.school.code}"> 
				<input type="text"
					class="form-control ztr myData" id="ownerName" name="ownerName"
					placeholder="选择所属学校" required style="cursor: pointer"
					value="${studentBase.school.name}">
			</div>
			
			
			
		</div>
	<#--	
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">所属班级:</label>
			<div class="col-sm-9">
				<select class="form-control selectpicker myData" id="studentBaseClazzCode" name="studentBaseClazzCode"> 
					<#if listClazz??> 
						<#list listClazz as clazz> 
							<#if clazz.code == studentBase.clazz.code>
								<option value="${clazz.code}" selected>${clazz.name}</option>
							<#else>
								<option value="${clazz.code}">${clazz.name}</option> 
							</#if>
						</#list> 
					</#if>
				</select>
			</div>
		</div>
		
		
		
	
	
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">准考证号:</label>
			<div class="col-sm-9">
				<input value="${subject.name}" type="text"
					class="form-control myData" name="subjectName" id="subjectName"
					placeholder="准考证号">
				<p class="error-tip"></p>
			</div>
		</div>
		
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">修改人:</label>
			<div class="col-sm-9">
				<input value="${subject.name}" type="text"
					class="form-control myData" name="subjectName" id="subjectName"
					placeholder="修改人">
				<p class="error-tip"></p>
			</div>
		</div>
		-->
		
		<input type="hidden" class="myData" name="id" id="studentBaseId"
			value="${studentBase.id}">
		<input type="hidden" class="myData" name="studentBaseGrade" id="studentBaseGrade"
			value="${studentBase.grade}">
		<input type="hidden" class="myData" name="studentBaseGuid" id="studentBaseGuid"
			value="${studentBase.guid}">	
			
	</form>
</div>
