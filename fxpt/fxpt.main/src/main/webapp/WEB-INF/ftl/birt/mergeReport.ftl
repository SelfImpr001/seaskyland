
<div class="page-content">
  <!-- 表单头-->
	<div class="page-header">
		<h1>
	    	<i class="icon-hand-right"></i>birt合并
	      	<div class="pull-right">
	            <button class="btn btn-primary" type="button" trigger="goback" id="to_goback" name=""> <<返回上级页面</button>
			</div>
	    </h1>
	</div>
	<div class="row">
	
		 
		
		 <div class="form-group" >
		      <div id="user_girt_ztreediv" class="col-xs-2  ">
		      <ul id="user_girt_ztree1" class="ztree sortables">
		      
		      </ul>
		   	 </div>
		      <div id="user_demo_ztreediv" class="col-xs-5 " >
		         <ul id="user_demo_ztree1" class="ztree sortables" style="height:600px;border:1px solid red;width:220px;">
		      </ul>
		   	 </div>
		 </div> 
		  <div class="form-group" >
		 	 <label class="col-sm-2 control-label">合并脚本名称</label>
			<div class="col-sm-4">
			  <input type="text" class="form-control myData"  id="name"  name="name" placeholder="合并脚本名称">
			  <p class="error-tip"></p>
			</div>
		</div>
	</div>
	
	 <div class="modal-footer">        
            <button type="button" class="btn btn-primary" id="save">确定</button>
        </div>
</div>