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
			<el-row>
				<el-col :span="24" >
					 <el-form-item label="所属站点" prop="siteId">
		    	<el-select v-model="formData.siteId" placeholder="请选择所属站点" style="display: block;" disabled>
			      <el-option :label="siteData.name" :value="siteData.id" v-for="siteData in siteList"></el-option>
			    </el-select>
			</el-form-item>
				</el-col>
			</el-row>
			 <el-row>
			 	<el-col :span="12">
					<el-form-item label="存储池名称" prop="name">
				    	<el-input v-model="formData.name" maxlength="16" placeholder="请输入存储池名称"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="类型" prop="type">
				    	<el-select v-model="formData.type" placeholder="请选择类型" style="display: block;">
					      <el-option :label="typeData.name" :value="typeData.code" v-for="typeData in typeList"></el-option>
					    </el-select>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
			 	<el-col :span="12">
					<el-form-item label="品牌" prop="vendor">
				    	<el-select v-model="formData.vendor" placeholder="请选择品牌" style="display: block;">
					      <el-option :label="vendorData.name" :value="vendorData.code" v-for="vendorData in saveData.vendor"></el-option>
					    </el-select>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="型号" prop="version">
				    	<el-select v-model="formData.version" placeholder="请选择型号" style="display: block;">
					      <el-option :label="versionData.name" :value="versionData.code" v-for="versionData in saveData.version"></el-option>
					    </el-select>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
			 	<el-col :span="12">
					<el-form-item label="IP" prop="ip">
				    	<el-input v-model="formData.ip" maxlength="15" placeholder="请输入IP"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="端口" prop="port">
				    	<el-input v-model="formData.port" maxlength="8" placeholder="请输入端口"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
			 	<el-col :span="12">
					<el-form-item label="用户名" prop="username">
				    	<el-input v-model="formData.username" maxlength="16" placeholder="请输入用户名"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="密码" prop="password">
				    	<el-input v-model="formData.password" maxlength="16" placeholder="请输入密码"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-form-item label="描述" prop="description">
		    	<el-input type="textarea" v-model="formData.description" maxlength="128"></el-input>
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

<script type="text/javascript" src="js/resource/storage/volumepaths/add.js?t=<%=new Date().getTime() %>"></script>
</html>