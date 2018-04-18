
(function() {
	"use strict";
	define(
			[ 'ajax', "formToJosn", '../../lib/CodeMirror/lib/codemirror',
					'../../lib/CodeMirror/mode/groovy/groovy',
					'../../lib/CodeMirror/addon/edit/matchbrackets',
					'../../lib/CodeMirror/addon/display/fullscreen',
					'validatejs', 'dialog', 'controller' ],
			function(ajax, a, codemirror,groovy, matchbrackets, fullscreen,
					a1, dialog, ctrl) {
				var self = undefined;
				function newAdd(html) {
					var dataCategoryId = $("#dataCategoryId").val();
					ctrl.getHtmlDialog(
									("dataTransform/newAdd?dataCategoryId=" + dataCategoryId),
									"big-modal",
									function(html) {
										ctrl.modal("数据导入配置管理>新增",html,	function() {
															var $form = $(html).find('#dataTransForm');
															var dataCategoryId = $form.find("select[id=dataCategoryId]").val();
															var name = $form.find("input[id=name]").val();
															var type = $form.find("input[id=type]:checked").val();
															var valid = $form.find("input[id=valid]:checked").val();
															var content = editAreaLoader.getValue("code");
															if (name.length < 1) {
																ctrl.moment("请输入名称","warning");
																return false;
															} else {
																var dataCategory = {
																	id : dataCategoryId
																};
																var dataTransform = {
																	name : name,
																	type : type,
																	content : content,
																	valid : valid,
																	dataCategory : dataCategory
																};
																ctrl.postJson("dataTransform",dataTransform,"新增字段成功!",
																				function() {
																					findList(dataCategoryId);
																				});
																return true;
															}
															return false;
														}, "保存");
											editArea();
											
									});
				}

				
				function deletes(pk) {
					ctrl.remove("dataTransform", "确定要删除吗？", "删除成功", {
						id : pk
					}, function() {
						var dataCategoryId = self.container.find(
								'select[name="dataCategoryId"]').val();
						findList(dataCategoryId);
					});
				}
				
				function editArea(){
					editAreaLoader.init({
						id: "code"	
						,start_highlight: true	
						,allow_resize: "both"
						,language: "en"
						,syntax: "java"	,
						allow_toggle: false
					});
				}
				
				
				
				function newUpdate(pk) {
					ctrl.getHtmlDialog(("dataTransform/newUpdate/" + pk),
							"big-modal", function(html) {
								ctrl.modal("数据导入配置管理>编辑", html, function() {
									var $form = $(html).find('#dataFieldform');
									var dataCategoryId = $form.find(
											"input[id=dataCategoryId]").val();
									var name = $form.find("input[id=name]")
											.val();
									var type = $form.find(
											"input[id=type]:checked").val();
									var valid = $form.find(
											"input[id=valid]:checked").val();
									var content = editAreaLoader.getValue("code");
									if (name.length < 1) {
										ctrl.moment("请输入名称", "warning");
										return false;
									} else {
										var dataCategory = {
											id : dataCategoryId
										};
										var dataTransform = {
											id : pk,
											name : name,
											type : type,
											content : content,
											valid : valid,
											dataCategory : dataCategory
										};
										ctrl.putJson("dataTransform",
												dataTransform, "修改成功!",
												function() {
													findList(dataCategoryId);
												});
										return true;
									}
									return false;
								}, "保存");
								editArea();
							});
				}

				function findList(dataCategoryId) {
					ctrl.getHtml("dataTransform/list?dataCategoryId="
							+ dataCategoryId, function(html) {
						ctrl.refreshDataGrid('dataTransForm_data_rows',
								self.container, $(html));
					});
				}

				function bindEvent() {
					var $obj = self.container;
					$("#dataCategoryId").change(
							function() {
								var dataCategoryId = self.container.find(
										'select[name="dataCategoryId"]').val();
								findList(dataCategoryId);
							});
					$obj.on('click','#trans_create',function() {
						newAdd($obj);
					});
					$obj.on('click', 'a[trigger]', function() {
						var trigger = $(this).attr('trigger');
						var pk = $(this).attr("pk");
						switch (trigger) {
						case 'remove':
							deletes(pk);
							break;
						case 'update':
							newUpdate(pk);
							break;
						default:

						}
					});
				}

				var o = self = {
					container : undefined,
					render : function(container) {
						var url = self.getCurMenu().attr("url");
						self.log("get url " + url);
						self.getHtml(url, function(html) {
							self.container = $(html);
							ctrl.appendToView(container, self.container);
							ctrl.initUI(self.container);
							bindEvent();
						});
					}
				};
				return $.extend(o, ctrl);
			});

})();