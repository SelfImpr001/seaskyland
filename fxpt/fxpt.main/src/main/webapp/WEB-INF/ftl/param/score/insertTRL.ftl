<#macro tr name=""  score = ""  zf = false bianyuan = false type="" >
	<tr class="template">
		<td class="col-sm-3">
			<input type="text" class="form-control showEdit" name="${name}" >
			<label class="showValue"></label>
		</td>
		<td class="col-sm-3">
			<input type="text" class="form-control showEdit" name="${score}" >
			<label class="showValue"></label>
		</td>
		<#if type=="dabiao"> 
			 <td class="col-sm-2">
				<input type="text" class="form-control showEdit" name="divideScore" >
				<label class="showValue"></label>
			 </td>
		 </#if>
		 <#if zf>
		 	<@subjectZF.subject ></@subjectZF.subject>
		 <#else>
		 	<@subjectL.subject zf = true></@subjectL.subject>
		 </#if>
		 <#if bianyuan>
		 	<td class="col-sm-3">
				<label class="showValue" style="display: block;font-size: 12px;color: #CCC;">科目为总分才可设置边缘分</label>
			</td>
		 </#if>
		<td class="col-sm-3" <#if score == "ratio"> style="display: none;" </#if>><a href="#" class="delete">删除</a></td>
	</tr>
</#macro>