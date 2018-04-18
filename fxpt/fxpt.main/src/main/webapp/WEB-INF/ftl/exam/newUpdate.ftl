<style>
.dropdown-menu{height:280px;}
.dialog-body{padding-bottom:0}
</style>
<div class="row table">
<#assign status="1">
	<#if exam.studentBaseStatus=0 & exam.impItemCount=0 & !exam.hasExamStudent>
	<#assign status="0">
	</#if>
	<form id="addExamsForm" name="addExamsForm" class="form-horizontal ">
		<div class="form-group" style="height:68px;">
			<label class="col-sm-2 control-label">考试名称</label>
			<div class="col-sm-10">
				<input type="text" class="form-control myData required" id="name"
					name="name" placeholder="" value="${exam.name}">
				<p class="error-tip"></p>
			</div>
		</div>

		<div class="form-group" style="height:68px;">
			<label class="col-sm-2 control-label">考试简称</label>
			<div class="col-sm-4">
				<input type="text" class="form-control myData required"
					id="sortName" name="sortName" placeholder=""
					value="${exam.sortName}">
				<p class="error-tip"></p>
			</div>
			
			
			<label class="col-sm-2 control-label">考试日期</label>
			<div class="col-sm-4">
				<div class="input-group" style="cursor: pointer">
					<input type="text" class="form-control date-picker myData"
						name="examDate" required value="${exam.examDate?string("yyyy-MM-dd")}" readonly style="cursor: pointer; background-color: #fff;"> <span
						class="input-group-addon"
						<#if status="1"> disabled="disabled"</#if>
						><i class="icon-calendar"></i></span>
				</div>
				<p class="error-tip"></p>
			</div>
			
			

		</div>


		<div class="form-group" style="height:68px;">
		
			<label class="col-sm-2 control-label">学年</label>
			<div class="col-sm-4">
				<select class="form-control selectpicker myData" id="schoolYear"
					name="schoolYear" <#if status="1"> disabled="disabled"</#if>> <#list year+1..year-5 as myYear> <#if
					exam.schoolYear==myYear>
					<option value="${myYear?string(" #")}" selected>${(myYear-1)?string("#")+"-"+(myYear)?string("#")+"学年"}</option>
					<#else>
					<option value="${myYear?string("#")}">${(myYear-1)?string("#")+"-"+(myYear)?string("#")+"学年"}</option>
					</#if> </#list>
				</select>
			</div>
			
			
			<label class="col-sm-2 control-label">学期</label>
			<div class="col-sm-4">
				<div class="download-check pull-left">
					<input type="radio" class="ace myData" name="schoolTerm"
						${(exam.schoolTerm==1)?string( "checked","")} id="schoolTerm"
						value="1"  <#if status="1"> disabled="disabled"</#if>> <span class="lbl"> </span>上学期
				</div>
				<div class="download-check pull-left">
					<input type="radio" ${(exam.schoolTerm==2)?string(
						"checked","")} name="schoolTerm" id="schoolTerm"
						class="ace
									myData" value="2" <#if status="1"> disabled="disabled"</#if>> <span class="lbl">
					</span>下学期
				</div>
			</div>
			
			
		</div>

		<div class="form-group" style="height:68px;">
			
			<label class="col-sm-2 control-label">年级</label>
			<div class="col-sm-4" style="max-height:180px">
				<select class="selectpicker form-control myData" id="grade"
					name="grade.id" <#if status="1"> disabled="disabled"</#if>> <#if grades?? > <#list grades as grade>
					<#if exam.grade.id==grade.id>
					<option value="${grade.id}" selected>${grade.name}</option> <#else>
					<option value="${grade.id}">${grade.name}</option> </#if> </#list>
					</#if>
				</select>
			</div>
			
			<label class="col-sm-2 control-label">考试类型</label>
			<div class="col-sm-4">
				<select class="selectpicker form-control myData" id="examType" name="examType.id" <#if status="1"> disabled="disabled"</#if>>
					<#if examTypes??> <#list examTypes as examType> <#if
					exam.examType.id == examType.id>
					<option value="${examType.id}" selected>${examType.name}</option>
					<#else>
					<option value="${examType.id}">${examType.name}</option> </#if>
					</#list> </#if>
				</select>
			</div>
			
		
		</div>

		<div class="form-group" style="height:68px;">
			<label class="col-sm-2 control-label">组织考试机构</label>
			<div class="col-sm-4 ztree-box">
				<input type="hidden" class="form-control myData" name="levelName"
					id="levelName" placeholder="考试级别" value="${exam.levelName!""}">
				<input type="hidden" class="myData" name="levelCode" id="levelCode"
					value="${exam.levelCode!0}"> <input type="hidden"
					class="myData" name="ownerCode" id="ownerCode"
					value="${exam.ownerCode!""}"> <input type="text" readonly
					class="form-control ztr myData" id="ownerName" name="ownerName"
					placeholder="选择组织考试机构" required style="cursor: pointer; background-color: #fff;"
					value="${exam.ownerName!""}" <#if status="1"> disabled="disabled"</#if>>
				<p class="error-tip"></p>
			</div>
			<label class="col-sm-2 control-label">考试创建人</label>
			<div class="col-sm-4">
				<input type="text" class="form-control myData" id="createUserName"
					name="createUserName" placeholder="" value="${exam.createUserName}"
					readonly <#if status="1"> disabled="disabled"</#if>>
				<p class="error-tip"></p>
			</div>
		</div>

		<input type="hidden" class="myData" name="id" id="examId"
			value="${exam.id}" />
	</form>
</div>