<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
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

        .delForm .el-dialog__body {
            height: 70px;
        }

        .delForm .el-dialog--center .el-dialog__body {
            padding: 10px 25px 10px;
        }

        .goTourlDialog .el-dialog__header,
        .goTourlDialog .el-dialog__body {
            padding: 0;
        }

        .vxe-cell .operate-btn:first-child {
            display: block;
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
                        <el-button size="small" @click="btnClick(btndata.code,true,0)"
                            :type=btnOperation(btndata.code,'type') :disabled=disabledValue[btndata.code]
                            :title="btndata.name+title" :icon=btnOperation(btndata.code,'icon')
                            v-if="btndata.type!=='navigation'">
                            {{ btndata.name }}
                        </el-button>
                        <el-dropdown v-else>
                            <el-button size="small">
                                {{ btndata.name }}<i class="el-icon-arrow-down el-icon--right"></i>
                            </el-button>
                            <el-dropdown-menu slot="dropdown">
                                <el-dropdown-item class="el-dropdown-menu-item" v-for="list in btndata.childrens">
                                    <vxe-button type="text" @click="btnClick(list.code,true,0)"
                                        :disabled=disabledValue[list.code]>
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

            <vxe-table size="small" :max-height="table_heighth" highlight-hover-row resizable auto-resize border="inner"
                style="color: #333333;" :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id" ref="xTable"
                @sort-change="sortChange" :data="tableData" @checkbox-change="selectChangeEvent"
                @cell-mouseenter="cellMouseenterEvent" @cell-mouseleave="cellMouseleaveEvent"
                @checkbox-all="selectAllEvent" :checkbox-config="{highlight: true, trigger: 'row'}"
                @cell-dblclick="cellDblClickManage" :sort-config="{trigger: 'cell',remote : 'true'}"
                :resizable-config="{minWidth: resizableMinWidth}" :row-class-name="tableRowClassName">
                <vxe-table-column type="checkbox" width="28" :resizable="false"></vxe-table-column>
                <vxe-table-column sortable field="name" v-bind="commonMinWidth('服务名称')" show-header-overflow
                    show-overflow width="7%">
                     <template v-slot="{ row }">
						<span v-html="row.name"></span>
						<span v-if="!row.meetExpectation">
							<el-tooltip placement="bottom">
								<div slot="content">
									<span class="tooltipText">该服务实际数据与预期不相符，具体请进入到单元界面，查看所有单元的版本与规模是否与预期一致。</span>
								</div>
								<i class="el-icon-warning" style="color:#f8453f"></i>
							</el-tooltip>
						</span>
	                </template>
                </vxe-table-column>
                <vxe-table-column sortable field="ownerName" v-bind="commonMinWidth('所属者')" show-header-overflow
                    show-overflow>
                </vxe-table-column>
                <vxe-table-column field="businessArea" v-bind="commonMinWidth('所属系统')" show-header-overflow>
                    <template v-slot="{ row }">
                        <div>
                            {{ jsonJudgeNotDefined(row,'row.businessSubsystem.businessSystem.name') }}<br>
                            {{ jsonJudgeNotDefined(row,'row.businessSubsystem.name') }}
                        </div>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="businessArea" v-bind="commonMinWidth('所属业务区')" show-header-overflow>
                    <template v-slot="{ row }">
                        <div>
                            {{ jsonJudgeNotDefined(row,'row.site.name') }}<br>
                            {{ jsonJudgeNotDefined(row,'row.businessArea.name') }}
                        </div>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="version" v-bind="commonMinWidth('版本架构')" show-header-overflow width="6%">
                    <template v-slot="{ row }">
                        <div>
                            {{ jsonJudgeNotDefined(row,'row.version') }}<br>
                            {{ jsonJudgeNotDefined(row,'row.sysArchitecture.display') }}
                        </div>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="scale.name" v-bind="commonMinWidth('规模架构')" show-header-overflow width="9%">
                    <template v-slot="{ row }">
                        <div>
                          	  规模：{{ jsonJudgeNotDefined(row,'row.scale.name') }}<br/>
                           	 架构：{{ jsonJudgeNotDefined(row,'row.arch.mode.display') }}
                            <span v-if="jsonJudgeNotDefined(row,'row.arch.mode.display')!=='单节点'">({{ row.unitCntText }})</span>
                            <!--  <span v-if="jsonJudgeNotDefined(row,'row.redisServCnt') > 1"> ,{{row.redisServCntText}}</span> -->
                            <template v-if="jsonJudgeNotDefined(row,'row.highAvailable')">
                            	<br>高可用：<i class="el-icon-success" style="color:#19BE6B;font-size:16px"></i>
                        	</template>
                        </div>
                    </template>
                </vxe-table-column>
                <vxe-table-column v-bind="commonMinWidth('磁盘信息')" show-header-overflow>
                    <template v-slot="{ row }">
                        <div>
                            <span v-if="getSession('storageMode') == 'volumepath'">类型：{{ jsonJudgeNotDefined(row,'row.diskType.display') }}<br/></span>
                            	表空间：{{ jsonJudgeNotDefined(row,'row.dataSize') }}<br/>
                            	日志空间：{{ jsonJudgeNotDefined(row,'row.logSize') }}
                        </div>
                    </template>
                </vxe-table-column>
                <vxe-table-column field="addresses" v-bind="commonMinWidth('服务地址')" show-header-overflow width="10%">
                    <template v-slot="{ row }">
                        <template v-for="list in row.addresses">
                            <span>{{ list }}</span><br>
                        </template>
                    </template>
                </vxe-table-column>
                <vxe-table-column sortable field="status.display" v-bind="commonMinWidth('状态')" type="html"
                    show-header-overflow show-overflow width="5%">
                    <template v-slot="{ row }">
                        <span v-if="row.brShow">
                            <a title="事件" @click="goToIncident(row)" class="taskStatusStyle"
                                v-if="row.hoverShow&&jsonJudgeNotDefined(row,'row.state.code') !== 'passing'">
                                <span class="el-icon-view"
                                    style="color: #f8453f;border: none;font-size: 16px;margin-right: 2px;vertical-align: text-bottom;"></span>
                            </a>
                            <span v-html="statusShow(row,'state')"></span>
                        </span>
                    </template>
                </vxe-table-column>
                <vxe-table-column sortable field="actionDisplay" v-bind="commonMinWidth('任务状态')" type="html"
                    show-header-overflow show-overflow width="7%">
                    <template v-slot="{ row }">
                        <el-tooltip v-if="jsonJudgeNotDefined(row, 'row.task.state.code') === 'running'"
                            placement="right">
                            <div slot="content">
                                <el-button type="primary" size="mini" @click="cancelBtn(row)">取消</el-button>
                            </div>
                            <a @click="actionDisplayClick(row)" class="taskStatusStyle"
                                v-html="statusShow(row,'task')"></a>
                        </el-tooltip>
                        <a v-else @click="actionDisplayClick(row)" class="taskStatusStyle"
                            v-html="statusShow(row,'task')"></a>
                    </template>
                </vxe-table-column>
                <vxe-table-column title="操作" :width="operationWidth" :visible="!XEUtils.isEmpty(rowBtnList)"
                    :resizable="false">
                    <template v-slot="{ row }">
                        <div class="operationBtn" v-for="value in rowBtnList" v-if="value.code!=btnReplaceList.manage">
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

            <el-dialog title="删除服务验证：" custom-class="delForm" :visible.sync="delFormShow" center width="368px"
                :close-on-click-modal="false" :show-close="false">
                <div style="padding-bottom:20px">是否删除服务 <b style="color:red">{{rowData.name}}</b></div>
                <el-form :model="delForm" :rules="delRules" ref="delForm" @submit.native.prevent>
                    <el-form-item prop="name">
                        <el-input v-model="delForm.name" placeholder="请输入该服务的服务名称进行校验！"
                            oninput="value=value.replace(/(^\s*)|(\s*$)/g, '')"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="btnClick(btnReplaceList.del,true,rowData)">确认删除</el-button>
                    <el-button @click="delFromInitialize()">取消</el-button>
                </div>
            </el-dialog>

            <el-dialog :visible.sync="goTourlDialogShow" custom-class="goTourlDialog" center width="368px"
                :close-on-click-modal="false" :show-close="false" :close-on-press-escape="false">
                <div style="margin: auto;padding-top: 30px;text-align: center;font-size: 24px;font-weight: bolder;">提交成功
                </div>
                <div
                    style="margin: auto;padding: 20px 0;color: red;text-align: center;font-size: 16px;font-weight: bolder;">
                    是否前往工单管理页面
                </div>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="goToServgroup(true)">前往</el-button>
                    <el-button @click="goToServgroup(false)">取消</el-button>
                </div>
            </el-dialog>

            <vxe-pager size="mini" @page-change="handleSizeCurrentChange" :page-sizes="tablePage.pageSizes"
                :current-page="tablePage.currentPage" :page-size="tablePage.pageSize" :total="tablePage.totalResult"
                :layouts="tablePage.pageLayouts">
            </vxe-pager>
        </div>
    </div>
</body>

<script type="text/javascript">
    <% Object btnPer = request.getAttribute("btnPer"); %>
    var btnLists = <%= btnPer %>
    var delBtnCode = ['btnManage']
    var ttBtnList = btnDataSeparation(btnLists, 'tabletop', delBtnCode)
    var rowBtnList = btnDataSeparation(btnLists, 'row', delBtnCode).sort(sortRowSeq)
    var operationWidth = operationWidthInit(rowBtnList, delBtnCode)
    var menu = getQueryVariable("menu")
    var searchKeyWord = getQueryVariable("searchKeyWord")
</script>

<script type="text/javascript" src="js/servgroup/cmhas/list.js?t=<%=new Date().getTime() %>"></script>

</html>