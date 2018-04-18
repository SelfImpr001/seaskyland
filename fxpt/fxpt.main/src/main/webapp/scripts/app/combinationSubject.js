(function() {
	"use strict";
	define([ 'ajax','dialog', 'fromToJosn', 'validatejs' ], function(ajax,dialog, a) {

		function list() {
			var examId = $("#examId").val();
			ajax({
				url : "/combinationSubject/list/" + examId,
				async : true,
				dataType : "html",
				callback : function(htmlStr) {
					$("#combinationSubjects").html(htmlStr);
					addCombinationSubject();
					newUpdate();
					deletes();
				}
			});
		}

		function newUpdate() {
			$(".update").click(
					function() {
						var combinationSubjectId = $(this).attr("objectId");
						var examId = $("#examId").val();
						ajax({
							url : "/combinationSubject/newUpdate/" + examId
									+ "/" + combinationSubjectId,
							async : true,
							dataType : "html",
							callback : function(htmlStr) {
								dialog.modal({
								 	canmove:true,
								 	size:"md",
								 	header: {
										txt: "总分/组合科目-编辑"
									},
								 	body:htmlStr,
								 	footer:{buttons:[{
										callback: function() {
											update($(this));
										}
									}]}
								 });
								validateForm();
							}
						});
					});
		}

		function update($dialog) {
			if (!$("#form1").valid()) {
				return false;
			}
			var combinationSubject = createCombinationSubject();
			ajax({
				url : "/combinationSubject/update",
				async : true,
				type : "POST",
				data : JSON.stringify(combinationSubject),
				callback : function(data) {
					list();
					$dialog.trigger('close');
				}
			});
		}

		function deletes() {
			$(".delete").click(function() {
				var combinationSubjectId = $(this).attr("objectId");
				ajax({
					url : "/combinationSubject/delete/" + combinationSubjectId,
					async : true,
					callback : function(data) {
						dialog.fadedialog({
							icon_info : "success",
							tip_txt : "删除成功!"
						});
						list();
					}
				});
			});
		}

		function validateForm() {

			$("#form1").validate({
				onKeyup : true,
				rules : {
					name : {
						required : true

					},
					testPaper : {
						required : true
					}
				},

				messages : {
					name : {
						required : '不能为空'
					},
					testPaper : {
						required : '不能为空',
						minlength : '至少选择两个以上'
					}
				}
			});
		}

		function addCombinationSubject() {
			$("#addCombinationSubject").click(function() {
				var examId = $("#examId").val();
				ajax({
					url : "/combinationSubject/newAdd/" + examId,
					async : true,
					dataType : "html",
					callback : function(htmlStr) {
						dialog.modal({
						 	canmove:true,
						 	size:"md",
						 	header: {
								txt: "总分/组合科目-新增"
							},
						 	body:htmlStr,
						 	footer:{buttons:[{
								callback: function() {
									add($(this));
								}
							}]}
						 });
						validateForm();
					}
				});
			});
		}

		function add($dialog) {
			if (!$("#form1").valid()) {
				return false;
			}
			var testPaperCount = $(".testPaper:checked").size();
			if (testPaperCount < 2) {
				$("#testPaperCount").text("必须选择2个以上试卷");
				return;
			}

			var combinationSubject = createCombinationSubject();

			ajax({
				url : "/combinationSubject/add",
				async : true,
				type : "POST",
				data : JSON.stringify(combinationSubject),
				callback : function(data) {
					list();
					$dialog.trigger('close');
				}
			});
		}

		function createCombinationSubject() {
			var combinationSubject = $("#form1").formToJson();

			var csXtps = new Array();
			$(".testPaper:checked").each(function() {
				var testPaper = {};
				testPaper["id"] = $(this).val();

				var csXtp = {};
				csXtp["testPaper"] = testPaper;

				csXtps.push(csXtp);
			});

			combinationSubject["childTestPaper"] = csXtps;

			var exam = {};
			exam["id"] = $("#examId").val();

			combinationSubject["exam"] = exam;
			return combinationSubject;
		}

		var o = {
			list : function() {
				list();
			}
		};

		return o;

	});

})();