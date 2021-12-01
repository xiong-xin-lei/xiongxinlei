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
        iframe {
            border: none;
        }
    </style>
</head>

<body>
<div id="list" v-cloak>

    <el-tabs v-model="activeName" type="border-card" @tab-click="requestsClick" style="margin-top: 20px">
        <el-tab-pane :label="tabData.name" :name="tabData.code" v-for="tabData in tabDatas"></el-tab-pane>
        <iframe width="100%" :height="iframe_heighth" :src="tabUrl()"></iframe>
    </el-tabs>

</div>
</body>

<script type="text/javascript">
    <% Object btnPer = request.getAttribute("btnPer"); %>
    var btnLists = <%= btnPer %> ;
</script>

<script type="text/javascript" src="js/system/operatelogs/list.js?t=<%=new Date().getTime() %>"></script>

</html>