<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<div class="page-content">
<@breadcrumbs.breadcrumbs currentPath="${title!'学生信息核对'}">  
  <#import "../commons/contentTop.ftl" as top>
  <@top.contentTop title="${title!'学生信息核对'}" />
  
  <#import "../commons/queryForm.ftl" as queryForm>
  <@queryForm.queryForm  showAngle=false>
	<div class="form-group  ">
	
	  <label class="col-md-1 col-sm-1 control-label">考试日期</label>
	  <div class="col-md-2 col-sm-2 ">
	    <div class="input-group">
	      <input type="text" class="form-control date-picker" name="examData" placeholder="请输入考试日期">
	      <span class="input-group-addon"><i class="icon-calendar bigger-110"></i></span>
	    </div>
	  </div>
	  
	  <label class="col-md-1 col-sm-1 control-label">考试名称</label>
	  <div class="col-md-2 col-sm-2">
	    <input type="text" class="form-control" name="examName" placeholder="请输入考试名称" rel="tooltip" title="考试名称不能超过50个字符">
	  </div>
	  
	  <label class="col-md-1 col-sm-1 control-label">考试简称</label>
	  <div class="col-md-2 col-sm-2">
	    <input type="text" class="form-control" name="examSortName" placeholder="请输入考试简称" title="考试简称不能超过50个字符">
	  </div>
	  	  
	  <label class="col-md-1 col-sm-1 control-label">核对学生状态</label>
	  <div class="col-md-2 col-sm-2 ">
	    <select class="form-control selectpicker" name="status">
	      <option value="-1">全部</value>
	      <option value="0">未核对</value>
	      <option value="1">核对中</value>
	      <option value="9">核对完成</value>
	    </select>
	  </div>
	  
	</div> 	  
  </@queryForm.queryForm>
  <#import "../commons/grid.ftl" as checkinGrid>
  <#assign headers=["考试日期","考试名称","考试简称","分析状态","导入学生状态","核对学生状态","操作"]>
  <#assign pager={"curpage":query.curpage,"totalpage":query.totalpage,"pagesize":query.pagesize,"totalRows":query.totalRows}>
  <#assign rows=[]>
  <#assign optBtn=[]>
  <#if query.list??>
    <#list query.list as checkin>
      <#assign pk=checkin.pk?c>
      <#if checkin.status=0>
	    <#assign buttons=[{"type":"text","title":"核对学生历次信息","trigger":"uncheckList","class":"btn btn-xs btn-success mr-2 grant"}]>
	    <#assign status="未核对">
	  <#elseif checkin.status=9>
	    <#assign buttons=[{"type":"text","title":"核对列表","trigger":"checkedList","class":"btn btn-xs btn-success mr-2 grant"}]>
	    <#assign status="完成核对">
	  <#elseif checkin.status=1>
	    <#assign buttons=[{"type":"text","title":"核对列表","trigger":"checkingList","class":"btn btn-xs btn-success mr-2 grant"}]>
	    <#assign status="正在核对"><#--<div class='progress-warp' pk='"+pk+"'></div> 可以进行进度条设置-->
	  </#if>
	  <#assign x=checkin.exam.status/>
	  <#assign examStatusText=""/>
	  <#switch x>
	  <#case 0>
	    <#assign examStatusText="未发布"/>
	    <#break>
	  <#case 1>
	    <#assign examStatusText="分析成功"/>
	    <#break>
	  <#case 2>
	    <#assign examStatusText="正在分析"/>
	    <#break>
	  <#case 3>
	    <#assign examStatusText=" 分析失败 "/>
	    <#break>	
	  <#case 4>
	    <#assign examStatusText="--"/>
	    <#break>
	  <#case 5>
	    <#assign examStatusText="已发布"/>
	    <#break>
	  <#case 6>
	    <#assign examStatusText="等待分析"/>
	    <#break>
	  <#case 11>
	    <#assign examStatusText="正在生成报告"/>
	    <#break>		    	    	        	    
	  </#switch>     
	  <#assign examDate=checkin.exam.examDate?string("yyyy-MM-dd")>	      
	  <#assign cells=[examDate,checkin.exam.name!"",checkin.exam.sortName!"",examStatusText,checkin.exam.hasExamStudent?string("已导入","未导入"),status]>
	  <#assign rows=rows+[cells]>
	  
	  <#assign optBtn=optBtn+[{"pk":checkin.exam.id,"buttons":buttons}]>
    </#list>
  </#if>
  <@checkinGrid.grid headers=headers rows=rows buttons=optBtn pager=pager showPager=true hasFirstCheckbox=false  idPreFix="examCheckin"/>  
</@breadcrumbs.breadcrumbs>
</div>  