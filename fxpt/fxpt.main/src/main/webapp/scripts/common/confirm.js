(function() {
	"use strict";
	define(function() {
		var defaultSetting = {
				title:"标题",
				content:"内容",
				isMove:false, //是否拖动
				widthSize:400,
				callback:function(obj){ }
		};
		/**
		 * 弹出框
		 */
		$.extend({
			"confirm" : function(opts) {
				
				var setting = {};
				
				$.extend(setting,defaultSetting,opts);
				
				if(setting.widthSize < 400)
					setting.widthSize = 400;
				var $content = $("<span>" + setting.content + "</span>");
				
				var $modal        = $("<div>",{"class":"modal fade tip-modal","data-backdrop":"static"});
				var $modal_dialog = $("<div>",{"class":"modal-dialog"});
				var $modal_content= $("<div>",{"class":"modal-content","style":"width:"+setting.widthSize+"px;"});
				
				$modal_content.append(buildHeader(setting.title,setting.isMove));
				$modal_content.append(buildBody($content));
				$modal_content.append(buildFooter());
				
				$modal_dialog.append($modal_content);
				$modal.append($modal_dialog);
				
				
				if(setting.callback)
					setting.callback($modal);
				
				/* 隐藏之后移除内容*/
				$modal.modal("show").on('hidden.bs.modal',function(){	
					$content.remove();
				});	
				
				return $modal;
				
				function buildHeader(title,isMove){
					/* 创建头部*/
					var $modal_header = $("<div>",{"class":"modal-header"});
					if(isMove)
						$modal_header = $("<div>",{"class":"modal-header","onmouseover":"comDrag(this)"});
					var $header_button= $("<button>",{"class":"close","type":"button","data-dismiss":"modal","aria-hidden":"true"});
					var $title_h4     = $("<h4>",{"class":"modal-title"});
					$header_button.html("&times;");
					$title_h4.html(title);
					$modal_header.append($header_button).append($title_h4);
					return $modal_header;
				}
				
				function buildBody(content){
					/* 创建主体 */
					var $modal_body   = $("<div>",{"class":"modal-body"});
					$modal_body.html(content);
					return $modal_body;
				}
				
				function buildFooter(){
					/* 创建尾部 */
					var $modal_footer = $("<div>",{"class":"modal-footer"});
					var $button_ok    = $("<button>",{"class":"btn btn-default","name":"yes","type":"button"});
					var $button_cancel= $("<button>",{"class":"btn btn-default","name":"cancel","type":"button","data-dismiss":"modal"});
					$button_ok.html("确定");
					$button_cancel.html("取消");
					$modal_footer.append($button_ok).append($button_cancel);
					
					return $modal_footer;
				}
				
			}
		});
	});

})();