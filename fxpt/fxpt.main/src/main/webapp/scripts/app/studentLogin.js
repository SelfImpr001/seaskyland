(function(){
	"use strict";
	define(['controller',"radioztree",'validatejs' ],function(ctrl,ztree){
		var validateRules = {
				validateAllOnece:false,
				rules : {
					name : {
						required : true
					},
					/*schoolName : {
						required : true
					},*/
					identity: {
						required : true
					},
					kaptcha:{
						required : true
					}
				},
				messages : {
					name : {
						required : '请输入您的姓名',
						   check : '姓名输入不正确'
					},
					/*schoolName : {
						required : '请选择您目前就读的学校'
					},*/
					identity: {
						required : '请输入您的学号',
						   check : '准考证号不合法'
					},
					kaptcha:{
						required : '请输入验证码'
					}

				},
				errorElement : "em",
				errorPlacement : function(error, element) {
					$(element).parent().addClass('has-error').removeClass('has-success');
					//$(element).parent().removeClass('right');
					$('div.modal-dialog span.tips-info-text').text(error);
				},
				success : function(label, element) {
					$(element).parent().removeClass('has-error').addClass('has-success');
					$('div.modal-dialog span.tips-info-text').text('我成功啦');
			}
		};

		//过滤 当菜单不是最后一级前面的单选框去掉
		function ajaxDataFilter(treeid,parentNode,nodes){
			$.each(nodes,function(i){
				this.nocheck = true;
			});
			return nodes;
		}
		
		
		/*function initOrgsTree() {
			ctrl.get("api/orgs","json", function(data) {
				var nodes = ajaxDataFilter("","",data.treeNodes);
				ztree({
					ztreeId : "orgTree",
					targetId : "schoolName",
					async : {
						enable : true,
						url : window.app.rootPath + "api/orgs",
						autoParam : [ "code", "type" ],
						dataFilter:ajaxDataFilter
					},
					onSelectedCb : function(nodes) {
						var node = null;
						if (nodes.length > 0) {
							node = nodes[0];
						}
						if (!node || !node.code || node.isParent) {
							return;
						}
						$("#schoolCode").val(node.code);
						$("#schoolName").val(node.name);
						$("#schoolName").parent().removeClass('has-error').addClass('has-success');
					},
					initData : nodes
				});
			}, false,false, {
				code : "",
				type : 0
			});
		};*/
		
		var o = {
			init: function(){
				
				//initOrgsTree();
				$("#kaptchaImages").click(function() {
						$(this).attr("src",window.app.rootPath+ "images/validateCode.jpg?"+ Math.floor(Math.random() * 100));
				}).next().click(function() {
					$("#kaptchaImages").click();
				});
				$('#loginForm input[type=text][name=kaptcha]').keyup(function(e){
					if(e.keyCode == 13){
						$('button').click();
					}
				});
				//var myformValidateRules = getValidateRules();
				var $form = $("#stuForm");
				ctrl.initUI($form);
				//$form.validate(validateRules);
				$form.submit(function(){
					for(var m in validateRules.rules){
						var o = validateRules.rules[m];
						if(o.required){
							var v = $form.find('input[name='+m+']');
							if(m=='name'){
								var reg = new RegExp("^([^\<\>\"\'\%\;\)\(\&\+]*)$");
								if(!reg.test(v.val())){
									validateRules.errorPlacement(validateRules.messages[m].check, v);
									return false;
								}
							}
							if(m=='identity'){
								var reg = new RegExp("^[0-9]*$");
								if(!reg.test(v.val())){
									validateRules.errorPlacement(validateRules.messages[m].check, v);
									return false;
								}
							}
							if(v.val().length == 0){									
								validateRules.errorPlacement(validateRules.messages[m].required, v);
								return false;
							}else{
								validateRules.success(validateRules.messages[m], v);
							}
						}
					}
					$('div.modal-dialog span.tips-info-text').text("正在登录，请稍候...");
				});

			},

			render: function(){
				this.init();
			}
		};
		return o;
		
	});

})();

