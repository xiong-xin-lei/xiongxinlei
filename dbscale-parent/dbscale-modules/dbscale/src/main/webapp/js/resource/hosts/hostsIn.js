var editIndex = parent.layer.getFrameIndex(window.name);
var hostIn = new Vue({
    el: '#in',
    data: {
        hostIn: true,
        hostsData: hostsData,
        hostJson: {},
        hostList: [],
        siteList: [],
        formData: {
            ip: '',
            port: '22',
            username: '',
            password: ''
        },
        formRules: {
            ip: [
                {required: true, message: '请选择IP'}
            ],
            port: [
                {required: true, message: '请输入端口'}
            ],
            username: [
                {required: true, message: '请输入用户名'}
            ],
            password: [
                {required: true, message: '请输入密码'}
            ]
        }
    },
    created: function () {
        if (!this.hostsData) {
            this.formData.ip = parent.listApp.hostJson.ip;
            this.hostJson = parent.listApp.hostJson;
        } else {
            this.hostList = parent.listApp.hostList;
        }
    },
    methods: {
        formSubmit: function () {
            var hostId
            var jsonData = {
                "username": this.formData.username,
                "password": this.formData.password,
                "port": this.formData.port
            }
            var _this = this
            let confirmsName = hostsData ? ['主机'] : _this.hostJson.ip
            if (hostsData) {
                commonConfirm(_this, '入库确认', getHintText('入库', confirmsName)).then(() => {
                    var successNum = 0;
                    var total = this.hostList.length;
                    var inErrorArr = '';
                    XEUtils.arrayEach(this.hostList, (v, i) => {
                        hostId = v.id;
                        parent.listApp.refreshSaveData.push(v)
                        sendPut("/" + getProjectName() + "/hosts/" + hostId + "/in", function (response) {
                            successNum++;
                            if (successNum == total) {
                                setTimeout(function () {
                                    operationCompletion(parent.listApp, "操作成功！")
                                    parent.listApp.returnList()
                                    layer.closeAll('loading')
                                    if (total != _this.hostList.length) {
                                    	operationCompletion( _this, inErrorArr.response.data.msg, "error")
                                    } else {
                                        _this.formClose()
                                    }
                                }, 1000);
                            }
                        }, function (error) {
                            inErrorArr = error
                            total--
                            if (successNum == total) {
                            	operationCompletion( _this, error.response.data.msg, "error")
                            }
                        }, jsonData)
                    })
                }).catch(() => {
                });
            } else {
                commonConfirm(_this, '入库确认', getHintText('入库', confirmsName)).then(() => {
                    hostId = this.hostJson.id;
                    parent.listApp.refreshSaveData.push(this.hostJson)
                    sendPut("/" + getProjectName() + "/hosts/" + hostId + "/in", function (response) {
                        setTimeout(function () {
                            operationCompletion(parent.listApp, "操作成功！")
                            parent.listApp.returnList()
                            layer.closeAll('loading')
                            _this.formClose()
                        }, 1000);
                    }, function (error) {
                    	operationCompletion( _this, error.response.data.msg, "error")
                    }, jsonData)
                }).catch(() => {
                });
            }
        },
        identification: function (formName) {
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
            var username = this.formData.username
            var password = this.formData.password
            var port = this.formData.port
            var urlData = "?username=" + username + "&password=" + password + "&port=" + port + "&hostsData=" + hostsData
            parent.listApp.inIdentificationAjaxFun(urlData, username, password, port)
        },
        identificationSuccessFun: function () {
            this.hostIn = false
            operationCompletion( this, "校验成功", "success")
        },
        formClose: function () {
            parent.layer.close(editIndex);
        }
    }
})