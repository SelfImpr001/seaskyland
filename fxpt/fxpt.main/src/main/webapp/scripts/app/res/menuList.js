(function(){
	"use strict";
	define(['jquery','ajax','validatejs',"ztreecore","ztreecheck","ajaxfileupload","ajaxmodal","dialog","controller","formToJosn","editTable","jqueryui","ztreecore","ztreecheck","ztreeexedit"],
			function($,ajax,a,b,c,ajaxfileupload,ajaxmodal,dialog,ctrl,formToJson,editTable){
		
	   function init(){
			var currNode = {
					"id":-1,
					"uuid":-1,
					"parentId": -1,
					"parentUuid":-1,
					"url":null,
					"isParent":false,
					"type":"menu"
			};
			var self = undefined;
			var validateRules = {
			 rules: {
					name:{
						required: true,
						maxlength: 200
					},
					url:{
						maxlength: 200
					},
					reorder:{
						required: true,
						number: true
					},
					remarks:{
						maxlength: 255
					}
				},
				messages: {
					name: {
						required: '名称不能为空',
						maxlength:'名称长度超过200个字符'
					},
					url: {
						maxlength:'名称长度超过200个字符'
					},
					reorder: {
						required: '排列顺序不能为空',
						number:'排列顺序只能是合法数字'
					},
					remarks: {
						maxlength:'名称长度超过255个字符'
					}
				}			
			};		
			
			var o = self = {
			   container:undefined,
			   type:'menu',
			   toString:function(){
				   return this.type ;
			   },
	           render:function(container){
	        	   
	        	   if(container)
	        	       this.container = container;
	        	   currNode.parentUuid = -1;
	        	   var url = ctrl.getCurMenu().attr("url")+"&menuList=0";
	        	   ctrl.getHtml(url,function(html) {        		  
	        		   ctrl.appendToView(self.container,$(html));
	        		   self.type = self.container.find('input[type=hidden]:eq(0)').val();
	        		   bindEvent();
	  				   //init();
	  				   ztree();
	  				   //addMenu();
	        	   });
	        	 //this.caleHeight();
	           },
	           save:function(url){
					ctrl.getHtml(url,function(htmlStr) {
						var $dialog = ctrl.modal("资源管理>新增",htmlStr,function() {
							var $form = $dialog.find('#'+self.type+'_form');
							$form.validate(validateRules);
							if(self.commit($form)){
								return true;
							}
							return false;
						},"保存");
						 removeIconAdd($dialog);
						ctrl.selectAll($dialog,'checkAll','gardeIds');
		        		ctrl.selectAll($dialog,'checkAll1','examTypeIds');
						$("body").one("change","input#fileUpload[type='file']",function(){
							fileupload();
						});
					});
	           },
	           remove:function(message,res,removeName,allType){
	        	   var sltype="";
	        	   if(message=="1"){
	        		   if(allType=="menu"){
	        			   sltype="菜单";
	        		   }else{
	        			   sltype="报告";
	        		   }
	        		   ctrl.remove("res?message="+message,"有角色和用户使用了该"+sltype+",确定要删除<b style='color:red;'>"+(removeName||"")+"</b>吗？",
		   						"<b style='color:red;'>"+ removeName + '</b>删除成功！',res,
		   						function(data){
		   					    	var obj      = $.fn.zTree.getZTreeObj(self.type + "_ztree1");
		   							var treeNode = obj.getNodeByTId(currNode.tId);
		   							refreshSubList(treeNode);
		   							refreshTree();
		   					});
	        	   }else{
	        		   ctrl.remove("res?message="+message,"确定要删除<b style='color:red;'>"+(removeName||"")+"</b>吗？",
		   						"<b style='color:red;'>"+ removeName + '</b>删除成功！',res,
		   						function(data){
		   					    	var obj      = $.fn.zTree.getZTreeObj(self.type + "_ztree1");
		   							var treeNode = obj.getNodeByTId(currNode.tId);
		   							refreshSubList(treeNode);
		   							refreshTree();
		   					});
	        	   }
					
	           },
	           update:function(url){
	        	   ctrl.getHtml(url,function(htmlStr) {
	        		   var $htmlObj = $(htmlStr);
	        		   var $dialog = ctrl.modal("资源管理>编辑",htmlStr,function() {
							var $form = $dialog.find('#'+self.type+'_form');
							$form.validate(validateRules);
							if(self.commit($form)){
								return true;
							}
							return false;
						},"保存");
	        		   removeIcon($dialog);
	        		   $dialog.on("change","input#fileUpload[type='file']",function(){
							fileupload();
						});
	        		   ctrl.selectAll($dialog,'checkAll','gardeIds');
	        		   ctrl.selectAll($dialog,'checkAll1','examTypeIds');
	        	   });
	           },
	           commit:function(form){
	        	   var bo = form.valid();
	        	   if(bo){ //验证
		   				var url  = form.attr("action");
		   			    var type = form.attr("method");
		   			    if(type == "post")
		   			    	url  +="?uriType=" + self.container.find('input#menuType').val(); 
		   			    
		   				var gradesstrs = '';
		   				$(".gardeIds:checked").each(function() {
//		   					grades[$(this).val()] = $(this).attr("data-name");
		   					gradesstrs += $(this).val()+',';
		   				});
		   				
		   				var examTypestrs = '';
		   				$(".examTypeIds:checked").each(function() {
		   					examTypestrs += $(this).val()+',';
		   				});
		   				
		   				var icon = $("#uploadImg").attr("src");
		   				var urlresource = form.formToJson();
		   				if(icon==""){
		   					urlresource.icon="";
		   				}
		   				urlresource["gradeids"] = gradesstrs;
		   				urlresource["examtypeids"] = examTypestrs;
		   				ctrl.log("urlresource :" + JSON.stringify(urlresource));
		   				var successMsg = "保存成功!";
						if(type == "put") successMsg = "修改成功!";
		   				ajax({
		   					url :url,
		   					type: type,
		   					dataType:"html",
		   					data:JSON.stringify(urlresource),
		   					successMsg:{
								show:true,
								header:{show:false},						
								tip_txt:successMsg,
							},
		   					callback : function(htmlStr) {
		   						self.container.find("#" + self.type + "_data_rows").html(htmlStr);  
		   						//init();
		   						refreshTree();
		   					}
		   				});
		   			}
		   			return bo;
	           },
	           caleHeight:function(){
	        	   /* 计算列表区域高度 pageContent = bodyHeight - navbarHeight*/
	        	   ctrl.log("列表区域高度 = bodyHeight{" + $("body").height() + "} - navbarHeight{" + $("#navbar").height() +"}");
	        	   var pageContentHeight = $("body").height() - $("#navbar").height();
	        	   $(".main-content").height(pageContentHeight);
	           }
	       };
			function refreshSubList(treeNode){
				currNode.url      =  "res/chidren/" + treeNode.uuid+"/0?isParent="+treeNode.isParent;
				currNode.thisUuid = treeNode.uuid;
				if(treeNode.level <= 1) 
					currNode.url += "&type="+self.type;
				
				if(treeNode.isParent)
					currNode.parentUuid = treeNode.uuid;//保存父节点uuid
				
				ctrl.getHtml(currNode.url,function(htmlStr) {
					//var type = self.container.find('#menuType').val();
					var $datagrid = self.container.find("tbody");
					$datagrid.children().remove();
					$datagrid.append(htmlStr);
					//self.container.find("#app_"+type+"_data_rows").html(htmlStr);  
					//	init();
	      	    });
			}
		//删除菜单icon	
		function removeIcon(container){
			container.on("click","#removeIcon",function(){
				var pk = $("#resPk").val();
				//var type =$("#resType").val();
				ctrl.postJson("/res/deleteIcon/"+pk,{},"删除图片成功！",function(data){
					//清除页面icon
					$("#uploadImg").attr("src","");
					$("#removeIcon").css("display","none");
					//container.dialog('close');
					//var url  = "res/updateUI?pk=" + pk + "&type="+type+"&ui_all=true";
					//self.update(url);
				});
			})
		}
		//新增页面方法
		function removeIconAdd(container){
			container.on("click","#removeIconAdd",function(){
				//清除页面icon
				$("#uploadImg").attr("src","");
				$("#removeIconAdd").css("display","none");
			})
		}
			function bindEvent(){
				var $obj = self.container;
		    	$obj.unbind().on('click','button,#menu_datagrid a[trigger],#module_datagrid a[trigger],#menu_datagrid a[trigger],#data_datagrid a[trigger]',function(){
		    		var trigger = $(this).attr('trigger');
		    		var pk = $(this).attr("pk");
		    		var type =$(this).attr('triggerType');
		    		switch(trigger){
		    		case 'update' :	  
						//var type = $("input[name='type']").val();
						var url  = "res/updateUI?pk=" + pk + "&type="+type+"&ui_all=true";
						ctrl.log(self.toString());
						self.update(url);
		    			break;
		    		case 'remove' :
		    			var name = $(this).parent().parent().parent().children(":eq(1)").text();
		    			ctrl.getJson("res/checkMenuId/"+pk,function(data){
		    				var allType=$("#menuType").val();
			    			if(data.message=="1"){
			    				self.remove(data.message,{pk:pk,uuid:currNode.parentUuid},name,allType);
			    			}else{
			    				self.remove(data.message,{pk:pk,uuid:currNode.parentUuid},name,allType);
			    			}
						});
						//var type = 
						//var url  = "res/delete?uriType="+$('input#menuType').val()+"&pk="+pk+"&uuid="+currNode.parentUuid;
		    			break;	    			
		    		case 'create' :
		    			var uuid = currNode.isParent ? currNode.parentUuid:currNode.uuid;
		    			if(currNode.thisUuid){
		    				uuid = currNode.thisUuid;
		    			}
						var url  = "res/addMenuUI?uuid=" + uuid+"&type="+self.type;
						self.save(url);
		    			break;	    			
		    		default:
		    			break;
		    		}
		    	});
			};

			function convertType(type){
				var typeName = "菜单";
				if (type == "data"){
					typeName = "数据权限";
				}else if (type=="module"){
					typeName ="模块";
				}
				return typeName;
			}

			function fileupload(){
				ctrl.imgUpload("res/fileUpload","fileUpload",function(obj){
					//var obj  = $.parseJSON(data);
					var path = $("#uploadImg").attr("name");
					ctrl.log("操作成功,图片存储路径:" + path + obj.tempPath);
					$("#uploadImg").attr("src",path + obj.tempPath);
					$("#uploadImg").parent().show();
					$("#icon").val(obj.imagePath);
					$("#removeIcon").css("display","");
					$("input#fileUpload[type='file']").on("change",function(){
						fileupload();
						$("#removeIconAdd").css("display","");
					});
				});
			}
			/**
			 *  刷新树
			 */
			function refreshTree(){
				var obj = $.fn.zTree.getZTreeObj(self.type + "_ztree1");
				var treeNode = obj.getNodeByTId(currNode.tId);
				requestTreeData(self.type + "_ztree1", treeNode);
			}
			var setting;
			function ztree(){
				
				//var $oNewTree = self.container.find("#"+self.type+"_ztree1");
				//$oNewTree.appendTo(self.container.find("#"+self.type+"_ztreediv"));
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
								isMove:true,
								isCopy:false,
								prev:true,
								inner:true,
								next:true,
							},
							
							
							},
						
						callback: {
							onExpand:beforExpand,
							onCollapse:onCollapse,
							onClick:onClick,
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
						if(targetNode==null){
							 ctrl.confirm("提示信息","不允许移动到该目录下！",function(){
			     			  });
							 ajaxInitTree();
						}else{
							var obj = $.fn.zTree.getZTreeObj(self.type + "_ztree1");
							var type=self.type;
							//用作排序
							var broId ="-1";
							var tId=treeNodes[0].tId;
							var broNode="";
							if(tId!=""){
								//前一个兄弟节点
								broNode=$("#"+tId).prev();
							}
							if(broNode!=undefined && broNode.length>0){
								//获得兄弟节点
								broId=obj.getNodeByTId(broNode[0].id).id;
							}
							//移动的节点
							var ids=treeNodes[0].id;
							//移动的目标父节点
							var parentId=targetNode.id;;
								ctrl.postJson("/res/dragRes/"+ids+"/"+parentId+"/"+type+"/"+broId,{},"移动成功！",function(data){
										var obj      = $.fn.zTree.getZTreeObj(self.type + "_ztree1");
										var treeNode = obj.getNodeByTId(currNode.tId);
										refreshSubList(treeNode);
										refreshTree();	
			   						
									//requestTreeData(parentName,obj.getNodeByTId(parentName));
								});
						}
						
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
			function refreshSubList(treeNode){
				currNode.url      =  "res/chidren/" + treeNode.uuid+"/0?isParent="+treeNode.isParent;
				currNode.thisUuid = treeNode.uuid;
				if(treeNode.level <= 1) 
					currNode.url += "&type="+self.type;
				
				if(treeNode.isParent)
					currNode.parentUuid = treeNode.uuid;//保存父节点uuid
				
				ctrl.getHtml(currNode.url,function(htmlStr) {
					//var type = self.container.find('#menuType').val();
					var $datagrid = self.container.find("tbody");
					$datagrid.children().remove();
					$datagrid.append(htmlStr);
					//self.container.find("#app_"+type+"_data_rows").html(htmlStr);  
					//	init();
	      	    });
			}
			
			}
			
//			var name =""; 
//			/**jquery ui sortable**/
//			function uiSortable(container){
//				var sourceNum = 0;
//				container.find(".sortables").sortable({
//					scroll:true,
//					placeholder:"ui-state-highlight",
//					connectWith:true,
//					items:"li",               //只是li可以拖动
//					revert:false,              //释放时加动画效果
//	 				activate: function(event, ui){
//	 					name =event.toElement.id;
//					},
//					update:function(event, ui){ //释放后的调用方法
//						var type=self.type;
//						var obj = $.fn.zTree.getZTreeObj(self.type + "_ztree1");
//						var ids="";
//						var names = name.substring(0,name.length-5);
//						//移动的兄弟节点
//						var broId ="-1";
//						var broNode ="";
//						//移动的节点
//						ids=obj.getNodeByTId(names).id;
//						if(names!=""){
//							//前一个兄弟节点
//							broNode=$("#"+names).prev();
//						}
//						if(broNode.length>0){
//							//获得兄弟节点
//							broId=obj.getNodeByTId(broNode[0].id).id;
//						}
//						//移动的目标父节点
//						var parentName=ui.item[0].parentNode.parentNode.children[1];
//						var parentId="";
//						if(parentName==undefined){
//							parentId="-1";
//						}else{
//							parentName=ui.item[0].parentNode.parentNode.children[1].id;
//							parentName=parentName.substring(0,parentName.length-2);
//							parentId = obj.getNodeByTId(parentName).id;
//						}
//						if(parentId=="-1"){
//							 ctrl.confirm("提示信息","不允许移动到该目录下！",function(){
//			     			  });
//							 ajaxInitTree();
//						}else{
//							ctrl.postJson("/res/dragRes/"+ids+"/"+parentId+"/"+type+"/"+broId,{},"移动成功！",function(data){
//								//var obj      = $.fn.zTree.getZTreeObj(self.type + "_ztree1");
//	   							var treeNode = obj.getNodeByTId(currNode.tId);
//	   							refreshSubList(treeNode);
//	   							refreshTree();
//		   						
//								//requestTreeData(parentName,obj.getNodeByTId(parentName));
//							});
//						}
//					}
//				
//				});
				//container.find(".sortablecollapsible").accordion({collapsible:true});
//			}
			/**
			 * 初始化树
			 */
			function ajaxInitTree(){
				currNode.url = "res/nodelist?level=1&type=" + self.type+"&menuList=0";
				ctrl.getJson(currNode.url,function(data){
					
					var zNodes = $.parseJSON(data.resJson);
					var zTree  = $.fn.zTree.init(self.container.find("#"+self.type+"_ztree1"), setting, zNodes);
					//$.fn.zTree.init($("#"+self.type+"_ztree1"), setting1, zNodes);
					var nodes  = zTree.getNodes();
					if(currNode.parentUuid == -1){
						currNode.isParent   = nodes[0].isParent;
						currNode.parentUuid = nodes[0].uuid;
						currNode.uuid       = nodes[0].uuid;
						currNode.tId        = nodes[0].tId;
						for (var i = 1; i < nodes.length; i++) {
							if(nodes[i].open && nodes[i].isParent && nodes[i].pId == null){
								currNode.isParent   = nodes[i].isParent;
								currNode.parentUuid = nodes[i].uuid;
								currNode.uuid       = nodes[i].uuid;
								currNode.tId        = nodes[i].tId;
								break;
							}
						}
					}
				});
				
				//uiSortable(self.container);
			}
			/**
			 * 刷新树
			 */
			function requestTreeData(treeId, treeNode){
				if(treeNode.pId){
					currNode.parentUuid  = treeNode.uuid;//保存父节点uuid
					currNode.url =  "res/chirdren/tree/" + treeNode.uuid+"/0";
					currNode.url += "?type="+self.type;
					currNode.thisUuid = treeNode.uuid;
					
					ctrl.getJson(currNode.url,function(data){
						ctrl.log(data.resJson);
						var treeObj = $.fn.zTree.getZTreeObj(treeId);
						var child = $.parseJSON(data.resJson);
						treeObj.removeChildNodes(treeNode);
						if(child.length == 0){
							treeObj.setting.data.keep.parent = false;
						}else{
							treeObj.setting.data.keep.parent = true;
							treeObj.addNodes(treeNode,child);
						}
					});
				}else{
					ajaxInitTree();
				}
			}
		   
		   this.doRender = function(container){
			   o.container = container;
			   return o.render();
		   };
	   }
		
       return {
    	   render:function(container){
    		   var render = new init();
    		   render.doRender(container);
    	   }
       };
	});
})();