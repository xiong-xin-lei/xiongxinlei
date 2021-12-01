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
            orderType: "",
            orderTypeDisplay: "",
            diskType: "",
            dataSize: 10,
            logSize: 10,
            port: 3306,
            paramCfg: [],
            msg: ""
        },
        dataShow: function () {
            if (this.formData.orderType === "mysql") {
                return "表空间"
            } else {
                return "数据目录"
            }
        },
        logShow: function () {
            if (this.formData.orderType === "mysql") {
                return "日志空间"
            } else {
                return "日志目录"
            }
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

                var businessSystemName = "";
                var businessSubsystem = "";
                if (data.businessSubsystem !== null) {
                    businessSubsystem = data.businessSubsystem.name
                    businessSystemName = data.businessSubsystem.businessSystem.name
                }

                var site = "";
                if (data.site !== null) {
                    site = data.site.name
                }

                var businessArea = "";
                if (data.businessArea !== null) {
                    businessArea = data.businessArea.name
                }

                XEUtils.arrayEach(data.orders, (v, i) => {
                    if (v.dataSize != null) {

                        var diskType = "";
                        if (v.diskType !== null) {
                            diskType = v.diskType.display
                        }

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
                            businessSystemName: businessSystemName,
                            businessSubsystem: businessSubsystem,
                            site: site,
                            businessArea: businessArea,
                            orderType: v.type.code,
                            orderTypeDisplay: orderTypeDisplay,
                            diskType: diskType,
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