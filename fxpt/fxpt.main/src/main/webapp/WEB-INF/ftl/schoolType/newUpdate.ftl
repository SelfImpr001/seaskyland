
<div class="modal-body">
	<form id="schoolTypeform" name="schoolTypeform" method="post" action="">
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">学校类别名称</label>
			<div class="col-sm-9">
				<input value="${schoolType.name}" type="text"
					class="form-control myData" name="schoolTypeName" id="schoolTypeName"
					placeholder="学校类别名称" maxlength="50">
				<p class="error-tip"></p>
			</div>
		</div>
		 <div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1" >排序</label>
			<div class="col-sm-9">
				<input value="${schoolType.ordernum}" type="text" class="form-control myData" name="ordernum" placeholder="排序" maxlength="2" onchange="if(/\D/.test(this.value)){this.value='';}">
				<p class="error-tip"></p>
			</div>
	   </div>
		<input type="hidden" class="myData" name="id" id="schoolTypeId"
			value="${schoolType.id}">
	</form>
</div>
