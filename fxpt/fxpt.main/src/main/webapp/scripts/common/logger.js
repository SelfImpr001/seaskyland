/**
 * 输出日志
 * @author liguiqing
 */

(function(){
	"use strict";
	define(function(){
		var o = function(msg){
			//判断浏览器,待实现
			if(window.dev && window.console)
			    console.log(msg);
		}
		
		return o;
	});
})();