<div class="modal-body" style="width: 100%; max-height: 500px; overflow: auto;">
		<input type="hidden" id="handleOption" value="${log.handleOptionLocation!''}>${log.handleOption!''}" />
		${log.logInfo!''}
		<#if log.erreInfo??>
			<div style='width:100%; max-height:360px; overflow-y:auto; overflow-x:auto;'>
				${log.erreInfo !''}
			</div>
		</#if> 
</div>
