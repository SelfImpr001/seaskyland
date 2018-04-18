(function() {
	"use strict";

	define([ 'ajax', 'fromToJosn', "./etl/etlExecutor",'validatejs' ], function(ajax, a,
			etlExecutor,b) {

		function list() {
			var testPaperId = $("#testPaperId").val();
			if (!testPaperId) {
				testPaperId = -1;
			}

			ajax({
				url : "/item/list/" + testPaperId,
				async : true,
				dataType : "html",
				callback : function(html) {
					$(".row").html(html);
					$('.selectpicker').selectpicker();
				}
			});
		}

		function deletes() {
			$("#deleteItem").click(function() {
				var testPaperId = $("#testPaperId").val();
				ajax({
					url : "/item/delete/" + testPaperId,
					async : true,
					callback : function(data) {
						list();
					}
				});
			});
		}

		function importItem() {
			$("#importItem").click(function() {
				var testPaperId = $("#testPaperId").val();
				var testPaperName = $("#testPaperId").find("option[value='"+testPaperId+"']").text();
			
				etlExecutor.newProcessor({
					tableName : "tb_item",
					contentObj : $(".page-content"),
					examId : $("#examId").val(),
					testPaperId : testPaperId,
					title : "导入【"+testPaperName+"】试题信息"
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
				url : "/item/home/" + examId,
				async : true,
				dataType : "html",
				callback : function(html) {
					$(".main-content").html(html);
					changeTestPaper();
					list();
					deletes();
					importItem();
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