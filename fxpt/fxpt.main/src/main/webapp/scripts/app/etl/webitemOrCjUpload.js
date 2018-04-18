(function() {
	"use strict";
	var model = [ "jquery", "ajax", "controller","webupload","dialog"];
	
	var radioTHMLForCj = '<div class="content" style="clear:both;"> <div id="myradio" class="content" ><span>'+
		'<label style="float:left;">成绩表已存在，是否要覆盖导入  </label>'+
		'<label class="radio-inline">'+
			'<input type="radio"  name="t" value="1"/>不是'+
		'</label>'+
		'<label class="radio-inline">'+
			'<input type="radio" name="t" checked value="2"/>是'+
		'</label>'+
		'<label>，  覆盖导 入时，相同的学生信息将会被覆盖</label>'+
	'</span></div> '+
		'</div>';
	var radioTHMLForXMB = '<div class="content" style="clear:both;"> <div id="myradio" class="content"><span>'+
				'<label style="float:left;">细目表已存在，是否要重新导入  </label>'+
				'<label class="radio-inline">'+
					'<input type="radio" checked name="t" value="1"/>不是'+
				'</label>'+
				'<label class="radio-inline">'+
					'<input type="radio" name="t" value="2"/>是'+
				'</label>'+
				'<label>，  重新导 入该科目成绩将会被删除</label>'+
			'</span></div> '+
				'</div>';
	
	var importTipHtmlForCJ = '导入须知:\
	<br/>1.选择本地excel文件，按住Ctrl或Shift键可以上传多份文件。\
	<br/>2.导入文件格式必须使用导入模板格式，否则可能上传出错。 ';
	var importTipHtmlForXMB = '导入须知:\
	<br/>1.选择本地excel文件，按住Ctrl或Shift键可以上传多份文件。\
	<br/>2.导入文件格式必须使用导入模板格式，否则可能上传出错。';

	define(model, function($, ajax, ctrl,webupload,dialog) {
		function UI(_opts) {
			
			var id="filelist_browse";
			var uploader =  undefined
			var opts = {
				title : "",
				examId : undefined,
				importType : undefined,
				schemeType : 1,
				fileTypeDesc : "只能选择excle和dbf文件",
				fileTypeExts : "*.xls;*.xlsx;*dbf",
				sessionId : undefined,
				sessionName : undefined,
				callBack : undefined
			};
			$.extend(opts, _opts);
			var url = "/upload/ui/" + _opts.schemeType + "/m";
		
			ctrl.getHtml(url, function(html) {
				var $html = $(html);
				var $dialog = ctrl.modal(_opts.title, $html, function() {
					var files= uploader.getFiles("complete");//获取上传成功的文件（不包括取消的文件）
					var hasError = false;
					$.each(files,function(i,file){
						if(file.serviceData.validateResult.hasError){
							hasError=true;
						}
					});
					if(!hasError){
						$dialog.find(".dialog-footer button:first").remove();
						$dialog.find(".dialog-footer button:first").text("确定");
						$dialog.find("#"+id+" div.webuploader-pick").remove();
						$dialog.find(".uploadify-button-text").remove();
						executorImport(files,0);//递归导入
					}else{
						ctrl.moment("存在错误文件，请删除之后再进行导入操作","warning");
						return false;
					}
				}, "导入", "lg");
				
				importTip(opts);
				
				var sessionId = $dialog.find("#sessionId").val();
				var sessionName = $dialog.find("#sessionName").val();
				var url = window.app.rootPath + "upload/true/" +_opts.examId + "/" + _opts.schemeType + "?" + sessionName + "=" + sessionId;
			    uploader = webupload.create({
			        // swf文件路径
			        swf: '/fxpt/static/scripts/lib/webuploader-0.1.5/Uploader.swf',
			        runtimeOrder:"html5,flash",
			        // 文件接收服务端。
			        server: url,
			        // 不压缩image
			        resize: false,
			        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
			        pick:  {
			        	id:$dialog.find("#"+id),
			        	innerHTML:"点击选择文件"
			        },
			        accept:{
			        	title:"",
			        	extensions:opts.fileTypeExts
			        },
			        auto:true,
			        duplicate:true,//去重
			        threads:1,//允许同时最大上传进程数
			        fileVal:"file",//后台接收值
			       
			    });
			    
			    function executorImport($files, idx) {
			    	if(idx+1>$files.length){
			    		_opts.callBack();
			    		return ;
			    	}
			    	var file = $files[idx];
					var li = $dialog.find("ul li#"+file.id);
					var span = $dialog.find("ul li#"+file.id).find("div.content span");
					
					var isOverlayimport = false;
					var isExistContent = file.serviceData.validateResult.existContent;
					var isHasError = file.serviceData.validateResult.hasError;
					
					if (isExistContent) {
						var $radioObj = li.find("#myradio :radio:checked");
						isOverlayimport = $radioObj.val() === "2";
						li.find("#myradio :radio").attr("disabled", true);
					}
					if (isHasError) {
						li.text("导入失败，因为文件有错,请看上面的文件信息排除错误后再导入。");
						return;
					}
					
					var tmpFile = {};
					tmpFile.fileName = file.serviceData.fileInfo.name;
					tmpFile.fileRealName=file.serviceData.fileInfo.name;
					tmpFile.desFileName = file.serviceData.fileInfo.desFileName;
					tmpFile.suffix = file.serviceData.fileInfo.suffix;
					tmpFile.schemeType = _opts.schemeType;
					tmpFile.examId = _opts.examId;
					tmpFile.importType = _opts.importType;
					tmpFile.overlayImport = isOverlayimport;
					var url = "/etl/batchexec";
					ctrl.postJson(url, tmpFile, "", function(data) {
					/*	if(data.processResult.hasError){
							span.html("<span style='color: red;'>"+data.processResult.etlLog.statusMessage+"</span>");	
						}else{*/
							span.html(data.processResult.message);
						//}
						span.parent().show();
						if (data.processResult.etlLog) {
							downLoadLogs(span, _opts.examId, data.processResult.etlLog.logContent, _opts.schemeType);
						}
						executorImport($files,idx+1);
					});
				}
			    $dialog.find("#"+id).append('<ul id="'+id+'_filelist" class="plupload_filelist"></ul>');
			    $(".dialog-footer", $dialog).find("button:eq(0)").attr("disabled",true);
			    //添加文件事件
			    uploader.on("fileQueued",function(file){
			    	var inputHTML="";
			    		inputHTML+='<li id="' + file.id + '" style="     overflow: hidden;   margin-top: 10px;    background-color: rgba(66, 139, 202, 0.11);">\
										<div class="plupload_file_name plupload_delete"><span>' + file.name + '</span><a href="javascript:;" style=" float: right;top: 5px;right: 5px;" class="glyphicon glyphicon-remove" ></a></div>\
										<div class="plupload_file_action"><a href="#"></a></div>\
										<div class="head" style="width:100%">\
										    <div id="progress" class="uploadify-progress" style="float:left;width:90%; background-color: darkgray;">\
										      <div class="uploadify-progress-bar" style="width: 0%;"></div>\
										    </div>\
										    <div id="view" style="float:left;width:10%;padding-left: 10px;" class="uploadify-progress-cuess">0%</div>\
										    <div style="float:right;width:5%;text-align:right;"></div>\
										</div>\
										<div class="plupload_file_status">正在上传</div>\
								</li>';
					$("#"+id+"_filelist").append(inputHTML);
					
					
			    });
			    /** 进度条事件*/
			    uploader.on("uploadProgress",function(file,progress){
			    	console.log(progress);
			    	$("ul li#"+file.id).find(".uploadify-progress-bar").css("width",(progress*100)+"%");
			    	$("ul li#"+file.id).find(".uploadify-progress-cuess").html((progress*100).toFixed(2)+"%");
			    });
			    /** 上传成功事件*/
			    uploader.on( 'uploadSuccess', function( file,response ) {
			    	var json = JSON.parse(response._raw);
			    	file["serviceData"]=json;
			    	var inputHTML="";
					if(response  && response.validateResult){
						var b = false;
						if(response.validateResult.hasError){
							b=true;
							inputHTML += '<div class="content" style="width:100%; clear:both; margin-top:5px;"><span style="display: block;">'+response.validateResult.messages.join(",")+'</span></div>';
						}else{
							if (response.validateResult.existContent) {
								b=true;
								var $radioTHML = getRadioHTML();
								$radioTHML.find(":radio").attr("name", file.id);
								inputHTML+=$radioTHML.html();
							}
						}
						if(!b){
							inputHTML += '<div class="content" style="width:100%; clear:both; margin-top:5px;"><span style="display: block;"></span></div>';
						}
					} 
					if(response.status){
			    		$("#"+file.id,$dialog).find("div.plupload_file_status").html("<span style='color: red;'>"+response.status.msg+"</span>");
			    		file.serviceData["validateResult"] ={hasError:true};
					}else{
			    		$("#"+file.id,$dialog).find("div.plupload_file_status").html("上传成功");
			    	}
					$("#"+file.id,$dialog).append(inputHTML);
			    });
			    
			    /** 文件上传失败时出发 */
			    uploader.on("uploadError",function(file,code){
			    	$("#"+file.id).find("div.plupload_file_status").html("<span style='color: red;'>上传失败</span>");
			    });
			    
			    /** 验证错误文件 */
			    uploader.on("error",function(code,file){
			    	 if("Q_TYPE_DENIED"==code){
			    		 ctrl.alert(file.name+" 文件类型不匹配,请选择("+opts.fileTypeExts+")的文件");
			    	 }else if("Q_EXCEED_SIZE_LIMIT"==code){
			    		 ctrl.alert(file.name+"超出上传文件的大小");
			    	 }else if("Q_EXCEED_NUM_LIMIT"==code){
			    		 ctrl.alert("超出上传文件的数量");
			    	 }
			    });
			    
			    /** 队列文件全部上传成功后触发 */
			    uploader.on("uploadFinished",function(){
			    	$(".dialog-footer", $dialog).find("button:eq(0)").attr("disabled",false);
			    });
			    
			    /** 删除事件绑定 */
			    $("#"+id).on("click","div.plupload_delete a",function(e) {
			    	//uploader.getFiles("complete"); 获取上传成功的文件（不包括取消的文件）
					var li = $(this).parent().parent();
					var file = uploader.getFile(li.attr("id"));
					uploader.stop(file);
					uploader.removeFile(file,true);
					li.remove();
					e.preventDefault();
					
				});
			    /** 底部信息展示 */
			    function importTip(settings) {
					var $tipDiv = $("<div/>", {
						"css" : {
							"float" : "left",
							"text-align" : "left"
						}
					});
					if (settings.schemeType === 2) {
						$tipDiv.html(importTipHtmlForXMB);
					}else{
						$tipDiv.html(importTipHtmlForCJ);
					}
					$(".dialog-footer", $dialog).prepend($tipDiv).css("height", "130px");
				}
				 
			});
			/** 确定是否覆盖单选框 展示 */
			function getRadioHTML() {
				var $radioTHML = $(radioTHMLForCj);
				if (opts.schemeType === 2) {
					$radioTHML = $(radioTHMLForXMB);
				}
				return $radioTHML;
			}
			/** 导入下载错误日志 */
			function downLoadLogs($span, examId, logFile, logType) {
				$span.find("a.logFileDown").click(
						function() {
							var url = window.app.rootPath + "download/log/"
									+ examId + "?logFile=" + logFile + "&logType="
									+ logType;
							$.download(url);
						});
			}
		} 
		var o = {
			UI : function(opts) {
				UI(opts);
			}
		};
		return o;
	});

})();