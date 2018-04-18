(function() {
	"use strict";
				define([ "jquery", "jqueryPager", "controller", "formToJosn", "dialog",
							"datepickerjs", "selectjs", "../etl/etlExecutor","radioztree" ], function($,
							$page, ctrl, formToJosn, dialog, datepickerjs, selectjs,
							etlExecutor,radioztree) {
	    var self = undefined;
	    function getPager(){
	    	var pager = ctrl.newPage(self.container);
    		return pager;
	    };
	    
	    function bindEvent(container){
	    	var $obj = self.container;
	    	ctrl.initUI($obj); 
	    
	    	$obj.on('click','#dowload_datagrid tbody div.btn-group a',function(){
	    		eventTrigge($(this),container);
	    	});
	    	
	    };
	    
	    function eventTrigge($trigger,container){
	    	var trigger = $trigger.attr('trigger');
    		var pk = $trigger.attr("pk");
    		switch(trigger){
    		case 'dowload' :   			
    			dowload(container,pk);
    			break;
    		
    		default:
    			return;
    		}
	    };
	    function dowload(container,pk){
	    	var cityname =	container.find("#citys").find(":selected").attr("orgname");
			var countyname = container.find("#countys").find(":selected").attr("orgname");
			var schoolname = container.find("#schools").find(":selected").attr("orgname");
			var clazzsname = container.find("#clazzs").find(":selected").attr("orgname");
			var examId = container.find("#examStatus").val();
			var orgname= "";
			
			var wl = (pk==2||pk==4)?"理科":"文科";
			
			if(schoolname!="全部"){
				orgname	= schoolname;
			}else if(countyname!="全部"){
				orgname	= countyname;
			} else if(cityname!="全部"){
				orgname	= cityname;
			} 
			
			if(clazzsname=="全部"){
				clazzsname	= "";
			} 
			//判断文理
			var filename = result[pk-1]+"_"+rendResult(container);
			if(examId==""){
				ctrl.alert("请选择考试","warning");
				return;
			}
			var url = window.app.rootPath + "download/down?examid="+examId+"&orgname="+orgname+"&wl="+wl+"&type="+pk+"&filename="+filename+"&clazzsname="+clazzsname;
			$.download(url);
			
	    }
	  
	    
	    function rendResult(container){
			var cityname =	container.find("#citys").find(":selected").attr("orgname");
			var countyname = container.find("#countys").find(":selected").attr("orgname");
			var schoolname = container.find("#schools").find(":selected").attr("orgname");
			var clazzsname = container.find("#clazzs").find(":selected").attr("orgname");
			
			var lastText=(cityname=="全部"?cityname+"市":cityname)+"_"
					+(countyname=="全部"?countyname+"区":countyname)+"_"
					+(schoolname=="全部"?schoolname+"学校":schoolname)+"_"
					+(clazzsname=="全部"?clazzsname+"班":clazzsname);
			container.find("#dowload_data_rows").html("");
			
			return lastText;
		}
	    
	  
	    function orgChecked(container){
	    	container.on("change","#citys",function(){
	    		if($(this).val()!=""){
	    			ctrl.getJson("org/tree/children?pk="+$(this).val() ,function(htmlStr) {
	    				var child = $.parseJSON(htmlStr.orgJson);
	    				addbos(container,"coountys",child)
	    			});
	    			
	    		}
	    		addbos(container,"schools","");
	    		addbos(container,"clazzs","");
	    		rendResult(container);
	    	}).on("change","#countys",function(){
	    		if($(this).val()!=""){
	    			ctrl.getJson("org/tree/children?pk="+$(this).val() ,function(htmlStr) {
	    				var child = $.parseJSON(htmlStr.orgJson);
	    				addbos(container,"schools",child)
	    			});
	    		}
	    		addbos(container,"schools","");
	    		addbos(container,"clazzs","");
	    		rendResult(container);
	    		
	    	}).on("change","#schools",function(){
	    		if($(this).val()!=""){
	    			var examId = container.find("#examStatus").val();
	    			if(examId==""){
	    				ctrl.alert("选择考试后才能选择班级","warning");
	    				addbos(container,"classz","");
	    	    		rendResult(container);
	    				return;
	    			}
	    			ctrl.getJson("dowload/tree/children?pk="+$(this).val()+"&examid="+examId,function(htmlStr) {
	    				
	    				var child = eval(htmlStr.orgJson);
	    				addbos(container,"clazzs",htmlStr.orgJson)
	    			});
	    			
	    		}
	    	
	    		rendResult(container);
	    		
	    	}).on("change","#clazzs",function(){
	    		rendResult(container);
	    		
	    	}).on("change","#examStatus",function(){
	    		var clazz = container.find("#clazzs").val();
				var clazzsname = container.find("#clazzs").find(":selected").attr("orgname");
	    		if(clazzsname!="全部"){
	    			ctrl.getJson("dowload/tree/children?pk="+clazz+"&examid="+$(this).val(),function(htmlStr) {
	    				
	    				var child = eval(htmlStr.orgJson);
	    				addbos(container,"clazzs",htmlStr.orgJson)
	    			});
	    		}
	    		rendResult(container);
	    	})
	    	
	    }
	    
	    function addbos(container,name,resule){
	    	$("#"+name).html("");
	    	container.find("#"+name).append('<option value="" orgname="全部">全部</option>')
	    	$.each(resule,function(index,obj){
	    		container.find("#"+name).append('<option value="'+obj['pk']+'" orgname="'+obj['name']+'" >'+obj['name']+'</option>');
	    	})
	    	container.find("#"+name).selectpicker("refresh");
	    	container.find("#"+name).selectpicker("render");
	    }
	    
	    
	    
	    function renderPager() {	       	
	    	ctrl.renderPager({
				"containerObj" : self.container,
				"pageObj" : self.container.find('#user_pager'),
				"callBack":self.query
			});	    	
		}
	    
	   
		function refresh(data){
			ctrl.refreshDataGrid('user_data_rows',self.container,$(data),renderPager);			
		};
		var result = ["过滤人员名单_文科","过滤人员名单_理科","小语种考生列表_文科","小语种考生列表_理科"];
		
		function rendResult(container){
			var cityname =	container.find("#citys").find(":selected").attr("orgname");
			var countyname = container.find("#countys").find(":selected").attr("orgname");
			var schoolname = container.find("#schools").find(":selected").attr("orgname");
			var clazzsname = container.find("#clazzs").find(":selected").attr("orgname");
			
			
			var lastText=(cityname=="全部"?cityname+"市":cityname)+"_"
					+(countyname=="全部"?countyname+"区":countyname)+"_"
					+(schoolname=="全部"?schoolname+"学校":schoolname)+"_"
					+(clazzsname=="全部"?clazzsname+"班":clazzsname);
			container.find("#dowload_data_rows").html("");
			var pk=1;
			for ( var index in result) {
				
				container.find("#dowload_data_rows").append ( '<tr><td>'+pk+'</td><td>'+result[index]+"_"+lastText+'</td><td><div class="btn-group">'
					+'<a href="javascript:void(0);" pk="'+pk+'" trigger="dowload" class="icon-download-alt text-success" }"" title="下载"></a>'
					+'</div></td></tr>');
				pk++;
			}
			
			return lastText;
		}
		
		var page=ctrl.newPage()
		var _listUrl = "/dowload/list/" + page.curSize + "/" + page.pageSize+"?ui_all=true";
		//var _listUrl = ctrl.getCurMenu().attr('url');
		var pages = ctrl.newPage();
	    var o = self = {
	       container:undefined,
	       setPage:function(index){
	    	   pages["curSize"] = index; 
	    	   _listUrl = "dowload/list/"+index+"/15?ui_all=true"; 
	       },
           render:function(container,data){
        	   ctrl.getHtml(_listUrl,function(html){
        		   self.container = $(html);
        		   ctrl.appendToView(container,self.container);
				   bindEvent(container);
				   orgChecked(container);
				   renderPager(container);
				   rendResult(container);
				   if(data!=undefined){
	        		   if(data.q!=undefined && data.q!=""){
	        			   self.container.find('#user_datagrid >div.query-form input:text').val(data.q);
	        		   }
	        	   }
        	   });
           }, query:function(page,data){
        	   if(!page){
        		   page = getPager();
        	   }
        	   //self.container.parent().attr("indexPage",page.curSize);
           },
           create:function(){
        	   this.view(-1);
           },
           view:function(pk){
        	   ctrl.getHtmlDialog(("user/view/" + pk),"big-modal",function(dialog){
        		   
        	   },function(){
        		   orgTree.destroy();
        	   });        	   
           },
           update:function(pk){

        	   if(pk > 0)
        	     this.view(pk);     	   
           },
           save:function(user,subjectIds,method){
        	   
           },
           remove:function(pk,userName){
        	   
           },
           updatePassword:function(pk){
        	   
           }
       };
       return o;
	});
    
})();