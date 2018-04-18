/**
 * 
 */

(function(){
	"use strict";
	define(['jquery','ajax','jqueryPager',"dialog","controller","validatejs","common",'datepickerjs','selectjs'],
			function($,ajax,pager,dialog,ctrl){
		var _listUrl = ctrl.getCurMenu().attr('url');
		var gradeUrl = 'grade/list?ui_all=true&gradeName=';
		var examTypeUrl = 'examType/list?ui_all=true&examTypeName= ';
		var o,self,_$container;
		return function(){				
			o = self = {
				container:undefined,
				queryForm:undefined,
			    render:function(container){
					ctrl.log(_listUrl);
					_$container = container;
					ctrl.getHtml(_listUrl,function(html) {
	        		    self.container = $(html);
	        		    self.queryForm = self.container.find('div.query-form');
	        	        ctrl.appendToView(container,self.container);	        	     
	        	        init();	        	        
		            });
				},
				query:function(page){
	            	if(!page){
	         		   page = ctrl.newPage(this.container);
	         	    }
	         	   
	         	    var queryPath = formToQueryPath( );
	         	    var url = "exam/checkin/list/" + page.curSize + "/" + page.pageSize + "?hasExamStudent=true" + queryPath;
	         	    ctrl.getHtml(url,function(data){
	         	    	refresh(data);
	         	    });
	            },
				uncheckList:function(pk){					
    	        	ctrl.getHtmlDialog(("/exam/checkin/rule/" + pk),'rule-modal',function(dialog){
    	        		dialog.find('button.btn:eq(0)').click(function(e){
    	        			var specs = dialog.find('input:checked');
    	        			if(specs.size()){
    	        				var queryPath = "";
    	        			    specs.each(function(i,n){
    	        			    	queryPath += n.name + "=" + n.value +"&";
    	        			    });
    	        				ctrl.postJson("/exam/checkin/rule/" + pk + "?" + queryPath,{},"本次考试学生核对已启动，稍息可查看核对结果",function(){
    	        					self.query();
    	        				});
    	        				$("#rule-modal").modal('hide');
    	        			}else{
    	        				ctrl.moment("请选择核对规则","warning");
    	        			}
    	        		});
    	        		
    	        	});
				},
				checkedList:function(pk){					
    	        	renderCheckedin(pk);
				},
				checkingList:function(pk){
					renderCheckedin(pk);
				},
				checkin:function(specs){
					
				}
			};
			
			function init(){
				//getGrades();
				//getExamType();
				
    	        renderPager();
    	        bindEvent();
    	        ctrl.initUI(self.container);
    	        
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
			
			function renderCheckedin(pk){
				var page = ctrl.newPage();
	        	var url = "/exam/checkin/checked/"+ pk + "/" + page.curSize + "/" + page.pageSize ;
				ctrl.getHtml(url, function(html) {
					self.container = $(html);
					ctrl.appendToView(_$container,self.container);
					bindCheckEvent(self.container);
					ctrl.renderPager({
						"containerObj" : self.container,
						"pageObj" : self.container.find("#checkedPager"),
						"callBack" : checkedList
					});
	
				}, "", page.data);
			};
			
			function renderCheckedless(pk){
				var page = ctrl.newPage();
	        	var url = "/exam/checkin/checkedless/"+ pk + "/" + page.curSize + "/" + page.pageSize ;
				ctrl.getHtml(url, function(html) {
					self.container = $(html);
					ctrl.appendToView(_$container,self.container);
					bindCheckEvent(self.container);
					ctrl.renderPager({
						"containerObj" : self.container,
						"pageObj" : self.container.find("#checkedPager"),
						"callBack" : checkedlessList
					});
	
				}, "", page.data);
			};
			
			function bindCheckEvent($html){
				$html.on('click',"div.page-header button:eq(0)",function(){
					self.render(_$container);
				});
				
				$html.on('click',"ul.nav li:eq(0)",function(){
					var pk = self.container.find(':hidden[name=examId]').val();
					renderCheckedin(pk);
				});
				
				$html.on('click',"ul.nav li:eq(1)",function(){
					var pk = self.container.find(':hidden[name=examId]').val();
					renderCheckedless(pk);
				});
			};
			
			function checkedList(page){
				var pk = self.container.find(':hidden[name=examId]').val();
				var checkedUrl = "/exam/checkin/checked/"+ pk + "/" + page.curSize + "/" + page.pageSize ;
				reRenderCheckedPage(page,checkedUrl,checkedList);				
			};
			
			function checkedlessList(page){				
				var pk = self.container.find(':hidden[name=examId]').val();
				var checkedUrl = "/exam/checkin/checkedless/"+ pk + "/" + page.curSize + "/" + page.pageSize ;
				reRenderCheckedPage(page,checkedUrl,checkedlessList);
			};
			
			function reRenderCheckedPage(page,checkedUrl,pageCallback){
				if(!page){
					page = ctrl.newPage();
				}
				ctrl.getHtml(checkedUrl, function(html) {
					var $html = $(html);
					if(page.curSize ==1){
						self.container = $html;
					    ctrl.appendToView(_$container,self.container);					    
					}else{
						self.container.find('tbody').children().remove();
						self.container.find('tbody').append($html.find('tbody').children());
						self.container.find('table').next().children().remove();
						self.container.find('table').next().append($html.find('table').next().children());
					}
				    ctrl.renderPager({
						"containerObj" : self.container,
						"pageObj" : self.container.find("#checkedPager"),
						"callBack" : pageCallback
					});					
	
				}, "", page.data);
			};
			
			function getGrades(){
				ctrl.getJson(gradeUrl,function(data) {					
					var $grade = self.container.find('#checkingrade');
					if(data.grades){
						$.each(data.grades,function(i,n){
							$grade.append('<option value="'+n.id+'">' +n.name +'</option>');
						});
						$grade.selectpicker("refresh");
					}
				});
			};
			
			function getExamType(){
				ctrl.getJson(examTypeUrl,function(data) {
					var $checkinexamtype = self.container.find('#checkinexamtype');
					if(data.examTypes){
						$.each(data.examTypes,function(i,n){
							if(n.valid){
								$checkinexamtype.append('<option value="'+n.id+'">' +n.name +'</option>');
							}
						});
						$checkinexamtype.selectpicker("refresh");
					}
				});
			};
			
			function renderPager(){
				progress();
				ctrl.renderPager({
					"containerObj" : self.container,
					"pageObj" : self.container.find('#examCheckin_pager'),
					"callBack":self.query
				});	
			};
			
			function bindEvent(){
				self.container.on('click','a[trigger]',function(){
					var trigger = $(this).attr('trigger');
		    		var pk = $(this).attr("pk");
		    		self[trigger](pk);
				});
			};
			
			function refresh(data){
				ctrl.refreshDataGrid('examCheckin_data_rows',self.container,$(data),renderPager);
				progress();
			}
			
			function progress(){
				/**测试进度条**/
				self.container.find('#examCheckin_data_rows div.progress-warp').each(function(i,n){
					var $c = $(n);
					var pk = $c.attr('pk');
					ctrl.ajaxExecute('ExamCheckinService',{pk:pk},'html',function(){
						$c.parent().text('完成核对');
						$c.remove();
					},$c);
				});
			};
			
			function formToQueryPath(){
				var $inputs = self.queryForm.find('input,select');
				var queryPath = "";
				$inputs.each(function(i,n){
					//var $n = $(n);
					if( (n.name=='status' && n.value * 1 >= 0) )
					    queryPath += "&" + n.name + '='+n.value;
					else if(n.name != 'status'&& n.value.length > 0)
						queryPath += "&" + n.name + '='+n.value;
				});
				return queryPath;
			}
			return o;
		}();
	});
})();