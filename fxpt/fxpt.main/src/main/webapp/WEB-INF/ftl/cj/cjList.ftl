<#import "../commons/pageHead.ftl" as pageHead> 
<div class="page-content">
  	<@pageHead.pageHead title="成绩信息" buttons=[{"id":"returnPage","name":"","text":"<<返回上级页面"}]/>
  
    <!--试卷列表-->
    <div style="overflow-y:auto;">
    <table class="table table-striped table-bordered table-hover">
      <thead>
        <tr>
       	  <#if map.zkzh==1><th>准考证号</th></#if>
          <#if map.isQk==1><th>是否缺考</th></#if>
          <#if map.totalScore==1><th>试卷得分</th></#if>
          <#if map.kgScore==1><th>客观题得分</th></#if>
          <#if map.zgScore==1><th>主观题得分</th></#if>
          <!--
          <#if map.omrstr==1><th>客观题选择串</th></#if>
          <#if map.scorestr==1><th>客观题得分串</th></#if>
            -->
          <#if items??>
          <#list items as item>
          <#if item.optionType==0>
          <th>${item.sortNum!""}得分</th>
          <#else>
          <th>${item.sortNum!""}得分</th>
          <th>${item.sortNum!""}选项</th>
          </#if>  		
          
          </#list>
          </#if> </tr>
      </thead>
      <tbody id="cj-ListTBody">
      	<#include "./cjListTBody.ftl">		
      </tbody>
      
    </table>
    </div>
    <div style="text-align: center">
		<div id="cj-cjList-pager" class="pager" style="margin-top: 20px; text-align: center"></div>
	</div>
 </div>
