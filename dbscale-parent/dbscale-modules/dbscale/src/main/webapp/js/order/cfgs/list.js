var listApp = new Vue({
    el: '#list',
    data: {
        iframe_heighth: Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) - 117 + "px",
        tabDatas: [],
        id: 0,
        activeName: '',
        tabUrl: function () {
            switch (this.activeName) {
                case 'tabMySQL':
                    return "/" + getProjectName() + "/app/order/cfgs/mysqlCfgs?id=" + this.id
                case 'tabCMHA':
                    return "/" + getProjectName() + "/app/order/cfgs/cmhaCfgs?id=" + this.id
                case 'tabRedis':
                    return "/" + getProjectName() + "/app/order/cfgs/redisCfgs?id=" + this.id
                case 'tabApache':
                    return "/" + getProjectName() + "/app/order/cfgs/apacheCfgs?id=" + this.id
                case 'tabNginx':
                    return "/" + getProjectName() + "/app/order/cfgs/nginxCfgs?id=" + this.id
            }
        }
    },
    created: function () {
        this.tabDatas = btnLists;
        this.activeName = btnLists[0].code;
    },
    methods: {
        returnList: function () {
        	var _this = this
        	var tempTag = _this.activeName
        	_this.activeName = ''
    		setTimeout(function () {
                _this.activeName = tempTag;
            }, 1);
        }
    },
    watch: {
        activeName: function (val) {
            var _this = this;
            this.activeName = val;
            for (var i = 0; i < _this.tabDatas.length; i++) {
                if (_this.tabDatas[i].code === val) {
                    _this.id = _this.tabDatas[i].id
                    break;
                }
            }
        }
    }
})