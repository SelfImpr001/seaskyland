(function() {
	"use strict";
	define([ 'dialog' ,"controllers",'validatejs','cookie' ], function(dialog,ctrl) {
		var validateRules = {
			rules : {
				username : {
					required : true
				},
				password : {
					required : true,
					minlength : 3
				}

			},
			messages : {
				username : {
					required : '请输入用户名'
				},
				password : {
					required : '请输入密码',
					minlength : '密码最小长度为3位'
				}

			},
			errorElement : "em",
			errorPlacement : function(error, element) {
				$(element).parent().addClass('error').removeClass('right');
				$(element).parent().removeClass('right');
				error.appendTo($('p.error-tip'));
				//$('#loginForm').find('button').removeProp('disabled');
			},
			success : function(label, element) {
				$(element).parent().removeClass('error').addClass('right');
//				$('#loginForm button').prop('disabled','disabled');
//				$($('p.error-tip em:visible').text('')[0]).text('正在验证身份信息，请稍候...');
//				$('body').css({cursor:'wait'});
				//remembered();
			}
		};
		
		function validate($form){
			var b = true;
			var re = /^\w{6,16}$/g;
			var paocode = $form.find("input[name=password]").val();
			var Repeatcode = $form.find("input[name=repassword]").val();
			var proname = $form.find("input[name=name]").val();
			var id = $form.find("input[name=id]").val();
			if(paocode.length==0){
				ctrl.moment("请输入密码！","warning");
				$form.find("#code").focus();
				b=false;
				return false;
			}
			else if(paocode.length<6||paocode.length>18){
				ctrl.moment("密码长度为6-18位！","warning");
				$form.find("#code").focus();
				b=false;
				return false;
			}else if(!re.test(paocode)){
				ctrl.moment("只允许6-16位数字、字母或者下划线！","warning");
				$form.find("#code").focus();
				b=false;
				return false;
			}
			/*else if(checkRepeat(id,paocode,proname)){
				ctrl.moment("该密码非法！请重新输入！","warning");
				$form.find("#code").focus();
				b=false;
				return false;
			}else if(checkPwd(id,paocode)){
				ctrl.moment("该密码不能与当前密码一致！","warning");
				$form.find("#code").focus();
				b=false;
				return false;
			}else*/ if(Repeatcode.length==0){
				ctrl.moment("请再次确认密码！","warning");
				$form.find("#code").focus();
				b=false;
				return false;
			}
			else if(Repeatcode!=paocode){
				ctrl.moment("确认密码与密码不一致！","warning");
				$form.find("#code").focus();
				b=false;
				return false;
			}
			return b;
		}

		function doLogin(){
			var $form = $('#loginForm');
			$form.find('button').prop('disabled','disabled');
			var username = $('#loginForm input[type=text][name=username]');
			var password = $('#loginForm input[type=password][name=password]');
			var rememberMe = $('#loginForm :checked[name=rememberMe]');
			var kaptcha = $('#loginForm input[type=text][name=kaptcha]');
			var data = {};
			data["username"] = username.val();
			data["password"] = password.val();
			data["rememberMe"] = rememberMe.val();
			data["kaptcha"] = kaptcha.val();
			//$form.submit();
			return ;
			$.ajax({
				url : 'login',
				data :  JSON.stringify(data),
				dataType : 'json',
				contentType: 'application/json',
				method : "POST",
				beforeSend:function(){
					$($('p.error-tip em:visible').text('')[0]).text('正在验证身份信息，请稍候...');
				},
				complete: function(){
					$form.find('button').removeProp('disabled');
					//$('p.error-tip em:visible').text('');
				},
				success : function(data,textStatus,jqXHR){
					if(data.status.success){
						//remembered();
						$('<form action="'+window.app.rootPath+'" method="get"/>').appendTo($('body')).submit();
					}else{
						if(data.status.code == 'Cntest-01001'){
							$($('p.error-tip em:visible').text('')[0]).text(data.status.msg);
							kaptcha.val('').focus();
						}else if(data.status.code == 'Cntest-01002'){
							$($('p.error-tip em:visible').text('')[0]).text(data.status.msg);
							username.focus();
						}
					}					
				},
				error: function(xhr,textStatus){
					$($('p.error-tip em:visible').text('')[0]).text(data.status.msg);
				}
			});
			return false;
		};
		
		function remembered(){
			var rememberMe = $('#loginForm :checked[name=rememberMe]');
			if(rememberMe.size() == 0)
				return;
			
			var username = $('#loginForm input[type=text][name=username]').val();
			var password = $('#loginForm input[type=password][name=password]').val();
			
			if(rememberMe.prop('checked')){
				var expire = $('input[type=hidden][name=expires]').val();
				$.cookie('rememberMe',"true",{expire:expire});
				$.cookie('username',username,{expire:expire});
				$.cookie('password',password,{expire:expire});
			}else{
				$.cookie('rememberMe',"false",{expire:-1});
				$.cookie('username',username,{expire:-1});
				$.cookie('password',password,{expire:-1});
			}
		};
		
		function getValidateRules() {
			var kaptcha = $('#loginForm input[name=kaptcha]');
			if (kaptcha.size() > 0) {
				validateRules.rules['kaptcha'] = {
					minlength : 4,
					required : true
				};

				validateRules.messages['kaptcha'] = {
					required : '请输入验证码',
					minlength : '验证码长度为4位'
				};
			}

			return validateRules;
		};

		  var passwordRules = {
					rules : {
						password : {
							required : true,
							minlength : 6
						},
		   		repassword :{
		   				 required:true,
		   				 minlength : 6,
		   				 equalTo:"#password"
		   			 }
					},
					
					messages : {
						password : {
							required : '请输入密码',
							minlength : '密码最小长度为6位'
						},
						repassword:{
							require :"请输入密码",
							minlength:'密码最小长度为6位',
							equakTo :"两次密码不一致"
						}
					}			
			    }; 
		
		var o = {
			init : function() {
				var myformValidateRules = getValidateRules();
				var $form = $("#loginForm");
				$form.validate(myformValidateRules);
				$("#reset").click(function(){
					$("#loginForm input[type=text],#loginForm input[type=password]").val('');
					$("#loginForm input[type=checkbox]")[0].checked = true;
					$("#loginForm p.error-tip em").text('');
				});
//				$("#login").click(function(){
//					$('#loginForm button').prop('disabled','disabled');
//				});
				$("#kaptchaImages").click(
						function() {
							$(this).attr(
									"src",
									window.app.rootPath
											+ "images/validateCode.jpg?"
											+ Math.floor(Math.random() * 100));
						}).next().click(function() {
					$("#kaptchaImages").click();
				});
				
				/**
				$('#loginForm input[type=text][name=kaptcha]').keyup(function(e){
					if(e.keyCode == 13){
						$("#login").click();
					}
				});
				**/
				if($.cookie('rememberMe') == 'true'){
					$('#loginForm input[type=text][name=username]').val($.cookie('username'));
					$('#loginForm input[type=password][name=password]').val($.cookie('password'));
				}
				var islogin = $("#islogin").val();
				
				if(islogin){
					
					var pk = $(this).attr("data-tt-value");
					ctrl.getHtmlDialog(("getUser/" + islogin), "pwd-modal",function(html) {
						var $htmlObj = $(html);
						ctrl.initUI($htmlObj);
						var $form = html.find('#form1')
						var $loginForm = $('#loginForm');
						$("#islogin").val("");
						 $form.on('click','button.btn-primary',function(){
							
								if($form.valid()){
									//var user = $form.formToJson();
									var user={name: $form.find("#name").val(), password: $form.find("#password").val(), pk: $form.find("#pk").val()};
									ctrl.putJson("updateIslogin",user,"密码修改成功！",function(){
										$loginForm.find("input[name=username]").val(user.name);
										$("#pwd-modal").modal("hide");
									});
								}
								return false;
							}).validate(passwordRules);
				/*		
					ctrl.modal("初始密码用户", html, function() {
						var flag = validate($form)
									if( flag){
										
										var pro = $form.serializeArray();
										
										var user={name: $form.find("#name").val(), password: $form.find("#password").val(), pk: $form.find("#pk").val()};
										ctrl.putJson("updateIslogin",user,"密码修改成功！",function(){
											$loginForm.find("input[name=username]").val(user.name);
											$loginForm.find("input[name=password]").val(user.password);
											$loginForm.submit();
										});
									}
								return  flag;
						}, "确定");*/
					});
			}
			},
			relogin : function(obj) {
				dialog.tipmodal({
					header : {
						show : true,
						txt : "登录超时"
					},
					tip_txt : "登录超时，请重新登录！",
					canmove : true, // 是否拖动
					icon_info : 'warning',
					footer : {
						show : true,
						buttons : [ {
							type : 'submit',
							txt : "确定",
							sty : 'primary',
							callback : function() {
								window.location.href = window.app.rootPath;
							}
						}, {
							type : 'button',
							txt : "取消",
							sty : 'default',
							callback : function() {
								$(this).trigger('close');
							}
						} ]
					}
				});
			},
			render : function() {
				this.init();
			},
			redraw : function(html) {
				var $html = $(html);
				var $loginWrapper = $html.find("div.login-inner-wrapper");

				dialog.modal({
					size : 'md',
					body : $loginWrapper,
					footer : {
						show : false
					}
				});
				this.init();
			}
		};
		return o;
	});

})();