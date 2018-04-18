/*
 * jQuery pager plugin
 * Version 1.0 (12/22/2008)
 * @requires jQuery v1.2.6 or later
 *
 * Example at: http://jonpauldavies.github.com/JQuery/Pager/PagerDemo.html
 *
 * Copyright (c) 2008-2009 Jon Paul Davies
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Read the related blog post and contact the author at http://www.j-dee.com/2008/12/22/jquery-pager-plugin/
 *
 * This version is far from perfect and doesn't manage it's own state, therefore contributions are more than welcome!
 *
 * Usage: .pager({ pagenumber: 1, pagecount: 15, buttonClickCallback: PagerClickTest });
 *
 * Where pagenumber is the visible page number
 *       pagecount is the total number of pages to display
 *       buttonClickCallback is the method to fire when a pager button is clicked.
 *
 * buttonClickCallback signiture is PagerClickTest = function(pageclickednumber) 
 * Where pageclickednumber is the number of the page clicked in the control.
 *
 * The included Pager.CSS file is a dependancy but can obviously tweaked to your wishes
 * Tested in IE6 IE7 Firefox & Safari. Any browser strangeness, please report.
 */
(function($) {
	var pageN="";
	$.fn.getCurPage = function() {
		var curPage = this.find(".pgCurrent").text();
		return curPage;
	};
	$.fn.clickCurPage = function() {
		var myId = $(this).attr("id");
		$(this).trigger(myId+"/curPage/click");
	};
	$.fn.pager = function(_opts) {

		var setting = {
			pageNum : 1,
			pageCount : 1,
			pageSize: 15,
			click : null
		};
		var self=$(this);
		
		
		
		var obj = null;
		$.extend(setting, _opts);

		var myId = $(this).attr("id");
		self.off(myId+"/curPage/click");
		self.on(myId+"/curPage/click",function(){
			if ($.isFunction(setting.click)) {
				setting.click(setting.pageNum, setting.pageCount);
			}
		});
		
		return this.each(function() {
			obj = this;
			renderpager();
		});

		function buttonClickCallback(pageNum) {
			setting.pageNum = parseInt(pageNum);
			renderpager();

			if ($.isFunction(setting.click)) {
				
				setting.click(pageNum, setting.pageCount);
			}
		}
		
		function renderpager() {
			var $pager = $('<ul class="pages"></ul>');
			if(setting.pageCount>0){
				if($("#pageSizeReal").val()!=undefined){
					setting.pageSize=$("#pageSizeReal").val();
				}
				
				if($('#pagesize').val()!=undefined && $('#pagesize').val()!=0){
					setting.pageSize=$("#pagesize").val();
				}
				
				if(pageN>0){
					setting.pageSize=pageN;
				}
				var opt="";
				var str=[5,15,30,50,100,200,500];
				for(var a=0;a<str.length;a++){
					if(setting.pageSize==str[a]){
						opt+='<option value='+str[a]+' selected>'+str[a]+'条/页</option>'
					}else{
						opt+='<option value='+str[a]+'>'+str[a]+'条/页</option>'
					}
				}
				$pager.append('<li class="page-number"><select id="pageSizeReal">'+opt+'</select></li>');
			}
			$pager.append(renderButton("first", "首页")).append(
					renderButton("prev", "上一页"));

			var startPoint = 1;
			var endPoint = 5;

			if (setting.pageNum > 3) {
				startPoint = setting.pageNum - 2;
				endPoint = setting.pageNum + 2;
			}

			if (endPoint > setting.pageCount) {
				startPoint = setting.pageCount - 4;
				endPoint = setting.pageCount;
			}

			if (startPoint < 1) {
				startPoint = 1;
			}

			for ( var page = startPoint; page <= endPoint; page++) {
				var currentButton = $('<li class="page-number">' + (page)
						+ '</li>');
//				if(page==startPoint+5){
//					currentButton = $('<li class="page-number">...</li>');
//					page == currentButton;
//				}else{
					page == setting.pageNum ? currentButton.addClass('pgCurrent')
							: currentButton.click(function() {
								buttonClickCallback(this.firstChild.data);
							});	
				//}
			
				
				currentButton.appendTo($pager);
			}

			
          //$pager.append('<input type="text"  id="gotoPage"/>');
			
			$pager.append(renderButton("next", "下一页")).append(
					renderButton("last", "末页"));
			$pager.append('<li class="page-number" id="pageCountYes">共'+setting.pageCount+'页</li>');
			var read ="";
			if(setting.pageCount==0){
				read="readonly";
			}
			$pager.append('<li class="page-number"><input id="pageGo" type="text" size="2"   value=""  '+read+'></li>');
			$pager.append(renderButton("goPage", "跳转"));
//			当前页数如果小于等于1页就整个分页隐藏
			if(setting.pageCount <= 1){
				$pager.hide();
			}
			// return $pager;
			$(obj).empty().append($pager);
			$('.pages li').not("[id='pageCount']").mouseover(function() {
				$(".pages").css({cursor:"pointer"});
				//document.body.style.cursor = "pointer";
			}
			).mouseout(function() {
				$(".pages").css({cursor:"auto"});
				//document.body.style.cursor = "auto";
			});
			
			$pager.find("#pageSizeReal").on("change",function(){
				setting.pageSize=$(this).val();
				$(obj).find('#pagesize').attr("value",setting.pageSize);
				pageN =setting.pageSize;
				setting.click(setting.pageNum, setting.pageCount);
				
			});
			
			$pager.find("#pageGo").change(function(){
				var pageGo=$(this).val();
				var test=/^[0-9]*$/;
				if(pageGo!=""){
					if(!test.test(pageGo)){
						alert("只能输入数字！");
						$pager.find("#pageGo").val("");
					}else{
						if(pageGo>setting.pageCount){
							$(obj).find("#pageGo").val(setting.pageCount);
						}
					}
				}
			});
		}
		function renderButton(labelId, labelText) {
			var $Button = $('<li class="pgNext">' + labelText + '</li>');
			var destPage = 1;
			var page=1;
			// work out destination page for required button type
			switch (labelId) {
			case "first":
				destPage = 1;
				break;
			case "prev":
				destPage = setting.pageNum - 1;
				break;
			case "next":
				destPage = setting.pageNum + 1;
				break;
			case "last":
				destPage = setting.pageCount;
				break;
			}
			
			// disable and 'grey' out buttons if not needed.
			if (labelId == "first" || labelId == "prev") {
				setting.pageNum <= 1 ? $Button.addClass('disable') : $Button
						.click(function() {
							buttonClickCallback(destPage);
						});
			} else if(labelId == "goPage") {
				$Button.click(function() {
						page=$(obj).find('#pageGo').val();
						if(page!=""){
							if(page>setting.pageCount){
								page=setting.pageCount;
								$(obj).find('#pageGo').attr("value",setting.pageCount);
							}
							setting.pageNum=page;
							if(pageN>0){
								setting.pageSize=pageN;
							}
							buttonClickCallback(setting.pageNum);
						}
				});
			} else {
				setting.pageNum >= setting.pageCount ? $Button
						.addClass('disable') : $Button.click(function() {
					buttonClickCallback(destPage);
				});
			}
			//没有数据时全部按钮置灰色
			if(setting.pageCount==0){
				$Button.addClass('pgEmpty');
			}
			return $Button;
		}
	};
	
})(jQuery);
