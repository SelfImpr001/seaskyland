<div class="page-content">
<#import "../commons/pageHead.ftl" as pageHead>
<form id="statistic">
 <#assign btns=[{"text":"保存","id":"saveSetting","class":"btn-primary","closeBtn":false}]>
 <#if setStatus==0>
<@pageHead.pageHead title="${title!'统计口径设置'}" buttons=btns/>
 <#else>
 <@pageHead.pageHead title="${title!'统计口径设置'}" buttons=[]/>
 </#if>

 <div class="form-item">
  <div class="panel panel-default">
    <table class="table table-bordered table-hover ">
      <tbody>
      <#if list??>
      		<tr>
	          <th style="text-align:right">单科统计</th>
	          <td>
	          <#list list as statistic>
						<#if statistic.stype=1>
							<#if statistic.checked=1>
							<input type="radio" name="single" value="${statistic.id}" checked="true" <#if setStatus==1>disabled</#if>>${statistic.sname}</input>&nbsp;&nbsp;
							<#else>
							<input type="radio" name="single" value="${statistic.id}" <#if setStatus==1>disabled</#if>>${statistic.sname}</input>&nbsp;&nbsp;
							</#if>
						</#if>
					</#list> 	
				</td>		
	        </tr>
	        
	      <tr>
	          <th style="text-align:right">多科合并统计</th>
	          <td>
	          <#list list as statistic>
						<#if statistic.stype=2>
							<#if statistic.checked=1>
							<input type="radio" name="multi" value="${statistic.id}" checked="true" <#if setStatus==1>disabled</#if>>${statistic.sname}</input>&nbsp;&nbsp;
							<#else>
							<input type="radio" name="multi" value="${statistic.id}" <#if setStatus==1>disabled</#if>>${statistic.sname}</input>&nbsp;&nbsp;
							</#if>
						</#if>
					</#list> 	
				</td>		
	        </tr>
	        
	        <tr>
	          <td colspan="2" style="text-align:left;">
	        	单科统计：单一学科或综合试卷统计分析<br>
	        	多科合并统计：总分或自定义学科组合统计分析<br>
	          </td>
	        </tr>
      </#if>
      </tbody>
    </table>
  </div>
</div>
</from>
</div>