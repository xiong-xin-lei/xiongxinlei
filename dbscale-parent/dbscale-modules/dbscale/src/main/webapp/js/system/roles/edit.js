var editIndex = parent.layer.getFrameIndex(window.name);
var roleId = getQueryVariable("roleId")
new Vue({
    el: '#edit',
    data: {
        dataScopeList: [],
        formData: {
            name: '',
            dataScope: '',
            manager: false,
            description: ''
        },
        formRules: {
            name: [
                {required: true, message: '请输入名称'}
            ],
            dataScope: [
                {required: true, message: '请选择可见数据范围'}
            ],
            manager: [
                {required: true, message: '请选择管理角色'}
            ]
        }
    },
    created: function () {
        this.dataScopeCreated()
    },
    methods: {
        dataScopeCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=data_scope", function (response) {
                _this.dataScopeList = response.data.data
                _this.editCreated();
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/roles/" + roleId, function (response) {
                var data = response.data.data;
                _this.formData = {
                    name: jsonJudgeNotDefined(data, 'data.name'),
                    dataScope: jsonJudgeNotDefined(data, 'data.dataScope.code'),
                    manager: jsonJudgeNotDefined(data, 'data.manager'),
                    description: jsonJudgeNotDefined(data, 'data.description')
                }
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        formSubmit: function (formName) {//editModal
            var verify = false;
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

            var jsonData = {
                "name": this.formData.name,
                "dataScope": this.formData.dataScope,
                "manager": this.formData.manager,
                "description": this.formData.description
            }
            var _this = this
            commonConfirm(_this, '编辑确认', getHintText('编辑')).then(() => {
                sendPut("/" + getProjectName() + "/roles/" + roleId, function (response) {
                    layer.closeAll('loading')
                    operationCompletion(parent.listApp, "操作成功！")
                    parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                    parent.listApp.returnList()
                    _this.formClose()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            }).catch(() => {
            });
        },
        formClose: function () {
            parent.layer.close(editIndex);
        }
    }
})