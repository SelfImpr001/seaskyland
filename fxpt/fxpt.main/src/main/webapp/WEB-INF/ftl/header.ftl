<#macro header >
<div class="navbar navbar-default" id="navbar">
    <div class="navbar-container" id="navbar-container">
        <div class="navbar-header pull-left">
            <a ${logoImage}  href="${request.contextPath}/home" class="navbar-brand"><span>深圳市教科院高三学业质量测评系统</span></a><!-- /.brand -->
            
        </div>	
    </div>
    <div class=" btn-group skin_change">
    	<i type="button" class="dropdown-toggle icon-picture" data-toggle="dropdown" aria-haspopup="true" title="换肤"></i>
    	<ul class="dropdown-menu">
    		<li class=""><a herf="#">默认</a></li>
    		<li class=""><a herf="#">颜色1</a></li>
    		<li class=""><a herf="#">颜色2</a></li>
    	</ul>
    </div>
    <!--个人信息-->
    <div class="navbar-header pull-right" role="navigation">
        <#if user??>
	        <#if user.orgCodes??>
	        	<#list user.orgCodes as orgCode>
	        		<input type="hidden" id="orgCode" name="orgCode" value="${orgCode}"/>
	        	</#list>
	        </#if>
        <ul class="nav ace-nav">
            <li class="light-blue">
                <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                    <img class="nav-user-photo" src="${request.contextPath}/static/resources/images/user.png" alt="user's Photo" />
                    <span class="user-info"><small>欢迎光临,</small><#if user.nickName??>${user.nickName}<#elseif user.realName??>${user.realName}<#elseif user.userInfo??>${user.userInfo.nickName}<#else>${user.name}</#if></span>
                    <i class="icon-caret-down"></i>
                </a>

                <ul class="user-menu pull-right dropdown-menu  dropdown-caret dropdown-close">
                    <#if theUser??>
                       <li><a href="javascript:void(0);" class="editPass" pk=${theUser.pk} data-tt-value="1"><i class="icon-lock "></i> 修改密码</a></li>
                 
                    </#if>  
                   <!-- <li><a href="javascript:void(0);"><i class="icon-user"></i>个人资料</a></li>-->
                    
              
                  <li><a  <#if !priview>href="${request.contextPath}/logout"</#if> ><i class="icon-off"></i> 退出</a></li>
                    <#-- <li><a href="http://192.168.2.123:18080/cas/logout"><i class="icon-off"></i> 退出SSO</a></li> -->
                   <#-- <li><a href="${sysLoginOut}"><i class="icon-off"></i> 退出</a></li>-->
                    <li><a href="javascript:void(0);" class="about"><i class="icon-flag"></i> 关于</a></li>
                 
                   <!-- <li class="divider"></li>-->
                </ul>
            </li>
        </ul>
        </#if>
           <input type="hidden"  name="title" id="title" value="${title}"/>
            <input type="hidden"  name="version" id="version" value="${version}"/>
            <input type="hidden"  name="date" id="date" value="${date}"/>
    </div>
</div>
</#macro>

