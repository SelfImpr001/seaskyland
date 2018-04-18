<#import "../commons/pageHead.ftl" as pageHead> 
<#import "../commons/table.ftl" as table>
<#import "../etlLog/logList.ftl" as logList>

<div class="page-content">
	<@pageHead.pageHead title="考试基本信息" buttons=[{"id":"returnPage","name":"","text":"<<返回上级页面"}]/>
	<#include "../exam/examInfo.ftl">
	<@pageHead.pageHead title="文件导入列表"/>
<@table.table tableId="statSchoolPersonList" head=["导入日期","导入人","文件名称","操作"]>
  <#list fileList as file>
	<tr>
		<td>${file.importTime?string(" yyyy-MM-dd")}</td>
		<td>${file.importer}</td>
		<td>${file.fileName}</td>
		<td>
		<a href="#" title="" objectId="${file.fileId}" class="impCj-downLoad">下载</a></td>
	</tr>
</#list>
  </@table.table>
</div>