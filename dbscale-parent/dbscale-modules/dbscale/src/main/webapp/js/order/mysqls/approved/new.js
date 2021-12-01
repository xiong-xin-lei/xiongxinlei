var approvedNewIndex = parent.layer.getFrameIndex(window.name);
var ogAutoExecute = getQueryVariable("ogAutoExecute") === "true"
new Vue({
    el: '#approvedNew',
    data: {
        ogAutoExecute: ogAutoExecute,
        stepsActive: 0,
        stepList: [{
            name: '业务信息',
            isShow: true,
        }, {
            name: '配置信息',
            isShow: true,
        }],
        stepPageNum: -10,
        stepPageNums: -10,
        advanced: false,
        backupPolicy: false,
        paramDeploy: false,
        cronExpressionList: [{
            code: 'day',
            name: '按天'
        }, {
            code: 'week',
            name: '按周'
        }, {
            code: 'month',
            name: '按月'
        }],
        timeList: function (start, end) {
            var array = []
            for (var i = start; i <= end; i++) {
                array.push({
                    code: i,
                    name: i
                })
            }
            return array
        },
        showItem: function (type) {
            switch (this.formData.cronExpression) {
                case 'week':
                    switch (type) {
                        case 'week':
                            return true
                        default:
                            return false
                    }
                case 'month':
                    switch (type) {
                        case 'month':
                            return true
                        default:
                            return false
                    }
                default:
                    return false
            }
        },
        formatToolTip: function (index) {
            return index + "G"
        },
        fileRetentionName: function () {
            switch (this.formData.cronExpression) {
                case 'day':
                    return "文件保留天数"
                case 'week':
                    return "文件保留周数"
                case 'month':
                    return "文件保留月数"
                default:
                    return "文件保留时间"
            }
        },
        formData: {
            type: "",
            name: "",
            ownerName: "",
            businessSystemName: "",
            businessSubsystem: "",
            site: "",
            businessArea: "",
            sysArchitecture: "",
            msg: "",
            mysqlversion: "",
            mysqlarch: "",
            mysqlunitCnt: 0,
            mysqlscale: "",
            mysqldiskType: "",
            mysqldataSize: 10,
            mysqllogSize: 10,
            highAvailable: false,
            proxyNum: 1,
            mysqlport: 3306,
            mysqlparamCfg: [],
            backupStorageType: '',
            backUpType: '',
            cronExpression: '',
            month: null,
            week: null,
            time: null,
            fileRetentionNum: undefined,
            description: ''
        }
    },
    created: function () {
        this.editCreated()
    },
    methods: {
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/order_groups/" + approvedId, function (response) {
                var data = response.data.data;
                var proxyNum = 1
                var ownerName = "";
                if (data.owner !== null) {
                    ownerName = ownerNameDispose(data.owner.name, data.owner.username)
                }

                var msg = "";
                if (data.msg !== null) {
                    msg = data.msg
                }

                XEUtils.arrayEach(data.orders, (v, i) => {
                    if (v.type.code === "mysql") {

                        var version = "";
                        if (v.version !== null) {
                            version = v.version.major + '.' + v.version.minor + '.' + v.version.patch + '.' + v.version.build
                        }

                        var jsonArray = []
                        if (v.cfg.param !== null) {
                            var jsonIndex = 0
                            for (var key in v.cfg.param) {
                                var o = {
                                    "key": key,
                                    "value": v.cfg.param[key],
                                    "num": jsonIndex,
                                    "del": "-"
                                };
                                jsonIndex++
                                jsonArray.push(o)
                            }
                            _this.formData.mysqlparamCfg = jsonArray
                            _this.stepList.push({
                                name: '参数配置',
                                isShow: true,
                            })
                            _this.advanced = true
                            _this.paramDeploy = true
                            _this.stepPageNums = _this.stepList.length - 1
                        }

                        if (v.cfg.backupStrategy !== undefined && v.cfg.backupStrategy !== null) {

                            _this.formData.backUpType = v.cfg.backupStrategy.type.display
                            _this.formData.backupStorageType = v.cfg.backupStrategy.backupStorageType.display

                            var cronExpressionArray = v.cfg.backupStrategy.cronExpression.split(' ')
                            _this.formData.time = cronExpressionArray[0] + " " + cronExpressionArray[1]
                            if (cronExpressionArray[2] !== "*") {
                                _this.formData.cronExpression = 'month'
                                _this.formData.month = cronExpressionArray[2]
                            } else if (cronExpressionArray[4] !== "*") {
                                _this.formData.cronExpression = 'week'
                                _this.formData.week = cronExpressionArray[4]
                            } else {
                                _this.formData.cronExpression = 'day'
                            }

                            _this.formData.fileRetentionNum = v.cfg.backupStrategy.fileRetentionNum
                            _this.formData.description = v.cfg.backupStrategy.description
                            _this.stepList.push({
                                name: '备份策略',
                                isShow: true,
                            })
                            _this.advanced = true
                            _this.backupPolicy = true
                            _this.stepPageNum = _this.stepList.length - 1
                        }

                        _this.formData = {
                            type: "MySQL",
                            name: data.name + '    (' + ownerName + ')',
                            ownerName: ownerName,
                            businessSystemName: jsonJudgeNotDefined(data, 'data.businessSubsystem.businessSystem.name'),
                            businessSubsystem: jsonJudgeNotDefined(data, 'data.businessSubsystem.name'),
                            site: jsonJudgeNotDefined(data, 'data.site.name'),
                            businessArea: jsonJudgeNotDefined(data, 'data.businessArea.name'),
                            sysArchitecture: jsonJudgeNotDefined(data, 'data.sysArchitecture.display'),
                            msg: msg,
                            mysqlversion: version,
                            mysqlarch: jsonJudgeNotDefined(v, 'v.arch.mode.display'),
                            mysqlunitCnt: jsonJudgeNotDefined(v, 'v.arch.unitCnt') - 1,
                            mysqlscale: jsonJudgeNotDefined(v, 'v.scale.name'),
                            mysqldiskType: jsonJudgeNotDefined(v, 'v.diskType.display'),
                            mysqldataSize: v.dataSize,
                            mysqllogSize: v.logSize,
                            highAvailable: data.highAvailable,
                            mysqlport: v.port,
                            mysqlparamCfg: jsonArray,
                            backupStorageType: _this.formData.backupStorageType,
                            backUpType: _this.formData.backUpType,
                            cronExpression: _this.formData.cronExpression,
                            month: _this.formData.month,
                            week: _this.formData.week,
                            time: _this.formData.time,
                            fileRetentionNum: _this.formData.fileRetentionNum,
                            description: _this.formData.description
                        }
                    } else if (v.type.code === "proxysql") {
                        proxyNum = jsonJudgeNotDefined(v, "v.arch.unitCnt")
                    }
                })
                _this.formData.proxyNum = proxyNum
                layer.closeAll('loading')
            }, function (error) {
                console.log(error)
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        formSubmit: function (formName, type) { //approvedNew
            var msg = this.formData.msg
            var status = ''
            if (type) {
                status = "approved"
            } else {
                status = "unapprove"
            }
            var jsonData = {
                msg: msg,
                state: status
            }
            var _this = this
            if (type) {
                commonConfirm(this, '审批确认', getHintText('', '', '是否确认同意审批')).then(() => {
                    _this.sendTableAjax(jsonData)
                }).catch(() => {
                })
            } else {
                var affirm = false
                if (msg.length === 0) {
                    this.$prompt('请输入拒绝理由', '提示', {
                        cancelButtonClass: "btn-custom-cancel",
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        closeOnClickModal: false,
                        inputType: 'textarea',
                        inputPattern: /\S/,
                        inputErrorMessage: '拒绝理由不能为空',
                        customClass: 'messageBoxMsgTest'
                    }).then(({value}) => {
                        jsonData.msg = value
                        _this.sendTableAjax(jsonData)
                    }).catch(() => {
                    });
                } else {
                    commonConfirm(this, '审批确认', getHintText('', '', '是否确认拒绝审批')).then(() => {
                        _this.sendTableAjax(jsonData)
                    }).catch(() => {
                    })
                }
            }
        },
        sendTableAjax: function (data) {
            var _this = this
            sendPut("/" + getProjectName() + "/order_groups/" + approvedId + "/examine", function (response) {
                layer.closeAll('loading')
                operationCompletion(parent.listApp, "操作成功！")
                parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                if (_this.ogAutoExecute && data.state === "approved") {
                    parent.listApp.goTourlDialogShow = true
                } else {
                    parent.listApp.returnList()
                }
                _this.formClose()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, data)
        },
        formClose: function () {
            parent.layer.close(approvedNewIndex);
        }
    }
})