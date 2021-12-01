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
    <link rel="stylesheet" href="css/order/OrderAdd.css?t=<%=new Date().getTime() %>">
    <style>
        .addIconClass {
            font-size: 26px;
            font-weight: bold;
            color: green;
            cursor: pointer;
        }
        .vxe-toolbar.size--small {
		    height: 32px;
		}
		.btnParentDiv .vxe-input {
		    width: 180px;
		}
    </style>
</head>

<body>
    <div id="add" v-cloak>
        <el-steps :active="stepsActive" finish-status="success" align-center style="padding-top:10px">
            <el-step title="业务信息"></el-step>
            <el-step title="主机信息"></el-step>
            <el-step title="注册信息"></el-step>
        </el-steps>
        <div style="padding: 30px 10px 10px 10px;">
            <el-form ref="addForm" :model="formData" :rules="formRules" label-width="115px" size="small">
                <div v-if="stepsActive === 0" class="elSteps" style="padding:0px 40px 0px 80px">
                    <el-row>
                       <el-col :span="24">
                            <el-form-item label="所属站点" prop="siteName">
                                <el-select v-model="formData.siteName" placeholder="所属站点为空，请检查！" disabled>
                                    <el-option :label="item.name" :value="item.id" v-for="item in siteList">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="所属业务区" prop="businessAreaName">
                                <el-select v-model="formData.businessAreaName" placeholder="请选择所属业务区" @change="clusterChange">
                                    <el-option :label="item.name" :value="item.id" v-for="item in businessAreaList">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="所属集群" prop="clusterName">
                                <el-select v-model="formData.clusterName" placeholder="请选择所属集群" >
                                    <el-option :label="item.name" :value="item.id" v-for="item in clusterList">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                        </el-col>
                    </el-row>
                </div>


                <div v-else-if="stepsActive === 1" class="elSteps">
	                <div class="btnParentDiv">
	            		<div id="btnPanel">
		                	<el-select v-model="architectureValue" placeholder="请选择架构" size="small" @change="archChange">
			                    <el-option v-for="item in architectureList" :label="item.name" :value="item.code"></el-option>
			                </el-select>
			                <vxe-toolbar>
			                    <template v-slot:buttons>
			                    	<vxe-input v-model="filterName" clearable="true" placeholder="" @keyup.enter.native="searchClick"></vxe-input>
                        			<el-button size="small" @click="searchClick" icon="el-icon-search" :disabled="XEUtils.isEmpty(tableDataAll)" style="height:32px">搜索</el-button>
			                        <el-button size="small" @click="returnList" icon="el-icon-refresh" style="height:32px">刷新</el-button>
			                    </template>
			                </vxe-toolbar>
	                	</div>
	                </div>
	                
                    <vxe-table class="iframeBorder" size="small" max-height="310px" highlight-hover-row resizable auto-resize border="inner"
                        style="color: #333333;" :tooltip-config="{theme: 'light'}" :align="allAlign" row-id="id"
                        ref="xTable" @sort-change="sortChange" :data="tableData" @checkbox-change="selectChangeEvent"
                        @checkbox-all="selectAllEvent" :checkbox-config="{highlight: true, trigger: 'row'}"
                   		:sort-config="{trigger: 'cell',remote : 'true'}">
                        <vxe-table-column type="checkbox" width="28" :resizable="false"></vxe-table-column>
                        <vxe-table-column field="ip" v-bind="commonMinWidth('主机名称')" show-header-overflow show-overflow
                            width="10%">
                            <template v-slot="{ row }">
                                <div>
                                    {{ jsonJudgeNotDefined(row,'row.name') }}<br>
                                    {{ jsonJudgeNotDefined(row,'row.ip') }}
                                </div>
                            </template>
                        </vxe-table-column>
                        <vxe-table-column field="resourceRates" v-bind="commonMinWidth('资源分配率')" show-header-overflow
                            width="16%">
                            <template v-slot="{ row }">
                                   <el-row v-for="listdata in row.resourceRates"
                                       :style="{ color: listdata.textColor }">
                                       <span>{{ listdata.name }}：</span>
                                       <span>{{ listdata.value }}</span>
                                   </el-row>
                            </template>
                        </vxe-table-column>
                        <vxe-table-column field="name" v-bind="commonMinWidth('主机信息')" show-header-overflow>
                            <template v-slot="{ row }">
                                   <el-row>
                                       <el-col :span="listdata.width" v-for="listdata in row.hostsInfo"
                                           :style="{ color: listdata.textColor }">
                                           <span>{{ listdata.name }}：</span>
                                           <span>{{ listdata.data }}</span>
                                       </el-col>
                                   </el-row>
                            </template>
                        </vxe-table-column>
                        <vxe-table-column sortable field="status" v-bind="commonMinWidth('状态')" type="html"
                            show-header-overflow show-overflow width="8%">
                            <template v-slot="{ row }">
                                <div v-if="row.enabled" v-html="statusShow(row)"></div>
                                <el-tooltip v-else  placement="left">
									<div slot="content">
										<span>{{ jsonJudgeNotDefined(row,'row.message') }}</span>
									</div>
				                    	<div v-html="statusShow(row)"></div>
								</el-tooltip>
                            </template>
                        </vxe-table-column>
                    </vxe-table>

                    <vxe-pager size="mini" @page-change="handleSizeCurrentChange" :page-sizes="tablePage.pageSizes"
                        :current-page="tablePage.currentPage" :page-size="tablePage.pageSize"
                        :total="tablePage.totalResult" :layouts="tablePage.pageLayouts">
                    </vxe-pager>
                </div>

                <div v-else-if="stepsActive === 2" class="elSteps" style="padding: 30px 40px 30px 20px;">
                	<el-row>
                		<el-col :span="12">
                            <el-form-item label="角色" prop="role">
                                <el-select v-model="formData.role" placeholder="请选择角色" style="display: block;">
                                    <el-option :label="data.name" :value="data.code" v-for="data in roleList">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="资源分配率上限" prop="maxUsage">
                                <el-input-number v-model="formData.maxUsage" size="medium" :min="10" :max="100"
                                    placeholder="请输入资源分配率上限"></el-input-number>
                            </el-form-item>
                        </el-col>
                	</el-row>
                    <el-row v-if="getSession('storageMode') != 'pvc'">
                        <el-col :span="12">
                            <el-form-item label="机械盘设备" prop="selectStorage">
                                <el-input v-model="formData.hddPaths" maxlength="64" placeholder='多个以","分割'></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="固态盘设备" prop="selectStorage">
                                <el-input v-model="formData.ssdPaths" maxlength="64" placeholder='多个以","分割'></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row v-if="getSession('storageMode') != 'pvc'">
                        <el-col :span="12">
                            <el-form-item label="外置存储" prop="selectStorage">
                                <el-select v-model="formData.remoteStorageId" placeholder="请选择外置存储"
                                    style="display: block;">
                                    <el-option :label="item.name" :value="item.id" v-for="item in remoteStorageList">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>
            </el-form>
        </div>

        <div style="position: absolute;right: 0px;bottom: 0px;width:100%">
            <div style="border-bottom: 2px solid #F3F3F3;"></div>
            <div style="float:right;padding: 10px 20px">
                <el-button size="small" @click="stepsActiveDel('addForm')" v-show="stepsActive!==0">上一步</el-button>
                <el-button size="small" @click="stepsActiveAdd('addForm')" v-show="stepsActive!==2">下一步</el-button>
                <el-button size="small" type="primary" @click="formSubmit('addForm')" v-show="stepsActive===2">立即注册</el-button>
                <el-button size="small" @click="formClose">取消</el-button>
            </div>
        </div>
    </div>
</body>

<script type="text/javascript" src="js/resource/hosts/add.js?t=<%=new Date().getTime() %>"></script>

</html>