<?xml version="1.0" encoding="GBK" ?>
<result>
	<#if result??>
		<success>${result?string('true', 'false')} </success>
	</#if>
	<#if info??>
		<info>${info}</info>
	</#if>
</result>