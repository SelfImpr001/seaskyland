(function(){
	"use strict";
	define(function(){
		
		var _contextPath = window.app.rootPath;
		var _len = _contextPath.length;
		if(_contextPath.substring(_len-1,_len) === '/'){
			_contextPath = _contextPath.substring(0,_len-1);
		}
		
		
		var o = function(opts){
			var defaultOptions = {
			        type:'GET',
			        dataType:'json',
			        contentType:"application/json",
			        timeOut : 1000,
			        data:{}
				};
			var _opts = {};
			
			$.extend(_opts,defaultOptions,opts);
			var _url = _contextPath+_opts.url;
			$.ajax({
				url : _url,
				type : _opts.type,
				async:_opts.async,
				dataType : _opts.dataType,  
				contentType:_opts.contentType,
				data:_opts.data,
				success : function(data) {
					_opts.callback(data);
				},
				error:function(data){
					
				} 
			});	
			
		};
		return o;
	});
})();