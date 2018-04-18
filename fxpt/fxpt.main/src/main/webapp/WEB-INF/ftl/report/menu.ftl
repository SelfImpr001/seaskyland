<#import "childMenu.ftl" as childMenu>

<#if menus??>
  <#list menus as menu>
  <li>
    <a id="reportEntry" href="javascript:void(0);" biurl="${menu.url!""}" uuid="${menu.uuid}"><i class="icon-th"></i><span class="menu-text"> ${menu.alias} </span>
	<#if menu.hasChild>
	  <b class="arrow icon-angle-down"></b>
	</#if>
	</a>
	<#if menu.hasChild>
	  <@childMenu.childMenu menus=menu.children/>
    </#if>
  </li>
  </#list>
</#if>
