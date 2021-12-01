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
        .el-input-group__append, .el-input-group__prepend {
            padding: 0 5px;
        }
        .el-radio-button{
            width: 33%;
            margin-top: 10px;
        }
        .el-radio-button__inner{
            width: 100%;
        }
        .el-radio-button:nth-child(3) .el-radio-button__inner{
            border-radius: 0 4px 4px 0;
        }
        .el-radio-button:nth-child(4) .el-radio-button__inner{
            border-radius: 4px 0 0 4px;
            border-left: 1px solid #DCDFE6;
        }
    </style>
</head>

<body>
<div id="usersCheck" v-cloak>
    <div style="margin: 30px auto; width: 80%;">
        <el-form ref="usersCheckForm" :model="formData" :label-width="labelWidth" :rules="formRules" size="small" @submit.native.prevent>
            <el-form-item label="密码" prop="password">
                <el-input v-model="formData.password" show-password autocomplete="new-password" autofocus @keyup.enter.native="formSubmit('usersCheckForm')">
                    <template slot="append">
                        <el-tooltip placement="bottom">
                            <div slot="content">
                                该密码为平台登录密码
                            </div>
                            <i class="el-icon-question" style="font-size:18px"></i>
                        </el-tooltip>
                    </template>
                </el-input>
            </el-form-item>
            <el-form-item label="关闭时长" prop="closeTime" v-if="closeTimeShow">
                <el-radio-group v-model="formData.closeTime" size="small">
                    <el-radio-button :label="list.code" v-for="list in closeTimeList">{{ list.name }}</el-radio-button>
                </el-radio-group>
                <%--<el-select v-model="formData.closeTime" placeholder="请选择关闭时长" style="display: block;">
                    <el-option :label="list.name" :value="list.code" v-for="list in closeTimeList"></el-option>
                </el-select>--%>
            </el-form-item>
        </el-form>
    </div>
    <div style="border-bottom: 2px solid #F3F3F3;"></div>
    <div style="float: right;padding: 10px 20px">
        <el-button size="small" type="primary" @click="formSubmit('usersCheckForm')">确认</el-button>
        <el-button size="small" @click="formClose">取消</el-button>
    </div>
</div>
</body>

<script type="text/javascript" src="js/servgroup/usersCheck.js?t=<%=new Date().getTime() %>"></script>

</html>