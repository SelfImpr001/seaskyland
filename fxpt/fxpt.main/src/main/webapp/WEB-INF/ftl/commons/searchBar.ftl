<#macro searchBar showSearchBar=true showTab=false tabs=[] searchBarId="" showPlaceholder=""> 
<div style="background:#eee; " class="clearfix">
	<#if showTab=true>
	    <ul role="tablist" class="nav nav-tabs modal-nav-tabs col-xs-8">
	        <#list tabs as tab> <li <#if tab_index==0>class="active"</#if>>
	        		<a data-toggle="tab" role="tab" href="${tab.href}">${tab.name}</a>
	        	</li>
	        </#list>
	    </ul>
    </#if>
    <#if showSearchBar=true>
    <div class="searchbar col-xs-4">
       <div class="input-group input-group-sm">
           <input type="text" class="form-control"  placeholder="${showPlaceholder!''}">
           <span class="input-group-btn">
               <button  type="button" id="${searchBarId}" class="btn btn-default"><span class="glyphicon ">查询</span></button>
           </span>
       </div>
    </div>
    </#if>
</div>
<div class="tab-content">
	<#nested>
</div>
</#macro>