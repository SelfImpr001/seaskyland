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
		${(exam.impCjCount>0)?string("已导入","未导入")}
	</td>
	<td>
	<!-- 
	<a href="#" title="" objectId="${exam.id}" class="cj-batchImport">批量导入</a>  -->
	<a href="javascript:void(0)" title="导入" objectId="${exam.id}" class="cj-appendBatchImport icon-cloud-upload text-success"></a>
	<#if (exam.impCjCount>0)>
	<span class="separator">|</span>
	<a href="javascript:void(0)" title="查看" objectId="${exam.id}" class="cj-view icon-search text-primary"></a>
	<span class="separator">|</span>
	<a href="javascript:void(0)" title="导入文件管理" objectId="${exam.id}" class="cj-viewFile icon-folder-close-alt text-danger"></a>
	</#if>
	</td>
</tr>
</#list>
</#if>
<input type="hidden" id="pageNum" value="${page.curpage}"> 
<input type="hidden" id="pageCount" value="${page.totalpage}">
