<#assign name="系统">
<#if showType=="menu">
	<#assign name="菜单">
<#elseif showType=="data">
	<#assign name="数据权限">
<#elseif showType=="module">
	<#assign name="报告">
</#if>
<#-- 不同类型 url名称叫法也不同 -->
<#if type=="data" || (parentResource.type)! == "data">
	<#assign urlName="${name}值">
<#else>
	<#assign urlName="${name}URI">
</#if>
  <form action="res/addMenu"  method="post" class="form-horizontal" id="${showType}_form" enctype="multipart/form-data">
    <div class="form-group" style="width:500px">
      <label for="m-password1" class="col-sm-3 control-label">上级</label> <input type="hidden" class="myData"
        name="parent.pk" value="${(parentResource.pk)!}" /> <input type="hidden" class="myData" name="parent.uuid"
        value="${(parentResource.uuid)!}" />
      <div class="col-sm-9">
        <p class="form-control-static">${(parentResource.name)!}</p>
      </div>
    </div>
    
    <div class="form-group"  style="width:500px">
      <label for="order1" class="col-sm-3 control-label">是否有效</label>
      <div class="col-sm-9 radio-content">
        <div class="download-check">
          <input id="yes" checked  type="radio" class="ace myData" name="available" value="true" /> 
          <label for="yes" class="lbl"></label>有效
        </div>&nbsp;
        <div class="download-check">
          <input  id="no"  type="radio" class="ace myData" name="available" minlength="2" value="false"  /> 
          <label for="no" class="lbl"></label>无效
        </div>&nbsp;
      </div>
    </div>
    
    <div class="form-group" style="display:none;width:500px" >
      <label for="order1" class="col-sm-3 control-label">类型</label>
      <div class="col-sm-9 radio-content">
        <div class="download-check">
          <input id="menu"<#if type! == "menu"> checked<#else>checked</#if> type="radio" class="ace myData" name="type" value="menu" /> 
          <label for="menu" class="lbl">菜单</label>
        </div>&nbsp;
        <div class="download-check">
          <input  id="data" <#if type! == 'data'> checked</#if> type="radio" class="ace myData" name="type" required minlength="2" value="data"  /> 
          <label for="data" class="lbl">数据权限</label>
        </div>&nbsp;
        <div class="download-check">
          <input  id="module" <#if type! == 'module'> checked</#if> type="radio" class="ace myData" name="type" required minlength="2" value="module"  /> 
          <label for="module" class="lbl">报告</label>
        </div>
      </div>
    </div>
    
    
    <div class="form-group"  style="width:500px">
      <label for="int1" class="col-sm-3 control-label">${name}名称</label>
      <div class="col-sm-9">
        <input type="text" class="form-control myData" id="int1" name="name" >
        <p class="error-tip"></p>
      </div>
    </div>

        
    <div class="form-group"  style="width:500px">
      <label for="int1" class="col-sm-3 control-label">${urlName}</label>
      <div class="col-sm-9">
        <input type="text" class="form-control myData" id="int1" name="url" >
        <p class="error-tip"></p>
      </div>
    </div>
    
    <#if type! != "data">
    <div class="form-group"   style="width:500px">
      <label for="fileUpload" class="col-sm-3 control-label">${name}icon</label>
      <div class="col-sm-9">
        <input type="file" id="fileUpload" name="fileUpload" >
        <input type="hidden" id="icon" name="icon" class="form-control myData" />
        <div style="width:200px; height:50px;overflow:hidden; display:none;">
          <img id="uploadImg" name="${request.contextPath}/static" src="" />
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           <span class="glyphicon glyphicon-remove" id="removeIconAdd" title="删除图片" style="width:13px;"></span>
        </div>
        <p>文件仅支持JPEG,TIFF,RAW,BMP,PNG,GIF,JPG格式,请选择正确格式文件上传</p>
        <p class="error-tip"></p>
      </div>
    </div>

    <div class="form-group"  style="width:500px">
      <label for="int1" class="col-sm-3 control-label">${name}关联文件</label>
      <div class="col-sm-9">
        <input type="text" class="form-control myData" id="int1" name="entry" >
        <p class="error-tip"></p>
      </div>
    </div>
    </#if>
    
    <div class="form-group"  style="width:500px">
      <label for="int11" class="col-sm-3 control-label">${name}排列顺序</label>
      <div class="col-sm-9">
        <input type="text" class="form-control myData" id="int11" name="reorder" >
        <p class="error-tip"></p>
      </div>
    </div>
    
	<div class="form-group"  style="width:500px">
	  <label for="desc" class="col-sm-3 control-label">描述</label>
	  <div class="col-sm-9">
	    <textarea id="desc" class="form-control myData" style="width:370px;" rows="3" name="remarks"></textarea>
	  </div>
	</div>
	<#if type=="module">
	<div class="form-group"  style="width:500px">
		<label class="col-sm-3 control-label">适用年级</label>
		<div class="col-sm-9">
			<div class="input-group" style="cursor: pointer">
			<label>
			<input name='checkAll' id='checkAll' type='checkbox' />全选/反选 
			</label>
			</br>
			<#if grades??> <#list grades as grade>
			<div class="download-check">
				<input type="checkbox" class="ace gardeIds myData" name="gardeIds"
					value="${grade.id}" required minlength="1"
					> <span class="lbl"></span>${grade.name}
			</div>
			</#list> </#if>
				<p class="error-tip" id="gardeIdsCount" style="display: inline-block; float: none"></p>
			</div>
			<p></p>
		</div>
	</div>
	<div class="form-group"  style="width:500px">
		<label class="col-sm-3 control-label">适用考试类型</label>
		<div class="col-sm-9">
			<div class="input-group" style="cursor: pointer">
			<label>
			<input name='checkAll1' id='checkAll1' type='checkbox' class=''/>全选/反选 
			</label>
			</br>
			<#if examTypes??> <#list examTypes as examType>
			<div class="download-check">
				<input type="checkbox" class="ace examTypeIds myData" name="examTypeIds"
					value="${examType.id}" required minlength="1"
					> <span class="lbl"></span>${examType.name}
			</div>
			</#list> </#if>
				<p class="error-tip" id="examTypeIdsCount" style="display: inline-block; float: none"></p>
			</div>
			<p></p>
		</div>
	</div>
	</#if>
  </form>

