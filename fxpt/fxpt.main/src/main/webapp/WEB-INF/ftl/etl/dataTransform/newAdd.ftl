<div class="modal-body" >
	<form id="dataTransForm" name="dataTransForm" method="post" action="">
	<div class="form-group">
               		 <div class="form-group">
							<label class="col-sm-3 control-label" for="m-password1">所属数据项</label>
							<div class="col-sm-9">
									<select class="form-control selectpicker myData" id="dataCategoryId" name="dataCategoryId" disabled="false"> 
										<#if dataCategorys??> 
											<#list dataCategorys as newdataCategory> 
												<#if dataCategoryId == newdataCategory.id>
													<option value="${newdataCategory.id}" selected>${newdataCategory.name}</option>
												<#else>
													<option value="${newdataCategory.id}">${newdataCategory.name}</option>
												</#if>
											</#list> 
										</#if>
									</select>
							</div>
					</div>     
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="m-password1">名称</label>
                         <div class="col-sm-9">
                          <input  type="text" class="form-control myData" id="name" name="name">
                          <p class="error-tip"></p>
                        </div>
                    </div>   
                    
                    <div class="form-group">
						<label class="col-sm-3 control-label" for="m-password1">验证类型</label>
						<div class="col-sm-9  radio-content">
							<div class="download-check">
								<input type="radio" class="ace myData" name="type" id="type"
									value="1" checked="true"> <span class="lbl"></span>验证
							</div>
							<div class="download-check">
								<input type="radio" class="ace myData" name="type" id="type"
									value="2"> <span class="lbl"></span>转换
							</div>
						</div>
					</div>
                    
			         <div class="form-group">
						<label class="col-sm-3 control-label" for="m-password1">是否有效</label>
						<div class="col-sm-9  radio-content">
							<div class="download-check">
								<input type="radio" class="ace myData" name="valid" id="valid"
									value="true" checked="true"> <span class="lbl"></span>有效
							</div>
							<div class="download-check">
								<input type="radio" class="ace myData" name="valid" id="valid"
									value="false"> <span class="lbl"></span>无效
							</div>
						</div>
					</div>
                    
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="m-password1">验证/转换表达式</label>
                        <div class="col-sm-9" id="new">
                        <textarea id="code" name="code" style="height: 300px; width: 100%;"  rows="8" ></textarea>
                        </div>
                    </div>
    </form>
</div>
