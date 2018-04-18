<#import "../commons/dialog.ftl" as roleDialog>
<#assign title="组织${(role.name)!}包含的用户">
<#assign footBtns=[{"text":"关闭","id":"","class":"btn-default","closeBtn":true}]>
<#assign dialogClass="big-modal ztree-modal">
<#assign hiddens=[{"name":"pk","id":"roleId","value":"${(role.pk)!}","class":"myData"}]>
<@roleDialog.dialog title=title footBtns=footBtns  showHeader=true showFooter=true hiddens=hiddens dialogClass=dialogClass >
 <div>
  <div id="role_datagrid" class="col-xs-12 col-md-12 col-sm-12 data-grid">
    <table class="table table-bordered table-striped table-hover">
      <thead>
        <tr>
          <th>用户名</th>
          <th>姓名</th>
          <th>手机</th>
          <th>邮箱</th>
          <th>状态</th>
        </tr>
      </thead>
      <tbody id="user_role_rows">
      <#if query.results??>
        <#list query.results as user>
		  <#assign statusText="可用">
	      <#if user.status=2>
	        <#assign statusText="禁用">
	      </#if>
		  <#if user.status=3>
	        <#assign statusText="锁定">
	      </#if>         
        <tr>
          <td>${user.name}</td>
          <td>${(user.userInfo.realName)!"--"}</td>
          <td>${(user.userInfo.cellphone)!"--"}</td>
          <td>${(user.userInfo.email)!"--"}</td>
          <td>${statusText}</td>
        </tr>
        </#list>
      <#else>  
        <tr>
          <td>--</td>
          <td>--</td>
          <td>--</td>
          <td>--</td>
          <td>--</td>
          <td>--</td>
        </tr>
      </#if> 
      </tbody>
    </table>
    <#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize}>

    <#import "../commons/pager.ftl" as myPage>
  </div>
   </div>
    <@myPage.pager id="user_role_page" pager=pager/>    
</@roleDialog.dialog>