<#if page.list??>
<#list page.list as stat>
	<tr>
		<td>${stat.cityName}</td>
		<td>${stat.countyName}</th>
		<td>${stat.schoolName}</th>
		<td><a href="#" id="studentList" schoolCode="${stat.schoolCode}">${stat.num?string("#")}</a></td>
	</tr>
</#list>
</#if>
<input type="hidden" id="pageNum" value="${page.curpage}"> 
<input type="hidden" id="pageCount" value="${page.totalpage}">

