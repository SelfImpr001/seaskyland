<#macro subject id=0 name="" zf=false>
	<td class="col-sm-3">
		<select class="form-control selectpicker showEdit" name="subjectId">
			<option value="" >请选择</option>
			<#-- analysisTestpaper -->
			<#list analysisTestpaperL as tst>
				<#if id = tst.subject.id >
					<option value="${tst.subject.id}" selected >${tst.name}</option>
				<#else>
					<option value="${tst.subject.id}">${tst.name}</option>
				</#if>
			</#list>
			<#if zf>
				<option value="98" <#if id=98>selected </#if>>总分</option>
			</#if>
		</select>
		<label class="showValue"></label>
	</td>
</#macro>