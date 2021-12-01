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
                <el-row>
                    <el-col :span="12" >
                        <el-form-item label="所属站点" prop="siteId">
                            <el-select v-model="formData.siteId" placeholder="请选择所属站点" style="display: block;" disabled>
                                <el-option :label="siteData.name" :value="siteData.id" v-for="siteData in siteList">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
						<el-form-item label="所属业务区" prop="businessAreaId">
							<el-select v-model="formData.businessAreaId" placeholder="请选择所属业务区" style="display: block;">
								<el-option :label="businessAreaData.name" :value="businessAreaData.id"
									v-for="businessAreaData in businessAreaList"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
                </el-row>
                <el-row>
                	<el-col :span="12" >
                        <el-form-item label="网段名称" prop="name">
                            <el-input v-model="formData.name" maxlength="32" placeholder="请输入网段名称"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="起始IP" prop="startIp">
                            <el-input v-model="formData.startIp" maxlength="15" placeholder="请输入起始IP"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                	<el-col :span="12" >
                        <el-form-item label="结束IP" prop="endIp">
                            <el-input v-model="formData.endIp" maxlength="15" placeholder="请输入结束IP"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="网关" prop="gateway">
                            <el-input v-model="formData.gateway" maxlength="15" placeholder="请输入网关"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                 <el-col :span="12" >
                        <el-form-item label="掩码" prop="netmask">
                            <el-input v-model="formData.netmask" maxlength="2" placeholder="请输入掩码" oninput = "value=value.replace(/[^\d]/g,'')"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="VLAN" prop="vlan">
                            <el-input v-model="formData.vlan" maxlength="8" placeholder="请输入VLAN" oninput = "value=value.replace(/[^\d]/g,'')"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                     <el-col :span="12" >
                        <el-form-item label="网络拓扑" prop="topology">
                            <el-select v-model="formData.topology" placeholder="请选择网络拓扑(可多选)" style="display: block;"
                                multiple>
                                <el-option :label="topologyData.name" :value="topologyData.code"
                                    v-for="topologyData in topologyList"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                     <el-col :span="12" style="display:none">
                        <el-form-item label="状态" prop="enabled">
                            <el-switch v-model="formData.enabled"></el-switch>
                            <span class="enabledText">{{ formData.enabled ? "启用":"停用" }}</span>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-form-item label="描述" prop="description">
                        <el-input type="textarea" v-model="formData.description" maxlength="128"></el-input>
                    </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </div>
        <div style="border-bottom: 2px solid #F3F3F3;"></div>
        <div style="float: right;padding: 10px 20px">
            <el-button size="small" type="primary" @click="formSubmit('addForm')">保存</el-button>
            <el-button size="small" @click="formClose">取消</el-button>
        </div>
    </div>
</body>

<script type="text/javascript" src="js/resource/networks/add.js?t=<%=new Date().getTime() %>"></script>