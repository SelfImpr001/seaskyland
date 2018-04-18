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
	<td>
		<#if (exam.impItemCount > 0)>
			已导入
		<#else>
			未导入
		</#if>
	</td>
	<td>
	<a href="#" title="导入" objectId="${exam.id}" class="impItem-importItem icon-cloud-upload text-success"></a>
	<span class="separator">|</span>
	<a href="#" title="批量导入" objectId="${exam.id}" class="impItem-multiImportItem icon-upload-alt text-primary"></a>
	<#if (exam.impItemCount > 0)>
	<span class="separator">|</span>
	<a href="#" title="查看" objectId="${exam.id}" class="impItem-view icon-search text-danger"></a>
	</#if>
	</td>
</tr>
</#list>
</#if>
<input type="hidden" id="pageNum" value="${page.curpage}"> 
<input type="hidden" id="pageCount" value="${page.totalpage}">
