(function() {
	"use strict";
	define([ 'ajax', 'validatejs', 'dialog' ,"controller"], function(ajax, validatejs, dialog,ctrl) {
		var self = undefined;
		function newAdd() {
			ctrl.getHtmlDialog(("biUser/addBiUser"), "big-modal",
					function(html) {
						ctrl.modal("BI用户设置>新增", html, function() {
							var $form = $(html).find('#biUserForm');
							var biInfoId = $form.find("select[name=biInfoId]").val();
							var userName = $form.find("input[name=userName]").val();
							var userPassword = $form.find("input[name=userPassword]").val();
							 if(userName.length<1){
								ctrl.moment("请输入用户名","warning");
								return false;	
							 }else if (userPassword.length < 1) {
								 ctrl.moment("请输入密码","warning");
								return false;
							}else{
								var biInfo = {
										id : biInfoId,
								};
								var biUser = {
										userName:userName,
										userPassword:userPassword,
										biInfo:biInfo
								};
								ctrl.postJson("biUser", biUser,
										"新增成功!", function() {
											self.render();
										});
								return true;
							}
							return false;
						}, "保存");
					});
		}
		
		
		function newUpdate(pk) {
			ctrl.getHtmlDialog(("biUser/updateBiUser/"+pk), "big-modal",
					function(html) {
						ctrl.modal("BI用户设置>编辑", html, function() {
							var $form = $(html).find('#biUserForm');
							var biInfoId = $form.find("input[name=id]").val();
							var userName = $form.find("input[name=userName]").val();
							var userPassword = $form.find("input[name=userPassword]").val();
							 if(userName.length<1){
								ctrl.moment("请输入用户名","warning");
								return false;	
							 }else if (userPassword.length < 1) {
								 ctrl.moment("请输入密码","warning");
								return false;
							}else{
								var biInfo = {
										id : biInfoId,
								};
								var biUser = {
										id : biInfoId,
										userName:userName,
										userPassword:userPassword,
										biInfo:biInfo
								};
									ctrl.putJson("biUser", biUser,
											"修改成功!", function() {
												self.render();
											});
									return true;
							}
							return false;
						}, "保存");
					});
		}
		
		
		function deletes(pk) {
			ctrl.remove("biUser", "确定要删除吗？", "删除成功", {
				id : pk
			}, function() {
				self.render();
			});
		}
		
		function bindEvent() {
			var $obj = self.container;
			$obj.on('click','#bi_create',function() {
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