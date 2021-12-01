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
    <style>
        .el-radio__input{
            display: inline-flex;
        }
        .el-radio-group{
            vertical-align: baseline;
        }
        .el-radio__label{
            font-size: 12px;
            vertical-align: middle;
        }
    </style>
</head>

<body>
<div id="rebuild" v-cloak>
    <el-row v-if="formData.backupFileSource === 'backup'">
        <div style="padding:40px 50px 20px 30px">
            <el-form ref="rebuildInitForm" :inline="false" :model="formData" label-width="110px" size="small" :rules="formRules">
                <el-form-item label="备份文件来源" prop="backupFileSource">
                    <el-radio-group v-model="formData.backupFileSource">
                        <el-radio :label="data.code" v-for="data in backupFileSourceList">{{ data.name }}</el-radio>
                    </el-radio-group>
                    <%--<el-select v-model="formData.backupFileSource" placeholder="请选择自动数据初始化" @change="hostChange">
                        <el-option :label="data.name" :value="data.code" v-for="data in backupFileSourceList"></el-option>
                    </el-select>--%>
                </el-form-item>
                <el-form-item label="备份存储类型" prop="backupStorageType">
                    <el-select v-model="formData.backupStorageType" placeholder="请选择备份存储类型">
                        <el-option :label="list.name" :value="list.code" v-for="list in backupStorageTypeList"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
        </div>
    </el-row>
    <div v-else>
        <el-row>
            <el-col :span="19">
                <div style="padding:20px 10px;height: 30px;">
                    <el-form ref="rebuildInitForm" :inline="true" :model="formData" label-width="110px" size="small" :rules="formRules">
                        <el-form-item label="备份文件来源" prop="backupFileSource">
                            <el-radio-group v-model="formData.backupFileSource">
                                <el-radio :label="data.code" v-for="data in backupFileSourceList">{{ data.name }}</el-radio>
                            </el-radio-group>
                            <%--<el-select v-model="formData.backupFileSource" placeholder="请选择自动数据初始化" @change="hostChange">
                                <el-option :label="data.name" :value="data.code" v-for="data in backupFileSourceList"></el-option>
                            </el-select>--%>
                        </el-form-item>
                    </el-form>
                </div>
            </el-col>
            <el-col :span="4">
                <div style="padding:20px;float:right">
                    <vxe-toolbar>
                        <template v-slot:buttons>
                            <el-button size="small" @click="backupFileSourceChange" icon="el-icon-refresh">刷新</el-button>
                        </template>
                    </vxe-toolbar>
                </div>
            </el-col>
        </el-row>
        <vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
                   :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" ref="restore" @sort-change="sortChange"
                   :data="tableData" @radio-change="selectChangeEvent" height="239"
                   :radio-config="{trigger: 'row', checkRowKey: defaultSelecteRow}"
                   :sort-config="{trigger: 'cell',remote : 'true'}">
            <vxe-table-column type="radio" width="28"></vxe-table-column>
            <vxe-table-column sortable field="unitName" title="所属单元" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="sizeData" title="大小" show-header-overflow show-overflow width="11%"></vxe-table-column>
            <vxe-table-column sortable field="createdAt" title="创建时间" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="name" title="文件名" show-header-overflow show-overflow width="38%"></vxe-table-column>
        </vxe-table>
        <vxe-pager size="mini" @page-change="handleSizeCurrentChange" :page-sizes="tablePage.pageSizes"
                   :current-page="tablePage.currentPage" :page-size="tablePage.pageSize" :total="tablePage.totalResult"
                   :layouts="tablePage.pageLayouts">
        </vxe-pager>
    </div>
    <div>
        <div style="border-bottom: 2px solid #F3F3F3;"></div>
        <div style="float: right;padding: 10px 20px">
            <el-button size="small" type="primary" @click="formSubmit('rebuildInitForm','restore')">保存</el-button>
            <el-button size="small" @click="formClose">取消</el-button>
        </div>
    </div>
</div>
</body>

<script type="text/javascript" src="js/servgroup/cmhas/manageTab/unit/rebuildInit.js?t=<%=new Date().getTime() %>"></script>

</html>