<#-- 点击树节点获取的列表      勿删-->
<table>
  <tbody class="sortables">
<#list orgList![] as org>
	  
  
	<tr> 
	<td><input name="myChecked" value="${org.pk!}" type="checkbox" class="ace"> <span class="lbl"></span> </td>
	  
	
	  <td>${org.name!}</td>
	
	 <td>
	    <div class="btn-group">
	    
	      <a href="javascript:void(0);" pk=${org.pk?c! ""} trigger="update">修改组织</a>  
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
		<div id="birtorg_pager" class="pager" style="margin-top: 20px; text-align: center"></div>
	  </div>  
  </div>
</#if>