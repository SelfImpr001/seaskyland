function getUrlFileName() {
	var pathname = window.location.pathname;
	if (pathname == '' || pathname == '/')
		return 'index';
	var pathArr = pathname.split("/");
	var fileName = (pathArr[pathArr.length - 1] == '') ? 'index'
			: pathArr[pathArr.length - 1].split(".")[0];
	return fileName;
}

// 定义appName
window['cntest'] = cntest = {
	version : '1.0'
};

if (!window.app) {
	// 如果没有设置 window.app
	var includeEntry = document.body.getAttribute('entry');
	if (includeEntry != null) {
		// 如果body内有设置entry属性
		var includeRootPath = document.body.getAttribute('rootPath');
		if (includeRootPath != null) {
			window.app = {
				rootPath : includeRootPath,
				entry : includeEntry
			};
		} else {
			window.app = {
				rootPath : "",
				entry : includeEntry
			};
		}
	} else {
		// 如果两个都没有设置则自动加载 与网址后缀名相同的js
		window.app = {
			rootPath : "",
			entry : getUrlFileName()
		};
	}
}

requirejs.config({
	contextPath : window.app.rootPath,
	baseUrl : window.app.rootPath + "static/scripts/",
	optimize : "none",

	paths : {
		"jquery" : "lib/jquery",
		"jquery-ui" : "lib/jquery-ui",
		"jqueryui" : "common/jquery-ui",
		"cookie" : "lib/jquery.cookie",
		"bootstrapjs" : "lib/bootstrap/bootstrap.min",
		"jq-uijs" : "lib/jquery-ui-1.9.2.custom.min",
		"lefttree" : "common/lefttree",
		"datepickerjs" : "lib/bootstrap/bootstrap-datetimepicker",
		"validatejs" : "lib/jquery.validate",
		"formToJosn" : "common/formToJosn",
		"jsonString" : "common/samrtBiToJsonString",
		"logger" : "common/logger",
		"ztreecore" : "lib/jquery.ztree.core",
		"ztreecheck" : "lib/jquery.ztree.excheck",
		"ztreeexedit" : "lib/jquery.ztree.exedit",
		"selectjs" : "lib/bootstrap/bootstrap-select",
		"validate" : "lib/jquery.validate",
		'scrollbar' : 'lib/jquery.mCustomScrollbar.concat.min',
		"ajax" : "common/ajax-constom",
		"ajaxmodal" : "common/ajaxmodal",
		'Tabs' : 'common/Tabs',
		"validate" : "lib/jquery.validate",
		"confirm" : "common/confirm",
		"editTable" : "common/editTable",
		"common" : "common/common",
		"ajaxfileupload" : "common/ajaxfileupload",
		"dialog" : "common/dialog",
		"basefn" : "common/basefn",
		"controller" : "common/controller",
		"controllers" : "common/controllers",
		"jqueryPager" : "lib/jquery.pager",
		"radioztree" : "common/radioztree",
		"ztreebox" : "common/ztreebox",
		"observer" : "common/Observer",
		"stringUtil" : "common/stringUtil",
		"download" : "common/jQuery.download",
		"strBuf" : "common/StringBuffer",
		"uploadify" : "lib/jquery.uploadify.min",
		"base64" : "common/jquery.base64",
		"plupload":"lib/plupload-3.0/js/plupload",
		"plqueue":"lib/plupload-3.0/js/jquery.plupload.queue/jquery.plupload.queue",
		"webupload":"lib/webuploader-0.1.5/webuploader"
	},
	shim : {
		'bootstrapjs' : {
			deps : [ 'jquery' ]
		},
		'lefttree' : {
			deps : [ 'jquery' ]
		},
		"datepickerjs" : {
			deps : [ 'jquery' ]
		},
		"ztreecheck" : {
			deps : [ 'ztreecore' ]
		}
	}
});

// 两次require,确保公共方法加载完成后才加入模块
require([ 'jquery', 'bootstrapjs' ], function() {
	require([ 'jquery', 'bootstrapjs' ], function() {
		window.app["pageSize"] = 15;
		window.dev = true;
		var p = [ 'app/' + window.app.entry ];
		if (!window.JSON) {// IE系列浏览器不支持JSON，使用JSON2
			p[1] = "util/json2";
		}
		require(p, function(module) {
			if (module) {
				module.render();
			}

		});
	});
});
