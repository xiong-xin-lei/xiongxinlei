var listApp = new Vue({
    el: '#list',
    data: {
        title: '强制重建记录',
        table_heighth: '',
        tableDataAll: [],
        tableData: [],
        searchData: [],
        sortData: [],
        refreshKey: ["id"],
        refreshSaveData: [],
        tablePage: {
            currentPage: 1,
            pageSize: PAGE_SIZE,
            totalResult: 0,
            pageSizes: PAGE_SIZES,
            pageLayouts: PAGE_LAYOUTS
        },
        startTime: XEUtils.timestamp(XEUtils.getWhatWeek(new Date(), -1)),
        endTime: XEUtils.now(),
        oldUrl: '',
        pickerOptions: {
            disabledDate(time) {
                return time.getTime() > Date.now();   //禁用以后的日期，今天不禁用
            }
        },
        statusShow: function (row, type) {
            let data = ''
            let taskText = ''
            let taskTextColor = '#000'
            if (type === 'task' || type === 'subTask') {
                switch (type) {
                    case "task":
                        data = jsonJudgeNotDefined(row, 'row.task.state')
                        taskText = jsonJudgeNotDefined(row, 'row.task.action.display') + jsonJudgeNotDefined(row, 'row.task.state.display')
                        break
                    case "subTask":
                        data = jsonJudgeNotDefined(row, 'row.state')
                        taskText = jsonJudgeNotDefined(row, 'row.action.display') + jsonJudgeNotDefined(row, 'row.state.display')
                        break
                }
                if (!XEUtils.isEmpty(data)) {
                    switch (data.code) {
                        case "success":
                            taskTextColor = COLOR_TASK_COMPLETED
                            break
                        case "failed":
                            taskTextColor = COLOR_TASK_ERROR
                            break
                        case "running":
                            taskTextColor = COLOR_TASK_RUNNING
                            break
                        case "timeout":
                            taskTextColor = COLOR_TASK_TIMEOUT
                            break
                        case "unknown":
                            taskTextColor = COLOR_TASK_UNKNOWN
                            break
                    }
                    // return "<span style='color:" + taskTextColor + ";border-color:" + taskTextColor + "'>" + taskText + "</span>"
                    return getProjectSvg(taskTextColor) + "<span>" + taskText + "</span>"
                } else {
                    return ""
                }
            }
        }
    },
    created: function () {
        var _this = this
        this.SearchLogs()
        setTimeout(function () {
            getHtmlMinWidth("list", _this, ['xTable'])
        }, 0)
    },
    methods: {
        dataDispose: function (data) {
            let jq_jsonData = [];

            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))
                jq_jsonData.push(json);
            });
            this.tableDataAll = jq_jsonData;
            this.searchData = this.tableDataAll.concat();
            this.sortData = this.searchData.concat();
            this.handlePageChange()
        },
        refreshAjaxFun: function (url) {
            var _this = this
            sendGet(url, function (response) {
                _this.dataDispose(response.data)
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        returnList: function () {
            this.refreshAjaxFun(this.oldUrl)
        },
        SearchLogs: function () {
            let url = "/" + getProjectName() + "/operatelogs/force_rebuild?site_id=" + getSession("siteId")
            if (!XEUtils.isNull(this.startTime)) {
                let startTime = new Date(this.startTime).setHours(0, 0, 0, 0)
                url += "&start=" + parseInt(startTime / 1000)
            }
            if (!XEUtils.isNull(this.endTime)) {
                let endTime = new Date(this.endTime).setHours(23, 59, 59, 999)
                url += "&end=" + parseInt(endTime / 1000)
            }
            this.oldUrl = url
            this.tablePage.currentPage = 1
            this.refreshAjaxFun(url)
        },
        handleSizeCurrentChange: function (value) {
            commonHandleSizeCurrentChange(this, value)
        },
        handlePageChange: function () {
            commonHandlePageChange(this)
            this.table_heighth = commonOnresize()
        },
        sortChange: function (column) {
            commonSortChange(this, column)
        },
        loadContentMethod: function({row}) {
            var _this = this
            return new Promise(resolve => {
                sendGet("/" + getProjectName() + "/tasks/" + row.task.id, function (response) {
                    layer.closeAll('loading');
                    let responseData = response.data.data
                    var arrayData = []
                    XEUtils.arrayEach(responseData.subtasks, (v, i) => {
                        let data = JSON.parse(JSON.stringify(v))
                        data.parentId = responseData.id
                        arrayData.push(_this.childDataDispose(data))
                    })
                    row.childRows = arrayData;
                    resolve()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, null)
            })
        },
        childDataDispose: function (v) {
            var json = JSON.parse(JSON.stringify(v))
            var consumeTime = Math.floor((new Date(v.endDateTime).getTime() - new Date(v.startDateTime).getTime()) / 1000)

            if (XEUtils.isEmpty(jsonJudgeNotDefined(v, 'v.startDateTime'))) {
                consumeTime = ""
            } else if (consumeTime < 0) {
                consumeTime = ""
            }
            json.consumeTime = consumeTime
            json.status = v.state
            return json;
        },
        toggleExpandRow: function (data) {
            this.$refs.xTable.toggleRowExpand(data)
        },
        childTableToggleExpandRow: function (row) {
            if (!XEUtils.isEmpty(row.msg))
                this.$refs["childTable" + row.parentId].toggleRowExpand(row)
        },
        tableRowClassName: function (row) {
            return commonTableRowClassName(row)
        }
    }
})
window.onresize = function () {
    listApp.table_heighth = commonOnresize()
}