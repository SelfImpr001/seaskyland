<@pageHead.pageHead title="自定义科目"
buttons=[{"id":"returnExamList","name":"","text":"<<返回上级页面"}]/>

<div>
	<div class="btn-group pull-left">
      		<button class="btn btn-primary btn-primary" type="button" trigger=""
		id="addCustomizeSubject" name="">增加</button>
      </div>
	
	<div id="message" style="float:left;margin-left:30px;color:red;"></div>
</div>

<#assign tableNames = ["科目名称","包含学科","操作"]>
<@table.table tableId="customizeSubject" head=tableNames css="editTable">

<tr class="template">
	<td style="width: 40%;"><input type="text"
		class="form-control showEdit"><label class="showValue"></label></td>
	<td style="width: 50%; text-align: left;">
	<select	class="form-control  showEdit" multiple>
			<#if testPapers??>
				<#list testPapers as tp>
					<option data-id="${tp.id}"  data-paperType="${tp.paperType}" data-fullScore="${tp.fullScore}">${tp.name}</option>
				</#list>
			</#if>
	</select> <label class="showValue"></label></td>
	<td style="width: 10%;"><a href="#" class="delete">删除</a></td>
</tr>

<#if combinationSubjects??>
	<#list combinationSubjects as cs>
		<tr>
			<td style="width: 40%;"><input value="${cs.name}" combinationSubjectId=${cs.id} type="text"
			class="form-control showEdit"><label class="showValue"></label></td>
			
			<td style="width: 40%; text-align: left;">
	<select	class="form-control selectpicker showEdit" multiple>
			<#if testPapers??>
				<#list testPapers as tp>
					<#assign hasTp = false>
					<#if cs.childTestPaper??>
						<#list cs.childTestPaper as tmpTp>
							<#if tmpTp.testPaper.id==tp.id>
								<#assign hasTp = true>
								<#break>
							</#if>
						</#list>
					</#if>
					
					<#if hasTp>
						<option data-id="${tp.id}"  data-paperType="${tp.paperType}" data-fullScore="${tp.fullScore}" selected>${tp.name}</option>
					<#else>
						<option data-id="${tp.id}"  data-paperType="${tp.paperType}" data-fullScore="${tp.fullScore}">${tp.name}</option>
					</#if>
				</#list>
			</#if>
	</select> <label class="showValue"></label>
	</td>
			
			<td style="width: 10%;"><a href="#" title="" 
			class="delete" data-id="${cs.id}">删除</a></td>
		</tr>
	</#list>
</#if>


</@table.table>
