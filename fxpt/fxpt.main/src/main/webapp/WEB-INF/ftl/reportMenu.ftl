<!doctype html>
<#import "meta1.ftl" as meta>
<#import "script.ftl" as script>
<#import "script.ftl" as script>
<html lang="zh-cn">
<head>
	<@meta.meta title=""/>
	<@script.script entry="index"/>
</head>
<body entry="index" rootPath="">
<div id="content">
	<!-- <div class="container"> -->
		<div class="row">
			<div class="col-lg-2 col-sm-3 hidden-xs left-side">
				<div class="heading">
					<h3>example</h3>
				</div>

				<ul class="nav nav-box">
				<#--
					<li>
						<a href="#"><span class="icon-folder-close"></span> &nbsp;2014深圳中学</a>
						<ul class="nav">
						
							<li>
								<a href="#"><span class="icon-folder-close"></span>学生报表</a>
								<ul class="nav">
									<li id="res"><a href="#" url="http://192.168.21.5:18080/smartbi/vision/openresource.jsp?user=admin&password=manager&resid=I402895850333cf81014596d0f0321a6c"><span class="icon-file"></span> 成绩表</a></li>
									<li id="res"><a href="#" url="http://192.168.21.5:18080/smartbi/vision/openresource.jsp?user=admin&password=manager&resid=I40289585744c75fb0145f464d9a202bd"><span class="icon-file"></span> 分数段</a></li>
								</ul>
							</li>
							<li>
								<a href="#"><span class="icon-folder-close"></span> SmartBi内置报表</a>
								<ul class="nav">
									<li id="res"><a href="#" url="http://192.168.21.5:18080/smartbi/vision/openresource.jsp?user=admin&password=manager&resid=I2c9410a623db31660123db5032d000b1"><span class="icon-file"></span> 产品销售明细分析</a></li>
									<li id="res"><a href="#" url="http://192.168.21.5:18080/smartbi/vision/openresource.jsp?user=admin&password=manager&resid=I2c9410a623cc37d10123cc410517002b"><span class="icon-file"></span> SmartBi基本功能展示</a></li>
									<li id="res"><a href="#" url="http://192.168.21.5:18080/smartbi/vision/openresource.jsp?user=admin&password=manager&resid=I2c949e121d27be5d011d29534fc50448"><span class="icon-file"></span> 雷达图</a></li>
									<li id="res"><a href="#" url="http://192.168.21.5:18080/smartbi/vision/openresource.jsp?user=admin&password=manager&resid=I2c949ece1c91eebd011c924bfb830077"><span class="icon-file"></span> 复杂报表-交叉表报</a></li>
									<li id="res"><a href="#" url="http://192.168.21.5:18080/smartbi/vision/openresource.jsp?user=admin&password=manager&resid=I2c9490741a9e6fa6011a9f6dd720269f"><span class="icon-file"></span> 指标报表-损益表</a></li>
								</ul>
							</li>
							
						</ul>
					</li>
				
					<#list listA4UserMenus as menu>
						<#if menu.parentId == 1>
							<li>
								<a href="#"><span class="icon-folder-close"></span> &nbsp; ${menu.menuName}</a>
								   <#list listA4UserMenus as childmenu> 
									  	<#if childmenu.parentId==menu.id>
											<ul class="nav">
												<li id="res"><a href="#" url="${childmenu.menuUrl}"><span class="icon-file"></span> ${childmenu.menuName}</a></li>
											</ul>
									    </#if>
									    
									   
								   </#list>
							</li>
						</#if>
					</#list>
						-->
					 <li class="active"><a href="#"><span class="icon-folder-close"></span>
				         ${node.name}</a>
				        <#if node.childs??>
				        	<@buildNode child=node.childs/>
				        </#if>
				     </li>
					<#macro buildNode child> 
					    <#if child?size gt 0> 
					        <#list child as t> 
					        	<ul class="nav">
							        	<li id="res"><a href="###" url="${t.description}"><span class="icon-folder-close"></span>${t.name}</a> 
								        	<#if t.childs??>
								            <@buildNode child=t.childs/>
								            </#if>
							            </li> 
					            </ul>
					        </#list> 
					    </#if> 
					</#macro> 
					
					<@buildNode child=node.childs/>

				<#--	
					<li>
						<a href="#"><span class="icon-folder-close"></span> &nbsp;一级</a>
						<ul class="nav">
							<li><a href="#"><span class="icon-file"></span> item1</a></li>
							<li><a href="#"><span class="icon-file"></span> item2</a></li>
							<li><a href="#"><span class="icon-file"></span> item3</a></li>
						</ul>
					</li>
				-->	
					
					<li class="h40"></li>
				</ul>
				<div class="on-off">
					<span class="togo"></span>
				</div>

				
			</div>
			<div class=" main">
			<!-- col-lg-10 col-sm-9	 -->	
				<div class="content">
					<iframe src="http://192.168.21.5:18080/smartbi/vision/openresource.jsp?user=admin&password=manager&resid=I2c9410a623cc37d10123cc410517002b&hidetoolbaritems=MYFAVORITE" id="frame1" name="frame1" frameborder="0">
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


</body>
</html>

<!--[if lte IE 6]>
  <script type="text/javascript" src="scripts/lib/bootstrap-ie.js"></script>
<![endif]-->

