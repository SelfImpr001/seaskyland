(function() {
	"use strict";

	var itemTemplateMessage = "<span>该工作簿的内容不符合细目表模板，请<a href='#'>下载细目表模板</a>与该文件进行对照;或者选择另外一个工作簿.</span>";

	var model = [ "jquery","ajax", "controller", "ajaxfileupload", "download" ];
	define(model, function($, ajax, ctrl, ajaxfileupload, download) {

		var $dialog = undefined;
		function sUI(_opts) {
			var opts = {
				title : "",
				examId : undefined,
				importType : undefined,
				schemeType : 1,
				effectiveFile : [ ".xls", ".xlsx", ".dbf" ],
				effectiveFileErrorMesg : "文件只能为dbf和excel文件",
				fileInfo : undefined,
				fileContent : undefined,
				validateResult : undefined,
				callBack : undefined
			};
			$.extend(opts, _opts);
			var url = "/upload/ui/" + opts.schemeType + "/s?testPaperId=0";
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);

				// showKgStrTable();
				$dialog = ctrl.modal(opts.title, $htmlObj, function() {
					executor();
					return false;
				}, "导入", "lg");
				$dialog.data("opts", opts);
				uploadFile();
				showKgStrTable();
			
			});
		}

		function showKgStrTable() {
			if (isImportCj()) {
				$dialog.find("#kgStrTable").show();
				changeKgOmrStr();
				chengeKgScoreStr();
			}
		}

		function changeKgOmrStr() {
			$dialog.find("#kgOmrStr").change(
					function() {
						var selFieldName = $(this).val();
						var expression = ".etl-field[isSelItem='true']"
								+ "[isSelOption='true'][defaultName='']";
						var $fieldObj = $(expression);
						var kgItemCount = $fieldObj.size();
						var kgItemCountOption = new Array();
						if (selFieldName != "") {
							for (var i = 1; i <= kgItemCount; i++) {
								kgItemCountOption.push(" <option value='"
										+ selFieldName + i + "' asValue='"
										+ selFieldName + i
										+ "' isOmrSplit='true'>" + selFieldName
										+ i + "</option>");
							}
						}
						$dialog.find(".etlSelect").each(function() {
							$(this).find("[isOmrSplit='true']").remove();
							$(this).append(kgItemCountOption.join(""));
						});

						$fieldObj.each(function(index) {
							var fieldName = $(this).attr("fieldName");
							var $fieldSel = $("#" + fieldName);
							var selValue = selFieldName;
							if (selFieldName != "") {
								selValue += (index + 1);
							}
							$fieldSel.val(selValue);
						});
					});
		}

		function chengeKgScoreStr() {
			$dialog.find("#kgScoreStr").change(
					function() {
						var selFieldName = $(this).val();
						var expression = $(".etl-field[isSelItem='true']"
								+ "[isSelOption='false'][defaultName='']");
						var $fieldObj = $(expression);
						var kgItemCount = $fieldObj.size();
						var kgItemCountOption = new Array();
						if (selFieldName != "") {
							for (var i = 1; i <= kgItemCount; i++) {
								kgItemCountOption.push(" <option value='"
										+ selFieldName + i + "' asValue='"
										+ selFieldName + i
										+ "' isScoreSplit='true'>"
										+ selFieldName + i + "</option>");
							}
						}

						$dialog.find(".etlSelect").each(function() {
							$(this).find("[isScoreSplit='true']").remove();
							$(this).append(kgItemCountOption.join(""));
						});

						$fieldObj.each(function(index) {
							var fieldName = $(this).attr("fieldName");
							var $fieldSel = $("#" + fieldName);
							var selValue = selFieldName;
							if (selFieldName != "") {
								selValue += (index + 1);
							}
							$fieldSel.val(selValue);
						});

					});
		}

		function uploadFile() {
			$dialog.find("#uploadFile").click(function() {
				var suffixName = uploadFileSuffix();
				if (!validateFile(suffixName)) {
					return false;
				}
				var $overlay = ctrl.tips("正在上传文件，请不要关闭和刷新浏览器");
				var uploadSetting = getFileUploadSetting();
				uploadSetting.success = uploadSuccessFun($overlay);
				$.ajaxFileUpload(uploadSetting);
			});
		}

		function getFileUploadSetting() {
			var opts = $dialog.data("opts");
			var url = window.app.rootPath + "upload/false/" + opts.examId + "/"
					+ opts.schemeType;
			return {
				url : url,
				secureuri : false,
				fileElementId : "file",
				dataType : "json"
			};
		}

		function uploadSuccessFun($overlay) {
			return function(data) {
				$overlay.fadeOut(function() {
					$(this).remove();
				});
				setValue(data);
				createSheetSelect();
				checkFile();
				createFieldSelect();
			}
		}

		function createFieldSelect() {
			var opts = $dialog.data("opts");
			var fields = opts.fileContent.head;
			var fieldOptionHTML = new Array();
			fieldOptionHTML.push("<option value=''>==选择字段==</option>");
			for ( var i in fields) {
				fieldOptionHTML.push(" <option value='" + fields[i]
						+ "' asValue='" + fields[i].toLowerCase() + "'>"
						+ fields[i] + "</option>");
			}
			var fieldOptionHTMLStr = fieldOptionHTML.join("");
			$(".etlSelect").each(function() {
				setField($(this), fieldOptionHTMLStr);
			});

			if (isImportCj()) {
				setKg(fieldOptionHTMLStr);
			}
		}

		function setKg(fieldOptionHTMLStr) {
			$(".kgStr").each(
					function() {
						$(this).html(fieldOptionHTMLStr);
						var fieldName = $(this).attr("id");
						var defaultName = $(
								"span[fieldName='" + fieldName + "']").attr(
								"defaultName");
						var defaultNames = defaultName.split("|");
						for ( var i in defaultNames) {
							var options = $(this).find(
									"option[asValue='"
											+ defaultNames[i].toLowerCase()
											+ "']");
							if (options.size() > 0) {
								var value = options.attr("value");
								$(this).val(value);
								$(this).change();
							}
						}
					});
		}

		function isImportCj() {
			var opts = $dialog.data("opts");
			return 3 === opts.schemeType;
		}

		function setField(filedObj, fieldOptionHTMLStr) {
			filedObj.html(fieldOptionHTMLStr);
			var fieldName = filedObj.attr("id");
			var defaultName = $("span[fieldName='" + fieldName + "']").attr(
					"defaultName");
			var defaultNames = defaultName.split("|");
			for ( var i in defaultNames) {
				var options = filedObj.find("option[asValue='"
						+ defaultNames[i].toLowerCase() + "']");
				if (options.size() > 0) {
					options.attr("selected", "true");
				}
			}
		}

		function checkFile() {
			var opts = $dialog.data("opts");
			var $message = $("<span></span>");
			if (opts.schemeType == 2 && !opts.validateResult.templateFile) {
				$message = $(itemTemplateMessage);
				downloadTemplate($message);
			}
			$dialog.find("#etl-sheetMessage").html($message);
		}

		function downloadTemplate($message) {
			$message.find("a").click(function() {
				var url = window.app.rootPath + "download/xmb";
				$.download(url);
			});
		}

		function createSheetSelect() {
			if (!isExcel()) {
				return;
			}
			var opts = $dialog.data("opts");
			$dialog.find("#sheetNamesTr").hide();
			var sheetNames = opts.fileContent.sheetNames;
			var sheetNameOptionHTML = new Array();
			for ( var i in sheetNames) {
				var html = "<option value='" + sheetNames[i] + "'>"
						+ sheetNames[i] + "</option>";
				sheetNameOptionHTML.push(html);
			}
			$dialog.find("#sheetNames").html(sheetNameOptionHTML.join(""));
			$dialog.find("#sheetNamesTr").show();
			
			//显示上传文件名称
			var str=opts.fileInfo.name;
			$("#fileName").html(str);
			
			// 改变excle的工作簿
			changeSheetName();
		}

		function changeSheetName() {
			var opts = $dialog.data("opts");
			$dialog.find("#sheetNames").change(function() {
				var $sheetObj = $(this);
				$sheetObj.attr("disabled", true);
				var parameter = {
					"fileName" : opts.fileInfo.name,
					"desFileName" : opts.fileInfo.desFileName,
					"suffix" : opts.fileInfo.suffix,
					"sheetName" : $sheetObj.val(),
					"schemeType" : opts.schemeType,
					"examId" : opts.examId,
					"isValidataHead" : false
				};
				var url = "/upload/changeSheet";
				ctrl.postJson(url, parameter, "", function(data) {
					setValue(data);
					checkFile();
					createFieldSelect();
					$sheetObj.attr("disabled", false);
				});
			});
		}

		function isExcel() {
			var opts = $dialog.data("opts");
			return opts.fileInfo.suffix.toLowerCase() === ".xls"
					|| opts.fileInfo.suffix.toLowerCase() === ".xlsx";
		}

		function setValue(data) {
			var opts = $dialog.data("opts");
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

		function uploadFileSuffix() {
			var filePath = $("#file").val();
			var suffixName = filePath.substring(filePath.lastIndexOf("."),
					filePath.length);
			return suffixName;
		}

		function validateFile(suffixName) {
			var opts = $dialog.data("opts");
			var effectiveFile = opts.effectiveFile;
			var isOk = false;
			$.each(effectiveFile, function() {
				if (this === suffixName.toLowerCase()) {
					isOk = true;
					return false;
				}
			});
			if (!isOk) {
				ctrl.alert(opts.effectiveFileErrorMesg);
			}
			return isOk;
		}

		function executor() {
			var opts = $dialog.data("opts");
			var isTemplateFile = opts.validateResult.templateFile;
			if (opts.schemeType === 2 && !isTemplateFile) {
				ctrl.alert("您选择的文件不符合模板!");
				return false;
			}

			var errorField = new Array();
			$dialog.find(".etl-field[isNeed='true']").each(function() {
				var fieldName = $(this).attr("fieldName");
				var mappingValue = $dialog.find("#" + fieldName).val();
				if (!mappingValue || mappingValue === "") {
					var asName = $(this).attr("asName");
					errorField.push(asName);
				}
			});

			if (errorField.length > 0) {
				var messageStr = errorField.join("未选择,必须选择字段<br/>") + "必须导入";
				ctrl.alert(messageStr);
				return false;
			}

			var isExistContent = opts.validateResult.existContent;
			if (isExistContent && opts.importType!=1) {
				//ctrl.confirm("", getExistContentMessage(), function() {
					createObjAndSubmit(true);
				//});
				return false;
			}
			createObjAndSubmit(false);
		}

		function getExistContentMessage() {
			var opts = $dialog.data("opts");
			var result = "";
			if(opts.importType==1){
				if (opts.schemeType === 1) {
					result = "您确定多文件导入吗？这样会覆盖已导入学校的学生信息！";
				} else if (opts.schemeType === 2) {
					result = "双向细目表已存在，您确定要覆盖重新导入?这样会删除该试卷的成绩。";
				}
			}else{
				if (opts.schemeType === 1) {
					result = "报名库已存在，您确定要覆盖重新导入?这样会删除所有的成绩。";
				} else if (opts.schemeType === 2) {
					result = "双向细目表已存在，您确定要覆盖重新导入?这样会删除该试卷的成绩。";
				}
			}
			
			return result;
		}
		///单个上传双向细目表方法
		function createObjAndSubmit(isOverlayImport) {
			var opts = $dialog.data("opts");

			var fieldRelation = new Array();
			$dialog.find(".etl-field").each(function() {
				var fieldName = $(this).attr("fieldName");
				var mappingValue = $dialog.find("#" + fieldName).val();
				if (mappingValue != "") {
					var tmpObj = {
						"from" : mappingValue,
						"to" : fieldName
					};
					fieldRelation.push(tmpObj);
				}
			});
			var webRetrieveResult = {};
			webRetrieveResult["schemeType"] = opts.schemeType;
			webRetrieveResult["fileName"] = opts.fileInfo.desFileName;
			webRetrieveResult["fileRealName"] = opts.fileInfo.name;
			webRetrieveResult["sheetName"] = $dialog.find("#sheetNames").val();
			webRetrieveResult["suffix"] = opts.fileInfo.suffix;
			webRetrieveResult["realName"] = opts.fileInfo.name;
			webRetrieveResult["examId"] = opts.examId;
			webRetrieveResult["importType"] = opts.importType;
			webRetrieveResult["omrStr"] = $dialog.find("#kgOmrStr").val();
			webRetrieveResult["scoreStr"] = $dialog.find("#kgScoreStr").val();
			webRetrieveResult["overlayImport"] = isOverlayImport;
			webRetrieveResult["webFieldRelation"] = fieldRelation;
			// webRetrieveResult["desFileName"] = opts.fileInfo.desFileName;
			ctrl.postJson("/etl/batchExecValidate/", webRetrieveResult, "", function(data) {

				// delete webRetrieveResult["desFileName"];
				if(data.is){
					ctrl.confirm("导入","已存在相同的数据是否覆盖",function(){
						ajax({
							url : "/etl/exec",
							async : true,
							type : "POST",
							dataType : "html",
							data : JSON.stringify(webRetrieveResult),
							callback : function(html) {
									sh = setInterval(function(){
										endexec(html,opts);
									},2000);
							}
						});
					},"覆盖",function(){
					},"取消");
				}else{
					ajax({
						url : "/etl/exec",
						async : true,
						type : "POST",
						dataType : "html",
						data : JSON.stringify(webRetrieveResult),
						callback : function(html) {
								sh = setInterval(function(){
									endexec(html,opts);
								},2000);
						}
					});
				}
			});
		}
		
		function endexec(html,opts){
			console.log(resultflag);
			if(resultflag = 1){
				clearInterval(sh);
				var $htmlObj1 = $(html);
				$htmlObj1.find("a").click(function() {
					var logFile = $(this).attr("data-logFile");
					downloadLogFile(opts.examId, logFile, opts.schemeType);
				});
				
				$dialog.find(".dialog-body").html($htmlObj1);
				$dialog.find(".dialog-footer button:first").remove();
				$dialog.find(".dialog-footer button:first").text("确定");
				console.log('导入线程完毕');
				if ($.isFunction(opts.callBack)) {
					opts.callBack();
				}
			}
		}
		var sh;
		var resultflag = 0;
		function execprogress() {
				ctrl.ajaxExecuteprogress('', {
					examId : 3
				}, 'html', function(result) {
					console.log('进度线程完毕');
					resultflag = 1;
				}, $dialog);
			
		}
		
		function downloadLogFile(examId, logFile, logType) {
			var url = window.app.rootPath + "download/log/" + examId
					+ "?logFile=" + logFile + "&logType=" + logType;
			$.download(url);
		}
		
		var o = {
			UI : function(opts) {
				sUI(opts);
			}
		
		};
		return o;
	});

})();