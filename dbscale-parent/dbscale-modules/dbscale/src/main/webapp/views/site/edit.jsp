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
</head>

<body>
    <div id="edit" v-cloak>
        <div style="padding:40px 40px 30px 20px">
            <el-form ref="editForm" :model="formData" label-width="100px" :rules="formRules" size="small">
                <el-form-item label="站点名称" prop="name">
                    <el-input v-model="formData.name" placeholder="请输入站点名称" maxlength="16" ></el-input>
                </el-form-item>
            </el-form>
        </div>
        <div style="border-bottom: 2px solid #F3F3F3;"></div>
        <div style="float: right;padding: 10px 20px">
            <el-button size="small" type="primary" @click="formSubmit('editForm')">保存</el-button>
            <el-button size="small" @click="formClose">取消</el-button>
        </div>
    </div>
</body>

<script type="text/javascript" src="js/site/edit.js?t=<%=new Date().getTime() %>"></script>
<script type="text/javascript">
    $(document).keydown(function (event) {
        if (event.keyCode == 13) {
            return false
        }
    });
</script>

</html>