var scaleUpIndex = parent.layer.getFrameIndex(window.name);
var rowData = parent.listApp.rowData;
var ogAutoExamine = getQueryVariable("ogAutoExamine") === "true"
var ogAutoExecute = getQueryVariable("ogAutoExecute") === "true"
var highAvailable = getQueryVariable("highAvailable") === 'true'
new Vue({
    el: '#scaleUp',
    data: {
        highAvailable: highAvailable,
        typeList: [
            {
                code: "mysql",
                name: "数据库"
            }, {
                code: "proxysql",
                name: "代理"
            }, {
                code: "cmha",
                name: "高可用"
            }
        ],
        scaleList: [],
        scaleDatas: [],
        formData: {
            id: '',
            type: '',
            name: '',
            cpuCnt: '',
            memSize: '',
            scaleName: ''
        },
        formRules: {
            type: [
                {required: true, message: '请选择类型'}
            ],
            scaleName: [
                {required: true, message: '请选择规模'}
            ]
        }
    },
    created: function () {
        this.formData = {
            id: rowData.id,
            name: rowData.name,
            type: this.typeList[0].code,
            cpuCnt: rowData.scale.cpuCnt,
            memSize: rowData.scale.memSize,
            scaleName: rowData.scale.name
        }
        this.createdDataView()
    },
    methods: {
        createdDataView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/serv_groups/mysql/" + rowData.id + "?replication=false&topology=false", function (response) {
                var json = {}
                if (XEUtils.has(response, 'data.data.servs'))
                    XEUtils.arrayEach(response.data.data.servs, (v, i) => {

                        var name = jsonJudgeNotDefined(v, "v.scale.name")
                        var cpuCnt = jsonJudgeNotDefined(v, "v.scale.cpuCnt")
                        var memSize = jsonJudgeNotDefined(v, "v.scale.memSize")

                        json[v.type.code] = {
                            name: name,
                            cpuCnt: cpuCnt,
                            memSize: memSize,
                        }
                    })
                _this.scaleDatas = json
                _this.formData.scaleName = _this.scaleDatas[_this.formData.type].name
                _this.formData.cpuCnt = _this.scaleDatas[_this.formData.type].cpuCnt
                _this.formData.memSize = _this.scaleDatas[_this.formData.type].memSize
                _this.scaleListView()
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        scaleListView: function () {
            var _this = this
            var type = this.formData.type
            sendGet("/" + getProjectName() + "/scales?type=" + type + "&enabled=true", function (response) {
                //_this.scaleList = response.data.data
                var json = []
                XEUtils.arrayEach(response.data.data, (v, i) => {
                    if (v.type.code === type) {
                        json.push(v)
                    }
                })
                _this.scaleList = json
                var show = true;
                XEUtils.arrayEach(_this.scaleList, (v, i) => {
                    if (_this.formData.scaleName === v.name) {
                        show = false;
                    }
                })

                if (show) {
                    _this.scaleList.push({
                        cpuCnt: _this.formData.cpuCnt,
                        memSize: _this.formData.memSize,
                        scaleName: _this.formData.scaleName
                    })
                }
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        typeChange: function () {
            this.formData.scaleName = this.scaleDatas[this.formData.type].name
            this.formData.cpuCnt = this.scaleDatas[this.formData.type].cpuCnt
            this.formData.memSize = this.scaleDatas[this.formData.type].memSize
            this.scaleListView()
        },
        formSubmit: function (formName) {//scaleUpModal
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
            var id = this.formData.id
            var type = this.formData.type
            var cpuCnt = this.formData.cpuCnt
            var memSize = this.formData.memSize
            var jsonData = {
                "type": type,
                "cpuCnt": parseFloat(cpuCnt),
                "memSize": parseFloat(memSize)
            }
            var _this = this
            const riskText = "对于对应的数据库资源进行计算扩容，需要审批后才执行，扩容过程会重启服务，导致服务出现短暂无法访问。"
            let setting = {
                customClass: "customizeWidth510pxClass"
            }
            parent.layer.min(scaleUpIndex);
            commonConfirm(parent.listApp, '扩容确认', getRiskLevelHtml(3, riskText) + getHintText('扩容'), setting).then(() => {
                parent.layer.restore(scaleUpIndex);
                sendPut("/" + getProjectName() + "/serv_groups/" + id + "/scale/cpumem", function (response) {
                    layer.closeAll('loading')
                    parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                    if (ogAutoExamine && ogAutoExecute) {
                        parent.listApp.returnList()
                    } else {
                        parent.listApp.goTourlDialogShow = true
                    }
                    _this.formClose()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            }).catch(() => {
                parent.layer.restore(scaleUpIndex);
            });
        },
        scaleChange: function (val) {
            var cpucnt = this.$refs.scaleRadios.hoverOption.$el.dataset.cpucnt
            var memsize = this.$refs.scaleRadios.hoverOption.$el.dataset.memsize
            this.formData.cpuCnt = parseFloat(cpucnt);
            this.formData.memSize = parseFloat(memsize);
        },
        formClose: function () {
            parent.layer.close(scaleUpIndex);
        }
    }
})