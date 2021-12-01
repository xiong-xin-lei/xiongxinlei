var unitType = parent.manageApp.topoDblclickType
var unitName = parent.manageApp.topoDblclickName
var dbClickData = null
parent.manageApp.topoDblclickType = null
parent.manageApp.topoDblclickName = null
ROWBTN_SHOWINDEX = 3
var mysqlListApp = new Vue({
    el: '#mysqlList',
    data: {
        title: '单元',
        table_heighth: '',
        rowData: {},
        btnList: ttBtnList,
        rowBtnList: rowBtnList,
        typeName: "数据库",
        unitValue: ["mysql", "proxysql", "cmha"],
        unitList: [
            {
                code: "mysql",
                name: "数据库"
            }, {
                code: "proxysql",
                name: "代理"
            }, {
                code: "cmha",
                name: "高可用"
            }
        ],
        disabledValue: {},
        btnReplaceList: {
            start: "btnStart",
            stop: "btnStop",
            terminal: "btnTerminal",
            service: "btnService",
            logView: "btnLogView",
            slowLog: "btnSlowLog",
            monitor: "btnMonitor",
            monitorService: 'btnMonitorService',
            rebuild: "btnRebuild",
            rebuildInit: "btnRebuildInit",
            backup: "btnBackup",
            restore: "btnRestore",
            updateRole: "btnUpdateRole",
            refresh: "refresh"
        },
        updateStatus: '',
        btnOperation: function (value, type) {
            if (type === 'type') {
                switch (value) {
                    case this.btnReplaceList.add:
                        return 'primary'
                    default:
                        return ''
                }
            } else if (type === 'icon') {
                switch (value) {
                    case this.btnReplaceList.start:
                        return 'el-icon-video-play'
                    case this.btnReplaceList.stop:
                        return 'el-icon-video-pause'
                    default:
                        return 'el-icon-setting'
                }
            }
        },
        statusShow: function (status, type) {
            if (status === null) {
                return ""
            }
            var data
            var svgElement
            if (type === 'state') {
                data = status.state
                if (!XEUtils.isEmpty(jsonJudgeNotDefined(data, "data.code"))) {
                    let svgHtml = ""
                    switch (data.code) {
                        case "passing":
                            svgHtml = getProjectSvg(COLOR_PASSING)
                            break
                        case "critical":
                            svgHtml = getProjectSvg(COLOR_CRITICAL)
                            break
                        case "unknown":
                            svgHtml = getProjectSvg(COLOR_UNKNOWN)
                            break
                        case "warning":
                            svgHtml = getProjectSvg(COLOR_WARNING)
                            break
                    }
                    let useHtml = status.hoverShow ? "" : svgHtml
                    if (data.code === "passing")
                        useHtml = svgHtml
                    return useHtml + "<span style='line-height:14px'>" + data.display + "</span>"
                } else {
                    return ""
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
                    }
                } else {
                    return ""
                }
            } else if (type === 'role') {
                data = jsonJudgeNotDefined(status, "status.topology.runningState.code");
                if (!!data) {
                    switch (data) {
                        case "passing":
                            return COLOR_PASSING
                        case "critical":
                            return COLOR_CRITICAL
                        case "warning":
                            return COLOR_WARNING
                        case "unknown":
                            return COLOR_UNKNOWN
                        default:
                            return COLOR_UNKNOWN
                    }
                } else {
                    return COLOR_UNKNOWN
                }
            } else if (type === 'topo') {
                data = jsonJudgeNotDefined(status, "status.topology.runningState.code");
                if (!!data) {
                    switch (data) {
                        case "passing":
                            svgElement = getProjectSvg(COLOR_PASSING)
                            break
                        case "critical":
                            svgElement = getProjectSvg(COLOR_CRITICAL)
                            break
                        case "warning":
                            svgElement = getProjectSvg(COLOR_WARNING)
                            break
                        case "unknown":
                            svgElement = getProjectSvg(COLOR_UNKNOWN)
                            break
                        default:
                            svgElement = ""
                    }
                } else {
                    svgElement = ""
                }
                return svgElement + "<span style='line-height:14px'>" + status.roleName + "</span>"
            } else if (type === 'hostState') {
                data = jsonJudgeNotDefined(status, "status.host.state")
                if (!!data && !!jsonJudgeNotDefined(status, "status.host.ip")) {
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
            } else if (type === "pod") {
                data = jsonJudgeNotDefined(status, "status.podState.display");
                if (!!data) {
                    var podElement = "<span>Pod：</span>"
                    switch (data) {
                        case "Pending":
                            return podElement + "<span style='color:#696bd8'>" + data + "</span>"
                        case "Running":
                            return podElement + "<span style='color:" + COLOR_TASK_RUNNING + "'>" + data + "</span>"
                        case "Succeeded":
                            return podElement + "<span style='color:" + COLOR_PASSING + "'>" + data + "</span>"
                        case "Unknown":
                            return podElement + "<span style='color:" + COLOR_UNKNOWN + "'>" + data + "</span>"
                        case "Failed":
                            return podElement + "<span style='color:" + COLOR_CRITICAL + "'>" + data + "</span>"
                    }
                }
            }
        },
        operationWidthFun: function (listCode) {
            switch (listCode) {
                case "mysql":
                    return operationWidthInit(rowBtnList)
                case "proxysql":
                case "cmha":
                    return operationWidthInit(this.rowBtnShowFun(null, listCode))
            }
        },
        rowBtnShowFun: function (rowData, type) {
            var _this = this
            var btnArray = []
            var rowRole = jsonJudgeNotDefined(rowData, 'rowData.topology.role.code')
            var rowBtnListTemp = XEUtils.toTreeArray(XEUtils.clone(_this.rowBtnList, true), {
                children: 'childrens',
                clear: true
            })
            switch (type) {
                case "mysql":
                    switch (rowRole) {
                        case "master":
                            rowBtnListTemp.forEach(function (value, index) {
                                switch (value.code) {
                                    default:
                                        btnArray.push(value)
                                }
                            })
                            return XEUtils.toArrayTree(btnArray, {parentKey: 'pid', children: 'childrens'})

                        case "slave":
                            rowBtnListTemp.forEach(function (value, index) {
                                switch (value.code) {
                                    default:
                                        btnArray.push(value)
                                }
                            })
                            return XEUtils.toArrayTree(btnArray, {parentKey: 'pid', children: 'childrens'})

                        default:
                            XEUtils.arrayEach(rowBtnListTemp, (value, index) => {
                                switch (value.code) {
                                    default:
                                        btnArray.push(value)
                                }
                            })
                            return XEUtils.toArrayTree(btnArray, {parentKey: 'pid', children: 'childrens'})
                    }
                case "proxysql":
                    XEUtils.arrayEach(rowBtnListTemp, (value, index) => {
                        switch (value.code) {
                            case _this.btnReplaceList.backup:
                            case _this.btnReplaceList.restore:
                            case _this.btnReplaceList.updateRole:
                                // case _this.btnReplaceList.rebuildInit:
                                return false
                            default:
                                btnArray.push(value)
                        }
                    })
                    return XEUtils.toArrayTree(btnArray, {parentKey: 'pid', children: 'childrens'})
                case "cmha":
                    XEUtils.arrayEach(rowBtnListTemp, (value, index) => {
                        switch (value.code) {
                            case _this.btnReplaceList.backup:
                            case _this.btnReplaceList.restore:
                            case _this.btnReplaceList.updateRole:
                            case _this.btnReplaceList.monitorService:
                                return false
                            default:
                                btnArray.push(value)
                        }
                    })
                    return XEUtils.toArrayTree(btnArray, {parentKey: 'pid', children: 'childrens'})
            }
        },
        allAlign: ALLALIGN,
        tableDataAll: [],
        tableData: [],
        searchData: [],
        sortData: [],
        filterName: {
            mysql: '',
            proxysql: '',
            cmha: ''
        },
        selectedData: {},
        refreshKey: ["id"],
        refreshSaveData: [],
        tablePage: {
            mysql: {
                currentPage: 1,
                pageSize: LINEFEED_PAGE_SIZE,
                totalResult: 0,
                pageSizes: PAGE_SIZES,
                pageLayouts: PAGE_LAYOUTS
            },
            proxysql: {
                currentPage: 1,
                pageSize: LINEFEED_PAGE_SIZE,
                totalResult: 0,
                pageSizes: PAGE_SIZES,
                pageLayouts: PAGE_LAYOUTS
            },
            cmha: {
                currentPage: 1,
                pageSize: LINEFEED_PAGE_SIZE,
                totalResult: 0,
                pageSizes: PAGE_SIZES,
                pageLayouts: PAGE_LAYOUTS
            }
        }
    },
    created: function () {
        var _this = this
        /*if (unitType !== null) {
            this.unitValue = [unitType]
            this.btnClick(this.btnReplaceList.refresh, true, 0, unitType)
        } else {
            this.btnClick(this.btnReplaceList.refresh, true, 0, this.unitList[0].code)
        }*/
        this.btnClick(this.btnReplaceList.refresh, true, 0, this.unitList[0].code)
        this.btnDisabledCreated()
        setTimeout(function () {
            let tableNameArray = []
            for (var i = 0; i < _this.unitList.length; i++) {
                tableNameArray.push(_this.unitList[i].code + 'Table')
            }
            getHtmlMinWidth("mysqlList", _this, tableNameArray)
        }, 0)
    },
    methods: {
        dataDispose: function (data) {
            var jq_jsonData = {
                mysql: [],
                proxysql: [],
                cmha: []
            };
            let topology = {}

            XEUtils.arrayEach(jsonJudgeNotDefined(data, "data.data.servs"), (value, index) => {
                if (jsonJudgeNotDefined(value, "value.type.code") === "cmha")
                    topology = value.topology

                XEUtils.arrayEach(value.units, (v, i) => {
                    if (value.type.code === v.type.code) {
                        var json = JSON.parse(JSON.stringify(v))

                        var dataSize = jsonJudgeNotDefined(v, "v.dataSize");
                        if (!!dataSize || dataSize === 0) {
                            dataSize = dataSize + "G"
                        }

                        var logSize = jsonJudgeNotDefined(v, "v.logSize");
                        if (!!logSize || logSize === 0) {
                            logSize = logSize + "G"
                        }

                        var memSize = jsonJudgeNotDefined(v, "v.memSize");
                        if (!!memSize || memSize === 0) {
                            memSize = memSize + "G"
                        }

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


                        json.dataSize = dataSize;
                        json.logSize = logSize;
                        json.memSize = memSize;
                        json.ip = ip;
                        json.versionValue = versionValue;
                        //brShow: 状态列 换行显示标识位 ，当状态为空时 不换行
                        json.brShow = (!XEUtils.isEmpty(jsonJudgeNotDefined(v, "v.state.code")) && !XEUtils.isEmpty(jsonJudgeNotDefined(v, "v.state.display")))
                        json.diskTypeDisplay = jsonJudgeNotDefined(v, "v.diskType.display");
                        json.actionDisplay = jsonJudgeNotDefined(v, "v.task.action.display") + jsonJudgeNotDefined(v, "v.task.state.display");
                        json.statusText = jsonJudgeNotDefined(v, "v.state.display");
                        json.roleDisplay = jsonJudgeNotDefined(v, "v.role.display");

                        if (json.id === unitName) {
                            dbClickData = json
                            unitName = null
                        }

                        if (jsonJudgeNotDefined(v, "v.relateId") !== "") {
                            if (XEUtils.isUndefined(jq_jsonData[v.type.code])) {
                                jq_jsonData[v.type.code] = []
                            }
                            jq_jsonData[v.type.code].push(json);
                        }
                    }
                })

            });

            XEUtils.arrayEach(jq_jsonData.mysql, (value, index) => {
                // console.log(value.relateId,index)
                let data = XEUtils.isEmpty(topology) ? {} : topology[value.relateId]

                var roleName = jsonJudgeNotDefined(data, "topology.role.display");
                var roleRunningCode = jsonJudgeNotDefined(data, "topology.runningState.code");
                var roleRunningDisplay = jsonJudgeNotDefined(data, "topology.runningState.display");
                var roleIoRunningCode = jsonJudgeNotDefined(data, "topology.ioThread.code");
                var roleIoRunningDisplay = jsonJudgeNotDefined(data, "topology.ioThread.display");
                var roleSqlRunningCode = jsonJudgeNotDefined(data, "topology.sqlThread.code");
                var roleSqlRunningDisplay = jsonJudgeNotDefined(data, "topology.sqlThread.display");
                var topologyStateCode = jsonJudgeNotDefined(data, "topology.state.code");
                var topologyStateDisplay = jsonJudgeNotDefined(data, "topology.state.display");
                var topologyReplModeCode = jsonJudgeNotDefined(data, "topology.replMode.code");
                var topologyReplMode = jsonJudgeNotDefined(data, "topology.replMode.display");
                var topologyCandidate = jsonJudgeNotDefined(data, "topology.candidate");
                var topologyMaintain = jsonJudgeNotDefined(data, "topology.maintain");
                var topologyIsolate = jsonJudgeNotDefined(data, "topology.isolate");
                var topologyMaintainText;
                if (topologyMaintain === '') {
                    topologyMaintainText = ""
                } else {
                    topologyMaintainText = topologyMaintain ? "是" : "否"
                }

                jq_jsonData.mysql[index].topology = data;
                jq_jsonData.mysql[index].roleName = roleName;
                jq_jsonData.mysql[index].roleRunningCode = roleRunningCode;
                jq_jsonData.mysql[index].roleRunningDisplay = roleRunningDisplay;
                jq_jsonData.mysql[index].roleIoRunningCode = roleIoRunningCode;
                jq_jsonData.mysql[index].roleIoRunningDisplay = roleIoRunningDisplay;
                jq_jsonData.mysql[index].roleSqlRunningCode = roleSqlRunningCode;
                jq_jsonData.mysql[index].roleSqlRunningDisplay = roleSqlRunningDisplay;
                jq_jsonData.mysql[index].topologyStateCode = topologyStateCode;
                jq_jsonData.mysql[index].topologyStateDisplay = topologyStateDisplay;
                jq_jsonData.mysql[index].topologyReplModeCode = topologyReplModeCode;
                jq_jsonData.mysql[index].topologyReplMode = topologyReplMode;
                jq_jsonData.mysql[index].topologyCandidate = topologyCandidate;
                jq_jsonData.mysql[index].topologyMaintain = topologyMaintain;
                jq_jsonData.mysql[index].topologyIsolate = topologyIsolate;
                jq_jsonData.mysql[index].topologyMaintainText = topologyMaintainText;
            })

            this.tableDataAll = jq_jsonData;
            this.searchData = XEUtils.clone(this.tableDataAll);
            this.sortData = XEUtils.clone(this.searchData);
            this.handlePageChange()
        },
        btnDisabledCreated: function (type) {
            var tempArray = XEUtils.values(this.btnReplaceList)
            var tempJson = {}
            var i
            for (i = 0; i < tempArray.length; i++) {
                XEUtils.set(tempJson, tempArray[i], true)
            }
            if (type === "all" || XEUtils.isUndefined(type)) {
                var tempUnitJson = {}
                for (i = 0; i < this.unitList.length; i++) {
                    XEUtils.set(tempUnitJson, this.unitList[i].code, tempJson)
                }
                this.disabledValue = tempUnitJson
            } else {
                this.disabledValue[type] = tempJson
            }
        },
        isAble: function (code, data, type) {
            var _this = this
            if (XEUtils.isArray(data)) {
                if (data.length === 0) {
                    this.btnDisabledCreated(type)
                } else {
                    XEUtils.clear(_this.disabledValue[data[0].type.code], false)
                    XEUtils.arrayEach(data, (v, index) => {
                        var tempArray = XEUtils.values(_this.btnReplaceList)
                        for (var i = 0; i < tempArray.length; i++) {
                            if (_this.isAble(tempArray[i], v, v.type.code)) {
                                _this.disabledValue[v.type.code][tempArray[i]] = true
                            }
                        }
                    })
                }
            } else {
                let hostStateCode = jsonJudgeNotDefined(data, 'data.host.state.code')
                switch (code) {
                    case _this.btnReplaceList.add:
                    case _this.btnReplaceList.update:
                    case _this.btnReplaceList.start:
                    case _this.btnReplaceList.stop:
                    case _this.btnReplaceList.backup:
                    case _this.btnReplaceList.restore:
                    case _this.btnReplaceList.rebuildInit:
                        return false

                    case _this.btnReplaceList.rebuild:
                        return hostStateCode !== "passing"

                    case _this.btnReplaceList.del:
                        return data.enabled === true;

                    case _this.btnReplaceList.refresh:
                        return false
                }
            }
        },
        actionDisplayClick: function (data) {
            //console.log(data.id)
            var _this = this
            var width = '960px'
            var height = '500px'
            var title = "任务列表"
            var url = "/" + getProjectName() + "/app/task"
            var urlData = "?obj_id=" + data.id + "&obj_type=unit"
            var type = jsonJudgeNotDefined(data, "data.type.code")
            var setting = {
                cancel: function (index, layero) {
                    _this.returnList(type)
                }
            }
            parent.layerOpenFun(width, height, title, url + urlData, setting)
        },
        goToIncident: function (data) {
            //console.log(data.id)
            var width = '960px'
            var height = '500px'
            var title = "事件"
            var url = "/" + getProjectName() + "/app/servgroup/incidentUnit"
            var urlData = "?obj_id=" + data.id + "&obj_type=mysql"
            parent.layerOpenFun(width, height, title, url + urlData)
        },
        returnList: function (typeRefres) {
            this.btnClick(this.btnReplaceList.refresh, true, 0, typeRefres)
        },
        handlePageChange: function (code) {
            if (XEUtils.isUndefined(code)) {
                for (var i = 0; i < this.unitList.length; i++) {
                    this.handlePageChange(this.unitList[i].code)
                }
            } else {
                var startNum = (this.tablePage[code].currentPage - 1) * this.tablePage[code].pageSize;
                var finishNum = startNum + this.tablePage[code].pageSize;
                this.tablePage[code].totalResult = this.searchData[code].length
                if (finishNum > this.tablePage[code].totalResult) {
                    finishNum = this.tablePage[code].totalResult
                }
                this.tableData[code] = this.sortData[code].slice(startNum, finishNum)

                commonGoToAssignPage(this, code)

                this.$refs[code + 'Table'][0].loadData(this.tableData[code])
                if (dbClickData !== null) {
                    let numTemp = XEUtils.findIndexOf(this.tableDataAll[code], item => item.id === dbClickData.id)
                    if (numTemp !== -1) {
                        this.$refs[code + 'Table'][0].setCheckboxRow(this.tableDataAll[code][numTemp], true)
                        dbClickData = null
                    }
                }
            }
            this.table_heighth = commonOnresize()
        },
        unitHandleChange: function (array) {
            var code = XEUtils.last(array)
            if (!XEUtils.isUndefined(code)) {
                if (XEUtils.isUndefined(this.tableDataAll[code])) {
                    this.btnClick(this.btnReplaceList.refresh, true, 0, code)
                }
            }
        },
        getUnitName: function (type) {
            let name = ""
            let index = XEUtils.findIndexOf(this.unitList, item => item.code === type)

            name = index > -1 ? this.unitList[index].name : "数据库"
            this.typeName = name

            return name
        },
        startAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/units/" + getCheckboxTablerId.id + "/start", function (response) {
                successFun(response, getCheckboxTablerId);
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        stopAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/units/" + getCheckboxTablerId.id + "/stop", function (response) {
                successFun(response, getCheckboxTablerId);
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        updateRoleAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/units/" + getCheckboxTablerId.id + "/role/master", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        rebuildInitAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/units/" + getCheckboxTablerId.id + "/rebuild_init", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, {})
        },
        usersCheck: function (row, openType) {
            var _this = this
            var width = '400px'
            var height = '200px'
            switch (openType) {
                case _this.btnReplaceList.terminal:
                case _this.btnReplaceList.service:
                    height = '200px'
                    break
                case _this.btnReplaceList.logView:
                case _this.btnReplaceList.slowLog:
                    width = '450px'
                    height = '300px'
                    break
            }
            var title = "校验"
            var url = "/" + getProjectName() + "/app/servgroup/usersCheck"
            var urlData = "?row_id=" + row.id + "&row_relateId=" + row.relateId + "&obj_type=mysql" + "&open_type=" + openType
            parent.layerOpenFun(width, height, title, url + urlData)
        },
        webSSHOpen: function (row, open_type) {
            var _this = this
            var typeText = open_type === _this.btnReplaceList.terminal ? "终端" : "服务"
            riskText = "将会进入到所选单元的" + typeText + "页面，请谨慎操作！"
            commonConfirm(parent.manageApp, '终端', getRiskLevelHtml(2, riskText)).then(() => {
                sendGet("/" + getProjectName() + "/units/" + row.id + "/terminals", function (response) {
                    var responseData = response.data.data
                    var url = "/app/servgroup/webSSH/" + row.id
                    var urlData = "?addr=" + responseData.addr + "&sessionId=" + responseData.sessionId +
                        "&scheme=" + responseData.scheme + "&openType=" + open_type

                    var urlName = typeText + "(" + row.relateId + ")"
                    var json = {
                        code: url + urlData,
                        name: urlName,
                    }
                    if (!XEUtils.isEmpty(responseData.addr) && !XEUtils.isEmpty(responseData.sessionId) && !XEUtils.isEmpty(responseData.scheme)) {
                        window.top.indexApp.menuClick(json, null, false)
                    }/*else{
                	operationCompletion(_this, '', 'error')
                }*/
                    layer.closeAll('loading');
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, null)
            }).catch(() => {
            });
        },
        logViewOpen: function (row, closeTime) {
            var _this = this
            sendGet("/" + getProjectName() + "/units/" + row.id + "/terminals", function (response) {
                var responseData = response.data.data
                var url = "/app/servgroup/logView/" + row.id
                var urlData = "?addr=" + responseData.addr + "&sessionId=" + responseData.sessionId + "&scheme=" + responseData.scheme + "&closeTime=" + closeTime
                var urlName = "日志查看(" + row.relateId + ")"
                var json = {
                    code: url + urlData,
                    name: urlName,
                }
                if (!XEUtils.isEmpty(responseData.addr) && !XEUtils.isEmpty(responseData.sessionId) && !XEUtils.isEmpty(responseData.scheme)) {
                    window.top.indexApp.menuClick(json, null, false)
                }/*else{
            	operationCompletion(_this, '', 'error')
            }*/
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        slowLogOpen: function (row, closeTime) {
            var _this = this
            sendGet("/" + getProjectName() + "/units/" + row.id + "/terminals", function (response) {
                var responseData = response.data.data
                var url = "/app/servgroup/slowLog/" + row.id
                var urlData = "?addr=" + responseData.addr + "&sessionId=" + responseData.sessionId + "&scheme=" + responseData.scheme + "&closeTime=" + closeTime
                var urlName = "慢日志(" + row.relateId + ")"
                var json = {
                    code: url + urlData,
                    name: urlName,
                }
                if (!XEUtils.isEmpty(responseData.addr) && !XEUtils.isEmpty(responseData.sessionId) && !XEUtils.isEmpty(responseData.scheme)) {
                    window.top.indexApp.menuClick(json, null, false)
                }/*else{
            	operationCompletion(_this, '', 'error')
            }*/
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        monitorOpen: function (row) {
            var _this = this
            sendGet("/" + getProjectName() + "/units/" + row.id + "/monitor", function (response) {
                var url = response.data.data
                var urlName = "单元监控(" + row.relateId + ")"
                var json = {
                    code: url,
                    name: urlName,
                }
                if (!XEUtils.isEmpty(url)) {
                    window.top.indexApp.menuClick(json, null, false)
                } else {
                    operationCompletion(_this, 'URL地址为空！', 'error')
                }
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        monitorServiceOpen: function (row) {
            var _this = this
            sendGet("/" + getProjectName() + "/units/" + row.id + "/monitor?type=service", function (response) {
                var url = response.data.data
                var urlName = "服务监控(" + row.relateId + ")"
                var json = {
                    code: url,
                    name: urlName,
                }
                if (!XEUtils.isEmpty(url)) {
                    window.top.indexApp.menuClick(json, null, false)
                } else {
                    operationCompletion(_this, 'URL地址为空！', 'error')
                }
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/serv_groups/mysql/" + rowId, function (response) {
                successFun(response, getCheckboxTablerId);
                layer.closeAll('loading');
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        cancelBtn: function (data) {
            var _this = this
            commonConfirm(_this, '任务取消确认', getHintText('取消')).then(() => {
                sendPut("/" + getProjectName() + "/tasks/" + data.task.id + "/cancel", function (response) {
                    layer.closeAll('loading')
                    _this.refreshSaveData.push(_this.selectedData)
                    operationCompletion(_this, "操作成功！")
                    _this.returnList()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, "error")
                }, null)
            }).catch(() => {
            });
        },
        btnClick: function (code, array, getCheckboxTablerId, type) {
            var _this = this
            var getCheckboxTablerNum = 0
            let rowType = ""
            if (this.$refs && this.$refs[type + 'Table']) {
                var getCheckboxTablerData = this.$refs[type + 'Table'][0].getCheckboxRecords()
                getCheckboxTablerNum = getCheckboxTablerData.length
            }
            if (!array) {
                getCheckboxTablerData = [getCheckboxTablerId]
                this.$refs[type + 'Table'][0].clearCheckboxRow()
                setTimeout(function () {
                    _this.$refs[type + 'Table'][0].setCheckboxRow(getCheckboxTablerId, true)
                }, 1);
                _this.selectChangeEvent({checked:true, records:getCheckboxTablerData, row:getCheckboxTablerId})
            }
            if (getCheckboxTablerNum === 1 && array) {
                array = false
                getCheckboxTablerId = getCheckboxTablerData[0]
            }
            if (code !== this.btnReplaceList.refresh) {
                this.refreshSaveData = []
                this.searchData = {}
                rowType = jsonJudgeNotDefined(getCheckboxTablerData[0], "json.type.code")
            }

            this.updateStatus = '';
            this.rowData = getCheckboxTablerId;

            var width = ''
            var height = ''
            var title = ""
            var url = ""
            var urlData = ""
            let confirmsName = array ? ['单元'] : getCheckboxTablerId.relateId
            let riskText = ""
            let setting = {
                customClass: "customizeWidth510pxClass"
            }
            let rowTypeName = _this.getUnitName(rowType)
            //console.log(code, array, getCheckboxTablerId.id)
            /*if (code !== this.btnReplaceList.refresh) {
                this.$refs[type + 'Table'][0].clearRowExpand()
            }*/
            switch (code) {
                case this.btnReplaceList.start://启动
                	commonConfirm(parent.manageApp, '启动确认', getHintText('启动', confirmsName)).then(() => {
                        sendAll(_this.startAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
                            if (errorArray.length === 0) {
                                operationCompletion(_this, "操作成功！")
                                _this.returnList(type)
                            } else {
                                _this.refreshSaveData = []
                                operationCompletion(_this, errorMsg(errorArray, array), 'error')
                            }
                        })
                    }).catch(() => {
                    });
                    break;

                case this.btnReplaceList.stop://停止
                   riskText = "将运行状态" + rowTypeName + "单元停止，会导致当前单元无法对外服务。"
                    commonConfirm(parent.manageApp, '停止确认', getRiskLevelHtml(2, riskText) + getHintText('停止', confirmsName), setting).then(() => {
                        sendAll(_this.stopAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
                            if (errorArray.length === 0) {
                                operationCompletion(_this, "操作成功！")
                                _this.returnList()
                            } else {
                                operationCompletion(_this, errorMsg(errorArray, array), 'error')
                            }
                        })
                    }).catch(() => {
                    });
                    break;

                case this.btnReplaceList.updateRole://角色设置//设置主
                    riskText = "将选中的单元设置为主节点，并且其他的单元设置为从节点，该操作会重新搭建复制关系。"
                    commonConfirm(parent.manageApp, '设置主确认', getRiskLevelHtml(2, riskText) + getHintText('设置主', confirmsName), setting).then(() => {
                        sendAll(_this.updateRoleAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
                            if (errorArray.length === 0) {
                                operationCompletion(_this, "操作成功！")
                                _this.returnList()
                            } else {
                                operationCompletion(_this, errorMsg(errorArray, array), 'error')
                            }
                        })
                    }).catch(() => {
                    });
                    break;

                case this.btnReplaceList.rebuild://重建
                    width = "355px"
                    height = "222px"
                    title = "重建"
                    url = "/" + getProjectName() + "/app/servgroup/mysqls/manageTab/mysql/rebuild"
                    if (type === "mysql") {
                        url += "Mysql"
                    	width = "780px"
                        height = "430px"
                    }
                    urlData = "?unitId=" + getCheckboxTablerId.id + "&clusterId=" + jsonJudgeNotDefined(getCheckboxTablerId, "data.host.cluster.id") +
                        "&diskType=" + jsonJudgeNotDefined(getCheckboxTablerId, "data.diskType.code") +
                        "&hostIp=" + jsonJudgeNotDefined(getCheckboxTablerId, "data.host.ip") +
                        "&rowId=" + rowId + "&arrayLength=" + _this.tableDataAll.length
                    this.selectedData = getCheckboxTablerId
                    parent.layerOpenFun(width, height, title, url + urlData)
                    break;

                case this.btnReplaceList.rebuildInit://重建初始化
                    if (type === "mysql") {
                        width = '779px'
                        height = '453px'
                        title = "重建初始化"
                        url = "/" + getProjectName() + "/app/servgroup/mysqls/manageTab/mysql/rebuildInit"
                        urlData = "?unitId=" + getCheckboxTablerId.id + "&source=unit" + "&servGroupId=" + rowId
                        _this.selectedData = getCheckboxTablerId
                        parent.layerOpenFun(width, height, title, url + urlData)
                    }else {
                        commonConfirm(parent.manageApp, '重建初始化确认', /*getRiskLevelHtml(4, riskText) + */getHintText('重建初始化', confirmsName)).then(() => {
                            sendAll(_this.rebuildInitAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
                                if (errorArray.length === 0) {
                                    operationCompletion(_this, "操作成功！")
                                    _this.returnList()
                                } else {
                                    operationCompletion(_this, errorMsg(errorArray, array), 'error')
                                }
                            })
                        }).catch(() => {
                        });
                    }
                    break;

                case this.btnReplaceList.backup://备份
                    width = "505px"
                    height = "315px"
                    title = "备份"
                    url = "/" + getProjectName() + "/app/servgroup/mysqls/manageTab/mysql/backUp"
                    urlData = "?id=" + getCheckboxTablerId.id
                    this.searchData = getCheckboxTablerId
                    parent.layerOpenFun(width, height, title, url + urlData)
                    break;

                case this.btnReplaceList.restore://还原
                    width = "1035px"
                    height = "355px"
                    title = "还原"
                    url = "/" + getProjectName() + "/app/servgroup/mysqls/manageTab/mysql/restore"
                    urlData = "?unitId=" + getCheckboxTablerId.id + "&unitName=" + getCheckboxTablerId.relateId + "&rowId=" + rowId
                    this.searchData = getCheckboxTablerId
                    parent.layerOpenFun(width, height, title, url + urlData)
                    break;

                case this.btnReplaceList.terminal://终端登录
                case this.btnReplaceList.service://服务登录
                case this.btnReplaceList.logView://日志查看
                case this.btnReplaceList.slowLog://慢日志
                    _this.usersCheck(getCheckboxTablerId, code)
                    break;

                case this.btnReplaceList.monitor://单元监控
                    _this.monitorOpen(getCheckboxTablerId)
                    break;

                case this.btnReplaceList.monitorService://服务监控
                    _this.monitorServiceOpen(getCheckboxTablerId)
                    break;

                case this.btnReplaceList.refresh://刷新
                    this.btnDisabledCreated(type)
                    this.refreshAjaxFun(getCheckboxTablerId, function (response, sendData) {
                        _this.dataDispose(response.data)
                    }, function (error, sendData) {
                        console.log(error)
                        operationCompletion(_this, error.response.data.msg, 'error')
                    })
                    break;
            }
        },
        sortChange: function (column) {
            var type = ""
            var data = jsonJudgeNotDefined(column, 'column.$table.data')
            if (XEUtils.isEmpty(data)) {
                type = column.type
            } else if (XEUtils.isArray(data)) {
                type = jsonJudgeNotDefined(data[0], 'data.type.code')
            }
            if (!XEUtils.isEmpty(type)) {
                if (column.order != null) {
                    this.sortData[type].sort(commonGetSortFun(column.order, column.prop))
                } else {
                    this.sortData[type] = XEUtils.clone(this.searchData[type]);
                }
                this.tablePage[type].currentPage = 1
                //this.$refs.xTable.clearAll()
                this.handlePageChange(type)
            }
        },
        cellMouseenterEvent({row, column}) {
            // console.log(`鼠标进入单元格${column.title}`)
            row.hoverShow = true
            this.$refs[row.type.code + "Table"][0].updateData()
        },
        cellMouseleaveEvent({row, column}) {
            // console.log(`鼠标离开单元格${column.title}`)
            row.hoverShow = false
            this.$refs[row.type.code + "Table"][0].updateData()
        },
        selectAllEvent: function (type) {
            //console.log(checked ? '所有勾选事件' : '所有取消事件', records)
            commonCancelHighlight(this, type)
            var records = this.$refs[type + 'Table'][0].getCheckboxRecords()
            this.isAble('', records, type)
        },
        selectChangeEvent: function ({checked, records, row}) {
            //console.log(checked ? '勾选事件' : '取消事件', records)
            commonCancelHighlight(this, row.type.code)
            this.isAble('', records, row.type.code)
        },
        toggleExpandRow: function (row, key) {
            var _this = this
            _this.$refs[row.type.code + 'Table'][0].toggleRowExpand(row)
        },
        topologyMaintainChange: function (row) {
            var _this = this
            var jsonData = {
                "maintenance": row.topologyMaintain
            }
            var topologyMaintainText = row.topologyMaintain ? "开启" : "关闭"
            commonConfirm(parent.manageApp, '维护模式', '是否确认' + topologyMaintainText + '维护模式"' + row.relateId + '"?').then(() => {
                sendPut("/" + getProjectName() + "/units/mysql/" + row.id + "/maintenance",
                    function (response) {
                        operationCompletion(_this, "操作成功！")
                        _this.returnList(row.type.code)
                    }, function (error) {
                        row.topologyMaintain = !row.topologyMaintain
                        operationCompletion(_this, error.response.data.msg, 'error')
                    }, jsonData)
            }).catch(() => {
                row.topologyMaintain = !row.topologyMaintain
            });
        },
        searchClick: function (type) {
            var data = this.tableDataAll[type].concat();
            var keyArray = ['relateId', 'ip', 'host.cluster.name', 'host.ip', 'cpuCnt', 'memSize', 'dataSize', 'logSize', 'versionValue',
                'roleRunningDisplay', 'roleName', 'roleDisplay', 'actionDisplay', 'statusText', 'topologyMaintainText', 'podState.display']
            this.searchData[type] = searchFun(this.filterName[type], data, keyArray)
            this.sortChange({order: null, type: type})
        },
        tableRowClassName: function (row) {
            return commonTableRowClassName(row)
        }
    }
})
window.onresize = function () {
    mysqlListApp.table_heighth = commonOnresize()
}