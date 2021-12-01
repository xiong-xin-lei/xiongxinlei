var addIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#Sysadd',
    data: {
        formData: {
            name: '',
            enabled: true,
            description: ''

        },
        formRules: {
            name: [
                {required: true, message: '请输入业务系统'}
            ]
        }
    },
    created: function () {
    },
    methods: {
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
                "enabled": this.formData.enabled,
                "description": this.formData.description
            }
            var _this = this
            //console.log("新增",jsonData)
            commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
                sendPost("/" + getProjectName() + "/business_systems", function (response) {
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