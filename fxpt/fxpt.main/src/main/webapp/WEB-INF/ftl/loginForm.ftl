<#macro loginForm title="" showCode=false showAgreemet=false showRememberme=false >
  <div class="login-inner-wrapper">
    <div class="form-box">
      <div class="login-main">
      	<h1 ${logoImage}>${title}</h1> 
      	     <input type="hidden" name="islogin" id="islogin" value="${isloin!}"/>
        <form action="${request.contextPath}/login" method="POST" id="loginForm">
          <fieldset>
            <label class="block clearfix"> <!---输入错误时，在span元素内增加Class .error; 输入正确时增加Class .right--> 
            <span class="block input-icon input-icon-right"> 
           
	            <#if Request["username"]??>
	              <input type="text" class="form-control" name="username" value="${Request["username"]!""}" placeholder=" 请输入用户名">
	            <#else>
	              <input type="text" class="form-control" <#if priview> disabled value=""</#if> name="username"  value="<@shiro.principal/>"   placeholder="请输入用户名" auto-login="true">     
	            </#if>
              <i class="icon-user "></i>
            </span>
            </label> 
            <label class="block clearfix"> <!---输入错误时，在span元素内增加Class .error; 输入正确时增加Class .right--> 
            <span class="block input-icon input-icon-right"> 
              <input type="password" class="form-control"  <#if priview> disabled value=""</#if> name="password" placeholder="请输入6位数以上的密码" value="${Request["password"]!""}"> 
              <i class="icon-lock"></i>
            </span>
            </label>
            <#if showCode=true>
            <label class="block clearfix"> <!---输入错误时，在span元素内增加Class .error; 输入正确时增加Class .right--> <span
              class="block input-icon input-icon-right pull-left"> <input type="text" class="form-control verify"
                name="kaptcha" placeholder=" 验证码"> 
                <img src="${request.contextPath}/images/validateCode.jpg" alt="验证码" id="kaptchaImages" style="cursor: pointer;"/> 
                <a href="javascript:void(0);" title="">换一个<i class="icon-refresh"></i></a>
            </span>
            </label>
            </#if>
            <#if showRememberme=true>
            <div class="clearfix remember-area">
              <label class="inline pull-left ">
               <input type="checkbox" name="rememberMe" class="ace" value="true" checked> 
               <span class="lbl" style="">&nbsp;&nbsp;记住我</span>
              </label>
            </div>
            </#if>
            <div class="clearfix txt-ali-c">
              <button type="submit" class="width-35 btn  btn-primary" <#if priview>disabled</#if> id="login"><i class="icon-key"></i>&nbsp;登 录&nbsp;&nbsp;</button>
              <button type="button" class="width-35 btn  btn-info" <#if priview>disabled</#if> id="reset"><i class="icon-refresh"></i>&nbsp;重置&nbsp;&nbsp;</button>
            </div>
            <div class="space-4"></div>
          </fieldset>
          <p class="error-tip" ><#if Request["LoginFailure"]??><em class="error">${Request["LoginFailure"]}</em></#if></p>
        </form>
        <input type="hidden" name="expires" value="${expires?c}">
      </div>
      <#if showAgreemet=true>
      <div class="box-bar">
        <span><a href="#"><i class="icon-arrow-left"></i>&nbsp;&nbsp;忘记密码</a></span> <span><a href="#">我要注册&nbsp;&nbsp;<i
            class="icon-arrow-right"></i></a></span>
      </div>
      </#if>
    </div>
  </div>

</#macro>