<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
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
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    %>
    <base href="<%=basePath%>">
    </base>
    <!-- 引入样式 -->
    <script src="js/commonCss.js?t=<%=new Date().getTime() %>"></script>
    <!-- 引入脚本 -->
    <script src="js/commonJs.js?t=<%=new Date().getTime() %>"></script>
    <!-- 引入地图组件 -->
    <script src="js/echarts.min.js?t=<%=new Date().getTime() %>"></script>
    <!-- 引入中国地图 -->
    <script src="js/china.js?t=<%=new Date().getTime() %>"></script>
    <link rel="stylesheet" href="css/list.css?t=<%=new Date().getTime() %>">
    <style>
        .btnParentDiv {
            border-bottom: 2px solid #c0c0c0;
        }
    </style>
</head>


<body>
    <div id="site" v-cloak>
        <header id="header">
            <div id="logo-group" style="height:42px">
                <!--  <span id="logo"> <img src="img/login_Logo.png" width="10%">
        </span> -->
            </div>
        </header>
        <div class="container">
            <div class="row">
                <div id="list-alert"></div>
            </div>
            <div class="row">
                <div id="updating-chart" class="chart txt-color-blue" style="height: 400px; margin: 0 5px 10px;">
                </div>
            </div>

            <div class="row">
                <h1 class="own_self_style_blue login-header-big"></h1>
                <article class="col-sm-4">
                    <div class="modal fade" id="remoteModal" role="dialog" aria-labelledby="remoteModalLabel"
                        aria-hidden="true" data-backdrop="static" data-keyboard="false">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <!-- content will be filled here from "ajax/modal-content/model-content-1.html" -->
                            </div>
                        </div>
                    </div>
                </article>
            </div>

            <div class="btnParentDiv" style="margin: 20px;">
                <div id="btnPanel">
                    <template>
                        <el-button size="small" @click="addAjaxFun" icon="el-icon-plus" type="primary">新增</el-button>
                    </template>
                    <div style="float:right">
                        <el-tooltip placement="left">
                            <div slot="content">
                                <span><span class="tooltipText">Web版本：</span>{{ version.web }}</span><br />
                                <span><span class="tooltipText">CM版本：</span>{{ version.cm }}</span>
                            </div>
                            <i class="el-icon-info" style="font-size: 24px;line-height:32px;color:#d3d3d3"></i>
                        </el-tooltip>
                    </div>
                </div>
            </div>

            <div style="margin: 20px;">
                <el-row :gutter="30" v-if="!XEUtils.isEmpty(datas)">
                    <el-col :span="5" v-for="site in datas">
                        <el-card :body-style="{ padding: '0px' }" shadow="hover">
                            <div style="font-size: 12px;position: relative;padding: 16px 10px;line-height: 24px;">
                                <i class="el-icon-info"
                                    style="position: absolute;top:3px;right:24px;cursor:pointer;font-size: 18px;"
                                    v-on:click="detailsSite(site.id)" title="站点详情"></i>
                                <i class="el-icon-close"
                                    style="position: absolute;top:3px;right:3px;cursor:pointer;font-size: 18px;"
                                    v-on:click="deleteSite(site)" title="删除站点"></i>
                                <span class="siteLabel">站点名称：</span>{{ site.name }}&nbsp;<i class="el-icon-edit-outline"
                                    style="cursor:pointer;font-size: 16px;margin-top:10px;" v-on:click="editAjaxfun(site.id)" title="站点详情"></i><br>
                                <span class="siteLabel">地域：</span>{{ site.region.display }}<br>
                                <span class="siteLabel">状态：</span><span
                                    v-html="statusShow(site.state.display)"></span><span>{{site.state.display}}</span>
                                <el-button size="mini" type="primary" title="进入站点" v-on:click="loginSite(site)"
                                    style="position: absolute;right:3px;bottom:3px">
                                    Enter<i class="el-icon-back el-icon--right"></i></el-button>
                                <div style="height: 13px"></div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
                <div v-else style="text-align: center">
                    <span style="color: #606266">暂无数据</span>
                </div>
            </div>
        </div>
    </div>
</body>

<script type="text/javascript">
    <% Object btnPer = request.getAttribute("btnPer"); %>
    var btnLists = <%= btnPer %> ;
</script>
<script type="text/javascript" src="js/site/list.js?t=<%=new Date().getTime() %>"></script>

</html>