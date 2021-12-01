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
<div id="rebuild" v-cloak>
    <el-steps :active="stepsActive" finish-status="success" align-center style="padding-top:10px">
        <el-step v-for="item in stepList" v-if="item.isShow" :title="item.name"></el-step>
    </el-steps>

    <div style="padding-top:10px" v-if="stepsActive === 0" class="elSteps"><%--iframeBorder--%>
        <div style="padding:10px 10px 10px 340px">
            <vxe-toolbar>
                <template v-slot:buttons>
                    <vxe-input v-model="filterName" clearable="true" placeholder=""></vxe-input>
                    <el-button size="small" @click="searchClick" icon="el-icon-search"
                               :disabled="XEUtils.isEmpty(tableDataAll)">搜索
                    </el-button>
                    <el-button size="small" @click="returnList" icon="el-icon-refresh">刷新
                    </el-button>
                </template>
            </vxe-toolbar>
        </div>
        <vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
                   :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" height="180"
                   ref="restore0" @sort-change="sortChange" :data="tableData"
                   :radio-config="{highlight: true, trigger: 'row', checkRowKey: defaultSelecteRow}"
                   :sort-config="{trigger: 'cell',remote : 'true'}">
            <vxe-table-column type="radio" width="28"></vxe-table-column>
            <vxe-table-column sortable field="unitName" title="所属单元" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="sizeData" title="大小" show-header-overflow show-overflow width="11%"></vxe-table-column>
            <vxe-table-column sortable field="createdAt" title="创建时间" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="name" title="文件名" show-header-overflow show-overflow width="38%"></vxe-table-column>
        </vxe-table>
        <vxe-pager size="mini" @page-change="handleSizeCurrentChange"
                   :page-sizes="tablePage.pageSizes" :current-page="tablePage.currentPage"
                   :page-size="tablePage.pageSize" :total="tablePage.totalResult"
                   :layouts="tablePage.pageLayouts">
        </vxe-pager>
    </div>

    <div style="padding-top:10px" v-if="stepsActive === 1" class="elSteps">
        <el-row>
            <el-col :span="19">
                <div style="padding:10px 10px;height: 30px;">
                    <el-form ref="rebuildForm" :inline="true" :model="formData" :rules="formRules" label-width="70px" size="small">
                        <el-form-item label="重建策略">
                            <el-select v-model="formData.strategy" placeholder="请选择重建策略" @change="hostChange">
                                <el-option :label="data.name" :value="data.code" v-for="data in rebuildList">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <%--<el-form-item label="强制重建">
                            <el-switch v-model="formData.force"></el-switch>
                            <span class="enabledText">{{ formData.force ? "是":"否" }}</span>
                        </el-form-item>--%>
                    </el-form>
                </div>
            </el-col>
            <el-col :span="3">
                <div style="padding:10px 14px;float:right">
                    <vxe-toolbar>
                        <template v-slot:buttons>
                            <%--<vxe-input v-model="filterNames" clearable="true" placeholder=""></vxe-input>
                            <el-button size="small" @click="searchClicks" icon="el-icon-search"
                                       :disabled="XEUtils.isEmpty(tableDataAlls)">搜索
                            </el-button>--%>
                            <el-button size="small" @click="hostChange" :disabled="formData.strategy != 'assign'" icon="el-icon-refresh">刷新
                            </el-button>
                        </template>
                    </vxe-toolbar>
                </div>
            </el-col>
        </el-row>
        <div>
            <vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
                       :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" ref="restore1" @sort-change="sortChange"
                       :data="tableDatas" @radio-change="selectChangeEvent" height="180"
                       :radio-config="{trigger: 'row', checkRowKey: defaultSelecteRows}"
                       :sort-config="{trigger: 'cell',remote : 'true'}">
                <vxe-table-column type="radio" width="28"></vxe-table-column>
                <vxe-table-column sortable field="ip" title="主机IP" show-header-overflow show-overflow>
                </vxe-table-column>
                <vxe-table-column field="resourceRates" title="资源分配率" show-header-overflow width="48%">
                    <template v-slot="{ row }">
                        <div>
                            <el-row>
                                <el-col :span="listdata.width" v-for="listdata in row.resourceRates" :style="{ color: listdata.textColor }">
                                    {{ listdata.name }}：
                                    <span>{{ listdata.value }}</span>
                                </el-col>
                            </el-row>
                        </div>
                    </template>
                </vxe-table-column>
                <vxe-table-column sortable field="state" title="运行状态" type="html" show-header-overflow show-overflow>
                    <template v-slot="{ row }">
                        <span v-html="statusShow(row,'state')"></span><br/>
                        <span v-html="statusShow(row,'role')"></span>
                    </template>
                </vxe-table-column>
                <vxe-table-column sortable field="enabled" title="是否可用" type="html" show-header-overflow show-overflow>
                    <template v-slot="{ row }">
                        <div v-html="statusShow(row,'enabled')"></div>
                    </template>
                </vxe-table-column>
            </vxe-table>
            <vxe-pager size="mini" @page-change="handleSizeCurrentChange" :page-sizes="tablePages.pageSizes"
                       :current-page="tablePages.currentPage" :page-size="tablePages.pageSize" :total="tablePages.totalResult"
                       :layouts="tablePages.pageLayouts">
            </vxe-pager>
        </div>
    </div>

    <div style="padding-top:10px" v-if="stepsActive === 2" class="elSteps"><%--iframeBorder--%>
        <vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;" :align="allAlign" row-id="id" height="234"
                   ref="restore2" :data="roleTableData" @sort-change="roleSortChange"
                   :tooltip-config="{theme: 'light'}" :sort-config="{trigger: 'cell',remote : 'true'}">
            <vxe-table-column sortable field="relateId" v-bind="commonMinWidth('单元名称')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="ip" v-bind="commonMinWidth('服务地址')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column v-bind="commonMinWidth('服务器')" type="html" show-header-overflow width="25%">
                <template v-slot="{ row }">
                    <span v-html="statusShow(row,'hostState')"></span>
                    <span>IP：{{ jsonJudgeNotDefined(row,"row.host.ip") }}</span><br>
                    <span>集群：{{ jsonJudgeNotDefined(row,"row.host.cluster.name") }}</span>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="roleName" v-bind="commonMinWidth('角色')" show-header-overflow show-overflow width="18%">
                <template v-slot="{ row }">
                    <el-switch v-model="row.roleCode" active-value="master" inactive-value="slave"></el-switch>
                    <span class="enabledText">{{ row.roleCode==="master" ? "源节点":"复制节点" }}</span>
                </template>
            </vxe-table-column>
            <vxe-table-column field="state" v-bind="commonMinWidth('状态')" type="html" show-header-overflow show-overflow width="12%">
                <template v-slot="{ row }">
                    <span v-if="row.brShow">
                        <span v-html="statusShow(row,'state')"></span>
                    </span>
                    <br v-if="row.brShow">
                    <span v-html="statusShow(row,'pod')"></span>
                </template>
            </vxe-table-column>
        </vxe-table>
        <vxe-pager size="mini" @page-change="roleHandleSizeCurrentChange"
                   :page-sizes="roleTablePage.pageSizes" :current-page="roleTablePage.currentPage"
                   :page-size="roleTablePage.pageSize" :total="roleTablePage.totalResult"
                   :layouts="roleTablePage.pageLayouts">
        </vxe-pager>
    </div>

    <div style="position: absolute;right: 0px;bottom: 0px;width:720px">
        <div style="border-bottom: 2px solid #F3F3F3;"></div>
        <div style="float: right;padding: 10px 20px;position: relative;">
            <el-button size="small" @click="stepsActive--" v-show="stepsActive!==0">上一步</el-button>
            <el-button size="small" @click="stepsActiveAdd('restore'+stepsActive)" v-show="!saveBtnShow">下一步</el-button>
            <el-button size="small" type="primary" @click="formSubmit('restore'+stepsActive)" v-show="saveBtnShow">保存</el-button>
            <el-button size="small" @click="formClose">取消</el-button>
        </div>
    </div>
</div>
</body>

<script type="text/javascript" src="js/servgroup/cmhas/manageTab/unit/rebuildMysql.js?t=<%=new Date().getTime() %>"></script>

</html>