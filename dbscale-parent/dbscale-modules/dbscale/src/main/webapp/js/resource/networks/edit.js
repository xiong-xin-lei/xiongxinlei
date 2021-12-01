var editIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#edit',
    data: {
        initialization: 0,
        endNum: 3,
        siteList: [],
        topologyList: [],
        businessAreaList: [],
        formData: {
            siteId: '',
            name: '',
            startIp: '',
            endIp: '',
            topologys: [],
            businessAreaId: '',
            gateway: '',
            netmask: '',
            enabled: true,
            vlan: '',
            description: ''
        },
        formRules: {
            name: [{
                required: true,
                message: '请输入网段名称'
            }],
            topologys: [{
                required: true,
                message: '请选择拓扑结构'
            }]
        }
    },
    created: function () {
        this.siteListView()
        this.businessAreaListView()
        this.topologyListView()
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
                operationCompletion(_this, error.response.data.msg, "error")
            }, null)
        },
        topologyListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=network_topology", function (response) {
                _this.topologyList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        topologyProcessing: function (data) {
            var topologyNew = [];
            XEUtils.arrayEach(data, (v, i) => {
                topologyNew.push(v.code)
            })
            return topologyNew
        },
        endFun: function () {
            this.initialization++
            if (this.initialization == this.endNum)
                this.editCreated()
        },
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/networks/" + networksId, function (response) {
                var data = response.data.data
                var businessAreaListShow = true
                XEUtils.arrayEach(_this.businessAreaList, (v, i) => {
                    if (jsonJudgeNotDefined(data, "data.businessArea.id") === v.id) {
                        businessAreaListShow = false
                    }
                })
                if (businessAreaListShow) {
                    var list = {
                        id: jsonJudgeNotDefined(data, "data.businessArea.id"),
                        name: jsonJudgeNotDefined(data, "data.businessArea.name")
                    }
                    _this.businessAreaList.push(list)
                }
                _this.formData = {
                        siteId: data.site.id,
                        startIp: data.startIp,
                        endIp: data.endIp,
                        businessAreaId: jsonJudgeNotDefined(data, "data.businessArea.id"),
                        topologys: _this.topologyProcessing(data.topologys),
                        gateway: data.gateway,
                        netmask: data.netmask,
                        name: data.name,
                        vlan: data.vlan,
                        description: data.description
                    },
                    layer.closeAll('loading')
            }, function (error) {
            	console.log(error)
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        formSubmit: function (formName) { //editModal
            var verify = false;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    return true;
                } else {
                    verify = true;
                    return false;
                }
            });
            if (verify) {
                return false;
            }

            var jsonData = {
                "name": this.formData.name,
                "topologys": this.formData.topologys,
                "businessAreaId": this.formData.businessAreaId,
                "description": this.formData.description
            }
            var _this = this
            commonConfirm(_this, '编辑确认', getHintText('编辑')).then(() => {
                sendPut("/" + getProjectName() + "/networks/" + networksId, function (response) {
                    layer.closeAll('loading')
                    operationCompletion(parent.listApp, "操作成功！")
                    parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                    parent.listApp.returnList()
                    _this.formClose()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            })
        },
        formClose: function () {
            if (parent.listApp.updateStatus)
                parent.listApp.returnList()
            parent.layer.close(editIndex)
        }
    }
})