(function() {
	"use strict";
	var itemTemplate = '<div id="cntest-fxpt-queue-${fileID}" name="cntest-fxpt-queue" message=""\
		class="uploadify-queue" style="width:100%;clear:both;" overlayImport=false \
		data-fileId="${fileID}" data-fileName="${fileName}">\
		  <div class="head" style="width:100%">\
	    <div  style="float:left;width:20%;">${fileName}</div>\
	    <div id="progress" class="uploadify-progress" style="float:left;width:70%;">\
	      <div class="uploadify-progress-bar" style="width: 1%;"></div>\
	    </div>\
	    <div id="view" style="float:left;width:5%;">0%</div>\
	    <div style="float:right;width:5%;text-align:right;">\
		<a href="javascript:void(0)" name="${fileID}\">X</a></div>\
	  </div>\
	  <div class="content" style="width:100%; clear:both; margin-top:5px;"></div>\
	</div>';

	var radioTHMLForCj = '<div style="clear:both;">\
			<label style="float:left;">成绩已存在，是否要重新导入。</label>\
			<div id="myradio" style="float:left;">\
				<label class="radio-inline">\
					<input  type="radio" checked name="t" value="1">不是\
				</label>\
				<label class="radio-inline">\
					<input type="radio" name="t" value="2">是\
				</label>\
			</div>\
			<div style="clear:both;"></div>\
		</div>';
	var radioTHMLForXMB = '<div style="clear:both;">\
		<div id="myradio" style="float:left;">\
		<label style="float:left;">细目表已存在，是否要重新导入  </label>\
		<label class="radio-inline">\
		<input type="radio" checked name="t" value="1">不是\
		</label>\
		<label class="radio-inline">\
		<input type="radio" name="t" value="2">是\
		</label>\
		</div>\
		<label style="float:left;">，  重新导入该科目成绩将会被删除</label>\
		<div style="clear:both;"></div>\
		</div>';
	var importTipHtmlForCJ = '导入须知:\
		<br/>1.选择本地excel文件，按住Ctrl或Shift键可以上传多份文件。\
		<br/>2.导入文件格式必须使用导入模板格式，否则可能上传出错。 ';
	var importTipHtmlForXMB = '导入须知:\
		<br/>1.选择本地excel文件，按住Ctrl或Shift键可以上传多份文件。\
		<br/>2.导入文件格式必须使用导入模板格式，否则可能上传出错。';
		
		var fileNum =0;
	var model = [ "jquery", "ajax", "controller", "ajaxfileupload", "download", "uploadify" ];
	define(model, function($, ajax, ctrl, ajaxfileupload, download, uploadify) {
		var $dialog = undefined;
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
				callBack : undefined,
				
			};
			$.extend(opts, _opts);
			var url = "/upload/ui/" + opts.schemeType + "/m";
			
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				opts.sessionId = $htmlObj.find("#sessionId").val();
				opts.sessionName = $htmlObj.find("#sessionName").val();
				opts.fileObj = $htmlObj.find("#files");
				
				$dialog = ctrl.modal(opts.title, $htmlObj, function() {
					executor();
					return false;
				}, "导入", "lg");
				
				$("button[type='submit']").addClass("disabled");
                count=0;
				$dialog.data("opts", opts);
				initUploadButton();
				importTip();
				$htmlObj.on("click","#files-queue a",function(){
					
					var fileID = $(this).attr("name");
					var files = $("#files").uploadify('cancel', fileID);
					$('#cntest-fxpt-queue-'+fileID).remove();
					fileNum = fileNum-1;
					var name=$(this).parent().parent().parent().attr("message");
					if(name=="success"||name=="error"){
						count=count-1;
					}
					panduan();
				})
				
			});
		}

		function importTip() {
			var $tipDiv = $("<div/>", {
				"css" : {
					"float" : "left",
					"text-align" : "left"
				}
			});
			var dialogOpts = $dialog.data("opts");
			if (dialogOpts.schemeType === 2) {
				$tipDiv.html(importTipHtmlForXMB);
			}else{
				$tipDiv.html(importTipHtmlForCJ);
			}
			$(".dialog-footer", $dialog).prepend($tipDiv)
					.css("height", "130px");
		}

		function executor() {
			var errorCount=$("div[message='error']").length;
			if(errorCount>0){
				ctrl.moment("存在错误文件，请删除之后再进行导入操作","warning");
				return false;
			}
			var $files = $dialog .find(".dialog-body div[name='cntest-fxpt-queue']");
			if ($files.size() <= 0) {
				return false;
			}
			
			executorImport($files, 0);
			$dialog.find(".dialog-footer button:first").remove();
			$dialog.find(".dialog-footer button:first").text("确定");
			$dialog.find("#files-button").remove();
			return false;
		}

		function executorImport($files, idx) {

			var dialogOpts = $dialog.data("opts");
			if ($files.size() <= idx) {
				if ($.isFunction(dialogOpts.callBack)) {
					dialogOpts.callBack();
				}
				return;
			}

			var $fileObj = $($files.get(idx));
			var fileOpts = $fileObj.data("opts");

			var isHasError = fileOpts.validateResult.hasError;
			var isExistContent = fileOpts.validateResult.existContent;
			var isOverlayimport = false;

			if (isExistContent) {
				var $radioObj = $fileObj.find(".content #myradio :radio:checked");
				isOverlayimport = $radioObj.val() === "2";

				$fileObj.find(".content #myradio :radio").attr("disabled", true);
			}

			if (isHasError) {
				var $span = $("<span/>", {
					"css" : {
						"color" : "#F00",
						"display" : "block"
					}
				});
				$span.text("导入失败，因为文件有错,请看上面的文件信息排除错误后再导入。");
				$fileObj.append($span);
				executorImport($files, idx + 1);
				return;
			}
			
			if (isExistContent && !isOverlayimport && dialogOpts.importType==0) {
				var $span = $("<span/>", {
					"css" : {
						"color" : "#F00",
						"display" : "block"
					}
				});
				$span.text("导入失败，因为不进行重复导入。");
				$fileObj.append($span);
				executorImport($files, idx + 1);
				return;
			}

			var tmpFile = {};
			
			tmpFile.fileName = fileOpts.fileInfo.name;
			tmpFile.fileRealName=fileOpts.fileInfo.name;
			tmpFile.desFileName = fileOpts.fileInfo.desFileName;
			tmpFile.suffix = fileOpts.fileInfo.suffix;
			
			tmpFile.schemeType = dialogOpts.schemeType;
			tmpFile.examId = dialogOpts.examId;
			tmpFile.importType = dialogOpts.importType;
			
			tmpFile.overlayImport = isOverlayimport;

			var url = "/etl/batchexec";
			ctrl.postJson(url, tmpFile, "", function(data) {

				var color = "#000";
				if (data.processResult.hasError) {
					color = "#F00";
				}

				var $span = $("<span/>", {
					"css" : {
						"color" : color,
						"display" : "block"
					}
				});
				$span.html(data.processResult.message);

				if (data.processResult.etlLog) {
					downLoadLogs($span, dialogOpts.examId,
							data.processResult.etlLog.logContent,
							dialogOpts.schemeType);
				}
				$fileObj.find(".content").append($span);
				executorImport($files, idx + 1);
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

		function initUploadButton() {
			var opts = $dialog.data("opts");
			var setting = initSetting();
			opts.fileObj.uploadify(setting);
		}
		
		function panduan(){
			if(count==fileNum){
				$("button[type='submit']").removeClass("disabled");
			}
		}

		function initSetting() {
			var opts = $dialog.data("opts");
			var falshURL = window.app.rootPath
					+ "static/resources/uploadify/uploadify.swf";
		

			var url = window.app.rootPath + "upload/true/" + opts.examId + "/"
					+ opts.schemeType + "?" + opts.sessionName + "="
					+ opts.sessionId;
			
			return {
				swf : falshURL,
				uploader : url,
				fileObjName : "file",
				method : "post",
				auto:true,
				debug : true,
				buttonText : "选择文件上传",
				fileTypeDesc : opts.fileTypeDesc,
				fileTypeExts : opts.fileTypeExts,
				itemTemplate : itemTemplate,
				onUploadSuccess : onUploadSuccess,
				onUploadProgress : onUploadProgress
				
			
			};
		}

		function onUploadSuccess(file, data, response) {
			var myData = $.parseJSON(data);
			var $fileObj = getFileObj(file);
			setValue($fileObj, file, myData);
			var dialogOpts = $dialog.data("opts");
			showMessage($fileObj, file);
			panduan();
			//追加导入时不需要提示
			if(dialogOpts.importType!=1){
				validateExistContent($fileObj, file);
			}
			

		}
//		window.onscroll=function(){
//			var divw=document.getElementById("myradio");
//			divw.style.top=document.getElementById("files-queue").scrollTop;
//			divw.style.left=document.getElementById("files-queue").scrollLeft+600;
//		}
		var count=0;
		function showMessage($fileObj, file) {
			setRoal();
			$fileObj.find(".content").html("");
			var opts = $fileObj.data("opts");
			var messages = opts.validateResult.messages;
			$.each(messages, function() {
				var $span = $("<span/>", {
					"css" : {
						"display" : "block"
					}
				});
				$span.text(this);
				$fileObj.find(".content").append($span);
				$("#cntest-fxpt-queue-"+file.id).attr("message","error");
				count++;
			});
			if (messages.length === 0) {
				var $span = $("<span/>", {
					"css" : {
						"display" : "block"
					}
				});
				$span.text("上传成功");
				$fileObj.find(".content").append($span);
				$("#cntest-fxpt-queue-"+file.id).attr("message","success");
				count++;
			}
			$("#view", $fileObj).html("100%");		
		}
		//设置滚动条
		function setRoal(){
			//获取浏览器的高度，宽度
			var divS =document.getElementById("files-queue");
			var divM =document.getElementById("dialogId");
			var heightDivs=divS.offsetHeight;
			if(heightDivs>200){
				divM.style.height="400px";
				divS.style.height="320px";
			}else{
				var maxheight = heightDivs+100;
				divM.style.height=maxheight+"px";
			}
			divS.style.position="absolute";
			divS.style.width="92%";
			divS.style.overflow="auto";
		}
		function validateExistContent($fileObj, file) {
			var opts = $fileObj.data("opts");
			if (opts.validateResult.existContent) {
				var $radioTHML = getRadioHTML();
				$radioTHML.find(":radio").attr("name", file.id);
				$fileObj.find(".content").append($radioTHML);
			}
		}

		function getRadioHTML() {
			var dialogOpts = $dialog.data("opts");
			var $radioTHML = $(radioTHMLForCj);
			if (dialogOpts.schemeType === 2) {
				$radioTHML = $(radioTHMLForXMB);
			}
			return $radioTHML;
		}

		function setFileObjAttr(file, name, value) {
			var $fileObj = getFileObj(file);
			$fileObj.attr(name, value);
		}

		function getFileObj(file) {
			return $dialog.find('#cntest-fxpt-queue-' + file.id);
		}

		function setValue($fileObj, file, data) {
			var opts = $fileObj.data("opts");
			if (!opts) {
				opts = {
					fileInfo : undefined,
					fileContent : undefined,
					validateResult : undefined
				};
				$fileObj.data("opts", opts);
			}

			if (data.fileInfo) {
				opts.fileInfo = data.fileInfo;
			}
			if (data.validateResult) {
				opts.validateResult = data.validateResult;
			}
			if (data.fileContent) {
				opts.fileContent = data.fileContent;
			}
		}
		
		function onUploadProgress(file, fileBytesLoaded, fileTotalBytes) {
			fileNum =$("#files-queue").children().size();
			var percent = parseInt(fileBytesLoaded / fileTotalBytes * 100)
					+ "%";
			var viewPercent = percent;
			if (percent === 100) {
				viewPercent = 99;
			}
			var $fileHTML = $dialog.find("#cntest-fxpt-queue-" + file.id);

			$fileHTML.find(".uploadify-progress-bar").css("width", percent);
			$fileHTML.find("#view").html(viewPercent);
			$("button[type='submit']").addClass("disabled");
			$fileHTML.find(".content").html("正在上传...");
			
			
		}
		


		var o = {
			UI : function(opts) {
				UI(opts);
			}
		};
		return o;
	});

})();