<#-- 点击树节点获取的列表      勿删-->
  <#assign index=1>
  <#list resList![] as data>
  <tr>
    <td><span class="lbl">${index}</span></td>
    <td>${(data.name)!}</td>
    <td>${(data.parent.name)!}</td>
     <td>${(data.available?string("有效","无效"))!}</td>
    <td title="${(data.remarks)!}">${(data.remarks)!}</td>
    <td>
	    <div class="btn-group">
	      <a href="javascript:void(0);" pk=${data.pk?c! "0"} trigger="update">修改</a>
	      <span class="separator">|</span>
	      <a href="javascript:void(0);" pk=${data.pk?c! "0"} trigger="remove">删除</a>     
	    </div>		
    </td>
  </tr>
  <#assign index=index+1>
  </#list>
