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
 <link rel="stylesheet" href="css/order/OrderCommon.css?t=<%=new Date().getTime() %>">
</head>

<body>
<div id="executeScaleUp" v-cloak>
	<el-steps :active="stepsActive" finish-status="success" align-center style="padding-top:10px">
            <el-step title="业务信息"></el-step>
            <el-step title="配置信息"></el-step>
    </el-steps>
    <div class="setupDIV">
		<el-form ref="executeScaleUp" :model="formData" label-width="115px" size="small">
		<div v-if="stepsActive === 0">
			<el-row>
					<el-col :span="24">
						<el-form-item label="类型" prop="type">
							<el-input disabled v-model="formData.type"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="服务名称" prop="name">
							<el-input disabled v-model="formData.name"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="所属站点" prop="site">
							<el-input disabled v-model="formData.site"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="所属业务区" prop="businessArea">
							<el-input disabled v-model="formData.businessArea"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="业务系统" prop="businessSystemName">
							<el-input disabled v-model="formData.businessSystemName"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-form-item label="业务子系统" prop="businessSubsystem">
							<el-input disabled v-model="formData.businessSubsystem"></el-input>
						</el-form-item>
					</el-col>
				</el-row>
		</div>
		<div v-else-if="stepsActive === 1">
			<el-row v-if="getSession('storageMode') == 'volumepath'">
				<el-col :span="24">
					<el-form-item label="磁盘类型" prop="diskType"status-icon>
						<el-input disabled v-model="formData.diskType"></el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="24">
					<el-form-item label="表空间" prop="dataSize"status-icon>
						<el-input disabled v-model="formData.dataSize">
							<template slot="append">G</template>
						</el-input>
					</el-form-item>
				</el-col>
			</el-row>
			<el-row>
				<el-col :span="24">
					<el-form-item label="日志空间" prop="logSize">
						<el-input disabled v-model="formData.logSize">
							<template slot="append">G</template>
						</el-input>
					</el-form-item>
				</el-col>
			</el-row>
		</div>
		</el-form>
	</div>
	<div style="position: absolute;right: 0px;bottom: 0px;width:778px">
			<div style="border-bottom: 2px solid #F3F3F3;"></div>
			<div style="float: right;padding: 10px 20px">
				<el-button size="small" @click="stepsActive++" v-show="stepsActive === 0">下一步</el-button>
				<el-button size="small" @click="stepsActive--" v-show="stepsActive === 1">上一步</el-button>
				<el-button size="small" type="primary" @click="formSubmit('executeScaleUp')" v-show="stepsActive === 1">执行</el-button>
				<el-button size="small" @click="formClose">取消</el-button>
			</div>
	</div>
</div>
</body>

<script type="text/javascript">
<%Object executeId = request.getAttribute("executeId");%>
var executeId = <%=executeId%>
</script>

<script type="text/javascript" src="js/order/redis/execute/scaleUpStorage.js?t=<%=new Date().getTime() %>"></script>
</html>