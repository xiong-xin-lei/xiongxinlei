var editIndex = parent.layer.getFrameIndex(window.name);
var groupId = getQueryVariable("groupId")
var userApp = new Vue({
    el: '#user',
    data: {
        data: [],
        value: [],
    },
    created: function () {
        this.userCreated()
    },
    methods: {
        userCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/users", function (response) {
                var data = response.data.data;
                var leftData = []
                var rightData = []
                if (XEUtils.isArray(data))
                    XEUtils.arrayEach(data, (list, index) => {
                        var name = ownerNameDispose(list.name, list.username) + "-" + list.company
                        var jsonTemp = {
                            label: name,
                            key: list.username
                        }
                        if (XEUtils.isArray(list.groups)) {
                            /*XEUtils.arrayEach(list.groups, (groupList, groupIndex) => {
                                if (groupList.id === groupId) {
                                    rightData.push(jsonTemp.key)
                                }
                            })*/
                            if (XEUtils.findIndexOf(list.groups, item => item.id === groupId) !== -1)
                                rightData.push(jsonTemp.key)
                        }
                        leftData.push(jsonTemp)
                    })
                _this.data = leftData
                _this.value = rightData
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        handleChange: function (value, direction, movedKeys) {
            switch (direction) {
                case "right":
                    // console.log("add", movedKeys)
                    this.addAjaxFun(movedKeys)
                    break
                case "left":
                    // console.log("del", movedKeys)
                    this.deleteAjaxFun(movedKeys)
                    break
            }
        },
        sendDeleteAjaxFun: function (data, successFun, falseFun) {
            var _this = this
            sendDelete("/" + getProjectName() + "/groups/" + groupId + "/users/" + data, function (response) {
                layer.closeAll('loading')
                successFun(response, data);
            }, function (error) {
                falseFun(error, data)
            }, null)
        },
        addAjaxFun: function (arrayData) {
            var _this = this
            sendPost("/" + getProjectName() + "/groups/" + groupId + "/users", function (response) {
                _this.userCreated()
            }, function (error) {
                _this.userCreated()
                operationCompletion(_this, error.response.data.msg, 'error')
            }, arrayData)
        },
        deleteAjaxFun: function (arrayData) {
            var _this = this
            if (XEUtils.isArray(arrayData)) {
                sendAll(_this.sendDeleteAjaxFun, arrayData, function (successArray, errorArray) {
                    if (errorArray.length !== 0)
                        operationCompletion(_this, errorMsg(errorArray, true, "data.data"), 'error')
                    _this.userCreated()
                })
            }
        },
        userAddFun: function () {
            var _this = this
            var width = '504px'
            var height = '315px'
            var title = "添加"
            var url = "/" + getProjectName() + "/app/system/groups/userAdd"
            var urlData = ""
            var setting = {
                offset: "auto",
                cancel: function (index, layer) {
                    _this.userCreated()
                }
            }
            layerOpenFun(width, height, title, url + urlData, setting)
        },
        formClose: function () {
            parent.layer.close(editIndex);
        }
    }
})