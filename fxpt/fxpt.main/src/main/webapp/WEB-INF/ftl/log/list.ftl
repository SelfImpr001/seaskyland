<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<#import "../commons/grid.ftl" as grid>
<#import "../commons/contentTop.ftl" as contentTop>
<div class="page-content">

<@breadcrumbs.breadcrumbs currentPath="${title!'日志查看'}">
<!-- 表单内容-->
<#assign buttons=[]>
<#if plan??>
  <#if plan.shutdowned=true>
  <#assign buttons=[]>
  </#if>
</#if>

	<#assign
	btns=[{"text":"下载","id":"downloadLog","class":"btn-primary","closeBtn":false}]>

  <@contentTop.contentTop title="${title!'日志查看'}" buttons=btns />
  <#import "../commons/queryForm.ftl" as queryForm>
  <@queryForm.queryForm showAngle=false>
	<div class="form-group">
		<div id="TextDiv" class="col-md-2 col-sm-2" style="display:none">
			<input type="text" class="form-control" id="optionName" name="optionName" placeholder="选项名字">
		</div>
		
		<div id="DateTimeDiv" style="display:block">
			<div class="col-md-2 col-sm-2">
				<input class="form-control date-picker myData"  data-date-format="YYYY-MM-dd HH:mm:ss" placeholder="开始时间" id="examStartData" name="startDate" type="text" >
			</div>
		
			<div class="col-md-2 col-sm-2">
				<input class="form-control date-picker myData"  data-date-format="YYYY-MM-dd HH:mm:ss" placeholder="结束时间" id="examEndData" name="endDate" type="text">
			</div>
		</div>
			
		<label class="col-md-1 col-sm-1 control-label">选项</label>
		<div class="col-md-2 col-sm-2">
			<select class="form-control selectpicker myData" id="optionValue" name="optionValue">
				 <option value="dateTime" trigger="dateTime">时间</option>
				 <option value="handlePro" trigger="handlePro">操作人</option>
				 <option value="handleOption" trigger="handleOption">操作项</option>
				 <option value="suferHandleOption" trigger="suferHandleOption">被操作对象</option>
		     </select>
		</div>
		
		
		<label class="col-md-1 col-sm-1 control-label">状态</label>
		<div class="col-md-2 col-sm-2">
			<select class="form-control selectpicker myData" id="status" name="status">
				 <option value="">全部</option>
				 <option value="success">成功</option>
				 <option value="error">失败</option>
		     </select>
		</div>
	</div>
  </@queryForm.queryForm>
  
  <#assign headers=["操作时间","操作人","所在IP","操作项","被操作对象","状态","详情"]>
  <#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize,"totalRows":query.totalRows}>
  <#assign rows=[]>
  <#assign optBtn=[]>
  <#if query.results??>
	<#list query.results as log >
		<#assign cells=[log.handleTime!'-',log.handlePro!'-',log.handleIP!'-',log.handleOption!'-',log.suferHandleOption!'-',log.status!'-']>
		<#assign rows=rows+[cells]>
		<#assign buttons=[ {"type":"text","title":"详情","trigger":"LogInfo","class":"icon-search text-success" } ]>
		<#assign optBtn=optBtn+[{"pk":log.id,"buttons":buttons}]>
	</#list>
  </#if>
  <@grid.grid headers=headers rows=rows buttons=optBtn pager=pager showPager=true hasFirstCheckbox=false checkedBoxName="batCheckBox"  showSeq=true idPreFix="log"/>
  </@breadcrumbs.breadcrumbs>
</div>