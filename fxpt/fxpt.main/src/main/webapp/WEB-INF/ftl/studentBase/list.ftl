<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<#import "../commons/grid.ftl" as studentBase>
<#import "../commons/contentTop.ftl" as userContentTop>
<div class="page-content">
<@breadcrumbs.breadcrumbs currentPath="${title!'学生库设置'}">
<!-- 表单内容-->
  <#assign
	buttons=[
	{"text":"考试学生导入","id":"sb_examStudent","class":"btn-primary","closeBtn":false}
	<#--,{"text":"文件导入模板下载","id":"sb_download","class":"btn-primary","closeBtn":false},
	     {"text":"文件导入","id":"sb_import","class":"btn-primary","closeBtn":false}-->]>
	         
	         
  <@userContentTop.contentTop title="${title!'学生库设置'}" buttons=buttons/>
  <#import "../commons/queryForm.ftl" as queryForm>
  <@queryForm.queryForm>
	<div class="form-group">
		<label class="col-md-1 col-sm-1 control-label">学号</label>
		<div class="col-md-3 col-sm-3">
			<input class="form-control myData" id="xh" name="xh" type="text" placeholder="学号" maxlength="50"> 			
		</div>

		<label class="col-md-1 col-sm-1 control-label">姓名</label>
		<div class="col-md-3 col-sm-3">
			<input type="text" class="form-control myData" id="qname" name="qname" placeholder="输入姓名" maxlength="50">
		</div>

		<label class="col-md-1 col-sm-1 control-label">学校名称</label>
		<div class="col-md-3 col-sm-3">
			<input type="text" class="form-control myData" id="qschoolName"	name="qschoolName" placeholder="输入学校名称" maxlength="50">
		</div>
	</div>
  </@queryForm.queryForm>

  <#assign headers=["省","市","区","学校代码","学校名称","学号","姓名","性别","学级","操作"]>
  <#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize,"totalRows":query.totalRows}>
  <#assign rows=[]>
  <#assign buttons=[
	{"type":"text","title":"编辑","trigger":"update","class":"icon-edit text-success"}
  ]>
  <#assign optBtn=[]>
  <#assign province="">
  <#assign city="">
  <#assign county="">
     <#if query.results????>
	        	<#list query.results as studentBase>
	        	  <#if studentBase.sex=1>
				     <#assign sexText="男">
				  <#elseif studentBase.sex=2>
				  	 <#assign sexText="女"> 
				  <#else>
				  	 <#assign sexText="">     
				  </#if>
				  <#if studentBase.school?? && studentBase.school.education?? >
				  	 <#assign county=studentBase.school.education.name>
					  <#if studentBase.school.education.parent??>
					  	  <#assign city=studentBase.school.education.parent.name>
					  	   <#if studentBase.school.education.parent.parent??>
					  	   		<#assign province=studentBase.school.education.parent.parent.name>
					  	   </#if>
					  </#if>
				  </#if>
	        	  <#assign cells=[province,city,county,studentBase.school.code,studentBase.school.name,studentBase.xh,studentBase.name,sexText,studentBase.grade]>
				  <#assign rows=rows+[cells]>
				  <#assign optBtn=optBtn+[{"pk":studentBase.id,"buttons":buttons}]>
	        	</#list>
	        </#if>
  <@studentBase.grid headers=headers rows=rows buttons=optBtn pager=pager showPager=true hasFirstCheckbox=false showSeq=true idPreFix="studentBase"/>
</@breadcrumbs.breadcrumbs>
</div>
