var addIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#add',
    data: {
        initialization: 0,
        endNum: 3,
        siteList: [],
        businessAreaList: [],
        defServsList: [],
        formData: {
            siteId: '',
            businessAreaId: '',
            name: '',
            defServs: [],
            haTag: '',
            enabled: true,
            description: ''
        },
        formRules: {
            siteId: [
                {required: true, message: '请选择所属站点'}
            ],
            businessAreaId: [
                {required: true, message: '请选择所属业务区'}
            ],
            name: [
                {required: true, message: '请输入集群名称'}
            ],
            defServs: [
                {required: true, message: '请选择包含软件'}
            ],
            haTag: [
                {required: true, message: '请输入高可用标签'}
            ],
            enabled: [
                {required: true, message: '请选择状态'}
            ]
        }
    },
    created: function () {
        this.siteListView()
        this.businessAreaListView()
        this.defServsListView()
    },
    methods: {
        siteListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sites", function (response) {
                _this.siteList = response.data.data
                _this.formData.siteId = getSession("siteId")
                _this.endFun()
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        businessAreaListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/business_areas?site_id=" + getSession("siteId") + "&enabled=true", function (response) {
                _this.businessAreaList = response.data.data
                _this.endFun()
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        defServsListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/def_servs?enabled=true", function (response) {
                _this.defServsList = response.data.data
               _this.endFun()
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        endFun: function (){
        	 this.initialization++
             if (this.initialization == this.endNum) 
                 layer.closeAll('loading')
        },
        sendAjaxFun: function () {
            var jsonData = {
                "businessAreaId": this.formData.businessAreaId,
                "name": this.formData.name,
                "imageTypes": this.formData.defServs,
                "haTag": this.formData.haTag,
                "enabled": this.formData.enabled,
                "description": this.formData.description
            }
            var _this = this
            commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
                sendPost("/" + getProjectName() + "/clusters", function (response) {
                    operationCompletion(parent.listApp, "操作成功！")
                    parent.listApp.returnList()
                    _this.formClose()
                }, function (error) {
                	operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            }).catch(() => {
            });
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
                return false
            }

            var verifyArray = []
            XEUtils.arrayEach(this.formData.defServs, (value) => {
                verifyArray.push(XEUtils.filter(this.defServsList, item => item.code === value)[0].stateful)
            })
            verifyArray = XEUtils.uniq(verifyArray)
            if (verifyArray.length !== 1) {
                commonConfirm(this, '操作确认', getHintText('', '', '警告：在生产环境，不建议在一个集群中同时包含无状态服务和有状态服务。')).then(() => {
                    this.sendAjaxFun()
                }).catch(() => {
                    verify = true
                })
            } else {
                this.sendAjaxFun()
            }
            if (verify) {
                return false
            }
        },
        formClose: function () {
            parent.layer.close(addIndex)
        }
    }
})