var approvedScaleUpIndex = parent.layer.getFrameIndex(window.name);
var ogAutoExecute = getQueryVariable("ogAutoExecute") === "true"
new Vue({
    el: '#approvedScaleUp',
    data: {
        ogAutoExecute: ogAutoExecute,
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
            sendGet("/" + getProjectName() + "/order_groups/" + approvedId, function (response) {
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
        formSubmit: function (formName, type) {//approvedScaleUp
            var msg = this.formData.msg
            var status = ''
            if (type) {
                status = "approved"
            } else {
                status = "unapprove"
            }
            var jsonData = {
                msg: msg,
                state: status
            }
            var _this = this
            if (type) {
                commonConfirm(this, '审批确认', getHintText('', '', '是否确认同意审批')).then(() => {
                    _this.sendTableAjax(jsonData)
                }).catch(() => {
                })
            } else {
                var affirm = false
                if (msg.length === 0) {
                    this.$prompt('请输入拒绝理由', '提示', {
                        cancelButtonClass: "btn-custom-cancel",
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        closeOnClickModal: false,
                        inputType: 'textarea',
                        inputPattern: /\S/,
                        inputErrorMessage: '拒绝理由不能为空',
                        customClass: 'messageBoxMsgTest'
                    }).then(({value}) => {
                        jsonData.msg = value
                        _this.sendTableAjax(jsonData)
                    }).catch(() => {
                    });
                } else {
                    commonConfirm(this, '审批确认', getHintText('', '', '是否确认拒绝审批')).then(() => {
                        _this.sendTableAjax(jsonData)
                    }).catch(() => {
                    })
                }
            }
        },
        sendTableAjax: function (data) {
            var _this = this
            sendPut("/" + getProjectName() + "/order_groups/" + approvedId + "/examine", function (response) {
                layer.closeAll('loading')
                operationCompletion(parent.listApp, "操作成功！")
                parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                if (_this.ogAutoExecute && data.state === "approved") {
                    parent.listApp.goTourlDialogShow = true
                } else {
                    parent.listApp.returnList()
                }
                _this.formClose()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, data)
        },
        formClose: function () {
            parent.layer.close(approvedScaleUpIndex);
        }
    }
})