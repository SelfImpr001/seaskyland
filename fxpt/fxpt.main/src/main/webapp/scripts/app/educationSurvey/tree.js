(function() {
	"use strict";
	define([ 'jquery' ], function($) {
		// 树形菜单
		function domtree(obj) {
			// obj为传入的jq对象
			var $obj = obj.parent();

			if ($obj.hasClass('active')) {

				$obj.find('.icon-folder-open').attr('class',
						'icon-folder-close');

				$obj.removeClass('active').find('li').removeClass('active')
			} else {
				// $('this > a span').attr('class','glyphicon
				// icon-folder-open');
				obj.find('.icon-folder-close')
						.attr('class', 'icon-folder-open');

				$obj.addClass('active')
			}
		};

//		function click(){
//			$(".node").click(function() {
//				alert(1);
//        	        location.href = window.app.rootPath+"/organization/list"+$(this).data("id");
//        	});
//		}
		var o = {
			render : function() {
				$('.nav-box a').bind('click',function(){
					domtree( $(this) );
				});	
//				click();
			}
		};
		return o;
	});

})();