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
                   :expand-config="{lazy: true,iconOpen:' ',iconClose:' ', loadMethod: loadContentMethod}"
                   @sort-change="sortChange">
            <vxe-table-column sortable type="expand" width="0" :resizable="false">
                <template v-slot:content="{ row }">
                    <vxe-table size="small" :ref="'childTable'+row.task.id" style="color: #333333;margin: 10px 40px;border-left: 1px #E8EAEC solid"
                               show-overflow show-header-overflow auto-resize border highlight-hover-row row-id="id"
                               :data="row.childRows" :expand-config="{showIcon: false, reserve: 'true'}">
                        <vxe-table-column width="0" type="expand">
                            <template v-slot:content="{ row }">
                                <div style="margin: 10px auto; width: 95%; white-space: pre-wrap; overflow-wrap: break-word;">错误信息：{{ row.msg }}</div>
                            </template>
                        </vxe-table-column>
                        <vxe-table-column field="objName" title="对象名称" width="9%"></vxe-table-column>
                        <vxe-table-column field="action.display" title="任务类型" width="10%"></vxe-table-column>
                        <vxe-table-column field="priority" title="优先级" width="7%"></vxe-table-column>
                        <vxe-table-column field="startDateTime" title="开始时间" width="16%"></vxe-table-column>
                        <vxe-table-column field="endDateTime" title="结束时间" width="16%"></vxe-table-column>
                        <vxe-table-column field="consumeTime" title="耗时（s）" width="10%"></vxe-table-column>
                        <vxe-table-column field="timeout" title="超时（s）" width="10%"></vxe-table-column>
                        <vxe-table-column field="status" type="html" title="任务状态" width="10%">
                            <template v-slot="{ row }">
                                <span @click="childTableToggleExpandRow(row)" :class="[!XEUtils.isEmpty(row.msg) ? 'taskStatusStyle' : '']" v-html="statusShow(row,'subTask')"></span>
                            </template>
                        </vxe-table-column>
                    </vxe-table>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="unitRelateName" v-bind="commonMinWidth('单元名称')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="sourceHostIp" v-bind="commonMinWidth('源主机')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="targetHostIp" v-bind="commonMinWidth('目标主机')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="created.timestamp" v-bind="commonMinWidth('任务状态')" show-header-overflow show-overflow>
                <template v-slot="{ row }">
                    <span class="taskStatusStyle" v-html="statusShow(row,'task')" @click="toggleExpandRow(row)"></span>
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

<script type="text/javascript" src="js/system/operatelogs/forceRebuild.js?t=<%=new Date().getTime() %>"></script>

</html>