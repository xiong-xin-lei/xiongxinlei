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
<div id="editLittle" v-cloak>
	<div style="margin: 30px 40px 0 0;">
		<el-form ref="editLittleForm" :model="formData" label-width="115px" :rules="formRules" size="small">
			<el-form-item label="主机IP" prop="ip">
		    	<el-input v-model="formData.ip" maxlength="15" disabled></el-input>
			</el-form-item>
			<el-row>
				<el-col :span="12">
					<el-form-item label="单元上限" prop="maxUnitCnt">
						<el-input-number v-model="formData.maxUnitCnt" size="small" :min="3" :max="maxUnitCntMax" placeholder="请输入单元上限"></el-input-number>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="资源分配率上限" prop="maxUsage">
						<el-input-number v-model="formData.maxUsage" size="small" :min="10" :max="100" placeholder="请输入资源分配率上限"></el-input-number>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="12">
					<el-form-item label="角色" prop="role">
						<el-select v-model="formData.role" placeholder="请选择角色" style="display: block;">
							<el-option :label="data.name" :value="data.code" v-for="data in roleList"></el-option>
						</el-select>
					</el-form-item>
				</el-col>
			</el-row>
			<el-form-item label="描述" prop="description">
		    	<el-input type="textarea" :rows="4" v-model="formData.description" maxlength="128"></el-input>
			</el-form-item>
			    
		</el-form>
	</div>
	<div style="border-bottom: 2px solid #F3F3F3;"></div>
	<div style="float: right;padding: 10px 20px">
		<el-button size="small" type="primary" @click="formSubmit('editLittleForm')">保存</el-button>
		<el-button size="small" @click="formClose">取消</el-button>
	</div>
</div>
</body>

<script type="text/javascript">
<%Object hostsId = request.getAttribute("hostsId");%>
var hostId = <%=hostsId%>
</script>

<script type="text/javascript" src="js/resource/hosts/edit/editLittle.js?t=<%=new Date().getTime() %>"></script>
</html>