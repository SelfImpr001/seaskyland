(function(){
	"use strict";
	define(["jquery","controller","ztreecore","ztreecheck"],function($,ctrl){
	    var self = undefined;
	    var oldNodes = []; //记录旧节点 区分角色资源 和 自定义资源	    
	    var _userArr=[];
	    var _dialog = undefined;
	    function renderUserPager(page) {
	    	if(!page)
	    	    page = getUserNewPager();
	    	    
	    	ctrl.renderPager({
   				  "containerObj" : _dialog.find('div.tab-content>div:eq(0)'),
   				  "pageObj" : _dialog.find('#user_inner_page'),
   				  "callBack":function(page){
   					   _dialog.find('div.tab-content>div:eq(0) thead :checkbox').prop('checked',false);
   					   self.queryUser(page);
   				   }      		
	    	});

		};

	    function usersChecked(){
	    	_dialog.find('div.tab-content>div:eq(0) ').on("click","thead :checkbox",function(e){
				var $userSelected = $(this).parent().parent().parent().next().find(" :checkbox");
				if(this.checked){
					$userSelected.each(function(i,n){
						userSelected(n);
					});
				}else{
					$userSelected.each(function(i,n){
						userUnselected(n);
					});					
				}
		    }).on("click","tbody :checkbox",function(e) {
		    	if(this.checked){
					userSelected(this);
				}else{
					userUnselected(this);
				}				
		   });
	    };
	    
	    function userSelected(n){
	    	n.checked = true;
			if($.inArray(n.value,_userArr) == -1){
				_userArr.push(n.value);
			}
	    };
	    
	    function userUnselected(n){
	    	n.checked = false;
			var j = $.inArray(n.value,_userArr);
			if(j > -1){
				_userArr.splice(j,1);
			}
	    };
	    
	    function showGrantTree(pk){
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
				callback:{
					beforeCheck:function(treeId, treeNode) {
						var zTree = $.fn.zTree.getZTreeObj("userztree");
						if(treeNode['checkOnly']){
							zTree.setting.check.chkboxType= {"Y":"s", "N":"s"};
						}else{
							zTree.setting.check.chkboxType={"Y":"ps", "N":"ps"};
						}

					}
				}
			};

	
			ctrl.get("user/view/grant/tree/"+pk,"json",function(data){
				var $ztreeData=data.ztree;
				$.fn.zTree.init(_dialog.find("#userztree"), setting, $ztreeData);

		        initTreeEvent();				
			});

			function initTreeEvent(){
				_dialog.find("[usertree='checked']").click(function() {
					var zTree = $.fn.zTree.getZTreeObj("userztree");
					var nodes = zTree.getSelectedNodes();
					for (var i = 0; i < nodes.length; i++) {
						zTree.checkNode(nodes[i],true,true,true);
					}
				});
				
				_dialog.find("[usertree='unchecked']").click(function() {
					var zTree = $.fn.zTree.getZTreeObj("userztree");
					var nodes = zTree.getSelectedNodes();
					for (var i = 0; i < nodes.length; i++) {
						zTree.checkNode(nodes[i],false,true,true);
					}
				});
			}
		}
	    
	    function showRoleTree(pk){
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
				callback:{
					onCheck:function(e, treeId, treeNode) {
						if(!treeNode.id)
							return true;
						var $rpztree = _dialog.find("#rpztree_"+treeNode.id);
						if($rpztree.size()==0){
							$rpztree =  _dialog.find("#rpztree").clone();
							$rpztree.attr("id","rpztree_"+treeNode.id);
							$rpztree.insertAfter("#rpztree");
							ctrl.getJson("role/permission/"+treeNode.id,function(data){								
								$.fn.zTree.init($rpztree, {data: {simpleData: {enable: true}}}, data.treeNodes);
								upateUserPermissions(treeNode.checked,"rpztree_"+treeNode.id);
							});
						}else{
							upateUserPermissions(treeNode.checked,"rpztree_"+treeNode.id);
						}
					}
				}
			};
			
			function upateUserPermissions(isAdd,rpTreeId){
				var zTree = $.fn.zTree.getZTreeObj("userztree");
				var root = zTree.getNodeByParam("dataType","data");
				var dataNodes = getRoleDataNodes(rpTreeId);
				zTree.getNodesByFilter(function(node){
					if(node.ckecked)
						return true;
					for(var i=0;i<dataNodes.length;i++){
						var nameKey = node.nameKey;
						var valueKey = node.valueKey;
						var permissionPk = node.permissionPk;
						
						var dataNode = dataNodes[i];
						if(dataNode.permissionPk == permissionPk && dataNode.nameKey == nameKey && dataNode.valueKey==valueKey ){
							if(node[nameKey] == dataNode[nameKey] && node[valueKey] == dataNode[valueKey]){
								zTree.checkNode(node,isAdd,true,false);
							}
						}
					}
					
					return node.checked;
				},false,root);
				var funcNodes = getRoleFuncsNodes(rpTreeId);
				root = zTree.getNodeByParam("dataType","funcs");
				zTree.getNodesByFilter(function(node){
					if(node.ckecked)
						return true;
					for(var i=0;i<funcNodes.length;i++){
						var funcNode = funcNodes[i];
						if(funcNode.pk == node.pk && funcNode.checked){
							zTree.checkNode(node,isAdd,false,false);
						}
					}
					
					return node.checked;
				},false,root);
			};
			
			function getRoleDataNodes(rpTreeId){
				var zTree = $.fn.zTree.getZTreeObj(rpTreeId);
				var root = zTree.getNodeByParam("dataType","data");
				var nodes = zTree.getNodesByFilter(function(node){return node.checked && node.permissionPk;},false,root);
				return nodes;
			};
			
			function getRoleFuncsNodes(rpTreeId){
				var zTree = $.fn.zTree.getZTreeObj(rpTreeId);
				var root = zTree.getNodeByParam("dataType","funcs");
				var nodes = zTree.getNodesByFilter(function(node){return  node.checked;},false,root);
				return nodes;
			};

			//生成的树的设置
			var setting2 = {
				check: {
					enable: false,
				},
				view: {
					dblClickExpand: false
				},
				data: {
					simpleData: {
						enable: true
					}
				}
			};
			ctrl.getJson("user/view/role/tree/"+pk,function(data){
				 var $ztreeData=data.ztree;
					oldNodes=$ztreeData;
					$.fn.zTree.init(_dialog.find("#roleztree"), setting, $ztreeData);
					var oNewNodes=[];
					for (var i = 0; i < $ztreeData.length; i++) {
						if($ztreeData[i].checked){
							oNewNodes[oNewNodes.length]=$ztreeData[i];
						}
					}		
			        $.fn.zTree.init(_dialog.find("#roleztreeright"), setting2, oNewNodes);
			        initTreeEvent();
			});
			
			function initTreeEvent(){
				_dialog.find("[roletree='checked']").click(function() {
					var zTree = $.fn.zTree.getZTreeObj("roleztree");
					var nodes = zTree.getSelectedNodes();
					for (var i = 0; i < nodes.length; i++) {
						zTree.checkNode(nodes[i],true,true,true);
					};
				});
				
				_dialog.find("[roletree='unchecked']").click(function() {
					var zTree = $.fn.zTree.getZTreeObj("roleztree");
					var nodes = zTree.getSelectedNodes();
					for (var i = 0; i < nodes.length; i++) {
						zTree.checkNode(nodes[i],false,true,true);
					};
				});
			};
		};	
		
		function getUserNewPager(){
			var page =  ctrl.newPage(_dialog);
			//page["userArr"]=[];
			return page;
		};
		
		function getFuncsNodes(){
			var zTree = $.fn.zTree.getZTreeObj("userztree");
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
		
		function getDatasNodes(userPk){
			var zTree = $.fn.zTree.getZTreeObj("userztree");
			var functionsRoot = zTree.getNodeByParam("dataType","data");
			var nodes = zTree.getNodesByFilter(function(node){return node.checked && node.permissionPk;},false,functionsRoot);//zTree.getCheckedNodes(true);
			var oNewNodes=[];			
			
			for (var i = 0; i < nodes.length; i++) {
				oNewNodes.push({});

				oNewNodes[i]['target'] = "user";
				oNewNodes[i]['targetPk'] = userPk;
				oNewNodes[i]['permission'] = {"pk":nodes[i]['permissionPk']};
				oNewNodes[i]['permissionName'] = nodes[i][nodes[i]['nameKey']];
				oNewNodes[i]['permissionValue'] = nodes[i][nodes[i]['valueKey']];
				oNewNodes[i]['fromPk'] = nodes[i][nodes[i]['idKey']];
				oNewNodes[i]['fromTable'] = nodes[i]['dataFromTable'];
			};
			return oNewNodes;
		}
		
		function updateUserPermissions(userPk){
			var roles = $.fn.zTree.getZTreeObj("roleztree").getCheckedNodes(true);
			var newRoles = [];
			$.each(roles,function(i,role){
				if(role.id){
					newRoles[newRoles.length] = {pk:role.id};
				}
			});
			self.updateRoles(userPk,newRoles);
			
			var urlResourses=getFuncsNodes();
			var dps=getDatasNodes(userPk);
			
			
			ctrl.postJson("user/dataAuthorized/"+userPk,dps); 
			self.updateGrant(userPk,urlResourses);		
		};
		
	    var o = self = {
           
           queryUser:function(page){
        	   if(!page){
        		   page =getUserNewPager();
        	   }
        	  
        	   var url = "user/list/" + page.curSize + "/" + page.pageSize;
        	   var q = _dialog.find('#batchSearch').parent().prev().val();

        	   if(q.length){
        		   url += "?q="+q;
        	   }
        	   
        	   ctrl.getHtml(url,function(html){
        		   var $html = $(html);
        		   var $tbody = _dialog.find('#user-rows');
        		   $tbody.empty();
        		   $html.find('#user_data_rows').children().each(function(i,n){
        			   var $row = $(n);
        			   $tbody.append($row);
        			   $row.children(":gt(4)").remove();
        			   var $td = $row.children(":eq(0)");
        			   $td.text('');
        			   var pk = $td.attr('pk');
        			   var checked = "";
        			   if($.inArray(pk,_userArr) > -1){
        				   checked = "checked";
   		    		   }
        			   $('<input name="userChecked" value="'+pk+'" type="checkbox" class="ace" '+checked+'><span class="lbl"></span>').appendTo($td);        			  
        		   });
        		   $tbody.parent().next().children(":hidden").remove();
        		   $tbody.parent().next().append($html.find('#user_pager').parent().find("input:hidden"));

        		   renderUserPager();
        	   });
           },
           grantFor:function(pk,_self,container){
        	   var pageIndex = _self.container.parent().attr("indexpage");
        	   if(pageIndex == undefined){
        		   	pageIndex = 1;
        	   } 
        	   
        	   /** 用户跳转单权限分配 */
        	   ctrl.getHtml(("user/view/role/" + pk),function(dialog){
        	       var $roleTree = _dialog = $(dialog);
        	       ctrl.appendToView(container,$roleTree);
        	       showGrantTree(pk);
        	       showRoleTree(pk);
				   
				   $roleTree.on('click','#grantBtn',function(){
					   updateUserPermissions(pk);				
			       }).on('click','#cancelBtn',function(){
			    	   self.grantFor(pk,_self,container);				
			       }).on('click','#to_goback',function(){ //返回上级页面
			    	   _self.setPage(pageIndex);
			    	   _self.render(container);
			       });
        	   });          	       	   
           },      
           batchGrant:function(_self,container){
        	   /** 用户跳转批量权限分配 */
        	   var pageIndex = _self.container.parent().attr("indexpage");
        	   if(pageIndex == undefined){
        		   	pageIndex = 1;
        	   } 
        	   _userArr=[];
        	   ctrl.getHtml("user/view/batchgrant",function(dialog){
        		   _dialog = $(dialog);
        		   ctrl.appendToView(container,_dialog);
        	       showGrantTree(-1);
        	       showRoleTree(-1);
                   _dialog.find('#user_inner_page').css({margin:'4px 0px 2px 0px'}).parent().prev().css({margin:'0px 0px 2px 0px'});;
				   renderUserPager();
				   usersChecked();
				   _dialog.on('click','#grantBtn',function(){
						
						if(_userArr.length==0){
							ctrl.alert("请选择需要授权的用户！","error");							
							return false;
						}
						$.each(_userArr,function(i,n){
							updateUserPermissions(n);
						});						
						
 					}).on('click','#batchSearch',function(){

 						_userArr = [];
 						self.queryUser();

			    	}).on('click','#cancelBtn',function(){
			    		self.batchGrant(_self,container);				
				    }).on('click','#to_goback',function(){  //返回上级页面
				    	_self.setPage(pageIndex);
			    		_self.render(container);
			    	});
					
					
        	   });
           },
           updateRoles:function(pk,roles){
			   ctrl.putJson("user/updateroles/"+pk,roles,false);        	
           },
           updateGrant:function(pk,urls){
				ctrl.putJson(("user/updateGrant/"+pk),urls,"授权成功",function(){
					//$("#pwd-modal").modal("hide");
				});        	   
           },
           updateBatchGrant:function(roleArr,userArr,urls){
				ctrl.putJson(("user/updateBatchGrant?roleIds="+roleArr+"&userIds="+userArr),urls,"授权成功",function(){
					//$("#pwd-modal").modal("hide");
				});
           }
       };
       return o;
	});
    
})();