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
<div id="backUp" v-cloak>
	  <div style="padding:30px 40px 30px 20px">
		<el-form ref="backUpForm" :model="formData" label-width="110px" :rules="formRules" size="small">
			<el-form-item label="备份存储类型" prop="backupStorageType">
		    	<el-select v-model="formData.backupStorageType" placeholder="请选择备份存储类型" style="display: block;">
			      <el-option :label="list.name" :value="list.code" v-for="list in backupStorageTypeList"></el-option>
			    </el-select>
			</el-form-item>
			<el-form-item label="备份类型" prop="backupType">
		    	<el-select v-model="formData.backupType" placeholder="请选择备份存储类型" style="display: block;">
			      <el-option :label="list.name" :value="list.code" v-for="list in backUpTypeList"></el-option>
			    </el-select>
			</el-form-item>
			<el-form-item label="文件保留天数" prop="fileRetentionTime">
				 <el-input-number v-model="formData.fileRetentionTime" placeholder="请输入文件保留天数" :min="1" controls-position="right" style="display: block;width:auto;" ></el-input-number>
    		</el-form-item>
		</el-form>
	</div>
	<div style="border-bottom: 2px solid #F3F3F3;"></div>
	<div style="float: right;padding: 10px 20px">
		<el-button size="small" type="primary" @click="formSubmit('backUpForm')">保存</el-button>
		<el-button size="small" @click="formClose">取消</el-button>
	</div>
</div>
</body>

<script type="text/javascript" src="js/servgroup/cmhas/manageTab/unit/backUp.js?t=<%=new Date().getTime() %>"></script>