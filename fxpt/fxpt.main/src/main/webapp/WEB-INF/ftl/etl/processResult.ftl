<span style="color:${processResult.hasError?string("#000","##F00")};">
<#if processResult.hasError>
	导入失败！${processResult.message}
<#else>
	导入成功！获取数据${processResult.extractNum}行，成功导入${processResult.loadNum}；
</#if>
<#if processResult.hasError>
查看详细点击<a href="#" data-logFile="${processResult.etlLog.logContent}">下载</a>日志
</#if>
</span>