var rebuildIndex = parent.layer.getFrameIndex(window.name);
var unitId = getQueryVariable("unitId")
var clusterId = getQueryVariable("clusterId")
var diskType = getQueryVariable("diskType")
var rowId = getQueryVariable("rowId")
// var arrayLength = XEUtils.toInteger(getQueryVariable("arrayLength"))
var arrayLength = XEUtils.toInteger(1)
var hostIp = getQueryVariable("hostIp")
new Vue({
    el: '#rebuild',
    data: {
        stepList: [{
            name: '选择备份文件',
            isShow: true
        }, {
            name: '选择重建策略',
            isShow: true
        }, {
            name: '设置角色',
            isShow: arrayLength > 1
        }],
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
            force: false,
            backupFileId: '',
            host: '',
            hostId: ''
        },
        formRules: {
            host: [
                {required: true, message: '请选择指定主机'}
            ]
        },
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
            } else if (type === 'hostState') {
                data = jsonJudgeNotDefined(status, "status.host.state")
                if (!!data) {
                    switch (data.code) {
                        case "passing":
                            return getProjectSvg(COLOR_PASSING)
                        case "critical":
                            return getProjectSvg(COLOR_CRITICAL)
                        case "unknown":
                            return getProjectSvg(COLOR_UNKNOWN)
                        case "warning":
                            return getProjectSvg(COLOR_WARNING)
                    }
                } else {
                    return ""
                }
            }
        },
        stepsActive: 0,
        filterName: '',
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
        filterNames: '',
        tableDataAlls: [],
        tableDatas: [],
        searchDatas: [],
        sortDatas: [],
        defaultSelecteRows: null,
        tablePages: {
            currentPage: 1,
            pageSize: PAGE_SIZE,
            totalResult: 0,
            pageSizes: PAGE_SIZES,
            pageLayouts: PAGE_LAYOUTS
        },
        roleFilterName: '',
        roleTableDataAll: [],
        roleTableData: [],
        roleSearchData: [],
        roleSortData: [],
        roleTablePage: {
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
        this.restoreGetFun()
    },
    methods: {
        dataDispose: function (data) {
            var jq_jsonData = [];

            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))

                var sizeNum = 0
                if (jsonJudgeNotDefined(v, "v.size")) {
                    sizeNum = jsonJudgeNotDefined(v, "v.size")
                }
                json.sizeData = sizeSuffixAlter(sizeNum * 1024)
                json.consumingTime = differenceTime(jsonJudgeNotDefined(v, "v.createdAt"), jsonJudgeNotDefined(v, "v.finishAt"))

                jq_jsonData.push(json)
            })

            this.tableDataAll = XEUtils.sortBy(jq_jsonData, 'createdAt').reverse();
            if (!XEUtils.isEmpty(this.tableDataAll))
                this.defaultSelecteRow = this.tableDataAll[0].id
            this.searchData = this.tableDataAll.concat();
            this.sortData = this.searchData.concat();
            this.handlePageChange()
        },
        dataDisposes: function (data) {
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

            this.tableDataAlls = XEUtils.sortBy(jq_jsonData, 'createdAt').reverse();
            if (!XEUtils.isEmpty(this.tableDataAlls))
                this.defaultSelecteRows = this.tableDataAlls[0].id
            this.searchDatas = this.tableDataAlls.concat();
            this.sortDatas = this.searchDatas.concat();
            this.handlePageChange()
        },
        roleDataDispose: function (data) {
            var jq_jsonData = [];
            XEUtils.arrayEach(data.data.servs, (value, index) => {
                if (value.type.code === 'mysql') {
                    XEUtils.arrayEach(value.units, (v, i) => {
                        if (v.type.code === 'mysql') {

                            var json = JSON.parse(JSON.stringify(v))

                            var ip = jsonJudgeNotDefined(v, "v.ip");
                            var port = jsonJudgeNotDefined(v, "v.port");
                            if (!!ip && !!port) {
                                ip = ip + ":" + port
                            }

                            var versionValue = "";
                            if (!!v.version) {
                                if (!!v.version.major)
                                    versionValue = v.version.major + '.' + v.version.minor + '.' + v.version.patch + '.' + v.version.build
                            }


                            //brShow: 状态列 换行显示标识位 ，当状态为空时 不换行
                            json.brShow = (!XEUtils.isEmpty(jsonJudgeNotDefined(v, "v.state.code")) && !XEUtils.isEmpty(jsonJudgeNotDefined(v, "v.state.display")))
                            json.ip = ip;
                            json.versionValue = versionValue;
                            json.diskTypeDisplay = jsonJudgeNotDefined(v, "v.diskType.display");
                            json.actionDisplay = jsonJudgeNotDefined(v, "v.task.action.display") + jsonJudgeNotDefined(v, "v.task.state.display");
                            json.statusText = jsonJudgeNotDefined(v, "v.state.display");
                            json.roleCode = jsonJudgeNotDefined(v, "v.replication.role.code");

                            if (jsonJudgeNotDefined(v, "v.relateId") !== "")
                                jq_jsonData.push(json);
                        }
                    })
                }

            });
            this.roleTableDataAll = jq_jsonData;
            this.roleSearchData = this.roleTableDataAll.concat();
            this.roleSortData = this.roleSearchData.concat();
            this.roleHandlePageChange()
        },
        restoreGetFun: function () {
            var _this = this
            var url = "/" + getProjectName() + "/backup/files";
            var urlData = "?site_id=" + getSession("siteId") + "&success=true&type=full&serv_group_id=" + rowId;
            sendGet(url + urlData, function (response) {
                _this.tablePage.currentPage = 1
                _this.dataDispose(response.data)
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        hostGetFun: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/hosts/" + hostIp, function (response) {
                var data = response.data.data
                if (jsonJudgeNotDefined(data, "data.state.code") !== "passing")
                    _this.formData.force = true
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        hostChange: function () {
            var _this = this
            if (this.formData.strategy === "assign") {
                sendGet("/" + getProjectName() + "/hosts?cluster_id=" + clusterId + "&enabled=true&disk_type=" + diskType + "&state=passing", function (response) {
                    _this.tablePages.currentPage = 1
                    _this.dataDisposes(response.data)
                    layer.closeAll('loading');
                }, function (error) {
                    console.log(error)
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, null)
            } else {
                this.tablePages = {
                    "currentPage": 1,
                    "pageSize": PAGE_SIZE,
                    "totalResult": 0,
                    "pageSizes": PAGE_SIZES,
                    "pageLayouts": PAGE_LAYOUTS
                }
                this.tableDataAlls = []
                this.tableDatas = []
            }
        },
        roleVIewFun: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/serv_groups/cmha/" + rowId + "?topology=false", function (response) {
                _this.roleDataDispose(response.data)
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        ruleFun: function (tableName) {
            let verify = true;
            let radioDate;

            switch (this.stepsActive) {
                case 0:
                    radioDate = this.$refs[tableName].getRadioRecord();
                    if (XEUtils.isEmpty(radioDate)) {
                        verify = false;
                    }
                    break;

                case 1:
                    if (this.formData.strategy === this.assignValue) {
                        radioDate = this.$refs[tableName].getRadioRecord();
                        if (XEUtils.isEmpty(radioDate)) {
                            verify = false;
                            this.formData.hostId = ''
                        } else {
                            this.formData.hostId = radioDate.relateId
                        }
                    }
                    break;

                case 2:
                    if (XEUtils.findIndexOf(this.roleTableDataAll, item => item.roleCode === "master") === -1)
                        verify = false;
                    break
            }

            return verify
        },
        stepsActiveAdd: function (tableName) {
            if (!this.ruleFun(tableName))
                return false

            let radioDate;
            switch (this.stepsActive) {
                case 0:
                    radioDate = this.$refs[tableName].getRadioRecord();
                    this.formData.backupFileId = radioDate.id
                    // this.hostGetFun()
                    break;

                case 1:
                    if (this.roleTableDataAll.length === 0)
                        this.roleVIewFun()
                    break;

            }
            this.stepsActive++

        },
        formSubmit: function (tableName) {//scaleUpModal
            if (!this.ruleFun(tableName))
                return false

            var jsonData = {
                "backupFileId": this.formData.backupFileId,
                "force": this.formData.force
            }
            if (this.formData.strategy === this.assignValue) {
                jsonData.hostRelateId = this.formData.hostId
            }
            /*//新加role字段
            if (arrayLength > 1) {
                let masterArray = []
                let slaveArray = []
                XEUtils.arrayEach(this.roleTableDataAll, (v, i) => {
                    let unitId = v.id
                    switch (v.roleCode) {
                        case "master":
                            masterArray.push(unitId)
                            break
                        case "slave":
                            slaveArray.push(unitId)
                            break
                    }
                })
                jsonData.role = {}
                jsonData.role.master = masterArray
                jsonData.role.slave = slaveArray
            } else {
                jsonData.role = {
                    master: [
                        unitId
                    ]
                }
            }*/

            var _this = this
            let parentApp = parent[0].cmhaListApp
            let typeName = XEUtils.isEmpty(parentApp.typeName) ? "数据库" : parentApp.typeName
            let tempText1 = "对选中的数据库单元进行重建，重建操作会将原来的单元删除并创建一个一样的单元，"
            let tempText2 = ''
            let tempText3 = '请谨慎操作！'
            if (this.formData.force) {
                tempText2 = '强制重建有可能导致主机上的存储资源释放不掉，'
            }
            let riskText = tempText1 + tempText2 + tempText3
            let setting = {
                customClass: "customizeWidth510pxClass"
            }
            parent.layer.min(rebuildIndex);
            commonConfirm(parent.manageApp, '重建确认', getRiskLevelHtml(4, riskText) + getHintText('重建'), setting).then(() => {
                parent.layer.restore(rebuildIndex);
                sendPut("/" + getProjectName() + "/units/" + unitId + "/rebuild", function (response) {
                    layer.closeAll('loading')
                    parent[0].cmhaListApp.refreshSaveData.push(parent[0].cmhaListApp.selectedData)
                    parent[0].cmhaListApp.returnList()
                    _this.formClose()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            }).catch(() => {
                parent.layer.restore(rebuildIndex);
            });
        },
        handleSizeCurrentChange: function ({currentPage, pageSize}) {
            this.tablePage.pageSize = pageSize
            this.tablePage.currentPage = currentPage
            this.tablePages.pageSize = pageSize
            this.tablePages.currentPage = currentPage
            this.handlePageChange()
        },
        handlePageChange: function () {
            var startNum = (this.tablePage.currentPage - 1) * this.tablePage.pageSize;
            var finishNum = startNum + this.tablePage.pageSize;
            this.tablePage.totalResult = this.searchData.length
            if (finishNum > this.tablePage.totalResult) {
                finishNum = this.tablePage.totalResult
            }
            this.tableData = this.sortData.slice(startNum, finishNum)

            var startNums = (this.tablePages.currentPage - 1) * this.tablePages.pageSize;
            var finishNums = startNums + this.tablePages.pageSize;
            this.tablePages.totalResult = this.searchDatas.length
            if (finishNums > this.tablePages.totalResult) {
                finishNums = this.tablePages.totalResult
            }
            this.tableDatas = this.sortDatas.slice(startNums, finishNums)
        },
        sortChange: function (column) {
            //console.log(column)
            if (column.order != null) {
                this.sortData.sort(commonGetSortFun(column.order, column.prop))
                this.sortDatas.sort(commonGetSortFun(column.order, column.prop))
            } else {
                this.sortData = this.searchData.concat();
                this.sortDatas = this.searchDatas.concat();
            }
            this.tablePage.currentPage = 1
            this.handlePageChange()
        },
        roleHandleSizeCurrentChange: function ({currentPage, pageSize}) {
            this.roleTablePage.pageSize = pageSize
            this.roleTablePage.currentPage = currentPage
            this.roleHandlePageChange()
        },
        roleHandlePageChange: function () {
            var startNum = (this.roleTablePage.currentPage - 1) * this.roleTablePage.pageSize;
            var finishNum = startNum + this.roleTablePage.pageSize;
            this.roleTablePage.totalResult = this.roleSearchData.length
            if (finishNum > this.roleTablePage.totalResult) {
                finishNum = this.roleTablePage.totalResult
            }
            this.roleTableData = this.roleSortData.slice(startNum, finishNum)
        },
        roleSortChange: function (column) {
            //console.log(column)
            if (column.order != null) {
                this.roleSortData.sort(commonGetSortFun(column.order, column.prop))
            } else {
                this.roleSortData = this.searchData.concat();
            }
            this.roleTablePage.currentPage = 1
            this.roleHandlePageChange()
        },
        selectChangeEvent: function ({row, rowIndex}) {
            //console.log(checked ? '勾选事件' : '取消事件', records)
            this.formData.host = row.relateId
        },
        searchClicks: function () {
            let keyArray = ['ip', 'site.name', 'businessArea.name', 'cluster.name', 'architecture.display',
                'storages.name', 'actionDisplay', 'stateText', 'enabledText', 'resourceRates<>.name', 'resourceRates<>.value']
            commonSearchClick(this, keyArray)
        },
        searchClick: function () {
            let keyArray = ['name', 'sizeData', 'type.display', 'backupStorageType.display', 'expiredAt', 'unitName',
                'createdAt', 'status.display', 'consumingTime']
            commonSearchClick(this, keyArray)
        },
        strategyChange: function (val) {
            if (val === this.assignValue) {
                this.hostListView()
            }
        },
        returnList: function () {
            document.location.reload();
        },
        formClose: function () {
            parent.layer.close(rebuildIndex);
        }
    },
    computed: {
        saveBtnShow: function () {
            if (arrayLength === 1) {
                return this.stepsActive !== 0
            } else {
                return this.stepsActive === 2
            }
        }
    }
})