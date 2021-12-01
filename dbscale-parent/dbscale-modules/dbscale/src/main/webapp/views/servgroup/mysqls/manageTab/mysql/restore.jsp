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
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
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

        .btnParentDiv {
            padding: 10px;
        }

        .vxe-toolbar {
            margin: 0;
        }
    </style>
</head>

<body>
<div id="mysqlRestore" v-cloak>
    <div style="background-color: #ffffff;">
        <div class="btnParentDiv">
            <div id="btnPanel">

                <el-select v-model="restoreRange" placeholder="请选择范围" size="small" @change="restoreRangeChange">
                    <el-option v-for="list in restoreRangeList" :label="list.name" :value="list.code"></el-option>
                </el-select>

                <vxe-toolbar>
                    <template v-slot:buttons>
                        <vxe-input v-model="filterName" clearable="true" placeholder="" @keyup.enter.native="searchClick"></vxe-input>
                        <el-button size="small" @click="searchClick" icon="el-icon-search" :disabled="XEUtils.isEmpty(tableDataAll)" >搜索</el-button>
                        <el-button size="small" @click="btnClick('refresh',true,0)" icon="el-icon-refresh">刷新
                        </el-button>
                    </template>
                </vxe-toolbar>

            </div>
        </div>

        <vxe-table size="small" highlight-hover-row resizable auto-resize border="inner" style="color: #333333;"
                   :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id"
                   ref="mysqlRestore" @sort-change="sortChange" :data="tableData"
                   @radio-change="selectChangeEvent" height="210"
                   :radio-config="{highlight: true, trigger: 'row'}"
                   :sort-config="{trigger: 'cell',remote : 'true'}">
            <vxe-table-column sortable field="unitName" title="所属单元" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="type.display" title="备份类型" show-header-overflow show-overflow width="10%"></vxe-table-column>
            <vxe-table-column sortable field="backupStorageType.display" title="备份存储类型" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="sizeData" title="大小" show-header-overflow show-overflow width="7%"></vxe-table-column>
            <vxe-table-column sortable field="createdAt" title="创建时间" show-header-overflow show-overflow></vxe-table-column>
            <vxe-table-column sortable field="name" title="文件名" show-header-overflow show-overflow width="24%"></vxe-table-column>
            <vxe-table-column sortable field="status.display" title="状态" show-header-overflow show-overflow width="8%"></vxe-table-column>
         	<vxe-table-column title="操作" width="6%">
	            <template v-slot="{ row }">
	            	 <vxe-button  type="text" status="primary" @click="btnClick('submit',true,row)">还原</vxe-button>
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

<script type="text/javascript" src="js/servgroup/mysqls/manageTab/mysql/restore.js?t=<%=new Date().getTime() %>"></script>

</html>