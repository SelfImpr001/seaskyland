<#import "../commons/pageHead.ftl" as pageHead> 
<#import "../commons/table.ftl" as table>
<#import "../etlLog/logList.ftl" as logList>

<div class="page-content">
	<@pageHead.pageHead title="考试基本信息" buttons=[{"class":"impItem-importItem","icon":"","text":"导入细目表"},{"class":"impItem-multiImportItem","icon":"","text":"批量导入"},{"id":"returnPage","name":"","text":"<<返回上级页面"}] />
	<#include "../exam/examInfo.ftl">
	<@pageHead.pageHead title="双向细目表统计列表"/>
	<@table.table tableId="testPapersList" head=["试卷","满分","操作"]>
		<#include "./testPaperListTBody.ftl">	
    </@table.table>
  
  <@pageHead.pageHead title="操作历史记录"/>
  <@logList.list logs=etlLogs/>
</div>