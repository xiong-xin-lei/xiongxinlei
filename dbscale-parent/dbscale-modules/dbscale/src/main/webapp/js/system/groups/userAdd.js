var userAddIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#userAdd',
    data: {
        allAlign: ALLALIGN,
        tableData: [],
        filterName: ''
    },
    created: function () {
    },
    methods: {
        searchClick: function () {
            var _this = this
            // console.log(this.filterName)
            sendGet("/" + getProjectName() + "/users/" + _this.filterName, function (response) {
                var data = response.data.data
                var arrayTemp = []
                if (XEUtils.isArray(data)) {
                    arrayTemp = data
                } else if (XEUtils.isPlainObject(data)) {
                    arrayTemp.push(data)
                }
                _this.tableData = arrayTemp
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        userAddFun: function (tableRef) {
            var _this = this
            var arrayTemp = []
            var getCheckboxTablerData = this.$refs[tableRef].getCheckboxRecords()
            if (XEUtils.isEmpty(getCheckboxTablerData)) {
                return false
            }
            XEUtils.arrayEach(getCheckboxTablerData, (value, index) => {
                arrayTemp.push(value.username)
            })
            sendPost("/" + getProjectName() + "/groups/" + parent.groupId + "/users", function (response) {
                _this.formClose()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, arrayTemp)
        },
        formClose: function () {
            parent.userApp.userCreated()
            parent.layer.close(userAddIndex);
        }
    }
})