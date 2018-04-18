<#assign name="系统">
<#if type == "menu">
	<#assign name="菜单">
<#elseif type=="data">
	<#assign name="数据权限">
<#elseif type=="module">
	<#assign name="报告">
</#if>
<#-- 类型   单选按钮的装备 -->
<#if resource.type! == "menu">
	<#assign menuChecked="checked">
<#elseif type=="data">
	<#assign dataChecked="checked">
<#elseif type=="app">
	<#assign appChecked="checked">
<#elseif type=="module">
	<#assign moduleChecked="checked">
</#if>

<#-- 不同类型 url名称叫法也不同 -->
<#if type=="data">
	<#assign urlName="${name}值">
<#else>
	<#assign urlName="${name}URI">
</#if>

<!-- 弹出的模态框 -->
<form action="res" method="put" id="${type}_form">
  	  
	<div class="form-group" style="display:none;">
      <label for="order1" class="col-sm-3 control-label">类型</label>
      <div class="col-sm-9 radio-content">
        <div class="download-check">
          <input id="menu"<#if resource.type! == "menu"> checked</#if> type="radio" class="ace myData" name="type" value="menu" /> 
          <label for="menu" class="lbl">菜单</label>
        </div>&nbsp;
        <div class="download-check">
          <input id="menu"<#if resource.type! == "app"> checked</#if> type="radio" class="ace myData" name="type" value="app" /> 
          <label for="menu" class="lbl">应用</label>
        </div>&nbsp;
        <div class="download-check">
          <input  id="data" <#if resource.type! == 'data'> checked</#if> type="radio" class="ace myData" name="type" required minlength="2" value="data"  /> 
          <label for="data" class="lbl">数据权限</label>
        </div>&nbsp;
        <div class="download-check">
          <input  id="module" <#if resource.type! == 'module'> checked</#if> type="radio" class="ace myData" name="type" required minlength="2" value="module"  /> 
          <label for="module" class="lbl">报告</label>
        </div>
      </div>
    </div>
    
    <div class="form-group">
      <label for="name" class="col-sm-3 control-label">${name}名称</label>
      <div class="col-sm-9">
        <input type="text" class="form-control myData" name="name" value="${resource.name!}" id="name"/> 
        <p class="error-tip"></p>
      </div>
    </div>
    
    <input type="HIDDEN" class="form-control myData" name="pk" id="resPk" value="${resource.pk!0}" />
    <input type="HIDDEN" class="form-control myData" name="type" id="resType" value="${resource.type!""}" />
    <input type="HIDDEN" class="form-control myData" name="uuid" value="${resource.uuid!}" /> 
    <input type="HIDDEN" class="form-control myData" name="parent.pk" value="${(resource.parent.pk)!}" /> 
	
	<#if resource.type! != "app">
    <div class="form-group">
      <label for="int1" class="col-sm-3 control-label">${urlName}</label>
      <div class="col-sm-9">
        <input type="text" class="form-control myData" value="${resource.url!}" id="int1" name="url" >
        <p class="error-tip"></p>
      </div>
    </div>
	<#if resource.type! != "data">
	<div class="form-group" >
      <label for="fileUpload" class="col-sm-3 control-label">${name}icon</label>
      <div class="col-sm-9">
        <input type="file" id="fileUpload" name="fileUpload" >
        <input type="hidden" id="icon" name="icon" value="${resource.icon!}" class="form-control myData" />
       
        <div style="width:200px; height:50px;overflow:hidden;<#if !resource.icon??>display:none;</#if>">
          <#assign imgpath="">
           <#if resource.icon??>
        	<#assign imgpath="${request.contextPath}/static" + resource.icon>
           </#if>
          <img id="uploadImg" name="${request.contextPath}/static" src="${imgpath!"#"}"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           <span class="glyphicon glyphicon-remove" id="removeIcon" title="删除图片" style="width:13px;"></span>
        </div>
         <p style="color:blue">图片文件仅支持.JPEG,.TIFF,.RAW,.BMP,.PNG,.GIF,.JPG这些格式，请选择正确的图片文件上传</p>
        <p class="error-tip"></p>
      </div>
    </div>
    
    <div class="form-group">
      <label for="int1" class="col-sm-3 control-label">${name}关联文件</label>
      <div class="col-sm-9">
        <input type="text" class="form-control myData" value="${resource.entry!}" id="int1" name="entry" >
        <p class="error-tip"></p>
      </div>
    </div>
    </#if>
    <div class="form-group">
      <label for="int1" class="col-sm-3 control-label">${name}顺序</label>
      <div class="col-sm-9">
        <input type="text" class="form-control myData" value="${resource.reorder!}" id="int1" name="reorder" >
        <p class="error-tip"></p>
      </div>
    </div>
    </#if>
    <#if resource.children?size != 0>
    <div class="form-group">
      <label for="name" class="col-sm-3 control-label">数据项
      </label>
      <div class="col-sm-9" style="height-line:10px;overflow:auto;height:55px;width:365px;">
      	<#assign index=1>
        <#list resource.children as childs> 
           <span style="margin-right:10px;">${index}. ${childs.name!}</span><br />
           <#assign index=index+1>
        </#list> 
      </div>
    </div>
    </#if>
    <div class="form-group">
      <label for="remarks" class="col-sm-3 control-label">描述</label>
      <div class="col-sm-9">
       <textarea id="remarks" class="form-control myData" style="width:350px;" rows="3" name="remarks">${resource.remarks!}</textarea>
       <p class="error-tip"></p>
       </div>
    </div>
    <#if type=="module">
	<div class="form-group">
		<label class="col-sm-3 control-label">适用年级</label>
		<div class="col-sm-9">
			<div class="input-group" style="cursor: pointer">
			<#if grades??> 
			<label>
			<input name='checkAll' id='checkAll' type='checkbox' />全选/反选 
			</label>
			</br>
			<#list grades as grade>
			<div class="download-check">
				<input type="checkbox" class="ace gardeIds myData" name="gardeIds"
					value="${grade.id}" required minlength="1"
					<#if resource.gradeids?? && resource.gradeids?split(",")?seq_contains(grade.id+"")>checked="checked"</#if>
					> <span class="lbl"></span>${grade.name}
			</div>
			</#list>
			</#if>
				<p class="error-tip" id="gardeIdsCount" style="display: inline-block; float: none"></p>
			</div>
			<p></p>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label">适用考试类型</label>
		<div class="col-sm-9">
			<div class="input-group" style="cursor: pointer">
			<#if examTypes??> 
			<label>
			<input name='checkAll1' id='checkAll1' type='checkbox' class=''/>全选/反选 
			</label>
			</br>
			<#list examTypes as examType>
			<div class="download-check">
				<input type="checkbox" class="ace examTypeIds myData" name="examTypeIds"
					value="${examType.id}" required minlength="1"
					<#if resource.examtypeids?? && resource.examtypeids?split(",")?seq_contains(examType.id+"")>checked="checked"</#if>
					> <span class="lbl"></span>${examType.name}
			</div>
			</#list> 
			</#if>
				<p class="error-tip" id="examTypeIdsCount" style="display: inline-block; float: none"></p>
			</div>
			<p></p>
		</div>
	</div>
	</#if>
</form>
