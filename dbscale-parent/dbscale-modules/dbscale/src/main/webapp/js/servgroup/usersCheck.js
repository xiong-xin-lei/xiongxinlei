var obj_id = getQueryVariable("row_id")
var obj_type = getQueryVariable("obj_type")
var obj_relateId = getQueryVariable("row_relateId")
var open_type = getQueryVariable("open_type")
var usersCheckIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#usersCheck',
    data: {
        labelWidth: '55px',
        closeTimeShow: false,
        closeTimeList: [{
            code: 0,
            name: '无限制'
        }, {
            code: 5 * 60,
            name: '5分钟'
        }, {
            code: 10 * 60,
            name: '10分钟'
        }, {
            code: 15 * 60,
            name: '15分钟'
        }, {
            code: 30 * 60,
            name: '30分钟'
        }, {
            code: 60 * 60,
            name: '1小时'
        }],
        formData: {
            password: '',
            closeTime: 0
        },
        formRules: {
            password: [
                {required: true, message: '请输入密码'}
            ],
            closeTime: [
                {required: true, message: '请选择关闭时长'}
            ]
        }
    },
    created: function () {
        switch (open_type) {
            case "btnLogView":
            case "btnSlowLog":
                this.labelWidth = "77px"
                this.closeTimeShow = true
                break
        }
    },
    methods: {
        formSubmit: function (formName) {//usersCheckForm
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
            var _this = this
            var password = this.formData.password
            var closeTime = _this.formData.closeTime
            var jsonData = {
                "pwd": password
            }
            var parentData = {
                id: obj_id,
                relateId: obj_relateId
            }
            sendPut("/" + getProjectName() + "/users/check", function (response) {
                switch (open_type) {
                    case "btnTerminal":
                    case "btnService":
                        parent[0][obj_type + "ListApp"].webSSHOpen(parentData, open_type)
                        break
                    case "btnLogView":
                        parent[0][obj_type + "ListApp"].logViewOpen(parentData, closeTime)
                        break
                    case "btnSlowLog":
                        parent[0][obj_type + "ListApp"].slowLogOpen(parentData, closeTime)
                        break
                }
                layer.closeAll('loading')
                _this.formClose()
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, jsonData)
        },
        formClose: function () {
            parent.layer.close(usersCheckIndex);
        }
    }
})