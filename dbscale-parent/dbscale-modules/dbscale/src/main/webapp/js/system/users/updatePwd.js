var updatePwdIndex = parent.layer.getFrameIndex(window.name);
var username = getQueryVariable("username")
var checkPwd = (rule, value, callback) => {
    var checkChar = new RegExp('^(?!.*\\\s)(?=.*[0-9].*)(?=.*[A-Za-z].*)(?=.*[~!@#%\\^*()_\\+`\\-=\\[\\]\\\{}|;\':"\\,\\./<>\\?]).{8,}$');
    var checkChar2 = new RegExp('^[0-9a-zA-Z].{6,}[0-9a-zA-Z]$');
    if (!checkChar.test(value) || !checkChar2.test(value)) {
        callback(new Error('新密码格式错误'));//密码必须由数字、字母和特殊字符组成，长度必须是8位及以上,不能以特殊字符开头和结尾，不能有空格、$、&
        return false;
    }
    if (value.indexOf('&') >= 0 || value.indexOf('$') >= 0) {
        callback(new Error('新密码格式错误'));
        return false;
    }
    callback();
};
var validatePass = (rule, value, callback) => {
    switch (value) {
        case '':
            callback(new Error('请再次输入新密码'));
            break;
        case userUpdatePwdApp.formData.newPwd:
            callback();
            break;
        default:
            callback(new Error('两次输入新密码不一致!'));
            break;
    }
};
var userUpdatePwdApp = new Vue({
    el: '#updatePwd',
    data: {
        formData: {
            originalPwd: '',
            newPwd: '',
            verifyNewPwd: ''
        },
        formRules: {
            originalPwd: [
                {required: true, message: '请输入原密码'}
            ],
            newPwd: [
                {required: true, message: '请输入新密码'},
                {validator: checkPwd, trigger: 'blur'}
            ],
            verifyNewPwd: [
                {required: true, message: '请再次输入新密码'},
                {validator: validatePass, trigger: 'blur'}
            ]
        }
    },
    created: function () {
    },
    methods: {
        formSubmit: function (formName) {//updatePwdModal
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
                "originalPwd": this.formData.originalPwd,
                "newPwd": this.formData.newPwd
            }
            var _this = this
            commonConfirm(_this, '修改确认', getHintText('修改密码')).then(() => {
                sendPut("/" + getProjectName() + "/users/" + username + "/pwd", function (response) {
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
            parent.layer.close(updatePwdIndex);
        }
    }
})