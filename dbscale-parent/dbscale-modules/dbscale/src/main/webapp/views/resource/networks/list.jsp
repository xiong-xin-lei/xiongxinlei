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
 .vxe-table--body tr td{
	 cursor: default;
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
            @checkbox-change="selectChangeEvent" @checkbox-all="selectAllEvent" :checkbox-config="{highlight: true, trigger: 'row'}"
            :sort-config="{trigger: 'cell',remote : 'true'}" :row-class-name="tableRowClassName"
            :expand-config="{iconOpen:' ',iconClose:' ', lazy: true, loadMethod: loadContentMethod}">
            <vxe-table-column type="checkbox" width="28" :resizable="false"></vxe-table-column>
            <vxe-table-column  type="expand" width="0" :resizable="false">
	            <template v-slot:content="{ row }">
	            	<el-row style="margin: 10px 40px">
	            		<el-col :span="3">
	            			<span>VLAN ID：</span><span style="padding-left:5px">{{ row.childRows.vlan }}</span>
	            		</el-col>
	            		<el-col :span="5">
	            			<span>网络拓扑：</span><span style="padding-left:5px">{{ row.childRows.topologys }}</span>
	            		</el-col>
	            		<el-col :span="6">
	            			<span>创建时间：</span><span style="padding-left:5px">{{ row.childRows.created.timestamp }}</span>
	            		</el-col>
	            		<el-col :span="10">
	            			<span>描述：</span><span style="padding-left:5px">{{ row.childRows.description }}</span>
	            		</el-col>
	            	</el-row>
	            </template>
            </vxe-table-column>
            <vxe-table-column sortable field="name" v-bind="commonMinWidth('网段名称')" show-header-overflow show-overflow >
            	<template v-slot="{ row }">
					<span v-html="row.name"  class="vxe-expand-text" @click="toggleExpandRow(row)"></span>
				</template>
            </vxe-table-column>
            <vxe-table-column sortable field="businessArea.name" v-bind="commonMinWidth('所属业务区')" width="15%" show-header-overflow show-overflow>
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
            <vxe-table-column sortable field="startIp" v-bind="commonMinWidth('起始IP')" width="12%" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="endIp" v-bind="commonMinWidth('结束IP')" width="12%" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="gateway" v-bind="commonMinWidth('网关')" width="12%" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="netmask" v-bind="commonMinWidth('掩码')" width="8%" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="IpUsed" v-bind="commonMinWidth('IP使用率')" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="status" v-bind="commonMinWidth('是否可用')" type="html" show-header-overflow show-overflow width="90px">
				<template v-slot="{ row }">
					<div v-html="statusShow(row.status)"></div>
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
    var ttBtnList = btnDataSeparation(btnLists,'tabletop');
    var rowBtnList = btnDataSeparation(btnLists,'row').sort(sortRowSeq);
    var operationWidth = operationWidthInit(rowBtnList)
	var menu = getQueryVariable("menu")
    var searchKeyWord = getQueryVariable("searchKeyWord")
</script>

<script type="text/javascript" src="js/resource/networks/list.js?t=<%=new Date().getTime() %>"></script>

</html>