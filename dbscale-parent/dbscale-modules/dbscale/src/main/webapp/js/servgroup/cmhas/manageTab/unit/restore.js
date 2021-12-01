var cmhaRestoreIndex = parent.layer.getFrameIndex(window.name);
var unitId = getQueryVariable("unitId")
var unitName = getQueryVariable("unitName")
var rowId = getQueryVariable("rowId")
var cmhaRestoreApp = new Vue({
    el: '#cmhaRestore',
    data: {
        btnReplaceList: {
            submit: "submit",
            refresh: "refresh"
        },
        restoreRangeList: [
            {
                code: "nowService",
                name: "当前服务"
            }, {
                code: "allService",
                name: "全部"
            }
        ],
        restoreRange: "nowService",
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

            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))

                var sizeNum = 0
                if (jsonJudgeNotDefined(v, "v.size")) {
                    sizeNum = jsonJudgeNotDefined(v, "v.size")
                }
                json.sizeData = sizeSuffixAlter(sizeNum * 1024)
                json.consumingTime = differenceTime(jsonJudgeNotDefined(v, "v.createdAt"), jsonJudgeNotDefined(v, "v.finishAt"))

                jq_jsonData.push(json)
            })

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
        submitAjaxFun: function (data, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/units/mysql/" + unitId + "/restore", function (response) {
                successFun(response);
                layer.closeAll('loading')
            }, function (error) {
                falseFun(error)
            }, data)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            var url = "/" + getProjectName() + "/backup/files";
            var urlData = "?site_id=" + getSession("siteId") + "&success=true&type=full&serv_group_id=" + rowId;
            switch (_this.restoreRange) {
                case "nowService":
                    urlData = "?site_id=" + getSession("siteId") + "&success=true&type=full&serv_group_id=" + rowId
                    break;
                case "allService":
                    urlData = "?site_id=" + getSession("siteId") + "&success=true&type=full"
                    break;
            }
            sendGet(url + urlData, function (response) {
                successFun(response, getCheckboxTablerId);
                layer.closeAll('loading');
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        btnClick: function (code, array, getCheckboxTablerId) {
            var _this = this
            var url = ""
            switch (code) {
                case this.btnReplaceList.submit://提交
                    if (getCheckboxTablerId) {
                        let parentApp = parent[0].cmhaListApp
                        let typeName = XEUtils.isEmpty(parentApp.typeName) ? "数据库" : parentApp.typeName
                        const riskText = "使用一个已经备份好的镜像，对于选中的" + typeName + "单元进行还原，还原操作会将选中的" + typeName + "原来的数据全部覆盖，并且无法恢复，请谨慎操作！"
                        let setting = {
                            customClass: "customizeWidth510pxClass"
                        }
                        parent.layer.min(cmhaRestoreIndex);
                        commonConfirm(parent.manageApp, '还原确认', getRiskLevelHtml(4, riskText) + getHintText('还原'), setting).then(() => {
                            parent.layer.restore(cmhaRestoreIndex);
                            var submitData = {
                                "backupFileId": getCheckboxTablerId.id
                            }
                            _this.submitAjaxFun(submitData, function (data) {
                                layer.closeAll('loading')
                                // operationCompletion(parent.manageApp, "操作成功！")
                                parent[0].cmhaListApp.refreshSaveData.push(parent[0].cmhaListApp.selectedData)
                                parent[0].cmhaListApp.returnList()
                                _this.formClose()
                            }, function (error) {
                                operationCompletion(_this, error.response.data.msg, 'error')
                            })
                        }).catch(() => {
                            parent.layer.restore(cmhaRestoreIndex);
                        });
                    }
                    break;

                case this.btnReplaceList.refresh://刷新
                    this.refreshAjaxFun(getCheckboxTablerId,
                        function (response, sendData) {
                            _this.dataDispose(response.data)
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
        selectChangeEvent: function ({row}) {
        },
        dropdownsBtnOperation: function (code, data, type) {
            if (type === 'show') {
                var boolean = false;
                this.btnList.forEach(function (v, i) {
                    if (v.code === code) {
                        boolean = true;
                        return false;
                    }
                })
                return boolean;
            } else if (type === 'disabled') {
                return false
            }
        },
        restoreRangeChange: function () {
            this.btnClick(this.btnReplaceList.refresh, true, 0)
        },
        searchClick: function () {
            let keyArray = ['name', 'sizeData', 'type.display', 'backupStorageType.display', 'unitName', 'createdAt', 'status.display']
            commonSearchClick(this, keyArray)
        },
        formClose: function () {
            parent.layer.close(cmhaRestoreIndex);
        }
    }
})