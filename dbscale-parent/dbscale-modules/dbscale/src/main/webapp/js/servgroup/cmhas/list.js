function delRulesName(rule, value, callback) {
    if (listApp.delForm.name === listApp.rowData.name) {
        callback();
    } else {
        callback(new Error('服务名错误'));
    }
}

var listApp = new Vue({
    el: '#list',
    data: {
        title: '服务',
        table_heighth: '',
        menu: menu,
        searchKeyWord: searchKeyWord,
        rowData: {},
        btnList: ttBtnList,
        rowBtnList: rowBtnList,
        disabledValue: {},
        btnReplaceList: {
            start: "btnStart",
            stop: "btnStop",
            scaleUp: "btnScaleUp",
            scaleUpstorage: "btnScaleUpStorage",
            imageUpdate: "btnImageUpdate",
            backup: "btnBackup",
            del: "btnDelete",
            manage: "btnManage",
            refresh: "refresh"
        },
        selectedData: {},
        delFormShow: false,
        goTourlDialogShow: false,
        delForm: {
            name: ""
        },
        delRules: {
            name: [{
                required: true,
                message: '请输入该服务的服务名称',
                trigger: 'blur'
            },
                {
                    validator: delRulesName,
                    trigger: ['change', 'blur']
                }
            ]
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
            if (type === 'state') {
                var data = status.status
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
                        case "ready":
                            return "<span style='color:" + COLOR_TASK_READY + ";border-color:" + COLOR_TASK_READY + "'>" + status.actionDisplay + "</span>"
                        case "unknown":
                            return "<span style='color:" + COLOR_TASK_UNKNOWN + ";border-color:" + COLOR_TASK_UNKNOWN + "'>" + status.actionDisplay + "</span>"
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
        refreshKey: ["id"],
        refreshSaveData: [],
        ogAutoExamine: false,
        ogAutoExecute: false,
        filterName: '',
        tablePage: {
            currentPage: 1,
            pageSize: LINEFEED_PAGE_SIZE,
            totalResult: 0,
            pageSizes: PAGE_SIZES,
            pageLayouts: PAGE_LAYOUTS
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
            var jq_jsonData = [];
            var jq_json = {};
            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))

                var versionJson = jsonJudgeNotDefined(v, "v.image.version")
                var version = ''
                if (!!versionJson) {
                    version = versionJson.major + "." + versionJson.minor + "." + versionJson.patch + "." + versionJson.build
                }

                var dataSize = jsonJudgeNotDefined(v, "v.dataSize");
                if (!!dataSize || dataSize === 0) {
                    dataSize = dataSize + "G"
                }

                var logSize = jsonJudgeNotDefined(v, "v.logSize");
                if (!!logSize || logSize === 0) {
                    logSize = logSize + "G"
                }

                let archModeDisplay = jsonJudgeNotDefined(v, "v.arch.mode.display")
                let unitCntText = ""
                if (!XEUtils.isEmpty(archModeDisplay) && archModeDisplay !== "单节点") {
                    unitCntText = XEUtils.toInteger(jsonJudgeNotDefined(v, "v.arch.unitCnt")) - 1
                    unitCntText += "副本"
                }

                let highAvailableText = ""
                if (!!jsonJudgeNotDefined(v, 'v.highAvailable')) {
                    highAvailableText = "高可用"
                }

                //brShow: 状态列 换行显示标识位 ，当状态为空时 不换行
                json.brShow = (!XEUtils.isEmpty(jsonJudgeNotDefined(v, "v.state.code")) && !XEUtils.isEmpty(jsonJudgeNotDefined(v, "v.state.display")))
                json.version = version
                json.unitCntText = unitCntText
                json.highAvailableText = highAvailableText
                json.dataSize = dataSize
                json.logSize = logSize
                json.disabledFlag = v.flag
                json.status = v.state
                json.ownerName = ownerNameDispose(v.owner.name, v.owner.username)
                json.statusText = jsonJudgeNotDefined(v, "v.state.display")
                json.actionDisplay = jsonJudgeNotDefined(v, "v.task.action.display") + jsonJudgeNotDefined(v, "v.task.state.display")
                json.childRows = []

                jq_jsonData.push(json);
            });
            this.tableDataAll = jq_jsonData;
            this.searchData = this.tableDataAll.concat();
            this.sortData = this.searchData.concat();
            this.searchClick({changePage: true})
            this.jumpSearch()
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
                var actionCode = jsonJudgeNotDefined(data, "data.task.action.code")
                var stateCode = jsonJudgeNotDefined(data, "data.task.state.code")
                switch (code) {
                    case this.btnReplaceList.imageUpdate:
                    case this.btnReplaceList.scaleUp:
                    case this.btnReplaceList.scaleUpstorage:
                    case this.btnReplaceList.start:
                    case this.btnReplaceList.stop:
                    case this.btnReplaceList.manage:
                    case this.btnReplaceList.refresh:
                    case this.btnReplaceList.backup:
                        return stateCode === "running" || !data.disabledFlag;
                    case this.btnReplaceList.del:
                        return false
                    default:
                        return false
                }
            }
        },
        userListView: function (successFun) {
            var _this = this
            var userName = getSession("UserName")
            sendGet("/" + getProjectName() + "/users/" + userName, function (response) {
                var data = response.data.data
                _this.ogAutoExamine = data.ogAutoExamine
                _this.ogAutoExecute = data.ogAutoExecute
                layer.closeAll('loading')
                successFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        actionDisplayClick: function (data) {
            var _this = this
            var width = '960px'
            var height = '600px'
            var title = "任务列表"
            var url = "/" + getProjectName() + "/app/task"
            var urlData = "?obj_id=" + data.id + "&obj_type=servGroup"
            var settings = {
                cancel: function (index, layero) {
                    _this.returnList()
                }
            }
            layerOpenFun(width, height, title, url + urlData, settings)
        },
        goToIncident: function (data) {
            var width = '960px'
            var height = '600px'
            var title = "事件"
            var url = "/" + getProjectName() + "/app/servgroup/incident"
            var urlData = "?obj_id=" + data.id + "&obj_type=cmha"
            layerOpenFun(width, height, title, url + urlData)
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
        scaleUpAjaxFun: function (getCheckboxTablerId, width, height, url) {
            var title = "计算扩容"
            this.selectedData = getCheckboxTablerId
            layerOpenFun(width, height, title, url)
        },
        scaleUpstorageAjaxFun: function (getCheckboxTablerId, width, height, url) {
            var title = "存储扩容"
            this.selectedData = getCheckboxTablerId
            layerOpenFun(width, height, title, url)
        },
        imageUpdateAjaxFun: function (getCheckboxTablerId, width, height, url) {
            var title = "升级"
            this.selectedData = getCheckboxTablerId
            layerOpenFun(width, height, title, url)
        },
        backupAjaxFun: function (getCheckboxTablerId, width, height, url) {
            var title = "备份"
            this.selectedData = getCheckboxTablerId
            layerOpenFun(width, height, title, url)
        },
        startAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/serv_groups/" + getCheckboxTablerId.id + "/start", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        stopAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/serv_groups/" + getCheckboxTablerId.id + "/stop", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        deleteAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendDelete("/" + getProjectName() + "/serv_groups/" + getCheckboxTablerId.id, function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(_this.tablePage.currentPage)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/serv_groups/cmha?site_id=" + getSession("siteId"), function (response) {
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
            this.rowData = getCheckboxTablerId
            var _this = this
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
                    _this.$refs.xTable.setCheckboxRow(getCheckboxTablerId, true)
                    _this.selectChangeEvent({checked: true, records: getCheckboxTablerData})
                }, 1);
            }
            if (getCheckboxTablerNum === 1 && array) {
                array = false
                getCheckboxTablerId = getCheckboxTablerData[0]
            }
            if (code !== this.btnReplaceList.refresh) {
                this.selectedData = {}
                this.refreshSaveData = []
            }
            let confirmsName = array ? ['服务'] : getCheckboxTablerId.name
            let riskText = ""
            let setting = {
                customClass: "customizeWidth510pxClass"
            }

            switch (code) {
                case this.btnReplaceList.start: //启动
                	commonConfirm(this, '启动确认', getHintText('启动', confirmsName)).then(() => {
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

                case this.btnReplaceList.stop: //停止
                    riskText = "将运行状态数据库停止，会导致整个数据库无法对外服务。"
                    commonConfirm(this, '停止确认', getRiskLevelHtml(3, riskText) + getHintText('停止', confirmsName), setting).then(() => {
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

                case this.btnReplaceList.scaleUp: //计算扩容
                    //console.log("扩容")
                    _this.userListView(function () {
                        let highAvailable = getCheckboxTablerId.highAvailable
                        let width = highAvailable ? 515 : 430
                        let height = highAvailable ? 320 : 270
                        url = "/" + getProjectName() + "/app/servgroup/cmhas/scaleUp"
                        urlData = "?ogAutoExamine=" + _this.ogAutoExamine + "&ogAutoExecute=" + _this.ogAutoExecute + "&highAvailable=" + highAvailable
                        _this.scaleUpAjaxFun(getCheckboxTablerId, width + "px", height + "px", url + urlData)
                    })
                    break;

                case this.btnReplaceList.scaleUpstorage: //存储扩容
                    //console.log("扩容")
                    _this.userListView(function () {
                        let highAvailable = getCheckboxTablerId.highAvailable
                        let width = highAvailable ? 600 : 515
                        let height = highAvailable ? 370 : 320
                        url = "/" + getProjectName() + "/app/servgroup/cmhas/scaleUpstorage"
                        urlData = "?ogAutoExamine=" + _this.ogAutoExamine + "&ogAutoExecute=" + _this.ogAutoExecute + "&highAvailable=" + highAvailable
                        _this.scaleUpstorageAjaxFun(getCheckboxTablerId, width + "px", height + "px", url + urlData)
                    })
                    break;

                case this.btnReplaceList.imageUpdate: //升级
                    //console.log("升级")
                    _this.userListView(function () {
                        let highAvailable = getCheckboxTablerId.highAvailable
                        let width = highAvailable ? 504 : 424
                        let height = highAvailable ? 315 : 265
                        url = "/" + getProjectName() + "/app/servgroup/cmhas/imageUpdate"
                        urlData = "?architecture=" + getCheckboxTablerId.sysArchitecture.code + "&ogAutoExamine=" + _this.ogAutoExamine + "&ogAutoExecute=" + _this.ogAutoExecute + "&highAvailable=" + highAvailable
                        _this.imageUpdateAjaxFun(getCheckboxTablerId, width + "px", height + "px", url + urlData)
                    })
                    break;

                case this.btnReplaceList.backup: //备份
                    //console.log("备份")
                    url = "/" + getProjectName() + "/app/servgroup/cmhas/backUp"
                    _this.backupAjaxFun(getCheckboxTablerId, '424px', '265px', url)
                    break;

                case this.btnReplaceList.manage: //管理
                    var id = 0
                    _this.rowBtnList.forEach(function (v, i) {
                        if (v.code === _this.btnReplaceList.manage) {
                            id = parseInt(v.id)
                        }
                    })
                    var indexId = this.rowData.id
                    var indexName = this.rowData.name
                    var json = {}
                    var menuData = JSON.parse(JSON.stringify(menu))
                    menuData.subMenu.servGroupName = indexName
                    url = "/app/servgroup/cmhas/manage/" + indexId + "?id=" + id + "&menu=" + encodeURIComponent(JSON.stringify(menuData), "utf-8") + "&highAvailable=" + getCheckboxTablerId.highAvailable
                    json = {
                        code: url,
                        name: indexName,
                    }
                    parent.window.indexApp.menuClick(json, null, false)
                    break;

                case this.btnReplaceList.del: //删除
                    if (_this.delFormShow === false) {
                        setting = {
                            customClass: "customizeWidth490pxClass"
                        }
                        riskText = "整个服务将被删除，删除操作不可逆，无法恢复。请谨慎操作！！！如果是下线需求，目前流程需要先停止后两周以后再删除下线。"
                        commonConfirm(this, '删除确认', getRiskLevelHtml(5, riskText), setting).then(() => {
                            _this.delFormShow = true
                        }).catch(() => {
                        });
                    } else {
                        _this.$refs["delForm"].validate((valid) => {
                            if (valid) {
                                _this.deleteAjaxFun(getCheckboxTablerId,
                                    function (data, sendData) {
                                        _this.delFromInitialize()
                                        _this.userListView(function () {
                                            if (_this.ogAutoExamine && _this.ogAutoExecute || !getCheckboxTablerId.disabledFlag) {
                                                _this.returnList()
                                            } else {
                                                _this.goTourlDialogShow = true
                                            }
                                            operationCompletion(_this, "操作成功！")
                                        })
                                    },
                                    function (error, sendData) {
                                        operationCompletion(_this, error.response.data.msg, 'error')
                                    })
                            } else {
                                return false;
                            }
                        });
                    }
                    break;

                case this.btnReplaceList.refresh: //刷新
                    this.btnDisabledCreated()
                    this.refreshAjaxFun(getCheckboxTablerId,
                        function (response, sendData) {
                            _this.dataDispose(response.data)
                        },
                        function (error, sendData) {
                            console.log(error)
                            operationCompletion(_this, error.response.data.msg, 'error')
                        })
                    break;
            }
        },
        delFromInitialize: function () {
            this.delFormShow = false
            this.delForm.name = ''
            this.$refs["delForm"].resetFields()
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
        cellDblClickManage({column, row, rowIndex}) {
            this.btnClick('btnManage', false, row)
        },
        jumpSearch: function () {
            if (!XEUtils.isBoolean(this.searchKeyWord) && XEUtils.isEmpty(this.filterName) && !XEUtils.isEmpty(this.searchKeyWord)) {
                var _this = this
                _this.filterName = decodeURI(decodeURI(this.searchKeyWord))
                _this.searchKeyWord = ''
                setTimeout(function () {
                    _this.searchClick()
                }, 0);
            } else {
                this.searchKeyWord = ''
            }
        },
        searchClick: function (settings) {
            let keyArray = ['name', 'ownerName', 'businessSubsystem.businessSystem.name', 'businessSubsystem.name', 'site.name',
                'businessArea.name', 'version', 'sysArchitecture.display', 'arch.name', 'scale.mode.display', 'unitCntText', 'highAvailableText',
                'diskType.display', 'dataSize', 'logSize', 'addresses<>', 'statusText', 'actionDisplay']
            commonSearchClick(this, keyArray, settings)
        },
        tableRowClassName: function (row) {
            return commonTableRowClassName(row)
        },
        goToServgroup: function (flag) {
            if (flag) {
                var json = {}
                var menuSkip = {}
                var urlName = ""
                var code = "/app/order/cmhas"
                var menuLists = parent.window.indexApp.menuLists
                var menuId;
                XEUtils.arrayEach(menuLists, (menuList, menuIndex) => {
                    XEUtils.arrayEach(menuList.childrens, (subMenuList, subMenuIndex) => {
                        if (subMenuList.code === code) {
                            menuSkip = {
                                name: menuList.name,
                                icon: menuList.icon,
                                subMenu: {
                                    name: subMenuList.name
                                }
                            }
                            urlName = subMenuList.name + "工单"
                            menuId = subMenuList.id
                        }
                    })
                })
                var url = code + "?id=" + menuId + "&menu=" + encodeURIComponent(JSON.stringify(menuSkip), "utf-8") + "&t=" + (new Date()).getTime()
                json = {
                    code: url,
                    name: urlName,
                }
                var tab = {
                    name: url
                }
                parent.window.indexApp.tabsJump(tab)
                parent.window.indexApp.menuClick(json, null, false)
            }
            this.returnList()
            this.goTourlDialogShow = false
        }
    }
})
window.onresize = function () {
    listApp.table_heighth = commonOnresize()
}