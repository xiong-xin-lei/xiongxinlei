var unitStatus = getQueryVariable("status")
var unitListApp = new Vue({
    el: '#unitList',
    data: {
        title: '单元',
        unitState: unitStatus,
        siteId: parent.parent.window.indexApp.siteId,
        disabledValue: {},
        btnReplaceList: {
            refresh: "refresh"
        },
        btnOperation: function (value, type) {
            if (type === 'type') {
                switch (value) {
                    case this.btnReplaceList.add:
                        return 'primary'
                    default:
                        return ''
                }
            } else if (type === 'icon') {
                switch (value) {
                    case this.btnReplaceList.start:
                        return 'el-icon-video-play'
                    case this.btnReplaceList.stop:
                        return 'el-icon-video-pause'
                    default:
                        return 'el-icon-setting'
                }
            }
        },
        statusShow: function (status, type) {
            if (status === null) {
                return ""
            }
            var data;
            if (type === 'state') {
                data = status.state
                if (!!data) {
                    switch (data.code) {
                        case "passing":
                            return getProjectSvg(COLOR_PASSING) + "<span style='line-height:14px'>" + data.display + "</span>"
                        case "critical":
                            return getProjectSvg(COLOR_CRITICAL) + "<span style='line-height:14px'>" + data.display + "</span>"
                        case "unknown":
                            return getProjectSvg(COLOR_UNKNOWN) + "<span style='line-height:14px'>" + data.display + "</span>"
                        case "warning":
                            return getProjectSvg(COLOR_WARNING) + "<span style='line-height:14px'>" + data.display + "</span>"
                    }
                } else {
                    return ""
                }
            } else if (type === 'task') {
                data = status.task
                if (!!jsonJudgeNotDefined(status, "status.task.state.code")) {
                    switch (data.state.code) {
                        case "success":
                            return "<span style='color:" + COLOR_TASK_COMPLETED + ";border-color:" + COLOR_TASK_COMPLETED + "'>" + status.actionDisplay + "</span>"
                        case "failed":
                            return "<span style='color:" + COLOR_TASK_ERROR + ";border-color:" + COLOR_TASK_ERROR + "'>" + status.actionDisplay + "</span>"
                        case "running":
                            return "<span style='color:" + COLOR_TASK_RUNNING + ";border-color:" + COLOR_TASK_RUNNING + "'>" + status.actionDisplay + "</span>"
                        case "timeout":
                            return "<span style='color:" + COLOR_TASK_TIMEOUT + ";border-color:" + COLOR_TASK_TIMEOUT + "'>" + status.actionDisplay + "</span>"
                        case "unknown":
                            return "<span style='color:" + COLOR_TASK_UNKNOWN + ";border-color:" + COLOR_TASK_UNKNOWN + "'>" + status.actionDisplay + "</span>"
                    }
                } else {
                    return ""
                }
            } else if (type === "pod") {
                data = jsonJudgeNotDefined(status, "status.podState.display");
                if (!!data) {
                    var podElement = "<span>Pod：</span>"
                    switch (data) {
                        case "Pending":
                            return podElement + "<span style='color:#696bd8'>" + data + "</span>"
                        case "Running":
                            return podElement + "<span style='color:" + COLOR_TASK_RUNNING + "'>" + data + "</span>"
                        case "Succeeded":
                            return podElement + "<span style='color:" + COLOR_PASSING + "'>" + data + "</span>"
                        case "Unknown":
                            return podElement + "<span style='color:" + COLOR_UNKNOWN + "'>" + data + "</span>"
                        case "Failed":
                            return podElement + "<span style='color:" + COLOR_CRITICAL + "'>" + data + "</span>"
                    }
                }
            }
        },
        allAlign: ALLALIGN,
        tableDataAll: [],
        tableData: [],
        searchData: [],
        sortData: [],
        filterName: '',
        tablePage: {
            currentPage: 1,
            pageSize: PAGE_SIZE,
            totalResult: 0,
            pageSizes: PAGE_SIZES,
            pageLayouts: PAGE_LAYOUTS
        }
    },
    created: function () {
        this.btnClick(this.btnReplaceList.refresh, true, 0)
    },
    methods: {
        dataDispose: function (data) {
            var jq_jsonData = [];
            var jq_json = {};
            var _this = this
            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))
                var dataSize = jsonJudgeNotDefined(v, "v.dataSize");
                if (!!dataSize || dataSize === 0) {
                    dataSize = dataSize + "G"
                }
                var logSize = jsonJudgeNotDefined(v, "v.logSize");
                if (!!logSize || logSize === 0) {
                    logSize = logSize + "G"
                }
                var memSize = jsonJudgeNotDefined(v, "v.memSize");
                if (!!memSize || memSize === 0) {
                    memSize = memSize + "G"
                }
                var ip = jsonJudgeNotDefined(v, "v.ip");
                var port = jsonJudgeNotDefined(v, "v.port");
                if (!!ip && !!port) {
                    ip = ip + ":" + port
                }

                var versionValue = "";
                if (!!v.version) {
                    if (!!v.version.major)
                        versionValue = v.version.major + '.' + v.version.minor + '.' + v.version.patch + '.' + v.version.build
                }

                var ownerName = jsonJudgeNotDefined(v, "v.servGroup.owner.name");
                var ownerUsername = jsonJudgeNotDefined(v, "v.servGroup.owner.username");

                json.dataSize = dataSize;
                json.logSize = logSize;
                json.memSize = memSize;
                json.ip = ip;
                json.versionValue = versionValue;
                json.ownerName = ownerNameDispose(ownerName, ownerUsername)
                json.diskTypeDisplay = jsonJudgeNotDefined(v, "v.diskType.display");
                json.actionDisplay = jsonJudgeNotDefined(v, "v.task.action.display") + jsonJudgeNotDefined(v, "v.task.state.display");
                json.statusText = jsonJudgeNotDefined(v, "v.state.display");

                if (jsonJudgeNotDefined(v, "v.relateId") !== "" && _this.unitState === jsonJudgeNotDefined(v, "v.state.code")){
                    jq_jsonData.push(json)
                }
                if(XEUtils.isEmpty(_this.unitState)){
                	jq_jsonData.push(json)
                }
            });
            this.tableDataAll = jq_jsonData;
            this.searchData = this.tableDataAll.concat();
            this.sortData = this.searchData.concat();
            this.handlePageChange()
        },
        handleSizeCurrentChange: function (value) {
            commonHandleSizeCurrentChange(this, value)
        },
        handlePageChange: function () {
        	commonHandlePageChange(this)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            var urlValue = ''
            if(XEUtils.isEmpty(_this.unitState)){
            	urlValue = "/" + getProjectName() + "/units?site_id=" + _this.siteId
            }else{
            	urlValue = "/" + getProjectName() + "/units?site_id=" + _this.siteId + "&state=" + _this.unitState
            }
            sendGet(urlValue, function (response) {
                successFun(response, getCheckboxTablerId);
                layer.closeAll('loading');
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        btnClick: function (code, array, getCheckboxTablerId) {
            if (this.$refs.xTable) {
                var getCheckboxTablerData = this.$refs.xTable.getCheckboxRecords()
                var getCheckboxTablerNum = getCheckboxTablerData.length
            }
            if (getCheckboxTablerNum === 1 && array) {
                array = false
                getCheckboxTablerId = getCheckboxTablerData[0]
            }
            var _this = this
            switch (code) {

                case this.btnReplaceList.refresh://刷新
                    if (getCheckboxTablerNum) {
                        this.$refs.xTable.clearCheckboxRow()
                        this.$refs.xTable.clearRowExpand()
                    }
                    this.refreshAjaxFun(getCheckboxTablerId,
                        function (response, sendData) {
                            _this.dataDispose(response.data)
                        }, function (error, sendData) {
                        	operationCompletion(_this, error.response.data.msg, 'error')
                        })
                    break;
            }
        },
        sortChange: function (column) {
        	commonSortChange(this, column)
        },
        cellDblClickManage({ column, row, rowIndex }) {
            var type = jsonJudgeNotDefined(row, 'row.servGroup.category')
            var id = 404000999
            var urlCode = "/app/servgroup/"
            switch (type) {
                case 'mysql':
                    id = 401000999
                    urlCode += type + 's'
                    break
                case 'cmha':
                    id = 402000999
                    urlCode += type + 's'
                    break
                case 'redis':
                    id = 403000999
                    urlCode += type
                    break
                case 'nginx':
                    id = 404000999
                    urlCode += type
                    break
            }
            var menuData = {}
            var menuLists = parent.parent.window.indexApp.menuLists
            var indexId = jsonJudgeNotDefined(row, 'row.servGroup.id')
            var indexName = jsonJudgeNotDefined(row, 'row.servGroup.name')
            var unitType = jsonJudgeNotDefined(row, 'row.type.code')
            var selectedUnit = jsonJudgeNotDefined(row, 'row.id')
            var json = {}
        	XEUtils.arrayEach(menuLists, (menuList, menuIndex) => {
        		XEUtils.arrayEach(menuList.childrens, (subMenuList, subMenuIndex) => {
                    if (subMenuList.code === urlCode) {
                        menuData = {
                            name: menuList.name,
                            icon: menuList.icon,
                            subMenu: {
                                name: subMenuList.name
                            }
                        }
                    }
                })
            })
            menuData.subMenu.servGroupName = indexName
            url = urlCode + "/manage/" + indexId + "?id=" + id + "&menu=" + encodeURIComponent(JSON.stringify(menuData),
            		"utf-8") + "&tabType=tabUnit&unitType=" + unitType + "&selectedUnit=" + selectedUnit + '&highAvailable=' + jsonJudgeNotDefined(row, "row.servGroup.highAvailable")
            json = {
                code: url,
                name: indexName,
            }
            window.top.indexApp.menuClick(json, null, false)
        }
    }
})