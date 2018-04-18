    <div class="col-xs-12" id="paper-info">
    <!--试卷列表-->
    <table class="table table-striped table-bordered table-hover">
        <thead>
        <tr>
              <th>序号</th>
		      <th>试卷</th>
		      <th>科目</th>
		      <th>题号</th>
		      <th>满分</th>
		      <th>大题号</th>
		      <th>知识点</th>
		      <th>知识内容</th>
		      <th>能力结构</th>
		      <th>题型</th>
		      <th>预测难度</th>
		      <th>选择类型</th>
		      <th>选择答案</th>
		      <th>对应成绩字段</th>
		      <th>是否选做题</th>
		      <th>选做题组</th>
		      <th>选做题模块</th>
		      <th>几选几</th>
		      <th>选做题满分</th>
        </tr>
        </thead>
        <tbody>
		  	<#if items??>
		  		<#list items as item>
		  			<tr>
				      <td>${item.sortNum!""}</td>
				      <td>${item.testPaper.name!""}</td>
				      <td>${(item.subject.name)!""}</td>
				      <td>${item.itemNo!""}</td>
				      <td>${((item.fullScore)!0)?string("0.00")}</td>
				      <td>${item.bigTitleNo!""}</td>
				      <td>${item.knowledge!""}</td>
				      <td>${item.knowledgeContent!""}</td>
				      <td>${item.ability!""}</td>
				      <td>${item.titleType!""}</td>
				      <td>${((item.forecastDifficulty)!0)?string("0.00")}</td>
				      <td>
				      <#if item.optionType==1>
				      	单项选择题
				      <#elseif item.optionType==2>
				      	多项选择题
				      <#else>
				      	非选做题
				      </#if>
				      </td>
				      <td>${item.rightOptioin!""}</td>
				      <td>${item.cjField!""}</td>
				      <td>${((item.choice)!false)?string("是","不是")}</td>
				      <td>${item.choiceGroup!""}</td>
				      <td>${item.choiceModule!""}</td>
				      <td>${item.choiceNumber!""}</td>
				      <td>${((item.choiceFullScore)!0)?string("0.00")}</td>
				    </tr>
		  		</#list>
		  	</#if>
        </tbody>
    </table>
    <input type="hidden" id="title"  value="title" />
 </div>



<#--
<table class="table table-bordered table-hover ">
  <thead>
    <tr>
      <th>序号</th>
      <th>试卷</th>
      <th>科目</th>
      <th>题号</th>
      <th>满分</th>
      <th>大题号</th>
      <th>知识点</th>
      <th>知识内容</th>
      <th>能力结构</th>
      <th>题型</th>
      <th>预测难度</th>
      <th>选择类型</th>
      <th>选择答案</th>
      <th>对应成绩字段</th>
      <th>是否选做题</th>
      <th>选做题组</th>
      <th>选做题模块</th>
      <th>几选几</th>
      <th>选做题满分</th>
    </tr>
  </thead>
  <tbody>
  	<#if items??>
  		<#list items as item>
  			<tr>
		      <td>${item.sortNum!""}</td>
		      <td>${item.testPaper.name!""}</td>
		      <td>${(item.subject.name)!""}</td>
		      <td>${item.itemNo!""}</td>
		      <td>${((item.fullScore)!0)?string("0.00")}</td>
		      <td>${item.bigTitleNo!""}</td>
		      <td>${item.knowledge!""}</td>
		      <td>${item.knowledgeContent!""}</td>
		      <td>${item.ability!""}</td>
		      <td>${item.titleType!""}</td>
		      <td>${((item.forecastDifficulty)!0)?string("0.00")}</td>
		      <td>
		      <#if item.optionType==1>
		      	单项选择题
		      <#elseif item.optionType==2>
		      	多项选择题
		      <#else>
		      	非选做题
		      </#if>
		      </td>
		      <td>${item.rightOptioin!""}</td>
		      <td>${item.cjField!""}</td>
		      <td>${((item.choice)!false)?string("是","不是")}</td>
		      <td>${item.choiceGroup!""}</td>
		      <td>${item.choiceModule!""}</td>
		      <td>${item.choiceNumber!""}</td>
		      <td>${((item.choiceFullScore)!0)?string("0.00")}</td>
		    </tr>
  		</#list>
  	</#if>
  </tbody>
</table>
-->
