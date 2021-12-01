var imageUpdateIndex = parent.layer.getFrameIndex(window.name);
var architecture = getQueryVariable("architecture")
var rowData = parent.listApp.rowData;
var ogAutoExamine = getQueryVariable("ogAutoExamine") === "true"
var ogAutoExecute = getQueryVariable("ogAutoExecute") === "true"
var highAvailable = getQueryVariable("highAvailable") === 'true'
new Vue({
    el: '#imageUpdate',
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
        imageVersionList: [],
        imageVersionDatas: [],
        formData: {
            id: '',
            name: '',
            type: '',
            version: ''
        },
        formRules: {
            type: [
                {required: true, message: '请选择类型'}
            ],
            version: [
                {required: true, message: '请选择版本'}
            ]
        }
    },
    created: function () {
        this.formData = {
            id: rowData.id,
            name: rowData.name,
            type: this.typeList[0].code,
            version: ''
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
                        var version = ""
                        if (v.version !== null)
                            if (v.version.major !== null && v.version.minor !== null && v.version.patch !== null && v.version.build !== null)
                                version = v.version.major + '.' + v.version.minor + '.' + v.version.patch + '.' + v.version.build

                        json[v.type.code] = version
                    })
                _this.imageVersionDatas = json
                _this.formData.version = _this.imageVersionDatas[_this.formData.type]
                _this.imageVersionListView()
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        imageVersionListView: function () {
            var _this = this
            var type = this.formData.type
            var versionArray = _this.formData.version.split('.')
            var tempMajor = XEUtils.toNumber(versionArray[0])
            var tempMinor = XEUtils.toNumber(versionArray[1])
            sendGet("/" + getProjectName() + "/images?site_id=" + getSession("siteId") + "&type=" + type + "&enabled=true&architecture=" + architecture + "&major=" + tempMajor + "&minor=" + tempMinor, function (response) {
                var json = []
                XEUtils.arrayEach(response.data.data, (v, i) => {
                    if (v.type.code === type) {
                        json.push({
                            version: v.version.major + '.' + v.version.minor + '.' + v.version.patch + '.' + v.version.build
                        })
                    }
                })
                _this.imageVersionList = json

                var show = true;
                XEUtils.arrayEach(_this.imageVersionList, (v, i) => {
                    if (_this.formData.version === v.version) {
                        show = false;
                    }
                })
                if (show) {
                    _this.imageVersionList.push({
                        version: _this.formData.version
                    })
                }
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        typeChange: function () {
            this.formData.version = this.imageVersionDatas[this.formData.type]
            this.imageVersionListView()
        },
        formSubmit: function (formName) {//imageUpdateModal
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
            var version = this.formData.version
            var majorVersion = version.split(".")[0]
            var minorVersion = version.split(".")[1]
            var patchVersion = version.split(".")[2]
            var buildVersion = version.split(".")[3]
            var jsonData = {
                "type": type,
                "version": {
                    "major": parseInt(majorVersion),
                    "minor": parseInt(minorVersion),
                    "patch": parseInt(patchVersion),
                    "build": parseInt(buildVersion)
                }
            }
            var _this = this
            const riskText = "对选择的软件版本进行升级，升级过程会重启服务，导致服务出现短暂无法访问。升级后需要应用岗位配合检查或者重启服务。"
            let setting = {
                customClass: "customizeWidth510pxClass"
            }
            parent.layer.min(imageUpdateIndex);
            commonConfirm(parent.listApp, '升级确认', getRiskLevelHtml(3, riskText) + getHintText('升级'), setting).then(() => {
                parent.layer.restore(imageUpdateIndex);
                sendPut("/" + getProjectName() + "/serv_groups/" + id + "/images", function (response) {
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
                parent.layer.restore(imageUpdateIndex);
            });
        },
        formClose: function () {
            parent.layer.close(imageUpdateIndex);
        }
    }
})