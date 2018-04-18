(function() {
	"use strict";
	var model = [ "jquery", "jqueryPager", "controller", "formToJosn",
			"editTable","../educationSurvey/MultiReportDataExecutor" ];
	define(model, function($, $page, ctrl, formToJosn, editTable,multiReport) {
		var $container = undefined;
		
		//新增身份验证---结果导出
		function  addExamValidate($htmlObj){
	        		multiReport.UI({
	        			title : "批量导入",
	        			fileTypeDesc : "只能选择zip文件",
	        			fileTypeExts : "*.zip",
	        			container :$container,
	        			self:self,
	        			listContainer: $htmlObj
	        		});
		}
		
		
		function addReportData($htmlObj){
			$htmlObj.on('click','#addReportData',function(){
    			var monitorname=$htmlObj.find("#monitorId").val();
    			var monitorHref=$htmlObj.find("#monitorhref").val();
    		
    			var fsdn = {
						monitorName : monitorname, 
						monitorHref : monitorHref ,
					};
    			ctrl.postJson("academicSupervise/addReportData",fsdn,"考试数据上传成功!",function(){
      			   
      		     }); 
			});
		}

		var self = undefined;
		var o = self = {
			render : function(container) {
				$container = $(container);
				 var url = ctrl.getCurMenu().attr("url");
		     	   ctrl.getHtml(url,function(html) {
		     		  var $htmlObj=$(html);
		     	      ctrl.appendToView(self.container,$htmlObj);
		     	      addExamValidate($htmlObj);  //加载导入插件
		     	      addReportData($htmlObj);
		     	   });
			}
		};
		return $.extend(o, ctrl);
	});
})();