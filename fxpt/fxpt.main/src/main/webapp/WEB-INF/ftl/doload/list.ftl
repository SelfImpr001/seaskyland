<#import "../commons/breadcrumbs.ftl" as breadcrumbs>
<#import "../commons/grid.ftl" as userGrid>
<#import "../commons/contentTop.ftl" as userContentTop>
<div class="page-content">
<@breadcrumbs.breadcrumbs currentPath="${title!'用户管理'}">
<!-- 表单内容  {"text":"批量授权","trigger":"batchGrant"} 删除批量授权功能-->
  
 
  <@userContentTop.contentTop title="${title!'数据下载列表'}" buttons=buttons/>
  
  <#assign headers=["序号","文件名称","操作"]>

  <#assign rows=[]>
  
  <#assign one="">


 <#assign context=["过滤人员名单_文科","过滤人员名单_理科","小语种考生列表_文科",	"小语种考生列表_理科"]>
  
    <#assign buttons1=[{"type":"text","title":"下载","trigger":"dowload","class":"icon-download-alt text-success"}]>
					
  <#assign optBtn=[]>
  <#assign isAdmin={ }>
  
   <#list context as user>
         
      <#assign cells=[user_index+1,user]>
      <#assign rows=rows+[cells]>
       <#assign optBtn=optBtn+[{"pk":user_index+1,"buttons":buttons1}]>
    </#list>
 
  <@userGrid.grid  headers=headers rows=rows hasNested=true
    buttons=optBtn pager=pager showPager=true hasFirstCheckbox=false showSeq=false idPreFix="dowload" isAdmin = isAdmin>
      <div class="query-form clearfix down" style="overflow: visible;">
        <div class="form-side col-xs-10 col-md-10 col-sm-10">
          <form class="form-horizontal " role="form">
			<div class="form-group  ">
			
			<label class="col-md-1 col-sm-1 control-label">考试：</label>
			  <div class="col-md-10 col-sm-10">
			   <select class="form-control selectpicker myData" id="examStatus"
					name="examStatus">
					 <#if exams??>
					<option value="" orgname="全部">全部</option>
					<#list exams as exam>
					<option value="${exam.id}" <#if exams?size==1>selected = "selelected"</#if>>${exam.name}</option>
					</#list>
					</#if>
				</select>
			  </div>
			 </div>
			 <div class="form-group  ">
			 
			  
			<label class="col-md-1 col-sm-1 control-label">市：</label>
			    <div class="col-md-2 col-sm-2">
			   <select class="form-control selectpicker myData" id="citys" retype="org" <#if citys?size==1> disabled</#if>
					name="examStatus">
					 <#if citys??>
					<option value="" orgname="全部">全部</option>
					<#list citys as city>
					<option value="${city.pk}"  orgname="${city.name}" <#if citys?size==1>selected = "selelected"</#if>>${city.name}</option>
					</#list>
					</#if>
				</select>
			  </div>
			   <label class="col-md-1 col-sm-1 control-label">区：</label> 
			    <div class="col-md-2 col-sm-2">
			  <select class="form-control selectpicker myData" id="countys" retype="org" <#if countys?size==1> disabled</#if>
					name="examStatus">
					 <#if countys??>
					<option value="" orgname="全部">全部</option>
					<#list countys as county>
					<option value="${county.pk}" orgname="${county.name}" <#if countys?size==1>selected = "selelected"</#if>>${county.name}</option>
					</#list>
					</#if>
				</select>
			  </div>
			  <label class="col-md-1 col-sm-1 control-label">学校：</label>
			     <div class="col-md-2 col-sm-2">
			    <select class="form-control selectpicker myData" id="schools"  retype="org" <#if schools?size==1> disabled</#if>
					name="examStatus">
					 <#if schools??>
					<option value="" orgname="全部">全部</option>
					<#list schools as school>
					<option value="${school.pk}" orgname="${school.name}" <#if schools?size==1>selected = "selelected"</#if>>${school.name}</option>
					</#list>
					</#if>
				</select>
			  </div>
			   <label class="col-md-1 col-sm-1 control-label">学校子类：</label> 
			     <div class="col-md-2 col-sm-2">
			  <select class="form-control selectpicker myData" id="clazzs" retype="org" <#if clazzs?size==1> disabled</#if>
					name="examStatus">
					 <#if clazzs??>
					<option value="" orgname="全部">全部</option>
					<#list clazzs as clazz>
					<option value="${clazz.pk}" orgname="${clazz.name}" <#if clazzs?size==1>selected = "selelected"</#if>>${clazz.name}</option>
					</#list>
					</#if>
				</select>
			  </div>
			   <!-- <label class="col-md-1 col-sm-1 control-label">年级：</label>
			   <div class="col-md-1 col-sm-1">
			  <select class="form-control selectpicker myData" id="examStatus"
					name="examStatus">
					<option value="">全部</option>
					<option value="0">未分析</option>
					<option value="1">分析成功</option>
					<option value="2">正在分析</option>
					<option value="3">分析失败</option>
					<option value="5">已发布</option>
					<option value="6">等待分析</option>
				</select>
			  </div>   -->
			</div> 
			           
          </form>
        </div>
        <div class="form-side col-xs-2 col-md-2 col-sm-2">          
          <a class="btn btn-success " herf="javascript:void(0);" title="查询">查询</a>
          <a class="btn btn-success " herf="javascript:void(0);" title="重置">清空</a>          
        </div>
      </div>  
  </@userGrid.grid>
</@breadcrumbs.breadcrumbs>
</div>