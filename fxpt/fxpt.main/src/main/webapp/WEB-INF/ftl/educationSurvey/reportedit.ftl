<div class="page-content">
  <!-- 表单头-->
	<div class="page-header">
		<h1>
	    	<i class="icon-hand-right"></i>结果上传
	    </h1>
	</div>
	<div class="row">	
	   <form id="reportEditForm" role="form" action="" class="form-horizontal " enctype="multipart/form-data">
		<div class="form-group">
		   <label class="col-sm-4 control-label" for="m-password1">考试名称:</label>
			<div class="col-sm-8">
				<select class="form-control selectpicker myData" id="monitorId" name="monitorName" style="width:400px;"> 
				<#if list??> 
				  <#list list as exam>
					<option value="${exam.id}">${exam.name}</option>
				  </#list> 
				</#if>
				</select>
			</div>
		</div>
		<div class="form-group" >
			<label class="col-sm-4 control-label">查看报告</label>
				<div class="col-sm-8">
				  <div id="files" class="uploadify" style="height: 30px; width: 120px; ">
					<object id="SWFUpload_1" type="application/x-shockwave-flash" data="/fxpt/static/resources/uploadify/uploadify.swf?preventswfcaching=1481608889864" width="120" height="30" class="swfupload" style="position: absolute; z-index: 1; ">
						<param name="wmode" value="transparent">
						<param name="movie" value="/fxpt/static/resources/uploadify/uploadify.swf?preventswfcaching=1481608889864">
						<param name="quality" value="high"><param name="menu" value="false">
						<param name="allowScriptAccess" value="always">
						<param name="flashvars" value="movieName=SWFUpload_1&amp;uploadURL=%2Febd%2FCollectinformation%2FimportAll%2Febd_student%2F30%3Fcjid%3D2447fb3f-925d-4d16-ae5c-d51eb583f3ca&amp;useQueryString=false&amp;requeueOnError=false&amp;httpSuccess=&amp;assumeSuccessTimeout=30&amp;params=&amp;filePostName=file&amp;fileTypes=*.xls%3B*.xlsx&amp;fileTypesDescription=%E5%8F%AA%E8%83%BD%E9%80%89%E6%8B%A9excle&amp;fileSizeLimit=0&amp;fileUploadLimit=0&amp;fileQueueLimit=999&amp;debugEnabled=true&amp;buttonImageURL=%2Febd%2F&amp;buttonWidth=120&amp;buttonHeight=30&amp;buttonText=&amp;buttonTextTopPadding=0&amp;buttonTextLeftPadding=0&amp;buttonTextStyle=color%3A%20%23000000%3B%20font-size%3A%2016pt%3B&amp;buttonAction=-110&amp;buttonDisabled=false&amp;buttonCursor=-2">
						<object type="application/x-shockeave-flash" data="flash/1.swf" width="120" height="30" class="swfupload" style="position: absolute; z-index: 1; ">
							<param name="wmode" value="transparent">
							<param name="quality" value="high">
							<param name="wmode" value="transparent">
							<param name="swfversion" value="6.0.65.0">
							<param name="expressinstall" value="/fxpt/static/resources/uploadify/uploadify.swf">
							<div>
								<h4>此页面上的内容需要较新的 Adobe Flash Player.</h4>
								<p><a href="http://www.adobe.com/go/getflashplayer"></a></p>
							</div>
						</object>
					</object>
				 </div>
				  <p class="error-tip"></p>
			</div>
		</div>
        <div class="form-group" >
		   <div  class="col-sm-12" style="padding-left:500px;">
			      <button class="btn btn-primary" id="addReportData" type="button">保存</button>
		   </div>	
		</div>	
 </form>
</div>
</div>
