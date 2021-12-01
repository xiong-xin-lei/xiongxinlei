var addIndex = parent.layer.getFrameIndex(window.name);
var checkPwd = (rule, value, callback) => {
    var checkChar = new RegExp('^(?!.*\\\s)(?=.*[0-9].*)(?=.*[A-Za-z].*)(?=.*[~!@#%\\^*()_\\+`\\-=\\[\\]\\\{}|;\':"\\,\\./<>\\?]).{8,}$');
    var checkChar2 = new RegExp('^[0-9a-zA-Z].{6,}[0-9a-zA-Z]$');
    if (!checkChar.test(value) || !checkChar2.test(value)) {
        callback(new Error('密码格式错误'));//密码必须由数字、字母和特殊字符组成，长度必须是8位及以上,不能以特殊字符开头和结尾，不能有空格、$、&
        return false;
    }
    if (value.indexOf('&') >= 0 || value.indexOf('$') >= 0) {
        callback(new Error('密码格式错误'));
        return false;
    }
    callback();
};
var validatePass = (rule, value, callback) => {
    switch (value) {
        case '':
            callback(new Error('请再次输入密码'));
            break;
        case userAddApp.formData.password:
            callback();
            break;
        default:
            callback(new Error('两次输入密码不一致!'));
            break;
    }
};
var userAddApp = new Vue({
    el: '#add',
    data: {
        roleList: [],
        formData: {
            username: '',
            name: '',
            password: '',
            verifyPassword: '',
            telephone: '',
            email: '',
            emerContact: '',
            emerTel: '',
            company: '',
            role: '',
            autoApproved: false,
            autoExecute: false
        },
        formRules: {
            username: [
                {required: true, message: '请输入用户名'}
            ],
            name: [
                {required: true, message: '请输入姓名'}
            ],
            password: [
                {required: true, message: '请输入密码'},
                {validator: checkPwd, trigger: 'blur'}
            ],
            verifyPassword: [
                {required: true, message: '请再次输入密码'},
                {validator: validatePass, trigger: 'blur'}
            ],
            telephone: [
                {required: true, message: '请输入手机号码'}
            ],
            email: [
                {required: true, message: '请输入邮箱'}
            ],
            emerContact: [
                {required: true, message: '请输入紧急联系人'}
            ],
            emerTel: [
                {required: true, message: '请输入紧急联系人电话'}
            ],
            company: [
                {required: true, message: '请输入所属单位'}
            ],
            role: [
                {required: true, message: '请选择角色'}
            ],
            autoApproved: [
                {required: true, message: '请选择自动审批'}
            ],
            autoExecute: [
                {required: true, message: '请选择自动执行'}
            ]
        }
    },
    created: function () {
        this.roleListView()
    },
    methods: {
        roleListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/roles", function (response) {
                _this.roleList = response.data.data
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
                "username": this.formData.username,
                "name": this.formData.name,
                "password": this.formData.password,
                "telephone": this.formData.telephone,
                "email": this.formData.email,
                "emerContact": this.formData.emerContact,
                "emerTel": this.formData.emerTel,
                "company": this.formData.company,
                "roleId": this.formData.role,
                "ogAutoExamine": this.formData.autoApproved,
                "ogAutoExecute": this.formData.autoExecute
            }
            var _this = this
            // console.log("新增",jsonData)
            commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
                sendPost("/" + getProjectName() + "/users", function (response) {
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