<#if page.list??>
	<#list page.list as stat>
		<tr>
			<td>${stat.cityName}</td>
			<td>${stat.countyName}</td>
			<td>${stat.schoolName}</td>
		
			<#if testPapers??>
				<#list 	testPapers as tp>
					<td><a href="#" class="cj-stat-list" data-schoolId="${stat.schoolId}" data-testPaperId="${tp.id}">${stat['skrs'+tp.id]}/${stat['bkrs'+tp.id]-stat['skrs'+tp.id]}/${stat['bkrs'+tp.id]}</a></td>
				</#list>		
			</#if>
						
		</tr>
	</#list>
</#if>
<input type="hidden" id="pageNum" value="${page.curpage}"> 
<input type="hidden" id="pageCount" value="${page.totalpage}">