(function() {
	"use strict";
	define([ 'jquery', 'ajax', 'formToJosn', "dialog", "controller",
			'validatejs', "./manager/manager" ], function($, ajax, form,
			dialog, ctrl,validatejs, manager) {
		var self = undefined;

		var o = self = {
			render : function(container) {
				self.getHtml('dashboard', function(html) {
					self.appendToView(container, html);
					var role = $(".page-content", container).attr("role");
					if (role === "manager") {
						manager.render(container);
					}
				});
			}
		};
		return $.extend(o, ctrl);
	});

})();