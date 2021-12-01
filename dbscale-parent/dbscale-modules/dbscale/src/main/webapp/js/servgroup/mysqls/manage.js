var tabType = getQueryVariable("tabType")
var topoDblclickType = getQueryVariable("unitType")
var topoDblclickName = getQueryVariable("selectedUnit")
var highAvailable = getQueryVariable("highAvailable") === 'true'
var manageApp = new Vue({
    el: '#manage',
    data: {
        iframe_heighth: Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) - 134 + "px",
        id: 0,
        menu: menu,
        topoDblclickType: topoDblclickType,
        topoDblclickName: topoDblclickName,
        highAvailable: highAvailable,
        tabDatas: [],
        activeName: '',
        showData: {
            flag: false,
            businessSystem: null,
            businessSubsystem: null,
            owner: null,
            businessArea: null,
            version: null,
            arch: null,
            scale: null,
            diskType: null
        },
        btnReplaceList: {
            topology: "tabTopology",
            unit: "tabUnit",
            paramCfg: "tabParamCfg",
            DB: "tabDB",
            user: "tabUser",
            backupStrategy: "tabBackupStrategy",
            backupFile: "tabBackupFile"
        },
        tabUrl: function () {
            var _this = this
            switch (this.activeName) {
                case this.btnReplaceList.topology://部署架构
                    return "/" + getProjectName() + "/app/servgroup/mysqls/manageTab/topology?rowId=" + rowId + "&id=" + this.id
                case this.btnReplaceList.unit://数据库
                    if (this.highAvailable) {
                        return "/" + getProjectName() + "/app/servgroup/mysqls/manageTab/unit?rowId=" + rowId + "&code=" + this.btnReplaceList.redis + "&id=" + this.id
                    } else {
                        return "/" + getProjectName() + "/app/servgroup/mysqls/manageTab/mysql?rowId=" + rowId + "&code=" + this.btnReplaceList.redis + "&id=" + this.id
                    }
                case this.btnReplaceList.paramCfg://参数配置
                    return "/" + getProjectName() + "/app/servgroup/mysqls/manageTab/paramCfg?rowId=" + rowId + "&id=" + this.id
                case this.btnReplaceList.DB://库管理
                    var menuData = JSON.parse(JSON.stringify(menu))
                    var btnName = ""
                    this.tabDatas.forEach(function (v, i) {
                        if (v.code === _this.activeName) {
                            btnName = v.name
                        }
                    })
                    menuData.subMenu.sSubMenu = {
                        name: btnName
                    }
                    return "/" + getProjectName() + "/app/servgroup/mysqls/manageTab/DB?rowId=" + rowId + "&id=" + this.id + "&menu=" + encodeURIComponent(JSON.stringify(menuData), "utf-8")
                case this.btnReplaceList.user://用户管理
                    return "/" + getProjectName() + "/app/servgroup/mysqls/manageTab/user?rowId=" + rowId + "&id=" + this.id
                case this.btnReplaceList.backupStrategy://备份策略
                    return "/" + getProjectName() + "/app/servgroup/mysqls/manageTab/backupStrategy?rowId=" + rowId + "&id=" + this.id
                case this.btnReplaceList.backupFile://备份文件
                    return "/" + getProjectName() + "/app/servgroup/mysqls/manageTab/backupFile?rowId=" + rowId + "&id=" + this.id
            }
        }
    },
    created: function () {
        let btnList = this.showTab(btnLists)
        this.tabDatas = btnList;
        // this.id = btnList[0].id;
        if (XEUtils.isEmpty(tabType)) {
            this.activeName = btnList[0].code;
        } else {
            this.activeName = tabType;
        }
        this.createdView();
    },
    methods: {
        showTab: function (val) {
            let tempArray = []
            XEUtils.arrayEach(val, (v, i) => {
                switch (v.code) {
                    case this.btnReplaceList.topology://部署架构
                        if (this.highAvailable)
                            tempArray.push(v)
                        break

                    default:
                        tempArray.push(v)
                }
            })
            return tempArray
        },
        createdView: function (val) {
            var _this = this
            sendGetNoLoading("/" + getProjectName() + "/serv_groups/mysql/" + rowId + "?replication=false&topology=false", function (response) {
                var data = response.data.data

                var businessSystem = jsonJudgeNotDefined(data, "data.businessSubsystem.businessSystem.name")
                var businessSubsystem = jsonJudgeNotDefined(data, "data.businessSubsystem.name")
                var sysArchitecture = jsonJudgeNotDefined(data, "data.sysArchitecture.display")

                var owner = ""
                if (data.owner !== null) {
                    owner = ownerNameDispose(data.owner.name, data.owner.username)
                }

                var businessArea = jsonJudgeNotDefined(data, "data.businessArea.name")

                if (data.servs.length !== 0) {
                    data.servs.forEach(function (v, i) {
                        if (v.type.code === "mysql") {

                            var version = ""
                            if (v.version !== null) {
                                version = v.version.major + '.' + v.version.minor + '.' + v.version.patch + '.' + v.version.build
                            }

                            var arch = jsonJudgeNotDefined(v, "v.arch.name")
                            var scale = jsonJudgeNotDefined(v, "v.scale.name")
                            var diskType = jsonJudgeNotDefined(v, "v.diskType.display")

                            _this.showData = {
                                businessSystem: businessSystem,
                                businessSubsystem: businessSubsystem,
                                owner: owner,
                                businessArea: businessArea,
                                version: version,
                                sysArchitecture: sysArchitecture,
                                arch: arch,
                                scale: scale,
                                diskType: diskType
                            }
                        }
                    })
                } else {
                    _this.showData = {
                        businessSystem: businessSystem,
                        businessSubsystem: businessSubsystem,
                        owner: owner,
                        businessArea: businessArea,
                        version: null,
                        sysArchitecture: null,
                        arch: null,
                        scale: null,
                        diskType: null
                    }
                }
                _this.showData.flag = true

                layer.closeAll('loading');
            }, function (error) {
                _this.showData.flag = false
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
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