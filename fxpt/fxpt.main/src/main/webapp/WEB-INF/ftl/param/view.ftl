<!-- 索引栏-->
<div id="breadcrumbs" class="breadcrumbs">
    <ul class="breadcrumb">
			<li> <i class="icon-home home-icon"></i> <a href="${request.contextPath}">主页</a> </li>
        	<li> <a href="javascript:window.location='./'+$('#uuId').val();">考试列表</a> </li>
			<li class="active">分析参数设置</li>
	</ul>
</div>
   
<!-- 表单内容-->
<div class="page-content">
    <!-- 表单头-->
   	<div class="page-header">
   		<h1>
              <i class="icon-hand-right"></i> 分析参数设置
        </h1>
    </div>
	<!-- 表单体-->
	<div class="row">
		<div id="form-01" class="col-xs-12">
		    <table class="table table-bordered table-striped table-hover">
	        <thead>
	            <tr>
	                <th  colspan="2">统计口径</th>                                                                               
	            </tr>
	        </thead>
	        <tbody>
	        <tbody>
	       <#if examParameterMap??>
      		<tr>
	          <th>${examParameterMap["subjectRange"].parameterAsName}</th>
	          <td>
	          	<#assign subjectRangeValues=[{"value":"1","name":"统计不缺考"},{"value":"2","name":"统计缺考"},{"value":"3","name":"统计分数大于0"},{"value":"4","name":"统计不缺考分数大于0"}]>
	          	<select class="form-control examparameter" parameterAsName="${examParameterMap["subjectRange"].parameterAsName}" parameterName="${examParameterMap["subjectRange"].parameterName}" parameterId="${examParameterMap["subjectRange"].id}">
	          		<#list subjectRangeValues as tmpValue>
	          			<#if examParameterMap["subjectRange"].parameterValue == tmpValue.value>
	          				<option value="${tmpValue.value}" selected>${tmpValue.name}</option>
	          			<#else>
	          			    <option value="${tmpValue.value}">${tmpValue.name}</option>
	          			</#if>
	          		</#list>
	          	</select>
	          </td>
	        </tr>
	        <tr>
	          <th>${examParameterMap["totalSubjectRange"].parameterAsName}</th>
	          <td>
	          	<#assign subjectRangeValues=[{"value":"0","name":"包含缺考"},{"value":"1","name":"至少一科不缺考"},{"value":"3","name":"所有科目都不缺考"},{"value":"4","name":"至少一科不缺考且分数大于0"},{"value":"5","name":"所有科目不缺考且分数大于0"}]>
	          	<select class="form-control examparameter" parameterAsName="${examParameterMap["totalSubjectRange"].parameterAsName}" parameterName="${examParameterMap["totalSubjectRange"].parameterName}" parameterId="${examParameterMap["totalSubjectRange"].id}">
	          		<#list subjectRangeValues as tmpValue>
	          			<#if examParameterMap["totalSubjectRange"].parameterValue == tmpValue.value>
	          				<option value="${tmpValue.value}" selected>${tmpValue.name}</option>
	          			<#else>
	          			    <option value="${tmpValue.value}">${tmpValue.name}</option>
	          			</#if>
	          		</#list>
	          	</select>
	          </td>
	        </tr>      
	         <tr>
	          <td colspan="2" style="text-align:right">
	        	<button id="saveParameterOk" type="button" class="btn btn-primary">保存</button>
	          </td>
	        </tr>
            </#if>
	        </tbody>
	    </table>
		    <div id="combinationSubjects"></div>
	</div>
</div>

<#--
<div class="heading-main">
  <div class="right-area"></div>
</div>
<div class="form-item">
  <div class="panel panel-default">
    <table class="table table-bordered table-hover ">
      <thead>
        <tr>
          <th  colspan="2">统计口径</th>
        </tr>
      </thead>
      <tbody>
      <#if examParameterMap??>
      		<tr>
	          <th>${examParameterMap["subjectRange"].parameterAsName}</th>
	          <td>
	          	<#assign subjectRangeValues=[{"value":"1","name":"统计不缺考"},{"value":"2","name":"统计缺考"},{"value":"3","name":"统计分数大于0"},{"value":"4","name":"统计不缺考分数大于0"}]>
	          	<select class="form-control examparameter" parameterAsName="${examParameterMap["subjectRange"].parameterAsName}" parameterName="${examParameterMap["subjectRange"].parameterName}" parameterId="${examParameterMap["subjectRange"].id}">
	          		<#list subjectRangeValues as tmpValue>
	          			<#if examParameterMap["subjectRange"].parameterValue == tmpValue.value>
	          				<option value="${tmpValue.value}" selected>${tmpValue.name}</option>
	          			<#else>
	          			    <option value="${tmpValue.value}">${tmpValue.name}</option>
	          			</#if>
	          		</#list>
	          	</select>
	          </td>
	        </tr>
	        
	        <tr>
	          <th>${examParameterMap["totalSubjectRange"].parameterAsName}</th>
	          <td>
	          	<#assign subjectRangeValues=[{"value":"0","name":"包含缺考"},{"value":"1","name":"至少一科不缺考"},{"value":"2","name":"所有科目都不缺考"},{"value":"3","name":"至少一科不缺考且分数大于0"},{"value":"4","name":"所有科目不缺考且分数大于0"}]>
	          	<select class="form-control examparameter" parameterAsName="${examParameterMap["totalSubjectRange"].parameterAsName}" parameterName="${examParameterMap["totalSubjectRange"].parameterName}" parameterId="${examParameterMap["totalSubjectRange"].id}">
	          		<#list subjectRangeValues as tmpValue>
	          			<#if examParameterMap["totalSubjectRange"].parameterValue == tmpValue.value>
	          				<option value="${tmpValue.value}" selected>${tmpValue.name}</option>
	          			<#else>
	          			    <option value="${tmpValue.value}">${tmpValue.name}</option>
	          			</#if>
	          		</#list>
	          	</select>
	          </td>
	        </tr>
	        
	        <tr>
	          <td colspan="2" style="text-align:right">
	        	<button id="saveParameterOk" type="button" class="btn btn-primary">保存</button>
	          </td>
	        </tr>
      </#if>
      </tbody>
    </table>
    <hr/>
    <br/>
    <div id="combinationSubjects"></div>
  </div>
</div>
-->