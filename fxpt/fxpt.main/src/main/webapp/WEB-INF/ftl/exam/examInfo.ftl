<div class="row form-horizontal">
	<table class="table table-bordered">
		<tbody>
			<tr>
				<td>
					<div class="form-group">
						<label class="col-sm-1 control-label">考试名称</label>
						<div class="col-sm-11"><p class="form-control-static">${exam.name}</p></div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form-group">
						<label class="col-sm-1 control-label">考试简称</label>
						<div class="col-sm-5"><p class="form-control-static">${exam.sortName}</p></div>
						<label class="col-sm-1 control-label">考试日期</label>
						<div class="col-sm-5"><p class="form-control-static">${exam.examDate?string(" yyyy-MM-dd")}</p></div>
					
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form-group">
						<label class="col-sm-1 control-label">学年</label>
						<div class="col-sm-5"><p class="form-control-static">${exam.schoolYearName}</p></div>
						<label class="col-sm-1 control-label">学期</label>
						<div class="col-sm-5"><p class="form-control-static"><#if exam.schoolTerm==1> 上学期 <#else>
							下学期 </#if></p></div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form-group">
						<label class="col-sm-1 control-label">年级</label>
						<div class="col-sm-5"><p class="form-control-static">${exam.grade.name}</p></div>
						<label class="col-sm-1 control-label">考试类型</label>
						<div class="col-sm-5"><p class="form-control-static">${exam.examType.name}</p></div>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>