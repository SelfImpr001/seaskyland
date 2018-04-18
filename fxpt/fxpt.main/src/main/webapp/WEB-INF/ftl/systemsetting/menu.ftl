<#import "childMenu.ftl" as childMenu>
<ul class="nav nav-list">
	<div class="sidebar-bg"></div>
  	<#if menus??>
  		<#list menus as menu>
  			<li><a menu="${menu.eventCode!""}" uuid="${menu.uuid!""}" href="${menu.url!"javascript:void(0);"}"><i class="icon-th"></i><span class="menu-text"> ${menu.alias} </span>
	  			<#if menu.hasChild>
	  			  	<b class="arrow icon-angle-down"></b>
	  			</#if></a>
		  			<#if menu.hasChild>
		  				<@childMenu.childMenu menus=menu.children/>
		  			</#if>
  			</li>
  		</#list>
  	</#if>
  </ul>
  <!-- /.nav-list -->
  <div class="sidebar-collapse" id="sidebar-collapse"> <i class="icon-double-angle-left" data-icon1="icon-double-angle-left" data-icon2="icon-double-angle-right"></i> </div>