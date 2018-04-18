<div class="modal fade   in" id="pwd-modal" role="dialog" aria-hidden="false" data-backdrop="static" style="display: block;"><div class="modal-backdrop fade in"></div>
	<div class="modal-dialog self-dialog ">
	  <div class="modal-content" style="">
	<div style="border-:1px solid red;" class="modal-header" id="modal-header">
	  <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
	  <h4>修改用户密码</h4>
    </div>
    <form id="form1" name="form1" enctype="multipart/form-data" class="form-horizontal" role="form" method="post" action="" novalidate="novalidate">
	  <div id="pwd-modalBody" class="modal-body">
		<div class="form-group" style="display:block">
		  <label class="col-sm-3 control-label" for="textinput">用户名</label>
		  <div class="col-sm-9">
		  <input value="${user.name}" readonly="" type="text" class="form-control myData" id="name" name="name" placeholder="">
		 	 <p class="error-tip"></p>
		  </div>
		</div>
		<div class="form-group" style="display:block">
		  <label class="col-sm-3 control-label" for="textinput">新密码</label>
		  <div class="col-sm-9">
		  <input value="" type="password" class="form-control myData" id="password" name="password" placeholder="密码长度不小于6位，可由字母、数字组成">
		 	 <p class="error-tip"></p>
		  </div>
		</div>
		<div class="form-group" style="display:block">
		  <label class="col-sm-3 control-label" for="textinput">确认新密码</label>
		  <div class="col-sm-9">
		  <input value="" type="password" class="form-control myData" id="repassword" name="repassword" placeholder="密码长度不小于6位，可由字母、数字组成">
		 	 <p class="error-tip"></p>
		  </div>
		</div>
		<input type="hidden" class="myData" name="pk" id="" value="1">
	  </div>
	  <div class="modal-footer">
		<div class="self-modal-footer">
		  <button class="btn btn-primary" type="button" id="update">修改</button>
		  <button data-dismiss="modal" class="btn btn-default" type="button" id="">取消</button>
		</div>
	  </div>
	  <input type="hidden" class="myData" name="id" id="pk" value="${user.pk}">
	</form>			
      </div>
  	</div>
  </div>