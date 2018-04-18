<#import "../commons/dialog.ftl" as roleDialog>
<#assign title="角色管理>编辑${('>'+role.name)!}">
<#assign footBtns=[{"text":"修改","id":"update","class":"btn-primary","closeBtn":false},
				  {"text":"取消","id":"","class":"btn-default","closeBtn":true}]>
<#assign hiddens=[{"name":"pk","id":"roleId","value":"${(role.pk)!}","class":"myData"}]>
<@roleDialog.dialog title=title footBtns=footBtns showHeader=true showFooter=true hiddens=hiddens>
	<#import "../commons/form.ftl" as form>
	<#if role.type=2>
	  <#assign fields=[{"name":"code","id":"code","text":"角色代码","value":"${(role.code)!}","isValid":"true","type":"textRole","placeholder","角色代码","readonly":""},
					 {"name":"name","id":"name","text":"角色名称","value":"${(role.name)!}","isValid":"true","type":"text","placeholder","角色名称","readonly":""},
					 {"name":"desc","id":"desc","text":"角色描述","value":"${(role.desc)!}","isValid":"false","type":"textarea","placeholder","角色描述...","readonly":""},
			<!-- 		 {"name":"available","id":"available","text":"是否有效","isValid":"false","type":"radio","placeholder":"",
					  "value":[{"checked":'${((role.available)!true)?string("checked","")}',"label":"有效","value":"true"},
					           {"checked":'${((role.available)!true)?string("","checked")}',"label":"无效","value":"false"}],"readonly":""}, -->
					 {"name":"type","id":"type","value":"${(role.type)!1}","type":"hidden"}]>
	</#if>
	<#if role.type=1>
	  <#assign fields=[{"name":"code","id":"code","text":"角色代码","value":"${(role.code)!}","isValid":"true","type":"text","placeholder","角色代码","readonly":"readonly"},
					 {"name":"name","id":"name","text":"角色名称","value":"${(role.name)!}","isValid":"true","type":"text","placeholder","角色名称","readonly":"readonly"},
					 {"name":"desc","id":"desc","text":"角色描述","value":"${(role.desc)!}","isValid":"false","type":"textarea","placeholder","角色描述...","readonly":""},
					 <!-- {"name":"available","id":"available","text":"是否有效","isValid":"false","type":"radio","placeholder":"",
					  "value":[{"checked":'${((role.available)!true)?string("checked","")}',"label":"有效","value":"true"},
					           {"checked":'${((role.available)!true)?string("","checked")}',"label":"无效","value":"false"}],"readonly":""}, -->
					 {"name":"type","id":"type","value":"${(role.type)!1}","type":"hidden"}]>
	</#if>					 
	<@form.form fields=fields/>
</@roleDialog.dialog>