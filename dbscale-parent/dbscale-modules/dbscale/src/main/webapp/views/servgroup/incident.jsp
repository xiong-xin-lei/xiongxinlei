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
        .statePointer{
            cursor: pointer;
        }
        .statePaddingLeft{
            padding-left: 21px;
        }
    </style>
</head>

<body>
<div id="incidentList" v-cloak>

    <div class="iframeBorder"<%-- style="margin: 20px 10px;"--%>>
        <div class="btnParentDiv" style="padding: 10px;">
            <div id="btnPanel">
                <vxe-toolbar>
                    <template v-slot:buttons>
                        <el-button size="small" @click="returnList" icon="el-icon-refresh">刷新</el-button>
                    </template>
                </vxe-toolbar>
            </div>
        </div>
        <vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
                   :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id"
                   ref="xTable" @sort-change="sortChange" :data="tableData"
                   :sort-config="{trigger: 'cell',remote : 'true'}"
                   :expand-config="{lazy: true, iconOpen:' ', iconClose:' ', accordion:'true', loadMethod: loadContentMethod}">
            <vxe-table-column type="expand" show-header-overflow show-overflow width="0">
                <template v-slot:content="{ row, rowIndex }">
                    <el-collapse accordion style="width: 90%;margin: 10px auto;border: 1px solid #EBEEF5;padding: 0 20px;">
                        <template v-for="list in childArray">
                            <el-collapse-item :title=" list.code + ' (' + row.childRows[list.value].length + ')' " :name="list.code">
                                <vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
                                           :align="allAlign" :ref="list.value+'Table'+rowIndex" :data="row.childRows[list.value]"
                                           :tooltip-config="{theme: 'light'}"
                                           :expand-config="{iconOpen:' ',iconClose:' '}">
                                    <vxe-table-column  type="expand" title="" show-header-overflow show-overflow width="0">
                                        <template v-slot:content="{ row }">
                                            <pre style="margin: 10px auto;width: 90%;white-space: pre-wrap;word-wrap: break-word;">{{ row.message }}</pre>
                                        </template>
                                    </vxe-table-column>
                                    <vxe-table-column field="type" title="Type" show-header-overflow show-overflow width="10%">
                                        <template v-slot="{ rowIndex }">
                                            <span class="vxe-expand-text" @click="expandClick(rowIndex,list.value,list.value+'Table'+row.rowIndex,row.rowIndex)">{{ row.childRows[list.value][rowIndex].type }}</span>
                                        </template>
                                    </vxe-table-column>
                                    <vxe-table-column field="reason" title="Reason" show-header-overflow show-overflow width="30%"></vxe-table-column>
                                    <vxe-table-column field="interval" title="Age" show-header-overflow show-overflow width="30%"></vxe-table-column>
                                    <vxe-table-column field="source" title="From" show-header-overflow show-overflow width="30%"></vxe-table-column>
                                    <%--<vxe-table-column field="message" title="Message" show-header-overflow></vxe-table-column>--%>
                                </vxe-table>
                            </el-collapse-item>
                        </template>
                    </el-collapse>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="relateId" title="单元名称" show-header-overflow show-overflow header-cell-class-name="statePaddingLeft">
                <template v-slot="{ row }">
                    <span :class="{ 'statePointer': (row.state.code !== 'passing') }" @click="toggleExpandRow(row,'relateId')" v-html="statusShow(row.state,'state')"></span>
                    <span>{{ row.relateId }}</span>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="type.display" title="类型" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="ipPort" title="地址" show-header-overflow show-overflow></vxe-table-column>
        </vxe-table>

        <vxe-pager size="mini" @page-change="handleSizeCurrentChange"
                   :page-sizes="tablePage.pageSizes" :current-page="tablePage.currentPage"
                   :page-size="tablePage.pageSize" :total="tablePage.totalResult"
                   :layouts="tablePage.pageLayouts">
        </vxe-pager>
    </div>
</div>
</body>

<script type="text/javascript" src="js/servgroup/incident.js?t=<%=new Date().getTime() %>"></script>

</html>