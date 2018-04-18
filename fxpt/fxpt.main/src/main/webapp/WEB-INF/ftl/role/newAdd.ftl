<#import "../commons/dialog.ftl" as roleDialog>
<#assign title="角色管理>新增">
<#assign footBtns=[{"text":"确定","id":"subAddBtn","class":"btn-primary","closeBtn":false},
				  {"text":"关闭","id":"","class":"btn-default","closeBtn":true}]>
<#assign hiddens=[{"name":"pk","id":"roleId","value":"${(role.pk)!}","class":"myData"}]>
<@roleDialog.dialog title=title footBtns=footBtns showHeader=true showFooter=true hiddens=hiddens>
<#import "../commons/form.ftl" as form>
<#assign fields=[{"name":"code","id":"code","text":"角色编号","value":"","isValid":"true","type":"textRole","placeholder","角色编号","readonly":""}
				 {"name":"name","id":"name","text":"角色名称","value":"${(role.name)!}","isValid":"true","type":"text","placeholder","角色名称","readonly":""},
				 {"name":"desc","id":"desc","text":"角色描述","value":"${(role.desc)!}","isValid":"false","type":"textarea","placeholder","角色描述...","readonly":""},
				 {"name":"available","id":"available","text":"是否有效","isValid":"false","type":"hide","placeholder":"",
				  "value":[{"checked":'${((role.available)!true)?string("checked","")}',"label":"有效","value":"true"},
				           {"checked":'${((role.available)!true)?string("","checked")}',"label":"无效","value":"false"}],"readonly":""}]>
<@form.form fields=fields/>
</@roleDialog.dialog>