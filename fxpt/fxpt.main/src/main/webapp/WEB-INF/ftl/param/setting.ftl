<#import "../commons/pageHead.ftl" as pageHead> 
<#import "table.ftl" as table>
<#import "./score/subjectW.ftl" as subjectW>
<#import "./score/subjectL.ftl" as subjectL>
<#import "./score/subjectZF.ftl" as subjectZF>
<#import "./score/insertTRW.ftl" as trW>
<#import "./score/insertTRL.ftl" as trL>

<div class="page-content">
<div id="customizeSubjectContainer">
	<#include "./customizeSubject.ftl">
</div>

<div id="mainSubjectContainer">
	<#include "./mainSubjectSetting.ftl">
</div>

<div id="forecastScoreLine">
	<#include "./forecastScoreLine.ftl">
</div>

<div id="pageParam">
	<#include "./pageParam.ftl">
</div>

<div class="row" style="margin-top:20px;">
	<div class="col-md-12">
		 <div class="btn-group pull-right">
		 	<button class="btn btn-primary btn-primary" type="button" trigger="" id="saveParam" name="">保存参数设置</button>
		 </div>
	</div>
</div>	
</div>