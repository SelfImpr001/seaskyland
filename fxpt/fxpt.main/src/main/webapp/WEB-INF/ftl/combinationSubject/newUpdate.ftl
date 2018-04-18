<div class="modal-body">
     <form id="form1" name="form1" method="post" action="">
     <#--名称-->
    	<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">名称:</label>
			<div class="col-sm-9">
				<input type="text" value="${combinationSubject.name}"
					class="form-control myData" name="name" id="m-password1"
					placeholder="名称">
				<p class="error-tip"></p>
			</div>
		</div>
     <#--/名称-->
     	
     <#--类型-->
     	<div class="form-group">
			<label class="col-sm-3 control-label" for="m-password1">类型:</label>
			<div class="col-sm-9  radio-content">
				 <div class="download-check">
              	 	<input name="paperType" type="radio" value="0" class="ace myData" ${(combinationSubject.paperType==0)?string("checked","")}>
                    <span class="lbl"> </span>所有
                </div>
				 <div class="download-check">
				 	<input name="paperType" type="radio" value="1" class="ace myData"	${(combinationSubject.paperType==1)?string("checked","")}>
                    <span class="lbl"> </span>理科
                </div>
				 <div class="download-check">
              	 	<input name="paperType" type="radio" value="2" class="ace myData" ${(combinationSubject.paperType==2)?string("checked","")}>
                    <span class="lbl"> </span>文科
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
						<input required minlength="2" type="checkbox" class="ace testPaper" name="testPaper" value="${testPaper.id}" ${((testPaperMarks[testPaper.id+""]!0) == 1)?string("checked","")}>
	                    <span class="lbl"></span>${testPaper.name}
	                </div>
                </#list>
                </#if>
                 <p class="error-tip" id="testPaperCount" style="display:inline-block; float:none"></p>
              </div>
         </div>
     <#--/对应试卷-->
     <input type="hidden" class="myData" name="id" value="${combinationSubject.id}">
    </form>
</div>