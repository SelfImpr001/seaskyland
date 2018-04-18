(function(){
	"use strict";
	define(['basefn'],function(basefn){
		window.zindex = 9999;  //最高层级
		window.idialog = 0; //记录有一个非模态框		

		function getdialog(settings,ismodal) {
			var $oDialogBox = $('<div class="dialog-box"></div>');
			$oDialogBox.html('<div class="dialog">\
								<div class="dialog-content">\
									<div id="dialogId" class="dialog-body" ><span id="dowmLoadReportSpanId" style="color:red,display:none};">11111</span></div>\
								</div>\
							</div>');
			$oDialogBox.css('z-index', ++window.zindex);

			var $oDialog = $oDialogBox.find('.dialog');
			$oDialog.addClass('dialog-' + settings.size);
			$oDialog.find('.dialog-body').html(settings.body);
			if (settings.header.show === true) {
				var $oHeader = $('<div class="dialog-header">\
							        <button type="button" class="close">&times;</button>\
							        <h4 class="dialog-title">' + settings.header.txt + '</h4>\
								</div>');
				$oHeader.prependTo($oDialogBox.find('.dialog-content'));
			}

			if (settings.footer.show === true) {

				var $oFooter = $('<div class="dialog-footer" id="subb"></div>');

				for (var i = 0; i < settings.footer.buttons.length; i++) {

					var $oBtn = $('<button class="btn"></button>');

					$oBtn.attr('type', settings.footer.buttons[i].type).addClass('btn-' + settings.footer.buttons[i].sty).html(settings.footer.buttons[i].txt);

					$oBtn.on('click', settings.footer.buttons[i].callback);

					$oBtn.on('close', function() {
						removemodal();
					});

					$oBtn.appendTo($oFooter);
				};

				$oFooter.appendTo($oDialog.find('.dialog-content'));
			}

			if (settings.untransparent === false) {
				$oDialog.addClass('trsp');
			}

			$oDialogBox.appendTo($(document.body));

			var $oTarget = settings.target;
			if ($oTarget[0].nodeName == 'body' || $oTarget[0].nodeName == 'BODY') {
				var iLeft = iRight = iTop = iBottom = 0;
			} else {
				var iLeft = basefn.getLeft($oTarget[0]);
				var iRight = basefn.cW() - (iLeft + $oTarget.width());
				var iTop = basefn.getTop($oTarget[0]);
				var iBottom = basefn.cH() - $oTarget.height() - iTop;
			}

			if(ismodal){
				$oDialogBox.addClass('modal-box');
				$oDialogBox.css({left:iLeft,right:iRight,top:iTop,bottom:iBottom});

				$oDialog.css({
					top: 40,
					left:(settings.target[0].offsetWidth-$oDialog[0].offsetWidth)/2
				});
			
				if (settings.backdrop === true) {
					var $oBackdrop = $('<div class="dialog-backdrop"></div>');
					$oBackdrop.css({
						left: iLeft,
						right: iRight,
						top: iTop,
						bottom: iBottom
					});
					$oBackdrop.prependTo($oDialogBox);
				}

			}else{
				$oDialogBox.addClass('nonmodal-box');
				$oDialogBox.css({
					top: basefn.getTop(settings.target[0]) + basefn.scrollTop() + 40,
					left: basefn.getLeft(settings.target[0]) + (settings.target[0].offsetWidth - $oDialog[0].offsetWidth) / 2
				});
				
			}

			if(settings.canmove === true){				
				comDrag($oHeader[0]);								
				$oHeader.mousedown(function(e) {
					$oDialog.css('z-index', ++window.zindex);

					var disX = e.clientX - $oDialog[0].offsetLeft;
					var disY = e.clientY - $oDialog[0].offsetTop;
					var that = $oDialog[0];

					document.onmousemove = function(e) {
						var e = e || window.event;
						var L = e.clientX - disX;
						var T = e.clientY - disY;
						L = L < 0 ? 0 : L;
						T = T < 0 ? 0 : T;
						if(ismodal){
							var iMaxL = $oDialogBox[0].offsetWidth - $oDialog[0].offsetWidth;
							var iMaxT = $oDialogBox[0].offsetHeight- $oDialog[0].offsetHeight;
						}else{
							var iMaxL = basefn.cW() - that.offsetWidth;
							var iMaxT = basefn.cH() - that.offsetHeight;
						}
						
						L = L > iMaxL ? iMaxL : L;
						
						T = T > iMaxT ? iMaxT : T;
						that.style.left = L + 'px';
						that.style.top = T + 'px';
					}

					document.onmouseup = function() {
						$oHeader.css({'cursor':'default'});						
						document.onmousemove = document.onmouseup = null;
					}
				});			
			}
				
			
			$oDialog.find('.close').on('click', function() {
				removemodal();
			});

			function removemodal() {
				$oDialog.animate({
					opacity: 0,
					top: '-=12px'
				}, 200, function() {

					if (settings.backdrop === false) {
						$oDialogBox.remove();
					} else {
						$oBackdrop.animate({
							opacity: 0
						}, 200, function() {
							$oDialogBox.remove();
						});
					}
				});
			}

			$oDialogBox.show = function(){
				if(settings.backdrop){
					$oDialogBox.find('.dialog-backdrop').animate({opacity:0.5},200,function(){
						$oDialog.animate({opacity:1,top:'+=12px'},300);
					});
				}else{
					
					$oDialog.animate({opacity:1,top:'+=12px'},300);					
				}
				return this;				
			};
			

			return $oDialogBox;
		}

	// }
		var dialog = {
			fadedialog: function(opts){
				var settings = {};
				var baseSettings = {
					canmove:false,
					size:'sm',
					backdrop:false
				};
				var defaultSettings = {
					speed:300,
					delay:1000,
					header:{show:false},
					footer:{show:false,buttons:[]},
					tip_txt:'这是一个提示',
					icon_info:'ing',
					target: $('body')				
				};

				$.extend(settings,defaultSettings,opts,baseSettings);

				var $oDialogBox = getdialog(settings,false); 

				$oDialogBox.find('.dialog-body').html('<p class="tipcont"><span class="tip-icon"></span>&nbsp;&nbsp;&nbsp;<span class="tip-txt">'+settings.tip_txt+'</span></p>');
				$oDialogBox.find('.tipcont').addClass('updata-'+settings.icon_info);				
				
				$oDialogBox.show().delay(settings.delay).animate({
						opacity:0,
						top:'-=7px'					
						},settings.speed,function(){
							$(this).remove();
							if(opts.callback){
								opts.callback();
							}
					}
				);				
			},
			tipmodal: function(opts){
				var settings = {};
				var defaultSettings ={
					target: $(document.body),
					backdrop:true,
					header:{show:true,txt:"提示"},
					footer:{
						show:true,
						buttons:[
							{type:'submit',txt:"确定",sty:'primary',callback:function(){}},
							{type:'button',txt:"取消",sty:'default',callback:function(){$(this).trigger('close');}}
						]
					},
					tip_txt:'这是一个提示',
					icon_info:'updata-ing',
					canmove:false,
					untransparent:true,
					tipsType:'msgif'
				};

				$.extend(settings,defaultSettings,opts,{size:'sm'});

				var $oModalBox = getdialog(settings,true);
				var $oModal = $oModalBox.find('.dialog');
				//显示进度条
				if(settings.tipsType == 'progressbar'){
					var processbar = 	function ($source){
						var bar = $('<div class="progress-warp"><table class="table progress" ><tr><td style="width:0px;">&nbsp;</td><td style="width:0px;text-align: left; color: #D9534F; background: transparent;"></td></tr></table></div>');
						
						function process(w){
							bar.find('td:eq(0)').css({width:w+"%"});
							bar.find('td:eq(1)').css({width:(100-w)+"%"});//.text(w+'%');
							setTimeout(function(){
								var newW = w + 10;
								if(newW * 1 < 100){
								    process(newW);
								}else{
									bar.find('td:eq(0)').css({width:"100%"});//.text('100%');
									bar.find('td:eq(1)').css({width:"0",display:'none'});
									setTimeout(function(){
										$source.hide(function(){																
											$(this).remove();
										});
									},100);
								}
							},100);
						};
						process(10);
						return bar;
					}($oModalBox);
					$oModalBox.find('.dialog-body').html(processbar);
					$oModalBox['complete'] = function(){
						
					};
				}else{
					$oModalBox.find('.dialog-body').html('<p class="tipcont"><span class="tip-icon"></span>&nbsp;&nbsp;&nbsp;<span class="tip-txt">'
							+ settings.tip_txt + '</span></p>');
					$oModalBox['complete'] = function(){
						$oModalBox.fadeOut(function(){						
							$(this).remove();
						});
					};
				}
				$oModalBox.find('.tipcont').addClass('updata-'+settings.icon_info);

				$oModalBox.show();
				
				
				return $oModalBox;
			},

			modal: function(opts){
				var defaultSettings = {
					size: 'lg',
					target: $(document.body),
					canmove:true,
					backdrop: true,
					header: {
						show: true,
						txt: "提示"
					},
					footer: {
						show: true,
						buttons: [{
							type: 'submit',
							txt: "确定",
							sty: 'primary',
							callback: function() {}
						}, {
							type: 'button',
							txt: "取消",
							sty: 'default',
							callback: function() {
								$(this).trigger('close');
							}
						}]
					},
					body:''

				};
				var settings = {};
				$.extend(true,settings,defaultSettings,opts);

				var $oModalBox = getdialog(settings,true);
				
				$oModalBox.show();
				return $oModalBox;
			},

			nonmodal: function(opts){
				var defaultSettings = {
					size: 'lg',
					target: $('body'),
					header: {
						show: true,
						txt: "提示"
					},
					footer: {
						show: true,
						buttons: [{
							type: 'submit',
							txt: "确定",
							sty: 'primary',
							callback: function() {}
						}, {
							type: 'button',
							txt: "取消",
							sty: 'default',
							callback: function() {
								$(this).trigger('close')
							}
						}]
					},
					body:''
				};
				var settings = {};
				$.extend(true,settings,defaultSettings, opts,{backdrop:false});

				var $oNonmodalBox = getdialog(settings,false);
				$oNonmodalBox.show();
				return $oNonmodalBox;
			}
		};

		return dialog;
	});
})();