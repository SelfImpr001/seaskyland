<#import "./querybar.ftl" as querybar>
<#macro queryForm showAngle=false>
<#if RequestParameters["ui_all"]??>
  <@querybar.querybar showAngle=showAngle>
  	<#nested> 
  </@querybar.querybar>
</#if>  
</#macro>
