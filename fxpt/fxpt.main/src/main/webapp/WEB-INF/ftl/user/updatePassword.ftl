<#import "../commons/dialog.ftl" as pwdDialog>
<#assign title="用户管理>修改用户密码${('>'+user.name)!}">
<#assign footBtns=[{"text":"修改","id":"update","class":"btn-primary","closeBtn":false},
				  {"text":"取消","id":"","class":"btn-default","closeBtn":true}]>
<#assign hiddens=[{"name":"pk","id":"","value":"${user.pk}","class":"myData"}]>
<@pwdDialog.dialog title=title footBtns=footBtns showHeader=true showFooter=true modalId="pwd-modal" hiddens=hiddens>
  <#import "../commons/form.ftl" as form>
  <#assign fields=[
    {"name":"name","id":"name","text":"用户名","value":"${user.name}","isValid":"true","type":"text","placeholder","","readonly":"readonly"},
	{"name":"password","id":"password","text":"新密码","value":"","isValid":"true","type":"text","placeholder","新密码","readonly":""}]>
  <@form.form fields=fields/>
</@pwdDialog.dialog>