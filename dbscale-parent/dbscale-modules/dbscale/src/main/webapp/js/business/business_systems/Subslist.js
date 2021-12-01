var listApp = new Vue({
    el: '#Subsyslist',
    data: {
        title: '业务子系统',
        table_heighth: '',
        menu: menu,
        btnList: ttBtnList,
        rowBtnList: rowBtnList,
        disabledValue: {},
        btnReplaceList: {
            add: "btnAdd",
            update: "btnUpdate",
            start: "btnEnabled",
            stop: "btnDisabled",
            del: "btnDelete",
            refresh: "refresh"
        },
        businessSystemList: '',
        updateStatus: '',
        selectedData: {},
        btnOperation: function (value, type) {
            if (type === 'type') {
                switch (value) {
                    case this.btnReplaceList.add:
                        return 'primary'
                    case this.btnReplaceList.refresh:
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
        this.businessSystemListData()
        setTimeout(function () {
            getHtmlMinWidth("Subsyslist", _this, ['xTable'])
        }, 0)
    },
    methods: {
        businessSystemListData: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/business_systems/" + SysIds, function (response) {
                _this.businessSystemList = response.data.data
                _this.btnClick(_this.btnReplaceList.refresh, true, 0)
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        dataDispose: function (data) {
            var jq_jsonData = [];
            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))
                var statusText = "";
                if (v.enabled !== null) {
                    statusText = v.enabled ? "是" : "否"
                }

                json.status = v.enabled
                json.createdName = ownerNameDispose(v.ownerName, v.owner)
                json.statusText = statusText
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
        SubsaddAjaxFun: function (getCheckboxTablerId) {
            var width = '538px'
            var height = '335px'
            var title = "新增"
            var url = "/" + getProjectName() + "/app/business/business_systems/Subsadd/" + SysIds
            var urlData = ""
            layerOpenFun(width, height, title, url + urlData)
        },
        SubsupdataAjaxFun: function (getCheckboxTablerId) {
            this.selectedData = getCheckboxTablerId
            var width = '538px'
            var height = '335px'
            var title = "编辑"
            var url = "/" + getProjectName() + "/app/business/business_systems/Subsedit/" + getCheckboxTablerId.id
            var urlData = ""
            layerOpenFun(width, height, title, url + urlData)
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
        SubsdeleteAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendDelete("/" + getProjectName() + "/business_subsystems/" + getCheckboxTablerId.id, function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(_this.tablePage.currentPage)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/business_subsystems?business_system_id=" + SysIds, function (response) {
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
            if (code !== this.btnReplaceList.refresh) {
                this.selectedData = {}
                this.refreshSaveData = []
            }

            this.updateStatus = ''
            let confirmsName = array ? ['业务子系统'] : getCheckboxTablerId.name
            switch (code) {
                case this.btnReplaceList.add://新增
                    this.SubsaddAjaxFun(getCheckboxTablerId)
                    break;

                case this.btnReplaceList.update://编辑
                    if (getCheckboxTablerId.status) {
                        commonConfirm(this, '编辑确认', getHintText('编辑', confirmsName)).then(() => {
                            this.updateStatus = getCheckboxTablerId.id
                            this.stopAjaxFun(getCheckboxTablerId, function (data, sendData) {
                                _this.SubsupdataAjaxFun(getCheckboxTablerId)
                                layer.closeAll('loading')
                            }, function (error, sendData) {
                                operationCompletion(_this, error.response.data.msg, 'error')
                            })
                        }).catch(() => {
                        });
                    } else {
                        this.SubsupdataAjaxFun(getCheckboxTablerId)
                    }
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

                case this.btnReplaceList.del://删除
                    commonConfirm(this, '删除确认', getHintText('删除', confirmsName)).then(() => {
                        sendAll(_this.SubsdeleteAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
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
        searchClick: function (settings) {
            let keyArray = ['name', 'gmtCreate', 'description', 'statusText']
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