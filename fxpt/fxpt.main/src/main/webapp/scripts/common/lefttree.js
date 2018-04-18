lefttree();
pathchange();
    
        function lefttree(){
            var $oDtree =$('.nav-list');
            var $oDtreeA = $('.nav-list a');

            $oDtreeA.bind('click',function(){
                domtree( $(this) );
            });


            function domtree(obj){  //参数a的$对象

                var $obj=obj.parent();

                $oDtree.find('li.active').removeClass('active');

                $obj.addClass('active');

                var $oSubmenu = $obj.find('> .submenu'); //二级菜单

                $oSubmenu.slideToggle(200);

            }


            var $oCollapseBtn = $('#sidebar .icon-double-angle-left');

            $oCollapseBtn.click(function(){
                $('#sidebar').toggleClass('menu-min');
                $(this).toggleClass('icon-double-angle-right','icon-double-angle-left')
            });

        }


        function pathchange(){

            var $oNav = $('.breadcrumb');
            
            $('.nav-list a').click(function(){
                var navList=ilevel($(this));
                var sHtml='<i class="icon-home home-icon"></i>';
                for(var attr in navList){
                    
                    sHtml+='<li><a href="#">'+navList[attr]+'</a></li>'
                }
                //去掉最后面一个'/'
                // var pNum = sHtml.lastIndexOf('<span>/</span>');  
                
                // sHtml = sHtml.substring(0,pNum) + sHtml.substring(pNum+14);
                
                $oNav.html(sHtml);
                
            });

                    

            function ilevel($obj){
                var json={};
                var iL=0;

                var isFirst = true; //判断点击是不是第一级

                while( !$obj.parent().parent().hasClass('nav-list') ){
                    isFirst = false;

                    if( iL == 0 ){
                        json[iL]=$obj.text();  //第一次是a里面的内容
                    }else{
                        json[iL]=$obj.siblings().text();  //第二次是li，找到a里面的内容
                    }
                    iL++;
                    $obj = $obj.parent().parent();
                }

                if( isFirst){
                    json[iL] = $obj.text();
                }else{
                    json[iL] = $obj.siblings().text();
                }
                
                
                var json2={};

                for(var attr in json){
                    json2[iL-attr] = json[attr];
                }

                //alert(JSON.stringify(json2))
                //alert(json2.length)
                return json2;
            }

        }

        function getTop(obj){
            var iTop=0;
            while(obj){
            iTop+=obj.offsetTop;

            obj=obj.offsetParent;
            }
            return iTop;
        }

        function getLeft(obj){
            var iLeft=0;
            while(obj){
            iLeft += obj.offsetLeft;

            obj=obj.offsetParent;
            }
            return iLeft;
        }

        // 数组去重
        function arrnodou(arr){
            
            var json = {};
            var aTmp = [];

            for(var i=0; i<arr.length; i++){
                
                if( !json[arr[i]] ){
                    json[arr[i]] = 1; //必须赋一个真值，否则无法去重。
                    aTmp.push( arr[i] );
                }
            }

            return aTmp;
        }

        function radioztree(opts){

            var _opts = {

                ztreeId  : opts.ztreeId || '',
                targetId: opts.targetId || '',
                radioType:opts.radioType || 'all', 
                onCheckCb : opts.onCheckCb || function(){},
                async   :opts.async || {},
                initData:opts.initData || [{}],
            }

            var $oNewTree = $('<ul id='+_opts.ztreeId+' class="ztree ztree-chechbox"></ul>');
            $oNewTree.appendTo($(document.body));

            var oModal_index = $('#'+_opts.targetId).parents('.modal').css('z-index');
            $oNewTree.css('z-index',oModal_index+1);

            var setting = {

                async: _opts.async,

                check: {
                    enable: true,
                    chkStyle: "radio",
                    radioType: _opts.radioType,
                },

                view: {
                    dblClickExpand: false
                },

                data: {
                    simpleData: {
                        enable: true
                    }
                },

                callback: {
                    onCheck: onCheck
                }
            };

            

            function onCheck(e, treeId, treeNode) {
                var  oZtree = $.fn.zTree.getZTreeObj(_opts.ztreeId);
                var nodes = oZtree.getCheckedNodes(true);
                
                _opts.onCheckCb(nodes);
            
            }

            var oTargetId = "#" + _opts.targetId;

            $(oTargetId).bind('focus',function(){             
                showMenu();
            });

            function showMenu() {
                var oTarget = $(oTargetId)[0];
            
                $oNewTree.css({left:getLeft(oTarget), top:getTop(oTarget)+$(oTargetId).outerHeight()}).slideDown("fast");           
                
                var minWidth = $(oTargetId).outerWidth();
                $oNewTree.show().css('min-Width',minWidth);
                $("body").bind("mousedown", onBodyDown);
            }

            function hideMenu() {
                $oNewTree.fadeOut("fast");

                $("body").unbind("mousedown", onBodyDown);
            }

            function onBodyDown(event) {
                if (!(event.target.id == _opts.targetId || event.target.id == _opts.ztreeId || $(event.target).parents("#"+_opts.ztreeId).length>0) || event.target.id == '') {
                    hideMenu();
                }
            }

            $(document).ready(function(){
                $.fn.zTree.init( $oNewTree, setting, opts.initData);
            });


        }










