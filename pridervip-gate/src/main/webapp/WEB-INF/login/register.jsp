<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>用户注册</title>
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
</head>

<body>
<!-- <form id="regFrm" action="userReg.action" method="post" onsubmit="">
    用户名：<input type="text" name="userName" id="uname"><span style="color: red;" id="userNameError"></span><br/>
    密码：<input type="password" name="password" id="pwd"><span style="color: red;" id="pwdError"></span><br/>
    确认密码：<input type="password" id="conpwd"><span style="color: red;" id="conpwdError"></span><br/>
    生日<input type="text" name="birthday" onClick="WdatePicker()" class="Wdate"><br/>
    <input type="submit" value="注册">
</form> -->
    <!--   <form id="regFrm" action="userReg.action" method="post" onsubmit="">
        用户名:<input type="text" name="userName" class="username" >
      密    码:<input type="password" name="password" class="password" >
          <button type="submit" class="submit_button">登录</button> -->
<form action="login.action" method="post">
        手机号：<input type="text" name="phone_mobile" ><br/>
        密码：<input type="password" name="login_password" ></span><br/>
        确认密码：<input type="password" name="login_password2" ><br/>
        <input type="button" value="注册" />
    </form>
</div>
</body>
</html>
