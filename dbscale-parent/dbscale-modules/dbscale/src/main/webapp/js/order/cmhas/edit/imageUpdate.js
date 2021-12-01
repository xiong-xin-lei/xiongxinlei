var editImageUpdateIndex = parent.layer.getFrameIndex(window.name);
var ogAutoExamine = getQueryVariable("ogAutoExamine") === "true"
var ogAutoExecute = getQueryVariable("ogAutoExecute") === "true"

new Vue({
    el: '#editImageUpdate',
    data: {
        ogAutoExamine: ogAutoExamine,
        ogAutoExecute: ogAutoExecute,
        stepsActive: 0,
        imageVersionList: [],
        formData: {
            type: "",
            name: "",
            ownerName: "",
            businessSystemName: "",
            businessSubsystem: "",
            sysArchitectureName: "",
            sysArchitectureCode: "",
            site: "",
            businessArea: "",
            orderType: "",
            version: "",
            versionId: "",
        },
        formRules: {
            version: [
                {required: true, message: '请选择版本', trigger: ['change', 'blur']}
            ]
        }
    },
    created: function () {
        this.editCreated();
    },
    methods: {
        imageVersionListView: function (val) {
            var _this = this
            sendGet("/" + getProjectName() + "/images?site_id=" + getSession("siteId") + "&type=" + _this.formData.orderType + "&enabled=true" + "&architecture=" + _this.formData.sysArchitectureCode, function (response) {
                var json = []
                XEUtils.arrayEach(response.data.data, (v, i) => {
                    if (v.type.code === _this.formData.orderType) {
                        json.push({
                            id: v.id,
                            version: v.version.major + '.' + v.version.minor + '.' + v.version.patch + '.' + v.version.build
                        })
                    }
                })
                _this.imageVersionList = json
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        stepsActiveAdd: function () {
            this.stepsActive++
            if (this.stepsActive === 1) {
                this.imageVersionListView()
            }
        },
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/order_groups/" + editId, function (response) {
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

                var sysArchitectureName = "";
                var sysArchitectureCode = "";
                if (data.sysArchitecture !== null) {
                    sysArchitectureName = data.sysArchitecture.display
                    sysArchitectureCode = data.sysArchitecture.code
                }

                XEUtils.arrayEach(data.orders, (v, i) => {
                    if (v.version !== null) {
                        if (v.version.major !== null) {

                            var version = "";
                            var versionId = "";
                            if (v.version !== null) {
                                version = v.version.major + '.' + v.version.minor + '.' + v.version.patch + '.' + v.version.build
                            }

                            show = true;
                            XEUtils.arrayEach(_this.imageVersionList, (v, i) => {
                                if (version === v.version) {
                                    versionId = v.id
                                    show = false;
                                }
                            })
                            if (show) {
                                _this.imageVersionList.push(version)
                            }

                            var diskType = "";
                            if (v.diskType !== null) {
                                diskType = v.diskType.code
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
                                type: "CMHA",
                                name: data.name + '    (' + ownerName + ')',
                                ownerName: ownerName,
                                businessSystemName: businessSystemName,
                                businessSubsystem: businessSubsystem,
                                sysArchitectureName: sysArchitectureName,
                                sysArchitectureCode: sysArchitectureCode,
                                site: site,
                                businessArea: businessArea,
                                orderType: v.type.code,
                                orderTypeDisplay: orderTypeDisplay,
                                version: version,
                                versionId: versionId,
                            }
                        }
                    }
                })

                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        formSubmit: function (formName, type) {//editImageUpdate
            var _this = this
            const majorVersion = this.formData.version.split(".")[0]
            const minorVersion = this.formData.version.split(".")[1]
            const patchVersion = this.formData.version.split(".")[2]
            const buildVersion = this.formData.version.split(".")[3]
            const jsonData = {
                "orders": [
                    {
                        "type": "mysql",
                        "version": {
                            "major": parseInt(majorVersion),
                            "minor": parseInt(minorVersion),
                            "patch": parseInt(patchVersion),
                            "build": parseInt(buildVersion)
                        }
                    }
                ]
            }
            commonConfirm(this, '编辑确认', getHintText('编辑')).then(() => {
                _this.sendTableAjax(jsonData)
            }).catch(() => {
            })
        },
        sendTableAjax: function (data) {
            var _this = this
            sendPut("/" + getProjectName() + "/order_groups/" + editId, function (response) {
                layer.closeAll('loading')
                operationCompletion(parent.listApp, "操作成功！")
                parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                if (this.ogAutoExamine && this.ogAutoExecute) {
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
            parent.layer.close(editImageUpdateIndex);
        },
        versionChange: function (e) {
            var id = this.$refs.versionRadios.hoverOption.$el.dataset.versionid
            this.formData.versionId = id;
        },
        showKey: function (val) {
            var _this = this;
            var temporaryData = [];
            XEUtils.arrayEach(val, (v, i) => {
                var showData = true;
                XEUtils.arrayEach(_this.paramData, (value, index) => {
                    if (value.key === v.key) {
                        showData = false
                    }
                })
                if (showData) {
                    temporaryData.push(v)
                }
            })
            return temporaryData
        }
    }
})