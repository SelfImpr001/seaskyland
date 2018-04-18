
<div class="modal-body">
	<form id="subjectform" name="subjectform" method="post" action="">
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">科目名称</label>
			<div class="col-sm-9">
				<input value="${subject.name}" type="text"
					class="form-control myData" name="subjectName" id="subjectName"
					placeholder="科目名称" maxlength="50">
				<p class="error-tip"></p>
			</div>
		</div>
		 <div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1" >排序</label>
			<div class="col-sm-9">
				<input value="${subject.ordernum}" type="text" class="form-control myData" name="ordernum" placeholder="排序" maxlength="2" onchange="if(/\D/.test(this.value)){this.value='';}">
				<p class="error-tip"></p>
			</div>
	   </div>
		<input type="hidden" class="myData" name="id" id="subjectId"
			value="${subject.id}">
	</form>
</div>
