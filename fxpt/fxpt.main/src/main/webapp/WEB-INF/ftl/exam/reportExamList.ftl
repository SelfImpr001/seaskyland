
<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<div class="page-content">
<@breadcrumbs.breadcrumbs currentPath="${title!'报告列表'}">  
  <#import "../commons/contentTop.ftl" as top>
  <@top.contentTop title="${title!'报告列表'}" />
  
  <#import "../commons/queryForm.ftl" as queryForm>
  
  <@queryForm.queryForm  showAngle=false>
	<div class="form-group">
	  <label class="col-md-1 col-sm-1 control-label">考试日期</label>
	  <div class="col-md-2 col-sm-2">
	    <div class="input-group">
	      <input class="form-control date-picker myData" id="examDate" name="examDate" type="text" placeholder="${.now?string("yyyy-MM-dd")}"> 
	      <span  class="input-group-addon"><i class="icon-calendar bigger-110"></i> </span>
	    </div>
	  </div>
	  <label class="col-md-1 col-sm-1 control-label">学年</label>
	  <div class="col-md-2 col-sm-2">
	    <select class="form-control selectpicker myData" id="schoolYear" name="schoolYear">
	      <option value="">全部</option>
	      <#list schoolYears as sc>
	      <option value="${sc.schoolYear!""}">${sc.schoolYearName!""}</option>
	      </#list>
	    </select>
	  </div>
	  <label class="col-md-1 col-sm-1 control-label">学期</label>
	  <div class="col-md-2 col-sm-2">
	    <select class="form-control selectpicker myData" id="schoolTerm" name="schoolTerm">
	      <option value="">全部</option>
	      <option value="1">上学期</option>
	      <option value="2">下学期</option>
	    </select>
	  </div>
	  <label class="col-md-1 col-sm-1 control-label">考试类型</label>
	  <div class="col-md-2 col-sm-2">
	    <select class="form-control selectpicker myData" id="examTypeId" name="examTypeId">
	      <option value="">全部</option>
	      <#list examType as t>
	      <option value="${t.id}">${t.name}</option>
	      </#list>
	    </select>
	  </div>
	</div>
	
	
  </@queryForm.queryForm>
  <#import "../commons/grid.ftl" as examGrid>
  <#assign headers=["考试名称","学年","学期","考试类型","考试日期","操作"]>
  <#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize,"totalRows":query.totalRows}>
  <#assign rows=[]>
  <#assign optBtn=[]>
  <#if query.results??>
    <#list query.results as exam>    
	  <#assign examDate=exam.examDate?string("yyyy-MM-dd")>	      
	  <#assign cells=[(exam.name),(exam.schoolYear-1)?string("#")+"-"+(exam.schoolYear)?string("#")+"学年",(exam.schoolTerm==1)?string("上学期","下学期"),
	                  (exam.examType.name)!"", exam.examDate?string("yyyy-MM-dd")]>
	  <#assign rows=rows+[cells]>
	  <#if isStudent>
	    <#assign reportPath = exam.ownerCode+"_"+(exam.id)?c+"_"+exam.examDate?string("yyyyMMdd")>
	    <#assign buttons=[{"type":"text","class":"icon-search text-success","title":"查看报告","href":request.contextPath+"/personalReport/exec/"+exam.id+"/"+exam.ownerCode,"target":"_blank"}]>
	  <#else>
	    <#assign buttons=[{"type":"text","class":"icon-search text-success","title":"查看报告","href":request.contextPath+"/report/home/"+exam.id,"target":"_blank"}]>
	  </#if>
	  <#assign optBtn=optBtn+[{"pk":exam.id,"buttons":buttons}]>
    </#list>
  </#if>
  <@examGrid.grid headers=headers rows=rows buttons=optBtn pager=pager showPager=true hasFirstCheckbox=false  idPreFix="examReport"/>  
  <div class="stat_web">
  	<script src="http://s4.cnzz.com/stat.php?id=1261044651&web_id=1261044651" language="JavaScript"></script>  	
  </div>
<style>
  	.stat_web>a{display: none;}
</style>
</@breadcrumbs.breadcrumbs>
</div>  
