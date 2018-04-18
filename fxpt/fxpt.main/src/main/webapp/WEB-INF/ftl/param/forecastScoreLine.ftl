<@pageHead.pageHead title="预测分数线"/>

<#--  (低分人数是否包含高分人数 -->
<div style="height:30px">
<label class="col-md-2 col-sm-2 control-label">低分人数是否包含高分人数</label>
	<div class="col-md-4 col-sm-4">
		<div class="input-group">
			<input type="radio" class="ace myData" name="f"
					paramName="${(params.scoreDip.paramName)!"scoreDip"}" 
					paramAsName="${(params.scoreDip.paramAsName)!"低分人数是否包含高分人数"}" 
					paramType="${(params.scoreDip.paramType)!"0"}" 
					value="${(params.scoreDip.paramValue)!"1"}" <#if flg>checked="checked"</#if> >
					<span class="lbl"> </span>包含 
			<input type="radio" class="ace myData" name="f"
					paramName="${(params.scoreDip.paramName)!"scoreDip"}" 
					paramAsName="${(params.scoreDip.paramAsName)!"低分人数是否包含高分人数"}" 
					paramType="${(params.scoreDip.paramType)!"0"}" 
					value="${(params.scoreDip.paramValue)!"0"}"  <#if !flg>checked="checked"</#if>>
					<span class="lbl"> </span>不包含
		</div>
	</div>
</div>
<#--  低分人数是否包含高分人数 ) -->

<#-- (隐藏元素 -->
<div class="hidden"> 
	<input id="examWL" type="hidden" value="${exam.wlForExamStudent?string("true","false")}"/>
</div>
<#-- 隐藏元素) -->

<#-- (tag 标签 -->
<div style="margin-bottom: 10px;">
	<ul id="myTab" class="nav nav-tabs">
		<#list setting?keys as set>
			<#if set_index==0>
				<li class="active"><a href="#${set}" data-toggle="tab">${setting[set]}</a></li>
			<#else>
				<li><a href="#${set}" data-toggle="tab">${setting[set]}</a></li>
			</#if>
		</#list>
	</ul>
</div>
<#-- tag 标签) -->

<#-- (tag 标签对应快 -->
<#assign scoreData={"youxiao":"divideScore","yuce":"divideScore","dabiao":"divideScale","jianzi":"divideScale","difen":"divideScale","bianyuan":"ratio"} >
<div id="myTabContent" class="tab-content">
	<div class="tab-pane fade in active" id="yuce">
		<#include "./score/yuce.ftl">
	</div>
	<div class="tab-pane fade" id="youxiao">
		<#include "./score/youxiao.ftl">
	</div>
	<div class="tab-pane fade" id="dabiao">
		<#include "./score/dabiao.ftl">
	</div>
	<div class="tab-pane fade" id="jianzi">
		<#include "./score/jianzi.ftl">
	</div>
	<div class="tab-pane fade" id="difen">
		<#include "./score/difen.ftl">
	</div>
</div>
<#-- tag 标签对应快) -->