(function() {
	"use strict";
	define([ 'ajax', 'fromToJosn', './combinationSubject','validatejs','dialog' ], function(ajax, a,
			combinationSubject,b,dialog) {

		function view() {
			var examId = $("#examId").val();
			ajax({
				url : "/parameter/home/" + examId,
				async : true,
				dataType : "html",
				callback : function(htmlStr) {
					$(".main-content").html(htmlStr);
					saveParameterOk();
					combinationSubject.list();
				}
			});
		}

		function saveParameterOk() {
			$("#saveParameterOk").click(
					function() {
						var examId = $("#examId").val();
						var exam = {
							"id" : examId
						};

						var exmaParameters = new Array();
						$(".examparameter").each(
								function() {
									var exmaParameter = {};
									exmaParameter["exam"] = exam;
									exmaParameter["id"] = $(this).attr(
											"parameterId");
									exmaParameter["parameterName"] = $(this)
											.attr("parameterName");
									exmaParameter["parameterValue"] = $(this)
											.val();
									exmaParameter["parameterAsName"] = $(this)
											.attr("parameterAsName");
									exmaParameters.push(exmaParameter);
								});
						ajax({
							url : "/parameter/udpate",
							async : true,
							type : "POST",
							data : JSON.stringify(exmaParameters),
							callback : function(data) {
								dialog.fadedialog({
									icon_info : "success",
									tip_txt : "保存成功!"
								});
							}
						});
					});
		}

		var o = {
			view : function() {
				view();
			}
		};

		return o;

	});

})();