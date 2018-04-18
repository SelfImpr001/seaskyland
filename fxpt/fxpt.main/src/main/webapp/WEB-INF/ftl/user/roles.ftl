<div class="page-content">
  <!-- 表单头-->
	<div class="page-header">
		<h1>
	    	<i class="icon-hand-right"></i>权限分配>${(user.name)!}
	      	<div class="pull-right">
	            <button class="btn btn-primary" type="button" trigger="goback" id="to_goback" name=""> <<返回上级页面</button>
			</div>
	    </h1>
	</div>
	<input type="hidden" id="userId" value="${(user.pk)!}">
	<div class="row">
		<form id="userEditForm" role="form" action="" class="form-horizontal ">
		  <div class="form-group" style="margin:0 -15px 0 1px;padding:0px;">
			<div class="col-sm-6" style="overflow:auto;">
			  <ul id="roleztree" class="ztree modal-ztree-checkbox" style="height:400px;padding: 10px 10px;"></ul>
			  <ul id="rpztree" style="display:none;"></ul>
			</div>
			<div class="col-sm-6" style="overflow:auto;">
			  <ul id="userztree" class="ztree modal-ztree-checkbox" style="height:400px;padding: 10px 10px;margin-right: 1px;overflow:;"></ul>
			</div>	
		  </div>
		</form>   
		<div style="display:table;width:auto;margin-left:auto;margin-right:auto;">
			      <button class="btn btn-primary" type="button" id="grantBtn" name="">授权</button>
			      <button class="btn btn-default" type="button" id="cancelBtn" name="">取消</button>
		</div>
	</div>
</div> 