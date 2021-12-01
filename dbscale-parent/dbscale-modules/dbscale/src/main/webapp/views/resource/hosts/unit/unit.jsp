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
		.vxe-body--expanded-cell .vxe-table{
			margin: 0 !important;
		}
		.col--last span:last-child{
			border-right:none !important;
		}
	</style>
</head>

<body>
<div id="unitList" v-cloak>

	<el-breadcrumb separator="/" style="padding: 20px 0;">
		<el-breadcrumb-item>
			<img class="pageSvgColor" :src="['img/'+menu.icon]" width="12px" height="12px" onload="SVGInject(this)">
			<span style="color:#999;font-size: 12px;">{{ menu.name }}</span>
		</el-breadcrumb-item>
		<el-breadcrumb-item>
			<span style="color:#999;font-size: 12px;">{{ menu.subMenu.name }} ({{ menu.subMenu.sSubMenu.ip }})</span>
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
				   @sort-change="sortChange" @checkbox-change="selectChangeEvent" @checkbox-all="selectAllEvent"
				   @cell-dblclick="cellDblClickManage">
			<vxe-table-column type="checkbox" width="28" :resizable="false"></vxe-table-column>
			<vxe-table-column sortable field="relateId" title="单元名称" show-header-overflow show-overflow width="12%"></vxe-table-column>
			<vxe-table-column sortable field="ownerName" title="所属者" show-header-overflow show-overflow></vxe-table-column>
			<vxe-table-column field="businessArea" title="所属系统" show-header-overflow>
				<template v-slot="{ row }">
					<div>
						{{ jsonJudgeNotDefined(row,"row.servGroup.businessSubsystem.businessSystem.name") }}<br>
						{{ jsonJudgeNotDefined(row,"row.servGroup.businessSubsystem.name") }}
					</div>
				</template>
			</vxe-table-column>
			<vxe-table-column sortable field="type.display" title="单元类型" show-header-overflow show-overflow width="7%"></vxe-table-column>
			<vxe-table-column sortable field="ip" title="服务地址" show-header-overflow show-overflow width="11%"></vxe-table-column>
			<vxe-table-column sortable field="versionValue" title="版本" show-header-overflow show-overflow></vxe-table-column>
			<vxe-table-column title="规模" type="html" show-header-overflow width="18%">
				<template v-slot="{ row }">
					<span style="display: inline-block;margin-right: 30px;vertical-align: middle;">
						CPU：{{ row.cpuCnt }}<br>
						内存：{{ row.memSize }}
					</span>
					<span style="display: inline-block;margin-right: 30px;vertical-align: middle;">
						{{ jsonJudgeNotDefined(row,'row.type.code')=='mysql'?'表空间':'数据目录' }}：{{ row.dataSize }}<br>
						{{ jsonJudgeNotDefined(row,'row.type.code')=='mysql'?'日志空间':'日志目录' }}：{{ row.logSize }}
                    </span>
				</template>
			</vxe-table-column>
			<vxe-table-column field="state" title="状态" type="html" show-header-overflow show-overflow>
				<template v-slot="{ row }">
					<span v-html="statusShow(row,'state')"></span><br>
					<span v-html="statusShow(row,'pod')"></span>
				</template>
			</vxe-table-column>
			<vxe-table-column sortable field="actionDisplay" title="任务状态" show-header-overflow show-overflow>
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
	<% Object btnPer = request.getAttribute("btnPer");%>
	var btnLists = <%= btnPer %>;
	var ttBtnList = btnDataSeparation(btnLists, 'tabletop');
	var rowBtnList = btnDataSeparation(btnLists, 'row').sort(sortRowSeq);
	var operationWidth = operationWidthInit(rowBtnList)
	var menu = getQueryVariable("menu")
</script>

<script type="text/javascript" src="js/resource/hosts/unit/unit.js?t=<%=new Date().getTime() %>"></script>

</html>