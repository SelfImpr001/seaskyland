(function(){
	"birt strict";
	define(['jquery','ajax','formToJosn',"dialog","controller","app/birt/birtOrg", "ajaxfileupload","download",
	        'validatejs',"ztreecore","ztreecheck","jqueryPager"],function($,ajax,form,dialog,ctrl,birtOrg,ajaxfileupload,download){
	    var self = undefined;
		var $dialog = undefined;
		var currNode,$zTree ;
	    var passwordRules = {
			rules : {
				password : {
					required : true,
					minlength : 6
				}
			},
			messages : {
				password : {
					required : '请输入密码',
					minlength : '密码最小长度为6位'
				}
			}			
	    };
	    
	    function getPager(){
	    	var pager = ctrl.newPage(self.container);
    		return pager;
	    };
	    
	    function bindEvent(container){
	    	var $obj = self.container;
	    	ctrl.initUI($obj); 
	    	self.container.on("click","#addBtn",function() {
        		self.save("orgBirt/view/-1");
        	}).on("click","#birtorg_datagrid a[trigger=remove]",function(){
        		var pk    = $(this).attr("pk");
        		var name  = $(this).parent().parent().parent().children(":eq(1)").text();
        		self.remove("orgBirt", name,{pk:pk});
        	}).on("click","#birtorg_datagrid a[trigger=update]",function(){
        		var pk = $(this).attr("pk");
        		self.update("orgBirt/view/"+pk);
        	}).on("click","#birtorg_datagrid a[trigger=orgOut]",function(){
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
        		
        		
        	}).on("click","#birtorg_datagrid a[trigger=nextOrg]",function(){  //根据当前组织查询下一级组织信息
        		var pk = $(this).attr("pk");
        		var count=$(this).attr("count");
        		
        		if(count>0){
        			ctrl.getHtml("org/subList?pk="+pk+"&isParent="+true + "&qname=-1",function(htmlStr) {
    					refresh(htmlStr);
        			});
        		}
        		
//        		ctrl.getHtml("org/nextOrglist/"+pk,function(html){
//        			ctrl.appendToView(self.container,html);  
        			
        	}).on('click',"#birtorg_datagrid div.query-form a:eq(0)",function(){
        		self.query();
        	}).on('keyup',"#birtorg_datagrid div.query-form input[name=orgName]",function(e){
        		
        		if(e.keyCode == 13){
        			self.query();
        			e.stopPropagation();
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
	    
	   
	    //用户列表全选
	    function selectCheckedAll(container){
	    	container.on('click','input[name="allChecked"]',function(){
	    		var examLists=document.getElementsByName("myChecked");
	    		//判断点击是 全选or全不选
	    		if(document.getElementsByName("allChecked")[0].checked){
	    			//全选
	    			for(var i=0;i<examLists.length;i++){
	    				examLists[i].checked=1;
	    			}
	    		}else{
	    			//全不选
	    			for(var i=0;i<examLists.length;i++){
	    				examLists[i].checked=0;
	    			}
	    		}
	    	});
	    }
	    //复选框选中时判断是否全选
	    function selectCheckedOne(container){
	    	container.on('click','input[name="myChecked"]',function(){
	    		var num = $("input[name='myChecked']").length;
	    		var checkedNum=$("input[name='myChecked']:checked").length;
	    		if(checkedNum==num){
	    			$("input[name='allChecked']")[0].checked=true;
	    		}else{
	    			$("input[name='allChecked']")[0].checked=false;
	    		}
	    	});
	    }
	    
	    //指定考试全选
	    function selectAll(container){
	    	container.on('click','input#checkAll',function(){
	    		var examLists=document.getElementsByName("examLists");
	    		//判断点击是 全选or全不选
	    		if(document.getElementById("checkAll").checked){
	    			//全选
	    			for(var i=0;i<examLists.length;i++){
	    				examLists[i].checked=1;
	    			}
	    		}else{
	    			//全不选
	    			for(var i=0;i<examLists.length;i++){
	    				examLists[i].checked=0;
	    			}
	    		}
	    	});
	    }
	    //单选（是否全选 选中）
	    function checkBtn(container){
	    	container.on('click','input[name="examLists"]',function(){
	    		
	    		//var checkedNum=$("input[name='examLists']checked").length;
	    		var checkedNoNum=document.getElementsByName("examLists").length;
	    		var checkedNum=document.getElementsByName("examLists");
	    		var a=0;
    			for(var i=0;i<checkedNum.length;i++){
    				if(checkedNum[i].checked){
    					a++;
    				}else{
    					document.getElementById("checkAll").checked=0;
    					break;
    				}
    			}
    			if(a==checkedNoNum){
    				document.getElementById("checkAll").checked=1;
    			}
	    	});
	    }
	    
	   
		
	    
	
	    function renderPager($pager) {	
	    	if($pager!=undefined){
	    		if( $pager.find('#birtorg_pager')!=undefined)
	    			ctrl.renderPager({
	    				"containerObj" : self.container,
	    				"pageObj" : $pager.find('#birtorg_pager'),
	    				"callBack":self.query
	    			});	    	
	    	}else{
	    		ctrl.renderPager({
					"containerObj" : self.container,
					"pageObj" : self.container.find('#birtorg_pager'),
					"callBack":self.query
				});	
	    	}
		}
	    

	   
	   /**
		 *  刷新树
		 */
		function refreshTree(){
			//var obj = $.fn.zTree.getZTreeObj("birtorg_ztree1");
			var treeNode = $zTree.getNodeByTId(currNode.tId);
			var treeParentNode = undefined;
			if(currNode.parentCparentChange)	
				treeParentNode = treeNode.getParentNode();	
			requestTreeData("birtorg_ztree1", treeParentNode||treeNode);
		}
		var setting ;
		function ztree(){
			var $oNewTree = self.container.find("#birtorg_ztree1");
			$oNewTree.appendTo(self.container.find("#birtorg_ztreediv"));
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
			
			var orgName = self.container.find('#birtorg_datagrid div.query-form input[name=orgName]').val();
			
				if(!pager)
				   pager = ctrl.newPage(self.container.find('#birtorg_datagrid'));
				ctrl.getHtml("orgBirt/subList/org/" + pager.curSize + "/" + pager.pageSize+"?pk="+pk+"&isParent=false&qname="+orgName,function(htmlStr) {
					refreshs(htmlStr);
	     	    });				
			

		}
		/* 初始化树 */
		function ajaxInitTree(level){
			ctrl.getJson("orgBirt/tree/list/"+level,function(data){
				ctrl.log(data.orgJson);
				var zNodes = $.parseJSON(data.orgJson);
				$zTree = $.fn.zTree.init(self.container.find("#birtorg_ztree1"), setting, zNodes);
			});
		}
		function requestTreeData(treeId, treeNode){
			if(treeNode){
				ctrl.getJson("orgBirt/tree/children?pk="+treeNode.id,function(data){
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
        	var $tbody = self.container.find("#birtorg_data_rows");
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
        	
/**树相关*/
		function refresh(data){
			ctrl.refreshDataGrid('birtorg_data_rows',self.container,$(data),renderPager);			
		};
		var page=ctrl.newPage()
		var _listUrl = "/orgBirt/list/" + page.curSize + "/" + page.pageSize+"?ui_all=true";
		//var _listUrl = ctrl.getCurMenu().attr('url');
		var pages = ctrl.newPage();
	    var o = self = {
	       container:undefined,
	       setPage:function(index){
	    	   pages["curSize"] = index; 
	    	   _listUrl = "/orgBirt/list/" + page.curSize + "/" + page.pageSize+"?ui_all=true"; 
	       },
	       save:function(url){
				ctrl.getHtml(url,function(htmlStr) {
					var $dialog = ctrl.modal("脚本组织>新增",htmlStr,function() {
						var $form = $dialog.find('#orgEdit');
						var flg=false;
						var org = $form.formToJson();
						self.commit(org,"POST");
						if($dialog.find('#cnt')[0].checked){
							return flg;
						}else{
								flg=true;
								return flg;
						}
									
						return flg;
						
					},"保存");
					var $newField = $dialog.find('#orgEdit div.form-group:eq(0)').clone();
					$newField.find('label').text('').next().remove();
					$('<div class="checkbox" style="float:left;margin-left: 15px"><label><input type="checkbox" id="cnt">连续新增</label></div>').insertAfter($newField.find('label'));
					$newField.css({'padding-bottom':0,'margin-bottom':0});
					//$newField.find('input').removeClass('form-control').text('连续新增')[0].type='checkbox';
					$dialog.find('#orgEdit>div').append($newField);
				});
	        },
	        commit:function(org,method){
	        	ctrl.log(JSON.stringify(org) + "  " + method);
	        	var successMsg = "保存成功!";
				if(method == "put") successMsg = "修改成功!";
	    	    ajax({
					url : "orgBirt",
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
						refreshs(data);
						refreshTree();
						clearInput();
					}
				 });
	        },
           render:function(container){
        	   currNode = {
      					"id":-1,
      					"parentChange":false,
      					"parentId": -1,
      					"isParent":false
      			   };
        	   ctrl.getHtml(_listUrl,function(html){
        		   
        		   self.container = $(html);
        		   ctrl.appendToView(container,self.container);
				   bindEvent(container);
				   renderPager();
				   selectCheckedAll(self.container);
				   selectCheckedOne(self.container);
				   ztree();
					  
        	   });
        	   
           },
           query:function(page){
        	   if(!page){
        		   page = getPager();
        	   }
        	   var url = "orgBirt/list/" + page.curSize + "/" + page.pageSize;
        	
        	   ctrl.getHtml(url,function(data){
        		   refresh(data);
        	   });
        	   self.container.parent().attr("indexPage",page.curSize);
           },
           update:function(url){
				ctrl.getHtml(url,function(htmlStr) {
					var $dialog = ctrl.modal("脚本组织>修改",htmlStr,function() {
						var $form = $dialog.find('#orgEdit');
						var name = $form.find("#name").val()
						if(name==""||name==undefined){
							ctrl.alert("请输入组织名称");
				    		return false;
						}
						var flg=false;
							
						var org = $form.formToJson();
							self.commit(org,"put");
						if($dialog.find('#cnt')[0].checked){
								return flg;
						}else{
							flg=true;
							return flg;
						}
												
						return flg;
						
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
	     	    ctrl.postJson("orgBirt/deleteValidate/"+data.pk,"","", function(data1){
	     		  if(data1.flg){
	     			 if(data1.mes==""){
			   				ctrl.remove(url,"确定要删除组织<b style='color:red;'>"+(removeName||"")+"</b>吗？","<b style='color:red;'>" + removeName + '</b>删除成功！',data,
								    function(data){
										//var obj      = $.fn.zTree.getZTreeObj("orgTree_ztree1");
										var treeNode = $zTree.getNodeByTId(currNode.tId);
										refreshSubList(treeNode);
										refreshTree();
								    }
						   );
		     		   }else{
		     			  ctrl.confirm("提示信息","<b style='color:red;'>"+(removeName||"")+"</b>"+data1.mes+"不允许删除！",function(){
		     				  
		     			  });
		     		   }
	     		  }else{
	     			 ctrl.confirm("提示信息",data1.mes,function(){
	     				  
	     			  });
	     		  }
	     	    	
	     	    });
	        },
     
      
       };
       return o;
	});
    
})();