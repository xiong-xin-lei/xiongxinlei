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
    el: '#CMHA',
    data: {
        initialization: 0,
        endNum: 5,
        judgeNull: true,
        mysqlScalesList: [],
        proxysqlScalesList: [],
        cmhaScalesList: [],
        mysqlDiskTypeList: [],
        proxysqlDiskTypeList: [],
        cmhaDiskTypeList: [],
        editDisabledFlag: () => XEUtils.findIndexOf(otherBtnList, item => item.code === "btnUpdate") !== -1,
        formData: {
            mysqltype: "",
            mysqlcpuCnt: '',
            mysqlmemSize: '',
            mysqlscales: '',
            mysqldiskTypes: '',
            mysqlPort: 3306,
            mysqldataSize: '',
            mysqllogSize: '',
            mysqlclusterHA: true,
            mysqlhostHA: true,
            /* mysqlnetworkHA: false,
               mysqlstorageHA: false,*/

            proxysqltype: "",
            proxysqlcpuCnt: '',
            proxysqlmemSize: '',
            proxysqlscales: '',
            proxysqldiskTypes: '',
            proxysqlPort: 6033,
            proxysqldataSize: '',
            proxysqllogSize: '',
            proxysqlclusterHA: true,
            proxysqlhostHA: true,
            /*proxysqlnetworkHA: false,
              proxysqlstorageHA: false,*/

            cmhatype: "",
            cmhacpuCnt: '',
            cmhamemSize: '',
            cmhascales: '',
            cmhadiskTypes: '',
            cmhaPort: 9600,
            cmhadataSize: '',
            cmhalogSize: '',
            cmhaclusterHA: true,
            cmhahostHA: true,
            /*cmhanetworkHA: false,
              cmhastorageHA: false*/
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
            proxysqlscales: [
                {required: true, message: '请选择所属规模', trigger: 'change'}
            ],
            proxysqldiskTypes: [
                {required: true, message: '请选择磁盘类型', trigger: 'change'}
            ],
            proxysqlPort: [
                {required: true, message: '请输入端口', trigger: 'blur'}
            ],
            proxysqldataSize: [
                {required: true, message: '请输入数据目录', trigger: 'blur'},
                {validator: checkNum, trigger: 'blur'}
            ],
            proxysqllogSize: [
                {required: true, message: '请输入日志目录', trigger: 'blur'},
                {validator: checkNum, trigger: 'blur'}
            ],
            cmhascales: [
                {required: true, message: '请选择所属规模', trigger: 'change'}
            ],
            cmhadiskTypes: [
                {required: true, message: '请选择磁盘类型', trigger: 'change'}
            ],
            cmhaPort: [
                {required: true, message: '请输入端口', trigger: 'blur'}
            ],
            cmhadataSize: [
                {required: true, message: '请输入数据目录', trigger: 'blur'},
                {validator: checkNum, trigger: 'blur'}
            ],
            cmhalogSize: [
                {required: true, message: '请输入日志目录', trigger: 'blur'},
                {validator: checkNum, trigger: 'blur'}
            ]
        },
        mysqlFormData: {},
        proxysqlFormData: {},
        cmhaFormData: {},
    },
    created: function () {
        this.cfgsData()
        this.scalesData()
        this.diskTypeData()
    },
    methods: {
        cfgsData: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/orders/cfgs/cmha", function (response) {
                var data = response.data.data;
                if (data != null && data.length != 0) {
                    _this.judgeNull = false
                    _this.editCreated(data)
                }
                _this.endFun()
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
        },
        scalesData: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/scales?type=mysql&enabled=true", function (response) {
                _this.mysqlScalesList = response.data.data
                _this.endFun()
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
            sendGet("/" + getProjectName() + "/scales?type=proxysql&enabled=true", function (response) {
                _this.proxysqlScalesList = response.data.data
                _this.endFun()
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
            sendGet("/" + getProjectName() + "/scales?type=cmha&enabled=true", function (response) {
                _this.cmhaScalesList = response.data.data
                _this.endFun()
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
        },
        diskTypeData: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=disk_type", function (response) {
                _this.mysqlDiskTypeList = response.data.data
                _this.proxysqlDiskTypeList = response.data.data
                _this.cmhaDiskTypeList = response.data.data
                _this.endFun()
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
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
                if (data.type.code == "mysql") {
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
                } else if (data.type.code == "proxysql") {

                    if (data.scale != null) {
                        cpuCnts = data.scale.cpuCnt
                        memSizes = data.scale.memSize
                        scalesNames = data.scale.name
                    }

                    _this.proxysqlFormData = {
                        proxysqltype: data.type.display,
                        proxysqlcpuCnt: cpuCnts,
                        proxysqlmemSize: memSizes,
                        proxysqlclusterHA: data.clusterHA,
                        proxysqlhostHA: data.hostHA,
                        /* 
                         proxysqlnetworkHA: data.networkHA,
                         proxysqlstorageHA: data.storageHA,*/
                        proxysqlPort: data.port,
                        proxysqldataSize: data.dataSize,
                        proxysqllogSize: data.logSize,
                        proxysqlscales: scalesNames,
                        proxysqldiskTypes: data.diskType.code,
                    }
                } else if (data.type.code == "cmha") {

                    if (data.scale != null) {
                        cpuCnts = data.scale.cpuCnt
                        memSizes = data.scale.memSize
                        scalesNames = data.scale.name
                    }

                    _this.cmhaFormData = {
                        cmhatype: data.type.display,
                        cmhacpuCnt: cpuCnts,
                        cmhamemSize: memSizes,
                        cmhaclusterHA: data.clusterHA,
                        cmhahostHA: data.hostHA,
                        /* 
                         cmhanetworkHA: data.networkHA,
                         cmhastorageHA: data.storageHA,*/
                        cmhaPort: data.port,
                        cmhadataSize: data.dataSize,
                        cmhalogSize: data.logSize,
                        cmhascales: scalesNames,
                        cmhadiskTypes: data.diskType.code,
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
                /*  
                  mysqlnetworkHA: _this.mysqlFormData.mysqlnetworkHA,
                  mysqlstorageHA: _this.mysqlFormData.mysqlstorageHA,*/

                proxysqltype: _this.proxysqlFormData.proxysqltype,
                proxysqlcpuCnt: _this.proxysqlFormData.proxysqlcpuCnt,
                proxysqlmemSize: _this.proxysqlFormData.proxysqlmemSize,
                proxysqlscales: _this.proxysqlFormData.proxysqlscales,
                proxysqldiskTypes: _this.proxysqlFormData.proxysqldiskTypes,
                proxysqlPort: _this.proxysqlFormData.proxysqlPort,
                proxysqldataSize: _this.proxysqlFormData.proxysqldataSize,
                proxysqllogSize: _this.proxysqlFormData.proxysqllogSize,
                proxysqlclusterHA: _this.proxysqlFormData.proxysqlclusterHA,
                proxysqlhostHA: _this.proxysqlFormData.proxysqlhostHA,
                /*   
                   proxysqlnetworkHA: _this.proxysqlFormData.proxysqlnetworkHA,
                   proxysqlstorageHA: _this.proxysqlFormData.proxysqlstorageHA,*/

                cmhatype: _this.cmhaFormData.cmhatype,
                cmhacpuCnt: _this.cmhaFormData.cmhacpuCnt,
                cmhamemSize: _this.cmhaFormData.cmhamemSize,
                cmhascales: _this.cmhaFormData.cmhascales,
                cmhadiskTypes: _this.cmhaFormData.cmhadiskTypes,
                cmhaPort: _this.cmhaFormData.cmhaPort,
                cmhadataSize: _this.cmhaFormData.cmhadataSize,
                cmhalogSize: _this.cmhaFormData.cmhalogSize,
                cmhaclusterHA: _this.cmhaFormData.cmhaclusterHA,
                cmhahostHA: _this.cmhaFormData.cmhahostHA,
                /*  
                  cmhanetworkHA: _this.cmhaFormData.cmhanetworkHA,
                  cmhastorageHA: _this.cmhaFormData.cmhastorageHA*/
            }
        },
        clusterHAChange: function (){
        	if(this.formData.mysqlclusterHA)
        		this.formData.mysqlhostHA = true
    		if(this.formData.proxysqlclusterHA)
        		this.formData.proxysqlhostHA = true
    		if(this.formData.cmhaclusterHA)
        		this.formData.cmhahostHA = true
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
            /*  
              var mysqlnetworkHA = this.formData.mysqlnetworkHA
              var mysqlstorageHA = this.formData.mysqlstorageHA*/

            //Proxysql 代理
            var proxysqltype = this.formData.proxysqltype
            var proxysqlcpuCnt = this.formData.proxysqlcpuCnt
            var proxysqlmemSize = this.formData.proxysqlmemSize
            var proxysqlscales = this.formData.proxysqlscales
            var proxysqldiskTypes = this.formData.proxysqldiskTypes

            XEUtils.arrayEach(this.proxysqlScalesList, (v, i) => {
                if (proxysqlscales == v.name) {
                    proxysqlcpuCnt = v.cpuCnt
                    proxysqlmemSize = v.memSize
                    proxysqltype = v.type.code
                }
            })

            var proxysqlPort = this.formData.proxysqlPort
            var proxysqldataSize = this.formData.proxysqldataSize
            var proxysqllogSize = this.formData.proxysqllogSize
            var proxysqlclusterHA = this.formData.proxysqlclusterHA
            var proxysqlhostHA = this.formData.proxysqlhostHA
            /* 
             var proxysqlnetworkHA = this.formData.proxysqlnetworkHA
             var proxysqlstorageHA = this.formData.proxysqlstorageHA
 */
            //cmha 高可用
            var cmhatype = this.formData.cmhatype
            var cmhacpuCnt = this.formData.cmhacpuCnt
            var cmhamemSize = this.formData.cmhamemSize
            var cmhascales = this.formData.cmhascales
            var cmhadiskTypes = this.formData.cmhadiskTypes

            XEUtils.arrayEach(this.cmhaScalesList, (v, i) => {
                if (cmhascales == v.name) {
                    cmhacpuCnt = v.cpuCnt
                    cmhamemSize = v.memSize
                    cmhatype = v.type.code
                }
            })

            var cmhaPort = this.formData.cmhaPort
            var cmhadataSize = this.formData.cmhadataSize
            var cmhalogSize = this.formData.cmhalogSize
            var cmhaclusterHA = this.formData.cmhaclusterHA
            var cmhahostHA = this.formData.cmhahostHA
            /*  
              var cmhanetworkHA = this.formData.cmhanetworkHA
              var cmhastorageHA = this.formData.cmhastorageHA*/

            if(getSession('storageMode') != 'volumepath'){
            	mysqldiskTypes = 'local_hdd'
        		proxysqldiskTypes = 'local_hdd'
    			cmhadiskTypes = 'local_hdd'
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
                /* 
                 "networkHA": mysqlnetworkHA,
                 "storageHA": mysqlstorageHA*/
            }, {
                "type": proxysqltype,
                "cpuCnt": proxysqlcpuCnt,
                "memSize": proxysqlmemSize,
                "diskType": proxysqldiskTypes,
                "port": parseInt(proxysqlPort),
                "dataSize": parseInt(proxysqldataSize),
                "logSize": parseInt(proxysqllogSize),
                "clusterHA": proxysqlclusterHA,
                "hostHA": proxysqlhostHA,
                /* 
                 "networkHA": proxysqlnetworkHA,
                 "storageHA": proxysqlstorageHA*/
            }, {
                "type": cmhatype,
                "cpuCnt": cmhacpuCnt,
                "memSize": cmhamemSize,
                "diskType": cmhadiskTypes,
                "port": parseInt(cmhaPort),
                "dataSize": parseInt(cmhadataSize),
                "logSize": parseInt(cmhalogSize),
                "clusterHA": cmhaclusterHA,
                "hostHA": cmhahostHA,
                /* 
                 "networkHA": cmhanetworkHA,
                 "storageHA": cmhastorageHA*/
            }]
            var _this = this
            //console.log("新增",jsonData)
            if (_this.judgeNull) {
                commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
                    sendPost("/" + getProjectName() + "/orders/cfgs/cmha", function (response) {
                        layer.closeAll('loading')
                        operationCompletion( _this, "工单配置  CMHA 新增成功！", "success")
                    }, function (error) {
                    	operationCompletion( _this, error.response.data.msg, "error")
                    }, jsonData)
                })
            } else {
            	commonConfirm(_this, '编辑确认', getHintText('编辑')).then(() => {
                    sendPut("/" + getProjectName() + "/orders/cfgs/cmha", function (response) {
                        layer.closeAll('loading')
                        operationCompletion( _this, "工单配置  CMHA 编辑成功！", "success")
                    }, function (error) {
                    	operationCompletion( _this, error.response.data.msg, "error")
                    }, jsonData)
                })
            }

        }
    }
})