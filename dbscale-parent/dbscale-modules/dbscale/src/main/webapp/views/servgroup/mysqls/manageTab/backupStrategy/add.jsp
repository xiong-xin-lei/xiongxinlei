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
    <base href="<%=basePath%>"></base>
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
<div id="backupStrategyAdd" v-cloak>
    <div style="padding:30px 40px 30px 20px">
        <el-form ref="backupStrategyForm" :model="formData" label-width="110px" :rules="formRules" size="small">
            <el-form-item label="备份存储类型" prop="backupStorageType">
                <el-select v-model="formData.backupStorageType" placeholder="请选择备份存储类型" style="display: block;">
                    <el-option :label="list.name" :value="list.code" v-for="list in backupStorageTypeList"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="备份类型" prop="type">
                <el-select v-model="formData.type" placeholder="请选择备份类型" style="display: block;">
                    <el-option :label="list.name" :value="list.code" v-for="list in backupTypeList"></el-option>
                </el-select>
            </el-form-item>

            <el-row>
                <el-col :span="8">
                    <el-form-item label="备份周期" prop="cronExpression">
                        <el-select v-model="formData.cronExpression" placeholder="请选择备份周期" style="display: block;">
                            <el-option :label="list.name" :value="list.code" v-for="list in cronExpressionList"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8" v-if="showItem('month')">
                    <el-form-item label="日期" prop="month" label-width="70px">
                        <el-select v-model="formData.month" placeholder="请选择日期" style="display: block;">
                            <el-option :label="list.name" :value="list.code" v-for="list in timeList(1,31)"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8" v-if="showItem('week')">
                    <el-form-item label="周" prop="week" label-width="70px">
                        <el-select v-model="formData.week" placeholder="请选择周" style="display: block;">
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
                        <el-input-number v-model="formData.fileRetentionNum" :placeholder=["请输入"+fileRetentionName()]
                                         :min="1" controls-position="right" style="display: block;width:auto;"></el-input-number>
                    </el-form-item>
                </el-col>
            </el-row>

            <el-form-item label="备份描述" prop="description">
                <el-input type="textarea" v-model="formData.description" maxlength="128"
                          placeholder="对此备份策略的描述"></el-input>
            </el-form-item>
        </el-form>
    </div>
    <div style="border-bottom: 2px solid #F3F3F3;"></div>
    <div style="float: right;padding: 10px 20px">
        <el-button size="small" type="primary" @click="formSubmit('backupStrategyForm')">保存</el-button>
        <el-button size="small" @click="formClose">取消</el-button>
    </div>
</div>
</body>

<script type="text/javascript"
        src="js/servgroup/mysqls/manageTab/backupStrategy/add.js?t=<%=new Date().getTime() %>"></script>

</html>