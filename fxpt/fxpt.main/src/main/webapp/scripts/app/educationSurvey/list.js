(function(){
	"use strict";
	define(['jquery','ajax','validatejs',"dialog",'controller',"formToJosn","../etl/reportExecutor","download","ztreecore","ztreecheck","jqueryPager"],
	function($,ajax,validate,dialog,ctrl,formToJson,reportExecutor,download){
		 var self = undefined;
		 var validateRules = {
				rules : {
					name : {
						required : true,
						maxlength:50
					},
					code : {
						required : true,
						maxlength:20
					}
				},
				messages : {
					name : {
						required : '请输入指标名称',
						maxlength:'名称长度不能超过50个字符'
					},
					code : {
						required : '请输入指标代码',
						maxlength:'名称长度不能超过20个字符'
					}
				}
//				errorPlacement:function(error, element){
//					//element.parent().find('div.tooltip').remove();
//					//element.tooltip('destroy');
//					//element.tooltip({title:error.html(),trigger:''});
//					//element.tooltip('show');
//				}
			}; 
		var currNode,$zTree ;
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
               var url = ctrl.getCurMenu().attr("url");
	     	   ctrl.getHtml(url,function(html) {
	     	       ctrl.appendToView(self.container,html);
	     		   //$(".main-content").html(htmlStr);
	     	      bindEvent();
				  ztree();
				  ctrl.initUI(self.container);
				  reportEvent(function() {viewUI();});
				  temDownLoad();
				   //add();
	     	   });
	        },
	        save:function(url){
				ctrl.getHtml(url,function(htmlStr) {
					var $dialog = ctrl.modal("指标设置>新增",htmlStr,function() {
						var uu=url;
						var $form = $dialog.find('#orgEdit');
						$form.validate(validateRules);
						if($form.valid()){
							var org = $form.formToJson();
							self.commit(org,"POST");
							if($dialog.find('#cnt')[0].checked){
								return false;
							}else{
								return true;
							}
						}
						return false;
					},"保存");
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
				ctrl.remove(url,"确定要删除组织<b style='color:red;'>"+(removeName||"")+"</b>吗？","<b style='color:red;'>" + removeName + '</b>删除成功！',data,
					    function(data){
							//var obj      = $.fn.zTree.getZTreeObj("orgTree_ztree1");
							var treeNode = $zTree.getNodeByTId(currNode.tId);
							refreshSubList(treeNode);
							refreshTree();
					    }
			   );
	        },
	        orgOut:function(url){
		     	   ctrl.getHtml(url,function(htmlStr) {
		     		  dialog.modal({
							size : 'md',// sm md lg
							header : {
								show : true,
								txt : '操作结果'
							},
							footer : {
								show : true,
								buttons : [ {
									type : 'submit',
									txt : "下载此文件",
									sty : 'primary',
									callback : function() {
										//var path = document.getElementById("excelPath").valueOf();
										var url = window.app.rootPath +"downloadreport/downloadexcel/" + 0;
										//var url =window.app.rootPath+"/org/downLoad/"+"组织架构.xlsx";
											$.download(url);
											$(this).trigger('close');
									}
								}, {
									type : 'button',
									txt : "取消",
									sty : 'hidden',
									callback : function() {
										$(this).trigger('close');
									}
								} ]
							},
							body : htmlStr
						});
		     	   });
		        },
	        update:function(url){
	     	   ctrl.getHtml(url,function(htmlStr) {
	     		  var $dialog = ctrl.modal("指标设置>修改",htmlStr,function() {
	     			   var $form = $dialog.find('#orgEdit');
	     			   $form.validate(validateRules);
	     			   if($form.valid()){
							var org = $form.formToJson();
							self.commit(org,"PUT");
							return true;							
						}
						return false;
					},"保存");
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
					url : "academicSupervise",
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
			   var treeNode = $zTree.getSelectedNodes()[0];
			   refreshSubList(treeNode,page);       	  
           }
       };
		
					
		function clearInput(){
			$('#orgEdit input:text:lt(2)').val('');
		};
		
		function bindEvent(){
        	self.container.on("click","#addBtn",function() {
        		self.save("academicSupervise/view/"+currNode.parentId + "/-1");
        	}).on("click","#orgTree_datagrid a[trigger=remove]",function(){
        		var pk    = $(this).attr("pk");
        		var name  = $(this).parent().parent().parent().children(":eq(1)").text();
        		self.remove("academicSupervise", name,{pk:pk});
        	}).on("click","#orgTree_datagrid a[trigger=update]",function(){
        		var pk = $(this).attr("pk");
        		self.update("academicSupervise/view/"+currNode.parentId+"/"+pk);
        	}).on("click","#orgTree_datagrid a[trigger=orgOut]",function(){
        		var pk = $(this).attr("pk");
        		var type = $(this).attr("type");
        		if(type==undefined){
        			type=1;
        		}
        		self.orgOut("org/orgOut/"+type+"/"+pk);
        	}).on('click',"#orgTree_datagrid div.query-form a:eq(0)",function(){
        		self.query();
        	}).on('keyup',"#orgTree_datagrid div.query-form input[name=orgName]",function(e){
        		
        		if(e.keyCode == 13){
        			self.query();
        			e.stopPropagation();
        		}
        	});
        };  
        //组织下载
		function downLoadOrg(){
				var path = $(this).attr("objectId");			
				var url = "/org/downLoad/"+path;
				ctrl.getHtml(url,function(){
					$.download(url);
				});
		}
        //组织模板下载
		function temDownLoad(){
			self.container.on("click","#temDownLoad",function() {
				var url = window.app.rootPath + "download/org";
				$.download(url);
			});
		}
		
        //组织架构导入
        function reportEvent(callBack){
        	self.container.on("click","#report",function() {
        		ctrl.postJson("/org/orExist/","","",function(data){
        				if(data.num>0){
        					ctrl.confirm("提示","已存在组织信息，是否重新导入！",function(){
        						reportExecutor.UI({
        							importType : 0,
        							schemeType : 11,
        							examId : null,
        							title : "导入组织",
        							callBack : callBack
        						});
        					});
        				}else{
        					reportExecutor.UI({
    							importType : 1,
    							schemeType : 11,
    							examId : null,
    							title : "导入组织",
    							callBack : callBack
    						});
        				}
        			});
        		});
        }
        //组织导入
    	function viewUI() {
				reportEvent(function() {
					viewUI();
				});
    		}		
        /**
		 *  刷新树
		 */
		function refreshTree(){
			//var obj = $.fn.zTree.getZTreeObj("orgTree_ztree1");
			var treeNode = $zTree.getNodeByTId(currNode.tId);
			var treeParentNode = undefined;
			if(currNode.parentCparentChange)	
				treeParentNode = treeNode.getParentNode();			
			requestTreeData("orgTree_ztree1", treeParentNode||treeNode);
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
			if(treeNode && treeNode.type && treeNode.type == 3){
				if(!pager)
				   pager = ctrl.newPage(self.container.find('#orgTree_datagrid'));
				ctrl.getHtml("academicSupervise/subList/" + pager.curSize + "/" + pager.pageSize+"?pk="+pk+"&isParent=false&qname="+orgName,function(htmlStr) {
					refresh(htmlStr);
	     	    });				
			}else{
				ctrl.getHtml("academicSupervise/subList?pk="+pk+"&isParent="+isParent + "&qname="+orgName,function(htmlStr) {
					refresh(htmlStr);
	     	    });			
			}

		}
		/* 初始化树 */
		function ajaxInitTree(level){
			ctrl.getJson("academicSupervise/tree/list/"+level,function(data){
				ctrl.log(data.orgJson);
				var zNodes = $.parseJSON(data.orgJson);
				$zTree = $.fn.zTree.init(self.container.find("#orgTree_ztree1"), setting, zNodes);
			});
		}
		function requestTreeData(treeId, treeNode){
			if(treeNode){
				ctrl.getJson("academicSupervise/tree/children?pk="+treeNode.id,function(data){
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
	    	ctrl.renderPager({
				"containerObj" : self.container,
				"pageObj" : $pager.find('#org_pager'),
				"callBack":self.query
			});	    	
		}
       return o;
	});
    
})();