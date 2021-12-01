var executeScaleUpIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#executeScaleUp',
    data: {
        stepsActive: 0,
        initialization: 0,
        endNum: 5,
        siteList: [],
        businessAreaList: [],
        clusterList: [],
        remoteStorageList: [],
        ntpServerList: [],
        formData: {
            type: "",
            name: "",
            ownerName: "",
            businessSystemName: "",
            businessSubsystem: "",
            site: "",
            businessArea: "",
            orderTypeDisplay: "",
            version: "",
            arch: "",
            scale: "",
            diskType: "",
            dataSize: 10,
            logSize: 10,
            port: 3306,
            paramCfg: [],
            msg: ""
        }
    },
    created: function () {
        this.editCreated()
    },
    methods: {
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/order_groups/" + executeId, function (response) {
                var data = response.data.data;
                var ownerName = "";
                if (data.owner !== null) {
                    ownerName = ownerNameDispose(data.owner.name, data.owner.username)
                }

                XEUtils.arrayEach(data.orders, (v, i) => {
                    if (v.scale != null) {

                        var orderTypeDisplay = jsonJudgeNotDefined(v, "v.type.code")
                        switch (orderTypeDisplay) {
                            case "mysql":
                                orderTypeDisplay = "数据库"
                                break
                            case "proxysql":
                                orderTypeDisplay = "代理"
                                break
                            case "cmha":
                                orderTypeDisplay = "高可用"
                                break
                        }

                        _this.formData = {
                            type: "MySQL",
                            name: data.name + '    (' + ownerName + ')',
                            ownerName: ownerName,
                            businessSystemName: jsonJudgeNotDefined(data, "json.businessSubsystem.businessSystem.name"),
                            businessSubsystem: jsonJudgeNotDefined(data, "json.businessSubsystem.name"),
                            site: jsonJudgeNotDefined(data, "json.site.name"),
                            businessArea: jsonJudgeNotDefined(data, "json.businessArea.name"),
                            orderTypeDisplay: orderTypeDisplay,
                            arch: jsonJudgeNotDefined(v, "json.arch.name"),
                            scale: jsonJudgeNotDefined(v, "json.scale.name"),
                            diskType: jsonJudgeNotDefined(v, "json.diskType.display"),
                            dataSize: v.dataSize,
                            logSize: v.logSize,
                            port: v.port,
                            paramCfg: XEUtils.toStringJSON(v.paramCfg),
                            msg: ""
                        }
                    }
                })

                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        formSubmit: function (formName) {//executeScaleUp
            var _this = this
            commonConfirm(this, '执行确认', getHintText('执行')).then(() => {
                _this.sendTableAjax(null)
            }).catch(() => {
            })
        },
        sendTableAjax: function (data) {
            var _this = this
            sendPut("/" + getProjectName() + "/order_groups/" + executeId + "/execute", function (response) {
                setTimeout(function () {
                    layer.closeAll('loading')
                    parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                    parent.listApp.goTourlDialogShow = true
                    _this.formClose()
                }, 1000);
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, data)
        },
        formClose: function () {
            parent.layer.close(executeScaleUpIndex);
        },
        clusterIdChange: function (data) {
            this.initialization--;
            this.formData.clusterId = "";
            this.clusterListView();
        }
    }
})