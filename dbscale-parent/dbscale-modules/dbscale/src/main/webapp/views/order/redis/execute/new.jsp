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
    <link rel="stylesheet" href="css/order/OrderCommon.css?t=<%=new Date().getTime() %>">
</head>

<body>
    <div id="executeNew" v-cloak>
        <el-steps :active="stepsActive" finish-status="success" align-center style="padding-top:10px">
            <el-step v-for="item in stepList" v-if="item.isShow" :title="item.name"></el-step>
        </el-steps>
        <div class="setupDIV">
            <el-form ref="executeNew" :model="formData" label-width="115px" size="small">
                <div v-if="stepsActive === 0">
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="类型" prop="type">
                                <el-input disabled v-model="formData.type"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="服务名称" prop="name">
                                <el-input disabled v-model="formData.name"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="所属站点" prop="site">
                                <el-input disabled v-model="formData.site"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="所属业务区" prop="businessArea">
                                <el-input disabled v-model="formData.businessArea"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="业务系统" prop="businessSystemName">
                                <el-input disabled v-model="formData.businessSystemName"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="业务子系统" prop="businessSubsystem">
                                <el-input disabled v-model="formData.businessSubsystem"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>
                <div v-else-if="stepsActive === 1">
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="端口" prop="port">
                                <el-input disabled v-model="formData.port"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="硬件架构" prop="architecture">
                                <el-input disabled v-model="formData.architecture"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="版本" prop="version">
                                <el-input disabled v-model="formData.version"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="架构" prop="arch">
                                <el-input disabled v-model="formData.arch"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="副本数量" prop="unitCnt" v-if="formData.arch!=='单节点'">
                                <el-input disabled v-model="formData.unitCnt"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="规模" prop="scale" status-icon>
                                <el-input disabled v-model="formData.scale"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12" v-if="getSession('storageMode') == 'volumepath'">
                            <el-form-item label="磁盘类型" prop="diskType" status-icon>
                                <el-input disabled v-model="formData.diskType"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="表空间(G)" prop="dataSize">
                                <el-slider v-model="formData.dataSize" show-input :min=10 :max=500 :step=1 disabled
                                    :format-tooltip="formatToolTip"></el-slider>
                            </el-form-item>
                        </el-col>
                    </el-row>
                   <!--  <el-row style="height: 51px">
                        <el-col :span="8">
                            <el-form-item label="分片结构">
                                <el-switch v-model="cntFlag" disabled @change="cntFlagChange"></el-switch>
                            </el-form-item>
                        </el-col>
                        <el-col :span="4" v-if="cntFlag">
                            <el-form-item label="分片数量">
                                <el-input-number v-model="formData.cnt" :min="2" :max="128" :step=1 disabled></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row> -->
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="高可用">
                                <el-switch v-model="formData.highAvailable" disabled @change="highAvailableChange"></el-switch>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="高级选项">
                                <el-switch v-model="advanced" disabled></el-switch>
                            </el-form-item>
                        </el-col>
                        <el-col :span="4" v-if="advanced">
                            <el-checkbox v-model="paramDeploy" style="padding-top:5px" disabled>参数配置
                            </el-checkbox>
                        </el-col>
                        <%--<el-col :span="4" v-if="advanced">
                            <el-checkbox v-model="backupPolicy" style="padding-top:5px" disabled>备份策略
                            </el-checkbox>
                        </el-col>--%>
                    </el-row>
                </div>
                <div v-else-if="stepsActive === stepPageNums" class="elSteps">
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="参数配置" prop="hotSpare">
                                <vxe-table size="small"
                                    style="color: #333333;margin: 10px 40px;border-left: 1px #E8EAEC solid"
                                    show-overflow :data="formData.paramCfg" show-header-overflow auto-resize border
                                    highlight-hover-row height="300px">
                                    <vxe-table-column field="key" title="参数名" width="70%"></vxe-table-column>
                                    <vxe-table-column field="value" title="参数值" width="30%"></vxe-table-column>
                                </vxe-table>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </div>
                <div v-else-if="stepsActive === stepPageNum" class="elSteps">
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="备份存储类型" prop="backupStorageType">
                                <el-input disabled v-model="formData.backupStorageType"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="备份类型" prop="backUpType">
                                <el-input disabled v-model="formData.backUpType"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="备份周期" prop="cronExpression">
                                <el-select disabled v-model="formData.cronExpression" placeholder=" " clearable
                                    style="display: block;">
                                    <el-option :label="list.name" :value="list.code" v-for="list in cronExpressionList">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8" v-if="showItem('month')">
                            <el-form-item label="日期" prop="month" label-width="70px">
                                <el-select disabled v-model="formData.month" clearable style="display: block;">
                                    <el-option :label="list.name" :value="list.code" v-for="list in timeList(1,31)">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8" v-if="showItem('week')">
                            <el-form-item label="周" prop="week" label-width="70px">
                                <el-select disabled v-model="formData.week" clearable style="display: block;">
                                    <el-option :label="list.name" :value="list.code" v-for="list in timeList(1,7)">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="时间" prop="time" label-width="70px">
                                <el-time-picker disabled v-model="formData.time" format="HH:mm" value-format="mm HH"
                                    style="width: auto;"></el-time-picker>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item :label="fileRetentionName()" prop="fileRetentionNum">
                                <el-input-number disabled v-model="formData.fileRetentionNum" controls-position="right"
                                    style="display: block;width:auto;"></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-form-item label="备份描述" prop="description">
                        <el-input type="textarea" v-model="formData.description" disabled></el-input>
                    </el-form-item>
                </div>
            </el-form>
        </div>
        <div style="position: absolute;right: 0px;bottom: 0px;width:778px">
            <div style="border-bottom: 2px solid #F3F3F3;"></div>
            <div style="float: right;padding: 10px 20px">
                <el-button size="small" @click="stepsActive--" v-show="stepsActive!==0">上一步</el-button>
                <el-button size="small" @click="stepsActive++"
                    v-show="stepsActive !== XEUtils.max([1, stepPageNum, stepPageNums])">下一步</el-button>
                <el-button size="small" type="primary" @click="formSubmit('executeNew')"
                    v-show="stepsActive === XEUtils.max([1, stepPageNum, stepPageNums])">执行
                </el-button>
                <el-button size="small" @click="formClose">取消</el-button>
            </div>
        </div>
    </div>
</body>

<script type="text/javascript">
    <% Object executeId = request.getAttribute("executeId"); %>
    var executeId = <%= executeId %>
</script>

<script type="text/javascript" src="js/order/redis/execute/new.js?t=<%=new Date().getTime() %>"></script>

</html>