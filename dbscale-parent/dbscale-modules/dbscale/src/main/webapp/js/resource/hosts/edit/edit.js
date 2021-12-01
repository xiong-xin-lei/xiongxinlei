var editIndex = parent.layer.getFrameIndex(window.name);
new Vue({
    el: '#edit',
    data: {
        initialization: 0,
        endNum: 4,
        siteList: [],
        businessAreaList: [],
        clusterList: [],
        remoteStorageList: [],
        roleList: [],
        formData: {
            siteId: '',
            businessAreaName: '',
            clusterId: '',
            ip: '',
            hddPaths: '',
            ssdPaths: '',
            maxUsage: '',
            remoteStorageId: '',
            role: '',
            description: ''
        },
        formRules: {
            ip: [
            	{required: true, message: '请输入主机IP'},
                {
                    pattern: /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/,
                    message: '请输入正确的IP地址',
                    trigger: 'blur'
                }
            ],
            clusterId: [
                {required: true, message: '请选择所属集群'}
            ],
            role: [
                {required: true, message: '请选择角色'}
            ],
            maxUsage: [
                {required: true, message: '请输入资源分配率上限'}
            ]
        }
    },
    created: function () {
        this.editCreated()
    },
    methods: {
        siteListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sites", function (response) {
                _this.siteList = response.data.data
                _this.formData.siteId = getSession("siteId")
                _this.initialization++
                _this.endFun()
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        businessAreaListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/business_areas?site_id=" + getSession("siteId") + "&enabled=true", function (response) {
                var tempArray = _this.businessAreaList.concat()
                if (XEUtils.some(response.data.data, item => item.id === tempArray[0].id)) {
                    _this.businessAreaList = response.data.data
                } else {
                    _this.businessAreaList = XEUtils.union(response.data.data, tempArray)
                }
                _this.endFun()
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        clusterListView: function (flag) {
            var _this = this
            sendGet("/" + getProjectName() + "/clusters?business_area_id=" + _this.formData.businessAreaName + "&enabled=true", function (response) {
            	var tempArray = _this.clusterList.concat()
                if (!flag) {
                    _this.clusterList = response.data.data
                } else {
                    XEUtils.arrayEach(response.data.data, (item, key) => {
                    	if(item.id != tempArray[0].id){
                    		_this.clusterList.push(item)
                    	}
                    })
                    //_this.clusterList = XEUtils.merge(response.data.data, tempArray)
                }
                _this.initialization++
                _this.endFun()
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        clusterView: function (clustIerd) {
            var _this = this
            sendGet("/" + getProjectName() + "/clusters/" + clustIerd, function (response) {
                _this.clusterList.push(jsonJudgeNotDefined(response, 'response.data.data'))
                _this.clusterListView(true)
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        remoteStorageListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/remote_storages?site_id=" + getSession("siteId") + "&enabled=true", function (response) {
                _this.remoteStorageList = response.data.data
                _this.initialization++
                _this.endFun()
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        roleListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=host_role", function (response) {
                _this.roleList = response.data.data
                _this.initialization++;
                if (_this.initialization == _this.endNum)
                    layer.closeAll('loading')
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        endFun: function () {
            if (this.initialization == this.endNum)
                layer.closeAll('loading')
        },
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/hosts/" + hostId, function (response) {
                var data = response.data.data
                var list = {
                    id: jsonJudgeNotDefined(data, 'data.businessArea.id'),
                    name: jsonJudgeNotDefined(data, 'data.businessArea.name')
                }
                _this.businessAreaList.push(list)

                _this.clusterView(jsonJudgeNotDefined(data, 'data.cluster.id'))

                var hddPaths = ''
                var ssdPaths = ''
                var remoteStorageId = ''
                var maxStorageUsage = 10
                if (data.hdd != null) {
                    hddPaths = arrayMerger(data.hdd.paths)
                    maxStorageUsage = parseInt(data.hdd.maxUsage)
                }
                if (data.ssd != null) {
                    ssdPaths = arrayMerger(data.ssd.paths)
                    maxStorageUsage = parseInt(data.ssd.maxUsage)
                }
                if (data.remoteStorage != null) {
                    remoteStorageId = jsonJudgeNotDefined(data, 'data.remoteStorage.id')
                    list = {
                        id: remoteStorageId,
                        name: jsonJudgeNotDefined(data, 'data.remoteStorage.name')
                    }
                    _this.remoteStorageList.push(list)
                    // remoteStorageName = data.remoteStorage.name
                    maxStorageUsage = parseInt(data.remoteStorage.maxUsage)
                }
                _this.formData = {
                    siteId: data.site.id,
                    businessAreaName: data.businessArea.id,
                    clusterId: data.cluster.id,
                    ip: data.ip,
                    hddPaths: hddPaths,
                    ssdPaths: ssdPaths,
                    maxStorageUsage: maxStorageUsage,
                    remoteStorageId: remoteStorageId,
                    maxUsage: data.maxUsage,
                    role: jsonJudgeNotDefined(data, "data.role.code"),
                    description: data.description
                }

                _this.siteListView()
                _this.businessAreaListView()
                _this.remoteStorageListView()
                _this.roleListView()
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        formSubmit: function (formName) {//editModal
            var verify = false
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

            var jsonData = {
                clusterId: this.formData.clusterId,
                ip: this.formData.ip,
                maxUsage: this.formData.maxUsage,
                role: this.formData.role,
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
            var _this = this
            commonConfirm(_this, '编辑确认', getHintText('编辑')).then(() => {
                sendPut("/" + getProjectName() + "/hosts/" + hostId, function (response) {
                    layer.closeAll('loading')
                    operationCompletion(parent.listApp, "操作成功！")
                    parent.listApp.refreshSaveData.push(parent.listApp.selectedData)
                    parent.listApp.returnList()
                    _this.formClose()
                }, function (error) {
                	operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            }).catch(() => {
            });
        },
        formClose: function () {
            parent.layer.close(editIndex);
        },
        clusterIdChange: function (data) {
            this.initialization--
            this.formData.clusterId = ""
            this.clusterListView()
        }
    }
})