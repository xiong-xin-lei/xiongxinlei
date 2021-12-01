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
<div id="add" v-cloak>
    <div style="padding:30px 40px 30px 20px">
		<el-form ref="addForm" :model="formData" label-width="100px" :rules="formRules" size="small">
			<el-row>
				<el-col :span="12">
					<el-form-item label="用户名" prop="username">
						<el-input v-model="formData.username" placeholder="请输入用户名" autocomplete="off"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-form-item label="密码" prop="password">
						<el-input v-model="formData.password" placeholder="请输入密码" maxlength="32" show-password autocomplete="off">
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
				</el-col>
				<el-col :span="12">
					<el-form-item label="确认密码" prop="verifyPassword" label-width="130px">
						<el-input v-model="formData.verifyPassword" placeholder="请再次输入密码" maxlength="32" show-password autocomplete="off"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-form-item label="姓名" prop="name">
						<el-input v-model="formData.name" placeholder="请输入姓名"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="手机号码" prop="telephone" label-width="130px">
						<el-input v-model="formData.telephone" placeholder="请输入手机号码" maxlength="11" oninput="value=value.replace(/[^\d]/g,'')"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-form-item label="邮箱" prop="email">
						<el-input v-model="formData.email" placeholder="请输入邮箱"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="所属单位" prop="company" label-width="130px">
						<el-input v-model="formData.company" placeholder="请输入所属单位"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-form-item label="紧急联系人" prop="emerContact">
						<el-input v-model="formData.emerContact" placeholder="请输入紧急联系人"></el-input>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="紧急联系人电话" prop="emerTel" label-width="130px">
						<el-input v-model="formData.emerTel" placeholder="请输入紧急联系人电话" maxlength="11" oninput="value=value.replace(/[^\d]/g,'')"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-form-item label="角色" prop="role">
						<el-select v-model="formData.role" placeholder="请选择角色" style="display: block;">
							<el-option :label="list.name" :value="list.id" v-for="list in roleList"></el-option>
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="组别" label-width="130px">
						<el-input v-model="formData.username" disabled></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-form-item label="自动审批" prop="autoApproved">
						<el-switch v-model="formData.autoApproved"></el-switch>
						<span class="enabledText">{{ formData.autoApproved ? "是":"否" }}</span>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="自动执行" prop="autoExecute" label-width="130px">
						<el-switch v-model="formData.autoExecute"></el-switch>
						<span class="enabledText">{{ formData.autoExecute ? "是":"否" }}</span>
					</el-form-item>
				</el-col>
			</el-row>
		</el-form>
	</div>
	<div style="border-bottom: 2px solid #F3F3F3;"></div>
	<div style="float: right;padding: 10px 20px">
		<el-button size="small" type="primary" @click="formSubmit('addForm')">保存</el-button>
		<el-button size="small" @click="formClose">取消</el-button>
	</div>
</div>
</body>

<script type="text/javascript" src="js/system/users/add.js?t=<%=new Date().getTime() %>"></script>

</html>