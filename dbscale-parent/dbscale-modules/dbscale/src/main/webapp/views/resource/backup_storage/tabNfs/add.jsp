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
		<el-form ref="addForm" :model="formData" label-width="110px" :rules="formRules" size="small">
			<el-row>
				<el-col :span="12">
					<el-form-item label="名称" prop="name">
						<el-input v-model="formData.name" maxlength="32" placeholder="请输入名称"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="所属业务区" prop="businessAreaId">
						<el-select v-model="formData.businessAreaId" placeholder="请选择所属业务区" style="display: block;">
							<el-option :label="item.name" :value="item.id" v-for="item in businessAreaList">
							</el-option>
						</el-select>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-form-item label="NFS地址" prop="nfsIp">
						<el-input v-model="formData.nfsIp" maxlength="32" placeholder="请输入NFS地址"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="源目录" prop="nfsSource">
						<el-input v-model="formData.nfsSource" maxlength="32" placeholder="请输入源目录"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-form-item label="挂载参数" prop="nfsOpts">
						<el-input v-model="formData.nfsOpts" maxlength="32" placeholder="请输入挂载参数"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-form-item label="描述" prop="description" >
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

<script type="text/javascript" src="js/resource/backup_storage/tabNfs/add.js?t=<%=new Date().getTime() %>"></script>
</html>