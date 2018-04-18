<#import "../commons/table.ftl" as table>
<#if testPaper??>
<div class="tab-pane fade active in" style="overflow-y:auto;"  id="item-testPaper-${testPaper.id?string("#")}">
	
	<@table.table tableId="examList" >
		   <thead>
				<tr>
			   		<#if map.subjectId==1><th>对应科目</th></#if>
			    	<#if map.bigTitleNo==1><th>大题号</th></#if>
			    	<#if map.itemNo==1><th>小题号</th></#if>
			    	<#if map.fullScore==1><th>分值</th></#if>
			    	<#if map.titleType==1><th>题型</th></#if>
			    	<#if map.ischoice==1><th>选做题题型</th></#if>
			    	<#if map.optionType==1><th>选择题题型</th></#if>
			    	<#if map.rightOptioin==1><th>选择题答案</th></#if>
			    	<#if map.cjField==1><th>对应成绩字段</th></#if>
			    	<#if map.knowledgeContent==1><th>知识内容</th></#if>
			    	<#if map.knowledge==1><th>知识点</th></#if>
			    	<#if map.ability==1><th>能力结构</th></#if>
			    	<#if map.forecastDifficulty==1><th>预测难度</th></#if>
					<#if map.choiceGroup==1><th>选做题组</th></#if>
					<#if map.choiceModule==1><th>选做题模块</th></#if>
					<#if map.choiceNumber==1><th>选做题几选几</th></#if>
				    <#if map.choiceFullScore==1> <th>选做题满分</th></#if>
					<#if map.allOptions==1> <th>客观题全部选项</th></#if>
				</tr>
			 </thead>
		<#list items as item>
			<tr>
				<#if map.subjectId==1><td>${(item.subject.name)!""}</td></#if>
		    	<#if map.bigTitleNo==1><td>${item.bigTitleNo!""}</td></#if>
		    	<#if map.itemNo==1><td>${item.itemNo!""}</td></#if>
		    	<#if map.fullScore==1><td>${(item.fullScore)!""}</td></#if>
		    	<#if map.titleType==1><td>${item.titleType!""}</td></#if>
		    	<#if map.ischoice==1><td><#if item.choiceOrNot==0>非选做题<#else>选做题</#if></td></#if>
		    	<#if map.optionType==1><td><#if item.optionType==1> 单项选择题 <#elseif item.optionType==2>
				多项选择题 <#else> 主观题 </#if></td></#if>
		    	<#if map.rightOptioin==1><td>${item.rightOptioin!""}</td></#if>
		    	<#if map.cjField==1><td>${item.cjField!""}</td></#if>
		    	<#if map.knowledgeContent==1><td>${item.knowledgeContent!""}</td></#if>
		    	<#if map.knowledge==1><td>${item.knowledge!""}</td></#if>
		    	<#if map.ability==1><td>${item.ability!""}</td></#if>
		    	<#if map.forecastDifficulty==1><td>${((item.forecastDifficulty)!0)?string("0.00")}</td></#if>
				<#if map.choiceGroup==1><td>${item.choiceGroup!""}</td></#if>
				<#if map.choiceModule==1><td>${item.choiceModule!""}</td></#if>
				<#if map.choiceNumber==1><td>${item.choiceNumber!""}</td></#if>
			    <#if map.choiceFullScore==1><td>${item.choiceFullScore!""}</td></#if>
				<#if map.allOptions==1><td>${item.allOptions!""}</td></#if>
			</tr>
		</#list>
	</@table.table>
</div>
</#if>
