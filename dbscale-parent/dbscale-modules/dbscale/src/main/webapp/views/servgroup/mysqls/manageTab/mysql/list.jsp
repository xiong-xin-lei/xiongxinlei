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
        .vxe-body--expanded-cell .vxe-table {
            margin: 0 !important;
        }

        .col--last span:last-child {
            border-right: none !important;
        }

        .mysqlContent > div > div {
            display: inline-block;
            white-space: normal;
            word-break: break-all;
        }

        .mysqlContent > div > div:nth-child(1) {
            width: 6%;
            float: left;
        }

        .mysqlContent > div > div:nth-child(2) {
            width: 94%;
        }

        .mysqlContentSlave > div > div:nth-child(1) {
            width: 12%;
            float: left;
        }

        .mysqlContentSlave > div > div:nth-child(2) {
            width: 88%;
        }
    </style>
</head>

<body>
<div id="mysqlList" v-cloak>

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
                                   :disabled="XEUtils.isEmpty(tableDataAll)">搜索
                        </el-button>
                        <el-button size="small" @click="returnList" icon="el-icon-refresh">刷新</el-button>
                    </template>
                </vxe-toolbar>

            </div>
        </div>

        <vxe-table size="small" :max-height="table_heighth" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
                   :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" ref="xTable" :data="tableData"
                   @cell-mouseenter="cellMouseenterEvent" @cell-mouseleave="cellMouseleaveEvent"
                   @sort-change="sortChange" @checkbox-change="selectChangeEvent" @checkbox-all="selectAllEvent"
                   :checkbox-config="{highlight: true, trigger: 'row'}" :sort-config="{trigger: 'cell',remote : 'true'}"
                   :expand-config="{trigger: 'default',iconOpen:' ',iconClose:' '}" :resizable-config="{minWidth: resizableMinWidth}"
                   :row-class-name="tableRowClassName">
            <vxe-table-column type="checkbox" width="28" :resizable="false"></vxe-table-column>
            <vxe-table-column sortable field="relateId" v-bind="commonMinWidth('单元名称')" show-header-overflow show-overflow>
                <template v-slot="{ row }">
                    <span v-if="jsonJudgeNotDefined(row, 'row.replication.role.code')!=='slave'">{{ row.relateId }}</span>
                    <span v-else-if="!!jsonJudgeNotDefined(row, 'row.replication.role.code')" class="vxe-expand-text" @click="toggleExpandRow(row,'relateId')">{{ row.relateId }}</span>
                	<span v-else >{{ row.relateId }}</span>
                </template>
            </vxe-table-column>
            <vxe-table-column type="expand" width="0" :resizable="false">
                <template v-slot:content="{ row }">
                    <div class="mysqlContent mysqlContentSlave" style="margin: 10px 40px;">
                        <div>
                            <div>master_ip：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.masterIp") }}</div>
                        </div>
                        <div>
                            <div>master_port：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.masterPort") }}</div>
                        </div>
                        <div>
                            <div>slave_io_running：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.slaveIoRunning.code") }}</div>
                        </div>
                        <div>
                            <div>slave_sql_running：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.slaveSqlRunning.code") }}</div>
                        </div>
                        <div>
                            <div>slave_io_state：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.slaveIoState") }}</div>
                        </div>
                        <div>
                            <div>slave_sql_running_state：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.slaveSqlState") }}</div>
                        </div>
                        <div>
                            <div>master_log_file：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.masterLogFile") }}</div>
                        </div>
                        <div>
                            <div>relay_master_log_file：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.relayMasterLogFile") }}</div>
                        </div>
                        <div>
                            <div>read_master_log_pos：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.readMasterLogPos") }}</div>
                        </div>
                        <div>
                            <div>exec_master_log_pos：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.execMasterLogPos") }}</div>
                        </div>
                        <div>
                            <div>relay_log_file：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.relayLogFile") }}</div>
                        </div>
                        <div>
                            <div>relay_log_pos：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.relayLogPos") }}</div>
                        </div>
                        <div>
                            <div>last_io_error：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.lastIoError") }}</div>
                        </div>
                        <div>
                            <div>last_sql_error：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.lastSqlError") }}</div>
                        </div>
                        <div>
                            <div>seconds_behind_master：</div>
                            <div>{{ jsonJudgeNotDefined(row, "row.replication.secondsBehindMaster") }}</div>
                        </div>
                    </div>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="ip" v-bind="commonMinWidth('服务地址')" show-header-overflow show-overflow>
            </vxe-table-column>
            <vxe-table-column sortable field="versionValue" v-bind="commonMinWidth('版本')" show-header-overflow show-overflow
                              width="8%"></vxe-table-column>
            <vxe-table-column v-bind="commonMinWidth('规模')" type="html" show-header-overflow width="18%">
                <template v-slot="{ row }">
                        <span style="display: inline-block;margin-right: 30px;vertical-align: middle;">
                            CPU：{{ row.cpuCnt }}<br>
                            内存：{{ row.memSize }}
                        </span>
                    <span style="display: inline-block;margin-right: 30px;vertical-align: middle;">
                            表空间：{{ row.dataSize }}<br>
                            日志空间：{{ row.logSize }}
                        </span>
                </template>
            </vxe-table-column>
            <vxe-table-column v-bind="commonMinWidth('服务器')" type="html" show-header-overflow width="12%">
                <template v-slot="{ row }">
                    <span v-html="statusShow(row,'hostState')"></span>
                    <span>IP：<span class="taskStatusStyle" @click="pageJumpFun('host', jsonJudgeNotDefined(row,'row.host.ip'))"><span>{{ jsonJudgeNotDefined(row,"row.host.ip") }}</span></span></span><br>
                    <span>集群：<span class="taskStatusStyle" @click="pageJumpFun('cluster', jsonJudgeNotDefined(row,'row.host.cluster.name'))"><span>{{ jsonJudgeNotDefined(row,"row.host.cluster.name") }}</span></span></span>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="roleName" v-bind="commonMinWidth('复制状态')" show-header-overflow show-overflow width="9%">
                <template v-slot="{ row }">
                    <template v-if="row.roleName">
                        <el-tooltip placement="right"
                                    v-if="jsonJudgeNotDefined(row,'row.replication.role.code') === 'slave'">
                            <div slot="content">
                                <span>复制Io状态：</span><span :style="{'color':row.roleIoRunningCode == 'No' ? 'red':'#FFF'}">{{ row.roleIoRunningDisplay }}</span><br/>
                                <span>复制Sql状态：</span><span :style="{'color':row.roleSqlRunningCode == 'No' ? 'red':'#FFF'}">{{ row.roleSqlRunningDisplay }}</span>
                            </div>
                            <span v-html="statusShow(row,'topo')"></span>
                        </el-tooltip>
                        <span v-else v-html="statusShow(row,'topo')"></span>
                    </template>
                    <br/>
                    <span>{{ row.archMode }}</span>
                </template>
            </vxe-table-column>
            <vxe-table-column field="state" v-bind="commonMinWidth('状态')" type="html" show-header-overflow show-overflow width="8%">
                <template v-slot="{ row }">
                    <span v-if="row.brShow">
                        <a title="事件" @click="goToIncident(row)" class="taskStatusStyle" v-if="row.hoverShow&&jsonJudgeNotDefined(row,'row.state.code') !== 'passing'">
                            <span class="el-icon-view" style="color: #f8453f;border: none;font-size: 16px;margin-right: 2px;vertical-align: text-bottom;"></span>
                        </a>
                        <span v-html="statusShow(row,'state')"></span>
                    </span>
                    <br v-if="row.brShow">
                    <span v-html="statusShow(row,'pod')"></span>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="actionDisplay" v-bind="commonMinWidth('任务状态')" show-header-overflow show-overflow width="8%">
               <template v-slot="{ row }">
                   	<el-tooltip v-if="jsonJudgeNotDefined(row, 'row.task.state.code') === 'running'" placement="right">
						<div slot="content">
							<el-button type="primary" size="mini" @click="cancelBtn(row)">取消</el-button>
						</div>
						<a @click="actionDisplayClick(row)" class="taskStatusStyle" v-html="statusShow(row,'task')"></a>
					</el-tooltip>
					<a v-else @click="actionDisplayClick(row)" class="taskStatusStyle" v-html="statusShow(row,'task')"></a>
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

        <vxe-pager size="mini" @page-change="handleSizeCurrentChange" :page-sizes="tablePage.pageSizes"
                   :current-page="tablePage.currentPage" :page-size="tablePage.pageSize" :total="tablePage.totalResult"
                   :layouts="tablePage.pageLayouts">
        </vxe-pager>
    </div>
</div>
</body>

<script type="text/javascript">
    <%Object btnPer = request.getAttribute("btnPer");
    Object rowId = request.getAttribute("rowId");%>
    var btnLists = <%= btnPer %>;
    var ttBtnList = btnDataSeparation(btnLists, 'tabletop');
    var rowBtnList = btnDataSeparation(btnLists, 'row').sort(sortRowSeq);
    var operationWidth = operationWidthInit(rowBtnList)
    var rowId = <%= rowId %>;
</script>

<script type="text/javascript" src="js/servgroup/mysqls/manageTab/mysql/list.js?t=<%=new Date().getTime() %>"></script>

</html>