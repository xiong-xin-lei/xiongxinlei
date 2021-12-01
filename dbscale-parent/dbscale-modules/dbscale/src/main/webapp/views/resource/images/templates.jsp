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
</head>

<body>
    <div id="templates" v-cloak>
    
		<el-breadcrumb separator="/" style="padding: 20px 0;" >
		<el-breadcrumb-item>
			<img class="pageSvgColor" :src="['img/'+menu.icon]" width="12px" height="12px" onload="SVGInject(this)">
			<span style="color:#999;font-size: 12px;">{{ menu.name }}</span>
		</el-breadcrumb-item>
        <el-breadcrumb-item><span style="color:#999;font-size: 12px;">{{ menu.subMenu.name }}({{type}}:{{images}})</span></el-breadcrumb-item>
        <el-breadcrumb-item><span style="color:#999;font-size: 12px;">{{ menu.subMenu.sSubMenu.name }}</span></el-breadcrumb-item>
    	</el-breadcrumb>
    
        <div class="iframeBorder">
        	<div class="btnParentDiv">
            <div id="btnPanel">
                <el-tag size="medium" type="info" effect="plain" style="height: 34px;line-height: 34px;border: none;font-size: 16px;font-weight: bolder;color:#333333;">{{type}}:{{images}}</el-tag>

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
                       :tooltip-config="{theme: 'light'}" row-id="id" ref="xTable" :data="tableData"
                       @sort-change="sortChange" @edit-closed="editClosedEvent"
                       :sort-config="{trigger: 'cell',remote : 'true'}"
                       :edit-config="{trigger: 'dblclick', mode: 'row', activeMethod: editDisabledFlag, icon: editIcon()}"
                       :resizable-config="{minWidth: resizableMinWidth}" :row-class-name="tableRowClassName">
<%--                <vxe-table-column type="checkbox" width="28" :resizable="false"></vxe-table-column>--%>
                <vxe-table-column sortable field="key" width="21%" v-bind="commonMinWidth('键')" show-header-overflow show-overflow></vxe-table-column>
                <vxe-table-column sortable field="value" width="10%" v-bind="commonMinWidth('值')" :edit-render="{name: 'input'}" show-header-overflow show-overflow>
                </vxe-table-column>
                <vxe-table-column sortable field="defaultValue" width="10%" v-bind="commonMinWidth('默认值')" :edit-render="{name: 'input'}" show-header-overflow show-overflow>
                </vxe-table-column>
                <vxe-table-column sortable field="range" width="12%" v-bind="commonMinWidth('范围')"  :edit-render="{name: 'input'}" show-header-overflow show-overflow>
                </vxe-table-column>
                <vxe-table-column sortable field="canSet" width="8%" v-bind="commonMinWidth('允许编辑')" :edit-render="{name: '$switch'}" show-header-overflow show-overflow>
                    <template v-slot="{ row }">
                        <el-switch v-model="row.canSet" disabled></el-switch>
                        <span class="enabledText disabledText">{{ row.canSet ? "是":"否" }}</span>
                    </template>
                    <template v-slot:edit="{ row }">
                        <el-switch v-model="row.canSet"></el-switch>
                        <span class="enabledText" style="color: #2b2b2b">{{ row.canSet ? "是":"否" }}</span>
                    </template>
                </vxe-table-column>
                <vxe-table-column sortable field="mustRestart" width="8%" v-bind="commonMinWidth('重启生效')"  :edit-render="{name: '$switch'}" show-header-overflow show-overflow>
                    <template v-slot="{ row }">
                        <el-switch v-model="row.mustRestart" disabled></el-switch>
                        <span class="enabledText disabledText">{{ row.mustRestart ? "是":"否" }}</span>
                    </template>
                    <template v-slot:edit="{ row }">
                        <el-switch v-model="row.mustRestart"></el-switch>
                        <span class="enabledText" style="color: #2b2b2b">{{ row.mustRestart ? "是":"否" }}</span>
                    </template>
                </vxe-table-column>
                <vxe-table-column sortable field="description" v-bind="commonMinWidth('描述')" :edit-render="{name: 'textarea'}" show-header-overflow show-overflow>
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
    <%
    Object type = request.getAttribute("type");
    Object images = request.getAttribute("images");
    Object btnPer=request.getAttribute("btnPer");
    %>
    var btnLists = <%=btnPer%>;
    var ttBtnList = [];
    var rowBtnList = [];
    var otherBtnList = [];
    $.each(btnLists,function(index,btn){
        var btnPos=btn.pos;
        if (btnPos !== undefined) {
            if (btnPos.indexOf("tabletop") != -1) {
                ttBtnList.push(btn);
            }
            if (btnPos.indexOf("row") != -1) {
                rowBtnList.push(btn);
            }
        }else {
            otherBtnList.push(btn);
        }
    });
    rowBtnList=rowBtnList.sort(sortRowSeq);
    var type = <%= type %>
    var images = <%= images %>
    var menu = getQueryVariable("menu")
</script>

<script type="text/javascript" src="js/resource/images/templates.js?t=<%=new Date().getTime() %>"></script>

</html>