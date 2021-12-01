var addIndex = parent.layer.getFrameIndex(window.name);
new Vue({
        el: '#add',
        data: {
        	initialization: 0,
        	endNum: 2,
        	siteList:[],
        	vendorList:[],
        	versionList:[],
        	typeList:[],
            saveData: {},
        	formData: {
        		siteId: '',
                name: '',
                vendor: '',
                version: '',
                type: '',
                ip: '',
                port: '',
                username: '',
                password: '',
                enabled: true,
                description: ''
            },
            formRules: {
                siteId: [
                    { required: true, message: '请选择所属站点' }
                ],
                name: [
                    { required: true, message: '请输入存储池名称' }
                ],
                vendor: [
                    { required: true, message: '请选择品牌' }
                ],
                version: [
                    { required: true, message: '请选择型号' }
                ],
                type: [
	                { required: true, message: '请选择类型' }
	            ],
                ip: [
                    { required: true, message: '请输入IP' },
			    	{ pattern: /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/, message: '请输入正确的IP地址', trigger: 'blur' }
                ],
            	port: [
                	{ required: true, message: '请输入端口' }
              	],
              	username: [
                	{ required: true, message: '请输入用户名' }
                ],
                password: [
                	{ required: true, message: '请输入密码' }
            	]
            }
        },
        created: function () {
        	this.saveData = parent.listApp.saveData;
        	this.formData.vendor = this.saveData.vendor[0].code;
        	this.formData.version = this.saveData.version[0].code;
            this.siteListView();
            this.typeListView();
        },
        methods: {
        	siteListView: function () {
        		var _this = this
        		sendGet("/" + getProjectName() + "/sites",function (response) {
        			_this.siteList = response.data.data,
        			_this.formData.siteId = getSession("siteId");
        			_this.endFun()
                  },function (error) {
                	  operationCompletion(_this, error.response.data.msg, "error")
                  },null)
        	},
        	typeListView: function () {
        		var _this = this
        		sendGet("/" + getProjectName() + "/dicts?dict_type_code=remote_storage_type",function (response) {
        			_this.typeList = response.data.data,
        			_this.formData.type = _this.typeList[0].code;
        			_this.endFun()
                  },function (error) {
                	  operationCompletion(_this, error.response.data.msg, "error")
                  },null)
        	},
        	endFun: function () {
        		this.initialization++
    			if(this.initialization == this.endNum)
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
        		if(verify){
        			return false;
        		}
                var jsonData = {
                		"siteId": this.formData.siteId,
                        "name": this.formData.name,
                        "vendor": this.formData.vendor,
                        "version": this.formData.version,
                        "type": this.formData.type,
                        "ip": this.formData.ip,
                        "port": this.formData.port,
                        "username": this.formData.username,
                        "password": this.formData.password,
                        "enabled": this.formData.enabled,
                        "description": this.formData.description
                }
                var _this = this
                commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
                	 sendPost("/" + getProjectName() + "/remote_storages",function (response) {
	                    operationCompletion(parent.listApp,"操作成功！")
	            		parent.listApp.returnList()
	            		_this.formClose()
	                  },function (error) {
	                	  operationCompletion(_this, error.response.data.msg, "error")
	                  },jsonData)
				}).catch(() => {
				});
            },
            formClose: function () {
                parent.layer.close(addIndex);
            }
        }
    })