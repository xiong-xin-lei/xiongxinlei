var listApp = new Vue({
    el: '#list',
    data: {
        title: '主机',
        table_heighth: '',
        menu: menu,
        searchKeyWord: searchKeyWord,
        btnList: ttBtnList,
        rowBtnList: rowBtnList,
        disabledValue: {},
        hostJson: {},
        hostList: [],
        btnReplaceList: {
            add: "btnAdd",
            update: "btnUpdate",
            start: "btnEnabled",
            stop: "btnDisabled",
            del: "btnDelete",
            btnIn: "btnIn",
            btnOut: "btnOut",
            monitor: "btnMonitor",
            unit: "btnUnit",
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
                    case this.btnReplaceList.add:
                        return 'el-icon-plus'
                    case this.btnReplaceList.update:
                        return 'el-icon-edit'
                    case this.btnReplaceList.start:
                        return 'el-icon-circle-check'
                    case this.btnReplaceList.stop:
                        return 'el-icon-circle-close'
                    case this.btnReplaceList.del:
                        return 'el-icon-delete'
                    case this.btnReplaceList.btnIn:
                        return 'el-icon-setting'
                    case this.btnReplaceList.btnOut:
                        return 'el-icon-setting'
                    case this.btnReplaceList.refresh:
                        return 'el-icon-refresh'
                    default:
                        return 'el-icon-setting'
                }
            }

        },
        statusShow: function (status, type) {
            if (type == 'state') {
                var data = status.state
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
            } else if (type == 'enabled') {
                var data = status.enabled
                if (data === null) {
                    return ""
                }
                if (data) {
                    return getProjectSvg(COLOR_ENABLED) + "<span style='line-height:14px'>是</span>"
                } else if (!data) {
                    return getProjectSvg(COLOR_DISABLED) + "<span style='line-height:14px'>否</span>"
                }
            } else if (type == 'task') {
                var data = status.task
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
            } else if (type == "role") {
                var data = status.role
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
        updateStatus: '',
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
        var _this = this
        this.btnDisabledCreated()
        this.btnClick(this.btnReplaceList.refresh, true, 0)
        setTimeout(function () {
            getHtmlMinWidth("list", _this, ['xTable'])
        }, 0)
    },
    methods: {
        dataDispose: function (data) {
            var jq_jsonData = []
            var jq_json = {}
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
                if(getSession('storageMode') != 'pvc'){
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
                            width: oneNum
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
                }

                var actionDisplay = "";
                if (v.task !== null) {
                    actionDisplay = v.task.action.display + v.task.state.display
                } else {
                    actionDisplay = "未入库"
                }

                var roleDisplay = ""
                if (!!jsonJudgeNotDefined(v, "v.task.state.code"))
                    roleDisplay = jsonJudgeNotDefined(v, "v.role.display")

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

                var distributableText = "";
                if (v.distributable !== null) {
                    if (v.distributable) {
                    	distributableText = "可分配"
                    } else {
                    	distributableText = "不可分配"
                    }
                }

                //brShow: 状态列 换行显示标识位 ，当状态为空时 不换行
                json.brShow = (!XEUtils.isEmpty(jsonJudgeNotDefined(v, "v.state.code")) && !XEUtils.isEmpty(jsonJudgeNotDefined(v, "v.state.display")))
                json.resourceRates = resourceRates
                json.actionDisplay = actionDisplay
                json.roleDisplay = roleDisplay
                json.stateText = stateText
                json.enabledText = enabledText
                json.distributableText = distributableText

                _this.searchKeyFun(json)

                jq_jsonData.push(json)
            });
            this.tableDataAll = jq_jsonData
            this.searchData = this.tableDataAll.concat()
            this.sortData = this.searchData.concat()
            this.searchClick({changePage: true})
        },
        searchKeyFun: function (data) {
            if (!XEUtils.isEmpty(searchKeyWord)) {
                switch (searchKeyType) {
                    case "highlight":
                        if (data.ip === this.searchKeyWord) {
                            this.refreshSaveData.push(data)
                            searchKeyWord = null
                        }
                        break
                    case "search":
                    default:
                        this.filterName = decodeURI(decodeURI(searchKeyWord))
                        searchKeyWord = null
                }
            }
        },
        btnDisabledCreated: function () {
            var tempArray = XEUtils.values(this.btnReplaceList)
            var tempJson = {}
            for (var i = 0; i < tempArray.length; i++) {
                XEUtils.set(tempJson, tempArray[i], true)
            }
            tempJson[this.btnReplaceList.add] = false
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
                switch (code) {
                    case this.btnReplaceList.add:
                        return false

                    case this.btnReplaceList.update:
                        if (data.relateId == null && (data.task == null || (data.task != null && data.task.state.code != "running"))) {
                            return false;
                        } else if (data.task != null && data.task.state.code != "running" && data.inSuccess == true) {
                            return false;
                        } else {
                            return true;
                        }

                    case this.btnReplaceList.start:
                        if (data.task !== null) {
                            if (data.task.state.code !== "running" && data.inSuccess === true && data.enabled !== true) {
                                return false
                            } else {
                                return true
                            }
                        } else {
                            return true;
                        }

                    case this.btnReplaceList.stop:
                        if (data.task !== null) {
                            if (data.task.state.code !== "running" && data.inSuccess === true && data.enabled !== false) {
                                return false
                            } else if (data.task.state.code === "timeout") {
                                return false
                            } else {
                                return true
                            }
                        } else {
                            return true;
                        }

                    case this.btnReplaceList.del:
                        if (data.relateId == null && (data.task == null || (data.task != null && data.task.state.code != "running"))) {
                            return false
                        } else {
                            return true
                        }

                    case this.btnReplaceList.btnIn:
                        if (data.relateId == null && (data.task == null || (data.task != null && data.task.state.code != "running"))) {
                            return false
                        } else {
                            return true
                        }

                    case this.btnReplaceList.btnOut:
                        if (data.task != null  && data.task.action.code != "out" && ((data.task.state.code != "running" && data.enabled == false) || data.task.state.code === "timeout")) {
                            return false
                        } else {
                            return true
                        }

                    case this.btnReplaceList.unit:
                        return !data.inSuccess;

                    case this.btnReplaceList.monitor:
                        return !data.inSuccess;

                    case this.btnReplaceList.refresh:
                        return false
                }
            }
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
        },
        addAjaxFun: function (getCheckboxTablerId) {
            var width = '560px'
            var height = '350px'
            var title = "注册"
            var url = "/" + getProjectName() + "/app/resource/hostsAdd"
            var urlData = ""
            layerOpenFun(width, height, title, url + urlData)
        },
        updataAjaxFun: function (getCheckboxTablerId) {
            this.selectedData = getCheckboxTablerId
            var width = '790px'
            var height = '490px'
            var title = "编辑"
            var url = "/" + getProjectName() + "/app/resource/hosts/edit/" + getCheckboxTablerId.id
            var urlData = ""
            layerOpenFun(width, height, title, url + urlData)
        },
        updataLittleAjaxFun: function (getCheckboxTablerId) {
            this.selectedData = getCheckboxTablerId
            var width = '640px'
            var height = '400px'
            var title = "编辑"
            var url = "/" + getProjectName() + "/app/resource/hosts/editLittle/" + getCheckboxTablerId.id
            var urlData = ""
            layerOpenFun(width, height, title, url + urlData)
        },
        /*inAjaxFun: function (getCheckboxTablerId) {
            var width = ''
            var height = ''
            var title = "入库"
            var url = "/" + getProjectName() + "/app/resource/hosts/in/" + getCheckboxTablerId
            var urlData = ""
            if (getCheckboxTablerId) {
                width = '445px'
                height = '278px'
                layerOpenFun(width, height, title, url + urlData)
            } else {
                width = '528px'
                height = '330px'
                layerOpenFun(width, height, title, url + urlData)
            }
        },
        inIdentificationAjaxFun: function (urlData) {
            var width = '528px'
            var height = '330px'
            var title = "入库校验"
            var url = "/" + getProjectName() + "/app/resource/hosts/in/identification"
            var settings = {
                maxmin: false
            }
            layerOpenFun(width, height, title, url + urlData, settings)
        },
        outAjaxFun: function (getCheckboxTablerId) {
            var width = ''
            var height = ''
            var title = "出库"
            var url = "/" + getProjectName() + "/app/resource/hosts/out/" + getCheckboxTablerId
            var urlData = ""
            if (getCheckboxTablerId) {
                width = '445px'
                height = '278px'
                layerOpenFun(width, height, title, url + urlData)
            } else {
                width = '528px'
                height = '330px'
                layerOpenFun(width, height, title, url + urlData)
            }
        },*/
        goToIncident: function (data) {
            //console.log(data.id)
            var width = '960px'
            var height = '600px'
            var title = "事件"
            var url = "/" + getProjectName() + "/app/resource/hosts/incident"
            var urlData = "?obj_id=" + data.id
            layerOpenFun(width, height, title, url + urlData)
        },
        monitorOpen: function (row) {
            var _this = this
            sendGet("/" + getProjectName() + "/hosts/" + row.id + "/monitor", function (response) {
                var url = response.data.data
                var urlName = "监控(" + row.ip + ")"
                var json = {
                    code: url,
                    name: urlName,
                }
                if (!XEUtils.isEmpty(url)) {
                    window.top.indexApp.menuClick(json, null, false)
                } else {
                    operationCompletion(_this, 'URL地址为空！', 'error')
                }
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        inAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/hosts/" + getCheckboxTablerId.id + "/in", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        outAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/hosts/" + getCheckboxTablerId.id + "/out", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        startAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/hosts/" + getCheckboxTablerId.id + "/enabled", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        stopAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/hosts/" + getCheckboxTablerId.id + "/disabled", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        deleteAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendDelete("/" + getProjectName() + "/hosts/" + getCheckboxTablerId.id, function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(_this.tablePage.currentPage)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/hosts?site_id=" + getSession("siteId"), function (response) {
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
            if (this.$refs.xTable) {
                var getCheckboxTablerData = this.$refs.xTable.getCheckboxRecords()
                var getCheckboxTablerNum = getCheckboxTablerData.length
            }
            if (!array) {
                getCheckboxTablerData = [getCheckboxTablerId]
                this.$refs.xTable.clearCheckboxRow()
                setTimeout(function () {
                    _this.$refs.xTable.setCheckboxRow(getCheckboxTablerId, true)
                    _this.selectChangeEvent({checked: true, records: getCheckboxTablerData})
                }, 1);
            }
            if (getCheckboxTablerNum === 1 && array) {
                array = false
                getCheckboxTablerId = getCheckboxTablerData[0]
            }
            this.hostJson = getCheckboxTablerId
            this.hostList = getCheckboxTablerData
            if (code !== this.btnReplaceList.refresh) {
                this.selectedData = {}
                this.refreshSaveData = []
            }
            this.updateStatus = ''
            let confirmsName = array ? ['主机'] : getCheckboxTablerId.ip

            switch (code) {
                case this.btnReplaceList.add://新增
                    this.addAjaxFun(getCheckboxTablerId)
                    break;

                case this.btnReplaceList.update://编辑
                    if (getCheckboxTablerId.relateId == null) {
                        this.updataAjaxFun(getCheckboxTablerId)
                    } else if (getCheckboxTablerId.enabled != null) {
                        if (getCheckboxTablerId.enabled) {
                            commonConfirm(_this, '编辑确认', getHintText('编辑', confirmsName)).then(() => {
                                this.updateStatus = getCheckboxTablerId.id
                                this.stopAjaxFun(getCheckboxTablerId,
                                    function (data, sendData) {
                                        _this.updataLittleAjaxFun(getCheckboxTablerId)
                                        layer.closeAll('loading')
                                    }, function (error, sendData) {
                                        operationCompletion(_this, getCheckboxTablerId.name + error.response.data.msg, 'error')
                                    })
                            }).catch(() => {
                            });
                        } else {
                            this.updataLittleAjaxFun(getCheckboxTablerId)
                        }
                    }
                    break;

                case this.btnReplaceList.btnIn://入库
                    // this.inAjaxFun(array)
                    commonConfirm(this, '入库确认', getHintText('入库', confirmsName)).then(() => {
                        sendAll(_this.inAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
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

                case this.btnReplaceList.btnOut://出库
                    // this.outAjaxFun(array)
                    commonConfirm(this, '出库确认', getHintText('出库', confirmsName)).then(() => {
                        sendAll(_this.outAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
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

                case this.btnReplaceList.unit://单元
                    let id = ''
                    let json = {}
                    menu.subMenu.sSubMenu = {
                        name: "单元信息",
                        ip: getCheckboxTablerId.ip
                    }
                    XEUtils.arrayEach(this.rowBtnList, (v, i) => {
                        if (v.code === _this.btnReplaceList.unit) {
                            id = v.id
                        }
                    })
                    let urls = "/app/resource/hosts/unit/" + getCheckboxTablerId.id
                    let urlData = "?id=" + id + "&hostId=" + getCheckboxTablerId.id + "&menu=" + encodeURIComponent(JSON.stringify(menu), "utf-8")
                    json = {
                        code: urls + urlData,
                        name: getCheckboxTablerId.ip + " (单元)",
                    }
                    window.top.indexApp.menuClick(json, null, false)
                    break;

                case this.btnReplaceList.monitor://监控
                    _this.monitorOpen(getCheckboxTablerId)
                    break;

                case this.btnReplaceList.start://启用
                    commonConfirm(this, '启用确认', getHintText('启用', confirmsName)).then(() => {
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

                case this.btnReplaceList.stop://停用
                    commonConfirm(this, '停用确认', getHintText('停用', confirmsName)).then(() => {
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

                case this.btnReplaceList.del://注销
                    commonConfirm(_this, '注销确认', getHintText('注销', confirmsName)).then(() => {
                        sendAll(_this.deleteAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
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
        loadContentMethod({row}) {
            var _this = this
            return new Promise(resolve => {
                sendGet("/" + getProjectName() + "/hosts/" + row.id, function (response) {
                    layer.closeAll('loading')
                    row.childRows = _this.childDataDispose(response.data.data, row.relateId)
                    resolve()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, null)
            })
        },
        toggleExpandRow: function (row) {
            var _this = this
            XEUtils.arrayEach(_this.tableData, (val, index) => {
                if (row.id === val.id) {
                    _this.$refs.xTable.toggleRowExpand(_this.tableData[index])
                }
            })
        },
        childDataDispose: function (v, relateId) {
            var jq_json = {}
            var diskType = []
            var resourceMax = [{
                name: "单元",
                value: v.unit.maxCnt
            }, {
                name: "CPU",
                value: v.cpu.maxUsage + "%"
            }, {
                name: "内存",
                value: v.mem.maxUsage + "%"
            }];
            if (v.hdd != null) {
                var hddJson = {
                    name: "存储",
                    value: v.hdd.maxUsage + "%"
                }
                resourceMax.push(hddJson)
            } else if (v.ssd != null) {
                var ssdJson = {
                    name: "存储",
                    value: v.ssd.maxUsage + "%"
                }
                resourceMax.push(ssdJson)
            }


            if (relateId === null) {
                if (v.hdd != null) {
                    hddPaths = arrayMerger(v.hdd.paths)
                    diskMaxUsage = v.hdd.maxUsage + "%"
                    diskType.push('机械盘')
                }
                if (v.ssd != null) {
                    ssdPaths = arrayMerger(v.hdd.paths)
                    diskMaxUsage = v.ssd.maxUsage + "%"
                    diskType.push('固态盘')
                }
                if (v.remoteStorage != null) {
                    remoteStorageName = v.remoteStorage.name
                    diskMaxUsage = v.remoteStorage.maxUsage + "%"
                    diskType.push('外置存储')
                }
                jq_json = {
                    "siteName": v.site.name,
                    "businessAreaName": v.businessArea.name,
                    "clusterName": v.cluster.name,
                    "ip": v.ip,
                    "room": v.room,
                    "seat": v.seat,
                    "role": jsonJudgeNotDefined(v, 'v.role.display'),
                    "resourceMax": resourceMax,
                    "unitMaxCnt": v.unit.maxCnt,
                    "maxUsage": v.maxUsage,
                    "cpuMaxUsage": v.cpu.maxUsage + "%",
                    "memMaxUsage": v.mem.maxUsage + "%",
                    "createdTimestamp": v.created.timestamp,
                    "enabled": v.enabled,
                    "diskType": arrayMerger(diskType),
                    "description": v.description
                };
            } else {
                var actionDisplay = "";
                if (v.task !== null) {
                    actionDisplay = v.task.action.display + v.task.state.display
                }
                jq_json = {
                    "siteName": v.site.name,
                    "businessAreaName": v.businessArea.name,
                    "clusterName": v.cluster.name,
                    "ip": v.ip,
                    "room": v.room,
                    "seat": v.seat,
                    "resourceMax": resourceMax,
                    "maxUsage": v.maxUsage,
                    "createdTimestamp": v.created.timestamp,
                    "actionDisplay": actionDisplay,
                    "enabled": v.enabled,
                    "state": v.state,
                    "unit": v.unit,
                    "cpu": v.cpu,
                    "mem": v.mem,
                    "podCnt": jsonJudgeNotDefined(v, 'v.pod.cnt'),
                    "maxPodCnt": jsonJudgeNotDefined(v, 'v.pod.maxCnt'),
                    "kernelVersion": v.kernelVersion,
                    "osImage": v.osImage,
                    "operatingSystem": v.operatingSystem,
                    "containerRuntimeVersion": v.containerRuntimeVersion,
                    "kubeletVersion": v.kubeletVersion,
                    "kubeProxyVersion": v.kubeProxyVersion,
                    "task": v.task,
                    "inSuccess": v.inSuccess,
                    "description": v.description
                };
                if (v.input != null) {
                    jq_json.inputTimestamp = v.input.timestamp
                }
                if (v.hdd != null) {
                    jq_json.hdd = v.hdd
                    jq_json.ssd = null
                    jq_json.hddPaths = arrayMerger(v.hdd.paths)
                    jq_json.ssdPaths = ""
                    jq_json.diskMaxUsage = v.hdd.maxUsage + "%"
                } else if (v.ssd != null) {
                    jq_json.hdd = null
                    jq_json.ssd = v.ssd
                    jq_json.hddPaths = ""
                    jq_json.ssdPaths = arrayMerger(v.ssd.paths)
                    jq_json.diskMaxUsage = v.ssd.maxUsage + "%"
                }
                if (v.remoteStorage != null) {
                    jq_json.remoteStorageName = v.remoteStorage.name
                } else {
                    jq_json.remoteStorageName = ""
                }
            }

            return jq_json;
        },
        searchClick: function (settings) {
            let keyArray = ['name', 'ip', 'businessArea.name', 'cluster.name', 'architecture.display', 'resourceRates<>.value',
                'remoteStorage.name', 'stateText', 'roleDisplay', 'enabledText', 'distributableText', 'actionDisplay']
            commonSearchClick(this, keyArray, settings)
        },
        tableRowClassName: function (row) {
            return commonTableRowClassName(row)
        },
        actionDisplayClick: function (data) {
            //console.log(data.id)
        	if(data.task === null)
        		return false
            var _this = this
            var width = '1060px'
            var height = '600px'
            var title = "任务列表"
            var url = "/" + getProjectName() + "/app/task"
            var urlData = "?obj_id=" + data.id + "&obj_type=host"
            var settings = {
                cancel: function (index, layero) {
                    _this.returnList()
                }
            }
            layerOpenFun(width, height, title, url + urlData, settings)
        },
        aAssociatedJumpClick: function (name, pageType) {
            var searchName = name
            var json = {}
            var menuSkip = {}
            var urlName = ""
            var menuLists = parent.parent.window.indexApp.menuLists
            var menuId;
            XEUtils.arrayEach(menuLists, (menuList, menuIndex) => {
                XEUtils.arrayEach(menuList.childrens, (subMenuList, subMenuIndex) => {
                    if (subMenuList.code === pagePathReturn(pageType)) {
                        menuSkip = {
                            name: menuList.name,
                            icon: menuList.icon,
                            subMenu: {
                                name: subMenuList.name
                            }
                        }
                        urlName = subMenuList.name
                        menuId = subMenuList.id
                    }
                })
            })
            var url = pagePathReturn(pageType) + "?id=" + menuId + "&menu=" + encodeURIComponent(JSON.stringify(menuSkip), "utf-8") + "&searchKeyWord=" + searchName + "&t=" + (new Date()).getTime()
            json = {
                code: url,
                name: urlName,
            }
            var tab = {
                name: url
            }
            parent.parent.window.indexApp.tabsJump(tab)
            parent.parent.window.indexApp.menuClick(json, null, false)
        }
    }
})
window.onresize = function () {
    listApp.table_heighth = commonOnresize()
}