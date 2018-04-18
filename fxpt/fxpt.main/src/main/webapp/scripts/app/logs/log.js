(function() {
	"use strict";
	define([ 'jquery', 'ajax', "dialog", "controller",
				'validatejs','jqueryPager','formToJosn'],
			function($, ajax, dialog,ctrl,pager) {
				var self = undefined;
				 
				function renderPager(){
					ctrl.renderPager({
						"containerObj" : self.container,
						"pageObj" : self.container.find('#log_pager'),
						"callBack":self.query
					});	
				}
				
				function formToQueryPath(){
					var $inputs = self.container.find('div.query-form input');
					var $selects = self.container.find('div.query-form select');
					var queryPath = "";
					$inputs.each(function(i,n){
						queryPath += "&" + n.name + '='+n.value;
					});
					$selects.each(function(i,n){
						queryPath += "&" + n.name + '='+n.value;
					});
					return queryPath;
				}
				
				function getPager(){
			    	var pager = ctrl.newPage(self.container);
		    		return pager;
			    };
			    function changes( container ){
			    	$(container).on("change","#optionValue",function(){
			    		var thisVal = $(this).val();
			    		if(thisVal =="dateTime"){
			    			$("#TextDiv").hide();	
			    			$("#DateTimeDiv").show();
			    			container.find("#optionName").val("");
			    		}else{
			    			$("#TextDiv").show();	
			    			$("#DateTimeDiv").hide();
			    			container.find("input[name=startDate],input[name=endDate]").val("");
			    		}
			    	});
			    }
			    
				//==========================end======================================
			    
				var o = self = {
					container:undefined,
					render : function(container) {
						var page = ctrl.newPage(self.container);
						var url = "log/list/"+page.curSize+"/"+page.pageSize+"?ui_all=true";
						self.log("get url " + url);
						self.getHtml(url,function(html) {
							self.container = $(html);
			        	    ctrl.appendToView(container,self.container);
			        	    ctrl.initUI(self.container);
			        	    
			        	    renderPager();
			        	    changes(self.container);
			        	    
			        	    self.container.on('click','[trigger="LogInfo"]',function() {
			        	    	var pk = $(this).attr("pk");
			        	    	self.logview(pk);
			        	    });
                            self.container.on('click', '[trigger="reset"]', function () {
                                self.container.find("form [type='text']").val("");
                            });

                            self.container.on('click', '#downloadLog', function () {
                                var startDate = $('#examStartData').val();
                                var endDate = $('#examEndData').val();
                                var optionValue = $('#optionValue').val();
                                var optionName = $('#optionName').val();
                                var status = $('#status').val();
                                var url = 'download/downLog?optionName=' + optionName + "&optionValue=" + optionValue + "&startDate=" + startDate + "&endDate=" + endDate + "&status=" + status;
                                window.open(url);
                            });
			        	    
			        	    self.container.on('click','.form-side>a:eq(0)',function() {
								self.query();
			        	    });
			        	    self.container.on('click',".form-side>a:eq(1)",function(){
			        	    	var $inputs = self.container.find('div.query-form input');
								$inputs.val("");
								var $select = self.container.find('div.query-form select');
								$select.each(function(){
									$(this).find("option")[0].selected=true;
								});
				        	});
			        	    
			        	    var widthcss = ["5%","10%","8%","8%","10%","25%","5%","5%"];
			        	    self.container.find("table").find("thead th").each(function(i){
			        	    	$(this).css("width", widthcss[i]);
			        	    });
						});
					},query:function(page){
							  if(!page){
				         		  page = getPager();
				         	  }
							  ctrl.getHtml("log/list/"+page.curSize+"/"+page.pageSize+"?"+formToQueryPath(),function(html){
								  ctrl.refreshDataGrid('log_datagrid',self.container,$(html),renderPager);
								  var widthcss = ["5%","10%","8%","8%","10%","25%","5%","5%"];
					        	  self.container.find("table").find("thead th").each(function(i){
					        		  $(this).css("width", widthcss[i]);
					        	  });
					        	  
				        	  });
							  
					 },logview : function(pk){
							ctrl.getHtmlDialog(("log/view/"+pk), "big-modal",function(html) {
								var $htmlObj = $(html);
								ctrl.initUI($htmlObj);
								
								ctrl.modal($htmlObj.find("#handleOption").val(), html, function() {
										return true;
								}, "","lg");
								$(".dialog-footer").css("display","none");
							});
					}
				};
				return $.extend(o, ctrl);
			});
})();