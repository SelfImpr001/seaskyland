<#import "meta.ftl" as meta>
<#import "script.ftl" as script>
<#import "loginForm.ftl" as loginForm>
<#import "studentLoginForm.ftl" as studentLoginForm>
<!DOCTYPE html>
<script type="text/javascript">
  var browser=navigator.appName
  var b_version=navigator.appVersion
  var version=b_version.split(";")
  var trim_Version=version[1].replace(/[ ]/g,"");
  if(browser=="Microsoft Internet Explorer" && (trim_Version=="MSIE8.0" || trim_Version=="MSIE7.0" || trim_Version=="MSIE6.0" || trim_Version=="MSIE5.0" || trim_Version=="MSIE5.5")){
    alert("本系统在IE8及其以下版本中可能无法正常操作！建议您更换谷歌(Chrome)浏览器或在IE8中启用兼容模式！")
  }
</script>
<html lang="zh-cn">
<#if isStudent=true>
<head>
  <@meta.meta title="学生学业报告查询"/>
</head>
<body class="login-stu" entry="studentLogin" rootPath="${request.contextPath}/" >
  <div class="login-stu-container">
  <@studentLoginForm.loginForm title="学生学业报告查询" showCode=showCode showAgreemet=false showRememberme=true/>
  </div>   
  <@script.script/>  
  	<div class="stat_web">
  		<script src="http://s4.cnzz.com/stat.php?id=1261044651&web_id=1261044651" language="JavaScript"></script>  		  
	</div>
	<style>
  		.stat_web>a{display: none;}
  	</style>
	<!-- Live800在线客服图标:Default[浮动图标] 开始-->
	<div style='display:none;'>
		<a href='http://www.live800.com'>web聊天</a>
	</div>
	<script language="javascript" src="http://chat56.live800.com/live800/chatClient/floatButton.js?jid=9197376726&companyID=277687&configID=72846&codeType=custom"></script>
	<div style='display:none;'>
		<a href='http://en.live800.com'>live chat</a>
	</div>
</body>
<#else>
<head>
  <@meta.meta title="${systemName}"/>                      
</head>
<body entry="login" rootPath="${request.contextPath}/"  class="login login_change" >
  <div class="login-container container">
  <@loginForm.loginForm title="${systemName}" showCode=showCode showAgreemet=false showRememberme=false/>
  </div>
     
  <@script.script/>
</body>
</#if>
</html>