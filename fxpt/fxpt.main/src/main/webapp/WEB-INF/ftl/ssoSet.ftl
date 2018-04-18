<!DOCTYPE html>
<html>
<head>
<script language="javascript" src="../static/scripts/lib/jquery.js"></script>
<script language="javascript" src="../static/scripts/app/pentahoSet/pentahoSet.js"></script>
<style type="text/css">
	div,ul,li,span,a,i{margin: 0;padding: 0;}
	html {
	height: 100%;
	position: relative;
}
body {
	background-color: #fff;
	color: #393939;
	font-family: 'microsoft yahei', 'Open Sans';
	font-size: 16px;
	line-height: 1.5;
	height: 100%;
	padding-bottom: 0;
}
ul{list-style:none;}
	h3{width:100%;height:50px;line-height:50px;background-color:#00a0e9;padding-left:20px;}
	label{width:212px;  display: inline-block;padding-left: 15px;}
	input{margin-top:10px}
	button{ height: 36px;margin: 26px auto;display: block;width: 100px;font-size: 20px;}
</style>
	
</head>

<body>
	<div>
		<a href="../systemSet/pentaho">pentaho配置</a>
		<a href="../systemSet/sso">分析平台安装配置</a>
	</div>
	<form action="sso" method="post">
		 <div>
		    <label>区域编码（area.org.code）</label>
			<input type="text" name="area_org_code"  id="area_org_code" value="${areaCode!''}" style="width:200px"><p style="padding-left:15px">注：可选项有wuhou(AB)、foshan(CHOICE)、xinjiang、nanshan</p> 
		</div>
		<h3>数据库连接配置</h3>
	   	<ul>
			<li><label>服务器地址</label>
			<input type="text" name="jdbc_ip"  id="jdbc_ip" value="${fxpthost!''}" style="width:300px">
			</li><li><label>端口</label>
			<input type="text" name="jdbc_port" id="jdbc_port"  value="${post!''}">
			</li><li><label>数据库名称</label>
			<input type="text" name="jdbc_database" id="jdbc_database" value="${tablname!''}">
			</li><li><label>用户名</label>
			<input type="text" name="jdbc_username" id="jdbc_username"  value="${name!''}">
			</li><li><label>密码</label>
			<input type="text" name="jdbc_password" id="jdbc_password" value="${password!''}">
			</li></ul>
			<div style="text-align:center"><span id="messageSSO" style="color:red"></span></div>
			<button type="button" id="ssoTest">连接测试</button>
		<ul>
		<h3>配置分析平台单点登录</h3>
	    <div>
		    <label>shiro.sso.server.url</label>
			<input type="text" name="shiro_sso_server_url" id="shiro_sso_server_url" value="${serverUrl!''}" style="width:600px">
		</div>
		<div>
			<label>shiro.sso.service.url</label>
			<input type="text" name="shiro_sso_service_url" id="shiro_sso_service_url" value="${serviceUrl!''}" style="width:600px">
		</div>
		<div>
			<label>shiro.sso.login.url</label>
			<input type="text"  name="shiro_sso_login_url" id="shiro_sso_login_url" value="${loginUrl!''}" style="width:600px">
		</div>
		<h3>配置分析平台退出URL</h3>
		
		<div>
		<label>退出URL</label>
		<input type="text" name="shiro_sso_loginOut_url" id="shiro_sso_loginOut_url" value="${loginOutUrl!''}" style="width:600px">
		</div>
		
		<div>
			<input type="hidden" name="action" value="submit">
		</div>
		<div style="text-align:center;margin-top:10px">
			<span id="ssoMessge"  style="color:red"></span>
		</div>
		<div>
			<button type="button" id="sbt" >确认</button>
		</div>	
		
	</form>
</body>
</html>