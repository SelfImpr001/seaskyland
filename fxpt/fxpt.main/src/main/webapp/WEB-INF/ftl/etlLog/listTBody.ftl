<#list logs as log>
	<tr>
		<td>${log.optionUser!""}</td>
		<td>${log.createDate?string("yyyy-MM-dd")}</td>
		<td>${log.optionContent!""}</td>
		<td>${log.statusMessage!""}</td>
		<td>
			<#if log.logIsFile>
				<a href="#" class="downloadLog" data-fileName="${log.logContent}">下载日志</a>
			<#else>
				${log.logContent!""}
			</#if>
		</td>
	</tr>
</#list>