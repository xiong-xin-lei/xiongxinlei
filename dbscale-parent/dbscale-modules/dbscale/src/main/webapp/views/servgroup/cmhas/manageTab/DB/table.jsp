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
	</style>
</head>

<body>
<div id="list" v-cloak>

    <el-breadcrumb separator="/" style="padding: 20px 0;">
		<el-breadcrumb-item>
			<img class="pageSvgColor" :src="['img/'+menu.icon]" width="12px" height="12px" onload="SVGInject(this)">
			<span style="color:#999;font-size: 12px;">{{ menu.name }}</span>
		</el-breadcrumb-item>
		<el-breadcrumb-item><span style="color:#999;font-size: 12px;">{{ menu.subMenu.name }} ({{ menu.subMenu.servGroupName }})</span></el-breadcrumb-item>
		<el-breadcrumb-item><span style="color:#999;font-size: 12px;">{{ menu.subMenu.sSubMenu.name }} ({{ menu.subMenu.sSubMenu.schemaName }})</span></el-breadcrumb-item>
        <el-breadcrumb-item><span style="color:#999;font-size: 12px;">{{ menu.subMenu.sSubMenu.sSSubMenu.name }}</span></el-breadcrumb-item>
    </el-breadcrumb>

    <div class="iframeBorder">
    
        <div class="btnParentDiv">
            <div id="btnPanel">

                <vxe-toolbar>
	          		<template v-slot:buttons>
		            	<vxe-input v-model="filterName" clearable="true" placeholder="" @keyup.enter.native="searchClick"></vxe-input>
		            	<el-button size="small" @click="searchClick" icon="el-icon-search" :disabled="XEUtils.isEmpty(tableDataAll)" >搜索</el-button>
						<el-button size="small" @click="btnClick('refresh',true,0)" icon="el-icon-refresh">刷新</el-button>
		          	</template>
		        </vxe-toolbar>
		        
            </div>
        </div>

		<vxe-table size="small" :max-height="table_heighth" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
				   :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" ref="xTable" @sort-change="sortChange" :data="tableData"
				   :sort-config="{trigger: 'cell',remote : 'true'}" :resizable-config="{minWidth: resizableMinWidth}" :row-class-name="tableRowClassName">
			<vxe-table-column sortable field="name" v-bind="commonMinWidth('表名')" show-header-overflow show-overflow></vxe-table-column>
			<vxe-table-column sortable field="sizeData" v-bind="commonMinWidth('表大小')" show-header-overflow show-overflow></vxe-table-column>
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
	var rowId = getQueryVariable("rowId")
	var schemaName = getQueryVariable("schemaName")
	var menu = getQueryVariable("menu")
</script>

<script type="text/javascript" src="js/servgroup/cmhas/manageTab/DB/table.js?t=<%=new Date().getTime() %>"></script>

</html>