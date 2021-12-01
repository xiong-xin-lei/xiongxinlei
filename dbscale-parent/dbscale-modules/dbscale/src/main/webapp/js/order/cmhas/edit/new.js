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
    switch (cmhaEditApp.formData.cronExpression) {
        case 'day':
            timeType = cmhaEditApp.formData.time !== null
            break;
        case 'week':
            timeType = cmhaEditApp.formData.time !== null && cmhaEditApp.formData.week !== null
            break;
        case 'month':
            timeType = cmhaEditApp.formData.time !== null && cmhaEditApp.formData.month !== null
            break;
    }
    var All = cmhaEditApp.formData.backupStorageType === '' && cmhaEditApp.formData.backUpType === '' &&
        cmhaEditApp.formData.cronExpression === '' && cmhaEditApp.formData.month === null &&
        cmhaEditApp.formData.week === null && cmhaEditApp.formData.time === null &&
        cmhaEditApp.formData.fileRetentionNum === undefined

    var FullAll = cmhaEditApp.formData.backupStorageType !== '' && cmhaEditApp.formData.backUpType !== '' &&
        cmhaEditApp.formData.cronExpression !== '' && cmhaEditApp.formData.fileRetentionNum !== undefined && timeType


    if (All) {
        return true
    } else if (FullAll) {
        return true
    } else {
        switch (cmhaEditApp.formData.cronExpression) {
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
var cmhaEditApp = new Vue({
    el: '#editNew',
    data: {
        ogAutoExamine: ogAutoExamine,
        ogAutoExecute: ogAutoExecute,
        initialization: 0,
        endNum: [4, 6],
        modeMin: 1,
        modeMax: 1,
        stepsActive: 0,
        businessSystemList: [],
        businessSubsystemList: [],
        businessAreaList: [],
        archList: [],
        modeList: [],
        mysqlimageVersionList: [],
        mysqlarchList: [],
        mysqlscaleList: [],
        mysqldiskTypeList: [],
        mysqlparamList: [],
        mysqlparamData: [],
        mysqlparamTableData: [],
        proxyArchList: [],
        proxyVersionData: {},
        proxyMinNum: null,
        proxyMaxNum: null,
        backupTypeList: [],
        backupStorageTypeList: [],
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
            type: '',
            name: "",
            ownerName: "",
            businessSystem: "",
            businessSubsystem: "",
            site: "",
            businessArea: "",
            sysArchitectureCode: "",
            sysArchitectureName: "",
            mysqlversion: "",
            mysqlversionId: "",
            mysqlarchMode: "",
            mysqlunitCnt: 0,
            mysqlcpuCnt: 0,
            mysqlmemSize: 0,
            mysqlscale: "",
            mysqldiskType: "",
            mysqldataSize: 10,
            mysqllogSize: 10,
            //proxyNum: null,
            highAvailable: false,
            proxyNum: 0,
            mysqlport: 3306,
            mysqlparamCfg: [],
            mysqlcnt: 0,
            mysqlmsg: "",
            backupStorageType: '',
            backUpType: '',
            cronExpression: '',
            month: null,
            week: null,
            time: null,
            fileRetentionNum: undefined,
            description: ''
        },
        formRules: {
            businessSystem: [
                {required: true, message: '请选择所属系统', trigger: ['change', 'blur']}
            ],
            businessSubsystem: [
                {required: true, message: '请选择所属子系统', trigger: ['change', 'blur']}
            ],
            businessArea: [
                {required: true, message: '请选择业务区', trigger: ['change', 'blur']}
            ],
            mysqlversion: [
                {required: true, message: '请选择版本', trigger: ['change', 'blur']}
            ],
            mysqlscale: [
                {required: true, message: '请选择规模', trigger: ['change', 'blur']}
            ],
            mysqlarchMode: [
                {required: true, message: '请选择架构', trigger: ['change', 'blur']}
            ],
            mysqlunitCnt: [
                {required: true, message: '请输入副本数量', trigger: ['change', 'blur']}
            ],
            mysqldiskType: [
                {required: true, message: '请选择磁盘类型', trigger: ['change', 'blur']}
            ],
            mysqlport: [
                {required: true, message: '请输入端口', trigger: ['change', 'blur']}
            ],
            mysqldataSize: [
                {required: true, message: '请输入表空间大小', trigger: ['change', 'blur']},
                {validator: checkNum, trigger: 'blur'}
            ],
            mysqllogSize: [
                {required: true, message: '请输入日志空间大小', trigger: ['change', 'blur']},
                {validator: checkNum, trigger: 'blur'}
            ],
            backupStorageType: [
                {validator: validateFileRetentionNum, message: '请选择备份存储类型', trigger: 'blur'}
            ],
            backUpType: [
                {validator: validateFileRetentionNum, message: '请选择备份类型', trigger: 'blur'}
            ],
            cronExpression: [
                {validator: validateFileRetentionNum, message: '请选择备份周期', trigger: 'blur'}
            ],
            month: [
                {validator: validateFileRetentionNum, message: '请选择日期', trigger: 'blur'}
            ],
            week: [
                {validator: validateFileRetentionNum, message: '请选择周', trigger: 'blur'}
            ],
            time: [
                {validator: validateFileRetentionNum, message: '请选择时间', trigger: 'blur'}
            ],
            fileRetentionNum: [
                {validator: validateFileRetentionNum, trigger: 'blur'}
            ]
        },
        addIconClassFun: function ({column, columnIndex}) {
            if (column.property === 'del') {
                return 'addIconClass'
            }
        }
    },
    created: function () {
        this.editCreated();
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

                var sysArchitectureName = "";
                var sysArchitectureCode = "";
                if (data.sysArchitecture !== null) {
                    sysArchitectureName = data.sysArchitecture.display
                    sysArchitectureCode = data.sysArchitecture.code
                }

                var site = "";
                if (data.site !== null) {
                    site = data.site.name
                }

                var proxyNum = 0

                XEUtils.arrayEach(data.orders, (v, i) => {
                    if (v.type.code === "mysql") {

                        var version = "";
                        var versionId = "";
                        if (v.version !== null) {
                            version = v.version.major + '.' + v.version.minor + '.' + v.version.patch + '.' + v.version.build
                        }

                        show = true;
                        XEUtils.arrayEach(_this.mysqlimageVersionList, (v, i) => {
                            if (version === v.version) {
                                versionId = v.id
                                show = false;
                            }
                        })
                        if (show) {
                            _this.mysqlimageVersionList.push(version)
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
                        XEUtils.arrayEach(_this.mysqlscaleList, (vScaleList, iScaleList) => {
                            if (v.scale.name === vScaleList.name) {
                                show = false;
                            }
                        })
                        if (show) {
                            _this.mysqlscaleList.push(v.scale)
                        }

                        var diskType = "";
                        if (v.diskType !== null) {
                            diskType = v.diskType.code
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
                                }
                                jsonIndex++
                                jsonArray.push(o)
                            }
                            _this.mysqlparamData = jsonArray
                            _this.mysqlparamTableData.push({
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
                        _this.mysqlformData = {
                            mysqlversion: version,
                            mysqlversionId: versionId,
                            mysqlarchMode: jsonJudgeNotDefined(v, 'v.arch.mode.code'),
                            mysqlunitCnt: unitCnt,
                            mysqlcpuCnt: cpuCnt,
                            mysqlmemSize: memSize,
                            mysqlscale: scale,
                            mysqldiskType: diskType,
                            mysqldataSize: v.dataSize,
                            mysqllogSize: v.logSize,
                            mysqlport: v.port,
                            mysqlparamCfg: jsonArray,
                            mysqlcnt: v.cnt,
                            mysqlmsg: ""
                        }
                    } else if (v.type.code === "proxysql") {
                        if (v.arch !== null) {
                            proxyNum = v.arch.unitCnt
                            _this.proxyMinNum = proxyNum
                            _this.proxyMaxNum = proxyNum
                        }
                    }
                })
                _this.formData = {
                    type: "CMHA",
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
                    sysArchitectureCode: sysArchitectureCode,
                    sysArchitectureName: sysArchitectureName,

                    mysqlversion: _this.mysqlformData.mysqlversion,
                    mysqlversionId: _this.mysqlformData.mysqlversionId,
                    mysqlarchMode: _this.mysqlformData.mysqlarchMode,
                    mysqlunitCnt: _this.mysqlformData.mysqlunitCnt,
                    mysqlcpuCnt: _this.mysqlformData.mysqlcpuCnt,
                    mysqlmemSize: _this.mysqlformData.mysqlmemSize,
                    mysqlscale: _this.mysqlformData.mysqlscale,
                    mysqldiskType: _this.mysqlformData.mysqldiskType,
                    mysqldataSize: _this.mysqlformData.mysqldataSize,
                    mysqllogSize: _this.mysqlformData.mysqllogSize,
                    highAvailable: data.highAvailable,
                    proxyNum: proxyNum,
                    mysqlport: _this.mysqlformData.mysqlport,
                    mysqlparamCfg: _this.mysqlformData.mysqlparamCfg,
                    mysqlcnt: _this.mysqlformData.mysqlcnt,
                    mysqlmsg: _this.mysqlformData.mysqlmsg,

                    backupStorageType: _this.formData.backupStorageType,
                    backUpType: _this.formData.backUpType,
                    cronExpression: _this.formData.cronExpression,
                    month: _this.formData.month,
                    week: _this.formData.week,
                    time: _this.formData.time,
                    fileRetentionNum: _this.formData.fileRetentionNum,
                    description: _this.formData.description
                }
                _this.highAvailableFlag = data.highAvailable
                
                _this.businessAreaListView()
                _this.businessSystemListView()
                _this.endFun()
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
                _this.businessSubsystemListView(true);
                _this.endFun()
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
        systemChange: function () {
            this.formData.businessSubsystem = ""
            this.businessSubsystemListView()
        },

        mysqlImageVersionListView: function (type) {
            var _this = this
            sendGet("/" + getProjectName() + "/images?site_id=" + getSession("siteId") + "&type=" + type + "&enabled=true" + "&architecture=" + _this.formData.sysArchitectureCode, function (response) {
                var array = []
                var onceData = {}
                var data = response.data.data
                XEUtils.arrayEach(data, (v, i) => {
                    if (v.type.code === type) {
                        array.push({
                            id: v.id,
                            version: v.version.major + '.' + v.version.minor + '.' + v.version.patch + '.' + v.version.build
                        })
                        if (i === 0) {
                            onceData = v
                        }
                    }

                    if (type === "mysql") {
                        _this.imageVersionList = array
                        _this.mysqlimageVersionList = array
                    } else if (type === "proxysql") {
                        if (XEUtils.isEmpty(array)) {
                        } else {
                            var proxyNewVersionData = onceData.version
                            _this.proxyVersionData = proxyNewVersionData
                            var sendVersionData = proxyNewVersionData.major + "." + proxyNewVersionData.minor
                            _this.mysqlArchListView(type, sendVersionData)
                        }
                    }
                })
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        versionChange: function () {
            this.initialization = 5
            var version = this.formData.mysqlversion.split(".")
            var sendVersionData = version[0] + "." + version[1]
            this.mysqlArchListView("mysql", sendVersionData)
            this.mysqlparamData = []
        },
        mysqlArchListView: function (type, sendVersionData) {
            var _this = this
            sendGet("/" + getProjectName() + "/archs?type=" + type + "&version=" + sendVersionData, function (response) {
                var data = response.data.data

                var array = []
                var modeArray = []
                if (type === "mysql") {
                    XEUtils.arrayEach(data, (v, i) => {
                        if (v.type.code === type && v.unitCnt !== 1) {
                            array.push(v)
                            if (modeArray.length === 0)
                                modeArray.push(v.mode)
                            if (modeArray[modeArray.length - 1].code !== v.mode.code)
                                modeArray.push(v.mode)
                        }
                    })
                    _this.archList = array
                    _this.modeList = XEUtils.uniq(modeArray)
                    _this.archChange(_this.formData.mysqlarchMode, _this.formData.mysqlunitCnt)
                } else if (type === "proxysql") {
                    XEUtils.arrayEach(data, (v, i) => {
                        if (v.type.code === type) {
                            array.push(v)
                        }
                    })
                    _this.proxyArchList = array.concat();
                    if (XEUtils.isEmpty(array)) {
                        _this.formData.proxyNum = _this.proxyMinNum
                    } else {
                        array.push(_this.formData.proxyNum)
                        _this.proxyMinNum = XEUtils.min(array, 'unitCnt').unitCnt
                        _this.proxyMaxNum = XEUtils.max(array, 'unitCnt').unitCnt
                    }
                }
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        archChange: function (value, num) {
            if (value === 'single') {
                this.formData.unitCnt = 0
            } else {
                let tempArray = XEUtils.filter(this.archList, item => item.mode.code === value)
                let tempMin = XEUtils.min(tempArray, item => item.unitCnt).unitCnt - 1
                let tempMax = XEUtils.max(tempArray, item => item.unitCnt).unitCnt - 1
                this.modeMin = parseInt(tempMin)
                this.modeMax = parseInt(tempMax)
                if (XEUtils.isUndefined(num))
                    this.formData.mysqlunitCnt = parseInt(tempMin)
            }
        },
        mysqlScaleListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/scales?type=mysql&enabled=true", function (response) {
                _this.mysqlscaleList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        mysqlscaleChange: function (val) {
            var cpucnt = this.$refs.mysqlscaleRadios.hoverOption.$el.dataset.cpucnt
            var memsize = this.$refs.mysqlscaleRadios.hoverOption.$el.dataset.memsize
            this.formData.mysqlcpuCnt = parseFloat(cpucnt);
            this.formData.mysqlmemSize = parseFloat(memsize);
        },
        diskTypeListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=disk_type", function (response) {
                _this.mysqldiskTypeList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
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

        mysqlprocImageVersionChanged: function () {
            var _this = this
            let version = this.formData.mysqlversion.split(".")
            sendGet("/" + getProjectName() + "/images/mysql/templates?site_id=" + getSession('siteId') + "&major=" + version[0] + "&minor=" + version[1] + "&patch=" + version[2] + "&build=" + version[3] + "&architecture=" + _this.formData.sysArchitectureCode, function (response) {
                _this.mysqlparamList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        mysqlparamChange: function (row) {
            var _this = this
            var mysqlparamData = this.mysqlparamList
            var mysqlparamLength = mysqlparamData.length
            for (var i = 0; i < mysqlparamLength; i++) {
                if (mysqlparamData[i].key === row.key) {
                    _this.mysqlparamData[row.num].value = mysqlparamData[i].value
                    break;
                }
            }
        },
        mysqladdParamRow: function () {
            var _this = this;
            var mysqlparamData = this.mysqlparamList
            var mysqlparamLength = mysqlparamData.length
            var mysqlparamTableData = this.mysqlparamData
            var mysqlparamTableLength = mysqlparamTableData.length
            this.mysqlparamData[mysqlparamTableLength] = {
                key: mysqlparamData[mysqlparamTableLength].key,
                value: mysqlparamData[mysqlparamTableLength].value,
                del: "-",
                num: mysqlparamTableLength
            }
            this.$refs.mysqlparamTable.reloadData(this.mysqlparamData)
        },
        mysqldelParamRow: function ({column, row}) {
            var mysqlparamTableData = this.mysqlparamData
            if (column.title === "+") {
                this.mysqlparamData.splice(row.num, 1)
                this.$refs.mysqlparamTable.reloadData(this.mysqlparamData)
            }
        },
        mysqlshowKey: function (val) {
            var _this = this;
            var temporaryData = [];
            XEUtils.arrayEach(val, (v, i) => {
                var showData = true;
                XEUtils.arrayEach(_this.mysqlparamData, (value, index) => {
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
        formSubmit: function (formName, type) {//editNew
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

            var _this = this
            const area = this.formData.businessArea
            const subSystem = this.formData.businessSubsystem
            const servName = this.formData.name
            const sysArchitectureName = this.formData.sysArchitectureName
            const sysArchitectureCode = this.formData.sysArchitectureCode

            const mysqlmajorVersion = this.formData.mysqlversion.split(".")[0]
            const mysqlminorVersion = this.formData.mysqlversion.split(".")[1]
            const mysqlpatchVersion = this.formData.mysqlversion.split(".")[2]
            const mysqlbuildVersion = this.formData.mysqlversion.split(".")[3]
            const mysqlmode = this.formData.mysqlarchMode
            const mysqlunitCnt = this.formData.mysqlunitCnt + 1
            const mysqlcpuCnt = this.formData.mysqlcpuCnt
            const mysqlmemSize = this.formData.mysqlmemSize
            const mysqldiskType = this.formData.mysqldiskType
            const mysqldataDirSize = this.formData.mysqldataSize
            const mysqllogDirSize = this.formData.mysqllogSize
            const highAvailable = this.formData.highAvailable
            const mysqlport = this.formData.mysqlport
            const mysqlcnt = this.formData.mysqlcnt

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

            if (_this.mysqlparamData.length != 0) {
                var mysqlparamCfg = {};
                XEUtils.arrayEach(_this.mysqlparamData, (v, i) => {
                    mysqlparamCfg[v.key] = v.value
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
                if (this.paramDeploy && _this.mysqlparamData.length != 0)
                    cfg.param = mysqlparamCfg

                if (XEUtils.isEmpty(cfg))
                    cfg = ''
            }

            const jsonData = {
                "businessAreaId": area,
                "businessSubsystemId": subSystem,
                "name": servName,
                "sysArchitecture": sysArchitectureCode,
                "highAvailable": highAvailable,
                "orders": [
                    {
                        "type": "mysql",
                        "version": {
                            "major": parseInt(mysqlmajorVersion),
                            "minor": parseInt(mysqlminorVersion),
                            "patch": parseInt(mysqlpatchVersion),
                            "build": parseInt(mysqlbuildVersion)
                        },
                        "archMode": mysqlmode,
                        "unitCnt": parseInt(mysqlunitCnt),
                        "cpuCnt": parseFloat(mysqlcpuCnt),
                        "memSize": parseFloat(mysqlmemSize),
                        "diskType": mysqldiskType,
                        "dataSize": mysqldataDirSize,
                        "logSize": mysqllogDirSize,
                        "port": parseInt(mysqlport),
                        "cfg": cfg,
                        "cnt": mysqlcnt
                    }
                ]
            }

            var proxyVersion = _this.proxyVersionData
            var proxyJson = {
                "type": "proxysql",
                "version": proxyVersion
            }
            var proxyArchMode = ""
            var proxyNum = _this.formData.proxyNum
            for (var i = 0; i < _this.proxyArchList.length; i++) {
                if (_this.proxyArchList[i].unitCnt === proxyNum) {
                    proxyArchMode = _this.proxyArchList[i].mode.code
                }
            }
            if (!XEUtils.isEmpty(proxyArchMode)) {
                proxyJson.archMode = proxyArchMode
                proxyJson.unitCnt = parseInt(proxyNum)
            }
            jsonData.orders.push(proxyJson)

           /* commonConfirm(this, '编辑确认', getHintText('编辑')).then(() => {
                _this.sendTableAjax(jsonData)
            }).catch(() => {
            })
        },
        sendTableAjax: function (data) {
            var _this = this
            sendPut("/" + getProjectName() + "/order_groups/" + editId, function (response) {
                layer.closeAll('loading')
                operationCompletion(parent.listApp, "操作成功！")
                parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                if (this.ogAutoExamine && this.ogAutoExecute) {
                    parent.listApp.goTourlDialogShow = true
                } else {
                    parent.listApp.returnList()
                }
                _this.formClose()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, data)
        },*/

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
                data.category = "cmha"
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
                case 1 :
                    this.mysqlImageVersionListView("mysql")
                    var version = this.formData.mysqlversion.split(".")
                    var sendVersionData = version[0] + "." + version[1]
                    this.mysqlArchListView("mysql", sendVersionData)
                    this.mysqlScaleListView()
                    this.diskTypeListView()
                    this.mysqlImageVersionListView('proxysql')
                    break;
                case 2:
                    if (this.stepList[2].name === "备份策略") {
                        this.backupTypeListView()
                        this.backupStorageTypeListView()
                    } else {
                        this.mysqlprocImageVersionChanged()
                    }
                    break;
                case 3:
                    if (this.stepList[3].name === "备份策略") {
                        this.backupTypeListView()
                        this.backupStorageTypeListView()
                    } else {
                        this.mysqlprocImageVersionChanged()
                    }
                    break;
            }
        },
        stepsActiveDel: function (formName) {
            this.stepsActive--
            this.$refs[formName].clearValidate();
        },

        formClose: function () {
            parent.layer.close(editNewIndex);
        }
    }
})