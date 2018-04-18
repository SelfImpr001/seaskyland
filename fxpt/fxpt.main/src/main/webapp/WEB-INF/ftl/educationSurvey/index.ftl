<#import "../meta.ftl" as meta>
<#import "../script.ftl" as script>

<html lang="zh-cn">
<head>
	<@meta.meta title=""/>
	
</head>
<body rootPath="">
<div class="heading-main">
	<div class="left-area">						
		<p>当前位置：</p>					
		<ol class="main-nav">
			<!-- <li><a href="#">Home</a><span>/</span></li>
			<li><a href="#">Library</a><span>/</span></li>
			<li class="active">Data</li> -->
		</ol>
	</div>
	<div class="right-area">		
		<a href="#" title="增加">
			<span class="glyphicon glyphicon-plus"></span>
		</a>
		<a href="#" title="刷新">
			<span class="glyphicon glyphicon-refresh"></span>
		</a>			
	</div>
</div>	
<div class="form-item2" style="border-:1px solid red;">
	<div class="col-sm-2 iframe-leftside iframe-side">
		<iframe src="${request.contextPath}/organization/tree"  width="100%" frameborder="0" height="100%" style="border-:1px solid red;"></iframe>
	</div>
	<div class="col-sm-10 iframe-rightside iframe-side">
		<iframe src="#####" frameborder="0" width="100%" height="100%" style="border-:1px solid red;" id="orgfrm"></iframe>
	</div>
</div>

</body>
</html>
