(function() {
	"use strict";

	define([ 'ajax' ], function(ajax) {

		function explain() {
			ajax({
				url : "/static/help/explain.html",
				async : true,
				dataType : "html",
				callback : function(html) {
					$("#main-content").html(html);
				}
			});
		}
		
		function onlinehelp() {
			ajax({
				url : "/static/help/onlinehelp.html",
				async : true,
				dataType : "html",
				callback : function(html) {
					$("#main-content").html(html);
				}
			});
		}

		var o = {
			explain : function() {
				explain();
			},
			onlinehelp : function() {
				onlinehelp();
			}
		};

		return o;

	});

})();