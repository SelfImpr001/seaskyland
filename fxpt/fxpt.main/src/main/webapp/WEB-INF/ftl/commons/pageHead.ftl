<#macro pageHead buttons=[] title="" group=false type="button">
  <!-- 表单头-->
  <div class="page-header">
    <h1 style="margin:0">
      <i class="icon-desktop" style="color:#428BCA;font-size: 22px;position: relative;top: 2px;margin-right: 8px;"></i> ${title}
      <#if group=true>
      <div class="btn-group pull-right">
      <#else>
      <div class="pull-right">
      </#if>
      <#list buttons as button>
      	<#if type=="a" || type=="A">
      	  <a  href="#" id="${button.id!""}" name="${button.name!""}" class="small ${button.class!""}" style="text-decoration: none;margin-right:10px;${button.style!""}">
      		<i class="${button.icon!"icon-share"}" style="font-size:20px;padding-right:5px;${button.iconstyle!""}"></i>${button.text!""}
      	  </a>
      	<#else>
      	  <button class="btn btn-primary ${button.class!""}"  type="button" trigger="${button.trigger!""}" id="${button.id!""}" name="${button.name!""}">${button.text!""}</button>
      	</#if>
      </#list>
      </div>
    </h1>
  </div>
</#macro>