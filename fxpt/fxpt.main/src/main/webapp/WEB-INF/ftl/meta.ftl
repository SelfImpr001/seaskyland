<#macro meta title="">
	<meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />	
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="renderer" content="webkit">
	<title>${title}</title>
    <!-- basic styles -->
    <link href="${request.contextPath}/static/resources/css/bootstrap.min.css" rel="stylesheet" />
    <link href="${request.contextPath}/static/resources/css/Pager.css" rel="stylesheet" />
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/font-awesome.min.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/bootstrap-datetimepicker.css"/>
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/bootstrap-select.css"/>
    <link rel="stylesheet" href="${request.contextPath}/static/resources/uploadify/uploadify.css"/>
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/editTable.css"/>
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/manager.css"/>
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/jquery-ui.css"/>
    
    <!--[if IE 7]>
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/font-awesome-ie7.min.css"/>
    <![endif]-->

    <!-- cntest styles -->
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/cntest.css" />
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/label-page.css" />
    <!--左侧导航样式-->
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/nav-list.css" />

    
    <!--权限树样式-->
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/zTreeStyle.css" />
    <!--滚动条-->
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/jquery.mCustomScrollbar.css">
    <!--[if lte IE 8]>
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/cntest-ie.min.css"/>
    <![endif]-->

    <!--[if lt IE 9]>
    <script src="${request.contextPath}/static/scripts/lib/bootstrap/html5shiv.js"></script>
    <script src="${request.contextPath}/static/scripts/lib/bootstrap/respond.min.js"></script>
    <![endif]-->
    <!--[if IE 8]>
    <style>
	.lbl {
	    display :none!important;
	}
	span.lbl{
	    display:none !important;
	    position:absolute;
	    left:-20px;
	    top:-2px;	    
	}
	input[type=checkbox].ace,input[type=radio].ace {
		-ms-filter:"progid:DXImageTransform.Microsoft.Alpha(opacity: 100)";
		filter:alpha:opacity(90);
	}
    </style>
    <![endif]-->
     <style>
    	@media all and (-ms-high-contrast:none),(-ms-high-contrast:active){
    		input[type=checkbox].ace,input[type=radio].ace {
	opacity: 1;
	position: static;
	z-index: auto;
	width: auto;
	height: auto;
	cursor: pointer;
    	}
    </style>
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/test.css"/>
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/skin.css"/>
    <link rel="stylesheet" href="${request.contextPath}/static/resources/css/blue-modular.css"/>
    <link rel="stylesheet" href="${request.contextPath}/static/scripts/lib/webuploader-0.1.5/webuploader.css"/>
</#macro>

