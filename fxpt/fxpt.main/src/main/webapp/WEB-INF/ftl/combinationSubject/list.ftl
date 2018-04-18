 <div style="text-align: right">
 	
		<a id="addCombinationSubject" href="#" title="增加试卷"> <span class="glyphicon glyphicon-plus">增加组合科目</span> </a>
			<hr/>	
	</div>
<table class="table table-bordered table-striped table-hover">
	<thead>
		<tr>
			<th  colspan="7">组合科目信息</th>
		</tr>
		<tr>
			<th>名称</th>
			<th>满分</th>
			<th>客观满分</th>
			<th>主观满分</th>
			<th>类型</th>
			<th>对应试卷</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
	<#if combinationSubjects??>
	<#list combinationSubjects as cs>
	<tr>
		<td>${cs.name!""}
			</th>
		<td>${cs.fullScore}
			</th>
		<td>${cs.kgScore}
			</th>
		<td>${cs.zgScore}
			</th>
		<td><#if cs.paperType == 1>
			理科
			<#elseif cs.paperType == 2>
			文科
			<#else>
			所有学生
			</#if>
			</th>
		<td><#list cs.childTestPaper as csXtp>
			${csXtp.testPaper.name},
			</#list>
			</th>
		<td class="opera"><a href="#" title="编辑" objectId="${cs.id}" class="update"><i class="icon-edit text-success"></i></a><span class="separator">|</span> <a href="#" title="删除" objectId="${cs.id}" class="delete"><i class="icon-trash text-primary"></i></a></td>
	</tr>
	</#list>
	</#if>
	</tbody>
	
</table>
