(function() {
	"use strict";
	define([ 'jquery','jquery-ui', 'ajax', "dialog", 'logger', 'common',"uploadify","cookie","datepickerjs", "selectjs","stringUtil" ], function($,$j,
			ajax, dialog, log, common,uploadify,cookie,datepickerjs,selectjs,stringUtil) {
		var self = undefined;
		function doAjax(opts) {
			var showMsg = true;
			if (!opts.successMsg)
				showMsg = false;

			var ajaxOpts = {
				url : opts.url,
				successMsg : {
					show : showMsg,
					header : {
						show : false
					},
					tip_txt : opts.successMsg
				},
				type : opts.method,
				callback : function(data) {
					if (opts.callback)
						opts.callback(data);
				}
			};
			if (opts.data)
				ajaxOpts["data"] = JSON.stringify(opts.data);
			if (opts.dataType)
				ajaxOpts["dataType"] = opts.dataType;
			ajax(ajaxOpts);
		};
		
		var o = self = {
			log : function(msg) {
				log(msg);
			},
			/*******************************************************************
			 * 确认对话框 
			 * @param title:对话框标题 
			 * @param msg:对话框消息 
			 * @param callback:确认后的回调函数 
			 * @param bnt1Text:第一个按钮文字
			 * @param callback2:取消的回调函数 
			 * @param bnt2Text：第二个按钮文字 
			 * @author:李贵庆
			 ******************************************************************/
			confirm : function(title, msg, callback1, btn1Text, callback2,
					btn2Text) {
				return dialog.tipmodal({
					header : {
						show : true,
						txt : title
					},
					tip_txt : msg,
					canmove : true, // 是否拖动
					icon_info : 'warning',
					footer : {
						show : true,
						buttons : [ {
							type : 'submit',
							txt : btn1Text || "确定",
							sty : 'primary',
							callback : function() {
								$(this).trigger('close');
								if (callback1) {
									callback1();
								}
							}
						}, {
							type : 'button',
							txt : btn2Text || "取消",
							sty : 'default',
							callback : function() {
								$(this).trigger('close');
								if (callback2) {
									callback2();
								}
							}
						} ]
					}
				});
			},
			alert:function(msg,type){
				return dialog.tipmodal({
					icon_info : type?type:'warning',//warning error success 
					tip_txt : msg,
					header : {
						show : false
					},
					footer : {
						show : true,
						buttons : [ {
							type : 'button',
							txt : "确定",
							sty : 'default',
							callback : function() {
								$(this).trigger('close');
							}
						} ]
					}
				});
			},
			tips : function(msg) {
				return dialog.tipmodal({
					header : {
						show : false
					},
					tip_txt : msg,
					canmove : false, // 是否拖动
					footer : {
						show : false
					}
				});
			},
			moment : function(msg, icon,callback) {
				return dialog.fadedialog({
					tip_txt : msg,
					icon_info : icon,
					callback:callback
				});
			},
			modal : function(title, content, callback, okBtnTxt, size,autoclose) {
				var $obj =  dialog.modal({
					size : size ? size : 'md',// sm md lg
					header : {
						show : true,
						txt : title
					},
					footer : {
						show : true,
						buttons : [ {
							type : 'submit',
							txt : okBtnTxt || "确定",
							sty : 'primary',
							callback : function() {
								if (callback && callback())
									$(this).trigger('close');
							}
						}, {
							type : 'button',
							txt : "取消",
							sty : 'default',
							callback : function() {
								$(this).trigger('close');
							}
						} ]
					},
					body : content
				});
				$obj.find('.selectpicker').selectpicker();
				//$obj.find('div.form-group:last').css({"padding-bottom": "0px","margin-bottom":"0px"});
				return $obj;
			},
			/**
			 * 得到当前激活的菜单 返回jquery a 标签对象
			 */
			getCurMenu : function() {
				return $('#menu li.active:eq(0) >a');
			},
			/*******************************************************************
			 * 提交Json数据，返回Json数据 
			 * @param url:执行POST的url 
			 * @param data:被post的json数据
			 * @param successMsg:执行成功的提示消息 
			 * @param callback:执行成功后的回调函数
			 * 
			 ******************************************************************/
			postJson : function(url, data, successMsg, callback) {
				this.post(url, data, "json", successMsg, callback);
			},
			/*******************************************************************
			 * 提交Json数据，返回Html数据 
			 * @param url:执行POST的url 
			 * @param data:被post的json数据
			 * @param successMsg:执行成功的提示消息 
			 * @param callback:执行成功后的回调函数
			 * 
			 ******************************************************************/
			postJsonHtml : function(url, data, successMsg, callback) {
				this.post(url, data, "html", successMsg, callback);
			},
			post : function(url, data, dataType, successMsg, callback) {
				doAjax({
					url : url,
					data : data,
					dataType : dataType,
					successMsg : successMsg,
					showMsg : successMsg ? true : false,
					callback : callback,
					method : "POST"
				});
			},
			/*******************************************************************
			 * @param url:执行POST的url 
			 * @param data:被post的json数据 
			 * @param successMsg:执行成功的提示消息
			 * @param callback:执行成功后的回调函数
			 ******************************************************************/
			putJson : function(url, data, successMsg, callback) {
				doAjax({
					url : url,
					data : data,
					successMsg : successMsg,
					showMsg : successMsg ? true : false,
					callback : callback,
					method : "PUT"
				});
			},
			getJson : function(url, callback, beforeMsg, data) {
				this.get(url, "json", callback, beforeMsg || " ", "", data);
			},
			getHtml : function(url, callback, beforeMsg, data) {
				if(url.indexOf("?")>0){
					url=url+"&ran="+Math.random();
				}else{
					url=url+"?ran="+Math.random();
				}
				this.get(url, "html", callback, beforeMsg || " ", "", data);
			},
			get : function(url, dataType, callback, beforeMsg, afterMsg, data) {
				ajax({
					url : url,
					dataType : dataType,
					data : data ? data : {},
					beforeSendMsg : {
						show : (beforeMsg ? true : false),
						tip_txt : beforeMsg
					},
					successMsg : {
						show : (afterMsg ? true : false),
						tip_txt : afterMsg
					},
					callback : function(data) {
						if (callback)
							callback(data);
					}
				});
			},
			/**
			 * 通过后台html代码生成对话框 
			 * @param url:读取html的地址 
			 * @param dialogId:对话框id，不带# 
			 * @param callback:回调函数
			 * @author:李贵庆
			 */
			getHtmlDialog : function(url, dialogId, callback,closeCallback) {
				this.getHtml(url, function(html) {
					var $html = $(html);
					$("body").append($html).one('mouseover',"#modal-header",function(){
						comDrag(this);
					});;
					$("#" + dialogId).modal("show").on('hidden.bs.modal',function() {
						$html.remove();
						if(closeCallback)
							closeCallback();
					});
					//$html.find('div.form-group:last').css({"padding-bottom": "0px","margin-bottom":"0px"});
					if (callback) {
						callback($html);
					}
				});
			},
			/*******************************************************************
			 * @param url:执行删除的URL 
			 * @param beforeMsg:删除执行前的提示信息 
			 * @param afterMsg:删除成功后的提示信息
			 * @param callback:function 删除成功后的回调函数 
			 * @author 李贵庆
			 ******************************************************************/
			remove : function(url, beforeMsg, afterMsg, obj, callback) {
				this.confirm("删除操作", beforeMsg,
						function() {
							ajax({
								url : url,
								successMsg : {
									show : true,
									header : {
										show : false
									},
									tip_txt : afterMsg || '删除成功！'
								},
								type : "DELETE",
								data : JSON.stringify(obj),
								callback : function(data) {
									if (data.status && data.status.success
											&& callback) {
										callback(data);
									}
								}
							});
						});
			},
			fileUpload : function(url, elementId, callback) {
				this.upload(url, elementId, function(data) {
					if (callback)
						callback(data);
				});
			},
			imgUpload : function(url, elementId, callback) {
				this.upload(url, elementId, function(data) {
					if (callback)
						callback(data);
				}, "正在上传图片，请稍候...", "图片上传成功", "图片上传");
			},
			upload : function(url, elementId, callback, beforMsg, afterMsg,
					errorTitile) {
				ajax({
					url : url,
					fileElementId : elementId,
					dataType : 'text',
					type : "POST",
					beforeSendMsg : {
						show : true,
						tip_txt : beforMsg || "正在上传文件，请稍候..."
					},
					successMsg : {
						show : true,
						header : {
							show : false
						},
						tip_txt : afterMsg || "文件上传成功"
					},
					errorMsg : {
						tip_txt : '上传失败！',
						header : {
							show : true,
							txt : errorTitile || '文件上传'
						}
					},
					callback : function(data) {
						if (callback)
							callback(data);
					}
				});
			},
			/**
			 * 将hmtl更新到container中,替换原来的html
			 * 
			 * @param container
			 * @param html
			 * @author 李贵庆
			 */
			appendToView : function(container, html) {
				if (container) {
					
					$(container).children('.page-content').remove();
					$(container).prepend(html);
				} else {
					$(".page-content:eq(0)").html(html);
				}
			},
			renderPager : function(opts) {
				var defOpts = {
					"containerObj" : undefined,
					"pageObj" : undefined,
					"callBack" : function(page) {
					}
				};

				$.extend(defOpts, opts);

				defOpts.containerObj = $("<div></div>").append(defOpts.containerObj.clone());
				
				var _pageNum = parseInt(defOpts.containerObj.find("#pageNum").val());
				_pageNum=_pageNum==0?1:_pageNum;
				var _pageCount = parseInt(defOpts.containerObj.find(
						"#pageCount").val());
				var _pageRows = parseInt(defOpts.containerObj.find("#pageRows").val());
				var pageSize = window.app["pageSize"]?window.app["pageSize"]:10;
				if(defOpts.containerObj.find("#pagesize").size()){
					pageSize = parseInt(defOpts.containerObj.find("#pagesize").val());
				}
				if($("#pageSizeReal option:selected").length>0){
					pageSize= $("#pageSizeReal option:selected")?$("#pageSizeReal option:selected").val():10;
				}
				if(_pageRows>0){
					if (_pageRows % pageSize > 0) {
						_pageCount = parseInt(_pageRows/pageSize) + 1;
					}else{
						_pageCount = parseInt(_pageRows/pageSize);
					}
				}
				defOpts.pageObj.pager({
					pageNum : _pageNum,
					pageCount : _pageCount,
					click : function(pageNum, pageCount) {
						if($("#pageSizeReal option:selected").length>0){
							pageSize= $("#pageSizeReal option:selected")?$("#pageSizeReal option:selected").val():10;
						}
						var page = {};
						page["pageSize"] = pageSize;
						page["curSize"] = pageNum;
						page["pageCount"] =pageCount;
						defOpts.callBack.call(this,page);
					}
				});
			},
			newPage:function($container){
				var page={};
				
				page["pageSize"] = window.app["pageSize"]?window.app["pageSize"]:10;
				 
				page["data"] = {};
				page["curSize"] = 1;
				if($container){
					var pageindex = $container.find("#pageNum");
			    	if(pageindex!=null && pageindex.val()!=undefined ){
			    		page["curSize"]=pageindex.val();
			    	}
			    	
					var pagerSizeObj = $container.find("#pagesize");
		    		if(pagerSizeObj.size()){
		    			page["pageSize"] = parseInt(pagerSizeObj.val());
					}					
				}
				if($("#pageSizeReal option:selected").length>0){
					page["pageSize"]= $("#pageSizeReal option:selected")?$("#pageSizeReal option:selected").val():10;
				}

				return page;
			},
			multiImport:function($fileObj,paramObj){
				var opts = {
					swf:window.app.rootPath+"static/resources/uploadify/uploadify.swf",
					uploader:"",
					fileObjName:"file",
					method:"post",
					debug:true,
					buttonText:"选择文件上传",
					itemTemplate:'<div id="cntest-fxpt-queue-${fileID}" name="cntest-fxpt-queue" class="uploadify-queue" style="width:100%;clear:both;" data-fileId="${fileID}" data-fileName="${fileName}">\
						  <div class="head" style="width:100%">\
				    <div  style="float:left;width:20%;">${fileName}</div>\
				    <div id="progress" class="uploadify-progress" style="float:left;width:70%;">\
				      <div class="uploadify-progress-bar" style="width: 1%;"></div>\
				    </div>\
				    <div id="view" style="float:left;width:5%;">0%</div>\
				    <div style="float:right;width:5%;text-align:right;">\
					<a href="javascript:$(\'#${instanceID}\').uploadify(\'cancel\', \'${fileID}\');\
						$(\'#cntest-fxpt-queue-${fileID}\').remove();">X</a></div>\
				  </div>\
				  <div class="content" style="width:100%; clear:both; margin-top:5px;"></div>\
				</div>',
					onUploadSuccess:function(file,data,response){
						var myData = $.parseJSON(data);
						
						if(!myData.contentInfo.templateFile){
							var $span=$("<span style='color:#F00;display:block;'></span>");
							$span.text("该文件不能导入，与模板不匹配！不能进行导入。");
							$('#cntest-fxpt-queue-'+file.id).find(".content").append($span);
						}else{
							var $span=$("<span style='color:#0C3;display:block;'></span>");
							$span.text("上传成功！");
							$('#cntest-fxpt-queue-'+file.id).find(".content").append($span);
						}
												
						if(!myData.contentInfo.existTestPaper){
							var $span=$("<span style='color:#F00;display:block;'></span>");
							$span.text("该文件对应的双向细目表不存在，请先导入双向细目表。");
							$('#cntest-fxpt-queue-'+file.id).find(".content").append($span);
						}
						
						
						$('#cntest-fxpt-queue-'+file.id).data("myData",myData);
					},
					onUploadProgress:function(file, fileBytesLoaded, fileTotalBytes){
						var percent=parseInt(fileBytesLoaded/fileTotalBytes*100)+"%";
						$("div[name='cntest-fxpt-queue'][data-fileId='"+file.id+"'] .uploadify-progress-bar").css("width",percent);
						$("div[name='cntest-fxpt-queue'][data-fileId='"+file.id+"'] #view").html(percent);
					},
					onSelect:function(file){
						//self.log(file.name)
					}
				};
				
				$.extend(opts,paramObj);
				
				//this.log(JSON.stringify(opts))
				$fileObj.uploadify(opts);
			},
			initUI:function($htmlObj){
				$htmlObj.find('.date-picker').datetimepicker({
					autoclose : true,
					format : 'yyyy-mm-dd',
					weekStart : 1,
					todayBtn : 1,
					minView : 3
				}).next().on('click', function() {
					$(this).prev().focus();
				});
				
				$htmlObj.find('.selectpicker').each(function(i,n){
					var $select = $(this);
					if($select.attr('disabled')){
						$select.selectpicker().css({'bacground-color':'#eee'});
						$select.next().css({'bacground-color':'#eee'});
					}else{
						$select.selectpicker();
					}
				});
				$htmlObj.find('div.query-form a.btn:eq(1)').click(function(){
					var $form = $(this).parent().prev();
					$form.find('input').val('');
					$form.find('select.selectpicker').each(function(){
						var $select = $(this);
						$select.children()[0].selected =true;
						$select.selectpicker("refresh");
						
					});
					//$form.find('select.selectpicker').selectpicker("全部");
				});
				$htmlObj.find('div.query-form a.btn:eq(2)').click(function(){
					$(this).hide().next().show().parent().prev().find('form>div:gt(0)').animate({height:'toggle',opacity:'toggle'},200);					
				}).next().click(function(){
					$(this).hide().prev().show().parent().prev().find('form>div:gt(0)').animate({height:'toggle',opacity:'toggle'},200);
				});
			},
			/**
			 * 更新数据表格中的数据
			 * 
			 * @param rowsId
			 *            数据表格的rowId,即tbody的id
			 * @param $container
			 *            数据表格的容器 jquery对象
			 * @param $html
			 *            待更新的html数据 jquery对象
			 * @param callback
			 *            更新完成后的回调
			 * @author 李贵庆
			 */
			refreshDataGrid:function(rowsId,$container,$html,callback){
				var $rows = $html.find('#' + rowsId);
				var $dataGrid = $container.find('#' + rowsId);
				$dataGrid.children().remove();
				if($rows.size()){								
					$dataGrid.append($rows.children());
				};
				
				$dataGrid.parent().next().children().remove();
				$dataGrid.parent().next().append($rows.parent().next().children());
				if($.isFunction(callback))
				    callback.call(this);
			},
			ajaxExecute:function(entry,data,dataType,finishedCallback,$target){
				var ctrl = this;
				if($target){
					ajax({
	      				url : '/executor/progress/' + entry,
	      				dataType : dataType||'html',
	      				beforeSendMsg:{show:false},
	      				successMsg:{show:false},
	      				errorMsg:{show:false},
	      				data : data ? data : {},
	      				callback : function(result) {
	      					if(!dataType || dataType === 'html'){
	      						var $p = $(result);
	      						$target.find('table.progress').remove();
	      						$target.append($p);
	      						if($p.find(':hidden').val() != 'true'){
	      							setTimeout(function(){
	      								ctrl.ajaxExecute(entry,data, dataType,finishedCallback,$target);
	      							},3000);
	      						}else{
	      							if(finishedCallback)
	      								finishedCallback();
	      						}      						
	      					}
	      				}
	        	   });
				}else{
					ajax({
	      				url : '/executor/progress/' + entry,
	      				dataType : dataType||'html',
	      				beforeSendMsg:{show:true,tipsType:'progressbar'},
	      				successMsg:{show:false},
	      				errorMsg:{show:false},
	      				data : data ? data : {},
	      				callback : function(data) {
	      					
	      				}
	        	   });					
				}

			},
			ajaxExecuteprogress:function(entry,data,dataType,finishedCallback,$target){
				var ctrl = this;
					ajax({
	      				url : '/etl/execProgress',
	      				dataType : dataType||'html',
	      				beforeSendMsg:{show:false},
	      				successMsg:{show:false},
	      				errorMsg:{show:false},
	      				data : data ? data : {},
	      				callback : function(result) {
	      					if(!dataType || dataType === 'html'){
	      						var $p = $(result);
	      						var percent = $p.find(':hidden').val();
	      						$target.find(".dialog-body").html($p);
	      						
	      						var bar,Label;
	      						bar = $target.find("#progressbar");
	      						Label = $target.find(".progress-label");
	      						Label.text(percent + "%");
	      						 
	      						bar.progressbar({
	      							value: percent++,
	      						});
	      						
	      						if(percent != '101'){
	      							setTimeout(function(){
	      								ctrl.ajaxExecuteprogress(entry,data, dataType,finishedCallback,$target);
	      							},1000);
	      						}else{
	      							if(finishedCallback)
	      								finishedCallback(result);
	      						}      						
	      					}
	      				}
	        	   });
			},
			
			selectAll:function(container,id,name){
		    	container.on('click','#'+id,function(){
		    		var gardeids = $("."+name);
		    		//判断点击是 全选or全不选
		    		if(this.checked){
		    			//全选
		    			for(var i=0;i<gardeids.length;i++){
		    				gardeids[i].checked=1;
		    			}
		    		}else{
		    			//全不选
		    			for(var i=0;i<gardeids.length;i++){
		    				if(gardeids[i].checked==1){
		    					gardeids[i].checked=0;
		    				}else{
		    					gardeids[i].checked=1;
		    				}
		    			}
		    		}
		    	});
		    },getPageIndex:function(container,is){
		    	if(container){
		    		var $inputVal = container.find("input#pageNum");
		    		if($inputVal!= undefined ){
		    			if(is){
		    				return $inputVal.val();
		    			}else{ 
		    				return "PageIndex="+$inputVal.val();
		    			}
		    		}else{
		    			if(is){
		    				return $inputVal.val();
		    			}else{ 
		    				return "PageIndex=1";
		    			}
		    		}
		    	}
		    },getindexPage:function(container){
		    	if(container){
		    		var $in = container.find("input#pageIndex");
		    		if($in){
		    			if($in.val() != undefined && $in.val() != "undefined" && $in.val() != "null" && $in.val() != ""){
		    				return $in.val();
		    			}
		    			return 1;
		    		}else{
		    			return 1;
		    		}
		    	}else{
	    			return 1;
	    		}
		    }
		};
		return $.extend(o,stringUtil);
	});
})();