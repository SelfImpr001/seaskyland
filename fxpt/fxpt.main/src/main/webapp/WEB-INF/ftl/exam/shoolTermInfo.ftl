<#macro shoolTermInfo examOfSchoolTerm>
<div class="timeline-container">
  <div class="timeline-label"> <span class="label timeLineFlag-1 label-lg "><b>${(examOfSchoolTerm.schoolYear-1)?string("0")+"-"+(examOfSchoolTerm.schoolYear)?string("0")}学年${(examOfSchoolTerm.schoolTerm==1)?string("上","下")}学期</b></span>
    <div class="pull-right" >
    </div>
  </div>
  <!--考试列表-->
  <#list examOfSchoolTerm.exams as exam>
  	 <div class="timeline-items odd">
	    <div class="timeline-item clearfix">
	      <div class="timeline-info"> <i class="timeline-indicator   grade-1 no-hover">${exam.grade.name}</i> </div>
	      <div class="widget-box transparent">
	        <div class="widget-header widget-header-small">
	          <h5 class="smaller"><span class="grey">${exam.name}</span> </h5>
	          <span class="widget-toolbar">
	          <div class="download-check" >
	          <!--
	            <input name="form-field-checkbox" type="checkbox" class="ace">
	           -->
	            <span class="lbl"></span> </div>
	          <a href="javascript:void(0);" data-action="collapse"><i class="icon-chevron-up"></i> </a> <!--<a href="javascript:void(0);" data-action="download"> <i class="icon-download-alt"></i> </a>--> </span> </div>
	        <div class="widget-body">
	          <div class="widget-main"> <!--评论-->
	            <div class="space-6"></div>
	            <div class="widget-toolbox clearfix">
	            <#if isStudent>
	              <div class="pull-left"> <i class="icon-hand-right grey bigger-125"></i> <a href="${request.contextPath}/personalReport/exec/${exam.id}/${user.getUserName()}" target="_blank" class="bigger-110">查看完整报告...</a> </div>
	            <#else>
	              <div class="pull-left"> <i class="icon-hand-right grey bigger-125"></i> <a href="javascript:void(0);" examId=${exam.id}  class="bigger-110 viewReport">查看完整报告...</a> </div>
	            </#if>
	            </div>
	          </div>
	        </div>
	      </div>
	    </div>
	  </div>
  </#list>
</div>
</#macro>