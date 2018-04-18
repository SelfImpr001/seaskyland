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
			var url = "/downloadreport/list/" + page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				ctrl.initUI($htmlObj);
				examSelector($htmlObj);
				dlexcel($htmlObj);
				resetting($htmlObj);
				ctrl.appendToView($container, $htmlObj);
				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $htmlObj.find("#pager"),
					"callBack" : search
				});

			}, "", page.data);
		}

		function examSelector($htmlObj) {
			$htmlObj.find('div.query-form a.btn:eq(0)').click(function() {
				var page = ctrl.newPage();
				search(page);
			});
		}

		function search(page) {
			var data = $container.find("div.query-form form").formToJson();
			page["data"] = data;
			var url = "/downloadreport/list/" + page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				$container.find("#examDlList table tbody").html($htmlObj);
				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $container.find("#examDlList #pager"),
					"callBack" : search
				});

			}, "", page.data);
		}
		
		
		function dlexcel($htmlObj){
			console.log("初始化点击事件 >>>>>>>>>>>");
			$htmlObj.find(".examdlexcel").click(function() {
				var examId = $(this).attr("objectId");
				console.log("点击开始 >>>>>>>>>>>examId");
				var url = window.app.rootPath +"downloadreport/downloadexcel/" + examId;
				$.download(url);
			});
		}
		
		function resetting($htmlObj){
			console.log("初始化点击事件 >>>>>>>>>>>");
			$htmlObj.find(".examresetting").click(function() {
				var examId = $(this).attr("objectId");			
				var url = "/downloadreport/dele/" +  examId;
				ctrl.getHtml(url,function(htmlStr){
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
								txt : "确认",
								sty : 'primary',
								callback : function() {
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
			});
		}

		
		var o = {
			render : function(container) {
				$container = $(container);
				console.log("liuyu test downloadreport >>>>>>>>>>>");
				list();
			}
		};
		return $.extend(o, ctrl);
	});
})();