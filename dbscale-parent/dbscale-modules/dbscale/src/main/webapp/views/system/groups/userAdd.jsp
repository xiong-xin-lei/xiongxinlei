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
<div id="userAdd" v-cloak>
    <div style="margin: 0 auto;width: 90%;">
        <el-input placeholder="" v-model="filterName" style="margin-top: 10px;">
            <el-button :disabled="XEUtils.isEmpty(filterName)" slot="append" icon="el-icon-search" @click="searchClick()"></el-button>
        </el-input>
    </div>
    <vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;margin: 10px 0;"
               ref="xTable" :data="tableData" height="140px"
               :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="username"
               :checkbox-config="{highlight: true, trigger: 'row'}">
        <vxe-table-column type="checkbox" width="28" :resizable="false"></vxe-table-column>
        <vxe-table-column field="username" title="登录用户名" show-header-overflow show-overflow></vxe-table-column>
        <vxe-table-column field="name" title="用户姓名" show-header-overflow show-overflow></vxe-table-column>
        <vxe-table-column field="company" title="所属单位" show-header-overflow show-overflow></vxe-table-column>
    </vxe-table>
    <div style="border-bottom: 2px solid #F3F3F3;"></div>
    <div style="float: right;padding: 10px 20px">
        <el-button size="small" type="primary" @click="userAddFun('xTable')">添加</el-button>
        <el-button size="small" @click="formClose">取消</el-button>
    </div>
</div>
</body>

<script type="text/javascript" src="js/system/groups/userAdd.js?t=<%=new Date().getTime() %>"></script>

</html>