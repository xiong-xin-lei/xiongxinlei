var listApp = new Vue({
    el: '#taskEdit',
    data: {
        title: '任务配置',
        table_heighth: '',
        allAlign: ALLALIGN,
        beForeEdit: [],
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
        this.refreshAjaxFun()
        setTimeout(function () {
            getHtmlMinWidth("taskEdit", _this, ['xTable'])
        }, 0)
    },
    methods: {
        refreshAjaxFun: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/subtask/cfgs", function (response) {
                _this.dataDispose(response.data.data)
                _this.beForeEdit = response.data.data
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        dataDispose: function (data) {
            var jq_jsonData = [];
            XEUtils.arrayEach(data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))
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
        editClosedEvent({row, column}, event) {
            var _this = this
            var jsonData = {
                "timeout": row.timeout,
                "description": row.description
            }
            sendPut("/" + getProjectName() + "/subtask/cfgs/" + row.objType.code + "/" + row.action.code, function (response) {
                layer.closeAll('loading')
                _this.$message.closeAll();
                operationCompletion(_this, "任务配置编辑成功！")
            }, function (error) {
                for (var i = 0; i < _this.beForeEdit.length; i++) {
                    if (row.action.display === _this.beForeEdit[i].action.display) {
                        row.timeout = _this.beForeEdit[i].timeout
                    }
                }
                operationCompletion(_this, error.response.data.msg, 'error')
            }, jsonData)
        },
        tableRowClassName: function (row) {
            return commonTableRowClassName(row)
        }
    }
})
window.onresize = function () {
    listApp.table_heighth = commonOnresize()
}