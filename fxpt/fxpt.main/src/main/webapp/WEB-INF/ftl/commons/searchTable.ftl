<#macro searchTable headers=[] hasFirstCheckbox=true bodyId="" rows=[]  showPager=false  showTab=false showSeq=false
pager={"curpage":1,"totalpage":1,"pagesize":10} tab={"id":"","class":""} checkbox={"headName":"","name":""} style="overflow:auto;">
<div style="${style}"  <#if showTab=true>id="${tab.id}" class="${tab.class}"</#if>>
<table class="table table-bordered table-striped table-hover">
<thead>
    <tr>
	<#if hasFirstCheckbox=true>
	   <th><input name="${checkbox.headName}" type="checkbox" class="ace"> <span class="lbl"></span> </th>
	</#if>
    <#if showSeq=true>
       <th>序号</th>
    </#if>
	<#list headers as h>
	   <th>${h}</th>
	</#list>
	</tr>
</thead>
<tbody id="${bodyId}">
   <#list rows as row>
      <tr>
      <#if row?size gt 0>
        <#list row as cell>
            <#if hasFirstCheckbox=true && cell_index==0>
            	<td><input name="${checkbox.name}" value="${cell}" type="checkbox" class="ace"> <span class="lbl"></span> </td>
            <#elseif showSeq=true && ((cell_index==0 && hasFirstCheckbox=false)
             || (cell_index==1 && hasFirstCheckbox=true))>
            	<td>${row_index+1}</td>
             <#else>
            	<td>${cell}</td>
            </#if>
        </#list>
      </#if>
      </tr>
    </#list>               
</tbody>
</table> 
<#if showPager=true>
  <#import "../commons/pager.ftl" as myPage>
  <@myPage.pager id=tab.id+"_inner_page" pager=pager/>
</#if>
</div>
</#macro>