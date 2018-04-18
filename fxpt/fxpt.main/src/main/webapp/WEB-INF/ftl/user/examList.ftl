<#import "../commons/pageHead.ftl" as pageHead>
<#import "../commons/querybar.ftl" as querybar>
<#import "../commons/table.ftl" as table>


<div class="page-content">
	<@pageHead.pageHead title="正在授权>>${user.name}"/>
	<@table.table tableId="examList" head=["<input name='checkAll' id='checkAll' type='checkbox' style='width:15px;height:15px'>","考试日期","考试名称","考试简称","组织考试机构","考试创建人","信息状态"]>
		<#if page.list??>	
		<#list page.list as exam>
		<tr>
			<td><input name="examLists" type="checkbox"  value="${exam.id}"  ${(exam.studentBaseStatus==1)?string( "checked","")}></td>
			<td>${exam.examDate?string("yyyy-MM-dd")}</td>
			<td>${exam.name}</td>
			<td>${exam.sortName!""}</td>
			<td>${exam.ownerName!""}</td>
			<td>${exam.createUserName!""}</td>
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
		</tr>
	</#list>
	</#if>
	<input type="hidden" id="pageNum" value="${page.curpage}"> 
	<input type="hidden" id="pageCount" value="${page.totalpage}">
	</@table.table>
</div>