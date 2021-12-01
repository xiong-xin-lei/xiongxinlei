var addIndex = parent.layer.getFrameIndex(window.name);
new Vue({
        el: '#add',
        data: {
        	businessAreaList: [],
        	formData: {
        		businessAreaId: '',
				name: '',
				nfsIp: '',
				nfsSource: '',
				nfsOpts: '',
				enabled: true,
				description: ''
            },
            formRules: {
                name: [
                    { required: true, message: '请输入名称' }
                ],
                businessAreaId: [
                    { required: true, message: '请选择所属业务区' }
                ],
                nfsIp: [
                    { required: true, message: '请输入NFS地址' },
                    {
                        pattern: /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/,
                        message: '请输入正确的NFS地址',
                        trigger: 'blur'
                    }
                ],
                nfsSource: [
                    { required: true, message: '请输入源目录' }
                ]
            }
        },
        created: function () {
            this.businessAreaListView()
        },
        methods: {
        	businessAreaListView: function () {
        		var _this = this
        		sendGet("/" + getProjectName() + "/business_areas?site_id=" + getSession("siteId") + "&enabled=true",function (response) {
        			_this.businessAreaList = response.data.data,
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
						"businessAreaId": this.formData.businessAreaId,
						"name": this.formData.name,
						"nfsIp": this.formData.nfsIp,
						"nfsSource": this.formData.nfsSource,
						"nfsOpts": this.formData.nfsOpts,
						"enabled": this.formData.enabled,
						"description": this.formData.description
                }
                var _this = this
                commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
	            	sendPost("/" + getProjectName() + "/nfs",function (response) {
	                	layer.closeAll('loading'),
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