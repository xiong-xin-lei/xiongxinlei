var listApp = new Vue({
    el: '#list',
    data: {
        title: '操作记录',
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
                json.creatorName = ownerNameDispose(v.created.name, v.created.username)
                json.siteName = jsonJudgeNotDefined(v, 'v.site.name')
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
            let url = "/" + getProjectName() + "/operatelogs?site_id=" + getSession("siteId")
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
        tableRowClassName: function (row) {
            return commonTableRowClassName(row)
        }
    }
})
window.onresize = function () {
    listApp.table_heighth = commonOnresize()
}