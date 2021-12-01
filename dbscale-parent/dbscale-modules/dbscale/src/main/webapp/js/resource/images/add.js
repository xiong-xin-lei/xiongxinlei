var addIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#add',
    data: {
        siteList: [],
        archList: [],
        typeList: [],
        formData: {
            siteId: '',
            type: '',
            arch: '',
            majorVersion: '',
            minorVersion: '',
            patchVersion: '',
            buildVersion: '',
            enabled: true,
            description: ''
        },
        formRules: {
            siteId: [{
                required: true,
                message: '请选择所属站点'
            }],
            name: [{
                required: true,
                message: '请输入业务区名称'
            }],
            enabled: [{
                required: true,
                message: '请选择状态'
            }],
            type: [{
                required: true,
                message: '请选择类型'
            }],
            arch: [{
                required: true,
                message: '请选择架构'
            }],
            majorVersion: [{
                    required: true,
                    message: '请输入主版本'
                },
                {
                    pattern: /^[+]{0,1}(\d+)$/,
                    message: '请输入正确的数字',
                    trigger: 'blur'
                }
            ],
            minorVersion: [{
                    required: true,
                    message: '请输入次版本'
                },
                {
                    pattern: /^[+]{0,1}(\d+)$/,
                    message: '请输入正确的数字',
                    trigger: 'blur'
                }
            ],
            patchVersion: [{
                    required: true,
                    message: '请输入修订版本'
                },
                {
                    pattern: /^[+]{0,1}(\d+)$/,
                    message: '请输入正确的数字',
                    trigger: 'blur'
                }
            ],
            buildVersion: [{
                    required: true,
                    message: '请输入编译版本'
                },
                {
                    pattern: /^[+]{0,1}(\d+)$/,
                    message: '请输入正确的数字',
                    trigger: 'blur'
                }
            ]
        }
    },
    created: function () {
        this.siteListView()
        this.archListView()
        this.typeListView()
    },
    methods: {
        siteListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sites", function (response) {
                _this.siteList = response.data.data
                _this.formData.siteId = getSession("siteId")
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, "error")
            }, null)
        },
        archListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=sys_architecture", function (response) {
                _this.archList = response.data.data
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, "error")
            }, null)
        },
        typeListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/def_servs", function (response) {
                _this.typeList = response.data.data
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, "error")
            }, null)
        },
        formSubmit: function (formName) { //addModal
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
            if (verify)
                return false;

            var jsonData = {
                "siteId": getSession("siteId"),
                "arch": this.formData.arch,
                "type": this.formData.type,
                "major": parseInt(this.formData.majorVersion),
                "minor": parseInt(this.formData.minorVersion),
                "patch": parseInt(this.formData.patchVersion),
                "build": parseInt(this.formData.buildVersion),
                "exporterPort": 9104,
                "enabled": this.formData.enabled,
                "description": this.formData.description
            }
            var _this = this
            commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
                sendPost("/" + getProjectName() + "/images", function (response) {
                    operationCompletion(parent.listApp, "操作成功！")
                    parent.listApp.returnList()
                    _this.formClose()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, "error")
                }, jsonData)
            }).catch(() => {});
        },
        formClose: function () {
            parent.layer.close(addIndex)
        }
    }
})