<#macro  dialog  title=""  dialogClass=""  footBtns=[] showHeader=true showFooter=true hiddens=[] modalId="viewPanel" 
         form={"method":"post","action":"","id":"form1","name":"form1","enctype":"multipart/form-data"} contentCss="">
  <#if showHeader==true && showFooter==true>
  <div class="modal fade ${dialogClass} " id="${modalId}"  role="dialog" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog self-dialog ">
	  <div class="modal-content" style="${contentCss}">
  </#if>
  <#if showHeader==true>
	<div style="border-:1px solid red;" class="modal-header" id="modal-header">
	  <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
	  <h4>${title}</h4>
    </div>
  </#if>
    <form id="${form.id!}" name="${form.name!}" enctype="${form.enctype!}" class="form-horizontal" role="form" 
        method="${form.method!}" action="${form.action!}">
	  <div id="${modalId}Body" class="modal-body">
	  <#nested>
	  <#list hiddens as hidden>       
		<input type="hidden" class="${hidden.class!}" name="${hidden.name!}" id="${hidden.id!}" value="${hidden.value!}">
	  </#list>
	  </div>
	  <#if showFooter==true>
	  <div class="modal-footer">
		<div class="self-modal-footer">
		<#list footBtns as button>         
		  <button <#if button.closeBtn=true>data-dismiss="modal"</#if>  class="btn ${button.class}" type="button" id="${button.id!""}">${button.text!""}</button>
		</#list>
		</div>
	  </div>
	  </#if>	
	</form>			
  <#if showHeader==true && showFooter==true>		
      </div>
  	</div>
  </div>
  </#if>
</#macro>