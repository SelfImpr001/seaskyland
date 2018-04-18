(function() {
	"use strict";
	define([ 'controller', "ztreebox" ], function(ctrl, ztree) {

		return function() {
			var o = undefined, _container = undefined;
			var orgTree = undefined;
			var $targetObj = undefined;
			var $orgIds = undefined;
			function onAsyncSuccess(e,treeId, treeNode) {
				var selectedOrgIds = $orgIds.val().split(",");
				$.each(treeNode.children,function(j,node){
					$.each(selectedOrgIds,function(i,id){					
						if(id == node.id){
							orgTree.checkNode(node,true,true);
						}
					});
				});
			};
			
			function onCheck(e, treeId, treeNode) {
				var nodes = orgTree.getCheckedNodes(true);
				var ids = "";
				var names = "";
				$.each(nodes,function(i,n){
					if(i < nodes.length-1){
						names += n.name + "，";
						ids += n.id +",";
					}else{
						names += n.name ;
						ids += n.id ;
					}
				});
				$orgIds.val(ids);
				$targetObj.val(names);
				$orgIds.next().val(ids);
				ctrl.log(names+ "  " + ids);
			};
			
			o = {
				container:undefined,
				targetId:undefined,
				ztreeId:undefined,
				show : function(targetId,ztreeId) {
					this.targetId = targetId;
					this.ztreeId = ztreeId;
					if(!this.container)
						this.container = $('#'+ztreeId);
					
					_container = this.container;
					$targetObj = $('#' + this.targetId);
					$orgIds =  $("#orgTreeAllId");
					
					ctrl.get("api/orgs","json", function(data) {
						orgTree = ztree({
							ztreeId : ztreeId||"ztree",
							targetId : targetId,
							settings:{
								check: {
									chkboxType: {"Y":"", "N":""}
								},
								async : {
									enable : true,
									url : window.app.rootPath + "api/orgs",
									autoParam : [ "code", "type" ]
								},
								callback: {
									onCheck: onCheck,
									onAsyncSuccess: onAsyncSuccess
								}
							},
							initData : data.treeNodes
						});
					}, false,false, {
						code : "",
						type : 0
					});
				},
				refresh:function(){
					//this.destroy();
					//this.show(this.targetid,this.treeId);
					orgTree.refresh();
				},
				reset:function(){
					var nodes = orgTree.getCheckedNodes(true);
					$.each(nodes,function(i,node){
						orgTree.checkNode(node,false,true,true);
					});
				},
				destroy:function(){
					orgTree.destroy();
					//$.fn.zTree.destroy(this.ztreeId);
				}
			};
			return o;
		}();
	});
})();