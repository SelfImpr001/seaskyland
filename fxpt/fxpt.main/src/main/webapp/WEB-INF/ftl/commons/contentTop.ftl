<#import "./pageHead.ftl" as pageHead >
<#macro contentTop buttons=[] title="" type="button">
  <#if RequestParameters["ui_all"]??>
  	<@pageHead.pageHead buttons=buttons title=title type=type/>
  </#if>
</#macro>