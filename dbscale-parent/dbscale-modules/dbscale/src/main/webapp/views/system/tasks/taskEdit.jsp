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
<div id="taskEdit" v-cloak>

    <div class="iframeBorder">

        <vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" :max-height="table_heighth" style="color: #333333;"
                   :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" ref="xTable" :data="tableData"
                   :sort-config="{trigger: 'cell',remote : 'true'}" :edit-config="{trigger: 'dblclick', mode: 'cell'}"
                   :resizable-config="{minWidth: resizableMinWidth}" :row-class-name="tableRowClassName"
                   @sort-change="sortChange" @edit-closed="editClosedEvent">
            <vxe-table-column sortable field="objType.display" v-bind="commonMinWidth('对象类型')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="action.display" v-bind="commonMinWidth('动作类型')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="timeout" v-bind="commonMinWidth('超时时间(秒)')" :edit-render="{name: 'input', attrs: {type: 'number'}}" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="description" v-bind="commonMinWidth('描述')" show-header-overflow show-overflow></vxe-table-column>
        </vxe-table>

        <vxe-pager size="mini" @page-change="handleSizeCurrentChange"
                   :page-sizes="tablePage.pageSizes" :current-page="tablePage.currentPage"
                   :page-size="tablePage.pageSize" :total="tablePage.totalResult"
                   :layouts="tablePage.pageLayouts">
        </vxe-pager>
    </div>
</div>
</body>

<script type="text/javascript">
    <%Object btnPer=request.getAttribute("btnPer");%>
    var btnLists = <%=btnPer%>;
</script>

<script type="text/javascript" src="js/system/tasks/taskEdit.js?t=<%=new Date().getTime() %>"></script>

</html>