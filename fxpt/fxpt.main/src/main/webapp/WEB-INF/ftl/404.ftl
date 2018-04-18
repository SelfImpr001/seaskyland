<#import "meta.ftl" as meta>
<#import "script.ftl" as script>
<html lang="zh-cn">
<head>
	<@meta.meta title="海云4A平台 404"/>
	
</head>
<body  class="over-h">
<div class="page-content">
  <div class="row">
    <div class="col-xs-12">
      <div class="error-container">
        <div class="well bigger-120">
          <h1 class="grey lighter smaller">
            <span class="blue  mr-10"><i class="icon-sitemap mr-10"></i>404</span>您所请求的功能尚未完成
          </h1>
          <hr>
          <p>很抱歉，您要使用的功能尚未完成。</p>
          <p>点击以下链接继续使用本系统</p>
          <ul class="list-unstyled spaced inline bigger-110 margin-15">
            <li><i class="icon-hand-right blue  mr-10"></i><a href="javascript:history.go(-1)">返回上一级页面</a></li>
            <li><i class="icon-hand-right blue  mr-10"></i><a href="${request.contextPath}">返回首页</a></li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</div>
<@script.script/>
</body>
</html>