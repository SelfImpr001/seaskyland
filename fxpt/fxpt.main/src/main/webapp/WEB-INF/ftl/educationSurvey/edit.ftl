<#import "../commons/dialog.ftl" as orgDialog>
<#if !parent??>
	<#assign dispaly="none">
</#if>
<#assign hiddens=[  {"name":"pk","value":"${org.pk!''}","class":"myData"},
					{"name":"parent.pk","value":"${(parent.pk)!''}","class":"myData"}
				 ]>
				 
<#assign select1="">
<#assign select2="">
<#assign select3="">
<#assign select4="">			 
<#if parent??>
  <#if parent.type=1>
    <#assign select2="selected">
  <#elseif parent.type=2>
    <#assign select3="selected">
  <#elseif parent.type=3>
    <#assign select4="selected">
  </#if>
<#else>
  <#assign select1="selected">
</#if>

<@orgDialog.dialog showHeader=false showFooter=false hiddens=[] form={"id":"orgEdit","role":"form"}>
	<#import "../commons/form.ftl" as form>
	<#if org.pk??>
		<input type="hidden" class="myData" name="pk"  value="${org.pk?c}">
	</#if>
	<#if parent??>
		<input type="hidden" class="myData" name="parent.pk"  value="${parent.pk?c}">
	</#if>
	<#if hasDataRef=true >
	   <#assign fields=[ {"name":"available","id":"available","text":"是否启用","isValid":"false","type":"radio","placeholder":"",
					 "value":[{"checked":"checked","label":"启用","value":"true"},
					 {"checked":"","label":"不启用","value":"false"}],"readonly":""},
	  				 {"name":"code","id":"code","text":"指标代码","type":"text","value":"${(org.code)!''}","placeholder":"请输入代码"},
					 {"name":"name","id":"name","text":"指标名称","type":"text","value":"${(org.name)!''}","placeholder":"请输入名称"},
					 {"name":"searchPoint","id":"searchPoint","text":"考察点","type":"textarea","value":"${(org.searchPoint)!''}"},
					 {"text":"上级指标","type":"html","value":"${(parent.code)!}-${(parent.name)!}","readonly":"readonly"}
					]>		  
		<@form.form fields=fields />
	<#else>
	  <#assign orgOpts=[]>
	   <#assign fields=[
	   				{"name":"available","id":"available","text":"是否启用","isValid":"false","type":"radio","placeholder":"",
					 "value":[{"checked":'${((org.available)!true)?string("checked","")}',"label":"启用","value":"true"},
					  {"checked":'${((org.available)!true)?string("","checked")}',"label":"不启用","value":"false"}],"readonly":""},
	  				 {"name":"code","id":"code","text":"指标代码","type":"text","value":"${(org.code)!''}","placeholder":"请输入代码"},
					 {"name":"name","id":"name","text":"指标名称","type":"text","value":"${(org.name)!''}","placeholder":"请输入名称"},
					 {"name":"searchPoint","id":"searchPoint","text":"考察点","type":"textarea","value":"${(org.searchPoint)!''}"}
					 ,{"text":"上级指标","type":"html","value":"${(parent.code)!}-${(parent.name)!}","readonly":"readonly"}
					]>	
	<@form.form fields=fields />
	</#if>
</@orgDialog.dialog>
