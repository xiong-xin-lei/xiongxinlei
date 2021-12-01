var listApp = new Vue({
    el: '#site',
    data: {
        datas: [],
        siteId: '',
        version: {},
        statusShow: function (status) {
            if (status) {
                return getProjectSvg(COLOR_ENABLED)
            } else if (!status) {
                return getProjectSvg(COLOR_DISABLED)
            }
        }
    },
    created: function () {
        this.versionListView()
        this.returnList()
    },
    methods: {
        versionListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sys/version", function (response) {
                var versionData = response.data.data
                var webValue = jsonJudgeNotDefined(versionData, 'versionData.webVersion.version') + " (" +
                    jsonJudgeNotDefined(versionData, 'versionData.webVersion.commitId') + ")"
                var cmValue = jsonJudgeNotDefined(versionData, 'versionData.cmVersion.version') + " (" +
                    jsonJudgeNotDefined(versionData, 'versionData.cmVersion.commitId') + ")"
                _this.version = {
                    web: webValue,
                    cm: cmValue
                }
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        returnList: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sites", function (response) {
                _this.datas = response.data.data;
                _this.initSitePage(response.data);
                layer.closeAll('loading');
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        initSitePage: function (datas) {
            var cityData = [];
            var seriesData = [];
            var _this = this
            XEUtils.arrayEach(datas.data, (site, index) => {
                seriesData.push({
                    name: site.name,
                    selected: true
                });
                cityData.push({
                    name: site.region.display,
                    value: 100
                });
            });

            var myChart = echarts.init(document.getElementById('updating-chart'));

            var geoCoordMap = {
                '上海': [121.48, 31.22],
                '北京': [116.46, 39.92]
            }
            var convertData = function (cityData) {
                var res = [];
                for (var i = 0; i < cityData.length; i++) {
                    var geoCoord = geoCoordMap[cityData[i].name];
                    if (geoCoord) {
                        res.push({
                            name: cityData[i].name,
                            value: geoCoord.concat(cityData[i].value)
                        });
                    }
                }
                return res;
            };
            option = {
                backgroundColor: '#404a59',
                title: {
                    text: '站点分布图',
                    subtext: '',
                    sublink: '',
                    left: 'center',
                    textStyle: {
                        color: '#fff'
                    }
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    y: 'bottom',
                    x: 'right',
                    data: ['pm2.5'],
                    textStyle: {
                        color: '#fff'
                    }
                },
                geo: {
                    map: 'china',
                    label: {
                        emphasis: {
                            show: false
                        }
                    },
                    roam: false,
                    itemStyle: {
                        normal: {
                            areaColor: '#323c48',
                            borderColor: '#111'
                        },
                        emphasis: {
                            areaColor: '#2a333d'
                        }
                    }
                },
                series: [{
                        name: '城市',
                        type: 'scatter',
                        coordinateSystem: 'geo',
                        data: convertData(cityData),
                        symbolSize: function (val) {
                            return val[2] / 10;
                        },
                        label: {
                            normal: {
                                formatter: '{b}',
                                position: 'right',
                                show: false
                            },
                            emphasis: {
                                show: true
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: '#ddb926'
                            }
                        }
                    },
                    {
                        name: 'Top 5',
                        type: 'effectScatter',
                        coordinateSystem: 'geo',
                        data: convertData(cityData.sort(function (a, b) {
                            return b.value - a.value;
                        }).slice(0, 6)),
                        symbolSize: function (val) {
                            return val[2] / 10;
                        },
                        showEffectOn: 'render',
                        rippleEffect: {
                            brushType: 'stroke'
                        },
                        hoverAnimation: true,
                        label: {
                            normal: {
                                formatter: '{b}',
                                position: 'right',
                                show: true
                            }
                        },
                        itemStyle: {
                            normal: {
                                color: '#f4e925',
                                shadowBlur: 10,
                                shadowColor: '#333'
                            }
                        },
                        zlevel: 1
                    }
                ]
            };
            myChart.setOption(option);
            myChart.on('click', function (params) { //点击事件
                /* console.log(params);
                 console.log(_this.datas);*/
            });
        },
        addAjaxFun: function () {
            var width = '1000px'
            var height = '670px'
            var title = "新增"
            var url = "/" + getProjectName() + "/app/sites/add"
            var urlData = ""
            layerOpenFun(width, height, title, url + urlData)
        },
        editAjaxfun: function (siteId) {
            var width = '420px'
            var height = '230px'
            var title = "编辑"
            var url = "/" + getProjectName() + "/app/sites/edit"
            var urlData = "?siteId=" + siteId
            layerOpenFun(width, height, title, url + urlData)
        },
        detailsSite: function (siteId) {
            var width = '760px'
            var height = '415px'
            var title = "站点详情"
            var url = "/" + getProjectName() + "/app/sites/details"
            var urlData = "?siteId=" + siteId
            var setting = {
                offset: "200px"
            }
            layerOpenFun(width, height, title, url + urlData, setting)
        },
        deleteSite: function (siteData) {
            var _this = this
            commonConfirm(this, '删除确认', getHintText('删除该站点')).then(() => {
                sendDelete("/" + getProjectName() + "/sites/" + siteData.id, function (response) {
                    layer.closeAll('loading')
                    operationCompletion(_this, "操作成功！")
                    _this.returnList()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, null)
            }).catch(() => {});
        },
        loginSite: function (site) {
            sessionStorage.setItem("siteId", site.id); //在跳转页面前在session中存储site的ID
            sessionStorage.setItem("storageMode", site.storageMode)
            localStorage.setItem("siteId", site.id);
            localStorage.setItem("storageMode", site.storageMode);
            window.location.href = "/" + getProjectName() + "/app/index";
        }
    }
});