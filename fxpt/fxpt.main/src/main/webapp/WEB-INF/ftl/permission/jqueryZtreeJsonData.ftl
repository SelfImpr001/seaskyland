[
<#if treeNodes??>
  <#list treeNodes as node>
    {
    <#list node?keys as key>
    "${key}":"${node[key]}"
    </#list>		
	},
	<#--{"id":"${node.id}","code":"${node.code}","name":"${node.name}","parentCode":"${node.parentCode}","type":${node.type},"isParent":${node.isParent?string("true","false")}},-->
  </#list>
</#if>
]