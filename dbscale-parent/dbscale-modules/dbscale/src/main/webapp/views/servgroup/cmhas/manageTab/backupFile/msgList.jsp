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
        pre {
            white-space: pre-wrap !important;
            word-wrap: break-word !important;
            *white-space: normal !important;
        }
    </style>
</head>

<body>
<div id="msgList" v-cloak>
    <pre style="margin: 20px 10px;">{{ msg }}</pre>
</div>
</body>

<script type="text/javascript" src="js/servgroup/cmhas/manageTab/backupFile/msgList.js?t=<%=new Date().getTime() %>"></script>

</html>