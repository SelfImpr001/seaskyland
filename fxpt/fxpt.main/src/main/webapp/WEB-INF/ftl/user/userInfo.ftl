<div class="row form-horizontal">
	<table class="table table-bordered">
		<tbody>
			<tr>
				<td>
					<div class="form-group">
						<label class="col-sm-1 control-label">用户名</label>
						<div class="col-sm-5"><p class="form-control-static"><#if user.name??>${user.name!''}</#if></p></div>
						<label class="col-sm-1 control-label">姓名</label>
						<div class="col-sm-5"><p class="form-control-static"><#if user.userInfo??>${user.userInfo.realName!''}</#if></p></div>
					
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form-group">
						<label class="col-sm-1 control-label">性别</label>
						<div class="col-sm-5"><p class="form-control-static"><#if user.userInfo??><#if user.userInfo.sex="UNKNOW">未知</#if>
						                                                     <#if user.userInfo.sex="MALE">男</#if>
						                                                      <#if user.userInfo.sex="FEMALE">女</#if></#if></p></div>
						<label class="col-sm-1 control-label">状态</label>
						<div class="col-sm-5"><p class="form-control-static"><#if user.status=1>启用</#if>
						                                                      <#if user.status=2>禁用</#if>
						                                                      <#if user.status=3>锁定</#if></p></div>
					
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form-group">
						<label class="col-sm-1 control-label">手机</label>
						<div class="col-sm-5"><p class="form-control-static"><#if user.userInfo??>${user.userInfo.cellphone!''}</#if></p></div>
						<label class="col-sm-1 control-label">固定电话</label>
						<div class="col-sm-5"><p class="form-control-static"><#if user.userInfo??>${user.userInfo.telphone!''}</#if></p></div>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="form-group">
						<label class="col-sm-1 control-label">邮箱</label>
						<div class="col-sm-5"><p class="form-control-static"><#if user.userInfo??>${user.userInfo.email!''}</#if></p></div>
						<label class="col-sm-1 control-label">组织关系</label>
						 <#assign names="">
		                 <#list ubelongs![] as belong>
		              		 <#assign names=names + "," + belong.org.name >
		                 </#list>
		                 <#if ubelongs?size !=0 >
		              	     <#assign names= names?substring(1)>
		                 </#if>
              <div class="col-sm-5"><p class="form-control-static">${names!''}</p></div>
					</div>
				</td>
			</tr>
			
		</tbody>
	</table>
</div>