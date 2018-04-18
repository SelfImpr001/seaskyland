(function() {
	"use strict";

	define([ 'jquery', 'logger' ], function($, log) {
		$.extend({
			download : function(_opts) {
				var opts = {
					url : undefined,
					data : []
				};
				
				if($.isPlainObject(_opts)){
					$.extend(opts, _opts);
				}else{
					opts.url=_opts;
				}
				if (!opts.url) {
					return;
				}

				function createFrame() {
					var id=new Date().getTime();
					var $frame = $("<iframe name='iframe"+id+"' style='display:none'  src=\"#\">" +
									"</iframe>");
					$("body").append($frame);
					return $frame;
				}

				function createForm($fram) {
					if($("#downloadForm").size()>0){
						$("#downloadForm").remove();
					}
					
					
					var $form = $("<form method='post'></form>");
					var framName = $fram.attr("name");
					$form.attr("action", opts.url);
					$form.attr("target", framName);
					if ($.isArray(opts.data)) {
						$.each(opts.data, function() {
							if (!$.isEmptyObject(this)) {
								$form.append("<input type='hidden' value='"
										+ this.value + "' name='" + this.name
										+ "'>");
							}
						});
					}
					$("body").append($form);
					return $form;
				}
				
				var $fram = createFrame();
				var $form = createForm($fram);
				$form.submit();
				$form.remove();

				
//				setTimeout(function(){
//					$form.remove();
//					$fram.remove();
//				}, 3000);
				
			}
		});
	});
})();