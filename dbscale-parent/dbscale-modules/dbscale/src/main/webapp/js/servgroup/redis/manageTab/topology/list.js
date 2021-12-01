var redisGreenUrl = 'image:///dbscale/img/redis_icon_green.svg';
var redisYellowUrl = 'image:///dbscale/img/redis_icon_yellow.svg';
var redisRedUrl = 'image:///dbscale/img/redis_icon_red.svg';
var redisSentinelGreenUrl = 'image:///dbscale/img/redisSentinel_icon_green.svg';
var redisSentinelYellowUrl = 'image:///dbscale/img/redisSentinel_icon_yellow.svg';
var redisSentinelRedUrl = 'image:///dbscale/img/redisSentinel_icon_red.svg';
var redisUrl = redisGreenUrl
var redisSentinelUrl = redisSentinelGreenUrl
var yellowColor = "#ff9900"

var topologyListApp = new Vue({
    el: '#topologyList',
    data: {
        //redisSentinelTopology : echarts.init(document.getElementById('redisSentinelTopology')),
    },
    created: function () {
        this.topologyCreated()
    },
    methods: {
        topologyCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/serv_groups/redis/" + rowId, function (response) {
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
            var redisSentinelList = [];
            var redisSentinelCount = 0;
            var redisMaster = null;
            var redisMasterCount = 0;

            if (!XEUtils.isEmpty(data) && !XEUtils.isEmpty(data.servs)) {
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
                        if (servsV.type.code === "redis") {
                            var tempUnitTopologyRole = jsonJudgeNotDefined(tempUnit, "tempUnit.replication.role.code")
                            if (tempUnitTopologyRole === "master") {
                                tempUnit.rawType = "redisMaster"
                                redisMaster = tempUnit;
                                redisMasterCount++;
                            } else {
                                tempUnit.rawType = "redis"
                                shardList.push(tempUnit);
                                shardCount++;
                            }
                        } else if (servsV.type.code === "redis-sentinel") {
                            tempUnit.rawType = "redisSentinel"
                            redisSentinelList.push(tempUnit);
                            redisSentinelCount++;
                        }
                    });

                })
            } else {
                operationCompletion(_this, "获取节点信息失败", 'error')
                // alert("获取节点信息失败");
                layer.closeAll('loading');
                return
            }

//redis-sentinel开始
            var redisSentinelLabelSize = 14

            nodeData.push({
                name: 'redisSentinel001',
                x: 95,
                y: 115,
                symbol: redisSentinelUrl,
                symbolSize: 50
            }, {
                name: 'redisSentinel002',
                x: 45,
                y: 200,
                symbol: redisSentinelUrl,
                symbolSize: 50
            }, {
                name: 'redisSentinel003',
                x: 145,
                y: 200,
                symbol: redisSentinelUrl,
                symbolSize: 50
            })
            if (redisSentinelCount === 3) {
                var leaderNum
                for (var i = 0; i < redisSentinelCount; i++) {
                    switch (jsonJudgeNotDefined(redisSentinelList[i], "data.state.code")) {
                        case "passing":
                            redisSentinelUrl = redisSentinelGreenUrl
                            break;
                        case "critical":
                            redisSentinelUrl = redisSentinelRedUrl
                            break;
                        case "warning":
                        default:
                            redisSentinelUrl = redisSentinelYellowUrl
                    }
                    nodeData[i].symbol = redisSentinelUrl
                    nodeData[i].name = jsonJudgeNotDefined(redisSentinelList[i], "data.id")
                    nodeData[i].rawData = redisSentinelList[i]
                    nodeData[i].label = _this.renderLabel(redisSentinelList[i], "bottom", 5, redisSentinelLabelSize)

                    if (jsonJudgeNotDefined(redisSentinelList[i], "data.replication.role.code") === "leader") {
                        leaderNum = i
                    }

                    var sourceNum = i
                    var targetNum
                    if (sourceNum + 1 === redisSentinelCount) {
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
                    source: "redisSentinel001",
                    target: "redisSentinel002",
                    symbolSize: [1, 12],
                    lineStyle: {
                        normal: {
                            curveness: -0.4,
                            color: "green",
                            width: 3
                        }
                    }
                }, {
                    source: "redisSentinel002",
                    target: "redisSentinel003",
                    symbolSize: [1, 12],
                    lineStyle: {
                        normal: {
                            curveness: -0.4,
                            color: "green",
                            width: 3
                        }
                    }
                }, {
                    source: "redisSentinel003",
                    target: "redisSentinel001",
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

//redis开始
            var redisCreatedSize = 50
            var redisLabelSize = 14
            var redisBoxStartY = 0
            var redisBoxEndY = 310
            var redisAlltop = 100
            var redisCreatedX = 300
            var redisCreatedY = redisBoxStartY + redisAlltop + 85
            var redisStepX = 110
            var redisStepY = 60
            var redisAddX = 0
            var redisAddY = 0
            var redisRowNum = 5
            var redislength = shardCount
            var redisMasterTextArray = []

            if (!!redisMaster) {
                var relateIdRedisMasterName = jsonJudgeNotDefined(redisMaster, "data.relateId")
                var relateIdRedisMasterArray = redisMaster.relateId.split('-')
                relateIdRedisMasterName = "⭐序号:" + relateIdRedisMasterArray[relateIdRedisMasterArray.length - 1]
                redisMasterTextArray = [
                    relateIdRedisMasterName,
                    "IP:" + jsonJudgeNotDefined(redisMaster, "data.ip")
                ].join('\n')
                switch (jsonJudgeNotDefined(redisMaster, "redisMaster.state.code")) {
                    case "passing":
                        redisUrl = redisGreenUrl
                        break;
                    case "critical":
                        redisUrl = redisRedUrl
                        break;
                    case "warning":
                    default:
                        redisUrl = redisYellowUrl
                }
                nodeData.push({
                    name: jsonJudgeNotDefined(redisMaster, "redisMaster.id"),
                    x: 300 + (redisStepX) * 2,
                    y: redisBoxStartY + redisAlltop,
                    symbol: redisUrl,
                    symbolSize: 70,
                    rawData: redisMaster,
                    label: _this.renderLabel(redisMaster, "top", 2, redisLabelSize, redisMasterTextArray)
                })
            }

            for (var i = 0; i < redislength; i++) {
                switch (jsonJudgeNotDefined(shardList[i], "data.state.code")) {
                    case "passing":
                        redisUrl = redisGreenUrl
                        break;
                    case "critical":
                        redisUrl = redisRedUrl
                        break;
                    case "warning":
                    default:
                        redisUrl = redisYellowUrl
                }
                redisAddY = redisStepY * Math.floor(i / redisRowNum)
                var redisAddArray = []
                switch (redislength) {
                    case 1:
                        redisAddArray = [2]
                        redisAddX = redisStepX * redisAddArray[i]
                        break;
                    case 2:
                        redisAddArray = [1, 3]
                        redisAddX = redisStepX * redisAddArray[i]
                        break;
                    case 3:
                        redisAddArray = [1, 2, 3]
                        redisAddX = redisStepX * redisAddArray[i]
                        break;
                    case 4:
                        redisAddArray = [0, 1, 3, 4]
                        redisAddX = redisStepX * redisAddArray[i]
                        break;
                    default:
                        redisAddX = redisStepX * (i % redisRowNum)
                        break;
                }

                nodeData.push({
                    name: jsonJudgeNotDefined(shardList[i], "data.id"),
                    x: redisCreatedX + redisAddX,
                    y: redisCreatedY + redisAddY,
                    symbol: redisUrl,
                    symbolSize: redisCreatedSize,
                    symbolOffset: [0, 10],
                    rawData: shardList[i],
                    label: _this.renderLabel(shardList[i], "bottom", 5, redisLabelSize)
                })
                var runningState = jsonJudgeNotDefined(shardList[i], "data.replication.state")
                var runningStateCode = jsonJudgeNotDefined(shardList[i], "data.replication.state.code")
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
                        source: jsonJudgeNotDefined(redisMaster, "redisMaster.id"),
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
                coord: [210, redisBoxStartY]
            }, {
                itemStyle: {
                    normal: {
                        color: 'grey',
                        opacity: 0.2
                    }
                },
                coord: [max_X, redisBoxEndY + redisAddY]
            }]);

            var $redisTopology = $('#redisTopology')
            //$redisTopology.css('height', Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) - 10)
            var topologyHeight = $redisTopology.height();
            $redisTopology.css('height', topologyHeight + (redisAddY) * 2)

            //图形最大两点
            nodeData.push({
                name: 'topLeft',
                x: 0,
                y: 0,
                symbolSize: 0
            }, {
                name: 'bottomRight',
                x: max_X,
                y: 80 + redisBoxEndY + redisAddY,
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

            this.$nextTick(function () {
                var redisTopology = echarts.init(document.getElementById('redisTopology'))
                redisTopology.setOption(graphChartOption);
                redisTopology.on('dblclick', function (params) {
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
            relateId = relateIdArray[relateIdArray.length - 1]

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
            var roleDisplay = jsonJudgeNotDefined(rawData, "rawData.replication.role.display")
            var stateDisplay = jsonJudgeNotDefined(rawData, "rawData.state.display")

            var returnData = "单元名称:    " + name + "</br>"
                + "IP地址:    " + serverIp + "</br>"
                + "端口:    " + port + "</br>"
                + "版本:    " + version + "</br>"
                + "主机IP:    " + hostIP + "</br>"
                + "所属集群:    " + clusterName

            switch (rawData.rawType) {
                case "redisMaster":
                case "redis":
                    returnData += "</br>角色:    " + roleDisplay
                    break;
            }

            returnData += "</br>状态:    " + stateDisplay

            return returnData
        },
        returnList: function () {
            document.location.reload();
        }
    },
    mounted() {
    }
})