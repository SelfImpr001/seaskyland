<#import "../commons/modal.ftl" as dialog>
<#assign footBtns=[{"text":"关闭","id":"","class":"btn-default","closeBtn":true}]>

<#assign dialogClass="modal-md ztree-modal">
<@dialog.modal title="数据权限管理>查看权限明细" footBtns=footBtns size=dialogClass showHeader=true showFooter=true  modalId="dp-dialog">	
	<#import "../commons/searchTable.ftl" as table>
	<#assign headers=["权限名称","权限值"]>
	<#assign  rows=[]>
	<#if result??>
	    <#list result as da>    
	      <#assign cells=["",da.permissionName,da.permissionValue]>
	      <#assign rows=rows+[cells]>
	    </#list>
	</#if>
	<#assign userBodyId="user-rows">
	<#assign pager={"curpage":1,"totalpage":1,"pagesize":15}>
	
	<@table.searchTable  rows=rows headers=headers hasFirstCheckbox=false showSeq=true showTab=false showPager=true pager=pager  style="overflow:auto;height:180px;margin: 1px 1px 0 0 ;"/>
	
</@dialog.modal>