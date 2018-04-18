(function(){
	"birt strict";
	define(['jquery','ajax',"jquery-ui",'formToJosn',"dialog","controller","app/birt/birtOrg", "ajaxfileupload","download",
	        'validatejs',"ztreecore","ztreecheck","jqueryPager"],function($,ajax,jquery_ui,form,dialog,ctrl,birtOrg,ajaxfileupload,download){
	    
		function refreshTree(){
			//var obj = $.fn.zTree.getZTreeObj("user_girt_ztree1");
			var treeNode = $zTree.getNodeByTId(currNode.tId);
			var treeParentNode = undefined;
			if(currNode.parentCparentChange)	
				treeParentNode = treeNode.getParentNode();	
			requestTreeData("user_girt_ztree1", treeParentNode||treeNode);
		}
		var setting ;
		var setting2;
				
	
		function ztree(){
			var $oNewTree = self.container.find("#user_girt_ztree1");
			$oNewTree.appendTo(self.container.find("#user_girt_ztreediv"));
			setting = {
				data: {
					simpleData: {
						enable: true
					},
					keep :{
						parent:true
					}
				},
				edit:{
					enable:true,
					showRemoveBtn:false,
					showRenameBtn:false,
					removeTitle:"remove",
					drag:{
						isMove:false,
						isCopy:true,
						prev:true,
						inner:true,
						next:true,
					},
					
					
					},
				
				callback: {
					onExpand:beforExpand,
					onCollapse:onCollapse,
//					onClick:onClick,
					beforeDrag:beforeDrag,
					onDrag:onDrag,
					beforeDrop:beforeDrop,
					onDrop:onDrop,
					onDargMove:onDargMove
				},
				
			};
			setting2 = {
					data: {
						simpleData: {
							enable: true
						},
						keep :{
							parent:true
						}
					},
					edit:{
						enable:true,
						showRemoveBtn:true,
						showRenameBtn:true,
						removeTitle:"remove",
						renameTitle:"rename",
						drag:{
							isMove:false,
							isCopy:false,
							prev:true,
							inner:function(treeid,treenodes,targeNode){
								if(targeNode!=null&&targeNode.dropInner===false){
									return false;
								}
								return true;
							},
							next:true,
						},
						
						
						},
					
					callback: {
						onExpand:beforExpand,
						onCollapse:onCollapse,
//						onClick:onClick,
						beforeDrag:beforeDrag,
						onDrag:onDrag,
						beforeDrop:beforeDrop,
						onDrop:onDrop,
						onDargMove:onDargMove
					},
					
				};
			ajaxInitTree(1);
			/* 展开树   */
			function beforExpand(e,treeId, treeNode){
				requestTreeData(treeId, treeNode);
			};
			//折叠树
			function onCollapse(e,treeId, treeNode){
				var obj = $.fn.zTree.getZTreeObj(treeId);
				obj.setting.data.keep.parent = true;
				obj.removeChildNodes(treeNode);
			};
			function beforeDrahs(treeid,treeNodes){
				return true;
			};
			
			function onClick(e, treeId, treeNode) {
		
				currNode.parentId = treeNode.id;
				if(!treeNode.isParent){
					var parentNode    = treeNode.getParentNode() || null;
					currNode.tId      = parentNode == null ? null:parentNode.tId;
				}
				else{
					currNode.tId      = treeNode.tId;
				}
				
				refreshSubList(treeNode);
			};
			function beforeDrag(treeId, treeNodes) {
				for (var i=0,l=treeNodes.length; i<l; i++) {
					if (treeNodes[i].drag === false) {
						return false;
					}
					if (treeNodes[i].pId==null||treeNodes[i].pId <0) {
						return false;
					}
				}
				return true;
			}
			function onDargMove(event,treeId,treeNodes){
				for (var i=0,l=treeNodes.length; i<l; i++) {
					treeNodes[i].dropInner=false;
					
				}
			}
			function onDrop(event,treeId, treeNodes,targetNode,moveType,isCopy) {
				for (var i=0,l=treeNodes.length; i<l; i++) {
					if (treeNodes[i].drag === false) {
						return false;
					}
				
					if(treeNodes[i].dropInner==false)
						return false;
				}
				return true;
			}
			function onDrag(event,treeId, treeNodes) {
				for (var i=0,l=treeNodes.length; i<l; i++) {
					if (treeNodes[i].drag === false) {
						return false;
					}
				}
				return true;
			}
			function beforeDrop(treeId, treeNodes, targetNode, moveType) {
					for (var i=0,l=treeNodes.length; i<l; i++) {
						if(treeNodes[i].dropInner!=false)
					treeNodes[i].dropInner=false;
					
				}
				return targetNode ? targetNode.drop !== false : true;
			}
			
			
		}
		function refreshSubList(treeNode,pager){
			var pk  	 = -1;
			var isParent = null;
			if(treeNode){
				pk 	 	 = treeNode.id;
				isParent = treeNode.isParent;
			}else{
				currNode.parentId = -1;   //当前节点已被删除,重新定位父节点
			}
			
			var orgName = self.container.find('#orgTree_datagrid div.query-form input[name=orgName]').val();
			
				if(!pager)
				   pager = ctrl.newPage(self.container.find('#orgTree_datagrid'));
				ctrl.getHtml("orgBirt/subList/" + pager.curSize + "/" + pager.pageSize+"?pk="+pk+"&isParent=false&qname="+orgName,function(htmlStr) {
					refreshs(htmlStr);
	     	    });				
			

		}
		/* 初始化树 */
		function ajaxInitTree(level){
			ctrl.getJson("orgBirt/tree/lists/"+level,function(data){
				ctrl.log(data.orgJson);
				var zNodes = $.parseJSON(data.orgJson);
				
				$zTree = $.fn.zTree.init(self.container.find("#user_girt_ztree1"), setting, zNodes);
				var zTree2 = $.fn.zTree.init(self.container.find("#user_demo_ztree1"), setting2);
			});
		}
		function requestTreeData(treeId, treeNode){
			if(treeNode){
				ctrl.getJson("orgBirt/tree/children/rep?pk="+treeNode.id,function(data){
					ctrl.log(data.orgJson);
					var treeObj = $.fn.zTree.getZTreeObj(treeId);
					var child = $.parseJSON(data.orgJson);
					treeObj.removeChildNodes(treeNode);
					if(child.length == 0){
						treeObj.setting.data.keep.parent = false;
					}else{
						treeObj.setting.data.keep.parent = true;
						treeObj.addNodes(treeNode,child);
					}
				});
			}else{
				ajaxInitTree(1);
			}
		}
	
        function refreshs(htmlStr){
        	var $html = $(htmlStr);
        	var $tbody = self.container.find("#user_data_rows");
			$tbody.children().remove();
			$tbody.parent().next().remove();
			$tbody.append($html.find('tr'));
			var $pager = $html.find('>div');
			if($pager.size()){
				$pager.insertAfter($tbody.parent());
				renderPager($pager);
			}else{
				$tbody.parent().next().remove();
			}
		
        }
        function clearInput(){
			$('#orgEdit input:text:lt(2)').val('');
		};
		
		var pages = ctrl.newPage();
	    var o = self = {
	     render:function(container,selfs){
	    	 self.container=container   
	    	 ztree();
	    	 container.find("#save").on("click",function(){
	    		var name  = container.find("#name").val();
	    		
	    			
	    			var obj = $.fn.zTree.getZTreeObj("user_demo_ztree1").getNodes();
	    			var m="";
	    			for (var int = 0; int < obj.length; int++) {
	    				if(m!=""){
	    					m+=","
	    				}
	    				m+=obj[int].id;
	    			}
	    			var webRetrieveResult={
	    					directory:m,
	    					name:name
	    			}
	    	if(name==undefined||name==""){	
	    		ctrl.alert("请输入合并文件名称");
	    		return false;
	    	}
	    	else if(m==""){
	    		ctrl.alert("请选择合并文件");
	    		return false;
	    	}
	    	
	    			ctrl.postJson("birt/mergeReports",webRetrieveResult,"合并脚本成功!",function(user){
	    				selfs.render(container);
//      			   $("#big-modal").modal('hide');
	    			});  
	    		
	    	 })
	    	  container.find("#to_goback").on("click",function(){
	    		 selfs.render(container);});
	           }	
	    		
	    };
       return o;
	});
    
})();