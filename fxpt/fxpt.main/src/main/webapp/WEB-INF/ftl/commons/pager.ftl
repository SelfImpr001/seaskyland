<#macro pager id="pager" pager={"curpage":"1","totalpage":"0","pagesize":"15","totalRows":"0"}>
<div style="text-align: center">
  <input type="hidden" id="pageNum" value="${(pager.curpage)!1}"> 
  <input type="hidden" id="pageCount" value="${(pager.totalpage)!0}">
  <input type="hidden" id="pagesize" value="${(pager.pagesize)!15}"> 
  <input type="hidden" id="totalRows" value="${(pager.totalRows)!0}">
  <div id="${id}" class="pager" style="margin-top: 20px; text-align: center"></div>
</div>
</#macro>