<#macro childMenu menus>
<ul class="submenu">
		<#list menus as menu>
			<li><a menu="${menu.eventCode!""}" uuid="${menu.uuid!""}" href="${menu.url!"javascript:void(0);"}"><i class="icon-double-angle-right"></i><span class="menu-text"> ${menu.alias} </span>
			<#if menu.hasChild>
	  			  	<b class="arrow icon-angle-down"></b>
	  			</#if></a>
			<#if menu.hasChild>
				<@childMenu menus=menu.children/>
		  	</#if></li>
		</#list>
</ul>
</#macro>