<div id="breadcrumbs" class="breadcrumbs">
    <ul class="breadcrumb">
		<li> <i class="icon-home home-icon"></i> <a href="${request.contextPath}">主页</a> </li>
        <li> <a href="javascript:window.location='./'+$('#uuId').val();">考试列表</a> </li>
        <li class="active">试题信息</li>
    </ul>
</div>

<div class="page-content">
    <div class="page-header">
        <h1>
            <i class="icon-hand-right"></i> 试题信息
            <div class="pull-right">
                <!--<a class="small" href="../exam/home"><i class="icon-share"></i>  返回试题列表 </a>-->
                <button id="importItem"  data-toggle="modal" class="btn btn-primary pull-right" type="button" style="margin-left:10px;">导入</button>
                <button id="deleteItem" data-toggle="modal" class="btn btn-primary pull-right" type="button">删除</button>            
            </div>
        </h1>
        <div class="form-item">
	          <div class="input-group">	
          			<select class="selectpicker myData" id="testPaperId" data-width="200px">
                      <#if testPapers??> 
                       	<#list testPapers as testPaper>
		              		<option value="${testPaper.id}">${testPaper.name}</option>
		              	</#list>
              		   </#if>
                    </select> 
	          </div>
	      </div>
    </div>
    <div class="row">
     
    </div>
</div>




<#--
<div class="heading-main">
        <div class="right-area">  
        <a id="importItem" href="#" title=""> <span class="glyphicon glyphicon-plus">导入小题信息</span> </a> 
        <a id="deleteItem" href="#" title=""> <span class="glyphicon glyphicon-plus">删除小题信息</span> </a>
        </div>
      </div>
      <div class="form-item">
        <div id="contentPanel" class="panel panel-default">
          <div class="panel-body">
            <div class="btn-box">
              <div class="row">
                <div class="col-sm-3">
                  <div class="input-group">
                  <label style="display:table-cell">试卷:</label>
                   <select class="form-control" id="testPaperId">
		              	<#list testPapers as testPaper>
		              		<option value="${testPaper.id}">${testPaper.name}</option>
		              	</#list>
  				  	</select>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div id="itemTable"></div>
          
          </div>
      </div>
      -->