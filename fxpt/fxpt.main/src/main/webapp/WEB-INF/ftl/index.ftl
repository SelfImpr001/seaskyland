<#import "meta.ftl" as meta> 
<#import "script.ftl" as script> 
<#import "show.ftl" as show> 
<#import "alert.ftl" as alert> 
<#import "commons/menuTree.ftl" as mt>
<#import "header.ftl" as header>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<@meta.meta title="${app.name}" />
<style>

.nav-list2 > li > .submenu li > a {
  padding-left: 33px;
}
.nav-list2 > li > .submenu li > .submenu > li > a {
  
  padding-left: 52px;
}
.nav-list2 > li > .submenu li > .submenu > li > .submenu > li > a {
  
  padding-left: 72px;
}

</style>
</head>

<body entry="index" rootPath="${request.contextPath}/" style="overflow:hidden" class=''>
  <@header.header/>
  <!---主体部分-->
  <div class="main-container" id="main-container">

    <div class="main-container-inner">
      <!--导航栏-->
      <div class="sidebar2" id="sidebar">
        <ul <#if !priview>id="menu"</#if> class="nav nav-list2 list-padding  pgEmpty"><@mt.mTree nodes=menus />
        
        </ul>
	      
        <!--<div class="sidebar-collapse" id="sidebar-collapse">
          <i class="icon-double-angle-left pgEmpty" data-icon1="icon-double-angle-left" data-icon2="icon-double-angle-right"></i>
        </div>-->
      </div>
      
      <!--拖拽的线 -->
      <div id="line"></div>
      <!--/导航栏-->
      <div class="main-content" id='content-right'>
		<div class="label-box">    
			<div class="pull-left" id="sidebar-suo2">
	    	<i class="icon-reorder"></i>
	    	</div>
		  <ul class="stand-box"></ul>
		   <ul class="mouseMenu">
		  </ul>
		  <a href="javascript:void(0);" class="more"> <span class="icon-hand-down"></span></a>
		  <ul class="park-box"></ul>                       
		</div>
		
		<!--页面内容部分-->
		<div class="page-box  page-box2" >
		  <div class="page-content" style="display:none; padding: 0 7px"></div>
		</div>           
      </div>
    </div>
  </div>
  <@script.script/>
</body>
</html>

