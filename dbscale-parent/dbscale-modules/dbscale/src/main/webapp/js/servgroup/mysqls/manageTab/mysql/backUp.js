var backUpIndex = parent.layer.getFrameIndex(window.name);
var rowId = getQueryVariable("id")
new Vue({
    el: '#backUp',
    data: {
        createdNum: 0,
        endNum: 2,
        backUpTypeList: [],
        backupStorageTypeList: [],
        formData: {
            id: rowId,
            backupStorageType: '',
            backupType: '',
            fileRetentionTime: 3
        },
        formRules: {
            backupStorageType: [
                {required: true, message: '请选择备份存储类型', trigger: 'blur'}
            ],
            backupType: [
                {required: true, message: '请选择备份类型', trigger: 'blur'}
            ],
            fileRetentionTime: [
                {required: true, message: '请输入文件保留天数', trigger: 'blur'},
            ]
        }
    },
    created: function () {
        this.backupStorageTypeListView()
        this.backupTypeListView()
    },
    methods: {
        backupStorageTypeListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=backup_storage_type", function (response) {
                _this.backupStorageTypeList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        backupTypeListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=backup_type", function (response) {
                _this.backUpTypeList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        endFun: function () {
            var _this = this
            _this.createdNum++
            if (_this.createdNum === _this.endNum) {
                if (_this.backupStorageTypeList.length !== 0)
                    _this.formData.backupStorageType = _this.backupStorageTypeList[0].code
                if (_this.backUpTypeList.length !== 0)
                    _this.formData.backupType = _this.backUpTypeList[0].code
                layer.closeAll('loading');
            }
        },
        formSubmit: function (formName) {//backUpModal
            var verify = false;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    return true;
                } else {
                    verify = true;
                    return false;
                }
            });
            if (verify) {
                return false;
            }
            var _this = this

            var times = 24 * 60 * 60 * 1000
            var currentTime = new Date().getTime()
            var retentionTime = times * _this.formData.fileRetentionTime
            var RealTime = currentTime + retentionTime
            var backupStorageType = _this.formData.backupStorageType
            var backupType = _this.formData.backupType
            var expired = new Date(RealTime).setHours(23, 59, 59, 999) / 1000
            var id = _this.formData.id

            var jsonData = {
                "backupStorageType": backupStorageType,
                "type": backupType,
                "expired": parseInt(expired),
            }
            let parentApp = parent[0].mysqlListApp
            let typeName = XEUtils.isEmpty(parentApp.typeName) ? "数据库" : parentApp.typeName
            const riskText = "对选中的" + typeName + "单元进行备份，备份期间" + typeName + "会出现短暂不可用，对库备份请联系应用岗位评估可以备份的时间！"
            let setting = {
                customClass: "customizeWidth510pxClass"
            }
            parent.layer.min(backUpIndex);
            commonConfirm(parent.manageApp, '备份确认', getRiskLevelHtml(1, riskText) + getHintText('备份'), setting).then(() => {
                parent.layer.restore(backUpIndex);
                sendPut("/" + getProjectName() + "/units/mysql/" + id + "/backup", function (response) {
                    layer.closeAll('loading')
                    parentApp.refreshSaveData.push(parentApp.selectedData)
                    parentApp.returnList()
                    _this.formClose()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            }).catch(() => {
                parent.layer.restore(backUpIndex);
            });
        },
        formClose: function () {
            parent.layer.close(backUpIndex);
        }
    }
})