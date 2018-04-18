(function() {
	"use strict";
	define([ 'ajax', 'validatejs', 'dialog' ,"controller"], function(ajax, validatejs, dialog,ctrl) {
		var self = undefined;
		function newAdd() {
			ctrl.getHtmlDialog(("biInfo/addBiInfo"), "big-modal",
					function(html) {
						ctrl.modal("BI系统设置>新增", html, function() {
							var $form = $(html).find('#biInfoForm');
							var name = $form.find("input[name=name]").val();
							var url = $form.find("input[name=url]").val();
							var remark = $form.find("input[name=remark]").val();
							 if(name.length<1){
								ctrl.moment("请输入SmartBI用户","warning");
								return false;	
							 }else if (url.length < 1) {
								ctrl.moment("请输入SmartBI访问地址","warning");
								return false;
							}else{
								var biInfo = {
										name : name,
										url : url,
										remark:remark,
								};
								ctrl.postJson("biInfo", biInfo,
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
			ctrl.getHtmlDialog(("biInfo/updateBiInfo/"+pk), "big-modal",
					function(html) {
						ctrl.modal("BI系统设置>编辑", html, function() {
							var $form = $(html).find('#biInfoForm');
							var name = $form.find("input[name=name]").val();
							var url = $form.find("input[name=url]").val();
							var remark = $form.find("input[name=remark]").val();
						    if(name.length<1){
								ctrl.moment("请输入SmartBI用户","warning");
								return false;	
							}else if (url.length < 1) {
									ctrl.moment("请输入SmartBI访问地址","warning");
								return false;
							}else{
									var biInfo = {
										id:pk,
										name : name,
										url : url,
										remark:remark,
									};
									ctrl.putJson("biInfo", biInfo,
											"修改成功!", function() {
												self.render();
											});
									return true;
							}
							return false;
						}, "保存");
					});
		}
		
		function cleanPool() {
			ctrl.remove("biInfo/cleanPool", "确定要清空smartbi连接池吗？", "清空smartbi连接池成功", {
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
					cleanPool();
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