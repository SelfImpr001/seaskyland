$(function(){
	var main = {
			dopost:function(url, data, dataType, successMsg,afterMsg){
				$.ajax({
					url : url,
					type : dataType,
					async:true,
					data:data,
					timeout:50000,
					dataTpye:'json',
					beforeSent:function(){
					},
					success:function(data){
						if(afterMsg=="pentaho"){
							$("#messagePentaho").text("测试连接通过！");
						}
						if(afterMsg=="sso"){
							$("#messageSSO").text("测试连接通过！");
						}
						if(afterMsg=="subPentaho"){
							$("#pentahoMessge").text("保存成功,需要手动重启pentaho才能生效！");
						}
						if(afterMsg=="ssoSub"){
							$("#ssoMessge").text("保存成功！数据库连接需要重启才能生效！");
						}
						if(afterMsg=="loginGo"){
							window.location.href="../systemSet/sso";
						}
					},
					error:function(data){
						if(afterMsg=="pentaho"){
							$("#messagePentaho").text("测试连接失败！");
						}
						if(afterMsg=="sso"){
							$("#messageSSO").text("测试连接失败！");
						}
						if(afterMsg=="subPentaho"){
							$("#pentahoMessge").text("保存失败，请检查文件路径是否正确！");
						}
						if(afterMsg=="ssoSub"){
							$("#ssoMessge").text("保存失败！");
						}
						if(afterMsg=="loginGo"){
							$("#loginMessge").text("用户名密码错误！");
						}
					},
					complete:function(){
					}
				
				});
			
			},getHtml: function(url, type,data) {
				var html="";
				$.ajax({
					url : url,
					type :type ,
					async:false,
					data:data,
					timeout:50000,
					dataTpye:"html",
					success:function(data){
						html = data;
					} 
				});
				return html;
			},post: function(url, type,d) {
				var html="";
				$.ajax({
					url : url,
					type :type ,
					async:false,
					data:d,
					timeout:50000,
					dataTpye:"json",
					success:function(data){
						html = data;
					},error:function(data){
						$("#name").val("");
						$("#pentahoAddress").val("");
						$("#post").val("");
						$("#password").val("");
						$("#tableName").val("");
						$("#pentahoMessge").text("请检查文件路径是否正确！");
					} 
				});
				return html;
			} 
		};
	//pentho
	$("#pentaho").click(function(){
		$("#messagePentaho").text("正在测试连接......");
		var host=$("#pentahoAddress").val();
		var name=$("#name").val();
		var tablename=$("#tableName").val();
		var password=$("#password").val();
		main.dopost("../systemSet/testDBconnect/pentaho?name="+name+"&host="+host+"&tablename="+tablename+"&password="+password,{},"get","","pentaho");
		
	});
	$("#hibernate").click(function(){
		var host=$("#hibernateAddress").val();
		var name=$("#hbname").val();
		var tablename=$("#htableName").val();
		var password=$("#hbpassword").val();
		main.dopost("../systemSet/testDBconnect/pentaho?name="+name+"&host="+host+"&tablename="+tablename+"&password="+password,{},"get","","hibernate");
		
	});
	//配置页面登录
	$("#loginGo").click(function(){
		var name=$("#username").val();
		var password=$("#password").val();
		main.dopost("../systemSet/loginGo?name="+name+"&password="+password,{},"get","","loginGo");
	});
	//重置
	$("#resert").click(function(){
		$("#username").val("");
		$("#pwd").val("");
	});
	$("#ssoTest").click(function(){
		$("#messageSSO").text("正在测试连接......");
		var host=$("#jdbc_ip").val();
		var name=$("#jdbc_username").val();
		var tablename=$("#jdbc_database").val();
		var password=$("#jdbc_password").val();
		main.dopost("../systemSet/testDBconnect/sso?name="+name+"&host="+host+"&tablename="+tablename+"&password="+password,{},"get","","sso");
		
	});
	
	$("#sbt").click(function(){
		var areaCode=$("#area_org_code").val();
		var host=$("#jdbc_ip").val();
		var name=$("#jdbc_username").val();
		var tablename=$("#jdbc_database").val();
		var password=$("#jdbc_password").val();
		var post=$("#jdbc_port").val();
		
		var server=$("#shiro_sso_server_url").val();
		var service=$("#shiro_sso_service_url").val();
		var login=$("#shiro_sso_login_url").val();
		var loginOut=$("#shiro_sso_loginOut_url").val();
		$("#ssoMessge").text("正在保存，请稍后...........");
		main.dopost("../systemSet/ssoSub?jdbc_username="+name+"&jdbc_ip="+host+"&jdbc_database="+tablename+"&jdbc_password="+password
				+"&shiro.sso.server.url="+server+"&shiro.sso.service.url="+service+"&shiro.sso.login.url="+login+"&shiro.sso.loginOut.url="+loginOut
				+"&jdbc_port="+post+"&area.org.code="+areaCode,{},"get","","ssoSub");
		
		
	});
	$("#subPentaho").click(function(){
		
		var contextAddress=$("#contextAddress").val();
		var host=$("#pentahoAddress").val();
		var post=$("#post").val();
		var name=$("#name").val();
		var tablename=$("#tableName").val();
		var password=$("#password").val();
		main.dopost("../systemSet/pentahoSub?name="+name+"&contextAddress="+contextAddress+"&pentahoAddress="+host+"&tableAddress="+tablename+"&password="+password+"&post="+post,{},"get","","subPentaho");
	});
	
	$("#fileViewId").click(function(){
		var data = main.getHtml("../systemSet/fileview","post",{});  
		var file = dialog.modal({
			body:data
			},function(){
				var selectvalue = file.find("#hiddenValue").val();
				var isDire = file.find("#isDire").val();
				$("#pentahoMessge").text("");
				if(isDire=="false"){
					$("#contextAddress").val(selectvalue);
					//根据路径去读取pentaho的配置文件
					var html=main.post("../systemSet/pentahoXML","post",{"selectvalue":selectvalue});
					if($(html).find("#name").val()!=undefined){
						$("#name").val($(html).find("#name").val());
					}
					if($(html).find("#pentahoAddress").val()!=undefined){
						$("#pentahoAddress").val($(html).find("#pentahoAddress").val());
					}
					if($(html).find("#post").val()!=undefined){
						$("#post").val($(html).find("#post").val());
					}
					if($(html).find("#password").val()!=undefined){
						$("#password").val($(html).find("#password").val());
					}
					if($(html).find("#tableName").val()!=undefined){
						$("#tableName").val($(html).find("#tableName").val());
					}
					return true;
				}else{
					alert("请选择一个文件");
					return false;
				}
		},function(){
			var pfile = file.find("#hiddenValue").val();
			var data = main.getHtml("../systemSet/fileview?return=true","post",{"pfile":pfile});
			file.find("#dialogId").html(data);
			var parent = $(data).find("#paremtfile").val();
			if(parent==undefined){
				file.find("#fileValue").html("");
			}else{
				file.find("#fileValue").html($(data).find("#paremtfile").val());
				file.find("#hiddenValue").val($(data).find("#paremtfile").val());
			}
			return false;
		});
		file.find("#dialogId").on("click","a",function(){
			var $a = $(this);
			var tag=$a.attr("tag");
			var data = main.getHtml("../systemSet/fileview","post",{"pfile":tag});
			file.find("#dialogId").html(data);
			file.find("#fileValue").html($a.attr("tag"));
			file.find("#hiddenValue").val($a.attr("tag"));
			
		})
	})
	
	
})