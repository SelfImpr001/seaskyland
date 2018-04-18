(function() {
	"use strict";

	define([ 'ajax', 'fromToJosn', "./etl/etlExecutor" ], function(ajax, b,
			etlExecutor) {
		var g_pageSize = window.app["pageSize"];
		function list(page) {
			var testPaperId = $("#testPaperId").val();
			if (!testPaperId) {
				testPaperId = -1;
			}

			if (!page) {
				page = {};
				page["pagesize"] = g_pageSize;
				page["curpage"] = 1;
			}

			ajax({
				url : "/cj/list/" + testPaperId,
				async : true,
				dataType : "html",
				type : "POST",
				data : JSON.stringify(page),
				callback : function(html) {
					$(".row").html(html);
					$('.selectpicker').selectpicker();
					renderPager();
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
					list(page);
				}
			});
		}

		function importTestPaperCj() {
			$("#importCj").click(function() {
				var testPaperId = $("#testPaperId").val();
				var testPaperName = $("#testPaperId").find("option[value='"+testPaperId+"']").text();
				etlExecutor.newProcessor({
					tableName : "tb_cj",
					contentObj : $(".page-content"),
					examId : $("#examId").val(),
					testPaperId : testPaperId,
					title : "导入【"+testPaperName+"】成绩"
				});
			});
		}

		function deletes() {
			$("#deleteCj").click(function() {
				var testPaperId = $("#testPaperId").val();
				ajax({
					url : "/cj/delete/" + testPaperId,
					async : true,
					callback : function(data) {
						list();
					}
				});
			});
		}
		
		function changeTestPaper() {
			$("#testPaperId").change(function() {
				list();
			});
		}
		function home() {
			var examId = $("#examId").val();
			ajax({
				url : "/cj/home/" + examId,
				async : true,
				dataType : "html",
				callback : function(html) {
					$(".main-content").html(html);
					changeTestPaper();
					list();
					deletes();
					importTestPaperCj();
				}
			});
		}

		var o = {
			list : function() {
				home();
			}
		};

		return o;

	});

})();