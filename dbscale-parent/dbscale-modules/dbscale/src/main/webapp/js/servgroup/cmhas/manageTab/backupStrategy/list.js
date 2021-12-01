var backupStrategyListApp = new Vue({
    el: '#backupStrategyList',
    data: {
        title: '备份策略',
        table_heighth: '',
        rowData: {},
        btnList: ttBtnList,
        rowBtnList: rowBtnList,
        disabledValue: {},
        btnReplaceList: {
            add: "btnAdd",
            update: "btnUpdate",
            start: "btnStart",
            stop: "btnStop",
            del: "btnDelete",
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
                    case this.btnReplaceList.refresh:
                        return 'el-icon-refresh'
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
            } else if (type == 'enabled') {
                var data = status.enabled

                if (data !== null) {
                    if (data) {
                        return getProjectSvg(COLOR_ENABLED) + "<span style='line-height:14px'>是</span>"
                    } else {
                        return getProjectSvg(COLOR_DISABLED) + "<span style='line-height:14px'>否</span>"
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
            getHtmlMinWidth("backupStrategyList", _this, ['xTable'])
        }, 0)
    },
    methods: {
        dataDispose: function (data) {
            var jq_jsonData = [];
            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))
                var enabledText = ""
                if (v.enabled !== null) {
                    if (v.enabled) {
                        enabledText = '是'
                    } else {
                        enabledText = '否'
                    }
                }
                json.enabledText = enabledText

                var fileRetentionNumText = jsonJudgeNotDefined(v, "v.fileRetentionNum") + ""
                var cronExpressionArray = jsonJudgeNotDefined(v, "v.cronExpression").split(' ')
                var cronExpressionText = ""
                var cronExpressionTimeText = cronExpressionArray[1] + ":" + cronExpressionArray[0]
                if (cronExpressionArray[2] !== "*") {
                    fileRetentionNumText += "月"
                    cronExpressionText = "每月" + cronExpressionArray[2] + "号 " + cronExpressionTimeText
                } else if (cronExpressionArray[4] !== "*") {
                    fileRetentionNumText += "周"
                    switch (cronExpressionArray[4] + '') {
                        case '1':
                            cronExpressionText = "每周一 " + cronExpressionTimeText
                            break;
                        case '2':
                            cronExpressionText = "每周二 " + cronExpressionTimeText
                            break;
                        case '3':
                            cronExpressionText = "每周三 " + cronExpressionTimeText
                            break;
                        case '4':
                            cronExpressionText = "每周四 " + cronExpressionTimeText
                            break;
                        case '5':
                            cronExpressionText = "每周五 " + cronExpressionTimeText
                            break;
                        case '6':
                            cronExpressionText = "每周六 " + cronExpressionTimeText
                            break;
                        case '7':
                            cronExpressionText = "每周日 " + cronExpressionTimeText
                            break;
                    }
                } else {
                    fileRetentionNumText += "天"
                    cronExpressionText = "每天 " + cronExpressionTimeText
                }
                json.cronExpressionText = cronExpressionText
                json.createdName = ownerNameDispose(v.created.name, v.created.username)
                json.fileRetentionNumText = fileRetentionNumText

                jq_jsonData.push(json)
            })

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
                        return data.enabled === true

                    case _this.btnReplaceList.stop:
                        return data.enabled === false

                    case _this.btnReplaceList.del:
                        return data.enabled === true

                    case _this.btnReplaceList.refresh:
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
            var width = '695px'
            var height = '434px'
            var title = "新增"
            var url = "/" + getProjectName() + "/app/servgroup/cmhas/manageTab/backupStrategy/add"
            var urlData = "?serv_group_id=" + rowId
            parent.layerOpenFun(width, height, title, url + urlData)
        },
        updataAjaxFun: function (getCheckboxTablerId) {
            this.selectedData = getCheckboxTablerId
            var width = '695px'
            var height = '434px'
            var title = "编辑"
            var url = "/" + getProjectName() + "/app/servgroup/cmhas/manageTab/backupStrategy/edit"
            var urlData = "?strategy_id=" + getCheckboxTablerId.id
            parent.layerOpenFun(width, height, title, url + urlData)
        },
        startAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/backup_strategies/" + getCheckboxTablerId.id + "/enabled", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        stopAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/backup_strategies/" + getCheckboxTablerId.id + "/disabled", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        deleteAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendDelete("/" + getProjectName() + "/backup_strategies/" + getCheckboxTablerId.id, function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(_this.tablePage.currentPage)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/backup_strategies?site_id=" + getSession("siteId") + "&serv_group_id=" + rowId, function (response) {
                successFun(response, getCheckboxTablerId)
                layer.closeAll('loading')
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        btnClick: function (code, array, getCheckboxTablerId) {
            this.updateStatus = '';
            this.rowData = getCheckboxTablerId;
            var _this = this
            var url = ""
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

            let confirmsName = array ? ['备份策略'] : getCheckboxTablerId.cronExpressionText + ' 备份策略'

            switch (code) {
                case this.btnReplaceList.add://新增
                    this.addAjaxFun(getCheckboxTablerId)
                    break;

                case this.btnReplaceList.update://编辑
                    if (getCheckboxTablerId.enabled) {
                        commonConfirm(parent.manageApp, '编辑确认', getHintText('编辑', confirmsName)).then(() => {
                            this.updateStatus = getCheckboxTablerId.id
                            this.stopAjaxFun(getCheckboxTablerId,
                                function (data, sendData) {
                                    _this.updataAjaxFun(getCheckboxTablerId)
                                    layer.closeAll('loading')
                                }, function (error, sendData) {
                                    operationCompletion(_this, error.response.data.msg, 'error')
                                })
                        }).catch(() => {
                        });
                    } else {
                        this.updataAjaxFun(getCheckboxTablerId)
                    }
                    break;

                case this.btnReplaceList.start://启用
                    commonConfirm(parent.manageApp, '启用确认', getHintText('启用', confirmsName)).then(() => {
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
                    commonConfirm(parent.manageApp, '停用确认', getHintText('停用', confirmsName)).then(() => {
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

                case this.btnReplaceList.del://删除
                    commonConfirm(parent.manageApp, '删除确认', getHintText('删除', confirmsName)).then(() => {
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
        loadContentMethod({row}) {
            var _this = this
            return new Promise(resolve => {
                sendGet("/" + getProjectName() + "/backup_strategies/" + row.id, function (response) {
                    layer.closeAll('loading')
                    row.childRows = _this.childDataDispose(response.data.data)
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
        childDataDispose: function (data) {
            var json = JSON.parse(JSON.stringify(v))
            json.createdName = ownerNameDispose(data.created.name, data.created.username)
            json.createdTimestamp = jsonJudgeNotDefined(data, "data.created.timestamp")
            return json;
        },
        searchClick: function (settings) {
            let keyArray = ['cronExpressionText', 'backupStorageType.display', 'type.display', 'fileRetentionNumText', 'enabledText']
            commonSearchClick(this, keyArray, settings)
        },
        tableRowClassName: function (row) {
            return commonTableRowClassName(row)
        }
    }
})
window.onresize = function () {
    backupStrategyListApp.table_heighth = commonOnresize()
}