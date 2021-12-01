var rebuildIndex = parent.layer.getFrameIndex(window.name);
var unitId = getQueryVariable("unitId")
var clusterId = getQueryVariable("clusterId")
var diskType = getQueryVariable("diskType")
var typeCode = getQueryVariable("typeCode")
new Vue({
    el: '#rebuild',
    data: {
        assignValue: "assign",
        rebuildList: [
            {
                code: "auto",
                name: "自动分配"
            }, {
                code: "assign",
                name: "指定主机"
            }
        ],
        hostList: [],
        formData: {
            strategy: 'auto',
            host: ''
        },
        formRules: {
            host: [
                {required: true, message: '请选择指定主机'}
            ]
        },
        filterName: '',
        statusShow: function (status, type) {
            var data = ""
            if (type === 'state') {
                data = status.state
                if (data !== null) {
                    switch (data.code) {
                        case "passing":
                            return getProjectSvg(COLOR_PASSING) + "<span style='line-height:14px'>" + data.display + "</span>"
                        case "critical":
                            return getProjectSvg(COLOR_CRITICAL) + "<span style='line-height:14px'>" + data.display + "</span>"
                        case "unknown":
                            return getProjectSvg(COLOR_UNKNOWN) + "<span style='line-height:14px'>" + data.display + "</span>"
                        case "warning":
                            return getProjectSvg(COLOR_WARNING) + "<span style='line-height:14px'>" + data.display + "</span>"
                    }
                } else {
                    return ""
                }
            } else if (type === 'enabled') {
                data = status.enabled
                if (data === null) {
                    return ""
                }
                if (data) {
                    return getProjectSvg(COLOR_ENABLED) + "<span style='line-height:14px'>是</span>"
                } else if (!data) {
                    return getProjectSvg(COLOR_DISABLED) + "<span style='line-height:14px'>否</span>"
                }
            } else if (type === 'task') {
                data = status.task
                if (!!jsonJudgeNotDefined(status, "status.task.state.code")) {
                    switch (data.state.code) {
                        case "success":
                            return "<span style='color:" + COLOR_TASK_COMPLETED + ";border-color:" + COLOR_TASK_COMPLETED + "'>" + status.actionDisplay + "</span>"
                        case "failed":
                            return "<span style='color:" + COLOR_TASK_ERROR + ";border-color:" + COLOR_TASK_ERROR + "'>" + status.actionDisplay + "</span>"
                        case "running":
                            return "<span style='color:" + COLOR_TASK_RUNNING + ";border-color:" + COLOR_TASK_RUNNING + "'>" + status.actionDisplay + "</span>"
                        case "timeout":
                            return "<span style='color:" + COLOR_TASK_TIMEOUT + ";border-color:" + COLOR_TASK_TIMEOUT + "'>" + status.actionDisplay + "</span>"
                        case "unknown":
                            return "<span style='color:" + COLOR_TASK_UNKNOWN + ";border-color:" + COLOR_TASK_UNKNOWN + "'>" + status.actionDisplay + "</span>"
                    }
                } else {
                    return "未入库"
                }
            } else if (type === "role") {
                data = status.role
                if (!!jsonJudgeNotDefined(status, "status.task.state.code")) {
                    if (data !== null) {
                        return "<span>角色：" + data.display + "<span>"
                    } else {
                        return ""
                    }
                } else {
                    return ""
                }
            }
        },
        allAlign: ALLALIGN,
        tableDataAll: [],
        tableData: [],
        searchData: [],
        sortData: [],
        defaultSelecteRow: null,
        tablePage: {
            currentPage: 1,
            pageSize: PAGE_SIZE,
            totalResult: 0,
            pageSizes: PAGE_SIZES,
            pageLayouts: PAGE_LAYOUTS
        },
        colorChange: function (value) {
            if (value > 80) {
                return '#f8453f'
            } else if (value > 60) {
                return '#ff9900'
            } else {
                return '#333'
            }
        }
    },
    created: function () {
        this.hostChange()
    },
    methods: {
        dataDispose: function (data) {
            var jq_jsonData = [];
            var _this = this
            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))
                var oneNum = 10
                var twoNum = 14

                var resourceRates = [{
                    name: "CPU",
                    data: v.cpu,
                    value: "",
                    textColor: _this.colorChange(),
                    width: oneNum
                }, {
                    name: "内存",
                    data: v.mem,
                    value: "",
                    textColor: _this.colorChange(),
                    width: twoNum
                }, {
                    name: "单元",
                    data: v.unit,
                    value: "",
                    textColor: _this.colorChange(),
                    width: oneNum
                }];
                if (v.cpu !== null) {
                    resourceRates[0].value = usedCapacityDispose(v.cpu.used, v.cpu.capacity)
                    resourceRates[0].textColor = _this.colorChange(((v.cpu.used / v.cpu.capacity) * 100).toFixed(1))
                }
                if (v.mem !== null) {
                    resourceRates[1].value = usedCapacityDispose(v.mem.used, v.mem.capacity, " G")
                    resourceRates[1].textColor = _this.colorChange(((v.mem.used / v.mem.capacity) * 100).toFixed(1))
                }
                if (v.unit !== null) {
                    resourceRates[2].value = usedCapacityDispose(v.unit.cnt, v.unit.maxCnt)
                    resourceRates[2].textColor = _this.colorChange(((v.unit.cnt / v.unit.maxCnt) * 100).toFixed(1))
                }

                if (v.hdd != null) {
                    var hddJson = {
                        name: "机械盘",
                        data: v.hdd,
                        textColor: _this.colorChange(),
                        value: usedCapacityDispose(v.hdd.used, v.hdd.capacity, " G"),
                        width: twoNum
                    }
                    hddJson.textColor = _this.colorChange(((v.hdd.used / v.hdd.capacity) * 100).toFixed(1))
                    resourceRates.push(hddJson)
                }
                if (v.ssd != null) {
                    var ssdJson = {
                        name: "固态盘",
                        data: v.ssd,
                        textColor: _this.colorChange(),
                        value: usedCapacityDispose(v.ssd.used, v.ssd.capacity, " G"),
                        width: twoNum
                    }
                    ssdJson.textColor = _this.colorChange(((v.ssd.used / v.ssd.capacity) * 100).toFixed(1))
                    resourceRates.push(ssdJson)
                }
                if (v.remoteStorage != null) {
                    var remoteStorageJson = {
                        name: "外置存储",
                        data: v.remoteStorage,
                        textColor: _this.colorChange(),
                        value: usedCapacityDispose(v.remoteStorage.used, v.remoteStorage.capacity, " G"),
                        width: twoNum
                    }
                    remoteStorageJson.textColor = _this.colorChange(((v.remoteStorage.used / v.remoteStorage.capacity) * 100).toFixed(1))
                    resourceRates.push(remoteStorageJson)
                }

                var actionDisplay = "";
                if (v.task !== null) {
                    actionDisplay = v.task.action.display + v.task.state.display
                }

                var stateText = "";
                if (v.state !== null) {
                    stateText = v.state.display
                }

                var enabledText = "";
                if (v.enabled !== null) {
                    if (v.enabled) {
                        enabledText = "是"
                    } else {
                        enabledText = "否"
                    }
                }

                json.resourceRates = resourceRates
                json.actionDisplay = actionDisplay
                json.stateText = stateText
                json.enabledText = enabledText
                jq_jsonData.push(json)
            })

            this.tableDataAll = XEUtils.sortBy(jq_jsonData, 'createdAt').reverse();
            if (!XEUtils.isEmpty(this.tableDataAll))
                this.defaultSelecteRow = this.tableDataAll[0].id
            this.searchData = this.tableDataAll.concat();
            this.sortData = this.searchData.concat();
            this.handlePageChange()
        },
        handleSizeCurrentChange: function (value) {
            commonHandleSizeCurrentChange(this, value)
        },
        handlePageChange: function () {
            commonHandlePageChange(this)
        },
        sortChange: function (column) {
            commonSortChange(this, column)
        },
        selectChangeEvent: function ({row, rowIndex}) {
            //console.log(checked ? '勾选事件' : '取消事件', records)
        },
        hostChange: function () {
            var _this = this
            switch (_this.formData.strategy) {
                case 'auto':
                    parent.layer.style(rebuildIndex, {
                        width: '355px',
                        height: '222px'
                    });
                    this.tablePage = {
                        "currentPage": 1,
                        "pageSize": PAGE_SIZE,
                        "totalResult": 0,
                        "pageSizes": PAGE_SIZES,
                        "pageLayouts": PAGE_LAYOUTS
                    }
                    this.tableDataAll = []
                    this.tableData = []
                    break;
                case 'assign':
                    parent.layer.style(rebuildIndex, {
                        width: '720px',
                        height: '450px'
                    });
                    sendGet("/" + getProjectName() + "/hosts?cluster_id=" + clusterId + "&enabled=true&disk_type=" + diskType + "&state=passing", function (response) {
                        _this.tablePage.currentPage = 1
                        _this.dataDispose(response.data)
                        layer.closeAll('loading');
                    }, function (error) {
                        operationCompletion(_this, error.response.data.msg, 'error')
                    }, null)
                    break;
                default:
                    break;
            }
        },
        formSubmit: function (formName, tableName) {//scaleUpModal
            var verify = false;
            var radioDate
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    return true;
                } else {
                    verify = true;
                    //console.log('error submit!!');
                    return false;
                }
            });
            if (this.formData.strategy === "assign") {
                radioDate = this.$refs[tableName].getRadioRecord();
                if (verify) {
                    return false;
                } else if (XEUtils.isEmpty(radioDate)) {
                    return false;
                }
            }

            var jsonData = {
                "force": true
            }
            var _this = this
            if (_this.formData.strategy === _this.assignValue) {
                jsonData.hostRelateId = radioDate.relateId
            }
            //console.log("新增",jsonData)
            let parentApp = parent.listApp
            let typeName = parentApp.typeName

            let hintText = ""
            let riskText = "对选中的" + typeName + "单元进行强制重建，强制重建有可能导致主机上的存储资源释放不掉，请谨慎操作！"
            hintText += getRiskLevelHtml(5, riskText)

            hintText += "强制重建完成之后，需要对该单元进行【重建初始化】操作！" + "<br>"
            hintText += getHintText('强制重建')

            let setting = {
                customClass: "customizeWidth540pxClass"
            }

            parent.layer.min(rebuildIndex);
            commonConfirm(parentApp, '强制重建确认', hintText, setting).then(() => {
                parent.layer.restore(rebuildIndex);
                sendPut("/" + getProjectName() + "/units/" + unitId + "/rebuild", function (response) {
                    layer.closeAll('loading')
                    parentApp.refreshSaveData.push(parentApp.selectedData)
                    parentApp.returnList()
                    _this.formClose()
                }, function (error) {
                    console.log(error)
                    operationCompletion(_this, error.response.data.msg, "error")
                }, jsonData)
            }).catch(() => {
                parent.layer.restore(rebuildIndex);
            });
        },
        searchClick: function () {
            var keyArray = ['ip', 'site.name', 'businessArea.name', 'cluster.name', 'architecture.display',
                'storages.name', 'actionDisplay', 'stateText', 'enabledText', 'resourceRates<>.name', 'resourceRates<>.value']
            commonSearchClick(this, keyArray)
        },
        returnList: function () {
            document.location.reload();
        },
        formClose: function () {
            parent.layer.close(rebuildIndex);
        }
    }
})