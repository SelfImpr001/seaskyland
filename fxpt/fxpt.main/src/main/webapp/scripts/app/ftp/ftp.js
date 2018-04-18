(function() {
	"use strict";
	define(
			[ 'jquery', 'ajax', 'formToJosn', "dialog", "controller",
					'validatejs',"ajaxfileupload" ],
			function($, ajax, form, dialog, ctrl,ajaxfileupload) {
				var self = undefined;
			
				
				function save(html) {
					var id = html.find("input[name=id]").val();
					var url = html.find("input[name=url]").val();
					var path = html.find("input[name=path]").val();
					var port = html.find("input[name=port]").val();
					var username = html.find("input[name=username]").val();
					var password = html.find("input[name=password]").val();
					var des = html.find("input[name=des]").val();
					var status = html.find("input[name=status]:checked").val();
					var ftpSetting = {
						id:id,
						url:url,
						path:path,
						port:port,
						username:username,
						password:password,
						des:des,
						status:status
					};
					ctrl.postJson("/ftpSetting/update", ftpSetting,
							"设置成功!", function() {
							self.render();
							});
				}
				
				function testFtpSuccess(html){
					var id = html.find("input[name=id]").val();
					var url = html.find("input[name=url]").val();
					var path = html.find("input[name=path]").val();
					var port = html.find("input[name=port]").val();
					var username = html.find("input[name=username]").val();
					var password = html.find("input[name=password]").val();
					var des = html.find("input[name=des]").val();
					var status = html.find("input[name=status]:checked").val();
					var ftpSetting = {
						id:id,
						url:url,
						path:path,
						port:port,
						username:username,
						password:password,
						des:des,
						status:status
					};
					ctrl.postJson("/ftpSetting/testFtpSuccess", ftpSetting,"", function(data) {
								if(data.isSuccess){
									ctrl.moment("FTP连接成功！", "success");
								}else{
									ctrl.moment("FTP连接失败！", "success");
								}
							});
				}
			
				function bindEvent() {
					var $obj = self.container;
					$obj.on('click','#saveSetting',function() {
								save($obj);
							});
					$obj.on('click','#testFtpSuccess',function() {
						testFtpSuccess($obj);
					});
			
				}

				var o = self = {
					container:undefined,
					render : function(container) {
						var url = self.getCurMenu().attr("url");
						self.log("get url " + url);
						self.getHtml(url,function(html) {
							self.container = $(html);
			        	    ctrl.appendToView(container,self.container);
							bindEvent();
						});
					}
				};
				return $.extend(o, ctrl);
			});

})();