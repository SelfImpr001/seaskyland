(function() {
	"use strict";
	var model = [ "jquery", "jqueryPager", "controller", "formToJosn", "editTable" ];
	define(model, function($, $page, ctrl, formToJosn, editTable) {
		var $container = undefined;
		function list(page) {
			if (!page) {
				page = ctrl.newPage();
			}
			var url = "/param/list/exam/" + page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				ctrl.initUI($htmlObj);
				examSelector($htmlObj);
				paramSettingUI($htmlObj);

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
			var url = "/param/list/exam/" + page.curSize + "/" + page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				$container.find("#examList table tbody").html($htmlObj);
				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $container.find("#examList #pager"),
					"callBack" : search
				});
				paramSettingUI($htmlObj);
			}, "", page.data);
		}

		function paramSettingUI($htmlObj) {
			$htmlObj.find(".paramSetting").click(function() {
				var examId = $(this).attr("objectId");
				var url = "/param/setting/" + examId;
				ctrl.getHtml(url, function(html) {
					var $htmlObj = $(html);
					ctrl.initUI($htmlObj);
					eventinit($htmlObj);
					returnExamList($htmlObj);
					initCustomizeSubjectTable($htmlObj);
					initForecastScoreLineTable($htmlObj);
					saveParam($htmlObj, examId);
					ctrl.appendToView($container, $htmlObj);
				}, "", {});

			});
		}

		function returnExamList($htmlObj) {
			$htmlObj.find("#returnExamList").click(function() {
				var page = ctrl.newPage();

				// page["curSize"] = ctrl.getindexPage($container);

				list(page);
			});
		}

		function initCustomizeSubjectTable($htmlObj) {
			var $btn = $htmlObj.find("#customizeSubjectContainer #addCustomizeSubject");
			var $table = $htmlObj.find("#customizeSubject");
			$table.editTable("click");
			deleteCustomizeSubjectTableRow($htmlObj);
			$btn.click(function() {
				var $htmlObj = $table.editTable("createRow");
				deleteCustomizeSubjectTableRow($htmlObj);
			});
		}

		function deleteCustomizeSubjectTableRow($htmlObj) {
			
			$htmlObj.find(".delete").on("click", function() {
				var $table = $htmlObj.find("#customizeSubject");
				var $tr = $(this).parents("tr");
				$table.editTable("deleteRow", $tr);
			});
		}
		//达标 事件关联 边缘生
		function eventinit($htmlObj){
			$htmlObj.find("div#dabiao").on("change","select[name='subjectId']",function(){
				var thisSelect = $(this).parent().next();
				if($(this).val()=="98"){
					thisSelect.html('<input type="text" class="form-control showEdit" name="ratio"><label class="showValue"></label>');
				}else{
					thisSelect.html('<label class="showValue" style="display: block;font-size: 12px;color: #CCC;">科目为总分才可设置边缘分</label>');
				}
				return true;
			})
		}
		//初始化函数
		function initForecastScoreLineTable($htmlObj) {
			var $tablesContainer = $htmlObj.find("#forecastScoreLine").find(".uplingScoreTable");
			$tablesContainer.each(function() {
				var $table = $(this).find("table");
				$table.editTable("click");
				deleteForecastScoreLineTableRow($table);
				var $btn = $(this).find("button[trigger=addScroe]");
				$btn.on("click", function() {
					//添加分数线
					var $htmlObj = $table.editTable("createRow");
					deleteCustomizeSubjectTableRow($htmlObj);
				});
			});
			
			//验证数据和格式是否输入正确才可切换tab
			$htmlObj.find("ul#myTab li").on("click",function(){
				var active =  $htmlObj.find("ul#myTab").find("li.active");
				var href = active.find("a").attr("href");
				var taggle = $("div"+href);
				$(taggle).find("div[id=message]").html("")
				var reg=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
				var mass = {"0":"名称","1":"分数","2":"科目","3":"边缘分不能为空","4":"边缘分不能为空"};
				var retrunM  = true;
				$.each(taggle.find("table"),function(index,table){
					var taArray = $(table).find("tr:gt(1)");
					var message = $(table).parent().parent().parent().find("div[id=message]");
					var scoretype= $(table).attr("scoretype");
					
					$.each(taArray,function(i,tr){
						$(tr).find("td").each(function(j,td){
							var input = $(td).find("input,select").val();
							if(input==undefined){
								return ;
							}
							
							if(scoretype=="4" &&(j==1 || j==4) ){
								reg=/^-?(0\.\d*[1-9]+\d*)|1$/;//小于1 大于0
								mass["1"] = "比例";
							}else{
								reg=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
								mass["1"] = "分数";
							}
							if(scoretype=="4"){
								
								if(!reg.test(input) && (j==1 || j==4) ){
									message.html(mass[j]+"填写不正确");
									retrunM= false;
								} else if(input!="" && (!/^(\d|[1-9]\d+)(\.\d+)?$/.test(input) && (j==2))){
									message.html("分数填写不正确");
									retrunM= false;
								}
							}else if(!reg.test(input) && (j==1) ){
								message.html(mass[j]+"填写不正确");
								retrunM= false;
							}
						});
					})
				})
				if(retrunM){
					taggle.find("#message").html("");
				}
				return retrunM;
			})
		}
		
		// 预测分数线 添加后的删除触发事件 <无效>
		function deleteForecastScoreLineTableRow($htmlObj) {
			$htmlObj.find(".delete").on("click", function() {
				var $table = $(this).parents("table");
				var $tr = $(this).parents("tr");
				$table.editTable("deleteRow", $tr);
			});
		}
		//保存参数按钮触发事件
		function saveParam($htmlObj, examId) {
			$htmlObj.find("#saveParam").click(function() {
				
				var customizeSubjects = validateAndCreateCustomizeSubject();
				var customizeSubjectHasError = customizeSubjects === false;
				var uplineScores = validateAndCreateUplineScore();
				var uplineScoresHasError = uplineScores === false;
				var params = validateAndCreatePageParam();
				var testPapers = validateExamMessage();
				var paramsHasError = params === false;
				var zfSubject = $('input[name="zfSubject"]:checked').val();
				var isError = uplineScoresHasError || customizeSubjectHasError;
				isError = isError || paramsHasError;
				if (isError) {
					return;
				}
				var paramSetting = {};
				paramSetting["customizeSubjects"] = customizeSubjects;
				paramSetting["uplineScores"] = uplineScores;
				paramSetting["params"] = params;
				paramSetting["testPapers"] = testPapers;
				paramSetting["zfSubject"] = zfSubject;
				var url = "/param/setting/" + examId;
				ctrl.postJson(url, paramSetting, "", function(data) {
					ctrl.moment("保存成功!", "success");
				});
			});
		}
		//页面参数 数据验证并返回数据数据
		function validateAndCreatePageParam() {
			var $pageParam = $container.find("#pageParam");
			var hasError = false;
			var errorMsg = "";
			var params = new Array();
			$pageParam.find(".myData").each(function() {
				var param = {};
				param["paramType"] = $(this).attr("paramtype");
				param["paramName"] = $(this).attr("paramname");
				param["paramAsName"] = $(this).attr("paramasname");
				var value = $(this).val();
				param["paramValue"] = value;
				if (!ctrl.isNumber(value)) {
					errorMsg = param["paramAsName"] + "必须为数字;";
					hasError = true;
					return false;
				}
				if (param["paramName"] != "scoreDip" || (param["paramName"] == "scoreDip" && $(this).attr("checked") == "checked")) {
					params.push(param);
				}
			});
			var param1 = {};
			// 处理分数线包含问题
			var s = $('input[name="f"]:checked').val();
			param1["paramType"] = "0";
			param1["paramName"] = "scoreDip";
			param1["paramAsName"] = "低分人数是否包含高分人数";
			param1["paramValue"] = s;
			params.push(param1);

			$pageParam.find("#message").text(errorMsg);
			if (hasError) {
				return false;
			}
			return params;
		}
		
		// 预测分数线 数据验证并返回数据数组
		function validateAndCreateUplineScore() {
			var taggle = $("div#myTabContent");
			$(taggle).find("div[id=message]").html("")
			
			var reg=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
			var mass = {"0":"名称","1":"分数","2":"科目","3":"边缘分不能为空","4":"边缘分不能为空"};
			var retrunM  = true;
			var scoreArray = new Array();
			$.each(taggle.find("table"),function(index,table){
				var taArray = $(table).find("tr:gt(1)");
				var message = $(table).parent().parent().parent().find("div[id=message]");
				var wl = $(table).attr("trigger");
				var level = $(table).attr("level");
				var scoretype= $(table).attr("scoretype");
				var mm = {};
				$.each(taArray,function(i,tr){
					var us = {"wlType":"0","name":"","subject":{"id":"0"},"divideScale":"0","divideScore":"0","level":"0","scoreType":"0","ratio":"0"};
					$(tr).find("td").each(function(j,td){
						var input = $(td).find("input,select").val();
						if(input==undefined){
							return ;
						}
						if(input=="" && scoretype!="4"){ 
							message.html(mass[j]+"不能为空");
							retrunM= false;
						} 
						if(scoretype=="4" &&(j==1 || j==4) ){
							reg=/^-?(0\.\d*[1-9]+\d*)|1$/;//小于1 大于0
							mass["1"] = "比例";
						}else{
							reg=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
							mass["1"] = "分数";
						}
						if(scoretype=="4"){
							//1 and 4 代表下标
							if(!reg.test(input) && (j==1 || j==4) ){
								message.html(mass[j]+"填写不正确");
								retrunM= false;
							}else if(input!="" && (!/^(\d|[1-9]\d+)(\.\d+)?$/.test(input) && (j==2))){
								message.html("分数填写不正确");
								retrunM= false;
							}
						}else if(!reg.test(input) && (j==1) ){
							message.html(mass[j]+"填写不正确");
							retrunM= false;
						}
						var inputValue = $(td).find("input,select").attr("name");
						if(inputValue=="subjectId"){
							us["subject"] = {"id":input};
							var mmIndex=mm[input];
							if(mmIndex!=undefined){
								mm[input] = mmIndex+1;
							}else{
								mm[input] = 1;
							}
						}else{
							if(input==""){
								input=undefined;
							}
							us[inputValue] = input;
						}
					});
					if(level!="" && level!=undefined){
						us["level"] = level;
					}else{
						us["level"] = mm[us.subject.id];
					}
					us["wlType"] = wl;
					us["scoreType"] = scoretype;
					scoreArray.push(us);
				})
			})
			if(retrunM){
				taggle.find("#message").html("");
			}else{
				return false;
			}
			return scoreArray;
		}
		// 设置学科类型并返回数据（主科，AB卷设置）验证
		function validateExamMessage() {
			var $tmpContainer = $container.find("#mainSubjectSetting");
			var $table = $tmpContainer.find("table");
			var $trs = $table.find("tbody tr").not(".template");
			var testPapers = new Array();
			$trs.each(function() {
				var $tds = $(this).find("td");
				var id = $($tds[0]).find("input").val();
				// 获取被选中的值
				var mainUrl = "mainType" + id;// 获取imput的name（主科）
				var masterSubject = false;// 主科标志
				var obj1 = document.getElementsByName(mainUrl);
				// 只有两个值的情况下可以这么写提高效率（页面获取的实际值为1 （true） 或 0（false））
				if (obj1[0].checked) {
					masterSubject = true;
				}
				
				// 获取AB卷被选中的值
				var idUrl = "subjectType" + id;// 获取imput的name（AB卷）
				var containPaper = false;// AB卷标志
				var obj = document.getElementsByName(idUrl);
				if (obj[0].checked) {
					containPaper = true;
				}
				
				// 获取是否分析被选中的值
				var analysisUrl = "analysisType" + id;// 获取imput的name（AB卷）
				var analysis = false;// 是否分析标志
				var obj2 = document.getElementsByName(analysisUrl);
				if (obj2[0].checked) {
					analysis = true;
				}
				var cs = {};
				cs["id"] = id;
				cs["masterSubject"] = masterSubject;
				cs["containPaper"] = containPaper;
				cs["hasAnalysis"] = analysis;
				testPapers.push(cs);
			});
			return testPapers;
		}
		// 自定义科目 数据验证并返回数据集合
		function validateAndCreateCustomizeSubject() {
			var $tmpContainer = $container.find("#customizeSubjectContainer");
			var $table = $tmpContainer.find("table");
			var $trs = $table.find("tbody tr").not(".template");

			var customizeSubjects = new Array();

			var hasError = false;
			var errorMsg = "";
			$trs.each(function() {
				var $tds = $(this).find("td");
				var name = $($tds[0]).find("input").val();
				var combinationSubjectId = $($tds[0]).find("input").attr("combinationSubjectId");
				var $opts = $($tds[1]).find("select option:selected");
				var csXtps = new Array();
				var hasL = false;
				var hasW = false;
				$opts.each(function() {
					var $opt = $(this);
					var paperType = $opt.attr("data-paperType");
					var id = $opt.attr("data-id");
					var fullScore = $opt.attr("data-fullScore");
					var name = $opt.text();
					if (paperType === "1") {
						hasL = true;
					} else if (paperType === "2") {
						hasW = true;
					}

					var testPaper = {};
					testPaper["id"] = id;
					var csXtp = {};
					csXtp["testPaper"] = testPaper;
					csXtps.push(csXtp);
				});

				if (hasW && hasL) {
					hasError = true;
					errorMsg = "组合科目中同时选择了文科成绩和理科的成绩，请重新选择！";
					return false;
				}
				if (csXtps.length < 2) {
					hasError = true;
					errorMsg = "必须选择2科试卷成绩以上！";
					return false;
				}

				if (name === "") {
					hasError = true;
					errorMsg = "科目名称不能为空，必须填写！";
					return false;
				}

				var paperType = 0;
				if (hasW) {
					paperType = 2;
				} else if (hasL) {
					paperType = 1;
				}

				var cs = {};
				if (combinationSubjectId) {
					cs["id"] = combinationSubjectId;
				}
				cs["name"] = name;
				cs["paperType"] = paperType;
				cs["childTestPaper"] = csXtps;
				customizeSubjects.push(cs);
			});

			$tmpContainer.find("#message").text(errorMsg);
			if (hasError) {
				return false;
			} else {
				return customizeSubjects;
			}
		}

		var self = undefined;
		var o = self = {
			render : function(container) {
				$container = $(container);
				list();
			}
		};
		return $.extend(o, ctrl);
	});
})();