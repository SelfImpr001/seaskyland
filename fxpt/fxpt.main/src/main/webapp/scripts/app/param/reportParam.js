(function() {
	"use strict";
	var model = [ "jquery", "jqueryPager", "controller", "formToJosn",
			"editTable", "strBuf" ];
	define(model, function($, $page, ctrl, formToJosn, editTable, strBuf) {
		var $container = undefined;

		function exams(page) {
			if (!page) {
				page = ctrl.newPage();
			}
			var url = "/report/param/exams/" + page.curSize + "/"
					+ page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				ctrl.initUI($htmlObj);
				examSelector($htmlObj);
				levelScoreSettingButtonEvent($htmlObj);
				setStandardScoreButtonEvent($htmlObj);
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
			var url = "/report/param/exams/" + page.curSize + "/"
					+ page.pageSize;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				$container.find("#examList table tbody").html($htmlObj);
				ctrl.renderPager({
					"containerObj" : $htmlObj,
					"pageObj" : $container.find("#examList #pager"),
					"callBack" : search
				});

			}, "", page.data);
		}

		/** ********************** */
		function levelScoreSettingButtonEvent($htmlObj) {
			$htmlObj.on("click", ".setLevelScore", function() {
				var examId = $(this).attr("objectId");
				levelScoreSettingViewPage(examId);
			});
		}

		function levelScoreSettingViewPage(examId) {
			var url = "/report/param/levelScore/view/" + examId;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				returnUperPage($htmlObj);
				updateLevelScore($htmlObj, examId);
				ctrl.appendToView($container, $htmlObj);
			});
		}

		function returnUperPage($htmlObj) {
			$htmlObj.find("#returnUperPage").click(function() {
				exams();
			});
		}

		function updateLevelScore($htmlObj, examId) {
			$htmlObj.find("#levelScoreTable").on("click", ".update",
					function() {
						var atpId = $(this).attr("objectId");
						var wl = $(this).attr("data-wl");
						updateView(examId, atpId, wl);
					});
		}

		function updateView(examId, atpId, wl) {
			var url = "/report/param/levelScore/update/view/" + examId + "/"
					+ wl + "/" + atpId;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				ctrl.initUI($htmlObj);
				
				var $table = $htmlObj.find("#levelScoreTable");
				var $atpSelect = $htmlObj.find("#atpSelect");
				var $wlSelect = $htmlObj.find("#wlSelect");
				var $levelNumSelecte = $htmlObj.find("#levelNumSelecte");
				
				changeWLSelect($htmlObj, examId, wl);
				changeAtpSelecte($htmlObj);
				changeLevelNumSelecte($htmlObj);
				levelScoreSaves($htmlObj);

				changeAtp($htmlObj,$table, $atpSelect, $wlSelect,$levelNumSelecte);

				returnUperPageUpdateValue($htmlObj, examId);
				ctrl.appendToView($container, $htmlObj);
			});
		}
		
		function levelScoreSaves($htmlObj){
			$htmlObj.find("#saves").click(function(){
				var wl = $htmlObj.find("#wlSelect").val();
				var atpId = $htmlObj.find("#atpSelect").val();
				var $table = $htmlObj.find("#levelScoreTable");
				var levelScoreArray = new Array();
				$table.find("tbody tr").not(".template").each(function(){
					var $tr = $(this);
					var levelName = $tr.find("[name='levelName']").val();
					var beginScore = $tr.find("[name='beginScore']").val();
					var endScore = $tr.find("[name='endScore']").val();
					var levelScore = $tr.find("[name='levelScore']").val();
					var obj = {};
					obj["beginScore"] = beginScore;
					obj["endScore"] = endScore;
					obj["levelName"] = levelName;
					obj["levelScore"] = levelScore;
					levelScoreArray.push(obj);
				});
				
				var url = "/report/param/levelScore/save/"+wl+"/" + atpId;
				ctrl.postJson(url, levelScoreArray, "增加成功!", function(
						data) {
					
					emptyLevelScoreTable($table);
					createLevetScoreTable($table,data["lsses"]);
					ctrl.moment("保存成功!", "success");
				});
			});
		}

		function changeLevelNumSelecte($htmlObj) {
			$htmlObj.find("#levelNumSelecte").change(function() {
				var $table = $htmlObj.find("#levelScoreTable");

				var $atpSelect = $htmlObj.find("#atpSelect");
				var $option = $atpSelect.find(":selected");
				var fullScore = parseFloat($option.attr("data-fullScore"));
				var levelCount = parseInt($(this).val());

				emptyLevelScoreTable($table);
				initLevetScoreTable($table, fullScore, levelCount);
			});
		}

		function changeWLSelect($htmlObj, examId) {
			$htmlObj.find("#wlSelect").change(function() {
				var $wlSelect = $(this);
				var wl = $(this).val();
				var url = new StringBuffer();
				url.append("/report/param/levelScore/atpList/");
				url.append(examId);
				url.append("/");
				url.append(wl);
				ctrl.getJson(url.toString(), function(data) {
					var $atpSelect = $htmlObj.find("#atpSelect");
					$atpSelect.empty();
					var atpList = data["atpList"];
					var atpListSize = atpList.length;
					for (var i = 0; i < atpListSize; i++) {
						var option = new StringBuffer();
						option.append("<option value='");
						option.append(atpList[i].id);
						option.append("' data-fullScore='");
						option.append(atpList[i].fullScore);
						option.append(" '>");
						option.append(atpList[i].name);
						option.append("</option>");
						$atpSelect.append(option.toString());
					}
					$atpSelect.selectpicker("refresh");

					var $table = $htmlObj.find("#levelScoreTable");
					var $levelNumSelecte = $htmlObj.find("#levelNumSelecte");
					$levelNumSelecte.find(":selected").attr("selected",false);
					changeAtp($htmlObj,$table, $atpSelect, $wlSelect,$levelNumSelecte);
				});
			});
		}

		function changeAtpSelecte($htmlObj) {
			$htmlObj.find("#atpSelect").change(function() {
				var $table = $htmlObj.find("#levelScoreTable");
				var $wlSelecte = $htmlObj.find("#wlSelect");
				var $levelNumSelecte = $htmlObj.find("#levelNumSelecte");
				$levelNumSelecte.find(":selected").attr("selected",false);
				changeAtp($htmlObj,$table, $(this), $wlSelecte,$levelNumSelecte);
			});
		}

		function changeAtp($htmlObj,$table, $atpSelect, $wlSelect,$levelNumSelecte) {
			var $option = $atpSelect.find(":selected");
			var atpId = $atpSelect.val();
			var wl = $wlSelect.val();
			var fullScore = parseFloat($option.attr("data-fullScore"));

			emptyLevelScoreTable($table);

			var url = new StringBuffer();
			url.append("/report/param/levelScore/data/");
			url.append(wl);
			url.append("/");
			url.append(atpId);

			ctrl.getJson(url.toString(), function(data) {
				var lsses = data["lsses"];
				var lssesCount = lsses.length;
				if (lssesCount == 0) {
					lssesCount = 4;
					initLevetScoreTable($table, fullScore, lssesCount);
				} else {
					createLevetScoreTable($table,lsses);
				}
				
//				$levelNumSelecte.find(":selected").attr("selected",false);
				//ctrl.log($levelNumSelecte.find(":selected"))
				$levelNumSelecte.find("[value='"+lssesCount+"']").attr("selected",true);
//				$htmlObj.find('.selectpicker').selectpicker("refresh");
				$levelNumSelecte.selectpicker("refresh");
			});
		}

		function emptyLevelScoreTable($table) {
			$table.find("tbody tr").not(".template").each(function() {
				var $tr = $(this);
				$table.editTable("deleteRow", $tr);
			});
		}

		function initLevetScoreTable($table, fullScore, levelCount) {
			var score = fullScore / levelCount;
			for (var i = 0; i < levelCount; i++) {
				var $htmlObj = $table.editTable("createRow");
				$htmlObj.find("[name='id']").text((i + 1));
				var beginScore = fullScore - (score * (i + 1));
				$htmlObj.find("[name='beginScore']").val(beginScore.toFixed(2));
				var endScore = fullScore - score * i;
				$htmlObj.find("[name='endScore']").val(endScore.toFixed(2));
			}
		}

		function createLevetScoreTable($table,lsses) {
			var lssesCount = lsses.length;
			for (var i = 0; i < lssesCount; i++) {
				var $htmlObj = $table.editTable("createRow");
				$htmlObj.find("[name='id']").text((i+1));
				$htmlObj.find("[name='beginScore']").val(lsses[i]["beginScore"]);
				$htmlObj.find("[name='endScore']").val(lsses[i]["endScore"]);
				$htmlObj.find("[name='levelName']").val(lsses[i]["levelName"]);
				$htmlObj.find("[name='levelScore']").val(lsses[i]["levelScore"]);
				$htmlObj.find("[name='num']").text(lsses[i]["num"]);
				var percent = lsses[i]["num"]/lsses[i]["skrs"]*100;
				$htmlObj.find("[name='percent']").text(percent.toFixed(2)+"%");			
			}
		}

		function returnUperPageUpdateValue($htmlObj, examId) {
			$htmlObj.find("#returnUperPage").click(function() {
				levelScoreSettingViewPage(examId);
			});
		}

		/** ********************** */
		
		
		/******************SetStandardScore******************/
		
		function setStandardScoreButtonEvent($htmlObj) {
			$htmlObj.on("click", ".setStandardScore", function() {
				var examid = $(this).attr("objectId");
				setStandardScoreView(examid);
			});
		}

		function setStandardScoreView(examid) {
			var url = "/report/param/standardScore/view/" + examid;
			ctrl.getHtml(url, function(html) {
				var $htmlObj = $(html);
				returnUperPage($htmlObj);
				updateStandardScore($htmlObj, examid);
				ctrl.appendToView($container, $htmlObj);
			});
		}
		
		function updateStandardScore($htmlObj, examid) {
					$htmlObj.find("#standardScoreTable").on("click", ".update",
							function() {
								var wl = $(this).attr("wl");
								updateStandardScoreView(examid, wl);
							});
		}
		
		
		function updateStandardScoreView(examid,wl){
					var url = "/report/param/standardScore/update/view/"
							+ examid + "/" + wl;
					ctrl.getHtml(url, function(html) {
						var $htmlObj = $(html);
						ctrl.initUI($htmlObj);
						autoSum($htmlObj);
						zsave($htmlObj);
						changeStandardWl($htmlObj);
						returnsetStandardScoreView($htmlObj, examid);
						ctrl.appendToView($container, $htmlObj);

					});
		}
		

		function returnsetStandardScoreView($htmlObj,examid) {
			$htmlObj.find("#returnsetStandardScoreView").click(function() {
				setStandardScoreView(examid);
			});
		}
		
		function autoSum($htmlObj){
			onload($htmlObj);
			$(document).on("input propertychange",".change",function() {
				onload($htmlObj);
			});
		}
		
		function onload($htmlObj){
			var str = "Z(总分)=";
			var zz = /^([0-9][0-9]?|100)$/;
			var res = "";
			$htmlObj.find(".change").each(function(i){
				if($(this).attr("subjectid") !="" && $(this).attr("subjectname")!="" && $(this).attr("examid")!=""){
					$htmlObj.find("#s"+$(this).attr("subjectid")).html("");
					if($(this).val()!=''){
						if(!zz.test($(this).val())){
							$htmlObj.find("#s"+$(this).attr("subjectid")).html("只能输入0-100数字");
							$(this).val("");
							return;
						}else{
							str+=$(this).val()+"%*Z("+$(this).attr("subjectname")+")+";
							res+=$(this).attr("examid")+"_"+$(this).attr("subjectid")+"_"+$(this).val()+";";
						}
					}
				}
			});
			$htmlObj.find("#stxt").html(str.substring(0, str.length-1));
		}
		
		
		function zsave($htmlObj){
			$htmlObj.find("#zsaves").click(function() {
						var res = new Array();
						$htmlObj.find(".change").each(function(i){
							if($(this).attr("subjectid") !="" && $(this).attr("subjectname")!="" && $(this).attr("examid")!="" && $(this).attr("zwl")!=""){
									res.push($(this).attr("examid")+"_"+$(this).attr("subjectid")+"_"+$(this).val()+"_"+$(this).attr("zwl"));
							}
						});
						var defaultvalue = $htmlObj.find("#defaultvalue").val();
						var zvalue = $htmlObj.find("#zvalue").val();
						var examid = $htmlObj.find("#examid").val();
						var wl = $htmlObj.find("#wl").val();
						res.push(examid+"_"+98+"_"+defaultvalue+":"+zvalue+"_"+wl);
						var url = "/report/param/standardScore/save/view/"+res;
						ctrl.postJson(url, res, "保存成功!", function(
								data) {
							ctrl.moment("保存成功!", "success");
						});
					});
		}
		
		
		function changeStandardWl($htmlObj){
			$htmlObj.find("#wlSelect").change(function() {
				var wl = $(this).val();
				var examid = $htmlObj.find("#examid").val();
				updateStandardScoreView(examid,wl);
			});
		}
		
		/****************************************************/
		
		
		
		
		

		var self = undefined;
		var o = self = {
			render : function(container) {
				$container = $(container);
				exams();
			}
		};
		return $.extend(o, ctrl);
	});
})();