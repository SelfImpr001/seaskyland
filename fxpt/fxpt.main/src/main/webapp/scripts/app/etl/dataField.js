(function() {
	"use strict";
	define(
			[ 'jquery', 'ajax', "formToJosn", 'dialog', 'controller',
					'validatejs'],
			function($, ajax, form, dialog, ctrl) {
				var self = undefined;
				
			function createNewPage(html) {
					var dataCategoryId = $("#dataCategoryId").val();
					ctrl.getHtmlDialog(("dataField/newAdd?dataCategoryId="+dataCategoryId), "big-modal",
							function(html) {
								ctrl.modal("导入字段管理>新增", html, function() {
									var $form = $(html).find('#dataFieldform');
									var dataCategoryId = $form.find("select[id=dataCategoryId]").val();
									var fieldName = $form.find("input[id=fieldName]").val();
									var asName = $form.find("input[id=asName]").val();
									var defaultName = $form.find("input[id=defaultName]").val();
									var valid = $form.find("input[id=valid]:checked").val();
									var need = $form.find("input[id=need]:checked").val();
									var sortNum = $form.find("input[id=sortNum]").val();
									var description = $form.find("textarea[id=description]").val();
									if (fieldName.length < 1) {
										ctrl.moment("请输入字段名称","warning");
										return false;
									}else if(defaultName.length < 1){
										ctrl.moment("请输入默认字段","warning");
										return false;
									}else if(sortNum.length < 1){
										ctrl.moment("请输入序号","warning");
										return false;
									}else{
										var dataCategory = {id:dataCategoryId};
										var dataField = {
												fieldName : fieldName,
												asName:asName,
												defaultName:defaultName,
												valid:valid,
												need:need,
												sortNum:sortNum,
												dataCategory:dataCategory,
												description:description
										};
										ctrl.postJson("dataField", dataField,
												"新增字段成功!", function() {
													findList(dataCategoryId);
												});
										return true;
									}
									return false;
								}, "保存");
						});
			}		
				
		function deletes(pk) {
			ctrl.remove("dataField", "确定要删除吗？", "删除成功", {
				id : pk
			}, function() {
				var dataCategoryId = self.container.find('select[name="dataCategoryId"]').val();
				findList(dataCategoryId);
			});
		}
		
		
		function newUpdate(pk) {
			ctrl.getHtmlDialog(("dataField/newUpdate/" + pk), "big-modal",
					function(html) {
						ctrl.modal("导入字段管理>编辑", html, function() {
							var $form = $(html).find('#dataFieldform');
							var dataCategoryId = $form.find("input[id=dataCategoryId]").val();
							var fieldName = $form.find("input[id=fieldName]").val();
							var asName = $form.find("input[id=asName]").val();
							var defaultName = $form.find("input[id=defaultName]").val();
							var valid = $form.find("input[id=valid]:checked").val();
							var need = $form.find("input[id=need]:checked").val();
							var sortNum = $form.find("input[id=sortNum]").val();
							var description = $form.find("textarea[id=description]").val();
							if (fieldName.length < 1) {
								ctrl.moment("请输入字段名称","warning");
								return false;
							}else if(defaultName.length < 1){
								ctrl.moment("请输入默认字段","warning");
								return false;
							}else if(sortNum.length < 1){
								ctrl.moment("请输入序号","warning");
								return false;
							}else{
								var dataCategory = {id:dataCategoryId};
								var dataField = {
										id:pk,
										fieldName : fieldName,
										asName:asName,
										defaultName:defaultName,
										valid:valid,
										need:need,
										sortNum:sortNum,
										dataCategory:dataCategory,
										description:description
								};
									ctrl.putJson("dataField", dataField,
											"修改成功!", function() {
												findList(dataCategoryId);
											});
									return true;
							}
							return false;
						}, "保存");
					});
		}
		function findList(dataCategoryId){
			ctrl.getHtml("/dataField/list?dataCategoryId="+dataCategoryId,function(html){
				ctrl.refreshDataGrid('dataField_data_rows',self.container,$(html));
			});
		}
		
		function bindEvent() {
					var $obj = self.container;
					$("#dataCategoryId").change(function() {
						var dataCategoryId = self.container.find('select[name="dataCategoryId"]').val();
						findList(dataCategoryId);
					});
					$obj.on('click','#filed_create',function() {
						createNewPage($obj);
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
						container:undefined,
						render : function(container) {
							var url = self.getCurMenu().attr("url");
							self.log("get url " + url);
							self.getHtml(url,function(html) {
								self.container = $(html);
				        	    ctrl.appendToView(container,self.container);
				        	    ctrl.initUI(self.container);
								bindEvent();
							});
						}
					};
					return $.extend(o, ctrl);
				});

})();