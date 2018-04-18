<#list testPapers as tp>
		<tr>
			<td>${tp.name}</td>
			<td>${tp.skrs}/${tp.bkrs-tp.skrs}/${tp.bkrs}</td>
			<td>
				<a href="#" title="" objectid="${tp.id}" class="view">查看</a>
				<span class="separator">|</span>
				<a href="#" title="" objectid="${tp.id}" class="delete">删除</a>
			</td>		
		</tr>
</#list>