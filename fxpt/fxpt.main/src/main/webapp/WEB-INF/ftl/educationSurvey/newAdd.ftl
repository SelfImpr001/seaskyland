<div class="modal fade big-modal" id="big-modal" data-backdrop="static">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header" id="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title">新增监测项目 </h4>
      </div>
      <div class="modal-body">
	    <form id="userEditForm" role="form" action="" class="form-horizontal ">
	    <input type="hidden" id="mpk" value="${m.pk!''}">
		  <div class="form-group" >
			<label class="col-sm-2 control-label">日期</label>
			
			<div class="col-md-4 col-sm-4">
			  	<div class="">
				  	<#if m.monitorDate??>
				  		<input class="form-control date-picker myData" id="monitorDate" value=" ${(m.monitorDate!'')?string('YYYY-MM-dd')} " name="monitorDate" type="text" >
				  	<#else>
				  		<input class="form-control date-picker myData" id="monitorDate" value=" ${(m.monitorDate!'')} " name="monitorDate" type="text" >
				  	</#if>
				 </div>
			</div>
		
			<label class="col-sm-2 control-label">学年</label>
			<div class="col-sm-4">
				<select class="form-control selectpicker myData" id="academicYear" name="academicYear">
					<option <#if (m.academicYear!'')=='2016-2017学年'>selected="selected"</#if> value="2016-2017学年">2016-2017学年</option>
					<option <#if (m.academicYear!'')=='2015-2016学年'>selected="selected"</#if> value="2015-2016学年">2015-2016学年</option>
					<option <#if (m.academicYear!'')=='2014-2013学年'>selected="selected"</#if> value="2014-2013学年">2014-2013学年</option>
				</select>
			</div>	
		  </div>
		  <div class="form-group" >
			<label class="col-sm-2 control-label">学期</label>
			<div class="col-sm-4">
              <div class="download-check">
                <input type="radio" <#if (m.semester!'')=='上学期'>checked="checked"</#if> class="ace myData" name="semester"   value="上学期">
                <span class="lbl"></span>上学期
              </div>  
              <div class="download-check">
                <input type="radio" <#if (m.semester!'')=='下学期'>checked="checked"</#if> class="ace myData" name="semester"   value="下学期">
                <span class="lbl"></span>下学期
              </div>
			</div>

			<label class="col-sm-2 control-label">分析类型</label>
			<div class="col-sm-4">
				<select class="form-control selectpicker myData" id="analysisType" name="analysisType">
					<option <#if (m.analysisType!'')=='学业监测'>selected="selected"</#if> value="学业监测">学业监测</option>
					<option <#if (m.analysisType!'')=='综合素质监测'>selected="selected"</#if> value="综合素质监测">综合素质监测</option>
					<option <#if (m.analysisType!'')=='学生成长环境监测'>selected="selected"</#if> value="学生成长环境监测">学生成长环境监测</option>
				</select>
			</div>	
		  </div>		  
		  
		 <div class="form-group" >
			<label class="col-sm-2 control-label">数据关联</label>
			<div class="col-sm-4">
               <select class="form-control selectpicker myData" id="questionName" name="questionName">
					<option <#if (m.questionName!'')=='2015-2016学年下学期8年级学业监测问卷调查'>selected="selected"</#if> value="2015-2016学年下学期8年级学业监测问卷调查">2015-2016学年下学期8年级学业监测问卷调查</option>
					<option <#if (m.questionName!'')=='2015-2016学年下学期8年级学业监测问卷调查'>selected="selected"</#if> value="2015-2016学年下学期8年级学生综合素质问卷调查">2015-2016学年下学期8年级学生综合素质问卷调查</option>
					<option <#if (m.questionName!'')=='2015-2016学年下学期8年级学业监测问卷调查'>selected="selected"</#if> value="2015-2016学年下学期8年级学生成长环境监测问卷调查">2015-2016学年下学期8年级学生成长环境监测问卷调查</option>
				</select>
			</div>
		 
			<label class="col-sm-2 control-label">年级</label>
			<div class="col-sm-4">
               <select class="form-control selectpicker myData" id="grade" name="grade">
					<option <#if (m.grade!'')=='一年级'>selected="selected"</#if> value="一年级">一年级</option>
					<option <#if (m.grade!'')=='二年级'>selected="selected"</#if> value="二年级">二年级</option>
					<option <#if (m.grade!'')=='三年级'>selected="selected"</#if> value="三年级">三年级</option>
					<option <#if (m.grade!'')=='四年级'>selected="selected"</#if> value="四年级">四年级</option>
					<option <#if (m.grade!'')=='五年级'>selected="selected"</#if> value="五年级">五年级</option>
					<option <#if (m.grade!'')=='六年级'>selected="selected"</#if> value="六年级">六年级</option>
					<option <#if (m.grade!'')=='初一'>selected="selected"</#if> value="初一">初一</option>
					<option <#if (m.grade!'')=='初二'>selected="selected"</#if> value="初二">初二</option>
					<option <#if (m.grade!'')=='初三'>selected="selected"</#if> value="初三">初三</option>
					<option <#if (m.grade!'')=='高一'>selected="selected"</#if> value="高一">高一</option>
					<option <#if (m.grade!'')=='高二'>selected="selected"</#if> value="高二">高二</option>
					<option <#if (m.grade!'')=='高三'>selected="selected"</#if> value="高三">高三</option>
				</select>
			</div>
		  </div>
		  
		  
		  <div class="form-group" >
			<label class="col-sm-2 control-label">组织考试机构</label>
			<div class="col-sm-4">
                <input type="text" class="form-control myData" name="institutions" id="institutions" value="${m.institutions!''}">
			</div>

			<label class="col-sm-2 control-label">创建人</label>
			<div class="col-sm-4">
				<input type="text" class="form-control myData" name="createUser" id="createUser" value="${m.createUser!''}" disabled="disabled">
			</div>	
		  </div>			
          <div class="modal-footer">
            <button type="button" class="btn btn-primary">保存</button>
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
          </div>
		</form> 
	  </div> 
    </div>
  </div>
</div>
