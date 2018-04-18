<#import "../meta.ftl" as meta>
<#import "../script.ftl" as script>

<html lang="zh-cn">
<head>
	<@meta.meta title=""/>
	
</head>
<body entry="organization/maintain" rootPath="${request.contextPath}/" class="over-h">

	<div class="form-user-item">
	<form name="organization" id="form" method="post" class="form-horizontal">
	    <input type="hidden" name="fk" value="${organization.fk}"/>
	    <input type="hidden" name="available" value="${organization.available}"/>
	    <input type="hidden" name="parent" value="${organization.parent}"/>
	    <input type="hidden" name="children" value="${organization.children}"/>
    
		<div class="form-group">
	    	<label  path="name" for="name-inner" class="col-sm-2 control-label">名称：</label>
	    	<div class="col-sm-6">
	      		<input path="name" type="text" class="form-control" id="name-inner" placeholder="名称"></input>
	    	</div>
	  	</div>
	  	<div class="form-group">
			<div class="col-sm-offset-2 col-sm-6">
				<shiro.hasPermission name="organization:update">
		            <form:button id="updateBtn" href="#" class="btn btn-default">修改</form:button>
		        </shiro.hasPermission>
				<shiro.hasPermission name="organization:delete">
            		<#if organization.root == false>
		            <form:button id="deleteBtn" href="#" class="btn btn-default">删除</form:button>
		           </#if>
        		</shiro.hasPermission>
			</div>
	  	</div>	
	</form>
	</div>

</body>
</html>
