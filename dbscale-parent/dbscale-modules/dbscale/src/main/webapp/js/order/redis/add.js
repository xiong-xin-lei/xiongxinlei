var setupIndex = parent.layer.getFrameIndex(window.name);
var ogAutoExamine = getQueryVariable("ogAutoExamine") === "true"
var ogAutoExecute = getQueryVariable("ogAutoExecute") === "true"
var validateFileRetentionNum = (rule, value, callback) => {
    var timeType = ""
    switch (redisApp.formData.cronExpression) {
        case 'day':
            timeType = redisApp.formData.time !== null
            break;
        case 'week':
            timeType = redisApp.formData.time !== null && redisApp.formData.week !== null
            break;
        case 'month':
            timeType = redisApp.formData.time !== null && redisApp.formData.month !== null
            break;
    }
    var All = redisApp.formData.backupStorageType === '' && redisApp.formData.backUpType === '' &&
        redisApp.formData.cronExpression === '' && redisApp.formData.month === null &&
        redisApp.formData.week === null && redisApp.formData.time === null &&
        redisApp.formData.fileRetentionNum === undefined

    var FullAll = redisApp.formData.backupStorageType !== '' && redisApp.formData.backUpType !== '' &&
        redisApp.formData.cronExpression !== '' && redisApp.formData.fileRetentionNum !== undefined && timeType

    var msg;
    switch (rule.field) {
        case "backupStorageType":
            msg = '请选择备份存储类型'
            break;
        case "backUpType":
            msg = '请选择备份类型'
            break;
        case "cronExpression":
            msg = '请选择备份周期'
            break;
        case "month":
            msg = '请选择日期'
            break;
        case "week":
            msg = '请选择周'
            break;
        case "time":
            msg = '请选择时间'
            break;
        case "fileRetentionNum":
            switch (redisApp.formData.cronExpression) {
                case 'day':
                    msg = '请输入文件保留天数'
                    break;
                case 'week':
                    msg = '请输入文件保留周数'
                    break;
                case 'month':
                    msg = '请输入文件保留月数'
                    break;
                default:
                    msg = '请输入文件保留时间'
            }
    }

    if (All) {
        callback();
    } else if (FullAll) {
        callback();
    } else {
        if (XEUtils.isNumber(value)) {
            value += ''
        }
        if (!XEUtils.isEmpty(value)) {
            callback();
        }
        callback(new Error(msg));
    }
};
var redisApp = new Vue({
    el: '#redis',
    data: {
        ogAutoExamine: ogAutoExamine,
        ogAutoExecute: ogAutoExecute,
        initialization: 0,
        endNum: [3, 5],
        modeMin: 1,
        modeMax: 1,
        stepsActive: 0,
        systemList: [],
        subSystemList: [],
        areaList: [],
        architectureList: [],
        imageVersionList: [],
        archList: [],
        modeList: [],
        scaleList: [],
        diskTypeList: [],
        paramList: [],
        paramData: [],
        backupTypeList: [],
        backupStorageTypeList: [],
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
        formData: {
            type: 'Redis',
            siteName: '',
            system: '',
            subSystem: '',
            servName: '',
            area: '',
            architecture: '',
            imageVersion: '',
            port: 6379,
            cnt: 1,
            arch: '',
            mode: '',
            scale: '',
            diskType: '',
            unitCnt: 1,
            cpuCnt: 0,
            memSize: 0,
            dataDirSize: 10,
            logDirSize: 10,
            paramCfg: {},
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
            type: [{
                required: true,
                message: '请选择类型',
                trigger: ['change', 'blur']
            }],
            siteName: [{
                required: true,
                message: '请输入站点名称',
                trigger: ['change', 'blur']
            }],
            system: [{
                required: true,
                message: '请选择所属系统',
                trigger: ['change', 'blur']
            }],
            subSystem: [{
                required: true,
                message: '请选择所属子系统',
                trigger: ['change', 'blur']
            }],
            servName: [{
                required: true,
                message: '请输入服务名称'
            },
                {
                    min: 4,
                    max: 8,
                    message: '长度在 4 到 8 个字符',
                    trigger: ['change', 'blur']
                },
                {
                    pattern: /^[0-9a-zA-Z]+$/,
                    message: '格式不正确（只能输入数字和英文）',
                    trigger: ['change', 'blur']
                }
            ],
            area: [{
                required: true,
                message: '请选择业务区',
                trigger: ['change', 'blur']
            }],
            architecture: [{
                required: true,
                message: '请选择硬件架构',
                trigger: ['change', 'blur']
            }],
            imageVersion: [{
                required: true,
                message: '请选择版本',
                trigger: ['change', 'blur']
            }],
            port: [{
                required: true,
                message: '请输入端口',
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
            dataDirSize: [{
                required: true,
                message: '请选择表空间大小',
                trigger: ['change', 'blur']
            }],
            logDirSize: [{
                required: true,
                message: '请选择日志空间大小',
                trigger: ['change', 'blur']
            }],
            backupStorageType: [{
                validator: validateFileRetentionNum,
                trigger: ['change', 'blur']
            }],
            backUpType: [{
                validator: validateFileRetentionNum,
                trigger: ['change', 'blur']
            }],
            cronExpression: [{
                validator: validateFileRetentionNum,
                trigger: ['change', 'blur']
            }],
            month: [{
                validator: validateFileRetentionNum,
                trigger: ['change', 'blur']
            }],
            week: [{
                validator: validateFileRetentionNum,
                trigger: ['change', 'blur']
            }],
            time: [{
                validator: validateFileRetentionNum,
                trigger: ['change', 'blur']
            }],
            fileRetentionNum: [{
                validator: validateFileRetentionNum,
                trigger: ['change', 'blur']
            }]
        },
        imageVersionLabel: '',
        flanksBorder: function (index, data) {
            var length = data.length
            for (var i = 3; i <= length;) {
                if (i === index) {
                    return "rightBorder"
                }
                i += 4
            }
            for (var i = 4; i <= length;) {
                if (i === index) {
                    return "leftBorder"
                }
                i += 4
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
        addIconClassFun: function ({column, columnIndex}) {
            if (column.property === 'del') {
                return 'addIconClass'
            }
        }
    },
    created: function () {
        this.siteListView()
        this.areaListView()
        this.systemListView()
    },
    methods: {
        siteListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sites", function (response) {
                XEUtils.arrayEach(response.data.data, (v, i) => {
                    if (v.id === getSession("siteId"))
                        _this.formData.siteName = v.name
                })
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        randServName: function () {
            this.formData.servName = randomWord(false, 8);
        },
        areaListView: function (val) {
            var _this = this
            sendGet("/" + getProjectName() + "/business_areas?site_id=" + getSession("siteId") + "&enabled=true", function (response) {
                _this.areaList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        systemListView: function (val) {
            var _this = this
            sendGet("/" + getProjectName() + "/business_systems?enabled=true", function (response) {
                _this.systemList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        systemChange: function (value) {
            this.subSystemListView(value)
        },
        subSystemListView: function (value) {
            var _this = this
            _this.formData.subSystem = '';
            sendGet("/" + getProjectName() + "/business_subsystems?business_system_id=" + value + "&enabled=true", function (response) {
                _this.subSystemList = response.data.data
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        architectureListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=sys_architecture", function (response) {
                _this.architectureList = response.data.data
                _this.formData.architecture = _this.architectureList[0].code
                _this.architectureChange()
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        architectureChange: function () {
            this.paramData = []
            this.imageVersionListView();
        },
        imageVersionListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/images?site_id=" + getSession("siteId") + "&architecture=" + _this.formData.architecture + "&type=redis&enabled=true", function (response) {
                _this.imageVersionList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        imageVersionChange: function () {
            var txt = this.$refs.imageVersionRadios.hoverOption.label
            this.imageVersionLabel = txt;
            this.archListView()
            this.paramData = []
        },
        archListView: function (val) {
            var _this = this
            var version = _this.imageVersionLabel.split(".")
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
                let modeInit = _this.modeList[0].code
                _this.formData.mode = modeInit
                _this.archChange(modeInit)
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        archChange: function (value) {
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
                this.formData.unitCnt = parseInt(tempMin)
            }
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
        scaleChange: function (val) {
            var changeIndex = this.$refs.scaleRadios.hoverIndex
            var cpucnt = this.scaleList[changeIndex].cpuCnt
            var memsize = this.scaleList[changeIndex].memSize
            this.formData.cpuCnt = parseFloat(cpucnt);
            this.formData.memSize = parseFloat(memsize);
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
        createdView: function (val) {
            var _this = this
            sendGet("/" + getProjectName() + "/orders/cfgs/redis", function (response) {
                if (response.data.data.length !== 0) {
                    XEUtils.arrayEach(response.data.data, (v, i) => {
                        if (v.type.code === "redis") {
                            if (v.scale) {
                                _this.formData.scale = v.scale.name
                                _this.formData.cpuCnt = v.scale.cpuCnt
                                _this.formData.memSize = v.scale.memSize
                            }
                            if (v.diskType)
                                _this.formData.diskType = v.diskType.code
                            if (v.port)
                                _this.formData.port = v.port
                            if (v.dataSize)
                                _this.formData.dataDirSize = v.dataSize
                            if (v.logSize)
                                _this.formData.logDirSize = v.logSize
                        }
                    })
                }
                _this.endFun()
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
        advancedChange: function () {
            //如果关闭高级选项，高级选项相关属性进行重置
            //关闭高级选项，默认用户取消参数配置和备份策略
            if (!this.advanced) {
                this.stepList.splice(2)
                this.stepPageNum = -10
                this.stepPageNums = -10
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

        procImageVersionChanged: function () {
            var _this = this
            var version = this.imageVersionLabel.split(".")
            sendGet("/" + getProjectName() + "/images/redis/templates?site_id=" + getSession('siteId') + "&major=" + version[0] + "&minor=" + version[1] + "&patch=" + version[2] + "&build=" + version[3] + "&architecture=" + _this.formData.architecture, function (response) {
                _this.paramList = response.data.data
                layer.closeAll('loading')
            }, function (error) {
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
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        backupStorageTypeListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=backup_storage_type", function (response) {
                _this.backupStorageTypeList = response.data.data
                layer.closeAll('loading')
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

        formSubmit: function (formName) { //addModal
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
            var area = this.formData.area
            var subSystem = this.formData.subSystem
            var servName = this.formData.servName
            var sysArchitecture = this.formData.architecture
            var highAvailable = this.formData.highAvailable
            var majorVersion = this.imageVersionLabel.split(".")[0]
            var minorVersion = this.imageVersionLabel.split(".")[1]
            var patchVersion = this.imageVersionLabel.split(".")[2]
            var buildVersion = this.imageVersionLabel.split(".")[3]
            var mode = this.formData.mode;
            var unitCnt = this.formData.unitCnt + 1
            var cpuCnt = this.formData.cpuCnt
            var memSize = this.formData.memSize
            var diskType = this.formData.diskType
            var dataDirSize = this.formData.dataDirSize
            var logDirSize = this.formData.logDirSize
            var port = this.formData.port

            var cnt = this.formData.cnt
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
                if (this.paramDeploy && _this.paramData.length != 0)
                    cfg.param = paramCfg

                if (XEUtils.isEmpty(cfg))
                    cfg = ''
            }

            var jsonData = {
                "category": "redis",
                "businessAreaId": area,
                "businessSubsystemId": subSystem,
                "name": servName,
                "sysArchitecture": sysArchitecture,
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
            commonConfirm(_this, '申请确认', getHintText('申请')).then(() => {
                sendPost("/" + getProjectName() + "/order_groups", function (response) {
                    layer.closeAll('loading')
                    operationCompletion(parent.listApp, "操作成功！")
                    if (_this.ogAutoExamine && _this.ogAutoExecute) {
                        parent.listApp.goTourlDialogShow = true
                    } else {
                        parent.listApp.returnList()
                    }
                    _this.formClose()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            }).catch(() => {
            });
        },

        stepsActiveAdd: function (formName) {
            var verify = false;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    return true;
                } else {
                    verify = true;
                    return false;
                }
            });
            if (verify) {
                return false;
            }
            this.stepsActive++
            switch (this.stepsActive) {
                case 1:
                    this.architectureListView()
                    this.scaleListView()
                    this.diskTypeListView()
                    this.createdView()
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
        },

        formClose: function () {
            parent.layer.close(setupIndex);
        }
    },
    computed: {
        backUp: function () {
            var num = 0
            fileRetentionNum = this.formData.fileRetentionNum
            if (!XEUtils.isUndefined(fileRetentionNum)) {
                fileRetentionNum = fileRetentionNum + ''
            }
            if (XEUtils.isEmpty(this.formData.backupStorageType) && XEUtils.isEmpty(this.formData.backUpType) &&
                XEUtils.isEmpty(this.formData.cronExpression) && XEUtils.isEmpty(this.formData.month) &&
                XEUtils.isEmpty(this.formData.week) && XEUtils.isEmpty(this.formData.time) &&
                XEUtils.isEmpty(fileRetentionNum)) {
                return num += 1
            } else {
                return num -= 1
            }
        }

    },
    watch: {
        backUp: {
            handler: function (newVal, oldVal) {
                if (newVal > oldVal) {
                    this.$refs["addForm"].clearValidate();
                }
            }
        }
    }
})