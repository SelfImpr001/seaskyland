<#assign enabled = true>
<#if user.name??>
  <#assign enabled = false>
</#if>
<div class="modal fade big-modal" id="big-modal" data-backdrop="static">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header" id="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title">用户管理>编辑用户信息${('>'+user.name)!}</h4>
      </div>
      <div class="modal-body" style='padding:20px 20px 0'>
	    <form id="userEditForm" role="form" action="" class="form-horizontal ">
		 <#if (user.pk > 0)>
			  <div class="form-group" >
				<label class="col-sm-2 control-label">用户名</label>
				<div class="col-sm-4">
				  <input value="${user.name!""}" type="text" <#if enabled=false>disabled</#if> class="form-control myData" id="name" name="name" placeholder="请输入用户名">
				  <p class="error-tip"></p>
				</div>
				<label class="col-sm-2 control-label">密码</label>
				<div class="col-sm-4">
				  <input type="password" value="${user.password!""}" <#if enabled=false>disabled</#if> class="form-control myData" id="password" name="password" placeholder="用户密码">	
				  <p class="error-tip"></p>
				</div>
			  </div>
		  <#else>
			  <div class="form-group" >
				<label class="col-sm-2 control-label">用户名</label>
				<div class="col-sm-4">
				  <input type="text" class="form-control myData" <#if enabled=false>disabled</#if> value=" " name="name" id="name"  placeholder="请输入用户名">
				  <p class="error-tip"></p>
				</div>
				<label class="col-sm-2 control-label">密码</label>
				<div class="col-sm-4">
				  <input type="password" class="form-control myData"  <#if enabled=false>disabled</#if> value="" name="password"  id="password" placeholder="用户密码">	
				  <p class="error-tip"></p>
				</div>
			  </div>
		  </#if>
		  <div class="form-group" >
		 	 <label class="col-sm-2 control-label">姓名</label>
			<div class="col-sm-4">
			  <input type="text" class="form-control myData" <#if user.userInfo??>value="${user.userInfo.realName}"</#if> id="realName"  name="userInfo.realName" placeholder="姓名">
			  <p class="error-tip"></p>
			</div>
			<!-- <label class="col-sm-2 control-label">状态</label>
			<div class="col-sm-4">
              <div class="download-check">
                <input type="radio" class="ace myData" name="status" <#if user.status=1>checked</#if>  value="1">
                <span class="lbl"></span>启用
              </div>  

              <div class="download-check">
                <input type="radio" class="ace myData" name="status" <#if user.status=2>checked</#if> value="2">
                <span class="lbl"></span>禁用
              </div>

              <div class="download-check">
                <input name="status" type="radio" class="ace myData"  <#if user.status=3>checked</#if>" value="3">
                <span class="lbl"></span>锁定
              </div>
			</div> -->
			
			<label class="col-sm-2 control-label">性别</label>
			<div class="col-sm-4">
              <div class="download-check">
                <input type="radio" class="ace myData" checked name="userInfo.sex" <#if user.userInfo??><#if user.userInfo.sex="UNKNOW">checked</#if></#if> value="UNKNOW">
	            <span class="lbl"></span>未知
              </div>  
              <div class="download-check">
                <input type="radio" class="ace myData" name="userInfo.sex" <#if user.userInfo??><#if user.userInfo.sex="MALE">checked</#if></#if> value="MALE">
	            <span class="lbl"></span>男
              </div>
              <div class="download-check">
                <input type="radio" class="ace myData" name="userInfo.sex" <#if user.userInfo??><#if user.userInfo.sex="FEMALE">checked</#if></#if> value="FEMALE">
	            <span class="lbl"></span>女
              </div>
			</div>

		  </div>		  
		  <div class="form-group" >
		  <label class="col-sm-2 control-label">手机</label>
			<div class="col-sm-4">
			  <input type="text" class="form-control myData" id="cellphone" <#if user.userInfo??>value="${user.userInfo.cellphone!""}"</#if> name="userInfo.cellphone"> 	
			  <p class="error-tip"></p>
			</div>
			
			<label class="col-sm-2 control-label">电子邮箱</label>
			<div class="col-sm-4">
			  <input type="email" class="form-control myData" id="email" name="userInfo.email" <#if user.userInfo??>value="${user.userInfo.email!""}"</#if> placeholder="email">	
			  <p class="error-tip"></p>
			</div>
		 
		  </div>	
		  	
		  <div class="form-group" >
			<label class="col-sm-2 control-label">固定电话</label>
			<div class="col-sm-4">
			  <input type="text" class="form-control myData"  id="telphone" name="userInfo.telphone" <#if user.userInfo??>value="${user.userInfo.telphone!""}"</#if> placeholder="固定电话" >
			  <p class="error-tip"></p>
			</div>

					  			
		  </div>
		  
		  <div class="form-group" >
			<label class="col-sm-2 control-label">组织关系</label>
			<div class="select-box col-sm-4 select-group">
              <#assign names="">
              <#assign ids="">
              <#list ubelongs![] as belong>
              		<#assign names=names + "," + belong.org.name >
              		<#assign ids  =ids + "," + belong.org.pk >
              </#list>
              <#if ubelongs?size !=0 >
              	<#assign names= names?substring(1)>
                <#assign ids  = ids?substring(1)>
              </#if>
			  <input type="text" readOnly  value="${names}" id="orgSelected" class="form-control ztr" name="organization.name" placeholder="组织关系" style="background-color:#fff">
			  <ul id="orgTree" class="ztree" style="z-index:20">
			    <input type="hidden"   value="${ids}" id="orgTreeAllId">
			    <input type="hidden" value="${ids}"   id="orgTreeId"  name="organization.id" >			  
			  </ul>
			</div>	
			<label class="col-sm-2 control-label">科目  </label>
			<label><input name='checkAll' id='checkAll' type='checkbox' style="margin-left:10px;"/>全选</label>
			<div class="col-sm-3"  style="height:150px;overflow:auto;border:1px solid #CCC;margin-left:15px;border-radius:5px">
				<div class="input-group" style="cursor: pointer">
				<table>
				<tr>
				<#if subjects??>
					<#list subjects as subject>
						<tr><td style="padding:3px 0!important;white-space:nowrap">
							<input type="checkbox" style="opacity:1" class="ace gardeIds myData" name="examLists" id="a${subject.id}"
							value="${subject.id}"   
							<#if dataAutStr?? && dataAutStr?split(",")?seq_contains(subject.id+"")>checked="checked"</#if>
							data-name="${subject.name}"> <label style="margin-left:25px;width:100%;" for="a${subject.id}" >${subject.name}</label>
						</td></tr>
					</#list> 
				</#if>
				</table>
					<p class="error-tip" id="gardeIdsCount" style="display: inline-block; float: none"></p>
				</div>
				<p></p>
			</div>
			  			
		  </div>		  		  
		  <#if user.pk gt 0>
          <input type="hidden" class="myData"name="pk" value="${user.pk}">
          <input type="hidden" class="myData" name="userInfo.pk" value="${user.userInfo.pk}">
          </#if>
          <input type="hidden" class="myData" name="type" value="${user.type!1}">
          <!-- 科目-->
          <div class="form-group" >
          
		</div>
          
          
          
          <div class="modal-footer">        
            <button type="button" class="btn btn-primary">保存</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
          </div>
         		  
		</form> 
	  </div> 
    </div>
  </div>
</div>
