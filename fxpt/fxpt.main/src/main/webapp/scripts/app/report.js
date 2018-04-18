 
(function() {
	"use strict";
	define(['ajax','app/user/user','basefn','Tabs','controller',"dialog",'scrollbar' ], function(ajax,user,basefn,Tabs,ctrl,dialog) {
		var biurl = $("#menu a:eq(0)").attr("url");
		var examId = $("#menu a:eq(0)").attr("entry");
		var tabs = new Tabs('div.main-content');
		var documentWidth = $(document).width();
		var botomRight = $('.main-content').width();
		var Crumbs;
		var blood;
		var bb;
		var cc;
		var reportForm = {
			form:undefined,
			create:function(c,iframeId){
				var biCookie= $("#biCookie").val();
				
				var html = '<form  target="'+iframeId+'" action="'+biurl+'" method="POST">'
				            + '<input type="hidden" name="resid" value=""><input type="hidden" name="paramsInfo" value="">'
				            + '<input type="hidden" name="refresh" value="true">'
				            + '<input type="hidden" name="hidetoolbaritems" value="REFRESH SAVE SAVEAS MYFAVORITE VIEW SELECTFIELD CHARTSETTING PARAMSETTING SUBTOTAL REPORTSETTING">'
				            + '<input type="hidden" name="smartbiCookie" value="'+biCookie+'"></form>';
				this.form = $(html);
				c.append(this.form);
				c.append("<iframe id='"+iframeId+"' src='' name='"+iframeId+"'  width='100%' height='100%' style='margin:0;padding:0;' frameborder='0'></iframe>");
			},
		    setResid:function(resid){
		    	this.form.find('input[name=resid]').val(resid);
		    },
		    setParamsInfo:function(paramsInfo){
		    	this.form.find('input[name=paramsInfo]').val(paramsInfo);
		    },
		    addParam:function(paramName,paramValue){
		    	if(!this.form.find('input[name='+paramName+']').size()){
		    		this.form.append('<input type="hidden" name="'+paramName+'" value="'+paramValue+'">');
		    	}else{
		    		this.form.find('input[name='+paramName+']').val(paramValue);
		    	}
		    },
		    submit:function(){
		    	this.form.submit();
		    }
		};
		
		function showReport(container,reportTmpId){
			reportForm.create(container,reportTmpId+"_"+"reportFrame");
			reportForm.setParamsInfo(JSON.stringify(dataPermission()));
			reportForm.setResid(reportTmpId);
			reportForm.submit();
		}
		
		function showPentahoReport(container,reportUrl, reportUuid){
			container.append("<iframe id='"+reportUuid+"' src='"+reportUrl+"' name='"+reportUuid+"'  width='100%' height='100%' style='margin:0;padding:0;' frameborder='0'></iframe>");
		}

		
		/**
		 * 通过数据权限构造访问参数
		 * 参数构造原理请参考《SmartBI系统集成开发指南》
		 */
		function dataPermission(){
			
			var $dp = $('#datapermision');
			var $ds = $dp.children();
			var params = [];
			params[params.length] = {
					id:'PARAM.数据分析与发布平台V2.考试名称',
					name:"考试名称",
					value:examId,
					displayValue:examName	
				};
			//var examId = $('#examId').val();
			var examName = $('#examName').val();
			//查看报表时用户当前用户的机构代码---------------------
			var orgCode = $(":hidden[name=orgCode]:eq(0)").val();
			var userCode = {
				name:"用户代码",
				value:orgCode,
				displayValue:orgCode	
			};
			params.push(userCode);
			
			var userName = $(":hidden[id=userName]:eq(0)").val();
			var userNames = {
				name:"用户名",
				value:userName,
				displayValue:userName	
			};
			params.push(userNames);
			var userId = $("#userId").val();
			var userIds = {
				name:"用户ID",
				value:userId,
				displayValue:userId	
			};			
			params.push(userIds);
			
			//----------------------------------------------------
			//查看报表时用户当前用户角色（如：county、school、student.）---------------------
			var roleType = $(":hidden[name=roleType]:eq(0)").val();
			var userRole = {
				name:"用户角色",
				value:roleType,
				displayValue:roleType	
			};
			params.push(userRole);
			//----------------------------------------------------
		
			if($ds.size()){
				$ds.each(function(i,n){
					var $this = $(this);
					var $dd = $this.children();
					if($dd.size()){
						var param = {
							id: $this.attr("pid"),
							name : $this.attr("name"),
							value:"",
							displayValue:"",
							stanbyValue:[]
						};
						$dd.each(function(j,m){
							var $li = $(this);
							
							if(j == 0){
								param.value = $li.attr("url");
								param.displayValue = $li.attr("name");
								var item = [];
								item[0] = $li.attr("url");
								item[1] = $li.attr("name");
								param.stanbyValue[j] = item;
							}else{
								var item = [];
								item[0] = $li.attr("url");
								item[1] = $li.attr("name");
								param.stanbyValue[j] = item;
							}
						});
						
						params[params.length] = param;
					}
				});
			}
			//------------------------------------
			return params;
		}
        var  $sidebar = $('#sidebar');
		function setScroll(){
        	$sidebar.mCustomScrollbar({	
				theme:"light-3",
				scrollInertia: 0
			});
        	$('#m_0').children('b').remove();
			setHeight();
			$(window).resize(setHeight);
			function setHeight(){
				$('.page-box').height(basefn.cH() - basefn.getTop($('.page-box:visible')[0] ));
			}
		}
		
		 //tab右键事件 start..........
        $(".stand-box,.mouseMenu").bind("contextmenu",function(){//阻止右键
	        	return false;
	        });
        $("#menu").find("a:not('.dropdown-toggle')").click(function(){//新建标签页触发事件
        	mousemenu();
        	
        });

        function mousemenu(){//标签页右键触发事件打开右键菜单
        	
        	 $(".stand-box li").off("mousedown");	 
        	 $(".stand-box li").on("mousedown",function(e){
		        	if(e.which === 3){
		        		if($(".stand-box li").length >1){
		        			 $($(".stand-box li").eq($(this).index()).siblings().find("button")).click();
		   	    		  $(".park-box li").find("button").click();		
		        		}
		        	
		        	}
        });
        };
        	mousemenu()
       
        //tab右键事件 end..........
        
        //换肤 start.....
       $(".skin_change li ").click(function(){
    	   var skin_color = $(this).text();
    	   switch(skin_color){

    	   case "默认" : $("body").attr("class"," "); break;
    	   case "颜色1" : $("body").attr("class","skin_color_dark"); break;
    	   case "颜色2" : $("body").attr("class","skin_color"); break;
    	   default:$("body").attr("class"," "); break;
    	   }
       });
       //换肤 end.....

//		拖拽菜单栏
        function drag(){
	        var clickX,line;
	        var labBtn = $("#main-container").find('#line');
	        var wrapWidth = $("#main-container").width();
	        var div1 =$("#sidebar");
	        var div2 =$("#content-right");
	        var open = true;
	        labBtn.mousedown(function(){
	            line= $(this).index('#line');
	            $(document).on("mousemove",function(e){
	                clickX = e.pageX;
	                //拖动按钮左边不出界
	                    if(clickX > 0) {
	                        labBtn.css('left', clickX + 'px');//按钮移动
	                        div1.width( clickX + 'px');

	                        div2.css("margin-left", clickX + 'px');
	                        return false;
	                    }
	                    if(clickX > wrapWidth) { 
	                        //按钮右边不出界
	                        labBtn.css('left',wrapWidth+ 'px'); //按钮最右边
	                        div1.width( wrapWidth + 'px' );//第一个div最右边
	                        div2.width( '0px' ); //第二个div为0
	                    }
	                });
	        });


	        $(document).mouseup(function(e) {
	            $(document).off("mousemove");
	        });
	        $('#sidebar-collapse').click(function(){
	        	$('.label-box').fadeToggle("normal","linear");
	        })
	        $('#eee, #sidebar-suo2').click(function(){
				if(open){
					$('#sidebar').hide('slow');
//					$('#eee').addClass('sidebar-suo2');
					$('#eee i').addClass('icon-circle-arrow-right');
					$('.main-content').animate({'margin-left':'0'},'slow');
					open = false;
				}else{
					$('#sidebar').show('slow');
//					$('#eee').removeClass('sidebar-suo2');
					$('#eee i').removeClass('icon-circle-arrow-right');
					if(clickX){
						$('.main-content').animate({'margin-left':clickX},'slow');
					}else{
						$('.main-content').animate({'margin-left':'220px'},'slow');
					}
					open = true;
				}
			});
        }
		function showFirstReport(menu){
			menu.click();
			if(menu.next().size()){
				showFirstReport(menu.next().find('li:eq(0) a:eq(0)'));
				
			}else{
				
			}
		};

		function createMenu(){
			var $menu = $('#menu');
			var ms = [];
			$menu.find('a').each(function(i,n){
				var $obj = $(n);
				ms[i] = {
					oLink:$obj,
					fnShow: function(){
//						this.oLink.parent().find('> .submenu').find('.submenu').css({"background":"#fbfbfb"});
						this.oLink.parent().find('> .submenu').stop(true).slideToggle('normal');
						this.oLink.parent().siblings().find('> .submenu').stop(true).slideUp('normal');
						this.oLink.find('b').stop(true).toggleClass('icon-angle-down');
						this.oLink.find('b').parent().parent().siblings().find('b').removeClass('icon-angle-down');
					},
					isLeaf:function(){
						return this.oLink.parent().find('> .submenu').size() == 0;
					},
					isRoot:function(){
						return this.oLink.attr('entry') * 1 > 0;
					},
					resid:function(){
						if(this.isLeaf()){
							return this.oLink.attr("url");
						}
						return undefined;
					},
					resUuid:function(){
						if(this.isLeaf()){
							return this.oLink.attr("id");
						}
						return undefined;
					},
					title:function(){
						return this.oLink.attr('menu');
					}
				};
			});
		//日志记录
      function takeNotes(examId,uuid){
        var url = "/log/takeNotes?uuid="+uuid+"&examId="+examId;
        ctrl.getJson(url, function(data) {
        });
      }
			$.each(ms,function(i,o){
				var $this = o.oLink;
				$this.click(function(){
					var thisExamId = examId;
					var activedTab = tabs.getActivedTab();
					if(activedTab){
						thisExamId = activedTab.id.substring(activedTab.id.lastIndexOf("_")+1);
					}
					o.fnShow();					
					if(o.isLeaf()){
						if(thisExamId != examId){
							ctrl.moment("正在切换考试，请稍候...",undefined,function(){
								tabs.closeAll();
								
								/**
								tabs.newTab({id:o.resid()+"_"+examId,title:o.title()},function(c){									
									showReport(c,o.resid());
								});	
								**/
								
								if(o.resid().indexOf("api/repos")>0) {
								 takeNotes(examId,o.title());
									tabs.newTab({id:o.resUuid()+"_"+examId,title:o.title()},function(c){
										showPentahoReport(c, o.resid()+"&param_examid="+examId+"&param_userid="+$(userId).val()+"&title="+o.title(), o.resUuid());
									});	
								} else {
									tabs.newTab({id:o.resid()+"_"+examId,title:o.title()},function(c){
										showReport(c,o.resid());
									});	
								}
							});
						}else{
						 
							if(o.resid().indexOf("api/repos")>0) {
							 takeNotes(examId,o.title());
								tabs.newTab({id:o.resUuid()+"_"+examId,title:o.title()},function(c){
									showPentahoReport(c, o.resid()+"&param_examid="+examId+"&param_userid="+$(userId).val()+"&title="+o.title(), o.resUuid());
								});	
							} else {
								tabs.newTab({id:o.resid()+"_"+examId,title:o.title()},function(c){
									showReport(c,o.resid());
								});	
							}
						}

						// 选中菜单字体黑体

						Crumbs = $(this).parents('.submenu').prev().children().text();
						bb = Crumbs.split("  ").reverse();
						bb.push($(this).text());
						for(var i = 0; i < bb.length; i++){
							$('#crumbs').append('<a/>');
							$('#crumbs a').eq(i).html(bb[i]+ " / ");
						}
						if(cc != $(this).text()){
							$('#crumbs a').last().remove();
						}
					}
				});
			});

			function hex(x) {
				return ("0" + parseInt(x).toString(16)).slice(-2);
			}
			
			return ms;			
		};		
		function about(){
			$(".about").click(function(){
				var title = $("#title").val();
				var version = $("#version").val();
				var date = $("#date").val();
				var $obj =  dialog.modal({
					size :  'md',// sm md lg
					header : {
						show : true,
						txt : "关于"
					},	
					footer : {
						show : false
					},
					body : "系统名称："+title+"<br><br>系统版本："+version+"<br><br>发布日期："+date
				});
				$obj.find('.selectpicker').selectpicker();
				return $obj;
			});
		}
		
		var o = {
			init : function(){
				setScroll();
				createMenu();				
				showFirstReport($('#menu a:eq(0)'));
				drag();
				about();
			},
			render : function() { 
				this.init();
			}
		};

		return o;

	});

})();
