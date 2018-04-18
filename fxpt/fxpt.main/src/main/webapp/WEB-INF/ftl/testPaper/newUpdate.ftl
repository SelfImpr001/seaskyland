<div class="modal-body">
	<form id="form1" name="form1" method="post" action="">
	<#--对应的科目-->
		<div class="select-group clearfix">
			<label class="col-sm-3 control-label" for="m-password1">对应的科目:</label>
			<div class="col-sm-9">
				<select class="selectpicker form-control myData" id="subjectId"
					name="subject.id"> <#list subjects as subject> <#if
					subject.id == testPaper.subject.id>
					<option value="${subject.id}" selected>${subject.name}</option>
					<#else>
					<option value="${subject.id}">${subject.name}</option> </#if>

					</#list>
				</select>
			</div>
		</div>
	
		<#--/对应的科目--> 
		<#--试卷名称-->
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">试卷名称:</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" name="name"
					id="m-password1" placeholder="试卷名称" value="${testPaper.name}">
			</div>
		</div>
		<#--试卷名称--> <#--试卷满分-->
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">试卷满分:</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" id="m-password1"
					name="fullScore" placeholder="试卷满分" value="${testPaper.fullScore}">
			</div>
		</div>
		<#--/试卷满分--> <#--客观题满分-->
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">客观题满分:</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" id="m-password1"
					name="kgScore" placeholder="客观题满分" value="${testPaper.kgScore}">
			</div>
		</div>
		<#--/客观题满分:--> <#--主观题满分-->
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">主观题满分:</label>
			<div class="col-sm-9">
				<input type="text" class="form-control myData" id="m-password1"
					name="zgScore" placeholder="主观题满分" value="${testPaper.zgScore}">
			</div>
		</div>
		<#--/主观题满分--> <#--试卷类型-->
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">试卷类型:</label>
			<div class="col-sm-9 radio-content">
				<div class="download-check">
					<input name="paperType" type="radio" value="0" class="ace myData"
						${(testPaper.paperType==0)?string("checked","")} > <span
						class="lbl">未分卷 </span>
				</div>
				<div class="download-check">
					<input name="paperType" type="radio" value="1" class="ace myData"
						${(testPaper.paperType==1)?string("checked","")}> <span
						class="lbl">理科试卷</span>
				</div>
				<div class="download-check">
					<input name="paperType" type="radio" value="2" class="ace myData"
						${(testPaper.paperType==2)?string("checked","")}> <span
						class="lbl">文科试卷</span>
				</div>
			</div>
		</div>

		<#--/试卷类型--> <#--是否综合试卷-->
		<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">是否综合试卷:</label>
			<div class="col-sm-9  radio-content">
				<div class="download-check">
					<input name="composite" type="radio" value="false"
						class="ace myData"${testPaper.composite?string("","checked")}>
					<span class="lbl"> </span>否
				</div>
				<div class="download-check">
					<input name="composite" type="radio" value="true"
						class="ace myData"${testPaper.composite?string("checked","")}>
					<span class="lbl"> </span>是
				</div>
			</div>
		</div>
		<#--/是否综合试卷--> <input type="hidden" class="myData" name="id"
			id="examId" value="${testPaper.id}">
	</form>
</div>

