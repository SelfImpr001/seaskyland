<#if query.results??>
<#list query.results as exam>
	<tr>
		<td>${exam.examDate?string("yyyy-MM-dd")}</td>
		<td>${exam.name}</td>
		<td>${exam.sortName!""}</td>
		<td>${exam.ownerName!""}</td>
		<td>${exam.createUserName!""}</td>
		<td><#if exam.status==0>
					未分析
				<#elseif exam.status==1>
					分析成功
				<#elseif exam.status==2>
					正在分析
				<#elseif exam.status==3>
					分析失败
				<#elseif exam.status==5>
					已发布
				<#elseif exam.status==6>
					等待分析
				<#elseif exam.status==11>
					正在生成报告
				</#if>
		</td>
	</tr>
</#list>
</#if>
<input type="hidden" id="pageNum" value="${query.curpage}"> 
<input type="hidden" id="pageCount" value="${query.totalpage}">
