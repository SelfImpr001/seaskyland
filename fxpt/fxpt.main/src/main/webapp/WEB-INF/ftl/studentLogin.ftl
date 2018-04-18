<#import "meta.ftl" as meta>
<#import "script.ftl" as script>
<#import "loginForm.ftl" as loginForm>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
  <@meta.meta title="2"/>
</head>
<body entry="login" rootPath="${request.contextPath}/"  class="login" >
  <#if license.accessabled=true>
  <div class="login-container container">
  <@loginForm.loginForm title="海云天数据分析与发布系统" showCode=showCode showAgreemet=false showRememberme=true/>
  </div>     
  <#else>
	<#include "nolicense.ftl">
  </#if>  
  <@script.script/>
</body>

</html>