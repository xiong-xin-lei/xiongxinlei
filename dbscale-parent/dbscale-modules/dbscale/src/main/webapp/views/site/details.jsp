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
	    .el-textarea__inner {
			border: 0px solid #DCDFE6;
	    }
	    .el-input__inner {
		    border: 0px solid #DCDFE6;
	    }
    </style>
</head>

<body>
    <div id="details" v-cloak>
        <div style="margin: 20px 20px 0px 0px">
            <el-form  label-width="90px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="站点名称" prop="siteName">
                            <el-input v-model="siteName" maxlength="8" disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="地域" prop=region>
                            <el-input v-model="region" maxlength="8" disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="类型" prop="type">
                            <el-input v-model="type" disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="域名" prop="domain">
                            <el-input v-model="domain" disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="端口" prop="port">
                            <el-input v-model="port" disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="状态" prop="status">
                            <el-input v-model="status" disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                	<el-col :span="12">
                        <el-form-item label="网络模式" prop="networkMode">
                            <el-input v-model="networkMode" disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="创建时间" prop="createdtimestamp">
                            <el-input v-model="createdtimestamp" disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="版本" prop="version">
                            <el-input type="textarea" rows="3" v-model="version" disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="描述" prop="description">
                            <el-input type="textarea" rows="3" v-model="description" disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </div>
    </div>
</body>

<script type = "text/javascript" src="js/site/details.js?t=<%=new Date().getTime() %>"></script>
</html>