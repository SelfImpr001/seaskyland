(function() {
	"use strict";
	define(
			[ 'jquery', 'ajax', 'formToJosn', "dialog", "controller",
					'validatejs',"ajaxfileupload" ],
			function($, ajax, form, dialog, ctrl,ajaxfileupload) {
				var self = undefined;
				function newAdd() {
					ctrl.getHtmlDialog(("subject/newAdd"), "big-modal",
							function(html) {
								ctrl.modal("科目设置>新增", html, function() {
									var $form = $(html).find('#subjectform');
									var subjectName = $form.find("input[name=subjectName]").val();
									var ordernum = $form.find("input[name=ordernum]").val();
									if (subjectName.length < 1) {
										ctrl.moment("请输入科目名称","warning");
										return false;
									}
									if(ordernum.length<1){
										ctrl.moment("请输入序号","warning");
										return false;
									}
									else{
										var subject = {
												name : subjectName,
												ordernum:ordernum
										};
										ctrl.postJson("subject", subject,
												"新增科目成功!", function() {
													self.render();
												});
										return true;
									}
									return false;
								}, "保存");
							});
				}
				
				function deletes(pk) {
					ctrl.remove("subject", "确定要删除吗？", "删除成功", {
						id : pk
					}, function() {
						var subjectName = self.container.find('input[name="subjectName"]').val();
						findSubject(subjectName);
					});
				}

				function newUpdate(pk) {
					ctrl.getHtmlDialog(("subject/newUpdate/"+pk), "big-modal",
							function(html) {
								ctrl.modal("科目设置>编辑", html, function() {
									var $form = $(html).find('#subjectform');
									var subjectName = $form.find("input[name=subjectName]").val();
									var ordernum = $form.find("input[name=ordernum]").val();
									if (subjectName.length<1) {
										ctrl.moment("科目名称不能为空","warning");
										return false;
									}
									if(ordernum.length<1){
										ctrl.moment("序号不能为空","warning");
										return false;
									}
									else{
										var subject = {
												id:pk,
												name : subjectName,
												ordernum:ordernum
											};
											ctrl.putJson("subject", subject,
													"科目修改成功!", function() {
														var subjectName = self.container.find('input[name="subjectName"]').val();
														findSubject(subjectName);
													});
											return true;
									}
									return false;
								}, "保存");
							});
				}


				function findSubject(subjectName) {
						   ctrl.getHtml("subject/list?subjectName="+subjectName,function(html){
							   ctrl.refreshDataGrid('subject_data_rows',self.container,$(html));
			        	   });
				}

				function newInit() {
					ctrl.getHtml("subject/newInit",function(htmlStr) {
						ctrl.modal("科目设置>初始化科目",htmlStr,function() {
							var $form = $("body").find('#subjectInit');
							return initSubject($form);
						},"保存");
					});
				}
				
				function initSubject(subjectForm) {
					var s = [];
					subjectForm.find('input[name="subjectType"]:checked').each(function(i) {
						s[i]=$(this).val();
					});
					if (s.length < 1) {
						ctrl.moment("请勾选至少一个年段以供初始化科目","warning");
						return false;
					}
					ctrl.postJson("subject/initSubject/", s,
							"初始化科目成功!", function() {
									self.render();
							});
					return true;
				}
				
				
				function bindEvent() {
					var $obj = self.container;
					$obj.on('click',"#subject_datagrid div.query-form a:eq(0)",function(){
						var subjectName = $obj.find('input[name="subjectName"]').val();
						findSubject(subjectName);
					});
					$obj.on('click',"#subject_datagrid div.query-form a:eq(1)",function(){
						$obj.find('input[name="subjectName"]').val("");
		        	});
					
					$obj.on('click',"#subject_create",function(){
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
						case 'initsubject':
							newInit();
							break;
						case 'import':
							newImport();
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