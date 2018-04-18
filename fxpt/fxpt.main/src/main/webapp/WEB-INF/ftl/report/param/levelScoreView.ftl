<#import "../../commons/pageHead.ftl" as pageHead> 
<#import "../../commons/table.ftl" as table>

<div class="page-content">
<@pageHead.pageHead title="等级设置及赋分" buttons=[{"id":"returnUperPage","name":"","text":"<<返回上级页面"}]/>
<#assign tableNames = ["文理分科","学科","划分等级","等级序号","起始分数","结束分数","等级类别","等级分","操作"]>
<@table.table tableId="levelScoreTable" head=tableNames css="editTable">
	<#list lssMap?keys as key>
		<#list lssMap[key] as wls>
			<tr>
				<#if wls_index == 0>
					<td rowspan="${lssMap[key]?size}">
						<#if key=="1">
							理科
						<#elseif key =="2">
							文科
						<#else>
							未分科
						</#if>
					</td>
				</#if>
				<td>${wls.analysisTestpaper.name}</td>
				<td>${wls.leveScoreSettings?size}</td>
				<td>
					<#list wls.leveScoreSettings as lss>
						${lss_index+1}</br>
					</#list>
				</td>
				<td>
					<#list wls.leveScoreSettings as lss>
						${lss.beginScore}</br>
					</#list>
				</td>
				<td>
					<#list wls.leveScoreSettings as lss>
						${lss.endScore}</br>
					</#list>
				</td>
				<td>
					<#list wls.leveScoreSettings as lss>
						${lss.levelName}</br>
					</#list>
				</td>
				<td>
					<#list wls.leveScoreSettings as lss>
						${lss.levelScore}</br>
					</#list>
				</td>
				<td><a href="#" title="修改" objectId="${wls.analysisTestpaper.id}" data-wl="${key}" class="update icon-edit text-success"></a></td>
			</tr>
			</#list>
		</#list>
	
</@table.table>
</div>