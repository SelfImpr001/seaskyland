<!-- 索引栏-->
<div id="breadcrumbs" class="breadcrumbs">
    <ul class="breadcrumb">
			<li><i class="icon-home home-icon"></i><a href="${request.contextPath}">首页</a></li>
			<li><a href="#">系统基础数据</a></li>
			<li class="active">
			     <#if (educationType==3)>
	              	    区县教育局
	             </#if>
				 <#if (educationType==2)>
	              	    地市教育局
	             </#if>
	             <#if (educationType==1)>
	               	    省教育局
	             </#if>
			</li>
	</ul>
</div>
   
<!-- 表单内容-->
<div class="page-content">
    <!-- 表单头-->
   	<div class="page-header">
   		<h1>
              <i class="icon-hand-right"></i> 
                 <#if (educationType==3)>
	              	    区县教育局
	             </#if>
                 <#if (educationType==2)>
	              	    地市教育局
	             </#if>
	             <#if (educationType==1)>
	               	    省教育局
	             </#if>
              <button id="newAddBtn" data-toggle="modal" class="btn btn-primary pull-right" type="button">新增</button>
        </h1>
    </div>
	<!-- 表单体-->
	<div class="row">
		<div id="form-01" class="col-xs-12">
		    <table class="table table-bordered table-striped table-hover">
	        <thead>
	        	<tr>
					<td colspan="9" class="table-header">
					
						<span class="date-label">教育局名称:</span>
						<div class="input-group pull-left">
						<input type="text" class="form-control search-query"
								placeholder="输入教育局名称" id="educationName" value="${(page.parameter.educationName)!""}"> <span class="input-group-btn">
								<button type="button" class="btn btn-purple btn-sm" id="search">
									查询 <i class="icon-search icon-on-right bigger-110"></i>
								</button>
							</span>
						</div>
					</td>
				</tr>
	            <tr>
	               <th>代码</th>
	              <th>名称</th>
	              <#if (educationType>2)>
	               <th>所属地市教育局</th>
	              </#if>
	              <#if (educationType>1)>
	               <th>所属省市教育局</th>
	              </#if>
	              <th>操作</th>
	            </tr>
	        </thead>
	        <tbody>
	        <#if page.list??>
	        	<#list page.list as education>
	        		 <tr>
			              <td>${education.code}</td>
			              <td>${education.name}</td>
			              <#if (educationType>2)>
			               <td>${education.parent.name}</td>
			              </#if>
			              <#if (educationType>1)>
			              	<#if educationType==2>
			              		<td>${education.parent.name}</td>
			              	</#if>
			              	<#if educationType==3>
			              		<td>${education.parent.parent.name}</td>
			              	</#if>
			               
			              </#if>
			              <td class="opera">
			              	<a href="#" title="编辑" objectId="${education.id}" educationType="${educationType}" class="update"><i class="icon-edit text-success"></i></a><span>|</span>
			              	<a href="#" title="删除" objectId="${education.id}" educationType="${educationType}" class="delete"><i class="icon-trash text-primary"></i></a>
			              </td>
			            </tr>
	        	</#list>
	        </#if>
	        </tbody>
	    </table>
	    <div style="text-align: center">
				<input type="hidden" id="pageNum" value="${page.curpage}"> <input
					type="hidden" id="pageCount" value="${page.totalpage}">
				<div id="pager" style="margin-top: 20px; text-align: center"></div>
			</div>
	    <input type="hidden" id="educationType" value="${educationType}"/>
		</div>
	</div>
</div>