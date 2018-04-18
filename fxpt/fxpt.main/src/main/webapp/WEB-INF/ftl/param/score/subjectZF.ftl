<#macro subject id=0 name="">
	<td class="col-sm-3">
		<select class="form-control selectpicker showEdit" name="subjectId">
			<option value="" >请选择</option>
			<option value="98" <#if id=98>selected </#if> >总分</option>
		</select>
		<label class="showValue"></label>
	</td>
</#macro>