var editIndex = parent.layer.getFrameIndex(window.name);
var groupId = getQueryVariable("groupId")
new Vue({
    el: '#edit',
    data: {
        formData: {
            name: '',
            description: ''
        },
        formRules: {
            name: [
                {required: true, message: '请输入组名'}
            ]
        }
    },
    created: function () {
        this.editCreated()
    },
    methods: {
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/groups/" + groupId, function (response) {
                var data = response.data.data;
                _this.formData = {
                    name: jsonJudgeNotDefined(data, 'data.name'),
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
                "description": this.formData.description
            }
            var _this = this
            commonConfirm(_this, '编辑确认', getHintText('编辑')).then(() => {
                sendPut("/" + getProjectName() + "/groups/" + groupId, function (response) {
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