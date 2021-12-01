var addIndex = parent.layer.getFrameIndex(window.name);
new Vue({
        el: '#add',
        data: {
        	siteList:[],
        	formData: {
        		siteId: '',
                name: '',
                enabled: true,
                description: ''
            },
            formRules: {
                siteId: [
                    { required: true, message: '请选择所属站点' }
                ],
                name: [
                    { required: true, message: '请输入业务区名称' }
                ],
	            enabled: [
   	               { required: true, message: '请选择状态' }
   	            ]
            }
        },
        created: function () {
            this.siteListView()
        },
        methods: {
        	siteListView: function () {
        		var _this = this
        		sendGet("/" + getProjectName() + "/sites",function (response) {
        			_this.siteList = response.data.data
        			_this.formData.siteId = getSession("siteId")
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
                		"siteId": this.formData.siteId,
                		"name": this.formData.name,
                		"enabled": this.formData.enabled,
                		"description": this.formData.description
                }
                var _this = this
                commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
	            	sendPost("/" + getProjectName() + "/business_areas",function (response) {
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