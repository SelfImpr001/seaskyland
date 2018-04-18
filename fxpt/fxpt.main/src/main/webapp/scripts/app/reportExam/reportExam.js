(function() {
	"use strict";
	define(
			[ 'jquery', 'ajax', 'formToJosn', "dialog", "controller",'validatejs',"app/reportExam/MultiReportDataExecutor"],
			function($, ajax, form, dialog, ctrl,multiReport) {
				var self = undefined;
				function viewReport() {
						var examId = $.attr("examId");
						recentlyExamList();
						window.location = "report/home/" + examId;
				}
				
				function recentlyExamList() {
					ajax({
						target: $("#main-content"),
						
						url : "/reportExam/recentlyList",
						async : true,
						dataType : "html",
						callback : function(htmlStr) {
							$("#main-content").html(htmlStr);
							viewReport();
						}
					});
				}
				


				
				function bindEvent() {
					var $obj = self.container;
					$obj.on('click',".viewReport",function(){
						viewReport();
						recentlyExamList();
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