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
<div id="edit" v-cloak>
	  <div style="padding:30px 40px 30px 20px">
		<el-form ref="editForm" :model="formData" label-width="100px" :rules="formRules" size="small">
			<el-form-item label="存储名称" prop="name">
		    	<el-input v-model="formData.name"></el-input>
			</el-form-item>
			<el-form-item label="供应商" prop="provisioner">
		    	<el-input v-model="formData.provisioner"></el-input>
			</el-form-item>
			<el-form-item label="描述" prop="description" >
		    	<el-input type="textarea" v-model="formData.description" maxlength="128"></el-input>
			</el-form-item>
			    
		</el-form>
	</div>
	<div style="border-bottom: 2px solid #F3F3F3;"></div>
	<div style="float: right;padding: 10px 20px">
		<el-button size="small" type="primary" @click="formSubmit('editForm')">保存</el-button>
		<el-button size="small" @click="formClose">取消</el-button>
	</div>
</div>
</body>

<script type="text/javascript">
<%Object areaId = request.getAttribute("areaId");%>
var areaId = <%=areaId%>
</script>

<script type="text/javascript" src="js/resource/storage/pvcs/edit.js?t=<%=new Date().getTime() %>"></script>
</html>