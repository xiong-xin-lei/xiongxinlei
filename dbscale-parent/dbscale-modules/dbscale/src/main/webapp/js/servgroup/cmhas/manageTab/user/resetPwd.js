var resetPwdIndex = parent.layer.getFrameIndex(window.name);
var highAvailable = parent.manageApp.highAvailable
var username = getQueryVariable("username");
var ip = getQueryVariable("ip");
var checkPwd = (rule, value, callback) => {
    var checkChar = new RegExp('^(?=.*[0-9].*)(?=.*[A-Za-z].*).{8,}$');
    var checkChar2 = new RegExp('[^&$:()\'\"\\s]{1,}');
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
        case resetPwdApp.formData.newPwd:
            callback();
            break;
        default:
            callback(new Error('两次输入新密码不一致!'));
            break;
    }
};
var resetPwdApp = new Vue({
    el: '#resetPwd',
    data: {
        highAvailable: highAvailable,
        formData: {
            newPwd: '',
            verifyNewPwd: ''
        },
        formRules: {
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
        formSubmit: function (formName) {//resetPwdModal
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
                "newPwd": this.formData.newPwd
            }
            var _this = this
            let _parent = parent[0].userListApp
            parent.layer.min(resetPwdIndex);
            commonConfirm(parent.manageApp, '重置密码', "是否确认重置密码？").then(() => {
                parent.layer.restore(resetPwdIndex);
                let url = "/" + getProjectName() + "/serv_groups/cmha/" + parent[0].rowId + "/users/" + username + "/pwd/reset"
                if (!_this.highAvailable)
                    url += "?ip=" + encodeURIComponent(ip, "utf-8")
                sendPut(url, function (response) {
                    layer.closeAll('loading')
                    operationCompletion(_parent, "操作成功！")
                    _parent.refreshSaveData.push(_parent.selectedData)
                    _parent.returnList()
                    _this.formClose()
                }, function (error) {
                    console.log(error)
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            }).catch(() => {
                parent.layer.restore(resetPwdIndex);
            });
        },
        formClose: function () {
            parent.layer.close(resetPwdIndex);
        }
    }
})