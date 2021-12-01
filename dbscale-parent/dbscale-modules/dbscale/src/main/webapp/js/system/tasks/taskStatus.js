var actionListApp = new Vue({
    el: '#taskStatus',
    data: {
        title: '任务查询',
        table_heighth: '',
        allAlign: ALLALIGN,
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
            var data = row.state
            if (type === 'task') {
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
        }
    },
    created: function () {
        var _this = this
        this.SearchTasks()
        setTimeout(function () {
            getHtmlMinWidth("taskStatus", _this, ['xTable'])
        }, 0)
    },
    methods: {
        refreshAjaxFun: function (url) {
            var _this = this
            sendGet(url, function (response) {
                _this.dataDispose(response.data.data)
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        returnList: function () {
            this.refreshAjaxFun(this.oldUrl)
        },
        SearchTasks: function () {
            let url = "/" + getProjectName() + "/tasks?site_id=" + getSession("siteId")
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
        dataDispose: function (data) {
            var jq_jsonData = [];
            var jq_json = {};
            XEUtils.arrayEach(data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))
                var consumeTime = Math.floor((new Date(v.endDateTime).getTime() - new Date(v.startDateTime).getTime()) / 1000)

                if (XEUtils.isEmpty(jsonJudgeNotDefined(v, 'v.startDateTime'))) {
                    consumeTime = ""
                } else if (consumeTime < 0) {
                    consumeTime = ""
                }
                var actionDisplay = jsonJudgeNotDefined(v, 'v.action.display')
                var statusDisplay = jsonJudgeNotDefined(v, 'v.state.display')
                var statusText = ''
                if (!XEUtils.isEmpty(actionDisplay) && !XEUtils.isEmpty(statusDisplay))
                    statusText = actionDisplay + statusDisplay

                json.creatorName = ownerNameDispose(v.created.name, v.created.username)
                json.status = v.state
                json.statusText = statusText
                json.consumeTime = consumeTime
                json.childRows = []
                jq_jsonData.push(json);
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
            this.table_heighth = commonOnresize()
        },
        sortChange: function (column) {
            commonSortChange(this, column)
        },
        cancelBtn:function (data) {
    		var _this = this 
    		commonConfirm(_this, '任务取消确认', getHintText('取消')).then(() => {
    			sendPut("/" + getProjectName() + "/tasks/" + data.id + "/cancel", function (response) {
                    layer.closeAll('loading')
                    _this.refreshSaveData.push(_this.selectedData)
                    operationCompletion(_this, "操作成功！")
                    _this.returnList()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, "error")
                }, null)
            }).catch(() => {
            });
    	},
        loadContentMethod({row}) {
            var _this = this
            return new Promise(resolve => {
                sendGet("/" + getProjectName() + "/tasks/" + row.id, function (response) {
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
        toggleExpandRow: function (row) {
            var _this = this
            XEUtils.arrayEach(_this.tableData, (val, index) => {
                if (row.id === val.id) {
                    _this.$refs.xTable.toggleRowExpand(_this.tableData[index])
                }
            })
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
    actionListApp.table_heighth = commonOnresize()
}