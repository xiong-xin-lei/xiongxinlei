var listApp = new Vue({
    el: '#list',
    data: {
        iframe_heighth: Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) - 117 + "px",
        tabDatas: [],
        id: 0,
        activeName: '',
        tabType: tabType,
        searchKeyWord: searchKeyWord,
        tabUrl: function () {
            switch (this.activeName) {
                case "tabNfs":
                    return "/" + getProjectName() + "/app/resource/backup_storage/tabNfs/list?id=" + this.id
            }
        }
    },
    created: function () {
        this.tabDatas = btnLists;
        this.activeName = btnLists[0].code;
        this.jumpSearch()
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
    	jumpSearch: function () {
    		if (!XEUtils.isBoolean(this.tabType) && !XEUtils.isUndefined(this.tabType)) {
    			 this.activeName = decodeURI(decodeURI(this.tabType))
    			 this.searchKeyWord = decodeURI(decodeURI(this.searchKeyWord))
    		}else{
    			this.searchKeyWord = ''
    		}
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
window.onresize = function () {
	listApp.table_heighth = commonOnresize()
}