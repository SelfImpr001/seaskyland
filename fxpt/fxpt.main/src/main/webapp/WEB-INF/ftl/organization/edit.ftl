<#import "../commons/dialog.ftl" as orgDialog>
<#if !parent??>
	<#assign dispaly="none">
</#if>
<#assign hiddens=[  {"name":"pk","value":"${org.pk!''}","class":"myData"},
					{"name":"parent.pk","value":"${(parent.pk)!''}","class":"myData"}
				 ]>
				 
<#assign select1="">
<#assign select2="">
<#assign select3="">
<#assign select4="">			 
<#if parent??>
  <#if parent.type=1>
    <#assign select2="selected">
  <#elseif parent.type=2>
    <#assign select3="selected">
  <#elseif parent.type=3>
    <#assign select4="selected">
  </#if>
<#else>
  <#assign select1="selected">
</#if>

<@orgDialog.dialog showHeader=false showFooter=false hiddens=[] form={"id":"orgEdit","role":"form"}>
	<#import "../commons/form.ftl" as form>
	<#if org.pk??>
	<input type="hidden" class="myData" name="pk"  id="orgId" value="${org.pk?c}">
	</#if>
	<#if parent??>
	<input type="hidden" class="myData" name="parent.pk"  value="${parent.pk?c}">
	</#if>
	<#if hasDataRef=true >
	  <#assign fields=[{"name":"code","id":"code","text":"组织代码","type":"textRole","value":"${(org.code)!''}","placeholder":"请输入代码"},
					 {"name":"name","id":"name","text":"组织名称","type":"text","value":"${(org.name)!''}","placeholder":"请输入名称"},
					 {"text":"上级组织","type":"html","value":"${(parent.code)!}-${(parent.name)!}","display":"${dispaly!}","readonly":"readonly"},
					 {"name":"type","text":"所属机构","type":"select","disabled":"disabled","options":[{"text":"省教育厅","value":"1","selected":select1},
					 												{"text":"市教育局","value":"2","selected":select2},
					 												{"text":"县区教育局","value":"3" ,"selected":select3},
					 												{"text":"学校","value":"4","selected":select4}]}
					]>	  
	<@form.form fields=fields />
	<#if parent?? && parent.type=3>
	 	<div class="form-group">
		  <label class="col-sm-3 control-label" for="textinput">学校类型</label>
		  <div class="col-sm-9">
		    <select name="type" id="schoolTypeId" class="form-control ace selectpicker "myData">
		    <option value="">请选择</option>
		    <#list schoolTypeSelect as op>
	      		<option value="${op.id}">${op.name}</option>
		    </#list>
		    </select>
		  </div>
		</div>
		
		<div class="form-group">
		  <label class="col-sm-3 control-label" for="textinput">学校学段</label>
		  <div class="col-sm-9">
		    <select name="type" id="schoolSegmentId" class="form-control ace selectpicker "myData">
		    <option value="">请选择</option>
		    <#list schoolSegmentSelect as os>
	      		<option value="${os.id}">${os.name}</option>
		    </#list>
		    </select>
		  </div>
		</div>
	</#if>
	
	
	<#else>
	  <#assign orgOpts=[]>
	  <#list parentLevels as pl>
	    <#assign selected="">
	    <#if parent??&&pl.pk = parent.pk><#assign selected="selected"></#if>
	    <#assign orgOpts= orgOpts+[{"text":pl.name,"value":pl.pk?c,"selected":selected}]>
	    <#assign selected="">
	  </#list>
	 
	  <#assign fields=[{"name":"code","id":"code","text":"组织代码","type":"textRole","value":"${(org.code)!''}","placeholder":"请输入代码"},
					 {"name":"name","id":"name","text":"组织名称","type":"text","value":"${(org.name)!''}","placeholder":"请输入名称"},
					 {"name":"parent","text":"上级组织","type":"select","options":orgOpts,"isData":""},
					 {"name":"type","text":"所属机构","type":"select","disabled":"disabled","options":[{"text":"省教育厅","value":"1","selected":select1},
					 												{"text":"市教育局","value":"2","selected":select2},
					 												{"text":"县区教育局","value":"3" ,"selected":select3},
					 												{"text":"学校","value":"4","selected":select4}]}
					]>	
	<@form.form fields=fields />
	
	<#if parent?? && parent.type=3>
		<div class="form-group">
		  <label class="col-sm-3 control-label" for="textinput">学校类型</label>
		  <div class="col-sm-9">
		    <select name="type" id="schoolTypeId" class="form-control ace selectpicker "myData">
		    <#if schoolType??>
		      <option value="">请选择</option>
		     <#else>
		     <option value="" selected="true">请选择</option>
		    </#if>
		    <#if !schoolTypeFlg>
		    	 <option value=""></option>
		    </#if>
		    <#list schoolTypeSelect as op>
			    <#if schoolTypeFlg>
			    	<#if schoolType.id=op.id>
			      		<option value="${op.id}" selected="true">${op.name}</option>
			      	<#else>
			      		<option value="${op.id}">${op.name}</option>
			      	</#if>
			      <#else>
			     	 <option value="${op.id}">${op.name}</option>
				  </#if>
			    </#list>
		    </select>
		  </div>
		</div>
		
		<div class="form-group">
		  <label class="col-sm-3 control-label" for="textinput">学校学段</label>
		  <div class="col-sm-9">
		    <select name="type" id="schoolSegmentId" class="form-control ace selectpicker "myData">		         
		    <#if schoolSegment??>
		      <option value="">请选择</option>
		     <#else>
		     <option value="" selected="true">请选择</option>
		    </#if>
			     <#if !schoolFlg>
			    	 <option value=""></option>
			    </#if>
			    <#list schoolSegmentSelect as os>
			      <#if schoolFlg>
		      		<option value="${os.id}"  <#if schoolSegment.id=os.id> selected="true" </#if> >${os.name}</option>
			   	 <#else>
			   	 	<option value="${os.id}" >${os.name}</option>
			   	  </#if>
			    </#list>
		    </select>
		  </div>
		</div>
	</#if>
	
	</#if>
</@orgDialog.dialog>
