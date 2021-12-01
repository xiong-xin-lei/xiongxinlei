var addIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#Subsadd',
    data: {
        formData: {
            name: '',
            description: '',
            enabled: true,
            businessSystemName: ''
        },
        formRules: {
            name: [
                {required: true, message: '请输入业务子系统'}
            ],
            businessSystemName: [
                {required: true, message: '请输入业务系统'}
            ],
        }
    },
    created: function () {
        this.businessSystemsListView()
    },
    methods: {
        businessSystemsListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/business_systems/" + SubsAddId, function (response) {
                _this.formData.businessSystemName = jsonJudgeNotDefined(response, "response.data.data.name")
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        formSubmit: function (formName) {//addModal
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
                "description": this.formData.description,
                "enabled": this.formData.enabled,
                "businessSystemId": SubsAddId
            }
            var _this = this
            //console.log("新增",jsonData)
            commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
                sendPost("/" + getProjectName() + "/business_subsystems", function (response) {
                    layer.closeAll('loading')
                    operationCompletion(parent.listApp, "操作成功！")
                    parent.listApp.returnList()
                    _this.formClose()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            }).catch(() => {
            });
        },
        formClose: function () {
            parent.layer.close(addIndex);
        }
    }
})