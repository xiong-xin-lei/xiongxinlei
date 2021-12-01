var addIndex = parent.layer.getFrameIndex(window.name)
var checkNum = (rule, value, callback) => {
	value = XEUtils.toString(value)
	if(value.split("")[0] === "0"){
		callback(new Error('格式不正确(开头数字不能为0)'));
    	return false;
	}
	value = XEUtils.toNumber(value)
    if(value<16||value>32){
    	callback(new Error('输入范围为 16-32'));
    	return false;
    }
    callback();
};
new Vue({
    el: '#add',
    data: {
        initialization: 0,
        endNum: 3,
        siteList: [],
        topologyList: [],
        businessAreaList: [],
        formData: {
            siteId: '',
            name: '',
            startIp: '',
            endIp: '',
            topology: [],
            businessAreaId: '',
            gateway: '',
            netmask: '',
            enabled: true,
            vlan: '',
            description: ''
        },
        formRules: {
            siteId: [
                { required: true, message: '请选择所属站点' }
            ],
            name: [
                { required: true, message: '请输入网段名称' }
            ],
            startIp: [
                { required: true, message: '请输入起始IP' },
                { pattern: /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/, message: '请输入正确的IP地址', trigger: 'blur' }

            ],
            endIp: [
                { required: true, message: '请输入结束IP' },
                { pattern: /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/, message: '请输入正确的IP地址', trigger: 'blur' }
            ],
            gateway: [
                { required: true, message: '请输入网关' },
                { pattern: /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/, message: '请输入正确的网关地址', trigger: 'blur' }
            ],
            vlan: [
                { required: true, message: '请输入VLAN ID' }
            ],
            netmask: [
                { required: true, message: '请输入掩码' },
                { validator: checkNum, trigger: 'blur'} 
            ],
            topology: [
                { required: true, message: '请选择拓扑结构' }
            ],
            businessAreaId: [
                {required: true, message: '请选择所属业务区'}
            ],
            enabled: [
                { required: true, message: '请选择状态' }
            ]
        }
    },
    created: function () {
        this.siteListView()
        this.businessAreaListView()
        this.topologyListView()
    },
    methods: {
        siteListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/sites", function (response) {
                _this.siteList = response.data.data
                _this.formData.siteId = getSession("siteId")
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
        topologyListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=network_topology", function (response) {
                _this.topologyList = response.data.data
                _this.endFun() 
            }, function (error) {
            	operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        endFun: function () {
        	this.initialization++
            if (this.initialization == this.endNum) 
                layer.closeAll('loading')
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

            var jsonData = {
                "name": this.formData.name,
                "startIp": this.formData.startIp,
                "endIp": this.formData.endIp,
                "gateway": this.formData.gateway,
                "netmask": this.formData.netmask,
                "vlan": this.formData.vlan,
                "topologys": this.formData.topology,
                "businessAreaId": this.formData.businessAreaId,
                "enabled": this.formData.enabled,
                "description": this.formData.description
            }
            var _this = this
            commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
	            sendPost("/" + getProjectName() + "/networks", function (response) {
	                layer.closeAll('loading')
	            	operationCompletion(parent.listApp,"操作成功！")
	                parent.listApp.returnList()
	                _this.formClose()
	            }, function (error) {
	            	operationCompletion(_this, error.response.data.msg, 'error')
	            }, jsonData)
            }).catch({
          	  
            })
        },
        formClose: function () {
            parent.layer.close(addIndex);
        }
    }
})