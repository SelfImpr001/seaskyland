<#import "../commons/table.ftl" as table>
<#macro list logs=[]>
	<@table.table tableId="logTable" head=["操作人","操作日期","操作内容","状态","日志"]>
		<#include "./listTBody.ftl">
  </@table.table>
</#macro>
