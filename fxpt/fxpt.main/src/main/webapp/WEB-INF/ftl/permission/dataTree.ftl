[
<#if treeNodes??>
	<#list treeNodes as node>
		<#----{"id":"${node.id}","name":"${node.name}","isParent":${node.isParent?string("true","false")}},-->
	</#list>
</#if>
]