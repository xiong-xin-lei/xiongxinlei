var menu = getQueryVariable("menu")
var hostId = getQueryVariable("hostId")
var listApp = new Vue({
    el: '#unitList',
    data: {
        title: '单元',
        table_heighth: '',
        menu: menu,
        btnList: ttBtnList,
        rowBtnList: rowBtnList,
        disabledValue: {},
        btnReplaceList: {
            start: "btnEnabled",
            stop: "btnDisabled",
            rebuild: "btnRebuild",
            refresh: "refresh"
        },
        unitList: [{
            code: "mysql",
            name: "数据库"
        }, {
            code: "proxysql",
            name: "代理"
        }, {
            code: "cmha",
            name: "高可用"
        }, {
            code: "redis",
            name: "数据库"
        }, {
            code: "redis-sentinel",
            name: "高可用"
        }
        ],
        typeName: '',
        updateStatus: '',
        selectedData: {},
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
                if (!!data) {
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
            pageSize: PAGE_SIZE,
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
            getHtmlMinWidth("unitList", _this, ['xTable'])
        }, 0)
    },
    methods: {
        dataDispose: function (data) {
            var jq_jsonData = [];
            var jq_json = {};
            XEUtils.arrayEach(data.data, (v, i) => {
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

                var ownerName = jsonJudgeNotDefined(v, "v.servGroup.owner.name");
                var ownerUsername = jsonJudgeNotDefined(v, "v.servGroup.owner.username");

                json.dataSize = dataSize;
                json.logSize = logSize;
                json.memSize = memSize;
                json.ip = ip;
                json.versionValue = versionValue;
                json.ownerName = ownerNameDispose(ownerName, ownerUsername)
                json.diskTypeDisplay = jsonJudgeNotDefined(v, "v.diskType.display");
                json.actionDisplay = jsonJudgeNotDefined(v, "v.task.action.display") + jsonJudgeNotDefined(v, "v.task.state.display");
                json.statusText = jsonJudgeNotDefined(v, "v.state.display");

                if (jsonJudgeNotDefined(v, "v.relateId") !== "")
                    jq_jsonData.push(json);
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
                    case _this.btnReplaceList.add:
                        return false

                    case _this.btnReplaceList.update:
                        return false

                    case _this.btnReplaceList.start:
                    case _this.btnReplaceList.del:
                        return data.enabled === true

                    case _this.btnReplaceList.stop:
                        return data.enabled === false

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
            var settings = {
                cancel: function (index, layero) {
                    _this.returnList()
                }
            }
            layerOpenFun(width, height, title, url + urlData, settings)
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
        getUnitName: function (type) {
            let name = ""
            let index = XEUtils.findIndexOf(this.unitList, item => item.code === type)

            name = index > -1 ? this.unitList[index].name : "数据库"
            this.typeName = name

            return name
        },
        startAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/business_subsystems/" + getCheckboxTablerId.id + "/enabled", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        stopAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/business_subsystems/" + getCheckboxTablerId.id + "/disabled", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        rebuildArrayAjaxFun: function (getCheckboxTableId, successFun, falseFun) {
            let _this = this
            let jsonData = {
                "force": true
            }
            sendPut("/" + getProjectName() + "/units/" + getCheckboxTableId.id + "/rebuild", function (response) {
                successFun(response, getCheckboxTableId)
                _this.refreshSaveData.push(getCheckboxTableId)
            }, function (error) {
                falseFun(error, getCheckboxTableId)
            }, jsonData)
        },
        rebuildAjaxFun: function (getCheckboxTablerId) {
            let unitId = getCheckboxTablerId.id
            let clusterId = jsonJudgeNotDefined(getCheckboxTablerId, 'getCheckboxTablerId.host.cluster.id')
            let diskType = jsonJudgeNotDefined(getCheckboxTablerId, 'getCheckboxTablerId.diskType.code')
            let typeCode = jsonJudgeNotDefined(getCheckboxTablerId, 'getCheckboxTablerId.type.code')
            var width = '355px'
            var height = '222px'
            var title = "强制重建"
            var url = "/" + getProjectName() + "/app/resource/hosts/unit/rebuild"
            var urlData = "?unitId=" + unitId + "&clusterId=" + clusterId + "&diskType=" + diskType + "&typeCode=" + typeCode
            this.selectedData = getCheckboxTablerId
            layerOpenFun(width, height, title, url + urlData)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/hosts/" + hostId + "/units", function (response) {
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
        btnClick: function (code, array, getCheckboxTablerId) {
            var _this = this
            let rowType = ""
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
                rowType = jsonJudgeNotDefined(getCheckboxTablerData[0], "json.type.code")
            }

            this.updateStatus = ''
            let confirmsName = array ? ['单元'] : getCheckboxTablerId.name
            let rowTypeName = _this.getUnitName(rowType)
            let riskText = ""
            switch (code) {
                /*case this.btnReplaceList.start://启用
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
                    break;*/

                case this.btnReplaceList.rebuild://强制重建
                    if (array) {
                        let setting = {
                            customClass: "customizeWidth540pxClass"
                        }
                        riskText = "对选中的单元进行强制重建，强制重建有可能导致主机上的存储资源释放不掉，请谨慎操作！"
                        let riskText1 = "强制重建完成之后，需要对该单元进行【重建初始化】操作！" + "<br>"
                        commonConfirm(this, '强制重建确认', getRiskLevelHtml(5, riskText) + riskText1 + getHintText('强制重建', confirmsName), setting).then(() => {
                            sendAll(_this.rebuildArrayAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
                                if (errorArray.length === 0) {
                                    operationCompletion(_this, "操作成功！")
                                    _this.returnList()
                                } else {
                                    operationCompletion(_this, errorMsg(errorArray, array), 'error')
                                }
                            })
                        }).catch(() => {
                        });
                    } else {
                        this.rebuildAjaxFun(getCheckboxTablerId)
                    }
                    break;

                case this.btnReplaceList.refresh://刷新
                    this.btnDisabledCreated()
                    this.refreshAjaxFun(getCheckboxTablerId, function (response, sendData) {
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
            var type = jsonJudgeNotDefined(row, 'row.servGroup.category')
            var id = 404000999
            var urlCode = "/app/servgroup/"
            switch (type) {
                case 'mysql':
                    id = 401000999
                    urlCode += type + 's'
                    break
                case 'cmha':
                    id = 402000999
                    urlCode += type + 's'
                    break
                case 'redis':
                    id = 403000999
                    urlCode += type
                    break
                case 'nginx':
                    id = 404000999
                    urlCode += type
                    break
            }
            var menuData = {}
            var menuLists = window.top.indexApp.menuLists
            var indexId = jsonJudgeNotDefined(row, 'row.servGroup.id')
            var indexName = jsonJudgeNotDefined(row, 'row.servGroup.name')
            var unitType = jsonJudgeNotDefined(row, 'row.type.code')
            var selectedUnit = jsonJudgeNotDefined(row, 'row.id')
            var json = {}
            XEUtils.arrayEach(menuLists, (menuList, menuIndex) => {
                XEUtils.arrayEach(menuList.childrens, (subMenuList, subMenuIndex) => {
                    if (subMenuList.code === urlCode) {
                        menuData = {
                            name: menuList.name,
                            icon: menuList.icon,
                            subMenu: {
                                name: subMenuList.name
                            }
                        }
                    }
                })
            })
            menuData.subMenu.servGroupName = indexName
            url = urlCode + "/manage/" + indexId + "?id=" + id + "&menu=" + encodeURIComponent(JSON.stringify(menuData), "utf-8") + "&unitType=" + unitType + "&selectedUnit=" + selectedUnit
            switch (type) {
                case "mysql":
                case "redis":
                    url += '&tabType=tabUnit&highAvailable=' + jsonJudgeNotDefined(row, "row.servGroup.highAvailable")
                    break;
                default:
                    url += '&tabType=tabUnit'
            }
            json = {
                code: url,
                name: indexName,
            }
            window.top.indexApp.menuClick(json, null, false)
        },
        searchClick: function (settings) {
            let keyArray = ['relateId', 'ownerName', 'servGroup.businessSubsystem.businessSystem.name', 'servGroup.businessSubsystem.name',
                'type.display', 'ip', 'versionValue', 'cpuCnt', 'memSize', 'dataSize', 'logSize', 'statusText', 'podState.display', 'actionDisplay']
            commonSearchClick(this, keyArray, settings)
        },
        tableRowClassName: function (row) {
            return commonTableRowClassName(row)
        }
    }
})
window.onresize = function () {
    listApp.table_heighth = commonOnresize()
}