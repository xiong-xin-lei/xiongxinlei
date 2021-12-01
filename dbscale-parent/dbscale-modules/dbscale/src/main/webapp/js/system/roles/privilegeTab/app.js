var role_id = getQueryVariable("roleId")
new Vue({
    el: '#app',
    data: {
        data: [],
        props: {
            label: 'name',
            children: 'childrens'
        },
        defaultCheckeds: []
    },
    created: function () {
        this.appDataCreated()
    },
    methods: {
        appDataCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/roles/cfg/apps?site_id=" + getSession("siteId"), function (response) {
                _this.data = response.data.data
                _this.appCreated()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        appCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/roles/" + role_id + "/cfg/apps?site_id=" + getSession("siteId"), function (response) {
                var data = response.data.data;
                var tempArray = []
                var dataTree = data.apps
                XEUtils.eachTree(dataTree, item => {
                    if (XEUtils.isEmpty(item.childrens))
                        tempArray.push(item.id)
                }, {children: 'childrens'})
                _this.defaultCheckeds = tempArray
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        saveData: function (refName) {
            var _this = this
            var tree = this.$refs[refName]
            var arrayData = XEUtils.uniq(XEUtils.union(tree.getCheckedKeys(), tree.getHalfCheckedKeys()))
            sendPost("/" + getProjectName() + "/roles/" + role_id + "/cfg/apps", function (response) {
                operationCompletion(_this, "操作成功！")
                _this.appCreated()
            }, function (error) {
                _this.appCreated()
                operationCompletion(_this, error.response.data.msg, 'error')
            }, arrayData)
        }
    }
})