(function() {
	"use strict";

	define([ "jquery", "jqueryPager", "controller", "formToJosn", "dialog",
			"datepickerjs", "selectjs", "radioztree" ], function($, $page,
			ctrl, formToJosn, dialog, datepickerjs, selectjs, radioztree) {

		var $container = undefined;
		function list(page) {
			if (!page) {
				page = ctrl.newPage();
			}

			var url = "/exam/list/" + page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				ctrl.initUI($htmlObj);
				ctrl.appendToView($container, $htmlObj);

				examSelector($htmlObj);
				newExam();
				initExamTableRowEvent($htmlObj);
			}, "", page.data);
		}

		function initExamTableRowEvent($htmlObj) {

			newUpdate();
			deletes();

			ctrl.renderPager({
				"containerObj" : $htmlObj,
				"pageObj" : $container.find("#examList #pager"),
				"callBack" : search
			});
		}

		function examSelector($htmlObj) {
			$htmlObj.find('div.query-form a.btn:eq(0)').click(function() {
				var page = ctrl.newPage();
				search(page);
			});
		}

		function search(page) {
			var data = $container.find("div.query-form form").formToJson();
			page["data"] = data;

			var url = "/exam/list/" + page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				$container.find("#examList table tbody").html($htmlObj);
				initExamTableRowEvent($htmlObj);
			}, "", page.data);
		}

		function newExam() {
			$("#newExam").click(function() {
				ctrl.getHtml("/exam/newExams", function(html) {

					var $htmlObj = $(html);
					ctrl.initUI($htmlObj);

					ctrl.modal("新增考试", $htmlObj, function() {
						return addExams();
					}, "保存", "lg");

					createTree();
					validateForm();
				}, "", {});
			});
		}

		function addExams() {
			if (!$("#addExamsForm").valid()) {
				return false;
			}

			var exams = createExams();
			ctrl.log(JSON.stringify(exams));
			ctrl.postJson("/exam/createExams", exams, "增加成功!", function(data) {
				ctrl.moment("增加成功!", "success");

				$("#cleanExamSelector").click();
				var page = ctrl.newPage();
				search(page);

			});
			return true;
		}

		function getLevelName(level) {
			if (level === "1") {
				return "省市级别";
			} else if (level === "2") {
				return "地市级别";
			} else if (level === "3") {
				return "区县级别";
			} else if (level === "4") {
				return "学校级别";
			}
		}

		function createExams() {
			var exams = $("#addExamsForm").formToJson();

			var grades = {};
			$(".gardeIds:checked").each(function() {
				grades[$(this).val()] = $(this).attr("data-name");
			});

			var examTypeName =$('#addExamsForm #examTypeId option:selected').attr("data-name");

			exams["grades"] = grades;
			exams["examTypeName"] = examTypeName;

			exams["levelName"] = getLevelName(exams["levelCode"]);

			return exams;
		}

		function validateForm() {
			$("#addExamsForm").validate({
				onKeyup : true,
				messages : {
					gardeIds : {
						required : '至少选择1个年级',
						minlength : '至少选择1个年级'
					},
					ownerName : {
						required : '必须选择组织考试的机构',
					}
				}
			});
		}

		function createTree() {
			ctrl.getJson("/education/tree", function(data) {
				radioztree({
					ztreeId : "myTree",
					targetId : "ownerName",
					async : {
						enable : true,
						url : window.app.rootPath + "/education/tree",
						autoParam : [ "code", "type" ]
					},
					onCheckCb : function(nodes) {
						var node = null;
						if (nodes.length > 0) {
							node = nodes[0];
						}
						if (!node || !node.code) {
							return;
						}
						$("#ownerCode").val(node.code);
						$("#ownerName").val(node.name);
						$("#levelCode").val(node.type);
					},
					initData : data.treeNodes
				});
			}, "", {
				code : "",
				type : 0
			});
		}

		function newUpdate() {
			$(".examUpdateButton").click(function() {
				var examId = $(this).attr("objectId");
				ctrl.getHtml("/exam/newUpdate/" + examId, function(html) {
					var $htmlObj = $(html);
					ctrl.initUI($htmlObj);

					ctrl.modal("修改考试", $htmlObj, function() {
						return update();
					}, "保存", "lg");

					createTree();
					validateForm();
				}, "", {});
			});
		}

		function update() {
			if (!$("#addExamsForm").valid()) {
				return false;
			}

			var exam = $("#addExamsForm").formToJson();
			ctrl.log(JSON.stringify(exam));
			ctrl.postJson("/exam/update", exam, "修改成功!", function(data) {
				ctrl.moment("修改成功!", "success");
				$container.find("#examList #pager").clickCurPage();

			});
			return true;
		}

		function deletes() {
			$(".examDeleteButton").click(function() {
				var examId = $(this).attr("objectId");
				var url = "/exam/delete/" + examId;
				ctrl.postJson(url, {}, "删除考试", function(data) {
					if (data.hasTestPaper || data.hasExamStudent) {
						var message = "考试存在试卷和报名库，删除失败";
						ctrl.moment(message, "error");

					} else {
						ctrl.moment("删除成功!", "success");
						$container.find("#examList #pager").clickCurPage();
					}
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