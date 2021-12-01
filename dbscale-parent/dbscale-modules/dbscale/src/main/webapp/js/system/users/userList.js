var usernameData = getSession("UserName")
new Vue({
    el: '#edit',
    data: {
        menu: menu,
        roleList: [],
        formData: {
            username: '',
            name: '',
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
                _this.editCreated();
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/users/" + usernameData, function (response) {
                var data = response.data.data;
                _this.formData = {
                    username: usernameData,
                    name: jsonJudgeNotDefined(data, 'data.name'),
                    telephone: jsonJudgeNotDefined(data, 'data.telephone'),
                    email: jsonJudgeNotDefined(data, 'data.email'),
                    emerContact: jsonJudgeNotDefined(data, 'data.emerContact'),
                    emerTel: jsonJudgeNotDefined(data, 'data.emerTel'),
                    company: jsonJudgeNotDefined(data, 'data.company'),
                    role: jsonJudgeNotDefined(data, 'data.role.id'),
                    autoApproved: jsonJudgeNotDefined(data, 'data.ogAutoExamine'),
                    autoExecute: jsonJudgeNotDefined(data, 'data.ogAutoExecute')
                }
                if (XEUtils.isEmpty(_this.roleList)) {
                    _this.roleList.push(data.role)
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
            commonConfirm(_this, '编辑确认', getHintText('编辑')).then(() => {
                sendPut("/" + getProjectName() + "/users/" + usernameData, function (response) {
                    layer.closeAll('loading')
                    operationCompletion(_this, "操作成功！")
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            }).catch(() => {
            });
        }
    }
})