var editImageUpdateIndex = parent.layer.getFrameIndex(window.name);
var ogAutoExamine = getQueryVariable("ogAutoExamine") === "true"
var ogAutoExecute = getQueryVariable("ogAutoExecute") === "true"

new Vue({
    el: '#editImageUpdate',
    data: {
        ogAutoExamine: ogAutoExamine,
        ogAutoExecute: ogAutoExecute,
        stepsActive: 0,
        initialization: 0,
        endNum: 1,
        siteList: [],
        businessSystemList: [],
        businessSubsystemList: [],
        businessAreaList: [],
        imageVersionList: [],
        scaleList: [],
        diskTypeList: [],
        paramList: [],
        paramData: [],
        paramTableData: [],
        formData: {
            type: "",
            name: "",
            ownerName: "",
            businessSystemName: "",
            businessSubsystem: "",
            site: "",
            businessArea: "",
            architecture: "",
            architectureCode: "",
            version: "",
            versionId: "",
            cpuCnt: 0.0,
            memSize: 0.0,
            scale: "",
            diskType: "",
            dataSize: 10,
            logSize: 10,
            port: 3306,
            paramCfg: [],
            cnt: 0,
            msg: ""
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
        businessSystemListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/business_systems?enabled=true", function (response) {
                _this.businessSystemList = response.data.data
                _this.businessSubsystemListView(true);
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        businessSubsystemListView: function (createdVal) {
            var _this = this
            sendGet("/" + getProjectName() + "/business_subsystems?business_system_id=" + _this.formData.businessSystemName + "&enabled=true", function (response) {
                _this.businessSubsystemList = response.data.data
                if (createdVal) {
                    _this.endFun();
                } else {
                    layer.closeAll('loading')
                }
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        businessAreaListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/business_areas?site_id=" + getSession("siteId") + "&enabled=true", function (response) {
                _this.businessAreaList = response.data.data
                _this.endFun();
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        siteListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sites", function (response) {
                _this.siteList = response.data.data
                _this.formData.siteId = getSession("siteId");
                _this.endFun();
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        imageVersionListView: function (val) {
            var _this = this
            var versionArray = _this.formData.version.split('.')
            var tempMajor = XEUtils.toNumber(versionArray[0])
            var tempMinor = XEUtils.toNumber(versionArray[1])
            sendGet("/" + getProjectName() + "/images?site_id=" + getSession("siteId") + "&architecture=" + _this.formData.architectureCode + "&type=redis&enabled=true" + "&major=" + tempMajor + "&minor=" + tempMinor, function (response) {
                var json = []
                XEUtils.arrayEach(response.data.data, (v, i) => {
                    if (v.type.code === "redis") {
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
        scaleListView: function (val) {
            var _this = this
            sendGet("/" + getProjectName() + "/scales?type=redis&enabled=true", function (response) {
                //_this.scaleList = response.data.data
                var json = []
                XEUtils.arrayEach(response.data.data, (v, i) => {
                    if (v.type.code === "redis") {
                        json.push(v)
                    }
                })
                _this.scaleList = json
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        diskTypeListView: function (val) {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=disk_type", function (response) {
                _this.diskTypeList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        endFun: function () {
            var _this = this
            _this.initialization++
            if (_this.initialization == _this.endNum) {
                _this.editCreated()
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

                var architecture = "";
                var architectureCode = "";
                if (data.sysArchitecture !== null) {
                    architecture = data.sysArchitecture.display
                    architectureCode = data.sysArchitecture.code
                }

                XEUtils.arrayEach(data.orders, (v, i) => {
                    if (v.type.code === "redis") {

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

                        var scale = "";
                        var cpuCnt = 0;
                        var memSize = 0;
                        if (v.scale !== null) {
                            scale = v.scale.name
                            cpuCnt = v.scale.cpuCnt
                            memSize = v.scale.memSize
                        }

                        var diskType = "";
                        if (v.diskType !== null) {
                            diskType = v.diskType.code
                        }

                        _this.formData = {
                            type: "Redis",
                            name: data.name + '    (' + ownerName + ')',
                            ownerName: ownerName,
                            businessSystemName: businessSystemName,
                            businessSubsystem: businessSubsystem,
                            site: site,
                            businessArea: businessArea,
                            architecture: architecture,
                            architectureCode: architectureCode,
                            version: version,
                            versionId: versionId,
                            cpuCnt: cpuCnt,
                            memSize: memSize,
                            scale: scale,
                            diskType: diskType,
                            dataSize: v.dataSize,
                            logSize: v.logSize,
                            port: v.port,
                            paramCfg: XEUtils.toStringJSON(v.paramCfg),
                            cnt: v.cnt,
                            msg: ""
                        }
                        _this.paramData = XEUtils.toStringJSON(v.paramCfg)
                        _this.paramTableData.push({
                            version: version,
                            paramCfg: XEUtils.toStringJSON(v.paramCfg)
                        })
                    }
                })
                _this.imageVersionListView()
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
                        "type": "redis",
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
                if (_this.ogAutoExamine && _this.ogAutoExecute) {
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
        systemChange: function (val) {
            this.formData.businessSubsystem = ""
            this.businessSubsystemListView(false)
        },
        versionChange: function (e) {
            var id = this.$refs.versionRadios.hoverOption.$el.dataset.versionid
            this.formData.versionId = id;
        },
        scaleChange: function (val) {
            var cpucnt = this.$refs.scaleRadios.hoverOption.$el.dataset.cpucnt
            var memsize = this.$refs.scaleRadios.hoverOption.$el.dataset.memsize
            this.formData.cpuCnt = parseFloat(cpucnt);
            this.formData.memSize = parseFloat(memsize);
        },
        paramChange: function (row) {
            //console.log(row)
            var _this = this
            var paramData = this.paramList
            var paramLength = paramData.length
            for (var i = 0; i < paramLength; i++) {
                if (paramData[i].key === row.key) {
                    _this.paramData[row.num].value = paramData[i].value
                    break;
                }
            }
        },
        footerMethod: function ({columns, data}) {
            var newRow = [];
            XEUtils.arrayEach(columns, (column, columnIndex) => {
                if (columnIndex === 1) {
                    newRow.push("+")
                } else {
                    var newCell = null;
                    newRow.push(newCell)
                }
            })
            return [newRow]
        },
        addParamRow: function ({items, $rowIndex, column, columnIndex, $columnIndex, $event}) {
            if (columnIndex === 1) {
                var _this = this;
                var paramData = this.paramList
                var paramLength = paramData.length
                var paramTableData = this.paramData
                var paramTableLength = paramTableData.length
                var paramNum;
                if (paramTableLength !== 0) {
                    for (var i = 0; i < paramLength; i++) {
                        var showData = true;
                        for (var j = 0; j < paramTableLength; j++) {
                            if (paramData[i].key === paramTableData[j].key) {
                                showData = false;
                            }
                        }
                        if (showData) {
                            paramNum = i;
                            break;
                        }
                    }
                    this.paramData[paramTableLength] = {
                        key: paramData[paramNum].key,
                        value: paramData[paramNum].value,
                        del: "-",
                        num: paramTableLength
                    }
                    this.$refs.paramTable.reloadData(this.paramData)
                }
            }
        },
        delParamRow: function ({column, row}) {
            if (column.title === "操作" && row.num !== 0) {
                this.paramData.splice(row.num, 1)
                this.$refs.paramTable.reloadData(this.paramData)
            }
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