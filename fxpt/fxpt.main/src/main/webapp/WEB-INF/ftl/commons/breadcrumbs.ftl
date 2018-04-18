<#macro breadcrumbs path=[] currentPath="" show=false>
<#if show=true>
<div id="breadcrumbs" class="breadcrumbs">
  <ul class="breadcrumb">
    <li><i class="icon-home home-icon"></i><a href="${request.contextPath}">首页</a></li>
    <#list path as p>
    <li class="active"><a href="#">${p}</a></li>
    </#list>
    <li class="active">${currentPath}</li>
  </ul>
</div>
<div class="page-content">
<#nested>
</div>
<#else>
  <#nested>
</#if>
</#macro>