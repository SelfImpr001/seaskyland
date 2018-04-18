(function(){
	"use strict";
	define(['jquery','ajax','validatejs',"ztreecore","ztreecheck","ajaxfileupload","ajaxmodal","dialog","controller","formToJosn"],
			function($,ajax,a,b,c,ajaxfileupload,ajaxmodal,dialog,ctrl,formToJson){
		
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
						maxlength: 100
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
						maxlength:'名称长度超过100个字符'
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
			   type:'module',
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
						$("body").one("change","input#fileUpload[type='file']",function(){
							fileupload();
						});
					});
	           },
	           remove:function(res,removeName){
					ctrl.remove("res","确定要删除<b style='color:red;'>"+(removeName||"")+"</b>吗？",
						"<b style='color:red;'>"+ removeName + '</b>删除成功！',res,
						function(data){
					    	var obj      = $.fn.zTree.getZTreeObj(self.type + "_ztree1");
							var treeNode = obj.getNodeByTId(currNode.tId);
							refreshSubList(treeNode);
							refreshTree();
						}
				   );
	           },
	           update:function(url){
	        	   ctrl.getHtml(url,function(htmlStr) {
	        		   var $dialog = ctrl.modal("资源管理>编辑",htmlStr,function() {
							var $form = $dialog.find('#'+self.type+'_form');
							$form.validate(validateRules);
							if(self.commit($form)){
								return true;
							}
							return false;
						},"保存");
	        		   $dialog.on("change","input#fileUpload[type='file']",function(){
							fileupload();
						});
	        		   
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
		   				
		   				var urlresource = form.formToJson();
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
			
			function bindEvent(){
				var $obj = self.container;
		    	$obj.unbind().on('click','button,#menu_datagrid a[trigger],#module_datagrid a[trigger],#menu_datagrid a[trigger],#data_datagrid a[trigger]',function(){
		    		
		    		var trigger = $(this).attr('trigger');
		    		var pk = $(this).attr("pk");
		    		switch(trigger){
		    		case 'update' :	    			
						var type = self.type;
						var url  = "res/updateUI?pk=" + pk + "&type="+type+"&ui_all=true";
						ctrl.log(self.toString());
						self.update(url);
		    			break;
		    		case 'remove' :
						var name = $(this).parent().parent().parent().children(":eq(1)").text();
						//var type = 
						//var url  = "res/delete?uriType="+$('input#menuType').val()+"&pk="+pk+"&uuid="+currNode.parentUuid;
						self.remove({pk:pk,uuid:currNode.parentUuid},name);
		    			break;	    			
		    		case 'create' :
		    			var uuid = currNode.isParent ? currNode.parentUuid:currNode.uuid;
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
					
					$("input#fileUpload[type='file']").on("change",function(){
						fileupload();
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
					callback: {
						onExpand:onExpand,
						onCollapse:onCollapse,
						onClick:onClick
					}
				};
				ajaxInitTree();
				/* 展开树   */
				function onExpand(e,treeId, treeNode){
					requestTreeData(treeId, treeNode);
				}
				//折叠树
				function onCollapse(e,treeId, treeNode){
					if(treeNode.pId == null) return;
					var obj = $.fn.zTree.getZTreeObj(treeId);
					obj.removeChildNodes(treeNode);
					treeNode.isParent = true;
				}
				function onClick(e, treeId, treeNode) {
					currNode.isParent = treeNode.isParent;
					currNode.uuid     = treeNode.uuid;
					if(!treeNode.isParent){
						if(treeNode.getParentNode()){
							var parentNode    = treeNode.getParentNode();
							currNode.tId      = parentNode.tId || -1; 
						}else{
							currNode.tId      = treeNode.tId || -1;
						}
					}
					else{
						currNode.parentUuid   = treeNode.uuid ;
						currNode.tId          = treeNode.tId || -1;
					}
					refreshSubList(treeNode);
				}
			}
			function refreshSubList(treeNode){
				currNode.url      =  "res/chidren/" + treeNode.uuid+"?isParent="+treeNode.isParent+"&menuList=0";
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
			/**
			 * 初始化树
			 */
			function ajaxInitTree(){
				currNode.url = "res/nodelist?level=1&type=" + self.type+"&menuList=0";
				ctrl.getJson(currNode.url,function(data){
					
					var zNodes = $.parseJSON(data.resJson);
					var zTree  = $.fn.zTree.init(self.container.find("#"+self.type+"_ztree1"), setting, zNodes);
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
			}
			/**
			 * 刷新树
			 */
			function requestTreeData(treeId, treeNode){
				if(treeNode.pId){
					currNode.parentUuid  = treeNode.uuid;//保存父节点uuid
					currNode.url =  "res/chirdren/tree/" + treeNode.uuid;
					if(treeNode.level == 1) 
						currNode.url += "?type="+self.type+"&menuList=0"; //menuList 0菜单 1 用户授权
					
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