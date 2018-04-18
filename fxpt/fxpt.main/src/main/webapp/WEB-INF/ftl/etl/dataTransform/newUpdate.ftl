<div class="modal-body">
	<form id="dataFieldform" name="dataFieldform" method="post" action="">
	<div class="form-group">

                     <div class="form-group">
                        <label class="col-sm-3 control-label" for="m-password1">名称</label>
                         <div class="col-sm-9">
                          <input value="${dataTransform.name}" type="text" class="form-control myData" id="name" name="name">
                          <p class="error-tip"></p>
                        </div>
                     </div>   
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="m-password1">验证类型</label>
                        <div class="col-sm-9 radio-content">
				          <div class="download-check">
								<#if dataTransform.type=1>
									<input type="radio" class="ace myData" name="type" id="type"
										value="1" checked="true"> <span class="lbl"></span>验证
									<input type="radio" class="ace myData" name="type" id="type"
										value="2"> <span class="lbl"></span>转换
								<#else>
									<input type="radio" class="ace myData" name="type" id="type"
										value="1" > <span class="lbl"></span>验证
									<input type="radio" class="ace myData" name="type" id="type"
										value="2" checked="true"> <span class="lbl"></span>转换
								</#if>
							</div>      	
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="m-password1">是否有效</label>
                           <div class="col-sm-9  radio-content">
                           <div class="download-check">
								<#if dataTransform.valid=true>
									<input type="radio" class="ace myData" name="valid" id="valid"
										value="true" checked="true"> <span class="lbl"></span>有效
									<input type="radio" class="ace myData" name="valid" id="valid"
										value="false"> <span class="lbl"></span>无效
								<#else>
									<input type="radio" class="ace myData" name="valid" id="valid"
										value="true" > <span class="lbl"></span>有效
									<input type="radio" class="ace myData" name="valid" id="valid"
										value="false" checked="true"> <span class="lbl"></span>无效
								</#if>
							</div>
                         </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="m-password1">验证/转换表达式</label>
                        <div class="col-sm-9">
                        <textarea id="code" name="code" style="height: 300px; width: 100%;"  rows="8" >${dataTransform.content}</textarea>
                        </div>
                    </div>
    			<input type="hidden" class="myData" name="dataCategoryId" id="dataCategoryId" value="${dataTransform.dataCategory.id}">
    </form>
</div>
