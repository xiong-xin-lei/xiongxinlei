<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%>
    <base href="<%=basePath%>">
    </base>
    <!-- 引入样式 -->
    <script src="js/commonCss.js?t=<%=new Date().getTime() %>"></script>
    <!-- 引入脚本 -->
    <script src="js/commonJs.js?t=<%=new Date().getTime() %>"></script>
    <style>
        .vxe-body--expanded-cell .vxe-table {
            margin: 0 !important;
        }

        .el-dialog__header,
        .el-dialog--center .el-dialog__body {
            padding: 0;
        }
    </style>
</head>

<body>
    <div id="cmhaList" v-cloak>

        <el-breadcrumb separator="/" style="margin: 20px 0px;">
            <el-breadcrumb-item>
                <img class="pageSvgColor" :src="['img/'+menu.icon]" width="12px" height="12px" onload="SVGInject(this)">
                <span style="color:#999;font-size: 12px;">{{ menu.name }}</span>
            </el-breadcrumb-item>
            <el-breadcrumb-item>
                <span style="color:#999;font-size: 12px;">{{ menu.subMenu.name }}</span>
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
                            <vxe-input v-model="filterName" clearable="true" placeholder=""
                                @keyup.enter.native="searchClick"></vxe-input>
                            <el-button size="small" @click="searchClick" icon="el-icon-search"
                                :disabled="XEUtils.isEmpty(tableDataAll)">搜索</el-button>
                            <el-button size="small" @click="returnList" icon="el-icon-refresh">刷新</el-button>
                        </template>
                    </vxe-toolbar>

                </div>
            </div>

            <vxe-table size="small" :max-height="table_heighth" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
                :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" ref="xTable" @sort-change="sortChange" :data="tableData" 
                @checkbox-change="selectChangeEvent" @checkbox-all="selectAllEvent" :checkbox-config="{highlight: true, trigger: 'row'}" 
                :sort-config="{trigger: 'cell',remote : 'true'}" :resizable-config="{minWidth: resizableMinWidth}" :row-class-name="tableRowClassName"
                :expand-config="{trigger: 'default',iconOpen:' ',iconClose:' ', lazy: true, loadMethod: loadContentMethod}">
                <vxe-table-column type="checkbox" width="28" :resizable="false"></vxe-table-column>
                <vxe-table-column type="expand" width="0" :resizable="false">
                    <template v-slot:content="{ row, rowIndex }">
                        <div v-if="row.createType.code === 'new'">
                            <el-row style="margin: 10px 40px">
                                <el-col :span="6">
                                    <span>端口：</span><span style="padding-left:5px">{{ row.childRows.port }}</span>
                                </el-col>
                                <el-col :span="6">
                                    <span>创建时间：</span><span
                                        style="padding-left:5px">{{ row.childRows.timestamp }}</span>
                                </el-col>
                                <el-col :span="6" v-if="row.childRows.proxyNum !== undefined">
                                    <span>代理数量：</span><span style="padding-left:5px">{{ row.childRows.proxyNum }}</span>
                                </el-col>
                                <el-col :span="6">
                                    <span>审批信息：</span><span style="padding-left:5px">{{ row.childRows.msg }}</span>
                                </el-col>
                            </el-row>
                            <el-row style="margin: 10px 40px" v-if="row.childRows.backupStorageType !== ''">
                                <el-col :span="6">
                                    <span>备份存储类型：</span><span
                                        style="padding-left:5px">{{ row.childRows.backupStorageType }}</span>
                                </el-col>
                                <el-col :span="6">
                                    <span>备份类型：</span><span
                                        style="padding-left:5px">{{ row.childRows.backUpType }}</span>
                                </el-col>
                                <el-col :span="6">
                                    <span>发起时间：</span><span
                                        style="padding-left:5px">{{ row.childRows.cronExpressionText }}</span>
                                </el-col>
                                <el-col :span="6">
                                    <span>保留时间：</span><span
                                        style="padding-left:5px">{{ row.childRows.fileRetentionNumText }}</span>
                                </el-col>
                            </el-row>
                            <el-row style="margin: 10px 40px" v-if="row.childRows.backupStorageType !== ''">
                                <el-col :span="12">
                                    <span>备份描述：</span><span
                                        style="padding-left:5px">{{ row.childRows.description }}</span>
                                </el-col>
                            </el-row>
                            <el-row v-if="!XEUtils.isEmpty(row.childRows.paramCfg)" style="margin: 10px 40px">
                                <el-col :span="12">
                                    <vxe-table size="small"
                                        style="color: #333333;margin: 10px 40px;border-left: 1px #E8EAEC solid"
                                        show-overflow show-header-overflow auto-resize border highlight-hover-row
                                        max-height="160px" row-id="id" :data="row.childRows.paramCfg">
                                        <vxe-table-column field="key" title="参数名" width="70%"></vxe-table-column>
                                        <vxe-table-column field="value" title="参数值" width="30%"></vxe-table-column>
                                    </vxe-table>
                                </el-col>
                            </el-row>
                        </div>
                        <div v-else>
                            <el-row style="margin: 10px 40px">
                                <el-col :span="6">
                                    <span>创建时间：</span><span
                                        style="padding-left:5px">{{ row.childRows.timestamp }}</span>
                                </el-col>
                                <el-col :span="18">
                                    <span>审批信息：</span><span style="padding-left:5px">{{ row.childRows.msg }}</span>
                                </el-col>
                            </el-row>
                        </div>
                    </template>
                </vxe-table-column>
                <vxe-table-column sortable field="name" v-bind="commonMinWidth('工单名')" show-header-overflow show-overflow width="6%">
                    <template v-slot="{ row }">
                        <span v-html="row.name" class="vxe-expand-text" @click="toggleExpandRow(row)"></span>
                    </template>
                </vxe-table-column>
                <vxe-table-column sortable field="ownerName" v-bind="commonMinWidth('所属者')" show-header-overflow show-overflow width="9%">
                </vxe-table-column>
                <vxe-table-column field="businessSubsystem" v-bind="commonMinWidth('业务系统')" show-header-overflow show-overflow width="7%">
                    <template v-slot="{ row }">
                        <span>
                            {{ jsonJudgeNotDefined(row,'row.businessSubsystem.businessSystem.name') }}<br>
                            {{ jsonJudgeNotDefined(row,'row.businessSubsystem.name') }}
                        </span>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="businessArea" v-bind="commonMinWidth('所属业务区')" show-header-overflow show-overflow width="8%">
                    <template v-slot="{ row }">
                        <span>
                            {{ jsonJudgeNotDefined(row,'row.site.name') }}<br>
                            {{ jsonJudgeNotDefined(row,'row.businessArea.name') }}
                        </span>
                    </template>
                </vxe-table-column>
                <vxe-table-column sortable field="cfgTitle" v-bind="commonMinWidth('服务类型')" show-header-overflow show-overflow width="100px"></vxe-table-column>
                <vxe-table-column v-bind="commonMinWidth('配置信息')" show-header-overflow>
                    <template v-slot="{ row }">
                        <div v-if="row.createType.code === 'new'">
                            <span style="display: inline-block;margin-right: 3%;vertical-align: middle;width: 26%;">
                                版本：{{ row.orderVersion }}<br>
                                硬件架构：{{jsonJudgeNotDefined(row, "row.sysArchitecture.display") }}
                            </span>
                            <span style="display: inline-block;margin-right: 3%;vertical-align: middle;width: 26%;">
                                规模：{{ jsonJudgeNotDefined(row, "row.orders.scale.name") }}<br>
                                架构：{{ jsonJudgeNotDefined(row, "row.orders.arch.mode.display") }}
                                <template v-if="row.orders.arch.mode.display!=='单节点'">
                                    <br>副本数量：{{ row.orders.arch.unitCnt-1 }}
                                </template>
                            </span>
                            <span style="display: inline-block;vertical-align: middle;"
                                v-if="row.orders.logSize !=null&& row.orders.dataSize !=null">
                               	 <span v-if="getSession('storageMode') == 'volumepath'">类型：{{ jsonJudgeNotDefined(row, 'row.orders.diskType.display') }}<br></span>
                                {{ row.cfgTitle === "数据库" ? "表空间":"数据目录" }}：{{ row.dataSize }}<br>
                                {{ row.cfgTitle === "数据库" ? "日志空间":"日志目录" }}：{{ row.logSize }}
                            </span>
                        </div>
                        <div v-else-if="row.createType.code === 'scale_up_cpumem'">
                            <span style="display: inline-block;margin-right: 30px;vertical-align: middle;">
                                规模：{{ jsonJudgeNotDefined(row, "row.orders.scale.name") }}
                            </span>
                        </div>
                        <div v-else-if="row.createType.code === 'scale_up_storage'">
                            <span style="display: inline-block;vertical-align: middle;"
                                v-if="row.orders.logSize !=null&& row.orders.dataSize !=null">
                                {{ row.cfgTitle === "数据库" ? "表空间":"数据目录" }}：{{ row.dataSize }}<br>
                                {{ row.cfgTitle === "数据库" ? "日志空间":"日志目录" }}：{{ row.logSize }}
                            </span>
                        </div>
                        <div v-else-if="row.createType.code === 'image_update'">
                            <span style="display: inline-block;vertical-align: middle;">
                                版本：{{ row.orderVersion }}
                            </span>
                        </div>
                        <div v-else-if="row.createType.code === 'delete'"></div>
                    </template>
                </vxe-table-column>
                <vxe-table-column sortable field="createType.display" v-bind="commonMinWidth('工单类型')" show-header-overflow show-overflow width="7%"></vxe-table-column>
                <vxe-table-column sortable field="status.display" v-bind="commonMinWidth('状态')" type="html" show-header-overflow show-overflow width="8%">
                    <template v-slot="{ row }">
                        <span v-if="row.taskId===null" v-html="statusShow(row.status)"></span>
                        <a v-else @click="actionDisplayClick(row)" class="taskStatusStyle" v-html="statusShow(row.status)"></a>
                    </template>
                </vxe-table-column>
                <vxe-table-column title="操作" :width="operationWidth" :visible="!XEUtils.isEmpty(rowBtnList)" :resizable="false">
                    <template v-slot="{ row }">
                        <div class="operationBtn" v-for="value in rowBtnList">
                            <span v-if="value.type!=='navigation'">
                                <vxe-button type="text" status="primary" :disabled="isAble(value.code,row)"
                                            @click="btnClick(value.code,false,row)" v-html="rowBtnValue(value)"></vxe-button>
                            </span>
                            <el-dropdown v-else>
                                <div style="line-height: 16px;">
                                    <span v-html="rowBtnValue(value)"></span>
                                    <i class="el-icon-arrow-down el-icon--right"></i>
                                </div>
                                <el-dropdown-menu>
                                    <el-dropdown-item class="el-dropdown-menu-item" v-for="list in value.childrens">
                                        <vxe-button type="text" @click="btnClick(list.code,false,row)"
                                                    :disabled="isAble(list.code,row)" v-html="rowBtnValue(list)"></vxe-button>
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </div>
                    </template>
                </vxe-table-column>
            </vxe-table>

            <el-dialog :visible.sync="goTourlDialogShow" center width="368px" :close-on-click-modal="false"
                :show-close="false" :close-on-press-escape="false">
                <div style="margin: auto;padding-top: 30px;text-align: center;font-size: 24px;font-weight: bolder;">提交成功
                </div>
                <div
                    style="margin: auto;padding: 20px 0;color: red;text-align: center;font-size: 16px;font-weight: bolder;">
                    	是否前往服务管理页面</div>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="goToServgroup(true)">前往</el-button>
                    <el-button @click="goToServgroup(false)">取消</el-button>
                </div>
            </el-dialog>

            <vxe-pager size="mini" @page-change="handleSizeCurrentChange" :page-sizes="tablePage.pageSizes"
                :current-page="tablePage.currentPage" :page-size="tablePage.pageSize" :total="tablePage.totalResult"
                :layouts="tablePage.pageLayouts">
            </vxe-pager>
        </div>
    </div>
</body>

<script type="text/javascript">
	<%Object btnPer = request.getAttribute("btnPer");
    Object code = request.getAttribute("code");%>
	var btnLists = <%=btnPer%>;
    var code = <%=code%>;
    var ttBtnList = btnDataSeparation(btnLists,'tabletop');
    var rowBtnList = btnDataSeparation(btnLists,'row').sort(sortRowSeq);
    var operationWidth = operationWidthInit(rowBtnList)
    var menu = getQueryVariable("menu")
</script>

<script type="text/javascript" src="js/order/cmhas/list.js?t=<%=new Date().getTime() %>"></script>

</html>