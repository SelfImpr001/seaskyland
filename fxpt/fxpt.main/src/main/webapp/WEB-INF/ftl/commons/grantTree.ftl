<#macro grantTree treeName="" leftTree="" rightTree="" showTab=false  tab={"id":"","class":""} >
<#if showTab=true>
<div id="${tab.id}" class="${tab.class}">
</#if>
<table class="table table-bordered">
<thead>
    <tr>
        <th class="empower-th not-allow" style="text-align:left">系统授权<div class="pull-right"> 
        [<a ${treeName}="checked" href="#" title="勾选"><i class="icon-check"></i></a>]
        [<a  ${treeName}="unchecked" href="#" title="取消勾选"><i class="icon-unchecked"></i></a>]</div></th>
        <th style="display:none;"></th>
        <th class="empower-th not-allow" style="display:none;">已分配</th></th>
    </tr>
</thead>    
<tbody> 
    <tr>
        <td width="100%">
           <div class="modal-lefttree">
           		<ul id="${leftTree}" class="ztree modal-ztree-checkbox"></ul>
           </div>
        </td>
        <td width="10%" style="display:none;">
                       
        </td>
        <td width="45%" style="display:none;">
            <div class="modal-righttree">
           		 <ul id="${rightTree}" class="ztree modal-ztree-checkbox"></ul>
            </div>                   
        </td>
    </tr>      
</tbody>
</table>
<#if showTab=true>
</div>
</#if>
</#macro>