var remote_storage_id = getQueryVariable("remote_storage_id")
var addIndex = parent.layer.getFrameIndex(window.name);
new Vue({
        el: '#add',
        data: {
        	initialization:0,
        	endNum:2,
        	performanceList:[],
        	formData: {
        		remoteStorages: '',
                name: '',
                performance:'',
                enabled: true,
                description: ''
            },
            formRules: {
            	remoteStorages: [
                    { required: true, message: '请选择所属存储' }
                ],
                name: [
                    { required: true, message: '请输入存储池名称' }
                ],
                performance: [
   	               { required: true, message: '请选择性能等级' }
   	            ]
            }
        },
        created: function () {
            this.performanceListView()
            this.remoteStorageIdView()
        },
        methods: {
        	remoteStorageIdView: function () {
        		var _this = this
        		sendGet("/" + getProjectName() + "/remote_storages?site_id=" + getSession("siteId"),function (response) {
        			XEUtils.arrayEach(response.data.data, (v,i) => {
        				if(remote_storage_id == v.id){
        					_this.formData.remoteStorages = v.name
        				}
        			})
        			_this.endFun()
                  },function (error) {
                	  operationCompletion(_this, error.response.data.msg, 'error')
                  },null)
        	},
        	performanceListView: function () {
        		var _this = this
        		sendGet("/" + getProjectName() + "/dicts?dict_type_code=performance",function (response) {
        			_this.performanceList = response.data.data
        			_this.endFun()
                  },function (error) {
                	  operationCompletion(_this, error.response.data.msg, 'error')
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
                		"name": this.formData.name,
                		"performance": this.formData.performance,
                		"enabled": this.formData.enabled,
                		"description": this.formData.description
                }
                var _this = this
                //console.log("新增",jsonData)
                commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
	                sendPost("/" + getProjectName() + "/remote_storages/" + remote_storage_id + "/pools",function (response) {
	                	layer.closeAll('loading'),
	                    operationCompletion(parent.listApp,"操作成功！")
	            		 parent.listApp.returnList()
	            		_this.formClose()
	                  },function (error) {
	                	  operationCompletion(_this, error.response.data.msg, 'error')
	                  },jsonData)
                  }).catch({
                	  
                  })
            },
            formClose: function() {
                parent.layer.close(addIndex);
            }
        }
    })