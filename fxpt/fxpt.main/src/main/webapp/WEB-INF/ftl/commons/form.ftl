<#macro form fields=[]>
<#list fields as field>
	<#if field.type=="text"> 
		<div class="form-group" style="display:${field.display!'block'}">
		  <label class="col-sm-3 control-label" for="textinput">${field.text}</label>
		  <div class="col-sm-9">
		  <input value="${field.value!}" ${field.readonly!} type="text" class="form-control myData" id="${field.id!}" name="${field.name!}" placeholder="${field.placeholder!}">
		 	 <p class="error-tip"></p>
		  </div>
		</div>
	</#if>
	<#if field.type=="password"> 
		<div class="form-group" style="display:${field.display!'block'}">
		  <label class="col-sm-3 control-label" for="textinput">${field.text}</label>
		  <div class="col-sm-9">
		  <input value="${field.value!}" ${field.readonly!} type="password" class="form-control myData" id="${field.id!}" name="${field.name!}" placeholder="${field.placeholder!}">
		 	 <p class="error-tip"></p>
		  </div>
		</div>
	</#if>
	<#if field.type=="textRole"> 
		<div class="form-group" style="display:${field.display!'block'}">
		  <label class="col-sm-3 control-label" for="textinput">${field.text}</label>
		  <div class="col-sm-9">
		  <input value="${field.value!}" ${field.readonly!} type="text" class="form-control myData" id="${field.id!}" name="${field.name!}" placeholder="${field.placeholder!}">
		 	 <p class="error-tip"><label id="roleNum" style="display:none;color:red" ></label><p>
		  </div>
		</div>
	</#if>
	<#if field.type=="html"> 
		<div class="form-group" style="display:${field.display!'block'}">
		  <label class="col-sm-3 control-label" for="textinput">${field.text}</label>
		  <div class="col-sm-9">
		  <input value="${field.value!}" ${field.readonly!} class="form-control ace" type="text">
		  <p class="error-tip"></p>
		  </div>
		</div>
	</#if>
	<#if field.type=="checkbox"> 
		<div class="form-group" style="display:${field.display!'block'}">
		  <label class="col-sm-3 control-label" for="textinput">${field.text}</label>
		  <div class="col-sm-9">
		  <input value="${field.value!}" type="checkbox" class="form-control myData" id="${field.id!}" name="${field.name!}" placeholder="${field.placeholder!}">
		  </div>
		</div
	</#if>	
	<#if field.type=="radio"> 
		<div class="form-group">
		  <label class="col-sm-3 control-label" for="">${field.text!}</label>
		  <div class="col-sm-9 ">
		  <#list field.value as radio>
		  	<div class="download-check">
		  	 <input ${radio.checked!} type="radio" class="form-control ace myData" name="${field.name!}"
		  	  ${field.readonly!} id="${field.name!}" value="${radio.value!}"> 
		  	 <label  class="lbl" style="line-height:5px" ></label>${radio.label!}
		  	</div>
		  </#list>
		 	 <p class="error-tip"></p>
		  </div>
		</div>
	</#if>
	<#if field.type=="textarea"> 
		<div class="form-group">
		  <label class="col-sm-3 control-label" for="textinput">${field.text!}</label>
		  <div class="col-sm-9">
		  <textarea  class="form-control myData" id="${field.id!}" name="${field.name!}" ${field.readonly!} placeholder="${field.placeholder!}" rows="5" style="width: 370px;">${field.value!}</textarea>
		  </div>
		</div>
	</#if>	
	<#if field.type=="select"> 
		<div class="form-group">
		  <label class="col-sm-3 control-label" for="textinput">${field.text!}</label>
		  <div class="col-sm-9">
		    <select name="${field.name!""}" id="${field.id!""}" class="form-control ace selectpicker ${field.isData!"myData"}" ${field.disabled!""}>
		    <#list field.options as op>
		      <option value="${op.value}" ${op.selected!""}>${op.text}</option>
		    </#list>
		    </select>
		  </div>
		</div>
	</#if>
	<#if field.type=="hidden"> 
		<input type="hidden" name="${field.name}" class="${field.isData!"myData"}"" value="${field.value!}" id="${(field.id)!}">
	</#if>		
</#list>
</#macro>