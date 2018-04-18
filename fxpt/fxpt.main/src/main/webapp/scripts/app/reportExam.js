(function() {
	"use strict";
	define([ 'ajax',"controller","jqueryPager" ], function(ajax,ctrl) {
        
		var _listUrl = ctrl.getCurMenu().attr('url');
		var self, o;
		self = o = {
			container:undefined,
			queryForm:undefined,
			render:function(container){   	  
        	   ctrl.getHtml(_listUrl,function(html){
        		   self.container = $(html);
        		   self.queryForm = self.container.find('div.query-form');
        	       ctrl.appendToView(container,self.container);
        	       ctrl.initUI(self.container);
        	       bindEvent();
        	       renderPager();
        	        
        	   });
		    },
            query:function(page){
            	if(!page){
	         	   page = ctrl.newPage(this.container);
	         	}
         	   
         	    var queryPath = formToQueryPath( );
         	    var url = "report/exam/list/" + page.curSize + "/" + page.pageSize + "?" + queryPath;
         	    ctrl.getHtml(url,function(data){
         	    	refresh(data);
         	    });	         	    
            }
		};

		function bindEvent(){
			self.queryForm.on('click','.form-side>a:eq(0)',function(){
				 self.query();
	        }).on('keydown','input[name=examName],input[name=examSortName]',function(e){
	        	$(this).tooltip('destroy');
	        	if(this.value.length > 50 && ((e.keyCode > 47 && e.keyCode < 254 ||  e.keyCode == 32))){
	        		$(this).tooltip('show');
	        		return false; 
	        	}
	        	return true;
	        });
		};
		
	    function renderPager() {	    	
	    	ctrl.renderPager({
				"containerObj" : self.container,
				"pageObj" : self.container.find('#examReport_pager'),
				"callBack":self.query
			});	    	
		};
		function refresh(data){
			ctrl.refreshDataGrid('examReport_data_rows',self.container,$(data),renderPager);
		};
		
		function formToQueryPath(){
			var $inputs = self.queryForm.find('input,select');
			var queryPath = "";
			$inputs.each(function(i,n){
				if($(n).val().length > 0)
				    queryPath += "&" + n.name + '='+n.value;
				});
			return queryPath.substring(queryPath.indexOf("&"),queryPath.length);
		};

		return o;

	});

})();