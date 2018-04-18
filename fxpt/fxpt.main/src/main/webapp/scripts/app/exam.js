(function() {
	"use strict";

	define(
			[ 'datepickerjs', 'ajax', 'fromToJosn', "./examStudent",
					"./testPaper", "./item", "./cj", "./parameter",
					'validatejs', "./etl/etlExecutor", "../common/radioztree",
					"ztree_corejs", "ztree_excheckjs","dialog",'lefttree','app/user' ],
			function(datepickerjs, ajax,fromToJosn, examStudent, testPaper, item, cj,
					parameter, validatejs, etlExecutor, radioztree, ztree_corejs,
					ztree_excheckjs,dialog,lefttree,user) {

				var g_pageSize = window.app["pageSize"];
				function newAdd() {
					$("#newAdd").click(function() {
						ajax({
							url : "/exam/newAdd",
							async : true,
							dataType : "html",
							callback : function(htmlStr) {
								dialog.modal({
								 	canmove:true,
								 	header: {
										txt: "考试-新增"
									},
								 	body:htmlStr,
								 	footer:{buttons:[{
										callback: function() {
											add($(this));
										}
									}]}
								 });
								
								datetime();
								$('.selectpicker').selectpicker();
								validateForm();
								createTree();
							}
						});
					});
				}

				function createTree() {
					ajax({
						url : "/education/tree",
						async : true,
						type : "GET",
						data : {
							code : "",
							type : 0
						},
						callback : function(data) {
							radioztree({
								ztreeId : "myTree",
								targetId : "ownerName",
								async : {
									enable : true,
									url : "/fxpt/education/tree",
									autoParam : [ "code", "type" ]
								},
								onCheckCb : function(nodes) {
									var node = null;
									if (nodes.length > 0) {
										node = nodes[0];
									}
									$("#ownerCode").val(node.code);
									$("#ownerName").val(node.name);
									$("#levelName")
											.val(getLevelName(node.type));
									$("#levelCode").val(node.type);
								},
								initData : data.treeNodes

							});
						}
					});
				}

				function getLevelName(level) {
					if (level == 1) {
						return "省市级别";
					} else if (level === 2) {
						return "地市级别";
					} else if (level === 3) {
						return "区县级别";
					} else if (level === 4) {
						return "学校级别";
					}
				}

				function validateForm() {
					$("#form1").validate({
						onKeyup : true,
						rules : {
							name : {
								required : true,
								maxlength:50
							},
							examDate : {
								required : true
							}
						},

						messages : {
							name : {
								required : '不能为空',
								maxlength:"考试名称不能超过50个字符"
							},
							examDate : {
								required : '不能为空'
							},
							ownerName : {
								required : '必须选择'
							}
						}
					});
				}

	
				function add($dialog) {
					if (!$("#form1").valid()) {
						return false;
					}
					var exam = $("#form1").formToJson();
					ajax({
						url : "/exam/add",
						async : true,
						type : "POST",
						data : JSON.stringify(exam),
						callback : function(data) {
							$dialog.trigger('close');
							list();
						}
					});
				}

				function datetime() {

					$('.date-picker').datetimepicker({
						autoclose : true,
						format : 'yyyy-mm-dd',
						weekStart : 1,
						todayBtn : 1,

						minView : 3,

					}).on('changeDate', function() {
						$(this).valid();
					}).next().on('click', function() {
						$(this).prev().focus();
					});
				}

				function newUpdate() {
					$(".update").click(function() {

						var examId = $(this).attr("objectId");
						ajax({
							url : "/exam/newUpdate/" + examId,
							async : true,
							dataType : "html",
							callback : function(htmlStr) {
								dialog.modal({
								 	canmove:true,
								 	header: {
										txt: "考试-编辑"
									},
								 	body:htmlStr,
								 	footer:{buttons:[{
										callback: function() {
											update($(this));
										}
									}]}
								 });
								$('.selectpicker').selectpicker();
								datetime();
								validateForm();
								createTree();
							}
						});
					});
				}

				function update($dialog) {
					if (!$("#form1").valid()) {
						return false;
					}
					var exam = $("#form1").formToJson();
					ajax({
						url : "/exam/update",
						async : true,
						type : "POST",
						data : JSON.stringify(exam),
						callback : function(data) {
							$dialog.trigger('close');
							clickCurPaget();
						}
					});
				}

				function deletes() {
					$(".delete")
							.click(
									function() {
										var examId = $(this).attr("objectId");
										ajax({
											url : "/exam/delete/" + examId,
											async : true,
											callback : function(data) {
												if (data.hasTestPaper
														|| data.hasExamStudent) {
													var message = "考试存在试卷和报名库，删除失败";
													dialog.fadedialog({
														icon_info : "error",
														tip_txt : message
													});
												} else {
													dialog.fadedialog({
														icon_info : "success",
														tip_txt : "删除成功!"
													});
													clickCurPaget();
												}
											}
										});

									});
				}
				function view() {
					$(".view").click(function() {
						var examId = $(this).attr("objectId");
						openView(examId);
					});
				}

				function openView(examId, callBack) {
					ajax({
						url : "/exam/view/" + examId,
						async : true,
						dataType : "html",
						callback : function(html) {
							$(".main-container-inner").html(html);
							resizeHeight();
							menuExamStudentList();
							menuExamInfo();
							menuTestPaperItem();
							menuTestPaperCj();
							menuParameter();
							info(callBack);
							lefttree();
						}
					});
				}

				function info(callBack) {
					var examId = $("#examId").val();
					ajax({
						url : "/exam/info/" + examId,
						async : true,
						dataType : "html",
						callback : function(html) {
							$(".main-content").html(html);
							testPaper.list();
							if (callBack) {
								callBack();
							}
						}
					});
				}

				function menuTestPaperItem() {
					$("#testPaperItem").click(function() {
						item.list();
					});
				}
				function menuTestPaperCj() {
					$("#testPaperCj").click(function() {
						cj.list();
					});
				}

				function menuExamInfo() {
					$("#examInfo").click(function() {
						info();
					});
				}
				function menuExamStudentList() {
					$("#examStudent").click(function() {
						examStudent.list();
					});
				}
				function menuParameter() {
					$("#examParameter").click(function() {
						parameter.view();
					});
				}

				function resizeHeight() {

					var cH = document.documentElement.clientHeight
							- $('.header').height();

					$('#content .content').height(cH);

					var iH = cH - $('.left-side .heading').height();

					$('.nav-box').height(iH);

				}

				function search() {
					$("#search").click(function() {
						var page = {};
						page["pagesize"] = g_pageSize;
						page["curpage"] = 1;
						skipPage(page);
					});
				}

				function createSearchObject() {
					var parameter = {};
					var examName = $("#examName").val();
					parameter["examName"] = examName;
					return parameter;
				}

				function skipPage(page) {
					var parameter = createSearchObject();
					page["parameter"] = parameter;
					list(page);
				}

				function renderPager() {
					var _pageNum = parseInt($("#pageNum").val());
					var _pageCount = parseInt($("#pageCount").val());
					$("#pager").pager({
						pageNum : _pageNum,
						pageCount : _pageCount,
						click : function(pageNum, pageCount) {
							var page = {};
							page["pagesize"] = g_pageSize;
							page["curpage"] = pageNum;
							skipPage(page);
						}
					});
				}

				function clickCurPaget() {
					var curPage = parseInt($("#pager").getCurPage());
					var page = {};
					page["pagesize"] = g_pageSize;
					page["curpage"] = curPage;
					skipPage(page);
				}

				function list(page) {
					if (!page) {
						page = {};
						page["pagesize"] = g_pageSize;
						page["curpage"] = 1;
					}
					ajax({
						url : "/exam/list/",
						async : true,
						type : "POST",
						dataType : "html",
						data : JSON.stringify(page),
						callback : function(html) {
							$(".main-content").html(html);
							newAdd();
							newUpdate();
							deletes();
							view();
							search();
							renderPager();
							importExamStudent();
							anlaysisExam();
						}
					});
				}

				function anlaysisExam() {
					$(".anlaysisExam")
							.click(
									function() {
										var examId = $(this).attr("objectId");
										var exam = {
											id : examId
										};
										ajax({
											url : "/syndata",
											async : true,
											type : "POST",
											data : JSON.stringify(exam),
											beforeSendMsg : {
												backdrop : false,
												tip_txt : '正在分析数据，请不要关闭浏览器和刷新...',
												icon_info : 'updata-ing',
												untransparent : true
											},
											callback : function(data) {
												dialog.tipmodal({
													footer:{
														show:true,
														buttons:[
															{type:'button',txt:"确定",sty:'default',callback:function(){$(this).trigger('close')}}
														]
													},
													tip_txt:'分析成功',
													icon_info:'success',
												});	
												
												clickCurPaget();
											}
										});
									});
				}

				function importExamStudent() {
					$(".importExamStudent").click(function() {
						var examId = $(this).attr("objectId");
						openView(examId, function() {
							etlExecutor.newProcessor({
								tableName : "tb_examstudent",
								contentObj : $(".page-content"),
								examId : $("#examId").val(),
								title : "导入考试学生信息"
							});
						});

					});
				}

				var o = {
					render : function() {
						list();
						user.setpwd($('#setpwd'));
					}
				};

				return o;

			});

})();