 <ul>
	 <#if fileArray?? && fileArray?size!=0>
	 	<#if isRoot>
			 <#list fileArray as file>
			 	<li><a href="javascript:;" tag="${file.path!''}">${file.path!''}</a></li>
			 </#list>
		 <#else>
			 <#list fileArray as file>
				 <li><a href="javascript:;" tag="${file.path!''}">${file.name!''}</a></li>
			 </#list>
		 </#if>
	<#else>
		<#if isDire>
			<li>没有文件夹了</li>
		<#else>
			 <li>选中路径文件：${paremt.path!''}</li>
		</#if>
		
	</#if>
 </ul>
<div>
	<#if paremt??>
		<input type="hidden" id="paremtfile" value="${paremt.path!''}"/>
	</#if>
	<input type="hidden" id="isDire" value="${isDire?string}"/>
</div>
 
 
 