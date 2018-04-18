(function(){
	"use strict";
	define(['jquery','ajax','validatejs',"ztreecore","ztreecheck","ajaxfileupload","ajaxmodal","dialog","controller","formToJosn"],
			function($,ajax,a,b,c,ajaxfileupload,ajaxmodal,dialog,ctrl,formToJson){
		var self = undefined;
		var validateRules = {
		 rules: {
				name:{
					required: true,
					maxlength: 200
				},
				url:{
					maxlength: 100
				},
				reorder:{
					required: true,
					number: true
				},
				remarks:{
					maxlength: 255
				}
			},
			messages: {
				name: {
					required: '名称不能为空',
					maxlength:'名称长度超过200个字符'
				},
				url: {
					maxlength:'名称长度超过100个字符'
				},
				reorder: {
					required: '序列不能为空',
					number:'序列只能是合法数字'
				},
				remarks: {
					maxlength:'名称长度超过255个字符'
				}
			}			
		};
		var o = self = {
			container:undefined,
           render:function(container){
        	   self.query(container);
           },
           query:function(container){
	     	   ctrl.getHtml($('li.active:eq(0) >a').attr("url")+"&menuList=0",function(html) {
	     		   self.container = $(html); 
	     		   ctrl.appendToView(container,self.container);
	     		   init();
	     	   });
	        },
	        save:function(url){
				ctrl.getHtml(url,function(htmlStr) {
					var formDiv = ctrl.modal("应用管理>注册系统",htmlStr,function() {
						var $form = formDiv.find('form');
						$form.validate(validateRules);
						if(self.commit($form)){
							self.query(container);
						}
						return false;
					},"保存");
				});
	        },
	        remove:function(url,removeName){
	     	   	ctrl.log("delete :" + url);
				ctrl.remove(url,"确定要删除应用<b style='color:red;'>"+(removeName||"")+"</b>吗？","<b style='color:red;'>" + removeName + '</b>删除成功！',{},
					    function(data){
							list();
					    }
			   );
	        },
	        update:function(url){
	        	ctrl.getHtml(url,function(htmlStr) {
					  var formDiv =  ctrl.modal("应用管理>编辑",htmlStr,function() {
						var $form = formDiv.find('form');
						$form.validate(validateRules);
	     			    self.commit($form);
					},"保存");
	     	   });
	        },
	        commit:function(form){
	        	var bo = form.valid();
				if(bo){
					var url  = form.attr("action");
					var type = form.attr("method");
					var urlresource = form.formToJson();
					ctrl.log("urlresource :" + JSON.stringify(urlresource));
					var successMsg = "保存成功!";
					if(type == "put") successMsg = "修改成功!";
					
					ajax({
						url : url,
						async : true,
						type: type,
						dataType:"html",
						data:JSON.stringify(urlresource),
						successMsg:{
							show:true,
							header:{show:false},						
							tip_txt:successMsg,
						}, callback : function(htmlStr) {
							self.query();
							init();
						}
					});
					$(".dialog-box").remove();
				}
				return bo;
	        }
       };
		function init(){
			add();
			update();
			deleted();
		}
		function add(){
			$('div.page-content button[name=add]').click(function() {
				self.save("res/addUI");
			})
		}
		function deleted(){
			$("div.page-content button[name=delete] ").click(function() {
				var elementId = $(this).attr("pk");
				var name  = $(this).parent().parent().parent().children(":eq(1)").text();
				var url   = "res/delete?pk=" + elementId + "&uriType=app";
				self.remove(url, name);
			});
			
			$('div.page-content a[trigger="remove"]').click(function() {
				var elementId = $(this).attr("pk");
				var name  = $(this).parent().parent().parent().children(":eq(1)").text();
				var url   = "res/delete?pk=" + elementId + "&uriType=app";
				self.remove(url, name);
			});
		}
		function update(){
			$('div.page-content button[name=update]').click(function() {
				var elementId = $(this).attr("pk");
				var type      = $("input[name='type']").val();
				var url       = "res/updateUI?pk=" + elementId + "&type=" + type
				self.update(url);
			});
			
			$('div.page-content a[trigger=update]').click(function() {
				var elementId = $(this).attr("pk");
				var type      = $("input[name='type']").val();
				var url       = "res/updateUI?pk=" + elementId + "&type=" + type
				self.update(url);
			});
			
		}
		function list(page) {
			ajax({
				url : $('li.active:eq(0) >a').attr("url"),
				async : true,
				type : "GET",
				dataType : "html",
				callback : function(html) {
					$(".main-content").html(html);
					init();
				}
			});
		}
       return o;
	});
})();





