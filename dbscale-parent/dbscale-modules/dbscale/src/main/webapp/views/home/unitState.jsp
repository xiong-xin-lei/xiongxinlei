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
	<base href="<%=basePath%>"></base>
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
		.taskStatusStyle span {
			cursor: auto;
			border-bottom: 0px
		}
	</style>
</head>

<body>
<div id="unitList" v-cloak>

	<div class="iframeBorder">

		<vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
				   ref="xTable" :align="allAlign" row-id="id" :data="tableData" :resizable-config="{minWidth: resizableMinWidth}"
				   :tooltip-config="{theme: 'light'}" @sort-change="sortChange" @cell-dblclick="cellDblClickManage"
				   :sort-config="{trigger: 'cell',remote : 'true'}">
			<vxe-table-column sortable field="relateId" v-bind="commonMinWidth('单元名称')" show-header-overflow show-overflow width="12%"></vxe-table-column>
			<vxe-table-column sortable field="ownerName" v-bind="commonMinWidth('所属者')" show-header-overflow show-overflow></vxe-table-column>
			<vxe-table-column field="businessArea" v-bind="commonMinWidth('所属系统')" show-header-overflow>
				<template v-slot="{ row }">
					<div>
						{{ jsonJudgeNotDefined(row,"row.servGroup.businessSubsystem.businessSystem.name") }}<br>
						{{ jsonJudgeNotDefined(row,"row.servGroup.businessSubsystem.name") }}
					</div>
				</template>
			</vxe-table-column>
			<vxe-table-column sortable field="type.display" v-bind="commonMinWidth('单元类型')" show-header-overflow show-overflow width="7%"></vxe-table-column>
			<vxe-table-column sortable field="ip" v-bind="commonMinWidth('服务地址')" show-header-overflow show-overflow></vxe-table-column>
			<vxe-table-column sortable field="versionValue" v-bind="commonMinWidth('版本')" show-header-overflow show-overflow></vxe-table-column>
			<vxe-table-column v-bind="commonMinWidth('规模')" type="html" show-header-overflow width="18%">
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
			<vxe-table-column field="state" v-bind="commonMinWidth('状态')" type="html" show-header-overflow show-overflow>
				<template v-slot="{ row }">
					<span v-html="statusShow(row,'state')"></span><br>
					<span v-html="statusShow(row,'pod')"></span>
				</template>
			</vxe-table-column>
			<vxe-table-column sortable field="actionDisplay" v-bind="commonMinWidth('任务状态')" show-header-overflow show-overflow>
				<template v-slot="{ row }">
					<a  class="taskStatusStyle" v-html="statusShow(row,'task')"></a>
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

<script type="text/javascript" src="js/home/unitState.js?t=<%=new Date().getTime() %>"></script>

</html>