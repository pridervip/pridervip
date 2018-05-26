<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>用户登陆</title>
</head>
<body>

<form action="/user/login.html" method="post">
	<p>
		用户名： <input name="name" />
	</p>
	<p>
		密码： <input name="pwd" type="password" />
	</p>
	<p>
		<input type="submit" value="登陆" />
		<input type="submit" value="注册" />
	</p>
</form>
</body>
</html>