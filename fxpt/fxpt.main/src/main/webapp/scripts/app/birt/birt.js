(function(){
	"birt strict";
	define(['jquery','ajax',"app/birt/mergeBirt",'formToJosn',"dialog","controller","app/birt/birtOrg", "ajaxfileupload","download",
	        'validatejs',"ztreecore","ztreecheck","ztreeexedit","jqueryPager"],function($,ajax,mergeBirt,form,dialog,ctrl,birtOrg,ajaxfileupload,download){
	    var self = undefined;
		var $dialog = undefined;
		var currNode,$zTree ;
		jQuery.validator.addMethod("cellPhone",function(v,e){
			var cellPhone = /^(130|131|132|133|134|135|136|137|138|139|150|151|152|153|154|155|156|157|158|159|180|181|182|184|187|188|189|)\d{8}$/;
			return cellPhone.test(v) || this.optional(e);
		},"请输入正确的手机号码");
		
	    var validateRules = {
			rules : {
				name : {
					required : true
				},
				password : {
					required : true,
					minlength : 6
				},
				"userInfo.cellphone":{
					cellPhone:""
				}
			},
			messages : {
				name : {
					required : '请输入文件名'
				},
				password : {
					required : '请输入密码',
					minlength : '密码最小长度为6位'
				}
			}
		}; 
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
	    	$obj.find('#user_datagrid >div.query-form a.btn:eq(0)').on('click',function(){	    		
	    		self.query(getPager());
	    	});
	    	
	    	$obj.on('click','#user_datagrid tbody div.btn-group a',function(){
	    		eventTrigge($(this),container);
	    	}).on('click','h1 button',function(){
	    		eventTrigge($(this),container);
	    	});
	    	self.container.on("click","#addBtn",function() {
        		self.save("orgBirt/view/"+currNode.parentId + "/-1/add");
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
        		
        		
        	}).on("click","#orgTree_datagrid a[trigger=nextOrg]",function(){  //根据当前组织查询下一级组织信息
        		var pk = $(this).attr("pk");
        		var count=$(this).attr("count");
        		if(count>0){
        			ctrl.getHtml("org/subList?pk="+pk+"&isParent="+true + "&qname=-1",function(htmlStr) {
    					refresh(htmlStr);
        			});
        		}
        		
//        		ctrl.getHtml("org/nextOrglist/"+pk,function(html){
//        			ctrl.appendToView(self.container,html);  
        			
        	}).on('click',"#orgTree_datagrid div.query-form a:eq(0)",function(){
        		self.query();
        	}).on('keyup',"#orgTree_datagrid div.query-form input[name=orgName]",function(e){
        		
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
	    
	    function eventTrigge($trigger,container){
	    	var trigger = $trigger.attr('trigger');
    		var pk = $trigger.attr("pk");
    		var wordName = $trigger.parent().parent().parent().children(":eq(5)").text();
    		switch(trigger){
    		case 'update' :   			
    			self.update(pk);
    			break;
    		case 'remove':
    			var userName = $trigger.parent().parent().parent().children(":eq(1)").text();
    			self.remove(pk,userName);
    			break;
    		case 'permission':
    			self.generateWord(pk,self,container);
    			break;
    		case 'create':
    			if(currNode.parentId>0){
    				self.create(container);
    			}else{
    				ctrl.alert("请选择左侧菜单");
    			}
//    				
    			
    			break;
    		case 'batchGrant':
    			self.batchGrant(self,container);
     			break;
    		case 'download':
    			if(wordName!="-"&&wordName!=""){
    				var url = window.app.rootPath + "birt/diwnWord/"+pk;
    				$.download(url);
    			}else{
    				ctrl.alert("请生成word后下载");
    			}
    			break;
    	
    		default:
    			return;
    		}
	    };
	   
	    function moreDelete(){
	    	//选中的个数
	    	var checkedNum=$("input[name='myChecked']:checked").length;
	    	var num = $("input[name='myChecked']");
	    	var pks="";
	    	if(checkedNum>0){
	    		//获取选中的所有值
	    		for(var i=0;i<$("input[name='myChecked']").length;i++){
	    			if(num[i].checked){
	    				//英文逗号隔开的pk串
	    				pks+=num[i].value+",";
	    			}
	    		}
	    		ctrl.confirm("操作","您将批量删除用户，是否继续？",function(){
	    			ctrl.postJson("user/moreDelete/"+pks,"","删除成功！", function(data){
		    			  self.query(getPager());
		    		  });
				},"确定",function(){
					return false;
				},"取消");
	    	}else{
	    		ctrl.moment("请选择要删除的用户！","warning");
	    	}
	    }
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
	    		if( $pager.find('#orgBirt_pager')!=undefined)
	    			ctrl.renderPager({
	    				"containerObj" : self.container,
	    				"pageObj" : $pager.find('#orgBirt_pager'),
	    				"callBack":self.query
	    			});	    	
	    	}else{
	    		ctrl.renderPager({
					"containerObj" : self.container,
					"pageObj" : self.container.find('#user_pager'),
					"callBack":self.query
				});	
	    	}
		}
	    
 	   function updateBelong(org,userPk){
		   ctrl.postJson("belong/batch/"+org,{"pk":-1,"user":{"pk":userPk}}); 		        		   
	   };
	   function validateFile(suffixName) {
			var opts = $dialog.data("opts");
			var effectiveFile = opts.effectiveFile;
			var isOk = false;
			$.each(effectiveFile, function() {
				if (this == suffixName.toLowerCase()) {
					isOk = true;
					return false;
				}
			});
			if(suffixName==undefined||suffixName==""||suffixName==null){
				ctrl.alert("请选择上传文件");
			}else
			if (!isOk) {
				ctrl.alert(opts.effectiveFileErrorMesg);
			}
			return isOk;
		}
	   function uploadFile() {
			$dialog.find("#uploadFile").click(function() {
				var suffixName = uploadFileSuffix();
				if (!validateFile(suffixName)) {
					return false;
				}
				var $overlay = ctrl.tips("正在上传文件，请不要关闭和刷新浏览器");
				var uploadSetting = getFileUploadSetting();
				uploadSetting.success = uploadSuccessFun($overlay);
				$.ajaxFileUpload(uploadSetting);
			});
		}
		function uploadFileSuffix() {
			var filePath = $("#file").val();
			var suffixName = filePath.substring(filePath.lastIndexOf("."),
					filePath.length);
			return suffixName;
		}

		function uploadSuccessFun($overlay) {
			return function(data) {
				$overlay.fadeOut(function() {
					$(this).remove();
				});
				$dialog.find("#directory").val(data.fileInfo.path)
				$dialog.find("#suffix").val(data.fileInfo.name)
			}
		}
	   function getFileUploadSetting() {
			var opts = $dialog.data("opts");
			var url = window.app.rootPath + "birt/upload/";
			return {
				url : url,
				secureuri : true,
				fileElementId : "file",
				dataType : "json",
				
				
			};
		}
		function isfrom(){
			
			var name  = $dialog.find("#name").val();
			var source = $dialog.find("#source").val();
			var directory = $dialog.find("#directory").val();
			var remark = $dialog.find("#remark").val();
			if(name==null||name==undefined||name==""){
				ctrl.moment("请输入文件名","warning");
				$dialog.find("input[name=name]").focus();
				return false;
			}
			else if(source==null||source==undefined||source==""){
				ctrl.moment("请输入文件来源","warning");
				$dialog.find("input[name=source]").focus();
				return false;
			}
			else
				if(directory==null||directory==undefined||directory==""){
					ctrl.moment("请上传文件","warning");
					$dialog.find("input[name=file]").focus();
					return false;
			}
			return true;
		}
	   function executor(container) {
			var opts = $dialog.data("opts");
			var webRetrieveResult = {};
			webRetrieveResult["pk"] = $dialog.find("#pk").val();
			var pk =  $dialog.find("#pk").val();
			webRetrieveResult["name"] = $dialog.find("#name").val();
			webRetrieveResult["source"] = $dialog.find("#source").val();
			webRetrieveResult["directory"] = $dialog.find("#directory").val();
			webRetrieveResult["remark"] = $dialog.find("#remark").val();
			webRetrieveResult["suffix"] = $dialog.find("#suffix").val();
			webRetrieveResult["orgBirt"] ={};
			webRetrieveResult = {
					pk:$dialog.find("#pk").val(),
					name:$dialog.find("#name").val(),
					source:$dialog.find("#source").val(),
					directory:$dialog.find("#directory").val(),
					remark:$dialog.find("#remark").val(),
					suffix:$dialog.find("#suffix").val(),
					orgBirt:{
						pk:$dialog.find("#orgBirt").val()
					}
					
			}
			var relust = false;
			if(isfrom()){
				if(pk==null||pk==""){
					var $overlay = ctrl.tips("正在保存脚本，请不要关闭和刷新浏览器");
					ajax({
						url : "/birt",
						async : false,
						type : "POST",
						dataType : "html",
						data : JSON.stringify(webRetrieveResult),
//					beforeSendMsg : {
//						backdrop : false,
//						tip_txt : '脚本新增成功',
//						icon_info : 'updata-ing',
//						untransparent : true
//					},
						callback : function(html) {
							$overlay.fadeOut(function() {
								$(this).remove();
							});
							relust =  true;
						}
					});
				}else{
					
					var $overlay = ctrl.tips("正在修改脚本，请不要关闭和刷新浏览器");
					ajax({
						url : "/birt",
						async : false,
						type : "put",
						dataType : "html",
						data : JSON.stringify(webRetrieveResult),
//					beforeSendMsg : {
//						backdrop : false,
//						tip_txt : '脚本新增成功',
//						icon_info : 'updata-ing',
//						untransparent : true
//					},
						callback : function(html) {
							$overlay.fadeOut(function() {
								$(this).remove();
							});
							relust =  true;
						}
					});
				}
				
				
			}
			return relust
			
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
			requestTreeData("user_ztree1", treeParentNode||treeNode);
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
				}/*,edit:{
					enable:true,
					showRemoveBtn:true,
					removeTitle:"remove",
					
					},
				view:{
					
				}*/
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
			
				if(!pager)
				   pager = ctrl.newPage(self.container.find('#orgTree_datagrid'));
				ctrl.getHtml("orgBirt/subList/" + pager.curSize + "/" + pager.pageSize+"?pk="+pk+"&isParent=false&qname="+orgName,function(htmlStr) {
					refreshs(htmlStr);
	     	    });				
			

		}
		/* 初始化树 */
		function ajaxInitTree(level){
			ctrl.getJson("orgBirt/tree/list/"+level,function(data){
				ctrl.log(data.orgJson);
				var zNodes = $.parseJSON(data.orgJson);
				$zTree = $.fn.zTree.init(self.container.find("#user_ztree1"), setting, zNodes);
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
        	
/**树相关*/
		function refresh(data){
			ctrl.refreshDataGrid('user_data_rows',self.container,$(data),renderPager);			
		};
		var page=ctrl.newPage()
		var _listUrl = "/birt/list/" + page.curSize + "/" + page.pageSize+"?ui_all=true";
		//var _listUrl = ctrl.getCurMenu().attr('url');
		var pages = ctrl.newPage();
	    var o = self = {
	       container:undefined,
	       setPage:function(index){
	    	   pages["curSize"] = index; 
	    	   _listUrl = "birt/list/"+index+"/15?ui_all=true"; 
	       },
	       save:function(url){
				ctrl.getHtml(url,function(htmlStr) {
					var $dialog = ctrl.modal("脚本组织>新增",htmlStr,function() {
						var $form = $dialog.find('#orgEdit');
						$form.validate(validateRules);
						var flg=false;
							
										var org = $form.formToJson();
//										if($("#schoolTypeId").size()){
//											org.schooltype ={id:$("#schoolTypeId").val()};							
//										}
//										if($("#schoolSegmentId").size()){
//											org.schoolSegment = {id:$("#schoolSegmentId").val()};
//										}
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
						refresh(data);
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
//        	   if(!page){
//        		   page = getPager();
//        	   }
//        	   var url = "birt/list/" + page.curSize + "/" + page.pageSize;
//        	   var q = self.container.find('#user_datagrid >div.query-form input:text').val();
//        	   if(q.length){
//        		   url += "?q="+q;
//        	   }
//        	   ctrl.getHtml(url,function(data){
//        		   refresh(data);
//        	   });
//        	   self.container.parent().attr("indexPage",page.curSize);
        	   var treeNode = $zTree.getSelectedNodes()[0];
			   refreshSubList(treeNode,page); 
           },
           create:function(container){
        	   this.view(-1,container);
           },
           view:function(pk,container){
        	   var opts = {
       				title : "",
       				examId : undefined,
       				importType : undefined,
       				schemeType : 1,
       				effectiveFile : [ ".rptdesign", ".xlsx", ".dbf",".text" ],
       				effectiveFileErrorMesg : "文件只能为rptdesign文件",
       				fileInfo : undefined,
       				fileContent : undefined,
       				validateResult : undefined,
       				callBack : undefined
       			};
        		$dialog=("opts", opts);
       			var url = "/birt/view/"+pk+"?org_id="+currNode.parentId;
       			ctrl.getHtml(url, function(html) {
       				var $htmlObj = $(html);
       				// showKgStrTable();
       				$dialog = ctrl.modal(opts.title, $htmlObj, function() {
       					var relust = executor($htmlObj);
       				 var treeNode = $zTree.getSelectedNodes()[0];
       				 refreshSubList(treeNode,getPager()); 
       					return relust;
       				}, "保存", "lg");
       				$dialog.data("opts", opts);
       				uploadFile();
//       				showKgStrTable();
       			
       			});       	   
           },
           update:function(pk){
        	   if(pk > 0)
        	     this.view(pk);     	   
           },
           remove:function(pk,userName){
        			 mes="确定要删除脚本【<b style='color:red;'>"+(userName||"")+"</b>】吗？";
	        	   ctrl.remove("birt",mes, "脚本删除成功！",{pk:pk},function(){
	        			self.query(getPager());
	        	   });
        	  
           },
     
           batchGrant:function(self,container){
        	   ctrl.getHtml(("birt/mergeReport"),function(html){
        		   ctrl.appendToView(container,$(html));
        		   mergeBirt.render(container,self)
        	   });
//        	   ctrl.getHtmlDialog(("birt/mergeReport"),"big-modal",function(dialog){
//        		   mergeBirt.render(dialog)
//        		   dialog.find('.selectpicker').selectpicker();
//        		   var $form = dialog.find('#userEditForm');
//        		   $form.on('click','button.btn-primary',function(){
//        			   if($form.valid()){
//        				  var webRetrieveResult={};
//        					webRetrieveResult["name"] = dialog.find("#name").val();
//        					webRetrieveResult["directory"] = dialog.find("#directory").val().toString();
//			        		   ctrl.postJson("birt/mergeReports",webRetrieveResult,"合并脚本成功!",function(user){
//			        			   self.render(container);
//			        			   $("#big-modal").modal('hide');
//			        		   });        		   
//			        	   
//															
//						}
//						return false;
//					}).validate(validateRules);
////        		    		   
//        	   },function(){
//        	   });
        	   
           } ,
           generateWord:function(pk,self,container){
        	 
        	   ctrl.getHtmlDialog(("birt/generateWords"),"big-modal",function(dialog){
        		   dialog.find('.selectpicker').selectpicker();
        		   var $form = dialog.find('#userEditForm');
        		   $form.on('click','button.btn-primary',function(){
        			   if($form.valid()){
        				  var soure = dialog.find("#source").val();
			        		   ctrl.putJson("birt/generateWord/",{"pk":pk,"source":soure},"生成腳本成功！可以下載!",function(user){
			        			   self.render(container);
			        			   $("#big-modal").modal('hide');
			        		   });    
															
						}
						return false;
					}).validate(validateRules);
//        		    		   
        	   },function(){
        	   });
        	   
           
        	
           }
       };
       return o;
	});
    
})();