<#-- 点击树节点获取的列表      勿删-->
<table>
<#list orgList![] as org>
	  
      <#assign typeText="未启用">
      <#if org.available>
      　　　　<#assign typeText="已启用">
      </#if>
	<tr>
	  <td width="15%" style="text-align:center;vertical-align:middle">${org.code!}</td>
	  <td width="15%" style="text-align:center;vertical-align:middle">${org.name!}</td>
	  <td width="45%" style="text-align:center;vertical-align:middle">${(org.searchPoint)!}</td>
	  <td width="10%" style="text-align:center;vertical-align:middle">${typeText}</td>
	  <td width="15%" style="text-align:center;vertical-align:middle">
	    <div class="btn-group">
	      <a href="javascript:void(0);" pk=${org.pk?c! ""} trigger="update">修改</a>
	      <span class="separator">|</span>
	      <a href="javascript:void(0);" pk=${org.pk?c! ""} trigger="remove">删除</a>     
	    </div>
	  </td>
	</tr>
</#list>
</table>
<#if query??>
  <#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize}>
  <div >
	  <div style="text-align: center">
		<input type="hidden" id="pageNum" value="${pager.curpage}"> 
		<input type="hidden" id="pageCount" value="${pager.totalpage}">
		<input type="hidden" id="pagesize" value="${pager.pagesize}">
		<input type="hidden" id="totalRows" value="${pager.totalRows!0}">
		<div id="org_pager" class="pager" style="margin-top: 20px; text-align: center"></div>
	  </div>  
  </div>
</#if>