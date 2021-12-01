var objId = getQueryVariable("obj_id")
var incidentListApp = new Vue({
    el: '#incidentList',
    data: {
        btnReplaceList: {
            refresh: "refresh"
        },
        childArray: [
            {
                code: "host",
                value: "hosts"
            }, {
                code: "node",
                value: "nodes"
            }
        ],
        statusShow: function (row, type) {
            if (type === 'state') {
                switch (row.code) {
                    case "passing":
                        return getProjectSvg(COLOR_PASSING)
                    case "critical":
                        return "<i class='el-icon-view' style='color: " + COLOR_CRITICAL + "'></i>"
                    case "unknown":
                        return "<i class='el-icon-view' style='color: " + COLOR_UNKNOWN + "'></i>"
                    case "warning":
                        return "<i class='el-icon-view' style='color: " + COLOR_WARNING + "'></i>"
                    default:
                        return "<i class='el-icon-view' style='color: " + COLOR_UNKNOWN + "'></i>"
                }
            }
        },
        allAlign: ALLALIGN,
        collapseData: {},
        collapseDataLength: function (value) {
            if (!XEUtils.isEmpty(this.collapseData)) {
                return this.collapseData[value].length
            } else {
                return 0
            }
        }
    },
    created: function () {
        this.btnClick(this.btnReplaceList.refresh, true, 0)
    },
    methods: {
        dataDispose: function (data) {
            var _this = this;
            var jsonData = JSON.parse(JSON.stringify(data.data));
            var json = {};
            XEUtils.arrayEach(_this.childArray, (value, index) => {
                json[value.value] = jsonData[value.value].reverse()
            })
            this.collapseData = json;
        },
        returnList: function () {
            document.location.reload();
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/hosts/" + objId + "/events", function (response) {
                successFun(response, getCheckboxTablerId);
                layer.closeAll('loading');
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        btnClick: function (code, array, getCheckboxTablerId) {
            var _this = this
            //console.log(code, array, getCheckboxTablerId.id)
            switch (code) {
                case this.btnReplaceList.refresh://刷新
                    this.refreshAjaxFun(getCheckboxTablerId,
                        function (response, sendData) {
                            _this.dataDispose(response.data)
                        }, function (error, sendData) {
                        	operationCompletion( _this, error.response.data.msg, "error")
                        })
                    break;
            }
        },
        expandClick: function (rowIndex, listValue, refName) {
            this.$refs[refName][0].toggleRowExpand(this.collapseData[listValue][rowIndex])
        }
    }
})