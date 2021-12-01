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
</head>

<body>
<div id="app" v-cloak>
    <div style="margin: 20px auto;width: 90%;">
        <%--<el-tree :data="data" :props="props" node-key="id" :default-expanded-keys="[2, 3]" :default-checked-keys="[5]"
                 show-checkbox @check-change="handleCheckChange"></el-tree>--%>

        <div style="padding:30px 40px 30px 20px">
            <el-tree :data="data" :props="props" node-key="id" :default-checked-keys="defaultCheckeds"
                     ref="tree" show-checkbox></el-tree>
        </div>
        <div style="border-bottom: 2px solid #F3F3F3;"></div>
        <div style="float: right;padding: 10px 20px">
            <el-button size="small" type="primary" @click="saveData('tree')">保存</el-button>
        </div>
    </div>
</div>
</body>

<script type="text/javascript" src="js/system/roles/privilegeTab/app.js?t=<%=new Date().getTime() %>"></script>
</html>