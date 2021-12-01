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
<div id="list" v-cloak>

    <div class="iframeBorder">
        <div class="btnParentDiv" style="font-size:12px">
            <div id="btnPanel">
                <div style="display: inline-block;">
                    <span class="demonstration" style="padding-right:5px">开始时间</span>
                    <el-date-picker size="small" v-model="startTime" type="date" :picker-options="pickerOptions"
                                    placeholder="选择时间" format="yyyy-MM-dd" value-format="timestamp"></el-date-picker>
                    <span class="demonstration" style="padding-left:10px;padding-right:5px">结束时间</span>
                    <el-date-picker size="small" v-model="endTime" type="date" :picker-options="pickerOptions"
                                    placeholder="选择时间" format="yyyy-MM-dd" value-format="timestamp"></el-date-picker>
                </div>
                <div style="padding-left:10px;display: inline-block;">
                    <el-button size="small" @click="SearchLogs()" icon="el-icon-search">搜索</el-button>
                </div>
                <div style="padding-left:10px;display: inline-block;">
                    <el-button size="small" @click="returnList" icon="el-icon-refresh">刷新</el-button>
                </div>
            </div>
        </div>

        <vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" :max-height="table_heighth" style="color: #333333;"
                   :tooltip-config="{theme: 'light'}" row-id="id" ref="xTable" :data="tableData"
                   :checkbox-config="{highlight: true, trigger: 'row'}" :sort-config="{trigger: 'cell',remote : 'true'}"
                   :resizable-config="{minWidth: resizableMinWidth}" :row-class-name="tableRowClassName"
                   @sort-change="sortChange">
            <%--<vxe-table-column sortable field="siteName" v-bind="commonMinWidth('所属站点')" show-header-overflow show-overflow></vxe-table-column>--%>
            <vxe-table-column sortable field="objType" v-bind="commonMinWidth('对象类型')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="objName" v-bind="commonMinWidth('对象名称')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="description" v-bind="commonMinWidth('描述')" width="20%" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="created.timestamp" v-bind="commonMinWidth('操作时间')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="creatorName" v-bind="commonMinWidth('操作者')" show-header-overflow show-overflow></vxe-table-column>

        </vxe-table>

        <vxe-pager size="mini" @page-change="handleSizeCurrentChange"
                   :page-sizes="tablePage.pageSizes" :current-page="tablePage.currentPage"
                   :page-size="tablePage.pageSize" :total="tablePage.totalResult"
                   :layouts="tablePage.pageLayouts">
        </vxe-pager>
    </div>
</div>
</body>

<script type="text/javascript" src="js/system/operatelogs/operate.js?t=<%=new Date().getTime() %>"></script>

</html>