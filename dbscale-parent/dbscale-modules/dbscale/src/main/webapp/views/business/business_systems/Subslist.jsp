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
    <base href="<%=basePath%>">
    </base>
    <!-- 引入样式 -->
    <script src="js/commonCss.js?t=<%=new Date().getTime() %>"></script>
    <!-- 引入脚本 -->
    <script src="js/commonJs.js?t=<%=new Date().getTime() %>"></script>
</head>

<body>
<div id="Subsyslist" v-cloak>
    <el-breadcrumb separator="/" style="padding: 20px 0;">
        <el-breadcrumb-item>
            <img class="pageSvgColor" :src="['img/'+menu.icon]" width="12px" height="12px" onload="SVGInject(this)">
            <span style="color:#999;font-size: 12px;">{{ menu.name }}</span>
        </el-breadcrumb-item>
        <el-breadcrumb-item><span style="color:#999;font-size: 12px;">{{ menu.subMenu.name }}
                    ({{businessSystemList.name}})</span>
        </el-breadcrumb-item>
        <el-breadcrumb-item>
            <span style="color:#999;font-size: 12px;">{{ menu.subMenu.sSubMenu.name }}</span>
        </el-breadcrumb-item>
    </el-breadcrumb>

    <div class="iframeBorder">
        <div class="btnParentDiv">
            <div id="btnPanel">
                <template v-for="btndata in btnList">
                    <el-button size="small" @click="btnClick(btndata.code,true,0)" :type=btnOperation(btndata.code,'type')
                               :disabled=disabledValue[btndata.code] :title="btndata.name+title" :icon=btnOperation(btndata.code,'icon')
                               v-if="btndata.type!=='navigation'">
                        {{ btndata.name }}
                    </el-button>
                    <el-dropdown v-else>
                        <el-button size="small">
                            {{ btndata.name }}<i class="el-icon-arrow-down el-icon--right"></i>
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item class="el-dropdown-menu-item" v-for="list in btndata.childrens">
                                <vxe-button type="text" @click="btnClick(list.code,true,0)" :disabled=disabledValue[list.code]>
                                    {{ list.name }}
                                </vxe-button>
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </template>

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
                   :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" ref="xTable" :data="tableData"
                   :resizable-config="{minWidth: resizableMinWidth}" :row-class-name="tableRowClassName"
                   :checkbox-config="{highlight: true, trigger: 'row'}" :sort-config="{trigger: 'cell',remote : 'true'}"
                   @sort-change="sortChange" @checkbox-change="selectChangeEvent" @checkbox-all="selectAllEvent">
            <vxe-table-column type="checkbox" width="28" :resizable="false"></vxe-table-column>
            <vxe-table-column sortable field="name" v-bind="commonMinWidth('业务子系统名称')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="gmtCreate" v-bind="commonMinWidth('创建时间')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="description" v-bind="commonMinWidth('描述')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="status" v-bind="commonMinWidth('是否可用')" type="html" show-header-overflow show-overflow width="90px">
                <template v-slot="{ row }">
                    <div v-html="statusShow(row.status)"></div>
                </template>
            </vxe-table-column>
            <vxe-table-column title="操作" :width="operationWidth" :visible="!XEUtils.isEmpty(rowBtnList)" :resizable="false">
                <template v-slot="{ row }">
                    <div class="operationBtn" v-for="value in rowBtnList">
                            <span v-if="value.type!=='navigation'">
                                <vxe-button type="text" status="primary" :disabled="isAble(value.code,row)" @click="btnClick(value.code,false,row)" v-html="rowBtnValue(value)"></vxe-button>
                            </span>
                        <el-dropdown v-else>
                            <div style="line-height: 16px;">
                                <span v-html="rowBtnValue(value)"></span>
                                <i class="el-icon-arrow-down el-icon--right"></i>
                            </div>
                            <el-dropdown-menu>
                                <el-dropdown-item class="el-dropdown-menu-item" v-for="list in value.childrens">
                                    <vxe-button type="text" @click="btnClick(list.code,false,row)" :disabled="isAble(list.code,row)" v-html="rowBtnValue(list)"></vxe-button>
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </el-dropdown>
                    </div>
                </template>
            </vxe-table-column>
        </vxe-table>

        <vxe-pager size="mini" @page-change="handleSizeCurrentChange" :page-sizes="tablePage.pageSizes"
                   :current-page="tablePage.currentPage" :page-size="tablePage.pageSize" :total="tablePage.totalResult"
                   :layouts="tablePage.pageLayouts">
        </vxe-pager>
    </div>
</div>
</body>

<script type="text/javascript">
    <% Object btnPer = request.getAttribute("btnPer");
       Object SysIds = request.getAttribute("SysIds");%>
    var btnLists = <%= btnPer %>;
    var ttBtnList = btnDataSeparation(btnLists, 'tabletop');
    var rowBtnList = btnDataSeparation(btnLists, 'row').sort(sortRowSeq);
    var operationWidth = operationWidthInit(rowBtnList)
    var SysIds =
    <%=SysIds%>
    var menu = getQueryVariable("menu")
</script>

<script type="text/javascript" src="js/business/business_systems/Subslist.js?t=<%=new Date().getTime() %>">
</script>

</html>