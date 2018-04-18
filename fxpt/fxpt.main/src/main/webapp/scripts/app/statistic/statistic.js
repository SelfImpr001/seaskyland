(function() {
	"use strict";
	define(
			[ 'jquery', 'ajax', 'formToJosn', "dialog", "controller",
					'validatejs',"ajaxfileupload" ],
			function($, ajax, form, dialog, ctrl,ajaxfileupload) {
				var self = undefined;
			
				
				function save(html) {
					var single = html.find("input[name=single]:checked").val();
					var multi = html.find("input[name=multi]:checked").val();
					ctrl.getHtml(("statisticSetting/update/"+single+"/"+multi),function(html){
						self.render();
					},"设置成功");       
				}

			
				function bindEvent() {
					var $obj = self.container;
					$obj.on('click','#saveSetting',function() {
								save($obj);
							});
			
				}

				var o = self = {
					container:undefined,
					render : function(container) {
						var url = self.getCurMenu().attr("url");
						self.log("get url " + url);
						self.getHtml(url,function(html) {
							self.container = $(html);
			        	    ctrl.appendToView(container,self.container);
							bindEvent();
						});
					}
				};
				return $.extend(o, ctrl);
			});

})();