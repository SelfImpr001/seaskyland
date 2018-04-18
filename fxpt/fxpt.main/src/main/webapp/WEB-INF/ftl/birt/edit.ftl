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
<#if org??>

	<input type="hidden" class="myData" name="pk"  id="orgId" value="${org.pk}">
	
	

	  <#assign fields=[
					 {"name":"name","id":"name","text":"组织名称","type":"text","value":"${org.name}","placeholder":"请输入名称"}
					]>	 
	
		<#else>
		<input type="hidden" class="myData" name="pk"  id="orgId" value="">
	
	

	  <#assign fields=[
					 {"name":"name","id":"name","text":"组织名称","type":"text","value":"","placeholder":"请输入名称"}
					]>	 
</#if>				 
	<@form.form fields=fields />
	 	
	
	
	
	
	
</@orgDialog.dialog>
