(function() {
	"use strict";
	define(
			[ 'jquery', 'ajax', 'formToJosn', "dialog", "controller",
					'validatejs' ],
			function($, ajax, form, dialog, ctrl) {
				var self = undefined;
				function newAdd() {
					ctrl.getHtmlDialog(("clazz/newAdd"), "big-modal",
							function(html) {
								ctrl.modal("班级设置>新增", html, function() {
									var $form = $(html).find('#clazzform');
									var clazzCode = $form.find("input[name=clazzCode]").val();
									var clazzName = $form.find("input[name=clazzName]").val();
									 if(clazzCode.length<1){
										ctrl.moment("请输入班级代码","warning");
										return false;	
									 }else if (clazzName.length < 1) {
										ctrl.moment("请输入班级名称","warning");
										return false;
									}else{
										var clazz = {
												code : clazzCode,
												name : clazzName
										};
										ctrl.postJson("clazz", clazz,
												"新增班级成功!", function() {
													self.render();
												});
										return true;
									}
									return false;
								}, "保存");
							});
				}
				
				function deletes(pk) {
					ctrl.remove("clazz", "确定要删除吗？", "删除成功", {
						id : pk
					}, function() {
						var clazzName = self.container.find('input[name="clazzName"]').val();
						findclazz(clazzName);
					});
				}

				function newUpdate(pk) {
					ctrl.getHtmlDialog(("clazz/newUpdate/"+pk), "big-modal",
							function(html) {
								ctrl.modal("班级设置>编辑", html, function() {
									var $form = $(html).find('#clazzform');
									var clazzCode = $form.find("input[name=clazzCode]").val();
									var clazzName = $form.find("input[name=clazzName]").val();
									if(clazzCode.length<1){
										ctrl.moment("班级代码不能为空","warning");
										return false;
									}
									if (clazzName.length<1) {
										ctrl.moment("班级名称不能为空","warning");
										return false;
									}else{
										var clazz = {
												id:pk,
												code:clazzCode,
												name : clazzName
											};
											ctrl.putJson("clazz", clazz,
													"班级修改成功!", function() {
														var clazzName = self.container.find('input[name="clazzName"]').val();
														findclazz(clazzName);
													});
											return true;
									}
									return false;
								}, "保存");
							});
				}


				function findclazz(clazzName) {
						   ctrl.getHtml("clazz/list?clazzName="+clazzName,function(html){
							   ctrl.refreshDataGrid('clazz_data_rows',self.container,$(html));
			        	   });
				}

				function bindEvent() {
					var $obj = self.container;
					$obj.on('click',"#clazz_datagrid div.query-form a:eq(0)",function(){
						var clazzName = $obj.find('input[name="clazzName"]').val();
						findclazz(clazzName);
					});
					$obj.on('click',"#clazz_datagrid div.query-form a:eq(1)",function(){
						$obj.find('input[name="clazzName"]').val("");
		        	});
					$obj.on('click',"#clazz_create",function(){
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