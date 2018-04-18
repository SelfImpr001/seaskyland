(function() {
	"use strict";

	var itemTemplate = '<div id="${fileID}" name="cntest-sjcj-queue"\
		class="uploadify-queue" style="width:100%;clear:both;" overlayImport=false \
		data-fileId="${fileID}" data-fileName="${fileName}">\
		  <div class="head" style="width:100%">\
	    <div  style="float:left;width:20%;">${fileName}</div>\
	    <div id="progress" class="uploadify-progress" style="float:left;width:70%;">\
	      <div class="uploadify-progress-bar" style="width: 1%;"></div>\
	    </div>\
	    <div id="view" style="float:left;width:5%;">0%</div>\
	    <div style="float:right;width:5%;text-align:right;">\
		<a href="javascript:$(\'#${instanceID}\').uploadify(\'cancel\', \'${fileID}\');\
			$(\'#${fileID}\').remove();$(\'#${instanceID}\').uploadify(\'disable\',false);">X</a></div>\
	  </div>\
	  <div class="content" style="width:100%; clear:both; margin-top:5px;height:30px;"></div>\
	</div>';

	var radioTHMLForCj = '<div style="clear:both;">\
			<label style="float:left;">成绩已存在，是否要重新导入。</label>\
			<div id="myradio" style="float:left;">\
				<label class="radio-inline">\
					<input  type="radio" checked name="t" value="1">不是\
				</label>\
				<label class="radio-inline">\
					<input type="radio" name="t" value="2">是\
				</label>\
			</div>\
			<div style="clear:both;"></div>\
		</div>';

	var importTipHtmlForXMB = '导入须知:\
		<br/>1.选择本地excel文件，按住Ctrl或Shift键可以上传多份文件。\
		<br/>2.导入文件格式必须使用导入模板格式，否则可能上传出错。';
	
			var model = [ "jquery", "ajax", "controller", "ajaxfileupload", "download",
			"uploadify" ];
			
	define(model, function($, ajax, ctrl, ajaxfileupload, download, uploadify) {
		var $dialog = undefined;
		var container = undefined;
		var self = undefined;
		var dataHide = new Array(); 
		var num =0;
		function UI(_opts) {
			var opts = {
				title : "",
				objectType : undefined,
				planId : undefined,
				importType : undefined,
				fileTypeDesc : "只能选择zip",
				fileTypeExts : "*.zip",
				sessionId : undefined,
				sessionName : undefined,
				callBack : undefined,
				container : undefined,
				self:undefined,
				listContainer : undefined,
				getPager : undefined,
				schoolPageInit : undefined
			};
			$.extend(opts, _opts);
			$dialog = opts;
			container = opts.container;
			self = opts.self;
			opts.fileObj = container.find("#files");
			initUploadButton();
		}
		function initUploadButton() {
			var opts = $dialog;
			var setting = initSetting();
			opts.fileObj.uploadify(setting);
		}

		function initSetting() {
			var opts = $dialog;
			var falshURL = window.app.rootPath
					+ "static/resources/uploadify/uploadify.swf";
			var url = window.app.rootPath + "academicSupervise/importAll";
			var $files = container.find(".dialog-body div[name='cntest-sjcj-queue']");
			return {
				swf : falshURL,
				uploader : url,
				fileObjName : "file",
				method : "post",
				debug : true,
				multi:false,
				buttonText : "选择文件上传 ",
				fileTypeDesc : opts.fileTypeDesc,
				fileTypeExts : opts.fileTypeExts,
				itemTemplate : itemTemplate,
				onUploadSuccess : onUploadSuccess,
				onSelect : onSelect,
				removeCompleted:false
			
			};
		}

		function onUploadSuccess(file, data, response) {
			var $fileObj=container.find('#' + file.id);
			$("#view",$fileObj).html("100%");
			$fileObj.find(".content").html( "<b style='color:#357EBD;'>上传成功</b>");
			if($fileObj.find("input:hidden").length>1){
				$fileObj.find("input:hidden").val(data);
			}else{
				$fileObj.append("<input type='hidden' id='monitorhref' name='monitorHref' value="+data+">")
			}
		}
		
		function onSelect(file){
				container.find("#files").uploadify("disable",true);
		}
		
		var o = {
			UI : function(opts) {
				UI(opts);
			}
		};
		return o;
	});

})();