var updateRoleIndex = parent.layer.getFrameIndex(window.name);
var unitId = getQueryVariable("unitId")
new Vue({
    el: '#updateRole',
    data: {
        formData: {
            id: unitId,
            role: true
        }
    },
    created: function () {
    },
    methods: {
        formSubmit: function () {
            var _this = this
            var id = _this.formData.id
            var role
            if (_this.formData.role) {
                role = "master"
            } else {
                role = "slave"
            }

            var jsonData = {
                "role": role
            }
            sendPut("/" + getProjectName() + "/units/" + id + "/role", function (response) {
                layer.closeAll('loading')
                parent[0].mysqlListApp.returnList()
                _this.formClose()
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, jsonData)
        },
        formClose: function () {
            parent.layer.close(updateRoleIndex);
        }
    }
})