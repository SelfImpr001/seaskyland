(function(){
	"use strict";
	define(['jquery','ajax','validatejs',"dialog",'controller',"formToJosn","../etl/reportExecutor","download","jqueryui","ztreecore","ztreecheck","jqueryPager"],
	function($,ajax,validate,dialog,ctrl,formToJson,reportExecutor,download){
		
		var self = undefined;
		 var validateRules = {
				rules : {
					name : {
						required : true,
						maxlength:50
					}
				},
				messages : {
					name : {
						required : '请输入组织名称',
						maxlength:'名称长度不能超过50个字符'
					}
				}
//				errorPlacement:function(error, element){
//					//element.parent().find('div.tooltip').remove();
//					//element.tooltip('destroy');
//					//element.tooltip({title:error.html(),trigger:''});
//					//element.tooltip('show');
//				}
			}; 
		var currNode,$zTree,$treeNodeNoe;
		var o = self = {
		   container:undefined,
		  
           render:function(container){
        	   if(container)
        	       this.container = container;
        	   //self.query();
        	   //self.caleHeight();
        	   currNode = {
   					"id":-1,
   					"parentChange":false,
   					"parentId": -1,
   					"isParent":false
   			   };
               var url = "org/list?ui_all=true";
	     	   ctrl.getHtml(url,function(html) {
	     	       ctrl.appendToView(self.container,html);
	     		   //$(".main-content").html(htmlStr);
	     	      bindEvent();
				  ztree();
				  ctrl.initUI(self.container);
				  reportEvent(function ref(){
					  var treeNode = $zTree.getNodeByTId(null);
						refreshSubList(treeNode);
						refreshTree();
				  });
				  temDownLoad();
				   //add();
	     	   });
	        },
	        save:function(url){
				ctrl.getHtml(url,function(htmlStr) {
					var $dialog = ctrl.modal("组织管理>新增",htmlStr,function() {
						var $form = $dialog.find('#orgEdit');
						$form.validate(validateRules);
						var flg=false;
						var code= document.getElementById("code").value;
						if(code!=null && code!="" ){
							$(htmlStr).find("#roleNum").hide();
							if($form.valid()){
									var org = $form.formToJson();
									if($("#schoolTypeId").size()){
										org.schooltype ={id:$("#schoolTypeId").val()};							
									}
									if($("#schoolSegmentId").size()){
										org.schoolSegment = {id:$("#schoolSegmentId").val()};
									}
									self.commit(org,"POST");
									if($dialog.find('#cnt')[0].checked){
										return flg;
									}else{
										flg=true;
										return flg;
									}
							}
								
						}else{
							document.getElementById("roleNum").innerText="请输入组织编码";
							document.getElementById("roleNum").style.display="";
						}
						
					},"保存");
					if(currNode.parentId==-1){
						var $selectEd = $dialog.find("select[name='type']");
						$selectEd.find("option:gt(1)").remove();
						$selectEd.attr("disabled",false);
						$selectEd.next().find("ul li:gt(1)").remove();
					}else{
						$dialog.find("select[name='type']").attr("disabled",true);
					}
					$dialog.find('select.selectpicker').selectpicker();
					onBlurNewAdd($dialog);
					var $newField = $dialog.find('#orgEdit div.form-group:eq(0)').clone();
					$newField.find('label').text('').next().remove();
					$('<div class="checkbox" style="float:left;margin-left: 15px"><label><input type="checkbox" id="cnt">连续新增</label></div>').insertAfter($newField.find('label'));
					$newField.css({'padding-bottom':0,'margin-bottom':0});
					//$newField.find('input').removeClass('form-control').text('连续新增')[0].type='checkbox';
					$dialog.find('#orgEdit>div').append($newField);
				});
	        },
	        remove:function(url,removeName,data){
	     	   	ctrl.log("delete :" + url);
	     	    ctrl.postJson("org/deleteValidate/"+data.pk,"","", function(data1){
	     		  if(data1.flg){
	     			 if(data1.mes==""){
			   				ctrl.remove(url,"确定要删除组织<b style='color:red;'>"+(removeName||"")+"</b>吗？","<b style='color:red;'>" + removeName + '</b>删除成功！',data,
							    function(data){
									var obj = $.fn.zTree.getZTreeObj("orgTree_ztree1");
									var treeNode = obj.getNodeByTId(currNode.tId);
									refreshSubList(treeNode);
									refreshTree();
							    }
						   );
		     		   }else{
		     			  ctrl.confirm("提示信息","<b style='color:red;'>"+(removeName||"")+"</b>"+data1.mes+"不允许删除！",function(){
		     			  });
		     		   }
	     		  }else{
	     			 ctrl.confirm("提示信息",data1.mes,function(){ });
	     		  }
	     	    });
	        },
	        orgOut:function(url){
		     	   ctrl.getHtml(url,function(htmlStr) {
		     	   });
		        },
	        update:function(url){
	     	   ctrl.getHtml(url,function(htmlStr) {
	     		  var $dialog = ctrl.modal("组织管理>修改",htmlStr,function() {
	     			  var $form = $dialog.find('#orgEdit');
	     			  $form.validate(validateRules);
	     			  var flg=false;
	     			  var code= document.getElementById("code").value;
					  var orgId=document.getElementById("orgId").value;
					  if(code!=null && code!="" ){
								$(htmlStr).find("#roleNum").hide();
								//document.getElementById("roleNum").style.display=="none";
			    			if($form.valid() ){
								var org = $form.formToJson();
								if($("#schoolTypeId").size()){
									org.schooltype ={id:$("#schoolTypeId").val()};							
								}
								if($("#schoolSegmentId").size()){
									org.schoolSegment = {id:$("#schoolSegmentId").val()};
								}
								self.commit(org,"PUT");
								flg=true;
								return flg;							
							}
							return flg;
						}else{
							document.getElementById("roleNum").innerText="请输入组织编码";
							document.getElementById("roleNum").style.display="";
							return flg;
						}
					  return flg;
	     		  },"保存");
	     	//	 onBlurUpdate($dialog);
	     		 $dialog.find('#orgEdit select[name=parent]').change(function(){
						$dialog.find(':hidden:eq(1)').val(this.value);
						currNode.parentChange = true;
				 });
	     	   });
	        },
	        commit:function(org,method){
	        	ctrl.log(JSON.stringify(org) + "  " + method);
	        	var successMsg = "保存成功!";
				if(method == "put") successMsg = "修改成功!";
	    	    ajax({
					url : "org",
					async : true,
					type : method,
					dataType:"html",
					data : JSON.stringify(org),
					successMsg:{
						show:true,
						header:{show:false},						
						tip_txt:successMsg,
					},
					callback : function(data) {
						refresh(data);
						refreshTree();
						clearInput();
					}
				 });
	        },
	        caleHeight:function(){
        	   /* 计算列表区域高度 pageContent = bodyHeight - navbarHeight*/
        	   ctrl.log("列表区域高度 = bodyHeight{" + $("body").height() + "} - navbarHeight{" + $("#navbar").height() +"}");
        	   var pageContentHeight = $("body").height() - $("#navbar").height();
        	   $(".main-content").height(pageContentHeight);
           },
           query:function(page){
			   var treeNode = $zTree.getNodeByTId(currNode.tId);
			   if(treeNode == null){
				   treeNode = $zTree.getSelectedNodes()[0];
			   }
			   refreshSubList(treeNode,page,false);
           },
       	userview:function(pk,page,dialog){
			ctrl.log("pk:" + pk);
			 if(!page){
	     		   page = ctrl.newPage(dialog);
	     	   }
			ctrl.getHtmlDialog("org/userview/1/"+15+"?roleId="+pk,"viewPanel",function(dialog){
				renderUserPager(pk,dialog,page);
			});				
		},
		queryUser:function(pk,page,dialog){
     	   if(!page){
     		   page = ctrl.newPage(dialog);
     	   }
     	   var url = "org/userview/" + page.curSize + "/" + page.pageSize;
     	   ctrl.getHtml(url+"?roleId="+pk,function(data){
     		  ctrl.refreshDataGrid('viewPanelBody',dialog,$(data),renderPager);
     		  renderUserPager(pk,dialog);
     	   });
        },
       };
		function renderUserPager(pk,dialog,page) {
			   ctrl.renderPager({
					"containerObj" : dialog,
					"pageObj" : dialog.find('#user_role_page'),
					"callBack":function(page){
						page.pageSize = dialog.find("#pageSizeReal").val();
						self.queryUser(pk,page,dialog);
					}
				});	
			   return ;
		}
		//新增 验证组织代码唯一性
		function  onBlurNewAdd(container){
			 container.on('blur','#code',function(){
		    	var code= document.getElementById("code").value;
		    	if(code!=null && code!=""){
		    		ctrl.postJson("org/validate/"+code+"/0","","", function(data){
			    		if(!data.flg){
			    			document.getElementById("roleNum").innerText="组织编码必须唯一";
			    			document.getElementById("roleNum").style.display="";
			    		}else{
			    			document.getElementById("roleNum").style.display="none";
			    		}
			    	});
		    	}else{
		    		document.getElementById("roleNum").innerText="请输入组织编码";
		    		document.getElementById("roleNum").style.display="";
		    	}
		    });
		}		
		
		//修改验证组织代码唯一性
		function  onBlurUpdate(container){
			 container.on('blur','#code',function(){
		    	var code= document.getElementById("code").value;
		    	var orgId=document.getElementById("orgId").value;
		    	if(code!=null && code!=""){
		    		ctrl.postJson("org/validate/"+code+"/"+orgId,"","", function(data){
			    		if(!data.flg){
			    			document.getElementById("roleNum").innerText="组织编码必须唯一";
			    			document.getElementById("roleNum").style.display="";
			    		}else{
			    			document.getElementById("roleNum").style.display="none";
			    		}
			    	});
		    	}else{
		    		document.getElementById("roleNum").innerText="请输入组织编码";
		    		document.getElementById("roleNum").style.display="";
		    	}
		    });
		}		
		
		
		function clearInput(){
			$('#orgEdit input:text:lt(2)').val('');
		};
		
		function bindEvent(){
        	self.container.unbind().on("click","#addBtn",function() {
        		self.save("org/view/"+currNode.parentId + "/-1/add");
        	}).on("click","#orgTree_datagrid a[trigger=remove]",function(){
        		var pk    = $(this).attr("pk");
        		var name  = $(this).parent().parent().parent().children(":eq(1)").text();
        		self.remove("org", name,{pk:pk});
        	}).on("click","#orgTree_datagrid a[trigger=update]",function(){
        		var pk = $(this).attr("pk");
        		self.update("org/view/"+currNode.parentId+"/"+pk+"/update");
        	}).on("click","#orgTree_datagrid a[trigger=orgOut]",function(){
        		var pk = $(this).attr("pk");
        		var type = $(this).attr("type");
        		if(type==undefined){
        			type=1;
        		}
        		//生成excel文件到服务器
        		ctrl.postJson("org/orgOut/"+type+"/"+pk,"","",function(data){
        			if(data.flg=="1"){
        				//下载到本地
                	    var url = window.app.rootPath +"downloadreport/downloadexcelForZZJG/0";
        				$.download(url);
        				$(this).trigger('close');
                		
        			}
        		});
        		
        		//self.orgOut("org/orgOut/"+type+"/"+0);
        		
        		/** 点击下级组织数  */
        	}).on("click","#orgTree_datagrid a[trigger=nextOrg]",function(){  //根据当前组织查询下一级组织信息
        		/*var pk = $(this).attr("pk"),
        		pid = $(this).attr("pid"),
        		type = $(this).attr("type");
        		var treeNode = $.fn.zTree.getZTreeObj("orgTree_ztree1");
        		//判断一级是否展开
        		if(type==2){
        			$treeNodeNoe = treeNode.getNodeByParam("id",pid,null);//获取一级父级节点
        			//展开一级
    				//treeNode.setting.callback.onExpand(null,"orgTree_ztree1",$treeNodeNoe);
        		}
        		if(type==3){
        			var nodetwo = treeNode.getNodeByParam("id",pid,null);//获取二级父节点
        		//	treeNode.setting.callback.onExpand(null,"orgTree_ztree1",nodetwo);
        		}
        		var node = treeNode.getNodeByParam("id",pk,null);
        		if(node!=null){
        			treeNode.setting.callback.onClick(null,"orgTree_ztree1",node);
	        			if(type!=3){
	            			treeNode.setting.callback.onExpand(null,"orgTree_ztree1",node);
	            		}
	            		self.container.find(".curSelectedNode").each(function(){
	            			$(this).removeClass("curSelectedNode");
	            		});
	            		self.container.find("#"+node.tId+"_a").addClass("curSelectedNode");
        		}*/
        		var pk = $(this).attr("pk");
        		var count=$(this).attr("count");
        		if(count>0){
        			ctrl.getHtml("org/subList?pk="+pk+"&isParent="+true ,function(htmlStr) {
    					refresh(htmlStr);
        			});
        		}
        	}).on('click',"#orgTree_datagrid div.query-form a:eq(0)",function(){
        		 var orgName = self.container.find('#orgTree_datagrid div.query-form input[name=orgName]').val();
        		 self.query();
        	}).on('keyup',"#orgTree_datagrid div.query-form input[name=orgName]",function(e){
        		if(e.keyCode == 13){
        			var orgName = self.container.find('#orgTree_datagrid div.query-form input[name=orgName]').val();
	           		if($.trim(orgName).length>0){
	           			self.query();
	        			e.stopPropagation();
	           		}
        			
        		}
        	});
        	
        	self.container.on('click','a[trigger="orglist"]',function(){
				var pk = $(this).attr("pk");
				var count=$(this).attr("count");
				if(count>0){
					self.userview(pk);
				}
			});
        };  
        
        //组织模板下载
		function temDownLoad(){
			self.container.on("click","#temDownLoad",function() {
				var url = window.app.rootPath + "download/org";
				$.download(url);
			});
		}
		
        //组织架构导入
        function reportEvent(callback){
        	self.container.on("click","#report",function() {
        		ctrl.postJson("/org/orExist/","","",function(data){
        					reportExecutor.UI({
    							importType : 1,
    							schemeType : 11,
    							examId : null,
    							title : "导入组织",
    							callBack : callback
    						});
        			});
        		});
        }		               
        
        /**
		 *  刷新树
		 */
		function refreshTree(){
			//var obj = $.fn.zTree.getZTreeObj("orgTree_ztree1");
			var treeNode = $zTree.getNodeByTId(currNode.tId);
			var treeParentNode = undefined;
			if(currNode.parentCparentChange){
				treeParentNode = treeNode.getParentNode();	
			}else{
				if(treeNode.type!=3){
					requestTreeData("orgTree_ztree1", treeParentNode||treeNode);
				}else{
					treeParentNode = treeNode.getParentNode();	
				}
				
			}
		}
		var setting ;
		function ztree(){
			var $oNewTree = self.container.find("#orgTree_ztree1");
			$oNewTree.appendTo(self.container.find("#orgTree_ztreediv"));
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
					onExpand:beforExpand,
					onCollapse:onCollapse,
					onClick:onClick
				}
			};
			ajaxInitTree(1);
		}
		/* 展开树   */
		function beforExpand(e,treeId, treeNode){
			if(treeNode!=null){
				currNode.parentId = treeNode.id;
			}
			requestTreeData(treeId, treeNode);
		};
		//折叠树
		function onCollapse(e,treeId, treeNode){
			var obj = $.fn.zTree.getZTreeObj(treeId);
			obj.setting.data.keep.parent = true;
			//obj.removeChildNodes(treeNode);
		};
		//文字点击
		function onClick(e, treeId, treeNode) {
			if(treeNode!=null){
				currNode.parentId = treeNode.id;
				currNode.tId      = treeNode.tId;
				//次代码放出后会影响组织中学校级别的翻页问题
				/*if(treeNode!=null && !treeNode.isParent){
					var parentNode    = treeNode.getParentNode() || null;
					currNode.tId      = parentNode == null ? null:parentNode.tId;
				} else{
					currNode.tId      = treeNode.tId;
				}*/
			}
			refreshSubList(treeNode,false,false);
		};
//		var name =""; 
//		/**jquery ui sortable**/
//		function uiSortable(container){
//			var sourceNum = 0;
//			container.find(".sortables").sortable({
//				scroll:true,
//				placeholder:"ui-state-highlight",
//				connectWith:true,
//				items:"li",               //只是li可以拖动
//				revert:false,              //释放时加动画效果
// 				activate: function(event, ui){
// 					name =event.toElement.textContent;
//					var trs = $(this).find("> li");
//				},
//				update:function(event, ui){ //释放后的调用方法
//					var names = name;
//					var parentName=ui.item[0].parentNode.parentNode.children[1];
//					if(parentName==undefined){
//						parentName="-1";
//					}else{
//						parentName=ui.item[0].parentNode.parentNode.children[1].textContent;
//					}
//					ctrl.postJson("/org/dragOrg/"+name+"/"+parentName,{},"移动成功！",function(data){
//						refreshTree();
//					});
//				}
//			
//			});
//	}
		function refreshSubList(treeNode,pager,is){
			//is:是否是输入框查询 true 是则否
			var pk  	 = -1;
			var isParent = null;
			debugger;
			if(treeNode){
				pk 	 	 = treeNode.id;
				isParent = treeNode.isParent;
			}else{
				currNode.parentId = -1;   //当前节点已被删除,重新定位父节点
			}
			var orgName = self.container.find('#orgTree_datagrid div.query-form input[name=orgName]').val();
			if(treeNode && treeNode.type && treeNode.type == 3){
				if(!pager)
				   pager = ctrl.newPage(self.container.find('#orgTree_datagrid'));
				ctrl.getHtml("org/subList/" + pager.curSize + "/" + pager.pageSize+"?pk="+pk+"&isParent=false&qname="+orgName+"&is=true",function(htmlStr) {
					refresh(htmlStr);
	     	    });
			}else{
				ctrl.getHtml("org/subList?pk="+pk+"&isParent="+isParent + "&qname="+orgName+"&is="+is,function(htmlStr) {
					refresh(htmlStr);
	     	    });
			}
		}
		/* 初始化树 */
		function ajaxInitTree(level){
			ctrl.getJson("org/tree/list/"+level,function(data){
				ctrl.log(data.orgJson);
				var zNodes = $.parseJSON(data.orgJson);
				$zTree = $.fn.zTree.init(self.container.find("#orgTree_ztree1"),setting,zNodes);
				//uiSortable(self.container);
			});
		}
		function requestTreeData(treeId, treeNode){
			if(treeNode){
				ctrl.getJson("org/tree/children?pk="+treeNode.id,function(data){
					ctrl.log(data.orgJson);
					var treeObj = $.fn.zTree.getZTreeObj(treeId);
					var child = $.parseJSON(data.orgJson);
					if(treeObj!=null){
						treeObj.removeChildNodes(treeNode);
						if(child.length == 0){
							treeObj.setting.data.keep.parent = false;
						}else{
							treeObj.setting.data.keep.parent = true;
							treeObj.addNodes(treeNode,child);
						}
					}else{
						
					}
				});
			}else{
				ajaxInitTree(1);
				
			}
		}
        function refresh(htmlStr){
        	var $html = $(htmlStr);
        	var $tbody = self.container.find("#orgTree_data_rows");
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
    
	    function renderPager($pager) {	
	    	if($pager!=undefined)
	    	if( $pager.find('#org_pager')!=undefined)
	    	ctrl.renderPager({
				"containerObj" : self.container,
				"pageObj" : $pager.find('#org_pager'),
				"callBack":self.query
			});	    	
		}
       return o;
	});
    
})();