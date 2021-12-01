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
    <link rel="stylesheet" href="css/xterm.css">
    <style>
        .terminal span{
            font-family: Consolas,"Courier New",Courier,FreeMono,monospace;
            font-size: 14px;
        }
    </style>
</head>

<body>
<div id="terminal" style="width: 100%;height: 100%"></div>
</body>
<script>
    var addr = getQueryVariable("addr")
    var sessionId = getQueryVariable("sessionId")
    var scheme = getQueryVariable("scheme")
    var openType = getQueryVariable("openType")
    /*var addr = "192.168.33.21:9090"
    var sessionId = "ad0e3f74163dd17f0d141fd9ed441ae8"*/
    var sshUrl = addr + '/api/sockjs/' + XEUtils.random(100, 999) + '/' + randomWord(false, 8) + '/websocket?' + sessionId;
</script>
<script src="js/xterm.js"></script>
<script src="js/webssh.js?t=<%=new Date().getTime() %>"></script>
<script type="text/javascript" src="js/servgroup/webSSH.js?t=<%=new Date().getTime() %>"></script>

</html>