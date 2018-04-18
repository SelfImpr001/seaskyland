<#macro modal  title=""  size="modal-md"  footBtns=[] showHeader=true showFooter=true hiddens=[] modalId="viewPanel" 
         form={"method":"post","action":"","id":"form1","name":"form1","enctype":"multipart/form-data"} contentCss="">
<div class="modal  fade in " role="dialog" aria-labelledby="myModal" aria-hidden="false" data-backdrop="static" id="${modalId}">
  <div class="modal-dialog self-dialog  ${size}">
    <div class="modal-content">
      <#if showHeader==true>  
      <div class="modal-header" id="modal-header">
        <button aria-hidden="true" data-dismiss="modal" class="close" type="button">×</button>
        <h4>${title}</h4>
      </div>
      </#if>  
      <div class="modal-body">
      <#nested>
      <#list hiddens as hidden>       
		<input type="hidden"  name="${hidden.name!}" id="${hidden.id!}" value="${hidden.value!}">
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
    </div>
  </div>
</div>
</#macro>