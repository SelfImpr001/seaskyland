(function() {
	"use strict";
	var index_l=0;
	define([ 'basefn','scrollbar','Tabs','app/dashboard','controller','dialog' ], function(basefn,scrollbar,Tabs,dashboard,ctrl,dialog) {
		function setScroll(){
			
			$('#sidebar').mCustomScrollbar({	
				theme:"light-3",
				scrollInertia: 0
				
			});
			$('#sidebar .icon-double-angle-left').click(function(){
                $('#sidebar').toggleClass('menu-min');
                $(this).toggleClass('icon-double-angle-right','icon-double-angle-left');
            });
			
//			$('.mCSB_dragger_bar').css('background-color','#0b5171');
			$('.page-content .row').css('padding-bottom','40px');
			setHeight();
			$(window).resize(setHeight);
			function setHeight(){
				$('.page-box').height(basefn.cH() - basefn.getTop($('.page-box:visible')[0] ));
			}
		}

		

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
		   var passwordRules = {
					rules : {
						password : {
							required : true,
							minlength : 6
						},
		   		repassword :{
		   				 required:true,
		   				 minlength : 6,
		   				 equalTo:"#password"
		   			 }
					},
					
					messages : {
						password : {
							required : '请输入密码',
							minlength : '密码最小长度为6位'
						},
						repassword:{
							require :"请输入密码",
							minlength:'密码最小长度为6位',
							equakTo :"两次密码不一致"
						}
					}			
			    };
		function updatePassword(){
			$(".editPass").click(function(){
				ctrl.getHtmlDialog(("user/getpwds/" + $(this).attr("pk")),'pwd-modal',function(dialog){
					var $form = dialog.find('#form1');
					$form.on('click','button.btn-primary',function(){
						if($form.valid()){
							var user = $form.formToJson();
						/*	if(user.password!=user.repassword){
								ctrl.alert("两次密码不一致","error")
								return;
							}*/
							user={name: user.getname, password: user.password, pk: user.pk};
							ctrl.putJson("user/updatepwd",user,"密码修改成功！",function(){
								$("#pwd-modal").modal("hide");
							});
						}
						return false;
					}).validate(passwordRules);
				});
			})
		}
//		菜单栏开关开关
		function Btn(){ 
			var open = true;
			$('#sidebar-collapse').click(function(){
				if(open){
					
					$('#sidebar').hide('slow');
					$('.main-content').animate({'margin-left':'0'},'slow');
					open = false;
				}else{
					$('#sidebar').show('slow');
					$('.main-content').animate({'margin-left':'224px'},'slow');
					open = true;
				}
			})
		}

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
	        //tab右键事件 start..........
	        $(".stand-box,.mouseMenu").bind("contextmenu",function(){//阻止右键
 	        	return false;
 	        });
	        $("#menu").find("a:not('.dropdown-toggle')").click(function(){//新建标签页触发事件
	        	mousemenu();
	        });
	        $(document).click(function(){//关闭右键菜单
	        	if($(".mouseMenu").is(":visible")){
	        		 $(".mouseMenu").css({"display":"none"});
	        	}
	        })
			$(".mouseMenu li").click(function(){ //右键菜单功能
				switch($(this).index()){
				  case 0 : 
					  $($(".stand-box li").eq(index_l).find("button")).click();
					  $(".mouseMenu").css({"display":"none"});
				  break;
		    	  case 1 : 
		    		  $($(".stand-box li").eq(index_l).siblings().find("button")).click();
		    		  $(".park-box li").find("button").click();
		    		  $(".mouseMenu").css({"display":"none"});
		    	  break;
		    	  default: ; 
		    	  break;
				}
			})
	        function mousemenu(){//标签页右键触发事件打开右键菜单
	        	
	        	 $(".stand-box li").off("mousedown");
	        	 $(".stand-box li").on("mousedown",function(e){
			        	if(e.which === 3){
			        		if($(".stand-box li").length >1){
			        			 index_l = $(this).index();
			        			$(".mouseMenu").css({"left":e.clientX-$("#sidebar").width(),"top":e.clientY-50,"display":"block"});		
			        		}
			        	
			        	}
			        	//双击关闭当前页
//			        	$(this).dblclick(function(){
//			        		if($(".stand-box li").length >1){
//	        		    	   $(this).find("button").click();
//			        		}
//	        		       })
	        });
	        };
	        mousemenu();
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
	        function side_mouse(flag){
	        	if(flag){
	        		 $(".side_menu li").on("mouseenter",function(){
		 	 	        	$(this).find(">ul").fadeIn(200).end().siblings().find(">ul").hide();
		 	 	        });
	        		 $(".side_menu").on("mouseleave",function(){
	 	 	        	$(this).find("ul").hide();
	 	 	        });
	        	}else{
	        		 $(".side_menu li").off("mouseenter");
	        		 $(".side_menu").off("mouseleave");
	        	}
	        	
	        }

	        $(document).mouseup(function(e) {
	            $(document).off("mousemove");
	        });
	        $('#sidebar-collapse').click(function(){
	        	$('.label-box').fadeToggle("normal","linear");
	        })
	        $('#eee, #sidebar-suo2').click(function(){
				if(open){
					$('#sidebar').animate({"width":"50px"},"slow");
					$('#eee i').addClass('icon-circle-arrow-right');
					$('.main-content').animate({'margin-left':'50'},'slow');	
					$("#sidebar").addClass("side_container").find("#menu").addClass("side_menu").find("ul").hide();
					$("#line").hide();
					side_mouse(open);
					open = false;
				}else{
					$('#sidebar').animate({"width":"220px"},"slow");
					$('#eee i').removeClass('icon-circle-arrow-right');
					if(clickX){
						$('.main-content').animate({'margin-left':clickX},'slow');
					}else{
						$('.main-content').animate({'margin-left':'220px'},'slow');
					}
					$("#line").show();
					side_mouse(open);
					$("#sidebar").removeClass("side_container").find("#menu").removeClass("side_menu");
					open = true;
				}
			});
        }
		function createMenu(){
			var $menu = $('#menu');
			var ms = [];
			$menu.find('a').each(function(i,n){
				var $obj = $(n);
				
				ms[i] = {
					oLink:$obj,
					fnShow: function(){
						$menu.find('li.active').removeClass('active');
		                $obj.parent().addClass('active');
		                this.oLink.parent().find('> .submenu').stop(true).slideToggle('normal');
						this.oLink.parent().siblings().find('> .submenu').stop(true).slideUp('normal');
						this.oLink.find('b').stop(true).toggleClass('icon-angle-down');
						this.oLink.find('b ').parent().parent().siblings().find('b').removeClass('icon-angle-down');
					},
					isLeaf:function(){
						return $obj.parent().find('> .submenu').size() == 0;
					},
					entry:function(){
						if(this.isLeaf()){	
							return this.oLink.attr("entry");
						}
						return undefined;
					}
				};
			});

			var tabs = window["tabs"] = new Tabs('div.main-content');
			
			$.each(ms,function(i,o){
				var $this = o.oLink;
				$this.click(function(){	
					
				o.fnShow();					
				if(o.isLeaf()){
					tabs.newTab({id:$this.attr('id'),title:$this.find('span').text()},function(c){
						var entry = o.entry();
						if (entry && entry.length > 0) {
							require([ entry ], function(m) {
								if (m.render) {
									m.render.call(m,c);
								}
							});
						}
					});									
					
				}
				// 选中菜单字体黑体

				});
			});
			return ms;			
		};
		
		function showMenu(menu){
			//暂时隐藏工作桌面，打开一个可显示内容的子菜单
			/**
			tabs.newTab({id:'myhome',title:'工作桌面',closer:false},function(c){
				dashboard.render(c);
			});
			**/
			
			if(!menu)
				menu = $('#menu a:eq(0)');
			menu.click();
			if(menu.next().size()){
				showMenu(menu.next().find('li:eq(0) a:eq(0)'));
			}
		};
			
		var o = {
			init: function(){
				setScroll();
				createMenu();
				showMenu();
				about();
				drag();
				updatePassword();
			},

			render: function(){				
				this.init();
			}
		};

		return o;
	});
})();
