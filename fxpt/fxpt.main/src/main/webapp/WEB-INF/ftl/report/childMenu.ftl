<#macro childMenu menus>
<ul class="submenu">
<#list menus as menu>
  <li>
	<a href="javascript:void(0);"  <#if menu.url??>resid="${menu.url!""}"</#if> <#if !menu.hasChild>class="myReport"</#if> title="${menu.alias}"><i class="icon-double-angle-right"></i>${menu.alias}
	  
	<#if menu.hasChild>
	  <b class="arrow icon-angle-down"></b>
	</#if>
	</a>
	<#if menu.hasChild>
	  <@childMenu menus=menu.children/>
  	</#if>
  </li>
</#list>
</ul>
</#macro>