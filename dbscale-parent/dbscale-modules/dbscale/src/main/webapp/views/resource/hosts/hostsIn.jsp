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
</head>

<body>
<div id="in" v-cloak>
	<div style="margin: 20px auto 0; width: 350px;">
		<el-form ref="inForm" :model="formData" label-width="65px" :rules="formRules" size="small">
			<el-form-item v-if="!hostsData" label="主机IP" prop="ip">
		    	<el-input v-model="formData.ip" disabled></el-input>
			</el-form-item>
			<el-form-item label="端口" prop="port">
		    	<el-input v-model="formData.port" maxlength="8"></el-input>
			</el-form-item>
			<el-form-item label="用户名" prop="username">
		    	<el-input v-model="formData.username" maxlength="16"></el-input>
			</el-form-item>
			<el-form-item label="密码" prop="password">
		    	<el-input v-model="formData.password" maxlength="16" show-password autocomplete="new-password"></el-input>
			</el-form-item>
		</el-form>
	</div>
	<div style="border-bottom: 2px solid #F3F3F3;"></div>
	<div style="float: right;padding: 10px 20px">
		<el-button size="small" @click="identification('inForm')">入库校验</el-button>
		<el-button size="small" type="primary" @click="formSubmit" :disabled="hostIn">入库</el-button>
		<el-button size="small" @click="formClose">取消</el-button>
	</div>
</div>
</body>

<script type="text/javascript">
<%Object hostsData = request.getAttribute("hostsData");%>
var hostsData = <%=hostsData%>
</script>

<script type="text/javascript" src="js/resource/hosts/hostsIn.js?t=<%=new Date().getTime() %>"></script>
</html>