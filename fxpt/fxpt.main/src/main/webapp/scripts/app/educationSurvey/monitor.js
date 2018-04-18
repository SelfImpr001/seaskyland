(function(){
	"use strict";
	define(['jquery','ajax',"dialog","controller","ztreecore","ztreecheck","validatejs","common","jqueryPager",'formToJosn'],function($,ajax,dialog,ctrl){	
		
		var self = undefined;
		var pageSize = 10;
		var validateRules = {
			onKeyup : true,
			rules : {
				name : {
					required : true
				},
				code : {
					required : true
				}
			},
			messages : {
				name : {
					required : '不能为空'
				},
				code : {
					required : '不能为空'
				}
			},
		}; 
		
		function getTHMLData(from){
			var date = {
					monitorDate:from.find("[name=monitorDate]").val(),
					academicYear:from.find("#academicYear :selected").val(),
					semester:from.find("[name=semester]").val(),
					analysisType:from.find("#analysisType :selected").val(),
					questionName:from.find("#questionName :selected").val(),
					grade:from.find("[name=grade]:selected").val(),
					institutions:from.find("#institutions").val(),
					createUser:from.find("#createUser").val()
			};
			return date;
		}
		function ininInfo($form){
			$form.on('change','input[name=grade]',function(){
				$form.find('input[name="grade"]').each(function(){
					$(this).prop("checked",false);
				});
				$(this).prop("checked",true);
			});
			$form.find("#monitorDate").datetimepicker({
				format:'yyyy-mm-dd',
				language:'zh-CN',
				weekStart:1,
				autoclose:true,
				todayBtn:true,
				startView:2,
				minView:2,
				maxView:2
			});
		}
		
		var _listUrl = ctrl.getCurMenu().attr('url');
		var o = self ={
			container:undefined,
		    render:function(container){
        	   //renderPager();
        	   ctrl.getHtml(_listUrl,function(html) {
        		   self.container = $(html);
        	       ctrl.appendToView(container,self.container);
        	       renderPager();
        	       bindEvent();
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
            },
            querym:function(page){
            	if(!page){
         		   page = getPager();
         	    }
         	    var url = "academicSupervise/monitorlist/" + page.curSize + "/" + page.pageSize;
         	    ctrl.getHtml(url,function(data){
         	    	refresh(data);
         	    });
            },
		    save:function(data,method){
		    	ctrl.postJson("academicSupervise/addMonitor",data,"保存成功",function(data){
	    			self.querym(getPager());						
	    		}); 
            },
			newadd:function(pk){
				ctrl.getHtmlDialog("academicSupervise/newAdd","big-modal",function(dialog){
					$(dialog).find("select").selectpicker("refresh");
						 
					var $form = dialog.find('form');
					$form.on('click','button.btn-primary',function(){
						var data = getTHMLData($form);
						self.save(data,"PUT");
						dialog.modal("hide");
  					}).validate(validateRules);
					ininInfo($form);
				});
			},
			remove:function(pk){
				ctrl.log("delete :" + pk);
				ctrl.remove("academicSupervise/monitorDelete/"+pk,"确定要删除该监测项目吗？", '删除成功！'," ",
				    function(data){
						self.querym(getPager());
				    }
			    );
			},
			update:function(pk){
				ctrl.getHtmlDialog("academicSupervise/view/"+pk,"big-modal",function(dialog){
					$(dialog).find("select").selectpicker("refresh");
					var $form = dialog.find('form');
					$form.on('click','button.btn-primary',function(){
						var data = getTHMLData($form);
						data["pk"]=pk;
						ctrl.postJson("academicSupervise/updateMonitor",data,"保存成功",function(data){
			    			self.querym(getPager());
			    		});
						dialog.modal("hide");
  					}).validate(validateRules);
					ininInfo($form);
				});			
			},
			view:function(pk){
				ctrl.getHtmlDialog("academicSupervise/newAdd/"+pk,"big-modal",function(dialog){
					$(dialog).find("select").selectpicker("refresh");
					var $form = dialog.find('form');
					$form.on('click','button.btn-primary',function(){
						var data = getTHMLData($form);
						self.update(data,"PUT");
						dialog.modal("hide");
  					}).validate(validateRules);
				});				
			},
			queryUser:function(pk,page,dialog){
	        	   if(!page){
	        		   page = ctr.newPage(dialog);
	        	   }
	        	   var url = "role/view/" + page.curSize + "/" + page.pageSize;
	        	   ctrl.getHtml(url+"?roleId="+pk,function(html){
	        		   var $html = $(html);
	        		   var $tbody = dialog.find('#mianPanel').find('#user_role_rows');
	        		   $tbody.children().remove();
	        		   $tbody.append($html.find('#user_role_rows').children());
	        		   $tbody.parent().next().children().remove();
	        		   $tbody.parent().next().append($html.find('#user_role_page').parent().children());
	        		   ctrl.renderPager({
	       				  "containerObj" : dialog,
	       				  "pageObj" : dialog.find('#user_role_page'),
	       				  "callBack":function(page){
	       					  self.queryUser(pk,page,dialog);
	       				   }
	       			   });
	        	   });
	
	           },
			grant:function(pk){
				ctrl.getHtmlDialog("role/grant/"+pk,"viewPanel",function(dialog){
					modalztree(pk);
					subGrant();
				});	
			},
			batchGrant:function(){
				ctrl.getHtmlDialog("role/batchGrant","viewPanel",function(dialog){
					modalztree(-1);
					subBatchGrant();
					dialog.on('click','#searchBtn',function() {
						var q =  $(this).parent().prev().val();
						ctrl.getJson("role/search?q="+q,function(data){
							var roles = data.roles;
							var $roleTable = dialog.find('#roleRows');
							
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
		
		function bindEvent(){
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
	    			self.remove(pk,roleName);
	    			break;
	    		case 'update' :   	
	    			self.update(pk);
	    			break;
	    		case 'view' :   	
	    			self.view(pk);
	    			break;
	    		case 'grant' :   	
	    			self.grant(pk);
	    			break;
	    		case 'batchGrant' :   	
	    			self.batchGrant();
	    			break;
	    		default:
	    			
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
			var functionsRoot = zTree.getNodeByParam("dataType","funcs");
			var nodes = zTree.getNodesByFilter(function(node){return node.checked},false,functionsRoot);//zTree.getCheckedNodes(true);
			var oNewNodes=[];
			for (var i = 0; i < nodes.length; i++) {
				oNewNodes.push({});
				oNewNodes[i]['pk'] = nodes[i]['pk'];
				oNewNodes[i]['reorder'] =i;
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