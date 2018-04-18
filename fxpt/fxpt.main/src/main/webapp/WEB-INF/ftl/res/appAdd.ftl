<#import "../commons/dialog.ftl" as appDialog>

<#assign hiddens=[  {"name":"available","value":"true","class":"myData"},
					{"name":"type","value":"app","class":"myData"}
				 ]>
<@appDialog.dialog showHeader=false showFooter=false hiddens=hiddens form={"method":"post","action":"res","id":"form","name":"form"}>
	<#import "../commons/form.ftl" as form>
	<#assign fields=[{"name":"name","id":"textinput","text":"系统名称","type":"text","placeholder","系统名称"},
					 {"name":"remarks","id":"textinput","text":"描述","type":"text"}
					]>
	<@form.form fields=fields/>
</@appDialog.dialog>