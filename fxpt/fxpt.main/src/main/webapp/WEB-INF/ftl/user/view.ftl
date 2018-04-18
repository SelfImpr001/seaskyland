<#import "../commons/pageHead.ftl" as pageHead> 
<#import "../commons/table.ftl" as table>
<#import "../etlLog/logList.ftl" as logList>

<div class="page-content">
	<@pageHead.pageHead title="用户基本信息" buttons=[{"id":"returnPage","name":"","text":"<<返回上级页面"}]/>
  	<#include "userInfo.ftl">
  <@pageHead.pageHead title="用户权限信息"/>
              <div class="form-group">
					<label class="col-sm-2 control-label">所属角色</label>
					<div class="col-sm-10"><p class="form-control-static">${rolenames}</p></div>			
			   </div>	
			   <div class="form-group">
					<label class="col-sm-12 control-label">考试列表</label>			
			   </div>
			   <div class="form-group">
			        <label class="col-sm-2 control-label"></label>
					<div class="col-sm-10">
					<@table.table tableId="roleAndexamDetail" head=["考试日期","考试名称","考试简称","组织考试机构","考试创建人","信息状态"]>
                         <#include "examDetail.ftl">
                      </@table.table>
                   </div>			
			   </div>
   
  <input type="hidden" id="userPk" value="${user.pk}" />
</div>