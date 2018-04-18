<div class="page-content">
  <!-- 表单头-->
	<div class="page-header">
		<h1>
	    	<i class="icon-hand-right"></i>角色授权<#if (role.name)??> >${(role.name)!} </#if>
	      	<div class="pull-right">
	            <button class="btn btn-primary" type="button" trigger="goback" id="t_goback" name=""> <<返回上级页面</button>
			</div>
	    </h1>
	</div>
	<input type="hidden" id="roleId" value="${(role.pk)!}">
	<div class="row">	
	   <div class="form-group" style="margin:0 -15px 0 1px;padding:0px;height:500px;">
	        <div class="col-sm-4" style="overflow:auto;">
				<div class="modal-lefttree">
				   <ul id="leftModalztree" class="ztree modal-ztree-checkbox" style="height:500px;" ></ul>
				</div>
			</div>	
			<div class="col-sm-8" style="overflow:auto;height:500px;width:65%;">
			 <#--根据批量授权 决定是否显示角色列表-->
					<#if !role??>
						<#import "../commons/searchBar.ftl" as searchBar>
						<#assign searchBarId="searchBtn">
						<#assign checkbox={"headName":"allChecked","name":"roleChecked"}>
						<@searchBar.searchBar showSearchBar=true showTab=false searchBarId=searchBarId showPlaceholder="按角色名称查询">
							<#import "../commons/searchTable.ftl" as searchTable>
							<#assign headers=["角色编号","角色名称","角色描述","是否有效"]>
							<#assign rows=[]>
							<#if roles??>
						    <#list roles as role>
						      <#assign cells=[role.pk!"",role.code!"",role.name!"",role.desc!"",role.available?string("有效","无效")]>
						      <#assign rows=rows+[cells]>
						    </#list>
							</#if>
							<#assign hasFirstCheckbox=true>
							<#assign bodyId="roleRows">
							<@searchTable.searchTable  rows=rows headers=headers hasFirstCheckbox=hasFirstCheckbox bodyId=bodyId checkbox=checkbox/>
						</@searchBar.searchBar>
					</#if>
			 
			 </div>
		 </div>
		 <div style="display:table;width:auto;margin-left:auto;margin-right:auto;">
			      <button class="btn btn-primary" type="button" id="grantBtn" name="">授权</button>
			      <button class="btn btn-default" type="button" id="cancelBtn" name="">取消</button>
		</div>
	</div>
</div>