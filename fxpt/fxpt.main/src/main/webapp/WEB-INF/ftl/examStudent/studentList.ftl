<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<#import "../commons/grid.ftl" as studentList>
<#import "../commons/contentTop.ftl" as userContentTop>
<#import "../commons/pageHead.ftl" as pageHead>
<#import "../commons/table.ftl" as table>
<div class="page-content">

<#assign btns=[{"text":"<<返回上级页面","id":"returnPage","class":"btn-primary","closeBtn":false}]>
<@pageHead.pageHead title="学生信息列表" buttons=btns/>

<@table.table tableId="studentList">
     <thead>
		<tr>
	    	<th>序号</th>
	   		<#if map.studentId==1><th>学号</th></#if>
	    	<#if map.name==1><th>姓名</th></#if>
	    	<#if map.domicile==1><th>户籍</th></#if>
	    	<#if map.nation==1><th>民族</th></#if>
	    	<#if map.zkzh==1><th>准考证号</th></#if>
	    	<#if map.learLanguage==1><th>语言</th></#if>
	    	<#if map.wl==1><th>文理</th></#if>
	    	<#if map.isPast==1><th>是否应届</th></#if>
	    	<#if map.isTransient==1><th>是否借读</th></#if>
	    	<#if map.studentType==1><th>学生类别</th></#if>
	    	<#if map.languagePattern==1><th>语言模式</th></#if>
	    	<#if map.languageType==1><th>语种</th></#if>
	    	<#if map.area==1><th>区域</th></#if>
			<#if map.gender==1><th>性别</th></#if>
			<#if map.schoolCode==1><th>学校代码</th></#if>
			<#if map.schoolName==1><th>学校名称</th></#if>
		    <#if map.classCode==1> <th>班级代码</th></#if>
			<#if map.className==1> <th>班级名称</th></#if>
		</tr>
	 </thead>
<#include "./studentListBody.ftl">
</@table.table>
</div>
<input type="hidden" id="schoolCode" value="${schoolCode}"/>
