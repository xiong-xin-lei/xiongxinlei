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
<div id="actionList" v-cloak>

    <div class="iframeBorder" style="margin: 20px 10px;">
        <vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
                   :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" ref="xTable" :data="tableData"
                   :resizable-config="{minWidth: resizableMinWidth}" :sort-config="{trigger: 'cell',remote : 'true'}"
                   :expand-config="{lazy: 'true', showIcon: false, loadMethod: loadContentMethod, reserve: 'true'}"
                   @sort-change="sortChange" @toggle-row-expand="rowExpand">
            <vxe-table-column width="0" type="expand" :resizable="false">
                <template v-slot:content="{ row }">
                    <vxe-table size="small" style="color: #333333;margin: 10px 20px;border-left: 1px #E8EAEC solid" show-overflow show-header-overflow auto-resize border
                               highlight-hover-row row-id="id" :ref="'childTable'+row.index" :data="row.childRows"
                               :expand-config="{showIcon: false, expandRowKeys: childExpandRowKeysFun(row.index), reserve: 'true'}">
                        <vxe-table-column width="0" type="expand">
                            <template v-slot:content="{ row }">
                                <div style="margin: 10px auto; width: 90%; white-space: pre-wrap; overflow-wrap: break-word;">错误信息：{{ row.msg }}</div>
                            </template>
                        </vxe-table-column>
                        <vxe-table-column field="objName" title="对象名称" width="11%"></vxe-table-column>
                        <vxe-table-column field="action.display" title="任务类型" width="8%"></vxe-table-column>
                        <vxe-table-column field="priority" title="优先级" width="7%"></vxe-table-column>
                        <vxe-table-column field="startDateTime" title="开始时间" width="16%"></vxe-table-column>
                        <vxe-table-column field="endDateTime" title="结束时间" width="16%"></vxe-table-column>
                        <vxe-table-column field="consumeTime" title="耗时(s)" width="8%"></vxe-table-column>
                        <vxe-table-column field="timeout" title="超时(s)" width="8%"></vxe-table-column>
                        <vxe-table-column field="status" title="状态" width="14%">
                            <template v-slot="{ row }">
                                <span @click="childTableToggleExpandRow(row)" :class="[!XEUtils.isEmpty(row.msg) ? 'taskStatusStyle' : '']" v-html="statusShow(row,'task')"></span>
                            </template>
                        </vxe-table-column>
                        <%--<vxe-table-column field="msg" title="错误信息" show-overflow></vxe-table-column>--%>
                    </vxe-table>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="action.display" v-bind="commonMinWidth('任务类型')" show-header-overflow show-overflow style="padding:10px"></vxe-table-column>
            <vxe-table-column sortable field="startDateTime" v-bind="commonMinWidth('开始时间')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="endDateTime" v-bind="commonMinWidth('结束时间')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="consumeTime" v-bind="commonMinWidth('耗时(s)')" show-header-overflow show-overflow width="9%"></vxe-table-column>
            <vxe-table-column sortable v-bind="commonMinWidth('状态')" type="expand" show-header-overflow show-overflow width="150px">
                <template v-slot="{ row }">
                    <span @click="toggleExpandRow(row)" class="taskStatusStyle" v-html="statusShow(row,'task')"></span>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="createdName" v-bind="commonMinWidth('操作者')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column type="refresh" show-header-overflow show-overflow width="50px" :resizable="false">
                <template v-slot="{ row,rowIndex }">
                    <i v-if="!rowIndex" class="el-icon-refresh actionRefresh" @click="refreshFristClick(row,0)"></i>
                </template>
            </vxe-table-column>
        </vxe-table>

        <vxe-pager size="mini" @page-change="handleSizeCurrentChange"
                   :page-sizes="tablePage.pageSizes" :current-page="tablePage.currentPage"
                   :page-size="tablePage.pageSize" :total="tablePage.totalResult"
                   :layouts="tablePage.pageLayouts">
        </vxe-pager>
    </div>
</div>
</body>

<script type="text/javascript" src="js/task.js?t=<%=new Date().getTime() %>"></script>

</html>