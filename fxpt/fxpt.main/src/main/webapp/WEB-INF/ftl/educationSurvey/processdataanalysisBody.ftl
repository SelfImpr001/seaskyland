<#if query.results??>
	<#list query.results as exam>
		<tr>
			<td>${exam.createTime!'--'}</td>
			<td>${exam.name!'--'} </td>
			<td>${exam.status!'--'}</td>
			<td>${exam.dataCollection!'--'}</td>
			<td>${exam.student!'--'}</td>
			<td>${exam.specTtem!'--'}</td>
			<td>${exam.score!'--'}</td>
			<td>
				<a href="#" title="分析" class="analysisExam icon-bar-chart text-success"></a>
			</td>
		</tr>
	</#list>
</#if>
<input type="hidden" id="pageNum" value="${query.curpage}"> 
<input type="hidden" id="pageCount" value="${query.totalpage}">

