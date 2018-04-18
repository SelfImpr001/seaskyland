(function() {
	"use strict";
	define(
			[ 'jquery', 'ajax', 'formToJosn', "dialog", "controller",
					'validatejs' ],
			function($, ajax, form, dialog, ctrl) {
				var self = undefined;
				function newAdd() {
					ctrl.getHtmlDialog(("grade/newAdd"), "big-modal",
							function(html) {
								ctrl.modal("年级设置>新增", html, function() {
									var $form = $(html).find('#gradeform');
									var gradeName = $form.find("input[name=gradeName]").val();
									if (gradeName.length < 1) {
										ctrl.moment("请输入年级名称","warning");
										return false;
									}else{
										var grade = {
												name : gradeName
										};
										ctrl.postJson("grade", grade,
												"新增年级成功!", function() {
													self.render();
												});
										return true;
									}
									return false;
								}, "保存");
							});
				}
				
				function deletes(pk) {
					ctrl.remove("grade", "确定要删除吗？", "删除成功", {
						id : pk
					}, function() {
						var gradeName = self.container.find('input[name="gradeName"]').val();
						findgrade(gradeName);
					});
				}

				function newUpdate(pk) {
					ctrl.getHtmlDialog(("grade/newUpdate/"+pk), "big-modal",
							function(html) {
								ctrl.modal("年级设置>编辑", html, function() {
									var $form = $(html).find('#gradeform');
									var gradeName = $form.find("input[name=gradeName]").val();
									if (gradeName.length<1) {
										ctrl.moment("年级名称不能为空","warning");
										return false;
									}else{
										var grade = {
												id:pk,
												name : gradeName
											};
											ctrl.putJson("grade", grade,
													"年级修改成功!", function() {
														var gradeName = self.container.find('input[name="gradeName"]').val();
														findgrade(gradeName);
													});
											return true;
									}
									return false;
								}, "保存");
							});
				}


				function findgrade(gradeName) {
						   ctrl.getHtml("grade/list?gradeName="+gradeName,function(html){
							   ctrl.refreshDataGrid('grade_data_rows',self.container,$(html));
			        	   });
				}

				function bindEvent() {
					var $obj = self.container;
					$obj.on('click',"#grade_datagrid div.query-form a:eq(0)",function(){
						var gradeName = $obj.find('input[name="gradeName"]').val();
						findgrade(gradeName);
					});
					$obj.on('click',"#grade_datagrid div.query-form a:eq(1)",function(){
						$obj.find('input[name="gradeName"]').val("");
		        	});
					$obj.on('click',"#grade_create",function(){
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