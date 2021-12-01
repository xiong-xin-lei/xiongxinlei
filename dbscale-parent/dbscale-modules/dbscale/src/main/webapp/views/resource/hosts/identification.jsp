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
        .vxe-table .vxe-body--expanded-column{
            border: none;
        }
    </style>
</head>

<body>
<div id="identification" v-cloak>

    <div style="margin: 5px 3%;">
        <vxe-table size="small" auto-resize border="none" style="color: #333333;" ref="xTable" :data="tableData"
                   :tooltip-config="{theme: 'light'}" :show-header="false" row-id="id"
                   :expand-config="{trigger: 'row',iconOpen: ' ',iconClose: ' ',toggleMethod: toggleExpandRow}">
            <vxe-table-column title="状态" show-header-overflow show-overflow width="10%" align="right">
                <template v-slot="{ row }">
                    <span v-html="statusShow(row.status)" :class="row.status !=='loading' ? 'vxe-expand-text' : ''"></span>
                </template>
            </vxe-table-column>
            <vxe-table-column type="expand" width="0">
                <template v-slot:content="{ row }">
                    <vxe-table size="small" style="color: #333333;margin: 0 5% 5px;border-left: 1px #E8EAEC solid"
                               show-overflow show-header-overflow auto-resize border highlight-hover-row
                               :data="row.childRows" :show-header="false" :ref='"xTable"+row.id'
                               :expand-config="{iconOpen:' ',iconClose:' '}">
                        <vxe-table-column field="ip" title="IP" width="26%"></vxe-table-column>
                        <vxe-table-column type="expand" width="0">
                            <template v-slot:content="{ row }">
                                <div style="white-space: pre-line;margin: 10px 4%;">{{ row.msg }}</div>
                            </template>
                        </vxe-table-column>
                        <vxe-table-column field="status" title="状态">
                            <template v-slot="{ row }">
                                <span v-html="statusShow(row.status)" @click="childToggleExpandRow(row)" :class="row.status !=='success' ? 'vxe-expand-text' : ''"></span>
                            </template>
                        </vxe-table-column>
                    </vxe-table>
                </template>
            </vxe-table-column>
            <vxe-table-column field="name" title="名称" show-header-overflow show-overflow></vxe-table-column>
        </vxe-table>

    </div>
</div>
</body>

<script type="text/javascript" src="js/resource/hosts/identification.js?t=<%=new Date().getTime() %>"></script>

</html>