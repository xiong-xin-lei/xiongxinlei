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
    <link rel="stylesheet" href="css/order/OrderAdd.css?t=<%=new Date().getTime() %>">
    <style>
        .addIconClass {
            font-size: 26px;
            font-weight: bold;
            color: green;
            cursor: pointer;
        }
    </style>
</head>

<body>
<div id="cmha" v-cloak>
    <div class="orderTitle" v-if="ogAutoExamine">该申请单将自动审批<span v-if="ogAutoExecute">并自动执行</span></div>
    <el-steps :active="stepsActive" finish-status="success" align-center style="padding-top:10px">
        <el-step v-for="item in stepList" v-if="item.isShow" :title="item.name"></el-step>
    </el-steps>
    <div style="padding: 20px 70px 10px 60px;">
        <el-form ref="addForm" :model="formData" :rules="formRules" label-width="115px" size="small">
            <div v-if="stepsActive === 0" class="elSteps" style="height:300px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="类型" prop="type">
                            <el-input v-model="formData.type" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="21">
                        <el-form-item label="服务名称" prop="servName">
                            <el-input class="elInput" v-model="formData.servName" placeholder="请输入服务名称" maxlength="8" size="small"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="2" :offset="1">
                        <el-button size="small" style="padding: 8px 15px;" @click="randServName">随机</el-button>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="所属站点" prop="siteName">
                            <el-input v-model="formData.siteName" disabled></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="所属业务区" prop="area">
                            <el-select v-model="formData.area" placeholder="请选择所属业务区" style="display: block;">
                                <el-option v-for="areaData in areaList" :label="areaData.name" :value="areaData.id"></el-option>
                            </el-select>
                        </el-form-item>

                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="业务系统" prop="system">
                            <el-select v-model="formData.system" placeholder="请选择业务系统" size="small" @change="systemChange" style="display: block;">
                                <el-option v-for="item in systemList" :label="item.name" :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="业务子系统" prop="subSystem">
                            <el-select v-model="formData.subSystem" placeholder="请选择业务子系统 " size="small" style="display: block;">
                                <el-option v-for="item in subSystemList" :label="item.name" :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>


            <div v-else-if="stepsActive === 1" class="elSteps">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="端口" prop="port">
                            <el-input class="elInput" v-model="formData.port" placeholder="请输入端口" maxlength="8" size="small" oninput="value=value.replace(/[^\d]/g,'')"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="硬件架构" prop="architecture">
                            <el-select v-model="formData.architecture" placeholder="请选择硬件架构" size="small" ref="architectureRadios" @change="architectureChange" style="display: block;">
                                <el-option v-for="item in architectureList" :value="item.code" :label="item.name">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="版本" prop="imageVersion">
                            <el-select v-model="formData.imageVersion" placeholder="请选择版本" size="small" ref="imageVersionRadios" @change="imageVersionChange" style="display: block;">
                                <el-option v-for="item in imageVersionList" :value="item.id"
                                           :label="item.version.major + '.' + item.version.minor + '.' + item.version.patch + '.' + item.version.build">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="架构" prop="mode">
                            <el-select v-model="formData.mode" placeholder="请选择架构" size="small" @change="archChange" style="display: block;">
                                <el-option v-for="item in modeList" :label="item.display" :value="item.code">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="副本数量" prop="unitCnt" v-if="formData.mode ==='replication'">
                            <el-input-number v-model="formData.unitCnt" :step="1" :min="modeMin" :max="modeMax"></el-input-number>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="规模" prop="scale">
                            <el-select v-model="formData.scale" placeholder="请选择规模" size="small" ref="scaleRadios" @change="scaleChange" style="display: block;">
                                <el-option v-for="item in scaleList" :label="item.name" :value="item.name" :data-cpuCnt="item.cpuCnt" :data-memSize="item.memSize">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="磁盘类型" prop="diskType" v-if="getSession('storageMode') == 'volumepath'">
                            <el-select v-model="formData.diskType" placeholder="请选择磁盘类型" style="display: block;">
                                <el-option :label="diskTypeData.name" :value="diskTypeData.code" v-for="diskTypeData in diskTypeList">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="表空间(G)" prop="dataDirSize">
                            <el-slider v-model="formData.dataDirSize" show-input :min=10 :max=500 :step=1 :format-tooltip="formatToolTip"></el-slider>
                        </el-form-item>
                    </el-col>
                </el-row>
                <%--<el-row>
                    <el-col :span="24">
                        <el-form-item label="日志空间(G)" prop="logDirSize">
                            <el-slider v-model="formData.logDirSize" show-input :min=10 :max=500 :step=1 :format-tooltip="formatToolTip"></el-slider>
                        </el-form-item>
                    </el-col>
                </el-row>--%>
               <!--  <el-row style="height: 51px">
                    <el-col :span="10">
                        <el-form-item label="高可用组件">
                            <el-switch v-model="formData.highAvailable" :disabled="formData.mode==='single'"></el-switch>
                            <span class="enabledText">{{ formData.highAvailable ? "CMHA":"" }}</span>
                        </el-form-item>
                    </el-col> 
                    <el-col :span="8" v-if="formData.highAvailable">
                </el-row>-->
                <el-row>
                    <el-row>
	                    <el-col :span="8">
	                        <el-form-item label="代理数量" prop="proxyNum">
	                            <el-input-number v-model="formData.proxyNum" :step="1" :min="proxyMinNum" :max="proxyMaxNum" :disabled="!formData.proxyNum"></el-input-number>
	                        </el-form-item>
	                    </el-col>
                        <el-col :span="8">
                            <el-form-item label="高级选项">
                                <el-switch v-model="advanced" @change="advancedChange"></el-switch>
                            </el-form-item>
                        </el-col>
                        <el-col :span="4" v-if="advanced">
                            <el-checkbox size="small" v-model="paramDeploy" style="padding-top:5px" @change="paramDeployChange">参数配置</el-checkbox>
                        </el-col>
                        <el-col :span="4" v-if="advanced">
                            <el-checkbox size="small" v-model="backupPolicy" style="padding-top:5px" @change="backupPolicyChange">备份策略</el-checkbox>
                        </el-col>
                    </el-row>
                </el-row>
            </div>

            <div v-else-if="stepsActive === stepPageNums" class="elSteps">
                <el-form-item label="参数配置" prop="hotSpare">
                    <vxe-table border show-footer height="300px" :data="paramData" ref="paramTable" @cell-click="delParamRow" @header-cell-click="addParamRow"
                               :header-cell-class-name="addIconClassFun">
                        <vxe-table-column field="del" title="+" width="53px" class-name="delParamcell" align="center"></vxe-table-column>
                        <vxe-table-column title="配置参数" max-width="240px" align="center">
                            <template v-slot="{ row }">
                                <el-select v-model="row.key" placeholder="请选择配置参数" size="small" ref="paramRadios"
                                           @change="paramChange(row)">
                                    <el-option v-for="item in showKey(paramList)" :label="item.key" :value="item.key">
                                    </el-option>
                                </el-select>
                            </template>
                        </vxe-table-column>
                        <vxe-table-column title="当前值" max-width="240px" align="center">
                            <template v-slot="{ row }">
                                <el-input v-model="row.value" placeholder="请输入内容" size="small" style="width:215px"></el-input>
                            </template>
                        </vxe-table-column>
                    </vxe-table>
                </el-form-item>
            </div>

            <div v-else-if="stepsActive === stepPageNum" class="elSteps">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="备份存储类型" prop="backupStorageType">
                            <el-select v-model="formData.backupStorageType" placeholder="请选择备份存储类型" style="display: block;" clearable>
                                <el-option :label="list.name" :value="list.code" v-for="list in backupStorageTypeList"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="备份类型" prop="backUpType">
                            <el-select v-model="formData.backUpType" placeholder="请选择备份类型" style="display: block;" clearable>
                                <el-option :label="list.name" :value="list.code" v-for="list in backupTypeList"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item label="备份周期" prop="cronExpression">
                            <el-select v-model="formData.cronExpression" placeholder="请选择备份周期" style="display: block;" clearable>
                                <el-option :label="list.name" :value="list.code" v-for="list in cronExpressionList"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" v-if="showItem('month')">
                        <el-form-item label="日期" prop="month" label-width="70px">
                            <el-select v-model="formData.month" placeholder="请选择日期" style="display: block;" clearable>
                                <el-option :label="list.name" :value="list.code" v-for="list in timeList(1,31)"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8" v-if="showItem('week')">
                        <el-form-item label="周" prop="week" label-width="70px">
                            <el-select v-model="formData.week" placeholder="请选择周" style="display: block;" clearable>
                                <el-option :label="list.name" :value="list.code" v-for="list in timeList(1,7)"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="时间" prop="time" label-width="70px">
                            <el-time-picker v-model="formData.time" format="HH:mm" value-format="mm HH" placeholder="请选择时间" style="width: auto;"></el-time-picker>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="8">
                        <el-form-item :label="fileRetentionName()" prop="fileRetentionNum">
                            <el-input-number v-model="formData.fileRetentionNum" :min="1" :placeholder=["请输入"+fileRetentionName()]
                                             :min="1" controls-position="right" style="display: block;width:auto;">>
                            </el-input-number>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="备份描述" prop="description">
                    <el-input type="textarea" v-model="formData.description" maxlength="128"
                              placeholder="对此备份策略的描述"></el-input>
                </el-form-item>
            </div>
        </el-form>
    </div>

    <div style="position: absolute;right: 0px;bottom: 0px;width:778px">
        <div style="border-bottom: 2px solid #F3F3F3;"></div>
        <div style="float:right;padding: 10px 20px">
            <el-button size="small" @click="stepsActiveDel('addForm')" v-show="stepsActive!==0">上一步</el-button>
            <el-button size="small" @click="stepsActiveAdd('addForm')"
                       v-show="stepsActive !== XEUtils.max([1, stepPageNum, stepPageNums])">下一步
            </el-button>
            <el-button size="small" type="primary" @click="formSubmit('addForm')"
                       v-show="stepsActive ===  XEUtils.max([1, stepPageNum, stepPageNums])">立即申请
            </el-button>
            <el-button size="small" @click="formClose">取消</el-button>
        </div>
    </div>
</div>
</body>

<script type="text/javascript" src="js/order/cmhas/add.js?t=<%=new Date().getTime() %>"></script>

</html>