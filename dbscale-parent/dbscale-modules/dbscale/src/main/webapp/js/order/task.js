var objType = getQueryVariable("obj_type")
var objId = getQueryVariable("obj_id")
var actionListApp = new Vue({
    el: '#actionList',
    data: {
        expandRowKeys: [],
        childExpandRowKeys: [],
        btnReplaceList: {
            refresh: "refresh"
        },
        statusShow: function (row, type) {
            if (type === 'task') {
                var data = row.status
                if (data !== null) {
                    switch (data.code) {
                        case "success":
                            return getProjectSvg(COLOR_TASK_COMPLETED) + "<span>" + row.action.display + row.status.display + "</span>"
                        case "failed":
                            return getProjectSvg(COLOR_TASK_ERROR) + "<span>" + row.action.display + row.status.display + "</span>"
                        case "running":
                            return getProjectSvg(COLOR_TASK_RUNNING) + "<span>" + row.action.display + row.status.display + "</span>"
                        case "timeout":
                            return getProjectSvg(COLOR_TASK_TIMEOUT) + "<span>" + row.action.display + row.status.display + "</span>"
                        case "ready":
                            return getProjectSvg(COLOR_TASK_READY) + "<span>" + row.action.display + row.status.display + "</span>"
                        case "unknown":
                            return getProjectSvg(COLOR_TASK_UNKNOWN) + "<span>" + row.action.display + row.status.display + "</span>"
                    }
                } else {
                    return ""
                }
            }
        },
        childExpandRowKeysFun: function (index) {
            return index === 0 ? this.childExpandRowKeys : []
        },
        allAlign: ALLALIGN,
        tableDataAll: [],
        tableData: [],
        searchData: [],
        sortData: [],
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
        this.btnClick(this.btnReplaceList.refresh, true, 0)
    },
    methods: {
        dataDispose: function (data) {
            var tempData = []
            var jq_jsonData = []
            var jq_json = {}
            if (XEUtils.isArray(data.data)) {
                tempData = data.data
            } else {
                tempData.push(data.data)
            }
            XEUtils.arrayEach(tempData, (v, i) => {

                var consumeTime = Math.floor((new Date(v.endDateTime).getTime() - new Date(v.startDateTime).getTime()) / 1000)

                if (XEUtils.isEmpty(jsonJudgeNotDefined(v, 'v.startDateTime'))) {
                    consumeTime = ""
                } else if (consumeTime < 0) {
                    consumeTime = ""
                }

                if (i === 0 && jsonJudgeNotDefined(v, "v.state.code") !== "success") {
                    this.expandRowKeys = []
                    this.expandRowKeys.push(v.id)
                }

                jq_json = {
                    "id": v.id,
                    "index": i,
                    "action": v.action,
                    "startDateTime": v.startDateTime,
                    "endDateTime": v.endDateTime,
                    "consumeTime": consumeTime,
                    "status": v.state,
                    "createdName": ownerNameDispose(v.created.name, v.created.username),
                    "childRows": []
                };
                jq_jsonData.push(jq_json);
            });
            this.tableDataAll = jq_jsonData;
            this.searchData = this.tableDataAll.concat();
            this.sortData = this.searchData.concat();
            this.handlePageChange()
        },
        handleSizeCurrentChange: function (value) {
            commonHandleSizeCurrentChange(this, value)
        },
        handlePageChange: function () {
            commonHandlePageChange(this)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/tasks/" + objId, function (response) {
                successFun(response, getCheckboxTablerId);
                layer.closeAll('loading');
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        btnClick: function (code, array, getCheckboxTablerId) {
            var _this = this
            switch (code) {
                case this.btnReplaceList.refresh://刷新
                    this.refreshAjaxFun(getCheckboxTablerId,
                        function (response, sendData) {
                            _this.dataDispose(response.data)
                            setTimeout(function () {
                                if (_this.expandRowKeys.length !== 0)
                                    _this.toggleExpandRow(_this.tableDataAll[0])
                            }, 0)
                        }, function (error, sendData) {
                            console.log(error)
                            operationCompletion(_this, error.response.data.msg, 'error')
                        })
                    break;
            }
        },
        sortChange: function (column) {
            commonSortChange(this, column)
        },
        toggleExpandRow: function (row) {
            this.$refs.xTable.toggleRowExpand(row)
            this.rowExpand({
                expanded: true,
                row: row,
                rowIndex: row.index
            })
        },
        childTableToggleExpandRow: function (row) {
            if (!XEUtils.isEmpty(row.msg))
                this.$refs["childTable" + row.index].toggleRowExpand(row)
        },
        loadContentMethod({row, rowIndex}) {
            var _this = this
            return new Promise(resolve => {
                resolve()
            })
        },
        childDataDispose: function (v) {
            var jq_json = {};

            var consumeTime = Math.floor((new Date(v.endDateTime).getTime() - new Date(v.startDateTime).getTime()) / 1000)

            if (XEUtils.isEmpty(jsonJudgeNotDefined(v, 'v.startDateTime'))) {
                consumeTime = ""
            } else if (consumeTime < 0) {
                consumeTime = ""
            }

            if (!XEUtils.isEmpty(v.msg))
                this.childExpandRowKeys.push(v.id)

            jq_json = {
                "id": v.id,
                "action": v.action,
                "objName": v.objName,
                "priority": v.priority,
                "status": v.state,
                "startDateTime": v.startDateTime,
                "endDateTime": v.endDateTime,
                "consumeTime": consumeTime,
                "timeout": v.timeout,
                "msg": v.msg
            };
            return jq_json;
        },
        rowExpand: function ({expanded, row, rowIndex}) {
            var xTable = this.$refs.xTable
            var _this = this
            if (expanded) {
                //xTable.reloadExpandContent(row)
                sendGet("/" + getProjectName() + "/tasks/" + row.id, function (response) {
                    layer.closeAll('loading');
                    var value = response.data.data
                    var jsonData = value;
                    var consumeTime = Math.floor((new Date(value.endDateTime).getTime() - new Date(value.startDateTime).getTime()) / 1000)
                    if (XEUtils.isEmpty(jsonJudgeNotDefined(value, 'value.startDateTime'))) {
                        consumeTime = ""
                    } else if (consumeTime < 0) {
                        consumeTime = ""
                    }

                    this.childExpandRowKeys = []

                    jsonData.index = rowIndex
                    jsonData.status = value.state
                    jsonData.consumeTime = consumeTime
                    jsonData.createdName = ownerNameDispose(value.created.name, value.created.username)

                    var arrayData = []
                    XEUtils.arrayEach(value.subtasks, (v, i) => {
                        let childTempData = _this.childDataDispose(v)
                        childTempData.index = row.index
                        arrayData.push(childTempData)
                    })
                    row.childRows = arrayData;
                    jsonData.childRows = arrayData;
                    _this.tableDataAll[rowIndex] = jsonData
                    _this.searchData = _this.tableDataAll.concat();
                    _this.sortData = _this.searchData.concat();
                    _this.handlePageChange()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, null)
            }
        },
        refreshFristClick: function (row, index) {
            var xTable = this.$refs.xTable
            var rowExpand = xTable.isExpandByRow(row)
            /*var rowExpandLazy = xTable.isRowExpandLoaded(row)*/
            var _this = this
            if (rowExpand) {
                // xTable.reloadExpandContent(_this.tableDataAll[index]){expanded, row, rowIndex}
                _this.rowExpand({
                    expanded: true,
                    row: row,
                    rowIndex: index
                })
            } else {
                sendGet("/" + getProjectName() + "/tasks/" + row.id, function (response) {
                    var value = response.data.data
                    var jsonData = value;
                    var consumeTime = Math.floor((new Date(value.endDateTime).getTime() - new Date(value.startDateTime).getTime()) / 1000)
                    if (XEUtils.isEmpty(jsonJudgeNotDefined(value, 'value.startDateTime'))) {
                        consumeTime = ""
                    } else if (consumeTime < 0) {
                        consumeTime = ""
                    }
                    jsonData.status = value.state
                    jsonData.index = index
                    jsonData.consumeTime = consumeTime
                    jsonData.createdName = ownerNameDispose(value.created.name, value.created.username)
                    _this.tableDataAll[index] = jsonData
                    _this.searchData = _this.tableDataAll.concat();
                    _this.sortData = _this.searchData.concat();
                    _this.handlePageChange()
                    layer.closeAll('loading');
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, null)
            }
        }
    }
})