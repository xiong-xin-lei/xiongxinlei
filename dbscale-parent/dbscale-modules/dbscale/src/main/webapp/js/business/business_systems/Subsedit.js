var editIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#Subsedit',
    data: {
        formData: {
            id: '',
            name: '',
            description: '',
            businessSystemName: ''
        },
        formRules: {
            name: [
                {required: true, message: '请输入业务子系统'}
            ],
            businessSystemName: [
                {required: true, message: '请输入业务系统'}
            ]
        }
    },
    created: function () {
        this.editCreated()
    },
    methods: {
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/business_subsystems/" + SubsId, function (response) {
                var data = response.data.data;
                _this.formData = {
                    id: data.businessSystem.id,
                    name: data.name,
                    description: data.description,
                    businessSystemName: data.businessSystem.name
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
                "description": this.formData.description
            }
            var _this = this
            commonConfirm(_this, '编辑确认', getHintText('编辑')).then(() => {
                sendPut("/" + getProjectName() + "/business_subsystems/" + SubsId, function (response) {
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
            if (parent.listApp.updateStatus) 
            	parent.listApp.returnList()
            parent.layer.close(editIndex);
        }
    }
})