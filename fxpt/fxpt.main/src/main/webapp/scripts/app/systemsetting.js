(function() {
	"use strict";

	define([ './etl/dataCategory', './etl/dataField', './etl/dataTransform',
			'./bi/dataBi', './bi/dataBiInfo', './subject', './examType',
			'./grade', './education', './school', 'lefttree', 'ajax','app/user/user' ],
			function(dataCategory, dataField, dataTransform, dataBi,
					dataBiInfo, subject, examType, grade, education, school, lefttree,
					ajax,user) {

				function collapsebox() {
					var $oBtn = $('a[data-action=collapse]');

					$oBtn.click(function() {

						$(this).children('i').toggleClass('icon-chevron-down',
								'icon-chevron-up');

						var $oContent = $(this).parent().parent().next().eq(0);

						$oContent.slideToggle(200);
					})

				}

				function navigateMenu() {
					var uuid = $("#menu_uuid").val();
					
					ajax({
						url : "/systemsetting/menu/"+uuid,
						async : true,
						dataType : "html",
						callback : function(htmlStr) {
							$("#sidebar").html(htmlStr);
							initLeftMenu();
							firstMenuOpen();
							lefttree();
						}
					});
				}

				function firstMenuOpen() {
					var $oDtreeA = $(".nav-list a[menu!='']");
					var $obj = $oDtreeA.first();
					$obj.click();
				}

				function initLeftMenu() {
					$("[menu='dataCategory']").click(function() {
						dataCategory.dataCategoryList();
					});
					$("[menu='dataField']").click(function() {
						dataField.dataFieldList();
					});
					$("[menu='transform']").click(function() {
						dataTransform.dataTransformList();
					});
					$("[menu='biSystemSetting']").click(function() {
						dataBiInfo.biInfoList();
					});
					$("[menu='biUserSetting']").click(function() {
						dataBi.dataBiList();
					});
					$("[menu='subject']").click(function() {
						subject.list();
					});
					$("[menu='examType']").click(function() {
						examType.list();
					});
					$("[menu='grade']").click(function() {
						grade.list();
					});
					$("[menu='education.province']").click(function() {
						education.list(1);
					});
					$("[menu='education.city']").click(function() {
						education.list(2);
					});
					$("[menu='education.county']").click(function() {
						education.list(3);
					});
					$("[menu='school']").click(function() {
						school.list("school");
					});
				}

				var o = {
					init : function() {
						navigateMenu();
						user.setpwd($('#setpwd'));
					},

					render : function() {
						this.init();
					}
				};

				return o;

			});

})();
