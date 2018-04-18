<#macro alert id="alertPanel" buttons=["确定"]>
<div class="modal fade tip-modal"id="${id}">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="alertPanel">&nbsp;</h4>
      </div>
      <div id="alertPanelContent" class="modal-body">
       
      </div>
      <div class="modal-footer">
      	<#list buttons as button>
          <button type="button" title="${button}" class="btn btn-default" data-dismiss="modal">${button}</button>
      	</#list>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
</#macro>