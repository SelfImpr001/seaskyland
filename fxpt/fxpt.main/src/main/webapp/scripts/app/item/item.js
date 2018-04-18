(function() {
	"use strict";
	var model = [ "jquery", "jqueryPager", "controller", "formToJosn",
			"dialog", "datepickerjs", "selectjs", "../etl/etlExecutor",
			"download", "../etl/MultiEtlExecutor", "../logs/logs","../etl/webitemOrCjUpload"];
	define(model, function($, $page, ctrl, formToJosn, dialog, datepickerjs,
			selectjs, etlExecutor, download, multiExecutor, logs,webitemOrCjUpload) {

		var $container = undefined;
		function list(page) {
			if(page== undefined){
				page = ctrl.newPage();
			}
			var url = "/item/list/exam/" + page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				ctrl.initUI($htmlObj);
				downloadTemplate($htmlObj);
				examSelector($htmlObj);

				ctrl.appendToView($container, $htmlObj);
				initExamListTableRowEvent($htmlObj);
			}, "", page.data);
		}

		function initExamListTableRowEvent($htmlObj) {
			view($htmlObj);
			importItem($htmlObj, function() {
				$container.find("#examList #pager").clickCurPage();
			});
			newMultiImportItem($htmlObj, function() {
				$container.find("#examList #pager").clickCurPage();
			});

			ctrl.renderPager({
				"containerObj" : $htmlObj,
				"pageObj" : $container.find("#examList #pager"),
				"callBack" : search
			});
		}

		function search(page) {
			var data = $container.find("div.query-form form").formToJson();
			page["data"] = data;
			var url = "/item/list/exam/" + page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				$container.find("#examList table tbody").html($htmlObj);
				initExamListTableRowEvent($htmlObj);
				$container.attr("indexPage",page.curSize);
			}, "", page.data);
		}

		function downloadTemplate($htmlObj) {
			$htmlObj.find("#item-downloadTemplate").click(function() {
				var url = window.app.rootPath + "download/xmb";
				$.download(url);
			});
		}
		function importItem($htmlObj, callBack, examId) {
			$htmlObj.find(".impItem-importItem").click(function() {
				if (!examId) {
					examId = $(this).attr("objectId");
				}
				etlExecutor.UI({
					schemeType : 2,
					examId : examId,
					title : "导入双向细目表",
					effectiveFile : [ ".xls", ".xlsx" ],
					effectiveFileErrorMesg : "文件只能为excel文件",
					callBack : callBack
				});
			});
		}
		/** 批量导入 */
		function newMultiImportItem($htmlObj, callBack, examId) {
			$htmlObj.find(".impItem-multiImportItem").click(function() {
				if (!examId) {
					examId = $(this).attr("objectId");
				}
				var schemeType = 2;
				webitemOrCjUpload.UI({
					schemeType : schemeType,
					examId : examId,
					title : "导入双向细目表",
					fileTypeDesc : "只能选择excle",
					fileTypeExts : "xls,xlsx",
					callBack : callBack,
					container:$container,
					self:self
				});
				
				/*var schemeType = 2;
				var browser=navigator.appName;
				var b_version=navigator.appVersion;
				var version=b_version.split(";");
				var trim_Version=version[1].replace(/[ ]/g,"");
				if((trim_Version=="MSIE8.0" || trim_Version=="MSIE7.0" || trim_Version=="MSIE6.0" || trim_Version=="MSIE5.0" || trim_Version=="MSIE5.5")){
					multiExecutor.UI({
						schemeType : schemeType,
						examId : examId,
						title : "导入双向细目表",
						fileTypeDesc : "只能选择excle",
						fileTypeExts : "*.xls;*.xlsx",
						callBack : callBack
					});
				}else{
					itemOrCjUpload.UI({
						schemeType : schemeType,
						examId : examId,
						title : "导入双向细目表",
						fileTypeDesc : "只能选择excle",
						fileTypeExts : "*.xls;*.xlsx",
						callBack : callBack,
						container:$container,
						self:self
						
					});
				}*/
				
				/*var dialog = multiExecutor.UI({
					schemeType : 2,
					examId : examId,
					title : "导入双向细目表",
					fileTypeDesc : "只能选择excle",
					fileTypeExts : "*.xls;*.xlsx",
					callBack : callBack
				});*/
				
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
		function examSelector($htmlObj) {
			$htmlObj.find('div.query-form a.btn:eq(0)').click(function() {
				var page = ctrl.newPage();
				search(page);
			});
		}

		function view($htmlObj) {
			$htmlObj.find(".impItem-view").click(function() {
				var examId = $(this).attr("objectId");
				viewUI(examId);
			});
		}

		function viewUI(examId) {
			var url = "/item/view/" + examId;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				returnExamList($htmlObj);
				var $tbody = $htmlObj.find("#testPapersList tbody");
				deleteTestPaper($tbody, examId);
				viewDetail($tbody, examId);
				importItem($htmlObj, importCallBackFun($htmlObj, examId),
						examId);
				newMultiImportItem($htmlObj,
						importCallBackFun($htmlObj, examId), examId);
				var $logTable = $htmlObj.find("#logTable");
				logs.downloadLogs($logTable, examId, 2);
				ctrl.appendToView($container, $htmlObj);
			});
		}

		function importCallBackFun($htmlObj, examId) {
			return function() {
				var $tbody = $htmlObj.find("#testPapersList tbody");
				testPaperList($tbody, examId);
				var $logTable = $htmlObj.find("#logTable");
				logs.list($logTable, examId, 2);
			}
		}

		function viewDetail($tbody, examId) {
			$tbody.find(".testPaperView").click(function() {
				var testPaperId = $(this).attr("objectid");
				var url = "/item/detail/true/" + examId + "/" + testPaperId;
				ctrl.getHtml(url, function(html) {
					var $htmlObj = $(html);
					returnViewPage($htmlObj, examId);
					itemTab($htmlObj, examId);
					ctrl.appendToView($container, $htmlObj);
				});
			});
		}
		function deleteTestPaper($tbody, examId) {
			$tbody.find(".testPaperDelete").click(function() {
				var testPaperId = $(this).attr("objectid");
				var url = "/item/delete/" + testPaperId;
				ctrl.postJson(url, {}, "", function(data) {
					if (data.isSuccess) {
						ctrl.moment("删除成功！", "success");
						testPaperList($tbody, examId);
					} else {
						ctrl.moment("删除失败！请先删除成绩", "error");
					}
					var $logTable = $container.find("#logTable");
					logs.list($logTable, examId, 2);
				});
			});
		}

		function testPaperList($tbody, examId) {
			var url = "/item/list/testPaper/" + examId;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				$tbody.html($htmlObj);
				deleteTestPaper($tbody, examId);
				viewDetail($tbody, examId);
			}, "", {});
		}

		function itemTab($htmlObj, examId) {
			$htmlObj.find("#item-tab li").click(
					function() {
						var testPaperId = $(this).attr("data-testPaperId");
						if ($("#item-testPaper-" + testPaperId).size() == 0) {
							var url = "/item/detail/false/" + examId + "/"
									+ testPaperId;
							ctrl.log("testPaperId:" + testPaperId);
							ctrl.getHtml(url, function(html) {
								var $htmlObj = $(html);
								var $itemTabContentObj = $("#item-tabContent");
								var $activeObj = $itemTabContentObj
										.find(".active");
								$activeObj.removeClass("active");
								$activeObj.removeClass("in");
								$itemTabContentObj.append($htmlObj);
							});
						}

					});
		}

		function returnExamList($htmlObj) {
			$htmlObj.find("#returnPage").click(function() {
				var page = ctrl.newPage();
				if($container.attr("indexpage") == undefined){
					page["curSize"] = 1;
				}else{
					page["curSize"] = $container.attr("indexpage");
				}
				list(page);
			});
		}
		function returnViewPage($htmlObj, examId) {
			$htmlObj.find("#returnPage").click(function() {
				viewUI(examId);
			});
		}

		var self = undefined;
		var o = self = {
			render : function(container) {
				$container = container;
				list();
			}
		};
		return $.extend(o, ctrl);
	});
})();