<div class="page-content">
  <!-- 表单头-->
	<div class="page-header">
		<h1>
	    	<i class="icon-hand-right"></i>用户批量权限分配
	      	<div class="pull-right">
	            <button class="btn btn-primary" type="button" trigger="goback" id="to_goback" name=""> <<返回上级页面</button>
			</div>
	    </h1>
	</div>
	<input type="hidden" id="userId" value="${(user.pk)!}">
	 <div class="row">
				<#import "../commons/searchBar.ftl" as searchBar>
				<#assign searchBarId="batchSearch">
				<#assign tabs=[{"href":"#user","name":"系统用户","id":"user"}]>
				<@searchBar.searchBar showSearchBar=true showTab=true searchBarId=searchBarId tabs=tabs>
					
					<#--用户列表-->
					<#import "../commons/searchTable.ftl" as userTable>
					<#assign userHeaders=["用户名","姓名","手机","邮箱"]>
					<#assign  userRows=[]>
					<#if users.results??>
					    <#list users.results as user>    
					      <#assign userCells=[user.pk,user.name,user.userInfo.realName,user.userInfo.cellphone!"--",user.userInfo.email!"--"]>
					      <#assign userRows=userRows+[userCells]>
					    </#list>
					</#if>
					<#assign userBodyId="user-rows">
					<#assign pager={"curpage":users.curpage,"totalpage":users.totalpage,"pagesize":users.pagesize}>
					<@userTable.searchTable  rows=userRows headers=userHeaders hasFirstCheckbox=hasFirstCheckbox 
					bodyId=userBodyId showTab=true checkbox={"headName":"userAllChecked","name":"userChecked"}
				    tab={"id":"user","class":"tab-pane active"} showPager=true pager=pager />
				</@searchBar.searchBar>
			    <form id="userEditForm" role="form" action="" class="form-horizontal ">
			   <div class="form-group" style="margin:0 -15px 0 1px;padding:0px;">
					<div class="col-sm-6" style="overflow:auto;">
					  <ul id="roleztree" class="ztree modal-ztree-checkbox" style="height:280px;padding: 10px 10px;"></ul>
					  <ul id="rpztree" style="display:none;"></ul>
					</div>
					<div class="col-sm-6" style="overflow:auto;">
					  <ul id="userztree" class="ztree modal-ztree-checkbox" style="height:280px;padding: 10px 10px;margin-right: 1px;overflow:;"></ul>
					</div>	
				    <div style="display:table;width:auto;margin-left:auto;margin-right:auto;">
					      <button class="btn btn-primary" type="button" id="grantBtn" name="">授权</button>
					      <button class="btn btn-default" type="button" id="cancelBtn" name="">取消</button>
					</div>
				</div>
				</form>
		</div>
</div>