<div class="sidebar-bg"></div>
<ul class="nav nav-list">
<#if menus??>
  <#list menus as menu>
  	<li><a href="javascript:void(0);" menu="${menu.eventCode!""}" uuid="${menu.uuid!""}" url="${menu.url!""}" class="homeMenu"><i class="icon-th"></i><span class="menu-text"> ${menu.alias!""} </span> </a> </li>
  	</#list>
  </#if>
</ul>
  <!-- /.nav-list -->
<div class="sidebar-collapse" id="sidebar-collapse"> <i class="icon-double-angle-left" data-icon1="icon-double-angle-left" data-icon2="icon-double-angle-right"></i> </div>