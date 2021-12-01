var listApp = new Vue({
    el: '#List',
    data: {
        hostIsAble: false,
        serviceIsAble: false,
        HURatio: 1,
        Presentation: {
            distributableCnt: 0,
            undistributableCnt: 0,
            showMemCapacity: 0,
            showMemUsed: 0,
            showHSCapacity: 0,
            showHSUsed: 0,
            showCPUCapacity: 0,
            showCPUUsed: 0,
            showNetCapacity: 0,
            showNetUsed: 0,
            showUnitUsed: 0,
            showMaxUnitCnt: 0,
            memTag: '',
            hsTag: ''
        },
        statusShow: function (type, status) {
            var _this = this
            if (type === "host") {
                switch (status) {
                    case "normal":
                        return getProjectSvg(COLOR_PASSING) + "可用：" +
                            "<span style='font-weight:bold'>" +
                            _this.Presentation.distributableCnt + "</span>"
                    case "fail":
                        return getProjectSvg(COLOR_CRITICAL) + "不可用：" +
                            "<span style='font-weight:bold'>" +
                            _this.Presentation.undistributableCnt + "</span>"
                }
            }
        }
    },
    created: function () {
        this.resourcesViewList()
    },
    methods: {
        resourcesViewList: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sites/" + getSession("siteId") + "/resources", function (response) {
                _this.dataDispose(response.data.data)
                layer.closeAll('loading')
            }, function (error) {
            	console.log(error)
                operationCompletion(_this, error.response.data.msg, "error")
            }, null)
        },
        returnList: function () {
            document.location.reload();
        },
        dataDispose: function (data) {
            var _this = this
            var memTag
            var hsTag
            var resource = data.resource
            var servGroup = data.servGroup

            var showMemCapacity = resource.mem.capacity
            if (showMemCapacity > 1024) {
                showMemCapacity = (showMemCapacity / 1024).toFixed(2)
                memTag = "T"
            } else {
                showMemCapacity = showMemCapacity
                memTag = "G"
            }

            var showMemUsed = resource.mem.used
            if (showMemUsed > 1024) {
                showMemUsed = (showMemUsed / 1024).toFixed(2)
            } else {
                showMemUsed = showMemUsed
            }
            var showHSCapacity = resource.ssd.capacity + resource.hdd.capacity
            if (showHSCapacity > 1024) {
                showHSCapacity = (showHSCapacity / 1024).toFixed(2)
                hsTag = "T"
            } else {
                showHSCapacity = showHSCapacity
                hsTag = "G"
            }

            var showHSUsed = resource.ssd.used + resource.hdd.used
            if (showHSUsed > 1024) {
                showHSUsed = (showHSUsed / 1024).toFixed(2)
            } else {
                showHSUsed = showHSUsed
            }
            _this.Presentation = {
                distributableCnt: resource.host.distributableCnt,
                undistributableCnt: resource.host.undistributableCnt,
                showMemCapacity: showMemCapacity,
                showMemUsed: showMemUsed,
                showHSCapacity: showHSCapacity,
                showHSUsed: showHSUsed,
                showCPUCapacity: resource.cpu.capacity,
                showCPUUsed: resource.cpu.used,
                showNetCapacity: resource.network.capacity,
                showNetUsed: resource.network.used,
                showUnitUsed: resource.unit.used,
                showMaxUnitCnt: resource.unit.capacity,
                memTag: memTag,
                hsTag: hsTag
            }

            var CPURate = ''
            if (resource.cpu.capacity !== 0) {
                CPURate = ((resource.cpu.used / resource.cpu.capacity) * 100)
                    .toFixed(1) + "%"
            }
            var MemRate = ''
            if (resource.mem.capacity !== 0) {
                MemRate = ((resource.mem.used / resource.mem.capacity) * 100)
                    .toFixed(1) + "%"
            }
            var UnitRate = ''
            if (resource.unit.capacity !== 0) {
                UnitRate = ((resource.unit.used / resource.unit.capacity) * 100)
                    .toFixed(1) + "%"
            }
            var HSRate = ''
            if ((resource.ssd.capacity + resource.hdd.capacity) !== 0) {
                HSRate = ((resource.ssd.used + resource.hdd.used) / (resource.ssd.capacity + resource.hdd.capacity) * 100)
                    .toFixed(1) + "%"
            }
            var NetRate = ''
            if (resource.network.capacity !== 0) {
                NetRate = ((resource.network.used / resource.network.capacity) * 100)
                    .toFixed(1) + "%"
            }
            var HostUnitRate = ''
            if (resource.host.distributableCnt !== null && resource.host.undistributableCnt !== null && (resource.host.distributableCnt + resource.host.undistributableCnt) !== 0) {
                HostUnitRate = ((resource.host.distributableCnt / (resource.host.distributableCnt + resource.host.undistributableCnt)) * 100).toFixed(1) + "%"
            }
            
            var disposeData = {
                "HostSum": resource.host.criticalCnt + resource.host.passingCnt +
                    resource.host.warningCnt,
                "UnitSum": data.unit.criticalCnt + data.unit.passingCnt +
                    data.unit.unknownCnt,
                "MysqlSum": servGroup.mysql.criticalCnt + servGroup.mysql.passingCnt +
                    servGroup.mysql.warningCnt,
                "CmhaSum": servGroup.cmha.criticalCnt + servGroup.cmha.passingCnt +
                    servGroup.cmha.warningCnt,
                "RedisSum": servGroup.redis.criticalCnt + servGroup.redis.passingCnt +
                	servGroup.redis.warningCnt,
                "CPUUnUsed": resource.cpu.capacity - resource.cpu.used,
                "MemUnUsed": resource.mem.capacity - resource.mem.used,
                "HSUnUsed": (resource.ssd.capacity + resource.hdd.capacity) -
                    (resource.ssd.used + resource.hdd.used),
                "HSUsed": resource.ssd.used + resource.hdd.used,
                "HSCapacity": resource.ssd.capacity + resource.hdd.capacity,
                "UnitUnUsed": resource.unit.capacity - resource.unit.used,
                "NetUnused": resource.network.capacity - resource.network.used,
                "CPURate": CPURate,
                "MemRate": MemRate,
                "UnitRate": UnitRate,
                "HSRate": HSRate,
                "NetRate": NetRate,
                "HostUnitRate": HostUnitRate
            }
            var seriesData = Object.assign(data, disposeData)
            var nameServeData = []
            var hostnameServeData = []
            var sumServeData = []
            var hostSumServeData = []
            var passServeData = []
            var hostPassServeData = []
            var warnServeData = []
            var hostWarnServeData = []
            var criticalServeData = []
            var hostCriticalServeData = []
            var hostUnknowServeData = []

            hostnameServeData.push("单元")
            hostSumServeData.push(seriesData.UnitSum)
            hostPassServeData.push(seriesData.unit.passingCnt)
            hostWarnServeData.push(0)
            hostCriticalServeData.push(seriesData.unit.criticalCnt)
            hostUnknowServeData.push(seriesData.unit.unknownCnt)

            this.HURatio = seriesData.UnitSum / seriesData.HostSum

            if (seriesData.UnitSum === 0) {
                this.HURatio = 1
                hostnameServeData.push("主机")
                hostSumServeData.push(this.HURatio * seriesData.HostSum)
                hostPassServeData.push(this.HURatio * seriesData.resource.host.passingCnt)
                hostWarnServeData.push(this.HURatio * seriesData.resource.host.warningCnt)
                hostCriticalServeData.push(this.HURatio * seriesData.resource.host.criticalCnt)
                hostUnknowServeData.push(0)
            } else {
                hostnameServeData.push("主机")
                hostSumServeData.push(this.HURatio * seriesData.HostSum)
                hostPassServeData.push(this.HURatio * seriesData.resource.host.passingCnt)
                hostWarnServeData.push(this.HURatio * seriesData.resource.host.warningCnt)
                hostCriticalServeData.push(this.HURatio * seriesData.resource.host.criticalCnt)
                hostUnknowServeData.push(0)
            }
            if (seriesData.MysqlSum !== 0) {
                nameServeData.push("MySQL")
                sumServeData.push(seriesData.MysqlSum)
                passServeData.push(seriesData.servGroup.mysql.passingCnt)
                warnServeData.push(seriesData.servGroup.mysql.warningCnt)
                criticalServeData.push(seriesData.servGroup.mysql.criticalCnt)
            }
            if (seriesData.CmhaSum !== 0) {
                nameServeData.push("CMHA")
                sumServeData.push(seriesData.CmhaSum)
                passServeData.push(seriesData.servGroup.cmha.passingCnt)
                warnServeData.push(seriesData.servGroup.cmha.warningCnt)
                criticalServeData.push(seriesData.servGroup.cmha.criticalCnt)
            }
            if (seriesData.RedisSum !== 0) {
                nameServeData.push("Redis")
                sumServeData.push(seriesData.RedisSum)
                passServeData.push(seriesData.servGroup.redis.passingCnt)
                warnServeData.push(seriesData.servGroup.redis.warningCnt)
                criticalServeData.push(seriesData.servGroup.redis.criticalCnt)
            }
            var AstrictData = [{
                value: seriesData.resource.host.distributableCnt,
                name: '可用'
            }, {
                value: seriesData.resource.host.undistributableCnt,
                name: '不可用'
            }]
            var CPUData = [{
                value: seriesData.resource.cpu.used,
                name: '使用率'
            }, {
                value: seriesData.CPUUnUsed,
                name: '未使用率'
            }, ]
            var MemData = [{
                value: seriesData.resource.mem.used,
                name: '使用率'
            }, {
                value: seriesData.MemUnUsed,
                name: '未使用率'
            }, ]
            var UnitData = [{
                value: seriesData.resource.unit.used,
                name: '使用率'
            }, {
                value: seriesData.UnitUnUsed,
                name: '未使用率'
            }, ]
            var NetData = [{
                value: seriesData.resource.network.used,
                name: '使用率'
            }, {
                value: seriesData.NetUnused,
                name: '未使用率'
            }, ]
            var HSData = [{
                value: seriesData.HSUsed,
                name: '使用率'
            }, {
                value: seriesData.HSUnUsed,
                name: '未使用率'
            }, ]

            if (nameServeData.length === 0) {
                this.serviceIsAble = true
            }
            if (seriesData.resource.host.distributableCnt + seriesData.resource.host.undistributableCnt === 0) {
                this.hostIsAble = true
            }

            var _this = this
            var distributablehostShowOption = {
                tooltip: {
                    trigger: 'item',
                    formatter: function (params, ticket, callback) {
                        var showHtm = "";
                        var titleName = params.name
                        var Num = "数量：" + params.value
                        var Proportion = "占比：" + params.percent + "%"
                        showHtm += titleName + '<br/>' + Num + '<br/>' +
                            Proportion
                        if (params.value === 0) {
                            return ''
                        } else {
                            return showHtm;
                        }
                    }
                },
                graphic: {
                    type: "text",
                    left: "center",
                    top: "center",
                    style: {
                        text: seriesData.HostUnitRate,
                        textAlign: "center",
                        fill: "#000",
                        width: 30,
                        height: 30,
                        fontSize: 18
                    },
                    onclick: function () {
                        _this.pageJumps("resource", "hosts")
                    }
                },
                backgroundColor: '#fafbfd',
                label: {
                    normal: {
                        // 各分区的提示内容
                        // params: 即下面传入的data数组,通过自定义函数，展示你想要的内容和格式
                        formatter: function (params) {
                            return params.name + "\n\n" + params.percent + "%";
                        },
                        textStyle: { // 提示文字的样式
                            color: '#595959',
                            fontSize: 12
                        }
                    }
                },
                series: [{
                    type: 'pie',
                    radius: ['45%', '70%'],
                    center: ['50%', '50%'],
                    data: AstrictData,
                    stillShowZeroSum: false,
                    label: {
                        normal: {
                            position: 'inner',
                            show: false,
                            formatter: function (params) {
                                if (params.value === 0) {
                                    return ''
                                } else {
                                    return params.value
                                }
                            },
                            textStyle: {
                                fontSize: 18,
                                fontWeight: 'bolder',
                                color: "#ffffff"
                            }
                        }
                    },
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        },
                        normal: {
                            color: function (params) {
                                //自定义颜色
                                var colorList = ['#19be6b', '#f8453f'];
                                return colorList[params.dataIndex]
                            }
                        }
                    }
                }]
            };

            var hoststatusShowOption = {
                tooltip: {
                    trigger: 'item',
                    formatter: function (params, ticket, callback) {
                        var showHtm = "";
                        var titleName = params.seriesName
                        if (params.name === '主机') {
                            var Num = params.name + " : " + params.value / _this.HURatio
                        } else {
                            var Num = params.name + " : " + params.value
                        }
                        showHtm += titleName + '<br/>' + Num + '<br/>'
                        return showHtm;
                    }
                },
                legend: {
                    icon: 'circle',
                    itemWidth: 10,
                    itemHeight: 10,
                    bottom: 0,
                    data: ['正常', '警告', '异常', '未知']
                },
                backgroundColor: '#fafbfd',
                grid: {
                    left: '6%',
                    right: '12%',
                    bottom: '3%',
                    top: '3%',
                    containLabel: true
                },
                xAxis: {
                    type: 'value',
                    show: false,
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    splitLine: {
                        show: false
                    },
                },
                yAxis: {
                    type: 'category',
                    data: hostnameServeData,
                    triggerEvent: true,
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    splitLine: {
                        show: false
                    },
                    axisLabel: {
                        show: true,
                        textStyle: {
                            fontSize: 14,
                        },
                        formatter: function (value, param) {
                            if (value === "单元") {
                                return value;
                            }
                            if (value === "主机") {
                                return value;
                            }
                        },
                    }
                },
                series: [{
                    name: '总数',
                    type: 'bar',
                    silent: true,
                    barWidth: 16,
                    barGap: '-100%', // Make series be overlap
                    itemStyle: {
                        normal: {
                            color: '#dddddd',
                            label: {
                                show: true,
                                position: 'right',
                                distance: 10,
                                formatter: function (params) {
                                    if (params.value > 0) {
                                        if (params.name === '主机') {
                                            return params.value / _this.HURatio;
                                        } else {
                                            return params.value
                                        }
                                    } else {
                                        return 0;
                                    }
                                },
                                textStyle: {
                                    fontSize: 24,
                                    fontWeight: 'bolder',
                                    color: "#000000"
                                }
                            },
                        }
                    },
                    data: hostSumServeData
                }, {
                    name: '正常',
                    type: 'bar',
                    stack: '总数',
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                position: 'top',
                                distance: 1,
                                formatter: function (params) {
                                    if (params.value > 0) {
                                        if (params.name === '主机') {
                                            return params.value / _this.HURatio;
                                        } else {
                                            return params.value
                                        }
                                    } else {
                                        return '';
                                    }
                                },
                                textStyle: {
                                    fontSize: 12,
                                    color: "#000000"
                                }
                            },
                            color: '#19be6b'
                        }
                    },
                    z: 10,
                    barWidth: 16,
                    data: hostPassServeData
                }, {
                    name: '警告',
                    type: 'bar',
                    stack: '总数',
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                position: 'top',
                                distance: 1,
                                formatter: function (params) {
                                    if (params.value > 0) {
                                        if (params.name === '主机') {
                                            return params.value / _this.HURatio;
                                        } else {
                                            return params.value
                                        }
                                    } else {
                                        return '';
                                    }
                                },
                                textStyle: {
                                    fontSize: 12,
                                    color: "#000000"
                                }
                            },
                            color: '#ff9900'
                        }
                    },
                    z: 10,
                    barWidth: 16,
                    data: hostWarnServeData
                }, {
                    name: '异常',
                    type: 'bar',
                    stack: '总数',
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                position: 'top',
                                distance: 1,
                                formatter: function (params) {
                                    if (params.value > 0) {
                                        if (params.name === '主机') {
                                            return params.value / _this.HURatio;
                                        } else {
                                            return params.value
                                        }
                                    } else {
                                        return '';
                                    }
                                },
                                textStyle: {
                                    fontSize: 12,
                                    color: "#000000"
                                }
                            },
                            color: '#f8453f'
                        }
                    },
                    z: 10,
                    barWidth: 16,
                    data: hostCriticalServeData
                }, {
                    name: '未知',
                    type: 'bar',
                    stack: '总数',
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                position: 'top',
                                distance: 1,
                                formatter: function (params) {
                                    if (params.value > 0) {
                                        if (params.name === '主机') {
                                            return params.value / _this.HURatio;
                                        } else {
                                            return params.value
                                        }
                                    } else {
                                        return '';
                                    }
                                },
                                textStyle: {
                                    fontSize: 12,
                                    color: "#000000"
                                }
                            },
                            color: '#696bd8'
                        }
                    },
                    z: 10,
                    barWidth: 16,
                    data: hostUnknowServeData
                }]
            };

            var serviceOption = {
                tooltip: {},
                legend: {
                    icon: 'circle',
                    itemWidth: 10,
                    itemHeight: 10,
                    bottom: 0,
                    data: ['正常', '警告', '异常']
                },
                backgroundColor: '#fafbfd',
                grid: {
                    left: '6%',
                    right: '12%',
                    bottom: '3%',
                    top: '3%',
                    containLabel: true
                },
                xAxis: {
                    type: 'value',
                    show: false,
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    splitLine: {
                        show: false
                    },
                },
                yAxis: {
                    type: 'category',
                    data: nameServeData,
                    triggerEvent: true,
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    splitLine: {
                        show: false
                    },
                    axisLabel: {
                        show: true,
                        textStyle: {
                            fontSize: 14,
                        },
                        formatter: function (value, param) {
                            if (value === "MySQL") {
                                return ' ' + '{mysqlImgValue|}' + '   ' + value;
                            }
                            if (value === "CMHA") {
                                return ' ' + '{cmhaImgValue|}' + '    ' + value;
                            }
                            if (value === "Redis") {
                                return ' ' + '{redisImgValue|}' + '     ' + value;
                            }
                            if (value === "Nginx") {
                                return ' ' + '{nginxImgValue|}' + '     ' + value;
                            }
                        },
                        rich: {
                            mysqlImgValue: {
                                height: 25,
                                align: 'center',
                                backgroundColor: {
                                    image: 'img/mysql_icon_green.svg'
                                }
                            },
                            cmhaImgValue: {
	                            height: 25,
	                            align: 'center',
	                            backgroundColor: {
	                                image: 'img/cmha_icon_green.svg'
	                            }
	                        },
                            redisImgValue: {
                                height: 30,
                                align: 'center',
                                backgroundColor: {
                                    image: 'img/redis.svg'
                                }
                            },
                            nginxImgValue: {
                                height: 25,
                                align: 'center',
                                backgroundColor: {
                                    image: 'img/nginx.png'
                                }
                            }
                        }
                    }
                },
                series: [{
                    name: '服务总数',
                    type: 'bar',
                    silent: true,
                    barWidth: 16,
                    barGap: '-100%', // Make series be overlap
                    itemStyle: {
                        normal: {
                            color: '#dddddd',
                            label: {
                                show: true,
                                position: 'right',
                                distance: 10,
                                formatter: '{c}',
                                textStyle: {
                                    fontSize: 24,
                                    fontWeight: 'bolder',
                                    color: "#000000"
                                }
                            },
                        }
                    },
                    data: sumServeData
                }, {
                    name: '正常',
                    type: 'bar',
                    stack: '服务总数',
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                position: 'top',
                                distance: 1,
                                formatter: function (params) {
                                    if (params.value > 0) {
                                        return params.value;
                                    } else {
                                        return '';
                                    }
                                },
                                textStyle: {
                                    fontSize: 12,
                                    color: "#000000"
                                }
                            },
                            color: '#19be6b'
                        }
                    },
                    z: 10,
                    barWidth: 16,
                    data: passServeData
                }, {
                    name: '警告',
                    type: 'bar',
                    stack: '服务总数',
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                position: 'top',
                                distance: 1,
                                formatter: function (params) {
                                    if (params.value > 0) {
                                        return params.value;
                                    } else {
                                        return '';
                                    }
                                },
                                textStyle: {
                                    fontSize: 12,
                                    color: "#000000"
                                }
                            },
                            color: '#ff9900'
                        }
                    },
                    z: 10,
                    barWidth: 16,
                    data: warnServeData
                }, {
                    name: '异常',
                    type: 'bar',
                    stack: '服务总数',
                    itemStyle: {
                        normal: {
                            label: {
                                show: true,
                                position: 'top',
                                distance: 1,
                                formatter: function (params) {
                                    if (params.value > 0) {
                                        return params.value;
                                    } else {
                                        return '';
                                    }
                                },
                                textStyle: {
                                    fontSize: 12,
                                    color: "#000000"
                                }
                            },
                            color: '#f8453f'
                        }
                    },
                    z: 10,
                    barWidth: 16,
                    data: criticalServeData
                }]
            };

            var CPUUsedOption = {
                tooltip: {
                    trigger: 'item',
                    position: 'right',
                    formatter: function (params, ticket, callback) {
                        var showHtm = "";
                        var titleName = 'CPU'
                        var Used = params.name + ":" +
                            params.percent.toFixed(1)
                        var Num = params.value.toFixed(1)
                        if (params.name === '未使用率') {
                            showHtm += titleName + '<br/>' + '未使用：' + Num +
                                '<br/>' + Used + "%"
                        } else {
                            showHtm += titleName + '<br/>' + '已使用：' + Num +
                                '<br/>' + Used + "%"
                        }
                        return showHtm;
                    }
                },
                backgroundColor: '#fafbfd',
                graphic: {
                    type: "text",
                    left: "center",
                    top: "center",
                    silent: true,
                    style: {
                        text: seriesData.CPURate,
                        textAlign: "center",
                        fill: "#000",
                        width: 30,
                        height: 30,
                        fontSize: 14
                    }
                },
                label: {
                    normal: {
                        // 各分区的提示内容
                        // params: 即下面传入的data数组,通过自定义函数，展示你想要的内容和格式
                        formatter: function (params) {
                            return params.name + "\n\n" + params.percent + "%";
                        },
                        textStyle: { // 提示文字的样式
                            color: '#595959',
                            fontSize: 12
                        }
                    }
                },
                series: [{
                    name: 'CPU',
                    type: 'pie',
                    radius: ['50%', '70%'],
                    center: ['50%', '50%'],
                    data: CPUData,
                    stillShowZeroSum: false,
                    label: {
                        normal: {
                            position: 'inner',
                            show: false
                        }
                    },
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        },
                        normal: {
                            color: function (params) {
                                //自定义颜色
                                return colorListReturn(params)[params.dataIndex]
                            }
                        }
                    }
                }]
            };

            var memSizeUsedOption = {
                tooltip: {
                    trigger: 'item',
                    position: 'right',
                    formatter: function (params, ticket, callback) {
                        var showHtm = "";
                        var titleName = '内存'
                        var Used = params.name + ":" +
                            params.percent.toFixed(1)
                        var Num = params.value.toFixed(1)
                        if (params.name === '未使用率') {
                            showHtm += titleName + '<br/>' + '未使用：' + Num +
                                '<br/>' + Used + "%"
                        } else {
                            showHtm += titleName + '<br/>' + '已使用：' + Num +
                                '<br/>' + Used + "%"
                        }
                        return showHtm;
                    }
                },
                backgroundColor: '#fafbfd',
                graphic: {
                    type: "text",
                    left: "center",
                    top: "center",
                    silent: true,
                    style: {
                        text: seriesData.MemRate,
                        textAlign: "center",
                        fill: "#000",
                        width: 30,
                        height: 30,
                        fontSize: 14
                    }
                },
                label: {
                    normal: {
                        // 各分区的提示内容
                        // params: 即下面传入的data数组,通过自定义函数，展示你想要的内容和格式
                        formatter: function (params) {
                            return params.name + "\n\n" + params.percent + "%";
                        },
                        textStyle: { // 提示文字的样式
                            color: '#595959',
                            fontSize: 12
                        }
                    }
                },
                series: [{
                    name: '内存',
                    type: 'pie',
                    radius: ['50%', '70%'],
                    center: ['50%', '50%'],
                    data: MemData,
                    stillShowZeroSum: false,
                    label: {
                        normal: {
                            position: 'inner',
                            show: false
                        }
                    },
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        },
                        normal: {
                            color: function (params) {
                                //自定义颜色
                                return colorListReturn(params)[params.dataIndex]
                            }
                        }
                    }
                }]
            };

            var UnitUsedOption = {
                tooltip: {
                    trigger: 'item',
                    position: 'right',
                    formatter: function (params, ticket, callback) {
                        var showHtm = "";
                        var titleName = '单元'
                        var Used = params.name + ":" +
                            params.percent.toFixed(1)
                        var Num = params.value.toFixed(1)
                        if (params.name === '未使用率') {
                            showHtm += titleName + '<br/>' + '未使用：' + Num +
                                '<br/>' + Used + "%"
                        } else {
                            showHtm += titleName + '<br/>' + '已使用：' + Num +
                                '<br/>' + Used + "%"
                        }
                        return showHtm;
                    }
                },
                backgroundColor: '#fafbfd',
                graphic: {
                    type: "text",
                    left: "center",
                    top: "center",
                    silent: true,
                    style: {
                        text: seriesData.UnitRate,
                        textAlign: "center",
                        fill: "#000",
                        width: 30,
                        height: 30,
                        fontSize: 14
                    }
                },
                label: {
                    normal: {
                        // 各分区的提示内容
                        // params: 即下面传入的data数组,通过自定义函数，展示你想要的内容和格式
                        formatter: function (params) {
                            return params.name + "\n\n" + params.percent + "%";
                        },
                        textStyle: { // 提示文字的样式
                            color: '#595959',
                            fontSize: 12
                        }
                    }
                },
                series: [{
                    name: '单元',
                    type: 'pie',
                    radius: ['50%', '70%'],
                    center: ['50%', '50%'],
                    data: UnitData,
                    stillShowZeroSum: false,
                    label: {
                        normal: {
                            position: 'inner',
                            show: false
                        }
                    },
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        },
                        normal: {
                            color: function (params) {
                                //自定义颜色
                                return colorListReturn(params)[params.dataIndex]
                            }
                        }
                    }
                }]
            };

            var HSUsedOption = {
                tooltip: {
                    trigger: 'item',
                    position: 'right',
                    formatter: function (params, ticket, callback) {
                        var showHtm = "";
                        var titleName = '存储'
                        var Used = params.name + ":" +
                            params.percent.toFixed(1)
                        var Num = params.value.toFixed(1)
                        if (params.name === '未使用率') {
                            showHtm += titleName + '<br/>' + '未使用：' + Num +
                                '<br/>' + Used + "%"
                        } else {
                            showHtm += titleName + '<br/>' + '已使用：' + Num +
                                '<br/>' + Used + "%"
                        }
                        return showHtm;
                    }
                },
                backgroundColor: '#fafbfd',
                graphic: {
                    type: "text",
                    left: "center",
                    top: "center",
                    silent: true,
                    style: {
                        text: seriesData.HSRate,
                        textAlign: "center",
                        fill: "#000",
                        width: 30,
                        height: 30,
                        fontSize: 14
                    }
                },
                label: {
                    normal: {
                        // 各分区的提示内容
                        // params: 即下面传入的data数组,通过自定义函数，展示你想要的内容和格式
                        formatter: function (params) {
                            return params.name + "\n\n" + params.percent + "%";
                        },
                        textStyle: { // 提示文字的样式
                            color: '#595959',
                            fontSize: 12
                        }
                    }
                },
                series: [{
                    name: '存储',
                    type: 'pie',
                    radius: ['50%', '70%'],
                    center: ['50%', '50%'],
                    data: HSData,
                    stillShowZeroSum: false,
                    label: {
                        normal: {
                            position: 'inner',
                            show: false
                        }
                    },
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        },
                        normal: {
                            color: function (params) {
                                //自定义颜色
                                return colorListReturn(params)[params.dataIndex]
                            }
                        }
                    }
                }]
            };

            var networkUsedOption = {
                tooltip: {
                    trigger: 'item',
                    position: 'right',
                    formatter: function (params, ticket, callback) {
                        var showHtm = "";
                        var titleName = '网段'
                        var Used = params.name + ":" +
                            params.percent.toFixed(1)
                        var Num = params.value.toFixed(1)
                        if (params.name === '未使用率') {
                            showHtm += titleName + '<br/>' + '未使用：' + Num +
                                '<br/>' + Used + "%"
                        } else {
                            showHtm += titleName + '<br/>' + '已使用：' + Num +
                                '<br/>' + Used + "%"
                        }
                        return showHtm;
                    }
                },
                backgroundColor: '#fafbfd',
                graphic: {
                    type: "text",
                    left: "center",
                    top: "center",
                    silent: true,
                    style: {
                        text: seriesData.NetRate,
                        textAlign: "center",
                        fill: "#000",
                        width: 30,
                        height: 30,
                        fontSize: 14
                    }
                },
                label: {
                    normal: {
                        // 各分区的提示内容
                        // params: 即下面传入的data数组,通过自定义函数，展示你想要的内容和格式
                        formatter: function (params) {
                            return params.name + "\n\n" + params.percent + "%";
                        },
                        textStyle: { // 提示文字的样式
                            color: '#595959',
                            fontSize: 12
                        }
                    }
                },
                series: [{
                    name: '网段',
                    type: 'pie',
                    radius: ['50%', '70%'],
                    center: ['50%', '50%'],
                    data: NetData,
                    stillShowZeroSum: false,
                    label: {
                        normal: {
                            position: 'inner',
                            show: false
                        }
                    },
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        },
                        normal: {
                            color: function (params) {
                                //自定义颜色
                                return colorListReturn(params)[params.dataIndex]
                            }
                        }
                    }
                }]
            };

            this.$nextTick(function () {

                var hoststatus = echarts.init(document.getElementById('hoststatusShow'))
                hoststatus.setOption(hoststatusShowOption);

                var distributablehost = echarts.init(document.getElementById('distributablehostShow'))
                distributablehost.setOption(distributablehostShowOption);

                var service = echarts.init(document.getElementById('service'))
                service.setOption(serviceOption);

                var CPUUsed = echarts.init(document.getElementById('CPUUsed'))
                CPUUsed.setOption(CPUUsedOption);
                var memSizeUsed = echarts.init(document.getElementById('memSizeUsed'))
                memSizeUsed.setOption(memSizeUsedOption);
                var UnitUsed = echarts.init(document.getElementById('UnitUsed'))
                UnitUsed.setOption(UnitUsedOption);
                var HSUsed = echarts.init(document.getElementById('HSUsed'))
                HSUsed.setOption(HSUsedOption);
                var networkUsed = echarts.init(document.getElementById('networkUsed'))
                networkUsed.setOption(networkUsedOption);

                var _this = this
                var state = ''
                hoststatus.on('click', function (params) { //点击事件
                    var width = '1340px'
                    var height = '600px'
                    var state = ''
                    switch (params.seriesName) {
                        case '正常':
                            state = 'passing'
                            break;
                        case '警告':
                            state = 'warning'
                            break;
                        case '异常':
                            state = 'critical'
                            break;
                        case '未知':
                            state = 'unknown'
                            break;
                    }
                    if (params.componentType === "yAxis") {
                    	if (params.value == "主机") {
                    		_this.pageJumps("resource", "hosts")
                    	}else{
                    		var title = "单元列表"
                            var url = "/" + getProjectName() + "/app/home/unitState?status=" + state + "&type=hoststatusShow"
                            layerOpenFun(width, height, title, url)
                    	}
                    } else {
                    	if (params.name == "主机") {
                    		_this.pageJumps("resource", "hosts", params.seriesName)
                    	}else{
                    		 var title = "单元列表"
                             var url = "/" + getProjectName() + "/app/home/unitState?status=" + state + "&type=hoststatusShow"
                             layerOpenFun(width, height, title, url)
                    	}
                    	
                    }
                });
                distributablehost.on('click', function (params) { //点击事件
                    var searchName = ''
                    switch (params.name) {
                        case '可用':
                        	searchName = '可分配'
                            break;
                        case '不可用':
                        	searchName = '不可分配'
                            break;
                    }
                    _this.pageJumps("resource", "hosts", searchName)
                });
                service.on('click', function (params) { //点击事件
                    var state = ''
                    switch (params.seriesName) {
                        case '正常':
                            state = 'passing'
                            break;
                        case '警告':
                            state = 'warning'
                            break;
                        case '异常':
                            state = 'critical'
                            break;
                    }
                    if (params.componentType === "yAxis") {
                        _this.pageJumps('servgroup', params.value)
                    } else {
                        _this.pageJumps('servgroup', params.name, params.seriesName)
                    }
                });
            })
        },
        pageJumps: function (groupType, type, searchName) {
            if (type === "MySQL") {
                type = "mysqls"
            } else if (type === "CMHA") {
            	type = "cmhas" 
            } else if (type === "Redis") {
                type = "redis"
            } 
            if (XEUtils.isEmpty(searchName))
                searchName = ''
            var json = {}
            var menuSkip = {}
            var urlName = ""
            var code = "/app/" + groupType + "/" + type
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
                        if (groupType === "servgroup") {
                            urlName = subMenuList.name + "服务"
                        } else {
                            urlName = subMenuList.name
                        }
                        menuId = subMenuList.id
                    }
                })
            })
            var url = code + "?id=" + menuId + "&menu=" + encodeURIComponent(JSON.stringify(menuSkip), "utf-8") + "&searchKeyWord=" + searchName + "&t=" + (new Date()).getTime()
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
    }
})