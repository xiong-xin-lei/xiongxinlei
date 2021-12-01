var editScaleUpIndex = parent.layer.getFrameIndex(window.name);
var ogAutoExamine = getQueryVariable("ogAutoExamine") === "true"
var ogAutoExecute = getQueryVariable("ogAutoExecute") === "true"

var checkNum = (rule, value, callback) => {
    if (value < 10) {
        callback(new Error('最小值为 10 '));
        return false;
    }
    callback();
};
new Vue({
    el: '#editScaleUp',
    data: {
        ogAutoExamine: ogAutoExamine,
        ogAutoExecute: ogAutoExecute,
        stepsActive: 0,
        initialization: 0,
        scaleList: [],
        formData: {
            type: "",
            name: "",
            ownerName: "",
            businessSystemName: "",
            businessSubsystem: "",
            site: "",
            businessArea: "",
            cpuCnt: 0.0,
            memSize: 0.0,
            scale: "",
            diskType: "",
            dataSize: "",
            logSize: ""
        },
        formRules: {
            scale: [
                {required: true, message: '请选择规模', trigger: ['change', 'blur']}
            ],
            dataSize: [
                {required: true, message: '请输入表空间大小', trigger: ['change', 'blur']},
                {validator: checkNum, trigger: 'blur'}
            ],
            logSize: [
                {required: true, message: '请选择日志空间大小', trigger: ['change', 'blur']},
                {validator: checkNum, trigger: 'blur'}
            ]
        }
    },
    created: function () {
        this.editCreated();
    },
    methods: {
        stepsActiveAdd: function () {
            if (this.stepsActive === 0) {
                this.scaleListView()
            }
            this.stepsActive++
        },
        scaleListView: function (val) {
            var _this = this
            sendGet("/" + getProjectName() + "/scales?type=" + _this.formData.orderType + "&enabled=true", function (response) {
                _this.scaleList = response.data.data
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
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

                XEUtils.arrayEach(data.orders, (v, i) => {
                    if (v.scale !== null) {

                        var scale = "";
                        var cpuCnt = 0;
                        var memSize = 0;
                        if (v.scale.name !== null) {
                            scale = v.scale.name
                            cpuCnt = v.scale.cpuCnt
                            memSize = v.scale.memSize
                        }

                        show = true;
                        XEUtils.arrayEach(_this.scaleList, (vScaleList, iScaleList) => {
                            if (v.scale !== null) {
                                if (v.scale.name === vScaleList.name) {
                                    show = false;
                                }
                            }
                        })
                        if (show) {
                            _this.scaleList.push(v.scale)
                        }

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
                            type: "CMHA",
                            name: data.name + '    (' + ownerName + ')',
                            ownerName: ownerName,
                            businessSystemName: businessSystemName,
                            businessSubsystem: businessSubsystem,
                            site: site,
                            businessArea: businessArea,
                            orderType: v.type.code,
                            orderTypeDisplay: orderTypeDisplay,
                            cpuCnt: cpuCnt,
                            memSize: memSize,
                            scale: scale,
                            diskType: diskType,
                            dataSize: v.dataSize,
                            logSize: v.logSize
                        }
                    }
                })
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        formSubmit: function (formName) {//editScaleUp
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
            var _this = this
            const orderType = this.formData.orderType
            const cpuCnt = this.formData.cpuCnt
            const memSize = this.formData.memSize
            const jsonData = {
                "orders": [
                    {
                        "type": orderType,
                        "cpuCnt": parseFloat(cpuCnt),
                        "memSize": parseFloat(memSize)
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
            parent.layer.close(editScaleUpIndex);
        },
        scaleChange: function (val) {
            var cpucnt = this.$refs.scaleRadios.hoverOption.$el.dataset.cpucnt
            var memsize = this.$refs.scaleRadios.hoverOption.$el.dataset.memsize
            this.formData.cpuCnt = parseFloat(cpucnt);
            this.formData.memSize = parseFloat(memsize);
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