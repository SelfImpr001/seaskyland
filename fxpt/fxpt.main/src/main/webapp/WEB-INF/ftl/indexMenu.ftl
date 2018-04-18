<!doctype html>
<html lang="zh-cn">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
	<title></title>
	<link rel="stylesheet" href="resource/css/bootstrap-custom.css">
	<link rel="stylesheet" href="resource/css/font-awesome.css">
	<link rel="stylesheet" href="resource/css/lib/bootstrap-datetimepicker.css">
	
	<link rel="stylesheet" href="resource/css/add_login_index.css">
	<!--[if lt IE 9]>
        <script src="scripts/lib/bootstrap/html5shiv.js"></script>
        <script src="scripts/lib/bootstrap/respond.min.js"></script>
    <![endif]-->
    <!--[if IE 7]>
		<link rel="stylesheet" href="resource/css/lib/bootstrap-ie/font-awesome-ie7.min.css">
	<![endif]-->
	<!--[if lte IE 6]>
		<link rel="stylesheet" type="text/css" href="resource/css/lib/bootstrap-ie/bootstrap-ie6.css">
		<link rel="stylesheet" type="text/css" href="resource/css/lib/bootstrap-ie/ie.css">
	<![endif]-->
	<script data-main="scripts/main" src="scripts/require.js"></script>	
</head>
<div class="header">
	<h1>海云天数据发布平台</h1>
	<div class="right-area">
		<p class="welcome">
			你好，今天是 <span>2014年5月4日</span>，欢迎登陆 <strong>海云天数据发布平台</strong>
		</p>
		<div class="icons">
			<a href="#" title="主页" data-toggle="modal" data-target="#myModal"><i class="icon-home icon-large"></i></a>
			<a href="#" title="密码修改"><i class="icon-key icon-large"></i></a>
			<a href="#" title="帮助"><i class="icon-h-sign"></i></a>
			<a href="#" title="退出"><i class="icon-off"></i></a>
		</div>
	</div>
</div>
<body entry="index" rootPath="">


<div id="content">
	<!-- <div class="container"> -->
		<div class="row">
			<div class="col-lg-2 col-sm-3 hidden-xs left-side">
				<div class="heading">
					<h3>example</h3>
				</div>

				<ul class="nav nav-box">

					<li>
						<a href="#"><span class="icon-folder-close"></span> &nbsp;一级</a>
						<ul class="nav">
							<li>
								<a href="#"><span class="icon-folder-close"></span> 二级菜单1</a>
								<ul class="nav">
									<li><a href="#"><span class="icon-file"></span> 三级菜单1</a></li>
									<li><a href="#"><span class="icon-file"></span> 三级菜单2</a></li>
									<li><a href="#"><span class="icon-file"></span> 三级菜单3</a></li>
								</ul>
							</li>
							<li>
								<a href="#"><span class="icon-folder-close"></span> 二级菜单2</a>
								<ul class="nav">
									<li><a href="#"><span class="icon-file"></span> 三级菜单1</a></li>
									<li><a href="#"><span class="icon-file"></span> 三级菜单2</a></li>
									<li><a href="#"><span class="icon-file"></span> 三级菜单3</a></li>
								</ul>
							</li>
							<li>
								<a href="#"><span class="icon-folder-close"></span> 二级菜单2</a>
								<ul class="nav">
									<li><a href="#"><span class="icon-file"></span> 三级菜单1</a></li>
									<li><a href="#"><span class="icon-file"></span> 三级菜单2</a></li>
									<li><a href="#"><span class="icon-file"></span> 三级菜单3</a></li>
								</ul>
							</li>
							<li>
								<a href="#"><span class="icon-folder-close"></span> 二级菜单2</a>
								<ul class="nav">
									<li><a href="#"><span class="icon-file"></span> 三级菜单1</a></li>
									<li><a href="#"><span class="icon-file"></span> 三级菜单2</a></li>
									<li><a href="#"><span class="icon-file"></span> 三级菜单3</a></li>
								</ul>
							</li>
							<li>
								<a href="#"><span class="icon-folder-close"></span> 二级菜单3</a>
								<ul class="nav">
									<li><a href="#"><span class="icon-file"></span> 三级菜单1</a></li>
									<li><a href="#"><span class="icon-file"></span> 三级菜单2</a></li>
									<li><a href="#"><span class="icon-file"></span> 三级菜单3</a></li>
								</ul>
							</li>
						</ul>
					</li>

					<li>
						<a href="#"><span class="icon-folder-close"></span> &nbsp;一级</a>
						<ul class="nav">
							<li><a href="#"><span class="icon-file"></span> item1</a></li>
							<li><a href="#"><span class="icon-file"></span> item2</a></li>
							<li><a href="#"><span class="icon-file"></span> item3</a></li>
						</ul>
					</li>
					<li>
						<a href="#"><span class="icon-folder-close"></span> &nbsp;一级</a>
						<ul class="nav">
							<li><a href="#"><span class="icon-file"></span> item1</a></li>
							<li><a href="#"><span class="icon-file"></span> item2</a></li>
							<li><a href="#"><span class="icon-file"></span> item3</a></li>
						</ul>
					</li>
					<li class="h40"></li>
				</ul>
				<div class="on-off">
					<span class="togo"></span>
				</div>

				
			</div>
			<div class=" main">
			<!-- col-lg-10 col-sm-9	 -->	
				<div class="content">
					<iframe src="table-example.html" id="frame1" name="frame1" frameborder="0">
					</iframe>		
				</div>
			</div> 
		</div>
	<!-- </div> -->
</div>

<div id="footer">
	<div class="container">
		
	</div>
</div>

<!-- 弹出框 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog self-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h4 class="modal-title" id="myModalLabel">这是一个标题</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal" role="form">

		  <div class="form-group">
		    <label for="m-password1" class="col-sm-2 control-label">原密码</label>
		    <div class="col-sm-7">
		      <input type="email" class="form-control" id="m-password1" placeholder="密码">
		      
		    </div>
		    <span>密码错误</span>
		  </div>

		  <div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
		      <div class="checkbox">
		        <label>
		          <input type="checkbox"> Remember me
		        </label>
		      </div>
		    </div>
		  </div>
		  
		  <div class="form-group">
		  	<label for="select1" class="col-sm-2 control-label">select</label>
		  	<div class="col-sm-7">
		  		<select class="form-control" id="select1">
		  					  <option value="1">1</option>
		  					  <option value="2">2</option>
		  					  <option value="3">3</option>
		  					  <option value="4">4</option>
		  					  <option value="5">5</option>
		  		</select>
		  	</div>
		  </div>


		  <div class="form-group">
		    <label for="inputPassword3" class="col-sm-2 control-label">Password</label>
		    <div class="col-sm-7">
		      <input type="password" class="form-control" id="inputPassword3" placeholder="Password">
		    </div>
		    <span>密码错误</span>
		  </div>


			<div class="form-group">
				<div class="checkbox-inline col-sm-offset-2">
					<label class="checkbox-inline">
					  <input type="checkbox" id="inlineCheckbox1" value="option1"> 1
					</label>
					<label class="checkbox-inline">
					  <input type="checkbox" id="inlineCheckbox2" value="option2"> 2
					</label>
					<label class="checkbox-inline">
					  <input type="checkbox" id="inlineCheckbox3" value="option3"> 3
					</label>
				</div>
			</div>
		</form>  
      </div>
      <div class="modal-footer">
        <div class="self-modal-footer">
        	<button type="button" class="btn btn-primary">确定</button>
        	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>       	
        </div>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div>


</body>
</html>

<!--[if lte IE 6]>
  <script type="text/javascript" src="scripts/lib/bootstrap-ie.js"></script>
<![endif]-->

