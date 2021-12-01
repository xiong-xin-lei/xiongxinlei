var mysqlGreenUrl = 'image:///dbscale/img/mysql_icon_green.svg';
var mysqlYellowUrl = 'image:///dbscale/img/mysql_icon_yellow.svg';
var mysqlRedUrl = 'image:///dbscale/img/mysql_icon_red.svg';
var proxysqlGreenUrl = 'image:///dbscale/img/proxysql_icon_green.svg';
var proxysqlRedUrl = 'image:///dbscale/img/proxysql_icon_red.svg';
var proxysqlYellowUrl = 'image:///dbscale/img/proxysql_icon_yellow.svg';
var cmhaGreenUrl = 'image:///dbscale/img/cmha_icon_green.svg';
var cmhaYellowUrl = 'image:///dbscale/img/cmha_icon_yellow.svg';
var cmhaRedUrl = 'image:///dbscale/img/cmha_icon_red.svg';
var mysqlUrl = mysqlGreenUrl
var proxysqlUrl = proxysqlGreenUrl
var cmhaUrl = cmhaGreenUrl
var yellowColor = "#ff9900"

var topologyListApp = new Vue({
    el: '#topologyList',
    data: {
        //cmhaTopology : echarts.init(document.getElementById('cmhaTopology')),
    },
    created: function () {
        this.topologyCreated()
    },
    methods: {
        topologyCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/serv_groups/mysql/" + rowId, function (response) {
                var responseData = response.data.data;
                _this.dataDispose(responseData)
            }, function (error) {
                console.log(error)
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        dataDispose: function (data) {
            var _this = this;
            var nodeData = [];
            var markAreaData = [];
            var links = [];
            var shardList = [];
            var shardCount = 0;
            var proxyList = [];
            var proxyCount = 0;
            var cmhaList = [];
            var cmhaCount = 0;
            var mysqlMaster = null;
            var mysqlMasterCount = 0;
            var topologyData = {}

            if (!XEUtils.isEmpty(data) && !XEUtils.isEmpty(data.servs)) {
                XEUtils.arrayEach(data.servs, (servsV, servsI) => {
                    if (servsV.type.code === "cmha")
                        topologyData = servsV.topology
                })
                XEUtils.arrayEach(data.servs, (servsV, servsI) => {
                    /*var tempServ = {};
                    tempServ.id = jsonJudgeNotDefined(servsV,"servsV.id");
                    tempServ.type = servsV.type;
                    tempServ.relateId = servsV.relateId;
                    tempServ.arch = servsV.arch;
                    tempServ.state = servsV.state;
                    tempServ.units = [];*/
                    XEUtils.arrayEach(servsV.units, (unitV, unitI) => {
                        var tempUnit = unitV;
                        var softwareVersion = "";
                        if (!!jsonJudgeNotDefined(unitV, "unitV.version")) {
                            if (!!jsonJudgeNotDefined(unitV, "unitV.version.major"))
                                softwareVersion = unitV.version.major + "." + unitV.version.minor + "." + unitV.version.patch + "." + unitV.version.build;
                        }
                        tempUnit.softwareVersion = softwareVersion
                        //tempServ.units.push(tempUnit);
                        if (servsV.type.code === "mysql") {
                            tempUnit.topology =  XEUtils.isEmpty(topologyData) ? {} : topologyData[tempUnit.relateId]
                            var tempUnitTopologyRole = jsonJudgeNotDefined(tempUnit, "tempUnit.topology.role.code")
                            if (tempUnitTopologyRole === "master") {
                                tempUnit.rawType = "mysqlMaster"
                                mysqlMaster = tempUnit;
                                mysqlMasterCount++;
                            } else {
                                tempUnit.rawType = "mysql"
                                shardList.push(tempUnit);
                                shardCount++;
                            }
                        } else if (servsV.type.code === "proxysql") {
                            tempUnit.rawType = "proxysql"
                            proxyList.push(tempUnit);
                            proxyCount++;
                        } else if (servsV.type.code === "cmha") {
                            tempUnit.rawType = "cmha"
                            cmhaList.push(tempUnit);
                            cmhaCount++;
                        }
                    });

                })
            } else {
                operationCompletion(_this, "获取节点信息失败", 'error')
                // alert("获取节点信息失败");
                layer.closeAll('loading');
                return
            }

//cmha开始
            var cmhaLabelSize = 14

            nodeData.push({
                name: 'cmha001',
                x: 95,
                y: 115,
                symbol: cmhaUrl,
                symbolSize: 50
            }, {
                name: 'cmha002',
                x: 45,
                y: 200,
                symbol: cmhaUrl,
                symbolSize: 50
            }, {
                name: 'cmha003',
                x: 145,
                y: 200,
                symbol: cmhaUrl,
                symbolSize: 50
            })
            if (cmhaCount === 3) {
                var leaderNum
                for (var i = 0; i < cmhaCount; i++) {
                    switch (jsonJudgeNotDefined(cmhaList[i], "data.state.code")) {
                        case "passing":
                            cmhaUrl = cmhaGreenUrl
                            break;
                        case "critical":
                            cmhaUrl = cmhaRedUrl
                            break;
                        case "warning":
                        default:
                            cmhaUrl = cmhaYellowUrl
                    }
                    nodeData[i].symbol = cmhaUrl
                    nodeData[i].name = jsonJudgeNotDefined(cmhaList[i], "data.id")
                    nodeData[i].rawData = cmhaList[i]
                    nodeData[i].label = _this.renderLabel(cmhaList[i], "bottom", 5, cmhaLabelSize)

                    if (jsonJudgeNotDefined(cmhaList[i], "data.role.code") === "leader") {
                        leaderNum = i
                    }

                    var sourceNum = i
                    var targetNum
                    if (sourceNum + 1 === cmhaCount) {
                        targetNum = 0
                    } else {
                        targetNum = sourceNum + 1
                    }

                    links.push({
                        source: sourceNum,
                        target: targetNum,
                        symbolSize: [1, 12],
                        lineStyle: {
                            normal: {
                                curveness: -0.4,
                                color: "green",
                                width: 3
                            }
                        }
                    });
                }

                var tempJson;
                var label;
                if (leaderNum !== undefined) {
                    tempJson = JSON.parse(JSON.stringify(nodeData[0]))
                    nodeData[0].symbol = JSON.parse(JSON.stringify(nodeData[leaderNum])).symbol
                    nodeData[0].name = JSON.parse(JSON.stringify(nodeData[leaderNum])).name
                    nodeData[0].rawData = JSON.parse(JSON.stringify(nodeData[leaderNum])).rawData
                    nodeData[0].label = JSON.parse(JSON.stringify(nodeData[leaderNum])).label
                    nodeData[leaderNum].symbol = JSON.parse(JSON.stringify(tempJson)).symbol
                    nodeData[leaderNum].name = JSON.parse(JSON.stringify(tempJson)).name
                    nodeData[leaderNum].rawData = JSON.parse(JSON.stringify(tempJson)).rawData
                    nodeData[leaderNum].label = JSON.parse(JSON.stringify(tempJson)).label

                    label = nodeData[0].label
                    label.normal.formatter = "⭐" + label.normal.formatter
                    label.normal.position = "top"
                    label.normal.distance = 2
                    nodeData[0].label = label
                } else {
                    label = nodeData[0].label
                    label.normal.position = "top"
                    label.normal.distance = 2
                    nodeData[0].label = label
                }


            } else {
                links.push({
                    source: "cmha001",
                    target: "cmha002",
                    symbolSize: [1, 12],
                    lineStyle: {
                        normal: {
                            curveness: -0.4,
                            color: "green",
                            width: 3
                        }
                    }
                }, {
                    source: "cmha002",
                    target: "cmha003",
                    symbolSize: [1, 12],
                    lineStyle: {
                        normal: {
                            curveness: -0.4,
                            color: "green",
                            width: 3
                        }
                    }
                }, {
                    source: "cmha003",
                    target: "cmha001",
                    symbolSize: [1, 12],
                    lineStyle: {
                        normal: {
                            curveness: -0.4,
                            color: "green",
                            width: 3
                        }
                    }
                });
            }

            markAreaData.push([{
                name: "高可用",
                itemStyle: {
                    normal: {
                        color: 'grey',
                        opacity: 0.2
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: "insideTopLeft", // markArea中文字（name）位置
                        color: "black", // markArea中文字（name）颜色
                        fontWeight: "bolder",
                        align: 'left',
                        opacity: 1.0,
                        fontSize: "20"
                    }
                },
                coord: [0, 0]
            }, {
                itemStyle: {
                    normal: {
                        color: 'grey',
                        opacity: 0.2
                    }
                },
                coord: [190, 310]
            }]);

            var max_X = 810

//proxysql开始
            var proxysqlCreatedSize = 50
            var proxysqlLabelSize = 14
            var proxysqlCreatedX = 300
            var proxysqlCreatedY = 50
            var proxysqlBoxStartY = 0
            var proxysqlBoxEndY = 80
            var proxysqlStepX = 110
            var proxysqlStepY = 60
            var proxysqlAddX = 0
            var proxysqlAddY = 0
            var proxysqlRowNum = 5
            var proxysqllength = proxyCount

            for (var i = 0; i < proxysqllength; i++) {
                switch (jsonJudgeNotDefined(proxyList[i], "data.state.code")) {
                    case "passing":
                        proxysqlUrl = proxysqlGreenUrl
                        break;
                    case "critical":
                        proxysqlUrl = proxysqlRedUrl
                        break;
                    case "warning":
                    default:
                        proxysqlUrl = proxysqlYellowUrl
                }
                var relateIdProxysqlName = jsonJudgeNotDefined(proxyList[i], "data.relateId")
                var relateIdProxysqlArray = relateIdProxysqlName.split('-')
                if (relateIdProxysqlArray.length === 4) {
                    //relateIdProxysqlName = relateIdProxysqlArray[2] + "-" + relateIdProxysqlArray[3]
                    relateIdProxysqlName = "序号:" + relateIdProxysqlArray[3]
                }
                proxysqlAddX = proxysqlStepX * (i % proxysqlRowNum)
                proxysqlAddY = proxysqlStepY * Math.floor(i / proxysqlRowNum)
                var proxysqlAddArray = [2]
                switch (proxysqllength) {
                    case 1:
                        proxysqlAddArray = [2]
                        proxysqlAddX = proxysqlStepX * proxysqlAddArray[i]
                        break;
                    case 2:
                        proxysqlAddArray = [1, 3]
                        proxysqlAddX = proxysqlStepX * proxysqlAddArray[i]
                        break;
                    case 3:
                        proxysqlAddArray = [1, 2, 3]
                        proxysqlAddX = proxysqlStepX * proxysqlAddArray[i]
                        break;
                    case 4:
                        proxysqlAddArray = [0, 1, 3, 4]
                        proxysqlAddX = proxysqlStepX * proxysqlAddArray[i]
                        break;
                    default:
                        proxysqlAddX = proxysqlStepX * (i % proxysqlRowNum)
                        break;
                }
                nodeData.push({
                    name: jsonJudgeNotDefined(proxyList[i], "data.id"),
                    x: proxysqlCreatedX + proxysqlAddX,
                    y: proxysqlCreatedY + proxysqlAddY,
                    symbol: proxysqlUrl,
                    symbolSize: proxysqlCreatedSize,
                    rawData: proxyList[i],
                    label: _this.renderLabel(proxyList[i], "top", 6, proxysqlLabelSize)
                })
            }

            markAreaData.push([{
                name: "代理",
                itemStyle: {
                    normal: {
                        color: 'grey',
                        opacity: 0.2
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: "insideTopLeft", // markArea中文字（name）位置
                        color: "black", // markArea中文字（name）颜色
                        fontWeight: "bolder",
                        align: 'left',
                        opacity: 1.0,
                        fontSize: "20"
                    }
                },
                coord: [210, proxysqlBoxStartY]
            }, {
                itemStyle: {
                    normal: {
                        color: 'grey',
                        opacity: 0.2
                    }
                },
                coord: [max_X, proxysqlBoxEndY + proxysqlAddY]
            }]);

//mysql开始
            var mysqlCreatedSize = proxysqlCreatedSize
            var mysqlLabelSize = proxysqlLabelSize
            var mysqlBoxStartY = proxysqlBoxEndY + proxysqlAddY + 40
            var mysqlBoxEndY = mysqlBoxStartY + 190
            var mysqlCreatedX = proxysqlCreatedX
            var mysqlCreatedY = mysqlBoxStartY + 140
            var mysqlStepX = proxysqlStepX
            var mysqlStepY = proxysqlStepY
            var mysqlAddX = 0
            var mysqlAddY = 0
            var mysqlRowNum = proxysqlRowNum
            var mysqllength = shardCount
            var mysqlMasterTextArray = []

            var proxysqlBoxPointToMysqlBoxColor
            var mysqlMasterRunningStateCode = jsonJudgeNotDefined(mysqlMaster, "mysqlMaster.topology.runningState.code")
            if (mysqlMasterCount === 1 && mysqlMasterRunningStateCode === "passing") {
                proxysqlBoxPointToMysqlBoxColor = "green"
            } else if (mysqlMasterCount === 0) {
                proxysqlBoxPointToMysqlBoxColor = "red"
            } else {
                proxysqlBoxPointToMysqlBoxColor = yellowColor
            }

            if (!!mysqlMaster) {
                var relateIdMysqlMasterName = jsonJudgeNotDefined(mysqlMaster, "data.relateId")
                var relateIdMysqlMasterArray = mysqlMaster.relateId.split('-')
                if (relateIdMysqlMasterArray.length === 4) {
                    //relateIdMysqlMasterName = relateIdMysqlMasterArray[2] + "-" + relateIdMysqlMasterArray[3]
                    relateIdMysqlMasterName = "⭐序号:" + relateIdMysqlMasterArray[3]
                }
                mysqlMasterTextArray = [
                    relateIdMysqlMasterName,
                    "IP:" + jsonJudgeNotDefined(mysqlMaster, "data.ip")
                ].join('\n')
                switch (jsonJudgeNotDefined(mysqlMaster, "mysqlMaster.state.code")) {
                    case "passing":
                        mysqlUrl = mysqlGreenUrl
                        break;
                    case "critical":
                        mysqlUrl = mysqlRedUrl
                        break;
                    case "warning":
                    default:
                        mysqlUrl = mysqlYellowUrl
                }
                nodeData.push({
                    name: jsonJudgeNotDefined(mysqlMaster, "mysqlMaster.id"),
                    x: proxysqlCreatedX + (mysqlStepX) * 2,
                    y: mysqlBoxStartY + 55,
                    symbol: mysqlUrl,
                    symbolSize: 70,
                    rawData: mysqlMaster,
                    label: _this.renderLabel(mysqlMaster, "top", 2, mysqlLabelSize, mysqlMasterTextArray)
                })
            }

            for (var i = 0; i < mysqllength; i++) {
                switch (jsonJudgeNotDefined(shardList[i], "data.state.code")) {
                    case "passing":
                        mysqlUrl = mysqlGreenUrl
                        break;
                    case "critical":
                        mysqlUrl = mysqlRedUrl
                        break;
                    case "warning":
                    default:
                        mysqlUrl = mysqlYellowUrl
                }
                mysqlAddY = mysqlStepY * Math.floor(i / mysqlRowNum)
                var mysqlAddArray = []
                switch (mysqllength) {
                    case 1:
                        mysqlAddArray = [2]
                        mysqlAddX = mysqlStepX * mysqlAddArray[i]
                        break;
                    case 2:
                        mysqlAddArray = [1, 3]
                        mysqlAddX = mysqlStepX * mysqlAddArray[i]
                        break;
                    case 3:
                        mysqlAddArray = [1, 2, 3]
                        mysqlAddX = mysqlStepX * mysqlAddArray[i]
                        break;
                    case 4:
                        mysqlAddArray = [0, 1, 3, 4]
                        mysqlAddX = mysqlStepX * mysqlAddArray[i]
                        break;
                    default:
                        mysqlAddX = mysqlStepX * (i % mysqlRowNum)
                        break;
                }

                nodeData.push({
                    name: jsonJudgeNotDefined(shardList[i], "data.id"),
                    x: mysqlCreatedX + mysqlAddX,
                    y: mysqlCreatedY + mysqlAddY,
                    symbol: mysqlUrl,
                    symbolSize: mysqlCreatedSize,
                    symbolOffset: [10, 10],
                    rawData: shardList[i],
                    label: _this.renderLabel(shardList[i], "bottom", 5, mysqlLabelSize)
                })
                var runningState = jsonJudgeNotDefined(shardList[i], "data.topology.runningState")
                var runningStateCode = jsonJudgeNotDefined(shardList[i], "data.topology.runningState.code")
                if (!!runningState || !!runningStateCode) {
                    var runningStateColor;
                    switch (runningStateCode) {
                        case "passing":
                            runningStateColor = "green"
                            break;
                        case "critical":
                            runningStateColor = "red"
                            break;
                        case "warning":
                            runningStateColor = yellowColor
                            break;
                        case "unknown":
                            runningStateColor = COLOR_UNKNOWN
                            break;
                        default:
                            runningStateColor = COLOR_UNKNOWN
                            break;
                    }

                    links.push({
                        source: jsonJudgeNotDefined(mysqlMaster, "mysqlMaster.id"),
                        target: jsonJudgeNotDefined(shardList[i], "data.id"),
                        lineStyle: {
                            normal: {
                                color: runningStateColor,
                                width: 3
                            }
                        },
                        symbolSize: [1, 12]
                    });
                }
            }

            markAreaData.push([{
                name: "数据库",
                itemStyle: {
                    normal: {
                        color: 'grey',
                        opacity: 0.2
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: "insideTopLeft", // markArea中文字（name）位置
                        color: "black", // markArea中文字（name）颜色
                        fontWeight: "bolder",
                        align: 'left',
                        opacity: 1.0,
                        fontSize: "20"
                    }
                },
                coord: [210, mysqlBoxStartY]
            }, {
                itemStyle: {
                    normal: {
                        color: 'grey',
                        opacity: 0.2
                    }
                },
                coord: [max_X, mysqlBoxEndY + mysqlAddY]
            }]);

            nodeData.push({
                name: "proxysqlBottom",
                category: 0,
                x: proxysqlCreatedX + (mysqlStepX) * 2,
                y: proxysqlBoxEndY + proxysqlAddY,
                symbolSize: 0
            });
            nodeData.push({
                name: "mysqlTop",
                category: 0,
                x: proxysqlCreatedX + (mysqlStepX) * 2,
                y: mysqlBoxStartY,
                symbolSize: 0
            });

            links.push({
                source: "proxysqlBottom",
                target: "mysqlTop",
                lineStyle: {
                    normal: {
                        color: proxysqlBoxPointToMysqlBoxColor,
                        width: 3
                    }
                },
                symbolSize: [1, 16]
            });

            var $cmhaTopology = $('#cmhaTopology')
            //$cmhaTopology.css('height', Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) - 10)
            var topologyHeight = $cmhaTopology.height();
            $cmhaTopology.css('height', topologyHeight + (proxysqlAddY + mysqlAddY) * 2)

            //图形最大两点
            nodeData.push({
                name: 'topLeft',
                x: 0,
                y: 0,
                symbolSize: 0
            }, {
                name: 'bottomRight',
                x: max_X,
                y: proxysqlBoxEndY + proxysqlAddY + mysqlBoxEndY + mysqlAddY,
                symbolSize: 0
            })

            var graphChartOption = {
                title: {
                    show: false
                },
                tooltip: {},
                series: [{
                    type: 'graph',
                    symbolSize: 50,
                    roam: false,
                    edgeSymbol: ['circle', 'arrow'],
                    edgeSymbolSize: [4, 10],
                    edgeLabel: {
                        normal: {
                            textStyle: {
                                fontSize: 20
                            }
                        }
                    },
                    data: nodeData,
                    links: links,
                    width: 1350,
                    /*height: topologyHeight,*/
                    top: "6%",
                    left: "2%",
                    /*top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0,*/
                    lineStyle: {
                        normal: {
                            opacity: 0.9,
                            width: 2,
                            curveness: 0
                        }
                    },
                    markArea: {
                        silent: true,
                        data: markAreaData
                    },
                    tooltip: {
                        formatter: _this.formatterFunc
                    }
                }]
            };

            /*var linksCopy = [];
            for (var i = 0; i < links.length; i++) {
                if (links[i].source != "null" && links[i].target == "null") {
                    linksCopy.push(links[i]);
                }
            }
            links = linksCopy;*/

            this.$nextTick(function () {
                var cmhaTopology = echarts.init(document.getElementById('cmhaTopology'))
                cmhaTopology.setOption(graphChartOption);
                //cmhaTopology.resize({height: proxyHeight + proxyDistance + shardHeightAll + graphAddHeight});
                cmhaTopology.on('dblclick', function (params) {
                    var type = jsonJudgeNotDefined(params, "params.data.rawData.type.code");
                    var name = jsonJudgeNotDefined(params, "params.data.name");
                    var unitUrl = ""
                    if (type !== '') {
                        parent.manageApp.activeName = parent.manageApp.btnReplaceList.unit
                        parent.manageApp.topoDblclickType = type
                        parent.manageApp.topoDblclickName = name
                    }
                });
            })
            //console.log(graphChartOption)

            layer.closeAll('loading');
        },
        renderLabel: function (rawData, position, distance, fontSize, formatter) {
            var relateId = jsonJudgeNotDefined(rawData, "data.relateId")
            var relateIdArray = relateId.split('-')
            if (relateIdArray.length === 4) {
                relateId = relateIdArray[3]
            }

            var formatterText = ""
            if (formatter === undefined) {
                formatterText = [
                    '{span|序号:' + relateId + '}',
                    '{span|IP:' + jsonJudgeNotDefined(rawData, "data.ip") + '}'
                ].join('\n')
            } else {
                formatterText = formatter
            }
            return {
                normal: {
                    show: true,
                    position: position,
                    distance: distance,
                    color: "#666",
                    align: 'left',
                    lineHeight: fontSize + 5,
                    fontSize: fontSize,
                    offset: [-60, 0],
                    rich: {
                        "span": {
                            color: "#666",
                            fontSize: fontSize
                        }
                    },
                    formatter: formatterText
                }
            };
        },
        formatterFunc: function (params, ticket, callback) {
            if (params.seriesType === "graph" && params.componentType === "series" && params.componentSubType === "graph" && params.dataType === "node") {
                return this.renderTooltip(params.data.rawData);
            }
            return "";
        },
        renderTooltip: function (rawData) {
            var name = jsonJudgeNotDefined(rawData, "rawData.relateId")
            var serverIp = jsonJudgeNotDefined(rawData, "rawData.ip")
            var port = jsonJudgeNotDefined(rawData, "rawData.port")
            var version = jsonJudgeNotDefined(rawData, "rawData.softwareVersion")
            var hostIP = jsonJudgeNotDefined(rawData, "rawData.host.ip")
            var clusterName = jsonJudgeNotDefined(rawData, "rawData.host.cluster.name")
            var roleDisplay = jsonJudgeNotDefined(rawData, "rawData.topology.role.display")
            var stateDisplay = jsonJudgeNotDefined(rawData, "rawData.state.display")
            var roleIoRunningDisplay = jsonJudgeNotDefined(rawData, "rawData.topology.ioThread.display");
            var roleSqlRunningDisplay = jsonJudgeNotDefined(rawData, "rawData.topology.sqlThread.display");

            var returnData = "单元名称:    " + name + "</br>"
                + "IP地址:    " + serverIp + "</br>"
                + "端口:    " + port + "</br>"
                + "版本:    " + version + "</br>"
                + "主机IP:    " + hostIP + "</br>"
                + "所属集群:    " + clusterName

            switch (rawData.rawType) {
                case "mysqlMaster":
                case "mysql":
                    returnData += "</br>角色:    " + roleDisplay
                    break;
            }

            returnData += "</br>状态:    " + stateDisplay

            switch (rawData.rawType) {
                case "mysql":
                    returnData += "</br>复制Io状态:    " + roleIoRunningDisplay + "</br>"
                        + "复制Sql状态:    " + roleSqlRunningDisplay
                    break;
            }

            return returnData
        },
        returnList: function () {
            document.location.reload();
        }
    },
    mounted() {
    }
})