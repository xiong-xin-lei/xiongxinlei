const validateValue = (rule, value, callback) => {
    var reg = new RegExp("[\u4e00-\u9fa5]");
    if (reg.test(value)) {
        callback(new Error("值不能含有中文字符"));
    }
};
var highAvailable = parent.manageApp.highAvailable
var tabParamCfgListApp = new Vue({
    el: '#tabParamCfgList',
    data: {
        title: '参数配置',
        table_heighth: '',
        highAvailable: highAvailable,
        rowData: {},
        btnList: ttBtnList,
        rowBtnList: rowBtnList,
        editDisabledFlag: () => XEUtils.findIndexOf(otherBtnList, item => item.code === "btnUpdate") !== -1,
        editIcon: function () {
            if (this.editDisabledFlag()) {
                return ""
            } else {
                return " "
            }
        },
        unitValue: "mysql",
        unitList: [
            {
                code: "mysql",
                name: "数据库"
            }, {
                code: "cmha",
                name: "高可用"
            }
        ],
        btnReplaceList: {
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
            } else if (type === 'disabled') {
                switch (value) {
                    case this.btnReplaceList.start:
                        return false

                    default:
                        return false
                }
            }
        },
        editActived: '',
        allAlign: ALLALIGN,
        tableDataAll: [],
        tableData: [],
        beForeEdit: [],
        searchData: [],
        sortData: [],
        filterName: '',
        tablePage: {
            currentPage: 1,
            pageSize: PAGE_SIZE,
            totalResult: 0,
            pageSizes: PAGE_SIZES,
            pageLayouts: PAGE_LAYOUTS
        },
        validRules: {
            value: [
                {validator: validateValue}
            ]
        }
    },
    created: function () {
        this.btnClick(this.btnReplaceList.refresh, false, null)
    },
    methods: {
        dataDispose: function (data) {
            var _this = this
            var jq_jsonData = [];
            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))

                var cansetText = "";
                if (v.canSet !== null) {
                    if (v.canSet) {
                        cansetText = "是"
                    } else {
                        cansetText = "否"
                    }
                }
                var mustRestartText = "";
                if (v.mustRestart !== null) {
                    if (v.mustRestart) {
                        mustRestartText = "是"
                    } else {
                        mustRestartText = "否"
                    }
                }

                json.cansetText = cansetText
                json.mustRestartText = mustRestartText

                jq_jsonData.push(json);
            });
            this.tableDataAll = jq_jsonData;
            this.searchData = this.tableDataAll.concat();
            this.sortData = this.searchData.concat();
            this.searchClick({changePage: true})
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
        unitChange: function () {
            this.btnClick(this.btnReplaceList.refresh, false, null)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/serv_groups/" + rowId + "/cfgs?type=" + _this.unitValue, function (response) {
                successFun(response, getCheckboxTablerId);
                layer.closeAll('loading');
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
            if (getCheckboxTablerNum === 1 && array) {
                array = false
                getCheckboxTablerId = getCheckboxTablerData[0]
            }
            switch (code) {
                case this.btnReplaceList.refresh://刷新
                    if (getCheckboxTablerNum) {
                        this.$refs.xTable.clearCheckboxRow()
                        this.$refs.xTable.clearRowExpand()
                    }

                    this.refreshAjaxFun(_this.servData, function (response, sendData) {
                        _this.dataDispose(response.data)
                        _this.beForeEdit = response.data.data
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
        tableRowClassName: function ({row, rowIndex, $rowIndex}) {
            if (this.editDisabledFlag()) {
                if (row.canSet === false) {
                    return 'info-row';
                } else {
                    return ''
                }
            } else {
                return false
            }
        },
        activeCellMethod({row, column, columnIndex}) {
            if (this.editDisabledFlag()) {
                return row.canSet
            } else {
                return false
            }
        },
        editClosedEvent: function ({row}) {
            var _this = this
            var flag = false
            var jsonData = {}

            jsonData.key = row.key
            jsonData.value = row.value

            var reg = new RegExp("[\u4e00-\u9fa5]");
            if (!reg.test(row.value)) {
                flag = true
            } else {
                operationCompletion(_this, "值不能含有中文字符", 'error', {"duration": "3000"})
            }

            var beForeEditValue = ""
            for (var i = 0; i < _this.beForeEdit.length; i++) {
                if (row.key === _this.beForeEdit[i].key) {
                    beForeEditValue = _this.beForeEdit[i].value
                }
            }

            if (flag) {
                commonConfirm(parent.manageApp, '修改确认', getHintText("", "", '是否将"' + row.key + '"的值修改为' + row.value + '?')).then(() => {
                    sendPut("/" + getProjectName() + "/serv_groups/" + rowId + "/cfgs?type=" + _this.unitValue, function (response) {
                        layer.closeAll('loading')
                        _this.$message.closeAll();
                        operationCompletion(_this, "参数配置编辑成功！", 'success', {"duration": "3000"})
                        _this.returnList()
                    }, function (error) {
                        row.value = beForeEditValue
                        operationCompletion(_this, error.response.data.msg, 'error')
                    }, jsonData)
                }).catch(() => {
                    row.value = beForeEditValue
                })
            }
        },
        selectAllEvent: function ({checked, records}) {
            //console.log(checked ? '所有勾选事件' : '所有取消事件', records)
        },
        selectChangeEvent: function ({checked, records}) {
            //console.log(checked ? '勾选事件' : '取消事件', records)
        },
        searchClick: function (settings) {
            let keyArray = ['key', 'value', 'defaultValue', 'range', 'cansetText', 'mustRestartText', 'desc']
            commonSearchClick(this, keyArray, settings)
        }
    }
})
window.onresize = function () {
    tabParamCfgListApp.table_heighth = commonOnresize()
}