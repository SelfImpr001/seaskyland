<#import "../commons/modal.ftl" as userDialog>
<#assign footBtns=[{"text":"保存","id":"","class":"btn-primary","closeBtn":false},
				  {"text":"关闭","id":"","class":"btn-default","closeBtn":true}]>

<#assign dialogClass="modal-lg">
<@userDialog.modal title="数据权限管理>新增" footBtns=footBtns size=dialogClass showHeader=true showFooter=true hiddens=hiddens modalId="dp-dialog">

<form id="dpEditForm" role="form" action="" class="form-horizontal ">
  <div class="form-group" >
	<label class="col-sm-2 control-label">权限名称</label>
	<div class="col-sm-4">
	  <input value="${(dp.name)!""}" type="text"  class="form-control myData"  name="name" placeholder="请输入权限名称">
	  <p class="error-tip"></p>
	</div>	
	<label class="col-sm-2 control-label">外部参数名称</label>
	<div class="col-sm-4">
	  <input value="${(dp.paramName)!""}" type="text"  class="form-control myData"  name="paramName" placeholder="请输入外部参数名称">
	  <p class="error-tip"></p>
	</div>	
  </div>
  <div class="form-group" >
	<label class="col-sm-2 control-label">数据权限来源表名称</label>
	<div class="col-sm-4">
	  <input value="${(dp.table)!""}" type="text"  class="form-control myData"  name="table" placeholder="请输入数据权限来源表名称">
	  <p class="error-tip"></p>
	</div>	
	<label class="col-sm-2 control-label">数据权限值主健字段</label>
	<div class="col-sm-4">
	  <input value="${(dp.paramKeyField)!""}" type="text"  class="form-control myData"  name="paramKeyField" placeholder="请输入数据权限值主健字段">
	  <p class="error-tip"></p>
	</div>	
  </div>
  <div class="form-group" >
	<label class="col-sm-2 control-label">权限名称字段</label>
	<div class="col-sm-4">
	  <input value="${(dp.paramNamefield)!""}" type="text"  class="form-control myData"  name="paramNamefield" placeholder="请输入权限名称字段">
	  <p class="error-tip"></p>
	</div>	
	<label class="col-sm-2 control-label">权限值字段</label>
	<div class="col-sm-4">
	  <input value="${(dp.paramValueField)!""}" type="text"  class="form-control myData"  name="paramValueField" placeholder="请输入权限值字段">
	  <p class="error-tip"></p>
	</div>	
  </div>
  <div class="form-group" >
	<label class="col-sm-2 control-label">上级关联字段</label>
	<div class="col-sm-4">
	  <input value="${(dp.parentRefKey)!""}" type="text"  class="form-control myData"  name="parentRefKey" placeholder="请输入上级关联字段">
	  <p class="error-tip"></p>
	</div>	
	<label class="col-sm-2 control-label">是否启用</label>
	<div class="col-sm-4">
	  <label class="radio-online" style="margin: 5px;">
	    <input type="radio"  class="myData" value="1" name="status"  <#if dp.status = 1>checked</#if>>启用
	  </label>
	  <label class="radio-online" style="margin: 5px;">
	    <input  type="radio"  class="myData"  value="0" name="status" <#if dp.status = 0>checked</#if>>禁用
	  </label>
	</div>	
  </div>  
  <div class="form-group" style="padding-bottom: 0px;margin-bottom:0px;">
	<label class="col-sm-2 control-label">数据取取值表达式</label>
	<div class="col-sm-10">
	  <textarea class="form-control myData"  name="source" row="3" style="width:685px;" >${(dp.source)!""}</textarea>
	</div>	
  </div>
  <#if dp.pk != -1><input type="hidden"  class="myData" value="${dp.pk?c}" name="pk" ></#if>             
</form>    
</@userDialog.modal>