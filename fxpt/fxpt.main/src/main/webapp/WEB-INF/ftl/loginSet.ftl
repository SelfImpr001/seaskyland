<!doctype html>
<html lang="en">
<head>
<script language="javascript" src="../static/scripts/lib/jquery.js"></script>
<script language="javascript" src="../static/scripts/app/pentahoSet/pentahoSet.js"></script>
	<meta charset="UTF-8" />
	<title>系统安装配置</title>
	<style>
		body{
			*{
				margin: 0;
				padding: 0;
			}
		}
		.login{
			
			border:1px solid red;
			width:400px;
			height: 300px;
			position: absolute;
			top: 0;
			bottom: 0;
			right: 0;
			left: 0;
			margin: auto;
		}
		h2{
			    margin: 20px;
			text-align: center;
			padding-bottom: 20px;
			border-bottom: 1px solid green;
		}
		ul{
			
			list-style: none;
			padding: 20px;
		}
		li{
			margin-top: 20px;
		}
		label{
			width:100px;
			display: inline-block;
		}
		input{
			padding-left: 10px;
			height: 20px;
   		 	width: 210px;
		}
		.btn{
			text-align: center;
		}
		button{
			width: 70px;
			height: 30px;
			margin:0 10px 0 10px;
		}
	</style>
</head>
<body>
		<form action="login" class="login">
		<h2>用户登录</h2>
		<ul>
			<li><label for="username">用户名:</label><input type="text" id="username" value="admin" placeholder="请输入用户名"/></li>
			<li><label for="pwd">密码:</label><input type="password" id="password" value=""  placeholder="请输入密码"/></li>
		</ul>
		<div class="btn">
			<button type="button" id="loginGo">登录</button>
			<button type="button" id="resert">重置</button><span id="loginMessge" style="color:red"></span>
		</div>
	
	</form>
</body>
</html>