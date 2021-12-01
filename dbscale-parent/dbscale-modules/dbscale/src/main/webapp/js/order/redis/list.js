var listApp = new Vue({
    el: '#redisList',
    data: {
        title: '工单',
        table_heighth: '',
        menu: menu,
        btnList: ttBtnList,
        rowBtnList: rowBtnList,
        disabledValue: {},
        btnReplaceList: {
            add: "btnAdd",
            jump: "btnJump",
            update: "btnUpdate",
            approved: "btnApproved",
            execute: "btnExecute",
            del: "btnDelete",
            refresh: "refresh"
        },
        btnOperation: function (value, type) {
            if (type === 'type') {
                switch (value) {
                    case this.btnReplaceList.add:
                        return 'primary'
                    case this.btnReplaceList.update:
                        return ''
                    case this.btnReplaceList.approved:
                        return ''
                    case this.btnReplaceList.execute:
                        return ''
                    case this.btnReplaceList.del:
                        return ''
                    case this.btnReplaceList.refresh:
                        return ''
                }
            } else if (type === 'icon') {
                switch (value) {
                    case this.btnReplaceList.add:
                        return 'el-icon-plus'
                    case this.btnReplaceList.update:
                        return 'el-icon-edit'
                    case this.btnReplaceList.approved:
                        return 'el-icon-setting'
                    case this.btnReplaceList.execute:
                        return 'el-icon-setting'
                    case this.btnReplaceList.del:
                        return 'el-icon-delete'
                    case this.btnReplaceList.refresh:
                        return 'el-icon-refresh'
                    default:
                        return 'el-icon-setting'
                }
            } else if (type === 'show') {
                switch (value) {
                    case this.btnReplaceList.update:
                        return false

                    case this.btnReplaceList.approved:
                        return false

                    default:
                        return true
                }
            }

        },
        statusShow: function (status) {
            if (status === null) {
                return ""
            }
            var type;
            switch (status.code) {
                case "executing":
                    type = COLOR_ORDER_EXECUTING
                    break;
                case "success":
                    type = COLOR_ORDER_SUCCESS
                    break;
                case "failed":
                    type = COLOR_ORDER_FAILED
                    break;
                case "approved":
                    type = COLOR_ORDER_APPROVED
                    break;
                case "unapprove":
                    type = COLOR_ORDER_UNAPPROVE
                    break;
                case "unapproved":
                    type = COLOR_ORDER_UNAPPROVED
                    break;
                default:
                    return ""
            }
            return getProjectSvg(type) + "<span style='line-height:14px'>" + status.display + "</span>"
        },
        selectedData: {},
        goTourlDialogShow: false,
        allAlign: ALLALIGN,
        tableDataAll: [],
        tableData: [],
        searchData: [],
        sortData: [],
        refreshKey: ["id"],
        refreshSaveData: [],
        ogAutoExamine: false,
        ogAutoExecute: false,
        filterName: '',
        tablePage: {
            currentPage: 1,
            pageSize: LINEFEED_PAGE_SIZE,
            totalResult: 0,
            pageSizes: PAGE_SIZES,
            pageLayouts: PAGE_LAYOUTS
        }
    },
    created: function () {
        var _this = this
        this.btnDisabledCreated()
        this.btnClick(this.btnReplaceList.refresh, true, 0)
        setTimeout(function () {
            getHtmlMinWidth("redisList", _this, ['xTable'])
        }, 0)
    },
    methods: {
        dataDispose: function (data) {
            var _this = this
            var jq_jsonData = [];
            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))
                var cfgTitle = "数据库";

                var orders;
                var orderVersion;
                var dataSize = "";
                var logSize = "";
                var cnt = "";
                var cntText = "";
                if (v.orders.length === 0 || v.orders === null) {
                    orders = null
                    orderVersion = null
                } else {
                    XEUtils.arrayEach(v.orders, (value, index) => {
                        if (v.createType.code === "image_update") {
                            if (value.version !== null) {
                                if (value.version.major !== null) {
                                    if (value.version === null) {
                                        orderVersion = ""
                                    } else {
                                        switch (value.type.code) {
                                            case "redis":
                                                cfgTitle = "数据库"
                                                break;
                                            case "redis-sentinel":
                                                cfgTitle = "高可用"
                                                break;
                                        }
                                        orderVersion = jsonJudgeNotDefined(value, 'value.version.major')
                                            + "." + jsonJudgeNotDefined(value, 'value.version.minor')
                                            + "." + jsonJudgeNotDefined(value, 'value.version.patch')
                                            + "." + jsonJudgeNotDefined(value, 'value.version.build')
                                    }
                                }
                            }
                        }
                        if (v.createType.code === "scale_up_cpumem") {
                            if (value.scale !== null) {
                                if (value.scale.name !== null) {
                                    switch (value.type.code) {
                                        case "redis":
                                            cfgTitle = "数据库"
                                            break;
                                        case "redis-sentinel":
                                            cfgTitle = "高可用"
                                            break;
                                    }
                                }
                                orders = value
                            }
                        } else if (v.createType.code === "scale_up_storage") {
                            if (value.dataSize !== null) {
                                switch (value.type.code) {
                                    case "redis":
                                        cfgTitle = "数据库"
                                        break;
                                    case "redis-sentinel":
                                        cfgTitle = "高可用"
                                        break;
                                }
                                orders = value
                                dataSize = jsonJudgeNotDefined(value, 'value.dataSize')
                                logSize = jsonJudgeNotDefined(value, 'value.logSize')
                            }
                        }
                        if (v.createType.code === "new") {
                            if (value.type.code === "redis") {
                                orders = value
                                orderVersion = jsonJudgeNotDefined(value, 'value.version.major')
                                    + "." + jsonJudgeNotDefined(value, 'value.version.minor')
                                    + "." + jsonJudgeNotDefined(value, 'value.version.patch')
                                    + "." + jsonJudgeNotDefined(value, 'value.version.build')
                                dataSize = jsonJudgeNotDefined(value, 'value.dataSize')
                                logSize = jsonJudgeNotDefined(value, 'value.logSize')
                                cnt = jsonJudgeNotDefined(value, 'value.cnt')
                                if (cnt > 1)
                                    cntText = jsonJudgeNotDefined(value, 'value.cnt') + "分片"
                            }
                        }
                    })
                }

                let archModeDisplay = jsonJudgeNotDefined(orders, "orders.arch.mode.display")
                let unitCntText = ""
                if (!XEUtils.isEmpty(archModeDisplay) && archModeDisplay !== "单节点") {
                    unitCntText = XEUtils.toInteger(jsonJudgeNotDefined(orders, "orders.arch.unitCnt")) - 1
                    unitCntText += "副本"
                }

                let highAvailableText = ""
                if (!!jsonJudgeNotDefined(v, 'v.highAvailable')) {
                    highAvailableText = "高可用"
                }

                if (dataSize !== "") {
                    dataSize = dataSize + "G"
                }
                if (logSize !== "") {
                    logSize = logSize + "G"
                }

                json.ownerName = ownerNameDispose(v.owner.name, v.owner.username)
                json.statusText = jsonJudgeNotDefined(v, 'v.state.display')
                json.cfgTitle = cfgTitle
                json.orders = orders
                json.orderVersion = orderVersion
                json.unitCntText = unitCntText
                json.highAvailableText = highAvailableText
                json.dataSize = dataSize
                json.logSize = logSize
                json.status = v.state
                json.cnt = cnt
                json.cntText = cntText

                jq_jsonData.push(json);
            });
            this.tableDataAll = jq_jsonData;
            this.searchData = this.tableDataAll.concat();
            this.sortData = this.searchData.concat();
            this.searchClick({changePage: true})
        },
        btnDisabledCreated: function () {
            var tempArray = XEUtils.values(this.btnReplaceList)
            var tempJson = {}
            for (var i = 0; i < tempArray.length; i++) {
                XEUtils.set(tempJson, tempArray[i], true)
            }
            tempJson[this.btnReplaceList.add] = false
            this.disabledValue = tempJson
        },
        isAble: function (code, data) {
            var _this = this
            if (XEUtils.isArray(data)) {
                if (data.length === 0) {
                    this.btnDisabledCreated()
                } else {
                    XEUtils.clear(_this.disabledValue, false)
                    XEUtils.arrayEach(data, (v, index) => {
                        var tempArray = XEUtils.values(_this.btnReplaceList)
                        for (var i = 0; i < tempArray.length; i++) {
                            if (_this.isAble(tempArray[i], v)) {
                                _this.disabledValue[tempArray[i]] = true
                            }
                        }
                    })
                }
            } else {
                switch (code) {
                    case this.btnReplaceList.approved:
                        switch (data.status.code) {
                            case "unapproved":
                                return false
                            default:
                                return true
                        }
                    case this.btnReplaceList.jump:
                        return XEUtils.isEmpty(data.servGroupId)
                    case this.btnReplaceList.execute:
                        switch (data.status.code) {
                            case "approved":
                                return false
                            case "failed":
                                return false
                            default:
                                return true
                        }
                    case this.btnReplaceList.update:
                        if (data.createType.code === "delete") {
                            return true
                        } else {
                            switch (data.status.code) {
                                case "unapproved":
                                    return false
                                case "unapprove":
                                    return false
                                case "failed":
                                    return false
                                default:
                                    return true
                            }
                        }
                    case this.btnReplaceList.del:
                        switch (data.status.code) {
                            case "unapproved":
                                return false
                            case "approved":
                                return false
                            case "unapprove":
                                return false
                            default:
                                return true
                        }
                }
            }
        },
        returnList: function () {
            commonRefreshList(this)
        },
        handleSizeCurrentChange: function (value) {
            commonHandleSizeCurrentChange(this, value)
        },
        handlePageChange: function () {
            commonHandlePageChange(this)
            this.table_heighth = commonOnresize()
        },
        userListView: function (successFun) {
            var _this = this
            var userName = getSession("UserName")
            sendGet("/" + getProjectName() + "/users/" + userName, function (response) {
                var data = response.data.data
                _this.ogAutoExamine = data.ogAutoExamine
                _this.ogAutoExecute = data.ogAutoExecute
                layer.closeAll('loading')
                successFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        actionDisplayClick: function (data) {
            //console.log(data.id)
            var _this = this
            var width = '960px'
            var height = '600px'
            var title = "任务列表"
            var url = "/" + getProjectName() + "/app/order/task"
            var urlData = "?obj_id=" + data.taskId
            var settings = {
                cancel: function (index, layero) {
                    _this.returnList()
                }
            }
            layerOpenFun(width, height, title, url + urlData, settings)
        },
        addAjaxFun: function (width, height, url) {
            var title = "新增"
            layerOpenFun(width, height, title, url)
        },
        editAjaxFun: function (getCheckboxTablerId, width, height, url) {
            var title = "编辑"
            this.selectedData = getCheckboxTablerId
            layerOpenFun(width, height, title, url)
        },
        approvedAjaxFun: function (getCheckboxTablerId, width, height, url) {
            var title = "审批"
            this.selectedData = getCheckboxTablerId
            layerOpenFun(width, height, title, url)
        },
        executeAjaxFun: function (getCheckboxTablerId, width, height, url) {
            var title = "执行"
            this.selectedData = getCheckboxTablerId
            layerOpenFun(width, height, title, url)
        },
        //批量执行
        executeBatchAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPut("/" + getProjectName() + "/order_groups/" + getCheckboxTablerId.id + "/execute", function (response) {
                successFun(response, getCheckboxTablerId);
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        deleteAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendDelete("/" + getProjectName() + "/order_groups/" + getCheckboxTablerId.id, function (response) {
                successFun(response, getCheckboxTablerId);
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        refreshAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendGet("/" + getProjectName() + "/order_groups?site_id=" + getSession("siteId") + "&category=redis", function (response) {
                successFun(response, getCheckboxTablerId);
                layer.closeAll('loading');
            }, function (error) {
                falseFun(error, getCheckboxTablerId)
            }, null)
        },
        btnClick: function (code, array, getCheckboxTablerId) {
            var _this = this
            var width
            var height
            var url = ""
            var urlData = ""
            if (this.$refs.xTable) {
                var getCheckboxTablerData = this.$refs.xTable.getCheckboxRecords()
                var getCheckboxTablerNum = getCheckboxTablerData.length
            }
            if (!array) {
                getCheckboxTablerData = [getCheckboxTablerId]
                this.$refs.xTable.clearCheckboxRow()
                setTimeout(function () {
                    _this.$refs.xTable.setCheckboxRow(getCheckboxTablerId, true)
                    _this.selectChangeEvent({checked: true, records: getCheckboxTablerData})
                }, 1);
            }
            if (getCheckboxTablerNum === 1 && array) {
                array = false
                getCheckboxTablerId = getCheckboxTablerData[0]
            }
            if (code !== this.btnReplaceList.refresh) {
                this.selectedData = {}
                this.refreshSaveData = []
            }
            let confirmsName = array ? ['工单'] : getCheckboxTablerId.name

            switch (code) {
                case this.btnReplaceList.add://新增
                    sendGet("/" + getProjectName() + "/orders/cfgs/redis", function (response) {
                        var data = response.data.data
                        layer.closeAll('loading')
                        if (data.length !== 0) {
                            _this.userListView(function () {
                                width = 778
                                height = 485
                                url = "/" + getProjectName() + "/app/order/redis/add"
                                urlData = "?ogAutoExamine=" + _this.ogAutoExamine + "&ogAutoExecute=" + _this.ogAutoExecute
                                if (_this.ogAutoExamine) {
                                    height += 24
                                }
                                _this.addAjaxFun(width + "px", height + "px", url + urlData)
                            })
                        } else {
                            _this.$message.closeAll();
                            operationCompletion(_this, '未配置工单默认值', 'error')
                        }
                    }, function (error) {
                        operationCompletion(_this, error.response.data.msg, 'error')
                    }, null)
                    break;

                case this.btnReplaceList.jump://跳转
                    var id = 403000999
                    var menuData = {}
                    var menuLists = window.top.indexApp.menuLists
                    var urlCode = "/app/servgroup/redis"
                    var indexId = getCheckboxTablerId.servGroupId
                    var indexName = getCheckboxTablerId.name
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
                    url = "/app/servgroup/redis/manage/" + indexId + "?id=" + id + "&menu=" + encodeURIComponent(JSON.stringify(menuData), "utf-8") + "&highAvailable=" + getCheckboxTablerId.highAvailable
                    json = {
                        code: url,
                        name: indexName,
                    }
                    window.top.indexApp.menuClick(json, null, false)
                    break;

                case this.btnReplaceList.update://编辑
                    _this.userListView(function () {
                        width = 778
                        height = 485

                        urlData = "?ogAutoExamine=" + _this.ogAutoExamine + "&ogAutoExecute=" + _this.ogAutoExecute
                        if (_this.ogAutoExamine) {
                            height += 24
                        }

                        switch (getCheckboxTablerId.createType.code) {
                            case "new"://新工单
                                url = "/" + getProjectName() + "/app/order/redis/edit/new/" + getCheckboxTablerId.id
                                _this.editAjaxFun(getCheckboxTablerId, width + "px", height + "px", url + urlData)
                                break;
                            case "scale_up_cpumem"://计量扩容
                                url = "/" + getProjectName() + "/app/order/redis/edit/scaleUp/cpumem/" + getCheckboxTablerId.id
                                _this.editAjaxFun(getCheckboxTablerId, width + "px", height + "px", url + urlData)
                                break;
                            case "scale_up_storage"://存储扩容
                                url = "/" + getProjectName() + "/app/order/redis/edit/scaleUp/storage/" + getCheckboxTablerId.id
                                _this.editAjaxFun(getCheckboxTablerId, width + "px", height + "px", url + urlData)
                                break;
                            case "image_update"://升级
                                url = "/" + getProjectName() + "/app/order/redis/edit/imageUpdate/" + getCheckboxTablerId.id
                                _this.editAjaxFun(getCheckboxTablerId, width + "px", height + "px", url + urlData)
                                break;
                        }
                    })
                    break;

                case this.btnReplaceList.approved://审批
                    _this.userListView(function () {
                        width = 778
                        height = 485
                        urlData = "?ogAutoExecute=" + _this.ogAutoExecute
                        if (_this.ogAutoExecute) {
                            height += 24
                        }

                        switch (getCheckboxTablerId.createType.code) {
                            case "new"://新工单
                                url = "/" + getProjectName() + "/app/order/redis/approved/new/" + getCheckboxTablerId.id
                                _this.approvedAjaxFun(getCheckboxTablerId, width + "px", height + "px", url + urlData)
                                break;
                            case "scale_up_cpumem"://计量扩容
                                url = "/" + getProjectName() + "/app/order/redis/approved/scaleUp/cpumem/" + getCheckboxTablerId.id
                                _this.approvedAjaxFun(getCheckboxTablerId, width + "px", height + "px", url + urlData)
                                break;
                            case "scale_up_storage"://存储扩容
                                url = "/" + getProjectName() + "/app/order/redis/approved/scaleUp/storage/" + getCheckboxTablerId.id
                                _this.approvedAjaxFun(getCheckboxTablerId, width + "px", height + "px", url + urlData)
                                break;
                            case "image_update"://升级
                                url = "/" + getProjectName() + "/app/order/redis/approved/imageUpdate/" + getCheckboxTablerId.id
                                _this.approvedAjaxFun(getCheckboxTablerId, width + "px", height + "px", url + urlData)
                                break;
                            case "delete"://删除
                                url = "/" + getProjectName() + "/app/order/redis/approved/delete/" + getCheckboxTablerId.id
                                _this.approvedAjaxFun(getCheckboxTablerId, width + "px", height + "px", url + urlData)
                                break;
                        }
                    })
                    break;

                case this.btnReplaceList.execute://执行
                    if (array) {
                        commonConfirm(this, '执行确认', getHintText('执行', confirmsName)).then(() => {
                            sendAll(_this.executeBatchAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
                                if (errorArray.length === 0) {
                                    operationCompletion(_this, "操作成功！")
                                    _this.goTourlDialogShow = true
                                    _this.returnList()
                                } else {
                                    _this.refreshSaveData = []
                                    operationCompletion(_this, errorMsg(errorArray, array), 'error')
                                }
                            })
                        }).catch(() => {
                        })
                    } else {
                        switch (getCheckboxTablerId.createType.code) {
                            case "new"://新工单
                                url = "/" + getProjectName() + "/app/order/redis/execute/new/" + getCheckboxTablerId.id
                                _this.executeAjaxFun(getCheckboxTablerId, '778px', '485px', url)
                                break;
                            case "scale_up_cpumem"://计量扩容
                                url = "/" + getProjectName() + "/app/order/redis/execute/scaleUp/cpumem/" + getCheckboxTablerId.id
                                _this.executeAjaxFun(getCheckboxTablerId, '778px', '485px', url)
                                break;
                            case "scale_up_storage"://存储扩容
                                url = "/" + getProjectName() + "/app/order/redis/execute/scaleUp/storage/" + getCheckboxTablerId.id
                                _this.executeAjaxFun(getCheckboxTablerId, '778px', '485px', url)
                                break;
                            case "image_update"://升级
                                url = "/" + getProjectName() + "/app/order/redis/execute/imageUpdate/" + getCheckboxTablerId.id
                                _this.executeAjaxFun(getCheckboxTablerId, '778px', '485px', url)
                                break;
                            case "delete"://删除
                                url = "/" + getProjectName() + "/app/order/redis/execute/delete/" + getCheckboxTablerId.id
                                _this.executeAjaxFun(getCheckboxTablerId, '778px', '485px', url)
                                break;
                        }
                    }
                    break;

                case this.btnReplaceList.del://删除
                    commonConfirm(this, '删除确认', getHintText('删除', confirmsName)).then(() => {
                        sendAll(_this.deleteAjaxFun, getCheckboxTablerData, function (successArray, errorArray) {
                            if (errorArray.length === 0) {
                                operationCompletion(_this, "操作成功！")
                                _this.returnList()
                            } else {
                                _this.refreshSaveData = []
                                operationCompletion(_this, errorMsg(errorArray, array), 'error')
                            }
                        })
                    }).catch(() => {
                    });
                    break;

                case this.btnReplaceList.refresh://刷新
                    this.btnDisabledCreated()
                    this.refreshAjaxFun(getCheckboxTablerId,
                        function (response, sendData) {
                            _this.dataDispose(response.data)
                        }, function (error, sendData) {
                            console.log(error)
                            operationCompletion(_this, error.response.data.msg, 'error')
                        })
                    break;
            }
        },
        sortChange: function (column, settings) {
            commonSortChange(this, column, settings)
        },
        selectAllEvent: function ({checked, records}) {
            //console.log(checked ? '所有勾选事件' : '所有取消事件', records)
            commonCancelHighlight(this)
            this.isAble('', records)
        },
        selectChangeEvent: function ({checked, records}) {
            //console.log(checked ? '勾选事件' : '取消事件', records)
            commonCancelHighlight(this)
            this.isAble('', records)
        },
        loadContentMethod({row}) {
            var _this = this
            return new Promise(resolve => {
                sendGet("/" + getProjectName() + "/order_groups/" + row.id, function (response) {
                    layer.closeAll('loading')
                    row.childRows = _this.childDataDispose(response.data.data)
                    resolve()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, null)
            })
        },
        toggleExpandRow: function (row) {
            var _this = this
            XEUtils.arrayEach(_this.tableData, (val, index) => {
                if (row.id === val.id) {
                    _this.$refs.xTable.toggleRowExpand(_this.tableData[index])
                }
            })
        },
        childDataDispose: function (v) {
            var jq_json = {};

            if (v.length === 0) {
                return []
            }

            if (v.orders.length !== 0 || v.orders !== null) {
                XEUtils.arrayEach(v.orders, (value, index) => {
                    if (value.type.code === "redis") {
                        var backUpType = ""
                        var backupStorageType = ""
                        var cronExpressionText = ""
                        var description = ""
                        var paramCfg = ""
                        if (v.createType.code === "new" && value.cfg !== null) {
                            paramCfg = value.cfg.param
                            if (value.cfg.backupStrategy !== undefined && value.cfg.backupStrategy !== null) {
                                backUpType = value.cfg.backupStrategy.type.display
                                backupStorageType = value.cfg.backupStrategy.backupStorageType.display

                                var fileRetentionNumText = jsonJudgeNotDefined(value, "value.cfg.backupStrategy.fileRetentionNum") + ""
                                var cronExpressionArray = value.cfg.backupStrategy.cronExpression.split(' ')
                                cronExpressionText = ""
                                var cronExpressionTimeText = cronExpressionArray[1] + ":" + cronExpressionArray[0]
                                if (cronExpressionArray[2] !== "*") {
                                    fileRetentionNumText += "月"
                                    cronExpressionText = "每月" + cronExpressionArray[2] + "号 " + cronExpressionTimeText
                                } else if (cronExpressionArray[4] !== "*") {
                                    fileRetentionNumText += "周"
                                    switch (cronExpressionArray[4] + '') {
                                        case '1':
                                            cronExpressionText = "每周一 " + cronExpressionTimeText
                                            break;
                                        case '2':
                                            cronExpressionText = "每周二 " + cronExpressionTimeText
                                            break;
                                        case '3':
                                            cronExpressionText = "每周三 " + cronExpressionTimeText
                                            break;
                                        case '4':
                                            cronExpressionText = "每周四 " + cronExpressionTimeText
                                            break;
                                        case '5':
                                            cronExpressionText = "每周五 " + cronExpressionTimeText
                                            break;
                                        case '6':
                                            cronExpressionText = "每周六 " + cronExpressionTimeText
                                            break;
                                        case '7':
                                            cronExpressionText = "每周日 " + cronExpressionTimeText
                                            break;
                                    }
                                } else {
                                    fileRetentionNumText += "天"
                                    cronExpressionText = "每天 " + cronExpressionTimeText
                                }
                                description = value.cfg.backupStrategy.description
                            }
                        }
                        jq_json = {
                            "port": value.port,
                            /*"hostHA": value.hostHA,*/
                            "timestamp": v.created.timestamp,
                            "paramCfg": paramCfg,
                            "msg": v.msg,
                            "backUpType": backUpType,
                            "backupStorageType": backupStorageType,
                            "cronExpressionText": cronExpressionText,
                            "fileRetentionNumText": fileRetentionNumText,
                            "description": description
                        };
                        var jsonArray = []
                        for (var key in paramCfg) {
                            var o = {
                                "key": key,
                                "value": paramCfg[key]
                            };
                            jsonArray.push(o)
                        }
                        jq_json.paramCfg = jsonArray
                    }
                })
            }
            return jq_json;
        },
        searchClick: function (settings) {
            let keyArray = ['name', 'ownerName', 'businessSubsystem.name', 'businessSubsystem.businessSystem.name', 'site.name',
                'businessArea.name', 'cfgTitle', 'orderVersion', 'sysArchitecture.display', 'orders.scale.name', 'orders.arch.mode.display',
                'unitCntText', 'cntText', 'highAvailableText', 'orders.diskType.display', 'dataSize', 'logSize', 'createType.display', 'state.display',]
            commonSearchClick(this, keyArray, settings)
        },
        tableRowClassName: function (row) {
            return commonTableRowClassName(row)
        },
        goToServgroup: function (flag) {
            if (flag) {
                var json = {}
                var menuSkip = {}
                var urlName = ""
                var code = "/app/servgroup/redis"
                var menuLists = parent.window.indexApp.menuLists
                var menuId;
                XEUtils.arrayEach(menuLists, (menuList, menuIndex) => {
                    XEUtils.arrayEach(menuList.childrens, (subMenuList, subMenuIndex) => {
                        if (subMenuList.code === code) {
                            menuSkip = {
                                name: menuList.name,
                                icon: menuList.icon,
                                subMenu: {
                                    name: subMenuList.name
                                }
                            }
                            urlName = subMenuList.name + "服务"
                            menuId = subMenuList.id
                        }
                    })
                })
                var url = code + "?id=" + menuId + "&menu=" + encodeURIComponent(JSON.stringify(menuSkip), "utf-8") + "&t=" + (new Date()).getTime()
                json = {
                    code: url,
                    name: urlName,
                }
                var tab = {
                    name: url
                }
                parent.window.indexApp.tabsJump(tab)
                parent.window.indexApp.menuClick(json, null, false)
            }
            this.returnList()
            this.goTourlDialogShow = false
        }
    }
})
window.onresize = function () {
    listApp.table_heighth = commonOnresize()
}