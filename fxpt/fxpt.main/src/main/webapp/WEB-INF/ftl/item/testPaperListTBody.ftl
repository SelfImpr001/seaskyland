<#list testPapers as tp>
	<tr>
		<td>${tp.name}</td>
		<td>${tp.fullScore}</td>
		<td>
			<a href="#" title="" objectid="${tp.id}" class="testPaperView">查看</a>
			<span class="separator">|</span>
			<a href="#" title="" objectid="${tp.id}" data-hascj="${tp.hasCj?string("true","false")}" class="testPaperDelete">删除</a>
		</td>
	</tr>
</#list>