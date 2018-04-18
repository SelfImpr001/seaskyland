<div class="form-item">
	<input type="hidden" id="fxpt-etl-Data">
	<div class="panel panel-default">	
		<table class="table table-bordered table-hover ">
	        <tbody>
	            <tr>
	              <th>选择文件</th>
	              <td colspan="3"><input type="file" name="file" id="file" class="pull-left"/> &nbsp;&nbsp;<button id="uploadFile" type="button" class="pull-left btn btn-primary">上传文件</button></td>
	            </tr>
	            <th style="text-align:right;">已选文件</th>
	              <td style="width:30%;"><span id="fileName"></span></td>
	              <th style="text-align:right;">选择工作簿</th>
	              <td style="width:30%;">
	              <select class="form-control myData" id="sheetNames" name="sheetNames">
	              <option value="">=选择工作簿===</option>
  				  	</select>
	              </td>
	        </tbody>
	    </table>
	    <p id="etl-sheetMessage" class="error-tip"></p>
	    <br/>
	    <hr/>
	    <table id="kgStrTable" class="table table-bordered table-hover " style="display: none;">
	        <tbody>
	        <tr>
		        <th><span defaultName="omrstr" fieldName="kgOmrStr">客观题选择字符串</span></th>
		        	<td>
		            	<select class="form-control myData kgStr" id="kgOmrStr">
		              		<option value="">==选择字段==</option>
	  				  	</select>
	  				 </td>
	  			<th><span defaultName="scoreStr" fieldName="kgScoreStr">客观题得分字符串</span></th>
		        <td>
		           	<select class="form-control myData kgStr" id="kgScoreStr">
		              <option value="">==选择字段==</option>
	  			  	</select>
	  			</td>
	        </tr>
	     </table>
	    <hr/>
	    <table class="table table-bordered table-hover ">
	        <tbody>     
	        <#if dataFields??>
	        	<#list dataFields as dataField>
	        		<#if dataField_index % 2 == 0>
	        			<tr>
	        		</#if>
	        		<th><span class="etl-field" fieldName="${dataField.fieldName}" 
	        		asName="${dataField.asName}" defaultName="${dataField.defaultName!""}" 
	        		sortNum="${dataField.sortNum}" isNeed="${dataField.need?string("true","false")}" 
	        		isSelItem="${dataField.selItem?string("true","false")}" isSelOption="${dataField.selOption?string("true","false")}"
	        		>${dataField.asName}</span></th>
	              <td>
	              	<select class="form-control myData etlSelect" id="${dataField.fieldName}">
	              <option value="">==选择字段==</option>
  				  	</select>
  				  	</td>
	        		<#if dataField_index % 2 == 1>
	        		</tr>
	        		</#if>
	        	</#list>
	        	
	        	<#if (dataFields?size%2==1)>
	        		<th></th>
	              <td></td>
	              </tr>
	        	</#if>
	        </#if>
	        <!--
	        <tr>
	              <th colspan="4"><button id="importDataOk" type="button" class="btn btn-primary pull-right">导入</button></th>
                </tr>
                -->
	        </tbody>
	    </table>
	</div>
</div>