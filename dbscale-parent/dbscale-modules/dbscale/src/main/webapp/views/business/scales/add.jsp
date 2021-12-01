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
<div id="add" v-cloak>
	  <div style="padding:30px 40px 30px 20px">
		<el-form ref="addForm" :model="formData" label-width="100px" :rules="formRules" size="small">
			<el-form-item label="类型" prop="type">
		    	<el-select v-model="formData.type" placeholder="请选择类型" style="display: block;">
			      <el-option :label="typeData.name" :value="typeData.code" v-for="typeData in typeList"></el-option>
			    </el-select>
			</el-form-item>
			<el-form-item label="CPU数量" prop="cpuCnt">
		    	<el-input v-model="formData.cpuCnt" maxlength="5" placeholder="请输入CPU数量" oninput="value=value.replace(/[^\d.]/g,'')"></el-input>
			</el-form-item>
			<el-form-item label="内存容量" prop="memSize">
		    	<el-input v-model="formData.memSize" maxlength="5" placeholder="请输入内存容量" oninput="value=value.replace(/[^\d.]/g,'')">
		    		<template slot="append">G</template>
		    	</el-input>
			</el-form-item>
			<el-form-item label="状态" prop="enabled" style="display:none">
		    	<el-switch v-model="formData.enabled"></el-switch>
		    	<span class="enabledText">{{ formData.enabled ? "启用":"停用" }}</span>
			</el-form-item>
			    
		</el-form>
	</div>
	<div style="border-bottom: 2px solid #F3F3F3;"></div>
	<div style="float: right;padding: 10px 20px">
		<el-button size="small" type="primary" @click="formSubmit('addForm')">保存</el-button>
		<el-button size="small" @click="formClose">取消</el-button>
	</div>
</div>
</body>

<script type="text/javascript" src="js/business/scales/add.js?t=<%=new Date().getTime() %>"></script>

</html>