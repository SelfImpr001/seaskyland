<#if page.list??>
<#list page.list as exam>
<tr>
	<td>${exam.examDate?string("yyyy-MM-dd")}</td>
	<td>${exam.name}</td>
	<td>${exam.sortName!""}</td>
	<td>
		<#if exam.status==0>
			未分析
		<#elseif exam.status==1>
			分析成功
		<#elseif exam.status==2>
			正在分析<div class='progress-warp' examId='${exam.id}'></div>
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
	<td>
		${exam.hasExamStudent?string("已导入","未导入")}
	</td>
	<td>
		${(exam.impItemCount>0)?string("已导入","未导入")}
	</td>
	<td>
		${(exam.impCjCount>0)?string("已导入","未导入")}
	</td>
	<td>
	
	<#if exam.status==2>
		正在分析...
	<#elseif exam.status==6>
		等待分析...
	<#else>
		<a href="#" title="分析" objectId="${exam.id}" class="analysisExam icon-bar-chart text-success"></a>
	</#if>
	</td>
</tr>
</#list>
</#if>
<input type="hidden" id="pageNum" value="${page.curpage}"> 
<input type="hidden" id="pageCount" value="${page.totalpage}">
