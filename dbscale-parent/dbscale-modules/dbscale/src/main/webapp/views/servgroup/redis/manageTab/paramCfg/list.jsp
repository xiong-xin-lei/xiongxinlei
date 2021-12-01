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
        .vxe-body--expanded-cell .vxe-table {
            margin: 0 !important;
        }

        .col--last span:last-child {
            border-right: none !important;
        }

        .col--last vxe-cell {
            width: auto !important;
        }

        .mysqlContent > div > div {
            display: inline-block;
            white-space: normal;
        }

        .mysqlContent > div > div:nth-child(1) {
            width: 11%
        }

        .mysqlContent > div > div:nth-child(2) {
            width: 85%
        }

        .vxe-table .info-row {
            background: #e9e9e9;
        }
    </style>
</head>

<body>
<div id="tabParamCfgList" v-cloak>

    <div class="iframeBorder">

        <div class="btnParentDiv">
            <div id="btnPanel">

                <el-select v-if="highAvailable" v-model="unitValue" placeholder="请选择单元" size="small" @change="unitChange">
                    <el-option v-for="list in unitList" :label="list.name" :value="list.code"></el-option>
                </el-select>

                <el-button size="small" @click="btnClick(btndata.code,true,0)" :type=btnOperation(btndata.code,'type')
                           :disabled=btnOperation(btndata.code,'disabled') :title="btndata.name+title"
                           :icon=btnOperation(btndata.code,'icon') v-for="btndata in btnList">
                    {{ btndata.name }}
                </el-button>

                <vxe-toolbar>
                    <template v-slot:buttons>
                        <vxe-input v-model="filterName" clearable="true" placeholder="" @keyup.enter.native="searchClick"></vxe-input>
                        <el-button size="small" @click="searchClick" icon="el-icon-search" :disabled="XEUtils.isEmpty(tableDataAll)">搜索</el-button>
                        <el-button size="small" @click="returnList" icon="el-icon-refresh">刷新</el-button>
                    </template>
                </vxe-toolbar>

            </div>
        </div>


        <vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" :max-height="table_heighth" style="color: #333333;"
                   :tooltip-config="{theme: 'light'}" keep-source row-id="id" ref="xTable" :data="tableData"
                   :checkbox-config="{highlight: true, trigger: 'row'}" :resizable-config="{minWidth: resizableMinWidth}"
                   :edit-config="{trigger: 'dblclick', mode: 'row', showStatus: true,activeMethod: activeCellMethod, icon: editIcon()}"
                   :edit-rules="validRules" :row-class-name="tableRowClassName"
                   @sort-change="sortChange" @edit-closed="editClosedEvent" :sort-config="{trigger: 'cell',remote : 'true'}">
            <vxe-table-column type="checkbox" width="28" :resizable="false"></vxe-table-column>
            <vxe-table-column sortable field="key" v-bind="commonMinWidth('键')" width="21%" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="value" v-bind="commonMinWidth('值')" width="10%" :edit-render="{name: 'input'}" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="defaultValue" v-bind="commonMinWidth('默认值')" width="10%" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="range" v-bind="commonMinWidth('范围')" width="11%" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="canSet" v-bind="commonMinWidth('允许编辑')" width="7%" show-header-overflow show-overflow>
                <template v-slot="{ row }">
                    <el-switch v-model="row.canSet" disabled></el-switch>
                    <span class="enabledText disabledText">{{ row.canSet ? "是":"否" }}</span>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="mustRestart" v-bind="commonMinWidth('重启生效')" width="7%" show-header-overflow show-overflow>
                <template v-slot="{ row }">
                    <el-switch v-model="row.mustRestart" disabled></el-switch>
                    <span class="enabledText disabledText">{{ row.mustRestart ? "是":"否" }}</span>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="desc" v-bind="commonMinWidth('描述')" show-header-overflow show-overflow></vxe-table-column>
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
    <%
    Object btnPer = request.getAttribute("btnPer");
    Object rowId = request.getAttribute("rowId");
    %>
    var btnLists = <%= btnPer %>;
    var ttBtnList = btnDataSeparation(btnLists, 'tabletop');
    var rowBtnList = btnDataSeparation(btnLists, 'row').sort(sortRowSeq);
    var otherBtnList = [];
    $.each(btnLists, function (index, btn) {
        var btnPos = btn.pos;
        if (btnPos === undefined) {
            otherBtnList.push(btn);
        }
    });
    var rowId = <%= rowId %>;
</script>

<script type="text/javascript" src="js/servgroup/redis/manageTab/paramCfg/list.js?t=<%=new Date().getTime() %>"></script>

</html>