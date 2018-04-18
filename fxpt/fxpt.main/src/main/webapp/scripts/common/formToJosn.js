(function() {
	"use strict";
	define(function() {
		/**
		 * 获取FORM表单中的数据 必须用class="myData"
		 */
		$.fn.extend({
			"formToJson" : function() {
				var obj = this.find(".myData");

				var o = {};
				obj.each(function() {
					var tagName = this.tagName.toUpperCase();
					if ("INPUT" == tagName) {
						var type = $(this).attr("type").toUpperCase();
						if (type == "TEXT" || type == "HIDDEN" || type == "PASSWORD" || type=="EMAIL") {
							inputText($(this));
						} else if (type == "RADIO") {
							inputRadio($(this));
						}
					} else if ("SELECT" == tagName) {
						if (this.multiple) {

						} else {
							selectS($(this));
						}
					} else if ("TEXTAREA" == tagName) {
						textarea($(this));
					}

				});

				return o;

				function textarea(jObj) {
					var nameArray = jObj.attr("name").split(".");
					var tempObj = o;
					$.each(nameArray, function(i, v) {
						if (i == nameArray.length - 1) {
							tempObj[v] = jObj.val();
						} else {
							tempObj = tempObj[v] = tempObj[v] || {};
						}
					});
				}
				;

				function inputRadio(jObj) {
					if(!jObj.attr("name")) return;
					var nameArray = jObj.attr("name").split(".");
					var tempObj = o;
					$.each(nameArray, function(i, v) {
						if (i == nameArray.length - 1) {
							var tempName = jObj.attr("name");
							tempObj[v] = $(
									"input[type=\"radio\"][name=\"" + tempName
											+ "\"]:checked").val();
						} else {
							tempObj = tempObj[v] = tempObj[v] || {};
						}
					});
				}
				;
				function selectS(jObj) {
					var nameArray = jObj.attr("name").split(".");
					var tempObj = o;
					$.each(nameArray, function(i, v) {
						if (i == nameArray.length - 1) {
							tempObj[v] = jObj.val();
						} else {
							tempObj = tempObj[v] = tempObj[v] || {};
						}
					});
				}
				;

				function inputText(jObj) {
					var nameArray = jObj.attr("name").split(".");
					var tempObj = o;
					$.each(nameArray, function(i, v) {
						if (i == nameArray.length - 1) {
							var tempValue = jObj.val();
							tempObj[v] = trim(tempValue);
						} else {
							tempObj = tempObj[v] = tempObj[v] || {};
						}
					});
				}
				;

				function trim(str) {
					return str.replace(/(^\s*)|(\s*$)/g, "");
				}
				;
			}
		});
	});

})();