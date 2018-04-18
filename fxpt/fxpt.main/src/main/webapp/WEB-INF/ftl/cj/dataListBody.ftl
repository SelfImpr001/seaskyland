<#if page.list??>
<#list page.list as dataSum>
<tr>
	<td>${dataSum.examDate?string("yyyy-MM-dd")}</td>
	<td>${dataSum.examName}</td>
	<td>${dataSum.allNum}</td>
	<td>${dataSum.subjectName}</td>
	<td>${dataSum.subjectNum}</td>
</tr>
</#list>
</#if>
<input type="hidden" id="pageNum" value="${page.curpage}"> 
<input type="hidden" id="pageCount" value="${page.totalpage}">