(function() {
	"use strict";
				define([ "jquery", "jqueryPager", "controller", "formToJosn", "dialog",
							"datepickerjs", "selectjs", "../etl/etlExecutor","radioztree" ], function($,
							$page, ctrl, formToJosn, dialog, datepickerjs, selectjs,
							etlExecutor,radioztree) {
				var self = undefined;
				var $container = undefined;
				var url = "studentBase/list/1/15?ui_all=true&xh=&qname&qschoolName=";
				function newUpdate(pk) {
					ctrl.getHtmlDialog(("studentBase/newUpdate/"+pk), "big-modal",
							function(html) {
								ctrl.modal("学生库设置>编辑", html, function() {
									var $form = $(html).find('#studentBaseform');
									var studentBaseGuid = $form.find("input[name=studentBaseGuid]").val();
									var studentBasexh = $form.find("input[name=studentBasexh]").val();
									var studentBaseName = $form.find("input[name=studentBaseName]").val();
									var studentBaseSex  = $form.find("select[name=studentBaseSex]").val();
									var studentBaseGrade = $form.find("input[name=studentBaseGrade]").val();
									var studentBaseSchoolCode = $form.find("input[name=ownerCode]").val();
									var school = {code:studentBaseSchoolCode};
									if (studentBaseName.length < 1) {
										ctrl.moment("学生名称不能为空","warning");
										return false;
									}else{
										var studentBase = {
												id:pk,
												guid:studentBaseGuid,
												name : studentBaseName,
												sex:studentBaseSex,
												grade:studentBaseGrade,
												school:school,
												xh:studentBasexh
											};
											ctrl.putJson("studentBase", studentBase,"学生修改成功!", function() {
											    self.query();
											});
											return true;
									}
									return false;
								}, "保存");
								createTree();
							});
				}
				
				//过滤 当菜单不是最后一级前面的单选框去掉
				function ajaxDataFilter(treeid,parentNode,nodes){
					for(var i=0;i<nodes.length;i++){
						var node = nodes[i];
						 var isParent = node.isParent;
	            		  if(isParent){
	                		node.nocheck=true;
	            		  }
					}
					return nodes;
				}
				
				
				function createTree() {
					ctrl.getJson("/education/tree", function(data) {
						var nodes = ajaxDataFilter("","",data.treeNodes);
						radioztree({
							ztreeId : "myTree",
							targetId : "ownerName",
							async : {
								enable : true,
								url : window.app.rootPath + "/education/tree",
								autoParam : [ "code", "type" ],
								dataFilter:ajaxDataFilter
							},
							onCheckCb : function(nodes) {
								var node = null;
								if (nodes.length > 0) {
									node = nodes[0];
								}
								if (!node || !node.code) {
									return;
								}
								$("#ownerCode").val(node.code);
								$("#ownerName").val(node.name);
							},
							initData : nodes
						});
					}, "", {
						code : "",
						type : 0
					});
				}
				
				
				function importExamStudent(){
					var page = getPager();
						ctrl.getHtml(("studentBase/examList/"+page.curSize+"/"+page.pageSize+"?hasExamStudent=true&studentBaseStatus=0"),function(html) {
							self.container = $(html);
			        	    ctrl.renderPager({
								"containerObj" : self.container,
								"pageObj" : self.container.find('#studentBase_pager'),
								"callBack":self.importQuery
							});	
			        	    ctrl.appendToView($container,self.container);
			        	    importExamStudents();
							returnMain();
						});
				}
				
				function returnMain(){
					var $obj = self.container;
					$obj.on('click','#returnSb',function(html) {
						self.render($container);
					});
				}
				
				function importExamStudents(){
					$("input[type=checkbox][name='allChecked']").click(function(){
						var list=$("input[name='examChecked']");
						var head=$("input[name='allChecked']");
						for(var i=0;i<list.length;i++){
							list[i].checked=head[0].checked;
						}
					});
					var $obj = self.container;
					$obj.on('click','#saveExams',function(html) {
						var examids = [];
						$("input[name='examChecked']:checked").each(function(){
							examids.push($(this).val());
						});
						if(examids.length<1){
							ctrl.moment("请至少选择一个要导入的考试","warning");
							return false;
						}
						var curSize = $("input[id='pageNum']").val();
						var pageSize =$("input[id='pagesize']").val();
						ctrl.postJson("studentBase/importExamStudents/"+curSize+"/"+pageSize, examids,
								"已更新学生库!", function() {
									self.importQuery();
								});
					});
				}

				function importStudent(callBack) {
					etlExecutor.UI({
						tableName : "kn_studentbase",
						schemeType : 5,
						examId:0,
						title : "导入学生信息",
						callBack:callBack
					});
				}
				
				function bindEvent() {
					var $obj = self.container;
					$obj.on('click','.form-side>a:eq(0)',function() {
								self.query();
					});
					
					$obj.on('click','#sb_examStudent',function() {
						importExamStudent();
					});
					
					$obj.on('click','#sb_download',function() {
						downloadTemplate();
					});
					
					$obj.on('click','#sb_import',function() {
						importStudent(self.render);
					});
					
					$obj.on('click', 'a[trigger]', function() {
						var trigger = $(this).attr('trigger');
						var pk = $(this).attr("pk");
						switch (trigger) {
						case 'update':
							newUpdate(pk);
							break;
						default:

						}
					});
				}

				function downloadTemplate(){
					var url = window.app.rootPath + "download/studentBase";
					$.download(url);
				}
				
				function renderPager(){
					ctrl.renderPager({
						"containerObj" : self.container,
						"pageObj" : self.container.find('#studentBase_pager'),
						"callBack":self.query
					});	
				}
				
				function importRenderPager(){
					ctrl.renderPager({
						"containerObj" : self.container,
						"pageObj" : self.container.find('#studentBase_pager'),
						"callBack":self.importQuery
					});	
				}
				function getPager(){
			    	var pager = ctrl.newPage(self.container);
		    		return pager;
			    };
				function formToQueryPath(){
					var $inputs = self.container.find('div.query-form input');
					var queryPath = "";
					$inputs.each(function(i,n){
						queryPath += "&" + n.name + '='+n.value;
					});
					return queryPath;
					
				}
				var o = self = {
					container:undefined,
					render : function(container) {
						$container = container;
						self.log("get url " + url);
						self.getHtml(url,function(html) {
							self.container = $(html);
			        	    ctrl.appendToView(container,self.container);
							bindEvent();
							renderPager();
							ctrl.initUI(self.container);
						});
					},
				   query:function(page){
						if(!page){
			         		   page = getPager();
			         	   }
						   ctrl.getHtml("studentBase/list/"+page.curSize+"/"+page.pageSize+"?"+formToQueryPath(),function(html){
							   ctrl.refreshDataGrid('studentBase_data_rows',self.container,$(html),renderPager);
			        	   });
				   },
				   importQuery:function(page){
						if(!page){
			         		   page = getPager();
			         	   }
						   ctrl.getHtml("studentBase/examList/"+page.curSize+"/"+page.pageSize+"?hasExamStudent=true&studentBaseStatus=0"+formToQueryPath(),function(html){
							   ctrl.refreshDataGrid('studentBase_data_rows',self.container,$(html),importRenderPager);
			        	   });
				   },
					
				};
				return $.extend(o, ctrl);
			});

})();