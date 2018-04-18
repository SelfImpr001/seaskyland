<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<#if result??>
		<h2 style="background-color: yellow; color: red;">${result}<h2> 
	</#if>
	<form action="user" method="post">
		<h3>新增</h3>
	    <label>uuid</label>
		<input type="text" name="UID" value="594b0a829b1411e6a1565f761f1fb274">
		<label>roleId</label>
		<input type="text" name="RoleId" value="superAdmin">
		<input type="text" name="LoginId" value="355454512165498798">
		<input type="text" name="NickName" value="学业系统">
		<input type="text" name="UserType" value="0000">
		<input type="text" name="UserName" value="学业系统">
		<input type="text" name="LockStatus" value="1">
		<input type="text" name="ApprovalStatus" value="2">
		<input type="text" name="Sex" value="2">
		<input type="text" name="Mobile" value="">
		<input type="text" name="Email" value="">
		<input type="text" name="Nation" value="00">
		<input type="text" name="OrgCode" value="nsjy">
		<input type="text" name="NativePlace" value="null">
		<input type="text" name="PreOrgID" value="null">
		<input type="text" name="ApprovalStatus" value="2">
		<input type="text" name="Course" value="00">
		<input type="text" name="PostCode" value="">
		<input type="text" name="action" value="create">
		<button type="submit">确认</button>
	</form>
	
	<form action="user" method="post">
		<h3>修改</h3>
	    <label>uuid</label>
		<input type="text" name="UID" value="594b0a829b1411e6a1565f761f1fb274">
		<input type="text" name="LoginId" value="355454512165498798">
		<input type="text" name="NickName" value="学业系统1">
		<input type="text" name="UserName" value="学业系统1">
		<input type="text" name="action" value="update">		
		<button type="submit">确认</button>
	</form>
	
	<form action="user" method="post">
		<h3>删除</h3>
		<label>uuid</label>
		<input type="text" name="UID" value="594b0a829b1411e6a1565f761f1fb274">
		<input type="text" name="action" value="delete">
		<button type="submit">确认</button>
	</form>
	<form action="login" method="get">
		<h3>登录</h3>
		<label>uuid</label>
		<input type="text" name="uuid" value="aaop138">
		<button type="submit">确认</button>
	</form>

	<#--
	
	<form action="${request.contextPath}/login" method="POST">
		<h3>登录</h3>
		<label>uuid</label>
		<input type="text" name="username" value="admin">
		<input type="text" name="password" value="123456">
		<button type="submit">确认</button>
	</form>
	-->
</body>
</html>