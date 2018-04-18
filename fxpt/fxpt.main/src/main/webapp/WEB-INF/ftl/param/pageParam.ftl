<@pageHead.pageHead title="页面参数"/>
<div class="row">
	<div class="form-group form-horizontal">
		<label class="col-md-2 col-sm-2 control-label">分数段</label>
		<div class="col-md-4 col-sm-4">
			<input type="text" class="form-control myData"
				paramName="${(params.scoreSegment.paramName)!"scoreSegment"}" 
				paramAsName="${(params.scoreSegment.paramAsName)!"分数段"}" 
				paramType="${(params.scoreSegment.paramType)!"0"}" 
				value="${(params.scoreSegment.paramValue)!"10"}">
		</div>


		<label class="col-md-2 col-sm-2 control-label">名次段</label>
		<div class="col-md-4 col-sm-4">
			<input type="text" class="form-control myData"
				paramName="${(params.rankSegment.paramName)!"rankSegment"}" 
				paramAsName="${(params.rankSegment.paramAsName)!"名次段"}" 
				paramType="${(params.rankSegment.paramType)!"0"}" 
				value="${(params.rankSegment.paramValue)!"50"}">
		</div>
	</div>

	<div class="form-group form-horizontal">
		<label class="col-md-2 col-sm-2 control-label">优秀率</label>
		<div class="col-md-4 col-sm-4">
			<div class="input-group">
				<input type="text" class="form-control myData"
					paramName="${(params.yxl.paramName)!"yxl"}" 
					paramAsName="${(params.yxl.paramAsName)!"优秀率"}" 
					paramType="${(params.yxl.paramType)!"0"}" 
					value="${(params.yxl.paramValue)!"85"}"> <label
					class="input-group-addon">%</label>
			</div>

		</div>


		<label class="col-md-2 col-sm-2 control-label">良好率</label>
		<div class="col-md-4 col-sm-4">
			<div class="input-group">
				<input type="text" class="form-control myData"
					paramName="${(params.lhl.paramName)!"lhl"}" 
					paramAsName="${(params.lhl.paramAsName)!"良好率"}" 
					paramType="${(params.lhl.paramType)!"0"}" 
					value="${(params.lhl.paramValue)!"75"}"> <label
					class="input-group-addon">%</label>
			</div>
		</div>
	</div>

	<div class="form-group form-horizontal">
		<label class="col-md-2 col-sm-2 control-label">及格率</label>
		<div class="col-md-4 col-sm-4">
			<div class="input-group">
				<input type="text" class="form-control myData"
					paramName="${(params.jgl.paramName)!"jgl"}" 
					paramAsName="${(params.jgl.paramAsName)!"及格率"}"
					paramType="${(params.jgl.paramType)!"0"}" 
					value="${(params.jgl.paramValue)!"60"}"> <label
					class="input-group-addon">%</label>
			</div>
		</div>


		<label class="col-md-2 col-sm-2 control-label">低分率 小于</label>
		<div class="col-md-4 col-sm-4">
			<div class="input-group">
				<input type="text" class="form-control myData"
					paramName="${(params.dfl.paramName)!"dfl"}" 
					paramAsName="${(params.dfl.paramAsName)!"低分率"}" 
					paramType="${(params.dfl.paramType)!"0"}" 
					value="${(params.dfl.paramValue)!"35"}"> <label
					class="input-group-addon">%</label>
			</div>
		</div>
		
		<label class="col-md-2 col-sm-2 control-label">基本合格率</label>
		<div class="col-md-4 col-sm-4">
			<div class="input-group">
				<input type="text" class="form-control myData"
					paramName="${(params.jbhgl.paramName)!"jbhgl"}" 
					paramAsName="${(params.jbhgl.paramAsName)!"基本合格率"}" 
					paramType="${(params.jbhgl.paramType)!"0"}" 
					value="${(params.jbhgl.paramValue)!"45"}"> <label
					class="input-group-addon">%</label>
			</div>
		</div>
		
			<div style="height:30px">
			<label class="col-md-2 col-sm-2 control-label">有效总分</label>
			<div class="col-md-4 col-sm-4">
				<div class="input-group">
					<input type="radio" class="ace" name="zfSubject"
						value="wl"  <#if zfwl>checked="checked"</#if>>
						<span class="lbl"> </span>总分（wl） 
						<input type="radio" class="ace" name="zfSubject"
						value="languageType"   <#if zflanguage>checked="checked"</#if>>
						<span class="lbl"> </span>总分（languageType）
				</div>
			</div>
			</div>
		
	</div>
</div>

<div id="message" style="display: block; height: 30px; color: red;"></div>