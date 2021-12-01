var executeImageUpdateIndex = parent.layer.getFrameIndex(window.name);
new Vue({
        el: '#executeImageUpdate',
        data: {
            stepsActive: 0,
        	initialization: 0,
        	endNum: 5,
        	siteList:[],
        	businessAreaList:[],
        	clusterList: [],
        	remoteStorageList: [],
        	ntpServerList: [],
        	formData: {
        		type: "",
                name: "",
                ownerName: "",
                businessSystemName : "",
                businessSubsystem: "",
                site: "",
                businessArea: "",
                version: "",
                arch: "",
                scale: "",
                diskType: "",
                dataSize: 10,
                logSize: 10,
                port: 3306,
                paramCfg: [],
                msg: ""
            }
        },
        created: function () {
            this.editCreated()
        },
        methods: {
        	editCreated: function () {
        		var _this = this
        		sendGet("/" + getProjectName() + "/order_groups/" + executeId,function (response) {
        			var data = response.data.data;
                    var ownerName = "";
                    if (data.owner !== null){
                        ownerName = ownerNameDispose(data.owner.name,data.owner.username)
                    }

                    var businessSystemName = "";
                    var businessSubsystem = "";
                    if(data.businessSubsystem !== null){
                        businessSubsystem = data.businessSubsystem.name
                        businessSystemName = data.businessSubsystem.businessSystem.name
                    }

                    var site = "";
                    if(data.site !== null){
                        site = data.site.name
                    }

                    var businessArea = "";
                    if(data.businessArea !== null){
                        businessArea = data.businessArea.name
                    }

                    XEUtils.arrayEach(data.orders, (v, i) => {
                        if (v.type.code === "mysql"){

                            var version = "";
                            if (v.version !== null){
                                version = v.version.major+'.'+v.version.minor+'.'+v.version.patch+'.'+v.version.build
                            }

                            var arch = "";
                            if (v.arch !== null){
                                arch = v.arch.name
                            }

                            var scale = "";
                            if (v.scale !== null){
                                scale = v.scale.name
                            }

                            var diskType = "";
                            if (v.diskType !== null){
                                diskType = v.diskType.display
                            }

                            _this.formData = {
                        		type: "CMHA",
                                name: data.name + '    (' + ownerName + ')',
                                ownerName: ownerName,
                                businessSystemName : businessSystemName,
                                businessSubsystem: businessSubsystem,
                                site: site,
                                businessArea: businessArea,
                                version: version,
                                arch: arch,
                                scale: scale,
                                diskType: diskType,
                                dataSize: v.dataSize,
                                logSize: v.logSize,
                                port: v.port,
                                paramCfg: XEUtils.toStringJSON(v.paramCfg),
                                msg: ""
                            }
                        }
                    })

                    layer.closeAll('loading')
                  },function (error) {
                	  operationCompletion(_this, error.response.data.msg, 'error')
                  },null)
        	},
        	formSubmit: function (formName) {//executeImageUpdate
                var _this = this
                commonConfirm(this, '执行确认', getHintText('执行')).then(() => {
                    _this.sendTableAjax(null)
                }).catch(() => {
                })
            },
            sendTableAjax: function (data) {
        	    var _this = this
                sendPut("/" + getProjectName() + "/order_groups/" + executeId + "/execute",function (response) {
                	  setTimeout(function () {
                          layer.closeAll('loading')
                          parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                          parent.listApp.goTourlDialogShow = true
                          _this.formClose()
                      },1000);
               },function (error) {
            	   operationCompletion(_this, error.response.data.msg, 'error')
                },data)
            },
            formClose: function () {
                parent.layer.close(executeImageUpdateIndex);
            },
            clusterIdChange: function (data) {
                this.initialization--;
                this.formData.clusterId = "";
                this.clusterListView();
            }
        }
    })