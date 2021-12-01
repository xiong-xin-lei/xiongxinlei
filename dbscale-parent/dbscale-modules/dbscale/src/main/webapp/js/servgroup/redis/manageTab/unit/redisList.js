var unitName = parent.manageApp.topoDblclickName
var dbClickData = null
parent.manageApp.topoDblclickName = null
var redisListApp = new Vue({
    el: '#redisList',
    data: {
        title: '数据库',
        table_heighth: '',
        btnList: ttBtnList,
        rowBtnList: rowBtnList,
        disabledValue: {},
        btnReplaceList: {
            start: "btnStart",
            stop: "btnStop",
            terminal: "btnTerminal",
            service: "btnService",
            logView: "btnLogView",
            slowLog: "btnSlowLog",
            monitor: "btnMonitor",
            monitorService: "btnServiceMonitor",
            rebuild: "btnRebuild",
            rebuildInit: "btnRebuildInit",
            backup: "btnBackup",
            restore: "btnRestore",
            updateRole: "btnUpdateRole",
            monitorData: "btnDataMonitor",
            monitorLog: "btnLogMonitor",
            refresh: "refresh"
        },
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
            var data;
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
                        case "unknown":
                            return "<span style='color:" + COLOR_TASK_UNKNOWN + ";border-color:" + COLOR_TASK_UNKNOWN + "'>" + status.actionDisplay + "</span>"
                    }
                } else {
                    return ""
                }
            } else if (type === 'topo') {
                data = jsonJudgeNotDefined(status, "status.roleRunningCode");
                role = jsonJudgeNotDefined(status, "status.replication.role.code");
                if (!!data) {
                    switch (data) {
                        case "Yes":
                            svgElement = getProjectSvg(COLOR_PASSING)
                            break
                        case "No":
                            svgElement = getProjectSvg(COLOR_CRITICAL)
                            break
                        case "Connecting":
                        case "unknown":
                            svgElement = getProjectSvg(COLOR_UNKNOWN)
                            break
                        default:
                            svgElement = ""
                    }
                } else {
                    svgElement = ""
                }
                if (role === "master") {
                    svgElement = getProjectSvg('#2D8CF0')
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
        selectedData: {},
        allAlign: ALLALIGN,
        tableDataAll: [],
        tableData: [],
        searchData: [],
        sortData: [],
        refreshKey: ["id"],
        refreshSaveData: [],
        filterName: '',
        tablePage: {
            currentPage: 1,
            pageSize: LINEFEED_PAGE_SIZE,
            totalResult: 0,
            pageSizes: PAGE_SIZES,
            pageLayouts: PAGE_LAYOUTS
        },
        copyState: null
    },
    created: function () {
        var _this = this
        this.btnDisabledCreated()
        this.btnClick(this.btnReplaceList.refresh, true, 0)
        setTimeout(function () {
            getHtmlMinWidth("redisList", _this, ['xTable'])
        }, 0)
    },
    methods: {
        dataDispose: function (data) {
            var jq_jsonData = [];
            var _this = this
            XEUtils.arrayEach(jsonJudgeNotDefined(data, 'data.data.servs'), (value, index) => {
                _this.copyState = jsonJudgeNotDefined(value, "value.arch.mode.code") === 'single'
                if (value.type.code === 'redis') {
                    XEUtils.arrayEach(value.units, (v, i) => {
                        if (v.type.code === 'redis') {

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
                            var roleName = "";
                            var roleNameShow = "";
                            var roleValue = jsonJudgeNotDefined(v, "v.replication.role");
                            var roleRunningCode = jsonJudgeNotDefined(v, "v.replication.state.code");
                            var roleRunningDisplay = jsonJudgeNotDefined(v, "v.replication.state.display");
                            /* var roleIoRunningDisplay = jsonJudgeNotDefined(v, "v.replication.slaveIoRunning.display");
                             var roleSqlRunningDisplay = jsonJudgeNotDefined(v, "v.replication.slaveSqlRunning.display");
                             var archMode = jsonJudgeNotDefined(v, "v.replication.archMode.display");*/
                            if (!!roleValue) {
                                roleName = roleValue.display
                                if (!roleName) {
                                    roleNameShow = "[" + roleName + "]"
                                } else {
                                    roleNameShow = roleName
                                }
                            }

                            //brShow: 状态列 换行显示标识位 ，当状态为空时 不换行
                            json.brShow = (!XEUtils.isEmpty(jsonJudgeNotDefined(v, "v.state.code")) && !XEUtils.isEmpty(jsonJudgeNotDefined(v, "v.state.display")))
                            json.dataSize = dataSize;
                            json.logSize = logSize;
                            json.memSize = memSize;
                            json.ip = ip;
                            json.versionValue = versionValue;
                            json.diskTypeDisplay = jsonJudgeNotDefined(v, "v.diskType.display");
                            json.actionDisplay = jsonJudgeNotDefined(v, "v.task.action.display") + jsonJudgeNotDefined(v, "v.task.state.display");
                            json.statusText = jsonJudgeNotDefined(v, "v.state.display");
                            json.roleName = roleName;
                            json.roleNameShow = roleNameShow;
                            json.roleRunningCode = roleRunningCode;
                            json.roleRunningDisplay = roleRunningDisplay;
                            /*json.roleIoRunningDisplay = roleIoRunningDisplay;
                            json.roleSqlRunningDisplay = roleSqlRunningDisplay;
                            json.archMode = archMode;*/

                            if (json.id === unitName) {
                                dbClickData = json
                                unitName = null
                            }

                            if (jsonJudgeNotDefined(v, "v.relateId") !== "")
                                jq_jsonData.push(json);
                        }
                    })
                }

            });
            this.tableDataAll = jq_jsonData;
            this.searchData = this.tableDataAll.concat();
            this.sortData = this.searchData.concat();
            this.searchClick({changePage: true})
        },
        btnDisabledCreated: function () {
            var tempArray = XEUtils.values(this.btnReplaceList)
            var tempJson = {}
            for (var i = 0; i < tempArray.length; i++) {
                XEUtils.set(tempJson, tempArray[i], true)
            }
            this.disabledValue = tempJson
        },
        isAble: function (code, data) {
            var _this = this
            if (XEUtils.isArray(data)) {
                if (data.length === 0) {
                    this.btnDisabledCreated()
                } else {
                    XEUtils.clear(_this.disabledValue, false)
                    XEUtils.arrayEach(data, (v, index) => {
                        var tempArray = XEUtils.values(_this.btnReplaceList)
                        for (var i = 0; i < tempArray.length; i++) {
                            if (_this.isAble(tempArray[i], v)) {
                                _this.disabledValue[tempArray[i]] = true
                            }
                        }
                    })
                }
            } else {
                let hostStateCode = jsonJudgeNotDefined(data, 'data.host.state.code')
                switch (code) {
                    case _this.btnReplaceList.rebuild:
                        return hostStateCode !== "passing"

                    case _this.btnReplaceList.rebuildInit:
                    case _this.btnReplaceList.backup:
                    case _this.btnReplaceList.restore:
                    case _this.btnReplaceList.start:
                    case _this.btnReplaceList.stop:
                        return false

                    case _this.btnReplaceList.del:
                        return data.enabled === true

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
            var setting = {
                cancel: function (index, layero) {
                    _this.returnList()
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
            var urlData = "?obj_id=" + data.id + "&obj_type=redis"
            parent.layerOpenFun(width, height, title, url + urlData)
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
            var urlData = "?row_id=" + row.id + "&row_relateId=" + row.relateId + "&obj_type=redis" + "&open_type=" + openType
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
        monitorDataOpen: function (row) {
            var _this = this
            sendGet("/" + getProjectName() + "/units/" + row.id + "/monitor?type=data", function (response) {
                var url = response.data.data
                var urlName = "数据卷监控(" + row.relateId + ")"
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
        monitorLogOpen: function (row) {
            var _this = this
            sendGet("/" + getProjectName() + "/units/" + row.id + "/monitor?type=log", function (response) {
                var url = response.data.data
                var urlName = "日志卷监控(" + row.relateId + ")"
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
        returnList: function () {
            commonRefreshList(this)
        },
        handleSizeCurrentChange: function (value) {
            commonHandleSizeCurrentChange(this, value)
        },
        handlePageChange: function () {
            commonHandlePageChange(this)
            this.table_heighth = commonOnresize()
            if (dbClickData !== null) {
                this.$refs['xTable'].setCheckboxRow(dbClickData, true)
                dbClickData = null
            }
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
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/serv_groups/redis/" + rowId, function (response) {
                successFun(response, getCheckboxTablerId)
                layer.closeAll('loading')
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
        btnClick: function (code, array, getCheckboxTablerId) {
            var _this = this

            var width = ''
            var height = ''
            var title = ""
            var url = ""
            var urlData = ""
            if (this.$refs.xTable) {
                var getCheckboxTablerData = this.$refs.xTable.getCheckboxRecords()
                var getCheckboxTablerNum = getCheckboxTablerData.length
            }
            if (!array) {
                getCheckboxTablerData = [getCheckboxTablerId]
                this.$refs.xTable.clearCheckboxRow()
                setTimeout(function () {
                    _this.$refs.xTable.setCheckboxRow(getCheckboxTablerId, true);
                    _this.selectChangeEvent({checked: true, records: getCheckboxTablerData})
                }, 1)
            }
            if (getCheckboxTablerNum === 1 && array) {
                array = false
                getCheckboxTablerId = getCheckboxTablerData[0]
            }
            if (code !== this.btnReplaceList.refresh) {
                this.selectedData = {}
                this.refreshSaveData = []
            }
            let confirmsName = array ? ['单元'] : getCheckboxTablerId.relateId
            let riskText = ""
            let setting = {
                customClass: "customizeWidth510pxClass"
            }

            switch (code) {
                case this.btnReplaceList.start://启动
                    commonConfirm(parent.manageApp, '启动确认', getHintText('启动', confirmsName), setting).then(() => {
                        sendAll(_this.startAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
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

                case this.btnReplaceList.stop://停止
                    riskText = "将运行状态数据库单元停止，会导致当前单元无法对外服务。"
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

                case this.btnReplaceList.rebuild://重建
                    width = "355px"
                    height = "222px"
                    title = "重建"
                    url = "/" + getProjectName() + "/app/servgroup/redis/manageTab/unit/rebuild"
                    urlData = "?unitId=" + getCheckboxTablerId.id +
                        "&clusterId=" + jsonJudgeNotDefined(getCheckboxTablerId, "data.host.cluster.id") +
                        "&diskType=" + jsonJudgeNotDefined(getCheckboxTablerId, "data.diskType.code") +
                        "&typeRefres=redis" + "&rowId=" + rowId + '&archModes=single' +
                        "&hostIp=" + jsonJudgeNotDefined(getCheckboxTablerId, "data.host.ip")
                    this.searchData = getCheckboxTablerId
                    parent.layerOpenFun(width, height, title, url + urlData)
                    break;

                case this.btnReplaceList.rebuildInit://重建初始化
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
                    break;

                case this.btnReplaceList.backup://备份
                    width = '505px'
                    height = '315px'
                    title = "备份"
                    url = "/" + getProjectName() + "/app/servgroup/redis/manageTab/mysql/backUp"
                    urlData = "?id=" + getCheckboxTablerId.id
                    _this.selectedData = getCheckboxTablerId
                    parent.layerOpenFun(width, height, title, url + urlData)
                    break;

                case this.btnReplaceList.restore://还原
                    width = '1035px'
                    height = '355px'
                    title = "还原"
                    url = "/" + getProjectName() + "/app/servgroup/redis/manageTab/mysql/restore"
                    urlData = "?unitId=" + getCheckboxTablerId.id + "&unitName=" + getCheckboxTablerId.relateId + "&rowId=" + rowId
                    _this.selectedData = getCheckboxTablerId
                    parent.layerOpenFun(width, height, title, url + urlData)
                    break;

                case this.btnReplaceList.updateRole://设置主
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
                    
                case this.btnReplaceList.monitorData://数据卷监控
                    _this.monitorDataOpen(getCheckboxTablerId)
                    break; 
                    
                case this.btnReplaceList.monitorLog://日志卷监控
                    _this.monitorLogOpen(getCheckboxTablerId)
                    break;

                case this.btnReplaceList.refresh://刷新
                    this.btnDisabledCreated()
                    this.refreshAjaxFun(getCheckboxTablerId,
                        function (response, sendData) {
                            _this.dataDispose(response.data)
                        }, function (error, sendData) {
                            console.log(error)
                            operationCompletion(_this, error.response.data.msg, 'error')
                        })
                    break;
            }
        },
        sortChange: function (column, settings) {
            commonSortChange(this, column, settings)
        },
        cellMouseenterEvent({row, column}) {
            // console.log(`鼠标进入单元格${column.title}`)
            row.hoverShow = true
            this.$refs.xTable.updateData()
        },
        cellMouseleaveEvent({row, column}) {
            // console.log(`鼠标离开单元格${column.title}`)
            row.hoverShow = false
            this.$refs.xTable.updateData()
        },
        selectAllEvent: function ({checked, records}) {
            //console.log(checked ? '所有勾选事件' : '所有取消事件', records)
            commonCancelHighlight(this)
            this.isAble('', records)
        },
        selectChangeEvent: function ({checked, records}) {
            //console.log(checked ? '勾选事件' : '取消事件', records)
            commonCancelHighlight(this)
            this.isAble('', records)
        },
        toggleExpandRow: function (row, key) {
            var _this = this
            XEUtils.arrayEach(_this.tableData, (val, index) => {
                if (row[key] === val[key]) {
                    _this.$refs.xTable.toggleRowExpand(_this.tableData[index])
                }
            })
        },
        searchClick: function (settings) {
            let keyArray = ['relateId', 'ip', 'host.cluster.name', 'host.ip', 'cpuCnt', 'memSize', 'dataSize', 'logSize',
                'versionValue', 'roleRunningDisplay', 'roleName', 'actionDisplay', 'statusText', 'podState.display']
            commonSearchClick(this, keyArray, settings)
        },
        tableRowClassName: function (row) {
            return commonTableRowClassName(row)
        }
    }
})
window.onresize = function () {
    redisListApp.table_heighth = commonOnresize()
}