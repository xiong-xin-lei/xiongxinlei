var editIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#out',
    data: {
        hostOut: true,
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
            this.hostJson = parent.listApp.hostList[0];
        }
        //this.editCreated()
    },
    methods: {
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/remote_storages/" + remoteStorageId, function (response) {
                var data = response.data.data;
                _this.formData = {
                    port: data,
                    username: data,
                    password: data
                }
                layer.closeAll('loading')
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
        },
        formSubmit: function (formName) {//editModal
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
            var hostId
            var jsonData = {
                "username": this.formData.username,
                "password": this.formData.password,
                "port": this.formData.port
            }
            var _this = this
            let confirmsName = hostsData ? ['主机'] : _this.hostJson.ip
            if (hostsData) {
            	commonConfirm(_this, '出库确认', getHintText('出库', confirmsName)).then(() => {
                    var successNum = 0;
                    var total = this.hostList.length;
                    var outErrorArr = '';
                    XEUtils.arrayEach(this.hostList, (v, i) => {
                        hostId = v.id;
                        parent.listApp.refreshSaveData.push(v)
                        sendPut("/" + getProjectName() + "/hosts/" + hostId + "/out", function (response) {
                            successNum++;
                            if (successNum == total) {
                                setTimeout(function () {
                                    operationCompletion(parent.listApp, "操作成功！")
                                    parent.listApp.returnList()
                                    layer.closeAll('loading')
                                    if (total != _this.hostList.length) {
                                        operationCompletion( _this, outErrorArr.response.data.msg, "error")
                                    } else {
                                        _this.formClose()
                                    }
                                }, 1000);
                            }
                        }, function (error) {
                            outErrorArr = error
                            total--
                            if (successNum == total) {
                            	operationCompletion( _this, error.response.data.msg, "error")
                            }
                        }, jsonData)
                    })
                }).catch(() => {
                });
            } else {
                commonConfirm(_this, '出库确认', getHintText('出库', confirmsName)).then(() => {
                    hostId = this.hostJson.id;
                    parent.listApp.refreshSaveData.push(this.hostJson)
	                    sendPut("/" + getProjectName() + "/hosts/" + hostId + "/out", function (response) {
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
        identification: function () {
            var hostId
            var jsonData = {
                "username": this.formData.username,
                "password": this.formData.password,
                "port": this.formData.port
            }
            var _this = this
            if (hostsData) {
                var successNum = 0;
                var total = this.hostList.length;
                var outErrorArr = '';
                XEUtils.arrayEach(this.hostList, (v, i) => {
                    hostId = v.id;
                    sendPut("/" + getProjectName() + "/hosts/" + hostId + "/identification?type=user", function (response) {
                        successNum++;
                        if (successNum == total) {
                            setTimeout(function () {
                                layer.closeAll('loading')
                                if (total != _this.hostList.length) {
                                    _this.hostOut = true
                                    operationCompletion( _this, "校验失败", "error")
                                } else {
                                    _this.hostOut = false
                                    operationCompletion( _this, "校验成功", "success")
                                }
                            }, 1000);
                        }
                    }, function (error) {
                        outErrorArr = error
                        total--
                        if (successNum == total) {
                            setTimeout(function () {
                                _this.hostOut = true
                                operationCompletion( _this, "校验失败", "error")
                            }, 1000);
                        }
                    }, jsonData)
                })
            } else {
                hostId = this.hostJson.id;
                sendPut("/" + getProjectName() + "/hosts/" + hostId + "/identification?type=user", function (response) {
                    setTimeout(function () {
                        _this.hostOut = false
                        operationCompletion( _this, "校验成功", "success")
                        layer.closeAll('loading')
                    }, 1000);
                }, function (error) {
                    setTimeout(function () {
                        _this.hostOut = true
                        operationCompletion( _this, "校验失败", "error")
                    }, 1000);
                }, jsonData)
            }
        },
        formClose: function () {
            parent.layer.close(editIndex);
        }
    }
})