<div class="modal-body">
	<form id="form1" name="form1" method="post" action="">
		<table class="table table-bordered">
			<tbody>
				<tr>
					<td width="50%"><div class="form-group">
							<label class="col-sm-3 control-label" for="m-password1">考试名称:</label>
							<div class="col-sm-9">
								<input type="text" class="form-control myData" id="name"
									name="name" placeholder="输入考试名称">
								<p class="error-tip"></p>
							</div>
						</div></td>

					<td width="50%"><div class="form-group">
							<label class="col-sm-3 control-label" for="m-password1">考试简称:</label>
							<div class="col-sm-9">
								<input type="text" class="form-control myData" id="sortName"
									name="sortName" placeholder="输入考试简称">
							</div>
						</div></td>
				</tr>
				<tr>
					<td><div class="select-group">
							<label class="col-sm-3 control-label" for="m-password1">类型:</label>
							<div class="col-sm-9">
								<select class="selectpicker myData" id="examTypeId"
									name="examType.id"> <#if examTypes??> <#list examTypes
									as examType>
									<option value="${examType.id}">${examType.name}</option>
									</#list> </#if>
								</select>
							</div>
						</div></td>
					<td><div class="select-group">
							<label class="col-sm-3 control-label" for="m-password1">年级:</label>
							 <div class="select-box col-sm-9">
                                <select class="selectpicker myData" id="gardeId" name="grade.id">
									<#if grades??> <#list grades as grade>
									<option value="${grade.id}">${grade.name}</option> </#list>
									</#if>
								</select>
                            </div>
							
						</div></td>
				</tr>
				<tr>
					<td><div class="form-group">
							<label class="col-sm-4 control-label" for="m-password1">参考学生是否分文理:</label>
							<div class="col-sm-8">
								<div class="download-check pull-left">
									<input checked type="radio" name="wlForExamStudent"
									id="wlForExamStudent" class="ace myData" value="true"> <span class="lbl">
									</span>分文理
								</div>
								<div class="download-check pull-left">
									<input type="radio" class="ace myData"
									name="wlForExamStudent" id="wlForExamStudent" value="false"> <span class="lbl">
									</span>不分文理
								</div>
							</div>
						</div></td>
					<td>
						<div class="form-group">
                                    <label for="order1" class="col-sm-3 control-label">考试时间:</label>
                                    <div class="col-sm-9">
                                        <div class="input-group">
                                          <input type="text" class="form-control date-picker myData" name="examDate" required>
                                          <span class="input-group-addon"><i class="icon-calendar"></i></span>
                                        </div>
                                        <p class="error-tip"></p>
                                    </div>
                                </div>  
					</td>
				</tr>

				<tr>
					<td><div class="select-group">
							<label class="col-sm-3 control-label" for="m-password1">学年:</label>
							<div class="col-sm-9">
								<select class="selectpicker myData" id="schoolYear"
									name="schoolYear"> <#list year+1..year-5 as myYear>
									<option value="${myYear?string("#")}">${(myYear-1)?string("#")+"-"+(myYear)?string("#")+"学年"}</option>
									</#list>
								</select>
							</div>
						</div></td>
					<td><div class="form-group">
							<label class="col-sm-2 control-label" for="m-password1">学期:</label>
							<div class="col-sm-9">
								<div class="download-check pull-left">
									<input checked type="radio" name="schoolTerm" id="schoolTerm"
										class="ace myData" value="1"> <span class="lbl">
									</span>上学期
								</div>
								<div class="download-check pull-left">
									<input type="radio" class="ace myData" name="schoolTerm"
										id="schoolTerm" value="2"> <span class="lbl">
									</span>下学期
								</div>
							</div>
						</div></td>
				</tr>


				<tr>
					<td><div class="select-group">
							<label class="col-sm-4 control-label" for="m-password1">参考学生所属届别:</label>
							<div class="col-sm-7">
								<select class="selectpicker myData" id="examStudentJiebie"
									name="examStudentJiebie"> <#list year+3..year-3 as
									myYear>
									<option value="${myYear?string("#")}">${(myYear)?string("#")+"届"}</option>
									</#list>
								</select>
							</div>
						</div></td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td>
					<div class="select-group">
                          <label for="Select-table" class="col-sm-3 control-label">所属教育局:</label>
                            	<div class="ztree-box col-sm-9">
                            	<input type="hidden" class="myData" name="ownerCode" id="ownerCode" value="">
                                        <input type="text" readonly class="form-control ztr myData" id="ownerName" name="ownerName" placeholder="选择组织考试的教育局" required>
                                        <p class="error-tip"></p>
                                    </div>
                                </div> 
					</td>
					<td><div class="form-group">
							<label class="col-sm-3 control-label" for="m-password1">考试级别:</label>
							<div class="col-sm-9">
								<input type="text" class="form-control myData" name="levelName"
									id="levelName" placeholder="考试级别" readonly> <input
									type="hidden" class="myData" name="levelCode" id="levelCode"
									value="">
							</div>
						</div></td>
				</tr>
			</tbody>
		</table>
	</form>
</div>
