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
        .addIconClass{
            font-size: 26px;
            font-weight: bold;
            color: green;
            cursor: pointer;
        }
        .delParamcell{
            font-size: 26px;
            font-weight: bold;
            color: red;
            cursor: pointer;
        }
        .vxe-table--body-wrapper{
            overflow-x: hidden;
        }
    </style>
</head>

<body>
<div id="userUpdata" v-cloak>

    <el-steps :active="stepsActive" finish-status="success" align-center style="padding-top:10px">
        <el-step title="用户信息"></el-step>
        <el-step title="全局权限"></el-step>
        <el-step title="数据库权限"></el-step>
    </el-steps>

    <div style="padding:30px 40px 30px 20px">
        <el-form ref="userUpdata" :model="formData" :rules="formRules" label-width="90px" size="small">
            <div v-if="stepsActive === 0">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="用户名" prop="username">
                            <el-input disabled v-model="formData.username" placeholder="请输入用户名" maxlength="32" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row v-if="highAvailable">
                    <el-col :span="24">
                        <el-form-item label="最大连接数" prop="maxConnection">
                            <el-input-number v-model="formData.maxConnection" :step="1" :min="1"></el-input-number>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="属性" prop="properties">
                            <el-radio-group v-model="formData.properties">
                                <el-radio :label="data.code" v-for="data in propertiesList">{{ data.name }}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row v-if="!highAvailable">
                    <el-col :span="24">
                        <el-form-item label="访问列表" prop="whiteIp">
                            <el-input disabled type="textarea" rows="3" v-model="formData.whiteIp" placeholder="多个地址使用逗号隔开，允许使用通配符，通配符为%，通配符只能在最后一位"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>
            <div v-else-if="stepsActive === 1">
                <el-row>
                    <el-col :span="24">
                        <el-form-item>
                            <el-checkbox-group v-model="formData.globalPer">
                                <el-checkbox :label="list.code" name="globalPer" v-for="list in globalPrivilegesList">
                                    {{list.description}}
                                </el-checkbox>
                            </el-checkbox-group>
                        </el-form-item>
                    </el-col>
                </el-row>
            </div>
            <div v-else-if="stepsActive === 2">
                <el-row>
                    <el-col :span="24">
                        <vxe-table border show-footer height="220px" :data="sqlPrivilegesData" ref="sqlPrivilegesTable"
                                   @cell-click="delParamRow" @header-cell-click="addParamRow" :header-cell-class-name="addIconClassFun">
                            <vxe-table-column field="del" title="+" width="35px" class-name="delParamcell" align="center"></vxe-table-column>
                            <vxe-table-column field="sql" title="数据库"  width="35%" align="center">
                                <template v-slot="{ row }">
                                    <el-select v-model="row.sql" placeholder="请选择数据库" size="small" @change="paramChange(row)">
                                        <el-option v-for="item in showKey(sqlList)" :label="item.name" :value="item.name">
                                        </el-option>
                                    </el-select>
                                </template>
                            </vxe-table-column>
                            <vxe-table-column field="privileges" title="权限" align="center">
                                <template v-slot="{ row }">
                                    <el-select v-model="row.privileges" multiple placeholder="请选择权限" @visible-change="privilegesClose" 
                                    			size="small" style="display: block" @change="selectAll">
                                        <el-option label="选择所有" value="all"></el-option>
                                        <el-option v-for="item in privilegesList" :label="item.description" :value="item.code"></el-option>
                                    </el-select>
                                </template>
                            </vxe-table-column>
                        </vxe-table>
                    </el-col>
                </el-row>
            </div>
        </el-form>
    </div>

    <div style="position: absolute;right: 0px;bottom: 0px;width:100%">
        <div style="border-bottom: 2px solid #F3F3F3;"></div>
        <div style="float: right;padding: 10px 20px">
            <el-button size="small" @click="stepsActiveBack" v-show="stepsActive!==0">上一步</el-button>
            <el-button size="small" @click="stepsActiveNext('userUpdata')" v-show="stepsActive!==2">下一步</el-button>
            <el-button size="small" type="primary" @click="formSubmitRules('userUpdata')" v-show="stepsActive===2">保存</el-button>
            <el-button size="small" @click="formClose">取消</el-button>
        </div>
    </div>

</div>
</body>

<script type="text/javascript" src="js/servgroup/mysqls/manageTab/user/update.js?t=<%=new Date().getTime() %>"></script>

</html>