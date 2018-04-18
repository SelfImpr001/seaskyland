<div class="row">
	<div class="col-md-${exam.wlForExamStudent?string("6","12")} uplingScoreTable">
		<div>
			<div class="btn-group pull-left" style="margin-bottom: 5px;">
				<button class="btn btn-primary btn-primary" type="button" trigger="addScroe" id="addScroeLineL" name="">${exam.wlForExamStudent?string("增加理科分数线","增加分数线")}</button>
			</div>
			<div id="message" style="float:left;margin-left:30px;color:red;"></div>
		</div>
		<@table.table tableId="" head=["名称","比例",'分数',"科目","边缘生统计分数线","操作"] css='editTable ${exam.wlForExamStudent?string("L","")}' wl='${exam.wlForExamStudent?string("1","0")}' scoreType=scoreMap["dabiao"]>
			<@trL.tr name="name"  score = scoreData["dabiao"] bianyuan = true zf = true type="dabiao" ></@trL.tr>
			<#list score_m['dabiao'].paramslsitL as us>
				<tr>
					<td class="col-sm-2">
						<input type="text" class="form-control showEdit" value='${us.name}' name="name"> 
						<label class="showValue"></label>
					</td>
					<td class="col-sm-2">
						<input type="text" class="form-control showEdit" value="${us.divideScale!''}" name="${scoreData['dabiao']}"> 
						<label class="showValue"></label>
					</td>
					<td class="col-sm-2">
						<input type="text" class="form-control showEdit" value="${us.divideScore!''}" name="${scoreData['youxiao']}"> 
						<label class="showValue"></label>
					</td>
					<@subjectZF.subject id = us.subject.id name="subjectId"></@subjectZF.subject>
					<#if us.ratio == 0>
						<td class="col-sm-2">
							<label class="showValue" style="display: block;font-size: 12px;color: #CCC;">科目为总分才可设置边缘分</label>
						</td>
					<#else>
						<td class="col-sm-2">
							<input type="text" class="form-control showEdit" name="ratio"  value="${us.ratio!''}" style="display: block;">
							<label class="showValue" style="display: none;"></label>
						</td>
					</#if>
					<td class="col-sm-2"><a href="#" class="delete">删除</a></td>
				</tr>
			</#list> 
		</@table.table>
	</div>

	<#if exam.wlForExamStudent>
		<div class="col-md-6 uplingScoreTable">
			<div>
				<div class="btn-group pull-left" style="margin-bottom: 5px;">
					<button class="btn btn-primary btn-primary" type="button" trigger="addScroe" id="addScroeLineW">增加文科分数线</button>
				</div>
				<div id="message" style="float:left;margin-left:30px;color:red;"></div>
			</div>
			<@table.table tableId="" head=["名称","比例","分数","科目","边缘生统计分数线","操作"] css="editTable W" wl='2' scoreType=scoreMap["dabiao"]>
				<@trW.tr name="name"  score = scoreData["dabiao"] bianyuan = true zf = true type="dabiao" ></@trW.tr>
				<#list score_m['dabiao'].paramslsitW as us>
					<tr>
						<td class="col-sm-2">
							<input type="text" class="form-control showEdit" value='${us.name}' name="name" >
							<label class="showValue"></label>
						</td>
						<td class="col-sm-2">
							<input type="text" class="form-control showEdit" value="${us.divideScale!''}" name="${scoreData['dabiao']}">
							<label class="showValue"></label>
						</td>
						<td class="col-sm-2">
							<input type="text" class="form-control showEdit" value="${us.divideScore!''}" name="${scoreData['youxiao']}"> 
							<label class="showValue"></label>
						</td>
						<@subjectZF.subject id = us.subject.id name="subjectId"></@subjectZF.subject>
						<#if us.ratio == 0>
							<td class="col-sm-2">
								<label class="showValue" style="display: block;font-size: 12px;color: #CCC;">科目为总分才可设置边缘分</label>
							</td>
						<#else>
							<td class="col-sm-2">
								<input type="text" class="form-control showEdit" name="ratio"  value="${us.ratio!''}" style="display: block;">
								<label class="showValue" style="display: none;"></label>
							</td>
						</#if>
						<td class="col-sm-2"><a href="#" class="delete">删除</a></td>
					</tr>
				</#list> 
			</@table.table>
		</div>
	</#if>
</div>