<#import "../../commons/pageHead.ftl" as pageHead> 
<#import "../../commons/table.ftl" as table>

<div class="page-content">
<@pageHead.pageHead title="导出分数设置" buttons=[{"id":"returnUperPage","name":"","text":"<<返回上级页面"}]/>
<#assign tableNames = ["文理分科","学科","学科满分","Z分数权重(%)","操作"]>
<@table.table tableId="standardScoreTable" head=tableNames css="editTable">
	<#list resMap?keys as key>
		<#list resMap[key] as res>
			<tr>
				<#if res_index == 0>
					<td rowspan="${resMap[key]?size}">
						<#if key=="1">
							理科
						<#elseif key =="2">
							文科
						<#else>
							未分科
						</#if>
					</td>
				</#if>
				<td>${res[2]}</td>
				<td>${res[5]}</td>
				<td>${res[6]!}</td>
			  <#if res_index == 0>
				<td rowspan="${resMap[key]?size}"><a href="#" title="修改" subjectid="${res[1]}" examid="${res[4]}" wl="${key}" class="update">修改</a></td>
			  </#if>
			</tr>
			</#list>
		</#list>
</@table.table>
</div>