(function() {
	"use strict";
	define(
			[ 'jquery', 'ajax', 'formToJosn', 'dialog', 'controller',
					'validatejs','ajaxfileupload' ],
			function($, ajax, form, dialog, ctrl,ajaxfileupload) {
				var self = undefined;
				function newAddDataCategory() {
					ctrl.getHtmlDialog(("dataCategory/newAdd"), "big-modal",
							function(html) {
								ctrl.modal("导入数据项管理>新增", html, function() {
									var $form = $(html).find('#dataCategoryform');
									var dataCategoryName = $form.find("input[name=dataCategoryName]").val();
									var dataCategoryTableName  = $form.find("input[name=dataCategoryTableName]").val();
									if (dataCategoryName.length < 1) {
										ctrl.moment("请输入数据类型名称","warning");
										return false;
									}else{
										var dataCategory = {
												name : dataCategoryName,
												tableName:dataCategoryTableName
										};
										ctrl.postJson("dataCategory", dataCategory,
												"新增数据项成功!", function() {
													self.render();
												});
										return true;
									}
									return false;
								}, "保存");
							});
				}
				
				function deleteDataCategory(pk) {
					ctrl.remove("dataCategory", "确定要删除吗？", "删除成功", {
						id : pk
					}, function() {
						self.render();
					});
				}
				
				function newUpdateDataCategory(pk) {
					ctrl.getHtmlDialog(("dataCategory/newUpdate/"+pk), "big-modal",
							function(html) {
								ctrl.modal("导入数据项管理>编辑", html, function() {
									var $form = $(html).find('#dataCategoryform');
									var dataCategoryName = $form.find("input[name=dataCategoryName]").val();
									var dataCategoryTableName = $form.find("input[name=dataCategoryTableName]").val();
									var schemeType = $form.find("input[name=schemeType]").val();
									if (dataCategoryName.length<1) {
										ctrl.moment("数据项名称不能为空","warning");
										return false;
									}else{
										var dataCategory = {
												id:pk,
												name : dataCategoryName,
												tableName:dataCategoryTableName,
												schemeType:schemeType
											};
											ctrl.putJson("dataCategory", dataCategory,
													"修改成功!", function() {
														self.render();
													});
											return true;
									}
									return false;
								}, "保存");
							});
				}
				
				function bindEvent() {
					var $obj = self.container;
					$obj.on('click','#cate_create',function() {
						newAddDataCategory();
					});
					$obj.on('click', 'a[trigger]', function() {
						var trigger = $(this).attr('trigger');
						var pk = $(this).attr("pk");
						switch (trigger) {
						case 'remove':
							deleteDataCategory(pk);
							break;
						case 'update':
							newUpdateDataCategory(pk);
							break;
						default:

						}
					});
				}
				
				var o = self = {
						container:undefined,
						render : function(container) {
							var url = self.getCurMenu().attr("url");
							self.log("get url " + url);
							self.getHtml(url,function(html) {
								self.container = $(html);
				        	    ctrl.appendToView(container,self.container);
								bindEvent();
							});
						}
					};
					return $.extend(o, ctrl);
				});

})();