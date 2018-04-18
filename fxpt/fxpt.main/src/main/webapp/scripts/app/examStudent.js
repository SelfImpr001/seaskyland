(function() {
	"use strict";

	define([ 'ajax', 'fromToJosn', "./etl/etlExecutor",'validatejs','dialog' ], function(ajax, b,
			etlExecutor,c,dialog) {

		var g_pageSize = window.app["pageSize"];
		function list(page) {
			if (!page) {
				page = {};
				page["pagesize"] = g_pageSize;
				page["curpage"] = 1;
			}

			var examId = $("#examId").val();
			ajax({
				url : "/examStudent/list/" + examId,
				async : true,
				type : "POST",
				dataType : "html",
				data : JSON.stringify(page),
				callback : function(html) {
					$(".main-content").html(html);
					importExamStudent();
					search();
					renderPager();
					deleteExamStudent();
				}
			});
		}

		function renderPager() {
			var _pageNum = parseInt($("#pageNum").val());
			var _pageCount = parseInt($("#pageCount").val());
			$("#pager").pager({
				pageNum : _pageNum,
				pageCount : _pageCount,
				click : function(pageNum, pageCount) {
					var page = {};
					page["pagesize"] = g_pageSize;
					page["curpage"] = pageNum;
					skipPage(page);
				}
			});
		}

		function search() {
			$("#search").click(function() {
				var page = {};
				page["pagesize"] = g_pageSize;
				page["curpage"] = 1;
				skipPage(page);
			});
		}

		function skipPage(page) {

			var schoolName = $("#schoolName").val();
			var param = {};
			param["schoolName"] = schoolName;
			page["parameter"] = param;
			list(page);
		}

		function importExamStudent() {
			$("#importExamStudent").click(function() {
				etlExecutor.newProcessor({
					tableName : "tb_examstudent",
					contentObj : $(".page-content"),
					examId : $("#examId").val(),
					title : "导入考试学生信息"
				});
			});
		}

		function deleteExamStudent() {
			$("#deleteExamStudent").click(function() {
				var examId = $("#examId").val();
				ajax({
					url : "/examStudent/delete/" + examId,
					async : true,
					callback : function(html) {
						dialog.fadedialog({
							icon_info : "success",
							tip_txt : "删除成功!"
						});
						list();
					}
				});
			});
		}

		var o = {
			list : function() {
				list();
			}
		};

		return o;

	});

})();