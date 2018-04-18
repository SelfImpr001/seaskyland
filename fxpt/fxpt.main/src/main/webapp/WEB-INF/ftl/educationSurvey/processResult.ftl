<span style="color:red};">
<#if !processResult.hasError>
${processResult.message}
<input id="excelPath" value="${fileName}" type="hidden"/>
<#else>
${processResult.message}
</#if>
</span>