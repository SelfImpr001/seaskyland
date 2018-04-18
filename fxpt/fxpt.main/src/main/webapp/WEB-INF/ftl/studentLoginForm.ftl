<#macro loginForm title="" showCode=false showAgreemet=false showRememberme=false >
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <span class="icon-info"></span>
        <h1>学生学业报告查询</h1>
      </div>
      <div class="modal-body">
        <form id="stuForm" role="form" action="${request.contextPath}/student/login" class="form-horizontal" method="POST">
          <div class="form-group" >
            <label class="col-sm-3 control-label">姓名</label>
            <div class="col-sm-9">
              <input  type="text" class="form-control myData"  name="name"  placeholder="请输入您的姓名" value="${Request["name"]!""}">
              <span class="required">*</span>
            </div> 
          </div>
          <#--  2016.12.1 屏蔽
          <div class="form-group" >
            <label class="col-sm-3 control-label">就读学校</label>
            <div class="col-sm-9 ">
              <input type="hidden" name="schoolCode" id="schoolCode" value="${Request["schoolCode"]!""}">
                <!--
                <select class="form-control selectpicker ztree-box" name="status" name="shoolName"  required id="shoolName">
                  <option value="-1">请选择学校</value>
                </select>
                ->
              <div class=" ztree-box  has-feedback" >                
                <input  type="text" class="form-control myData"  name="schoolName"   id="schoolName" placeholder="请选择学校" value="${Request["schoolName"]!""}" readOnly>
                <span class="glyphicon glyphicon-ok form-control-feedback icon-sort-down"></span>
                
              </div>
              <span class="required">*</span>
            </div> 
          </div>
          -->
          <div class="form-group" >
            <label class="col-sm-3 control-label">准考证号</label>
            <div class="col-sm-9">
              <input type="text" class="form-control myData"   name="identity" value="${Request["identity"]!""}" placeholder="请输入您的准考证号">
              <span class="required">*</span>
            </div> 
          </div>
          
          
           <#if showCode==true>
	          <div class="form-group" >
	           <label class="col-sm-3 control-label">验证码</label>
	           <div class="col-sm-9">
	             <input type="text" class="form-control verify" name="kaptcha"  placeholder="验证码" style="width:80px;"> 
	             <span class="img-group " >   
	                <img src="${request.contextPath}/images/validateCode.jpg" alt="验证码" id="kaptchaImages" /> 
	                <a href="javascript:void(0);" title="">换一个</a>
	             </span>
	            </div>
	          </div>
          </#if>
         
          <div class="form-group" >
            <label class="col-sm-3 control-label"></label>
            <div class="col-sm-9">
              <button type="submit" class="btn btn-primary" style="background-color:#00a0e9;width:177px;height:68px;">登录</button>
            </div> 
          </div> 
          <input type="hidden" name="rememberMe" class="ace myData" value="false">                                                       
        </form>          
      </div>
      <div class="modal-footer">
        <div class="modal-footer-sp"></div>
        <div class="modal-footer-info pull-left">
          <span class="tips-info-icon"></span><span class="tips-info-text"><#if Request["LoginFailure"]??>${Request["LoginFailure"]}<#else>请输入您的学籍信息，谢谢！</#if></span>
        </div>
      </div>
      <div class="modal-shadow"></div>
    </div>
  </div>
</#macro>