<div class="modal-body">
  <form id="form1" name="form1" method="post" action="">
    	<#--名称-->
     	<div class="form-group">
              <label class="col-sm-3 control-label">名称:</label>
              <div class="col-sm-9">
                <input type="text" class="form-control myData" name="name" id="m-password1" placeholder="名称">
                <p class="error-tip"></p>
              </div>
            </div>
    	<#--/名称-->
		    <#--类型-->
		    <div class="form-group">
              <label class="col-sm-3 control-label">类型:</label>
              <div class="col-sm-9  radio-content">
              	 <div class="download-check">
              	 	<input name="paperType" type="radio" value="0" class="ace myData" checked>
                    <span class="lbl"> </span>所有
                </div>
              	 <div class="download-check">
              	 	<input  name="paperType" type="radio" value="1" class="ace myData"> 
                    <span class="lbl"></span>文科 
                </div>
              	 <div class="download-check">
              	 	<input name="paperType" type="radio" value="2" class="ace myData"> 
                    <span class="lbl">  </span>理科
                </div>
              </div>
            </div>
          <#--/类型-->
               
       <#--对应试卷-->
        <div class="form-group">
              <label class="col-sm-3 control-label">对应试卷:</label>
              <div class="col-sm-9  radio-content">
                <#if testPapers??> 
               
                <#list testPapers as testPaper>
                	<div class="download-check">
						<input type="checkbox" class="ace testPaper" name="testPaper" value="${testPaper.id}" required minlength="2">
	                    <span class="lbl"></span>${testPaper.name}
	                </div>
                </#list>
                
                </#if>
                <p class="error-tip" id="testPaperCount" style="display:inline-block; float:none"></p>
              </div>
         </div>
       <#--/对应试卷-->    
    
  </form>
</div>
