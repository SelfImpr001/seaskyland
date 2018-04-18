<div class="row">
	<div class="col-md-${exam.wlForExamStudent?string("6","12")} uplingScoreTable">
		<div>
			<div class="btn-group pull-left" style="margin-bottom: 5px;">
				<button class="btn btn-primary btn-primary" type="button" trigger="addScroe">${exam.wlForExamStudent?string("增加理科分数线","增加分数线")}</button>
			</div>
			<div id="message" style="float:left;margin-left:30px;color:red;"></div>
		</div>
		<@table.table tableId="" head=["名称","名次","科目","操作"] css='editTable ${exam.wlForExamStudent?string("L","")}' wl='${exam.wlForExamStudent?string("1","0")}' scoreType=3 level=2 >
			<@trL.tr name="name"  score = scoreData["difen"] zf = false ></@trL.tr>
			<#list score_m['difen'].paramslsitL as us>
				<tr>
					<td class="col-sm-3">
						<input type="text" class="form-control showEdit" value='${us.name}' name="name">
						<label class="showValue"></label>
					</td>
					<td class="col-sm-3">
						<input type="text" class="form-control showEdit" value="${us.divideScale!''}" name="${scoreData['difen']}">
						<label class="showValue"></label>
					</td>
					<@subjectL.subject id = us.subject.id name="subjectId" zf = true></@subjectL.subject>
					<td class="col-sm-3"><a href="#" class="delete">删除</a></td>
				</tr>
			</#list> 
		</@table.table>
	</div>

	<#if exam.wlForExamStudent>
		<div class="col-md-6 uplingScoreTable">
			<div>
				<div class="btn-group pull-left" style="margin-bottom: 5px;">
					<button class="btn btn-primary btn-primary" type="button" trigger="addScroe">增加文科分数线</button>
				</div>
				<div id="message" style="float:left;margin-left:30px;color:red;"></div>
			</div>
			<@table.table tableId="" head=["名称","名次","科目","操作"] css="editTable W" wl='2' scoreType=3 level=2>
				<@trW.tr name="name"  score = scoreData["difen"] zf = false></@trW.tr>
				<#list score_m['difen'].paramslsitW as us>
					<tr>
						<td class="col-sm-3">
							<input type="text" class="form-control showEdit" value='${us.name}' name="name">
							<label class="showValue"></label>
						</td>
						<td class="col-sm-3">
							<input type="text" class="form-control showEdit" value="${us.divideScale!''}" name="${scoreData['difen']}">
							<label class="showValue"></label>
						</td>
						<@subjectW.subject id = us.subject.id name="subjectId" zf = true></@subjectW.subject>
						<td class="col-sm-3"><a href="#" class="delete">删除</a></td>
					</tr>
				</#list> 
			</@table.table>
		</div>
	</#if>
</div>