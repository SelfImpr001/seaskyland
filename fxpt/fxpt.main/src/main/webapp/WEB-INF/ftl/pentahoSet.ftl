 <!DOCTYPE html>
<html>
<head>

<link href="../static/resources/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="/fxpt/static/resources/css/jquery-ui.css">
<link rel="stylesheet" href="/fxpt/static/resources/css/cntest.css">


<script language="javascript" src="../static/scripts/lib/jquery.js"></script>
<script language="javascript" src="../static/scripts/app/pentahoSet/dialog.js"></script>
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

h1,h2,h3,h4,h5,h6,.h1,.h2,.h3,.h4,.h5,.h6 {
	font-family: 'microsoft yahei', "Open Sans", "Helvetica Neue", Helvetica,
		Arial, sans-serif;
	font-weight: 500;
	line-height: 1.1;
}	
ul{list-style:none;overflow:hidden;}
.divclass{padding:15px;background-color: #00a0e9;}
label{width:100px;display:inline-block;margin:10px 0px 10px 10px;}
.lab1{width:30%;}
.lab2{width:100%;}
button{height:36px;margin: 0 auto;display:inline-block;width: 100px;font-size: 20px;}
	
.btn-margin{width: 100px;margin: 20px auto;}
.btn-font{text-align:center;}
</style>
</head>


<body>
<a href="../systemSet/pentaho">pentaho配置</a>
<a href="../systemSet/sso">分析平台安装配置</a>
	<form action="pentaho" method="post">
		<div class="divclass">
		    <label class="lab1">1、pentaho配置文件context.xml的存放位置</label>
			<input type="text" name="contextAddress" id="contextAddress" value="" readonly="readonly" style="width:600px" placeholder="请选择pentaho数据库配置文件地址">
			<button type="button" id="fileViewId">预览文件</button>
		</div>
		<ul>
			<li><label>服务器地址</label>
			<input type="text" name="pentahoAddress"  id="pentahoAddress"  value="${address!''}" style="width:300px" placeholder="请输入服务器地址">
			</li><li><label>端口</label>
			<input type="text" name="post" id="post"  value="${post!''}" placeholder="请输入端口号">
			</li><li><label>数据库名称</label>
			<input type="text" name="tableName" id="tableName" value="${dataBase!''}" placeholder="请输入数据库名称">
			</li><li><label>用户名</label>
			<input type="text" name="name" id="name"  value="${userName!''}" placeholder="请输入用户名">
			</li><li><label>密码</label>
			<input type="text" name="password" id="password" value="${password!''}" placeholder="请输入密码">
			</li>
		</ul>
		<ul>
			<li class="btn-font"><span id="messagePentaho" style="color:red"></span></li>
			<li class="btn-margin"><button type="button" id="pentaho">连接测试</button></li>
		</ul>	
			<div class="sure divclass">
			    <label class="lab2 ">2、请登录http://localhost:port/pentaho管理后台，配置数据源manage Data Source</label>
		        <input type="hidden" name="action" value="submit">
			</div>
			<ul>	
			    <li class="btn-font"><span id="pentahoMessge" style="color:red"></span></li>	
			    <li class="btn-margin"><button class="sub" type="button" id="subPentaho">确认</button></li>
			</ul>
	</form>
</body>
</html>