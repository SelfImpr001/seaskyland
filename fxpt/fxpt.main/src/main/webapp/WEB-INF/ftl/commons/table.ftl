<#macro table tableId="" head=[] css="">
<div id="${tableId}" class="row">
  <div class="col-xs-12" id="form-01">
	<table class="table table-bordered table-striped table-hover ${css}">
	  <#if head?size gt 0>
	  <thead>
		<tr>
	    <#list head as h><th>${h}</th></#list>
		</tr>
	  </thead>
	  </#if>
	  <tbody>
		<#nested>
	  </tbody>
	</table>
	<div style="text-align: center">
	  <div id="pager" class="pager" style="margin-top: 20px; text-align: center"></div>
	</div>
  </div>
</div>
</#macro>