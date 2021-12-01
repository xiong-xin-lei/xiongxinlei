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
					<el-col :span="12">
						<el-form-item label="所属站点" prop="siteId">
							<el-select v-model="formData.siteId" placeholder="请选择所属站点" style="display: block;" disabled>
								<el-option :label="siteData.name" :value="siteData.id" v-for="siteData in siteList">
								</el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="所属业务区" prop="businessAreaId">
							<el-select v-model="formData.businessAreaId" placeholder="请选择所属业务区" style="display: block;">
								<el-option :label="businessAreaData.name" :value="businessAreaData.id"
									v-for="businessAreaData in businessAreaList"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-form-item label="集群名称" prop="name">
							<el-input v-model="formData.name" maxlength="16" placeholder="请输入集群名称"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="高可用标签" prop="haTag">
							<el-input v-model="formData.haTag" maxlength="32" placeholder="请输入高可用标签"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-form-item label="包含软件" prop="defServs">
							<el-select v-model="formData.defServs" placeholder="请选择包含软件（可多选）" style="display: block;"
								multiple>
								<el-option :value="defServsData.code" v-for="defServsData in defServsList">
									<el-tag v-if="defServsData.stateful === true" effect="dark" color="#00ABAC" style="text-align: center;height: 24px;line-height: 24px;border: none;">{{ defServsData.name }}</el-tag>
									<el-tag v-else-if="defServsData.stateful === false" effect="dark" color="#326CE5" style="text-align: center;height: 24px;line-height: 24px;border: none;">{{ defServsData.name }}</el-tag>
									<span v-else>{{ defServsData.name }}</span>
								</el-option>
							</el-select>
						</el-form-item>
				</el-row>
				<el-row>
					<el-form-item label="状态" prop="enabled" style="display:none">
						<el-switch v-model="formData.enabled"></el-switch>
						<span class="enabledText">{{ formData.enabled ? "启用":"停用" }}</span>
					</el-form-item>
				</el-row>
				<el-row>
					<el-form-item label="描述" prop="description">
						<el-input type="textarea" v-model="formData.description" maxlength="128"></el-input>
					</el-form-item>
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

<script type="text/javascript" src="js/resource/clusters/add.js?t=<%=new Date().getTime() %>"></script>
</html>