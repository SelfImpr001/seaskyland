(function() {
	"use strict";
	define(['jquery'],function($){
		
		
        function init(){
        	 $("#updateBtn").click(function() {
                 $("#form")
                         .attr("action", window.app.rootPath+"/organization/update/${organization.pk}")
                         .submit();
                 return false;
             });
             $("#deleteBtn").click(function() {
                 if(confirm("确认删除吗？")) {
                     $("#form")
                             .attr("action", window.app.rootPath+"/organization/delete/${organization.pk}")
                             .submit();
                 }
                 return false;
             });
        	
        };
        
        
        
		var o = {
           render:function(){
               init();
           }
       };
       return o;
	});

})();