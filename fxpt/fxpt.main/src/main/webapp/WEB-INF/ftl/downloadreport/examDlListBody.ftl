<#if page.list??>
<#list page.list as exam>
<tr>
	<td>${(exam.examDate)?string("yyyy-MM-dd")}</td>
	<td>${exam.name}</td>
	<td>${exam.sortName!""}</td>
	<td>
	<a href="#" title="${exam.id}" objectId="${exam.id}" name="examdlexcel" class="examdlexcel" >下载</a>
	<span class="separator">|</span>
	<a href="#" title="${exam.id}" objectId="${exam.id}" name="examresetting" class="examresetting" >重置</a>
	</td>
</tr>
</#list>
</#if>
<input type="hidden" id="pageNum" value="${page.curpage}"> 
<input type="hidden" id="pageCount" value="${page.totalpage}">
