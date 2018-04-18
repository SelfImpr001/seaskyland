<#import "../commons/pageHead.ftl" as pageHead> 
<#import "../commons/table.ftl" as table>
<#import "../etlLog/logList.ftl" as logList>

<div class="page-content">
	<@pageHead.pageHead title="考试基本信息" buttons=[{"class":"impStu-appendImportStudent","icon":"","text":"导入学生信息"},{"id":"deleteStudentInfo","icon":"","text":"删除学生信息"},{"id":"returnPage","name":"","text":"<<返回上级页面"}]/>
  	<#include "../exam/examInfo.ftl">
  <@pageHead.pageHead title="学生信息统计列表"/>
  <@table.table tableId="statSchoolPersonRexamList" head=["市","区","学校 (共有学校：${totalSchoolNum} 所)","学生人数 (共有学生：${totalNum} 人)"]>
		<#include "./statSchoolPersonTBody.ftl">
  </@table.table>
   <@pageHead.pageHead title="操作历史记录"/>
   <@logList.list logs=etlLogs/>
  
  <input type="hidden" id="examImportCjCount" value="${exam.impCjCount}" />
</div>