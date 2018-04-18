<#import "../commons/dialog.ftl" as roleDialog>
<#assign title="成员管理--${(role.name)!}">
<#assign footBtns=[{"text":"确认","id":"manageGo","class":"btn-primary","closeBtn":false},
				  {"text":"取消","id":"","class":"btn-default","closeBtn":true}]>
<#assign dialogClass="big-modal ztree-modal">
<#assign hiddens=[{"name":"pk","id":"roleId","value":"${(role.pk)!}","class":"myData"}]>
<@roleDialog.dialog title=title footBtns=footBtns  showHeader=true showFooter=true hiddens=hiddens dialogClass=dialogClass >
  <div id="role_datagrid1" class="col-xs-6 col-md-6 col-sm-6 data-grid" style="height:450px;overflow:auto;" >
    <table class="table table-bordered table-striped table-hover" id="leftTable">
      <thead>
        <tr>
          <th>未选用户(红色：刚移出的用户)</th>
        </tr>
      </thead>
      <tbody id="user_role_rows1">
      <#if userList??>
        <#list userList as users>
        <tr id="l${users.pk}">
          <td>
          <a href='javascript:void(0);' trigger='selectUser' pk="${users.pk}" userName="${users.name}" text-decoration="none">${users.name}</a>
          </td>
        </tr>
        </#list>
      <#else>  
        <tr>
          <td></td>
        </tr>
      </#if> 
      </tbody>
    </table>
   <!-- <#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize}> -->

    <!-- <#import "../commons/pager.ftl" as myPage>  -->
    <!-- <@myPage.pager id="user_role_page" pager=pager/>   -->
  </div>
  
  
    <div id="role_datagrid2" class="col-xs-6 col-md-6 col-sm-6 data-grid" style="height:450px;overflow:auto;">
    <table class="table table-bordered table-striped table-hover" id="selectTable">
      <thead>
        <tr>
          <th>已选用户(紫色：刚移进的用户)</th>
        </tr>
      </thead>
       <tbody id="user_role_rows2">
	      <#if query.results??>
	        <#list query.results as user>
	        <tr id="${user.pk}">
	          <td>
	          <a href='javascript:void(0);' trigger='removeUser' pk="${user.pk}" userName="${user.name}" text-decoration="none">${user.name}</a>
	          </td>
	        </tr>
	        </#list>
	      <#else>  
	        <tr>
	          <td></td>
	        </tr>
	      </#if>
      </tbody>
    </table>
  </div>
    
</@roleDialog.dialog>