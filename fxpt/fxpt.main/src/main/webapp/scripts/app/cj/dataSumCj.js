(function() {
	"use strict";
	var model = [ "jquery", "jqueryPager", "controller", "formToJosn",
			"dialog", "datepickerjs", "selectjs", "../etl/etlExecutor",
			"../etl/MultiEtlExecutor", "../logs/logs" ];
	define(model, function($, $page, ctrl, formToJosn, dialog, datepickerjs,
			selectjs, etlExecutor, multiExecutor, logs) {
		var $container = undefined;
		function list(page) {
			if (!page) {
				page = ctrl.newPage();
			}
			ctrl.getHtml("/cj/dataSumList/" + page.curSize + "/" + page.pageSize,
					function(html) {
						var $htmlObj = $(html);
						ctrl.initUI($htmlObj);
						examSelector($htmlObj);
						downloadTemplate($htmlObj);
						ctrl.appendToView($container, $htmlObj);
						initExamListRowEvent($htmlObj);
					}, "", page.data);
		}

		function initExamListRowEvent($htmlObj) {
			view($htmlObj);
			viewFile($htmlObj);
			newImportCj($htmlObj, function() {
				$container.find("#examList #pager").clickCurPage();
			});
			appendImportCj($htmlObj, function() {
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
			var url = "/cj/dataSumList/" + page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				$container.find("#examList table tbody").html($htmlObj);
				initExamListRowEvent($htmlObj);
			}, "", page.data);
		}

		function downloadTemplate($htmlObj) {
			$htmlObj.find("#downloadTemplate").click(function() {
				var url = window.app.rootPath + "download/cj";
				$.download(url);
			});
		}
		//查看
		function view($htmlObj) {
			var $cjViewObj = $htmlObj.find(".cj-view");
			$cjViewObj.click(function() {
				var examId = $(this).attr("objectId");
				viewCj(examId);
			});
		}
		//导入文件管理
		function viewFile($htmlObj) {
			var $cjViewObj = $htmlObj.find(".cj-viewFile");
			$cjViewObj.click(function() {
				var examId = $(this).attr("objectId");
				viewCjFile(examId);
			});
		}
		
		function viewCjFile(examId) {
			var url = "/cj/viewFile/" + examId;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				ctrl.appendToView($container, $htmlObj);
				returnExamList($htmlObj);
				returnViewFileUI($htmlObj);
			}, "", {});
		}

		//下载文件
		function returnViewFileUI($htmlObj) {
			$htmlObj.find(".impCj-downLoad").click(function() {
				var fileId = $(this).attr("objectId");
				var url = window.app.rootPath +"file/downLoad/"+fileId;
				$.download(url);
			});

		}
		function viewCj(examId) {
			var url = "/cj/view/" + examId;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				ctrl.appendToView($container, $htmlObj);
				returnExamList($htmlObj);
				var $logTable = $htmlObj.find("#logTable");
				logs.list($logTable, examId, 3);
				var $testPaperTable = $htmlObj.find("#testPapersList");
				testPaperStat($testPaperTable, examId);
				newImportCj($htmlObj, importCallBackFun($htmlObj, examId),
						examId);
				appendImportCj($htmlObj, importCallBackFun($htmlObj, examId),
				examId);
			}, "", {});
		}

		function importCallBackFun($htmlObj, examId) {
			return function() {
				var $logTable = $htmlObj.find("#logTable");
				logs.list($logTable, examId, 3);
				var $testPaperTable = $htmlObj.find("#testPapersList");
				testPaperStat($testPaperTable, examId);
			};
		}

		function testPaperStat($testPaperTable, examId) {
			var url = "/cj/testPaper/stat/" + examId;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				$testPaperTable.find("tbody").html($htmlObj);
				viewDetailCjList($testPaperTable, examId);
				deleteCj($testPaperTable, examId);
			}, "", {});
		}

		function deleteCj($testPaperTable, examId) {
			$testPaperTable.find("tbody .delete").click(function() {
				var testPaperId = $(this).attr("objectid");
				var url = "/cj/delete/" + testPaperId;
				ctrl.postJson(url, {}, "", function() {
					ctrl.moment("删除成功！", "success");
					testPaperStat($testPaperTable, examId);
					var $logTable = $container.find("#logTable");
					logs.list($logTable, examId, 3);
				});
			});
		}

		function viewDetailCjList($testPaperTable, examId) {
			$testPaperTable.find("tbody .view").click(
					function() {
						var page = ctrl.newPage();
						var testPaperId = $(this).attr("objectid");
						var url = "/cj/list/" + examId + "/" + testPaperId
								+ "/-1/" + page.curSize + "/" + page.pageSize;
						ctrl.getHtml(url, function(html) {
							var $htmlObj = $(html);
							ctrl.appendToView($container, $htmlObj);
							returnViewPage($htmlObj, examId);
							ctrl.renderPager({
								"containerObj" : $htmlObj,
								"pageObj" : $("#cj-cjList-pager"),
								"callBack" : cjListPaper(examId, testPaperId,
										-1)
							});

						}, "", {});

					});
		}

		function cjListPaper(examId, testPaperId, schoolId) {
			return function(page) {
				page.data = {
					"q" : true
				};
				var url = "/cj/list/" + examId + "/" + testPaperId + "/"
						+ schoolId + "/" + page.curSize + "/" + page.pageSize;

				ctrl.getHtml(url, function(html) {
					var $htmlObj = $(html);
					$("#cj-ListTBody").html($htmlObj);
					ctrl.renderPager({
						"containerObj" : $htmlObj,
						"pageObj" : $("#cj-cjList-pager"),
						"callBack" : cjListPaper(examId, testPaperId, schoolId)
					});

				}, "", page.data);
			};
		}

		function returnExamList($htmlObj) {
			$htmlObj.find("#returnPage").click(function() {
				list();
			});
		}

		function returnViewPage($htmlObj, examId) {
			$htmlObj.find("#returnPage").click(function() {
				viewCj(examId);
			});
		}

		//覆盖导入成绩
		function newImportCj($htmlObj, callBack, examId) {
			$htmlObj.find(".cj-batchImport").click(function() {
				if (!examId) {
					examId = $(this).attr("objectId");
				}
				multiExecutor.UI({
					importType : 0,
					schemeType : 3,
					examId : examId,
					title : "导入成绩",
					fileTypeDesc : "只能选择excle和dbf文件",
					fileTypeExts : "*.xls;*.xlsx;*.dbf",
					callBack : callBack
				});
			});
		}

		//追加导入成绩
		function appendImportCj($htmlObj, callBack, examId) {
			$htmlObj.find(".cj-appendBatchImport").click(function() {
				if (!examId) {
					examId = $(this).attr("objectId");
				}
				multiExecutor.UI({
					importType : 1,
					schemeType : 3,
					examId : examId,
					title : "导入成绩",
					fileTypeDesc : "只能选择excle和dbf文件",
					fileTypeExts : "*.xls;*.xlsx;*.dbf",
					callBack : callBack
				});
			});
		}
		function examSelector($htmlObj) {
			$htmlObj.find('div.query-form a.btn:eq(0)').click(function() {
				var page = ctrl.newPage();
				search(page);
			});
		}

		var self = undefined;
		var o = self = {
			render : function(container) {
				$container = $(container);
				list();
			}
		};
		return $.extend(o, ctrl);
	});
})();