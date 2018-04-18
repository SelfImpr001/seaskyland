<#import "../meta.ftl" as meta> 
<#import "../script.ftl" as script> 
<#import "../commons/table.ftl" as table>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<@meta.meta title="设置日志" />
</head>
<body>
 <@table.table tableId="logtable" head=["名字","级别"]>
 <tr class="template">
 	<#if logs??>
 		<#list logs as log>
 			<tr>
				<td>${log.name}</td>
				<td>
					<select logName="${log.name}">
						<option value="OFF" ${(log.level?string=="OFF")?string("selected","")}>OFF</option>
						<option value="ERROR" ${(log.level?string=="ERROR")?string("selected","")}>ERROR</option>
						<option value="WARN" ${(log.level?string=="WARN")?string("selected","")}>WARN</option>
						<option value="INFO" ${(log.level?string=="INFO")?string("selected","")}>INFO</option>
						<option value="DEBUG" ${(log.level?string=="DEBUG")?string("selected","")}>DEBUG</option>
						<option value="TRACE" ${(log.level?string=="TRACE")?string("selected","")}>TRACE</option>
						<option value="ALL" ${(log.level?string=="ALL")?string("selected","")}>ALL</option>
					</select>
				</td>
			</tr>
 		</#list>
 	</#if>
	
</tr>
 </@table.table>
<div style="text-align: center;">
 <button class="btn btn-primary " type="button" trigger="" id="settingLog" name="">设置</button>
</div>
  <@script.script entry="logs/settingLog"/>
</body>
</html>

