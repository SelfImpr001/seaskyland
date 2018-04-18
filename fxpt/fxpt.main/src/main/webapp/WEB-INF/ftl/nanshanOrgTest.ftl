<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<#if result??>
		<h2 style="background-color: yellow; color: red;">${result}<h2> 
	</#if>
	<form action="org" method="post">
		<h3>新增</h3>
	    <label>orgCode</label>
		<input type="text" name="orgCode" value="111">
		 <label>Parent</label>
		<input type="text" name="parent" value="">
		<label>Parents</label>
		<input type="text" name="parents" value="">
		<label>DisplayName</label>
		<input type="text" name="displayName" value="湖南">
		<label>Name</label>
		<input type="text" name="name" value="湖南">
		<label>action</label>
		<input type="text" name="action" value="create">
		<button type="submit">确认</button>
	</form>
	
	<form action="org" method="post">
		<h3>删除</h3>
		<label>orgCode</label>
		<input type="text" name="orgCode" value="111">
		<input type="text" name="action" value="delete">
		<button type="submit">确认</button>
	</form>
	<form action="org" method="post">
		<h3>修改</h3>
			 <label>orgCode</label>
			<input type="text" name="orgCode" value="111">
			<label>Parent</label>
			<input type="text" name="parent" value="">
			<label>Parents</label>
			<input type="text" name="parents" value="">
			<label>DisplayName</label>
			<input type="text" name="displayName" value="湖南">
			<label>Name</label>
			<input type="text" name="name" value="湖南">
			<label>action</label>
			<input type="text" name="action" value="update">
			<button type="submit">确认</button>
	</form>
</body>
</html>