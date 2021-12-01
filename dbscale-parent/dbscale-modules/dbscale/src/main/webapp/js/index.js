var indexApp = new Vue({
    el: '#index',
    data: {
        menuSwitch: true,
        homeUrl: "/" + getProjectName() + "/app/home/list",
        menuLists: menuList,
        iframe_heighth: Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) - 175.9 + "px",
        tabsValue: "0",
        openMenu: "",
        tabDatas: [],
        siteList: [],
        siteId: '',
        siteName: '',
        User: '',
        skipId: '',
        defaultActive: '',
        suMenuListId: '',
        version: {},
        initialization: 0,
        endNum: 3
    },
    created: function () {
        this.openMenu = menuList[0].code
        this.userSessionView()
        this.versionListView()
    },
    methods: {
        isShow: function (data){
            if (data.name == '存储管理'){
                var code = ''
                switch (getSession('storageMode')) {
                    case "pvc":
                        code = '/app/resource/storage/pvcs'
                        break;
                    case"volumepath":
                        code = "/app/resource/storage/volumepaths"
                        break
                }
                if(data.code === code){
                    return true
                }else{
                    return false
                }
            }else{
                return true
            }
        },
        menuClick: function (suMenuList, menuList, needMenu) {
            var jsonDatas = this.tabDatas.concat()
            var length = 0
            var fristIndex
            var frist = true
            var judge = false
            var jsonDataName = suMenuList.name
            this.suMenuListId = suMenuList.id
            var judgeIndex
            var url = ''
            var codeUrlArray
            var codeUrlArrayFlag = false
            if (needMenu) {
                var menuName = menuList.name
                var menuIcon = menuList.icon
                var suMenuId = suMenuList.id
                var suMenuName = suMenuList.name
                var menu = {
                    name: menuName,
                    icon: menuIcon,
                    subMenu: {
                        name: suMenuName
                    }
                }
                url = "/" + getProjectName() + suMenuList.code + "?id=" + suMenuId + "&menu=" + encodeURIComponent(JSON.stringify(menu), "utf-8")
                if (menuList.code === "order") {
                    if (suMenuList.id !== 399000000) {
                        jsonDataName = suMenuName + "工单"
                    }
                } else if (menuList.code === "servgroup") {
                    jsonDataName = suMenuName + "服务"
                }
            } else {
                if (menuList === null) {
                    url = "/" + getProjectName() + suMenuList.code
                    codeUrlArray = suMenuList.code.split(":")[0]
                    codeUrlArrayFlag = !!(codeUrlArray.toLowerCase() === "http" || codeUrlArray.toLowerCase() === "https")
                    if (codeUrlArrayFlag)
                        url = suMenuList.code
                } else {
                    url = "/" + getProjectName() + suMenuList.code + "?menu=" + encodeURIComponent(JSON.stringify(menuList), "utf-8")
                }
            }
            XEUtils.arrayEach(jsonDatas, (v, i) => {
                if (v.show) {
                    length++
                    if (frist) {
                        fristIndex = i
                        frist = false
                    }
                    if (url.split("?")[0] === v.url.split("?")[0] && !codeUrlArrayFlag) {
                        judge = true
                        judgeIndex = i
                        if (v.url.split("?")[1].indexOf(url.split("?")[1]) === -1) {
                            v.url = url
                        } else {
                            url = v.url
                        }
                        // return false
                    }
                }
            })
            if (judge) {
                this.tabsValue = url
                if (document.getElementById(this.tabsValue) != null)
                    document.getElementById(this.tabsValue).contentWindow.listApp.returnList()
            } else {
                if (length >= 9) {
                    var _this = this
                    this.tabDatas[fristIndex].show = false
                    setTimeout(function () {
                        _this.menuClick(suMenuList, menuList, needMenu)
                    }, 1);
                    return
                }
                var jsonData = {
                    name: jsonDataName,
                    url: url,
                    show: true
                }
                jsonDatas.push(jsonData);
                this.tabDatas = jsonDatas.concat();
                this.tabsValue = url
            }
        },
        tabsSwitch: function (tab, type, event) {
            if (tab.name === undefined) {
                this.defaultActive = " "
                this.openMenu = menuList[0].code
                document.getElementById("homePage").contentWindow.listApp.returnList()
            } else {
                var paths = tab.name.split("?")[0].split("/")
                var path = "/" + paths[2] + "/" + paths[3] + "/" + paths[4]
                if (paths[3] === "remote")
                    path = "/" + paths[2] + "/" + paths[3] + "/" + paths[4] + "/" + paths[5]
                var returnListFlag = !!(paths[3] == "servgroup" && paths.length > 5)
                var codeUrlArrayFlag = !!(paths[0].toLowerCase() === "http" || paths[0].toLowerCase() === "https")
                if (!returnListFlag && !codeUrlArrayFlag && type != "tabsEdit")
                    document.getElementById(tab.name).contentWindow.listApp.returnList()
                this.openMenu = path.split("/")[2]
                this.defaultActive = path
            }
        },
        tabsJump: function (tab) {
            var paths = tab.name.split("?")[0].split("/")
            var path = "/" + paths[1] + "/" + paths[2] + "/" + paths[3]
            if (paths[3] === "remote") {
                path = "/" + paths[1] + "/" + paths[2] + "/" + paths[3] + "/" + paths[4]
            }
            this.openMenu = path.split("/")[2]
            this.defaultActive = path
        },
        tabsEdit: function (url) {
            var jsonDatas = this.tabDatas.concat()
            var tabs = [];
            var value = this.tabsValue;
            var index;
            var showNum = 0;
            var firstShow = 0;
            var lastNum = null;
            XEUtils.arrayEach(jsonDatas, (v, i) => {
                if (v.show) {
                    showNum++
                    if (showNum === 0) {
                        firstShow = i;
                    }
                }
                if (v.url === url && v.show) {
                    if (value === url) {
                        if (i !== firstShow) {
                            if (lastNum === null) {
                                value = "0"
                            } else {
                                value = jsonDatas[lastNum].url
                            }
                        } else {
                            value = "0"
                        }
                    }
                    index = i
                } else {
                    tabs.push(v)
                }
                if (v.show) {
                    lastNum = i;
                }
            })
            if (showNum === 1) {
                value = "0"
            }
            this.tabsValue = value;
            this.tabDatas[index].show = false;
            var tab = {
                name: this.tabsValue
            }
            this.tabsSwitch(tab, 'tabsEdit')
            //this.tabDatas = jsonDatas.filter(tab => tab.url !== url);
        },
        createdOpen: function (type) {
            var array = [];
            if (type) {
                array.push(this.openMenu)
                return array;
            }
        },
        handleOpen: function (key, keyPath) {
            //console.log(key, keyPath);
            this.openMenu = keyPath[0]
        },
        userSessionView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sys/session/user", function (response) {
                var data = response.data.data
                if (XEUtils.isEmpty(data)) {
                    clearSession()
                    this.goToUrlName("login")
                }
                let super_managerFlag = jsonJudgeNotDefined(data, "data.role.id") === "system_super_manager"
                setSession("super_manager", super_managerFlag)
                setSession("UserName", data.username)
                _this.User = data.name
                _this.siteListView()
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        siteListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sites", function (response) {
                _this.siteList = response.data.data
                _this.siteId = getSession("siteId");
                var jsonData = {
                    "name": "站点配置",
                }
                _this.siteList.push(jsonData)
                XEUtils.arrayEach(_this.siteList, (v, i) => {
                    if (_this.siteId === v.id) {
                        if (XEUtils.isEmpty(_this.siteId)) {
                            _this.goToUrlName("site")
                        } else {
                            _this.siteName = v.name
                        }
                    }
                })
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        versionListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sys/version", function (response) {
                var versionData = response.data.data
                // var webValue = versionData.version + " (" + versionData.commit + ")"
                // var cmValue = versionData.cmVersion + " (" + versionData.cmCommit + ")"
	            var webValue = jsonJudgeNotDefined(versionData, 'versionData.webVersion.version') + " (" +
	                jsonJudgeNotDefined(versionData, 'versionData.webVersion.commitId') + ")"
	            var cmValue = jsonJudgeNotDefined(versionData, 'versionData.cmVersion.version') + " (" +
                	jsonJudgeNotDefined(versionData, 'versionData.cmVersion.commitId') + ")"
                _this.version = {
                    web: webValue,
                    cm: cmValue
                }
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        endFun: function () {
            var _this = this
            _this.initialization++
            if (_this.initialization === _this.endNum) {
                layer.closeAll('loading')
            }
        },
        siteRefresh: function (data) {
            if (data.name === "站点配置") {
                this.goToUrlName("site")
            } else {
                setSession("siteId", data.id)
                location.reload()
            }
        },
        siteDetail: function () {
            var width = '760px'
            var height = '415px'
            var title = "站点详情"
            var url = "/" + getProjectName() + "/app/sites/details"
            var urlData = "?siteId=" + getSession("siteId")
            var setting = {
                offset: "200px"
            }
            layerOpenFun(width, height, title, url + urlData, setting)
        },
        LoginOut: function () {
            var _this = this
            commonConfirm(_this, '注销确认', getHintText('注销')).then(() => {
                sessionStorage.clear()
                sendPut("/" + getProjectName() + "/logout", function () {
                    _this.goToUrlName("login")
                }, function () {
                    alert("请重试");
                }, null, null);
            }).catch(() => {});
        },
        goToUrlName: function (value = "") {
            let url = "/" + getProjectName() + "/"
            switch (value) {
                case "login":
                    url += ""
                    break
                case "site":
                    url += "/app/sites"
                    break
            }
            clearSession()
            location.href = url
        }
    }
})
window.onresize = function () {
    indexApp.iframe_heighth = Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) - 175.9 + "px";
}