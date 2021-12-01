var identificationIndex = parent.layer.getFrameIndex(window.name);
var username = getQueryVariable("username")
var password = getQueryVariable("password")
var port = getQueryVariable("port")
var hostsData = getQueryVariable("hostsData") === 'true'
var hostList = []
if (hostsData) {
    hostList = parent.listApp.hostList
} else {
    var hostJson = parent.listApp.hostJson
    hostList.push(hostJson)
}
new Vue({
    el: '#identification',
    data: {
        endNum: 2,
        tableData: [{
            id: 1,
            name: "用户校验",
            successNum: 0,
            falseNum: 0,
            sumNum: hostList.length,
            status: "loading",
            childRows: []
        }, {
            id: 2,
            name: "存储校验",
            successNum: 0,
            falseNum: 0,
            sumNum: hostList.length,
            status: "loading",
            childRows: []
        }, {
            id: 3,
            name: "单元上限校验",
            successNum: 0,
            falseNum: 0,
            sumNum: hostList.length,
            status: "loading",
            childRows: []
        }],
        statusShow: function (data) {
            let fontSize = "17px"
            switch (data) {
                case "loading":
                    return "<i class='el-icon-loading' style='font-size: " + fontSize + ";'></i>"
                case "success":
                    return "<i class='el-icon-success' style='color: green;font-size: " + fontSize + ";'></i>"// vxe-expand-text
                case "error":
                    return "<i class='el-icon-error' style='color: red;font-size: " + fontSize + ";'></i>"
                case "warning":
                    return "<i class='el-icon-warning' style='color: orange;font-size: " + fontSize + ";'></i>"
                default:
                    return "<span>" + data + "</span>"
            }
        }
    },
    created: function () {
        this.createdFun()
    },
    methods: {
        createdFun: function () {
            var jsonData = {
                "username": username,
                "password": password,
                "port": port
            }
            XEUtils.arrayEach(hostList, (value, index) => {
                this.identificationAjaxFun1(value, jsonData)
            })
        },
        successFun: function (num, value) {
            this.tableData[num].successNum++
            let jsonTemp = {
                num: num + 1,
                ip: value.ip,
                status: "success"
            }
            this.tableData[num].childRows.push(jsonTemp)
            this.endFun(num)
        },
        falseFun: function (num, value, status, msg) {
            this.tableData[num].falseNum++
            let errorTemp = '未验证'
            let jsonTemp = {
                num: num + 1,
                ip: value.ip,
                status: status,
                msg: msg
            }
            this.tableData[num].childRows.push(jsonTemp)

            if (num < this.endNum) {
                let numTemp = num + 1
                this.falseFun(numTemp, value, errorTemp, errorTemp)
            }

            if (status !== errorTemp)
                this.endFun(num)
        },
        endFun: function (num) {
            let successNum = this.tableData[num].successNum
            let falseNum = this.tableData[num].falseNum
            let sum = successNum + falseNum
            var status = "loading"
            if (sum === this.tableData[num].sumNum) {
                switch (sum) {
                    case 0:
                        status = ""
                        break
                    case successNum:
                        status = "success"
                        break
                    case falseNum:
                        status = "error"
                        break
                    default:
                        status = "warning"
                }
                this.tableData[num].status = status

                //如果加载完后，状态为error或者warning，展开该行
                if (this.tableData[num].status === "error" || this.tableData[num].status === "warning")
                    this.$refs.xTable.toggleRowExpand(this.tableData[num])

                if (num < this.endNum) {
                    this.tableData[num + 1].sumNum = sum
                    this.endFun(num + 1)
                } else {
                    var flag = true
                    for (var i = 1; i <= this.endNum; i++) {
                        if (i !== this.endNum) {
                            if (this.tableData[0].sumNum !== this.tableData[i].sumNum)
                                flag = false
                        } else {
                            if (this.tableData[0].sumNum !== this.tableData[i].successNum)
                                flag = false
                        }
                    }
                    if (flag) {
                        parent[0].hostIn.identificationSuccessFun()
                        this.formClose()
                    }
                }
            }
        },
        identificationAjaxFun1: function (value, jsonData) {
            var _this = this
            let num = 0
            let sendData = JSON.parse(JSON.stringify(jsonData))
            let url = "/" + getProjectName() + "/hosts/" + value.id + "/identification"
            let urlData = "?type=user"
            sendPutNoLoading(url + urlData, function (response) {
                _this.successFun(num, value)
                _this.identificationAjaxFun2(value, jsonData)
            }, function (error) {
                let status = "请检查SSH信息（包含 IP地址，端口号，用户名，密码）"
                let msg = jsonJudgeNotDefined(error, 'error.response.data.msg')
                _this.falseFun(num, value, status, msg)
            }, sendData)
        },
        identificationAjaxFun2: function (value, jsonData) {
            var _this = this
            let num = 1
            let sendData = JSON.parse(JSON.stringify(jsonData))
            let url = "/" + getProjectName() + "/hosts/" + value.id + "/identification"
            let urlData = "?type=storage"
            sendPutNoLoading(url + urlData, function (response) {
                _this.successFun(num, value)
                _this.identificationAjaxFun3(value, jsonData)
            }, function (error) {
                let disk_name = ''
                if (value.hdd != null) {
                    disk_name = "机械盘"
                } else if (value.ssd != null) {
                    disk_name = "固态盘"
                }
                let status = "请检查本地磁盘设备名（" + disk_name + " 未发现）"
                let msg = jsonJudgeNotDefined(error, 'error.response.data.msg')
                _this.falseFun(num, value, status, msg)
            }, sendData)
        },
        identificationAjaxFun3: function (value, jsonData) {
            var _this = this
            let num = 2
            let sendData = JSON.parse(JSON.stringify(jsonData))
            let url = "/" + getProjectName() + "/hosts/" + value.id + "/identification"
            let urlData = "?type=network"
            sendPutNoLoading(url + urlData, function (response) {
                _this.successFun(num, value)
            }, function (error) {
                let status = "填写的单元上限大于硬件上限"
                let msg = jsonJudgeNotDefined(error, 'error.response.data.msg')
                _this.falseFun(num, value, status, msg)
            }, sendData)
        },
        toggleExpandRow: function ({row}) {
            return row.status !== "loading"
        },
        childToggleExpandRow: function (row) {
            let name = "xTable" + row.num
            if (row.status !== "success")
                this.$refs[name].toggleRowExpand(row)
        },
        formClose: function () {
            parent.layer.close(identificationIndex);
        }
    }
})