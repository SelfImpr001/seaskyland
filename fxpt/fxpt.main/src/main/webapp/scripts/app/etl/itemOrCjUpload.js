(function() {
	"use strict";
	var model = [ "jquery", "ajax", "controller","plupload"];
	define(model, function($, ajax, ctrl) {
		function UI(_opts) {
			var opts = {
				title : "",
				examId : undefined,
				importType : undefined,
				schemeType : 1,
				fileTypeDesc : "只能选择excle和dbf文件",
				fileTypeExts : "*.xls;*.xlsx;*dbf",
				sessionId : undefined,
				sessionName : undefined,
				callBack : undefined
			};
			$.extend(opts, _opts);
			var url = "/upload/ui/" + _opts.schemeType + "/m";
			var plupload = undefined;
			ctrl.getHtml(url, function(html) {
				var $html = $(html);
				var $dialog = ctrl.modal(_opts.title, $html, function() {
					
					var hasError = false;
					$.each(plupload.files,function(i,file){
						if(file.serviceData.validateResult.hasError){
							hasError=true;
						}
					});
					if(!hasError){
						$dialog.find(".dialog-footer button:first").remove();
						$dialog.find(".dialog-footer button:first").text("确定");
						$dialog.find("div.plupload_buttons").remove();
						$.each(plupload.files,function(i,file){
							var li = $dialog.find("ul li#"+file.id);
							var span = $dialog.find("ul li#"+file.id).find("div.content span");
							var isOverlayimport = false;
							var importType = _opts.importType;
							var isExistContent = file.serviceData.validateResult.existContent;
							var isHasError = file.serviceData.validateResult.hasError;
							
							if (isExistContent) {
								var $radioObj = li.find("#myradio :radio:checked");
								isOverlayimport = $radioObj.val() === "2";
								li.find("#myradio :radio").attr("disabled", true);
							}
							if (isHasError) {
								li.text("导入失败，因为文件有错,请看上面的文件信息排除错误后再导入。");
								return;
							}
							if (isExistContent && !isOverlayimport && importType==0) {
								$span.text("导入失败，因为不能进行重复导入。");
								$fileObj.append($span);
								executorImport($files, idx + 1);
								return;
							}
							
							var tmpFile = {};
							tmpFile.fileName = file.serviceData.fileInfo.name;
							tmpFile.fileRealName=file.serviceData.fileInfo.name;
							tmpFile.desFileName = file.serviceData.fileInfo.desFileName;
							tmpFile.suffix = file.serviceData.fileInfo.suffix;
							tmpFile.schemeType = _opts.schemeType;
							tmpFile.examId = _opts.examId;
							tmpFile.importType = _opts.importType;
							tmpFile.overlayImport = isOverlayimport;
							var url = "/etl/batchexec";
							ctrl.postJson(url, tmpFile, "", function(data) {
								span.html(data.processResult.message);
								if (data.processResult.etlLog) {
									downLoadLogs(span, _opts.examId, data.processResult.etlLog.logContent, _opts.schemeType);
								}
								_opts.callBack();
							});
						});
					}else{
						ctrl.moment("存在错误文件，请删除之后再进行导入操作","warning");
						return false;
					}
					
				}, "导入", "lg");
				
				
				var sessionId = $dialog.find("#sessionId").val();
				var sessionName = $dialog.find("#sessionName").val();
				var url = window.app.rootPath + "upload/true/" +_opts.examId + "/" + _opts.schemeType + "?" + sessionName + "=" + sessionId;
				plupload = $dialog.find("#filelist").pluploadQueue({
					container_:$dialog,
					url : url,
					chunk_size : '0',
					unique_names : true,
					multiple_queues:true,
					file_data_name:"file",//服务器接收参数的名字
					auto_start:true,
					prevent_duplicates:true,//不允许队列中存在重复的
					filters : {
						max_file_size : '200000mb',
						mime_types: [
							{title : "只能选择excl", extensions : "xls"},
							{title : "只能选择excl", extensions : "xlsx"}
						],
						prevent_duplicates:false
					},
					multi_selection:true,//false:单个文件上传，true:多个文件上传
					disableBrowse:false,//可以多次点击上传
					resize : {width : 320, height : 240, quality : 90},
					flash_swf_url : '/fxpt/static/scripts/lib/plupload-3.0/js/Moxie.swf',
					silverlight_xap_url : '/fxpt/static/scripts/lib/plupload-3.0/js/Moxie.xap',
				});
			});
		}
		function downLoadLogs($span, examId, logFile, logType) {
			$span.find("a.logFileDown").click(
					function() {
						var url = window.app.rootPath + "download/log/"
								+ examId + "?logFile=" + logFile + "&logType="
								+ logType;
						$.download(url);
					});
		}
		var o = {
			UI : function(opts) {
				UI(opts);
			}
		};
		return o;
	});

})();