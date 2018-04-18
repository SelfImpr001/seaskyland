(function(){
	"use strict";
	define(['jquery','ajax',"dialog","controller","ztreecore","ztreecheck","validatejs","common","jqueryPager",'formToJosn'],function($,ajax,dialog,ctrl){	
		var flg =false;
		var self = undefined;
		var pageSize = 10;
		var validateRules = {
			onKeyup : true,
			rules : {
				name : {
					required : true
				}
			},
			messages : {
				name : {
					required : '不能为空'
				}
			},
		}; 
		
		var pages = ctrl.newPage();
		var _listUrl = "/role/list/" + pages.curSize + "/" + pages.pageSize+"?ui_all=true";
		var o = self ={
			container:undefined,
		    render:function(container){
        	   //renderPager();
		    	var url = "role/list/"+pages.curSize+"/15?ui_all=true"; 
		    	ctrl.getHtml(url,function(html) {
        		   self.container = $(html);
        	       ctrl.appendToView(container,self.container);
        	       renderPager();
        	       bindEvent(container);
              });
            },
            query:function(page){
            	if(!page){
         		   page = getPager();
         	    }
         	    var url = "role/list/" + page.curSize + "/" + page.pageSize;
         	    ctrl.getHtml(url,function(data){
         	    	refresh(data);
         	    });
         	   self.container.parent().attr("indexpage",page.curSize);
            },
		    save:function(role,method){
		    	if(method.toUpperCase() == "POST"){
		    		ctrl.postJson("role",role,"新增角色成功!",function(data){
		    			self.query(getPager());						
		    		}); 
		    	}else{
		    		ctrl.putJson("role",role,"修改角色成功!",function(data){
		    			self.container.find("#role_pager").clickCurPage();
		    		}); 
		    	}
            },
			newadd:function(pk){
				ctrl.log("pk:" + pk);
				ctrl.getHtmlDialog("role/newAdd","viewPanel",function(dialog){
					var $form = dialog.find('#form1');
					$form.on('click','button.btn-primary',function(){
						
						if(!$form.valid()){
  							return false;
  						}
						var code= document.getElementById("code").value;
						if(code!=null && code!=""){
				    		ctrl.postJson("role/validate/"+code+"/0","","", function(data){
					    		if(!data.flg){
					    			document.getElementById("roleNum").innerText="角色编码必须唯一";
					    			document.getElementById("roleNum").style.display="";
					    		}else{
					    			document.getElementById("roleNum").style.display="none";
					    			var role = $form.formToJson();
									self.save(role,"POST");
									dialog.modal("hide");
					    		}
					    		
					    	});
						}else{
							document.getElementById("roleNum").innerText="不能为空";
							document.getElementById("roleNum").style.display="";
						}
  					}).validate(validateRules);
					onBlurNewAdd($form);
				});
		 	},
			remove:function(pk,roleName,count){
				ctrl.log("delete :" + pk);
				var mes ="";
				if(count!=" "){
					mes="此角色关联了"+count+"个用户,确定要删除角色<b style='color:red;'>"+(roleName||"")+"</b>吗？"
				}else{
					count=="0";
					mes="确定要删除角色<b style='color:red;'>"+(roleName||"")+"</b>吗？"
				}
				ctrl.remove("role?count="+count,mes, '删除成功！',{pk:pk},
				    function(data){
						self.query(getPager());
				    }
			    );
			},
			update:function(pk){
				ctrl.log("pk:" + pk);
				ctrl.getHtmlDialog("role/update/"+pk,"viewPanel",function(dialog){
					var $form = dialog.find('#form1');
					$form.on('click','button.btn-primary',function(){
						if(!$form.valid()){
  							return false;
  						}
						var type =document.getElementById("type").value;
						var code= document.getElementById("code").value;
						var roleId=document.getElementById("roleId").value;
						if(code!=null && code!=""){
							if(type==1){
								var role = $form.formToJson();
								self.save(role,"PUT");
								dialog.modal("hide");
							}else{
					    		ctrl.postJson("role/validate/"+code+"/"+roleId,"","", function(data){
						    		if(!data.flg){
						    			document.getElementById("roleNum").innerText="角色编码必须唯一";
						    			document.getElementById("roleNum").style.display="";
						    		}else{
						    			document.getElementById("roleNum").style.display="none";
						    			var role = $form.formToJson();
										self.save(role,"PUT");
										dialog.modal("hide");
						    		}
						    		
						    	});
							}
						}else{
							document.getElementById("roleNum").innerText="不能为空";
							document.getElementById("roleNum").style.display="";
						}
  					}).validate(validateRules);
					onBlurUpdate($form);
				});				
			},
			view:function(pk){
				ctrl.log("pk:" + pk);
				ctrl.getHtmlDialog("role/view/1/"+pageSize+"?roleId="+pk,"viewPanel",function(dialog){
					renderUserPager(pk,dialog);
				});				
			},
			manage:function(pk){
				ctrl.log("pk:" + pk);
				ctrl.getHtmlDialog("role/manage?roleId="+pk,"viewPanel",function(dialog){
					renderUserPager(pk,dialog);
					//点击事件：点击用户，添加到右边已选列表中
					$(dialog).on('click','a[trigger="selectUser"]',function(){
						var pk = $(this).attr("pk");
						var userName=$(this).attr("userName");
						var style=$(this).attr("style");
						$("#l"+pk+"").remove();
						if(style=="color:red"){
							//去掉左边的添加到右边末尾一行 append
							$("#selectTable tbody").prepend("<tr id="+pk+"><td>" +
					                "<a href='javascript:void(0);' trigger='removeUser'  pk="+pk+" userName="+userName+">" +userName+
							        "</a></td></tr>");
						}else{
							//去掉左边的添加到右边开头一行
							$("#selectTable tbody").prepend("<tr id="+pk+"><td>" +
				                "<a href='javascript:void(0);' trigger='removeUser' name='purple' id="+pk+" style='color:purple' pk="+pk+" userName="+userName+">" +userName+
						        "</a></td></tr>");
						}
					});
					//移除选择了的人员
					$(dialog).on('click','a[trigger="removeUser"]',function(){
						var pk = $(this).attr("pk");
						var userName=$(this).attr("userName");
							$("#"+pk+"").remove();
							var style=$(this).attr("style");
							if(style=="color:purple"){
								//添加到最后一行：appent
								$("#leftTable tbody").prepend("<tr id=l"+pk+"><td>" +
						                "<a href='javascript:void(0);' trigger='selectUser'  pk="+pk+"  userName="+userName+">" +userName+
								        "</a></td></tr>");
							}else{
								$("#leftTable tbody").prepend("<tr id=l"+pk+"><td>" +
						                "<a href='javascript:void(0);' trigger='selectUser' name ='red' id="+pk+" style='color:red' pk="+pk+" userName="+userName+">" +userName+
								        "</a></td></tr>");
							}
					});
					//提交成员管理
					$(dialog).on('click','button.btn-primary',function(){
						//移除的角色ID
						var deleteUserIds="";
						//添加新用户的角色Id
						var addUserIds="";
						var addnum=$('a[name="purple"]').size();
						var deleteNum=$('a[name="red"]').size();
						for(var i=0;i<addnum;i++){
							addUserIds+=$('a[name="purple"]')[i].id+",";
						}
						for(var i=0;i<deleteNum;i++){
							deleteUserIds+=$('a[name="red"]')[i].id+",";
						}
						ctrl.postJson("role/userManage/"+pk,{"deleteUserIds":deleteUserIds,"addUserIds":addUserIds},"操作成功!",function(data){
							dialog.modal("hide");
							self.render();
		     		    });     
					});
				});				
			},
			queryUser:function(pk,page,dialog){
	        	   if(!page){
	        		   page = ctr.newPage(dialog);
	        	   }
	        	   var url = "role/view/" + page.curSize + "/" + page.pageSize;
	        	   ctrl.getHtml(url+"?roleId="+pk,function(data){
//	        		   ctrl.appendToView(dialog,data);
	        		  ctrl.refreshDataGrid('viewPanelBody',dialog,$(data),renderPager);
	        		  renderUserPager(pk,dialog);
	        	   });
	           },
			grant:function(pk,container){
				var pageIndex = self.container.parent().attr("indexpage");
				if(pageIndex == undefined){
					pageIndex = 1;
	        	} 
				ctrl.getHtml("role/grant/"+pk,function(html){
					var $htmlObj = $(html);
	        	     ctrl.appendToView(container,$htmlObj);
					 modalztree(pk);
					 subGrant();
					 $htmlObj.on('click','#t_goback',function(){
						 pages["curSize"] = pageIndex;
						 self.render(container);
						 
					 }).on('click','#cancelBtn',function(){
				    	   self.grant(pk,container);				
				    });
				});	
			},
			batchGrant:function(container){
				ctrl.getHtml("role/batchGrant",function(html){
					var $htmlObj = $(html);
	        	    ctrl.appendToView(container,$htmlObj);
					modalztree(-1);
					subBatchGrant();
					$htmlObj.on('click','#searchBtn',function() {
						var q =  $(this).parent().prev().val();
						ctrl.getJson("role/search?q="+q,function(data){
							var roles = data.roles;
							var $roleTable = $htmlObj.find('#roleRows');
							
							if(roles.length > 0){
								$roleTable.children().show();
							}else{
								$roleTable.children().hide();
								return;
							}
							
							var $row = $roleTable.children(":eq(0)");
							$roleTable.children().remove();
							
							$.each(roles,function(i,role){
								var $newRow = $row.clone();
								$roleTable.append($newRow);
							    var $cells = $newRow.children();
							    $cells.each(function(j){
							    	switch(j){
							    	case 0:		 	   		
							    		$(this).attr("value",role.pk);
							    		break;
							    	case 1:
							    		if(role.code!=null)
							    		$(this).text(role.code);
							    		break;
							    	case 2:
							    		$(this).text(role.name);
							    		break;
							    	case 3:
							    		if(role.desc!=null)
							    		$(this).text(role.desc);
							    		break;
							    	case 4:
							    		var text = "有效";
							    		if(role.available == 0)
							    			text = "无效";
							    		$(this).text(text);
							    		break;		
							    	default:
							    	}
							    	
							    });
							});
						});
					});
					$htmlObj.on('click','#t_goback',function(){
						self.render(container);
					 }).on('click','#cancelBtn',function(){
				    	   self.batchGrant(container);				
					    });
					allChecked();
				});
			}
		};
		
		function getPager(){
	    	var pager = ctrl.newPage(self.container);
    		return pager;
	    };
	    
	   function renderUserPager(pk,dialog) {
		   ctrl.renderPager({
				"containerObj" : dialog,
				"pageObj" : dialog.find('#user_role_page'),
				"callBack":function(page){
					page.pageSize = dialog.find("#pageSizeReal").val();
					self.queryUser(pk,page,dialog);
				}
			});	
		   return;
			var _pageNum = parseInt(dialog.find("#userPageNum").val());
			var _pageCount = parseInt(dialog.find("#userPageCount").val());
			pageSize = parseInt(dialog.find("#userPageSize").val());
			dialog.find("#user_role_page").pager({
				pageNum : _pageNum,
				pageCount : _pageCount,
				click : function(pageNum, pageCount) {
					var page = {
						pageNum : pageNum,
						size : pageSize,
					};
					self.queryUser(pk,page,dialog);
				}
			});
		}
		
		function allChecked(){
			$("input[type=checkbox][name='allChecked']").click(function(){
				var roleList=$("input[name='roleChecked']");
				var head=$("input[name='allChecked']");
				for(var i=0;i<roleList.length;i++){
					roleList[i].checked=head[0].checked;
				}
			});
		}
		//新增
		function  onBlurNewAdd(container){
		    container.on('blur','#code',function(){
		    	var code= document.getElementById("code").value;
		    	if(code!=null && code!=""){
		    		ctrl.postJson("role/validate/"+code+"/0","","", function(data){
			    		if(!data.flg){
			    			document.getElementById("roleNum").innerText="角色编码必须唯一";
			    			document.getElementById("roleNum").style.display="";
			    		}else{
			    			document.getElementById("roleNum").style.display="none";
			    		}
			    	});
		    	}else{
		    		document.getElementById("roleNum").innerText="不能为空";
		    		document.getElementById("roleNum").style.display="";
		    	}
		    });
		}
		//修改
		function  onBlurUpdate(container){
		    container.on('blur','#code',function(){
		    	var type= document.getElementById("type").value;
		    	var code= document.getElementById("code").value;
		    	var roleId=document.getElementById("roleId").value;
		    	if(type!=1){
		    		if(code!=null && code!=""){
			    		ctrl.postJson("role/validate/"+code+"/"+roleId,"","", function(data){
				    		if(!data.flg){
				    			document.getElementById("roleNum").innerText="角色编码必须唯一";
				    			document.getElementById("roleNum").style.display="";
				    		}else{
				    			document.getElementById("roleNum").style.display="none";
				    		}
				    	});
			    	}else{
			    		document.getElementById("roleNum").innerText="不能为空！";
			    		document.getElementById("roleNum").style.display="";
			    	}
		    	}
		    });
		}
	
		function bindEvent(container){
			var $obj = self.container;
			$($obj).on('click','.btn[trigger],#role_data_rows div.btn-group a',function(){
	    		var trigger = $(this).attr('trigger');
	    		var pk = $(this).attr("pk");
	    		switch(trigger){
	    		case 'newAdd' :   	
	    			self.newadd(pk);
	    			break;
	    		case 'remove' : 
	    			var roleName = $(this).parent().parent().parent().children(":eq(2)").text();
	    			var count = $(this).parent().parent().parent().children(":eq(5)").text();
	    			self.remove(pk,roleName,count);
	    			break;
	    		case 'update' :   	
	    			self.update(pk);
	    			break;
	    		case 'view' :   	
	    			self.view(pk);
	    			break;
	    		case 'grant' :   	
	    			self.grant(pk,container);
	    			break;
	    		case 'batchGrant' :   	
	    			self.batchGrant(container);
	    			break;
	    		case 'manage' :
	    			self.manage(pk);
	    			break;
	    		default:
	    			
	    		}
	    	});
			
			$($obj).on('click','a[trigger="userlist"]',function(){
				var pk = $(this).attr("pk");
				var count=$(this).attr("count");
				if(count>0){
					self.view(pk);
				}
			});
			
		}
		
		function renderPager(){
			ctrl.renderPager({
				"containerObj" : self.container,
				"pageObj" : self.container.find('#role_pager'),
				"callBack":self.query
			});	
		}
		
			
		function modalztree(roleId){
			var setting = {
				check: {
					enable: true,
					chkboxType: {"Y":"ps", "N":"ps"}
				},
				view: {
					dblClickExpand: false
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
				 	//onCheck: onCheck,
				 	beforeCheck:onBeforeCheck
				}
			};

			ctrl.getJson("role/permission/"+roleId,function(data){
				var $ztreeData = data.treeNodes||[];
				var zTree = $.fn.zTree.init($("#leftModalztree"), setting, $ztreeData);
				initTreeEvent();
				var checkedNodes = zTree.getCheckedNodes(true);
				if(checkedNodes.length >0){
					//zTree.checkNode(checkedNodes[0],true,true,true);
				}
		  	    
			});				

			
			function initTreeEvent(){
				$("[lefttree='checked']").click(function() {
					var zTree = $.fn.zTree.getZTreeObj("leftModalztree");
					var nodes = zTree.getSelectedNodes();
					for (var i = 0; i < nodes.length; i++) {
						zTree.checkNode(nodes[i],true,true,true);
					}
					
				});
				
				$("[lefttree='unchecked']").click(function() {
					var zTree = $.fn.zTree.getZTreeObj("leftModalztree");
					var nodes = zTree.getSelectedNodes();
					for (var i = 0; i < nodes.length; i++) {
						zTree.checkNode(nodes[i],false,true,true);
					}
				});
			}

			function onBeforeCheck(treeId,treeNode){
				var zTree = $.fn.zTree.getZTreeObj("leftModalztree");
				if(treeNode['checkOnly']){
					zTree.setting.check.chkboxType= {"Y":"", "N":""};
				}else{
					zTree.setting.check.chkboxType={"Y":"ps", "N":"ps"};
				}
			}
		}
		
		function getGrantNodes(){
			var zTree = $.fn.zTree.getZTreeObj("leftModalztree");
			//菜单权限
			var functionsRoot = zTree.getNodeByParam("dataType","funcs");
			//报表权限
			var reportsRoot = zTree.getNodeByParam("dataType","reports");
			var nodes = zTree.getNodesByFilter(function(node){return node.checked},false,functionsRoot);//zTree.getCheckedNodes(true);
			var reports = zTree.getNodesByFilter(function(node){return node.checked},false,reportsRoot);
			var oNewNodes=[];
			var i = 0
			for (i; i < nodes.length; i++) {
				oNewNodes.push({});
				oNewNodes[i]['pk'] = nodes[i]['pk'];
				oNewNodes[i]['reorder'] =i;
			};
			for (var a=0; a < reports.length; a++) {
				oNewNodes.push({});
				oNewNodes[i]['pk'] = reports[a]['pk'];
				oNewNodes[i]['reorder'] =i;
				i++;
			};
			return oNewNodes;
		}
		
		function getFuncsNodes(){
			var zTree = $.fn.zTree.getZTreeObj("leftModalztree");
			var functionsRoot = zTree.getNodeByParam("dataType","funcs");
			var nodes = zTree.getNodesByFilter(function(node){return node.checked;},false,functionsRoot);//zTree.getCheckedNodes(true);
			var oNewNodes=[];
			for (var i = 0; i < nodes.length; i++) {
				oNewNodes.push({});
				oNewNodes[i]['pk'] = nodes[i]['pk'];
				oNewNodes[i]['reorder'] =i;
			};
			return oNewNodes;
		}
		
		function getDatasNodes(roleId){
			var zTree = $.fn.zTree.getZTreeObj("leftModalztree");
			var functionsRoot = zTree.getNodeByParam("dataType","data");
			var nodes = zTree.getNodesByFilter(function(node){return node.checked && node.permissionPk;},false,functionsRoot);//zTree.getCheckedNodes(true);
			var oNewNodes=[];			
			
			for (var i = 0; i < nodes.length; i++) {
				oNewNodes.push({});

				oNewNodes[i]['target'] = "role";
				oNewNodes[i]['targetPk'] = roleId;
				oNewNodes[i]['permission'] = {"pk":nodes[i]['permissionPk']};
				oNewNodes[i]['permissionName'] = nodes[i][nodes[i]['nameKey']];
				oNewNodes[i]['permissionValue'] = nodes[i][nodes[i]['valueKey']];
				oNewNodes[i]['fromPk'] = nodes[i][nodes[i]['idKey']];
				oNewNodes[i]['fromTable'] = nodes[i]['dataFromTable'];
			};
			return oNewNodes;
		}
		
		function subBatchGrant() {
			$("#grantBtn").click(function() {
				var urlResourses=getGrantNodes();	
				var roleArr=[];
				$("input[type=checkbox][name='roleChecked']:checked").each(function(){
					roleArr.push($(this).val());
				});
				
				if(roleArr.length==0){
					 dialog.tipmodal({
						 header:{show:true,txt:"操作提示"},
							tip_txt:"请选择需要授权的角色",
							canmove:true, //是否拖动
							icon_info:'error',
							footer:{
								show:true,
								buttons:[{
									type:'submit',txt:"确定",						
									sty:'primary',
									callback:function(){
										$(this).trigger('close');
									}}
								]
							}
					 });
					 return false;
				}
				
				
				$.each(roleArr,function(i,roleId){
					var dps=getDatasNodes(roleId);
					ctrl.postJson("role/dataAuthorized/"+roleId,dps); 
				});
				
				ctrl.postJson("role/subBatchGrant?idList="+roleArr,urlResourses,"授权成功!",function(data){
					$("#viewPanel").modal("hide");
     		    });     
			});
		}
		
		function subGrant() {
			$("#grantBtn").click(function() {
				var roleId = $("#roleId").val();
				var urlResourses=getGrantNodes();
				var dps=getDatasNodes(roleId);
				ctrl.postJson("role/dataAuthorized/"+roleId,dps); 
				ctrl.postJson("role/subGrant?roleId="+roleId,urlResourses,"授权成功!",function(data){
					$("#viewPanel").modal("hide");
     		    });
			});
		}

		function refresh(data){
			ctrl.refreshDataGrid('role_data_rows',self.container,$(data),renderPager);			
		}
		
        return o;
	});
    
})();