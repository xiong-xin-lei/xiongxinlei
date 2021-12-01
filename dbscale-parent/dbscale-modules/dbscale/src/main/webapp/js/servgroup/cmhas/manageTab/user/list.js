var highAvailable = parent.highAvailable
var userListApp = new Vue({
    el: '#userList',
    data: {
        title: '用户',
        table_heighth: '',
        highAvailable: highAvailable,
        btnList: ttBtnList,
        rowBtnList: rowBtnList,
        disabledValue: {},
        btnReplaceList: {
            add: "btnAdd",
            update: "btnUpdate",
            pwd: "btnPwd",
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
                    default:
                        return 'el-icon-setting'
                }
            }
        },
        selectedData: {},
        allAlign: ALLALIGN,
        tableDataAll: [],
        tableData: [],
        searchData: [],
        sortData: [],
        refreshKey: ["username", "whiteIp"],
        refreshSaveData: [],
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
            getHtmlMinWidth("userList", _this, ['xTable'])
        }, 0)
    },
    methods: {
        dataDispose: function (data) {
            var jq_jsonData = [];
            var jq_json = {};
            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))
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

                    case _this.btnReplaceList.pwd:
                        return false

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
        addAjaxFun: function (width, height, url) {
            var title = "新增"
            parent.layerOpenFun(width, height, title, url)
        },
        updateAjaxFun: function (getCheckboxTablerId, width, height, url) {
            this.selectedData = getCheckboxTablerId
            var title = "编辑"
            parent.layerOpenFun(width, height, title, url)
        },
        resetPwdAjaxFun: function (getCheckboxTablerId) {
            const _this = this
            const title = "重置密码"
            let width = '424px'
            let height = '265px'
            let url = "/" + getProjectName() + "/app/servgroup/cmhas/manageTab/user/resetPwd"
            let urlData = "?username=" + getCheckboxTablerId.username + "&ip=" + getCheckboxTablerId.whiteIp
            this.searchData = getCheckboxTablerId
            parent.layerOpenFun(width, height, title, url + urlData)
        },
        pwdAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            let url = "/" + getProjectName() + "/serv_groups/cmha/" + rowId + "/users/" + getCheckboxTablerId.username + "/pwd/reset"
            if (!_this.highAvailable)
                url += "?ip=" + encodeURIComponent(getCheckboxTablerId.whiteIp, "utf-8")
            sendPut(url, function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(getCheckboxTablerId)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        deleteAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            let url = "/" + getProjectName() + "/serv_groups/cmha/" + rowId + "/users/" + getCheckboxTablerId.username
            if (!_this.highAvailable)
                url += "?ip=" + encodeURIComponent(getCheckboxTablerId.whiteIp, "utf-8")
            sendDelete(url, function (response) {
                successFun(response, getCheckboxTablerId)
                _this.refreshSaveData.push(_this.tablePage.currentPage)
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/serv_groups/cmha/" + rowId + "/users", function (response) {
                successFun(response, getCheckboxTablerId)
                layer.closeAll('loading')
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        btnClick: function (code, array, getCheckboxTablerIdData) {
            var _this = this
            var getCheckboxTablerId = JSON.parse(JSON.stringify(getCheckboxTablerIdData))
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
                getCheckboxTablerId = JSON.parse(JSON.stringify(getCheckboxTablerData[0]))
                if (!_this.highAvailable) {
                    var whiteIp = getCheckboxTablerId.whiteIp
                    getCheckboxTablerId.whiteIp = getCheckboxTablerId.whiteIp.replace(/\%/g, "%25");
                }
            }
            if (code !== this.btnReplaceList.refresh) {
                this.selectedData = {}
                this.refreshSaveData = []
            }
            var url = ""
            var urldata = ""
            this.updateStatus = ''
            var width
            var height
            let username = _this.highAvailable ? getCheckboxTablerId.username : getCheckboxTablerId.username + '@' + getCheckboxTablerId.whiteIp
            let confirmsName = array ? ['用户'] : username
            switch (code) {
                case this.btnReplaceList.add://新增
                    width = 688
                    height = 430
                    url = "/" + getProjectName() + "/app/servgroup/cmhas/manageTab/user/add"
                    _this.addAjaxFun(width + 'px', height + 'px', url)
                    break;

                case this.btnReplaceList.update://编辑
                    url = "/" + getProjectName() + "/app/servgroup/cmhas/manageTab/user/update"
                    urldata = "?username=" + getCheckboxTablerId.username + "&ip=" + getCheckboxTablerId.whiteIp;
                    _this.updateAjaxFun(getCheckboxTablerId, '688px', '430px', url + urldata)
                    break;

                case this.btnReplaceList.pwd://重置密码
                    _this.resetPwdAjaxFun(getCheckboxTablerId)
                    /*commonConfirm(parent.manageApp, '重置确认', getHintText('重置', confirmsName)).then(() => {
                        sendAll(_this.pwdAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
                            if (errorArray.length === 0) {
                                let setting = {
                                    duration: 0
                                }
                                _this.$message.closeAll()
                                operationCompletion(_this, "操作成功，重置密码为：" + successArray[0].object.data.data, 'success', setting)
                                _this.returnList()
                            } else {
                                operationCompletion(_this, errorMsg(errorArray, array), 'error')
                            }
                        })
                    }).catch(() => {
                    });*/
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
            let keyArray = ['username', 'dbPrivileges<>.dbName', 'dbPrivileges<>.privileges']
            if (this.highAvailable) {
                keyArray.push('maxConnection')
                keyArray.push('properties.display')
            } else {
                keyArray.push('whiteIp')
            }
            commonSearchClick(this, keyArray, settings)
        },
        tableRowClassName: function (row) {
            return commonTableRowClassName(row)
        }
    }
})
window.onresize = function () {
    userListApp.table_heighth = commonOnresize()
}