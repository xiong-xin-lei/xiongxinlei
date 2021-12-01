var userIndex = parent.layer.getFrameIndex(window.name);
var username = getQueryVariable("username")
var ip = getQueryVariable("ip")
var highAvailable = parent.manageApp.highAvailable
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
var userUpdataApp = new Vue({
    el: '#userUpdata',
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
            maxConnection: 1000,
            properties: '',
            whiteIp: '',
            globalPer: []
        },
        formRules: {
            username: [
                {required: true, message: '请输入用户名'}
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
        submitData: {},
        addIconClassFun: function ({column, columnIndex}) {
            if (column.property === 'del') {
                return 'addIconClass'
            }
        }
    },
    created: function () {
        this.formDataCreatedFun()
    },
    methods: {
        formDataCreatedFun: function () {
            var _this = this
            let url = "/" + getProjectName() + "/serv_groups/mysql/" + parent[0].rowId + "/users/" + username
            if (!_this.highAvailable)
                url += "?ip=" + encodeURIComponent(ip, "utf-8")
            sendGet(url, function (response) {
                var takeData = response.data.data
                var globalPrivilegesData = []
                var privilegesData = []
                if (takeData.dbPrivileges.length !== 0) {
                    takeData.dbPrivileges.forEach(function (v, i) {
                        if (v.dbName === '*') {
                            globalPrivilegesData = v.privileges
                        } else {
                            var temporaryJson = {
                                sql: v.dbName,
                                privileges: v.privileges
                            }
                            if (privilegesData.length === 0) {
                                temporaryJson.del = '-'
                            } else {
                                temporaryJson.del = '-'
                            }
                            temporaryJson.num = privilegesData.length

                            privilegesData.push(temporaryJson)
                        }
                    })
                }
                _this.formData.username = takeData.username
                _this.formData.whiteIp = takeData.whiteIp
                _this.formData.maxConnection = takeData.maxConnection
                _this.formData.properties = jsonJudgeNotDefined(takeData, "takeData.properties.code")
                _this.formData.globalPer = globalPrivilegesData
                _this.sqlPrivilegesData = privilegesData
                _this.propertiesListView()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        propertiesListView: function () {
            let _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=mysql_user_properties", function (response) {
                _this.propertiesList = response.data.data
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        stepsActiveOne: function () {
            this.stepsActiveOneNum++
            if (this.stepsActiveOneNum === this.stepsActiveOneSum) {
                this.$refs.sqlPrivilegesTable.reloadData(this.sqlPrivilegesData)
                layer.closeAll('loading');
            }
        },
        DBAjaxFun: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/serv_groups/mysql/" + parent[0].rowId + "/db/schemas", function (response) {
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

            commonConfirm(_this, '编辑确认', getHintText('编辑')).then(() => {
                _this.formSubmit()
            }).catch(() => {
            });
        },
        formSubmit: function () {
            let _this = this
            let maxConnection = _this.formData.maxConnection;
            let properties = _this.formData.properties;
            let globalPrivileges = {
                dbName: "*",
                privileges: _this.formData.globalPer
            };
            let privilegesArray = [];
            if (_this.formData.globalPer.length !== 0) {
                privilegesArray.push(globalPrivileges);
            }

            _this.sqlPrivilegesData.forEach(function (v, i) {
                if (v.sql !== "" && v.privileges.length !== 0) {
                    var jsonData = {
                        dbName: v.sql,
                        privileges: v.privileges
                    }
                    privilegesArray.push(jsonData);
                }
            })

            _this.submitData = {
                dbPrivileges: privilegesArray
            }

            if (highAvailable) {
                _this.submitData.maxConnection = maxConnection
                _this.submitData.properties = properties
                _this.highAvailableAjaxFun()
            } else {
                let whiteIpList = _this.formData.whiteIp.split(",");
                sendAll(_this.noHighAvailableAjaxFun, whiteIpList, function (successArray, errorArray) {
                    if (errorArray.length === 0) {
                        _this.successFun(successArray)
                    } else {
                        _this.falseFun(errorArray)
                    }
                })
            }
        },
        highAvailableAjaxFun: function () {
            let _this = this
            sendPut("/" + getProjectName() + "/serv_groups/mysql/" + parent[0].rowId + "/users/" + username, function (response) {
                let tempJson = {
                    data: {username: username},
                    object: response
                }
                _this.successFun([tempJson])
            }, function (error) {
                let tempJson = {
                    data: {username: username},
                    object: error
                }
                _this.falseFun([tempJson])
            }, _this.submitData)
        },
        noHighAvailableAjaxFun: function (val, successFun, falseFun) {
            let _this = this
            sendPut("/" + getProjectName() + "/serv_groups/mysql/" + parent[0].rowId + "/users/" + username + "?ip=" + encodeURIComponent(val, "utf-8"), function (response) {
                successFun(response, val)
            }, function (error) {
                falseFun(error, val)
            }, _this.submitData)
        },
        successFun: function (successArray) {
            parent[0].userListApp.returnList()
            parent[0].userListApp.refreshSaveData.push(parent[0].userListApp.selectedData)
            operationCompletion(parent[0].userListApp, "操作成功")
            this.formClose()
        },
        falseFun: function (errorArray) {
            operationCompletion(this, errorMsg(errorArray, false), 'error')
        },
        formClose: function () {
            parent.layer.close(userIndex);
        }
    }
})