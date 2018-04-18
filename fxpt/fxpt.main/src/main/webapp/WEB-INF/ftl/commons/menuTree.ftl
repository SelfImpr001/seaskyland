<#macro mTree nodes=[] idPrefix="m">
<#list nodes as node>
    <li >
	  <a id="${idPrefix+"_" + node_index}" menu="${node.name!""}" href="javascript:void(0);" url="${node.url!'#'}" 
		  <#if node.eventCode??> entry="${node.eventCode}"</#if> 
		  <#if node.children??>class="dropdown-toggle"</#if>
		>
		
		<#if node.childParent??> ${node.name!} <#else><i class="${node.icon!}"></i><span class="" title=${node.name!}> ${node.name!}</span></#if>
		<#if node.hasChild><b class="arrow icon-angle-right"></b></#if>
	  </a>
		
	  <#if node.children??>
		 <ul class="submenu">
			<@mTree node.children idPrefix+"_"+node_index/>
		 </ul>
	  </#if>
    </li>
</#list>
</#macro>