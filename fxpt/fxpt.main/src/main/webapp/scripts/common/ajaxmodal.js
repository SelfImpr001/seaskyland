(function(){
	"use strict";
	define(function(){

		var defaultSetting ={
			target: $(document.body),
			backdrop:true,
			header:{show:true,txt:"提示"},
			footer:{
				show:true,
				buttons:[
					{type:'submit',txt:"确定",style:'primary',callback:function(){}},
					{type:'button',txt:"取消",style:'default',callback:function(){$(this).trigger('close')}},
				],
			},
			tip_txt:'',
			icon_info:'updata-ing',
			untransparent:true
		};

		var ajaxmodal = function(opts){

			//if($obj.is(':hidden'))return;
			//alert( JSON.stringify(opts))
			var setting ={}; //配置参数

			$.extend(setting,defaultSetting,opts);
			
			var $obj = setting.target;
			if($obj[0].nodeName == 'body' || $obj[0].nodeName == 'BODY'){
				var iLeft = iRight = iTop = iBottom = 0;
			}else{
				var iLeft = getLeft($obj[0]); 
				var iRight = document.documentElement.clientWidth-( iLeft + $obj.width() );
				var iTop = getTop($obj[0]);
				var iBottom =document.documentElement.clientHeight- $obj.height()-iTop ;	
			}
							
	
			var $oDialog = $('<div class="tip-modal Modal-ajax-tip" id="Modal-ajax-tip" data-backdrop="static"></div>');
			
			$oDialog.css({left:iLeft,right:iRight,top:iTop,bottom:iBottom});

			$oDialog[0].innerHTML= '<div class="modal-dialog">\
									    <div class="modal-content">\
									      <div class="modal-body">\
									         <p class="updata"><span class="tip-icon"></span>&nbsp;&nbsp;&nbsp;<span class="tip-txt">'+setting.tip_txt+'</span></p>\
									      </div>\
									    </div>\
									</div>';

  			$oDialog.find('.updata').addClass(setting.icon_info);

	  		if( setting.header.show === true ){
	  			var $oHeader = $('<div class="modal-header" onmouseover="comDrag(this)">\
								        <button type="button" class="close">&times;</button>\
								        <h4 class="modal-title">'+setting.header.txt+'</h4>\
								    </div>');
		      	$oHeader.prependTo( $oDialog.find('.modal-content') );
	  		} 

	  		if( setting.footer.show === true ){

	  			var $oFooter = $('<div class="modal-footer"></div>');

	  			//alert(defaultSetting.footer.buttons[0])
	  			for (var i = 0; i < setting.footer.buttons.length; i++) {

	  				if(opts.footer){
	  					setting.footer.buttons[i] = $.extend(defaultSetting.footer.buttons[i], opts.footer.buttons[i] );
	  				}
	  				
	  				var $oBtn = $('<button type="'+setting.footer.buttons[i].type+'" class="btn btn-'+setting.footer.buttons[i].style+'" close="">'+setting.footer.buttons[i].txt+'</button>');

	  				$oBtn.on('click',setting.footer.buttons[i].callback)

	  				$oBtn.on('close',function(){
	  					removemodal();
	  				});

	  				$oBtn.appendTo( $oFooter );
	  			};

		  		$oFooter.appendTo( $oDialog.find('.modal-content') );

		  	}

		  	if( !setting.untransparent ){
		  		$oDialog.find('.modal-content').addClass('trsp');

		  	}	
		 
			$oDialog.find('.close').on('click',function(){
				removemodal();
			});

			$oDialog.find('.colse-btn').on('click',function(){
				removemodal();
			});

			var  iranNum = + + new Date();

			function removemodal(){
				$oDialog.animate({opacity:0},300,function(){
					$(this).remove();
				});

				
			}
		

			$oDialog.appendTo( $(document.body) ).animate({opacity:1},300);

			if (setting.backdrop){
				var $oBackdrop = $('<div class="ajaxmodal-backdrop" style="z-index:1"></div>');
				$oBackdrop.css({left:iLeft,right:iRight,top:iTop,bottom:iBottom});
				$oBackdrop.prependTo( $oDialog );
			};									
			

			return $oDialog;		
						
		}


		return ajaxmodal;

	});
})();