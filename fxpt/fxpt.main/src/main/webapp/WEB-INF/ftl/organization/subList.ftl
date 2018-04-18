<#-- 点击树节点获取的列表      勿删-->
<table>
  <tbody class="sortables">
<#list orgList![] as org>
	  
      <#assign typeText="省教育厅">
      <#if org.type=2>
      　　　　<#assign typeText="市教育局">
      <#elseif org.type=3>
      　　　　<#assign typeText="县（区）教育局">
      <#elseif org.type=4>
      　　　　<#assign typeText="学校">
      </#if>
  
	<tr>
	  <td>${org.code!}</td>
	  <td>${org.name!}</td>
	  <#if (org.parent??)&&(org.parent.name??)>
	  	<td>${(org.parent.name)!}</td>
	  <#else>
	  	<td></td>
	  </#if>
	  <td>${typeText}</td>
	  <#if org.orgCount != 0>
	  <td><a href="javascript:void(0);" trigger="nextOrg" pk=${org.pk?c! ""} orgName=${org.name!""} count=${(org.orgCount)!0}>${(org.orgCount)!}</a></td>
	  <#else>
	  <td></td>
	  </#if>
	  <td><a href='javascript:void(0);'  trigger='orglist' pk=${org.pk?c! ""} orgName=${org.name!""} count=${(org.userCount)!0}>${org.userCount! ""}</a></td>
	  <td>
	    <div class="btn-group">
	      <a href="javascript:void(0);" pk=${org.pk?c! ""}   trigger="update">修改</a>
	      <span class="separator">|</span>
	      <a href="javascript:void(0);" pk=${org.pk?c! ""} type=${org.type} trigger="orgOut">导出</a>
	      <span class="separator">|</span>
	      <a href="javascript:void(0);" pk=${org.pk?c! ""} trigger="remove">删除</a>     
	    </div>
	  </td>
	</tr>
</#list>
	</tbody>
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