(function() {
	"use strict";
	var model = [ "jquery", "jqueryPager", "controller", "formToJosn",
			"dialog", "datepickerjs", "selectjs", "../etl/etlExecutor",
			"download", "../logs/logs" ];
	define(model, function($, $page, ctrl, formToJosn, dialog, datepickerjs,
			selectjs, etlExecutor, download, logs) {

		var $container = undefined;
		function list(page) {
			if(page== undefined){
				page = ctrl.newPage();
			}
			ctrl.getHtml("/examStudent/list/exam/" + page.curSize + "/"
					+ page.pageSize, function(html) {
				var $htmlObj = $(html);
				ctrl.initUI($htmlObj);
				examSelector($htmlObj);
				downloadTemplate($htmlObj);
				ctrl.appendToView($container, $htmlObj);
				initTableRowEvent($htmlObj);
			}, "", page.data);
		}

		function initTableRowEvent($htmlObj) {
			view($htmlObj);
			viewFile($htmlObj);
			rollInStudent($htmlObj);
			importStudent($htmlObj, function() {
				$container.find("#examList #pager").clickCurPage();
			});
			appendImportStudent($htmlObj, function() {
				$container.find("#examList #pager").clickCurPage();
			});
			returnViewFileUI($htmlObj);
			ctrl.renderPager({
				"containerObj" : $htmlObj,
				"pageObj" : $container.find("#examList #pager"),
				"callBack" : search
			});
		}

		function search(page) {
			var data = $container.find("div.query-form form").formToJson();
			page["data"] = data;
			
			ctrl.getHtml("/examStudent/list/exam/" + page.curSize + "/"
					+ page.pageSize, function(html) {
				var $htmlObj = $(html);
				$container.find("#examList table tbody").html($htmlObj);
				initTableRowEvent($htmlObj);
				$container.attr("indexPage",page.curSize);
			}, "", page.data);
		}

		function downloadTemplate($htmlObj) {
			$htmlObj.find("#downloadTemplate").click(function() {
				var url = window.app.rootPath + "download/bmk";
				$.download(url);
			});
		}

		function examSelector($htmlObj) {
			$htmlObj.find('div.query-form a.btn:eq(0)').click(function() {
				var page = ctrl.newPage();
				search(page);
			});
		}

		function studentListSearch(page) {
			var schoolCode = $("#schoolCode").val();
			var examId = $("#examId").val();
			page["data"] = {
				"query" : true
			};
			ctrl.getHtml("examStudent/studentList/" + examId + "/" + schoolCode
					+ "/" + page.curSize + "/" + page.pageSize, function(html) {
				var $htmlObj = $(html);
				//ctrl.appendToView($container, $htmlObj);
				$container.find("#studentList tbody").html("");
				$container.find("#studentList tbody").eq(1).html($htmlObj);
				ctrl.initUI($htmlObj);
				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $container.find("#studentList #pager"),
					"callBack" : studentListSearch
				});
			}, "", page.data);
		}

		function rollinSearch(page) {
			var examId = $container.find("#examId").val();
			var data = $container.find("div.query-form:eq(0) form:eq(0)")
					.formToJson();
			page["data"] = data;
			var url = "examStudent/list/exam/" + page.curSize + "/"
					+ page.pageSize + "?hasExamStudent=true&examId=" + examId;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				$container.find("#examRonllinList table:eq(0) tbody").html(
						$htmlObj);

				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $container.find("#examRonllinList #pager"),
					"callBack" : rollinSearch
				});

			}, "", page.data);
		}
		//覆盖导入学生信息
		function importStudent($htmlObj, callBack, examId) {
			$htmlObj.find(".impStu-importStudent").click(function() {
				if (!examId) {
					examId = $(this).attr("objectId");
				}
				etlExecutor.UI({
					importType : 0,
					schemeType : 1,
					examId : examId,
					title : "导入学生",
					callBack : callBack
				});
			});
		}

		//追加导入学生信息
		function appendImportStudent($htmlObj, callBack, examId) {
			$htmlObj.find(".impStu-appendImportStudent").click(function() {
				if (!examId) {
					examId = $(this).attr("objectId");
				}
				etlExecutor.UI({
					importType : 1,
					schemeType : 1,
					examId : examId,
					title : "导入学生",
					callBack : callBack
				});
			});
		}
		//下载文件
		function returnViewFileUI($htmlObj) {
			$htmlObj.find(".impStu-downLoad").click(function() {
				var fileId = $(this).attr("objectId");
				var url = window.app.rootPath +"file/downLoad/"+fileId;
				$.download(url);
			});

		}
		
		function rollInStudent($html) {
			$html.on("click", "table tbody a.impStu-rollInStudent", function() {
				var examId = $(this).attr("objectId");
				var examName = $(this).parent().parent().find('td:eq(2)')
						.text();
				var page = ctrl.newPage();
				var url = "examStudent/list/exam/" + page.curSize + "/"
						+ page.pageSize
						+ "?rollin=true&hasExamStudent=true&examId=" + examId
						+ "&targetExamName=" + examName;
				ctrl.getHtml(url, function(html) {
					var $htmlObj = $(html);
					returnExamList($htmlObj);
					ctrl.appendToView($container, $htmlObj);
					ctrl.initUI($htmlObj);
					$htmlObj.find('div.page-header:eq(0)>h1 button:eq(0)')
							.click(function() {
								list();
							});
					$htmlObj.find('div.query-form:eq(0) a.btn:eq(0)').click(
							function() {
								rollinSearch(page);
							});
					$htmlObj.find("#examRonllinList").on("click", 'tbody td a',
							function(e) {

								doRollin(examId, $(this), page);
							});
					ctrl.renderPager({
						"containerObj" : $htmlObj,
						"pageObj" : $container.find("#examRonllinList #pager"),
						"callBack" : search
					});
				});
			});
		}
		;

		function doRollin(targetExamId, $src, page) {
			var srcExamId = $src.attr("objectId");
			var examName = $src.parent().parent().find('td:eq(1)').text();
			ctrl.confirm("操作提示", "确定从考试【<b style='color:red;'>"
					+ (examName || "") + "</b>】转入学生数据吗？", function() {
				ctrl.putJson("examStudent/rollin/from/" + srcExamId + "/to/"
						+ targetExamId, {}, "学生数据转入已经成功启动", function() {
					list();
					// rollinSearch(page);
				});
			});
		}
		;

		function view($htmlObj) {
			$htmlObj.find(".impStu-view").click(function() {
				var examId = $(this).attr("objectId");
				viewUI(examId);
			});
		}
		function viewFile($htmlObj) {
			$htmlObj.find(".impStu-viewFile").click(function() {
				var examId = $(this).attr("objectId");
				var url = "/examStudent/viewFile/student/" + examId;
				ctrl.getHtml(url, function(html) {
						var $htmlObj = $(html);
						returnViewFileUI($htmlObj);
						returnExamList($htmlObj);
						ctrl.appendToView($container, $htmlObj);
						
				});
			});
		}
		function studentList($htmlObj, examId) {
			$htmlObj.find("#studentList").click(
					function() {
						var schoolCode = $(this).attr("schoolcode");
						var page = ctrl.newPage();
						var url = "examStudent/studentList/" + examId + "/"
								+ schoolCode + "/" + page.curSize + "/"
								+ page.pageSize;
						ctrl.getHtml(url, function(html) {
							var $htmlObj = $(html);
							returnViewUI($htmlObj, examId);
							ctrl.appendToView($container, $htmlObj);
							ctrl.initUI($htmlObj);
							ctrl.renderPager({
								"containerObj" : $htmlObj,
								"pageObj" : $container
										.find("#studentList #pager"),
								"callBack" : studentListSearch
							});
						});

					});
		}

		function returnViewUI($htmlObj, examId) {
			
			$htmlObj.find("#returnPage").click(function() {
				viewUI(examId);
			});

		}
	

		function viewUI(examId) {
			var page = ctrl.newPage();
			if($container.attr("indexpage_") == undefined){
				page["curSize"] = 1;
			}else{
				page["curSize"] = $container.attr("indexpage_");
			}
			var url = "/examStudent/view/" + examId + "/" + page.curSize + "/"
					+ page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				returnExamList($htmlObj);
				deleteStudent($htmlObj, examId);
				studentList($htmlObj, examId);
				var $logTable = $htmlObj.find("#logTable");
				logs.downloadLogs($logTable, examId, 1);
				importStudent($htmlObj, function() {
					viewUI(examId);
				}, examId);
				returnViewFileUI($htmlObj);
				appendImportStudent($htmlObj, function() {
					viewUI(examId);
				}, examId);
				ctrl.appendToView($container, $htmlObj);
				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $htmlObj.find("#pager"),
					"callBack" : statSchoolPersonPage($htmlObj, examId)
				});
			});
		}

		function statSchoolPersonPage($pageHtmlObj, examId) {
			return function(page) {
				page.data = {
					"q" : true
				};
				var url = "/examStudent/view/" + examId + "/" + page.curSize
						+ "/" + page.pageSize;
				ctrl.getHtml(url, function(html) {
					var $htmlObj = $(html);
					studentList($htmlObj, examId);
					$pageHtmlObj.find("#statSchoolPersonRexamList table tbody")
							.html($htmlObj);
					ctrl.renderPager({
						"containerObj" : $htmlObj,
						"pageObj" : $pageHtmlObj.find("#pager"),
						"callBack" : statSchoolPersonPage($pageHtmlObj, examId)
					});
					$container.attr("indexPage_",page.curSize);
				}, "", page.data);
			};

		}

		function returnExamList($htmlObj) {
			$htmlObj.find("#returnPage").click(function() {
				var page = ctrl.newPage();
				if($container.attr("indexpage") == undefined){
					page["curSize"] = 1;
				}else{
					page["curSize"] = $container.attr("indexpage");
				}
				$container.removeAttr("indexpage_");
				list(page);
			});
		}

		function deleteStudent($htmlObj, examId) {
			$htmlObj.find("#deleteStudentInfo").click(function() {
				var url = "/examStudent/delete/" + examId;
				ctrl.confirm("提示","确定要删除学生信息吗？",function(){
					ctrl.postJson(url, {}, "", function(data) {
						if (data.isSuccess) {
							ctrl.moment("删除成功！", "success");
						} else {
							ctrl.moment("不能删除！考试已经导入了成绩", "error");
						}
						viewUI(examId);
					});
				});
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