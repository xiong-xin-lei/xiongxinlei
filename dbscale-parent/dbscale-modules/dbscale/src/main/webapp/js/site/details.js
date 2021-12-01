var siteId = getQueryVariable("siteId")
new Vue({
    el: '#details',
    data: {
        siteName: '',
        region: '',
        type: '',
        domain: '',
        port: '',
        version: '',
        networkMode: '',
        status: '',
        createdtimestamp: '',
        description: '',
    },
    created: function () {
        this.refreshSite();
    },
    methods: {
        refreshSite: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sites/" + siteId, function (response) {
                layer.closeAll('loading');
                _this.siteName = response.data.data.name,
                    _this.region = response.data.data.region.display,
                    _this.type = response.data.data.type,
                    _this.domain = response.data.data.domain,
                    _this.port = response.data.data.port,
                    _this.version = response.data.data.version,
                    _this.networkMode = response.data.data.networkMode,
                    _this.status = response.data.data.state.display,
                    _this.createdtimestamp = response.data.data.created.timestamp,
                    _this.description = response.data.data.description
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
    }
})