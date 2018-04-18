(function() {
	"use strict";
	var model = [ "jquery", "download", "controller" ];
	define(model, function($, download, ctrl) {
		function setting() {
			$("#settingLog").click(function() {
				var list = new Array();
				$("select").each(function() {
					var $this = $(this);
					var name = $this.attr("logName");
					var value = $this.val();

					list.push({
						"name" : name,
						"value" : value
					});
				});

				ctrl.postJson("/log/setting", list, "设置成功!", function(data) {
					ctrl.moment("设置成功!", "success");
				});

			});

		}

		var self = undefined;
		var o = self = {
			render : function() {
				setting();
			}
		};
		return o;
	});

})();