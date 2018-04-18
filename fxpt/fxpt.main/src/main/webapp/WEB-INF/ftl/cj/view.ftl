<#import "../commons/pageHead.ftl" as pageHead> 
<#import "../commons/table.ftl" as table>
<#import "../etlLog/logList.ftl" as logList>

<div class="page-content">
	<@pageHead.pageHead title="考试基本信息" buttons=[{"class":"cj-appendBatchImport","icon":"","text":"导入"},{"id":"returnPage","name":"","text":"<<返回上级页面"}]/>
	<#include "../exam/examInfo.ftl">
	<@pageHead.pageHead title="成绩信息统计列表"/>
	<@table.table tableId="testPapersList" head=["试卷","考试/缺考/总计","操作"]>
    </@table.table>
	<@pageHead.pageHead title="操作历史记录"/>
	<@logList.list logs=[]/>
</div>