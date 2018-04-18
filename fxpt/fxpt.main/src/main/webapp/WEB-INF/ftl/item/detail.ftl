<#import "../commons/pageHead.ftl" as pageHead> 
<#import "../commons/table.ftl" as table>
<div class="page-content">
	<@pageHead.pageHead title=exam.name buttons=[{"id":"returnPage","name":"","text":"<<返回上级页面"}] />
	<div class="bs-example bs-example-tabs">
		<ul id="item-tab" class="nav nav-tabs" role="tablist">
			<#if testPapers??>
				<#list testPapers as tp>
						<li data-testPaperId="${tp.id?string("#")}" class="${(tp.id==testPaper.id)?string("active","")}">
							<a href="#item-testPaper-${tp.id?string("#")}" class="dropdown-toggle" id="menu${tp.id?string("#")}" role="tab" data-toggle="tab">${tp.name}</a>
						</li>				
				</#list>
			</#if>
		</ul>
		<div id="item-tabContent" class="tab-content">
			<#include "./itemList.ftl">
		</div>
	</div>


</div>