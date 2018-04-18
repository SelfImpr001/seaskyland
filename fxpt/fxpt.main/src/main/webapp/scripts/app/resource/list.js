(function(){
	"use strict";
	define(['jquery','common/ajax','lib/jquery.treetable','lib/jquery.pager'],function($,ajax){
		var contextPath = $("#contextPath").val();
		
        function init(){
        	$("#tabletree").treetable({ expandable: true }).treetable("expandNode", 1);
        	$(".deleteBtn").click(function() {
        	    if(confirm("确认删除吗?")) {
        	        location.href = contextPath+"/resource/"+$(this).data("id")+"/delete";
        	        
        	    }
        	});
        	
        };
        
        
        function renderPager(){
			var _pageNum=parseInt($("#pageNum").val());
			var _pageCount=parseInt($("#pageCount").val());
			var _pageSize=parseInt($("#pageSize").val());
			$("#pager").pager({
				pageNum : _pageNum,
				pageCount : _pageCount,
				click : function(pageNum, pageCount) {
					var $obj = $("#rightFrm",window.parent.document);
					$obj.attr("src","/resource/"+pageNum+"/"+_pageSize);
					
					ajax({
						url : "/resource/"+pageNum+"/"+_pageSize,
						async : true,
						type : "GET",
						dataType : "html",
						//data : JSON.stringify(page),
						callback : function(html) {
							$('tbody').empty().append(html);
							
						}
					});
				}
			});
		}
        
		var o = {
           render:function(){
        	   var page = {};
				page["pagesize"] = 4;
				page["curpage"] = 1;
				//$("#tabletree").treetable({ expandable: true }).treetable("expandNode", 1);;
				//return;
        	  renderPager();
//        	   ajax({
//					url : "/resource/"+1+"/"+4,
//					async : true, 
//					type : "GET",
//					dataType : "html",
////					data : JSON.stringify(page),
//					callback : function(html) {
//						$('#tabletree tbody').empty().append(html);
//						//$("#tabletree").treetable({ expandable: true }).treetable("expandNode", 1);
//						console.log('-----------');
//					}
//				});        	   
               init();
           }
       };
       return o;
	});
    
})();