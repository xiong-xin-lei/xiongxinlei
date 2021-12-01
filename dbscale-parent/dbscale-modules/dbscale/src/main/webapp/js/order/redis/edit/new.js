var editNewIndex = parent.layer.getFrameIndex(window.name);
var ogAutoExamine = getQueryVariable("ogAutoExamine") === "true"
var ogAutoExecute = getQueryVariable("ogAutoExecute") === "true"

var checkNum = (rule, value, callback) => {
    if (value < 10) {
        callback(new Error('最小值为 10 '));
        return false;
    }
    callback();
};
var validateFileRetentionNum = (rule, value, callback) => {
    var timeType = ""
    switch (redisEditApp.formData.cronExpression) {
        case 'day':
            timeType = redisEditApp.formData.time !== null
            break;
        case 'week':
            timeType = redisEditApp.formData.time !== null && redisEditApp.formData.week !== null
            break;
        case 'month':
            timeType = redisEditApp.formData.time !== null && redisEditApp.formData.month !== null
            break;
    }
    var All = redisEditApp.formData.backupStorageType === '' && redisEditApp.formData.backUpType === '' &&
        redisEditApp.formData.cronExpression === '' && redisEditApp.formData.month === null &&
        redisEditApp.formData.week === null && redisEditApp.formData.time === null &&
        redisEditApp.formData.fileRetentionNum === undefined

    var FullAll = redisEditApp.formData.backupStorageType !== '' && redisEditApp.formData.backUpType !== '' &&
        redisEditApp.formData.cronExpression !== '' && redisEditApp.formData.fileRetentionNum !== undefined && timeType


    if (All) {
        return true
    } else if (FullAll) {
        return true
    } else {
        switch (redisEditApp.formData.cronExpression) {
            case 'day':
                callback(new Error('请输入文件保留天数'));
                break;
            case 'week':
                callback(new Error('请输入文件保留周数'));
                break;
            case 'month':
                callback(new Error('请输入文件保留月数'));
                break;
            default:
                callback(new Error('请输入文件保留时间'));
                break;
        }
        callback(new Error('请全部填写'));
        return false
    }

    callback();
};
var redisEditApp = new Vue({
    el: '#editNew',
    data: {
        ogAutoExamine: ogAutoExamine,
        ogAutoExecute: ogAutoExecute,
        initialization: 0,
        endNum: [4, 4],
        modeMin: 1,
        modeMax: 1,
        stepsActive: 0,
        siteList: [],
        businessSystemList: [],
        businessSubsystemList: [],
        businessAreaList: [],
        imageVersionList: [],
        archList: [],
        modeList: [],
        scaleList: [],
        diskTypeList: [],
        paramList: [],
        paramData: [],
        paramTableData: [],
        backupTypeList: [],
        backupStorageTypeList: [],
        cntFlag: false,
        highAvailableFlag: false,
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
            type: '',
            name: "",
            ownerName: "",
            businessSystem: "",
            businessSubsystem: "",
            site: "",
            businessArea: "",
            architecture: "",
            architectureCode: "",
            version: "",
            unitCnt: 0,
            cpuCnt: 0,
            memSize: 0,
            arch: "",
            mode: '',
            scale: "",
            diskType: "",
            dataSize: 10,
            logSize: 10,
            port: 6379,
            cnt: 1,
            /* hostHA: false,*/
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
        },
        formRules: {
            businessSystem: [{
                required: true,
                message: '请选择所属系统',
                trigger: ['change', 'blur']
            }],
            businessSubsystem: [{
                required: true,
                message: '请选择所属子系统',
                trigger: ['change', 'blur']
            }],
            businessArea: [{
                required: true,
                message: '请选择业务区',
                trigger: ['change', 'blur']
            }],
            version: [{
                required: true,
                message: '请选择版本',
                trigger: ['change', 'blur']
            }],
            scale: [{
                required: true,
                message: '请选择规模',
                trigger: ['change', 'blur']
            }],
            mode: [{
                required: true,
                message: '请选择架构',
                trigger: ['change', 'blur']
            }],
            unitCnt: [{
                required: true,
                message: '请输入副本数量',
                trigger: ['change', 'blur']
            }],
            diskType: [{
                required: true,
                message: '请选择磁盘类型',
                trigger: ['change', 'blur']
            }],
            port: [{
                required: true,
                message: '请输入端口',
                trigger: ['change', 'blur']
            }],
            dataSize: [{
                required: true,
                message: '请输入表空间大小',
                trigger: ['change', 'blur']
            }, {
                validator: checkNum,
                trigger: 'blur'
            }],
            logSize: [{
                required: true,
                message: '请输入日志空间大小',
                trigger: ['change', 'blur']
            }, {
                validator: checkNum,
                trigger: 'blur'
            }],
            backupStorageType: [{
                validator: validateFileRetentionNum,
                message: '请选择备份存储类型',
                trigger: 'blur'
            }],
            backUpType: [{
                validator: validateFileRetentionNum,
                message: '请选择备份类型',
                trigger: 'blur'
            }],
            cronExpression: [{
                validator: validateFileRetentionNum,
                message: '请选择备份周期',
                trigger: 'blur'
            }],
            month: [{
                validator: validateFileRetentionNum,
                message: '请选择日期',
                trigger: 'blur'
            }],
            week: [{
                validator: validateFileRetentionNum,
                message: '请选择周',
                trigger: 'blur'
            }],
            time: [{
                validator: validateFileRetentionNum,
                message: '请选择时间',
                trigger: 'blur'
            }],
            fileRetentionNum: [{
                validator: validateFileRetentionNum,
                trigger: 'blur'
            }]
        },
        addIconClassFun: function ({column, columnIndex}) {
            if (column.property === 'del') {
                return 'addIconClass'
            }
        }
    },
    created: function () {
        this.editCreated()
    },
    methods: {
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/order_groups/" + editId, function (response) {
                var data = response.data.data;
                var ownerName = "";
                if (data.owner !== null) {
                    ownerName = ownerNameDispose(data.owner.name, data.owner.username)
                }

                var site = "";
                if (data.site !== null) {
                    site = data.site.name
                }

                var architecture = "";
                var architectureCode = "";
                if (data.sysArchitecture !== null) {
                    architecture = data.sysArchitecture.display
                    architectureCode = data.sysArchitecture.code
                }

                XEUtils.arrayEach(data.orders, (v, i) => {
                    if (v.type.code === "redis") {

                        var version = "";
                        if (v.version !== null) {
                            version = v.version.major + '.' + v.version.minor + '.' + v.version.patch + '.' + v.version.build
                        }

                        var scale = "";
                        var cpuCnt = 0;
                        var memSize = 0;
                        if (v.scale !== null) {
                            scale = v.scale.name
                            cpuCnt = v.scale.cpuCnt
                            memSize = v.scale.memSize
                        }

                        show = true;
                        XEUtils.arrayEach(_this.scaleList, (vScaleList, iScaleList) => {
                            if (v.scale.name === vScaleList.name) {
                                show = false;
                            }
                        })
                        if (show) {
                            _this.scaleList.push(v.scale)
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
                            _this.paramData = jsonArray
                            _this.paramTableData.push({
                                version: version,
                                paramCfg: jsonArray
                            })
                            _this.stepList.push({
                                name: '参数配置',
                                isShow: true,
                            })
                            _this.advanced = true
                            _this.paramDeploy = true
                            _this.stepPageNums = _this.stepList.length - 1
                            _this.endNum.push(1)
                        }
                        if (v.cfg.backupStrategy !== undefined && v.cfg.backupStrategy !== null) {
                            var backupType = v.cfg.backupStrategy.type
                            var backupStorageType = v.cfg.backupStrategy.backupStorageType

                            show = true;
                            XEUtils.arrayEach(_this.backupTypeList, (vbackupTypeList, ibackupTypeList) => {
                                if (backupType.display === vbackupTypeList.name) {
                                    show = false;
                                }
                            })
                            if (show) {
                                _this.backupTypeList.push(backupType)
                            }

                            show = true;
                            XEUtils.arrayEach(_this.backupStorageTypeList, (vbackupStorageTypeList, ibackupStorageTypeList) => {
                                if (backupStorageType.display === vbackupStorageTypeList.name) {
                                    show = false;
                                }
                            })
                            if (show) {
                                _this.backupStorageTypeList.push(backupStorageType)
                            }
                            _this.formData.backUpType = v.cfg.backupStrategy.type.code
                            _this.formData.backupStorageType = v.cfg.backupStrategy.backupStorageType.code

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
                            _this.endNum.push(2)
                        }
                        let unitCnt = jsonJudgeNotDefined(v, 'v.arch.unitCnt') - 1
                        _this.modeMin = unitCnt
                        _this.modeMax = unitCnt
                        _this.formData = {
                            type: "Redis",
                            nameText: data.name + '    (' + ownerName + ')',
                            name: data.name,
                            ownerName: ownerName,
                            businessSystemId: jsonJudgeNotDefined(data, 'data.businessSubsystem.businessSystem.id'),
                            businessSystemName: jsonJudgeNotDefined(data, 'data.businessSubsystem.businessSystem.name'),
                            businessSubsystemId: jsonJudgeNotDefined(data, 'data.businessSubsystem.id'),
                            businessSubsystemName: jsonJudgeNotDefined(data, 'data.businessSubsystem.name'),
                            businessAreaId: jsonJudgeNotDefined(data, 'data.businessArea.id'),
                            businessAreaName: jsonJudgeNotDefined(data, 'data.businessArea.name'),
                            businessSystem: jsonJudgeNotDefined(data, 'data.businessSubsystem.businessSystem.id'),
                            businessSubsystem: jsonJudgeNotDefined(data, 'data.businessSubsystem.id'),
                            site: site,
                            businessArea: jsonJudgeNotDefined(data, 'data.businessArea.id'),
                            architecture: architecture,
                            architectureCode: architectureCode,
                            version: version,
                            cpuCnt: cpuCnt,
                            memSize: memSize,
                            mode: jsonJudgeNotDefined(v, 'v.arch.mode.code'),
                            unitCnt: unitCnt,
                            scale: jsonJudgeNotDefined(v, 'v.scale.name'),
                            diskType: jsonJudgeNotDefined(v, 'v.diskType.code'),
                            dataSize: v.dataSize,
                            logSize: v.logSize,
                            port: v.port,
                            paramCfg: jsonArray,
                            cnt: v.cnt,
                            msg: data.msg,
                            backupStorageType: _this.formData.backupStorageType,
                            backUpType: _this.formData.backUpType,
                            cronExpression: _this.formData.cronExpression,
                            month: _this.formData.month,
                            week: _this.formData.week,
                            time: _this.formData.time,
                            fileRetentionNum: _this.formData.fileRetentionNum,
                            description: _this.formData.description,
                            highAvailable: data.highAvailable
                        }
                        _this.highAvailableFlag = data.highAvailable
                        if (v.cnt > 1)
                            _this.cntFlag = true
                    }
                })
                _this.endFun()
                _this.businessAreaListView()
                _this.businessSystemListView()
            }, function (error) {
                console.log(error)
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        businessAreaListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/business_areas?site_id=" + getSession("siteId") + "&enabled=true", function (response) {
                _this.businessAreaList = response.data.data
                var businessAreaId = _this.formData.businessAreaId
                var businessAreaName = _this.formData.businessAreaName
                var exists = XEUtils.findIndexOf(_this.businessAreaList, item => item.id === businessAreaId)
                if (exists === -1) {
                    var list = {
                        id: businessAreaId,
                        name: businessAreaName
                    }
                    _this.businessAreaList.push(list)
                }
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        businessSystemListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/business_systems?enabled=true", function (response) {
                _this.businessSystemList = response.data.data
                var businessSystemId = _this.formData.businessSystemId
                var businessSystemName = _this.formData.businessSystemName
                var exists = XEUtils.findIndexOf(_this.businessSystemList, item => item.id === businessSystemId)
                if (exists === -1) {
                    var list = {
                        id: businessSystemId,
                        name: businessSystemName
                    }
                    _this.businessSystemList.push(list)
                }
                _this.endFun()
                _this.businessSubsystemListView(true);
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        businessSubsystemListView: function (createdVal) {
            var _this = this
            sendGet("/" + getProjectName() + "/business_subsystems?business_system_id=" + _this.formData.businessSystem + "&enabled=true", function (response) {
                _this.businessSubsystemList = response.data.data
                if (createdVal) {
                    var businessSubsystemId = _this.formData.businessSubsystemId
                    var businessSubsystemName = _this.formData.businessSubsystemName
                    var exists = XEUtils.findIndexOf(_this.businessSubsystemList, item => item.id === businessSubsystemId)
                    if (exists === -1) {
                        var list = {
                            id: businessSubsystemId,
                            name: businessSubsystemName
                        }
                        _this.businessSubsystemList.push(list)
                    }
                    _this.endFun()
                } else {
                    layer.closeAll('loading')
                }
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        systemChange: function (val) {
            this.formData.businessSubsystem = ""
            this.businessSubsystemListView(false)
        },

        imageVersionListView: function (val) {
            var _this = this
            sendGet("/" + getProjectName() + "/images?site_id=" + getSession("siteId") + "&architecture=" + _this.formData.architectureCode + "&type=redis&enabled=true", function (response) {
                var json = []
                XEUtils.arrayEach(response.data.data, (v, i) => {
                    json.push({
                        id: v.id,
                        version: v.version.major + '.' + v.version.minor + '.' + v.version.patch + '.' + v.version.build
                    })
                })
                _this.imageVersionList = json
                var show = true;
                XEUtils.arrayEach(_this.imageVersionList, (v, i) => {
                    if (_this.formData.version === v.version) {
                        show = false;
                    }
                })
                if (show) {
                    _this.imageVersionList.push(_this.formData.version)
                }
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        versionChange: function () {
            this.initialization = 3
            this.archListView()
            this.paramData = []
        },
        archListView: function () {
            var _this = this
            var version = _this.formData.version.split(".")
            var sendVersionData = version[0] + "." + version[1]
            sendGet("/" + getProjectName() + "/archs?type=redis&version=" + sendVersionData, function (response) {
                _this.archList = response.data.data
                var modeArray = []
                XEUtils.arrayEach(response.data.data, (v, i) => {
                    if (i === 0)
                        modeArray.push(v.mode)
                    if (modeArray[modeArray.length - 1].code !== v.mode.code)
                        modeArray.push(v.mode)
                })
                _this.modeList = XEUtils.uniq(modeArray)
                _this.archChange(_this.formData.mode, _this.formData.unitCnt)
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        scaleListView: function (val) {
            var _this = this
            sendGet("/" + getProjectName() + "/scales?type=redis&enabled=true", function (response) {
                _this.scaleList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        diskTypeListView: function (val) {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=disk_type", function (response) {
                _this.diskTypeList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        archChange: function (value, num) {
            if (value === 'single') {
                this.formData.unitCnt = 0
                this.cntFlag = false
                this.formData.highAvailable = false
                /*this.cntFlagChange(true)
                this.highAvailableChange(true)*/
            } else {
                let tempArray = XEUtils.filter(this.archList, item => item.mode.code === value)
                let tempMin = XEUtils.min(tempArray, item => item.unitCnt).unitCnt - 1
                let tempMax = XEUtils.max(tempArray, item => item.unitCnt).unitCnt - 1
                this.modeMin = parseInt(tempMin)
                this.modeMax = parseInt(tempMax)
                if (XEUtils.isUndefined(num))
                    this.formData.unitCnt = parseInt(tempMin)
            }
        },
        scaleChange: function (val) {
            var cpucnt = this.$refs.scaleRadios.hoverOption.$el.dataset.cpucnt
            var memsize = this.$refs.scaleRadios.hoverOption.$el.dataset.memsize
            this.formData.cpuCnt = parseFloat(cpucnt);
            this.formData.memSize = parseFloat(memsize);
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
        advancedChange: function () {
            //如果关闭高级选项，高级选项相关属性进行重置
            //关闭高级选项，默认用户取消参数配置和备份策略
            if (!this.advanced) {
                this.stepList.splice(2)
                this.stepPageNum = -10
                this.stepPageNums = -10
                this.backupPolicy = false
                this.paramDeploy = false
            }
        },
        paramDeployChange: function () {
            var _this = this
            if (this.paramDeploy) {
                this.stepList.push({
                    name: '参数配置',
                    isShow: true
                })
                this.endNum.push(1)
                this.stepPageNums = this.stepList.length - 1
            } else {
                XEUtils.arrayEach(this.stepList, (item, key) => {
                    if (item.name === '参数配置') {
                        _this.stepList.splice(key, 1)
                        _this.endNum.splice(key, 1)
                    }
                })
                this.stepPageNum = this.stepList.length - 1
                this.stepPageNums = -10
            }
        },
        backupPolicyChange: function () {
            var _this = this
            if (this.backupPolicy) {
                this.stepList.push({
                    name: '备份策略',
                    isShow: true
                })
                this.endNum.push(2)
                this.stepPageNum = this.stepList.length - 1
            } else {
                XEUtils.arrayEach(this.stepList, (item, key) => {
                    if (item.name === '备份策略') {
                        _this.stepList.splice(key, 1)
                        _this.endNum.splice(key, 1)
                    }
                })
                this.stepPageNums = this.stepList.length - 1
                this.stepPageNum = -10
            }
        },

        procImageVersionChanged: function () {
            var _this = this
            const version = this.formData.version.split(".")
            sendGet("/" + getProjectName() + "/images/redis/templates?site_id=" + getSession('siteId') + "&major=" + version[0] + "&minor=" + version[1] + "&patch=" + version[2] + "&build=" + version[3] + "&architecture=" + _this.formData.architectureCode, function (response) {
                _this.paramList = response.data.data
                _this.$refs.paramTable.reloadData(_this.paramData)
                _this.endFun()
            }, function (error) {
                console.log(error)
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        paramChange: function (row) {
            var _this = this
            var paramData = this.paramList
            for (var i = 0; i < paramData.length; i++) {
                if (paramData[i].key === row.key) {
                    _this.paramData[row.num].value = paramData[i].value
                    break;
                }
            }
        },
        addParamRow: function () {
            var paramData = this.paramList
            var paramLength = paramData.length
            var paramTableData = this.paramData
            var paramTableLength = paramTableData.length
            this.paramData[paramTableLength] = {
                key: paramData[paramTableLength].key,
                value: paramData[paramTableLength].value,
                del: "-",
                num: paramTableLength
            }
            this.$refs.paramTable.reloadData(this.paramData)
        },
        delParamRow: function ({column, row}) {
            var paramTableData = this.paramData
            if (column.title === "+") {
                this.paramData.splice(row.num, 1)
                this.$refs.paramTable.reloadData(this.paramData)
            }
        },
        showKey: function (val) {
            var _this = this;
            var temporaryData = [];
            XEUtils.arrayEach(val, (v, i) => {
                var showData = true;
                XEUtils.arrayEach(_this.paramData, (value, index) => {
                    if (value.key === v.key) {
                        showData = false
                    }
                })
                if (showData) {
                    temporaryData.push(v)
                }
            })
            return temporaryData
        },

        backupTypeListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=backup_type", function (response) {
                _this.backupTypeList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        backupStorageTypeListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=backup_storage_type", function (response) {
                _this.backupStorageTypeList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },

        endFun: function () {
            this.initialization++
            if (this.initialization === this.endNum[this.stepsActive]) {
                layer.closeAll('loading')
                this.initialization = 0
            }
        },

        verifyChange: function () {
            var verify = false;
            this.$refs['editNew'].validate((valid) => {
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
        },
        formSubmit: function (formName, type) { //editNew
            var verify = false;
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

            var _this = this
            const area = this.formData.businessArea
            const subSystem = this.formData.businessSubsystem
            const servName = this.formData.name
            const architectureCode = this.formData.architectureCode
            const highAvailable = this.formData.highAvailable
            const majorVersion = this.formData.version.split(".")[0]
            const minorVersion = this.formData.version.split(".")[1]
            const patchVersion = this.formData.version.split(".")[2]
            const buildVersion = this.formData.version.split(".")[3]
            const mode = this.formData.mode;
            const unitCnt = this.formData.unitCnt + 1
            const cpuCnt = this.formData.cpuCnt
            const memSize = this.formData.memSize
            const diskType = this.formData.diskType
            const dataDirSize = this.formData.dataSize
            const logDirSize = this.formData.logSize
            const port = this.formData.port

            let cnt = this.formData.cnt
            if (!this.cntFlag)
                cnt = 1

            var backupStorageType = this.formData.backupStorageType
            var backUpType = this.formData.backUpType
            var cronExpressionType = this.formData.cronExpression
            var month = this.formData.month
            var week = this.formData.week
            var time = this.formData.time
            var cronExpression = ""
            switch (cronExpressionType) {
                case 'day':
                    cronExpression = time + " * * *"
                    break;
                case 'week':
                    cronExpression = time + " * * " + week
                    break;
                case 'month':
                    cronExpression = time + " " + month + " * *"
                    break;
            }
            var fileRetentionNum = this.formData.fileRetentionNum
            var description = this.formData.description
            if (_this.paramData.length != 0) {
                var paramCfg = {}
                XEUtils.arrayEach(_this.paramData, (v, i) => {
                    paramCfg[v.key] = v.value
                })
            }
            var cfg = ''
            if (this.advanced) {
                cfg = {}
                if (this.backupPolicy && backupStorageType !== "") {
                    cfg.backupStrategy = {
                        "backupStorageType": backupStorageType,
                        "type": backUpType,
                        "cronExpression": cronExpression,
                        "fileRetentionNum": parseInt(fileRetentionNum),
                        "description": description
                    }
                }
                if (this.paramDeploy && _this.paramData.length != 0)
                    cfg.param = paramCfg

                if (XEUtils.isEmpty(cfg))
                    cfg = ''
            }

            var jsonData = {
                "businessAreaId": area,
                "businessSubsystemId": subSystem,
                "sysArchitecture": architectureCode,
                "name": servName,
                "highAvailable": highAvailable,
                "orders": [{
                    "type": "redis",
                    "version": {
                        "major": parseInt(majorVersion),
                        "minor": parseInt(minorVersion),
                        "patch": parseInt(patchVersion),
                        "build": parseInt(buildVersion)
                    },
                    "archMode": mode,
                    "unitCnt": parseInt(unitCnt),
                    "cpuCnt": parseFloat(cpuCnt),
                    "memSize": parseFloat(memSize),
                    "diskType": diskType,
                    "dataSize": dataDirSize,
                    "logSize": logDirSize,
                    "port": parseInt(port),
                    "cfg": cfg,
                    "cnt": parseInt(cnt)
                }]
            }
            commonConfirm(this, '编辑确认', getHintText('编辑')).then(() => {
                if (_this.highAvailableFlag === highAvailable) {
                    _this.sendTableAjax(jsonData)
                } else {
                    _this.rebuildOrderAjax(jsonData)
                }
            }).catch(() => {
            })
        },

        sendTableAjax: function (data) {
            var _this = this
            sendPut("/" + getProjectName() + "/order_groups/" + editId, function (response) {
                _this.successFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, data)
        },
        rebuildOrderAjax: function (data) {
            var _this = this

            // 先进行删除工单
            sendDelete("/" + getProjectName() + "/order_groups/" + editId, function (response) {
                //删除成功后，添加提交信息并新建新工单
                data.category = "redis"
                sendPost("/" + getProjectName() + "/order_groups", function (response) {
                    _this.successFun()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, data)
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        successFun: function () {
            layer.closeAll('loading')
            operationCompletion(parent.listApp, "操作成功！")
            parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
            if (this.ogAutoExamine && this.ogAutoExecute) {
                parent.listApp.goTourlDialogShow = true
            } else {
                parent.listApp.returnList()
            }
            this.formClose()
        },

        stepsActiveAdd: function (formName) {
            var _this = this
            var verify = false;
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
            this.stepsActive++
            switch (this.stepsActive) {
                case 1:
                    this.imageVersionListView()
                    this.archListView()
                    this.scaleListView()
                    this.diskTypeListView()
                    break;
                case 2:
                    if (this.stepList[2].name === "备份策略") {
                        this.backupTypeListView()
                        this.backupStorageTypeListView()
                    } else {
                        this.procImageVersionChanged()
                    }
                    break;
                case 3:
                    if (this.stepList[3].name === "备份策略") {
                        this.backupTypeListView()
                        this.backupStorageTypeListView()
                    } else {
                        this.procImageVersionChanged()
                    }
                    break;
            }
        },
        stepsActiveDel: function (formName) {
            this.stepsActive--
            this.$refs[formName].clearValidate();
            var _this = this
            var falg = true
            XEUtils.arrayEach(this.paramTableData, (v, i) => {
                if (_this.formData.version === v.version) {
                    falg = false
                    _this.paramTableData.paramCfg = _this.paramData
                }
            })
            if (falg) {
                this.paramTableData.push({
                    version: _this.formData.version,
                    paramCfg: _this.paramData
                })
            }
        },
        formClose: function () {
            parent.layer.close(editNewIndex);
        }
    }
})