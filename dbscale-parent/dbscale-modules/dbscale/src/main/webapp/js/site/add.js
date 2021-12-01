var addIndex = parent.layer.getFrameIndex(window.name);
new Vue({
        el: '#add',
        data: {
        	regionList:[],
        	formData: {
                name: '',
                region: '',
                description: '',
                kubeconfig: ''
            },
            formRules: {
            	region: [
                    { required: true, message: '请选择地域' }
                ],
                name: [
                    { required: true, message: '请输入站点名称' }
                ],
                kubeconfig: [
   	               { required: true, message: '请输入证书' }
   	            ]
            }
        },
        created: function () {
        	this.regionListView()
        },
        methods: {
        	regionListView: function () {
        		var _this = this
        		sendGet("/" + getProjectName() + "/dicts?dict_type_code=region",function (response) {
        			_this.regionList = response.data.data
    				layer.closeAll('loading')
                  },function (error) {
                	  operationCompletion(_this, error.response.data.msg, "error")
                  },null)
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
                		"name": this.formData.name,
                		"region": this.formData.region,
                		"kubeconfig": this.formData.kubeconfig,
                		"description": this.formData.description
                }
                var _this = this
                commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
	            	sendPost("/" + getProjectName() + "/sites",function (response) {
	                    operationCompletion(parent.listApp,"操作成功！")
	            		parent.listApp.returnList()
	            		_this.formClose()
	                  },function (error) {
	                	  operationCompletion(_this, error.response.data.msg, "error")
	                  },jsonData)
				}).catch(() => {
				});
            },
            formClose: function() {
                parent.layer.close(addIndex)
            }
        }
    })