var scaleUpIndex = parent.layer.getFrameIndex(window.name);
var rowData = parent.listApp.rowData;
var ogAutoExamine = getQueryVariable("ogAutoExamine") === "true"
var ogAutoExecute = getQueryVariable("ogAutoExecute") === "true"
var highAvailable = getQueryVariable("highAvailable") === 'true'
var checkNum = (rule, value, callback) => {
    if (scaleUpApp.formData.type === 'mysql') {
        if (value < 10) {
            callback(new Error('最小值为 10 '));
            return false;
        }
    } else {
        if (value < 5) {
            callback(new Error('最小值为 5 '));
            return false;
        }
    }
    callback();
};
var scaleUpApp = new Vue({
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
        formData: {
            id: '',
            type: '',
            name: '',
            dataSize: '',
            logSize: ''
        },
        formRules: {
            type: [
                {required: true, message: '请选择类型'}
            ],
            dataSize: [
                {required: true, message: '请输入表空间'},
                {validator: checkNum, trigger: 'blur'}
            ],
            logSize: [
                {required: true, message: '请输入日志空间'},
                {validator: checkNum, trigger: 'blur'}
            ]
        }
    },
    created: function () {
        this.formData = {
            id: rowData.id,
            name: rowData.name,
            type: this.typeList[0].code,
            dataSize: XEUtils.toInteger(rowData.dataSize),
            logSize: XEUtils.toInteger(rowData.logSize)
        }
        this.createdDataView()
    },
    methods: {
        createdDataView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/serv_groups/cmha/" + rowData.id + "?replication=false&topology=false", function (response) {
                var json = {}
                if (XEUtils.has(response, 'data.data.servs'))
                    XEUtils.arrayEach(response.data.data.servs, (v, i) => {

                        var name = jsonJudgeNotDefined(v, "v.scale.name")
                        var dataSize = jsonJudgeNotDefined(v, "v.dataSize")
                        var logSize = jsonJudgeNotDefined(v, "v.logSize")

                        json[v.type.code] = {
                            name: name,
                            dataSize: dataSize,
                            logSize: logSize
                        }
                    })
                _this.scaleDatas = json
                _this.formData.scaleName = _this.scaleDatas[_this.formData.type].name
                _this.formData.dataSize = _this.scaleDatas[_this.formData.type].dataSize
                _this.formData.logSize = _this.scaleDatas[_this.formData.type].logSize
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        dataShow: function () {
            if (this.formData.type === "mysql") {
                return "表空间"
            } else {
                return "数据目录"
            }
        },
        logShow: function () {
            if (this.formData.type === "mysql") {
                return "日志空间"
            } else {
                return "日志目录"
            }
        },
        typeChange: function () {
            this.formData.scaleName = this.scaleDatas[this.formData.type].name
            this.formData.dataSize = this.scaleDatas[this.formData.type].dataSize
            this.formData.logSize = this.scaleDatas[this.formData.type].logSize
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
            var dataSize = this.formData.dataSize
            var logSize = this.formData.logSize
            var jsonData = {
                "type": type,
                "dataSize": parseInt(dataSize),
                "logSize": parseInt(logSize)
            }
            var _this = this
            parent.layer.min(scaleUpIndex);
            commonConfirm(parent.listApp, '扩容确认', getHintText('扩容')).then(() => {
                parent.layer.restore(scaleUpIndex);
                sendPut("/" + getProjectName() + "/serv_groups/" + id + "/scale/storages", function (response) {
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
        formClose: function () {
            parent.layer.close(scaleUpIndex);
        }
    }
})