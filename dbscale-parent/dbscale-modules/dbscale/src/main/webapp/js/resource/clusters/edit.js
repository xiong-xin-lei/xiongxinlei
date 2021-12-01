var editIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#edit',
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
            description: ''
        },
        formRules: {
            name: [
                {required: true, message: '请输入集群名称'}
            ],
            defServs: [
                {required: true, message: '请选择包含软件'}
            ],
            haTag: [
                {required: true, message: '请输入高可用标签'}
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
                _this.endFun()
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
        },
        businessAreaListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/business_areas?site_id=" + getSession("siteId") + "&enabled=true", function (response) {
                _this.businessAreaList = response.data.data
                _this.endFun()
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
        },
        defServsListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/def_servs?enabled=true", function (response) {
                _this.defServsList = response.data.data
                _this.endFun()
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
        },
        imageTypesProcessing: function (data) {
            var imageTypesNew = []
            XEUtils.arrayEach(data, (v, i) => {
                imageTypesNew.push(v.code)
            })
            return imageTypesNew
        },
        endFun: function () {
        	this.initialization++
            if (this.initialization == this.endNum) 
                this.editCreated()
        },
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/clusters/" + clusterId, function (response) {
                var data = response.data.data
                var businessAreaListShow = true
                XEUtils.arrayEach(_this.businessAreaList, (v, i) => {
                    if (data.businessArea.id === v.id) {
                        businessAreaListShow = false
                    }
                })
                if (businessAreaListShow) {
                    var list = {
                        id: data.businessArea.id,
                        name: data.businessArea.name
                    }
                    _this.businessAreaList.push(list)
                }
                _this.formData = {
                    siteId: data.site.id,
                    businessAreaId: data.businessArea.id,
                    name: data.name,
                    defServs: _this.imageTypesProcessing(data.imageTypes),
                    haTag: data.haTag,
                    description: data.description
                }
                layer.closeAll('loading')
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
        },
        sendAjaxFun: function () {

        	var jsonData = {
                "businessAreaId": this.formData.businessAreaId,
                "name": this.formData.name,
                "imageTypes": this.formData.defServs,
                "haTag": this.formData.haTag,
                "description": this.formData.description
            }
            var _this = this
            commonConfirm(_this, '编辑确认', getHintText('编辑')).then(() => {
                sendPut("/" + getProjectName() + "/clusters/" + clusterId, function (response) {
                    layer.closeAll('loading')
                    operationCompletion(parent.listApp, "操作成功！")
                    parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                    parent.listApp.returnList()
                    _this.formClose()
                }, function (error) {
                	operationCompletion( _this, error.response.data.msg, "error")
                }, jsonData)
            }).catch(() => {
            });
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
        	if(parent.listApp.updateStatus)
        		parent.listApp.returnList()
            parent.layer.close(editIndex)
        }
    }
})