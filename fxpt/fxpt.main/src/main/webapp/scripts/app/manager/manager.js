(function() {
	"use strict";
	var model = [ 'jquery' ];
	define(model, function($) {
		var $container = undefined;
		
		var o = {
			render : function(container) {
				$container = container;
			}
		};
		return o;
	});

})();