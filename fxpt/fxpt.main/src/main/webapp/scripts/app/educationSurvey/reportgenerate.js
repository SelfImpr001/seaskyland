(function() {
	"use strict";
	var model = [ "jquery", "jqueryPager", "controller", "formToJosn",
			"editTable","../educationSurvey/MultiReportDataExecutor" ];
	define(model, function($, $page, ctrl, formToJosn, editTable,multiReport) {
		var $container = undefined;
		function list(page) {
			if (!page) {
				page = ctrl.newPage();
			}
			var url = "/academicSupervise/reportgeneratelist/"+ page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				ctrl.initUI($htmlObj);
				examSelector($htmlObj);
				ctrl.appendToView($container, $htmlObj);
				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $htmlObj.find("#pager"),
					"callBack" : search
				});
			}, "", page.data);
		}

		function examSelector($htmlObj) {
			$htmlObj.find('div.query-form a.btn:eq(0)').click(function() {
				var page = ctrl.newPage();
				search(page);
			});
		}
		
		function search(page) {
			var data = $container.find("div.query-form form").formToJson();
			page["data"] = data;
			var url = "/academicSupervise/reportgeneratelist/" + page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				$container.find("#reportList table tbody").html($htmlObj);
				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $container.find("#reportList #pager"),
					"callBack" : search
				});
			}, "", page.data);
		}

		function returnExamList($htmlObj) {
			$htmlObj.find("#returnExamList").click(function() {
				list();
			});
		}
		

		var self = undefined;
		var o = self = {
			render : function(container) {
				$container = $(container);
				list();
				$container.on("click","[trigger='monitorHref']",function(){
					var href =$(this).attr("value");// "C://Documents and Settings//Administrator//桌面//aa.doc";
					window.location.href = window.app.rootPath+"academicSupervise/erreExpor/"+ href;
				});
			}
		};
		return $.extend(o, ctrl);
	});
})();