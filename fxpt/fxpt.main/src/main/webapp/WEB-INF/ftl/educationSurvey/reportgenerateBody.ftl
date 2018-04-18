<#if page.list??>
	<#list page.list as report>
	<tr>
		<td>${report.monitorName}</td>
		<td>${report.monitorSchoolYear}</td>
		<td>${report.monitorSemester!""}</td>
		<td>${report.monitorType}</td>
		<td>${report.monitorDate?string("yyyy-MM-dd")}
		</td>
		<td>
			<a href="javascript:void(0);"  title="查看报告" value="${report.id!''}" trigger="monitorHref" class="analysisExam icon-search"></a>
		</td>
	</tr>
	</#list>
 </#if>
 <input type="hidden" id="pageNum" value="${page.curpage}"> 
 <input type="hidden" id="pageCount" value="${page.totalpage}">

