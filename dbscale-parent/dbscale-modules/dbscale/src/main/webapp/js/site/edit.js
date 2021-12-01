var editIndex = parent.layer.getFrameIndex(window.name);
var siteId = getQueryVariable("siteId")
new Vue({
        el: '#edit',
        data: {
        	siteList:[],
        	formData: {
        		siteId: '',
                name: ''
            },
            formRules: {
            	 name: [
                     { required: true, message: '请输入站点名称' }
                 ]
            }
        },
        created: function () {
            this.editCreated()
        },
        methods: {
        	editCreated: function () {
        		var _this = this
        		sendGet("/" + getProjectName() + "/sites/" + siteId,function (response) {
        			var data = response.data.data
        			_this.formData.name = data.name
                    layer.closeAll('loading')
                  },function (error) {
                	  operationCompletion(_this, error.response.data.msg, "error")
                  },null)
        	},
        	formSubmit: function (formName) {//editModal
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
                }
                var _this = this
                parent.layer.min(editIndex);
                commonConfirm(parent.listApp, '编辑确认', getHintText('编辑')).then(() => {
                	parent.layer.restore(editIndex);
	                sendPut("/" + getProjectName() + "/sites/" + siteId,function (response) {
	                	operationCompletion(parent.listApp,"操作成功！")
	                	parent.listApp.returnList()
	            		_this.formClose()
	                  },function (error) {
	                	  operationCompletion(_this, error.response.data.msg, "error")
	                  },jsonData)
                  }).catch(() => {
                	  parent.layer.restore(editIndex);
                  });
            },
            formClose: function () {
            	if(parent.listApp.updateStatus)
            		parent.listApp.returnList()
                parent.layer.close(editIndex)
            }
        }
    })