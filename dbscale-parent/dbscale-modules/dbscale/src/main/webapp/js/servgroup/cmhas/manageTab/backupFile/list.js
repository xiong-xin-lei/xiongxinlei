var backupFileListApp = new Vue({
    el: '#backupFileList',
    data: {
        title: '备份文件',
        table_heighth: '',
        rowData: {},
        btnList: ttBtnList,
        rowBtnList: rowBtnList,
        disabledValue: {},
        btnReplaceList: {
            add: "btnAdd",
            del: "btnDelete",
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
                    case this.btnReplaceList.add:
                        return 'el-icon-plus'
                    case this.btnReplaceList.del:
                        return 'el-icon-delete'
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
        this.btnDisabledCreated()
        this.btnClick(this.btnReplaceList.refresh, true, 0)
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
                json.statusCode = jsonJudgeNotDefined(v, "v.status.code")

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
                    case _this.btnReplaceList.del:
                        return false

                    case _this.btnReplaceList.refresh:
                        return false
                }
            }
        },
        handleSizeCurrentChange: function (value) {
            commonHandleSizeCurrentChange(this, value)
        },
        handlePageChange: function () {
            commonHandlePageChange(this)
            this.table_heighth = commonOnresize()
        },
        statusDisplayClick: function (data) {
            var width = '720px'
            var height = '450px'
            var title = "错误信息"
            var url = "/" + getProjectName() + "/app/servgroup/cmhas/manageTab/backupFile/msgList"
            var urlData = "?msg=" + encodeURIComponent(data.msg, "utf-8")
            parent.layerOpenFun(width, height, title, url + urlData)
        },
        addAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            let jsonData = {
                servGroupId: rowId
            }
            sendPost("/" + getProjectName() + "/backup/files/external", function (response) {
                successFun(response, getCheckboxTablerId)
                // _this.refreshSaveData.push(getCheckboxTablerId)
                _this.refreshSaveData.push(_this.tablePage.currentPage)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, jsonData)
        },
        deleteAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendDelete("/" + getProjectName() + "/backup/files/" + getCheckboxTablerId.id, function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/backup/files?site_id=" + getSession("siteId") + "&serv_group_id=" + rowId, function (response) {
                successFun(response, getCheckboxTablerId)
                layer.closeAll('loading')
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        btnClick: function (code, array, getCheckboxTablerId) {
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
            let confirmsName = array ? ['备份文件'] : getCheckboxTablerId.name

            switch (code) {
                case this.btnReplaceList.add://新增
                    this.addAjaxFun(getCheckboxTablerData, function (response) {
                        let data = response.data.data
                        let tempArray = [{
                            name: "文件名",
                            details: data.id
                        }, {
                            name: "路径",
                            details: data.directory
                        }]
                        let settings = {
                            duration: 0
                        }
                        operationCompletion(_this, errorMsg(tempArray, true, "json.name", "json.details"), 'success', settings)
                    }, function (error) {
                        operationCompletion(_this, error.response.data.msg, 'error')
                    })
                    break;

                case this.btnReplaceList.del://删除
                    commonConfirm(parent.manageApp, '删除确认', getHintText('删除', confirmsName)).then(() => {
                        sendAll(_this.deleteAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
                            if (errorArray.length === 0) {
                                operationCompletion(_this, "操作成功！")
                                _this.btnClick(_this.btnReplaceList.refresh, true, 0)
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
            let keyArray = ['unitName', 'type.display', 'backupStorageType.display', 'sizeData', 'createdAt', 'consumingTime', 'expiredAt',
                'name', 'directory', 'status.display']
            commonSearchClick(this, keyArray, settings)
        },
        tableRowClassName: function (row) {
            return commonTableRowClassName(row)
        }
    }
})
window.onresize = function () {
    backupFileListApp.table_heighth = commonOnresize()
}