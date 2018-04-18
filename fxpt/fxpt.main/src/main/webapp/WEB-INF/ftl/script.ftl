<#macro script entry="">
  <script type="text/javascript" src="${request.contextPath}/static/scripts/lib/editArea/edit_area_full.js"></script>
  <#if entry != "">  	
  <script type="text/javascript">
	window["app"]={rootPath:'${request.contextPath}/',entry:'${entry}'};
  </script>
  </#if>
  <script type="text/javascript" data-main="${request.contextPath}/static/scripts/main"  src="${request.contextPath}/static/scripts/require.js"></script>
  <script type="text/javascript">
   var v = + + new Date;
	requirejs.config({
		urlArgs : "v="+v
	});
  </script>
</#macro>

