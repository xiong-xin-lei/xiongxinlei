var listApp = new Vue({
        el: '#list',
        data: {
            iframe_heighth: Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) - 100 + "px",
            tabDatas: [],
            activeName: '',
            tabUrl: function () {
                switch (this.activeName) {
                    case 'tabTask':
                        return "/" + getProjectName() + "/app/system/tasks/taskStatus"
                    case 'tabTaskCfg':
                        return "/" + getProjectName() + "/app/system/tasks/taskEdit"
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
            },
            requestsClick: function (tab, event) {
                this.activeName = tab.name;
            }
        }
    })