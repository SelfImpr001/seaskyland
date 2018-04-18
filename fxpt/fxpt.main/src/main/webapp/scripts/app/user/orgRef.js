(function(){
	"use strict";
	define(['jquery',"controller","radioztree","ztreecore","ztreecheck"],function($,ctrl,ztree){
	
	    /* 组织关系树 */
	    function orgZTree(){
			var $oNewTree = $('#ztree1');
			var setting = {
				check: {
					enable: true,
					chkStyle: "checkbox",
					chkboxType: {"Y":"", "N":""}
				},
				view: {
					dblClickExpand: false
				},
				data: {
					simpleData: {
						enable: true
					},
					keep :{
						parent:true
					}
				},
				callback: {
					beforeClick: beforeClick,
					onCheck: onCheck,
					onExpand:beforExpand,
					beforeCheck:beforeCheck,
					onCollapse:onCollapse
				}
			};

			var zNodes,allId;

			ctrl.getJson("org/tree/list/1",function(data){
				zNodes = $.parseJSON(data.orgJson);					
			});
			function beforeClick(treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("ztree1");
				zTree.checkNode(treeNode, !treeNode.checked, null, true);
				return false;
			}
			/* 被选中之前 */
			function beforeCheck(treeId, treeNode){
				if(!treeNode.open && treeNode.isParent)
					beforExpand(null,treeId,treeNode);
			}
			function onCheck(e, treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("ztree1"),
				nodes = zTree.getCheckedNodes(true),
				v = "",id = "",allId="";
				for (var i=0, l=nodes.length; i<l; i++) {
//					if(!nodes[i].isParent){
						v += nodes[i].name + ",";
						id+= nodes[i].id   + ",";
//					}
					allId+= nodes[i].id   + ",";
				}
				if (v.length > 0  )  v   = v.substring(0, v.length-1);
				if (id.length > 0 ) id   = id.substring(0, id.length-1);
				if (allId.length > 0 ) allId = allId.substring(0, allId.length-1);
				var orgTree      = $("#orgTree");  //显示名称
				var orgTreeId    = $("#orgTreeId");//保存叶节点id
				var orgTreeAllId = $("#orgTreeAllId");//保存被选择的所有id
				orgTree.val(v);          
				orgTreeId.val(id);    
				orgTreeAllId.val(allId);
			}
			/* 展开树   */
			function beforExpand(e,treeId, treeNode){
				ctrl.getJson("org/tree/children?pk="+treeNode.id,function(data){
					var treeObj = $.fn.zTree.getZTreeObj(treeId);
					var child = $.parseJSON(data.orgJson);
					var allId = $("#orgTreeAllId").val().split(",");
					if(allId.length > 0){
						showCheckeds(child,allId);
					}
					treeObj.addNodes(treeNode,child);					
				});
				
			}
			//折叠树
			function onCollapse(e,treeId, treeNode){
				var obj = $.fn.zTree.getZTreeObj(treeId);
				obj.removeChildNodes(treeNode);
			}
			$('#orgTree').focus(function(){
				var Ids = $("#orgTreeId").val().split(",");
				showCheckeds(zNodes,Ids);
				var allId = $("#orgTreeAllId").val().split(",");
				showCheckeds(zNodes,allId);
				$.fn.zTree.init($("#ztree1"), setting, zNodes);
				showMenu();
			});
			function showCheckeds(zNodes,ids){
				for(var i = 0; i < zNodes.length;i++){
					for(var j = 0; j < ids.length;j++){
						if(zNodes[i].id == ids[j]){
							zNodes[i].checked = true;
							break;
						}
					}
				}
			}
			function hideCheckeds(zNodes){
				for(var i = 0; i < zNodes.length;i++){
					zNodes[i].checked = false;
				}
			}
			function showMenu() {
				var orgTree = $("#orgTree")[0];
				$("#ztree1").css({'border':'1px #cccccc solid','left':getLeft(orgTree), 'top':getTop(orgTree)+$("#orgTree").outerHeight()}).slideDown("fast");
				var minWidth = $('#orgTree').outerWidth();
				$("#ztree1").css('min-Width',minWidth);
				$("body").bind("mousedown", onBodyDown);
			}

			function hideMenu() {
				hideCheckeds(zNodes);
				$.fn.zTree.destroy("ztree1");
				$("#ztree1").fadeOut("fast");
				$("body").unbind("mousedown", onBodyDown);
			}
			function onBodyDown(event) {
				if (!(event.target.id == "menuBtn" || event.target.id == "orgTree" || event.target.id == "ztree1" || $(event.target).parents("#ztree1").length>0) || event.target.id == '') {
					hideMenu();
				}
			}
		}

       return orgZTree;
	});
    
})();