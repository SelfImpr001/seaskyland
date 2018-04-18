
<div class="form-item" id="report-modal"  >
	<input type="hidden" id="fxpt-etl-Data">
	<div class="panel panel-default">	
		<table class="table table-bordered table-hover ">
	        <tbody>
	            <tr>
	              <th>选择文件</th>
	              <td colspan="3"><input type="file" name="file" id="file" class="pull-left"/> &nbsp;&nbsp;<button id="uploadFile" type="button" class="pull-left btn btn-primary">上传文件</button></td>
	            </tr>
	            <tr id="sheetNamesTr" style="display: none;">
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
	    <div style="height:240px;overflow:auto">   
	    <table class="table table-bordered table-hover ">
	     <#if report??>
           <input type="hidden" name="pk" id="pk" value="${report.pk!}"  class="form-control myData kgStr">
           <input type="hidden" name="orgBirt.pk" id="orgBirt" value="${org!}"  class="form-control myData kgStr">
          </#if>
	  
	        <tbody> 
	         <tr>
		        <th><span defaultName="omrstr" fieldName="kgOmrStr">脚本名称</span></th>
		        	<td>
		        	<input type="text" name="name" id="name" value="${report.name!''}" class="form-control myData kgStr">
		            	
	  				 </td>
	  			<th><span defaultName="scoreStr" fieldName="kgScoreStr">脚本来源</span></th>
		        <td>
		         <input type="text" name="source" id="source" value="${report.source!''}"  class="form-control myData kgStr">
	  			</td>
	        </tr>
	         <tr>
		        <th><span defaultName="omrstr" fieldName="kgOmrStr" >存放地址</span></th>
		        	<td>
		        	<input readonly="true"  type="text"name="directory" id="directory" value="${report.directory!''}" class="form-control myData kgStr" >
		            <input readonly="true"  type="hidden"name="suffix" id="suffix"  class="form-control myData kgStr" >
		            	
	  				 </td>
	  			<th><span defaultName="scoreStr" fieldName="kgScoreStr">备注</span></th>
		        <td>
		         <input type="text" name="remark"id="remark" value="${report.remark!''}" class="form-control myData kgStr">
	  			</td>
	        </tr>
	         
	        </tbody>
	    </table>
	     </div>
	    <p id="etl-sheetMessage" class="error-tip"></p>
	</div>
</div>