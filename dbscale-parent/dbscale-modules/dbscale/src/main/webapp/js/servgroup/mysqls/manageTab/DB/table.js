var listApp = new Vue({
    el: '#list',
    data: {
        title: '表信息',
        table_heighth: '',
        menu: menu,
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
        this.btnClick('refresh', true, 0)
    },
    methods: {
        dataDispose: function (data) {
            var jq_jsonData = [];
            XEUtils.arrayEach(data.data.tables, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))
                json.sizeData = sizeSuffixAlter(jsonJudgeNotDefined(v, "v.size"))
                jq_jsonData.push(json)
            })

            this.tableDataAll = jq_jsonData;
            this.searchData = this.tableDataAll.concat();
            this.sortData = this.searchData.concat();
            this.searchClick({changePage: true})
        },
        returnList: function () {
            this.btnClick('refresh', true, 0)
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
            sendGet("/" + getProjectName() + "/serv_groups/mysql/" + rowId + "/db/schemas/" + schemaName, function (response) {
                successFun(response, getCheckboxTablerId);
                layer.closeAll('loading');
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        btnClick: function (code, array, getCheckboxTablerId) {
            var _this = this
            switch (code) {
                case 'refresh'://刷新
                    this.loading = true
                    this.refreshAjaxFun(getCheckboxTablerId,
                        function (response, sendData) {
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
        searchClick: function (settings) {
            let keyArray = ['name', 'sizeData']
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