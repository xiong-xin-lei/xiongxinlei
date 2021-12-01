var setupIndex = parent.layer.getFrameIndex(window.name);
var selectStorage = (rule, value, callback) => {
    var hddPaths = addApp.formData.hddPaths.length !== 0;
    var ssdPaths = addApp.formData.ssdPaths.length !== 0;
    var remoteStorageId = addApp.formData.remoteStorageId.length !== 0;
    if (hddPaths || ssdPaths) {
        callback();
    } else {
        if (!remoteStorageId) {
            callback(new Error('机械盘、固态盘、外置存储至少输一项'));
        }
    }
};
var addApp = new Vue({
    el: '#add',
    data: {
        initialization: 0,
        endNum: [2, 2, 2],
        stepsActive: 0,
        maxUnitCntMax: 100,
        siteList: [],
        businessAreaList: [],
        clusterList: [],
        remoteStorageList: [],
        roleList: [],
        hostSelectData: [],
        architectureValue: '',
        architectureList: [],
        formData: {
            siteName: '',
            businessAreaName: '',
            clusterName: '',
            hddPaths: '',
            ssdPaths: '',
            remoteStorageId: '',
            role: 'dbscale-node',
            maxUnitCnt: 10,
            maxUsage: 100,
            description: ''
        },
        formRules: {
        	siteName: [
                {required: true, message: '请选择所属站点'}
            ],
            businessAreaName: [
                {required: true, message: '请选择所属业务区'}
            ],
            clusterName: [
                {required: true, message: '请选择所属集群'}
            ],
            selectStorage: [
                {validator: selectStorage}
            ],
            role: [
                {required: true, message: '请选择角色'}
            ],
            maxUnitCnt: [
                {required: true, message: '请输入单元上限'}
            ],
            maxUsage: [
                {required: true, message: '请输入资源分配率上限'}
            ]
        },
        statusShow: function (row) {
            if (row.enabled) {
                return getProjectSvg(COLOR_ENABLED)
            } else {
            	return getProjectSvg(COLOR_DISABLED)
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
            pageSize: LINEFEED_PAGE_SIZE,
            totalResult: 0,
            pageSizes: PAGE_SIZES,
            pageLayouts: PAGE_LAYOUTS
        },
        colorChange: function (value) {
            if (!value) {
                return "#333333"
            } else {
                return "red"
            }
        }
    },
    created: function () {
        this.siteListView()
        this.businessAreaListView()
        parent.layer.style(setupIndex, {
            width: '560px',
            height: '350px'
        });
    },
    methods: {
        dataDispose: function (data) {
            var jq_jsonData = []
            var _this = this
            XEUtils.arrayEach(data.data, (v, i) => {
                var json = JSON.parse(JSON.stringify(v))

                var oneNum = 9
                var twoNum = 9
                var threeNum = 6

                var resourceRates = [{
                    name: "CPU",
                    data: v.cpu,
                    value: "",
                    textColor: _this.colorChange(),
                    width: oneNum
                }, {
                    name: "内存",
                    data: v.mem,
                    value: "",
                    textColor: _this.colorChange(),
                    width: twoNum
                }, {
                    name: "存储",
                    data: v.storage,
                    value: "",
                    textColor: _this.colorChange(),
                    width: twoNum
                }];
                if (v.cpu !== null) {
                    resourceRates[0].value = usedCapacityDispose(v.cpu.used, v.cpu.capacity)
                    //resourceRates[0].textColor = _this.colorChange(v.cpu.upLimit)
                }
                if (v.mem !== null) {
                    resourceRates[1].value = usedCapacityDispose(v.mem.used, v.mem.capacity, " G")
                    //resourceRates[1].textColor = _this.colorChange(v.mem.upLimit)
                }
                if (v.storage !== null) {
                    resourceRates[2].value = usedCapacityDispose(v.storage.used, v.storage.capacity, " G")
                    //resourceRates[2].textColor = _this.colorChange(v.storage.upLimit)
                }

                var hostsInfo = [{
                    name: "架构",
                    data: v.architecture.display,
                    value: "",
                    textColor: _this.colorChange(),
                    width: oneNum
                }, {
                    name: "kubelet版本",
                    data: v.kubeletVersion,
                    value: "",
                    textColor: _this.colorChange(),
                    width: twoNum
                }, {
                    name: "kubeProxy版本",
                    data: v.kubeProxyVersion,
                    value: "",
                    textColor: _this.colorChange(),
                    width: threeNum
                }, {
                    name: "OS版本",
                    data: v.osImage,
                    value: "",
                    textColor: _this.colorChange(),
                    width: oneNum
                }, {
                    name: "容器版本",
                    data: v.containerRuntimeVersion,
                    value: "",
                    textColor: _this.colorChange(),
                    width: twoNum
                }, {
                    name: "操作系统",
                    data: v.operatingSystem,
                    value: "",
                    textColor: _this.colorChange(),
                    width: threeNum
                }, {
                    name: "kernel版本",
                    data: v.kernelVersion,
                    value: "",
                    textColor: _this.colorChange(),
                    width: oneNum
                }, {
                    name: "注册时间",
                    data: v.gmtCreated,
                    value: "",
                    textColor: _this.colorChange(),
                    width: twoNum
                }];

                json.resourceRates = resourceRates
                json.hostsInfo = hostsInfo
                json.status = false
                jq_jsonData.push(json)
            });
            this.tableDataAll = jq_jsonData
            this.searchData = this.tableDataAll.concat()
            this.sortData = this.searchData.concat()
            this.handlePageChange()
        },
        handleSizeCurrentChange: function (value) {
            commonHandleSizeCurrentChange(this, value)
        },
        handlePageChange: function () {
            commonHandlePageChange(this)
        },
        siteListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sites", function (response) {
                _this.siteList = response.data.data
                _this.formData.siteName = getSession("siteId")
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        businessAreaListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/business_areas?site_id=" + getSession("siteId") + "&enabled=true", function (response) {
                _this.businessAreaList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        clusterListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/clusters?business_area_id=" + _this.formData.businessAreaName + "&enabled=true", function (response) {
                _this.clusterList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        architectureListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=sys_architecture", function (response) {
                _this.architectureList = response.data.data
                _this.architectureValue = _this.architectureList[0].code
                _this.nodeSelectView()
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        remoteStorageListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/remote_storages?site_id=" + getSession("siteId") + "&enabled=true", function (response) {
                _this.remoteStorageList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        roleListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=host_role", function (response) {
                _this.roleList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        nodeSelectView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/nodes?site_id=" + getSession("siteId") + "&arch=" + _this.architectureValue + "&os=linux&role=dbscale-prepared", function (response) {
            	_this.dataDispose(response.data)
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        endFun: function () {
            this.initialization++
            if (this.initialization === this.endNum[this.stepsActive]) {
                layer.closeAll('loading')
                this.initialization = 0
            }
        },
        archChange: function () {
        	this.initialization = 1
    		this.nodeSelectView()
        },
        returnList: function () {
        	this.initialization = 1
        	this.nodeSelectView()
        },
        addAjaxFun: function (getCheckboxTablerId, successFun, falseFun) {
            var _this = this
            sendPost("/" + getProjectName() + "/hosts", function (response) {
            	successFun(response, getCheckboxTablerId)
            }, function (error) {
            	falseFun(error, getCheckboxTablerId)
            }, getCheckboxTablerId)
        },
        sortChange: function (column) {
            commonSortChange(this, column)
        },
        selectAllEvent: function ({ checked, records }) {
            //console.log(checked ? '所有勾选事件' : '所有取消事件', records)
        	this.hostSelectData = records 
        },
        selectChangeEvent: function ({ checked, records }) {
            //console.log(checked ? '勾选事件' : '取消事件', records)
        	this.hostSelectData = records 
        },
        searchClick: function () {
            let keyArray = ['ip', 'name', 'resourceRates<>.name', 'resourceRates<>.value','hostsInfo<>.name', 'hostsInfo<>.data']
            commonSearchClick(this, keyArray)
        },
        stepsActiveAdd: function (formName) {
        	var verify = false;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    return true;
                } else {
                    verify = true;
                    //console.log('error submit!!');
                    return false;
                }
            });
            if (verify) {
                return false;
            }
            if( this.stepsActive === 1 && this.hostSelectData.length === 0 ){
            	this.$message.closeAll()
            	operationCompletion(this, '请选择主机！', 'warning')
            	return false;
            }
            this.stepsActive++
            switch (this.stepsActive) {
	            case 1 :
	            	this.architectureListView()
	                parent.layer.style(setupIndex, {
	                    width: '1000px',
	                    height: '600px',
	                    left: '55vh'
	                })
	                break;
	                  
	            case 2 :
	            	this.$message.closeAll()
	            	this.remoteStorageListView()
	                this.roleListView()
	                if(getSession('storageMode') != 'pvc'){
	                	 parent.layer.style(setupIndex, {
	 	                    width: '710px',
	 	                    height: '400px'
	 	                })
	                }else{
	                	parent.layer.style(setupIndex, {
	 	                    width: '710px',
	 	                    height: '370px'
	 	                })
	                }
	               
	                break;
	        }
        },
        stepsActiveDel: function (formName) {
        	var _this = this
            this.stepsActive--
            switch (this.stepsActive) {
	            case 0 :
	            	parent.layer.style(setupIndex, {
	                    width: '560px',
	                    height: '350px'
	                })
	                break;
	                
	            case 1 :
	            	parent.layer.style(setupIndex, {
	                    width: '1000px',
	                    height: '600px',
	                    left: '150px'
	                })
	                setTimeout(function () {
		                XEUtils.arrayEach(_this.hostSelectData, (item, key) => {
		                	_this.$refs.xTable.setCheckboxRow(item, true)
		                })
	                }, 1);
	                break;
	        }
            this.$refs[formName].clearValidate();
        },
        clusterChange: function (data) {
        	this.initialization = 1 
            this.formData.clusterName = ""
            this.clusterListView()
        },
        formSubmit: function (formName) {//addModal
            var verify = false;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    return true;
                } else {
                    verify = true;
                    //console.log('error submit!!');
                    return false;
                }
            });
            if (verify) {
                return false;
            }
            var hddPaths = []
            var ssdPaths = []
            if (this.formData.hddPaths)
                hddPaths = this.formData.hddPaths.split(',')
            if (this.formData.ssdPaths)
                ssdPaths = this.formData.ssdPaths.split(',')
            var tempData = []
            XEUtils.arrayEach(this.hostSelectData, (item, key) => {
            	var jsonData = {
	                    clusterId: this.formData.clusterName,
	                    ip: item.ip,
	                    role: this.formData.role,
	                    maxUnitCnt: this.formData.maxUnitCnt,
	                    maxUsage: this.formData.maxUsage,
	                    description: this.formData.description
	                }
            	if(getSession('storageMode') != 'pvc'){
            		var volumePath = {
        				 hddPaths: hddPaths,
        				 ssdPaths: ssdPaths,
        				 remoteStorageId: this.formData.remoteStorageId,
            		}
            		jsonData.volumePath = volumePath
            	}
                tempData.push(jsonData)
            })
            if ( this.hostSelectData.length !== 1 ) {
            	array = true
            } else {
            	array = false
            }
            var _this = this
            commonConfirm(_this, '注册确认', getHintText('注册')).then(() => {
            	sendAll(_this.addAjaxFun, tempData, function (successArray, errorArray) {
                    if (errorArray.length === 0) {
                        operationCompletion(_this, "操作成功！")
                        parent.listApp.returnList()
                        _this.formClose()
                    } else {
                        operationCompletion(_this, errorMsg(errorArray, array), 'error')
                    }
                })
            }).catch({
            })
        },
        formClose: function () {
            parent.layer.close(setupIndex);
        }
    }
})