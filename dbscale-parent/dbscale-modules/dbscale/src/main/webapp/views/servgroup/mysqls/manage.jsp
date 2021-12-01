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
        iframe {
            border: none;
        }

        table {
            table-layout: fixed;
        }

        table td {
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }
    </style>
</head>

<body>
<div id="manage" v-cloak>

    <el-breadcrumb separator="/" style="padding: 20px 0;height: 16px">
        <el-breadcrumb-item>
            <img class="pageSvgColor" :src="['img/'+menu.icon]" width="12px" height="12px" onload="SVGInject(this)">
            <span style="color:#999;font-size: 12px;">{{ menu.name }}</span>
        </el-breadcrumb-item>
        <el-breadcrumb-item>
            <span style="color:#999;font-size: 12px;">{{ menu.subMenu.name }}</span>
        </el-breadcrumb-item>
        <el-breadcrumb-item>
            <span style="color:#999;font-size: 12px;">{{ menu.subMenu.servGroupName }}</span>
            <el-tooltip placement="right" v-if="showData.flag">
                <div slot="content">
                    <table>
                        <tr>
                            <td>所属系统：</td>
                            <td>{{ showData.businessSystem }}</td>
                        </tr>
                        <tr>
                            <td>所属子系统：</td>
                            <td>{{ showData.businessSubsystem }}</td>
                        </tr>
                        <tr>
                            <td>所属者：</td>
                            <td>{{ showData.owner }}</td>
                        </tr>
                        <tr>
                            <td>所属业务区：</td>
                            <td>{{ showData.businessArea }}</td>
                        </tr>
                        <tr>
                            <td>硬件架构：</td>
                            <td>{{ showData.sysArchitecture }}</td>
                        </tr>
                        <tr v-if="getSession('storageMode') == 'volumepath'">
                            <td>磁盘类型：</td>
                            <td>{{ showData.diskType }}</td>
                        </tr>
                    </table>
                </div>
                <i class="el-icon-info" style="vertical-align: middle;cursor: default;"></i>
            </el-tooltip>
        </el-breadcrumb-item>
    </el-breadcrumb>

    <el-tabs v-model="activeName" type="border-card">
        <el-tab-pane :label="tabData.name" :name="tabData.code" :data-id="tabData.id" v-for="tabData in tabDatas"></el-tab-pane>
        <iframe width="100%"<%-- :height="iframe_heighth"--%> style="height: calc(100vh - 134px);" :src="tabUrl()"></iframe>
    </el-tabs>

</div>
</body>

<script type="text/javascript">
    <%
    Object btnPer = request.getAttribute("btnPer");
    Object rowId = request.getAttribute("rowId");
    %>
    var menu = getQueryVariable("menu")
    var btnLists = <%= btnPer %>;
    var rowId = <%= rowId %>;
</script>

<script type="text/javascript" src="js/servgroup/mysqls/manage.js?t=<%=new Date().getTime() %>"></script>

</html>