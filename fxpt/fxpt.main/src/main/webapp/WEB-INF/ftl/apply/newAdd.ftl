<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" >
 
 
<head>     
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />     
<title>图片上传</title>     
<style type="text/css">
#preview{width:260px;height:140px;border:1px solid #000;overflow:hidden;}
#preview2{width:260px;height:140px;border:1px solid #000;overflow:hidden;}
#uploadImgHandle {filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image);}
</style>    
</head>     
<body>
<form  id="applyform" name="applyform" method="post" action="apply/update">
 <div  class="form-group"  style="width:500px">
 
	      <label  for="order1" class="col-sm-3 control-label">是否有效</label>
	      <div  class="col-sm-9 radio-content">
	        <div  class="download-check">
	          <input  id="yes"  type="radio" checked  class="ace myData" name="status" value="true" /> 
	          <label  for="yes" class="lbl"></label>有效
	        </div>&nbsp;
	        <div  class="download-check">
	          <input   id="no"   type="radio" class="ace myData" name="status" minlength="2" value="false"  /> 
	          <label  for="no" class="lbl"></label>无效
	        </div>&nbsp;
	      </div>
	    </div>
	

		<div  class="form-group">
			<label  class="col-sm-3 control-label" for="m-password1" >系统名称</label>
			<div  class="col-sm-9">
				<input type="text" class="form-control myData"   name="systemName" id="systemName" placeholder="系统名称" maxlength="100" value="">
				<p  class="error-tip"></p>
			</div>
		</div>
	 <div  class="form-group"   style="width:500px">
      <label  for="fileUploadL" class="col-sm-3 control-label">登录界面图标</label>
      <div  class="col-sm-7" id="loginDefault">
        <input  type="file" id="fileLogin"  />    
        <a href="#" class="col-sm-0"  style="font-size:15px;text-decoration:none;" >恢复默认</a>   
        <div  id="preview" >
    		<img  id="uploadImgLogin"  class="col-sm-10" width=100% height=auto border=0 src=''>
    		
		</div>
        <p>尺寸建议:150X40px、支持JPEG,TIFF,RAW,BMP,PNG,GIF,JPG格式</p>
        <p class="error-tip"></p>
      </div>
       <div class="col-sm-2" style="text-align:center;padding-top:80px;">
       <a href="priview/loginLY/-1/-1"  class="col-sm-0" id="loginHref" target="_blank" style="font-size:20px;text-decoration:none;">预览</a>
       </div>
    </div>
    
     <div  class="form-group"   style="width:500px">
      <label  for="fileUploadH" class="col-sm-3 control-label">操作界面图标</label>
      <div  class="col-sm-7" id="handleDefault">
      <input  type="file"  id="fileHandle"  />     
      <a href="#" class="col-sm-0"  style="font-size:15px;text-decoration:none;" >恢复默认</a>  
        <div  id="preview2">
    		<img  id="uploadImgHandle"  class="col-sm-10" width=100% height=auto border=0 src=''>
		</div>
        <p>尺寸建议:150X40px、支持JPEG,TIFF,RAW,BMP,PNG,GIF,JPG格式</p>
        <p class="error-tip"></p>
      </div>
      <div  class="col-sm-2" style="text-align:center;padding-top:80px;">
         <a href="priview/mainLY/-1/-1" class="col-sm-0" target="_blank" style="font-size:20px;text-decoration:none;">预览</a>
      </div>
    </div>
     <div  class="form-group"  style="width:500px">
      <label  for="int11" class="col-sm-3 control-label">排列顺序</label>
      <div  class="col-sm-9">
        <input  type="text" class="form-control myData" id="order" name="order"  value="">
        <p  class="error-tip"></p>
      </div>
    </div>
</form>
</body>     
</html>
