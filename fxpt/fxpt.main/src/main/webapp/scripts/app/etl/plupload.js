;(function($) {
	var uploaders = {};

	var radioTHMLForCj = '<div class="content" style="clear:both;">\
		<label style="float:left;">成绩已存在，是否要重新导入。</label>\
		<div id="myradio" >\
			<label class="radio-inline">\
				<input  type="radio" checked name="t" value="1">不是\
			</label>\
			<label class="radio-inline">\
				<input type="radio" name="t" value="2">是\
			</label>\
		</div>\
		<div style="clear:both;"></div>\
	</div>';
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
	function _(str) {
		return plupload.translate(str) || str;
	}
	function renderUI(id, target) {
		target.contents().each(function(i, node) {
			node = $(node);

			if (!node.is('.plupload')) {
				node.remove();
			}
		});
		target.prepend(
			'<div class="plupload_wrapper plupload_scroll">' +
				'<div id="' + id + '_container" class="plupload_container">' +
					'<div class="plupload">' +
						'<div class="plupload_content">' +
							'<div class="plupload_filelist_footer">' +
								'<div class="plupload_file_name">' +
									'<div class="plupload_buttons">' +
										'<a href="#" class="plupload_button plupload_add btn btn-primary" id="' + id + '_browse">' + _('选择文件上传') + '</a>' +
										'<a href="#" class="plupload_button plupload_start btn btn-primary" style="margin-left:50px;display: none;">' + _('上传') + '</a>' +
										'<span class="uploadify-button-text"> <b style="color:#777;float: right; margin-right: 53px; height: 30px; margin-top: 6px;">在未提示上传成功之前，请勿关闭浏览器或刷新页面!</b> </span>'+
									'</div>' +
									'<span class="plupload_upload_status"></span>' +
								'</div>' +
								'<ul id="' + id + '_filelist" class="plupload_filelist"></ul>' +
							'</div>' +
						'</div>' +
					'</div>' +
				'</div>' +
				'<input type="hidden" id="' + id + '_count" name="' + id + '_count" value="0" />' +
			'</div>'
		);
		
	}

	$.fn.pluploadQueue = function(settings) {
		if (settings) {
			this.each(function() {
				var uploader, target, id, csontents_bak, container_;

				target = $(this);
				id = target.attr('id');

				if (!id) {
					id = plupload.guid();
					target.attr('id', id);
				}

				contents_bak = target.html();
				renderUI(id, target);
				container_ = settings.container_;
				settings = $.extend({
					dragdrop : true,
					browse_button : id + '_browse',
					container : id,
					multi_selection:false
				}, settings);
				importTip(settings);

				// Enable drag/drop (see PostInit handler as well)
				if (settings.dragdrop) {
					settings.drop_element = id + '_filelist';
				}

				uploader = new plupload.Uploader(settings);

				uploaders[id] = uploader;

				function handleStatus(file) {
					var actionClass;

					if (file.status == plupload.DONE) {
						actionClass = 'plupload_done';
					}

					if (file.status == plupload.FAILED) {
						actionClass = 'plupload_failed';
					}

					if (file.status == plupload.QUEUED) {
						actionClass = 'plupload_delete';
					}

					if (file.status == plupload.UPLOADING) {
						actionClass = 'plupload_uploading';
					}
					var icon = $('#' + file.id).attr('class', actionClass).find('a').css('display', 'block');
					if (file.hint) {
						icon.attr('title', file.hint);	
					}
				}
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
					$(".dialog-footer", settings.container_).prepend($tipDiv).css("height", "130px");
				}
				function updateTotalProgress() {
					$('span.plupload_total_status', target).html(uploader.total.percent + '%');
					$('div.plupload_progress_bar', target).css('width', uploader.total.percent + '%');
				}
				
				function validateExistContent($fileObj, file) {
					var opts = $fileObj.data("opts");
					if (opts.validateResult.existContent) {
						var $radioTHML = getRadioHTML();
						$radioTHML.find(":radio").attr("name", file.id);
						$fileObj.find(".content").append($radioTHML);
					}
				}

				function getRadioHTML() {
					var $radioTHML = $(radioTHMLForCj);
					if (settings.schemeType === 2) {
						$radioTHML = $(radioTHMLForXMB);
					}
					return $radioTHML;
				}

				function updateList() {
					
					if(!settings.multi_selection){//单选
						var array = new Array();
						var Fl = uploader.files.length;
						if(Fl==1){
							array.push(uploader.files[0]);
						}else if(Fl>1){
							array.push(uploader.files[Fl-1]);
						}
						uploader.files = array;
					}
					var fileList = $('ul.plupload_filelist', target).html(''), inputCount = 0, inputHTML;
					$.each(uploader.files, function(i, file) {
						inputHTML = '';
						if (file.status == plupload.DONE) {
							if (file.target_name) {
								inputHTML += '<input type="hidden" name="' + id + '_' + inputCount + '_tmpname" value="' + plupload.xmlEncode(file.target_name) + '" />';
							}
							inputHTML += '<input type="hidden" name="' + id + '_' + inputCount + '_name" value="' + plupload.xmlEncode(file.name) + '" />';
							inputHTML += '<input type="hidden" name="' + id + '_' + inputCount + '_status" value="' + (file.status == plupload.DONE ? 'done' : 'failed') + '" />';
							inputCount++;
							$('#' + id + '_count').val(inputCount);
						}
						
						inputHTML += '<div class="head" style="width:100%">\
										    <div id="progress" class="uploadify-progress" style="float:left;width:90%; background-color: darkgray;">\
										      <div class="uploadify-progress-bar" style="width: ' + file.percent + '%;"></div>\
										    </div>\
										    <div id="view" style="float:left;width:10%;padding-left: 10px;" class="uploadify-progress-cuess">' +  file.percent + '%</div>\
										    <div style="float:right;width:5%;text-align:right;">\
									</div>\
						 		  </div>';
						
						if(file.percent=="100"){
							inputHTML += '<div class="plupload_file_status">上传成功</div>';
						}
						if(file.serviceData){
							var b = false;
							if(file.serviceData.validateResult.hasError){
								b=true;
								inputHTML += '<div class="content" style="width:100%; clear:both; margin-top:5px;"><span style="display: block;">'+file.serviceData.validateResult.messages.join(",")+'</span></div>';
							}else{
								if (file.serviceData.validateResult.existContent) {
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
						fileList.append(
							'<li id="' + file.id + '" style="     overflow: hidden;   margin-top: 10px;    background-color: rgba(66, 139, 202, 0.11);">' +
								'<div class="plupload_file_name plupload_delete"><span>' + file.name + '</span><a href="javascript:;" style=" float: right;top: 5px;right: 5px;" class="glyphicon glyphicon-remove" ></a></div>' +
								'<div class="plupload_file_action"><a href="#"></a></div>' +
								inputHTML +
							'</li>'
						);

						handleStatus(file);
					});
					
					fileList.find("div.plupload_delete a").on("click",function(e) {
						var li = $(this).parent().parent();
						var file = uploader.getFile(li.attr("id"));
						$('#' + file.id).remove(); 
						uploader.removeFile(file);
						e.preventDefault();
						
					});
					
					$('span.plupload_total_file_size', target).html(plupload.formatSize(uploader.total.size));

					if (uploader.total.queued === 0) {
						$('span.plupload_add_text', target).html(_('Add Files'));
					} else {
						$('span.plupload_add_text', target).html(plupload.sprintf(_('%d files queued'), uploader.total.queued));
					}

					$('a.plupload_start', target).toggleClass('plupload_disabled', uploader.files.length == (uploader.total.uploaded + uploader.total.failed));

					// Scroll to end of file list
					fileList[0].scrollTop = fileList[0].scrollHeight;

					updateTotalProgress();
					 
				}
				
				uploader[""]= function(){
					
					
				}

				function destroy() {
					delete uploaders[id];
					uploader.destroy();
					target.html(contents_bak);
					uploader = target = contents_bak = null;
				}

				uploader.bind("UploadFile", function(up, file) {
					$('#' + file.id).addClass('plupload_current_file');
				});

				uploader.bind('Init', function(up, res) {
					// Enable rename support
					if (!settings.unique_names && settings.rename) {
						target.on('click', '#' + id + '_filelist div.plupload_file_name span', function(e) {
							var targetSpan = $(e.target), file, parts, name, ext = "";

							// Get file name and split out name and extension
							file = up.getFile(targetSpan.parents('li')[0].id);
							name = file.name;
							parts = /^(.+)(\.[^.]+)$/.exec(name);
							if (parts) {
								name = parts[1];
								ext = parts[2];
							}

							// Display input element
							targetSpan.hide().after('<input type="text" />');
							targetSpan.next().val(name).focus().blur(function() {
								targetSpan.show().next().remove();
							}).keydown(function(e) {
								var targetInput = $(this);

								if (e.keyCode == 13) {
									e.preventDefault();

									// Rename file and glue extension back on
									file.name = targetInput.val() + ext;
									targetSpan.html(file.name);
									targetInput.blur();
								}
							});
						});
					}

					$('a.plupload_start', target).click(function(e) {
						if (!$(this).hasClass('plupload_disabled')) {
							uploader.start();
						}
						//e.preventDefault();
					});

					$('a.plupload_stop', target).click(function(e) {
						e.preventDefault();
						uploader.stop();
					});

					$('a.plupload_start', target).addClass('plupload_disabled');
					$(".dialog-footer", settings.container_).find("button:eq(0)").attr("disabled",true);
				});

				uploader.bind("Error", function(up, err) {
					var file = err.file, message;

					if (file) {
						message = err.message;

						if (err.details) {
							message += " (" + err.details + ")";
						}

						if (err.code == plupload.FILE_SIZE_ERROR) {
							alert(_("Error: File too large:") + " " + file.name);
						}

						if (err.code == plupload.FILE_EXTENSION_ERROR) {
							alert(_("Error: Invalid file extension:") + " " + file.name);
						}
						
						file.hint = message;
						$('#' + file.id).attr('class', 'plupload_failed').find('a').css('display', 'block').attr('title', message);
					}

					if (err.code === plupload.INIT_ERROR) {
						setTimeout(function() {
							destroy();
						}, 1);
					}
				});

				uploader.init();

				uploader.bind('StateChanged', function() {
					if (uploader.state === plupload.STARTED) {
						if(settings.disableBrowse){
							$('li.plupload_delete a,div.plupload_buttons', target).hide();
						}
						uploader.disableBrowse(settings.disableBrowse);

						$('span.plupload_upload_status,div.plupload_progress,a.plupload_stop', target).css('display', 'block');
						//$('span.plupload_upload_status', target).html('Uploaded ' + uploader.total.uploaded + '/' + uploader.files.length + ' files');

						if (settings.multiple_queues) {
							$('span.plupload_total_status,span.plupload_total_file_size', target).show();
						}
					} else {
						updateList();
						$('a.plupload_stop,div.plupload_progress', target).hide();
						$('a.plupload_delete', target).css('display', 'block');

						if (settings.multiple_queues && uploader.total.uploaded + uploader.total.failed == uploader.files.length) {
							$(".plupload_buttons,.plupload_upload_status", target).css("display", "inline");
							uploader.disableBrowse(settings.disableBrowse);

							$(".plupload_start", target).addClass("plupload_disabled");
							$('span.plupload_total_status,span.plupload_total_file_size', target).hide();
						}
					}
				});

				uploader.bind('FilesAdded', updateList);

				uploader.bind('FilesRemoved', function() {
					// since the whole file list is redrawn for every change in the queue
					// we need to scroll back to the file removal point to avoid annoying
					// scrolling to the bottom bug (see #926)
					var scrollTop = $('#' + id + '_filelist').scrollTop();
					updateList();
					$('#' + id + '_filelist').scrollTop(scrollTop);
				});

				uploader.bind('FileUploaded', function(up, file,data) {
					var json = JSON.parse(data.response);
					/*if(typeof(json)=="object" && Object.prototype.toString().call(json).toLocaleString()=="[object object]"  && !json.length){
						file["serviceData"]=json;
					}*/
					file["serviceData"]=json;
					handleStatus(file);
				});

				uploader.bind("UploadProgress", function(up, file) {
					if(file.percent==100){
						$('#' + file.id + ' div.plupload_file_status', target).html("上传成功");
					}else{
						$('#' + file.id + ' div.plupload_file_status', target).html(file.percent + '%');
					}
					handleStatus(file);
					updateTotalProgress();
				});
				
				uploader.bind("UploadComplete", function(up, file) {
					$(".dialog-footer", settings.container_).find("button:eq(0)").attr("disabled",false);
				});

				// Call setup function
				if (settings.setup) {
					settings.setup(uploader);
				}
			});
			return uploaders[$(this[0]).attr('id')];
		} else {
			// Get uploader instance for specified element
			return uploaders[$(this[0]).attr('id')];
		}
	};
})(jQuery);
