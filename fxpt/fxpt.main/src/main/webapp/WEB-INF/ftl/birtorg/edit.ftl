<#import "../commons/dialog.ftl" as orgDialog>
<#if !parent??>
	<#assign dispaly="none">
</#if>
<#if org??>
<#assign hiddens=[  {"name":"pk","value":"${org.pk}","class":"myData"}
				
				 ]>
</#if>			 
		 


<@orgDialog.dialog showHeader=false showFooter=false hiddens=[] form={"id":"orgEdit","role":"form"}>
	<#import "../commons/form.ftl" as form>
	<#if me="add">
	<#if org??>

	</#if>
	
	<#if org??>
	  <#assign fields=[
					 {"name":"name","id":"name","text":"组织名称","type":"text","value":"","placeholder":"请输入名称"},
					 {"text":"上级组织","type":"html","value":"${(org.name)!}","readonly":"readonly"}
				
					]>	 
		<#else>
		  <#assign fields=[
					 {"name":"name","id":"name","text":"组织名称","type":"text","value":"","placeholder":"请输入名称"},
					 {"text":"上级组织","type":"html","value":"无","readonly":"readonly"}
				
					]>	
		</#if>
		<#else>
		<#if org??>

	
	</#if>
	
	<#if org??>
	  <#assign fields=[
					 {"name":"name","id":"name","text":"组织名称","type":"text","value":"${org.name}","placeholder":"请输入名称"},
					 {"text":"上级组织","type":"html","value":"${(org.parent.name)!}","readonly":"readonly"}
				
					]>	 
		<#else>
		  <#assign fields=[
					 {"name":"name","id":"name","text":"组织名称","type":"text","value":"${org.name}","placeholder":"请输入名称"},
					 {"text":"上级组织","type":"html","value":"无","readonly":"readonly"}
				
					]>	
		</#if>
		</#if>
		
					 
	<@form.form fields=fields />
	 	
	
	
	
	
	
</@orgDialog.dialog>
