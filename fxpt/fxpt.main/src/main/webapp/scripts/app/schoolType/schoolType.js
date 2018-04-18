(function() {
	"use strict";
	var model = [ "jquery", "jqueryPager", "controller", "formToJosn",
			"dialog", "datepickerjs", "selectjs","download","../etl/etlExecutor",
			"../etl/MultiEtlExecutor", "../logs/logs" ];
	define(model, function($, $page, ctrl, formToJosn, dialog, datepickerjs,
			selectjs,download, etlExecutor, multiExecutor, logs) {
		//定以一个容器
		var $container = undefined;
		//查询列表的方法
		function list(page) {
			if (!page) {
				page = ctrl.newPage();
			}
			var url = "/schoolType/list/" + page.curSize + "/" + page.pageSize+"?ui_all=true";
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				//ctrl.initUI($htmlObj);
				//schoolTypeSelector($htmlObj);
				//dlexcel($htmlObj);
				ctrl.appendToView($container, $htmlObj);
				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $htmlObj.find("#pager"),
//					"callBack" : search
				});

			}, "", page.data);
		}
		
		function schoolTypeSelector($htmlObj) {
			$htmlObj.find('div.query-form a.btn:eq(0)').click(function() {
				var page = ctrl.newPage();
				search(page);
			});
		}
		
//		function search(page) {
//			console.log("search schooltype >>>>>>>>>>>");
//			var data = $container.find("div.query-form form").formToJson();
//			page["data"] = data;
//			var url = "/schoolType/list/" + page.curSize + "/" + page.pageSize;
//			ctrl.getHtml(url, function(html) {
//				var $htmlObj = $(html);
//				$container.find("#examDlList table tbody").html($htmlObj);
//				ctrl.renderPager({
//					"containerObj" : $htmlObj,
//					"pageObj" : $container.find("#examDlList #pager"),
//					"callBack" : search
//				});
//
//			}, "", page.data);
//		}
		
		function newAdd() {
			ctrl.getHtmlDialog(("schoolType/newAdd"), "big-modal",
					function(html) {
						ctrl.modal("学校类型设置>新增", html, function() {
							var $form = $(html).find('#schoolTypeform');
							var schooleTypeName = $form.find("input[name=schooleTypeName]").val();
							var ordernum = $form.find("input[name=ordernum]").val();
							if (schooleTypeName.length < 1) {
								ctrl.moment("请输入学校类别名称","warning");
								return false;
							}
							if(ordernum.length<1){
								ctrl.moment("请输入序号","warning");
								return false;
							}
							else{
								var schoolType = {
										name : schooleTypeName,
										ordernum:ordernum
								};
								ctrl.postJson("schoolType", schoolType,
										"新增学校类别成功!", function() {
//											self.render();
									var schoolTypeName = $container.find('input[name="schoolTypeName"]').val();
									findSchoolType(schoolTypeName);
										});
								return true;
							}
							return false;
						}, "保存");
					});
		}
		
		function deletes(pk) {
			ctrl.remove("schoolType", "确定要删除吗？", "删除成功", {
				id : pk
			}, function() {
				var schoolTypeName = self.container.find('input[name="schoolTypeName"]').val();
				findSchoolType(schoolTypeName);
			});
		}
		
		function newUpdate(pk) {
			ctrl.getHtmlDialog(("schoolType/newUpdate/"+pk), "big-modal",
					function(html) {
						ctrl.modal("学校类别设置>编辑", html, function() {
							var $form = $(html).find('#schoolTypeform');
							var schoolTypeName = $form.find("input[name=schoolTypeName]").val();
							var ordernum = $form.find("input[name=ordernum]").val();
							if (schoolTypeName.length<1) {
								ctrl.moment("学校类别名称不能为空","warning");
								return false;
							}
							if(ordernum.length<1){
								ctrl.moment("序号不能为空","warning");
								return false;
							}
							else{
								var schoolType = {
										id:pk,
										name : schoolTypeName,
										ordernum:ordernum
									};
									ctrl.putJson("schoolType", schoolType,
											"学校类型修改成功!", function() {
												var schoolTypeName = $container.find('input[name="schoolTypeName"]').val();
												findSchoolType(schoolTypeName);
											});
									return true;
							}
							return false;
						}, "保存");
					});
		}
		
		function findSchoolType(schoolTypeName) {
			var page = ctrl.newPage();
			var url = "/schoolType/list/" + page.curSize + "/" + page.pageSize+"?schoolTypeName="+schoolTypeName;
			ctrl.getHtml(url,function(html){
				ctrl.refreshDataGrid('schoolType_data_rows',self.container,$(html));
     	   });
		}

		function bindEvent() {
			var $obj = $container;
			self.container = $container;
			$obj.on('click',"#schoolType_datagrid div.query-form a:eq(0)",function(){
				var schoolTypeName = $obj.find('input[name="schoolTypeName"]').val();
				findSchoolType(schoolTypeName);
			});
			$obj.on('click',"#schoolType_datagrid div.query-form a:eq(1)",function(){
				$obj.find('input[name="schoolTypeName"]').val("");
        	});
			$obj.on('click',"#schoolType_create",function(){
				newAdd();
        	});
			$obj.on('click', 'a[trigger]', function() {
				var trigger = $(this).attr('trigger');
				var pk = $(this).attr("pk");
				switch (trigger) {
				case 'remove':
					deletes(pk);
					break;
				case 'update':
					newUpdate(pk);
					break;
				default:

				}
			});
		}
		//ie下严格模式报错，分开定义 默认(var o = self = {...})
	var	self = {
			render : function(container) {
				$container = $(container);
				list();
				bindEvent();
			}
		};
		var o = self;
		return $.extend(o, ctrl);
	});
})();