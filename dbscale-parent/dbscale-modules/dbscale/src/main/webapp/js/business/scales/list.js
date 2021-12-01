var listApp = new Vue({
    el: '#list',
    data: {
        title: '规模',
        table_heighth: '',
        menu: menu,
        btnList: ttBtnList,
        rowBtnList: rowBtnList,
        disabledValue: {},
        btnReplaceList: {
            add: "btnAdd",
            start: "btnEnabled",
            stop: "btnDisabled",
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
        statusShow: function (status) {
            if (status === null) {
                return ""
            }
            if (status) {
                return getProjectSvg(COLOR_ENABLED) + "<span style='line-height:14px'>是</span>"
            } else if (!status) {
                return getProjectSvg(COLOR_DISABLED) + "<span style='line-height:14px'>否</span>"
            }
        },
        allAlign: ALLALIGN,
        tableDataAll: [],
        tableData: [],
        searchData: [],
        sortData: [],
        refreshKey: ["type.display", "name"],
        refreshSaveData: [],
        filterName: '',
        tablePage: {
            currentPage: 1,
            pageSize: PAGE_SIZE,
            totalResult: 0,
            pageSizes: PAGE_SIZES,
            pageLayouts: PAGE_LAYOUTS
        },
        rowBtnShowIndex: ROWBTN_SHOWINDEX,
        rowBtnShowNum: ROWBTN_SHOWNUM,
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
            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))
                var statusText = "";
                if (v.enabled !== null) {
                    statusText = v.enabled ? "是" : "否"
                }
                json.statusText = statusText
                json.status = v.enabled
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
            var width = '510px'
            var height = '320px'
            var title = "新增"
            var url = "/" + getProjectName() + "/app/business/scales/add"
            var urlData = ""
            layerOpenFun(width, height, title, url + urlData)
        },
        startAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/scales/" + getCheckboxTablerId.type.code + "/enabled?cpu=" + getCheckboxTablerId.cpuCnt + "&mem=" + getCheckboxTablerId.memSize, function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        stopAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/scales/" + getCheckboxTablerId.type.code + "/disabled?cpu=" + getCheckboxTablerId.cpuCnt + "&mem=" + getCheckboxTablerId.memSize, function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        deleteAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendDelete("/" + getProjectName() + "/scales/" + getCheckboxTablerId.type.code + "?cpu=" + getCheckboxTablerId.cpuCnt + "&mem=" + getCheckboxTablerId.memSize, function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(_this.tablePage.currentPage)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/scales", function (response) {
                successFun(response, getCheckboxTablerId)
                layer.closeAll('loading')
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
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
            if (code !== this.btnReplaceList.refresh)
                this.refreshSaveData = []

            let confirmsName = array ? ['规模'] : getCheckboxTablerId.type.display + ":" + getCheckboxTablerId.name
            switch (code) {
                case this.btnReplaceList.add:// 新增
                    this.addAjaxFun(getCheckboxTablerId)
                    break;

                case this.btnReplaceList.start:// 启用
                    commonConfirm(this, '启用确认', getHintText('启用', confirmsName)).then(() => {
                        sendAll(_this.startAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
                            if (errorArray.length === 0) {
                                operationCompletion(_this, "操作成功！")
                                _this.returnList()
                            } else {
                                operationCompletion(_this, errorMsg(errorArray, array, function (getCheckboxTablerId) {
                                    return getCheckboxTablerId.type.display + ":" + getCheckboxTablerId.name
                                }), 'error')
                            }
                        })
                    }).catch(() => {
                    });
                    break;

                case this.btnReplaceList.stop:// 停用
                    commonConfirm(this, '停用确认', getHintText('停用', confirmsName)).then(() => {
                        sendAll(_this.stopAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
                            if (errorArray.length === 0) {
                                operationCompletion(_this, "操作成功！")
                                _this.returnList()
                            } else {
                                operationCompletion(_this, errorMsg(errorArray, array, function (getCheckboxTablerId) {
                                    return getCheckboxTablerId.type.display + ":" + getCheckboxTablerId.name
                                }), 'error')
                            }
                        })
                    }).catch(() => {
                    });
                    break;

                case this.btnReplaceList.del:// 删除
                    commonConfirm(this, '删除确认', getHintText('删除', confirmsName)).then(() => {
                        sendAll(_this.deleteAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
                            if (errorArray.length === 0) {
                                operationCompletion(_this, "操作成功！")
                                _this.returnList()
                            } else {
                                operationCompletion(_this, errorMsg(errorArray, array, function (getCheckboxTablerId) {
                                    return getCheckboxTablerId.type.display + ":" + getCheckboxTablerId.name
                                }), 'error')
                            }
                        })
                    }).catch(() => {
                    });
                    break;

                case this.btnReplaceList.refresh:// 刷新
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
        searchClick: function (settings) {
            let keyArray = ['type.display', 'name', 'cpuCnt', 'memSize', 'statusText']
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