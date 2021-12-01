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
		<el-form ref="editForm" :model="formData" :rules="formRules" label-width="100px" size="small">
			<el-row>
				<el-col :span="12">
					<el-form-item label="用户名" prop="username">
						<el-input v-model="formData.username" placeholder="请输入用户名" autocomplete="off" disabled></el-input>
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
		<el-button size="small" type="primary" @click="formSubmit('editForm')">保存</el-button>
		<el-button size="small" @click="formClose">取消</el-button>
	</div>
</div>
</body>

<script type="text/javascript" src="js/system/users/edit.js?t=<%=new Date().getTime() %>"></script>

</html>