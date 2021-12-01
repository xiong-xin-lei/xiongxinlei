var editIndex = parent.layer.getFrameIndex(window.name);
var editLittle = new Vue({
    el: '#editLittle',
    data: {
    	initialization: 0,
        endNum: 1,
        hostEdit: true,
        siteList: [],
        createdData: {},
        maxUnitCntMax: 100,
        roleList: [],
        formData: {
            ip: '',
            maxUnitCnt: '',
            maxUsage: '',
            role:'',
            description: ''
        },
        formRules: {
            role: [
                {required: true, message: '请选择角色'}
            ],
            maxUnitCnt: [
                {required: true, message: '请输入单元上限'}
            ],
            maxUsage: [
                {required: true, message: '请输入资源分配率上限'}
            ]
        }
    },
    created: function () {
    	this.editCreated()
    },
    methods: {
        roleListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=host_role", function (response) {
                _this.roleList = response.data.data
                _this.endFun()
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        endFun: function () {
        	 this.initialization ++
        	 if (this.initialization == this.endNum)
        		 layer.closeAll('loading')
        },
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/hosts/" + hostId, function (response) {
                var data = response.data.data;
                _this.formData = {
                    ip: data.ip,
                    role:jsonJudgeNotDefined(data, 'data.role.code'),
                    maxUnitCnt: XEUtils.toNumber(jsonJudgeNotDefined(data, 'data.unit.maxCnt')),
                    maxUsage: XEUtils.toNumber(data.maxUsage),
                    description: data.description
                }
                _this.createdData = data
                _this.roleListView()
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
                ip: this.formData.ip,
                role: this.formData.role,
                maxUnitCnt: XEUtils.toNumber(this.formData.maxUnitCnt),
                maxUsage: XEUtils.toNumber(this.formData.maxUsage),
                description: this.formData.description,
            }
            var _this = this
            commonConfirm(_this, '编辑确认', getHintText('编辑')).then(() => {
                sendPut("/" + getProjectName() + "/hosts/" + hostId, function (response) {
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
        	if(parent.listApp.updateStatus)
        		parent.listApp.returnList()
            parent.layer.close(editIndex)
        }
    }
})