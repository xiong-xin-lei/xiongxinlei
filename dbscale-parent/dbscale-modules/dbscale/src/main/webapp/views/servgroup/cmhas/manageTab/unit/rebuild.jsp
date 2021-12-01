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
</head>

<body>
    <div id="rebuild" v-cloak>
        <el-row v-if="formData.strategy !== 'auto'">
            <el-col :span="19">
                <div style="padding:20px 10px;height: 30px;">
                    <el-form ref="rebuildForm" :inline="true" :model="formData" label-width="70px" size="small">
                        <el-form-item label="重建策略">
                            <el-select v-model="formData.strategy" placeholder="请选择重建策略" style="display: block;"
                                @change="hostChange">
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
            <el-col :span="4">
                <div style="padding:20px;float:right">
                    <vxe-toolbar>
                        <template v-slot:buttons>
                            <el-button size="small" @click="hostChange" :disabled="formData.strategy != 'assign'"
                                icon="el-icon-refresh">刷新
                            </el-button>
                        </template>
                    </vxe-toolbar>
                </div>
            </el-col>
        </el-row>
        <el-row v-else>
            <div style="padding:40px 30px;">
                <el-form ref="rebuildForm" :inline="false" :model="formData" label-width="70px" size="small">
                    <el-form-item label="重建策略">
                        <el-select v-model="formData.strategy" placeholder="请选择重建策略" style="display: block;"
                            @change="hostChange">
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
        </el-row>
        <div v-if="formData.strategy !== 'auto'">
            <vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
                :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" ref="restore" @sort-change="sortChange"
                :data="tableData" @radio-change="selectChangeEvent" height="239"
                :radio-config="{trigger: 'row', checkRowKey: defaultSelecteRow}"
                :sort-config="{trigger: 'cell',remote : 'true'}">
                <vxe-table-column type="radio" width="28"></vxe-table-column>
                <vxe-table-column sortable field="ip" title="主机IP" show-header-overflow show-overflow>
                </vxe-table-column>
                <vxe-table-column field="resourceRates" title="资源分配率" show-header-overflow width="48%">
                    <template v-slot="{ row }">
                        <div>
                            <el-row>
                                <el-col :span="listdata.width" v-for="listdata in row.resourceRates" :style="{ color: listdata.textColor }">
                                    {{ listdata.name }}：<span>{{ listdata.value }}</span>
                                </el-col>
                            </el-row>
                        </div>
                    </template>
                </vxe-table-column>
                <vxe-table-column sortable field="state" title="运行状态" type="html" show-header-overflow show-overflow>
                    <template v-slot="{ row }">
                        <span v-html="statusShow(row,'state')"></span><br />
                        <span v-html="statusShow(row,'role')"></span>
                    </template>
                </vxe-table-column>
                <vxe-table-column sortable field="enabled" title="是否可用" type="html" show-header-overflow show-overflow>
                    <template v-slot="{ row }">
                        <div v-html="statusShow(row,'enabled')"></div>
                    </template>
                </vxe-table-column>
            </vxe-table>
            <vxe-pager size="mini" @page-change="handleSizeCurrentChange" :page-sizes="tablePage.pageSizes"
                :current-page="tablePage.currentPage" :page-size="tablePage.pageSize" :total="tablePage.totalResult"
                :layouts="tablePage.pageLayouts">
            </vxe-pager>
        </div>
        <div style="position: absolute;right: 0px;bottom: 0px;width:100%">
            <div style="border-bottom: 2px solid #F3F3F3;"></div>
            <div style="float: right;padding: 10px 20px">
                <el-button size="small" type="primary" @click="formSubmit('rebuildForm','restore')">保存</el-button>
                <el-button size="small" @click="formClose">取消</el-button>
            </div>
        </div>
    </div>
</body>

<script type="text/javascript" src="js/servgroup/cmhas/manageTab/unit/rebuild.js?t=<%=new Date().getTime() %>"></script>

</html>