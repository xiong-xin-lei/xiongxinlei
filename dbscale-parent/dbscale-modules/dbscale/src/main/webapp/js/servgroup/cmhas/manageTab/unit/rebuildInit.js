var rebuildIndex = parent.layer.getFrameIndex(window.name);
var servGroupId = getQueryVariable("servGroupId")
var unitId = getQueryVariable("unitId")
var source = getQueryVariable("source")
new Vue({
    el: '#rebuild',
    data: {
        backupFileSource: 'backup',
        assignValue: "select",
        backupFileSourceList: [
            {
                code: "backup",
                name: "从当前主库备份"
            }, {
                code: "select",
                name: "从已存在备份文件中选择"
            }
        ],
        backupStorageTypeList: [],
        hostList: [],
        formData: {
            backupFileSource: 'backup',
            backupStorageType: '',
            host: ''
        },
        formRules: {
            backupFileSource: [
                {required: true, message: '请选择备份文件来源'}
            ],
            backupStorageType: [
                {required: true, message: '请选择备份存储类型'}
            ],
            host: [
                {required: true, message: '请选择指定主机'}
            ]
        },
        filterName: '',
        allAlign: ALLALIGN,
        tableDataAll: [],
        tableData: [],
        searchData: [],
        sortData: [],
        defaultSelecteRow: null,
        tablePage: {
            currentPage: 1,
            pageSize: PAGE_SIZE,
            totalResult: 0,
            pageSizes: PAGE_SIZES,
            pageLayouts: PAGE_LAYOUTS
        }
    },
    created: function () {
        this.backupFileSourceChange()
    },
    methods: {
        dataDispose: function (data) {
            var jq_jsonData = [];
            var _this = this
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

            this.tableDataAll = XEUtils.sortBy(jq_jsonData, 'createdAt').reverse();
            if (!XEUtils.isEmpty(this.tableDataAll))
                this.defaultSelecteRow = this.tableDataAll[0].id
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
        sortChange: function (column) {
            commonSortChange(this, column)
        },
        selectChangeEvent: function ({row, rowIndex}) {
            //console.log(checked ? '勾选事件' : '取消事件', records)
        },
        backupStorageTypeListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=backup_storage_type", function (response) {
                _this.backupStorageTypeList = response.data.data
                _this.formData.backupStorageType = _this.backupStorageTypeList[0].code
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, "error")
            }, null)
        },
        restoreGetFun: function () {
            var _this = this
            var url = "/" + getProjectName() + "/backup/files";
            var urlData = "?site_id=" + getSession("siteId") + "&success=true&type=full&serv_group_id=" + servGroupId;
            sendGet(url + urlData, function (response) {
                _this.tablePage.currentPage = 1
                _this.dataDispose(response.data)
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        backupFileSourceChange: function () {
            var _this = this
            switch (_this.formData.backupFileSource) {
                case 'backup':
                    _this.backupStorageTypeListView()
                    parent.layer.style(rebuildIndex, {
                        width: '542px',
                        height: '265px'
                    });
                    this.tablePage = {
                        "currentPage": 1,
                        "pageSize": PAGE_SIZE,
                        "totalResult": 0,
                        "pageSizes": PAGE_SIZES,
                        "pageLayouts": PAGE_LAYOUTS
                    }
                    this.tableDataAll = []
                    this.tableData = []
                    break;
                case 'select':
                    this.restoreGetFun()
                    parent.layer.style(rebuildIndex, {
                        width: '779px',
                        height: '453px'
                    });
                    break;
            }
        },
        formSubmit: function (formName, tableName) {//scaleUpModal
            let _this = this
            let verify = false;
            let jsonData = {}
            let radioDate
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    return true;
                } else {
                    verify = true;
                    //console.log('error submit!!');
                    return false;
                }
            });
            if (verify) {
                return false;
            }

            if (_this.formData.backupFileSource === _this.assignValue) {
                radioDate = this.$refs[tableName].getRadioRecord()
                if (XEUtils.isEmpty(radioDate)) {
                    return false;
                }
                jsonData.backupFileId = radioDate.id
            } else {
                jsonData.backupStorageType = _this.formData.backupStorageType
            }

            //console.log("新增",jsonData)
            let parentApp = parent[0].cmhaListApp
            let typeName = XEUtils.isEmpty(parentApp.typeName) ? "数据库" : parentApp.typeName

            let riskText = "对选中的" + typeName + "单元进行重建，重建操作会将原来的单元删除并创建一个一样的单元，强制重建有可能导致主机上的存储资源释放不掉，请谨慎操作！"

            parent.layer.min(rebuildIndex);
            commonConfirm(parent.manageApp, '重建初始化确认', /*getRiskLevelHtml(4, riskText) + */getHintText('重建初始化')).then(() => {
                parent.layer.restore(rebuildIndex);
                sendPut("/" + getProjectName() + "/units/" + unitId + "/rebuild_init", function (response) {
                    layer.closeAll('loading')
                    parentApp.refreshSaveData.push(parentApp.selectedData)
                    parentApp.returnList()
                    _this.formClose()
                }, function (error) {
                    console.log(error)
                    operationCompletion(_this, error.response.data.msg, "error")
                }, jsonData)
            }).catch(() => {
                parent.layer.restore(rebuildIndex);
            });
        },
        searchClick: function () {
            var keyArray = ['unitName', 'sizeData', 'createdAt', 'name']
            commonSearchClick(this, keyArray)
        },
        returnList: function () {
            document.location.reload();
        },
        formClose: function () {
            parent.layer.close(rebuildIndex);
        }
    },
    watch: {
        'formData.backupFileSource': function () {
            this.backupFileSourceChange()
        }
    }
})