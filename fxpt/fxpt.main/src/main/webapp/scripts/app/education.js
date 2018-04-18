(function() {
	"use strict";
	define(
			[ 'ajax', 'dialog', 'fromToJosn', 'validatejs' ],
			function(ajax, dialog) {
				var g_pageSize = window.app["pageSize"];
				function list(educationType, page) {
					if (!page) {
						page = {};
						page["pagesize"] = g_pageSize;
						page["curpage"] = 1;
					}

					ajax({
						url : "/education/list/" + educationType,
						async : true,
						dataType : "html",
						type : "POST",
						data : JSON.stringify(page),
						callback : function(htmlStr) {
							$(".main-content").html(htmlStr);
							renderPager();
							newAdd();
							deletes();
							newUpdate();
							search();
						}
					});
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

				function skipPage(page) {
					var parameter = createSearchObject();
					page["parameter"] = parameter;
					var educationType = $("#educationType").val();
					list(educationType, page);
				}
				function createSearchObject() {
					var parameter = {};
					var educationName = $("#educationName").val();
					parameter["educationName"] = educationName;
					return parameter;
				}

				function search() {
					$("#search").click(function() {
						var page = {};
						page["pagesize"] = g_pageSize;
						page["curpage"] = 1;
						skipPage(page);
					});
				}

				function deletes() {
					$(".delete").click(function() {
						var educationType = $("#educationType").val();
						var educationId = $(this).attr("objectId");
						ajax({
							url : "/education/delete/" + educationId,
							async : true,
							callback : function(data) {
								if (data.hasEducation || data.hasSchool) {
									var message = "关联教育局或者学校，删除失败";
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

				function newUpdate() {
					$(".update").click(function() {
						var educationId = $(this).attr("objectId");
						ajax({
							url : "/education/newUpdate/" + educationId,
							async : true,
							dataType : "html",
							callback : function(htmlStr) {
								
								var educationType=$(htmlStr).find("#type").val();
								var titleText="";
								if(educationType === "1"){
									titleText="省教育厅";
								}else if(educationType==="2"){
									titleText="地市教育局";
								}else if(educationType === "3"){
									titleText="区县教育局";
								}
								
								dialog.modal({
									canmove : true,
									size : "md",
									header : {
										txt : titleText+"-编辑"
									},
									body : htmlStr,
									footer : {
										buttons : [ {
											callback : function() {
												update($(this));
											}
										} ]
									}
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

					var education = $("#form1").formToJson();
					if (education.type === "2") {
						var parent = {
							code : $("#provinceCode").val()
						};
						education.parent = parent;
					}
					if (education.type === "3") {
						var parent = {
							code : $("#cityCode").val()
						};
						education.parent = parent;
					}

					ajax({
						url : "/education/update",
						async : true,
						type : "POST",
						data : JSON.stringify(education),
						callback : function(data) {
							clickCurPaget();
							$dialog.trigger('close');
						}
					});
				}

				function newAdd() {
					$("#newAddBtn").click(function() {
						var educationType = $("#educationType").val();
						ajax({
							url : "/education/newAdd/" + educationType,
							async : true,
							dataType : "html",
							callback : function(htmlStr) {
								var titleText="";
								if(educationType === "1"){
									titleText="省教育厅";
								}else if(educationType==="2"){
									titleText="地市教育局";
								}else if(educationType === "3"){
									titleText="区县教育局";
								}
								
								dialog.modal({
									canmove : true,
									size : "md",
									header : {
										txt : titleText+"-新增"
									},
									body : htmlStr,
									footer : {
										buttons : [ {
											callback : function() {
												add($(this));
											}
										} ]
									}
								});
								changeProvinceEducation();
								validateForm();
							}
						});
					});
				}

				function validateForm() {
					$("#form1").validate({
						onKeyup : true,
						rules : {
							code : {
								required : true

							},
							name : {
								required : true

							}
						},

						messages : {
							code : {
								required : '不能为空'
							},
							name : {
								required : '不能为空'
							}
						},
					});
				}

				function changeProvinceEducation() {
					$("#provinceEducation").change(
							function() {
								var educationType = $("#educationType").val();
								if (educationType != "3") {
									return;
								}
								var code = $(this).val();
								ajax({
									url : "/education/child/" + code,
									async : true,
									callback : function(data) {
										var options = new Array();
										for ( var i in data.educations) {
											options.push("<option value='"
													+ data.educations[i].code
													+ "'>"
													+ data.educations[i].name
													+ "</option>");
										}
										$("#cityEducation").html(
												options.join(""));
									}
								});
							});
				}

				function add($dialog) {
					if (!$("#form1").valid()) {
						return false;
					}

					var education = $("#form1").formToJson();
					if (education.type === "2") {
						var parent = {
							code : $("#provinceEducation").val()
						};
						education.parent = parent;
					}
					if (education.type === "3") {
						var parent = {
							code : $("#cityEducation").val()
						};
						education.parent = parent;
					}

					ajax({
						url : "/education/add",
						async : true,
						type : "POST",
						data : JSON.stringify(education),
						callback : function(data) {
							if (data.isExistEducation) {
								$("#code").removeClass("valid").addClass(
										"error");
								$("#codeTip")
										.append(
												"<label for='code' class='error'>代码已存在</label>");

							} else {
								$dialog.trigger('close');
								list(education.type);
							}
						}
					});
				}

				var o = {
					list : function(educationType) {
						list(educationType);
					}
				};

				return o;

			});

})();