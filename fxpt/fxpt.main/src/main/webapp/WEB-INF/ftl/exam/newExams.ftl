<div class="modal-body table" style="overflow-y:visible;">
	<form id="addExamsForm" name="addExamsForm" class="form-horizontal ">
		<div class="form-group" style="height:68px;">

			<label class="col-sm-2 control-label">考试日期</label>
			<div class="col-sm-4">
				<div class="input-group" style="cursor: pointer">
					<input type="text" class="form-control date-picker myData"
						name="examDate" required value="${date?string("yyyy-MM-dd")}" readonly
						style="cursor: pointer; background-color: #fff;"> <span
						class="input-group-addon"><i class="icon-calendar"></i></span>
				</div>
				<p class="error-tip"></p>
			</div>


			<label class="col-sm-2 control-label">学年</label>
			<div class="col-sm-4">
				<select required class="selectpicker form-control  myData" id="schoolYear"
					name="schoolYear"> <#list year+1..year-5 as myYear> <#if
					(month>=2 && month<=8 && myYear_index == 1) >
					<option selected value="${myYear?string("#")}">${(myYear-1)?string("#")+"-"+(myYear)?string("#")+"学年"}</option>
					<#elseif (month>=9 && month<=12 && myYear_index == 0) || ( month==1 && myYear_index == 1)>
					<option selected value="${myYear?string("#")}">${(myYear-1)?string("#")+"-"+(myYear)?string("#")+"学年"}</option>
					<#else>
					<option value="${myYear?string("#")}">${(myYear-1)?string("#")+"-"+(myYear)?string("#")+"学年"}</option>
					</#if> </#list>
				</select>
				<p class="error-tip"></p>
			</div>

			

		</div>

		<div class="form-group" style="height:68px;">
			
			<label class="col-sm-2 control-label">学期</label>
			<div class="col-sm-4">
				<div class="download-check pull-left">
					<input <#if ( (month>=9 && month<=12) ||
					month==1)>checked</#if> type="radio" class="ace myData"
					name="schoolTerm" id="schoolTerm" value="1"> <span class="lbl">
					</span>上学期
				</div>
				<div class="download-check pull-left">
					<input  <#if (month>=2 && month<=8)>checked</#if>
					type="radio" name="schoolTerm" id="schoolTerm" class="ace myData"
					value="2"> <span class="lbl"> </span>下学期
				</div>
			</div>
			
			
			<label class="col-sm-2 control-label">考试类型</label>
			<div class="col-sm-4">
				<select class="selectpicker form-control myData" id="examTypeId"
					name="examTypeId" required> <#if examTypes??> <#list examTypes as
					examType>
					<option value="${examType.id}" data-name="${examType.name}">${examType.name}</option>
					</#list> </#if>
				</select>
				<p class="error-tip"></p>
			</div>
			
		</div>

		<div class="form-group">
			<label class="col-sm-2 control-label">年级</label>
			<div class="col-sm-10">
				<div class="input-group" style="cursor: pointer">
				<#if grades??> <#list grades as grade>
				<div class="download-check">
					<input type="checkbox" class="ace gardeIds myData" name="gardeIds"
						value="${grade.id}" required minlength="1"
						data-name="${grade.name}"> <span class="lbl"></span>${grade.name}
				</div>
				</#list> </#if>
				
					<p class="error-tip" id="gardeIdsCount" style="display: inline-block; float: none"></p>
				</div>
				<p></p>
			</div>
		</div>

		<div class="form-group" style="height:68px;">
			<label class="col-sm-2 control-label">组织考试机构</label>
			<div class="col-sm-4">
				<input type="hidden" class="myData" name="levelCode" id="levelCode" value="${(userOrg.type)!""}"> 
				<input type="hidden" class="myData" name="ownerCode" id="ownerCode" value="${(userOrg.code)!""}"> 
				<input type="text" readonly class="form-control ztr myData" id="ownerName" value="${(userOrg.name)!""}" name="ownerName" placeholder="选择组织考试机构" required style="cursor: pointer; background-color: #fff;">
				<p class="error-tip"></p>
			</div>
			<label class="col-sm-2 control-label">考试创建人</label>
			<div class="col-sm-4">
				<input type="text" class="form-control myData" id="createUserName"
					name="createUserName" placeholder="" value="${user.userName}"
					readonly>
				<p class="error-tip"></p>
			</div>
		</div>
	</form>
</div>
