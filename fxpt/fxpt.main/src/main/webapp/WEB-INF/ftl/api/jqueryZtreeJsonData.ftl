[
<#if treeNodes??>
  <#list treeNodes as node>
	{"id":"${node.id}","code":"${node.code}","name":"${node.name}","parentCode":"${node.parentCode}","type":${node.type},"isParent":${node.isParent?string("true","false")}},
  </#list>
</#if>
]
<#--- {"id":"${node.id}","code":"${node.code}","name":"${node.name}","parentCode":"${node.parentCode}","type":${node.type},"isParent":${node.isParent?string("true","false")}},--->
<#---
	{
	<#list node?keys as key>
	"${key}":"${node[key]?c}"
	</#list>
	},

-->