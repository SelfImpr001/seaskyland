[
<#if treeNodes??>
	<#list treeNodes as myNode>
		{"code":"${myNode.code}","name":"${myNode.name}","parentCode":"${myNode.parentCode}","type":${myNode.type},"isParent":${myNode.isParent?string("true","false")}},
	</#list>
</#if>
]
