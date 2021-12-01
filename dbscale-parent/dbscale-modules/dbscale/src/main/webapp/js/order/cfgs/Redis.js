var checkMysqlNum = (rule, value, callback) => {
    value = XEUtils.toString(value)
    if (value.split("")[0] === "0") {
        callback(new Error('格式不正确'));
        return false;
    }
    value = XEUtils.toNumber(value)
    if (value < 10) {
        callback(new Error('最小值为 10 '));
        return false;
    }
    callback();
};
var checkNum = (rule, value, callback) => {
    value = XEUtils.toString(value)
    if (value.split("")[0] === "0") {
        callback(new Error('格式不正确'));
        return false;
    }
    value = XEUtils.toNumber(value)
    if (value < 5) {
        callback(new Error('最小值为 5 '));
        return false;
    }
    callback();
};
new Vue({
    el: '#redis',
    data: {
        initialization: 0,
        endNum: 4,
        judgeNull: true,
        mysqlScalesList: [],
        sentinelScalesList: [],
        mysqlDiskTypeList: [],
        sentinelDiskTypeList: [],
        editDisabledFlag: () => XEUtils.findIndexOf(otherBtnList, item => item.code === "btnUpdate") !== -1,
        formData: {
            mysqltype: "",
            mysqlcpuCnt: '',
            mysqlmemSize: '',
            mysqlscales: '',
            mysqldiskTypes: '',
            mysqlPort: 6379,
            mysqldataSize: '',
            mysqllogSize: '',
            mysqlclusterHA: true,
            mysqlhostHA: true,

            sentineltype: "",
            sentinelcpuCnt: '',
            sentinelmemSize: '',
            sentinelscales: '',
            sentineldiskTypes: '',
            sentinelPort: 26379,
            sentineldataSize: '',
            sentinellogSize: '',
            sentinelclusterHA: false,
            sentinelhostHA: false,
        },
        formRules: {
            mysqlscales: [
                {required: true, message: '请选择所属规模', trigger: 'change'}
            ],
            mysqldiskTypes: [
                {required: true, message: '请选择磁盘类型', trigger: 'change'}
            ],
            mysqlPort: [
                {required: true, message: '请输入端口', trigger: 'blur'}
            ],
            mysqldataSize: [
                {required: true, message: '请输入表空间', trigger: 'blur'},
                {validator: checkMysqlNum, trigger: 'blur'}
            ],
            mysqllogSize: [
                {required: true, message: '请输入日志空间', trigger: 'blur'},
                {validator: checkMysqlNum, trigger: 'blur'}
            ],
            sentinelscales: [
                {required: true, message: '请选择所属规模', trigger: 'change'}
            ],
            sentineldiskTypes: [
                {required: true, message: '请选择磁盘类型', trigger: 'change'}
            ],
            sentinelPort: [
                {required: true, message: '请输入端口', trigger: 'blur'}
            ],
            sentineldataSize: [
                {required: true, message: '请输入数据目录', trigger: 'blur'},
                {validator: checkNum, trigger: 'blur'}
            ],
            sentinellogSize: [
                {required: true, message: '请输入日志目录', trigger: 'blur'},
                {validator: checkNum, trigger: 'blur'}
            ]
        },
        mysqlFormData: {},
        sentinelFormData: {},
    },
    created: function () {
        this.cfgsData()
        this.scalesData()
        this.diskTypeData()
    },
    methods: {
        cfgsData: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/orders/cfgs/redis", function (response) {
                var data = response.data.data;
                if (data != null && data.length != 0) {
                    _this.judgeNull = false
                    _this.editCreated(data)
                }
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, "error")
            }, null)
        },
        scalesData: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/scales?type=redis&enabled=true", function (response) {
                _this.mysqlScalesList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, "error")
            }, null)
            sendGet("/" + getProjectName() + "/scales?type=redis-sentinel&enabled=true", function (response) {
                _this.sentinelScalesList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, "error")
            }, null)
        },
        diskTypeData: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=disk_type", function (response) {
                _this.mysqlDiskTypeList = response.data.data
                _this.sentinelDiskTypeList = response.data.data
                _this.endFun()
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, "error")
            }, null)
        },
        endFun: function () {
            this.initialization++
            if (this.initialization == this.endNum)
                layer.closeAll('loading')
        },
        editCreated: function (data) {
            var _this = this
            XEUtils.arrayEach(data, (data, index) => {
                var cpuCnts = ""
                var memSizes = ""
                var scalesNames = ""
                if (data.type.code == "redis") {
                    if (data.scale != null) {
                        cpuCnts = data.scale.cpuCnt
                        memSizes = data.scale.memSize
                        scalesNames = data.scale.name
                    }

                    _this.mysqlFormData = {
                        mysqltype: data.type.display,
                        mysqlcpuCnt: cpuCnts,
                        mysqlmemSize: memSizes,
                        mysqlclusterHA: data.clusterHA,
                        mysqlhostHA: data.hostHA,
                        /* 
                          mysqlnetworkHA: data.networkHA,
                          mysqlstorageHA: data.storageHA,*/
                        mysqlPort: data.port,
                        mysqldataSize: data.dataSize,
                        mysqllogSize: data.logSize,
                        mysqlscales: scalesNames,
                        mysqldiskTypes: data.diskType.code,
                    }
                } else if (data.type.code == "redis-sentinel") {

                    if (data.scale != null) {
                        cpuCnts = data.scale.cpuCnt
                        memSizes = data.scale.memSize
                        scalesNames = data.scale.name
                    }

                    _this.sentinelFormData = {
                        sentineltype: data.type.display,
                        sentinelcpuCnt: cpuCnts,
                        sentinelmemSize: memSizes,
                        sentinelclusterHA: data.clusterHA,
                        sentinelhostHA: data.hostHA,
                        /* 
                         sentinelnetworkHA: data.networkHA,
                         sentinelstorageHA: data.storageHA,*/
                        sentinelPort: data.port,
                        sentineldataSize: data.dataSize,
                        sentinellogSize: data.logSize,
                        sentinelscales: scalesNames,
                        sentineldiskTypes: data.diskType.code,
                    }
                }
            })
            _this.formData = {
                mysqltype: _this.mysqlFormData.mysqltype,
                mysqlcpuCnt: _this.mysqlFormData.mysqlcpuCnt,
                mysqlmemSize: _this.mysqlFormData.mysqlmemSize,
                mysqlscales: _this.mysqlFormData.mysqlscales,
                mysqldiskTypes: _this.mysqlFormData.mysqldiskTypes,
                mysqlPort: _this.mysqlFormData.mysqlPort,
                mysqldataSize: _this.mysqlFormData.mysqldataSize,
                mysqllogSize: _this.mysqlFormData.mysqllogSize,
                mysqlclusterHA: _this.mysqlFormData.mysqlclusterHA,
                mysqlhostHA: _this.mysqlFormData.mysqlhostHA,

                sentineltype: _this.sentinelFormData.sentineltype,
                sentinelcpuCnt: _this.sentinelFormData.sentinelcpuCnt,
                sentinelmemSize: _this.sentinelFormData.sentinelmemSize,
                sentinelscales: _this.sentinelFormData.sentinelscales,
                sentineldiskTypes: _this.sentinelFormData.sentineldiskTypes,
                sentinelPort: _this.sentinelFormData.sentinelPort,
                sentineldataSize: _this.sentinelFormData.sentineldataSize,
                sentinellogSize: _this.sentinelFormData.sentinellogSize,
                sentinelclusterHA: _this.sentinelFormData.sentinelclusterHA,
                sentinelhostHA: _this.sentinelFormData.sentinelhostHA,
            }
        },
        clusterHAChange: function (){
        	if(this.formData.mysqlclusterHA)
        		this.formData.mysqlhostHA = true
    		if(this.formData.sentinelclusterHA)
        		this.formData.sentinelhostHA = true
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
            //mysql 数据库
            var mysqltype = this.formData.mysqltype
            var mysqlcpuCnt = this.formData.mysqlcpuCnt
            var mysqlmemSize = this.formData.mysqlmemSize
            var mysqlscales = this.formData.mysqlscales
            var mysqldiskTypes = this.formData.mysqldiskTypes

            XEUtils.arrayEach(this.mysqlScalesList, (v, i) => {
                if (mysqlscales == v.name) {
                    mysqlcpuCnt = v.cpuCnt
                    mysqlmemSize = v.memSize
                    mysqltype = v.type.code
                }
            })

            var mysqlPort = this.formData.mysqlPort
            var mysqldataSize = this.formData.mysqldataSize
            var mysqllogSize = this.formData.mysqllogSize
            var mysqlclusterHA = this.formData.mysqlclusterHA
            var mysqlhostHA = this.formData.mysqlhostHA

            //sentinel 高可用
            var sentineltype = this.formData.sentineltype
            var sentinelcpuCnt = this.formData.sentinelcpuCnt
            var sentinelmemSize = this.formData.sentinelmemSize
            var sentinelscales = this.formData.sentinelscales
            var sentineldiskTypes = this.formData.sentineldiskTypes

            XEUtils.arrayEach(this.sentinelScalesList, (v, i) => {
                if (sentinelscales == v.name) {
                    sentinelcpuCnt = v.cpuCnt
                    sentinelmemSize = v.memSize
                    sentineltype = v.type.code
                }
            })

            var sentinelPort = this.formData.sentinelPort
            var sentineldataSize = this.formData.sentineldataSize
            var sentinellogSize = this.formData.sentinellogSize
            var sentinelclusterHA = this.formData.sentinelclusterHA
            var sentinelhostHA = this.formData.sentinelhostHA
            
            if(getSession('storageMode') != 'volumepath'){
            	mysqldiskTypes = 'local_hdd'
        		sentineldiskTypes = 'local_hdd'
            }

            var jsonData = [{
                "type": mysqltype,
                "cpuCnt": mysqlcpuCnt,
                "memSize": mysqlmemSize,
                "diskType": mysqldiskTypes,
                "port": parseInt(mysqlPort),
                "dataSize": parseInt(mysqldataSize),
                "logSize": parseInt(mysqllogSize),
                "clusterHA": mysqlclusterHA,
                "hostHA": mysqlhostHA,
            }, {
                "type": sentineltype,
                "cpuCnt": sentinelcpuCnt,
                "memSize": sentinelmemSize,
                "diskType": sentineldiskTypes,
                "port": parseInt(sentinelPort),
                "dataSize": parseInt(sentineldataSize),
                "logSize": parseInt(sentinellogSize),
                "clusterHA": sentinelclusterHA,
                "hostHA": sentinelhostHA,
            }]
            var _this = this
            //console.log("新增",jsonData)
            if (_this.judgeNull) {
                commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
                    sendPost("/" + getProjectName() + "/orders/cfgs/redis", function (response) {
                        layer.closeAll('loading')
                        _this.judgeNull = false
                        operationCompletion(_this, "工单配置  Redis 新增成功！", "success")
                    }, function (error) {
                        operationCompletion(_this, error.response.data.msg, "error")
                    }, jsonData)
                })
            } else {
                commonConfirm(_this, '编辑确认', getHintText('编辑')).then(() => {
                    sendPut("/" + getProjectName() + "/orders/cfgs/redis", function (response) {
                        layer.closeAll('loading')
                        operationCompletion(_this, "工单配置  Redis 编辑成功！", "success")
                    }, function (error) {
                        operationCompletion(_this, error.response.data.msg, "error")
                    }, jsonData)
                })
            }

        }
    }
})