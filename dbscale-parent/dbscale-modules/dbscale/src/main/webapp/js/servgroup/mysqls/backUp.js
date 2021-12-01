var backUpIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#backUp',
    data: {
        backupStorageTypeList: [],
        formData: {
            id: '',
            backupStorageType: '',
            fileRetentionTime: 3
        },
        formRules: {
            backupStorageType: [
                {required: true, message: '请选择备份存储类型'}
            ],
            fileRetentionTime: [
                {required: true, message: '请输入文件保留天数', trigger: 'blur'},
            ]
        }
    },
    created: function () {
        var rowData = parent.listApp.rowData;
        this.formData = {
            id: rowData.id,
            fileRetentionTime: 3
        }
        this.backUpListView()
    },
    methods: {
        backUpListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=backup_storage_type", function (response) {
                _this.backupStorageTypeList = response.data.data
                layer.closeAll('loading');
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
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
            var expired = new Date(RealTime).setHours(23, 59, 59, 999) / 1000
            var id = _this.formData.id

            var jsonData = {
                "backupStorageType": backupStorageType,
                "type": "full",
                "expired": parseInt(expired),
            }
        	commonConfirm(_this, '备份确认', getHintText('备份')).then(() => {
                sendPut("/" + getProjectName() + "/serv_groups/mysql/" + id + "/backup", function (response) {
                    layer.closeAll('loading')
                    parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                    parent.listApp.returnList()
                    _this.formClose()
                }, function (error) {
                	operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            })
        },
        formClose: function () {
            parent.layer.close(backUpIndex);
        }
    }
})