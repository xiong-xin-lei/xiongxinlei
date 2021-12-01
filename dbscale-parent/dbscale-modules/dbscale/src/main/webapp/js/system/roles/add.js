var addIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#add',
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
                // _this.formData.dataScope = getSession("siteId");
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
                "dataScope": this.formData.dataScope,
                "manager": this.formData.manager,
                "description": this.formData.description
            }
            var _this = this
            // console.log("新增", jsonData)
            commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
                sendPost("/" + getProjectName() + "/roles", function (response) {
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