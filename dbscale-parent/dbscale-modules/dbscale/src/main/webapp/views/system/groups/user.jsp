<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>DBScale</title>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    %>
    <base href="<%=basePath%>"></base>
    <!-- 引入样式 -->
    <script src="js/commonCss.js?t=<%=new Date().getTime() %>"></script>
    <!-- 引入脚本 -->
    <script src="js/commonJs.js?t=<%=new Date().getTime() %>"></script>
    <style>
        .el-transfer__buttons{
            width: 45px;
        }
        .el-button+.el-button{
            margin: 0;
        }
        .el-transfer-panel{
            width: 42.8%;
            height: 557px;
        }
        .transfer-footer {
            margin-left: 15px;
            padding: 6px 5px;
        }
    </style>
</head>

<body>
<div id="user" v-cloak>
    <div style="margin: 20px auto;width: 90%;">
        <template>
            <el-transfer filterable v-model="value" :titles="['人员信息', '人员信息']" :data="data" @change="handleChange">
                <el-button class="transfer-footer" slot="right-footer" size="small" @click="userAddFun()">添加</el-button>
            </el-transfer>
        </template>
    </div>
</div>
</body>

<script type="text/javascript" src="js/system/groups/user.js?t=<%=new Date().getTime() %>"></script>

</html>