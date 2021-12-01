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
<div id="imageUpdate" v-cloak>
	  <div style="padding:30px 40px 30px 20px">
		<el-form ref="imageUpdateForm" :model="formData" :rules="formRules" label-width="100px" size="small">
			<el-form-item label="服务名称" prop="name">
				<el-input v-model="formData.name" disabled></el-input>
			</el-form-item>
			<el-form-item label="类型" prop="type" v-if="highAvailable">
				<el-select v-model="formData.type" placeholder="请选择类型" style="display: block;" @change="typeChange">
					<el-option :label="data.name" :value="data.code" v-for="data in typeList"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item label="版本" prop="version">
				<el-select v-model="formData.version" placeholder="请选择版本" style="display: block;">
					<el-option :label="data.version" :value="data.version" v-for="data in imageVersionList"></el-option>
				</el-select>
			</el-form-item>

		</el-form>
	</div>
	<div style="border-bottom: 2px solid #F3F3F3;"></div>
	<div style="float: right;padding: 10px 20px">
		<el-button size="small" type="primary" @click="formSubmit('imageUpdateForm')">保存</el-button>
		<el-button size="small" @click="formClose">取消</el-button>
	</div>
</div>
</body>

<script type="text/javascript" src="js/servgroup/cmhas/imageUpdate.js?t=<%=new Date().getTime() %>"></script>