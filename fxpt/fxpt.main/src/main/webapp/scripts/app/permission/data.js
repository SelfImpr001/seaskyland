/**
 * 
 */

(function(){
	"use strict";
	define(['jquery','ajax','jqueryPager',"dialog","controller","validatejs","common","ztreecheck"],
			function($,ajax,pager,dialog,ctrl){
		var _listUrl = ctrl.getCurMenu().attr('url');
		var _childrenUrl = _listUrl.substring(0,_listUrl.indexOf("?"));
		var o,self;
		var _treeSettings ={
			check: {
				chkboxType: {"Y":"", "N":""}
			},
			async : {
				enable : false,
				url : window.app.rootPath + "permission/data/tree",
				autoParam : [ "pk" ]
			}
		};
		
		var validateRules = {
				rules : {
					name : {
						required : true,
						minlength : 1
					},
					paramName : {
						required : true,
						minlength : 1
					},
					paramKeyField:{
						required : true,
						minlength : 1
					},
					paramNamefield:{
						required : true,
						minlength : 1
					},
					paramValueField:{
						required : true,
						minlength : 1
					},
					table:{
						required : true,
						minlength : 1
					}
				},
				messages : {
					name : {
						required : "请输入参数名称"
					},
					paramName : {
						required : "请输入外部参数名称"
					},
					paramKeyField:{
						required : "请输入数据权限值主健字段"
					},
					paramNamefield:{
						required : "请输入权限名称字段"
					},
					paramValueField:{
						required : "请输入权限值字段"
					},
					table:{
						required : "请输入数据权限来源表名称"
					}
				}
			}; 

		return function(){				
			var permissionTtree = undefined;
			var treeCallback = {
				onClick: onClick
			};
			o = self = {
				container:undefined,
				
			    render:function(container){
					ctrl.log(_listUrl);					
					ctrl.getHtml(_listUrl,function(html) {
	        		    self.container = $(html);
	        		    //self.queryForm = self.container.find('div.query-form');
	        	        ctrl.appendToView(container,self.container);	        	     
	        	        
	        	        self.renderTree();
	        	        init();	        	        
		            });
				},
				view:function(pk){
					ctrl.getHtmlDialog(("permission/data/view/" + pk),"dp-dialog",function(dialog){
		        		   var $form = dialog.find('#dpEditForm');
		        		   dialog.on('click','button.btn-primary',function(){
		        			   $form.validate(validateRules);
		        			   if($form.valid()){
									var dp = $form.formToJson();
									if(pk > 0){
										self.save(dp,"PUT");
									}else{
										self.save(dp,"POST");
									} 								
								}
								return false;
							});//.validate(validateRules);
		        		   
		        	   },function(){
		        		   
		        	   });
				},
				save:function(permission,method){
					var node = getTreeSelectedNode();
					if(node.id!=-1){
						permission["parent"] = {pk:node.id};
					}
					
					if(method === 'POST' || method === 'post'){
					    ctrl.postJson("permission/data",permission,"新增数据权限成功!",function(data){
					    	addNode(data.permission.pk,data.permission.name);
					    });
					    
					}else if (method === 'PUT' || method === 'put'){
						ctrl.putJson("permission/data",permission,"修改数据权限成功!",function(data){
							updateNodeName(data.permission.pk,data.permission.name);
						});
						
				    }
					 
					refresh();
				},
				remove:function(pk,name){
					ctrl.remove("permission/data","删除将会清除所有角色和用户此权限的授权，确定要删除权限【<b style='color:red;'>"+(name||"")+"</b>】吗？",
		        			   "权限删除成功！",{pk:pk,name:name},function(){
		        		refresh();
		        		removeNode(pk);
		        	});
				},
				disabled:function(pk){
					ctrl.putJson("permission/data/status/0",{pk:pk},"数据权限禁用成功!",function(){
						refresh();
					});
				},
				enabled:function(pk){
					ctrl.putJson("permission/data/status/1",{pk:pk},"数据权限启用成功!",function(){
						refresh();
					});
				},
				showDetail:function(pk){
					ctrl.getHtmlDialog(("permission/data/detail/" + pk +"/1/15"),"dp-dialog",function(dialog){
		        		
		            });
				},
				renderTree:function(){
					ctrl.get("permission/data/tree","json", function(data) {
						_treeSettings['callback'] = treeCallback;
						permissionTtree = $.fn.zTree.init( $("#dataPermission_ztree1"), _treeSettings, data.treeNodes||[]);
					}, false,false, {
						pk : -1,
					});
				}
			};
			
			function init(){
    	        bindEvent();
    	        ctrl.initUI(self.container);
			};
			
		    function bindEvent(){
		    	var $obj = self.container;

		    	$obj.on('click','#dataPermission_datagrid tbody div.btn-group a',function(){
		    		eventTrigge($(this));
		    	}).on('click','h1 button',function(){
		    		eventTrigge($(this));
		    	});		    	
		    };
		    
		    function eventTrigge($e){
		    	var trigger = $e.attr('trigger');
	    		var pk = $e.attr("pk");
	    		switch(trigger){
	    		case 'create':
	    			self.view(-1);
	    			break;
	    		case 'view':
	    			self.showDetail(pk);
	    			break;	    			
	    		case 'update' :  			
	    			self.view(pk);
	    			break;
	    		case 'remove':
	    			var name = $e.parent().parent().parent().find('td:eq(1)').text();
	    			self.remove(pk,name);
	    			break;
	    		case 'disabled':
	    			self.disabled(pk);
	    			break;
	    		case 'enabled':
	    			self.enabled(pk);
	    			break;	
	    		default:
	    			return;
	    		}
		    };
			
		    function getTreeSelectedNode(){
		    	//var zTree = $.fn.zTree.getZTreeObj("dataPermission_ztree1");
        		var nodes = permissionTtree.getSelectedNodes();
        		if(nodes.length == 0){
        			return permissionTtree.getNodes()[0];
        		}else{
        			return nodes[0];
        		}
		    };
		    
		    function refresh(){
		    	onClick(false,"dataPermission_ztree1",getTreeSelectedNode());
		    };
		    
		    function removeNode(id){
		    	var node = permissionTtree.getNodeByParam("id",id);
		    	if(node!=null){
		    		permissionTtree.removeNode(node);
		    	}
		    };
		    
		    function addNode(id,name){
		    	var node = getTreeSelectedNode();
		    	permissionTtree.addNodes(node,{id:id,name:name});
		    };
		    
		    function updateNodeName(id,name){
		    	var node = permissionTtree.getNodeByParam("id",id);
		    	if(node!=null){
		    		node.name = name;
		    		permissionTtree.updateNode(node);
		    	}
		    };
		    
			function onClick(e, treeId, treeNode){
				if(treeNode.isParent==false)
					return;

				ctrl.getHtml(_childrenUrl + "?pk=" + treeNode.id,function(html) {
					var $datagrid = self.container.find("tbody");
					$datagrid.children().remove();
					$datagrid.append($(html).find('tbody').children());
	      	    });
			};
			
			return o;
		}();
	});
})();