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
		.el-input-group__append, .el-input-group__prepend {
			background-color: #F5F7FA;
			color: #909399;
			vertical-align: middle;
			display: table-cell;
			position: relative;
			border: 1px solid #DCDFE6;
			border-radius: 4px;
			padding: 0 5px;
			width: 1px;
			white-space: nowrap;
		}
		.el-tooltip__popper{
			max-width: 70%;
		}
	</style>
</head>

<body>
<div id="updatePwd" v-cloak>
	<div style="padding:30px 40px 30px 20px">
		<el-form ref="updatePwdForm" :model="formData" label-width="100px" :rules="formRules" size="small">
			<el-form-item label="原密码" prop="originalPwd">
				<el-input v-model="formData.originalPwd" placeholder="请输入原密码" maxlength="32" show-password autocomplete="off"></el-input>
			</el-form-item>
			<el-form-item label="新密码" prop="newPwd">
				<el-input v-model="formData.newPwd" placeholder="请输入新密码" maxlength="32" show-password autocomplete="off">
					<template slot="append">
						<el-tooltip placement="bottom">
							<div slot="content">
								1. 密码必须包含数字、小写字母、大写字母和特殊字符，长度必须是8位及以上。<br/>
								2. 密码不能以特殊字符开头和结尾<br/>
								3. 密码中不能包含有特殊字符&<br/>
								4. 密码中不能包含有特殊字符$<br/>
								5. 密码中不能有空格</div>
							<i class="el-icon-question" style="font-size:18px"></i>
						</el-tooltip>
					</template>
				</el-input>
			</el-form-item>
			<el-form-item label="确认新密码" prop="verifyNewPwd">
				<el-input v-model="formData.verifyNewPwd" placeholder="请再次输入新密码" maxlength="32" show-password autocomplete="off"></el-input>
			</el-form-item>
		</el-form>
	</div>
	<div style="border-bottom: 2px solid #F3F3F3;"></div>
	<div style="float: right;padding: 10px 20px">
		<el-button size="small" type="primary" @click="formSubmit('updatePwdForm')">保存</el-button>
		<el-button size="small" @click="formClose">取消</el-button>
	</div>
</div>
</body>

<script type="text/javascript" src="js/system/users/updatePwd.js?t=<%=new Date().getTime() %>"></script>

</html>