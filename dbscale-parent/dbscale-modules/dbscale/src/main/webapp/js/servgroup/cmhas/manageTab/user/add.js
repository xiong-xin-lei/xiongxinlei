var userIndex = parent.layer.getFrameIndex(window.name);
var highAvailable = parent.manageApp.highAvailable
var checkPwd = (rule, value, callback) => {
    /*var checkChar = new RegExp('^(?!.*\\\s)(?=.*[0-9].*)(?=.*[A-Za-z].*)(?=.*[~!@#%\\^*()_\\+`\\-=\\[\\]\\\{}|;\':"\\,\\./<>\\?]).{8,}$');
    var checkChar2 = new RegExp('^[0-9a-zA-Z].{6,}[0-9a-zA-Z]$');
    if (!checkChar.test(value) || !checkChar2.test(value)) {
        callback(new Error('密码格式错误'));//密码必须由数字、字母和特殊字符组成，长度必须是8位及以上,不能以特殊字符开头和结尾，不能有空格、$、&
        return false;
    }
    if (value.indexOf('&') >= 0 || value.indexOf('$') >= 0) {
        callback(new Error('密码格式错误'));
        return false;
    }*/
    /*var pswCheck = new RegExp('^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$')
    var specharsCheck1 = new RegExp('[$\x22]+')
    var specharsCheck2 = new RegExp('[&\x22]+')
    var blankCheck = new RegExp('\s')*/
    var valueArray = value.split('')

    var pswCheck = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/;
    var headTailCheck = /[%&',;=?$!()<>-@#^*"|\x22]+/;
    var specharsCheck1 = /[$\x22]+/;
    var specharsCheck2 = /[&\x22]+/;
    var blankCheck = /\s/;

    if (!pswCheck.test(value) || specharsCheck1.test(value) || specharsCheck2.test(value) || blankCheck.test(value) || headTailCheck.test(valueArray[0]) || headTailCheck.test(valueArray[valueArray.length - 1])) {
        callback(new Error('密码格式错误'));//密码必须由数字、字母和特殊字符组成，长度必须是8位及以上,不能以特殊字符开头和结尾，不能有空格、$、&
        return false;
    }
    callback();
};
var validatePass = (rule, value, callback) => {
    switch (value) {
        case '':
            callback(new Error('请再次输入密码'));
            break;
        case userAddApp.formData.password:
            callback();
            break;
        default:
            callback(new Error('两次输入密码不一致!'));
            break;
    }
};
var whiteIpRules = (rule, value, callback) => {
    var whiteIpFlag = true;
    if (value === '') {
        whiteIpFlag = false;
        callback(new Error('请输入访问列表'));
    } else {
        var valArray = value.split(",");
        for (var i = 0; i < valArray.length; i++) {
            var IPValArray = valArray[i].split(".")
            if (IPValArray[3] === '%') {
                IPValArray[3] = "1"
                var ipValidVal = arrayMerger(IPValArray, null, '.')
                if (!isValidIP(ipValidVal)) {
                    whiteIpFlag = false;
                    callback(new Error('访问列表格式错误（正确格式：xxx.xxx.xxx.xxx或xxx.xxx.xxx.%）'));
                    return false
                }
            } else {
                if (!isValidIP(valArray[i])) {
                    whiteIpFlag = false;
                    callback(new Error('访问列表格式错误（正确格式：xxx.xxx.xxx.xxx或xxx.xxx.xxx.%）'));
                    return false
                }
            }
        }
    }
    if (whiteIpFlag) {
        callback();
    }
};
var userAddApp = new Vue({
    el: '#userAdd',
    data: {
        highAvailable: highAvailable,
        stepsActive: 0,
        stepsActiveOneNum: 0,
        stepsActiveOneSum: 2,
        dataBaseNum: 0,
        propertiesList: [],
        sqlList: [],
        sqlPrivilegesData: [],
        globalPrivilegesList: [],
        privilegesList: [],
        formData: {
            username: '',
            password: '',
            verifyPassword: '',
            maxConnection: 1000,
            properties: '',
            whiteIp: '',
            globalPer: []
        },
        formRules: {
            username: [
                {required: true, message: '请输入用户名'}
            ],
            password: [
                {required: true, message: '请输入密码'},
                {validator: checkPwd, trigger: 'blur'}
            ],
            verifyPassword: [
                {required: true, message: '请再次输入密码'},
                {validator: validatePass, trigger: 'blur'}
            ],
            maxConnection: [
                {required: true, message: '请输入最大连接数'}
            ],
            properties: [
                {required: true, message: '请选择路由组'}
            ],
            whiteIp: [
                {required: true, message: '请输入访问列表'},
                {validator: whiteIpRules, trigger: 'blur'}
            ]
        },
        addIconClassFun: function ({column, columnIndex}) {
            if (column.property === 'del') {
                return 'addIconClass'
            }
        }
    },
    created: function () {
        this.propertiesListView()
    },
    methods: {
        propertiesListView: function () {
            let _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=mysql_user_properties", function (response) {
                let data = response.data.data
                _this.propertiesList = data
                if (data.length > 0)
                    _this.formData.properties = data[0].code
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        stepsActiveOne: function () {
            this.stepsActiveOneNum++
            if (this.stepsActiveOneNum === this.stepsActiveOneSum) {
                if (this.sqlList) {
                    this.sqlPrivilegesData[0] = {
                        sql: '',
                        privileges: [],
                        del: "-",
                        num: 0
                    }
                } else {
                    this.sqlPrivilegesData = []
                }
                this.$refs.sqlPrivilegesTable.reloadData(this.sqlPrivilegesData)
                layer.closeAll('loading');
            }
        },
        DBAjaxFun: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/serv_groups/cmha/" + parent[0].rowId + "/db/schemas", function (response) {
                _this.sqlList = response.data.data;
                _this.stepsActiveOne()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        globalPrivilegesAjaxFun: function (successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/privileges?enabled=true&global=true", function (response) {
                successFun(response);
                layer.closeAll('loading');
            }, function (error) {
                falseFun(error)
            }, null)
        },
        privilegesAjaxFun: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/privileges?enabled=true&global=false", function (response) {
                if (response.data.data) {
                    _this.privilegesList = response.data.data
                }
                _this.stepsActiveOne()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        showKey: function (val) {
            var _this = this;
            var temporaryData = [];
            XEUtils.arrayEach(val, (v, i) => {
                var showData = true;
                _this.sqlPrivilegesData.forEach(function (value, index) {
                    if (value.sql === v.name) {
                        showData = false
                    }
                })
                if (showData) {
                    temporaryData.push(v)
                }
            })
            return temporaryData
        },
        paramChange: function (row) {
            //console.log(row)
            /*var _this = this
            var sqlPrivilegesData = this.sqlList
            var paramLength = sqlPrivilegesData.length
            for ( var i = 0; i < paramLength; i++ ) {
                if(sqlPrivilegesData[i].name === row.sql){
                    _this.sqlPrivilegesData[row.num].privileges = sqlPrivilegesData[i].privileges
                    break;
                }
            }*/
            this.dataBaseNum = row.num
            this.sqlPrivilegesData[row.num].privileges = []
        },
        selectAll(val) {
            var _this = this
            var privilegesArray = []
            _this.privilegesList.forEach(function (val, index) {
                privilegesArray.push(val.code)
            })
            XEUtils.arrayEach(val, (v, i) => {
                if (v === 'all') {
                    _this.sqlPrivilegesData[_this.dataBaseNum].privileges = privilegesArray
                }
            })
            if (val.length > privilegesArray.length) {
                _this.sqlPrivilegesData[_this.dataBaseNum].privileges = []
            }
        },
        privilegesClose: function (flag) {
            if (!flag) {
                this.$refs.sqlPrivilegesTable.loadData(this.sqlPrivilegesData)
            }
        },
        addParamRow: function ({column, row}) {
            if (column.title !== '+') {
                return false
            }
            var _this = this;
            var paramData = this.sqlList
            var paramLength = paramData.length
            var paramTableData = this.sqlPrivilegesData
            var paramTableLength = paramTableData.length
            var paramNum;
            if (paramTableLength !== 0) {
                /*for ( var i = 0; i < paramLength; i++ ) {
                    var showData = true;

                    for ( var j = 0; j < paramTableLength; j++ ) {
                        if(paramData[i].sql === paramTableData[j].sql){
                            showData = false;
                        }
                    }
                    if(showData){
                        paramNum = i;
                        break;
                    }
                }*/
                if (this.sqlPrivilegesData[paramTableLength - 1].sql === '') {
                    return false
                }
                this.sqlPrivilegesData[paramTableLength] = {
                    sql: '',
                    privileges: [],
                    del: "-",
                    num: paramTableLength
                }
                this.$refs.sqlPrivilegesTable.reloadData(this.sqlPrivilegesData)
            } else {
                this.sqlPrivilegesData[0] = {
                    sql: '',
                    privileges: [],
                    del: "-",
                    num: 0
                }
                this.$refs.sqlPrivilegesTable.reloadData(this.sqlPrivilegesData)
            }
        },
        delParamRow: function ({column, row}) {
            var _this = this
            var paramTableData = this.sqlPrivilegesData
            var paramTableLength = paramTableData.length
            if (column.title === "+") {
                this.sqlPrivilegesData.splice(row.num, 1)
                for (var i = row.num; i < paramTableLength - 1; i++) {
                    _this.sqlPrivilegesData[i].num--
                }
                this.$refs.sqlPrivilegesTable.reloadData(this.sqlPrivilegesData)
            }
            /*if (column.title === "+" && row.num === 0){
                this.addParamRow()
            }*/
        },
        stepsActiveBack: function () {
            this.stepsActive--
        },
        stepsActiveNext: function (formName) {
            var _this = this
            var verify = false;
            if (this.stepsActive === 0) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        return true;
                    } else {
                        verify = true;
                        //console.log('error submit!!');
                        return false;
                    }
                });
                if (verify) {
                    return false;
                }
                //_this.formData.globalPer = []
                if (_this.globalPrivilegesList.length === 0) {
                    this.globalPrivilegesAjaxFun(function (response) {
                        if (response.data.data) {
                            _this.globalPrivilegesList = response.data.data
                        }
                    }, function (error) {
                        operationCompletion(_this, error.response.data.msg, 'error')
                    })
                }
            } else if (this.stepsActive === 1) {
                //_this.sqlPrivilegesData = []
                if (_this.stepsActiveOneNum === 0) {
                    _this.privilegesAjaxFun()
                    _this.DBAjaxFun()
                }
            }
            this.stepsActive++
        },
        formSubmitRules: function (formName) {
            var _this = this;
            var submitRules = true;
            var submitRulesText = "";
            for (var i = 0; i < _this.sqlPrivilegesData.length; i++) {
                var cyclicData = _this.sqlPrivilegesData[i];
                if (cyclicData.sql === '') {
                    if (cyclicData.privileges.length !== 0) {
                        submitRulesText = "数据库";
                        submitRules = false;
                        return false
                    }
                } else if (cyclicData.privileges.length === 0) {
                    if (cyclicData.sql !== '') {
                        submitRulesText = "权限";
                        submitRules = false;
                        return false
                    }
                }
            }

            if (_this.formData.globalPer.length === 0) {
                if (_this.sqlPrivilegesData.length === 0) {
                    submitRulesText = "权限";
                    submitRules = false;
                } else if (_this.sqlPrivilegesData.length === 1) {
                    if (_this.sqlPrivilegesData[0].sql === '' && _this.sqlPrivilegesData[0].privileges.length === 0) {
                        submitRulesText = "权限";
                        submitRules = false;
                    }
                }
            }

            if (!submitRules) {
                operationCompletion(_this, submitRulesText + "未选择", 'warning')
                return false
            }

            commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
                _this.formSubmit()
            }).catch(() => {
            });
        },
        formSubmit: function () {
            var _this = this
            var authType = "mysql_native_password";
            var username = _this.formData.username;
            var password = _this.formData.password;
            var maxConnection = _this.formData.maxConnection;
            var properties = _this.formData.properties;
            var whiteIpList = _this.formData.whiteIp.split(",");
            var globalPrivileges = {
                dbName: "*",
                privileges: _this.formData.globalPer
            };
            var privilegesArray = [];
            if (_this.formData.globalPer.length !== 0) {
                privilegesArray.push(globalPrivileges);
            }

            _this.sqlPrivilegesData.forEach(function (v, i) {
                if (v.sql !== "" && v.privileges.length !== 0) {
                    let jsonData = {
                        dbName: v.sql,
                        privileges: v.privileges
                    }
                    privilegesArray.push(jsonData);
                }
            })

            var submitJsonData = {
                authType: authType,
                username: username,
                password: password,
                dbPrivileges: privilegesArray
            }

            if (highAvailable) {
                submitJsonData.maxConnection = maxConnection
                submitJsonData.properties = properties
            } else {
                submitJsonData.whiteIps = whiteIpList
            }

            //console.log(submitJsonData)
            sendPost("/" + getProjectName() + "/serv_groups/cmha/" + parent[0].rowId + "/users", function (response) {
                parent[0].userListApp.returnList()
                _this.formClose()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, submitJsonData)

        },
        formClose: function () {
            parent.layer.close(userIndex);
        }
    }
})