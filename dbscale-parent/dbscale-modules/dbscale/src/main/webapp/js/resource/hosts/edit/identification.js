var editIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#editIdentification',
    data: {
        formData: {
            ip: '',
            port: '22',
            username: '',
            password: ''
        },
        formRules: {
            ip: [
                {required: true, message: '请选择IP'}
            ],
            port: [
                {required: true, message: '请输入端口'}
            ],
            username: [
                {required: true, message: '请输入用户名'}
            ],
            password: [
                {required: true, message: '请输入密码'}
            ]
        }
    },
    created: function () {
        var parentData = parent.editLittle.createdData
        this.formData.ip = parentData.ip
    },
    methods: {
        formSubmit: function (formName) {
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
            var username = this.formData.username
            var password = this.formData.password
            var port = this.formData.port
            parent.editLittle.identificationAjaxFun(username, password, port)
            this.formClose()
        },
        formClose: function () {
            parent.layer.close(editIndex);
        }
    }
})