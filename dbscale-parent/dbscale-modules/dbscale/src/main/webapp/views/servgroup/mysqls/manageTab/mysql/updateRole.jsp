<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title>DBScale</title>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<base href="<%=basePath%>"></base>
<!-- 引入样式 -->
<script src="js/commonCss.js?t=<%=new Date().getTime() %>"></script>
<!-- 引入脚本 -->
<script src="js/commonJs.js?t=<%=new Date().getTime() %>"></script>
<style>
.el-input--small .el-input__inner{
	height: 32px;
    line-height: 32px;
    text-align: left;
}
</style>
</head>

<body>
<div id="updateRole" v-cloak>
	<div style="padding:40px 70px 30px">
		<el-checkbox :value="formData.role" @change="formData.role=true">源节点</el-checkbox>
		<el-checkbox style="float: right" :value="!formData.role" @change="formData.role=false">复制节点</el-checkbox>
	</div>
	<div style="border-bottom: 2px solid #F3F3F3;"></div>
	<div style="float: right;padding: 10px 20px">
		<el-button size="small" type="primary" @click="formSubmit()">保存</el-button>
		<el-button size="small" @click="formClose">取消</el-button>
	</div>
</div>
</body>

<script type="text/javascript" src="js/servgroup/mysqls/manageTab/mysql/updateRole.js?t=<%=new Date().getTime() %>"></script>