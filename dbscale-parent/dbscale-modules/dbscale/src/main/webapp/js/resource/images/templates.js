var editIndex = parent.layer.getFrameIndex(window.name)
var architecture = getQueryVariable("architecture")
var listApp = new Vue({
    el: '#templates',
    data: {
        title: '配置',
        table_heighth: '',
        type: '',
        menu: menu,
        images: '',
        tableData: [],
        tableDataAll: [],
        editDisabledFlag: () => XEUtils.findIndexOf(otherBtnList, item => item.code === "btnUpdate") !== -1,
        editIcon: function () {
            if (this.editDisabledFlag()) {
                return ""
            } else {
                return " "
            }
        },
        beForeEdit: [],
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
        this.type = type
        this.images = images
        this.refreshAjaxFun()
        setTimeout(function () {
            getHtmlMinWidth("templates", _this, ['xTable'])
        }, 0)
    },
    methods: {
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            var major = _this.images.split(".")[0]
            var minor = _this.images.split(".")[1]
            var patch = _this.images.split(".")[2]
            var build = _this.images.split(".")[3]
            sendGet("/" + getProjectName() + "/images/" + _this.type + "/templates?site_id=" + getSession('siteId') + "&major=" + major + "&minor=" + minor + "&patch=" + patch + "&build=" + build + "&architecture=" + architecture, function (response) {
                _this.dataDispose(response.data)
                _this.beForeEdit = response.data.data
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        dataDispose: function (data) {
            var jq_jsonData = [];
            var jq_json = {};
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

                jq_jsonData.push(json)
            });
            this.tableDataAll = jq_jsonData
            this.searchData = this.tableDataAll.concat()
            this.sortData = this.searchData.concat()
            this.searchClick({changePage: true})
        },
        returnList: function () {
            this.refreshAjaxFun()
        },
        handleSizeCurrentChange: function (value) {
            commonHandleSizeCurrentChange(this, value)
        },
        handlePageChange: function () {
            commonHandlePageChange(this)
            this.table_heighth = commonOnresize()
        },
        sortChange: function (column, settings) {
            commonSortChange(this, column, settings)
        },
        editClosedEvent: function ({row}) {
            var _this = this
            var jsonData = {
                "key": row.key,
                "value": row.value,
                "range": row.range,
                "defaultValue": row.defaultValue,
                "canSet": row.canSet,
                "mustRestart": row.mustRestart,
                "description": row.description
            }

            var beForeEditValue = {}
            for (var i = 0; i < _this.beForeEdit.length; i++) {
                if (row.key === _this.beForeEdit[i].key) {
                    beForeEditValue = _this.beForeEdit[i]
                }
            }

            commonConfirm(this, '修改确认', getHintText('修改', row.key)).then(() => {
                var major = _this.images.split(".")[0]
                var minor = _this.images.split(".")[1]
                var patch = _this.images.split(".")[2]
                var build = _this.images.split(".")[3]
                sendPut("/" + getProjectName() + "/images/" + _this.type + "/templates?site_id=" + getSession('siteId') + "&major=" + major + "&minor=" + minor + "&patch=" + patch + "&build=" + build + "&architecture=" + architecture,
                    function (response) {
                        layer.closeAll('loading')
                        _this.$message.closeAll()
                        operationCompletion(_this, '配置编辑成功', 'success')
                        _this.refreshAjaxFun()
                    }, function (error) {
                        row.value = beForeEditValue.value
                        row.defaultValue = beForeEditValue.defaultValue
                        row.range = beForeEditValue.range
                        row.canSet = beForeEditValue.canSet
                        row.mustRestart = beForeEditValue.mustRestart
                        row.description = beForeEditValue.description
                        operationCompletion(_this, error.response.data.msg, 'error')
                    }, jsonData)
            }).catch(() => {
                row.value = beForeEditValue.value
                row.defaultValue = beForeEditValue.defaultValue
                row.range = beForeEditValue.range
                row.canSet = beForeEditValue.canSet
                row.mustRestart = beForeEditValue.mustRestart
                row.description = beForeEditValue.description
            })
        },
        selectChangeEvent: function ({checked, records}) {
            commonCancelHighlight(this)
        },
        searchClick: function (settings) {
            let keyArray = ['key', 'value', 'defaultValue', 'range', 'cansetText', 'mustRestartText', 'description']
            commonSearchClick(this, keyArray, settings)
        },
        tableRowClassName: function (row) {
            return commonTableRowClassName(row)
        },
        formClose: function () {
            parent.layer.close(addIndex)
        }
    }
})
window.onresize = function () {
    listApp.table_heighth = commonOnresize()
}