/**
 * 
 */

(function(){
	"use strict";
	define(['jquery','ajax','jqueryPager',"dialog","controller",
	        '../../lib/jquery.treetable','common'],
			function($,ajax,pager,dialog,ctrl){
		var o,self;	
		var _listUrl = ctrl.getCurMenu().attr('url');
		var analyseUrl = "report/selfdefined/analyse";
		var table = '<table class="table table-bordered  table-hover" id="datagrid"><thead><tr></tr></thead><tbody></tbody></table>'
		var orgDimesions = {orgs:[{level:1,dimesionName:"省",dimesionType:"PROVINCE"},{level:2,dimesionName:"市",dimesionType:"CITY"},
		                          {level:3,dimesionName:"区/县",dimesionType:"COUNTY"},{level:4,dimesionName:"学校",dimesionType:"SCHOOL"},],
		                    getDimession:function (level){
		                    	for(var i=0;i<this.orgs.length;i++){
		                    		if(this.orgs[i].level == level)
		                    			return this.orgs[i];
		                    	}
		                    	return this.orgs[3];
		                    }};
		return function(){				
			o = self = {
				container:undefined,
				outerContainer:undefined,
			    render:function(container){
			    	if(!this.outerContainer)
			    		this.outerContainer = container;
			    	ctrl.getHtml(_listUrl,function(html){
		        		   self.container = $(html);
		        	       ctrl.appendToView(container,self.container);
		        	       init();
		        	});
				},
				analysis:function(){
					var examId = this.container.find('a.defined[data-tt-type=exam]').attr('data-tt-value');
    	        	
    	        	var AnalysisResource = {examId:examId,analysisTestpapers:[],formulas:[]};
    	        	buildTestPapers(AnalysisResource);
    	        	buildFormulas(AnalysisResource);

    				var dimesionGroupType = 1;
    				buildOrgTreeAnalysisFilterWhere(AnalysisResource,dimesionGroupType);//self.container.find('ul[data-tt-item=org] li>a.defined');
    				
    				buildFilterWhere(AnalysisResource,++dimesionGroupType,"WL","文理科");
    				buildFilterWhere(AnalysisResource,++dimesionGroupType,"GENDER","性别");
    				buildFilterWhere(AnalysisResource,++dimesionGroupType,"NATION","民族");
    				
    	
    	        	ctrl.postJson(analyseUrl +'/tpl/'+examId,AnalysisResource,false,function(data){
    	        		ctrl.log(data);
    	        		self.container.find('#datagrid').remove();
    	        		var dataGrid = $(table);
    	        		self.container.find('#gridContainer').append(dataGrid);
    	        		self.buildGrid(dataGrid);
    	        		var keyIndex = [];
    	        		var formulaindex = -1;
    	        		var paperindex = -1;
    	        		var orgIndex = -1;
    	        		var formula=[];
    	        		dataGrid.find('th').each(function(i,n){
    	        			var $n = $(n);
    	        			if($n.attr('data-tt-type') != 'testPapers' && $n.attr('data-tt-type') != 'Formula'  && $n.attr('data-tt-type') != 'org'){
    	        				keyIndex[keyIndex.length] = i;
    	        				return true;
    	        			}
    	        			if($n.attr('data-tt-type') == 'Formula'){
    	        				formula[formula.length] = {text:$(n).text(),index:i,toFixed:$(n).attr('fixed')};
    	        				if(formulaindex ==-1)
    	        					formulaindex = i;
    	        				return true;
    	        			}
    	        			
    	        			if(paperindex==-1 && $n.attr('data-tt-type') == 'testPapers')
    	        				paperindex = i;
    	        			
    	        			if(paperindex==-1 && $n.attr('data-tt-type') == 'org')
    	        				orgIndex = i;
    	        		});
    	        			        		
    	        		dataGrid.find('tbody tr').each(function(i,n){
    	        			var key = [];
    	        			var paper = "";
    	        			var $tr = $(this);
    	        			$tr.children().each(function(k,m){
    	        				var $m = $(m);
    	        				if(k == orgIndex){
    	        					//key[key.length] = $m.text();
    	        					buildOrgKey($tr,k,key); 
    	        					return true;
    	        				}
    	        				     
    	        				for(var a = 0;a<keyIndex.length;a++){
    	        					if(k == keyIndex[a]){
    	        						key[key.length] = $m.text();
    	        					}
    	        						
    	        				}
    	        				
    	        				if(k == paperindex)
    	        					paper = $(this).text();
    	        			});
  
    	        			key = key.join('.');
    	        			
    	        			$.each(formula,function(l){
    	        				var $ftd = $tr.find('td:eq('+this.index+')');
    	        				var o = data.result[key];
    	        				if(!o){
    	        					$ftd.text('--');
    	        					return true;
    	        				}
    	        					
        	        			var p = o[paper];
    	        				if(p)
    	        				  $ftd.text(p[this.text].toFixed(this.toFixed|0));
    	        				else
    	        					$ftd.text('--');
    	        			});
    	        			drawGrid(); 
    	        			
    	        		});

    	        	});
    	        					
				},
				buildGrid:function(){
					var $exam = this.container.find('div.form-tree ul[data-tt-item=EXAM] li a.defined');
					var $report = this.container.find('div.form-tree ul[data-tt-item=REPORT] li a.defined');
					this.container.find("#dataRow h3").text($exam.text()+"-" + $report.text());
					//var $grid = this.container.find('#dataRow #datagrid');
					//$grid.find('thead th').remove();
					//$grid.find('tbody tr').remove();
					buildOrgTree();
				}
			};
			
			function buildOrgKey($tr,index,key){
				//var paretnId = $tr.attr('data-tt-parent-id');
				//if(paretnId){
				//	var $ptr = $tr.parent().find('tr[data-tt-id='+paretnId+']:last');
				//	buildOrgKey($ptr,index,key);
				//}
				var $td = $tr.find('td:eq('+index+')');
				key[key.length] = $td.text().trim();
				//key.splice(0,0,$td.text());
				//return key;
			}
			
			function init(){
				self.container.find('div.page-header').css({'margin-bottom':'0px'});
    	        
				//单选分析项
				self.container.on('click','div.single-selected form li>a',function(){
    	        	var $a = $(this);
    	        	itemDefine($a,true);
    	        	if($a.attr('data-tt-type') === 'exam'){
    	        		_listUrl = _listUrl.replace(/(examId=)([^&]*)/gi,'examId='+$a.attr('data-tt-value'));
    	        		self.render(this.outerContainer);
    	        	}
    	        });
    	        
    	        //多选分析项
    	        self.container.on('click','div.multi-selected form li>a',function(){
    	        	var $a = $(this);
    	        	itemDefine($a);
    	        	showChildren($a);
    	        });
    	        
    	        //多选按钮
    	        self.container.on('click','button.multi-check',function(){
    	        	var $btn = $(this);
    	        	$btn.toggleClass('multi-checked');
    	        	if($btn.hasClass('multi-checked')){
    	        		$btn.find('>span').removeClass('glyphicon-plus').addClass('icon-ok');
    	        		$btn.parent().prev().find('a:visible:not(.defined)').click();
    	        	}else{
    	        		$btn.find('>span').removeClass('icon-ok').addClass('glyphicon-plus');
    	        		$btn.parent().prev().find('a.defined').click();
    	        	}
    	        	
    	        	return false;
    	        });
    	        
    	        //分析按钮
    	        self.container.on('click','button.analyse',function(){
    	        	self.analysis();
    	        	return false;
    	        });
    	        
    	        //drawGrid();
			};
			
			function buildOrgTree(){
				var selectedOrgs = self.container.find('ul[data-tt-item=org] li a.defined');
				var ths = {th:[],add:function(text,type,fixed){
					var b = false;
					for(var i=0;i<this.th.length;i++){
						if(this.th[i].text == text){
							b = true;
							break;
						}
					}
					if(!b)
						this.th[this.th.length]={text:text,type:type,fixed:fixed|0};
				}};
				var levels = {ls:[],add:function(l){
					var b = false;
					for(var i=0;i<this.ls.length;i++){
						if(this.ls[i] == l){
							b = true;
							break;
						}
					}
					if(!b)
						this.ls[this.ls.length] = l;
				}};
				
				//levels.add($("#rootLevel").val()*1);
				
				selectedOrgs.each(function(i,n){
					var $n = $(n);
					var level = $n.attr('data-tt-level');
					levels.add(level);
					ths.add(orgDimesions.getDimession(level*1).dimesionName,"org");
					return false;
				});
				var selectedWL = self.container.find('ul[data-tt-item=WL] li a.defined');
				if(selectedWL.size() > 0)
					ths.add("文理科","WL");

				var selectedTestPapers = self.container.find('ul[data-tt-item=testPapers] li a.defined');
				if(selectedTestPapers.size() > 0)
					ths.add("学科","testPapers");
				
				var selectedWL = self.container.find('ul[data-tt-item=GENDER] li a.defined');
				if(selectedWL.size() > 0)
					ths.add("性别","GENDER");
				
				var selectedWL = self.container.find('ul[data-tt-item=NATION] li a.defined');
				if(selectedWL.size() > 0)
					ths.add("民族","NATION");

				var selectedFormula = self.container.find('ul[data-tt-item=Formula] li a.defined');
				selectedFormula.each(function(i,n){
					var $n = $(n);
					ths.add($n.text(),"Formula",$n.attr('fixed'));
				});
				//ctrl.log(ths);
				var datagrid = self.container.find('#datagrid');
				$.each(ths.th,function(){
					datagrid.find('thead tr').append('<th data-tt-type="'+this.type+'" fixed="'+this.fixed+'">'+this.text+'</th>');
				});
				
				var level = levels.ls[0];//$("#rootLevel").val()*1;
				var rows = [];
				var rows = buildRows(level);
				$.each(rows,function(i,row){
					var $tr =  $('<tr>');
					$.each(row,function(k,column){
						if(column["id"]){
							$tr.attr('data-tt-id',column["id"]);
							if(column["parent"])
							    $tr.attr('data-tt-parent-id',column["parent"]);
						}
						$tr.append('<td>'+column+'</td>');
						
						
					});
					datagrid.find('tbody').append($tr);
				});
				
				
				//ctrl.log(rows);				
			};
			
			function buildRows(level){
				var thisRows = [];
				self.container.find('ul[data-tt-item=org] li a.defined[data-tt-level='+level+']').each(function(i,n){
					var $n = $(n);
					//var row = [];
					//row[0] = {text:$n.text(),id:$n.attr('data-tt-value'),toString:function(){return this.text;}};
					thisRows[thisRows.length] = {text:$n.text(),id:$n.attr('data-tt-value'),toString:function(){return this.text;}};
					var newRows = buildOrgColumn($n.attr('data-tt-value'));
					$.each(newRows,function(i,row){
						thisRows[thisRows.length] = row;
					});
					//rows[rows.length] = row;
				});
				thisRows = biuldColumn(thisRows,"WL");
				thisRows = biuldColumn(thisRows,"testPapers");
				thisRows = biuldColumn(thisRows,"GENDER");
				thisRows = biuldColumn(thisRows,"NATION");
				thisRows = biuldFormulaColumn(thisRows);
				return thisRows;
			};
			
			function buildOrgColumn(parent){
				var newRows = [];
				self.container.find('ul[data-tt-item=org] li a.defined[data-tt-parent='+parent+']').each(function(i,n){
				    var $n = $(n);
				    //var newRow = [];
				    newRows[newRows.length] = {text:$n.text(),id:$n.attr('data-tt-value'),parent:parent,toString:function(){return this.text;}};//$.extend([],row);
					//newRow[newRow.length] = $n.text();
					
					var myLevel = $n.attr('data-tt-value');
					if(myLevel){
						var myRows = buildOrgColumn(myLevel);
						if(myRows.length){
							$.each(myRows,function(){
								newRows[newRows.length] = this;
							});
						}else{
							//newRows[newRows.length] = newRow;
						}	
					}					
				});
				return newRows;
			};
			
			function biuldFormulaColumn(thisRows){
				//var newRows = [];
				$.each(thisRows,function(a,row){
					self.container.find('ul[data-tt-item=Formula] li a.defined').each(function(i,n){
						var $n = $(n);
						//var newRow = $.extend([],row);
						row[row.length] = "0.00";
					});					
				});
				return thisRows;
			};
			
			function biuldColumn(oldRows,item){
				var newRows = [];
				$.each(oldRows,function(a,row){
					self.container.find('ul[data-tt-item='+item+'] li a.defined').each(function(i,n){
						var $n = $(n);
						var newRow = [];
						//newRow = $.extend([],row);
						if($.isArray(row)){
							newRow = $.extend([],row);
						}else{
							newRow[0] = row;
						}
						
						newRow[newRow.length] = $n.text();
						newRows[newRows.length] = newRow;
						
					});					
				});
				return newRows.length>0?newRows:oldRows;
			};
			
			
			function drawGrid(){
				self.container.find('#datagrid').treetable({expandable:true});
			};
			
			function showChildren($a){
				var parentLevel = $a.attr('data-tt-level');
				var $childrenDiv = self.container.find('div.form-tree[data-tt-level-parent='+parentLevel+']');
				if($childrenDiv.size()<1)
					return;
				
				$childrenDiv = self.container.find('div.form-tree[data-tt-level-parent='+parentLevel+']:visible');

				var parentCode = $a.attr('data-tt-value');
				if($childrenDiv.size() < 1 ){
					$childrenDiv = self.container.find('div.form-tree[data-tt-parent='+parentCode+']');
					$childrenDiv.show();
					return;
				}
				
				
				if($a.hasClass('defined')){
					var $thisChildren =$childrenDiv.find('li[data-tt-parent='+parentCode+']');
					
					if($thisChildren.size() > 0){
						$thisChildren.show();
		        	}else{
		        		$thisChildren = self.container.find('div.form-tree[data-tt-parent='+parentCode+'] li');
		        		$childrenDiv.find('ul').append($thisChildren.clone(true));
		        	}	        		
	        	}else{
		        	$childrenDiv.find('li[data-tt-parent='+parentCode+']').hide().find('a.defined').click();
	        	}				
			};
			
			function itemDefine($a,onlyOne){
	        	if(!$a.hasClass('defined')){
	        		if(onlyOne){
	    	        	$a.parent().parent().find('a.defined').removeClass('defined');	    	        		        			
	        		}
	        		$a.addClass('defined');    	        		
	        	}else{
	        		if(!onlyOne)
	        		    $a.toggleClass('defined');
	        	}       	
			}
			
			function buildTestPapers(AnalysisResource){
				var testPapers = self.container.find('ul[data-tt-item=testPapers] li>a.defined');
				testPapers.each(function(i,n){
					var $paper = $(n);
					AnalysisResource.analysisTestpapers[i] = {id:$paper.attr('data-value'),name:$paper.text()};
				});
			};
			
			function buildFormulas(AnalysisResource){
				var formulas = self.container.find('ul[data-tt-item=Formula] li>a.defined');
				formulas.each(function(i,n){
					var $formula = $(n);
					AnalysisResource.formulas[i] = {"formulaType":$formula.attr('data-value'),name:$formula.text()};
				});				
			};
			

			function buildOrgTreeAnalysisFilterWhere(AnalysisResource,dimesionGroupType){
				
				var orgFilters = self.container.find('ul[data-tt-item=org] li>a.defined:visible');//[data-tt-parent='+parentCode+']
				if(orgFilters.size() < 1)
					return false;
				var dimessions = {s:[],
				addValue:function(dimesionType,value){
					for(var i=0;i<this.s.length;i++){
						if(this.s[i]["dimesionType"] == dimesionType){
							if(!this.s[i]["dimensionAttributeValues"]){
								this.s[i]["dimensionAttributeValues"]=[];
								this.s[i]["dimensionAttributeValues"][0] = value;
								break;
							}
							var l = this.s[i]["dimensionAttributeValues"].length;
							this.s[i]["dimensionAttributeValues"][l] = value;
							break;
						}
					}
				},addDimession:function(dimession){
					this.s[this.s.length] = dimession;
				},has:function(dimesion){
					var b = false;
					for(var i=0;i<this.s.length;i++){
						if(this.s[i]["dimesionType"] == dimesion.dimesionType){
							b = true;
							break;
						}
					}
					return b;
				}};

				orgFilters.each(function(i,n){
					var org = $(n);
					//if(i == 0){
					var level = org.attr('data-tt-level');
					var orgDimesion = orgDimesions.getDimession(level*1);
					var dimession = {};
					$.extend(true,dimession,orgDimesion);
					delete dimession["level"];
					dimession["dimesionGroupType"] = dimesionGroupType;
					if(!dimessions.has(dimession)){
						dimessions.addDimession(dimession);
					}
					dimessions.addValue(dimession.dimesionType,org.text());
					
				});
				for(var i = 0;i<dimessions.s.length;i++){
					addDimession(AnalysisResource,dimessions.s[i]);
				}
				
			};
			
			function buildFilterWhere(AnalysisResource,dimesionGroupType,dimesionType,dimesionName){
				var filters = self.container.find('ul[data-tt-item='+dimesionType+'] li>a.defined');//[data-tt-parent='+parentCode+']
				if(filters.size() < 1)
					return false;
				
				var dimession = {dimesionGroupType:dimesionGroupType,dimesionName:dimesionName,dimesionType:dimesionType,dimensionAttributeValues:[]};
				filters.each(function(i,n){
					dimession["dimensionAttributeValues"][i] = $(n).text();
				});
				
				addDimession(AnalysisResource,dimession);
			};
			
			function addDimession(AnalysisResource,dimession){
				var filter = AnalysisResource["analysisFilterWhere"];
				if(!filter){
				    AnalysisResource["analysisFilterWhere"] = dimession;			    
				}else{					
					addDimessionNext(filter,dimession);

			    }
			}
			
			function addDimessionNext(dimession,nextDimession){
				if(dimession["nextAnalysisFilterWhere"]){
					addDimessionNext(dimession["nextAnalysisFilterWhere"],nextDimession);
				}else{
					dimession["nextAnalysisFilterWhere"]=nextDimession;
				}
			}	
			
			return o;
		}();
	});
})();