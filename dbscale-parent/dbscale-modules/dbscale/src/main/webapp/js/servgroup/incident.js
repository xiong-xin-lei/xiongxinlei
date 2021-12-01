var objType = getQueryVariable("obj_type")
var objId = getQueryVariable("obj_id")
var incidentListApp = new Vue({
    el: '#incidentList',
    data: {
        btnReplaceList: {
            refresh: "refresh"
        },
        childArray: [
            {
                code: "unit",
                value: "units"
            }, {
                code: "pods",
                value: "pods"
            }, {
                code: "networkClaim",
                value: "networkClaims"
            }, {
                code: "volumePathData",
                value: "volumePathDatas"
            }, {
                code: "volumePathLog",
                value: "volumePathLogs"
            }
        ],
        statusShow: function (row, type) {
            if (type === 'state') {
                switch (row.code) {
                    case "passing":
                        return getProjectSvg(COLOR_PASSING)
                    case "critical":
                        return "<i class='el-icon-view' style='color: " + COLOR_CRITICAL + "'></i>"
                    case "unknown":
                        return "<i class='el-icon-view' style='color: " + COLOR_UNKNOWN + "'></i>"
                    case "warning":
                        return "<i class='el-icon-view' style='color: " + COLOR_WARNING + "'></i>"
                    default:
                        return "<i class='el-icon-view' style='color: " + COLOR_UNKNOWN + "'></i>"
                }
            }
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
            var jq_jsonData = [];
            var jq_json = {};
            data.data.servs.forEach(function (value, index) {
                if (!XEUtils.isEmpty(value.units)) {
                    value.units.forEach(function (v, i) {
                        var json = JSON.parse(JSON.stringify(v))
                        var ipPort = jsonJudgeNotDefined(v, 'v.ip')
                        var port = jsonJudgeNotDefined(v, 'v.port')
                        if (ipPort !== "") {
                            ipPort += ":" + port
                        }
                        json.ipPort = ipPort
                        json.childRows = []
                        jq_jsonData.push(json);

                    })
                }
            });
            this.tableDataAll = jq_jsonData;
            this.searchData = this.tableDataAll.concat();
            this.sortData = this.searchData.concat();
            this.handlePageChange()
        },
        returnList: function () {
            document.location.reload();
        },
        handleSizeCurrentChange: function (value) {
            commonHandleSizeCurrentChange(this, value)
        },
        handlePageChange: function () {
            commonHandlePageChange(this)
            this.table_heighth = commonOnresize()
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/serv_groups/" + objType + "/" + objId, function (response) {
                successFun(response, getCheckboxTablerId);
                layer.closeAll('loading');
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        toggleExpandRow: function (row, key) {
            var _this = this
            XEUtils.arrayEach(_this.tableData, (val, index) => {
                if (row[key] === val[key] && row.state.code !== 'passing') {
                    _this.$refs.xTable.toggleRowExpand(_this.tableData[index])
                }
            })
        },
        btnClick: function (code, array, getCheckboxTablerId) {
            var _this = this
            switch (code) {
                case this.btnReplaceList.refresh://刷新
                    this.refreshAjaxFun(getCheckboxTablerId,
                        function (response, sendData) {
                            _this.dataDispose(response.data)
                        }, function (error, sendData) {
                        	operationCompletion(_this, error.response.data.msg, 'error')
                        })
                    break;
            }
        },
        sortChange: function (column) {
            commonSortChange(this, column)
        },
        loadContentMethod({row, rowIndex}) {
            var _this = this
            return new Promise(resolve => {
                sendGet("/" + getProjectName() + "/units/" + row.id + "/events", function (response) {
                    var jsonData = JSON.parse(JSON.stringify(response.data.data));
                    XEUtils.arrayEach(_this.childArray, (value, index) => {
                        row.childRows[value.value] = jsonData[value.value].reverse()
                    })
                    row.rowIndex = rowIndex;
                    layer.closeAll('loading');
                    resolve()
                }, function (error) {
                	operationCompletion(_this, error.response.data.msg, 'error')
                }, null)
            })
        },
        expandClick: function (rowIndex, listValue, refName, parentRowIndex) {
            this.$refs[refName][0].toggleRowExpand(this.tableData[parentRowIndex].childRows[listValue][rowIndex])
        }
    }
})