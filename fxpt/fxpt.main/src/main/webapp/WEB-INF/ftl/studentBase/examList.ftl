<#import "../commons/pageHead.ftl" as pageHead>
<#import "../commons/querybar.ftl" as querybar>
<#import "../commons/table.ftl" as table>
<#import "../commons/grid.ftl" as studentBase>

<div class="page-content">
 <#assign btns=[{"text":"导入","id":"saveExams","class":"btn-primary","closeBtn":false},{"text":"<<返回上级页面","id":"returnSb","class":"btn-primary","closeBtn":false}]>
<@pageHead.pageHead title="考试学生导入" buttons=btns/>
<#assign headers=["考试日期","考试名称","考试简称","学年","学期","年级","考试类型","导入学生状态"]>
<#assign pager={"curpage":page.curpage,"totalpage":page.totalpage,"pagesize":page.pagesize,"totalRows":page.totalRows}>
<#assign rows=[]>
<#assign optBtn=[]>
<#assign checkbox=[]>
	<!--页面内容部分-->
	<#if page.list??>
			<#list page.list as exam>
			 	<#assign cells=[exam.id,(exam.examDate)?string("yyyy-MM-dd"),exam.name,exam.sortName,(exam.schoolYear-1)?string("#")+"-"+(exam.schoolYear)?string("#")+"学年",(exam.schoolTerm==1)?string("上学期","下学期"),exam.grade.name,exam.examType.name,(exam.hasExamStudent==true)?string("已导入","未导入")]>
				<#assign rows=rows+[cells]>
			</#list>
	</#if>
 <@studentBase.grid headers=headers rows=rows buttons=[] pager=pager showPager=true hasFirstCheckbox=true showSeq=false checkedBoxName="examChecked" idPreFix="studentBase"/>
</div>
