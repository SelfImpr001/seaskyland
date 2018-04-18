(function() {
	"use strict";
	define(
			[ 'jquery', 'ajax', 'formToJosn', "dialog", "controller",
					'validatejs',"ajaxfileupload" ],
			function($, ajax, form, dialog, ctrl,ajaxfileupload) {
				var self = undefined;
				function newAdd() {
					ctrl.getHtmlDialog(("apply/newAdd"), "big-modal",
							function(html) {
								var $htmlObj = $(html);
								ctrl.modal("应用设置>新增", html, function() {
									var $form = $(html).find('#applyform');
									var systemName = $('#systemName').val();
									var loginIcon =$('#uploadImgLogin').attr("src");
									var handleIcon = $('#uploadImgHandle').attr("src");
									var order = $('#order').val();
									var status=$("input:radio[name='status']:checked").val();
									if (systemName.length<1) {
										ctrl.moment("系统名称不能为空","warning");
										return false;
									}
									if(loginIcon.length<1){
										ctrl.moment("登录界面图标不能为空","warning");
										return false;
									}
									if(loginIcon.length>50000){
										ctrl.moment("你所上传的图片太大","warning");
										return false;
									}
									if(handleIcon.length<1){
										ctrl.moment("操作界面图标不能为空","warning");
										return false;
									}
									if(handleIcon.length>50000){
										ctrl.moment("你所上传的图片太大","warning");
										return false;
									}
									if (order.length<1) {
										ctrl.moment("排序号不能为空","warning");
										return false;
									}
									else{
										var apply = {
												systemName : systemName,
												loginIcon : loginIcon,
												handleIcon : handleIcon,
												order : order,
												status : status
											};
										ctrl.postJson("apply/addApply", apply,
												"新增应用成功!", function() {
													self.render();
												});
										return true;
									}
									return false;
								}, "保存");
								changeHandle();
								changeLogin();
								changeSystemName();
								defaultApplyLogin($htmlObj);
								defaultApplyHandle($htmlObj);
							});
				}
				
				function deletes(pk) {
//					ctrl.remove("apply/delete", "确定要删除吗？", "删除成功", {
//						id : pk
//					}, function() {
////						var subjectName = self.container.find('input[name="subjectName"]').val();
////						findSubject(subjectName);
//					});
					ctrl.postJson("apply/delete/"+pk,"",
							"删除成功!", function() {
						ctrl.getHtml("apply/list?applyName=",function(html){
							   ctrl.refreshDataGrid('apply_data_rows',self.container,$(html));
			        	   });
					});
				}

				function newUpdate(pk) {
					ctrl.getHtmlDialog(("apply/newUpdate/"+pk), "big-modal",
							function(html) {
								var $htmlObj = $(html);
								ctrl.modal("应用设置>编辑", html, function() {
									var $form = $(html).find('#applyform');
									var systemName = $('#systemName').val();
									var loginIcon =$('#uploadImgLogin').attr("src");
									var handleIcon = $('#uploadImgHandle').attr("src");
									var order = $('#order').val();
									var status=$("input:radio[name='status']:checked").val();
									if (systemName.length<1) {
										ctrl.moment("系统名称不能为空","warning");
										return false;
									}
//									if(loginIcon.length<1){
//										ctrl.moment("登录界面图标不能为空","warning");
//										return false;
//									}
									if(loginIcon.length>50000){
										ctrl.moment("你所上传的图片太大","warning");
										return false;
									}
//									if(handleIcon.length<1){
//										ctrl.moment("操作界面图标不能为空","warning");
//										return false;
//									}
									if(handleIcon.length>50000){
										ctrl.moment("你所上传的图片太大","warning");
										return false;
									}
									if (order.length<1) {
										ctrl.moment("排序号不能为空","warning");
										return false;
									}
									else{
										var apply = {
												id : pk,
												systemName : systemName,
												loginIcon : loginIcon,
												handleIcon : handleIcon,
												order : order,
												status : status
											};
											ctrl.putJson("apply/applyUpdate", apply,
													"应用修改成功!", function() {
														self.render();
													});
											return true;
									}
									return false;
								}, "保存");
								changeHandle();
								changeLogin();
								changeSystemName();
								defaultApplyLogin($htmlObj);
								defaultApplyHandle($htmlObj);
							});
				}
				//恢复默认(登录界面)
				function defaultApplyLogin($htmlObj){
					$htmlObj.on("click","#loginDefault a",function(){
						var id = $('#id').val();
						if(id==undefined){
							 $("#uploadImgLogin").attr("src","");
							 changeURL();
						}else{
							 ctrl.postJson("apply/defaultApply/"+id+"/login","","恢复成功！", function() {
								 $("#uploadImgLogin").attr("src","");
								 changeURL();
							 });
						}
						
					})
				}
				//恢复默认(操作界面)
				function defaultApplyHandle($htmlObj){
					$htmlObj.on("click","#handleDefault a",function(){
						var id = $('#id').val();
						if(id==undefined){
							 $("#uploadImgHandle").attr("src","");
							 changeURL();
						}else{
							 ctrl.postJson("apply/defaultApply/"+id+"/handle","","恢复成功！", function() {
								 $("#uploadImgHandle").attr("src","");
								 changeURL();
							 });
						}
						
					})
				}
				

				function findApplyName(applyName) {
						   ctrl.getHtml("apply/list?applyName="+applyName,function(html){
							   ctrl.refreshDataGrid('subject_data_rows',self.container,$(html));
			        	   });
				}

				function newInit() {
					ctrl.getHtml("subject/newInit",function(htmlStr) {
						ctrl.modal("应用设置>初始化应用",htmlStr,function() {
							var $form = $("body").find('#subjectInit');
							return initSubject($form);
						},"保存");
					});
				}
				
				function initSubject(applyform) {
					var s = [];
					applyform.find('input[name="subjectType"]:checked').each(function(i) {
						s[i]=$(this).val();
					});
					if (s.length < 1) {
						ctrl.moment("请勾选至少一个年段以供初始化应用","warning");
						return false;
					}
					ctrl.postJson("subject/initSubject/", s,
							"初始化应用成功!", function() {
									self.render();
							});
					return true;
				}
				//登录界面图片上传
				function changeLogin(){
					$("#fileLogin").change(function(){
					  var file = document.getElementById('fileLogin');
			          var MAXWIDTH  = 260; 
			          var MAXHEIGHT = 140;
			          var div = document.getElementById('preview');
			          if (file.files && file.files[0]) {
			        	  if(validate(file.files[0].name)){
				            div.innerHTML ='<img id=uploadImgLogin>';
				            var img = document.getElementById('uploadImgLogin');
				            img.onload = function(){
					            var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
					            img.width  =  rect.width;
					            img.height =  rect.height;
								img.style.marginLeft = rect.left+'px';
					            img.style.marginTop = rect.top+'px';
				            }
				          var reader = new FileReader();
				          reader.onload = function(evt){
				        	 logoImageAdd(evt.target.result,"login");
				         	 img.src = evt.target.result;
				         	 var systemName=document.getElementById("systemName").value;
				         	 if(systemName==""){
				         		systemName="-1";
				         	 }
				         	 document.getElementById('loginHref').href="priview/loginLY/logoImage"+"/"+systemName;
				          }
				          reader.readAsDataURL(file.files[0]);
			           }else{
			           	  alert("请选择正确格式的图标上传");
			           }
			        }
					});
			      }
				//操作界面图片
				function changeHandle(){
					$("#fileHandle").change(function(){
					var file = document.getElementById('fileHandle');
					var MAXWIDTH  = 260; 
		            var MAXHEIGHT = 140;
		            var div = document.getElementById('preview2');
		            if (file.files && file.files[0]) {
		                if(validate(file.files[0].name)){
			            div.innerHTML ='<img id=uploadImgHandle>';
			            var img = document.getElementById('uploadImgHandle');
			            img.onload = function(){
			                var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
			                img.width  =  rect.width;
			                img.height =  rect.height;
			                img.style.marginLeft = rect.left+'px';
			                img.style.marginTop = rect.top+'px';
			            }
		                var reader = new FileReader();
		                reader.onload = function(evt){
		                	 logoImageAdd(evt.target.result,"handle");
				         	 img.src = evt.target.result;
				         	 var systemName=document.getElementById("systemName").value;
				         	 if(systemName==""){
				         		systemName="-1";
				         	 }
				         	 document.getElementById('handleHref').href="priview/mainLY/logoImage/"+systemName;
		                	}
		                reader.readAsDataURL(file.files[0]);
		               }else{
		               	  alert("请选择正确格式的图标上传");
		               }
		          }
			     });
		     }
			//系统名称变更
			function changeSystemName(){
				$("#systemName").change(function(){
					changeURL();
				});
		     }
			function changeURL(){
				var systemName=document.getElementById("systemName").value;
	         	 if(systemName==""){
	         		systemName="-1";
	         	 }
				if(document.getElementById('uploadImgLogin').src.length>200){
					document.getElementById('loginHref').href="priview/loginLY/logoImage"+"/"+systemName;
					logoImageAdd(document.getElementById('uploadImgLogin').src,"login");
				}else{
					document.getElementById('loginHref').href="priview/loginLY/-1"+"/"+systemName;
					logoImageAdd("","login");
				}
				if(document.getElementById('uploadImgHandle').src.length>200){
					 document.getElementById('handleHref').href="priview/mainLY/logoImage/"+systemName;
					 logoImageAdd(document.getElementById('uploadImgHandle').src,"handle");
				}else{
					 document.getElementById('handleHref').href="priview/mainLY/-1/"+systemName;
					 logoImageAdd("","handle");
				}
			}
		     function validate(name){
	        	var gs=name.split(".");
	        	if(gs.length>1){
	        		var g=gs[1];
	        		if(("JPEG,TIFF,RAW,BMP,PNG,GIF,JPG").indexOf(g)>-1 || ("jpeg,tiff,raw,bmp,png,gif,jpg").indexOf(g)>-1 ){
	        		  return true;
	        		}else{
	        		  return false;
	         	    }
	        	}else{
	        		return false;
	        	}
		     } 
		     //上传时保存临时文件，方便预览
		     function logoImageAdd(baseImage,type){
		    	var loginIcon="";
		    	var handleIcon="";
		    	if(type=="login"){
		    		loginIcon=baseImage;
		    	}else{
		    		handleIcon=baseImage;
		    	}
				var apply = {
						loginIcon : loginIcon,
						handleIcon : handleIcon,
					};
		    	 ctrl.postJson("apply/justImageAdd/"+type,apply,"", function() {
				 });
		     }
		     function clacImgZoomParam( maxWidth, maxHeight, width, height ){
	            var param = {top:0, left:0, width:width, height:height};
	            if( width>maxWidth || height>maxHeight )
	            {
	               var rateWidth = width / maxWidth;
	               var rateHeight = height / maxHeight;
	                 
	                if( rateWidth > rateHeight )
	                {
	                    param.width =  maxWidth;
	                    param.height = Math.round(height / rateWidth);
	                }else
	                {
	                    param.width = Math.round(width / rateHeight);
	                    param.height = maxHeight;
	                }
	            }
	            param.left = Math.round((maxWidth - param.width) / 2);
	            param.top = Math.round((maxHeight - param.height) / 2);
	            return param;
	        }
				
		     
		     function bindEvent() {
					var $obj = self.container;
					$obj.on('click',"#apply_datagrid div.query-form a:eq(0)",function(){
						var applyName = $obj.find('input[name="systemName"]').val();
						ctrl.getHtml("apply/list?applyName="+applyName,function(html){
							   ctrl.refreshDataGrid('apply_data_rows',self.container,$(html));
			        	   });
						
					});
					$obj.on('click',"#subject_datagrid div.query-form a:eq(1)",function(){
						$obj.find('input[name="subjectName"]').val("");
		        	});
					
					$obj.on('click',"#apply_create",function(){
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