<!-- 索引栏-->
<div id="breadcrumbs" class="breadcrumbs">
    <ul class="breadcrumb">
			<li><i class="icon-home home-icon"></i><a href="${request.contextPath}">首页</a></li>
			<li><a href="#">系统基础数据</a></li>
			<li class="active">学校管理</li>
	</ul>
</div>
   
<!-- 表单内容-->
<div class="page-content">
    <!-- 表单头-->
   	<div class="page-header">
   		<h1>
              <i class="icon-hand-right"></i> 学校管理
              <button  id="importSchool" data-toggle="modal" class="btn btn-primary pull-right" type="button" style="margin-left:10px">导入</button>
              <button  id="newAddBtn" data-toggle="modal" class="btn btn-primary pull-right" type="button">新增</button>
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
							</span>
						</div>
						<span class="date-label">学校名称:</span>
						<div class="input-group pull-left">
						<input type="text" class="form-control search-query"
								placeholder="输入学校名称" id="schoolName" value="${(page.parameter.schoolName)!""}"> <span class="input-group-btn">
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
	              <th>类型</th>
	              <th>所属教育局</th>
	              <th>操作</th>                                                                                   
	            </tr>
	        </thead>
	        <tbody>
	        <tbody>
	        <#if page.list??>
	        	<#list page.list as school>
	        		 <tr>
			              <td>${school.code}</td>
			              <td>${school.name}</td>
			              <td>${school.schoolType!""}</td>
			              <td>${school.education.parent.parent.name+">>"+school.education.parent.name+">>"+school.education.name}</td>
			              <td class="opera">
			              	<a href="#" title="编辑" objectId="${school.id}" class="update"><i class="icon-edit text-success"></i></a><span class="separator">|</span>
			              	<a href="#" title="删除" objectId="${school.id}" class="delete"><i class="icon-trash text-primary"></i></a>
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
	    <input type="hidden" id="title"  value="title" />
		</div>
	</div>
</div>