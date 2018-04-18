/*依赖dialog文件
函数传参为json，
	target:$DOM元素，被遮罩的元素,默认body 
	header：json，头部
		show: Boolean,是否显示头部，默认true
		txt：头部的文字，默认“提示”
	footer：json, 尾部
		show：Boolean,是否显示尾部，默认true
		bottons： json，尾部按键信息，默认确定、取消两个按钮
			type：按钮类型，
			txt： 按钮文字
			style： 按钮风格，有default primary info danger warning几种风格
			callback： 点击后的回调函数，关闭事件为自定义方法 close
*/
(function(){
	"use strict";
	define(['dialog','app/login'],function(dialog,login){

		var oDefaultSettings = {
			target: $(document.body),
			type:'GET',
			//data:'json',
			contentType: 'application/json',
			dataType:'json',
			timeOut : 1000,
			beforeSendMsg:{
				backdrop:false,
				show:true,
				header: {show:false},
				tip_txt:'系统正在处理，请稍候...',
				icon_info:'updata-ing',
				footer:{show:false},
				untransparent:false
			},
			successMsg:{
				show:false,
				delay:1000,
				tip_txt:'操作成功！',
				icon_info:'success',
				header:{show:true, txt:'操作成功'}
			},
			warningMsg:{
				icon_info:'warning',
				header:{show:true, txt:'操作失败'}
			},
			errorMsg:{tip_txt:'操作失败！',
				icon_info:'error',
				canmove:true,
				header:{show:true, txt:'操作失败'},
				footer:{
					show:true,
					buttons:[
						{type:'submit',txt:"确定",sty:'primary',callback:function(){$(this).trigger('close');}}
					]
				}
			}
		};


		var o = function(opts){
			var settings = {};
	
			$.extend(true,settings,oDefaultSettings,opts);

			var oTarget = settings.target;

			settings.beforeSendMsg.target = settings.beforeSendMsg.target? settings.beforeSendMsg.target : oTarget;
			settings.successMsg.target = settings.successMsg.target? settings.successMsg.target : oTarget;
			settings.errorMsg.target = settings.errorMsg.target? settings.errorMsg.target : oTarget;
			if(opts.async!=undefined){
				settings.async =opts.async;
			}else{
				settings.async =true;
			}
			var $overlay = undefined;

			var _url = window.app.rootPath + settings.url;
			if (settings.url.substring(0, 1) === '/') {
				var length = opts.url.length;
				_url = window.app.rootPath
						+ settings.url.substring(1, length);
			}
			
			return (function(){
				var ajaxOpts = {
					url: _url,
					type: settings.type,
					contentType: settings.contentType,
					dataType: settings.dataType,
					async:settings.async,
					timeOut: settings.timeOut,
					beforeSend:function(){
						if(settings.beforeSendMsg.show){
							if(!$overlay){
								$overlay = dialog.tipmodal(settings.beforeSendMsg);
							}
						}	    
					},	
					complete: function(){
						/* 隐藏之后移除内容*/
						if($overlay){
							$overlay.complete();											
						}
						
					},
					success: function(data,textStatus,jqXHR){
						if(jqXHR){
							var sessionstatus = jqXHR.getResponseHeader("sessionstatus");
							if(sessionstatus === 'timeout'){
								login.relogin(data);						
								return;
							}
						}
						
						
						var tmpData = null;
						if(typeof(data) === "string"){
							try{
								tmpData = jQuery.parseJSON(data);
								
							}catch(e){
							}
						}else{
							tmpData = data;
						}
						if(data.timeOut){
							login.relogin(tmpData);
							return;
						}
						if(tmpData && tmpData.status && tmpData.status.success == false){
							dialog.tipmodal({
								footer:{
									show:true,
									buttons:[
										{type:'button',txt:"确定",sty:'default',callback:function(){$(this).trigger('close');}}
									]
								},
								tip_txt:'操作失败:'+tmpData.status.msg,
								icon_info:'error',
							});							
						}else{
							if(settings.successMsg.show)
								dialog.fadedialog(settings.successMsg);
							settings.callback(data);
						}
					},
					error: function(xhr,textStatus){
						dialog.tipmodal(settings.errorMsg);
					}
				};
				ajaxOpts["data"] = settings.data;
				if(settings.dataType === 'text'){
					ajaxOpts.fileElementId = settings.fileElementId ||"fileUpload";
					$.ajaxFileUpload(ajaxOpts);
				}else{
					$.ajax(ajaxOpts);
				}
			    
			})();

		};

		return o;

	});
})();