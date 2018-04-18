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
			var url = "academicSupervise/analysislist/1/15";
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				ctrl.initUI($htmlObj);
				ctrl.appendToView($container, $htmlObj);
				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $htmlObj.find("#pager")
				});
			}, "", page.data);
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