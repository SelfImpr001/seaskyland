<#--
* 
* headers=["",""]
* rows=[["","",""],["","",""]] 构造时请二维数据请保持与headers长度一致
* buttons=[{"pk":"","buttons":[{"type":"icon-gear","title":"",,"trigger":""}]] 构造时一维数据长度请与rows保持一致
* pager={}
* @author 李贵庆 2014-06-27
* 
-->

<#macro grid headers=[] rows=[] buttons=[] pager={} 
	showPager=false hasFirstCheckbox=false showSeq=false showTree=false idPreFix="my" hasNested=false checkedBoxName="myChecked" isAdmin={}>
  <!-- 表单体-->
  
  <div class="row">
  <#if showTree=true>
    <div id="${idPreFix}_ztreediv" class="col-xs-2" >
      <ul id="${idPreFix}_ztree1" class="ztree sortables" ><#--这里是树形节点加载的位置-->
      </ul>
    </div>
    <div id="${idPreFix}_datagrid" class="col-xs-10 data-grid">	      
  <#else>
    <div id="${idPreFix}_datagrid" class="col-xs-12 data-grid">
  </#if>
      <#if hasNested>
	    <#nested> 
      </#if>
      <table class="table table-bordered table-striped table-hover" style="background:#fff;">
        <thead>
        <@thead headers=headers showSeq=showSeq hasFirstCheckbox=hasFirstCheckbox />
        </thead>
        <tbody id="${idPreFix}_data_rows" >
        <#list rows as row>
          <#assign btn= {} >
          <#if buttons?size gt 0>
          <#assign btn= buttons[row_index] >
          </#if>
          <tr>       
          <#if showSeq=true>
            <td pk="${btn.pk?c}">${row_index+1}</td>
          </#if>
          <#if row?size gt 0>
            <#list row as cell>
	          <#if hasFirstCheckbox=true && cell_index==0>
	          <td><#if !isAdmin[cell?string]??> <input name="${checkedBoxName}" value="${cell}"  type="checkbox" class="ace" ></#if>  <span class="lbl"></span> </td>
	          <#elseif showSeq=true && ((cell_index==-1 && hasFirstCheckbox=false))>
	          <td>${row_index+1}</td>
	          <#else>
	          <td>${cell}</td>
	          </#if>
            </#list>
          </#if>
          <#if buttons?size gt 0>
            <td>                           
              <@optbtn buttons=btn.buttons pk=btn.pk />                            
            </td>
          </#if>
          </tr>
        </#list>  
        </tbody>
      </table>
      <#if showPager=true>
        <#import "../commons/pager.ftl" as myPage>
        <@myPage.pager id=idPreFix+"_pager" pager=pager/>
	  </#if>
    </div>
  </div>
</#macro>

<#macro thead headers=[] hasFirstCheckbox=false showSeq=false checkedBoxName="allChecked">
  <tr>
  <#if hasFirstCheckbox=true>
    <th><input name="${checkedBoxName}" type="checkbox" class="ace"> <span class="lbl"></span> </th>
  </#if>
  <#if showSeq=true>
    <th>序号</th>
  </#if>            
  <#list headers as h>
    <th>${h}</th>
  </#list>
  </tr>
</#macro>
<#macro optbtn buttons=[] style=1 pk=-1>
<#if style=1>  
  <div class="btn-group">
  <#list buttons as button>
    <#if button.type = "text">
    <a href="${button.href!"javascript:void(0);"}" pk=${pk?c} <#if button.trigger??>trigger="${button.trigger}"</#if> <#if button.target??>target="${button.target}"</#if> <#if button.class??>class="${button.class}"</#if> }' title="${button.title!""}"></a>
    <#if button_index+1 != btn.buttons?size><span class="separator">|</span></#if>
    <#else>
    <button pk=${pk?c} class="${button.class!"btn btn-xs btn-primary mr-2 grant"}" title="${button.title!""}" <#if button.trigger??>trigger="${button.trigger}"</#if> <#if button.name??>name="${button.name}"</#if>>                  
      <i class="${button.type!"icon-gear"} bigger-120"></i>
    </button>
    </#if>              
  </#list>
  </div>
 </#if>
</#macro>

