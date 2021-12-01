var executeNewIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#executeNew',
    data: {
        stepsActive: 0,
        initialization: 0,
        endNum: 5,
        siteList: [],
        businessAreaList: [],
        clusterList: [],
        remoteStorageList: [],
        ntpServerList: [],
        cntFlag: false,
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
        cronExpressionList: [
            {
                code: 'day',
                name: '按天'
            }, {
                code: 'week',
                name: '按周'
            }, {
                code: 'month',
                name: '按月'
            }
        ],
        timeList: function (start, end) {
            var array = []
            for (var i = start; i <= end; i++) {
                array.push(
                    {
                        code: i,
                        name: i
                    }
                )
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
            architecture: "",
            version: "",
            arch: "",
            unitCnt: 0,
            scale: "",
            diskType: "",
            dataSize: 10,
            logSize: 10,
            port: 6379,
            paramCfg: [],
            msg: "",
            backupStorageType: '',
            backUpType: '',
            cronExpression: '',
            month: null,
            week: null,
            time: null,
            fileRetentionNum: undefined,
            description: '',
            highAvailable: false
        }
    },
    created: function () {
        this.editCreated()
    },
    methods: {
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/order_groups/" + executeId, function (response) {
                var data = response.data.data;
                var ownerName = "";
                if (data.owner !== null) {
                    ownerName = ownerNameDispose(data.owner.name, data.owner.username)
                }

                XEUtils.arrayEach(data.orders, (v, i) => {
                    if (v.type.code === "redis") {
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
                            _this.formData.paramCfg = jsonArray
                            _this.paramData = jsonArray
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
                            type: "Redis",
                            name: data.name + '    (' + ownerName + ')',
                            ownerName: ownerName,
                            businessSystemName: jsonJudgeNotDefined(data, 'data.businessSubsystem.businessSystem.name'),
                            businessSubsystem: jsonJudgeNotDefined(data, 'data.businessSubsystem.name'),
                            site: jsonJudgeNotDefined(data, 'data.site.name'),
                            businessArea: jsonJudgeNotDefined(data, 'data.businessArea.name'),
                            architecture: jsonJudgeNotDefined(data, 'data.sysArchitecture.display'),
                            version: version,
                            arch: jsonJudgeNotDefined(v, 'v.arch.mode.display'),
                            unitCnt: jsonJudgeNotDefined(v, 'v.arch.unitCnt') - 1,
                            scale: jsonJudgeNotDefined(v, 'v.scale.name'),
                            diskType: jsonJudgeNotDefined(v, 'v.diskType.display'),
                            dataSize: v.dataSize,
                            logSize: v.logSize,
                            port: v.port,
                            paramCfg: jsonArray,
                            msg: data.msg,
                            backupStorageType: _this.formData.backupStorageType,
                            backUpType: _this.formData.backUpType,
                            cronExpression: _this.formData.cronExpression,
                            month: _this.formData.month,
                            week: _this.formData.week,
                            time: _this.formData.time,
                            fileRetentionNum: _this.formData.fileRetentionNum,
                            description: _this.formData.description,
                            highAvailable: data.highAvailable,
                            cnt: v.cnt
                        }
                        if (v.cnt > 1)
                            _this.cntFlag = true
                    }
                })

                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        cntFlagChange: function (val) {
            //分片结构打开时，关闭高可用
            if (val)
                this.formData.highAvailable = false
        },
        highAvailableChange: function (val) {
            //高可用打开时，关闭分片结构
            if (val)
                this.cntFlag = false
        },
        formSubmit: function (formName) {//executeNew
            var _this = this
            commonConfirm(this, '执行确认', getHintText('执行')).then(() => {
                _this.sendTableAjax(null)
            }).catch(() => {
            })
        },
        sendTableAjax: function (data) {
            var _this = this
            sendPut("/" + getProjectName() + "/order_groups/" + executeId + "/execute", function (response) {
                setTimeout(function () {
                    layer.closeAll('loading')
                    parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                    parent.listApp.goTourlDialogShow = true
                    _this.formClose()
                }, 1000);
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, data)
        },
        formClose: function () {
            parent.layer.close(executeNewIndex);
        },
        clusterIdChange: function (data) {
            this.initialization--;
            this.formData.clusterId = "";
            this.clusterListView();
        }
    }
})