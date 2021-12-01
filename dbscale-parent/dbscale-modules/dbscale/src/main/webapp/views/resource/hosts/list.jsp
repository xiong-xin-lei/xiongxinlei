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
        .vxe-table--body tbody .hotSpareName {
            background-color: #EDEDED;
        }

        .redText {
            color: red;
        }
    </style>
</head>

<body>
<div id="list" v-cloak>

    <el-breadcrumb separator="/" style="padding: 20px 0;">
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
                        <vxe-input v-model="filterName" clearable="true" placeholder="" @keyup.enter.native="searchClick"></vxe-input>
                        <el-button size="small" @click="searchClick" icon="el-icon-search" :disabled="XEUtils.isEmpty(tableDataAll)">搜索</el-button>
                        <el-button size="small" @click="returnList" icon="el-icon-refresh">刷新</el-button>
                    </template>
                </vxe-toolbar>

            </div>
        </div>

        <vxe-table size="small" :max-height="table_heighth" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
                   :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" ref="xTable" @sort-change="sortChange" :data="tableData"
                   @cell-mouseenter="cellMouseenterEvent" @cell-mouseleave="cellMouseleaveEvent"
                   @checkbox-change="selectChangeEvent" @checkbox-all="selectAllEvent" :checkbox-config="{highlight: true, trigger: 'row'}"
                   :sort-config="{trigger: 'cell',remote : 'true'}" :resizable-config="{minWidth: resizableMinWidth}" :row-class-name="tableRowClassName"
                   :expand-config="{iconOpen:' ',iconClose:' ', lazy: true, loadMethod: loadContentMethod}">
            <vxe-table-column type="checkbox" width="28" :resizable="false"></vxe-table-column>
            <vxe-table-column type="expand" width="0" :resizable="false">
                <template v-slot:content="{ row }">
                    <table style="margin: 10px 2%;width: 96%" v-if="row.relateId == null">
                        <tr>
                            <td style="width: 8%">资源分配率上限：</td>
                            <td style="width: 17%">{{ row.childRows.maxUsage }}</td>
                            <td style="width: 5%">机房：</td>
                            <td style="width: 20%">{{ row.childRows.room }}</td>
                            <td style="width: 4%">机位：</td>
                            <td style="width: 20%">{{ row.childRows.seat }}</td>
                            <td style="width: 5%" v-if="getSession('storageMode') == 'volumepath'">磁盘类型：</td>
                            <td style="width: 20%" v-if="getSession('storageMode') == 'volumepath'">{{ row.childRows.diskType }}</td>
                        </tr>
                        <tr>
                            <td>角色：</td>
                            <td>{{ row.childRows.role }}</td>
                            <td>注册时间：</td>
                            <td>{{ row.childRows.createdTimestamp }}</td>
                        </tr>
                        <tr>
                            <td>描述：</td>
                            <td colspan="5">{{ row.childRows.description }}</td>
                        </tr>
                    </table>

                    <table style="margin: 10px 2%;width: 96%" v-else>
                        <tr>
                            <td style="width: 6%">单元上限：</td>
                            <td style="width: 19%">{{ row.childRows.resourceMax[0].value }}</td>
                            <td style="width: 8%">资源分配率上限：</td>
                            <td style="width: 17%">{{ row.childRows.maxUsage }}</td>
                            <td style="width: 5%">Pod个数：</td>
                            <td style="width: 20%">{{ row.childRows.podCnt }}</td>
                            <td style="width: 6%">Pod上限：</td>
                            <td style="width: 19%">{{ row.childRows.maxPodCnt }}</td>
                        </tr>
                        <tr>
                            <td>机房：</td>
                            <td>{{ row.childRows.room }}</td>
                            <td>机位：</td>
                            <td>{{ row.childRows.seat }}</td>
                            <td>注册时间：</td>
                            <td>{{ row.childRows.createdTimestamp }}</td>
                            <td>入库时间：</td>
                            <td>{{ row.childRows.inputTimestamp }}</td>
                        </tr>
                        <tr>
                            <td>操作系统：</td>
                            <td>{{ row.childRows.operatingSystem }}</td>
                            <td>OS版本：</td>
                            <td>{{ row.childRows.osImage }}</td>
                            <td>容器版本：</td>
                            <td>{{ row.childRows.containerRuntimeVersion }}</td>
                            <td>kernel版本：</td>
                            <td>{{ row.childRows.kernelVersion }}</td>
                        </tr>
                        <tr>
                            <td>kubelet版本：</td>
                            <td>{{ row.childRows.kubeletVersion }}</td>
                            <td>描述：</td>
                            <td colspan="5">{{ row.childRows.description }}</td>
                        </tr>
                    </table>
                </template>
            </vxe-table-column>
            <vxe-table-column v-bind="commonMinWidth('主机IP')" show-header-overflow show-overflow width="8%">
                <template v-slot="{ row }">
                	<span class="vxe-expand-text" @click="toggleExpandRow(row)">{{ row.name }}</span>
                	<br v-if="!XEUtils.isEmpty(row.name)">
                    <span class="vxe-expand-text" @click="toggleExpandRow(row)">{{ row.ip }}</span>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="businessArea.name" v-bind="commonMinWidth('所属业务区')" show-header-overflow show-overflow width="10%">
                <template v-slot="{ row }">
                	<a v-if="jsonJudgeNotDefined(row,'row.businessArea.enabled')" class="taskStatusStyle" @click="aAssociatedJumpClick(row.businessArea.name, '所属业务区')" >
						<span style="color:#19be6b;border-bottom: 1px solid #19be6b;">
						    {{ jsonJudgeNotDefined(row,'row.businessArea.name') }}
						</span>
					</a>
					<el-tooltip v-else  placement="right">
						<div slot="content">
							<span><span class="tooltipText">是否可用：</span>{{ jsonJudgeNotDefined(row,'row.businessArea.enabled') ? "是" : "否"}}</span>
						</div>
						<a class="taskStatusStyle" @click="aAssociatedJumpClick(row.businessArea.name, '所属业务区')" >
	                    	<span style="color: red; border-bottom: 1px solid red">
							    {{ jsonJudgeNotDefined(row,'row.businessArea.name') }}
							</span>
						</a>
					</el-tooltip>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="cluster.name" v-bind="commonMinWidth('所属集群')" show-header-overflow show-overflow>
                 <template v-slot="{ row }">
                	<a v-if="jsonJudgeNotDefined(row,'row.cluster.enabled')" class="taskStatusStyle" @click="aAssociatedJumpClick(row.cluster.name, '所属集群')" >
						<span style="color:#19be6b;border-bottom: 1px solid #19be6b;">
						    {{ jsonJudgeNotDefined(row,'row.cluster.name') }}
						</span>
					</a>
					<el-tooltip v-else  placement="right">
						<div slot="content">
							<span><span class="tooltipText">是否可用：</span>{{ jsonJudgeNotDefined(row,'row.cluster.enabled') ? "是" : "否"}}</span>
						</div>
						<a class="taskStatusStyle" @click="aAssociatedJumpClick(row.cluster.name, '所属集群')" >
	                    	<span style="color: red; border-bottom: 1px solid red">
							    {{ jsonJudgeNotDefined(row,'row.cluster.name') }}
							</span>
						</a>
					</el-tooltip>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="architecture.display" v-bind="commonMinWidth('架构')" show-header-overflow show-overflow width="7%"></vxe-table-column>
            <vxe-table-column field="resourceRates" v-bind="commonMinWidth('资源分配率')" show-header-overflow width="23%">
                <template v-slot="{ row }">
                    <div v-if="row.inSuccess">
                        <el-row>
                            <el-col :span="listdata.width" v-for="listdata in row.resourceRates" :style="{ color: listdata.textColor }">
                                <span>{{ listdata.name }}：</span>
                                <span>{{ listdata.value }}</span>
                            </el-col>
                        </el-row>
                    </div>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="remoteStorage.name" v-bind="commonMinWidth('外置存储')" v-if="getSession('storageMode') != 'pvc'" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column field="state" v-bind="commonMinWidth('运行状态')" type="html" show-header-overflow show-overflow width="7%">
                <template v-slot="{ row }">
                	<template v-if="row.brShow ">
	                    <el-tooltip placement="right" v-if="getSession('storageMode') == 'volumepath'">
	                        <div slot="content">
	                            <span>agent状态：{{ jsonJudgeNotDefined(row,'row.agentState.display') }}</span><br/>
	                            <span>node状态：{{ jsonJudgeNotDefined(row,'row.nodeState.display') }}</span><br/>
	                        </div>
	                        <template >
		                    	<span>
			                        <a title="事件" @click="goToIncident(row)" class="taskStatusStyle" v-if="row.hoverShow && jsonJudgeNotDefined(row,'row.state.code') !== 'passing'">
			                            <span class="el-icon-view" style="color: #f8453f;border: none;font-size: 16px;margin-right: 2px;vertical-align: text-bottom;"></span>
			                        </a>
			                        <span v-html="statusShow(row,'state')"></span><br/>
			                    </span>
		                    </template>	
	                    </el-tooltip>
						<template v-else >
	                    	<span>
		                        <a title="事件" @click="goToIncident(row)" class="taskStatusStyle" v-if="row.hoverShow&&jsonJudgeNotDefined(row,'row.state.code') !== 'passing'">
		                            <span class="el-icon-view" style="color: #f8453f;border: none;font-size: 16px;margin-right: 2px;vertical-align: text-bottom;"></span>
		                        </a>
		                        <span v-html="statusShow(row,'state')"></span><br/>
		                    </span>
	                    </template>
                    </template>
                    <span v-html="statusShow(row,'role')"></span>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="enabled" v-bind="commonMinWidth('是否可用')" type="html" show-header-overflow show-overflow width="7%">
                <template v-slot="{ row }">
                    <div v-html="statusShow(row,'enabled')"></div>
                </template>
            </vxe-table-column>
            <vxe-table-column sortable field="actionDisplay" v-bind="commonMinWidth('任务状态')" show-header-overflow show-overflow width="7%">
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

        <vxe-pager size="mini" @page-change="handleSizeCurrentChange"
                   :page-sizes="tablePage.pageSizes" :current-page="tablePage.currentPage"
                   :page-size="tablePage.pageSize" :total="tablePage.totalResult"
                   :layouts="tablePage.pageLayouts">
        </vxe-pager>
    </div>
</div>
</body>

<script type="text/javascript">
    <%Object btnPer=request.getAttribute("btnPer");%>
    var btnLists = <%=btnPer%>;
    var delBtnCode = ['btnUnit']
    var ttBtnList = btnDataSeparation(btnLists, 'tabletop', delBtnCode);
    var rowBtnList = btnDataSeparation(btnLists, 'row').sort(sortRowSeq);
    var operationWidth = operationWidthInit(rowBtnList)
    var menu = getQueryVariable("menu")
    var searchKeyWord = getQueryVariable("searchKeyWord")
    var searchKeyType = getQueryVariable("searchKeyType")
</script>

<script type="text/javascript" src="js/resource/hosts/list.js?t=<%=new Date().getTime() %>"></script>

</html>