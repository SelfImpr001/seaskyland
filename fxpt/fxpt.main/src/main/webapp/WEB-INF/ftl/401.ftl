<#import "meta.ftl" as meta>

<html lang="zh-cn">
<head>
	<@meta.meta title="海云4A平台 服务器异常"/>
	
</head>
<body  class="over-h">
<div class="page-content">
  <div class="row">
    <div class="col-xs-12">
      <div class="error-container">
        <div class="well bigger-120">
          <h1 class="grey lighter smaller">
            <span class="blue  mr-10"><i class="icon-sitemap mr-10"></i>401</span>未授权
          </h1>
          <hr>
          <p>很抱歉，您无权使用所有请求的功能。</p>
          <p>请重新登录后再重试</p>
          <ul class="list-unstyled spaced inline bigger-110 margin-15">
            <li><i class="icon-hand-right blue  mr-10"></i><a href="${request.contextPath}/logout">重新登录</a></li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>