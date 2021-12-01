var resetPwdIndex = parent.layer.getFrameIndex(window.name);
var username = getQueryVariable("username");
var rowData = parent.listApp.rowData;
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
        case userresetPwdApp.formData.newPwd:
            callback();
            break;
        default:
            callback(new Error('两次输入新密码不一致!'));
            break;
    }
};
var userresetPwdApp = new Vue({
    el: '#resetPwd',
    data: {
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
                "pwd": this.formData.newPwd
            }
            var _this = this
            const riskText = "对于当前所有节点的密码进行重置，重置成功后会应用新来的连接需要使用新密码访问，否则会连接失败，请谨慎操作！"
            let setting = {
                customClass: "customizeWidth510pxClass"
            }
            parent.layer.min(resetPwdIndex);
            commonConfirm(parent.listApp, '重置密码', getRiskLevelHtml(4, riskText) + "重置密码将会重启容器，是否确认重置密码？", setting).then(() => {
                parent.layer.restore(resetPwdIndex);
                sendPut("/" + getProjectName() + "/serv_groups/redis/" + rowData.id + "/pwd", function (response) {
                    layer.closeAll('loading')
                    operationCompletion(parent.listApp, "操作成功！")
                    parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                    parent.listApp.returnList()
                    _this.formClose()
                }, function (error) {
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