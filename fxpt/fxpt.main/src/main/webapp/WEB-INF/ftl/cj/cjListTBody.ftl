<#if page.list??>
	<#list page.list as cj>
		<tr>
		  <#if map.zkzh==1><td>${cj.zkzh!""}</td></#if>
          <#if map.isQk==1><td>${(cj.isQk==0)?string("否","是")}</td></#if>
          <#if map.totalScore==1><td>${cj.totalScore!""}</td></#if>
          <#if map.kgScore==1><td>${cj.kgScore!""}</td></#if>
          <#if map.zgScore==1><td>${cj.zgScore!""}</td></#if>
          <!--
          <#if map.omrstr==1><td>${cj.omrstr!""}</td></#if>
          <#if map.scorestr==1><td>${cj.scorestr!""}</td></#if>
          -->
			<#if items??>
	          <#list items as item>
	           <#if item.optionType==0>
	            <td>${cj['score'+item.id]}</td>
	           <#else>
	            <td>${cj['score'+item.id]}</td>
	            <td>${cj['selOpt'+item.id]}</td>
	           </#if>  		
	          
	         </#list>
	        </#if> 						
		</tr>
	</#list>
</#if>
<input type="hidden" id="pageNum" value="${page.curpage}"> 
<input type="hidden" id="pageCount" value="${page.totalpage}">