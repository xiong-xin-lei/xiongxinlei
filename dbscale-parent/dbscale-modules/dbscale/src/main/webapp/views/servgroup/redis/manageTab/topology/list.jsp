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
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%>
    <base href="<%=basePath%>"></base>
    <!-- 引入样式 -->
    <script src="js/commonCss.js?t=<%=new Date().getTime() %>"></script>
    <!-- 引入脚本 -->
    <script src="js/commonJs.js?t=<%=new Date().getTime() %>"></script>
    <!-- 引入地图组件 -->
    <script src="js/echarts.min.js"></script>
    <style>
        .btnParentDiv {
            padding: 0;
            border: none;
            width: 1370px;
            margin: 0 auto;
        }
    </style>

<body>
<div id="topologyList">
    <div class="btnParentDiv">
        <el-button size="small" icon="el-icon-refresh" @click="returnList" circle style="position: fixed; top: 0; right: 0; z-index: 1;"></el-button>
    </div>
    <div id="redisTopology" style="height: 550px; width: 1370px; margin: 0 auto;"></div>
</div>
</body>

<script type="text/javascript">
    <% Object rowId = request.getAttribute("rowId");%>
    var rowId = <%= rowId %>;
</script>

<script type="module" src="js/servgroup/redis/manageTab/topology/list.js?t=<%=new Date().getTime() %>"></script>
</html>
