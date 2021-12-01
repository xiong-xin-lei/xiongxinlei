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
    <script src="js/commonCss.js"></script>
    <!-- 引入脚本 -->
    <script src="js/commonJs.js"></script>
</head>

<body>
    <div id="add" v-cloak>
         <div style="padding:30px 40px 30px 20px">
            <el-form ref="addForm" :model="formData" label-width="95px" :rules="formRules" size="small">
                <el-form-item label="所属存储" prop="remoteStorages">
                	 <el-input v-model="formData.remoteStorages" maxlength="16" disabled></el-input>
                </el-form-item>
                <el-form-item label="存储池名称" prop="name">
                    <el-input v-model="formData.name" maxlength="16"></el-input>
                </el-form-item>
                <el-form-item label="性能等级" prop="performance">
                    <el-select v-model="formData.performance" placeholder="请选择性能等级" style="display: block;">
                        <el-option :label="item.name" :value="item.code" v-for="item in performanceList"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="状态" prop="enabled" style="display:none">
			    	<el-switch v-model="formData.enabled"></el-switch>
			    	<span class="enabledText">{{ formData.enabled ? "启用":"停用" }}</span>
				</el-form-item>
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

<script type="text/javascript" src="js/resource/storage/volumepaths/RGmanage/add.js?t=<%=new Date().getTime() %>"></script>
</html>