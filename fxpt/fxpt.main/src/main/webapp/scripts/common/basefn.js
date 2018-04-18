(function(){
    "use strict";
    define([],function(){
        var obj;
        var o=obj = {
            getTop: function(obj){
                var iTop=0;
                while(obj){
                    iTop+=obj.offsetTop;
                    obj=obj.offsetParent;
                }
                return iTop;
            },

            getLeft: function(obj){
                var iLeft=0;
                while(obj){
                    iLeft += obj.offsetLeft;

                    obj=obj.offsetParent;
                }
                return iLeft;
            },

            cH:function(){
                return document.documentElement.clientHeight;
            },

            cW:function(){
                return document.documentElement.clientWidth;
            },

            scrollTop: function(){
            	return document.body.scrollTop || document.documentElement.scrollTop;
            },
            arrnodou: function(arr){            
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
        }; 

        return o;

    });   
})();    
       

       
        












