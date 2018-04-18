(function() {
	"use strict";
	var model = [ "jquery", "download", "controller" ];
	define(model, function($, download, ctrl) {

		var self = undefined;
		var o = self = {
			downloadLogs : function($logTable, examId, logType) {
				$logTable.find("tbody .downloadLog").click(
						function() {
							var fileName = $(this).attr("data-filename");
							var url = window.app.rootPath + "download/log/"
									+ examId + "?logFile=" + fileName
									+ "&logType=" + logType;
							$.download(encodeURI(url));
						});
			},
			list : function($logTable, examId, logType) {
				var url = "/logs/list/" + examId + "/" + logType + "/1/10";
				ctrl.getHtml(url, function(html) {
					var $htmlObj = $(html);
					$logTable.find("tbody").html($htmlObj);
					self.downloadLogs($logTable, examId, logType);
				}, "", {});
			}
		};
		return o;
	});

})();