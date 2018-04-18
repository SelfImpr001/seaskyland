(function() {
	"use strict";
	define([ 'jquery', 'ajax', 'formToJosn', "dialog", "controller",
			'validatejs', "ajaxfileupload" ,"download"], function($, ajax, form, dialog,
			ctrl, ajaxfileupload,download) {
		var self = undefined;

		function bindEvent() {
			var $obj = self.container;
			$obj.on('click', '#saveSetting', function() {
				save($obj);
			});
			$obj.on('click', '#addSynUsers', function() {
				newadd();
			});
			$obj.on('click', '#downSynUsers', function() {
				download();
			});
			
			$obj.on('click', 'a[trigger]', function() {
				var trigger = $(this).attr('trigger');
				var pk = $(this).attr("pk");
				switch (trigger) {
				case 'remove':
					remove(pk);
					break;
				case 'update':
					update(pk);
					break;	
				default:

				}
			});
		}
		
		function update(pk){
			ctrl.getHtmlDialog(("synusers/newupdate/"+pk), "big-modal",
					function(html) {
						ctrl.modal("同步用户设置>编辑", html, function() {
							var $form = $(html).find('#synusersform');
							var id = $form.find("input[name=id]").val();
							var roleid = $form.find("#roleid  option:selected").val();
							var namingrules = $form.find("input:checkbox[name='namingRules']:checked").map(function(index,elem){return $(elem).val();}).get().join('、');
							var passwordrules = $form.find("input:radio[name=passwordrules]:checked").val();
							var isSyn =  $form.find("input:radio[name=isSyn]:checked").val();
							var defaultpassword =  $form.find("input[name=defaultpassword]").val();
							 if(roleid.length<1 || roleid=='请选择'){
								ctrl.moment("请选择一个用户对应的角色","warning");
								return false;	
							 }else if (namingrules.length < 1) {
								ctrl.moment("请勾选用户命名规则","warning");
								return false;
							 }else if (passwordrules=="one" && defaultpassword.length<1){
								ctrl.moment("请填写固定密码","warning");
								return false;
							 }else{
								var synUsers = {
								id:id,
								roleid:roleid,
								namingrules:namingrules,
								passwordrules:passwordrules,
								defaultpassword:defaultpassword,
								isSyn:isSyn
								};
								ctrl.postJson("synusers/update", synUsers,
										"同步用户设置修改成功!", function() {
											self.render();
										});
							}
							 return true;
						}, "保存");
					});
		}
		
		function remove(pk){
			ctrl.remove("synusers", "确定要删除吗？", "删除成功", {
				id : pk
			}, function() {
				self.render();
			});
		}
		
		function download(){
			window.location.href=window.app.rootPath+"synusers/download";
		}
		
		function newadd(){
			ctrl.getHtmlDialog(("synusers/newAdd"), "big-modal",
					function(html) {
						ctrl.modal("同步用户设置>新增", html, function() {
							var $form = $(html).find('#synusersform');
							var roleid = $form.find("#roleid  option:selected").val();
							var namingrules = $form.find("input:checkbox[name='namingRules']:checked").map(function(index,elem){return $(elem).val();}).get().join('、');
							var passwordrules = $form.find("input:radio[name=passwordrules]:checked").val();
							var isSyn =  $form.find("input:radio[name=isSyn]:checked").val();
							var defaultpassword =  $form.find("input[name=defaultpassword]").val();
							 if(roleid.length<1 || roleid=='请选择'){
								ctrl.moment("请选择一个用户对应的角色","warning");
								return false;	
							 }else if (namingrules.length < 1) {
								ctrl.moment("请勾选用户命名规则","warning");
								return false;
							 }else if (passwordrules=="one" && defaultpassword.length<1){
								ctrl.moment("请填写固定密码","warning");
								return false;
							 }else{
								var synUsers = {
								roleid:roleid,
								namingrules:namingrules,
								passwordrules:passwordrules,
								defaultpassword:defaultpassword,
								isSyn:isSyn
								};
								ctrl.postJson("synusers/save", synUsers,
										"新增同步用户设置成功!", function() {
											self.render();
										});
							}
							 return true;
						}, "保存");
					});
		}
		
		
		
		function save($obj){
			var status = 0;
			if($('#synusers #status').is(':checked')){
				status=1;
			}
			$("#synusers tr#settings").each(function(){
				var $this= $(this);
				var roleid = $this.find("#roleid option:selected").val();
				var namingrules = $this.find("input:checkbox[name='namingRules']:checked").map(function(index,elem){return $(elem).val();}).get().join('、');
				var passwordrules = $this.find("input:radio[name='passwordrules']:checked").val();
				var defaultpassword = $this.find("#defaultpassword").val();
				var synUsers = {
						roleid:roleid,
						namingrules:namingrules,
						passwordrules:passwordrules,
						defaultpassword:defaultpassword,
						isSyn:status
				};
				ctrl.postJson("synusers/save",synUsers,"设置成功",function(){
					self.render();
				});
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
					bindEvent();
				});
			}
		};
		return $.extend(o, ctrl);
	});

})();