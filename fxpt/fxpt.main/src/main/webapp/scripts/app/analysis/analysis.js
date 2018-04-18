(function() {
	"use strict";
	var model = [ "jquery", "jqueryPager", "controller", "formToJosn",
			"editTable" ];
	define(model, function($, $page, ctrl, formToJosn, editTable) {
		var $container = undefined;
		function list(page) {
			if (!page) {
				page = ctrl.newPage();
			}
			var url = "/analysis/list/" + page.curSize + "/" + page.pageSize;
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
				analysisExam();
				progress();
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
			var url = "/analysis/list/" + page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				$container.find("#examList table tbody").html($htmlObj);
				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $container.find("#examList #pager"),
					"callBack" : search
				});
				progress();
			}, "", page.data);
		}

		function returnExamList($htmlObj) {
			$htmlObj.find("#returnExamList").click(function() {
				list();
			});
		}

		function analysisExam() {
			var $tbody = $("#examList table tbody", $container);
			$tbody.on("click", "tr .analysisExam", function() {
				var examId = $(this).attr("objectId");
				var url = "/analysis/" + examId;
				ctrl.postJson(url, {}, "", function(data) {
					$("#examList #pager", $container).clickCurPage();
				});
			});
		}

		function progress() {
			/** 测试进度条* */
			var $tbody = $("#examList table tbody", $container);
			$("div.progress-warp", $tbody).each(function(i, n) {
				var $c = $(n);
				var examId = $c.attr('examId');
				ctrl.ajaxExecute('IAanalysisService', {
					examId : examId
				}, 'html', function() {
					$("#examList #pager", $container).clickCurPage();
				}, $c);
			});
		}

		var self = undefined;
		var o = self = {
			render : function(container) {
				$container = $(container);
				list();
			}
		};
		return $.extend(o, ctrl);
	});
})();