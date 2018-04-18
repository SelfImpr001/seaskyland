(function(){
	"use strict";
	define(['basefn',"ztreecore", "ztreecheck"],function(basefn){
		var defaultSetting = { 
			async: {enable:false},
            check: {
                enable: true,
                chkStyle: "checkbox"
            },
            view: {
                dblClickExpand: false,
                selectedMulti:false
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        };
		return function(opts){

            var oTargetId = "#" + opts.targetId;
            var oTarget = $(oTargetId)[0];

            var $oNewTree = $('<ul id='+opts.ztreeId+' class="ztree ztree-chechbox"></ul>');
            
            var oModal_index = $('#'+opts.targetId).parents('.dialog').css('z-index');
            $oNewTree.css('z-index',oModal_index+1);

            var setting = $.extend(true,{},defaultSetting,opts.settings||{});


            $(oTargetId).bind('focus',function(event){             
                showMenu(event);
            });
            var $oTreeBox = $('<div style="position:absolute;"></div>');
            $oTreeBox.css('z-index',++window.zindex);
            function showMenu(event) {
                
                var cH = document.documentElement.clientHeight;
                              
                var iT_width = $(oTargetId).outerWidth();
                var iT_height = $(oTargetId).outerHeight();
                var iT_top = $(oTargetId)[0].offsetTop;
                var iT_left = $(oTargetId)[0].offsetLeft;
                $oTreeBox.css({width:iT_width,
                    height:iT_height,
                    top:iT_top,
                    left:iT_left
                });
                var iP_zindex = $(oTargetId).parents('.modal-dialog').css('z-index');
                //alert(iP_zindex)
                $(oTargetId).css('z-index',iP_zindex+2);
                $oTreeBox.appendTo($(oTargetId).parent()).css('z-index',iP_zindex+1);
                $oTreeBox.show();
                setTop();
                var minWidth = $(oTargetId).outerWidth();
                //alert(minWidth)
                $oNewTree.show().css('min-Width',minWidth);
                $("body").bind("mousedown", onBodyDown);
                
                $(window).resize(function(){
                    setTop();
                });
                
            }
            
            function setTop(){
                if( 300+$(oTargetId).outerHeight()+basefn.getTop( $(oTarget)[0] ) >document.documentElement.clientHeight ){                    
                    $oNewTree.css({bottom:'100%',top:'auto'});
                }else{       
                    $oNewTree.css({top:'100%',bottom:'auto'});    
                }
            }

            function hideMenu() {
                $oNewTree.fadeOut("fast");
                $oTreeBox.hide();
                $("body").unbind("mousedown", onBodyDown);
            }

            function onBodyDown(event) {
                if (!(event.target.id == opts.targetId || event.target.id == opts.ztreeId || $(event.target).parents("#"+opts.ztreeId).length>0) || event.target.id == '') {
                    hideMenu();
                }
            }
            var tree = $.fn.zTree.init( $oNewTree, setting, opts.initData||[]);
            $oNewTree.prependTo( $oTreeBox );
            return tree;
        };
	});
})();