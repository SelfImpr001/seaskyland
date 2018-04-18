<!doctype html>
<#import "meta.ftl" as meta>
<#import "script.ftl" as script>
<#import "navbar.ftl" as navbar>

<html lang="zh-cn">
<head>
	<@meta.meta title=""/>
	<@script.script />
</head>
<body class="overflow-h" entry="report" rootPath="">
<div class="header">
	<@navbar.navbar />
</div>	
<div id="R-main" class="mrt-20">
<div class="R-main-wrap">
		<div class="row content">

		<a href="index?userId=${a4User.userId}" title="返回考试列表" class="btn btn-default back">
			<span class="glyphicon glyphicon-arrow-right"></span>
		</a>

		<ul class="nav nav-tabs" id="myTab">
		  	<li><a href="#test-info" data-toggle="tab">考试信息</a></li>
		  	<li><a href="#papers-info" data-toggle="tab">试卷信息</a></li>
		  	<li><a href="#analysis" data-toggle="tab">分析信息</a></li>
		  	<li class="active"><a href="#report-html" data-toggle="tab">报告</a></li>
		</ul>

<!-- Tab panes -->
		<div class="tab-content">

		  	<div class="tab-pane" id="test-info">
	
				<div class="page-header title-text">
					<h2>这是一个报告111 <small>Subtext for header</small></h2>
				</div>
	
				<p class="the-text">
				  	some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text 
				</p>



		  	</div>
		  	<div class="tab-pane" id="papers-info">

		  		<div class="page-header title-text">
					<h2>这是一个报告222 <small>Subtext for header</small></h2>
				</div>
	
				<p class="the-text">
				  	some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text 
				</p>

		  	</div>
		  	<div class="tab-pane" id="analysis">
		  		
		  		<div class="page-header title-text">
					<h2>这是一个报告333 <small>Subtext for header</small></h2>
				</div>
	
				<p class="the-text">
				  	some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text some text 
				</p>

		  	</div>
		  	<div class="tab-pane active" id="report-html">
		  		<iframe src="reportMenu?userId=${a4User.userId}" id="r-index" name="r-index" frameborder="0" style="width:100%; height:800px;">
				</iframe>
		  	</div>
		</div>
		
	</div>
</div>	






</div>

<script data-main="scripts/main" src="scripts/require.js"></script>	
</body>
</html>
<!--[if lte IE 6]>
  <script type="text/javascript" src="scripts/lib/bootstrap-ie.js"></script>
<![endif]-->

