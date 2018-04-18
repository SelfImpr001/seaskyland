(function() {
	"use strict";
	define(
			[ 'jquery', 'ajax', 'formToJosn', "dialog", "controller",
					'validatejs' ],
			function($, ajax, form, dialog, ctrl) {
				var self = undefined;
				function newAdd() {
					ctrl.getHtmlDialog(("examType/newAdd"), "big-modal",
							function(html) {
								ctrl.modal("考试类型设置>新增", html, function() {
									var $form = $(html).find('#examTypeform');
									var examTypeName = $form.find("input[name=examTypeName]").val();
									var valid = $form.find("input[name=valid]:checked").val();
									if (examTypeName.length < 1) {
										ctrl.moment("请输入考试类型名称","warning");
										return false;
									}else{
										var examType = {
												name : examTypeName,
												valid: valid
										};
										ctrl.postJson("examType", examType,
												"新增考试类型成功!", function() {
													self.render();
												});
										return true;
									}
									return false;
								}, "保存");
							});
				}
				
				function deletes(pk) {
					ctrl.remove("examType", "确定要删除吗？", "删除成功", {
						id : pk
					}, function() {
						var examTypeName = self.container.find('input[name="examTypeName"]').val();
						findexamType(examTypeName);
					});
				}

				function newUpdate(pk) {
					ctrl.getHtmlDialog(("examType/newUpdate/"+pk), "big-modal",
							function(html) {
								ctrl.modal("考试类型设置>编辑", html, function() {
									var $form = $(html).find('#examTypeform');
									var examTypeName = $form.find("input[name=examTypeName]").val();
									var valid = $form.find("input[name=valid]:checked").val();
									if (examTypeName.length<1) {
										ctrl.moment("考试类型名称不能为空","warning");
										return false;
									}else{
										var examType = {
												id:pk,
												name : examTypeName,
												valid : valid
											};
											ctrl.putJson("examType", examType,
													"考试类型修改成功!", function() {
														var examTypeName = self.container.find('input[name="examTypeName"]').val();
														findexamType(examTypeName);
													});
											return true;
									}
									return false;
								}, "保存");
							});
				}


				function findexamType(examTypeName) {
						   ctrl.getHtml("examType/list?examTypeName="+examTypeName,function(html){
							   ctrl.refreshDataGrid('examType_data_rows',self.container,$(html));
			        	   });
				}

				function bindEvent() {
					var $obj = self.container;
					$obj.on('click',"#examType_datagrid div.query-form a:eq(0)",function(){
						var examTypeName = $obj.find('input[name="examTypeName"]').val();
						findexamType(examTypeName);
					});
					$obj.on('click',"#examType_datagrid div.query-form a:eq(1)",function(){
						$obj.find('input[name="examTypeName"]').val("");
		        	});
					$obj.on('click',"#examType_create",function(){
						newAdd();
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
							bindEvent();
						});
					}
				};
				return $.extend(o, ctrl);
			});

})();