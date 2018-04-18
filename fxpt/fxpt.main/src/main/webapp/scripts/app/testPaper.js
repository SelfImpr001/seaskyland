(function() {
	"use strict";

	define(
			[ 'ajax', 'fromToJosn', 'validatejs',"./etl/etlExecutor","dialog"],
			function(ajax, b, c,etlExecutor,dialog) {

				function list() {
					var examId = $("#examId").val();
					ajax({
						url : "/testPaper/list/" + examId,
						async : true,
						dataType : "html",
						callback : function(htmlStr) {
							$("#testPaper").html(htmlStr);
							newAdd();
							deletes();
							newUpdate();
							importItem();
							importCj();
							deleteItem();
							deleteCj();
						}
					});
				}
				
				function deleteItem(){
					$(".deleteItem").click(function(){
						var testPaperId = $(this).attr("objectId");
						ajax({
							url : "/item/delete/" + testPaperId,
							async : true,
							callback : function(data) {
								list();
							}
						});
					});
				}
				
				function deleteCj(){
					$(".deleteCj").click(function(){
						var testPaperId = $(this).attr("objectId");
						ajax({
							url : "/cj/delete/" + testPaperId,
							async : true,
							callback : function(data) {
								list();
							}
						});
					});
				}

				function importItem() {
					$(".importItem").click(function() {
						var testPaperId = $(this).attr("objectId");
						var testPaperName = $(this).attr("objectName");;
					
						etlExecutor.newProcessor({
							tableName : "tb_item",
							contentObj : $(".page-content"),
							examId : $("#examId").val(),
							testPaperId : testPaperId,
							title : "导入【"+testPaperName+"】试题信息"
						});
					});
				}

				function importCj() {
					$(".importCj").click(function() {
						var testPaperId = $(this).attr("objectId");
						var testPaperName = $(this).attr("objectName");;
						etlExecutor.newProcessor({
							tableName : "tb_cj",
							contentObj : $(".page-content"),
							examId : $("#examId").val(),
							testPaperId : testPaperId,
							title : "导入【"+testPaperName+"】成绩"
						});
					});
				}

				function newAdd() {
					$("#addTestPaper").click(function() {
						ajax({
							url : "/testPaper/newAdd",
							async : true,
							dataType : "html",
							callback : function(htmlStr) {
								dialog.modal({
								 	canmove:true,
								 	size:"md",
								 	header: {
										txt: "试卷信息-新增"
									},
								 	body:htmlStr,
								 	footer:{buttons:[{
										callback: function() {
											add($(this));
										}
									}]}
								 });
								$('.selectpicker').selectpicker();
								validateForm();
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
							fullScore : {
								required : true,
								benum : ''

							},
							kgScore : {
								required : true,
								benum : ''

							},
							zgScore : {
								required : true,
								benum : ''
							}
						},
						messages : {
							name : {
								required : '不能为空'
							},
							fullScore : {
								required : '不能为空',
								benum : '必须为数字'

							},
							kgScore : {
								required : '不能为空',
								benum : '必须为数字'
							},
							zgScore : {
								required : '不能为空',
								benum : '必须为数字'
							}
						},
					});
				}

				function add($dialog) {
					if (!$("#form1").valid()) {
						return false;
					}
					var testPaper = $("#form1").formToJson();
					testPaper["exam"] = {
						"id" : $("#examId").val()
					};
					ajax({
						url : "/testPaper/add",
						async : true,
						type : "POST",
						data : JSON.stringify(testPaper),
						callback : function(data) {
							list();
							$dialog.trigger('close');
						}
					});
				}

				function deletes() {
					$(".delete")
							.click(
									function() {
										var testPaperId = $(this).attr(
												"objectId");
										ajax({
											url : "/testPaper/delete/"
													+ testPaperId,
											async : true,
											callback : function(data) {
												if (data.hasCj
														|| data.hasItem
														&& data.hasCombinationSubject) {
													var message = "试卷有关联的题目，成绩或者参与合并科目，删除失败";
													dialog.fadedialog({
														icon_info : "error",
														tip_txt :message
													});
												} else {
													dialog.fadedialog({
														icon_info : "success",
														tip_txt : "删除成功!"
													});
													list();
												}
											}
										});
									});
				}

				function newUpdate() {
					$(".update").click(function() {
						var testPaperId = $(this).attr("objectId");
						ajax({
							url : "/testPaper/newUpdate/" + testPaperId,
							async : true,
							dataType : "html",
							callback : function(htmlStr) {
								dialog.modal({
								 	canmove:true,
								 	size:"md",
								 	header: {
										txt: "试卷信息-修改"
									},
								 	body:htmlStr,
								 	footer:{buttons:[{
										callback: function() {
											update($(this));
										}
									}]}
								 });
								$('.selectpicker').selectpicker();
								validateForm();
							}
						});
					});
				}

				function update($dialog) {
					if (!$("#form1").valid()) {
						return false;
					}
					var testPaper = $("#form1").formToJson();
					testPaper["exam"] = {
						"id" : $("#examId").val()
					};
					ajax({
						url : "/testPaper/update",
						async : true,
						type : "POST",
						data : JSON.stringify(testPaper),
						callback : function(data) {
							list();
							$dialog.trigger('close');
						}
					});
				}

				var o = {
					list : function() {
						list();
					}
				};

				return o;

			});

})();