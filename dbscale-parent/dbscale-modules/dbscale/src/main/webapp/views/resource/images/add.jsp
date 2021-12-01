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
    <div id="add" v-cloak>
        <div style="padding:30px 40px 30px 20px">
            <el-form ref="addForm" :model="formData" label-width="100px" :rules="formRules" size="small">
                <el-form-item label="所属站点" prop="siteId">
                    <el-select v-model="formData.siteId" placeholder="请选择所属站点" style="display: block;" disabled>
                        <el-option :label="siteData.name" :value="siteData.id" v-for="siteData in siteList"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="架构" prop="arch">
                    <el-select v-model="formData.arch" placeholder="请选择架构" style="display: block;">
                        <el-option :label="item.name" :value="item.code" v-for="item in archList"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="类型" prop="type">
                    <el-select v-model="formData.type" placeholder="请选择类型" style="display: block;">
                        <el-option :label="item.name" :value="item.code" v-for="item in typeList"></el-option>
                    </el-select>
                </el-form-item>
                <el-row>
                    <el-col
                        style="width:100px;height:32px;font-size:12px;line-height:32px;text-align:right;padding-right:20px;color:#666666">
                        <label><span style="color:#F56C6C;margin-right: 4px;">*</span>版本</label></el-col>
                    <el-col :span="4">
                        <el-form-item prop="majorVersion" label-width="0px">
                            <el-input v-model="formData.majorVersion" placeholder="主版本"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4" :offset="1">
                        <el-form-item prop="minorVersion" label-width="0px">
                            <el-input v-model="formData.minorVersion" placeholder="次版本"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4" :offset="1">
                        <el-form-item prop="patchVersion" label-width="0px">
                            <el-input v-model="formData.patchVersion" placeholder="修订版本"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4" :offset="1">
                        <el-form-item prop="buildVersion" label-width="0px">
                            <el-input v-model="formData.buildVersion" placeholder="编译版本"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="状态" prop="enabled" style="display:none">
                    <el-switch v-model="formData.enabled"></el-switch>
                    <span class="enabledText">{{ formData.enabled ? "启用":"停用" }}</span>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input type="textarea" v-model="formData.description" maxlength="128"></el-input>
                </el-form-item>

            </el-form>
        </div>
        <div style="border-bottom: 2px solid #F3F3F3;"></div>
        <div style="float: right;padding: 10px 20px">
            <el-button size="small" type="primary" @click="formSubmit('addForm')">保存</el-button>
            <el-button size="small" @click="formClose">取消</el-button>
        </div>
    </div>
</body>

<script type="text/javascript" src="js/resource/images/add.js?t=<%=new Date().getTime() %>"></script>

</html>