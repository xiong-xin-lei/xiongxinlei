var listApp = new Vue({
    el: '#list',
    data: {
        title: '镜像',
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
            refresh: "refresh",
            templates: "btnTemplate"
        },
        btnOperation: function (value, type) {
            if (type === 'type') {
                switch (value) {
                    case this.btnReplaceList.add:
                        return 'primary'
                    case this.btnReplaceList.update:
                        return ''
                    case this.btnReplaceList.start:
                        return ''
                    case this.btnReplaceList.stop:
                        return ''
                    case this.btnReplaceList.del:
                        return ''
                    case this.btnReplaceList.refresh:
                        return ''
                    case this.btnReplaceList.templates:
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
                    case this.btnReplaceList.templates:
                        return 'el-icon-s-tools'
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
        statefulShow: function (status) {
            if (status === null) {
                return ""
            }
            if (status) {
                return getProjectSvg("#00abac") + "<span style='line-height:14px'>是</span>"
            } else if (!status) {
                return getProjectSvg("#326CE5") + "<span style='line-height:14px'>否</span>"
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
            getHtmlMinWidth("list", _this, ['xTable'])
        }, 0)
    },
    methods: {
        dataDispose: function (data) {

            var jq_jsonData = []
            var jq_json = {}
            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))
                var statusText = "";
                if (v.enabled !== null) {
                    if (v.enabled) {
                        statusText = "是"
                    } else {
                        statusText = "否"
                    }
                }

                var statefulText = "";
                if (v.stateful !== null) {
                    if (v.stateful) {
                        statefulText = "有状态"
                    } else {
                        statefulText = "无状态"
                    }
                }

                json.version = jsonJudgeNotDefined(v, 'v.version.major')
                    + "." + jsonJudgeNotDefined(v, 'v.version.minor')
                    + "." + jsonJudgeNotDefined(v, 'v.version.patch')
                    + "." + jsonJudgeNotDefined(v, 'v.version.build')
                json.status = v.enabled
                json.statusText = statusText
                json.statefulText = statefulText
                json.name = jsonJudgeNotDefined(v, 'v.type.display')
                    + ":" + jsonJudgeNotDefined(v, 'v.version.major')
                    + "." + jsonJudgeNotDefined(v, 'v.version.minor')
                    + "." + jsonJudgeNotDefined(v, 'v.version.patch')
                    + "." + jsonJudgeNotDefined(v, 'v.version.build')

                jq_jsonData.push(json)
            });
            this.tableDataAll = jq_jsonData
            this.searchData = this.tableDataAll.concat()
            this.sortData = this.searchData.concat()
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

                    case _this.btnReplaceList.templates:
                        return false

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
            var width = '700px'
            var height = '435px'
            var title = "注册"
            var url = "/" + getProjectName() + "/app/resource/images/add"
            var urlData = ""
            layerOpenFun(width, height, title, url + urlData)
        },
        startAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/images/" + getCheckboxTablerId.id + "/enabled", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        stopAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/images/" + getCheckboxTablerId.id + "/disabled", function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        deleteAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendDelete("/" + getProjectName() + "/images/" + getCheckboxTablerId.id, function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(_this.tablePage.currentPage)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/images?site_id=" + getSession("siteId"), function (response) {
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
                this.refreshSaveData = []
            }
            let confirmsName = array ? ['镜像'] : getCheckboxTablerId.name

            switch (code) {
                case this.btnReplaceList.add://新增
                    this.addAjaxFun(getCheckboxTablerId)
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

                case this.btnReplaceList.templates://配置
                    var json = {}
                    var btnId = 0
                    let btnName
                    XEUtils.arrayEach(this.rowBtnList, (v, i) => {
                        if (v.code === _this.btnReplaceList.templates) {
                            btnId = v.id
                            btnName = v.name
                        }
                    })
                    menu.subMenu.sSubMenu = {
                        name: btnName
                    }
                    var url = "/app/resource/images/templates/" + getCheckboxTablerId.type.code + "/" + getCheckboxTablerId.version
                    var urlData = "?architecture=" + getCheckboxTablerId.architecture.code + "&menu=" + encodeURIComponent(JSON.stringify(menu), "utf-8") + "&id=" + btnId
                    json = {
                        code: url + urlData,
                        name: btnName,
                    }
                    parent.window.indexApp.menuClick(json, null, false)
                    break;
            }
        },
        menuClick: function (getCheckboxTablerId, name) {
            var _this = this
            var json = {}
            menu.subMenu.sSubMenu = {
                name: name
            }
            var urls = "/app/resource/images/templates/" + getCheckboxTablerId.typeCode + "/" + getCheckboxTablerId.version + "?architecture=" + getCheckboxTablerId.architecture.code + "&menu=" + encodeURIComponent(JSON.stringify(menu), "utf-8")
            json = {
                code: urls,
                name: name,
            }
            parent.window.indexApp.menuClick(json, null, false)
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
            let keyArray = ['type.display', 'version', 'architecture.display', 'statefulText', 'gmtCreated', 'statusText']
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