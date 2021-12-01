var approvedDeleteIndex = parent.layer.getFrameIndex(window.name);
var ogAutoExecute = getQueryVariable("ogAutoExecute") === "true"
new Vue({
    el: '#approvedDelete',
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
                    if (v.type.code === "mysql") {

                        var version = "";
                        if (v.version !== null) {
                            version = v.version.major + '.' + v.version.minor + '.' + v.version.patch + '.' + v.version.build
                        }

                        var arch = "";
                        if (v.arch !== null) {
                            arch = v.arch.name
                        }

                        var scale = "";
                        if (v.scale !== null) {
                            scale = v.scale.name
                        }

                        var diskType = "";
                        if (v.diskType !== null) {
                            diskType = v.diskType.display
                        }

                        _this.formData = {
                            type: "CMHA",
                            name: data.name + '    (' + ownerName + ')',
                            ownerName: ownerName,
                            businessSystemName: businessSystemName,
                            businessSubsystem: businessSubsystem,
                            site: site,
                            businessArea: businessArea,
                            version: version,
                            arch: arch,
                            scale: scale,
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
        formSubmit: function (formName, type) {//approvedDelete
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
            	commonConfirm(this, '审批确认', getHintText('','', '是否确认同意审批')).then(() => {
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
                	commonConfirm(this, '审批确认', getHintText('','', '是否确认拒绝审批')).then(() => {
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
            parent.layer.close(approvedDeleteIndex);
        }
    }
})