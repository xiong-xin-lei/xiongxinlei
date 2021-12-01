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

        .redisContent > div > div {
            display: inline-block;
            white-space: normal;
            word-break: break-all;
        }

        .redisContent > div > div:nth-child(1) {
            width: 6%;
            float: left;
        }

        .redisContent > div > div:nth-child(2) {
            width: 94%;
        }

        .redisContentSlave > div > div:nth-child(1) {
            width: 12%;
            float: left;
        }

        .redisContentSlave > div > div:nth-child(2) {
            width: 88%;
        }
    </style>
</head>

<body>
<div id="redisList" v-cloak>

    <div class="iframeBorder">

        <el-collapse v-model="unitValue" style="padding: 0 20px;" @change="unitHandleChange">
            <template v-for="list in unitList">
                <el-collapse-item :title="list.name" :name="list.code">
                	<template slot="title">
                		{{list.name}}<span v-if="loadbalanceIps != 'none' && !XEUtils.isEmpty(loadbalanceIps)"><span v-if="list.code === 'proxysql' ">（ 负载均衡地址：{{loadbalanceIps}} ）</span></span>
				    </template>
                    <div class="iframeBorder">
                        <div class="btnParentDiv">
                            <template v-for="btndata in btnList">
                                <el-button size="small" @click="btnClick(btndata.code,true,0,list.code)" :type=btnOperation(btndata.code,'type')
                                           :disabled=disabledValue[list.code][btndata.code] :title="btndata.name+title" :icon=btnOperation(btndata.code,'icon')
                                           v-if="btndata.type!=='navigation'">
                                    {{ btndata.name }}
                                </el-button>
                                <el-dropdown v-else>
                                    <el-button size="small">
                                        {{ btndata.name }}<i class="el-icon-arrow-down el-icon--right"></i>
                                    </el-button>
                                    <el-dropdown-menu slot="dropdown">
                                        <el-dropdown-item class="el-dropdown-menu-item" v-for="lists in btndata.childrens">
                                            <vxe-button type="text" @click="btnClick(lists.code,true,0,list.code)" :disabled=disabledValue[lists.code]>
                                                {{ lists.name }}
                                            </vxe-button>
                                        </el-dropdown-item>
                                    </el-dropdown-menu>
                                </el-dropdown>
                            </template>

                            <vxe-toolbar>
                                <template v-slot:buttons>
                                    <vxe-input v-model="filterName[list.code]" clearable="true" placeholder="" @keyup.enter.native="searchClick(list.code)"></vxe-input>
                                    <el-button size="small" @click="searchClick(list.code)" icon="el-icon-search"
                                               :disabled="XEUtils.isEmpty(tableDataAll[list.code])">搜索
                                    </el-button>
                                    <el-button size="small" @click="returnList(list.code)"
                                               icon="el-icon-refresh">刷新
                                    </el-button>
                                </template>
                            </vxe-toolbar>

                        </div>
                        <vxe-table size="small" highlight-hover-row auto-resize resizable border="inner" :max-height="table_heighth" style="color: #333333;"
                                   :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" :ref="list.code+'Table'" :data="tableData[list.code]"
                                   :checkbox-config="{highlight: true, trigger: 'row'}" :sort-config="{trigger: 'cell',remote : 'true'}"
                                   :expand-config="{trigger: 'default',iconOpen:' ',iconClose:' '}" :row-class-name="tableRowClassName"
                                   :resizable-config="{minWidth: resizableMinWidth}"
                                   @cell-mouseenter="cellMouseenterEvent" @cell-mouseleave="cellMouseleaveEvent"
                                   @sort-change="sortChange" @checkbox-change="selectChangeEvent" @checkbox-all="selectAllEvent(list.code)">
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
                                    <div style="margin: 10px 40px;" v-if="list.code==='redis'">
                                        <div class="redisContent redisContentSlave"
                                             v-if="jsonJudgeNotDefined(row, 'row.replication.role.code')==='slave'">
                                            <div v-if="getSession('storageMode') == 'volumepath'">
                                                <div>磁盘类型：</div>
                                                <div>{{ row.diskTypeDisplay }}</div>
                                            </div>
                                            <div>
                                                <div>role：</div>
                                                <div>{{ jsonJudgeNotDefined(row, "row.replication.role.code") }}</div>
                                            </div>
                                            <div>
                                                <div>self_ip：</div>
                                                <div>{{ jsonJudgeNotDefined(row, "row.replication.selfIp") }}</div>
                                            </div>
                                            <div>
                                                <div>self_port：</div>
                                                <div>{{ jsonJudgeNotDefined(row, "row.replication.selfPort") }}</div>
                                            </div>
                                            <div>
                                                <div>master_ip：</div>
                                                <div>{{ jsonJudgeNotDefined(row, "row.replication.masterIp") }}</div>
                                            </div>
                                            <div>
                                                <div>master_port：</div>
                                                <div>{{ jsonJudgeNotDefined(row, "row.replication.masterPort") }}</div>
                                            </div>
                                        </div>
                                        <div class="redisContent" v-else>
                                            <div v-if="getSession('storageMode') == 'volumepath'">
                                                <div>磁盘类型：</div>
                                                <div>{{ row.diskTypeDisplay }}</div>
                                            </div>
                                            <div>
                                                <div>role：</div>
                                                <div>{{ jsonJudgeNotDefined(row, "row.replication.role.code") }}</div>
                                            </div>
                                        </div>
                                    </div>
                                </template>
                            </vxe-table-column>
                            <vxe-table-column sortable field="ip" v-bind="commonMinWidth('服务地址')" show-header-overflow show-overflow></vxe-table-column>
                            <vxe-table-column sortable field="versionValue" v-bind="commonMinWidth('版本')" show-header-overflow show-overflow width="8%"></vxe-table-column>
                            <vxe-table-column v-bind="commonMinWidth('规模')" type="html" show-header-overflow width="18%">
                                <template v-slot="{ row }">
                                    <span style="display: inline-block;margin-right: 15px;vertical-align: middle;">
                                            CPU：{{ row.cpuCnt }}<br>
                                            内存：{{ row.memSize }}
                                    </span>
                                    <span style="display: inline-block;vertical-align: middle;">
                                        {{ list.code === "redis" ? "表空间":"数据目录" }}：{{ row.dataSize }}<br>
                                        {{ list.code === "redis" ? "日志空间":"日志目录" }}：{{ row.logSize }}
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
                            <vxe-table-column v-if="true" :visible="list.code !=='redis-sentinel'?true:false" sortable field="roleName" v-bind="commonMinWidth('复制状态')" show-header-overflow show-overflow width="9%">
				                <template v-slot="{ row }">
				                    <span v-html="statusShow(row,'topo')"></span>
				                    <br/><span>{{ row.archMode }}</span>
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
                            <vxe-table-column title="操作" :width="operationWidthFun(list.code)" :visible="!XEUtils.isEmpty(rowBtnList)" :resizable="false">
                                <template v-slot="{ row }">
                                    <div class="operationBtn" v-for="value in rowBtnShowFun(row,list.code)">
			                            <span v-if="value.type!=='navigation'">
			                                <vxe-button type="text" status="primary" :disabled="isAble(value.code,row)"
                                                        @click="btnClick(value.code,false,row,list.code)" v-html="rowBtnValue(value)"></vxe-button>
			                            </span>
                                        <el-dropdown v-else>
                                            <div style="line-height: 16px;">
                                                <span v-html="rowBtnValue(value)"></span>
                                                <i class="el-icon-arrow-down el-icon--right"></i>
                                            </div>
                                            <el-dropdown-menu>
                                                <el-dropdown-item class="el-dropdown-menu-item" v-for="lists in value.childrens">
                                                    <vxe-button type="text" @click="btnClick(lists.code,false,row,list.code)"
                                                                :disabled="isAble(lists.code,row)" v-html="rowBtnValue(lists)"></vxe-button>
                                                </el-dropdown-item>
                                            </el-dropdown-menu>
                                        </el-dropdown>
                                    </div>
                                </template>
                            </vxe-table-column>
                        </vxe-table>

                        <vxe-pager size="mini"
                                   @page-change="function({currentPage, pageSize}){tablePage[list.code].pageSize = pageSize; tablePage[list.code].currentPage = currentPage; handlePageChange(list.code)}"
                                   :page-sizes="tablePage[list.code].pageSizes" :current-page="tablePage[list.code].currentPage"
                                   :page-size="tablePage[list.code].pageSize" :total="tablePage[list.code].totalResult"
                                   :layouts="tablePage[list.code].pageLayouts">
                        </vxe-pager>
                    </div>
                </el-collapse-item>
            </template>
        </el-collapse>
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
    var rowId = <%= rowId %>;
</script>

<script type="text/javascript" src="js/servgroup/redis/manageTab/unit/list.js?t=<%=new Date().getTime() %>"></script>

</html>