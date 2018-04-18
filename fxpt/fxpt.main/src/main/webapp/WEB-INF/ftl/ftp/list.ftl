<#import "../commons/pageHead.ftl" as pageHead>
<form id="statistic">
 <#assign btns=[{"text":"测试FTP连接","id":"testFtpSuccess","class":"btn-primary","closeBtn":false},{"text":"保存","id":"saveSetting","class":"btn-primary","closeBtn":false}]>
<@pageHead.pageHead title="FTP参数设置" buttons=btns/>

 <div class="form-item">
  <div class="panel panel-default">
    <table class="table table-bordered table-hover ">
      <tbody>
      
         <#if list??>
            <#list list as ftp>
      		<tr>
	          <th style="text-align:right">地址：</th>
	         	 <td>
					 <input type="text" name="url" id="url" value="${ftp.url!}"></input>&nbsp;&nbsp;
				</td>
				<th style="text-align:right">端口：</th>
				 <td>
					<input type="text" name="port" id="port" value="${ftp.port!}"></input>&nbsp;&nbsp;
			 	 </td>			
	        </tr>
	        
	        <tr>
	          <th style="text-align:right">账号：</th>
	         	 <td>
					 <input type="text" name="username" id="username" value="${ftp.username!}"></input>&nbsp;&nbsp;
				</td>
				<th style="text-align:right">密码：</th>
				 <td>
					<input type="password" name="password" id="password" value="${ftp.password!}"></input>&nbsp;&nbsp;
			 	 </td>			
	        </tr>
	        
	           <tr>
	          <th style="text-align:right">上传目录：</th>
	         	 <td >
					 <input type="text" name="path" id="path" value="${ftp.path!}"></input>&nbsp;&nbsp;
				</td>
				 <th style="text-align:right">启用：</th>
	         	 <td >
					<#if ftp.status=1>
						<input type="radio" name="status" id="status"  value="1" checked="true">启用</input>&nbsp;&nbsp;
						<input type="radio" name="status" id="status" value="0">禁用</input>&nbsp;&nbsp;
					<#else>
						<input type="radio" name="status" id="status" value="1">启用</input>&nbsp;&nbsp;
						<input type="radio" name="status" id="status" value="0" checked="true">禁用</input>&nbsp;&nbsp;
					</#if>
				</td>
				
	        </tr>
	        <tr>
	        <th style="text-align:right">备注：</th>
				 <td colspan="3" >
					<input type="text" name="des" id="des" class="form-control" value="${ftp.des!}"></input>&nbsp;&nbsp;
			 	 </td>		
	      	</tr>
	      	
	      	<input type="hidden" name="id" value="${ftp.id!}"/>
	      	
	        </#list>
	     </#if>   
	        
	       <tr>
	          <td colspan="4" style="text-align:left;color:red">
	        	上传目录：所要上传的服务器文件夹名称，不填则默认上传到FTP服务器根目录<br>
	        	启用： 状态为禁用则上传功能失效<br>
	          </td>
	        </tr>
      </tbody>
    </table>
  </div>
</div>
</from>