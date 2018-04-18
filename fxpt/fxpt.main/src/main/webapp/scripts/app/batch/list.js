(function(){
	"use strict";
	define(['jquery','ajax','formToJosn',"app/user/grant","dialog","controller","app/user/userOrg",
	        'validatejs',"ztreecore","ztreecheck","jqueryPager"],function($,ajax,form,grant,dialog,ctrl,orgTree){
	    var self = undefined;
		var _listUrl = ctrl.getCurMenu().attr('url');
		
		
		function getPager(){
	    	var pager = ctrl.newPage(self.container);
    		return pager;
	    };
	    function renderPager() {	       	
	    	ctrl.renderPager({
				"containerObj" : self.container,
				"pageObj" : self.container.find('#exam_pager'),
				"callBack":self.query
			});	    	
		}
		function refresh(data){
			ctrl.refreshDataGrid('exam_data_rows',self.container,$(data),renderPager);			
		};
		function analysisExam($html) {
			var $tbody = $html.find("#exam_datagrid table tbody", self.container);
			$tbody.on("click", "a[trigger='batchExec']", function() {
				var pk = $(this).attr("pk");
				ctrl.postJson("/batch/batchExecNum/"+pk, {} ,"生成成功!", function(data) {
					   ctrl.confirm("操作","已生成<b style='color:red;'>"+data.status.msg+"</b>条数据,继续将会删除<b style='color:red;'>"+data.status.msg+"</b>条生成的数据，是否要继续",function(){
						   ctrl.postJson("/batch/batchExec/"+pk, {} ,"", function(data) {
							   self.query(getPager());
							});
						},"继续",function(){
							return false;
						},"取消");
					});
			});
			
			$tbody.on("click","a[trigger=batchReset]",function(){
			   var pk = $(this).attr("pk");
			   ctrl.confirm("操作","您将删除该学校的所有学生个人报告，是否继续",function(){
					ctrl.postJson("/batch/batchReset/"+pk, {} ,"重置成功!", function(data) {
					});
				},"确定",function(){
					return false;
				},"取消");
			   
		   });
			$tbody.on("click","a[trigger=batchInfo]",function(){
			   var pk = $(this).attr("pk");
			   ctrl.postJson("/batch/batchExecNum/"+pk, {} ,"生成成功!", function(data) {
				   ctrl.alert("已生成<b style='color:red;'>"+data.status.msg+"</b>条数据,共<b style='color:red;'>"+data.size+"</b>条数据");
				});
		   });
			   
		}
		function setTime($html){
			$html.find("#exam_datagrid div.progress-warp").each(function(i,n){
				var exmaid=$(n).attr("examid");
				$.ajax({
					url:"batch/c?exmaid="+exmaid,
					type:"get",
					data:{},
					dataType:"json",
					asyncs:false,
					success:function(data){
						var status = data.status;
						if(status=="5"){
							self.query();
						}else{
							$(n).html("("+data.length+")");
						}
					}
				});
			});
		}
		function progress($html) {
			var interval = window.setInterval(function(){
				$html.find("#exam_datagrid div.progress-warp").each(function(i,n){
					var exmaid=$(n).attr("examid");
					$.ajax({
						url:"batch/c?exmaid="+exmaid,
						type:"get",
						data:{},
						dataType:"json",
						asyncs:true,
						success:function(data){
							var status = data.status;
							if(status=="5"){
								self.query();
								window.clearInterval(interval);
							}else{
								$(n).html("("+data.length+")");
							}
						}
					});
				});
			} , 10000);
		}
	    var o = self = {
	       container:undefined,
           render:function(container){   	  
        	   ctrl.getHtml(_listUrl,function(html){
        		   self.container = $(html);
        	       ctrl.appendToView(container,self.container);
				   renderPager();
				   analysisExam(self.container);
				   progress(self.container);
				   setTime(self.container);
        	   });
           },
           query:function(page){
        	   if(!page){
        		   page = getPager();
        	   }
        	   var url = "/batch/list/"+page.curSize+"/"+page.pageSize+"?examStatus=5&ui_all=true"
        	   ctrl.getHtml(url,function(data){
        		  refresh(data);
        		  progress($(data));
        		  setTime(self.container);
        	   });
           }     	   
       };
       return o;
	});
})();