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
    </style>
</head>

<body>
    <div id="backupFileList" v-cloak>
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
                            <el-button size="small" @click="btnClick('refresh',true,0)" icon="el-icon-refresh">刷新
                            </el-button>
                        </template>
                    </vxe-toolbar>

                </div>
            </div>

            <vxe-table size="small" :max-height="table_heighth" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
                :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" ref="xTable" @sort-change="sortChange"
                :data="tableData" @checkbox-change="selectChangeEvent" @checkbox-all="selectAllEvent"
                :checkbox-config="{highlight: true, trigger: 'row'}" :sort-config="{trigger: 'cell',remote : 'true'}"
                :resizable-config="{minWidth: resizableMinWidth}" :row-class-name="tableRowClassName">
                <vxe-table-column type="checkbox" width="28" :resizable="false"></vxe-table-column>
                <vxe-table-column sortable field="unitName" v-bind="commonMinWidth('所属单元')" show-header-overflow show-overflow width="8%">
                </vxe-table-column>
                <vxe-table-column sortable field="type.display" v-bind="commonMinWidth('备份类型')" show-header-overflow show-overflow
                    width="6%"></vxe-table-column>
                <vxe-table-column sortable field="backupStorageType.display" v-bind="commonMinWidth('备份存储类型')" show-header-overflow
                    show-overflow width="8%"></vxe-table-column>
                <vxe-table-column sortable field="sizeData" v-bind="commonMinWidth('大小')" show-header-overflow show-overflow width="6%">
                </vxe-table-column>
                <vxe-table-column sortable field="createdAt" v-bind="commonMinWidth('创建时间')" show-header-overflow show-overflow
                    width="10%"></vxe-table-column>
                <vxe-table-column sortable field="consumingTime" v-bind="commonMinWidth('耗时')" show-header-overflow show-overflow
                    width="6%"></vxe-table-column>
                <vxe-table-column sortable field="expiredAt" v-bind="commonMinWidth('过期时间')" show-header-overflow show-overflow
                    width="10%"></vxe-table-column>
                <vxe-table-column sortable field="name" v-bind="commonMinWidth('文件名')" show-header-overflow show-overflow width="15%">
                </vxe-table-column>
                <vxe-table-column sortable v-bind="commonMinWidth('目录')" show-header-overflow show-overflow width="18%">
                	<template v-slot="{ row }">
						<span v-if="!jsonJudgeNotDefined(row,'row.external')"> {{ jsonJudgeNotDefined(row,'row.directory') }}</span>
						<el-tooltip v-else  placement="right">
							<div slot="content">
								<span><span class="tooltipText">外部导入</span>	
							</div>
	                    	<span style="color: red;">{{ jsonJudgeNotDefined(row,'row.directory') }}</span>
						</el-tooltip>
	                </template>
                </vxe-table-column><!-- 外部导入时变为红色；鼠标悬浮时显示“外部导入” -->
                <vxe-table-column sortable field="status.display" v-bind="commonMinWidth('状态')" show-header-overflow show-overflow
                    width="6%">
                    <template v-slot="{ row }">
                        <a v-if="row.statusCode === 'Failed'" class="taskStatusStyle" @click="statusDisplayClick(row)">
                            <span style="color: #f8453f;border-color: #f8453f;">{{ row.status.display }}</span>
                        </a>
                        <span v-else>{{ row.status.display }}</span>
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
	<% Object btnPer = request.getAttribute("btnPer");
    Object rowId = request.getAttribute("rowId"); %>
	var btnLists = <%= btnPer %> ;
	var ttBtnList = btnDataSeparation(btnLists,'tabletop');
	var rowBtnList = btnDataSeparation(btnLists,'row').sort(sortRowSeq);
    var operationWidth = operationWidthInit(rowBtnList)
    var rowId = <%= rowId %> ;
</script>

<script type="text/javascript"
    src="js/servgroup/mysqls/manageTab/backupFile/list.js?t=<%=new Date().getTime() %>"></script>

</html>