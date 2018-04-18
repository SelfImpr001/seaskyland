(function(){
	"use strict";
	define(['jquery','ajax','formToJosn',"app/user/grant","dialog","controller","app/user/userOrg",
	        'validatejs',"ztreecore","ztreecheck","jqueryPager"],function($,ajax,form,grant,dialog,ctrl,orgTree){
	    var self = undefined;

		jQuery.validator.addMethod("cellPhone",function(v,e){
			var cellPhone = /^(130|131|132|133|134|135|136|137|138|139|150|151|152|153|154|155|156|157|158|159|180|181|182|184|187|188|189|)\d{8}$/;
			return cellPhone.test(v) || this.optional(e);
		},"请输入正确的手机号码");
		
	    var validateRules = {
	    	ignore:"input[name=examLists]",
			rules : {
				name : {
					required : true
				},
				"userInfo.realName":{
					required : true
				},
				password : {
					required : true,
					minlength : 6
				},
				"userInfo.cellphone":{
					cellPhone:""
				}
			},
			messages : {
				name : {
					required : '请输入用户名'
				},
				"userInfo.realName":{
					required : '请输入姓名'
				},
				password : {
					required : '请输入密码',
					minlength : '密码最小长度为6位'
				}
			}
		}; 
	    var passwordRules = {
			rules : {
				password : {
					required : true,
					minlength : 6
				}
			},
			messages : {
				password : {
					required : '请输入密码',
					minlength : '密码最小长度为6位'
				}
			}			
	    };
	    
	    function getPager(){
	    	var pager = ctrl.newPage(self.container);
    		return pager;
	    };
	    
	    function bindEvent(container){
	    	var $obj = self.container;
	    	ctrl.initUI($obj); 
	    	$obj.find('#user_datagrid >div.query-form a.btn:eq(0)').on('click',function(){	    		
	    		self.query(getPager());
	    	});
	    	
	    	$obj.on('click','#user_datagrid tbody div.btn-group a',function(){
	    		eventTrigge($(this),container);
	    	}).on('click','h1 button',function(){
	    		eventTrigge($(this),container);
	    	});
	    	
	    };
	    
	    function eventTrigge($trigger,container){
	    	var trigger = $trigger.attr('trigger');
    		var pk = $trigger.attr("pk");
    		switch(trigger){
    		case 'update' :   			
    			self.update(pk);
    			break;
    		case 'updatePassword' :
    			self.updatePassword(pk);
    			break;
    		case 'remove':
    			var userName = $trigger.parent().parent().parent().children(":eq(2)").text();
    			self.remove(pk,userName);
    			break;
    		case 'permission':
    			grant.grantFor(pk,self,container);
    			break;
    		case 'create':
    			self.create();
    			break;
    		case 'batchGrant':
    			grant.batchGrant(self,container);
     			break;
    		case 'permissionForexam':
    			permissionForexam(pk);
    			break;
    		case 'userDetails': //用户详情
    			showUserDetails(pk,container);
    			break;
    		case 'moreDelete':
    			moreDelete();
     			break;
    		case 'synchronous':
    			synchronousPentaho();
    			break;
    		default:
    			return;
    		}
	    };
	    //同步用到pentaho
	    function synchronousPentaho(){
	    	ctrl.confirm("操作","您将同步用户到pentaho中，是否继续？",function(){
    			ctrl.postJson("user/synchronous","","同步成功！", function(data){
	    			  self.query(getPager());
	    		  });
			},"确定",function(){
				return false;
			},"取消");
	    }
	    
	    //授权
	    function permissionForexam(pk){
     	   var page = getPager();
     	   var url = "user/examList/"+pk+"/"+ page.curSize + "/" + page.pageSize;
     	   //授权考试列表
	    	ctrl.getHtmlDialog(url, "big-modal",function(html) {
	    		var page = ctrl.newPage(self.container);
				var title = "";
				title="指定考试";
				var $htmlObj = $(html);
				ctrl.initUI($htmlObj);
				selectAll($htmlObj);
				checkBtn($htmlObj);
				//考试授权
				ctrl.modal(title, html, function() {
					var exams =document.getElementsByName("examLists");
					var examids="";
					for(var i=0;i<exams.length;i++){
						if(exams[i].checked){
							examids+=exams[i].value+",";
						}
					}
					if(examids==null ||examids=="" ){
						examids=0;
					}
					ctrl.postJson("user/givePower/"+examids+"/"+pk,{},
							"", function(data) {
						  ctrl.moment(data.status.msg,"success"); 
						});    			   
				}, "确认","lg");
				$(".dialog-body").css("height","400px");
				$(".dialog-body").css("overflow","auto");
	    	});
	    	
	    }
	    function moreDelete(){
	    	//选中的个数
	    	var checkedNum=$("input[name='myChecked']:checked").length;
	    	var num = $("input[name='myChecked']");
	    	var pks="";
	    	if(checkedNum>0){
	    		//获取选中的所有值
	    		for(var i=0;i<$("input[name='myChecked']").length;i++){
	    			if(num[i].checked){
	    				//英文逗号隔开的pk串
	    				pks+=num[i].value+",";
	    			}
	    		}
	    		ctrl.confirm("操作","您将批量删除用户，是否继续？",function(){
	    			ctrl.postJson("user/moreDelete/"+pks,"","删除成功！", function(data){
		    			  self.query(getPager());
		    		  });
				},"确定",function(){
					return false;
				},"取消");
	    	}else{
	    		ctrl.moment("请选择要删除的用户！","warning");
	    	}
	    }
	    //用户列表全选
	    function selectCheckedAll(container){
	    	container.on('click','input[name="allChecked"]',function(){
	    		var examLists=document.getElementsByName("myChecked");
	    		//判断点击是 全选or全不选
	    		if(document.getElementsByName("allChecked")[0].checked){
	    			//全选
	    			for(var i=0;i<examLists.length;i++){
	    				examLists[i].checked=1;
	    			}
	    		}else{
	    			//全不选
	    			for(var i=0;i<examLists.length;i++){
	    				examLists[i].checked=0;
	    			}
	    		}
	    	});
	    }
	    //复选框选中时判断是否全选
	    function selectCheckedOne(container){
	    	container.on('click','input[name="myChecked"]',function(){
	    		var num = $("input[name='myChecked']").length;
	    		var checkedNum=$("input[name='myChecked']:checked").length;
	    		if(checkedNum==num){
	    			$("input[name='allChecked']")[0].checked=true;
	    		}else{
	    			$("input[name='allChecked']")[0].checked=false;
	    		}
	    	});
	    }
	    
	    //指定考试全选
	    function selectAll(container){
	    	container.on('click','input#checkAll',function(){
	    		var examLists=document.getElementsByName("examLists");
	    		//判断点击是 全选or全不选
	    		if(document.getElementById("checkAll").checked){
	    			//全选
	    			for(var i=0;i<examLists.length;i++){
	    				examLists[i].checked=1;
	    			}
	    		}else{
	    			//全不选
	    			for(var i=0;i<examLists.length;i++){
	    				examLists[i].checked=0;
	    			}
	    		}
	    	});
	    }
	    //单选（是否全选 选中）
	    function checkBtn(container){
	    	container.on('click','input[name="examLists"]',function(){
	    		
	    		//var checkedNum=$("input[name='examLists']checked").length;
	    		var checkedNoNum=document.getElementsByName("examLists").length;
	    		var checkedNum=document.getElementsByName("examLists");
	    		var a=0;
    			for(var i=0;i<checkedNum.length;i++){
    				if(checkedNum[i].checked){
    					a++;
    				}else{
    					document.getElementById("checkAll").checked=0;
    					break;
    				}
    			}
    			if(a==checkedNoNum){
    				document.getElementById("checkAll").checked=1;
    			}
	    	});
	    }
	    
	    function showUserDetails(pk,container){
	    	var pageindex =  self.container.find("#pageNum");
	    	if(pageindex!=undefined){
	    		pageindex = pageindex.val();
	    	}else{
	    		pageindex = 1;
	    	}
	    	
	    	var page = ctrl.newPage();
			var url = "/user/viewUI/" + pk + "/" + page.curSize + "/"
					+ page.pageSize;
			var p = self.container.find('#user_datagrid >div.query-form input:text').val();
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				ctrl.appendToView(container, $htmlObj);
				self.container = $htmlObj;
				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $htmlObj.find("#pager"),
					"callBack" : statExamDetailPage($htmlObj,pk)
				});   
				
				$htmlObj.on("click","#returnPage",function(){
					var page = ctrl.newPage();
					page["curSize"]=pageindex;
					if(p!="" && undefined !=p){
						_listUrl = "user/list/"+page.curSize+"/15?ui_all=true&q="+p;
					}else{
						_listUrl = "user/list/"+page.curSize+"/15?ui_all=true";
					}
					self.render(container,{"q":p});
				});
			});
	    }
		function statExamDetailPage($pageHtmlObj, userId) {
			return function(page) {
				page.data = {
					"q" : true
				};
				var url = "/user/viewUI/" + userId + "/" + page.curSize
						+ "/" + page.pageSize;
				ctrl.getHtml(url, function(html) {
					var $htmlObj = $(html);
					$pageHtmlObj.find("#roleAndexamDetail table tbody")
							.html($htmlObj);
					ctrl.renderPager({
						"containerObj" : $htmlObj,
						"pageObj" : $pageHtmlObj.find("#pager"),
						"callBack" : statExamDetailPage($pageHtmlObj, userId)
					});

				}, "", page.data);
			};

		}
	    
	    function renderPager() {	       	
	    	ctrl.renderPager({
				"containerObj" : self.container,
				"pageObj" : self.container.find('#user_pager'),
				"callBack":self.query
			});	    	
		}
	    
 	   function updateBelong(org,userPk,subjectIds){
		   ctrl.postJson("belong/batch/"+org+"/"+subjectIds,{"pk":-1,"user":{"pk":userPk}}); 		        		   
	   };
	   
   	   function clearUserForm(){
		  //清空基本信息
   		   document.getElementById("userEditForm").reset();
		   orgTree.reset();
	   };	    
		function refresh(data){
			ctrl.refreshDataGrid('user_data_rows',self.container,$(data),renderPager);			
		};
		var page=ctrl.newPage()
		var _listUrl = "/user/list/" + page.curSize + "/" + page.pageSize+"?ui_all=true";
		//var _listUrl = ctrl.getCurMenu().attr('url');
		var pages = ctrl.newPage();
	    var o = self = {
	       container:undefined,
	       setPage:function(index){
	    	   pages["curSize"] = index; 
	    	   _listUrl = "user/list/"+index+"/15?ui_all=true"; 
	       },
           render:function(container,data){
        	   ctrl.getHtml(_listUrl,function(html){
        		   self.container = $(html);
        		   ctrl.appendToView(container,self.container);
				   bindEvent(container);
				   renderPager();
				   selectCheckedAll(self.container);
				   selectCheckedOne(self.container);
				   if(data!=undefined){
	        		   if(data.q!=undefined && data.q!=""){
	        			   self.container.find('#user_datagrid >div.query-form input:text').val(data.q);
	        		   }
	        	   }
        	   });
           }, query:function(page,data){
        	   if(!page){
        		   page = getPager();
        	   }
        	   var url = "user/list/" + page.curSize + "/" + page.pageSize;
        	   if(data!=undefined){
        		   if(data.q!=undefined && data.q!=""){
        			   self.container.find('#user_datagrid >div.query-form input:text').val(data.q);
        		   }
        	   }
        	   var q = self.container.find('#user_datagrid >div.query-form input:text').val();
        	   if(q.length){
        		   url += "?q="+q;
        	   }
        	   ctrl.getHtml(url,function(data){
        		   refresh(data);
        	   });
        	   //self.container.parent().attr("indexPage",page.curSize);
           },
           create:function(){
        	   this.view(-1);
           },
           view:function(pk){
        	   ctrl.getHtmlDialog(("user/view/" + pk),"big-modal",function(dialog){
        		   var $form = dialog.find('#userEditForm');
        		   $form.on('click','button.btn-primary',function(){
        			   if($form.valid()){ //这样写在修改或新增用户时，（点击保存时）科目项显示有问题  */
							var user = $form.formToJson();
							//获取科目列表.
							var subjects =$("input[type =checkbox][name=examLists]");
							var subjectIds="";
							for(var i=0;i<subjects.length;i++){
								if(subjects[i].checked){
									subjectIds+=subjects[i].value+",";
								}
							}
							if(subjectIds==null ||subjectIds=="" ){
								subjectIds=0;
							}else{
								subjectIds=subjectIds.substring(0,subjectIds.length-1)
							}
							if(pk > 0){
								self.save(user,subjectIds,"PUT");
							}else{
								self.save(user,subjectIds,"POST");
							} 								
						}
						return false;
					}).validate(validateRules);
        		   
        		   $form.on('click','button.btn-default',function(){
        			   self.container.find("#user_pager").clickCurPage();
        		   });        	
        		   orgTree.show("orgSelected","orgTree");
        		   
        		   selectAll($form);
        		   //orgZTree(); 
        	   },function(){
        		   orgTree.destroy();
        	   });        	   
           },
           update:function(pk){

        	   if(pk > 0)
        	     this.view(pk);     	   
           },
           save:function(user,subjectIds,method){
        	   
        	   ctrl.postJson("user/validata",user,"",function(data){
    			   if(data.is){
    				   var org = $("#userEditForm #orgTreeId").val()||-1;
    	        	   if(method === 'POST' || method === 'post'){
    	        		   ctrl.postJson("user",user,"新增用户成功!",function(user){
    	        			  updateBelong(org,user.pk,subjectIds);
    	        			  ctrl.confirm("操作提示","继续添加其他用户吗？",function(){
    	        				   clearUserForm();        				   
    	        			   },"继续",function(){
    	        				   self.query(getPager());
    							   $("#big-modal").modal('hide');
    	        			   });
    	        		   });      		   
    	        	   }else {
    	        		   ctrl.putJson("user",user,"修改用户成功!",function(user){
    	        			   updateBelong(org,user.pk,subjectIds);   
    	        			   $("#big-modal").modal('hide');
    	                	   self.query(getPager());
    	        		   });
    	        	   } 
    			   }else{
    				   ctrl.alert("用户名已存在");
    			   }
    		   });
           },
           remove:function(pk,userName){
        	   //删除用户时验证是否关联权限，并给以提示；在删除时，对应关联权限必须清空
        	   ctrl.postJson("user/validate/"+pk,"","", function(data){
        		  var mes="";
        		   if(data.flg!=""){
        			 mes="此用户【<b style='color:red;'>"+(userName||"")+"</b>】关联了角色并做权限分配，确定要删除吗？"
        		   }else{
        			 mes="确定要删除用户【<b style='color:red;'>"+(userName||"")+"</b>】吗？";
        		   }
	        	   ctrl.remove("user",mes, "用户删除成功！",{pk:pk},function(){
	        			self.query(getPager());
	        	   });
        	   });
           },
           updatePassword:function(pk){
        	   ctrl.getHtmlDialog(("user/getpwd/" + pk),'pwd-modal',function(dialog){
        		   var $form = dialog.find('#form1');
        		   $form.on('click','button.btn-primary',function(){
						if($form.valid()){
							var user = $form.formToJson();
							ctrl.putJson("user/updatepwd",user,"密码修改成功！",function(){
								$("#pwd-modal").modal("hide");
							});
						}
						return false;
					}).validate(passwordRules);
        	   });
           }
       };
       return o;
	});
    
})();