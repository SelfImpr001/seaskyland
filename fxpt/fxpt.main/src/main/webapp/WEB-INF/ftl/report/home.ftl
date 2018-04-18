<#import "../meta.ftl" as meta>
<#import "../header.ftl" as header>
<#import "../script.ftl" as script>
<#import "../commons/menuTree.ftl" as mt>
<!doctype html>
<html lang="zh-cn">
<head>
<@meta.meta title=app.name/>
<style>
.crumbs{
	height:30px;
	width:100%;
	background:#fff;
	padding: 5px 0 0 14px;
}
</style>
</head>
<body entry="report" rootPath="" style="overflow:hidden;" class="">
<!--header-->
<@header.header/>
<!-- /-header -->
<!---主体部分-->
<div class="main-container" id="main-container">
    <div class="main-container-inner sidebar_pentaho">
      <!--导航栏-->
      <div class="sidebar2" id="sidebar">
	      <ul id="menu" class="nav nav-list2"><@mt.mTree nodes=menus /></ul>
      </div>
      <!--拖拽的线 -->
      <div id="line"></div>
      <!--/导航栏-->
      <div class="main-content" id='content-right'>
      	<!--<div class='crumbs' id='crumbs'>
      		
      	</div> 
      	-->
		<div class="label-box">
		<div class="pull-left" id="sidebar-suo2">
	    	<i class="icon-reorder"></i>
	    	</div>
		  <ul class="stand-box"></ul>
		  <a href="javascript:void(0);" class="more"> <span class="icon-double-angle-right icon-large"></span></a>
		  <ul class="park-box"></ul>
		</div>
		
		<!--页面内容部分-->
		<div class="page-box page-box2" >
		  <div class="page-content" style="display:none;height:100%;"></div>
		</div>           
      </div>       
	  <!--/正文内容-->
    </div>
</div>
<#list exams as exam>
<div class="exam-list" style="display:none;">
<input type="hidden" name="examId" value="${exam.id}"/>
<input type="hidden" name="examName" value="${exam.name}"/>
</div>
</#list>
<input type="hidden" id="biCookie" value=""/>
<input type="hidden" id="userName" value="${user.name}"/>
<input type="hidden" id="userId" value="${user.pk}"/>
<#list orgs as org>
<input type="hidden" name="orgCode" value="${org.code}"/>
<input type="hidden" id="fromtype" value="${org.type}"/>
</#list>
<input type="hidden" id="examId" value="0"/>
<#list roleTypes as roleType>
<input type="hidden" name="roleType" value="${roleType}"/>
</#list>

<!-- 数据权限列表-->
<div style="display:none;" id="datapermision">
<#---
<#list datas as d>
  <ul pid="${d.url}" uuid="${d.uuid}" name="${d.name}">
  <#if d.hasChild>
  <@dm ds=d.children/>
  </#if>
  </ul>
</#list>
--->
</div>
<!-- 数据权限列表-->
<@script.script entry="report"/>
</body>
</html>
<#macro dm ds>
  <#list ds as d>
  <li url="${d.url}" uuid="${d.uuid}" name="${d.name}"/>
  </#list>
</#macro>