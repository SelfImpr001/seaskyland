<!doctype html>
<#import "../meta.ftl" as meta>
<#import "../header.ftl" as header>
<#import "../script.ftl" as script>
<html lang="zh-cn">
<head>
<@meta.meta title=""/>
<@script.script entry="systemsetting"/>
</head>

<body entry="index" rootPath="">
<input id="menu_uuid" type="hidden" name="menu_uuid" value="${uuid}">
<!--header-->
<@header.header/>
<!-- /-header -->


<!---主体部分-->
<div class="main-container" id="main-container">

    <div class="main-container-inner">
        <!--导航栏-->
        <div class="sidebar" id="sidebar">
            
        </div>
        <!--/导航栏-->
		<!--正文内容-->
        <div id="main-content" class="main-content">
         
        </div>
		<!--/正文内容-->
    </div>
</div>

</body>
</html>