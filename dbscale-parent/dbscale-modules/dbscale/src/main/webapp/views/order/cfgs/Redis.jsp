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
        iframe {
            border: none;
        }

        .el-main {
            padding: 10px 20px 0;
        }
    </style>
</head>

<body>
<div id="redis" v-cloak>
    <div>
        <el-form ref="addForm" :model="formData" label-width="100px" :rules="formRules" size="small" :disabled="!editDisabledFlag()"
                 validate-on-rule-change="false">
            <el-container>
                <el-header style="height:30px;line-height:30px">数据库</el-header>
                <div style="border-bottom: 2px solid rgb(243, 243, 243);"></div>
                <el-main>
                    <el-row>
                        <el-col :span="7">
                            <el-form-item label="规模" prop="mysqlscales">
                                <el-select v-model="formData.mysqlscales" placeholder="请选择规模"
                                           style="display: block;">
                                    <el-option :value="scalesData.name" v-for="scalesData in mysqlScalesList">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="端口" prop="mysqlPort">
                                <el-input v-model="formData.mysqlPort" placeholder="请输入端口" maxlength="8" oninput="value=value.replace(/[^\d]/g,'')"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="7" v-if="getSession('storageMode') == 'volumepath'">
                            <el-form-item label="磁盘类型" prop="mysqldiskTypes">
                                <el-select v-model="formData.mysqldiskTypes" placeholder="请选择磁盘类型"
                                           style="display: block;">
                                    <el-option :label="diskTypeData.name" :value="diskTypeData.code"
                                               v-for="diskTypeData in mysqlDiskTypeList">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="表空间" prop="mysqldataSize">
                                <el-input v-model="formData.mysqldataSize" placeholder="请输入表空间大小" maxlength="6" oninput="value=value.replace(/[^\d]/g,'')">
                                    <template slot="append">G</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="日志空间" prop="mysqllogSize">
                                <el-input v-model="formData.mysqllogSize" placeholder="请输入日志空间大小" maxlength="6" oninput="value=value.replace(/[^\d]/g,'')">
                                    <template slot="append">G</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                         <el-col :span="6">
                             <el-form-item label="集群高可用" prop="mysqlclusterHA">
                                 <el-switch v-model="formData.mysqlclusterHA" @change="clusterHAChange"></el-switch>
                                 <span class="enabledText">{{ formData.mysqlclusterHA ? "开":"关" }}</span>
                             </el-form-item>
                         </el-col>
                         <el-col :span="6">
                             <el-form-item label="主机高可用" prop="mysqlhostHA">
                                 <el-switch v-model="formData.mysqlhostHA"></el-switch>
                                 <span class="enabledText">{{ formData.mysqlhostHA ? "开":"关" }}</span>
                             </el-form-item>
                         </el-col>
                         <!--  <el-col :span="6">
                             <el-form-item label="网络高可用" prop="mysqlnetworkHA">
                                 <el-switch v-model="formData.mysqlnetworkHA"></el-switch>
                                 <span class="enabledText">{{ formData.mysqlnetworkHA ? "开":"关" }}</span>
                             </el-form-item>
                         </el-col>
                         <el-col :span="6">
                             <el-form-item label="存储高可用" prop="mysqlstorageHA">
                                 <el-switch v-model="formData.mysqlstorageHA"></el-switch>
                                 <span class="enabledText">{{ formData.mysqlstorageHA ? "开":"关" }}</span>
                             </el-form-item>
                         </el-col> -->
                     </el-row>
                </el-main>
            </el-container>
            <el-container>
                <el-header style="height:30px;line-height:30px">高可用</el-header>
                <div style="border-bottom: 2px solid rgb(243, 243, 243);"></div>
                <el-main>
                    <el-row>
                        <el-col :span="7">
                            <el-form-item label="规模" prop="sentinelscales">
                                <el-select v-model="formData.sentinelscales" placeholder="请选择规模"
                                           style="display: block;">
                                    <el-option :value="scalesData.name" v-for="scalesData in sentinelScalesList">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="端口" prop="sentinelPort">
                                <el-input v-model="formData.sentinelPort" placeholder="请输入端口" maxlength="8" oninput="value=value.replace(/[^\d]/g,'')"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="7" v-if="getSession('storageMode') == 'volumepath'">
                            <el-form-item label="磁盘类型" prop="sentineldiskTypes">
                                <el-select v-model="formData.sentineldiskTypes" placeholder="请选择磁盘类型"
                                           style="display: block;">
                                    <el-option :label="diskTypeData.name" :value="diskTypeData.code"
                                               v-for="diskTypeData in sentinelDiskTypeList">
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="数据目录" prop="sentineldataSize">
                                <el-input v-model="formData.sentineldataSize" placeholder="请输入数据目录大小" maxlength="6" oninput="value=value.replace(/[^\d]/g,'')">
                                    <template slot="append">G</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="7">
                            <el-form-item label="日志目录" prop="sentinellogSize">
                                <el-input v-model="formData.sentinellogSize" placeholder="请输入日志目录大小" maxlength="6" oninput="value=value.replace(/[^\d]/g,'')">
                                    <template slot="append">G</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                         <el-col :span="6">
                             <el-form-item label="集群高可用" prop="sentinelclusterHA">
                                 <el-switch v-model="formData.sentinelclusterHA" @change="clusterHAChange"></el-switch>
                                 <span class="enabledText">{{ formData.sentinelclusterHA ? "开":"关" }}</span>
                             </el-form-item>
                         </el-col>
                         <el-col :span="6">
                             <el-form-item label="主机高可用" prop="sentinelhostHA">
                                 <el-switch v-model="formData.sentinelhostHA"></el-switch>
                                 <span class="enabledText">{{ formData.sentinelhostHA ? "开":"关" }}</span>
                             </el-form-item>
                         </el-col>
                         <!--  <el-col :span="6">
                             <el-form-item label="网络高可用" prop="sentinelnetworkHA">
                                 <el-switch v-model="formData.sentinelnetworkHA"></el-switch>
                                 <span class="enabledText">{{ formData.sentinelnetworkHA ? "开":"关" }}</span>
                             </el-form-item>
                         </el-col>
                         <el-col :span="6">
                             <el-form-item label="存储高可用" prop="sentinelstorageHA">
                                 <el-switch v-model="formData.sentinelstorageHA"></el-switch>
                                 <span class="enabledText">{{ formData.sentinelstorageHA ? "开":"关" }}</span>
                             </el-form-item>
                         </el-col> -->
                     </el-row>
                </el-main>
            </el-container>
        </el-form>
    </div>
    <div style="border-bottom: 2px solid #F3F3F3;" v-if="editDisabledFlag()"></div>
    <div style="float: right;padding: 10px 20px" v-if="editDisabledFlag()">
        <el-button size="small" type="primary" @click="formSubmit('addForm')">保存</el-button>
    </div>
</div>
</body>

<script type="text/javascript">
    <% Object btnPer = request.getAttribute("btnPer"); %>
    var btnLists = <%= btnPer %>;
    var ttBtnList = [];
    var rowBtnList = [];
    var otherBtnList = [];
    $.each(btnLists, function (index, btn) {
        var btnPos = btn.pos;
        if (btnPos !== undefined) {
            if (btnPos.indexOf("tabletop") != -1) {
                ttBtnList.push(btn);
            }
            if (btnPos.indexOf("row") != -1) {
                rowBtnList.push(btn);
            }
        } else {
            otherBtnList.push(btn);
        }
    });
    rowBtnList = rowBtnList.sort(sortRowSeq);
</script>

<script type="text/javascript" src="js/order/cfgs/Redis.js?t=<%=new Date().getTime() %>"></script>

</html>