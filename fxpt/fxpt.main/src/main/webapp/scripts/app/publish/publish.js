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
			var url = "/publish/list/" + page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				ctrl.initUI($htmlObj);
				examSelector($htmlObj);
				publish($htmlObj);
				publishCancel($htmlObj);
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
			var url = "/publish/list/" + page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				publish($htmlObj);
				publishCancel($htmlObj);
				$container.find("#examList table tbody").html($htmlObj);
				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $container.find("#examList #pager"),
					"callBack" : search
				});

			}, "", page.data);
		}
		//发布
		function publish($htmlObj){
			$htmlObj.find(".examPublish").click(function() {
				var examId = $(this).attr("objectId");
				ctrl.postJson("publish/update/"+examId,"","发布成功!",function(data){
					list();
     		    });   
			});
		}
		//取消发布
		function publishCancel($htmlObj){
			$htmlObj.find(".examCancelPublish").click(function() {
				var examId = $(this).attr("objectId");
				ctrl.postJson("publish/resetUpdate/"+examId,"","取消发布成功!",function(data){
					list();
     		    });   
			});
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
			}
		};
		return $.extend(o, ctrl);
	});
})();