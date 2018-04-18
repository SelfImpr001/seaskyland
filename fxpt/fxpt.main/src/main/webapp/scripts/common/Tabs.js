/**
 * <pre>
 * 标签页组件(Tabs)
 * </pre>
 * <b>© 2011-2014 版权所有 深圳市海云天科技股份有限公司</b>.
 * Dual licensed under the MIT and GPL licenses.
 * http://docs.jquery.com/License
 * @author 李贵庆
 * @date 2014-9-18
 * @version 1.0.0
 */
(function(){
	"use strict";
	var __tabIdPrefix = "tab_";
	var __settings = {
		autoRefresh : false,
		closer:true,
		headerExpr : '.stand-box',
		headerWidth : '12%',
		bodyExpr : '.page-box .page-content:eq(0)'
	};

	var __hideBarSettings = {
		expr : '.park-box',
		tabWidth : 190,
		tabHeight : 30
	};
	
	var __tabHeaderHtml = '<li class="item stand-item">\
							 <a class="txt" href="javascript:void(0);"></a>\
							 <button type="button" class="close " data-dismiss="modal" aria-hidden="true" style="color:#fff">&times;</button>\
						   </li>';
	var __gopts = {
			allCloser:false,  //是否所有的tab都可以关闭，默认是保持一个不关闭，当该参数为false时，创建的首个tab为不能关闭者
			autoCloser:true   //是否自动调整关闭按钮的显示或者隐藏；当allCloser=true时，本参数无效
		};
	define(['jquery','logger'],function($,log){
		return function(container,gopts){		
			var tabOpts = $.extend(true,{},__gopts,gopts||{});
			
			//tab对象，一个tab包含有标签页及内容
	    	function Tab(opts){
	    		var self = this;
	    		var title = (opts.title|| '');
	    		var entry = (opts.entry|| '');
	    		this.id = __tabIdPrefix + opts.id;
	    		this.header = $(__tabHeaderHtml);
	    		this.header.attr('id',this.id);
	    		this.header.attr('title',title);
	    		this.header.attr("entry",entry);
	    		if(title.length > 10){
	    			title = title.substring(0,8) + "";
	    		}
	    		this.header.find('>li').css('width',(opts.headerWidth || '20%'));
	    		this.header.find('a').text(title);
	            this.headerCommands = [];
	            this.closerCommands = [];
	    		this.header.click(function(e){
	    			$.each(self.headerCommands,function(i,c){
	    				c.call(self);
	    			});
	    			e.stopPropagation();
	    		});
	            
				this.closer = this.header.find('.close');
				if(!opts.closer){
					this.canNotBeClosed();
				}			    			
				this.body = $('<div class="page-content tab-body" ></div>');
				this.body.attr('id',this.id + "_c");
				this.closer.click(function(e){												
					self.destroy(function(){
						$.each(self.closerCommands,function(i,c){
					
		    				c.call(self);
		    			});	
					});
					e.stopPropagation();
				});
				
				this.prev = undefined;
				this.next = undefined;
				
				this.state = false;
				
	    	};
	    	Tab.prototype.toString = function(){return this.id;};
	    	Tab.prototype.actived = function(){
	    		this.state = true;
	    		this.header.addClass('active');
	    		this.body.show();
	    	};
	    	Tab.prototype.inactive = function(){
	    		this.header.removeClass('active');
	    		this.body.hide();
	    		this.state = false;
	    	};
	    	Tab.prototype.isActived = function(){
	    		return this.state;//this.header.hasClass('active');
	    	};
	    	Tab.prototype.destroy = function(fn,animated){
	    		if(animated==undefined || animated==true){
	    			this.header.animate({width:0,opacity:0},function(){
						$(this).empty().remove();
						if($.isFunction(fn)){
							fn.call(this);
						}
					});
	    		}else{
	    			this.header.empty().remove();
	    		}
	    		
	    		this.body.empty().remove();	    		
	    	};

	    	Tab.prototype.addTabClickListener = function(fn){
	    		this.headerCommands.push(fn);
	    	};
	    	
	    	Tab.prototype.addCloserClickListener = function(fn){
	    		this.closerCommands.push(fn);
	    	};
	    	
	    	Tab.prototype.canNotBeClosed = function(){
	    		this.header.addClass("nocloser");
	    		this.closer.hide();
	    	};
	    	
	    	Tab.prototype.canBeClosed = function(){
	    		this.header.removeClass("nocloser");
	    		this.closer.show();
	    	};
	    	
	    	//tab关系链，用于维护每个tab间的关系
	    	var tabChain = {
	    		tabs:[],
	    		newTab:function(opts){
	        	    //默认第一个tab没有关闭按钮
	        	    if(tabOpts.allCloser){
	        		    opts['closer'] = true;
	        	    } else if(!tabOpts.allCloser && this.tabs.length == 0 ){
	        		    opts['closer'] = false;
	        	    }
	    			var tab = new Tab(opts);
		        	this.put(tab);
		        	   
	        	    if(!tabOpts.allCloser && tabOpts.autoCloser){
	        	    	if(this.tabs.length == 1)
	        	    		this.tabs[0].canNotBeClosed();
	        	    	else{
        	    			$.each(this.tabs,function(){
    	        	    		this.canBeClosed();
    	        	    	});
	        	    	}
	        	    }    			
		        	return tab;
	    		},
	    		get:function(id){
	    			var t = undefined;
	    			$.each(this.tabs,function(i,n){
	    				if(n.id == id){
	    					t = n;
	    					return false;
	    				}
	    			});
	    			return t;
	    		},
	    		put:function(tab){
	    			if(!this.get(tab.id)){
	    				var prev = this.tabs[this.tabs.length-1];
	    				if(prev){
	    					prev.next = tab;
	    					tab.prev = prev;
	    				}
	    				this.tabs[this.tabs.length] = tab;
	    			}   			
	    		},
	    		remove:function(tab){

	    			var my = this;
	    			$.each(this.tabs,function(i,n){
	    				if(tab.id == n.id){
	    		    		if(tab.prev){
	    		    			if(tab.isActived())
	    		    			    tab.prev.actived();
	    		    			if(tab.next){
	    		    				tab.prev.next = tab.next;
	    		    				tab.next.prev = tab.prev;
	    		    			}else{
	    		    				tab.prev.next = undefined;
	    		    			}
	    		    		}else if(tab.next){
	    		    			if(tab.isActived())
	    		    			    tab.next.actived();
	    		    			tab.next.prev = undefined;
	    		    		}   		    		
	    					tab.prev = undefined;
	    					tab.next = undefined;
	    					tab.destroy(undefined,false);
	    					my.tabs.splice(i,1);
	    					return false;
	    				}
	    			});
	    			if(!tabOpts.allCloser && this.tabs.length == 1){
	    				this.tabs[0].canNotBeClosed() ;
	    			}
	    			
	    		},
	    		getActived:function(){
	    			var activeTab = this.tabs[0];
	    			$.each(this.tabs,function(){
	    				if(this.isActived()){
	    					activeTab = this;
	    					return false;
	    				}
	    			});
	    			return activeTab;
	    		},
	    		removeAll:function(){
	    			while(this.tabs.length !=0){
	    				this.remove(this.tabs[0]);
	    			}
	    		}
	    	};
	    	
	    	//tab页面视图控制
	    	var tabView = {
	    		visibleBar : undefined,  //可见区
	    	    hideBar : undefined,     //隐藏区
	    	    moreBtn :undefined,
	    	    panel : undefined,       //整个tabs的容器
	    	    init : function(panel){
	    	    	this.panel = panel;
	    	    	this.visibleBar = this.panel.find(__settings.headerExpr);
	    	    	this.hideBar = this.panel.find(__hideBarSettings.expr);
	    	    	this.moreBtn = this.panel.find('.more');
	    	    	this.moreBtn.hide();
	    	    	var hideBar = this.hideBar;
	    	    	this.moreBtn.mouseenter(function(){
	    	    		hideBar.slideDown(100);
	    			});
	    	    	$('.label-box').mouseleave(function(e){
	    	    		hideBar.slideUp(100);	    					
	    			});
	    	    },
	    	    show : function(tab){
	    	    	//最大可见的tab数量
	        	   var maxVisibleTabs = Math.floor(100/__settings.headerWidth.slice(0,-1));       	  
	        	   //当前可见的tab数量
	        	   var visibledTabs = this.visibleBar.children().size();
	        	   this.pushToVisibleBar(tab);
	        	   //当新加入标签页后超过了VisibleBar的最大容量，移动VisibleBar最后一个tab进入hideBar
	        	   if((maxVisibleTabs - visibledTabs) < 1){
	        		   this.moveToHideBar();	        		 
	    		   }
	    	    },
	    	    pushToVisibleBar:function(tab){
	    	    	var nocloseTab = this.visibleBar.find('li.nocloser').last();
		            if(nocloseTab.size() > 0 )
	    	    	    tab.header.insertAfter(nocloseTab);
		            else
	    	    	    this.visibleBar.prepend(tab.header);
		        	tab.header.css({'height':'100%',width:0}).animate({width:__settings.headerWidth});
		        	tab.body.insertBefore(this.panel.find(__settings.bodyExpr));
	    	    },
	    	    moveToHideBar:function(){
	    	    	this.moreBtn.show();
	    	    	this.moreBtn.css('opacity',1);
	    			this.hideBar.css('opacity',1);	    			   
	    			this.visibleBar.children().last().prependTo(this.hideBar)
	    			     .css({height:__hideBarSettings.tabHeight,width:__hideBarSettings.tabWidth});
	    			 
	    	    },
	    	    moveToVisibleBar:function(tab){
	    	    	
	    	    	var hideTabs = this.hideBar.find('#'+tab.id);
	    	    	if(hideTabs.size()){
	    	    		this.visibleBar.children().last().prependTo(this.hideBar)
	    	    		    .css({height:__hideBarSettings.tabHeight,width:__hideBarSettings.tabWidth});
	    	    		var nocloseTab = this.visibleBar.find('li.nocloser').last();
			            if(nocloseTab.size() > 0 ){
			            	tab.header.insertAfter(nocloseTab).css({'height':'100%',width:0}).animate({width:__settings.headerWidth});
			            }else{
			            	tab.header.prependTo(this.visibleBar).css({'height':'100%',width:0}).animate({width:__settings.headerWidth});
	    	    	    }
	    	    	}
	    	    },
	    	    reDrawHideBar:function(){
		            var maxVisibleTabs = Math.floor(100/__settings.headerWidth.slice(0,-1));       	  
		        	var visibledTabs = this.visibleBar.children().size();
		        	var left = maxVisibleTabs - visibledTabs;
		        	if(left > 0){
		                while(left > 0){
		                	this.hideBar.children().first().appendTo(this.visibleBar).css({'height':'100%',width:0})
		                	    .animate({width:__settings.headerWidth});
		                	left--;
		                }        		 
		    		}
	    	    	
		        	if(this.hideBar.children().size()==0){		 
	    	    		this.moreBtn.css('opacity',0);
		    			this.hideBar.css('opacity',0);
		    			this.moreBtn.hide();
	    	    	}
	    	    },
	    	    destroy:function(){
	    	    	this.panel.empty();
	    	    }
	    	};
	    	
	        var tabs,self;
	        tabs = self = {
	           container:undefined,
	           create:function(container){   	  
	        	   this.container = $(container);
	        	   tabView.init(this.container);
	           },
	           destroy:function(){
	        	   tabView.destroy();
	        	   tabChain.removeAll();
	           },
	           newTab:function(opts,fnBodyRender){
	        	   if(!opts.id)
	        		   return;
	        	   var tabId = __tabIdPrefix + opts.id;
	        	   var b = this.switchTab(tabId,true);
	        	   
	        	   if(b)
	        		   return;

	        	   var tab = tabChain.newTab($.extend(true,{},__settings,opts));
	        	   tabView.show(tab);	        	   
	        	   tab.addTabClickListener(function(){
	        		   
	        		   self.switchTab(tabId,true);
	        	   });
	        	   tab.addCloserClickListener(function(){
	        		   tabChain.remove(tab);
	        		   tabView.reDrawHideBar();
	        	   });
	        	   
	        	   this.switchTab(tabId,false);
	        	  if(fnBodyRender && $.isFunction(fnBodyRender)){
	        		   fnBodyRender.call(this,tab.body);
	        	   }else{
	        		   tab.body.html('<div>No Data!</div>');
	        	   }
	           },
	           switchTab:function(id,status){
	        	   var tab = tabChain.get(id);
	        	   if(tab){
	        		   $.each(tabChain.tabs,function(i,n){
	        			   n.inactive();
	        		   });
	        		   tab.actived();
	        		   tabView.moveToVisibleBar(tab);
	        		   if(status)
	        		   if(tab.header.attr("entry")!=""&&tab.header.attr("entry")!=null&&tab.header.attr("entry")!=undefined)
						require([tab.header.attr("entry")], function(m) {
							if (m.render) {
								m.render.call(m,tab.body);
							}
						});
					
	        		   

	        		   return true;
	        	   }
	        	   
	        	   return false;
	           },
	           removeTab:function(id){
	        	   tabChain.remove(id);
	           },
	           closeAll:function(){
	        	   tabChain.removeAll();
	           },
	           getActivedTab:function(){
	        	   return tabChain.getActived();
	           }
	       };
	   
	       tabs.create(container);
		   return tabs;
	   };
    });
})();