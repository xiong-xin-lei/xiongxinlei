<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign in</title>
	<%  
	String path = request.getContextPath();  
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
	%>  
	<base href="<%=basePath%>"></base>
    <!-- 引入样式 -->
    <script src="js/commonCss.js?t=<%=new Date().getTime() %>"></script>
    <!-- 引入脚本 -->
    <script src="js/commonJs.js?t=<%=new Date().getTime() %>"></script>
 	<link rel="stylesheet" href="css/login.css?t=<%=new Date().getTime() %>"> 
</head>

<body>
    <div id="login" v-cloak>
        <div class="login">
            <img src="img/login_Logo.png" width="330px" style="margin-bottom: 25px;">
            <el-form :model="loginForm" ref="loginForm">
                <el-form-item prop="name" >
                    <el-input v-model="loginForm.username" placeholder="请输入账号" clearable @keyup.enter.native="submitForm('loginForm')"></el-input>
                </el-form-item>
                <el-form-item prop="password" >
                    <el-input v-model="loginForm.password" placeholder="请输入密码" show-password @keyup.enter.native="submitForm('loginForm')"></el-input>
                </el-form-item>
                <el-form-item class="gap">
                    <el-button @click="submitForm('loginForm')" style="width: 100%;margin-bottom: 100px;">登录</el-button>
                    <!-- <el-button @click="resetForm('loginForm')">重置</el-button> -->
                </el-form-item>
            </el-form>
            <span class="copyright">版权 © {{nowYear}}
                保留所有权利</span>
        </div>
    </div>
</body>

<script type="text/javascript" src="js/login.js?t=<%=new Date().getTime() %>"></script>
</html>