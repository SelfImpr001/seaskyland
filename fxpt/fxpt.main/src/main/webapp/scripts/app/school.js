(function() {
	"use strict";
	define(
			[ 'ajax', 'formToJosn', "./etl/etlExecutor", 'validatejs','dialog' ],
			function(ajax, a, etlExecutor, b,dialog) {
				var g_pageSize = window.app["pageSize"];
				function list(page) {
					if (!page) {
						page = {};
						page["pagesize"] = g_pageSize;
						page["curpage"] = 1;
					}

					ajax({
						url : "/school/list",
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
							importSchool();
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

				function search() {
					$("#search").click(function() {
						var page = {};
						page["pagesize"] = g_pageSize;
						page["curpage"] = 1;
						skipPage(page);
					});
				}

				function skipPage(page) {
					var parameter = createSearchObject();
					page["parameter"] = parameter;
					list(page);
				}
				function createSearchObject() {
					var parameter = {};
					var educationName = $("#educationName").val();
					parameter["educationName"] = educationName;
					var schoolName = $("#schoolName").val();
					parameter["schoolName"] = schoolName;
					return parameter;
				}

				function importSchool() {
					$("#importSchool").click(function() {
						etlExecutor.newProcessor({
							tableName : "tb_school",
							contentObj : $(".page-content"),
							title : "导入学校信息",
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

				function newAdd() {
					$("#newAddBtn").click(function() {
						ajax({
							url : "/school/newAdd",
							async : true,
							dataType : "html",
							callback : function(htmlStr) {
								dialog.modal({
								 	canmove:true,
								 	size:"md",
								 	header: {
										txt: "学校管理-新增"
									},
								 	body:htmlStr,
								 	footer:{buttons:[{
										callback: function() {
											add($(this));
										}
									}]}
								 });
								changeProvinceEducation();
								changeCityEducation();
								$("#provinceEducation").change();
								validateForm();
							}
						});
					});
				}

				function add($dialog) {
					if (!$("#form1").valid()) {
						return false;
					}
					var school = $("#form1").formToJson();
					var education = {
						code : $("#countyEducation").val()
					};
					school.education = education;
					ajax({
						url : "/school/add",
						async : true,
						type : "POST",
						data : JSON.stringify(school),
						callback : function(data) {
							if (data.isExistSchool) {
								$("#code").removeClass("valid").addClass("error");
								$("#codeTip")
										.append(
												"<label for='code' class='error'>代码已存在</label>");
							} else {
								list();
								$dialog.trigger('close');
							}
						}
					});
				}

				function deletes() {
					$(".delete")
							.click(
									function() {
										var schoolId = $(this).attr("objectId");
										ajax({
											url : "/school/delete/" + schoolId,
											async : true,
											callback : function(data) {
												if (data.hasExamStudent) {
													var message = "该学校已近参与了考试，删除失败";
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
						var schoolId = $(this).attr("objectId");
						ajax({
							url : "/school/newUpdate/" + schoolId,
							async : true,
							dataType : "html",
							callback : function(htmlStr) {
								dialog.modal({
								 	canmove:true,
								 	size:"md",
								 	header: {
										txt: "学校管理-编辑"
									},
								 	body:htmlStr,
								 	footer:{buttons:[{
										callback: function() {
											update($(this));
										}
									}]}
								 });
								changeProvinceEducation();
								changeCityEducation();
								$("#provinceEducation").change();
								validateForm();
							}
						});
					});
				}

				function update($dialog) {
					if (!$("#form1").valid()) {
						return false;
					}
					var school = $("#form1").formToJson();
					var education = {
						code : $("#countyEducation").val()
					};
					school.education = education;
					ajax({
						url : "/school/update",
						async : true,
						type : "POST",
						data : JSON.stringify(school),
						callback : function(data) {
							$dialog.trigger('close');
							clickCurPaget();
						}
					});
				}

				function changeProvinceEducation() {
					$("#provinceEducation").change(
							function() {
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

										var cityCode = $("#cityEducation")
												.attr("cityCode");
										$("#cityEducation").find(
												"option[value='" + cityCode
														+ "']").attr(
												"selected", true);
										$("#cityEducation").change();
									}
								});
							});
				}

				function changeCityEducation() {
					$("#cityEducation").change(
							function() {
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
										$("#countyEducation").html(
												options.join(""));
										var countyCode = $("#countyEducation")
												.attr("countyCode");
										$("#countyEducation").find(
												"option[value='" + countyCode
														+ "']").attr(
												"selected", true);
									}
								});
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