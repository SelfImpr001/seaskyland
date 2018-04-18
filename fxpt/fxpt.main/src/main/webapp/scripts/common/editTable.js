(function() {
	"use strict";
	var model = [ "jquery" ];
	define(model, function($) {
		$.fn.extend({
			"editTable" : function(param, $tr) {
				var $self = $(this);
				var $tbody = $self.find("tbody");

				if (param == "createRow") {
					return createRow();
				} else if (param == "deleteRow") {
					deleteRow($tr);
				}else if(param == "click"){
					tableTrClick();
				}
				
				function tableTrClick(){
					var $trs = $tbody.find("tr").not(".template");
					trsHide();
					$trs.each(function(){
						trClick($(this));
					});
				}

				function createRow() {
					var $html = $tbody.find(".template").clone();
					$html.removeClass("template");
					$html.find("select").next(".bootstrap-select").remove();
					$html.find("select").selectpicker();
					trsHide();
					trClick($html);
					$tbody.append($html);
					return $html;
				}

				function trClick($html) {
					$html.on("click", function() {
						trsHide();
						trShow($(this));
					});
				}

				function trsHide() {
					var $trs = $tbody.find("tr").not(".template");
					$trs.each(function() {
						trHide($(this));
					});
				}

				function trHide($tr) {
					$tr.find(".showEdit").each(function() {
						var value = "";
						if (this.tagName.toLowerCase() === "select") {
							value = multiSelect($(this));
							$(this).hide();
						} else if (this.tagName.toLowerCase() === "div") {
							$(this).hide();
							return true;
						} else {
							value = inputTextValue($(this));
							$(this).hide();
						}
						var $td = $(this).parents("td");
						$td.find(".showValue").text(value).show();
					});
				}
				
				function trShow($tr){
					$tr.find(".showEdit").each(function() {
						var value = "";
						if (this.tagName.toLowerCase() === "select") {
							
						}else {
							$(this).show();
						}
						var $td = $(this).parents("td");
						$td.find(".showValue").hide();
					});
				}

				function inputTextValue($obj) {
					var value = $obj.val();
					return value;
				}

				function multiSelect($obj) {
					var $opt = $obj.find("option:selected");
					var values = new Array();
					$opt.each(function() {
						values.push($(this).text());
					});
					return values.join(",");
				}

				function deleteRow($tr) {
					$tr.remove();
				}

			}
		});
	});
})();