(function(){
	"use strict";
	define(['basefn'],function(basefn){

		//$('.stand-box').width( $('.label-box').width()-$('.label-box .more').width() );

		function getObjByINow(value){	//根据iNow值获取对象
			for (var i = 0; i < tabArr.standArr.length; i++) {
				if( tabArr.standArr[i].settings.iNow == value ){
					return tabArr.standArr[i];
				}
			};
			for (var i = 0; i < tabArr.parkArr.length; i++) {
				if( tabArr.parkArr[i].settings.iNow == value ){
					return tabArr.parkArr[i];
				}
			};
			return false;
		}

		function getObjByIndex(value){	//根据index值获取对象
			for (var i = 0; i < tabArr.length; i++) {
				if( tabArr[i].index == value ){
					return tabArr[i];
				}
			};
			return false;
		}

		function getObjByPrimary(attr,value){	//根据一级值获取对象
			for (var i = 0; i < tabArr.length; i++) {
				if( tabArr[i][attr] == value ){
					return tabArr[i];
				}
			};
			return false;
		}

		window.tabArr = {	//储存每个窗口对象
			standArr:[],
			parkArr:[]
		};
  
		function Tab(obj){
			// this.oLink = obj.parent('li');
			this.oLabel = null;
			this.oLabelBox = null;

			this.bodyBox = $('.page-box');
			this.labelStandBox =$('.stand-box');
			this.labelParkBox = $('.park-box');
			this.showParkBtn = $('.label-box .more');
			this.closeBtn = null;
			
			this.index = -1; //索引
			this.isReadyShow = false;
			this.isStand = false;
			this.isShow = false;
			this.imaxStand = -1;
			//var bodyBox = this.bodyBox;
			this.settings = {
				standLabelWidth:'20%',//请给百分比的宽度
				parkLabelWidth:190,
				parkLabelHeight:40,
				title:obj.text(),
				iNow: obj.attr('id'),
				emptyShow:function(){
					return '<h2>空啦空啦！</h2>';
				},
				bodyCreate:function(){
					//if(bodyBox.find('#tab_c_'+this.iNow).size()){
					//	bodyBox.find('.page-content').hide().find('#tab_c_'+this.iNow).show();
					//}else{
					//	
					//}
					return '<h2>没填内容？！</h2>';
				}
			};
		}


		Tab.prototype.json={};//防止一个图标重复点开窗口

		Tab.prototype.isParking = function(){
			if(  tabArr.parkArr.length>0 ){
				return true;
			}
			return false;
		};

		Tab.prototype.init=function(opts){
			$.extend(true ,this.settings,opts);
			//console.log(this.json)
			if( this.json[this.settings.iNow] == undefined ){
				this.json[this.settings.iNow]=true;
			}
			
			if( this.json[this.settings.iNow] ){

				this.create(this.settings);
				this.setDada();

				this.json[this.settings.iNow] = false;
			}else{				
				 getObjByINow(this.settings.iNow).fnShow();				
			};			
		};

		Tab.prototype.create = function(){
			this.oLabel = $('<li class="item stand-item">\
			                    <a class="txt" href="javascript:;">'+this.settings.title +'</a>\
			                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>\
			                </li>');

			this.closeBtn = this.oLabel.find('.close');
			this.closeBtn.hide();
			//this.bodyBox
		};

		Tab.prototype.setDada = function(){			
			this.imaxStand = Math.floor(100/this.settings.standLabelWidth.slice(0,-1)) ;

			this.oLabel.width(this.settings.standLabelWidth);
			this.labelParkBox.width(this.settings.parkLabelWidth);
			
			this.fnShow();

			if( this.isParking() ){
				this.createParking();	
			}

			var that = this;
			this.oLabel.click(function(){
				that.fnShow();
				that.settings.labelclick();
			});

			this.closeBtn.click(function(e){
				that.fnRemove(e);
				return false;
			});
			// this.oLink.click(function(){
			// 	that.fnShow();
			// });
		};

		Tab.prototype.createParking = function(){
			this.showParkBtn.css('opacity',1);
			this.labelParkBox.css('opacity',1);

			var that = this;
			this.showParkBtn.mouseover(function(){
				that.labelParkBox.slideDown(100);
			});
			$(document).click(function(e){
				that.labelParkBox.slideUp(100); 
					
			});
		};

		Tab.prototype.fnShow = function(){
			if(!this.isStand){
				this.fnStand(true);
			}
			if( tabArr.standArr.length >1 ){
				for (var i = 0; i < tabArr.standArr.length; i++) {
					tabArr.standArr[i].oLabel.removeClass('active');
					tabArr.standArr[i].isShow = false;
					tabArr.standArr[i].closeBtn.show();
				};
			}			
				
			
			// this.oLink.addClass('active');
			this.oLabel.addClass('active');
			this.isShow = true;
			this.settings.bodyCreate();
			//this.bodyBox.html( this.settings.bodyCreate() );
		};

		Tab.prototype.fnStand = function(isReadyShow){
			var isReadyShow = isReadyShow || false;

			if(this.isStand)return false;
			this.isStand = true;
			this.oLabelBox = null;
			this.oLabelBox = this.labelStandBox;
			var that = this;
			if(!this.json[this.settings.iNow]){	//已经生成了的情况(parkBox中点击出现在stand中,或者standBox中位置空余 parkBox中的上去)
				for(var i= 0; i<tabArr.parkArr.length; i++){
					if( tabArr.parkArr[i].index> that.index){
						tabArr.parkArr[i].index--;
					}							
				}
				tabArr.parkArr.splice(that.index,1);

				var oInnerBox = this.oLabel.detach();
				
				if(isReadyShow){	//如果即将显示，那么它出现在首位(否则出现在末尾)
					// this.oLink.removeClass('active');
					tabArr.standArr[0].fnPark();
					oInnerBox.prependTo(this.oLabelBox).show().css('height','100%').css({width:that.settings.standLabelWidth});
					tabArr.standArr.push(this);
					this.index = this.labelStandBox.children().length-1;

				}else{
					for(var i=0; i<tabArr.standArr.length; i++){
						tabArr.standArr[i].index++;
					}
					this.oLabel.appendTo(this.oLabelBox).show();
					tabArr.standArr.unshift(this);
					this.index = 0;
					
				}
				this.oLabel.css('height','100%').animate({width:that.settings.standLabelWidth});

			}else{	//没有生成的情况
				
				if( tabArr.standArr.length >= this.imaxStand ){
					tabArr.standArr[0].fnPark();
				}
								
				this.oLabel.prependTo(this.oLabelBox);
				//tabArr.standArr.push(this);
				this.index = tabArr.standArr.length-1;
				//console.log(tabArr.standArr)
				this.oLabel.css({'height':'100%',width:0}).animate({width:that.settings.standLabelWidth});
			}
		};

		Tab.prototype.fnPark = function(){	//standBox放不下被挤下去才会执行
			if(!this.isStand)return;
			this.isStand = false;

			var that = this;
			for (var i = 0; i < tabArr.standArr.length; i++) {
				if( tabArr.standArr[i].index > this.index ){
					tabArr.standArr[i].index--;	
				}
			};
			tabArr.standArr.splice(that.index,1);
			this.oLabelBox = null;

			this.oLabelBox = this.labelParkBox;
			tabArr.parkArr.push(this);
			this.oLabel.prependTo(that.labelParkBox).css({height:this.settings.parkLabelHeight,width:that.settings.parkLabelWidth});
			this.index = this.oLabelBox.children().length-1;			
		};

		Tab.prototype.fnRemove = function(e){
			
			var proIndex = this.index;
			var that = this;
			if( !this.isStand ){
				this.oLabel.animate({height:0},function(){
					$(this).remove();

					for(var i= 0; i<tabArr.parkArr.length; i++){
						if( tabArr.parkArr[i].index> proIndex){
							tabArr.parkArr[i].index--;
						}							
					}
					tabArr.parkArr.splice(that.index,1);
				});

			}else{
				
				for(var i= 0; i<tabArr.standArr.length; i++){
					if( tabArr.standArr[i].index> proIndex){
						tabArr.standArr[i].index--;
					}							
				}
				tabArr.standArr.splice(that.index,1);

				this.oLabel.animate({width:0,opacity:0},function(){
					$(this).remove();
				});
				
				this.json[this.settings.iNow] = true;
				
				if(tabArr.parkArr.length>0){
					tabArr.parkArr[tabArr.parkArr.length-1].fnStand();
				}				
			};

			this.settings.removeCallback(this.isShow);

			if (tabArr.standArr.length == 0) {
				this.bodyBox.empty().html(this.settings.emptyShow());
				return;
			};
			if(tabArr.parkArr.length == 0){
				this.showParkBtn.css('opacity',0);
				this.labelParkBox.css('opacity',0);
			}
			if(tabArr.standArr.length == 1){
				tabArr.standArr[0].closeBtn.hide();
			};
			
		};

		 window.Tab = Tab;
	});
})();



