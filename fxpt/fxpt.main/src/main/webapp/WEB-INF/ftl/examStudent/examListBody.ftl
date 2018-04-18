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
	<td>${exam.hasExamStudent?string("已导入","未导入")}</td>
	<td>
	<!--<a href="#" title="" objectId="${exam.id}" class="impStu-importStudent">导入</a>-->
	<a href="#" title="导入" objectId="${exam.id}" class="impStu-appendImportStudent icon-cloud-upload text-success"></a>
	<#if exam.hasExamStudent!=true>
	<span class="separator">|</span>
	<a href="#" title="转入" objectId="${exam.id}" class="impStu-rollInStudent icon-share-alt text-primary"></a>
	</#if>
	<#if exam.hasExamStudent>
	<span class="separator">|</span>
	<a href="#" title="查看" objectId="${exam.id}" class="impStu-view icon-search text-danger"></a>
	<span class="separator">|</span>
	<a href="#" title="导入文件管理" objectId="${exam.id}" class="impStu-viewFile icon-folder-close-alt text-warning"></a>
	</#if>
	</td>
</tr>
</#list>
</#if>
<input type="hidden" id="pageNum" value="${page.curpage}"> 
<input type="hidden" id="pageCount" value="${page.totalpage}">
